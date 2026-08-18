/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.events;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HandlePlayerMovement {
    static private boolean active = true;
    static public boolean isThrusting = false;
    static public boolean isCumming = false;
    static public MovementInput movement;

    @SubscribeEvent
    public void PreventPlayerFromMoving(InputUpdateEvent event) {
        movement = event.getMovementInput();
        isThrusting = HandlePlayerMovement.movement.sneak;
        isCumming = HandlePlayerMovement.movement.jump;
        if (active) {
            return;
        }
        if (HandlePlayerMovement.movement.jump) {
            PlayerGirl.void_i();
        }
        if (HandlePlayerMovement.movement.sneak) {
            GirlEntity.triggerFastSexAction(Minecraft.getMinecraft().player.getPersistentID());
        }
        if (HandlePlayerMovement.movement.jump && SexUI.cumPercentage >= 1.0) {
            GirlEntity.triggerCumAction(Minecraft.getMinecraft().player.getPersistentID());
        }
        HandlePlayerMovement.movement.backKeyDown = false;
        HandlePlayerMovement.movement.forwardKeyDown = false;
        HandlePlayerMovement.movement.leftKeyDown = false;
        HandlePlayerMovement.movement.rightKeyDown = false;
        HandlePlayerMovement.movement.sneak = false;
        HandlePlayerMovement.movement.jump = false;
        HandlePlayerMovement.movement.moveForward = 0.0f;
        HandlePlayerMovement.movement.moveStrafe = 0.0f;
        Minecraft.getMinecraft().player.setVelocity(0.0, 0.0, 0.0);
    }

    public static boolean b() {
        return active;
    }

    public static void setMovementLock(boolean bl) {
        active = bl;
        if (!bl) {
            HandlePlayerMovement.a();
        }
    }

    @SideOnly(value=Side.CLIENT)
    static void a() {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (!PlayerGirl.isOwnerPlayer(entityPlayerSP)) {
            return;
        }
        ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString("Jump to get out of the animation"), true);
    }

    @SubscribeEvent
    public void PreventPlayerFromTakingAction(MouseEvent event) {
        if (!active && event.isButtonstate()) {
            event.setCanceled(true);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

