/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.KoboldEgg;

import java.awt.Color;

import com.trolmastercard.sexmod.girls.Kobold.EyeAndKoboldColor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class KoboldEggRenderer
extends GeoEntityRenderer<KoboldEggEntity> {
    final static public Color eggColor = new Color(223, 206, 155);
    KoboldEggEntity eggEntity;

    public KoboldEggRenderer(RenderManager renderManager, AnimatedGeoModel<KoboldEggEntity> model) {
        super(renderManager, model);
    }

    @Override
    public void render(GeoModel model, KoboldEggEntity animatable, float partialTicks, float red, float green, float blue, float alpha) {
        this.eggEntity = animatable;
        super.render(model, animatable, partialTicks, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float r, float g, float b, float alpha) {
        String string = geoBone.getName();
        if ("shell".equals(string)) {
            r = (float) eggColor.getRed() / 255.0f;
            g = (float) eggColor.getGreen() / 255.0f;
            b = (float) eggColor.getBlue() / 255.0f;
        }
        if ("colorSpots".equals(string)) {
            Vec3i vec3i = EyeAndKoboldColor.safeValueOf(this.eggEntity.getDataManager().get(KoboldEggEntity.EGG_COLOR)).getMainColor();
            r = (float)vec3i.getX() / 255.0f;
            g = (float)vec3i.getY() / 255.0f;
            b = (float)vec3i.getZ() / 255.0f;
        }
        super.renderRecursively(bufferBuilder, geoBone, r, g, b, alpha);
    }

    //@Override
    //public ResourceLocation getEntityTexture(Entity entity) {
    //    return super.getEntityTexture((i_class410)entity);
    //}
//
    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    super.doRender((i_class410)entity, d, d2, d3, f, f2);
    //}
}

