/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GalathCoinModel
extends AnimatedGeoModel<GalathCoin> {
    @Override
    public ResourceLocation getModelLocation(GalathCoin cc_class1242) {
        return new ResourceLocation("sexmod", "geo/galath/galath_coin.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GalathCoin cc_class1242) {
        return new ResourceLocation("sexmod", "textures/items/galath_coin/galath_coin.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GalathCoin cc_class1242) {
        return new ResourceLocation("sexmod", "animations/galath/galath_coin.animation.json");
    }
}

