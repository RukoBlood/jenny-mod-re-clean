/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class KoboldEggRenderer
extends GeoEntityRenderer<KoboldEggEntity> {
    final static public Color b = new Color(223, 206, 155);
    KoboldEggEntity a;

    public KoboldEggRenderer(RenderManager renderManager, AnimatedGeoModel<KoboldEggEntity> animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    public void render(GeoModel geoModel, KoboldEggEntity i_class4102, float f, float f2, float f3, float f4, float f5) {
        this.a = i_class4102;
        super.render(geoModel, i_class4102, f, f2, f3, f4, f5);
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        String string = geoBone.getName();
        if ("shell".equals(string)) {
            f = (float)b.getRed() / 255.0f;
            f2 = (float)b.getGreen() / 255.0f;
            f3 = (float)b.getBlue() / 255.0f;
        }
        if ("colorSpots".equals(string)) {
            Vec3i vec3i = EyeAndKoboldColor.safeValueOf(this.a.getDataManager().get(KoboldEggEntity.b)).getMainColor();
            f = (float)vec3i.getX() / 255.0f;
            f2 = (float)vec3i.getY() / 255.0f;
            f3 = (float)vec3i.getZ() / 255.0f;
        }
        super.renderRecursively(bufferBuilder, geoBone, f, f2, f3, f4);
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

