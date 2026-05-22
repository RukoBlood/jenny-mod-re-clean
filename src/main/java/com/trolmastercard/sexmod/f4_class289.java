/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteractSpecific
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class f4_class289 {
    @SubscribeEvent
    public void a(PlayerInteractEvent.EntityInteractSpecific entityInteractSpecific) {
        ItemStack itemStack;
        Entity entity = entityInteractSpecific.getTarget();
        if (!(entity instanceof GirlEntity)) {
            return;
        }
        EntityPlayer entityPlayer = entityInteractSpecific.getEntityPlayer();
        if (entityPlayer.getHeldItemMainhand().getItem() == Items.NAME_TAG) {
            itemStack = entityPlayer.getHeldItemMainhand();
        } else if (entityPlayer.getHeldItemOffhand().getItem() == Items.NAME_TAG) {
            itemStack = entityPlayer.getHeldItemOffhand();
        } else {
            return;
        }
        String string = itemStack.getDisplayName();
        if (string.isEmpty()) {
            return;
        }
        ((GirlEntity)entity).g(string);
        if (!entityPlayer.capabilities.isCreativeMode) {
            itemStack.shrink(1);
        }
        entityInteractSpecific.setCanceled(true);
        entityInteractSpecific.setResult(Event.Result.DENY);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

