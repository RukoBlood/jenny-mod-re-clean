/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlModel;
import net.minecraft.util.ResourceLocation;

public class CatModel
extends GirlModel<GirlEntity> {
    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{
                new ResourceLocation("sexmod", "geo/cat/cat.geo.json"),
                new ResourceLocation("sexmod", "geo/cat/cat.geo.json")};
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/cat/cat.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girlEntity) {
        return new ResourceLocation("sexmod", "animations/cat/cat.animation.json");
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    @Override
    public String[] TopArmor() {
        return new String[]{"armorShoulderR", "armorShoulderL", "armorChest", "armorBoobs"};
    }

    @Override
    public String[] Top() {
        return new String[]{"boobsFlesh", "cloth"};
    }

    @Override
    public String[] BottomArmor() {
        return new String[]{"armorBootyR", "armorBootyL", "armorPantsLowL", "armorPantsLowR", "armorPantsLowR", "armorPantsUpR", "armorPantsUpL", "armorHip"};
    }

    @Override
    public String[] Bottom() {
        return new String[]{"fleshL", "fleshR", "vagina", "curvesL", "curvesR", "kneeL", "kneeR", "cloth"};
    }

    @Override
    public String[] ShoesArmor() {
        return new String[]{"armorShoesL", "armorShoesR"};
    }
}

