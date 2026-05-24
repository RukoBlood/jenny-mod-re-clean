/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.items.ItemStackHandler
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public abstract class Supporter
extends GirlEntity
implements IInventory {
    final static public DataParameter<Boolean> K = EntityDataManager.createKey(GirlEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(111);
    public ItemStackHandler L = new ItemStackHandler(27);

    protected Supporter(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(K, false);
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
    public ItemStack getStackInSlot(int n) {
        if (n >= this.L.getSlots()) {
            return ItemStack.EMPTY;
        }
        return this.L.getStackInSlot(n);
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        return this.L.extractItem(n, n2, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        return this.L.extractItem(n, this.L.getStackInSlot(n).getCount(), false);
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.L.setStackInSlot(n, itemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getField(int n) {
        return n;
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

