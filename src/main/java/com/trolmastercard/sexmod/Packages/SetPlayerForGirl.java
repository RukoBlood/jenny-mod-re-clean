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
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.girls.GirlEntity;
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

public class SetPlayerForGirl
implements IMessage {
    boolean a;
    UUID c;
    UUID b;

    public SetPlayerForGirl() {
        this.a = false;
    }

    public SetPlayerForGirl(UUID uUID, UUID uUID2) {
        this.c = uUID;
        this.b = uUID2;
        this.a = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
    }

    public static class a_inner59
    implements IMessageHandler<SetPlayerForGirl, IMessage> {
        public IMessage a(SetPlayerForGirl b0_class582, MessageContext messageContext) {
            if (!b0_class582.a || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @SetPlayerForGirl :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(b0_class582.c);
                for (GirlEntity em_class2582 : arrayList) {
                    PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                    try {
                        playerList.getPlayerByUUID(b0_class582.b).getName();
                    } catch (NullPointerException nullPointerException) {
                        System.out.println("couldn't find player with UUID: " + b0_class582.b);
                        System.out.println("could only find players with thsese UUID's:");
                        for (EntityPlayerMP entityPlayerMP : playerList.getPlayers()) {
                            System.out.println(entityPlayerMP.getName() + " " + entityPlayerMP.getUniqueID());
                        }
                        continue;
                    }
                    if (em_class2582 instanceof JennyEntity) {
                        ((JennyEntity)em_class2582).af = true;
                    }
                    em_class2582.void_e(b0_class582.b);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(SetPlayerForGirl iMessage, MessageContext messageContext) {
            return this.a((SetPlayerForGirl)iMessage, messageContext);
        }

        private static NullPointerException a(NullPointerException nullPointerException) {
            return nullPointerException;
        }
    }
}

