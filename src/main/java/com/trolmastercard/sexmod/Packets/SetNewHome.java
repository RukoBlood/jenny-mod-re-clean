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
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetNewHome
implements IMessage {
    boolean b;
    UUID c;
    Vec3d a;

    public SetNewHome() {
    }

    public SetNewHome(UUID uUID, Vec3d vec3d) {
        this.c = uUID;
        this.a = vec3d;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        byteBuf.writeDouble(this.a.x);
        byteBuf.writeDouble(this.a.y);
        byteBuf.writeDouble(this.a.z);
    }

    public static class a_inner14
    implements IMessageHandler<SetNewHome, IMessage> {
        public IMessage a(SetNewHome a6_class132, MessageContext messageContext) {
            if (!a6_class132.b) {
                System.out.println("received an invalid message @SetNewHome :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(a6_class132.c);
                if (arrayList.isEmpty()) {
                    return;
                }
                for (GirlEntity em_class2582 : arrayList) {
                    em_class2582.homeCoords = new Vec3d(a6_class132.a.x, Math.floor(a6_class132.a.y), a6_class132.a.z);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(SetNewHome iMessage, MessageContext messageContext) {
            return this.a((SetNewHome)iMessage, messageContext);
        }
    }
}

