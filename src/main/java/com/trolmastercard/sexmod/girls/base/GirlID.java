/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.base;

import java.util.HashMap;
import java.util.UUID;

public class GirlID {
    static HashMap<UUID, GirlEntity> GirlEntityIDList = new HashMap();

    public static void PutGirlInList(GirlEntity girlEntity) {
        GirlEntityIDList.put(girlEntity.girlID(), girlEntity);
    }

    public static void RemoveGirlInList(GirlEntity girlEntity) {
        GirlEntityIDList.remove(girlEntity.girlID());
    }

    public static void ClearGirlList() {
        GirlEntityIDList.clear();
    }

    public static GirlEntity GetGirlID(UUID uUID) {
        return GirlEntityIDList.get(uUID);
    }
}

