/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

public class Vector3fSexmodSpecial {
    final static public Vector3fSexmodSpecial ZERO = new Vector3fSexmodSpecial(0.0f, 0.0f, 0.0f);
    public float x;
    public float y;
    public float z;

    public Vector3fSexmodSpecial(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3fSexmodSpecial subtract(Vector3fSexmodSpecial other) {
        return new Vector3fSexmodSpecial(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3fSexmodSpecial add(Vector3fSexmodSpecial other) {
        return new Vector3fSexmodSpecial(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3fSexmodSpecial scale(float mul) {
        return new Vector3fSexmodSpecial(this.x * mul, this.y * mul, this.z * mul);
    }
}

