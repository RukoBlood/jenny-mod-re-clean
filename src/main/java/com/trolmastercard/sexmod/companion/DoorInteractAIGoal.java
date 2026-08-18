/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.companion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public class DoorInteractAIGoal extends EntityAIBase {
    protected EntityLiving entity;
    protected BlockPos doorPos = BlockPos.ORIGIN;
    protected BlockDoor doorBlock;
    boolean hasPassedDoor;
    float initStepX;
    float initStepY;
    int closeDelayTimer = 10;

    public DoorInteractAIGoal(EntityLiving e) {
        this.entity = e;
        if (!(e.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    @Override
    public boolean shouldExecute() {
        boolean noDoorsNearby = true;
        for (int xOffset = -3; xOffset < 5; ++xOffset) {
            for (int zOffset = -3; zOffset < 5; ++zOffset) {
                IBlockState state = this.entity.world.getBlockState(this.entity.getPosition().add(xOffset, 0, zOffset));
                if (!(state.getBlock() instanceof BlockDoor) || state.getMaterial() != Material.WOOD) continue;
                noDoorsNearby = false;
                break;
            }
            if (!noDoorsNearby) break;
        }
        if (noDoorsNearby) {
            return false;
        }


        PathNavigateGround groundNavigator = (PathNavigateGround)this.entity.getNavigator();
        Path currentPath = groundNavigator.getPath();
        if (currentPath != null && !currentPath.isFinished() && groundNavigator.getEnterDoors()) {
            for (int i = 0; i < Math.min(currentPath.getCurrentPathIndex() + 2, currentPath.getCurrentPathLength()); ++i) {
                PathPoint pathPoint = currentPath.getPathPointFromIndex(i);
                this.doorPos = new BlockPos(pathPoint.x, pathPoint.y + 1, pathPoint.z);
                if (!(this.entity.getDistanceSq(this.doorPos.getX(), this.entity.posY, this.doorPos.getZ()) <= 2.25)) continue;
                this.doorBlock = this.getDoorAt(this.doorPos);
                if (this.doorBlock == null) continue;
                return true;
            }
            this.doorPos = new BlockPos(this.entity).up();
            this.doorBlock = this.getDoorAt(this.doorPos);
            return this.doorBlock != null;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.closeDelayTimer >= 0;
    }

    @Override
    public void startExecuting() {
        this.hasPassedDoor = false;
        this.initStepX = (float)((double)((float)this.doorPos.getX() + 0.5f) - this.entity.posX);
        this.initStepY = (float)((double)((float)this.doorPos.getZ() + 0.5f) - this.entity.posZ);
        this.doorBlock.toggleDoor(this.entity.world, this.doorPos, true);
    }

    @Override
    public void updateTask() {
        float curStepZ;
        float curStepX = (float)((double)((float)this.doorPos.getX() + 0.5f) - this.entity.posX);
        float dotProduct = this.initStepX * curStepX + this.initStepY * (curStepZ = (float)((double)((float)this.doorPos.getZ() + 0.5f) - this.entity.posZ));
        if (dotProduct < 0.0f && --this.closeDelayTimer <= 0) {
            this.doorBlock.toggleDoor(this.entity.world, this.doorPos, false);
            this.hasPassedDoor = true;
        }
    }

    @Override
    public void resetTask() {
        this.closeDelayTimer = 10;
    }

    private BlockDoor getDoorAt(BlockPos pos) {
        IBlockState iBlockState = this.entity.world.getBlockState(pos);
        Block block = iBlockState.getBlock();
        return block instanceof BlockDoor && iBlockState.getMaterial() == Material.WOOD ? (BlockDoor)block : null;
    }
}

