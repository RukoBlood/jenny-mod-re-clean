/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.EnumPacketDirection;

public class FakeNetHandler extends NetHandlerPlayClient {
    public FakeNetHandler(Minecraft mc) {
        super(mc, mc.currentScreen, new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), mc.getSession().getProfile());
    }
}

