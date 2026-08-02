/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Slime.friendlySlime;

import com.trolmastercard.sexmod.FriendlySlimeGelLayer;
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
        this.shadowSize = 0.25f * (float)entity.h();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    // a
    // preRenderCallback
    @Override
    protected void preRenderCallback(FriendlySlimeEntity entity, float f) {
        float f2 = 0.999f;
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
        float f3 = entity.h();
        float f4 = (entity.h + (entity.e - entity.h) * f) / (f3 * 0.5f + 1.0f);
        float f5 = 1.0f / (f4 + 1.0f);
        GlStateManager.scale(f5 * f3, 1.0f / f5 * f3, f5 * f3);
    }

    // a
    @Override
    protected ResourceLocation getEntityTexture(FriendlySlimeEntity entity) {
        return TEXTURE;
    }
}

