/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

public class AnimationStateHolder {
    public double pitch;
    public double roll;
    public double prevPitch;
    public double prevRoll;

    public AnimationStateHolder(double pitch, double roll, double prevPitch, double prevRoll) {
        this.pitch = pitch;
        this.roll = roll;
        this.prevPitch = prevPitch;
        this.prevRoll = prevRoll;
    }
}

