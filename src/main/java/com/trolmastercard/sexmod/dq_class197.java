/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class dq_class197
implements IMessage {
    boolean a = false;
    UUID b;

    public dq_class197() {
    }

    public dq_class197(UUID uUID) {
        this.b = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
    }

    public static class a_inner198
    implements IMessageHandler<dq_class197, IMessage> {
        public IMessage a(dq_class197 dq_class1972, MessageContext messageContext) {
            if (!dq_class1972.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid message @CatThrowAwayItem :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(dq_class1972.b);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote || !(em_class2582 instanceof LunaEntity)) continue;
                    LunaEntity eb_class2362 = (LunaEntity)em_class2582;
                    eb_class2362.void_j();
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(dq_class197 iMessage, MessageContext messageContext) {
            return this.a((dq_class197)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

