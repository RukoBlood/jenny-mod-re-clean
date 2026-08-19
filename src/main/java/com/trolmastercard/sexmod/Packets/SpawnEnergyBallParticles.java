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
    boolean c = false;
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
            this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        } catch (Exception exception) {
            this.a = null;
        }
        try {
            this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        } catch (Exception exception) {
            this.b = null;
        }
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)(this.a == null ? "trol was here" : this.a.toString()));
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)(this.b == null ? "trol was here" : this.b.toString()));
    }

    public static class Handler
    implements IMessageHandler<SpawnEnergyBallParticles, IMessage> {
        public IMessage a(SpawnEnergyBallParticles ab_class212, MessageContext messageContext) {
            if (!ab_class212.c || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnEnergyBallParticles :(");
                return null;
            }
            GirlEntity em_class2582 = GirlEntity.getClientGirlEntity(ab_class212.a);
            if (!(em_class2582 instanceof GalathEntity)) {
                System.out.println("doesnt exit");
                return null;
            }
            GalathCoin.summonGalathFor(ab_class212.b, (GalathEntity)em_class2582);
            return null;
        }

                @Override
        public IMessage onMessage(SpawnEnergyBallParticles iMessage, MessageContext messageContext) {
            return this.a((SpawnEnergyBallParticles)iMessage, messageContext);
        }
    }
}

