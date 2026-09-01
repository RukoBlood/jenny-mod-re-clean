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

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ForcePlayerGirlUpdate implements IMessage {
    boolean isValid = false;
    UUID id;
    int outfitIndex;
    Action action;

    public ForcePlayerGirlUpdate() {
    }

    public ForcePlayerGirlUpdate(UUID uUID, int n, Action action) {
        this.id = uUID;
        this.outfitIndex = n;
        this.action = action;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.outfitIndex = byteBuf.readInt();
        this.action = Action.valueOf(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.id.toString());
        byteBuf.writeInt(this.outfitIndex);
        ByteBufUtils.writeUTF8String(byteBuf, this.action.toString());
    }

    public static class Handler implements IMessageHandler<ForcePlayerGirlUpdate, IMessage> {

        @Override
        public IMessage onMessage(ForcePlayerGirlUpdate msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @ForcePlayerGirlUpdate :(");
                return null;
            }
            PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(msg.id);
            if (playerGirl == null) {
                return null;
            }
            playerGirl.getDataManager().set(GirlEntity.CUR_ACTION, msg.action.toString());
            playerGirl.getDataManager().set(GirlEntity.OUTFIT_INDEX, msg.outfitIndex);
            return null;
        }
    }
}

