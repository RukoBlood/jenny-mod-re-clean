/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public class fl_class318 {
    public static BlockPos a(Path path) {
        if (path == null) {
            return BlockPos.ORIGIN;
        }
        PathPoint pathPoint = path.getFinalPathPoint();
        if (pathPoint == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(pathPoint.x, pathPoint.y, pathPoint.z);
    }

    public static BlockPos a(EntityLiving entityLiving) {
        PathNavigate pathNavigate = entityLiving.getNavigator();
        Path path = pathNavigate.getPath();
        return fl_class318.a(path);
    }

    public static boolean a(Path path, BlockPos[] blockPosArray) {
        int n = path.getCurrentPathLength();
        ArrayList<PathPoint> arrayList = new ArrayList<PathPoint>();
        for (int i = 0; i < n; ++i) {
            arrayList.add(path.getPathPointFromIndex(i));
        }
        for (PathPoint pathPoint : arrayList) {
            for (BlockPos blockPos : blockPosArray) {
                if (pathPoint.x != blockPos.getX() || pathPoint.y != blockPos.getY() || pathPoint.z != blockPos.getZ()) continue;
                return true;
            }
        }
        return false;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

