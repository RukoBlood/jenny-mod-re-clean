/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GirlWorldData extends WorldSavedData {
    //final static String NBT_1 = "sexmod:static_custom_model_manager";
    //final static String NBT_2 = "sexmod:static_custom_model_manager";
    static public HashMap<UUID, String> galath = new HashMap();
    static public HashMap<UUID, String> manglelie = new HashMap();

    public GirlWorldData() {
        super("sexmod:static_custom_model_manager");
    }

    public GirlWorldData(String string) {
        super("sexmod:static_custom_model_manager");
    }

    public static String getCustomModelCode(GirlEntity girl) {
        String code = buildModelCode(girl);
        return code == null ? "" : code;
    }

    private static String buildModelCode(GirlEntity girl) {
        if (girl instanceof GalathEntity) {
            UUID girlId = girl.girlID();
            UUID ownerId = GalathMangTracker.getManglelieOwnerId(girlId);
            if (ownerId == null) {
                ownerId = girlId;
            }
            return galath.get(ownerId);
        }
        if (girl instanceof ManglelieEntity) {
            UUID uUID = GalathMangTracker.getManglelieOwnerId(((ManglelieEntity)girl).getCorruptPlayerUUID());
            return manglelie.get(uUID == null ? girl.girlID() : uUID);
        }
        return null;
    }

    public static void setCustomModelCode(GirlEntity girl) {
        if (girl instanceof GalathEntity) {
            UUID uUID = girl.girlID();
            UUID uUID2 = GalathMangTracker.getManglelieOwnerId(uUID);
            if (uUID2 == null) {
                uUID2 = uUID;
            }
            galath.put(uUID2, girl.getCustomModelCode());
            return;
        }
        if (girl instanceof ManglelieEntity) {
            UUID uUID = GalathMangTracker.getManglelieOwnerId(((ManglelieEntity)girl).getCorruptPlayerUUID());
            manglelie.put(uUID == null ? girl.girlID() : uUID, girl.getCustomModelCode());
        }
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        World world = event.getWorld();
        world.getMapStorage().setData("sexmod:static_custom_model_manager", this);
        this.markDirty();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        world.getMapStorage().getOrLoadData(GirlWorldData.class, "sexmod:static_custom_model_manager");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = nbt.getCompoundTag("sexmod:static_custom_model_manager");
        this.writeNBT(tag.getCompoundTag("galath"), galath);
        this.writeNBT(tag.getCompoundTag("mang"), manglelie);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("galath", this.serializeOwnership(galath));
        tag.setTag("mang", this.serializeOwnership(manglelie));
        nbt.setTag("sexmod:static_custom_model_manager", tag);
        return nbt;
    }

    NBTTagCompound serializeOwnership(HashMap<UUID, String> ownershipMap) {
        NBTTagCompound tag = new NBTTagCompound();
        int i = 0;
        for (Map.Entry<UUID, String> entries : ownershipMap.entrySet()) {
            UUID uUID = entries.getKey();
            tag.setString("UUID" + i, uUID.toString());
            tag.setString("MODEL" + i, entries.getValue());
            ++i;
        }
        return tag;
    }

    void writeNBT(NBTTagCompound nbt, HashMap<UUID, String> ownershipMap) {
        int i = 0;
        String ids;
        while (!(ids = nbt.getString("UUID" + i)).isEmpty()) {
            ownershipMap.put(UUID.fromString(ids), nbt.getString("MODEL" + i));
            ++i;
        }
    }

    public static void clearAll() {
        galath.clear();
        manglelie.clear();
    }
}

