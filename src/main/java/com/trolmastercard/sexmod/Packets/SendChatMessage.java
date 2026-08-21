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
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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
    boolean b;
    String a;
    int d;
    UUID c;

    public SendChatMessage(String string, int n, UUID uUID) {
        this.a = string;
        this.d = n;
        this.c = uUID;
        this.b = true;
    }

    public SendChatMessage() {
        this.b = false;
    }

    public void fromBytes(ByteBuf byteBuf) {
        try {
            int n = byteBuf.readInt();
            byte[] byArray = new byte[n];
            for (int i = 0; i < n; ++i) {
                byArray[i] = byteBuf.readByte();
            }
            this.a = new String(byArray);
            this.d = byteBuf.readInt();
            this.c = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
            this.b = true;
        } catch (IndexOutOfBoundsException e) {
            this.b = false;
            System.out.println("couldn't read bytes @SendChatMessage :(");
        }
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.a.getBytes().length);
        byteBuf.writeBytes(this.a.getBytes());
        byteBuf.writeInt(this.d);
        ByteBufUtils.writeUTF8String(byteBuf, this.c.toString());
    }


    public static class Handler implements IMessageHandler<SendChatMessage, IMessage> {
        @Override
        public IMessage onMessage(SendChatMessage msg, MessageContext ctx) {
            if (!msg.b) {
                System.out.println("recieved an unvalid message @SendChatMessage :(");
                return null;
            }
            if (ctx.side.isClient()) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(msg.a));
            } else {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                    Vec3d vec3d = GirlEntity.girlList(msg.c).get(0).getPreviousPosition();
                    PackageHandler.INSTANCE.sendToAllAround(new SendChatMessage(msg.a, msg.d, msg.c), new NetworkRegistry.TargetPoint(msg.d, vec3d.x, vec3d.y, vec3d.z, 40.0));
                });
            }
            return null;
        }
    }
}

