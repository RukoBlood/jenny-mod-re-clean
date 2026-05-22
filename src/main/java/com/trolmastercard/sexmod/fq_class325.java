/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class fq_class325
extends WorldSavedData {
    final static public List<BlockPos> c = new ArrayList<BlockPos>();
    final static public List<BlockPos> b = new ArrayList<BlockPos>();
    final static String d = "sexmod:galath_spawn_list";
    final static String a = "sexmod:galath_spawn_list";

    public fq_class325() {
        super("sexmod:galath_spawn_list");
    }

    public fq_class325(String string) {
        super("sexmod:galath_spawn_list");
    }

    public static void a(BlockPos blockPos, List<BlockPos> list) {
        list.add(blockPos);
    }

    @SubscribeEvent
    public void a(WorldEvent.Save save) {
        World world = save.getWorld();
        world.getMapStorage().setData("sexmod:galath_spawn_list", this);
        this.markDirty();
    }

    @SubscribeEvent
    public void a(WorldEvent.Load load) {
        World world = load.getWorld();
        world.getMapStorage().getOrLoadData(fq_class325.class, "sexmod:galath_spawn_list");
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("sexmod:galath_spawn_list");
        this.b(nBTTagCompound2, "", c);
        this.b(nBTTagCompound2, "mang", b);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        this.a(nBTTagCompound2, "", c);
        this.a(nBTTagCompound2, "mang", b);
        nBTTagCompound.setTag("sexmod:galath_spawn_list", nBTTagCompound2);
        return nBTTagCompound;
    }

    void a(NBTTagCompound nBTTagCompound, String string, List<BlockPos> list) {
        nBTTagCompound.setInteger("sexmod:pos_amount" + string, list.size());
        int n = 0;
        for (BlockPos blockPos : list) {
            nBTTagCompound.setInteger("sexmod:x" + string + n, blockPos.getX());
            nBTTagCompound.setInteger("sexmod:y" + string + n, blockPos.getY());
            nBTTagCompound.setInteger("sexmod:z" + string + n, blockPos.getZ());
            ++n;
        }
    }

    void b(NBTTagCompound nBTTagCompound, String string, List<BlockPos> list) {
        list.clear();
        int n = nBTTagCompound.getInteger("sexmod:pos_amount" + string);
        for (int i = 0; i < n; ++i) {
            list.add(new BlockPos(nBTTagCompound.getInteger("sexmod:x" + string + i), nBTTagCompound.getInteger("sexmod:y" + string + i), nBTTagCompound.getInteger("sexmod:z" + string + i)));
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

