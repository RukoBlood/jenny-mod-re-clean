/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.math.Vec3i;

public enum SkinColor {
    LIGHT_GREEN(213, 239, 150),
    MEDIUM_GREEN(189, 165, 91),
    DARK_GREEN(160, 183, 135),
    LIGHT_YELLOW(234, 176, 102),
    LIGHT_BLUE(187, 203, 252);

    final private Vec3i color;

    private SkinColor(int red, int green, int blue) {
        this.color = new Vec3i(red, green, blue);
    }

    public Vec3i getColor() {
        return this.color;
    }

    public static int indexOf(SkinColor color) {
        int idx = 0;
        for (SkinColor value : SkinColor.values()) {
            if (color == value) {
                return idx;
            }
            ++idx;
        }
        return idx;
    }
}

