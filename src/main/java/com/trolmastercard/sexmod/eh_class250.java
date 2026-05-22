/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.math.Vec3i;

public enum eh_class250 {
    RED(255, 0, 0),
    VIOLET(132, 30, 156),
    YELLOW(243, 247, 0),
    BROWN(105, 60, 9),
    TURKEY(0, 206, 217),
    BLUE(0, 0, 255);

    final private Vec3i b;

    private eh_class250(int n2, int n3, int n4) {
        this.b = new Vec3i(n2, n3, n4);
    }

    public Vec3i a() {
        return this.b;
    }

    public static eh_class250 a(Vec3i vec3i) {
        for (eh_class250 eh_class2502 : eh_class250.values()) {
            if (!vec3i.equals(eh_class2502.a())) continue;
            return eh_class2502;
        }
        return RED;
    }

    public static int a(eh_class250 eh_class2502) {
        int n = 0;
        for (eh_class250 eh_class2503 : eh_class250.values()) {
            if (eh_class2502 == eh_class2503) {
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

