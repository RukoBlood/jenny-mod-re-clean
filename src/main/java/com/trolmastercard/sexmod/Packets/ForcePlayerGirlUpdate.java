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
    UUID c;
    int b;
    Action a;

    public ForcePlayerGirlUpdate() {
    }

    public ForcePlayerGirlUpdate(UUID uUID, int n, Action action) {
        this.c = uUID;
        this.b = n;
        this.a = action;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.b = byteBuf.readInt();
        this.a = Action.valueOf(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.c.toString());
        byteBuf.writeInt(this.b);
        ByteBufUtils.writeUTF8String(byteBuf, this.a.toString());
    }

    public static class Handler implements IMessageHandler<ForcePlayerGirlUpdate, IMessage> {

        @Override
        public IMessage onMessage(ForcePlayerGirlUpdate msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.CLIENT)) {
                System.out.println("received an invalid message @ForcePlayerGirlUpdate :(");
                return null;
            }
            PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(msg.c);
            if (playerGirl == null) {
                return null;
            }
            playerGirl.getDataManager().set(GirlEntity.CUR_ACTION, msg.a.toString());
            playerGirl.getDataManager().set(GirlEntity.OUTFIT_INDEX, msg.b);
            return null;
        }
    }
}

