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

public class GuiOpenHandler {
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiMultiplayer) {
            PlayerGirl.playerGirlList.clear();
            PlayerGirl.playerGirlUUIDHashtable.clear();
        }
    }
}

