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
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetPlayerForGirl implements IMessage {
    boolean isValid;
    UUID c;
    UUID b;

    public SetPlayerForGirl() {
        this.isValid = false;
    }

    public SetPlayerForGirl(UUID uUID, UUID uUID2) {
        this.c = uUID;
        this.b = uUID2;
        this.isValid = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.b = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.c.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.b.toString());
    }

    public static class Handler implements IMessageHandler<SetPlayerForGirl, IMessage> {
        @Override
        public IMessage onMessage(SetPlayerForGirl msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @SetPlayerForGirl :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(msg.c);
                for (GirlEntity girl : arrayList) {
                    PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                    try {
                        playerList.getPlayerByUUID(msg.b).getName();
                    } catch (NullPointerException nullPointerException) {
                        System.out.println("couldn't find player with UUID: " + msg.b);
                        System.out.println("could only find players with thsese UUID's:");
                        for (EntityPlayerMP entityPlayerMP : playerList.getPlayers()) {
                            System.out.println(entityPlayerMP.getName() + " " + entityPlayerMP.getUniqueID());
                        }
                        continue;
                    }
                    if (girl instanceof JennyEntity) {
                        ((JennyEntity)girl).shouldStartDoggySex = true;
                    }
                    girl.setInteractionPlayerUUID(msg.b);
                }
            });
            return null;
        }
    }
}

