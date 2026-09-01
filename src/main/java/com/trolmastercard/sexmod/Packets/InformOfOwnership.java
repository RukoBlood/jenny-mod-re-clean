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

import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class InformOfOwnership
implements IMessage {
    boolean isValid = false;
    boolean isDebugEnabled;

    public InformOfOwnership() {
    }

    public InformOfOwnership(boolean bl) {
        this.isDebugEnabled = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.isDebugEnabled = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.isDebugEnabled);
    }

    public static class Handler implements IMessageHandler<InformOfOwnership, IMessage> {
        @Override
        public IMessage onMessage(InformOfOwnership msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @InformOfOwnership :(");
                return null;
            }
            GalathMangTracker.debugEnabled = msg.isDebugEnabled;
            return null;
        }
    }
}

