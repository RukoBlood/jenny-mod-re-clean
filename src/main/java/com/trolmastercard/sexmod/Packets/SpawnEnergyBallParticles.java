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
    UUID galath;
    UUID manglelie;

    public SpawnEnergyBallParticles() {
    }

    public SpawnEnergyBallParticles(UUID uUID, UUID uUID2) {
        this.galath = uUID;
        this.manglelie = uUID2;
    }

    public void fromBytes(ByteBuf byteBuf) {
        try {
            this.galath = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        } catch (Exception exception) {
            this.galath = null;
        }
        try {
            this.manglelie = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        } catch (Exception exception) {
            this.manglelie = null;
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.galath == null ? "trol was here" : this.galath.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.manglelie == null ? "trol was here" : this.manglelie.toString());
    }

    public static class Handler implements IMessageHandler<SpawnEnergyBallParticles, IMessage> {
        @Override
        public IMessage onMessage(SpawnEnergyBallParticles msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnEnergyBallParticles :(");
                return null;
            }
            GirlEntity girl = GirlEntity.getClientGirlEntity(msg.galath);
            if (girl instanceof GalathEntity) {
                GalathCoin.summonGalathFor(msg.manglelie, (GalathEntity) girl);
            } else {
                System.out.println("doesnt exit");
            }
            return null;
        }
    }
}

