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
    boolean a = false;
    boolean b;

    public InformOfOwnership() {
    }

    public InformOfOwnership(boolean bl) {
        this.b = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
    }

    public static class Handler implements IMessageHandler<InformOfOwnership, IMessage> {
        @Override
        public IMessage onMessage(InformOfOwnership msg, MessageContext ctx) {
            if (!msg.a || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @InformOfOwnership :(");
                return null;
            }
            GalathMangTracker.debugEnabled = msg.b;
            return null;
        }
    }
}

