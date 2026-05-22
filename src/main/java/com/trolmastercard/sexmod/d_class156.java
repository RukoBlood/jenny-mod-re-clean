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

public class d_class156 {
    public static boolean a(Entity entity) {
        if (entity instanceof EntityCreeper) {
            return false;
        }
        if (entity instanceof EntityPigZombie) {
            return false;
        }
        if (entity instanceof EntityGuardian) {
            return false;
        }
        return !(entity instanceof EntityEnderman);
    }

    public static boolean a(World world, Vec3d vec3d, Entity entity) {
        RayTraceResult rayTraceResult = world.rayTraceBlocks(vec3d, entity.getPositionVector().add(0.0, entity.getEyeHeight(), 0.0), true, true, false);
        if (rayTraceResult == null) {
            return true;
        }
        return rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

