/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.math.Vec3i;
//g5
//goblin colors or some shit
public enum g5_class349 {
    PURPLE(103, 39, 123),
    ORANGE(251, 153, 56),
    BLACK(30, 33, 38),
    BLUE(88, 83, 186),
    BROWN(63, 35, 34),
    PINK(247, 102, 109),
    RED(241, 69, 49),
    GREEN(75, 143, 106);

    final private Vec3i b;

    private g5_class349(int n2, int n3, int n4) {
        this.b = new Vec3i(n2, n3, n4);
    }

    public Vec3i a() {
        return this.b;
    }

    public static int a(g5_class349 g5_class3492) {
        int n = 0;
        for (g5_class349 g5_class3493 : g5_class349.values()) {
            if (g5_class3492 == g5_class3493) {
                return n;
            }
            ++n;
        }
        return n;
    }
}

