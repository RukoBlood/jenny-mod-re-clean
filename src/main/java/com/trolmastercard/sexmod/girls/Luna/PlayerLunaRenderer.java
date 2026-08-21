/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Luna;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerLunaRenderer
extends PlayerGirlRenderer {
    float z = 0.0f;

    public PlayerLunaRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.65f, 0.65f, 0.65f);
    }

    @Override
    protected ItemStack getHeldItem(@Nullable ItemStack input) {
        switch (this.renderEntity.getCurrentAction()) {
            case FISHING_IDLE: 
            case FISHING_START: {
                ItemStack itemStack2 = ((LunaEntity)this.renderEntity).lunaRod;
                this.renderEntity.setHeldItem(EnumHand.MAIN_HAND, itemStack2);
                return itemStack2;
            }
        }
        return input;
    }

    boolean boolean_b() {
        return this.renderEntity.getDataManager().get(GirlEntity.IS_ANCHORED);
    }

    @Override
    protected void onBoneRenderStart(String boneName, GeoBone geoBone) {
        if (Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        switch (boneName) {
            case "head": {
                this.z = geoBone.getRotationX();
                break;
            }
            case "backHair": {
                if (this.boolean_b() || !(this.z > 0.0f)) break;
                double d = this.z / TrigMath.wrapDegrees(45.0f);
                float f = (float) RotationHelper.LerpDouble(0.0, 0.75, d);
                geoBone.setPositionZ(f);
                geoBone.setPositionY(f);
                geoBone.setRotationX(-this.z);
                break;
            }
            case "frontHairL": 
            case "frontHairR": {
                if (this.boolean_b()) break;
                geoBone.setRotationX(-this.z);
            }
        }
    }

    @Override
    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        super.applyItemPostRotation(isLeftHand, stack);
        switch (stack.getItem().getItemUseAction(stack)) {
            case BLOCK: 
            case BOW: {
                break;
            }
            default: {
                GlStateManager.rotate(isLeftHand ? 60.0f : 150.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, 0.08, -0.05);
            }
        }
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        GlStateManager.rotate(isLeftHand ? 60.0f : 150.0f, 1.0f, 0.0f, 0.0f);
        if (isLeftHand) {
            GlStateManager.translate(0.12, 0.0, 0.0);
        }
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (!isLeftHand && isActive) {
            GlStateManager.rotate(120.0f, 0.0f, 1.0f, 0.0f);
            return;
        }
        if (!isLeftHand && !isActive) {
            GlStateManager.translate(0.0, 0.3, -0.15);
            GlStateManager.rotate(-45.0f, 1.0f, 0.0f, 0.0f);
            return;
        }
        if (isLeftHand && !isActive) {
            GlStateManager.translate(-0.025, -0.05, 0.0);
            return;
        }
    }
}

