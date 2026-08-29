/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.trolmastercard.sexmod.events;

import javax.swing.JFrame;

import com.trolmastercard.sexmod.gui.PornWarningWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PornWarning extends JFrame {
    public boolean didIt = false;

    @SubscribeEvent
    public void PornWarning(TickEvent.ClientTickEvent event) {
        if (!this.didIt) {
            this.didIt = true;
            PornWarningWindow.Launch();
        }
    }
}

