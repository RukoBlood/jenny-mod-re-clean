/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class TrailSegment {
    final static public float GRAVITY = 9.81f;
    final static public float TIME_DELTA = 0.05f;
    final static public float AIR_RESISTANCE = 0.05f;
    final static public float COLLISION_OFFSET = 0.03f;
    World world;
    Vec3d prevPosition;
    Vec3d currentPosition;
    Vec3d velocity;

    public TrailSegment(World world, Vec3d initialPos, Vec3d initialVelocity) {
        this.world = world;
        this.currentPosition = initialPos;
        this.prevPosition = initialPos;
        this.velocity = initialVelocity;
    }

    public void onUpdate() {
        int LastZ;
        int LastY;
        int LastX;

        if (Vec3d.ZERO.equals(this.velocity)) {
            this.prevPosition = this.currentPosition;
            return;
        }

        this.velocity = new Vec3d(
                this.velocity.x * (double)(1 - AIR_RESISTANCE),
                (this.velocity.y - (GRAVITY * TIME_DELTA)) * (double)(1 - AIR_RESISTANCE),
                this.velocity.z * (double)(1 - AIR_RESISTANCE)
        );

        this.prevPosition = this.currentPosition;
        this.currentPosition = new Vec3d(
                this.currentPosition.x + this.velocity.x * (double)TIME_DELTA,
                this.currentPosition.y + this.velocity.y * (double)TIME_DELTA,
                this.currentPosition.z + this.velocity.z * (double)TIME_DELTA
        );

        BlockPos lastAirBlockPos = new BlockPos(this.prevPosition);
        Vec3i collidedBlockPos = null;

        for (BlockPos stepPos : TrailSegment.calculateLineBlockPositions(new BlockPos(this.prevPosition), new BlockPos(this.currentPosition))) {
            if (this.world.getBlockState(stepPos).getBlock() == Blocks.AIR) {
                lastAirBlockPos = stepPos;
                continue;
            }
            collidedBlockPos = stepPos;
            break;
        }

        if (collidedBlockPos == null) {
            return;
        }

        int blockX = collidedBlockPos.getX();
        if (blockX - (LastX = lastAirBlockPos.getX()) != 0) {
            double borderX = Math.max(blockX, LastX);
            double slopeY = (this.prevPosition.y - this.currentPosition.y) / (this.prevPosition.x - this.currentPosition.x);
            double interceptY = this.currentPosition.y - slopeY * this.currentPosition.x;
            double intersectY = slopeY * borderX + interceptY;
            double slopeZ = (this.prevPosition.z - this.currentPosition.z) / (this.prevPosition.x - this.currentPosition.x);
            double interceptZ = this.currentPosition.z - slopeZ * this.currentPosition.x;
            double intersectZ = slopeZ * borderX + interceptZ;
            this.currentPosition = new Vec3d(borderX + (double)(COLLISION_OFFSET * (float)(blockX > LastX ? -1 : 1)), intersectY, intersectZ);
            this.velocity = new Vec3d(0.0, 0.0, 0.0);
            return;
        }

        int blockY = collidedBlockPos.getY();
        if (blockY - (LastY = lastAirBlockPos.getY()) != 0) {
            double borderY = Math.max(blockY, LastY);
            double slopeX = (this.prevPosition.x - this.currentPosition.x) / (this.prevPosition.y - this.currentPosition.y);
            double interceptX = this.currentPosition.x - slopeX * this.currentPosition.y;
            double intersectX = slopeX * borderY + interceptX;
            double slopeZ = (this.prevPosition.z - this.currentPosition.z) / (this.prevPosition.y - this.currentPosition.y);
            double interceptZ = this.currentPosition.z - slopeZ * this.currentPosition.y;
            double intersectZ = slopeZ * borderY + interceptZ;
            this.currentPosition = new Vec3d(intersectX, borderY + (double)(COLLISION_OFFSET * (float)(blockY > LastY ? -1 : 1)), intersectZ);
            this.velocity = new Vec3d(0.0, 0.0, 0.0);
            return;
        }

        int blockZ = collidedBlockPos.getZ();
        if (blockZ - (LastZ = lastAirBlockPos.getZ()) != 0) {
            double borderZ = Math.max(blockZ, LastZ);
            double SlopeY = (this.prevPosition.y - this.currentPosition.y) / (this.prevPosition.z - this.currentPosition.z);
            double interceptY = this.currentPosition.y - SlopeY * this.currentPosition.z;
            double intersectY = SlopeY * borderZ + interceptY;
            double SlopeX = (this.prevPosition.x - this.currentPosition.x) / (this.prevPosition.z - this.currentPosition.z);
            double interceptX = this.currentPosition.x - SlopeX * this.currentPosition.z;
            double intersectX = SlopeX * borderZ + interceptX;
            this.currentPosition = new Vec3d(intersectX, intersectY, borderZ + (double)(COLLISION_OFFSET * (float)(blockZ > LastZ ? -1 : 1)));
            this.velocity = new Vec3d(0.0, 0.0, 0.0);
            return;
        }
    }

    static List<BlockPos> calculateLineBlockPositions(BlockPos start, BlockPos end) {
        ArrayList<BlockPos> lineBlocks = new ArrayList<BlockPos>();
        lineBlocks.add(start);

        int x1 = start.getX(); int y1 = start.getY(); int z1 = start.getZ();
        int x2 = end.getX(); int y2 = end.getY(); int z2 = end.getZ();

        int dx = Math.abs(x2 - x1); int dy = Math.abs(y2 - y1); int dz = Math.abs(z2 - z1);

        int stepX = x1 < x2 ? 1 : -1; int stepY = y1 < y2 ? 1 : -1; int stepZ = z1 < z2 ? 1 : -1;

        int maxDelta = Math.max(dx, Math.max(dy, dz));

        int curX = x1; int curY = y1; int curZ = z1;
        int errX = maxDelta / 2; int errY = maxDelta / 2; int errZ = maxDelta / 2;

        for (int i = 0; i < maxDelta; ++i) {
            lineBlocks.add(new BlockPos(curX, curY, curZ));
            errY -= dy;
            errZ -= dz;
            if ((errX -= dx) < 0) {
                curX += stepX;
                errX += maxDelta;
                continue;
            }
            if (errY < 0) {
                curY += stepY;
                errY += maxDelta;
                continue;
            }
            if (errZ >= 0) continue;
            curZ += stepZ;
            errZ += maxDelta;
        }

        lineBlocks.add(end);
        return lineBlocks;
    }
}

