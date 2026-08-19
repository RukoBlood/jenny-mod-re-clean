package com.trolmastercard.sexmod.girls.Galath;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CumDrainDamageSource extends DamageSource {
    GalathEntity galath;
    Vec3d pos;

    public CumDrainDamageSource(GalathEntity entity) {
        super("galath");
        this.galath = entity;
        this.pos = entity.getPositionVector();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase ent) {
        return new TextComponentString(ent.getName() + " got his cum drained by a Succubus");
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
        return this.galath;
    }

    @Override
    @Nullable
    public Entity getTrueSource() {
        return this.galath;
    }

    @Override
    @Nullable
    public Vec3d getDamageLocation() {
        return this.pos;
    }
}

