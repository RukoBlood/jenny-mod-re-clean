/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.interfaces;

import java.lang.reflect.Field;

import com.trolmastercard.sexmod.CachedAnimationProcessor;
import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.exception.GeoModelException;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

// TODO
//  if generic conflicts, then revert to: 'T extends IAnimatable'
public abstract class IGirlAnimGeoModel<T extends GirlEntity> extends AnimatedGeoModel<T> {
    protected IGirlAnimGeoModel() {
        try {
            Field field = Class.forName("software.bernie.geckolib3.model.AnimatedGeoModel").getDeclaredField("animationProcessor");
            field.setAccessible(true);
            field.set(this, new CachedAnimationProcessor(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public GeoModel getModel(ResourceLocation resourceLocation) {
        GeoModel model = super.getModel(resourceLocation);
        if (model == null) {
            throw new GeoModelException(resourceLocation, "Could not find model.");
        }
        this.getAnimationProcessor().clearModelRendererList();
        for (GeoBone bone : model.topLevelBones) {
            this.registerBone(bone);
        }
        return model;
    }
}

