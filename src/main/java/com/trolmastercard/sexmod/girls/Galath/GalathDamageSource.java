/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Galath;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class GalathDamageSource extends DamageSource {
    GalathEntity galathEntity;
    Vec3d sourceLocation;

    public GalathDamageSource(GalathEntity galathEntity) {
        super("galath");
        this.galathEntity = galathEntity;
        this.sourceLocation = galathEntity.getPositionVector();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        return new TextComponentString(entity.getName() + " was slain by Galath");
    }

    @Override
    @Nullable
    public Entity getImmediateSource() {
        return this.galathEntity;
    }

    @Override
    @Nullable
    public Entity getTrueSource() {
        return this.galathEntity;
    }

    @Override
    @Nullable
    public Vec3d getDamageLocation() {
        return this.sourceLocation;
    }
}

