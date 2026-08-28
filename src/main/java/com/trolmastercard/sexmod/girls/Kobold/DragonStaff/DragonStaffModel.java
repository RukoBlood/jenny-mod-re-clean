/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.DragonStaff;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonStaffModel extends AnimatedGeoModel<DragonStaffItem> {
    @Override
    public ResourceLocation getModelLocation(DragonStaffItem t) {
        return new ResourceLocation("sexmod", "geo/kobold/staff.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DragonStaffItem t) {
        return new ResourceLocation("sexmod", "textures/entity/kobold/staff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonStaffItem e) {
        return new ResourceLocation("sexmod", "animations/kobold/staff.animation.json");
    }
}

