/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.interfaces;

import com.trolmastercard.sexmod.util.AnimationStateHolder;

public interface IWingsOwner {
    public AnimationStateHolder getWingAnimationState();

    public boolean hasWingState();

    public boolean isWingsVisible();

    public boolean isWingsAnimated();
}

