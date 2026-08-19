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
import com.trolmastercard.sexmod.girls.Kobold.EyeAndKoboldColor;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClaimTribe
implements IMessage {
    boolean valid = false;
    UUID d;
    UUID a;
    String b;

    public ClaimTribe() {
    }

    public ClaimTribe(UUID uUID, UUID uUID2, String string) {
        this.d = uUID;
        this.a = uUID2;
        this.b = string;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.d = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.d.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b);
    }

    public static class a_inner355
    implements IMessageHandler<ClaimTribe, IMessage> {
        public IMessage a(ClaimTribe msg, MessageContext ctx) {
            if (!msg.valid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @ClaimTribe :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                List<KoboldEntity> list = KoboldManager.getTribeMembersList(msg.d);
                EyeAndKoboldColor eyeAndKoboldColor_ = null;
                for (KoboldEntity object2 : list) {
                    if (object2.hasMaster()) continue;
                    EntityDataManager entityDataManager = object2.getDataManager();
                    entityDataManager.set(GirlEntity.MASTER, msg.a.toString());
                    entityDataManager.set(KoboldEntity.aU, msg.b);
                    eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.CURRENT_ACTION));
                }
                if (eyeAndKoboldColor_ == null) {
                    return;
                }
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
                String string = ctx.getServerHandler().player.getName();
                for (EntityPlayer entityPlayer : playerList.getPlayers()) {
                    entityPlayer.sendMessage(new TextComponentString(String.format("%s formed the " + (Object)((Object) eyeAndKoboldColor_.getTextColor()) + "%s " + (Object)((Object)TextFormatting.WHITE) + "Tribe", string, msg.b)));
                }
                KoboldManager.setTribeAlerted(msg.d, true);
                KoboldManager.setTribeMaster(msg.d, ctx.getServerHandler().player.getPersistentID());
            });
            return null;
        }

                @Override
        public IMessage onMessage(ClaimTribe iMessage, MessageContext messageContext) {
            return this.a((ClaimTribe)iMessage, messageContext);
        }
    }
}

