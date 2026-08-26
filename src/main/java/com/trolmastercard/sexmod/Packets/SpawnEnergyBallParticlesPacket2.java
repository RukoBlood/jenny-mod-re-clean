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

import com.trolmastercard.sexmod.girls.Galath.EnergyBall.EnergyBallEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnEnergyBallParticlesPacket2 implements IMessage {
    Vec3d energyPos;
    boolean isLeftSide;
    boolean isValid = false;

    public SpawnEnergyBallParticlesPacket2() {
    }

    public SpawnEnergyBallParticlesPacket2(Vec3d vec3d, boolean bl) {
        this.energyPos = vec3d;
        this.isLeftSide = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.energyPos = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.isLeftSide = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.energyPos.x);
        byteBuf.writeDouble(this.energyPos.y);
        byteBuf.writeDouble(this.energyPos.z);
        byteBuf.writeBoolean(this.isLeftSide);
    }

    public static class Handler implements IMessageHandler<SpawnEnergyBallParticlesPacket2, IMessage> {

        @Override
        public IMessage onMessage(SpawnEnergyBallParticlesPacket2 msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnEnergyBallParticles :(");
                return null;
            }
            if (msg.isLeftSide) {
                EnergyBallEntity.spawnDragonBreath(msg.energyPos);
            } else {
                EnergyBallEntity.spawnDragonBreathRandom(msg.energyPos);
            }
            return null;
        }
    }
}

