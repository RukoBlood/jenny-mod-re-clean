/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest2;

public class df_class178
extends EntityAIWatchClosest2 {
    public boolean a = true;

    public df_class178(EntityLiving entityLiving, Class<? extends Entity> clazz, float f, float f2) {
        super(entityLiving, clazz, f, f2);
    }

    @Override
    public void updateTask() {
        if (this.a) {
            super.updateTask();
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

