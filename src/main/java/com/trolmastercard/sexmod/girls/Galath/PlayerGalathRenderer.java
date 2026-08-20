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

public class PlayerGalathRenderer extends PlayerGirlRenderer {
    final static HashSet<String> BLACKLISTED_BONES = new HashSet<String>(Arrays.asList("kneeL", "kneeR", "shinL", "shinR", "armorHelmet", "sockL", "sockR", "braBoobL", "braBoobR", "armorNippleR", "armorNippleL", "slip", "turnable", "static"));

    public PlayerGalathRenderer(RenderManager manager, AnimatedGeoModel model) {
        super(manager, model);
    }

    @Nullable
    protected Vector3fSexmodSpecial getAdditionalOverlayColor(GirlEntity entity) {
        if (entity.world instanceof FakeWorld) {
            return null;
        }
        if (((IGalath) entity).isWingsAnimated()) {
            return null;
        }
        return GalathRenderer.OVERLAY_COLOR_NONE;
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        //HashSet<String> hashSet = GalathRenderer.BLACKLISTED_BONES; this hashet never used
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
        if (!BLACKLISTED_BONES.contains(boneName)) {
            return this.getBaseColorVector(r, g, b);
        }
        if ("armorHelmet".equals(boneName)) {
            return super.calculateBoneArmorColor(boneName, r, g, b);
        }
        ItemStack stack = ItemStack.EMPTY;
        switch (boneName) {
            case "braBoobL": 
            case "braBoobR": 
            case "armorNippleR": 
            case "armorNippleL": {
                stack = this.renderEntity.getDataManager().get(Fighter.CHEST_SLOT);
                break;
            }
            case "turnable": 
            case "static": 
            case "slip": {
                stack = this.renderEntity.getDataManager().get(Fighter.LEGS_SLOT);
                break;
            }
            case "shinL": 
            case "shinR": 
            case "sockL": 
            case "sockR": 
            case "kneeL": 
            case "kneeR": {
                stack = this.renderEntity.getDataManager().get(Fighter.BOOTS_SLOT);
            }
        }
        if (!(stack.getItem() instanceof ItemArmor)) {
            return this.getBaseColorVector(r, g, b);
        }
        ItemArmor armor = (ItemArmor) stack.getItem();
        switch (armor.getArmorMaterial()) {
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
        int colorInt = armor.getColor(stack);
        float colorRed = (float)(colorInt >> 16 & 0xFF) / 255.0f;
        float colorGreen = (float)(colorInt >> 8 & 0xFF) / 255.0f;
        float colorBlue = (float)(colorInt & 0xFF) / 255.0f;
        return new Vector4f(r *= colorRed, g *= colorGreen, b *= colorBlue, -0.09375f);
    }

    protected void renderModelBuffer(GeoModel model, BufferBuilder buffer, GirlEntity entity, float r, float g, float b, float a, float partialTicks) {
        GeoBone bone = model.topLevelBones.get(0);
        GeoBone bodyBone = null;
        GeoBone headBone = null;

        for (GeoBone child : bone.childBones) {
            switch (child.getName()) {
                case "steve": {
                    headBone = child;
                    break;
                }
                case "body": {
                    bodyBone = child;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);
        this.renderRecursively(buffer, bodyBone, r, g, b, a);
        Tessellator.getInstance().draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getOrCreateDynamicSkin(this.renderEntity));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.renderRecursively(buffer, headBone, r, g, b, this.renderEntity.getRenderScaleFactor());
        Tessellator.getInstance().draw();
        MATRIX_STACK.pop();
    }
}

