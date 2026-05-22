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
package com.trolmastercard.sexmod;

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

public class t_class423
implements IMessage {
    boolean c = false;
    UUID a;
    ItemStack b;

    public t_class423() {
    }

    public t_class423(UUID uUID, ItemStack itemStack) {
        this.a = uUID;
        this.b = itemStack;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = ByteBufUtils.readItemStack((ByteBuf)byteBuf);
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        ByteBufUtils.writeItemStack((ByteBuf)byteBuf, (ItemStack)this.b);
    }

    public static class a_inner424
    implements IMessageHandler<t_class423, IMessage> {
        public IMessage a(t_class423 t_class4232, MessageContext messageContext) {
            if (!t_class4232.c || messageContext.side != Side.SERVER) {
                System.out.println("recieved an unvalid message @RemoveItems :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                InventoryPlayer inventoryPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID((UUID)t_class4232.a).inventory;
                for (int i = 0; i < inventoryPlayer.getSizeInventory(); ++i) {
                    ItemStack itemStack = inventoryPlayer.getStackInSlot(i);
                    if (!itemStack.getItem().equals(t_class4232.b.getItem())) continue;
                    itemStack.shrink(t_class4232.b.getCount());
                    break;
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(t_class423 iMessage, MessageContext messageContext) {
            return this.a((t_class423)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

