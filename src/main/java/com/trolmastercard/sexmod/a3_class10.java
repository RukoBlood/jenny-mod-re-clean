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

public class a3_class10
extends DamageSource {
    GalathEntity a;
    Vec3d b;

    public a3_class10(GalathEntity f__class2972) {
        super("galath");
        this.a = f__class2972;
        this.b = f__class2972.getPositionVector();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBase) {
        return new TextComponentString(entityLivingBase.getName() + " got his cum drained by a Succubus");
    }

    @Override
    public boolean isUnblockable() {
        return true;
    }

    @Override
    public boolean canHarmInCreative() {
        return true;
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

