/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Luna;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import com.trolmastercard.sexmod.util.MatrixHelper;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class LunaRenderer extends GirlRenderer {
    float r;

    public LunaRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    protected ItemStack getHeldItem(@Nullable ItemStack input) {
        switch (this.renderEntity.getCurrentAction()) {
            case FISHING_IDLE: 
            case FISHING_START: {
                ItemStack itemStack2 = ((LunaEntity)this.renderEntity).ao;
                ItemStack itemStack3 = this.renderEntity.getDataManager().get(LunaEntity.FISHING_ROD);
                if (itemStack3.equals(ItemStack.EMPTY)) {
                    return itemStack2;
                }
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack3);
                EnchantmentHelper.setEnchantments(map, itemStack2);
                this.renderEntity.setHeldItem(EnumHand.MAIN_HAND, itemStack2);
                return itemStack2;
            }
        }
        return input;
    }

    boolean boolean_a() {
        return this.renderEntity.getDataManager().get(GirlEntity.IS_ANCHORED);
    }

    @Override
    protected void onBoneProcessing(BufferBuilder buffer, String boneName, GeoBone bone) {
        if (Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        switch (boneName) {
            case "head": {
                this.r = bone.getRotationX();
                break;
            }
            case "backHair": {
                if (this.boolean_a()) break;
                double d = this.r / TrigMath.wrapDegrees(45.0f);
                float f = (float) RotationHelper.LerpDouble(0.0, 0.75, d);
                bone.setPositionZ(f);
                bone.setPositionY(f);
                bone.setRotationX(-this.r);
                break;
            }
            case "sideHairR": 
            case "sideHairL": {
                if (this.boolean_a()) break;
                double d = this.r / TrigMath.wrapDegrees(45.0f);
                float f = (float) RotationHelper.LerpDouble(0.0, (double)1.3f, d);
                bone.setPositionZ(-f);
                bone.setPositionY(f);
            }
            case "frontHairL": 
            case "frontHairR": {
                if (this.boolean_a()) break;
                bone.setRotationX(-this.r);
                break;
            }
            case "offhand": {
                LunaEntity eb_class2362 = (LunaEntity)this.renderEntity;
                ItemStack itemStack = this.renderEntity.getDataManager().get(LunaEntity.CAUGHT_ITEM);
                if (itemStack.equals(ItemStack.EMPTY) || eb_class2362.throwBackPercentage != 1.0f) break;
                GlStateManager.pushMatrix();
                Tessellator.getInstance().draw();
                MatrixHelper.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, bone);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.scale(eb_class2362.fishSizePercentage, eb_class2362.fishSizePercentage, eb_class2362.fishSizePercentage);
                Minecraft.getMinecraft().getItemRenderer().renderItem(this.renderEntity, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
                GirlRenderer.tempBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
                GlStateManager.popMatrix();
            }
        }
    }
}

