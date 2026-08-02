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

import com.trolmastercard.sexmod.companion.fighter.EquipmentSlot;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class d4_class162 extends Container {
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

            this.d = new Slot[]{
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.WEAPON, iItemHandler, EquipmentSlot.GirlEquipmentType.WEAPON.id, 31, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.BOW, iItemHandler, EquipmentSlot.GirlEquipmentType.BOW.id, 50, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.HELMET, iItemHandler, EquipmentSlot.GirlEquipmentType.HELMET.id, 72, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.CHEST_PLATE, iItemHandler, EquipmentSlot.GirlEquipmentType.CHEST_PLATE.id, 91, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.PANTS, iItemHandler, EquipmentSlot.GirlEquipmentType.PANTS.id, 110, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.SHOES, iItemHandler, EquipmentSlot.GirlEquipmentType.SHOES.id, 129, 60)
            };

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

}

