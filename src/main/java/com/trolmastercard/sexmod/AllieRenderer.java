/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AllieRenderer
extends GirlRenderer {
    public AllieRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    public void render(GeoModel geoModel, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
        AllieEntity ev_class2752 = (AllieEntity)em_class2582;
        if (em_class2582.currentAction() == Action.NULL && !em_class2582.boolean_h()) {
            return;
        }
        f5 = ev_class2752.U = ev_class2752.U == 1.0f ? ev_class2752.U : ev_class2752.U - 0.01f;
        GlStateManager.scale(f5, f5, f5);
        GlStateManager.translate(0.0f, f5 == 1.0f ? 0.0f : 3.0f - f5 * 3.0f, 0.0f);
        super.render(geoModel, em_class2582, f, f2, f3, f4, f5);
    }

    @Override
    protected void a(double d, double d2, double d3) {
        if (this.j.currentAction() == Action.NULL) {
            return;
        }
        if (this.j.boolean_h()) {
            return;
        }
        if (this.j.currentAction().hideNameTag) {
            return;
        }
        if (AllieRenderer.i.getRenderManager().renderViewEntity == null) {
            return;
        }
        this.renderLivingLabel(this.j, this.j.java_lang_String_ab(), d, d2 + (double)this.j.float_i(), d3, 300);
    }
}

