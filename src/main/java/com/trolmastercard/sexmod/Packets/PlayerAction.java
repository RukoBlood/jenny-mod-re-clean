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

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerAction implements IMessage {
    boolean isValid;
    UUID girlId;
    UUID playerId;

    public PlayerAction() {
    }

    public PlayerAction(UUID uUID, UUID uUID2) {
        this.girlId = uUID;
        this.playerId = uUID2;
        this.isValid = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.playerId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.playerId.toString());
    }

    public static class Handler implements IMessageHandler<PlayerAction, IMessage> {
        @Override
        public IMessage onMessage(PlayerAction msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                    if (!girl.world.isRemote && girl.girlID().equals(msg.girlId)) {
                        girl.world.getPlayerEntityByUUID(msg.playerId).openGui(Main.instance, 0, girl.world, girl.getPosition().getX(), girl.getPosition().getY(), girl.getPosition().getZ());
                    }
                }
            });
            return null;
        }
    }
}

