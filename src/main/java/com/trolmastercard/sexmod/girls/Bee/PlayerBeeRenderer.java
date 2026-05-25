/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Bee;

import com.trolmastercard.sexmod.girls.PlayerGirlRenderer;
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
    protected void a(boolean bl, ItemStack itemStack) {
        GlStateManager.rotate(bl ? 290.0f : 90.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override
    protected void void_c() {
        GlStateManager.translate(0.0f, -0.6f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    @Override
    protected void a(boolean bl) {
        super.a(bl);
        if (bl) {
            GlStateManager.translate(0.1, 0.0, 0.0);
        }
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        if (bl) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.14f, -0.17f);
            if (bl2) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.067, 0.0, 0.0);
            }
        } else if (bl2) {
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.165f, 0.0f);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

