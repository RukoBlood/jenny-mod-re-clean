/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class aq_class39
implements IMessage {
    boolean d = false;
    float a;
    float b;
    int c;

    public aq_class39() {
    }

    public aq_class39(float f, float f2, int n) {
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

    public static class a_inner40
    implements IMessageHandler<aq_class39, IMessage> {
        public IMessage a(aq_class39 aq_class392, MessageContext messageContext) {
            if (!aq_class392.d || messageContext.side != Side.CLIENT) {
                System.out.println("received an invalid message @SetPlayerCam :(");
                return null;
            }
            System.out.println(Thread.currentThread().getName());
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.addScheduledTask(() -> {
                minecraft.gameSettings.thirdPersonView = aq_class392.c;
                EntityPlayerSP entityPlayerSP = minecraft.player;
                entityPlayerSP.rotationYaw = aq_class392.b;
                entityPlayerSP.prevRotationYaw = aq_class392.b;
                entityPlayerSP.prevRotationYawHead = aq_class392.b;
                entityPlayerSP.rotationYawHead = aq_class392.b;
                entityPlayerSP.renderYawOffset = aq_class392.b;
                entityPlayerSP.rotationPitch = aq_class392.a;
                entityPlayerSP.prevRotationPitch = aq_class392.a;
            });
            return null;
        }

                @Override
        public IMessage onMessage(aq_class39 iMessage, MessageContext messageContext) {
            return this.a((aq_class39)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

