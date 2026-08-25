/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Goblin;

import net.minecraft.util.math.Vec3i;
//g5
//goblin colors or some shit
public enum HairColor {
    PURPLE(103, 39, 123),
    ORANGE(251, 153, 56),
    BLACK(30, 33, 38),
    BLUE(88, 83, 186),
    BROWN(63, 35, 34),
    PINK(247, 102, 109),
    RED(241, 69, 49),
    GREEN(75, 143, 106);

    final private Vec3i color;

    private HairColor(int r, int g, int b) {
        this.color = new Vec3i(r, g, b);
    }

    public Vec3i getColor() {
        return this.color;
    }

    public static int indexOf(HairColor color) {
        int index = 0;
        for (HairColor value : HairColor.values()) {
            if (color == value) {
                return index;
            }
            ++index;
        }
        return index;
    }
}

