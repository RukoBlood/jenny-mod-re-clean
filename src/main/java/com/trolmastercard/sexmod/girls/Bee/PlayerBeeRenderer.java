/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Bee;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerBeeRenderer
extends PlayerGirlRenderer {
    public PlayerBeeRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        GlStateManager.rotate(isLeftHand ? 290.0f : 90.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0f, -0.6f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        super.applyBowRotation(isLeftHand);
        if (isLeftHand) {
            GlStateManager.translate(0.1, 0.0, 0.0);
        }
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        if (isLeftHand) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.14f, -0.17f);
            if (isActive) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.067, 0.0, 0.0);
            }
        } else if (isActive) {
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.165f, 0.0f);
        }
    }
}

