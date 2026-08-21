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

import com.trolmastercard.sexmod.girls.Galath.GalathCoin.GalathCoin;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnEnergyBallParticles implements IMessage {
    boolean isValid = false;
    UUID a;
    UUID b;

    public SpawnEnergyBallParticles() {
    }

    public SpawnEnergyBallParticles(UUID uUID, UUID uUID2) {
        this.a = uUID;
        this.b = uUID2;
    }

    public void fromBytes(ByteBuf byteBuf) {
        try {
            this.a = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        } catch (Exception exception) {
            this.a = null;
        }
        try {
            this.b = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        } catch (Exception exception) {
            this.b = null;
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.a == null ? "trol was here" : this.a.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.b == null ? "trol was here" : this.b.toString());
    }

    public static class Handler implements IMessageHandler<SpawnEnergyBallParticles, IMessage> {
        @Override
        public IMessage onMessage(SpawnEnergyBallParticles msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnEnergyBallParticles :(");
                return null;
            }
            GirlEntity girl = GirlEntity.getClientGirlEntity(msg.a);
            if (girl instanceof GalathEntity) {
                GalathCoin.summonGalathFor(msg.b, (GalathEntity) girl);
            } else {
                System.out.println("doesnt exit");
            }
            return null;
        }
    }
}

