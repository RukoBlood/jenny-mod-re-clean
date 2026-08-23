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
    final static public List<BlockPos> TEMP_POSITIONS = new ArrayList<>();
    //final static String d = "sexmod:galath_spawn_list";
    //final static String a = "sexmod:galath_spawn_list";

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
    public void onSave(WorldEvent.Save save) {
        World world = save.getWorld();
        world.getMapStorage().setData("sexmod:galath_spawn_list", this);
        this.markDirty();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load load) {
        World world = load.getWorld();
        world.getMapStorage().getOrLoadData(StructureTracker.class, "sexmod:galath_spawn_list");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound nBTTagCompound2 = nbt.getCompoundTag("sexmod:galath_spawn_list");
        this.readNBT(nBTTagCompound2, "", STRUCTURE_POSITIONS);
        this.readNBT(nBTTagCompound2, "mang", TEMP_POSITIONS);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        this.writeNBT(nBTTagCompound2, "", STRUCTURE_POSITIONS);
        this.writeNBT(nBTTagCompound2, "mang", TEMP_POSITIONS);
        nbt.setTag("sexmod:galath_spawn_list", nBTTagCompound2);
        return nbt;
    }

    void writeNBT(NBTTagCompound nbt, String key, List<BlockPos> positions) {
        nbt.setInteger("sexmod:pos_amount" + key, positions.size());
        int i = 0;
        for (BlockPos pos : positions) {
            nbt.setInteger("sexmod:x" + key + i, pos.getX());
            nbt.setInteger("sexmod:y" + key + i, pos.getY());
            nbt.setInteger("sexmod:z" + key + i, pos.getZ());
            ++i;
        }
    }

    void readNBT(NBTTagCompound nbt, String key, List<BlockPos> positions) {
        positions.clear();
        int count = nbt.getInteger("sexmod:pos_amount" + key);
        for (int i = 0; i < count; ++i) {
            positions.add(new BlockPos(
                    nbt.getInteger("sexmod:x" + key + i),
                    nbt.getInteger("sexmod:y" + key + i),
                    nbt.getInteger("sexmod:z" + key + i)
            ));
        }
    }
}

