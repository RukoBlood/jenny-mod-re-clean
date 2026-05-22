/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

public class e1_class217 {
    final static public e1_class217 a = new e1_class217(0, 0);
    public int c;
    public int b;

    public e1_class217(int n, int n2) {
        this.c = n;
        this.b = n2;
    }

    public float a(int n, int n2) {
        float f = n - this.c;
        float f2 = n2 - this.b;
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    public String toString() {
        return String.format("(%s, %s)", this.c, this.b);
    }
}

