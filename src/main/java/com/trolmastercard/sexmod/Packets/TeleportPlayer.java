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

public class TeleportPlayer implements IMessage {
    boolean messageValid;
    String PlayerUUID;
    Vec3d pos;
    float yaw;
    float pitch;

    public TeleportPlayer() {
        this.messageValid = false;
    }

    public TeleportPlayer(String PlayerUUID, Vec3d pos) {
        this.PlayerUUID = PlayerUUID;
        this.pos = pos;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.messageValid = true;
    }

    public TeleportPlayer(String PlayerUUID, Vec3d pos, float yaw, float pitch) {
        this.PlayerUUID = PlayerUUID;
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.messageValid = true;
    }

    public TeleportPlayer(String PlayerUUID, double x, double y, double z, float yaw, float pitch) {
        this.PlayerUUID = PlayerUUID;
        this.pos = new Vec3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.messageValid = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.PlayerUUID = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.pos = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.yaw = byteBuf.readFloat();
        this.pitch = byteBuf.readFloat();
        this.messageValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.PlayerUUID);
        byteBuf.writeDouble(this.pos.x);
        byteBuf.writeDouble(this.pos.y);
        byteBuf.writeDouble(this.pos.z);
        byteBuf.writeFloat(this.yaw);
        byteBuf.writeFloat(this.pitch);
        this.messageValid = true;
    }

    public static class Handler implements IMessageHandler<TeleportPlayer, IMessage> {
        public IMessage onMessageMain(TeleportPlayer message, MessageContext ctx) {
            if (!message.messageValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @TeleportPlayer :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                try {
                    System.out.println("teleporting player " + message.PlayerUUID + " to " + message.pos);
                    EntityPlayerMP entityPlayerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(message.PlayerUUID));
                    message.yaw = MathHelper.wrapDegrees(message.yaw);
                    message.pitch = MathHelper.wrapDegrees(message.pitch);
                    entityPlayerMP.setLocationAndAngles(message.pos.x, message.pos.y, message.pos.z, message.yaw, message.pitch);
                    entityPlayerMP.setRotationYawHead(message.yaw);
                    entityPlayerMP.motionX = 0.0;
                    entityPlayerMP.motionY = 0.0;
                    entityPlayerMP.motionZ = 0.0;
                    entityPlayerMP.connection.setPlayerLocation(message.pos.x, message.pos.y, message.pos.z, message.yaw, message.pitch, EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class));
                } catch (Exception exception) {
                    System.out.println("couldn't find player with UUID: " + message.PlayerUUID);
                    System.out.println("could only find the following players:");
                    System.out.println(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getFormattedListOfPlayers(true));
                }
            });
            return null;
        }

        @Override
        public IMessage onMessage(TeleportPlayer iMessage, MessageContext messageContext) {
            return this.onMessageMain((TeleportPlayer)iMessage, messageContext);
        }
    }
}

