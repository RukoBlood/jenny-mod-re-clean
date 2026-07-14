/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package com.trolmastercard.sexmod.util;

import com.trolmastercard.sexmod.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientServerCheck {
    public static boolean getInstance() {
        String thread = Thread.currentThread().getName().toLowerCase();
        if (thread.contains("server")) {
            return true;
        }
        if (thread.contains("client")) {
            return false;
        }
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) {
            return false;
        }
        boolean bl = server.isCallingFromMinecraftThread();
        Main.LOGGER.warn("couldn't clarify if is running on a server or client thread. Came to the solution onServer=" + bl);
        return bl;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

