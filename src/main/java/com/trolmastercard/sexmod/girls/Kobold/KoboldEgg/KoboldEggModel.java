/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.KoboldEgg;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KoboldEggModel extends AnimatedGeoModel<KoboldEggEntity> {
    @Override
    public ResourceLocation getModelLocation(KoboldEggEntity t) {
        return new ResourceLocation("sexmod", "geo/kobold/koboldegg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(KoboldEggEntity t) {
        return new ResourceLocation("sexmod", "textures/entity/kobold/koboldegg.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(KoboldEggEntity e) {
        return new ResourceLocation("sexmod", "animations/kobold/egg.animation.json");
    }
}

