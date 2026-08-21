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
import com.trolmastercard.sexmod.girls.Bee.BeeEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BeeOpenChest implements IMessage {
    boolean isValid = false;
    UUID girls;
    UUID playerId;

    public BeeOpenChest() {
    }

    public BeeOpenChest(UUID uUID, UUID uUID2) {
        this.girls = uUID;
        this.playerId = uUID2;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girls = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.playerId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girls.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.playerId.toString());
    }

    public static class Handler implements IMessageHandler<BeeOpenChest, IMessage> {
        @Override
        public IMessage onMessage(BeeOpenChest msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("received an invalid message @BeeOpenChest :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girls);
                for (GirlEntity girl : girls) {
                    EntityPlayerMP player;
                    BeeEntity bee;
                    if (!girl.world.isRemote
                            && girl instanceof BeeEntity
                            && (bee = (BeeEntity) girl).getDataManager().get(BeeEntity.IS_TAMED)
                            && (player = (EntityPlayerMP) bee.world.getPlayerEntityByUUID(msg.playerId)) != null) {
                        player.openGui(Main.instance, 1, girl.world, girl.getPosition().getX(), girl.getPosition().getY(), girl.getPosition().getZ());
                        return;
                    }
                }
            });
            return null;
        }
    }
}

