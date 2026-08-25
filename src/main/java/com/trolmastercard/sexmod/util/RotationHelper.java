/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class RotationHelper {
    public static Vec3d lerpVec3d(Vec3d start, Vec3d end, int step) {
        if (step == 0) {
            return end;
        }
        Vec3d vec3d3 = end.subtract(start);
        return start.add(vec3d3.x / (double)step, vec3d3.y / (double)step, vec3d3.z / (double)step);
    }

    public static double LerpDouble(double start, double end, double step) {
        return start + (end - start) * step;
    }

    public static float LerpFloat(float start, float end, float step) {
        return start + (end - start) * step;
    }

    public static float lerpFloatAngular(float start, float end, double step) {
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
        return (float)Math.toDegrees(RotationHelper.lerpFloatAngular((float)StartRad, (float)EndRad, step));
    }

    public static Vec3d LerpVec3d(Vec3d start, Vec3d end, double step) {
        Vec3d delta = end.subtract(start);
        return start.add(new Vec3d(delta.x * step, delta.y * step, delta.z * step));
    }

    public static Vector3fSexmodSpecial LerpVector3f(Vector3fSexmodSpecial start, Vector3fSexmodSpecial end, double step) {
        Vector3fSexmodSpecial delta = end.subtract(start);
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

    public static double smoothStep(double t) {
        return -(Math.cos(Math.PI * t) - 1.0) / 2.0;
    }

    public static double easeInQuart(double t) {
        return 1.0 - Math.cos(Math.PI * t / 2.0);
    }

    public static double lerpAngle(double start, double end, double step) {
        double smooth = (1.0 - Math.cos(step * Math.PI)) / 2.0;
        return start * (1.0 - smooth) + end * smooth;
    }
}

