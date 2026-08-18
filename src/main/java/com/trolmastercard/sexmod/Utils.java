/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class Utils {
    public static Vec3d getVectorToPlayer(Entity entity, EntityPlayer entityPlayer, float f) {
        Vec3d vec3d = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY + (double)entityPlayer.getEyeHeight(), entity.lastTickPosZ), entity.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0), (double)f);
        Vec3d vec3d2 = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(entityPlayer.lastTickPosX, entityPlayer.lastTickPosY, entityPlayer.lastTickPosZ), entityPlayer.getPositionVector(), (double)f);
        return vec3d.subtract(vec3d2);
    }

    public static Vec3d a(Entity entity, EntityPlayer entityPlayer, float f) {
        Vec3d vec3d = Utils.getInterpolatedPosition(entity, f);
        if (entityPlayer == null) {
            return vec3d;
        }
        Vec3d vec3d2 = Utils.getInterpolatedPosition(entityPlayer, f);
        return vec3d.subtract(vec3d2);
    }

    public static Vec3d getInterpolatedPosition(Entity entity, float f) {
        if (!(entity instanceof GirlEntity)) {
            return Utils.b(entity, f);
        }
        GirlEntity em_class2582 = (GirlEntity)entity;
        if (!em_class2582.isAnchored()) {
            return Utils.b(entity, f);
        }
        return em_class2582.getTargetPosition();
    }

    static Vec3d b(Entity entity, float step) {
        return ReferenceAndRotationHelper.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)step);
    }

    public static void a() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

}

