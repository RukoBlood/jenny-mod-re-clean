/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class eo_class262 {
    @SubscribeEvent(priority=EventPriority.LOW)
    public void a(LivingDeathEvent livingDeathEvent) {
        if (livingDeathEvent.getEntity() instanceof GirlEntity) {
            GirlEntity em_class2582 = (GirlEntity)livingDeathEvent.getEntity();
            GirlEntity.GirlEntityList().remove(em_class2582);
        }
    }
}

