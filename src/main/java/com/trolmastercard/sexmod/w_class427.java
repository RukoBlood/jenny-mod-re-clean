/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.UUID;
import javax.annotation.Nonnull;

import com.trolmastercard.sexmod.Packages.StartStandingSexAnimation;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class w_class427 {
    static public w_class427 a;
    private a_inner428 b;

    public void a() {
        if (w_class427.a.b == null) {
            return;
        }
        w_class427.a.b.e -= 1.0f;
        if (w_class427.a.b.e <= 0.0f) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.timeout", new Object[0])));
            this.c();
        }
    }

    public a_inner428 b() {
        return w_class427.a.b;
    }

    void c() {
        w_class427.a.b = null;
    }

    public void a(@Nonnull a_inner428 a_inner4282) {
        World world = Minecraft.getMinecraft().player.world;
        EntityPlayer entityPlayer = world.getPlayerEntityByUUID(a_inner4282.d);
        EntityPlayer entityPlayer2 = world.getPlayerEntityByUUID(a_inner4282.c);
        if (entityPlayer2 == null || entityPlayer == null) {
            return;
        }
        TextComponentString textComponentString = new TextComponentString((Object)((Object)TextFormatting.LIGHT_PURPLE) + (a_inner4282.b ? entityPlayer2.getName() : entityPlayer.getName()) + " " + (Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.playerxaskedfory", new Object[0]) + " " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format(a_inner4282.a, new Object[0]));
        TextComponentString textComponentString2 = new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.autodeletion", new Object[0]));
        TextComponentString textComponentString3 = new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + "[ " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format("genderswap.sexpromt.accept", new Object[0]) + (Object)((Object)TextFormatting.DARK_PURPLE) + " | " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format("genderswap.sexpromt.decline", new Object[0]) + (Object)((Object)TextFormatting.DARK_PURPLE) + " ]");
        entityPlayer.sendMessage(textComponentString);
        entityPlayer.sendMessage(textComponentString2);
        entityPlayer.sendMessage(textComponentString3);
        this.b = a_inner4282;
    }

    @SubscribeEvent
    public void a(ClientChatEvent clientChatEvent) {
        if (a.b() == null) {
            return;
        }
        String string = clientChatEvent.getMessage().toLowerCase();
        if (string.equals(I18n.format("genderswap.sexpromt.accept", new Object[0]).toLowerCase())) {
            a_inner428 a_inner4282 = a.b();
            this.a(a_inner4282.a, a_inner4282.d, a_inner4282.c);
            this.c();
            clientChatEvent.setCanceled(true);
        }
        if (string.equals(I18n.format("genderswap.sexpromt.decline", new Object[0]).toLowerCase())) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.declineconformation", new Object[0])));
            this.c();
            clientChatEvent.setCanceled(true);
        }
    }

    void a(String string, UUID uUID, UUID uUID2) {
        PackageHandler.networkWrapper.sendToServer((IMessage)new StartStandingSexAnimation(uUID, uUID2, string));
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner428 {
        public String a;
        public UUID c;
        public UUID d;
        public float e;
        boolean b;

        public a_inner428(String string, UUID uUID, UUID uUID2, boolean bl) {
            this.a = string;
            this.c = uUID;
            this.d = uUID2;
            this.e = 1200.0f;
            this.b = bl;
        }
    }
}

