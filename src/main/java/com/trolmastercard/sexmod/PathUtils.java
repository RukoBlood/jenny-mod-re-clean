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

public class PathUtils {
    public static BlockPos getFinalPathPosition(Path path) {
        if (path == null) {
            return BlockPos.ORIGIN;
        }
        PathPoint finalPoint = path.getFinalPathPoint();
        if (finalPoint == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(finalPoint.x, finalPoint.y, finalPoint.z);
    }

    public static BlockPos getFinalPathPosition(EntityLiving entity) {
        PathNavigate navigator = entity.getNavigator();
        Path currentPath = navigator.getPath();
        return PathUtils.getFinalPathPosition(currentPath);
    }

    public static boolean isPathIntersecting(Path path, BlockPos[] targetPositions) {
        int pathLength = path.getCurrentPathLength();
        ArrayList<PathPoint> pathPoints = new ArrayList<PathPoint>();
        for (int i = 0; i < pathLength; ++i) {
            pathPoints.add(path.getPathPointFromIndex(i));
        }
        for (PathPoint pathPoint : pathPoints) {
            for (BlockPos targetPos : targetPositions) {
                if (pathPoint.x != targetPos.getX() || pathPoint.y != targetPos.getY() || pathPoint.z != targetPos.getZ()) continue;
                return true;
            }
        }
        return false;
    }
}

