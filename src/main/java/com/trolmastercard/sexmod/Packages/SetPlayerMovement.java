/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gui.SexUI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetPlayerMovement implements IMessage {
    boolean messageValid;
    boolean setActive;

    public SetPlayerMovement(boolean bl) {
        this.setActive = bl;
        this.messageValid = true;
    }

    public SetPlayerMovement() {
        this.messageValid = false;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.setActive = byteBuf.readBoolean();
        this.messageValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.setActive);
        this.messageValid = true;
    }

    public static class Handler implements IMessageHandler<SetPlayerMovement, IMessage> {
        public IMessage onMessageMain(SetPlayerMovement message, MessageContext ctx) {
            if (!message.messageValid || ctx.side != Side.CLIENT) {
                System.out.println("received an invalid message @SetPlayerMovement :(");
                return null;
            }
            HandlePlayerMovement.a(message.setActive);
            Minecraft.getMinecraft().player.setVelocity(0.0, 0.0, 0.0)
            if (message.setActive) {
                SexUI.hide();
            }
            return null;
        }

                @Override
        public IMessage onMessage(SetPlayerMovement iMessage, MessageContext messageContext) {
            return this.onMessageMain((SetPlayerMovement)iMessage, messageContext);
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }
}

