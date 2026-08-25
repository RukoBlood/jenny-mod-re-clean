/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Goblin;

import net.minecraft.util.math.Vec3i;

public enum EyeColor {
    RED(255, 0, 0),
    VIOLET(132, 30, 156),
    YELLOW(243, 247, 0),
    BROWN(105, 60, 9),
    TURKEY(0, 206, 217),
    BLUE(0, 0, 255);

    final private Vec3i color;

    private EyeColor(int r, int g, int b) {
        this.color = new Vec3i(r, g, b);
    }

    public Vec3i getColor() {
        return this.color;
    }

    public static EyeColor fromColor(Vec3i rgb) {
        for (EyeColor color : EyeColor.values()) {
            if (rgb.equals(color.getColor())) {
                return color;
            }
        }
        return RED;
    }

    public static int indexOf(EyeColor color) {
        int index = 0;
        for (EyeColor value : EyeColor.values()) {
            if (color == value) {
                return index;
            }
            ++index;
        }
        return index;
    }
}

