/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.IWorldGenerator
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.network.IGuiHandler
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.registry.GameRegistry
 */
package com.trolmastercard.sexmod;

import java.io.IOException;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void preInitRegistries(FMLPreInitializationEvent fMLPreInitializationEvent) {
        GameRegistry.registerWorldGenerator((IWorldGenerator) WorldGeneration.Generate(), 0);
        EntityInitialize.Register();
        ItemRegister.RegisterItems();
    }

    public void initRegistries(FMLInitializationEvent fMLInitializationEvent) throws IOException {
        Main.setConfigs();
        SoundEventHandler.RegisterSounds();
        net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler((Object) Main.instance, (IGuiHandler)new et_class272());
        bn_class89.a(false);
        NetworkRegistry.RegisterMessages();
    }

    public void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) throws IOException {
        this.setUpCustomModelsOnServer();
    }

    void setUpCustomModelsOnServer() {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
            return;
        }
        CustomModels.c(false);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

