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
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CatEatingDone
implements IMessage {
    boolean isValid = false;
    UUID b;

    public CatEatingDone() {
    }

    public CatEatingDone(UUID uUID) {
        this.b = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.b.toString());
    }

    public static class Handler implements IMessageHandler<CatEatingDone, IMessage> {
        @Override
        public IMessage onMessage(CatEatingDone msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid message @CatEatingDone :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(msg.b);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote || !(em_class2582 instanceof LunaEntity)) continue;
                    LunaEntity eb_class2362 = (LunaEntity)em_class2582;
                    eb_class2362.void_h();
                }
            });
            return null;
        }
    }
}

