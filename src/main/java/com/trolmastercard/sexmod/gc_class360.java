/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.util.math.Vec3d;

public class gc_class360 {
    public static double a(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = vec3d2.y - vec3d.y;
        double d3 = vec3d2.z - vec3d.z;
        return Math.atan2(d3, Math.sqrt(d * d + d2 * d2));
    }

    public static float b(float f) {
        if ((f %= 360.0f) < 0.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static float a(float var0) {
        float var1;
        return (var1 = var0 % 360.0F) >= 0.0F ? var1 : var1 + 360.0F;
    }

    public static double a(double var0) {
        double var2;
        return (var2 = var0 % 360.0) >= 0.0 ? var2 : var2 + 360.0;
    }

    public static float c(float f) {
        return (float)(Math.PI * 2 / (360.0 / (double)f));
    }

    public static float c(double d) {
        return (float)(Math.PI * 2 / (360.0 / d));
    }

    public static float d(float f) {
        return (float)(57.29577951308232 * (double)f);
    }

    public static double b(double d) {
        return 57.29577951308232 * d;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

