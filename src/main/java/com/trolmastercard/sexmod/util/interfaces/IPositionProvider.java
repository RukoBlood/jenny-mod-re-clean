/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.interfaces;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface IPositionProvider {
    public Vec3d getPosition(GirlEntity girl);
}

