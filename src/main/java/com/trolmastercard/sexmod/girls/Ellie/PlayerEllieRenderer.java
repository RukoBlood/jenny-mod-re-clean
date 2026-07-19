/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Ellie;

import com.trolmastercard.sexmod.girls.PlayerGirlRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerEllieRenderer
extends PlayerGirlRenderer {
    public PlayerEllieRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0f, -1.5f, 0.0f);
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
                GlStateManager.rotate(isLeftHand ? 90.0f : 180.0f, 1.0f, 0.0f, 0.0f);
                if (isLeftHand) {
                    GlStateManager.translate(0.0, 0.23900000452995301, -0.1f);
                    break;
                }
                GlStateManager.translate(0.0, 0.1, -0.07);
            }
        }
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        GlStateManager.rotate(isLeftHand ? 90.0f : 180.0f, 1.0f, 0.0f, 0.0f);
        if (isLeftHand) {
            GlStateManager.translate(0.2, -0.2, 0.0);
        }
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        if (isLeftHand) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            if (isActive) {
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.4f, 0.0f, 0.228f);
            }
        } else {
            GlStateManager.translate(0.0f, 0.282f, 0.141f);
            if (isActive) {
                GlStateManager.translate(0.165, -0.45f, 0.0);
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-27.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.translate(0.0, 0.0, -0.05);
            }
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

