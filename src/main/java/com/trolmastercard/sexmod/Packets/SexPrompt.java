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

import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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
    boolean isValid = false;
    String action;
    UUID player;
    UUID ownerId;
    boolean isGuiPending;

    public SexPrompt() {
    }

    public SexPrompt(String string, UUID uUID, UUID uUID2, boolean bl) {
        this.action = string;
        this.player = uUID;
        this.ownerId = uUID2;
        this.isGuiPending = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.action = ByteBufUtils.readUTF8String(byteBuf);
        this.player = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.ownerId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isGuiPending = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.action);
        ByteBufUtils.writeUTF8String(byteBuf, this.player.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.ownerId.toString());
        byteBuf.writeBoolean(this.isGuiPending);
    }

    public static class Handler implements IMessageHandler<SexPrompt, IMessage> {
        @Override
        public IMessage onMessage(SexPrompt msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("received an invalid message @SexPrompt :(");
                return null;
            }
            if (ctx.side.equals(Side.CLIENT)) {
                SexPromptManager.INSTANCE.setNewActivePrompt(new SexPromptManager.SexPrompt(msg.action, msg.player, msg.ownerId, msg.isGuiPending));
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                World world = ctx.getServerHandler().player.world;
                EntityPlayer entityPlayer = world.getPlayerEntityByUUID(msg.ownerId);
                EntityPlayer entityPlayer2 = world.getPlayerEntityByUUID(msg.player);
                if (entityPlayer == null) {
                    System.out.println("Sex prompt invalid -> female player not found");
                    return;
                }
                if (entityPlayer2 == null) {
                    System.out.println("Sex prompt invalid -> male player not found");
                    return;
                }
                PacketHandler.INSTANCE.sendTo(new SexPrompt(msg.action, msg.player, msg.ownerId, msg.isGuiPending), (EntityPlayerMP)(msg.isGuiPending ? entityPlayer : entityPlayer2));
            });
            return null;
        }
    }
}

