/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.lang.reflect.Field;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.exception.GeoModelException;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

// TODO
//  if generic conflicts, then revert to: 'T extends IAnimatable'
public abstract class IGirlAnimGeoModel<T extends GirlEntity>
extends AnimatedGeoModel<T> {
    protected IGirlAnimGeoModel() {
        try {
            Field field = Class.forName("software.bernie.geckolib3.model.AnimatedGeoModel").getDeclaredField("animationProcessor");
            field.setAccessible(true);
            field.set(this, new c1_class110(this));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public GeoModel getModel(ResourceLocation resourceLocation) {
        GeoModel geoModel = super.getModel(resourceLocation);
        if (geoModel == null) {
            throw new GeoModelException(resourceLocation, "Could not find model.");
        }
        this.getAnimationProcessor().clearModelRendererList();
        for (GeoBone geoBone : geoModel.topLevelBones) {
            this.registerBone(geoBone);
        }
        return geoModel;
    }
}

