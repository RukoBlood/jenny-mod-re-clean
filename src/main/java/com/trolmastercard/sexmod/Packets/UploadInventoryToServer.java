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

import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.Supporter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UploadInventoryToServer implements IMessage {
    boolean isValid = false;
    ItemStack[] d;
    UUID a;
    UUID c;

    public UploadInventoryToServer() {
    }

    public UploadInventoryToServer(UUID uUID, UUID uUID2, ItemStack[] itemStackArray) {
        this.a = uUID;
        this.d = itemStackArray;
        this.c = uUID2;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.c = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        int n = byteBuf.readInt();
        this.d = new ItemStack[n];
        for (int i = 0; i < n; ++i) {
            this.d[i] = ByteBufUtils.readItemStack(byteBuf);
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.a.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.c.toString());
        byteBuf.writeInt(this.d.length);
        for (ItemStack itemStack : this.d) {
            ByteBufUtils.writeItemStack(byteBuf, itemStack);
        }
    }

    public static class Handler implements IMessageHandler<UploadInventoryToServer, IMessage> {
        @Override
        public IMessage onMessage(UploadInventoryToServer msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @UploadInventoryToServer :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.a);
                for (GirlEntity girl : girls) {
                    if (!girl.world.isRemote) {
                        EntityPlayer player = girl.world.getPlayerEntityByUUID(msg.c);
                        if (player != null) {
                            InventoryPlayer inventory = player.inventory;
                            for (int i = 0; i < 36; ++i) {
                                inventory.setInventorySlotContents(i, msg.d[i]);
                            }
                            if (girl instanceof LunaEntity) {
                                Fighter fighter = (Fighter) girl;
                                fighter.inventory.setStackInSlot(0, msg.d[36]);
                                fighter.inventory.setStackInSlot(1, msg.d[37]);
                                fighter.inventory.setStackInSlot(2, msg.d[38]);
                                fighter.inventory.setStackInSlot(3, msg.d[39]);
                                fighter.inventory.setStackInSlot(4, msg.d[40]);
                                fighter.inventory.setStackInSlot(5, msg.d[41]);
                                fighter.inventory.setStackInSlot(6, msg.d[42]);
                            } else if (girl instanceof Fighter) {
                                Fighter e2_class2183 = (Fighter) girl;
                                e2_class2183.inventory.setStackInSlot(0, msg.d[36]);
                                e2_class2183.inventory.setStackInSlot(1, msg.d[37]);
                                e2_class2183.inventory.setStackInSlot(2, msg.d[38]);
                                e2_class2183.inventory.setStackInSlot(3, msg.d[39]);
                                e2_class2183.inventory.setStackInSlot(4, msg.d[40]);
                                e2_class2183.inventory.setStackInSlot(5, msg.d[41]);
                            }
                            if (girl instanceof Supporter) {
                                Supporter supporter = (Supporter) girl;
                                for (int i = 0; i < 27; ++i) {
                                    supporter.invHandler.setStackInSlot(i, msg.d[i + 36]);
                                }
                            }
                        }
                    }
                }
            });
            return null;
        }
    }
}

