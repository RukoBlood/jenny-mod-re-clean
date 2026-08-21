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

public class GalathRapePounce
implements IMessage {
    boolean isValid = false;
    boolean b;

    public GalathRapePounce() {
    }

    public GalathRapePounce(boolean bl) {
        this.b = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
    }

    public static class Handler implements IMessageHandler<GalathRapePounce, IMessage> {
        @Override
        public IMessage onMessage(GalathRapePounce msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid message @GalathRapePounce :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girl = GirlEntity.getActiveSceneInfo(ctx.getServerHandler().player.getPersistentID());
                if (girl instanceof GalathEntity) {
                    ((GalathEntity)girl).handleRapeAction(msg.b);
                }
            });
            return null;
        }
    }
}

