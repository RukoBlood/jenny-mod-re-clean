/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.deprecated.NYIWinchesterItem;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NYIWinchesterModel extends AnimatedGeoModel<NYIWinchesterItem> {
    @Override
    public ResourceLocation getModelLocation(NYIWinchesterItem item) {
        return new ResourceLocation("sexmod", "geo/west/winchester.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(NYIWinchesterItem item) {
        return new ResourceLocation("sexmod", "textures/items/winchester/winchester.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(NYIWinchesterItem item) {
        return new ResourceLocation("sexmod", "animations/west/winchester.animation.json");
    }
}

