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

public class cd_class125
implements IMessage {
    boolean a = false;

    public void fromBytes(ByteBuf byteBuf) {
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class a_inner126
    implements IMessageHandler<cd_class125, IMessage> {
        public IMessage a(cd_class125 cd_class1252, MessageContext messageContext) {
            if (!cd_class1252.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid Message @GalathBackOffRape :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity em_class2582 = GirlEntity.a(messageContext.getServerHandler().player.getPersistentID(), true);
                if (em_class2582 instanceof GalathEntity) {
                    ((GalathEntity)em_class2582).void_w();
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(cd_class125 iMessage, MessageContext messageContext) {
            return this.a((cd_class125)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

