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
import com.trolmastercard.sexmod.gender_change.SexPromptManager;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.util.Handlers.GuiHandler;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.RenderHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Handlers.EventHandler;
import com.trolmastercard.sexmod.world.FakeWorld;
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

public class ClientProxy extends CommonProxy {
    static public boolean IS_PRELOADING = false;
    static public KeyBinding[] keyBindings;

    @Override
    public void postInit(FMLPostInitializationEvent event) throws IOException {
    }

    @Override
    public void preInitRegistries(FMLPreInitializationEvent event) {
        super.preInitRegistries(event);
        RenderHandler.Register();
    }

    @Override
    public void initRegistries(FMLInitializationEvent event) throws IOException {
        keyBindings = new KeyBinding[2];
        ClientProxy.keyBindings[0] = new KeyBinding("Interact with your goblin", 34, "Sex mod");
        ClientProxy.keyBindings[1] = new KeyBinding("open character customisation menu", 76, "Sex mod");
        for (KeyBinding kb : keyBindings) {
            ClientRegistry.registerKeyBinding((KeyBinding)kb);
        }
        Main.setConfigs();
        SoundsHandler.RegisterSounds();
        net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler((Object) Main.instance, (IGuiHandler)new GuiHandler(true));
        EventHandler.Register(true);
        PackageHandler.RegisterMessages();
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager renderManager = mc.getRenderManager();
        FakeWorld fakeWorld = new FakeWorld();
        IS_PRELOADING = true;
        try {
            for (PlayerGirlEntity entity : PlayerGirlEntity.values()) {
                renderManager.renderEntity(entity.npcClass.getDeclaredConstructor(World.class).newInstance(fakeWorld), 0.0, 0.0, 0.0, 0.0f, 0.0f, false);
            }
        } catch (Exception e) {
            System.out.println("error while preloading:");
            e.printStackTrace();
        }
        IS_PRELOADING = false;
        SexPromptManager.INSTANCE = new SexPromptManager();
        ClientCommandHandler.instance.registerCommand((ICommand) WhitelistServerModelsCommand.WHITELIST_SERVER_MODELS_COMMAND);
        ClientCommandHandler.instance.registerCommand((ICommand) SetModelCodeCommand.SET_MODEL_CODE_COMMAND);
        ClientCommandHandler.instance.registerCommand((ICommand) FutaCommand.FUTA_COMMAND);
        Minecraft.getMinecraft().effectRenderer.registerParticle(625115, (n, world, x, y, z, d4, d5, d6, nArray) -> new DragonBreathParticle(world, x, y, z));
    }
}

