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

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnParticle
implements IMessage {
    boolean d = false;
    UUID c;
    String b;
    int a;

    public SpawnParticle() {
    }

    public SpawnParticle(UUID uUID, String string) {
        this.c = uUID;
        this.b = string;
        this.a = 1;
    }

    public SpawnParticle(UUID uUID, String string, int n) {
        this.c = uUID;
        this.b = string;
        this.a = n;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.a = byteBuf.readInt();
        this.d = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b);
        byteBuf.writeInt(this.a);
    }

    public static class a_inner261
    implements IMessageHandler<SpawnParticle, IMessage> {
        public IMessage a(SpawnParticle en_class2602, MessageContext messageContext) {
            if (!en_class2602.d || !messageContext.side.equals((Object)Side.CLIENT)) {
                System.out.println("received an invalid message @SpawnParticle :(");
                return null;
            }
            ArrayList<GirlEntity> arrayList = GirlEntity.girlList(en_class2602.c);
            for (GirlEntity em_class2582 : arrayList) {
                if (!em_class2582.world.isRemote) continue;
                for (int i = 0; i < en_class2602.a; ++i) {
                    GirlEntity.spawnParticlesAround(EnumParticleTypes.getByName(en_class2602.b), em_class2582);
                }
            }
            return null;
        }

                @Override
        public IMessage onMessage(SpawnParticle iMessage, MessageContext messageContext) {
            return this.a((SpawnParticle)iMessage, messageContext);
        }
    }
}

