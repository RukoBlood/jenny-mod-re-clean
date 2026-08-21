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
    public void a(RenderWorldLastEvent renderWorldLastEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }
        UUID uUID = minecraft.player.getPersistentID();
        GirlEntity girlEntity = null;
        for (GirlEntity entity : GirlEntity.getGirlEntityList()) {
            IGoblin ai_class302;
            if (entity == null || entity.isDead || !entity.world.isRemote || !(entity instanceof IGoblin) || !uUID.equals((ai_class302 = (IGoblin) ((Object) entity)).getOwnerUUID()))
                continue;
            girlEntity = entity;
            break;
        }
        if (girlEntity == null) {
            return;
        }
        Render render = minecraft.getRenderManager().getEntityRenderObject(girlEntity);
        if (render == null) {
            return;
        }
        float f = minecraft.player.rotationYaw;
        GoblinRenderer.N = (float) ((double) minecraft.player.movementInput.moveStrafe * GoblinRenderer.G.x);
        GoblinRenderer.N += -(f - GoblinRenderer.H) * 3.0f;
        GoblinRenderer.N = RotationHelper.LerpFloat(GoblinRenderer.I, GoblinRenderer.N, 0.1f);
        float f2 = -minecraft.player.rotationPitch;
        GoblinRenderer.x = (float) ((double) minecraft.player.movementInput.moveForward * GoblinRenderer.G.z + (double) ((float) minecraft.player.motionY) * GoblinRenderer.G.y);
        GoblinRenderer.x += -(f2 - GoblinRenderer.t) * 3.0f;
        GoblinRenderer.x = RotationHelper.LerpFloat(GoblinRenderer.E, GoblinRenderer.x, 0.1f);
        GoblinRenderer.getInterpolatedYaw(girlEntity, renderWorldLastEvent.getPartialTicks());
        GoblinRenderer.H = f;
        GoblinRenderer.I = GoblinRenderer.N;
        GoblinRenderer.t = f2;
        GoblinRenderer.E = GoblinRenderer.x;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void b(RenderWorldLastEvent renderWorldLastEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player == null) {
            return;
        }
        UUID uUID = minecraft.player.getPersistentID();
        for (GirlEntity girlEntity : GirlEntity.getGirlEntityList()) {
            if (!girlEntity.world.isRemote || girlEntity.isDead || !(girlEntity instanceof IGoblin)) continue;
            IGoblin ai_class302 = (IGoblin) ((Object) girlEntity);
            if (girlEntity.getCurrentAction() != Action.START_THROWING) continue;
            girlEntity.setLocallyRegistered(true);
            minecraft.getRenderManager().renderEntity(girlEntity, 0.0, 0.0, 0.0, uUID.equals(ai_class302.getOwnerUUID()) ? -420.69f : 0.0f, minecraft.getRenderPartialTicks(), false);
            girlEntity.setLocallyRegistered(false);
            return;
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderHandEvent renderHandEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        UUID uUID = minecraft.player.getPersistentID();
        for (GirlEntity em_class2582 : GirlEntity.getGirlEntityList()) {
            IGoblin ai_class302;
            UUID uUID2;
            Action fp_class3242;
            if (!(em_class2582 instanceof IGoblin) || (fp_class3242 = em_class2582.getCurrentAction()) != Action.PICK_UP && fp_class3242 != Action.START_THROWING || !uUID.equals(uUID2 = (ai_class302 = (IGoblin) ((Object) em_class2582)).getOwnerUUID()))
                continue;
            renderHandEvent.setCanceled(true);
            break;
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderPlayerEvent.Pre pre) {
        UUID uUID = pre.getEntityPlayer().getPersistentID();
        for (GirlEntity girlEntity : GirlEntity.getGirlEntityList()) {
            if (!(girlEntity instanceof IGoblin)) continue;
            IGoblin ai_class302 = (IGoblin) ((Object) girlEntity);
            Action action = girlEntity.getCurrentAction();
            if (action != Action.PICK_UP && action != Action.START_THROWING || !uUID.equals(ai_class302.getOwnerUUID()))
                continue;
            pre.setCanceled(true);
            break;
        }
    }
}

