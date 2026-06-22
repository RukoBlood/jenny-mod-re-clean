/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import net.minecraft.util.math.Vec3d;

public class TrigMath {
    public static double calculatePitchAngle(Vec3d source, Vec3d target) {
        double dX = target.x - source.x;
        double dY = target.y - source.y;
        double dZ = target.z - source.z;
        return Math.atan2(dZ, Math.sqrt(dX * dX + dY * dY));
    }

    public static float b(float f) {
        if ((f %= 360.0f) < 0.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static float normalizedAngle360(float angle) {
        float normalized;
        return (normalized = angle % 360.0F) >= 0.0F ? normalized : normalized + 360.0F;
    }

    public static double normalizedAngle360(double angle) {
        double var2;
        return (var2 = angle % 360.0) >= 0.0 ? var2 : var2 + 360.0;
    }

    public static float toRadians(float deg) {
        return (float)(Math.PI * 2 / (360.0 / (double)deg));
    }

    public static float toRadians(double deg) {
        return (float)(Math.PI * 2 / (360.0 / deg));
    }

    public static float toDegrees(float rad) {
        return (float)(57.29577951308232 * (double)rad);
    }

    public static double toDegrees(double rad) {
        return 57.29577951308232 * rad;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

