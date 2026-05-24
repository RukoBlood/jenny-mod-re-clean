/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Player;

import java.util.HashSet;

import com.trolmastercard.sexmod.girls.PlayerGirlRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerBiaRenderer
extends PlayerGirlRenderer {
    public PlayerBiaRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void void_c() {
        GlStateManager.translate(0.0, -1.0, -0.05);
        GlStateManager.scale(0.65f, 0.65f, 0.65f);
    }

    @Override
    protected void a(boolean bl) {
        super.a(bl);
        if (bl) {
            GlStateManager.translate(0.15, 0.0, 0.0);
        }
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        super.a(bl, bl2);
        if (!bl && !bl2) {
            GlStateManager.translate(0.0, -0.1, 0.05);
            GlStateManager.rotate(40.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
            return;
        }
        if (bl && !bl2) {
            GlStateManager.translate(-0.025, -0.1, 0.0);
            return;
        }
    }

    @Override
    public HashSet<String> a() {
        return new HashSet<String>(){
            {
                this.add("boobs");
                this.add("booty");
                this.add("vagina");
                this.add("fuckhole");
                this.add("leaf7");
                this.add("leaf8");
            }
        };
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

