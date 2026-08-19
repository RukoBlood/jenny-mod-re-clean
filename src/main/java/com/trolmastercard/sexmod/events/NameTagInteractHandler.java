/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteractSpecific
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.events;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NameTagInteractHandler {
    @SubscribeEvent
    public void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        ItemStack nametagItem;
        Entity entity = event.getTarget();
        if (entity instanceof GirlEntity) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.getHeldItemMainhand().getItem() == Items.NAME_TAG) {
                nametagItem = player.getHeldItemMainhand();
            } else if (player.getHeldItemOffhand().getItem() == Items.NAME_TAG) {
                nametagItem = player.getHeldItemOffhand();
            } else {
                return;
            }

            String displayName = nametagItem.getDisplayName();
            if (!displayName.isEmpty()) {
                ((GirlEntity) entity).setCustomNameOverride(displayName);
                if (!player.capabilities.isCreativeMode) {
                    nametagItem.shrink(1);
                }
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }
}

