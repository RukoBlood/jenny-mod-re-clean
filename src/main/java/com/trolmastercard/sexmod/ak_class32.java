/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ak_class32 {
    public static Vec3d b(Entity entity, EntityPlayer entityPlayer, float f) {
        Vec3d vec3d = b6_class67.a(new Vec3d(entity.lastTickPosX, entity.lastTickPosY + (double)entityPlayer.getEyeHeight(), entity.lastTickPosZ), entity.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0), (double)f);
        Vec3d vec3d2 = b6_class67.a(new Vec3d(entityPlayer.lastTickPosX, entityPlayer.lastTickPosY, entityPlayer.lastTickPosZ), entityPlayer.getPositionVector(), (double)f);
        return vec3d.subtract(vec3d2);
    }

    public static Vec3d a(Entity entity, EntityPlayer entityPlayer, float f) {
        Vec3d vec3d = ak_class32.a(entity, f);
        if (entityPlayer == null) {
            return vec3d;
        }
        Vec3d vec3d2 = ak_class32.a(entityPlayer, f);
        return vec3d.subtract(vec3d2);
    }

    public static Vec3d a(Entity entity, float f) {
        if (!(entity instanceof GirlEntity)) {
            return ak_class32.b(entity, f);
        }
        GirlEntity em_class2582 = (GirlEntity)entity;
        if (!em_class2582.boolean_Q()) {
            return ak_class32.b(entity, f);
        }
        return em_class2582.net_minecraft_util_math_Vec3d_o();
    }

    static Vec3d b(Entity entity, float f) {
        return b6_class67.a(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)f);
    }

    public static void a() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

