/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetPlayerCam implements IMessage {
    boolean isValid = false;
    float pitch;
    float yaw;
    int thirdPersonTypes;

    public SetPlayerCam() {
    }

    public SetPlayerCam(float f, float f2, int n) {
        this.pitch = f;
        this.yaw = f2;
        this.thirdPersonTypes = n;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.pitch = byteBuf.readFloat();
        this.yaw = byteBuf.readFloat();
        this.thirdPersonTypes = byteBuf.readInt();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeFloat(this.pitch);
        byteBuf.writeFloat(this.yaw);
        byteBuf.writeInt(this.thirdPersonTypes);
    }

    public static class Handler implements IMessageHandler<SetPlayerCam, IMessage> {
        @Override
        public IMessage onMessage(SetPlayerCam msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.CLIENT) {
                System.out.println("received an invalid message @SetPlayerCam :(");
                return null;
            }
            System.out.println(Thread.currentThread().getName());
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.addScheduledTask(() -> {
                minecraft.gameSettings.thirdPersonView = msg.thirdPersonTypes;
                EntityPlayerSP player = minecraft.player;
                player.rotationYaw = msg.yaw;
                player.prevRotationYaw = msg.yaw;
                player.prevRotationYawHead = msg.yaw;
                player.rotationYawHead = msg.yaw;
                player.renderYawOffset = msg.yaw;
                player.rotationPitch = msg.pitch;
                player.prevRotationPitch = msg.pitch;
            });
            return null;
        }
    }
}

