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

public class PacketSendGirlToBed
implements IMessage {
    boolean a;
    UUID girlID;

    public PacketSendGirlToBed() {
        this.a = false;
    }

    public PacketSendGirlToBed(UUID uUID) {
        this.girlID = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlID = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlID.toString());
    }

    public static class a_inner24
    implements IMessageHandler<PacketSendGirlToBed, IMessage> {
        // TODO propagate this onMessage inlining to all other packets eventually...
        @Override
        public IMessage onMessage(PacketSendGirlToBed ac_class232, MessageContext messageContext) {
            if (ac_class232.a) {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                    ArrayList<GirlEntity> arrayList = GirlEntity.girlList(ac_class232.girlID);
                    for (GirlEntity girlEntity : arrayList) {
                        if (!girlEntity.world.isRemote && girlEntity instanceof IBeddableSexGirl) {
                            ((IBeddableSexGirl) girlEntity).goToSexBed();
                        }
                    }
                });
            } else {
                System.out.println("received an invalid message @SendGirlToSex :(");
            }
            return null;
        }
    }
}

