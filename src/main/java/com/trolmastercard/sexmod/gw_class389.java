/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.util.Reference;

public enum gw_class389 {
    GIRL_SPECIFIC,
    HEAD(0, "customHead"),
    FOOT_L(60, "customShoeL"),
    FOOT_R(80, "customShoeR"),
    HAND_L(100, "customHandL"),
    HAND_R(120, "customHandR"),
    CUSTOM_BONE(140);

    final static public String SEPARATOR = "#";
    public int buttonIDPlus;
    public int buttonIDMinus;
    public String boneName = null;
    public int iconXPos = 0;

    private gw_class389() {
    }

    private gw_class389(int n2) {
        this.iconXPos = n2;
    }

    private gw_class389(int n2, String string2) {
        this.iconXPos = n2;
        this.boneName = string2;
        ++Reference.i;
        this.buttonIDPlus = Reference.i++;
        this.buttonIDMinus = Reference.i;
    }

    public static int a() {
        return gw_class389.values().length - 2;
    }
}

