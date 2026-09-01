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
    boolean isValid = false;
    Vec3d delta;
    UUID girlId;

    public UpdateVelocity(Vec3d vec3d, UUID uUID) {
        this.delta = vec3d;
        this.girlId = uUID;
    }

    public UpdateVelocity() {
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.delta = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.delta.x);
        byteBuf.writeDouble(this.delta.y);
        byteBuf.writeDouble(this.delta.z);
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
    }

    public static class Handler implements IMessageHandler<UpdateVelocity, IMessage> {
        @Override
        public IMessage onMessage(UpdateVelocity msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid message @UpdateVelocity :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girl = GirlEntity.getServerGirlEntity(msg.girlId);
                if (girl instanceof GalathEntity) {
                    GalathEntity galath = (GalathEntity) girl;
                    if (ctx.getServerHandler().player.equals(galath.getRidingPlayer())) {
                        galath.applyVelocityDelta(msg.delta);
                    }
                }
            });
            return null;
        }
    }
}

