/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Slime.FriendlySlimeEntity;
import com.trolmastercard.sexmod.girls.Slime.FriendlySlimeRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class a4_class11
implements LayerRenderer<FriendlySlimeEntity> {
    final private FriendlySlimeRenderer b;
    final private ModelBase a = new ModelSlime(0);

    public a4_class11(FriendlySlimeRenderer bp_class922) {
        this.b = bp_class922;
    }

    //a
    @Override
    public void doRenderLayer(FriendlySlimeEntity ay_class512, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!ay_class512.isInvisible()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.a.setModelAttributes(this.b.getMainModel());
            this.a.render(ay_class512, f, f2, f4, f5, f6, f7);
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

