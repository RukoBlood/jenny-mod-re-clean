/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.companion;

import java.util.List;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AvoidPlayerGoal extends EntityAIAvoidEntity<EntityPlayer> {
    final ManglelieEntity manglelie;
    final float distance;

    public AvoidPlayerGoal(ManglelieEntity manglelie, float distance, double farSpeed, double nearSpeed) {
        super(manglelie, EntityPlayer.class, distance, farSpeed, nearSpeed);
        this.manglelie = manglelie;
        this.distance = distance;
    }

    boolean shouldAvoid() {
        if (this.manglelie.getCorruptPlayerUUID() != null) {
            return true;
        }

        BlockPos pos = this.manglelie.getPosition();
        BlockPos offset = new BlockPos(this.distance, this.distance, this.distance);
        List<GalathEntity> galaths = this.manglelie.world.getEntitiesWithinAABB(GalathEntity.class, new AxisAlignedBB(pos.add(offset), pos.subtract(offset)));

        for (GalathEntity galathEntity : galaths) {
            if (!galathEntity.world.isRemote && !galathEntity.isDead && galathEntity.hasMasterOAlgo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldExecute() {
        return !this.shouldAvoid() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.shouldAvoid() && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        this.manglelie.getDataManager().set(ManglelieEntity.IS_SCARED, true);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.manglelie.getDataManager().set(ManglelieEntity.IS_SCARED, false);
        super.resetTask();
    }

}