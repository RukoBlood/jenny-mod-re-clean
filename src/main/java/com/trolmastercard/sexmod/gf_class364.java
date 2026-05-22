/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class gf_class364
implements IMessage {
    boolean a = false;
    boolean b;

    public gf_class364() {
    }

    public gf_class364(boolean bl) {
        this.b = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
    }

    public static class a_inner365
    implements IMessageHandler<gf_class364, IMessage> {
        public IMessage a(gf_class364 gf_class3642, MessageContext messageContext) {
            if (!gf_class3642.a || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @InformOfOwnership :(");
                return null;
            }
            GalathMangTracker.f = gf_class3642.b;
            return null;
        }

                @Override
        public IMessage onMessage(gf_class364 iMessage, MessageContext messageContext) {
            return this.a((gf_class364)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

