/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NameStorage extends WorldSavedData {
    final static String DATA_IDENTIFIER = "sexmod:customstaticgirlnames";
    final static HashMap<UUID, HashMap<PlayerGirlEntity, String>> GLOBAL_NAMES_MAP = new HashMap();

    public NameStorage() {
        super(DATA_IDENTIFIER);
    }

    public NameStorage(String identifier) {
        super(DATA_IDENTIFIER);
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        World world = event.getWorld();
        assert world.getMapStorage() != null;
        world.getMapStorage().setData(DATA_IDENTIFIER, this);
        this.markDirty();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        assert world.getMapStorage() != null;
        world.getMapStorage().getOrLoadData(NameStorage.class, DATA_IDENTIFIER);
    }

    public static void setCustomName(UUID uUID, PlayerGirlEntity pg, String name) {
        HashMap<PlayerGirlEntity, String> playerNames = GLOBAL_NAMES_MAP.get(uUID);
        if (playerNames == null) {
            playerNames = new HashMap();
        }
        playerNames.put(pg, name);
        GLOBAL_NAMES_MAP.put(uUID, playerNames);
    }

    @Nullable
    public static String getCustomName(UUID uUID, PlayerGirlEntity pg) {
        HashMap<PlayerGirlEntity, String> playerNames = GLOBAL_NAMES_MAP.get(uUID);
        if (playerNames == null) {
            return null;
        }
        return playerNames.get((Object)pg);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        for (String string : nBTTagCompound.getKeySet()) {
            UUID uUID;
            try {
                uUID = UUID.fromString(string);
            } catch (IllegalArgumentException illegalArgumentException) {
                continue;
            }
            GLOBAL_NAMES_MAP.put(uUID, this.deserializePlayerNames(nBTTagCompound.getCompoundTag(string)));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        for (Map.Entry<UUID, HashMap<PlayerGirlEntity, String>> entry : GLOBAL_NAMES_MAP.entrySet()) {
            UUID uUID = entry.getKey();
            nBTTagCompound.setTag(uUID.toString(), this.serializePlayerNames(entry.getValue()));
        }
        return nBTTagCompound;
    }

    private NBTTagCompound serializePlayerNames(HashMap<PlayerGirlEntity, String> names) {
        NBTTagCompound playerNBT = new NBTTagCompound();
        for (Map.Entry<PlayerGirlEntity, String> entry : names.entrySet()) {
            playerNBT.setString(entry.getKey().name(), entry.getValue());
        }
        return playerNBT;
    }

    private HashMap<PlayerGirlEntity, String> deserializePlayerNames(NBTTagCompound playerNBT) {
        HashMap<PlayerGirlEntity, String> hashMap = new HashMap<PlayerGirlEntity, String>();
        for (PlayerGirlEntity pg : PlayerGirlEntity.values()) {
            String customName = playerNBT.getString(pg.name());
            if (customName.isEmpty()) continue;
            hashMap.put(pg, customName);
        }
        return hashMap;
    }
}

