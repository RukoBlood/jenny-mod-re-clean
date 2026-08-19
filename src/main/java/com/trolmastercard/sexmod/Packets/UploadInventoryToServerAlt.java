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
    boolean a;
    UUID b;

    public UploadInventoryToServerAlt() {
    }

    public UploadInventoryToServerAlt(UUID uUID) {
        this.b = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
    }

    public static class a_inner155
    implements IMessageHandler<UploadInventoryToServerAlt, IMessage> {
        public IMessage a(UploadInventoryToServerAlt cz_class1542, MessageContext messageContext) {
            if (!cz_class1542.a || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @UploadInventoryToServer :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(cz_class1542.b);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote) continue;
                    em_class2582.world.removeEntity(em_class2582);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(UploadInventoryToServerAlt iMessage, MessageContext messageContext) {
            return this.a((UploadInventoryToServerAlt)iMessage, messageContext);
        }
    }
}

