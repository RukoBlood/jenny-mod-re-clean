/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

public class Vector2d {
    public double b;
    public double a;

    public Vector2d(double d, double d2) {
        this.b = d;
        this.a = d2;
    }

    public Vector2d a(Vector2d g8_class3532) {
        return new Vector2d(this.b - g8_class3532.b, this.a - g8_class3532.a);
    }
}

