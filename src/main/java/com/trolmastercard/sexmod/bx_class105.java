/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class bx_class105
extends Container {
    final private IInventory a;
    final private int d;
    static public List<bx_class105> b = new ArrayList<bx_class105>();
    public UUID c;

    public bx_class105(IInventory iInventory, IInventory iInventory2, EntityPlayer entityPlayer, UUID uUID) {
        int n;
        int n2;
        this.c = uUID;
        b.add(this);
        this.a = iInventory2;
        iInventory2.openInventory(entityPlayer);
        this.d = 3;
        int n3 = -18;
        for (n2 = 0; n2 < 3; ++n2) {
            for (n = 0; n < 9; ++n) {
                this.addSlotToContainer(new Slot(iInventory2, n + n2 * 9, 8 + n * 18, 18 + n2 * 18));
            }
        }
        for (n2 = 0; n2 < 3; ++n2) {
            for (n = 0; n < 9; ++n) {
                this.addSlotToContainer(new Slot(iInventory, n + n2 * 9 + 9, 8 + n * 18, 103 + n2 * 18 + n3));
            }
        }
        for (n2 = 0; n2 < 9; ++n2) {
            this.addSlotToContainer(new Slot(iInventory, n2, 8 + n2 * 18, 161 + n3));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.a.isUsableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n < this.d * 9 ? !this.mergeItemStack(itemStack2, this.d * 9, this.inventorySlots.size(), true) : !this.mergeItemStack(itemStack2, 0, this.d * 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        this.a.closeInventory(entityPlayer);
    }

    public IInventory a() {
        return this.a;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

