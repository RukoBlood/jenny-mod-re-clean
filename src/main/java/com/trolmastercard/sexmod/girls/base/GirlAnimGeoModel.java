/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.base;

import java.lang.reflect.Field;

import com.trolmastercard.sexmod.util.anim.CachedAnimationProcessor;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.exception.GeoModelException;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class GirlAnimGeoModel<T extends GirlEntity> extends AnimatedGeoModel<T> {
    protected GirlAnimGeoModel() {
        try {
            Field field = Class.forName("software.bernie.geckolib3.model.AnimatedGeoModel").getDeclaredField("animationProcessor");
            field.setAccessible(true);
            field.set(this, new CachedAnimationProcessor(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public GeoModel getModel(ResourceLocation location) {
        GeoModel model = super.getModel(location);
        if (model == null) {
            throw new GeoModelException(location, "Could not find model.");
        }
        this.getAnimationProcessor().clearModelRendererList();
        for (GeoBone bone : model.topLevelBones) {
            this.registerBone(bone);
        }
        return model;
    }
}

