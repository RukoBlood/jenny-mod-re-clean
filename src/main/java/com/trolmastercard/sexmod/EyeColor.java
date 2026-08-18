/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.math.Vec3i;

public enum EyeColor {
    RED(255, 0, 0),
    VIOLET(132, 30, 156),
    YELLOW(243, 247, 0),
    BROWN(105, 60, 9),
    TURKEY(0, 206, 217),
    BLUE(0, 0, 255);

    final private Vec3i b;

    private EyeColor(int n2, int n3, int n4) {
        this.b = new Vec3i(n2, n3, n4);
    }

    public Vec3i a() {
        return this.b;
    }

    public static EyeColor a(Vec3i vec3i) {
        for (EyeColor eh_class2502 : EyeColor.values()) {
            if (!vec3i.equals(eh_class2502.a())) continue;
            return eh_class2502;
        }
        return RED;
    }

    public static int a(EyeColor eh_class2502) {
        int n = 0;
        for (EyeColor eh_class2503 : EyeColor.values()) {
            if (eh_class2502 == eh_class2503) {
                return n;
            }
            ++n;
        }
        return n;
    }
}

