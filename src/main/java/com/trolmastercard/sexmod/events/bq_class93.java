/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.events;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class bq_class93 {
    @SubscribeEvent
    public void a(GuiOpenEvent guiOpenEvent) {
        if (guiOpenEvent.getGui() instanceof GuiMainMenu || guiOpenEvent.getGui() instanceof GuiMultiplayer) {
            PlayerGirl.Z.clear();
            PlayerGirl.playerGirlUUIDHashtable.clear();
        }
    }
}

