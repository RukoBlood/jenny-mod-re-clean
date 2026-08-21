/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateVelocity implements IMessage {
    boolean c = false;
    Vec3d b;
    UUID a;

    public UpdateVelocity(Vec3d vec3d, UUID uUID) {
        this.b = vec3d;
        this.a = uUID;
    }

    public UpdateVelocity() {
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.a = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.b.x);
        byteBuf.writeDouble(this.b.y);
        byteBuf.writeDouble(this.b.z);
        ByteBufUtils.writeUTF8String(byteBuf, this.a.toString());
    }

    public static class Handler implements IMessageHandler<UpdateVelocity, IMessage> {
        @Override
        public IMessage onMessage(UpdateVelocity msg, MessageContext ctx) {
            if (!msg.c || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid message @UpdateVelocity :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girl = GirlEntity.getServerGirlEntity(msg.a);
                if (girl instanceof GalathEntity) {
                    GalathEntity galath = (GalathEntity) girl;
                    if (ctx.getServerHandler().player.equals(galath.getRidingPlayer())) {
                        galath.applyVelocityDelta(msg.b);
                    }
                }
            });
            return null;
        }
    }
}

