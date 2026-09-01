/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendChatMessage implements IMessage {
    boolean isValid;
    String message;
    int dimension;
    UUID girlId;

    public SendChatMessage(String string, int n, UUID uUID) {
        this.message = string;
        this.dimension = n;
        this.girlId = uUID;
        this.isValid = true;
    }

    public SendChatMessage() {
        this.isValid = false;
    }

    public void fromBytes(ByteBuf byteBuf) {
        try {
            int n = byteBuf.readInt();
            byte[] byArray = new byte[n];
            for (int i = 0; i < n; ++i) {
                byArray[i] = byteBuf.readByte();
            }
            this.message = new String(byArray);
            this.dimension = byteBuf.readInt();
            this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
            this.isValid = true;
        } catch (IndexOutOfBoundsException e) {
            this.isValid = false;
            System.out.println("couldn't read bytes @SendChatMessage :(");
        }
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.message.getBytes().length);
        byteBuf.writeBytes(this.message.getBytes());
        byteBuf.writeInt(this.dimension);
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
    }


    public static class Handler implements IMessageHandler<SendChatMessage, IMessage> {
        @Override
        public IMessage onMessage(SendChatMessage msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("recieved an unvalid message @SendChatMessage :(");
                return null;
            }
            if (ctx.side.isClient()) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(msg.message));
            } else {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                    Vec3d vec3d = GirlEntity.girlList(msg.girlId).get(0).getPreviousPosition();
                    PacketHandler.INSTANCE.sendToAllAround(new SendChatMessage(msg.message, msg.dimension, msg.girlId), new NetworkRegistry.TargetPoint(msg.dimension, vec3d.x, vec3d.y, vec3d.z, 40.0));
                });
            }
            return null;
        }
    }
}

