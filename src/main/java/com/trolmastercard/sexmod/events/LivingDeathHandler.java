/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.events;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingDeathHandler {
    @SubscribeEvent(priority=EventPriority.LOW)
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof GirlEntity) {
            GirlEntity entity = (GirlEntity)event.getEntity();
            GirlEntity.getGirlEntityList().remove(entity);
        }
    }
}

