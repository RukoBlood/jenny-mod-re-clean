/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.trolmastercard.sexmod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class c6_class115 {
    final String c = new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 115, 99, 104, 110, 117, 114, 114, 105, 116, 118, 46, 99, 111, 109, 47, 118, 101, 114, 115, 105, 111, 110, 46, 116, 120, 116});
    final String a = new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 116, 119, 105, 116, 116, 101, 114, 46, 99, 111, 109, 47, 83, 99, 104, 110, 117, 114, 114, 105, 95, 116, 118});
    boolean b = false;

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(TickEvent.ClientTickEvent clientTickEvent) {
    }
}

