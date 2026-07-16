/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.trolmastercard.sexmod.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class DeprecatedCheckForUpdates {
    //https://schnurritv.com/version.txt
    final String updateURL = new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 115, 99, 104, 110, 117, 114, 114, 105, 116, 118, 46, 99, 111, 109, 47, 118, 101, 114, 115, 105, 111, 110, 46, 116, 120, 116});
    //https://twitter.com/Schnurri_tv
    final String TwitterURL = new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 116, 119, 105, 116, 116, 101, 114, 46, 99, 111, 109, 47, 83, 99, 104, 110, 117, 114, 114, 105, 95, 116, 118});

    boolean hasCheckedForUpdates = false;

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        /*
        * This function should check for updates
        * But because mod was given to trolmastercard (idiot)
        * All the code was removed
        * Also why the fuck schnurri obfuscated strings like that???
        * */
    }
}

