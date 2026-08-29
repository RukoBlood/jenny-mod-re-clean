package com.trolmastercard.sexmod.events;

import com.trolmastercard.sexmod.girls.Galath.CumDrainDamageSource;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GirlCombatProtection {
    @SubscribeEvent
    public void onGirlAttack(LivingAttackEvent event) {
        if (event.getSource() != DamageSource.OUT_OF_WORLD) {
            if (event.getEntity() instanceof GirlEntity) {
                GirlEntity girl = (GirlEntity) event.getEntity();
                if (girl instanceof PlayerGirl) {
                    event.setCanceled(true);
                } else {
                    event.setCanceled(girl.getInteractionPlayerUUID() != null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event) {
        DamageSource damageSource = event.getSource();
        if (damageSource != DamageSource.OUT_OF_WORLD && !(damageSource instanceof CumDrainDamageSource)) {
            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                GirlEntity activeGirl = GirlEntity.getGirlByUUID(player.getPersistentID());
                if (activeGirl != null) {
                    if (activeGirl.getDistance(player) < 1.0f) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}

