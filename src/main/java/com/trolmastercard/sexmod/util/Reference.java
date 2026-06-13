/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import com.trolmastercard.sexmod.Vector3f;
import com.trolmastercard.sexmod.ColorRGBA;
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

    public static double LerpDouble(double start, double end, double step) {
        return start + (end - start) * step;
    }

    public static float LerpFloat(float start, float end, float step) {
        return start + (end - start) * step;
    }

    public static float a(float start, float end, double step) {
        float delta = end - start;
        while ((double)delta < -Math.PI) {
            delta = (float)((double)delta + Math.PI * 2);
        }
        while ((double)delta >= Math.PI) {
            delta = (float)((double)delta - Math.PI * 2);
        }
        return (float)((double)start + (double)delta * step);
    }

    public static float LerpAngleDegrees(float start, float end, double step) {
        double StartRad = Math.toRadians(start);
        double EndRad = Math.toRadians(end);
        return (float)Math.toDegrees(Reference.a((float)StartRad, (float)EndRad, step));
    }

    public static Vec3d LerpVec3d(Vec3d start, Vec3d end, double step) {
        Vec3d delta = end.subtract(start);
        return start.add(new Vec3d(delta.x * step, delta.y * step, delta.z * step));
    }

    public static Vector3f LerpVector3f(Vector3f start, Vector3f end, double step) {
        Vector3f delta = end.subtract(start);
        return start.add(delta.scale((float)step));
    }

    public static Vec3i LerpVec3i(Vec3i start, Vec3i end, double step) {
        Vec3d delta = new Vec3d(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
        return new Vec3i((double)start.getX() + delta.x * step, (double)start.getY() + delta.y * step, (double)start.getZ() + delta.z * step);
    }

    public static ColorRGBA LerpColorRGBA(ColorRGBA start, ColorRGBA end, double step) {
        ColorRGBA delta = new ColorRGBA(end.r - start.r, end.g - start.g, end.b - start.b, end.a - start.a);
        return new ColorRGBA((int)((double)start.r + (double)delta.r * step), (int)((double)start.g + (double)delta.g * step), (int)((double)start.b + (double)delta.b * step), (int)((double)start.a + (double)delta.a * step));
    }

    public static double EaseOutQuart(double value) {
        return 1.0 - Math.pow(1.0 - value, 4.0);
    }

    public static double EaseOutCubic(double d) {
        return 1.0 - Math.pow(1.0 - d, 3.0);
    }

    public static double EaseOutBack(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return 1.0 + d3 * Math.pow(d - 1.0, 3.0) + d2 * Math.pow(d - 1.0, 2.0);
    }

    public static double EaseInBack(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return d3 * d * d * d - d2 * d * d;
    }

    public static double EaseOutSine(double d) {
        return Math.sin(d * Math.PI / 2.0);
    }

    public static double EaseInCubic(double d) {
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

