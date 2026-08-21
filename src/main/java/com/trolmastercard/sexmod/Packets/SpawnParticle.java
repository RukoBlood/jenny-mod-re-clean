/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnParticle implements IMessage {
    boolean isValid = false;
    UUID c;
    String b;
    int a;

    public SpawnParticle() {
    }

    public SpawnParticle(UUID uUID, String string) {
        this.c = uUID;
        this.b = string;
        this.a = 1;
    }

    public SpawnParticle(UUID uUID, String string, int n) {
        this.c = uUID;
        this.b = string;
        this.a = n;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.b = ByteBufUtils.readUTF8String(byteBuf);
        this.a = byteBuf.readInt();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.c.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.b);
        byteBuf.writeInt(this.a);
    }

    public static class Handler implements IMessageHandler<SpawnParticle, IMessage> {
        @Override
        public IMessage onMessage(SpawnParticle msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnParticle :(");
                return null;
            }
            ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.c);
            for (GirlEntity girl : girls) {
                if (girl.world.isRemote) {
                    for (int i = 0; i < msg.a; ++i) {
                        GirlEntity.spawnParticlesAround(EnumParticleTypes.getByName(msg.b), girl);
                    }
                }
            }
            return null;
        }
    }
}

