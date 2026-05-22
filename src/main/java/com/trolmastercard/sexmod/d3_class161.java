/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

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

public class d3_class161 {
    static private boolean c = true;
    static public boolean d = false;
    static public boolean a = false;
    static public MovementInput b;

    @SubscribeEvent
    public void a(InputUpdateEvent inputUpdateEvent) {
        b = inputUpdateEvent.getMovementInput();
        d = d3_class161.b.sneak;
        a = d3_class161.b.jump;
        if (c) {
            return;
        }
        if (d3_class161.b.jump) {
            PlayerGirl.void_i();
        }
        if (d3_class161.b.sneak) {
            GirlEntity.k(Minecraft.getMinecraft().player.getPersistentID());
        }
        if (d3_class161.b.jump && ds_class200.c >= 1.0) {
            GirlEntity.f(Minecraft.getMinecraft().player.getPersistentID());
        }
        d3_class161.b.backKeyDown = false;
        d3_class161.b.forwardKeyDown = false;
        d3_class161.b.leftKeyDown = false;
        d3_class161.b.rightKeyDown = false;
        d3_class161.b.sneak = false;
        d3_class161.b.jump = false;
        d3_class161.b.moveForward = 0.0f;
        d3_class161.b.moveStrafe = 0.0f;
        Minecraft.getMinecraft().player.setVelocity(0.0, 0.0, 0.0);
    }

    public static boolean b() {
        return c;
    }

    public static void a(boolean bl) {
        c = bl;
        if (!bl) {
            d3_class161.a();
        }
    }

    @SideOnly(value=Side.CLIENT)
    static void a() {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (!PlayerGirl.e(entityPlayerSP)) {
            return;
        }
        ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString("Jump to get out of the animation"), true);
    }

    @SubscribeEvent
    public void a(MouseEvent mouseEvent) {
        if (!c && mouseEvent.isButtonstate()) {
            mouseEvent.setCanceled(true);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

