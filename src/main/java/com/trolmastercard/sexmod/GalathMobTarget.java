/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
//d.class
public class GalathMobTarget {
    public static boolean isValidTarget(Entity e) {
        if (e instanceof EntityCreeper) {
            return false;
        }
        if (e instanceof EntityPigZombie) {
            return false;
        }
        if (e instanceof EntityGuardian) {
            return false;
        }
        return !(e instanceof EntityEnderman);
    }

    public static boolean hasLineOfSight(World world, Vec3d startPos, Entity target) {
        RayTraceResult result = world.rayTraceBlocks(
                startPos,
                target.getPositionVector().add(0.0, target.getEyeHeight(), 0.0),
                true,
                true,
                false
        );
        if (result == null) {
            return true;
        }
        return result.typeOfHit != RayTraceResult.Type.BLOCK;
    }
}

