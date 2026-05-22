/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.gc_class360;
import javax.vecmath.Vector3f;
import net.minecraft.util.math.Vec3d;

public class ck_class135 {
    public static Vec3d a(Vec3d vec3d, double d) {
        return new Vec3d(vec3d.x * d, vec3d.y * d, vec3d.z * d);
    }

    public static double a(Vector3f vector3f, Vec3d vec3d) {
        return (double)vector3f.x * vec3d.x + (double)vector3f.y * vec3d.y + (double)vector3f.z * vec3d.z;
    }

    public static double a(Vec3d vec3d, Vec3d vec3d2) {
        return vec3d.x * vec3d2.x + vec3d.y * vec3d2.y + vec3d.z * vec3d2.z;
    }

    public static Vec3d b(Vec3d vec3d, Vec3d vec3d2) {
        return new Vec3d(vec3d.y * vec3d2.z - vec3d.z * vec3d2.y, vec3d.z * vec3d2.x - vec3d.x * vec3d2.z, vec3d.x * vec3d2.y - vec3d.y * vec3d2.x);
    }

    public static Vec3d a(double d, double d2, double d3, float f) {
        return ck_class135.a(new Vec3d(d, d2, d3), f);
    }

    public static Vec3d a(Vec3d vec3d, float f) {
        return ck_class135.a(vec3d, 0.0f, f);
    }

    public static Vec3d a(Vec3d vec3d, float f, float f2) {
        Vec3d vec3d2 = new Vec3d(vec3d.x, vec3d.y * Math.cos((double)f * (Math.PI / 180)) - vec3d.z * Math.sin((double)f * (Math.PI / 180)), vec3d.y * Math.sin((double)f * (Math.PI / 180)) + vec3d.z * Math.cos((double)f * (Math.PI / 180)));
        Vec3d vec3d3 = new Vec3d(-Math.sin((double)(f2 + 90.0f) * (Math.PI / 180)) * vec3d2.x - Math.sin((double)f2 * (Math.PI / 180)) * vec3d2.z, vec3d2.y, Math.cos((double)(f2 + 90.0f) * (Math.PI / 180)) * vec3d2.x + Math.cos((double)f2 * (Math.PI / 180)) * vec3d2.z);
        return vec3d3;
    }

    public static Vec3d a(double d, double d2, double d3, float f, float f2) {
        return ck_class135.a(new Vec3d(d, d2, d3), f, f2);
    }

    public static Vec3d a(Vec3d vec3d, float f, float f2, float f3) {
        f = gc_class360.c(f);
        f2 = gc_class360.c(f2);
        f3 = gc_class360.c(f3);
        double d = (float)Math.sin(f);
        double d2 = (float)Math.cos(f);
        double d3 = (float)Math.sin(f2);
        double d4 = (float)Math.cos(f2);
        double d5 = (float)Math.sin(f3);
        double d6 = (float)Math.cos(f3);
        double d7 = vec3d.y * d2 - vec3d.z * d;
        double d8 = vec3d.y * d + vec3d.z * d2;
        vec3d = new Vec3d(vec3d.x, d7, d8);
        double d9 = vec3d.x * d4 + vec3d.z * d3;
        d8 = -vec3d.x * d3 + vec3d.z * d4;
        vec3d = new Vec3d(d9, vec3d.y, d8);
        d9 = vec3d.x * d6 - vec3d.y * d5;
        d7 = vec3d.x * d5 + vec3d.y * d6;
        return new Vec3d(d9, d7, vec3d.z);
    }

    public static Vec3d c(Vec3d vec3d) {
        return new Vec3d(-vec3d.x, vec3d.y, -vec3d.z);
    }

    public static Vec3d a(Vec3d vec3d) {
        return new Vec3d(-vec3d.x, -vec3d.y, vec3d.z);
    }

    public static Vec3d b(Vec3d vec3d) {
        return new Vec3d(vec3d.x, -vec3d.y, -vec3d.z);
    }

    static double a(double d, double d2, double d3) {
        return (d3 - d) / (d2 - d);
    }

    public static double a(Vec3d vec3d, Vec3d vec3d2, Vec3d vec3d3) {
        return ck_class135.a(vec3d.x, vec3d2.x, vec3d3.x);
    }
}

