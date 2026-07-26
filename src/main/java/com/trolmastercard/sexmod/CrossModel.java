/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrossModel extends AnimatedGeoModel<CustomModelEntity> {
    @Override
    public ResourceLocation getModelLocation(CustomModelEntity entity) {
        if (entity.isItemModel) {
            return new ResourceLocation("sexmod", "geo/cross.geo.json");
        }
        return CustomModel.k(entity.getModelName());
    }

    @Override
    public ResourceLocation getTextureLocation(CustomModelEntity cy_class1532) {
        if (cy_class1532.isItemModel) {
            return new ResourceLocation("sexmod", "textures/cross.png");
        }
        return CustomModel.c(cy_class1532.getModelName());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CustomModelEntity cy_class1532) {
        return new ResourceLocation("sexmod", "animations/slime/slime.animation.json");
    }

    @Override
    public void setLivingAnimations(CustomModelEntity cy_class1532, Integer n, @Nullable AnimationEvent animationEvent) {
    }
}

