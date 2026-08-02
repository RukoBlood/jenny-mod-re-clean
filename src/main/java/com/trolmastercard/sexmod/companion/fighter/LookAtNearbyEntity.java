/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.companion.fighter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest2;

//df.class
public class LookAtNearbyEntity extends EntityAIWatchClosest2 {
    public boolean ShouldLook = true;

    public LookAtNearbyEntity(EntityLiving entityLiving, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
        super(entityLiving, watchTargetClass, maxDistance, chanceIn);
    }

    @Override
    public void updateTask() {
        if (this.ShouldLook) {
            super.updateTask();
        }
    }
}

