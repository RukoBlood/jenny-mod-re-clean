/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.items.ItemStackHandler
 */
package com.trolmastercard.sexmod.girls.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public abstract class Supporter extends GirlEntity implements IInventory {

    final static public DataParameter<Boolean> HAS_CHEST;
    public ItemStackHandler invHandler = new ItemStackHandler(27);

    static {
        HAS_CHEST = EntityDataManager.createKey(GirlEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(111);
    }

    protected Supporter(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(HAS_CHEST, false);
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= this.invHandler.getSlots()) {
            return ItemStack.EMPTY;
        }
        return this.invHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.invHandler.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.invHandler.extractItem(index, this.invHandler.getStackInSlot(index).getCount(), false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.invHandler.setStackInSlot(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return id;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
    }
}

