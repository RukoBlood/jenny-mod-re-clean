/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class g__class356
implements IMessage {
    boolean a = false;
    boolean b;

    public g__class356() {
    }

    public g__class356(boolean bl) {
        this.b = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
    }

    public static class a_inner357
    implements IMessageHandler<g__class356, IMessage> {
        public IMessage a(g__class356 g__class3562, MessageContext messageContext) {
            if (!g__class3562.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid message @GalathRapePounce :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_c(messageContext.getServerHandler().player.getPersistentID());
                if (em_class2582 instanceof GalathEntity) {
                    ((GalathEntity)em_class2582).c(g__class3562.b);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(g__class356 iMessage, MessageContext messageContext) {
            return this.a((g__class356)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

