/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import com.trolmastercard.sexmod.f7_class292;
import com.trolmastercard.sexmod.gv_class388;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

public class Reference {
    final static public String MOD_ID = "sexmod";
    final static public String NAME = "Fapcraft";
    final static public String VERSION = "1.1.0";
    final static public String CLIENT = "com.trolmastercard.sexmod.proxy.ClientProxy";
    final static public String COMMON = "com.trolmastercard.sexmod.proxy.CommonProxy";
    final static public Random RANDOM = new Random();
    static public int b = 0;
    static public int i = 0;
    final static public int a = 4674237;
    final static public int e = 6281823;
    static public Vec3d j = Vec3d.ZERO;
    static public Vec3d k = Vec3d.ZERO;

    public static Vec3d a(Vec3d vec3d, Vec3d vec3d2, int n) {
        if (n == 0) {
            return vec3d2;
        }
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        return vec3d.add(vec3d3.x / (double)n, vec3d3.y / (double)n, vec3d3.z / (double)n);
    }

    public static double Lerp(double start, double end, double step) {
        return start + (end - start) * step;
    }

    public static float Lerp(float start, float end, float step) {
        return start + (end - start) * step;
    }

    public static float a(float f, float f2, double d) {
        float f3 = f2 - f;
        while ((double)f3 < -Math.PI) {
            f3 = (float)((double)f3 + Math.PI * 2);
        }
        while ((double)f3 >= Math.PI) {
            f3 = (float)((double)f3 - Math.PI * 2);
        }
        return (float)((double)f + (double)f3 * d);
    }

    public static float b(float f, float f2, double d) {
        double d2 = Math.toRadians(f);
        double d3 = Math.toRadians(f2);
        return (float)Math.toDegrees(Reference.a((float)d2, (float)d3, d));
    }

    public static Vec3d a(Vec3d vec3d, Vec3d vec3d2, double d) {
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        return vec3d.add(new Vec3d(vec3d3.x * d, vec3d3.y * d, vec3d3.z * d));
    }

    public static f7_class292 a(f7_class292 f7_class2922, f7_class292 f7_class2923, double d) {
        f7_class292 f7_class2924 = f7_class2923.b(f7_class2922);
        return f7_class2922.a(f7_class2924.a((float)d));
    }

    public static Vec3i a(Vec3i vec3i, Vec3i vec3i2, double d) {
        Vec3d vec3d = new Vec3d(vec3i2.getX() - vec3i.getX(), vec3i2.getY() - vec3i.getY(), vec3i2.getZ() - vec3i.getZ());
        return new Vec3i((double)vec3i.getX() + vec3d.x * d, (double)vec3i.getY() + vec3d.y * d, (double)vec3i.getZ() + vec3d.z * d);
    }

    public static gv_class388 a(gv_class388 gv_class3882, gv_class388 gv_class3883, double d) {
        gv_class388 gv_class3884 = new gv_class388(gv_class3883.a - gv_class3882.a, gv_class3883.d - gv_class3882.d, gv_class3883.c - gv_class3882.c, gv_class3883.b - gv_class3882.b);
        return new gv_class388((int)((double)gv_class3882.a + (double)gv_class3884.a * d), (int)((double)gv_class3882.d + (double)gv_class3884.d * d), (int)((double)gv_class3882.c + (double)gv_class3884.c * d), (int)((double)gv_class3882.b + (double)gv_class3884.b * d));
    }

    public static double e(double d) {
        return 1.0 - Math.pow(1.0 - d, 4.0);
    }

    public static double g(double d) {
        return 1.0 - Math.pow(1.0 - d, 3.0);
    }

    public static double c(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return 1.0 + d3 * Math.pow(d - 1.0, 3.0) + d2 * Math.pow(d - 1.0, 2.0);
    }

    public static double d(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return d3 * d * d * d - d2 * d * d;
    }

    public static double b(double d) {
        return Math.sin(d * Math.PI / 2.0);
    }

    public static double a(double d) {
        return d * d * d;
    }

    public static double h(double d) {
        return -(Math.cos(Math.PI * d) - 1.0) / 2.0;
    }

    public static double f(double d) {
        return 1.0 - Math.cos(Math.PI * d / 2.0);
    }

    public static double a(double d, double d2, double d3) {
        double d4 = (1.0 - Math.cos(d3 * Math.PI)) / 2.0;
        return d * (1.0 - d4) + d2 * d4;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

