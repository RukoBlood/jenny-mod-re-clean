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
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class LunaContainer extends Container {
    LunaEntity luna;
    public Slot[] slots;
    public UUID containerID;
    static public List<LunaContainer> OPEN_CONTAINERS = new ArrayList<LunaContainer>();

    public LunaContainer(LunaEntity luna, InventoryPlayer playerInv, UUID ID) {
        this.containerID = ID;
        OPEN_CONTAINERS.add(this);

        if (luna.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            int row;
            IItemHandler itemHandler = (IItemHandler)luna.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            this.luna = luna;

            this.slots = new Slot[]{
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.WEAPON, itemHandler, EquipmentSlot.GirlEquipmentType.WEAPON.id, 41, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.BOW, itemHandler, EquipmentSlot.GirlEquipmentType.BOW.id, 59, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.HELMET, itemHandler, EquipmentSlot.GirlEquipmentType.HELMET.id, 81, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.CHEST_PLATE, itemHandler, EquipmentSlot.GirlEquipmentType.CHEST_PLATE.id, 100, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.PANTS, itemHandler, EquipmentSlot.GirlEquipmentType.PANTS.id, 119, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.SHOES, itemHandler, EquipmentSlot.GirlEquipmentType.SHOES.id, 138, 60),
                    new EquipmentSlot(EquipmentSlot.GirlEquipmentType.ROD, itemHandler, EquipmentSlot.GirlEquipmentType.ROD.id, 22, 60)
            };

            ArrayList<Slot> playerInvSlots = new ArrayList<Slot>();

            for (row = 0; row < 3; ++row) {
                for (int col = 0; col < 9; ++col) {
                    playerInvSlots.add(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
                }
            }

            for (row = 0; row < 9; ++row) {
                playerInvSlots.add(new Slot(playerInv, row, 8 + row * 18, 142));
            }
            for (Slot slot : this.slots) {
                this.addSlotToContainer(slot);
            }
            for (Slot slot : playerInvSlots) {
                this.addSlotToContainer(slot);
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            itemStack = stackInSlot.copy();
            int customSlotsCount = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
            if (index < customSlotsCount
                    ? !this.mergeItemStack(stackInSlot, customSlotsCount, this.inventorySlots.size(), true)
                    : !this.mergeItemStack(stackInSlot, 0, customSlotsCount, false)) {
                return ItemStack.EMPTY;
            }
            if (stackInSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            slot.onTake(playerIn, stackInSlot);
        }
        return itemStack;
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        super.putStackInSlot(slotID, stack);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
    }
}

