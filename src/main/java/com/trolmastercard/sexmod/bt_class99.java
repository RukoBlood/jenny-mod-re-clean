/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.List;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class bt_class99 extends EntityAIAvoidEntity<EntityPlayer> {
    final ManglelieEntity manglelie;
    final float b;

    public bt_class99(ManglelieEntity entity, float f, double d, double d2) {
        super(entity, EntityPlayer.class, f, d, d2);
        this.manglelie = entity;
        this.b = f;
    }

    boolean a() {
        if (this.manglelie.java_util_UUID_v() != null) {
            return true;
        }
        BlockPos blockPos = this.manglelie.getPosition();
        BlockPos blockPos2 = new BlockPos(this.b, this.b, this.b);
        List<GalathEntity> list = this.manglelie.world.getEntitiesWithinAABB(GalathEntity.class, new AxisAlignedBB(blockPos.add(blockPos2), blockPos.subtract(blockPos2)));
        for (GalathEntity galathEntity : list) {
            if (galathEntity.world.isRemote || galathEntity.isDead || !galathEntity.maybeMountedByMangFn()) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldExecute() {
        if (this.a()) {
            return false;
        }
        return super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.a()) {
            return false;
        }
        return super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        this.manglelie.getDataManager().set(ManglelieEntity.ar, true);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.manglelie.getDataManager().set(ManglelieEntity.ar, false);
        super.resetTask();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

