/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.trolmastercard.sexmod;

import javax.swing.JFrame;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ShowWarning extends JFrame {
    public boolean firstlaunch = false;

    @SubscribeEvent
    public void a(TickEvent.ClientTickEvent clientTickEvent) {
        if (this.firstlaunch) {
            return;
        }
        this.firstlaunch = true;
        PornWarning.ShowWarning();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

