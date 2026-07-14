/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.gameevent.TickEvent$RenderTickEvent
 */
package com.trolmastercard.sexmod;

import java.util.UUID;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GirlRenderEvent {
    Vec3d origPos = null;
    Vec3d origLastTickPos = null;

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            if (girl.isDead || girl.getID() == null || girl.currentAction() == Action.NULL)
                continue;

            EntityPlayer player = event.getEntityPlayer();

            if (!girl.currentAction().hasPlayer || !girl.getID().equals(player.getPersistentID()) && !girl.getID().equals(player.getUniqueID()))
                continue;

            event.setCanceled(true);

            return;
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        PlayerGirl playerGirl = PlayerGirl.GetPlayer(player);

        if (playerGirl != null && playerGirl.boolean_Q()) {
            event.setCanceled(true);
            return;
        }

        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            UUID girlID = girl.getID();
            Action currentAction = girl.currentAction();

            if (girl.isDead || girlID == null || currentAction == null || !currentAction.hasPlayer || !girlID.equals(player.getUniqueID()) && !girlID.equals(player.getPersistentID()))
                continue;
            event.setCanceled(true);

            return;
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void OnRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if (this.origPos != null) {
                mc.player.setPosition(this.origPos.x, this.origPos.y, this.origPos.z);
                mc.player.lastTickPosX = this.origLastTickPos.x;
                mc.player.lastTickPosY = this.origLastTickPos.y;
                mc.player.lastTickPosZ = this.origLastTickPos.z;
                this.origPos = null;
                this.origLastTickPos = null;
            }
            return;
        }

        if (mc.gameSettings.thirdPersonView != 0) {
            return;
        }

        GirlEntity girlEntity = GirlEntity.a(mc.player.getPersistentID(), false);
        if (girlEntity == null) {
            return;
        }
        if (!girlEntity.currentAction().useBoyCam) {
            return;
        }
        if (girlEntity.boolean_m()) {
            return;
        }
        this.origPos = mc.player.getPositionVector();
        this.origLastTickPos = new Vec3d(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ);
        Vec3d targetCameraPos = girlEntity.boolean_Q() ? girlEntity.b("boyCam").add(girlEntity.net_minecraft_util_math_Vec3d_o()) : girlEntity.b("boyCam").add(Reference.LerpVec3d(new Vec3d(girlEntity.lastTickPosX, girlEntity.lastTickPosY, girlEntity.lastTickPosZ), girlEntity.getPositionVector(), (double)event.renderTickTime));
        mc.player.posX = targetCameraPos.x;
        mc.player.posY = targetCameraPos.y - (double)mc.player.getEyeHeight();
        mc.player.posZ = targetCameraPos.z;
        mc.player.lastTickPosX = targetCameraPos.x;
        mc.player.lastTickPosY = targetCameraPos.y - (double)mc.player.getEyeHeight();
        mc.player.lastTickPosZ = targetCameraPos.z;
    }
}

