/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.GirlEntity;

import java.util.HashMap;
import java.util.UUID;

public class fs_class327 {
    static HashMap<UUID, GirlEntity> a = new HashMap();

    public static void b(GirlEntity girlEntity) {
        a.put(girlEntity.girlID(), girlEntity);
    }

    public static void a(GirlEntity girlEntity) {
        a.remove(girlEntity.girlID());
    }

    public static void a() {
        a.clear();
    }

    public static GirlEntity a(UUID uUID) {
        return a.get(uUID);
    }
}

