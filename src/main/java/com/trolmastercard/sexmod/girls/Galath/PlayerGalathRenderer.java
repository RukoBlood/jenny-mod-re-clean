/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.Nullable;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import com.trolmastercard.sexmod.util.Vector3fSexmodSpecial;
import com.trolmastercard.sexmod.util.anim.BoneDeformProcessor;
import com.trolmastercard.sexmod.util.interfaces.IGalath;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerGalathRenderer
extends PlayerGirlRenderer {
    final static HashSet<String> z = new HashSet<String>(Arrays.asList("kneeL", "kneeR", "shinL", "shinR", "armorHelmet", "sockL", "sockR", "braBoobL", "braBoobR", "armorNippleR", "armorNippleL", "slip", "turnable", "static"));

    public PlayerGalathRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Nullable
    protected Vector3fSexmodSpecial getAdditionalOverlayColor(GirlEntity entity) {
        if (entity.world instanceof FakeWorld) {
            return null;
        }
        if (((IGalath)((Object) entity)).isWingsAnimated()) {
            return null;
        }
        return GalathRenderer.OVERLAY_COLOR_NONE;
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        HashSet<String> hashSet = GalathRenderer.BLACKLISTED_BONES;
        GalathRenderer.BLACKLISTED_BONES.addAll(BoneDeformProcessor.EXCLUDED_MESH_BONES);
        return GalathRenderer.BLACKLISTED_BONES;
    }

    @Override
    protected void drawOverlayLines(Tessellator tessellator, BufferBuilder buffer, GirlEntity girl, Vector3fSexmodSpecial rgb, float thickness) {
        PlayerGalathRenderer.drawCustomOverlayBundle(tessellator, buffer, girl, rgb, thickness);
    }

    @Override
    public void doRender(GirlEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (PlayerGalathRenderer.mc.gameSettings.thirdPersonView == 0 && PlayerGalathRenderer.mc.player.getPersistentID().equals(((PlayerGirl) entity).getOwnerUserUUID()) && !entity.isAnchored()) {
            return;
        }
        GalathRenderer.renderCustomEffects(entity, partialTicks);
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        super.applyBowRotation(isLeftHand);
        if (isLeftHand) {
            GlStateManager.translate(0.15, 0.0, 0.0);
        }
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (isLeftHand) {
            GlStateManager.translate(0.0, -0.05, -0.05);
            GlStateManager.rotate(15.0f, 1.0f, 0.0f, 0.0f);
            if (isActive) {
                GlStateManager.translate(0.3, 0.2, 0.0);
                GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(15.0f, 0.0f, 0.0f, 1.0f);
            }
        } else {
            GlStateManager.translate(0.0, 0.0, 0.1);
            GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
            if (isActive) {
                GlStateManager.rotate(-29.0f, 1.0f, 0.0f, 0.0f);
            }
        }
    }

    @Override
    protected Vector4f calculateBoneArmorColor(String boneName, float r, float g, float b) {
        if (!z.contains(boneName)) {
            return this.getBaseColorVector(r, g, b);
        }
        if ("armorHelmet".equals(boneName)) {
            return super.calculateBoneArmorColor(boneName, r, g, b);
        }
        ItemStack itemStack = ItemStack.EMPTY;
        switch (boneName) {
            case "braBoobL": 
            case "braBoobR": 
            case "armorNippleR": 
            case "armorNippleL": {
                itemStack = this.renderEntity.getDataManager().get(Fighter.CHEST_SLOT);
                break;
            }
            case "turnable": 
            case "static": 
            case "slip": {
                itemStack = this.renderEntity.getDataManager().get(Fighter.LEGS_SLOT);
                break;
            }
            case "shinL": 
            case "shinR": 
            case "sockL": 
            case "sockR": 
            case "kneeL": 
            case "kneeR": {
                itemStack = this.renderEntity.getDataManager().get(Fighter.BOOTS_SLOT);
            }
        }
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return this.getBaseColorVector(r, g, b);
        }
        Object object = (ItemArmor)itemStack.getItem();
        switch (((ItemArmor)object).getArmorMaterial()) {
            default: {
                return new Vector4f(r, g, b, -0.1875f);
            }
            case GOLD: {
                return new Vector4f(r, g, b, -0.15625f);
            }
            case IRON: 
            case CHAIN: {
                return new Vector4f(r, g, b, -0.125f);
            }
            case LEATHER: 
        }
        int n = ((ItemArmor)object).getColor(itemStack);
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        return new Vector4f(r *= f4, g *= f5, b *= f6, -0.09375f);
    }

    protected void processModelSkeleton(GeoModel model, BufferBuilder buffer, GirlEntity entity, float r, float g, float b, float a, float partialTicks) {
        GeoBone geoBone = model.topLevelBones.get(0);
        GeoBone geoBone2 = null;
        GeoBone geoBone3 = null;
        for (GeoBone child : geoBone.childBones) {
            switch (child.getName()) {
                case "steve": {
                    geoBone3 = child;
                    break;
                }
                case "body": {
                    geoBone2 = child;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.renderRecursively(buffer, geoBone2, r, g, b, a);
        Tessellator.getInstance().draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getOrCreateDynamicSkin(this.renderEntity));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.renderRecursively(buffer, geoBone3, r, g, b, this.renderEntity.getRenderScaleFactor());
        Tessellator.getInstance().draw();
        MATRIX_STACK.pop();
    }
}

