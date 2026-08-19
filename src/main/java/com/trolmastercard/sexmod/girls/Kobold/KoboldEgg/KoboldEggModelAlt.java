/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.KoboldEgg;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KoboldEggModelAlt extends AnimatedGeoModel<KoboldEggItem> {
    @Override
    public ResourceLocation getModelLocation(KoboldEggItem t) {
        return new ResourceLocation("sexmod", "geo/kobold/koboldegg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(KoboldEggItem t) {
        return new ResourceLocation("sexmod", "textures/entity/kobold/koboldegg.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(KoboldEggItem e) {
        return new ResourceLocation("sexmod", "animations/kobold/egg.animation.json");
    }
}

