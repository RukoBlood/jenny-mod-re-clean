/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetPlayerMovement implements IMessage {
    boolean isValid;
    boolean setActive;

    public SetPlayerMovement(boolean bl) {
        this.setActive = bl;
        this.isValid = true;
    }

    public SetPlayerMovement() {
        this.isValid = false;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.setActive = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.setActive);
        this.isValid = true;
    }

    public static class Handler implements IMessageHandler<SetPlayerMovement, IMessage> {
        @Override
        public IMessage onMessage(SetPlayerMovement msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.CLIENT) {
                System.out.println("received an invalid message @SetPlayerMovement :(");
                return null;
            }
            HandlePlayerMovement.setMovementLock(msg.setActive);
            Minecraft.getMinecraft().player.setVelocity(0.0, 0.0, 0.0);
            if (msg.setActive) {
                SexUI.hide();
            }
            return null;
        }
    }
}

