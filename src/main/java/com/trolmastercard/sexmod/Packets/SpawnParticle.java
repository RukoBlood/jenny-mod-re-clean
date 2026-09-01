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
    UUID girlId;
    String particleType;
    int amount;

    public SpawnParticle() {
    }

    public SpawnParticle(UUID uUID, String string) {
        this.girlId = uUID;
        this.particleType = string;
        this.amount = 1;
    }

    public SpawnParticle(UUID uUID, String string, int n) {
        this.girlId = uUID;
        this.particleType = string;
        this.amount = n;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.particleType = ByteBufUtils.readUTF8String(byteBuf);
        this.amount = byteBuf.readInt();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.particleType);
        byteBuf.writeInt(this.amount);
    }

    public static class Handler implements IMessageHandler<SpawnParticle, IMessage> {
        @Override
        public IMessage onMessage(SpawnParticle msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnParticle :(");
                return null;
            }
            ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girlId);
            for (GirlEntity girl : girls) {
                if (girl.world.isRemote) {
                    for (int i = 0; i < msg.amount; ++i) {
                        GirlEntity.spawnParticlesAround(EnumParticleTypes.getByName(msg.particleType), girl);
                    }
                }
            }
            return null;
        }
    }
}

