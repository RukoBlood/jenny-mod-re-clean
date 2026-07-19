/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Jenny;

import com.trolmastercard.sexmod.girls.PlayerGirlRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerJennyRenderer
extends PlayerGirlRenderer {
    public PlayerJennyRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0f, -1.25f, 0.0f);
        GlStateManager.scale(0.8f, 0.8f, 0.8f);
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (!isLeftHand && !isActive) {
            GlStateManager.translate(0.0, -0.1, 0.05);
            GlStateManager.rotate(40.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
            return;
        }
        if (isLeftHand && !isActive) {
            GlStateManager.translate(-0.025, -0.1, 0.0);
            return;
        }
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        super.applyBowRotation(isLeftHand);
        if (isLeftHand) {
            GlStateManager.translate(0.15, 0.0, 0.0);
        }
    }
}

