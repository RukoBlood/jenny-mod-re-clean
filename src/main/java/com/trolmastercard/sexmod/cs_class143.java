/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class cs_class143
extends DamageSource {
    GalathEntity a;
    Vec3d b;

    public cs_class143(GalathEntity f__class2972) {
        super("galath");
        this.a = f__class2972;
        this.b = f__class2972.getPositionVector();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBase) {
        return new TextComponentString(entityLivingBase.getName() + " was slain by Galath");
    }

    @Override
    @Nullable
    public Entity getImmediateSource() {
        return this.a;
    }

    @Override
    @Nullable
    public Entity getTrueSource() {
        return this.a;
    }

    @Override
    @Nullable
    public Vec3d getDamageLocation() {
        return this.b;
    }
}

