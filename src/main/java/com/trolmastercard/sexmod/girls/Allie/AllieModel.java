/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Allie;

import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlModel;
import net.minecraft.util.ResourceLocation;

public class AllieModel
extends GirlModel<GirlEntity> {
    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[] {
                new ResourceLocation("sexmod", "geo/allie/allie.geo.json"),
                new ResourceLocation("sexmod", "geo/allie/armored.geo.json"),
                new ResourceLocation("sexmod", "geo/allie/allie.geo.json")};
    }

    @Override
    public ResourceLocation getModelLocation(GirlEntity girl) {
        if (girl.world instanceof FakeWorld) {
            return this.modelLocations[0];
        }
        if (girl.getDataManager().get(GirlEntity.OUTFIT_INDEX) > this.modelLocations.length) {
            System.out.println("Girl doesn't have an outfit Nr." + girl.getDataManager().get(GirlEntity.OUTFIT_INDEX) + " so im just making her nude lol");
            return this.modelLocations[0];
        }
        if (girl instanceof PlayerAllie) {
            return this.modelLocations[girl.getDataManager().get(GirlEntity.OUTFIT_INDEX)];
        }
        if (girl.getDataManager().get(GirlEntity.OUTFIT_INDEX) == 1) {
            return this.modelLocations[2];
        }
        return this.modelLocations[0];
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/allie/allie.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/allie/allie.animation.json");
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
        return new String[]{"boobsFlesh", "clothes", "clothesR", "clothesL"};
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

