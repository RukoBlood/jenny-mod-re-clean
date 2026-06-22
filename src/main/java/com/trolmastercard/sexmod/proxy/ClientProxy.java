/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.ClientCommandHandler
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.network.IGuiHandler
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 */
package com.trolmastercard.sexmod.proxy;

import java.io.IOException;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.RenderHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Handlers.bn_Handler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ClientProxy
extends CommonProxy {
    static public boolean IS_PRELOADING = false;
    static public KeyBinding[] keyBindings;

    @Override
    public void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) throws IOException {
    }

    @Override
    public void preInitRegistries(FMLPreInitializationEvent fMLPreInitializationEvent) {
        super.preInitRegistries(fMLPreInitializationEvent);
        RenderHandler.Register();
    }

    @Override
    public void initRegistries(FMLInitializationEvent fMLInitializationEvent) throws IOException {
        keyBindings = new KeyBinding[2];
        ClientProxy.keyBindings[0] = new KeyBinding("Interact with your goblin", 34, "Sex mod");
        ClientProxy.keyBindings[1] = new KeyBinding("open character customisation menu", 76, "Sex mod");
        for (KeyBinding kb : keyBindings) {
            ClientRegistry.registerKeyBinding((KeyBinding)kb);
        }
        Main.setConfigs();
        SoundsHandler.RegisterSounds();
        net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler((Object) Main.instance, (IGuiHandler)new et_class272(true));
        bn_Handler.a(true);
        PackageHandler.RegisterMessages();
        Minecraft minecraft = Minecraft.getMinecraft();
        RenderManager renderManager = minecraft.getRenderManager();
        FakeWorld gj_class3722 = new FakeWorld();
        IS_PRELOADING = true;
        try {
            for (PlayerGirlEntity fy_class3352 : PlayerGirlEntity.values()) {
                renderManager.renderEntity(fy_class3352.npcClass.getDeclaredConstructor(World.class).newInstance(gj_class3722), 0.0, 0.0, 0.0, 0.0f, 0.0f, false);
            }
        } catch (Exception exception) {
            System.out.println("error while preloading:");
            exception.printStackTrace();
        }
        IS_PRELOADING = false;
        w_class427.a = new w_class427();
        ClientCommandHandler.instance.registerCommand((ICommand) WhitelistServerModelsCommand.a);
        ClientCommandHandler.instance.registerCommand((ICommand) SetModelCodeCommand.a);
        ClientCommandHandler.instance.registerCommand((ICommand) FutaCommand.b);
        Minecraft.getMinecraft().effectRenderer.registerParticle(625115, (n, world, d, d2, d3, d4, d5, d6, nArray) -> new ParticleGalathTrail(world, d, d2, d3));
    }
}

