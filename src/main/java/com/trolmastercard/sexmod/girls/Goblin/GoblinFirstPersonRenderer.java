/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.girls.Goblin;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.util.interfaces.IGoblin;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GoblinFirstPersonRenderer {
    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.thirdPersonView == 0) {
            UUID uUID = mc.player.getPersistentID();
            GirlEntity girlEntity = null;
            for (GirlEntity entity : GirlEntity.getGirlEntityList()) {
                if (entity != null && !entity.isDead && entity.world.isRemote && entity instanceof IGoblin && uUID.equals(((IGoblin) entity).getOwnerUUID())) {
                    girlEntity = entity;
                    break;
                }
            }
            if (girlEntity != null) {
                Render render = mc.getRenderManager().getEntityRenderObject(girlEntity);
                if (render != null) {
                    float yaw = mc.player.rotationYaw;
                    GoblinRenderer.strafeRotation = (float) ((double) mc.player.movementInput.moveStrafe * GoblinRenderer.MOVEMENT_DIR_VECTOR.x);
                    GoblinRenderer.strafeRotation += -(yaw - GoblinRenderer.lastPlayerYaw) * 3.0f;
                    GoblinRenderer.strafeRotation = RotationHelper.LerpFloat(GoblinRenderer.prevStrafeRotation, GoblinRenderer.strafeRotation, 0.1f);
                    float f2 = -mc.player.rotationPitch;
                    GoblinRenderer.forwardRotation = (float) ((double) mc.player.movementInput.moveForward * GoblinRenderer.MOVEMENT_DIR_VECTOR.z + (double) ((float) mc.player.motionY) * GoblinRenderer.MOVEMENT_DIR_VECTOR.y);
                    GoblinRenderer.forwardRotation += -(f2 - GoblinRenderer.lastPlayerPitch) * 3.0f;
                    GoblinRenderer.forwardRotation = RotationHelper.LerpFloat(GoblinRenderer.prevForwardRotation, GoblinRenderer.forwardRotation, 0.1f);
                    GoblinRenderer.getInterpolatedYaw(girlEntity, event.getPartialTicks());
                    GoblinRenderer.lastPlayerYaw = yaw;
                    GoblinRenderer.prevStrafeRotation = GoblinRenderer.strafeRotation;
                    GoblinRenderer.lastPlayerPitch = f2;
                    GoblinRenderer.prevForwardRotation = GoblinRenderer.forwardRotation;
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    GlStateManager.enableAlpha();
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            UUID uUID = mc.player.getPersistentID();
            for (GirlEntity girlEntity : GirlEntity.getGirlEntityList()) {
                if (girlEntity.world.isRemote && !girlEntity.isDead && girlEntity instanceof IGoblin) {
                    IGoblin goblin = (IGoblin) girlEntity;
                    if (girlEntity.getCurrentAction() != Action.START_THROWING) continue;
                    girlEntity.setLocallyRegistered(true);
                    mc.getRenderManager().renderEntity(girlEntity, 0.0, 0.0, 0.0, uUID.equals(goblin.getOwnerUUID()) ? -420.69f : 0.0f, mc.getRenderPartialTicks(), false);
                    girlEntity.setLocallyRegistered(false);
                    return;
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        UUID uUID = mc.player.getPersistentID();
        for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
            Action action;
            if (girl instanceof IGoblin && ((action = girl.getCurrentAction()) == Action.PICK_UP || action == Action.START_THROWING) && uUID.equals(((IGoblin) girl).getOwnerUUID())) {
                event.setCanceled(true);
                break;
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        UUID uUID = event.getEntityPlayer().getPersistentID();
        for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
            if (girl instanceof IGoblin) {
                IGoblin goblin = (IGoblin) girl;
                Action action = girl.getCurrentAction();
                if ((action == Action.PICK_UP || action == Action.START_THROWING) && uUID.equals(goblin.getOwnerUUID())) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }
}

