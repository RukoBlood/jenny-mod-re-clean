/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class bj_class84
extends WorldSavedData {
    final static String a = "sexmod:static_custom_model_manager";
    final static String d = "sexmod:static_custom_model_manager";
    static public HashMap<UUID, String> c = new HashMap();
    static public HashMap<UUID, String> b = new HashMap();

    public bj_class84() {
        super("sexmod:static_custom_model_manager");
    }

    public bj_class84(String string) {
        super("sexmod:static_custom_model_manager");
    }

    public static String c(GirlEntity em_class2582) {
        String string = bj_class84.b(em_class2582);
        if (string == null) {
            return "";
        }
        return string;
    }

    private static String b(GirlEntity em_class2582) {
        if (em_class2582 instanceof GalathEntity) {
            UUID uUID = em_class2582.girlID();
            UUID uUID2 = GalathMangTracker.f(uUID);
            if (uUID2 == null) {
                uUID2 = uUID;
            }
            return c.get(uUID2);
        }
        if (em_class2582 instanceof ManglelieEntity) {
            UUID uUID = GalathMangTracker.f(((ManglelieEntity)em_class2582).java_util_UUID_v());
            return b.get(uUID == null ? em_class2582.girlID() : uUID);
        }
        return null;
    }

    public static void a(GirlEntity em_class2582) {
        if (em_class2582 instanceof GalathEntity) {
            UUID uUID = em_class2582.girlID();
            UUID uUID2 = GalathMangTracker.f(uUID);
            if (uUID2 == null) {
                uUID2 = uUID;
            }
            c.put(uUID2, em_class2582.java_lang_String_C());
            return;
        }
        if (em_class2582 instanceof ManglelieEntity) {
            UUID uUID = GalathMangTracker.f(((ManglelieEntity)em_class2582).java_util_UUID_v());
            b.put(uUID == null ? em_class2582.girlID() : uUID, em_class2582.java_lang_String_C());
        }
    }

    @SubscribeEvent
    public void a(WorldEvent.Save save) {
        World world = save.getWorld();
        world.getMapStorage().setData("sexmod:static_custom_model_manager", this);
        this.markDirty();
    }

    @SubscribeEvent
    public void a(WorldEvent.Load load) {
        World world = load.getWorld();
        world.getMapStorage().getOrLoadData(bj_class84.class, "sexmod:static_custom_model_manager");
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("sexmod:static_custom_model_manager");
        this.a(nBTTagCompound2.getCompoundTag("galath"), c);
        this.a(nBTTagCompound2.getCompoundTag("mang"), b);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        nBTTagCompound2.setTag("galath", this.a(c));
        nBTTagCompound2.setTag("mang", this.a(b));
        nBTTagCompound.setTag("sexmod:static_custom_model_manager", nBTTagCompound2);
        return nBTTagCompound;
    }

    NBTTagCompound a(HashMap<UUID, String> hashMap) {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        int n = 0;
        for (Map.Entry<UUID, String> entry : hashMap.entrySet()) {
            UUID uUID = entry.getKey();
            nBTTagCompound.setString("UUID" + n, uUID.toString());
            nBTTagCompound.setString("MODEL" + n, entry.getValue());
            ++n;
        }
        return nBTTagCompound;
    }

    void a(NBTTagCompound nBTTagCompound, HashMap<UUID, String> hashMap) {
        int n = 0;
        String string;
        while (!"".equals(string = nBTTagCompound.getString("UUID" + n))) {
            hashMap.put(UUID.fromString(string), nBTTagCompound.getString("MODEL" + n));
            ++n;
        }
        return;
    }

    public static void a() {
        c.clear();
        b.clear();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

