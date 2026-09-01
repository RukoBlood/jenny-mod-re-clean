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
    ItemStack[] stacks;
    UUID girlId;
    UUID playerId;

    public UploadInventoryToServer() {
    }

    public UploadInventoryToServer(UUID uUID, UUID uUID2, ItemStack[] itemStackArray) {
        this.girlId = uUID;
        this.stacks = itemStackArray;
        this.playerId = uUID2;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.playerId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        int n = byteBuf.readInt();
        this.stacks = new ItemStack[n];
        for (int i = 0; i < n; ++i) {
            this.stacks[i] = ByteBufUtils.readItemStack(byteBuf);
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.playerId.toString());
        byteBuf.writeInt(this.stacks.length);
        for (ItemStack stack : this.stacks) {
            ByteBufUtils.writeItemStack(byteBuf, stack);
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
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girlId);
                for (GirlEntity girl : girls) {
                    if (!girl.world.isRemote) {
                        EntityPlayer player = girl.world.getPlayerEntityByUUID(msg.playerId);
                        if (player != null) {
                            InventoryPlayer inventory = player.inventory;
                            for (int i = 0; i < 36; ++i) {
                                inventory.setInventorySlotContents(i, msg.stacks[i]);
                            }
                            if (girl instanceof LunaEntity) {
                                Fighter fighter = (Fighter) girl;
                                fighter.inventory.setStackInSlot(0, msg.stacks[36]);
                                fighter.inventory.setStackInSlot(1, msg.stacks[37]);
                                fighter.inventory.setStackInSlot(2, msg.stacks[38]);
                                fighter.inventory.setStackInSlot(3, msg.stacks[39]);
                                fighter.inventory.setStackInSlot(4, msg.stacks[40]);
                                fighter.inventory.setStackInSlot(5, msg.stacks[41]);
                                fighter.inventory.setStackInSlot(6, msg.stacks[42]);
                            } else if (girl instanceof Fighter) {
                                Fighter fighter = (Fighter) girl;
                                fighter.inventory.setStackInSlot(0, msg.stacks[36]);
                                fighter.inventory.setStackInSlot(1, msg.stacks[37]);
                                fighter.inventory.setStackInSlot(2, msg.stacks[38]);
                                fighter.inventory.setStackInSlot(3, msg.stacks[39]);
                                fighter.inventory.setStackInSlot(4, msg.stacks[40]);
                                fighter.inventory.setStackInSlot(5, msg.stacks[41]);
                            }
                            if (girl instanceof Supporter) {
                                Supporter supporter = (Supporter) girl;
                                for (int i = 0; i < 27; ++i) {
                                    supporter.invHandler.setStackInSlot(i, msg.stacks[i + 36]);
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

