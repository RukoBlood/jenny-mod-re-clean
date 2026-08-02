/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gender_change;

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

//w.class
public class SexPromptManager {
    static public SexPromptManager INSTANCE;
    private SexPrompt activePrompt;

    public void tick() {
        if (SexPromptManager.INSTANCE.activePrompt == null) {
            return;
        }
        SexPromptManager.INSTANCE.activePrompt.timer -= 1.0f;
        if (SexPromptManager.INSTANCE.activePrompt.timer <= 0.0f) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.timeout", new Object[0])));
            this.deletePrompt();
        }
    }

    public SexPrompt getActivePrompt() {
        return SexPromptManager.INSTANCE.activePrompt;
    }

    void deletePrompt() {
        SexPromptManager.INSTANCE.activePrompt = null;
    }

    public void setNewActivePrompt(@Nonnull SexPrompt activePrompt) {
        World world = Minecraft.getMinecraft().player.world;
        EntityPlayer girl = world.getPlayerEntityByUUID(activePrompt.female);
        EntityPlayer boy = world.getPlayerEntityByUUID(activePrompt.male);
        if (boy == null || girl == null) {
            return;
        }
        TextComponentString mainPromptMessage = new TextComponentString((Object)((Object)TextFormatting.LIGHT_PURPLE) + (activePrompt.senderIsMale ? boy.getName() : girl.getName()) + " " + (Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.playerxaskedfory", new Object[0]) + " " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format(activePrompt.action, new Object[0]));
        TextComponentString timeoutWarningMessage = new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.autodeletion", new Object[0]));
        TextComponentString actionButtonsMessage = new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + "[ " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format("genderswap.sexpromt.accept", new Object[0]) + (Object)((Object)TextFormatting.DARK_PURPLE) + " | " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format("genderswap.sexpromt.decline", new Object[0]) + (Object)((Object)TextFormatting.DARK_PURPLE) + " ]");
        girl.sendMessage(mainPromptMessage);
        girl.sendMessage(timeoutWarningMessage);
        girl.sendMessage(actionButtonsMessage);
        this.activePrompt = activePrompt;
    }

    @SubscribeEvent
    public void answer(ClientChatEvent event) {
        if (INSTANCE.getActivePrompt() == null) {
            return;
        }
        String inputMsg = event.getMessage().toLowerCase();
        if (inputMsg.equals(I18n.format("genderswap.sexpromt.accept", new Object[0]).toLowerCase())) {
            SexPrompt currentRequest = INSTANCE.getActivePrompt();
            this.startSex(currentRequest.action, currentRequest.female, currentRequest.male);
            this.deletePrompt();
            event.setCanceled(true);
        }
        if (inputMsg.equals(I18n.format("genderswap.sexpromt.decline", new Object[0]).toLowerCase())) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.declineconformation", new Object[0])));
            this.deletePrompt();
            event.setCanceled(true);
        }
    }

    void startSex(String animationID, UUID receiver, UUID sender) {
        PackageHandler.INSTANCE.sendToServer((IMessage)new StartStandingSexAnimation(receiver, sender, animationID));
    }

    public static class SexPrompt {
        public String action;
        public UUID male;
        public UUID female;
        public float timer;
        boolean senderIsMale;

        public SexPrompt(String action, UUID male, UUID female, boolean senderIsMale) {
            this.action = action;
            this.male = male;
            this.female = female;
            this.timer = 1200.0f;
            this.senderIsMale = senderIsMale;
        }
    }
}

