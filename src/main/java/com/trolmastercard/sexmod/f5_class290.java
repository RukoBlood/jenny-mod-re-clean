/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.EnumPacketDirection;

public class f5_class290
extends NetHandlerPlayClient {
    public f5_class290(Minecraft minecraft) {
        super(minecraft, minecraft.currentScreen, new x(EnumPacketDirection.CLIENTBOUND), minecraft.getSession().getProfile());
    }
}

