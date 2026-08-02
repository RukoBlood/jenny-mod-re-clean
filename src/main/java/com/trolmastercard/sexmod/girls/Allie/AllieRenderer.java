/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Allie;

import com.trolmastercard.sexmod.girls.Action;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlRenderer;
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
    public void render(GeoModel model, GirlEntity entity, float partialTicks, float r, float g, float b, float a) {
        AllieEntity allieEntity = (AllieEntity) entity;
        if (entity.currentAction() == Action.NULL && !entity.isLocallyRegistered()) {
            return;
        }
        a = allieEntity.U = allieEntity.U == 1.0f ? allieEntity.U : allieEntity.U - 0.01f;
        GlStateManager.scale(a, a, a);
        GlStateManager.translate(0.0f, a == 1.0f ? 0.0f : 3.0f - a * 3.0f, 0.0f);
        super.render(model, entity, partialTicks, r, g, b, a);
    }

    @Override
    protected void renderNameTag(double x, double y, double z) {
        if (this.renderEntity.currentAction() == Action.NULL) {
            return;
        }
        if (this.renderEntity.isLocallyRegistered()) {
            return;
        }
        if (this.renderEntity.currentAction().hideNameTag) {
            return;
        }
        if (AllieRenderer.mc.getRenderManager().renderViewEntity == null) {
            return;
        }
        this.renderLivingLabel(this.renderEntity, this.renderEntity.getDisplayNameText(), x, y + (double)this.renderEntity.getNameTagHeightOffset(), z, 300);
    }
}

