/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.items.CapabilityItemHandler
 *  net.minecraftforge.items.IItemHandler
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class d4_class162
extends Container {
    GirlEntity b;
    public Slot[] d;
    public UUID a;
    static public List<d4_class162> c = new ArrayList<d4_class162>();

    public d4_class162(GirlEntity em_class2582, InventoryPlayer inventoryPlayer, UUID uUID) {
        this.a = uUID;
        c.add(this);
        if (em_class2582.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            int n;
            IItemHandler iItemHandler = (IItemHandler)em_class2582.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            this.b = em_class2582;
            this.d = new Slot[]{new fe_class305(fe_class305.b_inner307.WEAPON, iItemHandler, fe_class305.b_inner307.WEAPON.id, 31, 60), new fe_class305(fe_class305.b_inner307.BOW, iItemHandler, fe_class305.b_inner307.BOW.id, 50, 60), new fe_class305(fe_class305.b_inner307.HELMET, iItemHandler, fe_class305.b_inner307.HELMET.id, 72, 60), new fe_class305(fe_class305.b_inner307.CHEST_PLATE, iItemHandler, fe_class305.b_inner307.CHEST_PLATE.id, 91, 60), new fe_class305(fe_class305.b_inner307.PANTS, iItemHandler, fe_class305.b_inner307.PANTS.id, 110, 60), new fe_class305(fe_class305.b_inner307.SHOES, iItemHandler, fe_class305.b_inner307.SHOES.id, 129, 60)};
            ArrayList<Slot> arrayList = new ArrayList<Slot>();
            for (n = 0; n < 3; ++n) {
                for (int i = 0; i < 9; ++i) {
                    arrayList.add(new Slot(inventoryPlayer, i + n * 9 + 9, 8 + i * 18, 84 + n * 18));
                }
            }
            for (n = 0; n < 9; ++n) {
                arrayList.add(new Slot(inventoryPlayer, n, 8 + n * 18, 142));
            }
            for (Slot slot : this.d) {
                this.addSlotToContainer(slot);
            }
            for (Slot slot : arrayList) {
                this.addSlotToContainer(slot);
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            int n2 = this.inventorySlots.size() - entityPlayer.inventory.mainInventory.size();
            if (n < n2 ? !this.mergeItemStack(itemStack2, n2, this.inventorySlots.size(), true) : !this.mergeItemStack(itemStack2, 0, n2, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            slot.onTake(entityPlayer, itemStack2);
        }
        return itemStack;
    }

    @Override
    public void putStackInSlot(int n, ItemStack itemStack) {
        super.putStackInSlot(n, itemStack);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

