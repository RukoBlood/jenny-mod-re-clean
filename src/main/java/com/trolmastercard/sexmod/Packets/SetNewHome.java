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
        this.c = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.a = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.c.toString());
        byteBuf.writeDouble(this.a.x);
        byteBuf.writeDouble(this.a.y);
        byteBuf.writeDouble(this.a.z);
    }

    public static class Handler implements IMessageHandler<SetNewHome, IMessage> {
        @Override
        public IMessage onMessage(SetNewHome msg, MessageContext ctx) {
            if (!msg.b) {
                System.out.println("received an invalid message @SetNewHome :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(msg.c);
                if (arrayList.isEmpty()) {
                    return;
                }
                for (GirlEntity em_class2582 : arrayList) {
                    em_class2582.homeCoords = new Vec3d(msg.a.x, Math.floor(msg.a.y), msg.a.z);
                }
            });
            return null;
        }
    }
}

