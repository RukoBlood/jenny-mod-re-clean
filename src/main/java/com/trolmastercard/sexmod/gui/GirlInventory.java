/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GirlInventory extends Container {
    final private IInventory inv;
    final private int rows;
    static public List<GirlInventory> activeContainers = new ArrayList<GirlInventory>();
    public UUID uuid;

    public GirlInventory(IInventory playerInv, IInventory otherInv, EntityPlayer player, UUID uUID) {
        int slotX;
        int slotY;
        this.uuid = uUID;
        activeContainers.add(this);
        this.inv = otherInv;
        otherInv.openInventory(player);

        this.rows = 3;
        int yOffset = -18;

        for (slotY = 0; slotY < 3; ++slotY) {
            for (slotX = 0; slotX < 9; ++slotX) {
                this.addSlotToContainer(new Slot(otherInv, slotX + slotY * 9, 8 + slotX * 18, 18 + slotY * 18));
            }
        }

        for (slotY = 0; slotY < 3; ++slotY) {
            for (slotX = 0; slotX < 9; ++slotX) {
                this.addSlotToContainer(new Slot(playerInv, slotX + slotY * 9 + 9, 8 + slotX * 18, 103 + slotY * 18 + yOffset));
            }
        }

        for (slotY = 0; slotY < 9; ++slotY) {
            this.addSlotToContainer(new Slot(playerInv, slotY, 8 + slotY * 18, 161 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.inv.isUsableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int idx) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(idx);
        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if (idx < this.rows * 9 ? !this.mergeItemStack(stackInSlot, this.rows * 9, this.inventorySlots.size(), true) : !this.mergeItemStack(stackInSlot, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        this.inv.closeInventory(entityPlayer);
    }

    public IInventory getGirlInv() {
        return this.inv;
    }
}

