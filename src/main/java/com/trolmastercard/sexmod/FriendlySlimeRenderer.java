/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class FriendlySlimeRenderer
extends RenderLiving<FriendlySlimeEntity> {
    final static private ResourceLocation a = new ResourceLocation("textures/entity/slime/slime.png");

    public FriendlySlimeRenderer(RenderManager renderManager) {
        super(renderManager, new c__class120(), 0.25f);
        this.addLayer(new a4_class11(this));
    }

    // a
    @Override
    public void doRender(FriendlySlimeEntity ay_class512, double d, double d2, double d3, float f, float f2) {
        this.shadowSize = 0.25f * (float)ay_class512.h();
        super.doRender(ay_class512, d, d2, d3, f, f2);
    }

    // a
    // preRenderCallback
    @Override
    protected void preRenderCallback(FriendlySlimeEntity ay_class512, float f) {
        float f2 = 0.999f;
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
        float f3 = ay_class512.h();
        float f4 = (ay_class512.h + (ay_class512.e - ay_class512.h) * f) / (f3 * 0.5f + 1.0f);
        float f5 = 1.0f / (f4 + 1.0f);
        GlStateManager.scale(f5 * f3, 1.0f / f5 * f3, f5 * f3);
    }

    // a
    @Override
    protected ResourceLocation getEntityTexture(FriendlySlimeEntity friendlySlimeEntity) {
        return a;
    }
}

