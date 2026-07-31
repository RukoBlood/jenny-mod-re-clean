/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

public class Vector3fColor {
    final static public Vector3fColor ZERO = new Vector3fColor(0.0f, 0.0f, 0.0f);
    public float x;
    public float y;
    public float z;

    public Vector3fColor(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3fColor subtract(Vector3fColor other) {
        return new Vector3fColor(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3fColor add(Vector3fColor other) {
        return new Vector3fColor(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3fColor scale(float mul) {
        return new Vector3fColor(this.x * mul, this.y * mul, this.z * mul);
    }
}

