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
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class gh_class368
implements IMessage {
    boolean b;
    String a;
    int d;
    UUID c;

    public gh_class368(String string, int n, UUID uUID) {
        this.a = string;
        this.d = n;
        this.c = uUID;
        this.b = true;
    }

    public gh_class368() {
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
            this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
            this.b = true;
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.b = false;
            System.out.println("couldn't read bytes @SendChatMessage :(");
            return;
        }
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.a.getBytes().length);
        byteBuf.writeBytes(this.a.getBytes());
        byteBuf.writeInt(this.d);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
    }

    private static IndexOutOfBoundsException a(IndexOutOfBoundsException indexOutOfBoundsException) {
        return indexOutOfBoundsException;
    }

    public static class a_inner369
    implements IMessageHandler<gh_class368, IMessage> {
        public IMessage a(gh_class368 gh_class3682, MessageContext messageContext) {
            if (!gh_class3682.b) {
                System.out.println("recieved an unvalid message @SendChatMessage :(");
                return null;
            }
            if (messageContext.side.isClient()) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(gh_class3682.a));
            } else {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                    Vec3d vec3d = GirlEntity.girlList(gh_class3682.c).get(0).net_minecraft_util_math_Vec3d_M();
                    NetworkRegistry.networkWrapper.sendToAllAround((IMessage)new gh_class368(gh_class3682.a, gh_class3682.d, gh_class3682.c), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(gh_class3682.d, vec3d.x, vec3d.y, vec3d.z, 40.0));
                });
            }
            return null;
        }

                @Override
        public IMessage onMessage(gh_class368 iMessage, MessageContext messageContext) {
            return this.a((gh_class368)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

