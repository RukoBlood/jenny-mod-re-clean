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
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class bv_class101
implements IMessage {
    Vec3d a;
    boolean c;
    boolean b = false;

    public bv_class101() {
    }

    public bv_class101(Vec3d vec3d, boolean bl) {
        this.a = vec3d;
        this.c = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.c = byteBuf.readBoolean();
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.a.x);
        byteBuf.writeDouble(this.a.y);
        byteBuf.writeDouble(this.a.z);
        byteBuf.writeBoolean(this.c);
    }

    public static class a_inner102
    implements IMessageHandler<bv_class101, IMessage> {
        public IMessage a(bv_class101 bv_class1012, MessageContext messageContext) {
            if (!bv_class1012.b || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnEnergyBallParticles :(");
                return null;
            }
            if (bv_class1012.c) {
                EnergyBallEntity.a(bv_class1012.a);
            } else {
                EnergyBallEntity.c(bv_class1012.a);
            }
            return null;
        }

                @Override
        public IMessage onMessage(bv_class101 iMessage, MessageContext messageContext) {
            return this.a((bv_class101)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

