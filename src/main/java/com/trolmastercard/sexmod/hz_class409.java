/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

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

public class hz_class409
extends EntityAIBase {
    protected EntityLiving c;
    protected BlockPos b = BlockPos.ORIGIN;
    protected BlockDoor d;
    boolean e;
    float f;
    float a;
    int g = 10;

    public hz_class409(EntityLiving entityLiving) {
        this.c = entityLiving;
        if (!(entityLiving.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    @Override
    public boolean shouldExecute() {
        boolean bl = true;
        for (int i = -3; i < 5; ++i) {
            for (int j = -3; j < 5; ++j) {
                IBlockState iBlockState = this.c.world.getBlockState(this.c.getPosition().add(i, 0, j));
                if (!(iBlockState.getBlock() instanceof BlockDoor) || iBlockState.getMaterial() != Material.WOOD) continue;
                bl = false;
                break;
            }
            if (!bl) break;
        }
        if (bl) {
            return false;
        }
        PathNavigateGround pathNavigateGround = (PathNavigateGround)this.c.getNavigator();
        Path path = pathNavigateGround.getPath();
        if (path != null && !path.isFinished() && pathNavigateGround.getEnterDoors()) {
            for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); ++i) {
                PathPoint pathPoint = path.getPathPointFromIndex(i);
                this.b = new BlockPos(pathPoint.x, pathPoint.y + 1, pathPoint.z);
                if (!(this.c.getDistanceSq(this.b.getX(), this.c.posY, this.b.getZ()) <= 2.25)) continue;
                this.d = this.a(this.b);
                if (this.d == null) continue;
                return true;
            }
            this.b = new BlockPos(this.c).up();
            this.d = this.a(this.b);
            return this.d != null;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.g >= 0;
    }

    @Override
    public void startExecuting() {
        this.e = false;
        this.f = (float)((double)((float)this.b.getX() + 0.5f) - this.c.posX);
        this.a = (float)((double)((float)this.b.getZ() + 0.5f) - this.c.posZ);
        this.d.toggleDoor(this.c.world, this.b, true);
    }

    @Override
    public void updateTask() {
        float f;
        float f2 = (float)((double)((float)this.b.getX() + 0.5f) - this.c.posX);
        float f3 = this.f * f2 + this.a * (f = (float)((double)((float)this.b.getZ() + 0.5f) - this.c.posZ));
        if (f3 < 0.0f && --this.g <= 0) {
            this.d.toggleDoor(this.c.world, this.b, false);
            this.e = true;
        }
    }

    @Override
    public void resetTask() {
        this.g = 10;
    }

    private BlockDoor a(BlockPos blockPos) {
        IBlockState iBlockState = this.c.world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return block instanceof BlockDoor && iBlockState.getMaterial() == Material.WOOD ? (BlockDoor)block : null;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

