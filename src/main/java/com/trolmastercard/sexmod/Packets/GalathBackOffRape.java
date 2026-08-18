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
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class GalathBackOffRape implements IMessage {
    boolean valid = false;

    public void fromBytes(ByteBuf byteBuf) {
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class Handler implements IMessageHandler<GalathBackOffRape, IMessage> {
        public IMessage execute(GalathBackOffRape msg, MessageContext ctx) {
            if (!msg.valid || !ctx.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid Message @GalathBackOffRape :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girl = GirlEntity.getGirlByUUID(ctx.getServerHandler().player.getPersistentID(), true);
                if (girl instanceof GalathEntity) {
                    ((GalathEntity)girl).handleRapeState();
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(GalathBackOffRape iMessage, MessageContext messageContext) {
            return this.execute((GalathBackOffRape)iMessage, messageContext);
        }
    }
}

