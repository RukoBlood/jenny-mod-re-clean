/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.ConcurrentModificationException;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class am_class34 {
    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderWorldLastEvent renderWorldLastEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }
        UUID uUID = minecraft.player.getPersistentID();
        GirlEntity em_class2582 = null;
        try {
            for (GirlEntity em_class2583 : GirlEntity.GirlEntityList()) {
                ai_class30 ai_class302;
                if (em_class2583 == null || em_class2583.isDead || !em_class2583.world.isRemote || !(em_class2583 instanceof ai_class30) || !uUID.equals((ai_class302 = (ai_class30)((Object)em_class2583)).java_util_UUID_e())) continue;
                em_class2582 = em_class2583;
                break;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        if (em_class2582 == null) {
            return;
        }
        Render render = minecraft.getRenderManager().getEntityRenderObject(em_class2582);
        if (render == null) {
            return;
        }
        float f = minecraft.player.rotationYaw;
        GoblinRenderer.N = (float)((double)minecraft.player.movementInput.moveStrafe * GoblinRenderer.G.x);
        GoblinRenderer.N += -(f - GoblinRenderer.H) * 3.0f;
        GoblinRenderer.N = b6_class67.a(GoblinRenderer.I, GoblinRenderer.N, 0.1f);
        float f2 = -minecraft.player.rotationPitch;
        GoblinRenderer.x = (float)((double)minecraft.player.movementInput.moveForward * GoblinRenderer.G.z + (double)((float)minecraft.player.motionY) * GoblinRenderer.G.y);
        GoblinRenderer.x += -(f2 - GoblinRenderer.t) * 3.0f;
        GoblinRenderer.x = b6_class67.a(GoblinRenderer.E, GoblinRenderer.x, 0.1f);
        GoblinRenderer.a(em_class2582, renderWorldLastEvent.getPartialTicks());
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
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                if (!em_class2582.world.isRemote || em_class2582.isDead || !(em_class2582 instanceof ai_class30)) continue;
                ai_class30 ai_class302 = (ai_class30)((Object)em_class2582);
                if (em_class2582.currentAction() != Action.START_THROWING) continue;
                em_class2582.b(true);
                minecraft.getRenderManager().renderEntity(em_class2582, 0.0, 0.0, 0.0, uUID.equals(ai_class302.java_util_UUID_e()) ? -420.69f : 0.0f, minecraft.getRenderPartialTicks(), false);
                em_class2582.b(false);
                return;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
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
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                ai_class30 ai_class302;
                UUID uUID2;
                Action fp_class3242;
                if (!(em_class2582 instanceof ai_class30) || (fp_class3242 = em_class2582.currentAction()) != Action.PICK_UP && fp_class3242 != Action.START_THROWING || !uUID.equals(uUID2 = (ai_class302 = (ai_class30)((Object)em_class2582)).java_util_UUID_e())) continue;
                renderHandEvent.setCanceled(true);
                break;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderPlayerEvent.Pre pre) {
        UUID uUID = pre.getEntityPlayer().getPersistentID();
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                if (!(em_class2582 instanceof ai_class30)) continue;
                ai_class30 ai_class302 = (ai_class30)((Object)em_class2582);
                Action fp_class3242 = em_class2582.currentAction();
                if (fp_class3242 != Action.PICK_UP && fp_class3242 != Action.START_THROWING || !uUID.equals(ai_class302.java_util_UUID_e())) continue;
                pre.setCanceled(true);
                break;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
        return concurrentModificationException;
    }
}

