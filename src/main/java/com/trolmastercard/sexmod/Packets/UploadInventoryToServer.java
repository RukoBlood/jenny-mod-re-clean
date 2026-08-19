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

public class UploadInventoryToServer
implements IMessage {
    boolean b = false;
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
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        int n = byteBuf.readInt();
        this.d = new ItemStack[n];
        for (int i = 0; i < n; ++i) {
            this.d[i] = ByteBufUtils.readItemStack((ByteBuf)byteBuf);
        }
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        byteBuf.writeInt(this.d.length);
        for (ItemStack itemStack : this.d) {
            ByteBufUtils.writeItemStack((ByteBuf)byteBuf, (ItemStack)itemStack);
        }
    }

    public static class a_inner61
    implements IMessageHandler<UploadInventoryToServer, IMessage> {
        public IMessage a(UploadInventoryToServer b1_class602, MessageContext messageContext) {
            if (!b1_class602.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @UploadInventoryToServer :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(b1_class602.a);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote) continue;
                    EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(b1_class602.c);
                    if (entityPlayer == null) {
                        return;
                    }
                    InventoryPlayer inventoryPlayer = entityPlayer.inventory;
                    for (int i = 0; i < 36; ++i) {
                        inventoryPlayer.setInventorySlotContents(i, b1_class602.d[i]);
                    }
                    if (em_class2582 instanceof LunaEntity) {
                        Fighter fighter = (Fighter)em_class2582;
                        fighter.inventory.setStackInSlot(0, b1_class602.d[36]);
                        fighter.inventory.setStackInSlot(1, b1_class602.d[37]);
                        fighter.inventory.setStackInSlot(2, b1_class602.d[38]);
                        fighter.inventory.setStackInSlot(3, b1_class602.d[39]);
                        fighter.inventory.setStackInSlot(4, b1_class602.d[40]);
                        fighter.inventory.setStackInSlot(5, b1_class602.d[41]);
                        fighter.inventory.setStackInSlot(6, b1_class602.d[42]);
                    } else if (em_class2582 instanceof Fighter) {
                        Fighter e2_class2183 = (Fighter)em_class2582;
                        e2_class2183.inventory.setStackInSlot(0, b1_class602.d[36]);
                        e2_class2183.inventory.setStackInSlot(1, b1_class602.d[37]);
                        e2_class2183.inventory.setStackInSlot(2, b1_class602.d[38]);
                        e2_class2183.inventory.setStackInSlot(3, b1_class602.d[39]);
                        e2_class2183.inventory.setStackInSlot(4, b1_class602.d[40]);
                        e2_class2183.inventory.setStackInSlot(5, b1_class602.d[41]);
                    }
                    if (!(em_class2582 instanceof Supporter)) continue;
                    Supporter fo_class3232 = (Supporter)em_class2582;
                    for (int i = 0; i < 27; ++i) {
                        fo_class3232.invHandler.setStackInSlot(i, b1_class602.d[i + 36]);
                    }
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(UploadInventoryToServer iMessage, MessageContext messageContext) {
            return this.a((UploadInventoryToServer)iMessage, messageContext);
        }
    }
}

