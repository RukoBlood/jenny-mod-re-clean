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

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RemoveItems implements IMessage {
    boolean isValid = false;
    UUID playerId;
    ItemStack requiredItem;

    public RemoveItems() {
    }

    public RemoveItems(UUID uUID, ItemStack itemStack) {
        this.playerId = uUID;
        this.requiredItem = itemStack;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.playerId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.requiredItem = ByteBufUtils.readItemStack(byteBuf);
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.playerId.toString());
        ByteBufUtils.writeItemStack(byteBuf, this.requiredItem);
    }

    public static class Handler implements IMessageHandler<RemoveItems, IMessage> {
        @Override
        public IMessage onMessage(RemoveItems msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("recieved an unvalid message @RemoveItems :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                InventoryPlayer inventoryPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(msg.playerId).inventory;
                for (int i = 0; i < inventoryPlayer.getSizeInventory(); ++i) {
                    ItemStack itemStack = inventoryPlayer.getStackInSlot(i);
                    if (!itemStack.getItem().equals(msg.requiredItem.getItem())) continue;
                    itemStack.shrink(msg.requiredItem.getCount());
                    break;
                }
            });
            return null;
        }
    }
}

