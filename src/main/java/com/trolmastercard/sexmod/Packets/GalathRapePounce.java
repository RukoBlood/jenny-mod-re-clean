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
    boolean a = false;
    boolean b;

    public GalathRapePounce() {
    }

    public GalathRapePounce(boolean bl) {
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
    implements IMessageHandler<GalathRapePounce, IMessage> {
        public IMessage a(GalathRapePounce g__class3562, MessageContext messageContext) {
            if (!g__class3562.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid message @GalathRapePounce :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity em_class2582 = GirlEntity.getActiveSceneInfo(messageContext.getServerHandler().player.getPersistentID());
                if (em_class2582 instanceof GalathEntity) {
                    ((GalathEntity)em_class2582).handleRapeAction(g__class3562.b);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(GalathRapePounce iMessage, MessageContext messageContext) {
            return this.a((GalathRapePounce)iMessage, messageContext);
        }
    }
}

