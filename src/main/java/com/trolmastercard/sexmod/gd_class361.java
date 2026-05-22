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
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class gd_class361
implements IMessage {
    boolean d = false;
    UUID c;
    int b;
    Action a;

    public gd_class361() {
    }

    public gd_class361(UUID uUID, int n, Action fp_class3242) {
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
    implements IMessageHandler<gd_class361, IMessage> {
        public IMessage a(gd_class361 gd_class3612, MessageContext messageContext) {
            if (!gd_class3612.d || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @ForcePlayerGirlUpdate :(");
                return null;
            }
            PlayerGirl ei_class2512 = PlayerGirl.d_(gd_class3612.c);
            if (ei_class2512 == null) {
                return null;
            }
            ei_class2512.getDataManager().set(GirlEntity.J, gd_class3612.a.toString());
            ei_class2512.getDataManager().set(GirlEntity.D, gd_class3612.b);
            return null;
        }

                @Override
        public IMessage onMessage(gd_class361 iMessage, MessageContext messageContext) {
            return this.a((gd_class361)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

