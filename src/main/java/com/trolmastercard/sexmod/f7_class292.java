/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

public class f7_class292 {
    final static public f7_class292 d = new f7_class292(0.0f, 0.0f, 0.0f);
    public float a;
    public float c;
    public float b;

    public f7_class292(float f, float f2, float f3) {
        this.a = f;
        this.c = f2;
        this.b = f3;
    }

    public f7_class292 b(f7_class292 f7_class2922) {
        return new f7_class292(this.a - f7_class2922.a, this.c - f7_class2922.c, this.b - f7_class2922.b);
    }

    public f7_class292 a(f7_class292 f7_class2922) {
        return new f7_class292(this.a + f7_class2922.a, this.c + f7_class2922.c, this.b + f7_class2922.b);
    }

    public f7_class292 a(float f) {
        return new f7_class292(this.a * f, this.c * f, this.b * f);
    }
}

