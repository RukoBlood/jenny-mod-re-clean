/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Slime.friendlySlime;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class FriendlySlimeRenderer extends RenderLiving<FriendlySlimeEntity> {
    final static private ResourceLocation TEXTURE = new ResourceLocation("textures/entity/slime/slime.png");

    public FriendlySlimeRenderer(RenderManager renderManager) {
        super(renderManager, new FriendlySlimeModel(), 0.25f);
        this.addLayer(new FriendlySlimeGelLayer(this));
    }

    // a
    @Override
    public void doRender(FriendlySlimeEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.shadowSize = 0.25f * (float)entity.getSquishFactor();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    // a
    // preRenderCallback
    @Override
    protected void preRenderCallback(FriendlySlimeEntity entity, float f) {
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
        float squish = entity.getSquishFactor();
        float f4 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * f) / (squish * 0.5f + 1.0f);
        float f5 = 1.0f / (f4 + 1.0f);
        GlStateManager.scale(f5 * squish, 1.0f / f5 * squish, f5 * squish);
    }

    // a
    @Override
    protected ResourceLocation getEntityTexture(FriendlySlimeEntity entity) {
        return TEXTURE;
    }
}

