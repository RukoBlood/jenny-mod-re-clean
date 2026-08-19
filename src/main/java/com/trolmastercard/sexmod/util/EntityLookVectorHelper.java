/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityLookVectorHelper {
    public static Vec3d getLookVectorTo(Entity entity, EntityPlayer player, float partialTicks) {
        Vec3d entityPos = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY + (double)player.getEyeHeight(), entity.lastTickPosZ), entity.getPositionVector().add(0.0, player.getEyeHeight(), 0.0), partialTicks);
        Vec3d playerPos = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), partialTicks);
        return entityPos.subtract(playerPos);
    }

    public static Vec3d getAimVector(Entity entity, EntityPlayer player, float partialTicks) {
        Vec3d lookVec = getInterpolatedPosition(entity, partialTicks);
        if (player == null) {
            return lookVec;
        }

        Vec3d playerLookVec = getInterpolatedPosition(player, partialTicks);
        return lookVec.subtract(playerLookVec);
    }

    public static Vec3d getInterpolatedPosition(Entity entity, float partialTicks) {
        if (!(entity instanceof GirlEntity)) {
            return getLookVectorYaw(entity, partialTicks);
        }

        GirlEntity girl = (GirlEntity)entity;
        return !girl.isAnchored() ? getLookVectorYaw(entity, partialTicks) : girl.getTargetPosition();
    }

    static Vec3d getLookVectorYaw(Entity entity, float partialTicks) {
        return ReferenceAndRotationHelper.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)partialTicks);
    }

    public static void setFullbrightLightMap() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

}

