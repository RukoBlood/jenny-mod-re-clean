/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KoboldEggModel
extends AnimatedGeoModel<KoboldEggItem> {
    @Override
    public ResourceLocation getModelLocation(KoboldEggItem c7_class1162) {
        return new ResourceLocation("sexmod", "geo/kobold/koboldegg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(KoboldEggItem c7_class1162) {
        return new ResourceLocation("sexmod", "textures/entity/kobold/koboldegg.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(KoboldEggItem c7_class1162) {
        return new ResourceLocation("sexmod", "animations/kobold/egg.animation.json");
    }
}

