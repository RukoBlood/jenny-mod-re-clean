/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Slime.friendlySlime.FriendlySlimeEntity;
import com.trolmastercard.sexmod.girls.Slime.friendlySlime.FriendlySlimeRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class FriendlySlimeGelLayer implements LayerRenderer<FriendlySlimeEntity> {
    final private FriendlySlimeRenderer slimeRenderer;
    final private ModelBase gelModel = new ModelSlime(0);

    public FriendlySlimeGelLayer(FriendlySlimeRenderer renderer) {
        this.slimeRenderer = renderer;
    }

    //a
    @Override
    public void doRenderLayer(FriendlySlimeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.isInvisible()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.gelModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.gelModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    // gay synthetic
    //@Override
    //public void doRenderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
    //    this.a((ay_class51)entityLivingBase, f, f2, f3, f4, f5, f6, f7);
    //}
}

