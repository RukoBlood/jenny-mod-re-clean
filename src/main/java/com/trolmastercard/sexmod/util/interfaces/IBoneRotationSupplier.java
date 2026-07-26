/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.interfaces;

import com.trolmastercard.sexmod.girls.GirlEntity;

@FunctionalInterface
public interface IBoneRotationSupplier {
    public float getRotation(GirlEntity girl);
}

