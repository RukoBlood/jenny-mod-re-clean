/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingAttackEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ah_class29 {
    @SubscribeEvent
    public void b(LivingAttackEvent livingAttackEvent) {
        if (livingAttackEvent.getSource() == DamageSource.OUT_OF_WORLD) {
            return;
        }
        if (!(livingAttackEvent.getEntity() instanceof GirlEntity)) {
            return;
        }
        GirlEntity em_class2582 = (GirlEntity)livingAttackEvent.getEntity();
        if (em_class2582 instanceof PlayerGirl) {
            livingAttackEvent.setCanceled(true);
        } else {
            livingAttackEvent.setCanceled(em_class2582.getID() != null);
        }
    }

    @SubscribeEvent
    public void a(LivingAttackEvent livingAttackEvent) {
        DamageSource damageSource = livingAttackEvent.getSource();
        if (damageSource == DamageSource.OUT_OF_WORLD || damageSource instanceof a3_class10) {
            return;
        }
        if (!(livingAttackEvent.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)livingAttackEvent.getEntity();
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_i(entityPlayer.getPersistentID());
        if (em_class2582 == null) {
            return;
        }
        if (em_class2582.getDistance(entityPlayer) < 1.0f) {
            livingAttackEvent.setCanceled(true);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

