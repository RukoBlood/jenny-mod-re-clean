/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.List;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class bt_class99
extends EntityAIAvoidEntity<EntityPlayer> {
    final ManglelieEntity a;
    final float b;

    public bt_class99(ManglelieEntity f8_class2932, float f, double d, double d2) {
        super(f8_class2932, EntityPlayer.class, f, d, d2);
        this.a = f8_class2932;
        this.b = f;
    }

    boolean a() {
        if (this.a.java_util_UUID_v() != null) {
            return true;
        }
        BlockPos blockPos = this.a.getPosition();
        BlockPos blockPos2 = new BlockPos(this.b, this.b, this.b);
        List<GalathEntity> list = this.a.world.getEntitiesWithinAABB(GalathEntity.class, new AxisAlignedBB(blockPos.add(blockPos2), blockPos.subtract(blockPos2)));
        for (GalathEntity f__class2972 : list) {
            if (f__class2972.world.isRemote || f__class2972.isDead || !f__class2972.maybeMountedByMangFn()) continue;
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
        this.a.getDataManager().set(ManglelieEntity.ar, true);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.a.getDataManager().set(ManglelieEntity.ar, false);
        super.resetTask();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

