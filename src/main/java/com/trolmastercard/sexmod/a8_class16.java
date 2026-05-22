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
import java.util.EnumSet;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class a8_class16
implements IMessage {
    boolean b;
    String c;
    Vec3d e;
    float a;
    float d;

    public a8_class16() {
        this.b = false;
    }

    public a8_class16(String string, Vec3d vec3d) {
        this.c = string;
        this.e = vec3d;
        this.a = 0.0f;
        this.d = 0.0f;
        this.b = true;
    }

    public a8_class16(String string, Vec3d vec3d, float f, float f2) {
        this.c = string;
        this.e = vec3d;
        this.a = f;
        this.d = f2;
        this.b = true;
    }

    public a8_class16(String string, double d, double d2, double d3, float f, float f2) {
        this.c = string;
        this.e = new Vec3d(d, d2, d3);
        this.a = f;
        this.d = f2;
        this.b = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.e = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.a = byteBuf.readFloat();
        this.d = byteBuf.readFloat();
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c);
        byteBuf.writeDouble(this.e.x);
        byteBuf.writeDouble(this.e.y);
        byteBuf.writeDouble(this.e.z);
        byteBuf.writeFloat(this.a);
        byteBuf.writeFloat(this.d);
        this.b = true;
    }

    public static class a_inner17
    implements IMessageHandler<a8_class16, IMessage> {
        public IMessage a(a8_class16 a8_class162, MessageContext messageContext) {
            if (!a8_class162.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @TeleportPlayer :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                try {
                    System.out.println("teleporting player " + a8_class162.c + " to " + a8_class162.e);
                    EntityPlayerMP entityPlayerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(a8_class162.c));
                    a8_class162.a = MathHelper.wrapDegrees(a8_class162.a);
                    a8_class162.d = MathHelper.wrapDegrees(a8_class162.d);
                    entityPlayerMP.setLocationAndAngles(a8_class162.e.x, a8_class162.e.y, a8_class162.e.z, a8_class162.a, a8_class162.d);
                    entityPlayerMP.setRotationYawHead(a8_class162.a);
                    entityPlayerMP.motionX = 0.0;
                    entityPlayerMP.motionY = 0.0;
                    entityPlayerMP.motionZ = 0.0;
                    entityPlayerMP.connection.setPlayerLocation(a8_class162.e.x, a8_class162.e.y, a8_class162.e.z, a8_class162.a, a8_class162.d, EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class));
                } catch (Exception exception) {
                    System.out.println("couldn't find player with UUID: " + a8_class162.c);
                    System.out.println("could only find the following players:");
                    System.out.println(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getFormattedListOfPlayers(true));
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(a8_class16 iMessage, MessageContext messageContext) {
            return this.a((a8_class16)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

