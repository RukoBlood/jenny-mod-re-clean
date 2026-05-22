/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.math.Vec3i;

public enum by_class106 {
    LIGHT_GREEN(213, 239, 150),
    MEDIUM_GREEN(189, 165, 91),
    DARK_GREEN(160, 183, 135),
    LIGHT_YELLOW(234, 176, 102),
    LIGHT_BLUE(187, 203, 252);

    final private Vec3i a;

    private by_class106(int n2, int n3, int n4) {
        this.a = new Vec3i(n2, n3, n4);
    }

    public Vec3i a() {
        return this.a;
    }

    public static int a(by_class106 by_class1062) {
        int n = 0;
        for (by_class106 by_class1063 : by_class106.values()) {
            if (by_class1062 == by_class1063) {
                return n;
            }
            ++n;
        }
        return n;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

