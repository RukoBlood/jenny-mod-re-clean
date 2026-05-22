/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KoboldEggModel2
extends AnimatedGeoModel<KoboldEggEntity> {
    @Override
    public ResourceLocation getModelLocation(KoboldEggEntity i_class4102) {
        return new ResourceLocation("sexmod", "geo/kobold/koboldegg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(KoboldEggEntity i_class4102) {
        return new ResourceLocation("sexmod", "textures/entity/kobold/koboldegg.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(KoboldEggEntity i_class4102) {
        return new ResourceLocation("sexmod", "animations/kobold/egg.animation.json");
    }
}

