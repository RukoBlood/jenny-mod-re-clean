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
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UploadInventoryToServerAlt
implements IMessage {
    boolean isValid;
    UUID allieId;

    public UploadInventoryToServerAlt() {
    }

    public UploadInventoryToServerAlt(UUID uUID) {
        this.allieId = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.allieId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.allieId.toString());
    }

    public static class Handler implements IMessageHandler<UploadInventoryToServerAlt, IMessage> {
        @Override
        public IMessage onMessage(UploadInventoryToServerAlt msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @UploadInventoryToServer :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(msg.allieId);
                for (GirlEntity girl : arrayList) {
                    if (!girl.world.isRemote) {
                        girl.world.removeEntity(girl);
                    }
                }
            });
            return null;
        }
    }
}

