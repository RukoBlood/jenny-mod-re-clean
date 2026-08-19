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

public class ForcePlayerGirlUpdate
implements IMessage {
    boolean d = false;
    UUID c;
    int b;
    Action a;

    public ForcePlayerGirlUpdate() {
    }

    public ForcePlayerGirlUpdate(UUID uUID, int n, Action fp_class3242) {
        this.c = uUID;
        this.b = n;
        this.a = fp_class3242;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = byteBuf.readInt();
        this.a = Action.valueOf(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.d = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        byteBuf.writeInt(this.b);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
    }

    public static class a_inner362
    implements IMessageHandler<ForcePlayerGirlUpdate, IMessage> {
        public IMessage a(ForcePlayerGirlUpdate gd_class3612, MessageContext messageContext) {
            if (!gd_class3612.d || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @ForcePlayerGirlUpdate :(");
                return null;
            }
            PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(gd_class3612.c);
            if (ei_class2512 == null) {
                return null;
            }
            ei_class2512.getDataManager().set(GirlEntity.CUR_ACTION, gd_class3612.a.toString());
            ei_class2512.getDataManager().set(GirlEntity.OUTFIT_INDEX, gd_class3612.b);
            return null;
        }

                @Override
        public IMessage onMessage(ForcePlayerGirlUpdate iMessage, MessageContext messageContext) {
            return this.a((ForcePlayerGirlUpdate)iMessage, messageContext);
        }
    }
}

