/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StructureTracker extends WorldSavedData {
    final static public List<BlockPos> STRUCTURE_POSITIONS = new ArrayList<BlockPos>();
    final static public List<BlockPos> TEMP_POSITIONS = new ArrayList<BlockPos>();
    final static String d = "sexmod:galath_spawn_list";
    final static String a = "sexmod:galath_spawn_list";

    public StructureTracker() {
        super("sexmod:galath_spawn_list");
    }

    public StructureTracker(String string) {
        super("sexmod:galath_spawn_list");
    }

    public static void addPosInList(BlockPos pos, List<BlockPos> positions) {
        positions.add(pos);
    }

    @SubscribeEvent
    public void SaveSpawnList(WorldEvent.Save save) {
        World world = save.getWorld();
        world.getMapStorage().setData("sexmod:galath_spawn_list", this);
        this.markDirty();
    }

    @SubscribeEvent
    public void LoadSpawnList(WorldEvent.Load load) {
        World world = load.getWorld();
        world.getMapStorage().getOrLoadData(StructureTracker.class, "sexmod:galath_spawn_list");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound nBTTagCompound2 = nbt.getCompoundTag("sexmod:galath_spawn_list");
        this.b(nBTTagCompound2, "", STRUCTURE_POSITIONS);
        this.b(nBTTagCompound2, "mang", TEMP_POSITIONS);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        this.a(nBTTagCompound2, "", STRUCTURE_POSITIONS);
        this.a(nBTTagCompound2, "mang", TEMP_POSITIONS);
        nbt.setTag("sexmod:galath_spawn_list", nBTTagCompound2);
        return nbt;
    }

    void a(NBTTagCompound nbt, String string, List<BlockPos> positions) {
        nbt.setInteger("sexmod:pos_amount" + string, positions.size());
        int n = 0;
        for (BlockPos pos : positions) {
            nbt.setInteger("sexmod:x" + string + n, pos.getX());
            nbt.setInteger("sexmod:y" + string + n, pos.getY());
            nbt.setInteger("sexmod:z" + string + n, pos.getZ());
            ++n;
        }
    }

    void b(NBTTagCompound nBTTagCompound, String string, List<BlockPos> list) {
        list.clear();
        int n = nBTTagCompound.getInteger("sexmod:pos_amount" + string);
        for (int i = 0; i < n; ++i) {
            list.add(new BlockPos(
                    nBTTagCompound.getInteger("sexmod:x" + string + i),
                    nBTTagCompound.getInteger("sexmod:y" + string + i),
                    nBTTagCompound.getInteger("sexmod:z" + string + i)
            ));
        }
    }
}

