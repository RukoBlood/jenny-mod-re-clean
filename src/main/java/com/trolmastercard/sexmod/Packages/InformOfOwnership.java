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

    public static class a_inner365
    implements IMessageHandler<InformOfOwnership, IMessage> {
        public IMessage a(InformOfOwnership gf_class3642, MessageContext messageContext) {
            if (!gf_class3642.a || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @InformOfOwnership :(");
                return null;
            }
            GalathMangTracker.f = gf_class3642.b;
            return null;
        }

                @Override
        public IMessage onMessage(InformOfOwnership iMessage, MessageContext messageContext) {
            return this.a((InformOfOwnership)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

