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

import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.gender_change.SexPromptManager;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SexPrompt implements IMessage {
    boolean e = false;
    String c;
    UUID b;
    UUID a;
    boolean d;

    public SexPrompt() {
    }

    public SexPrompt(String string, UUID uUID, UUID uUID2, boolean bl) {
        this.c = string;
        this.b = uUID;
        this.a = uUID2;
        this.d = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = ByteBufUtils.readUTF8String(byteBuf);
        this.b = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.a = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.d = byteBuf.readBoolean();
        this.e = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.c);
        ByteBufUtils.writeUTF8String(byteBuf, this.b.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.a.toString());
        byteBuf.writeBoolean(this.d);
    }

    public static class Handler implements IMessageHandler<SexPrompt, IMessage> {
        @Override
        public IMessage onMessage(SexPrompt msg, MessageContext ctx) {
            if (!msg.e) {
                System.out.println("received an invalid message @SexPrompt :(");
                return null;
            }
            if (ctx.side.equals(Side.CLIENT)) {
                SexPromptManager.INSTANCE.setNewActivePrompt(new SexPromptManager.SexPrompt(msg.c, msg.b, msg.a, msg.d));
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                World world = ctx.getServerHandler().player.world;
                EntityPlayer entityPlayer = world.getPlayerEntityByUUID(msg.a);
                EntityPlayer entityPlayer2 = world.getPlayerEntityByUUID(msg.b);
                if (entityPlayer == null) {
                    System.out.println("Sex prompt invalid -> female player not found");
                    return;
                }
                if (entityPlayer2 == null) {
                    System.out.println("Sex prompt invalid -> male player not found");
                    return;
                }
                PackageHandler.INSTANCE.sendTo(new SexPrompt(msg.c, msg.b, msg.a, msg.d), (EntityPlayerMP)(msg.d ? entityPlayer : entityPlayer2));
            });
            return null;
        }
    }
}

