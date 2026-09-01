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

public class SetNewHome implements IMessage {
    boolean valid;
    UUID girlId;
    Vec3d pos;

    public SetNewHome() {
    }

    public SetNewHome(UUID uUID, Vec3d vec3d) {
        this.girlId = uUID;
        this.pos = vec3d;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.pos = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        byteBuf.writeDouble(this.pos.x);
        byteBuf.writeDouble(this.pos.y);
        byteBuf.writeDouble(this.pos.z);
    }

    public static class Handler implements IMessageHandler<SetNewHome, IMessage> {
        @Override
        public IMessage onMessage(SetNewHome msg, MessageContext ctx) {
            if (!msg.valid) {
                System.out.println("received an invalid message @SetNewHome :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(msg.girlId);
                if (arrayList.isEmpty()) {
                    return;
                }
                for (GirlEntity girl : arrayList) {
                    girl.homeCoords = new Vec3d(msg.pos.x, Math.floor(msg.pos.y), msg.pos.z);
                }
            });
            return null;
        }
    }
}

