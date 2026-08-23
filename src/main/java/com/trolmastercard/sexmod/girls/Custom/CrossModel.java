/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Custom;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrossModel extends AnimatedGeoModel<CustomModelEntity> {
    @Override
    public ResourceLocation getModelLocation(CustomModelEntity entity) {
        if (entity.isItemModel) {
            return new ResourceLocation("sexmod", "geo/cross.geo.json");
        }
        return CustomModel.getModelResource(entity.getModelCode());
    }

    @Override
    public ResourceLocation getTextureLocation(CustomModelEntity entity) {
        if (entity.isItemModel) {
            return new ResourceLocation("sexmod", "textures/cross.png");
        }
        return CustomModel.getTextureResource(entity.getModelCode());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CustomModelEntity entity) {
        return new ResourceLocation("sexmod", "animations/slime/slime.animation.json");
    }

    @Override
    public void setLivingAnimations(CustomModelEntity entity, Integer uniqueID, @Nullable AnimationEvent event) {
    }
}

