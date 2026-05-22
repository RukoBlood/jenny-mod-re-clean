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

import com.trolmastercard.sexmod.d3_class161;
import com.trolmastercard.sexmod.ds_class200;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class gz_class393
implements IMessage {
    boolean a;
    boolean b;

    public gz_class393(boolean bl) {
        this.b = bl;
        this.a = true;
    }

    public gz_class393() {
        this.a = false;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
        this.a = true;
    }

    public static class a_inner394
    implements IMessageHandler<gz_class393, IMessage> {
        public IMessage a(gz_class393 gz_class3932, MessageContext messageContext) {
            if (!gz_class3932.a || messageContext.side != Side.CLIENT) {
                System.out.println("received an invalid message @SetPlayerMovement :(");
                return null;
            }
            d3_class161.a(gz_class3932.b);
            try {
                Minecraft.getMinecraft().player.setVelocity(0.0, 0.0, 0.0);
            } catch (Exception exception) {
                // empty catch block
            }
            if (gz_class3932.b) {
                ds_class200.c();
            }
            return null;
        }

                @Override
        public IMessage onMessage(gz_class393 iMessage, MessageContext messageContext) {
            return this.a((gz_class393)iMessage, messageContext);
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }
}

