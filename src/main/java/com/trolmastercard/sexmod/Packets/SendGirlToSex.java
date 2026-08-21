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
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendGirlToSex
implements IMessage {
    boolean isValid;
    UUID girlID;

    public SendGirlToSex() {
        this.isValid = false;
    }

    public SendGirlToSex(UUID uUID) {
        this.girlID = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlID = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlID.toString());
    }

    public static class Handler implements IMessageHandler<SendGirlToSex, IMessage> {
        // propagate this onMessage inlining to all other packets eventually...
        // Yes daddy palkaline
        @Override
        public IMessage onMessage(SendGirlToSex message, MessageContext ctx) {
            if (message.isValid) {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                    ArrayList<GirlEntity> arrayList = GirlEntity.girlList(message.girlID);
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

