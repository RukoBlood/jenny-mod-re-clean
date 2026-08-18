/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.companion.fighter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest2;

//df.class
public class WatchClosestGirlGoal extends EntityAIWatchClosest2 {
    public boolean isWatching = true;

    public WatchClosestGirlGoal(EntityLiving entityLiving, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
        super(entityLiving, watchTargetClass, maxDistance, chanceIn);
    }

    @Override
    public void updateTask() {
        if (this.isWatching) {
            super.updateTask();
        }
    }
}

