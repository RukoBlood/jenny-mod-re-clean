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

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class StartStandingSexAnimation
implements IMessage {
    boolean isValid;
    UUID receiver;
    UUID sender;
    String animationId;

    public StartStandingSexAnimation() {
    }

    public StartStandingSexAnimation(UUID uUID, UUID uUID2, String string) {
        this.receiver = uUID;
        this.sender = uUID2;
        this.animationId = string;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.receiver = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.sender = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.animationId = ByteBufUtils.readUTF8String(byteBuf);
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.receiver.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.sender.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.animationId);
    }

    public static class Handler implements IMessageHandler<StartStandingSexAnimation, IMessage> {
        @Override
        public IMessage onMessage(StartStandingSexAnimation msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @StartStandingSexAnimation :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(msg.receiver);
                if (playerGirl != null) {
                    if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
                        for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                            if (girl instanceof PlayerGirl) {
                                playerGirl = (PlayerGirl) girl;
                                if (!playerGirl.world.isRemote && playerGirl.getOwnerUserUUID().equals(msg.receiver)) {
                                    break;
                                }
                            }
                        }
                    }
                    playerGirl.handleOwnerCommand(msg.animationId, msg.sender);
                }
            });
            return null;
        }
    }
}

