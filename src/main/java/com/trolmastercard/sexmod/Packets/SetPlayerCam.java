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
    boolean d = false;
    float a;
    float b;
    int c;

    public SetPlayerCam() {
    }

    public SetPlayerCam(float f, float f2, int n) {
        this.a = f;
        this.b = f2;
        this.c = n;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = byteBuf.readFloat();
        this.b = byteBuf.readFloat();
        this.c = byteBuf.readInt();
        this.d = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeFloat(this.a);
        byteBuf.writeFloat(this.b);
        byteBuf.writeInt(this.c);
    }

    public static class Handler implements IMessageHandler<SetPlayerCam, IMessage> {
        @Override
        public IMessage onMessage(SetPlayerCam msg, MessageContext ctx) {
            if (!msg.d || ctx.side != Side.CLIENT) {
                System.out.println("received an invalid message @SetPlayerCam :(");
                return null;
            }
            System.out.println(Thread.currentThread().getName());
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.addScheduledTask(() -> {
                minecraft.gameSettings.thirdPersonView = msg.c;
                EntityPlayerSP entityPlayerSP = minecraft.player;
                entityPlayerSP.rotationYaw = msg.b;
                entityPlayerSP.prevRotationYaw = msg.b;
                entityPlayerSP.prevRotationYawHead = msg.b;
                entityPlayerSP.rotationYawHead = msg.b;
                entityPlayerSP.renderYawOffset = msg.b;
                entityPlayerSP.rotationPitch = msg.a;
                entityPlayerSP.prevRotationPitch = msg.a;
            });
            return null;
        }
    }
}

