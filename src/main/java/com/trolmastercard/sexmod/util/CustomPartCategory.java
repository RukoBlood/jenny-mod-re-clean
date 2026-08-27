/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

public enum CustomPartCategory {
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

    private CustomPartCategory() {
    }

    private CustomPartCategory(int xPos) {
        this.iconXPos = xPos;
    }

    private CustomPartCategory(int xPos, String boneName) {
        this.iconXPos = xPos;
        this.boneName = boneName;
        ++Reference.BUTTON_ID;
        this.buttonIDPlus = Reference.BUTTON_ID++;
        this.buttonIDMinus = Reference.BUTTON_ID;
    }

    public static int getCount() {
        return CustomPartCategory.values().length - 2;
    }
}

