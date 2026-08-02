/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.events;

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
public class InteractionPrompt {
    static public InteractionPrompt instance;
    private ActiveRequest pendingRequest;

    public void onTickUpdate() {
        if (InteractionPrompt.instance.pendingRequest == null) {
            return;
        }
        InteractionPrompt.instance.pendingRequest.ticksLeft -= 1.0f;
        if (InteractionPrompt.instance.pendingRequest.ticksLeft <= 0.0f) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.timeout", new Object[0])));
            this.clearPendingRequest();
        }
    }

    public ActiveRequest getPendingRequest() {
        return InteractionPrompt.instance.pendingRequest;
    }

    void clearPendingRequest() {
        InteractionPrompt.instance.pendingRequest = null;
    }

    public void openInteractivePrompt(@Nonnull ActiveRequest request) {
        World world = Minecraft.getMinecraft().player.world;
        EntityPlayer receiver = world.getPlayerEntityByUUID(request.receiverUUID);
        EntityPlayer sender = world.getPlayerEntityByUUID(request.senderUUID);
        if (sender == null || receiver == null) {
            return;
        }
        TextComponentString mainPromptMessage = new TextComponentString((Object)((Object)TextFormatting.LIGHT_PURPLE) + (request.isSenderInitiator ? sender.getName() : receiver.getName()) + " " + (Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.playerxaskedfory", new Object[0]) + " " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format(request.animationId, new Object[0]));
        TextComponentString timeoutWarningMessage = new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.autodeletion", new Object[0]));
        TextComponentString actionButtonsMessage = new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + "[ " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format("genderswap.sexpromt.accept", new Object[0]) + (Object)((Object)TextFormatting.DARK_PURPLE) + " | " + (Object)((Object)TextFormatting.LIGHT_PURPLE) + I18n.format("genderswap.sexpromt.decline", new Object[0]) + (Object)((Object)TextFormatting.DARK_PURPLE) + " ]");
        receiver.sendMessage(mainPromptMessage);
        receiver.sendMessage(timeoutWarningMessage);
        receiver.sendMessage(actionButtonsMessage);
        this.pendingRequest = request;
    }

    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
        if (instance.getPendingRequest() == null) {
            return;
        }
        String inputMsg = event.getMessage().toLowerCase();
        if (inputMsg.equals(I18n.format("genderswap.sexpromt.accept", new Object[0]).toLowerCase())) {
            ActiveRequest currentRequest = instance.getPendingRequest();
            this.sendStartAnimationPacket(currentRequest.animationId, currentRequest.receiverUUID, currentRequest.senderUUID);
            this.clearPendingRequest();
            event.setCanceled(true);
        }
        if (inputMsg.equals(I18n.format("genderswap.sexpromt.decline", new Object[0]).toLowerCase())) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + I18n.format("genderswap.sexpromt.declineconformation", new Object[0])));
            this.clearPendingRequest();
            event.setCanceled(true);
        }
    }

    void sendStartAnimationPacket(String animationID, UUID receiver, UUID sender) {
        PackageHandler.INSTANCE.sendToServer((IMessage)new StartStandingSexAnimation(receiver, sender, animationID));
    }

    public static class ActiveRequest {
        public String animationId;
        public UUID senderUUID;
        public UUID receiverUUID;
        public float ticksLeft;
        boolean isSenderInitiator;

        public ActiveRequest(String animID, UUID sender, UUID receiver, boolean isSenderInit) {
            this.animationId = animID;
            this.senderUUID = sender;
            this.receiverUUID = receiver;
            this.ticksLeft = 1200.0f;
            this.isSenderInitiator = isSenderInit;
        }
    }
}

