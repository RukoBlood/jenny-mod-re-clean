/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

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
    public ResourceLocation getModelLocation(GirlEntity em_class2582) {
        if (em_class2582.world instanceof FakeWorld) {
            return this.c[0];
        }
        if (em_class2582.getDataManager().get(GirlEntity.D) > this.c.length) {
            System.out.println("Girl doesn't have an outfit Nr." + em_class2582.getDataManager().get(GirlEntity.D) + " so im just making her nude lol");
            return this.c[0];
        }
        if (em_class2582 instanceof PlayerAllie) {
            return this.c[em_class2582.getDataManager().get(GirlEntity.D)];
        }
        if (em_class2582.getDataManager().get(GirlEntity.D) == 1) {
            return this.c[2];
        }
        return this.c[0];
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/allie/allie.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity em_class2582) {
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

