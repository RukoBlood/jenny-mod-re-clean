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
package com.trolmastercard.sexmod.proxy;

import java.io.IOException;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.gui.ModGuiHandler;
import com.trolmastercard.sexmod.util.Handlers.*;
import com.trolmastercard.sexmod.world.WorldGenStructure;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void preInitRegistries(FMLPreInitializationEvent fMLPreInitializationEvent) {
        GameRegistry.registerWorldGenerator((IWorldGenerator) WorldGenStructure.Generate(), 0);
        EntityInnit.Register();
        ItemHandler.RegisterItems();
    }

    public void initRegistries(FMLInitializationEvent fMLInitializationEvent) throws IOException {
        Main.setConfigs();
        SoundsHandler.RegisterSounds();
        net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler((Object) Main.instance, (IGuiHandler)new ModGuiHandler());
        EventHandler.Register(false);
        PackageHandler.RegisterMessages();
    }

    public void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) throws IOException {
        this.setUpCustomModelsOnServer();
    }

    void setUpCustomModelsOnServer() {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
            return;
        }
        CustomModel.LoadModels(false);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

