/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod.util;

import javax.vecmath.Vector3f;

import net.minecraft.util.math.Vec3d;

public class VectorMath {
    public static Vec3d scale(Vec3d vec3d, double d) {
        return new Vec3d(vec3d.x * d, vec3d.y * d, vec3d.z * d);
    }

    public static double dotProduct(Vector3f vecA, Vec3d vecB) {
        return (double)vecA.x * vecB.x + (double)vecA.y * vecB.y + (double)vecA.z * vecB.z;
    }

    public static double dotProduct(Vec3d vecA, Vec3d vecB) {
        return vecA.x * vecB.x + vecA.y * vecB.y + vecA.z * vecB.z;
    }

    public static Vec3d crossProduct(Vec3d vec3d, Vec3d vec3d2) {
        return new Vec3d(
                vec3d.y * vec3d2.z - vec3d.z * vec3d2.y,
                vec3d.z * vec3d2.x - vec3d.x * vec3d2.z,
                vec3d.x * vec3d2.y - vec3d.y * vec3d2.x
        );
    }

    public static Vec3d RotateY(double x, double y, double z, float yaw) {
        return VectorMath.rotate(new Vec3d(x, y, z), yaw);
    }

    public static Vec3d rotate(Vec3d vec, float yaw) {
        return VectorMath.rotate(vec, 0.0f, yaw);
    }

    public static Vec3d rotate(Vec3d vec, float pitch, float yaw) {
        Vec3d RotatedX = new Vec3d(
                vec.x,
                vec.y * Math.cos((double)pitch * (Math.PI / 180)) - vec.z * Math.sin((double)pitch * (Math.PI / 180)),
                vec.y * Math.sin((double)pitch * (Math.PI / 180)) + vec.z * Math.cos((double)pitch * (Math.PI / 180))
        );

        Vec3d RotatedY = new Vec3d(
                -Math.sin((double)(yaw + 90.0f) * (Math.PI / 180)) * RotatedX.x - Math.sin((double)yaw * (Math.PI / 180)) * RotatedX.z,
                RotatedX.y,
                Math.cos((double)(yaw + 90.0f) * (Math.PI / 180)) * RotatedX.x + Math.cos((double)yaw * (Math.PI / 180)) * RotatedX.z);
        return RotatedY;
    }

    public static Vec3d rotate(double x, double y, double z, float pitch, float yaw) {
        return VectorMath.rotate(new Vec3d(x, y, z), pitch, yaw);
    }

    public static Vec3d rotateEuler(Vec3d vec, float pitch, float yaw, float roll) {
        pitch = TrigMath.wrapDegrees(pitch);
        yaw = TrigMath.wrapDegrees(yaw);
        roll = TrigMath.wrapDegrees(roll);

        double sinP = (float)Math.sin(pitch);
        double cosP = (float)Math.cos(pitch);
        double sinY = (float)Math.sin(yaw);
        double cosY = (float)Math.cos(yaw);
        double sinR = (float)Math.sin(roll);
        double cosR = (float)Math.cos(roll);
        double ty = vec.y * cosP - vec.z * sinP;
        double tz = vec.y * sinP + vec.z * cosP;
        vec = new Vec3d(vec.x, ty, tz);
        double tx = vec.x * cosY + vec.z * sinY;
        tz = -vec.x * sinY + vec.z * cosY;
        vec = new Vec3d(tx, vec.y, tz);
        tx = vec.x * cosR - vec.y * sinR;
        ty = vec.x * sinR + vec.y * cosR;
        return new Vec3d(tx, ty, vec.z);
    }

    public static Vec3d MirrorXZ(Vec3d vec3d) {
        return new Vec3d(-vec3d.x, vec3d.y, -vec3d.z);
    }

    public static Vec3d MirrorXY(Vec3d vec3d) {
        return new Vec3d(-vec3d.x, -vec3d.y, vec3d.z);
    }

    public static Vec3d MirrorYZ(Vec3d vec3d) {
        return new Vec3d(vec3d.x, -vec3d.y, -vec3d.z);
    }

    static double getLinearFactor(double min, double max, double value) {
        return (value - min) / (max - min);
    }

    public static double getLinearFactor(Vec3d min, Vec3d max, Vec3d value) {
        return VectorMath.getLinearFactor(min.x, max.x, value.x);
    }
}

