/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 */
package com.trolmastercard.sexmod.util.Handlers;

import java.io.File;
import java.io.IOException;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.events.*;
import com.trolmastercard.sexmod.girls.Allie.LampItem;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Galath.GalathCoin;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.girls.Goblin.am_class34;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEggItem;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaRod;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.Allie.PlayerAllieRenderer;
import com.trolmastercard.sexmod.girls.Goblin.PlayerGoblin;
import com.trolmastercard.sexmod.gui.EscapeMinigameUI;
import com.trolmastercard.sexmod.gui.GalathFlightUI;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.world.NameStorage;
import com.trolmastercard.sexmod.world.WorldGeneration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//bn.class
//Registers Events

public class EventHandler {
    public static void Register(boolean bl) throws IOException {
        MinecraftForge.EVENT_BUS.register((Object)new GirlCombatProtection());
        MinecraftForge.EVENT_BUS.register((Object)new eo_class262());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerConnectionEvents());
        MinecraftForge.EVENT_BUS.register((Object)new HornyPotion());
        MinecraftForge.EVENT_BUS.register((Object)new ArmorHandler());
        MinecraftForge.EVENT_BUS.register((Object)new ho_class404.a_inner405());
        MinecraftForge.EVENT_BUS.register((Object)new FighterAI.a_inner339());
        MinecraftForge.EVENT_BUS.register((Object) LampItem.LAMP_ITEM);
        MinecraftForge.EVENT_BUS.register((Object) DragonStaffItem.DRAGON_STAFF);
        MinecraftForge.EVENT_BUS.register((Object) EditorWand.EDITOR_WAND);
        MinecraftForge.EVENT_BUS.register((Object)new LunaRod());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerGirlEvents());
        MinecraftForge.EVENT_BUS.register((Object)new LunaEntity.CreeperLogic());
        MinecraftForge.EVENT_BUS.register((Object)new GirlBedInteraction());
        MinecraftForge.EVENT_BUS.register((Object) Fire.FIRE);
        MinecraftForge.EVENT_BUS.register((Object)new KoboldEntity.c_inner311());
        MinecraftForge.EVENT_BUS.register((Object)new DragonStaffItem.a_inner408());
        MinecraftForge.EVENT_BUS.register((Object)new KoboldManager.KoboldSavedData("tribes"));
        MinecraftForge.EVENT_BUS.register((Object)new KoboldEggItem());
        MinecraftForge.EVENT_BUS.register((Object)new am_class34());
        MinecraftForge.EVENT_BUS.register((Object)new GoblinEntity.c_inner222());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerGoblin.a_inner265());
        MinecraftForge.EVENT_BUS.register((Object)new LampItem.a_inner38());
        MinecraftForge.EVENT_BUS.register((Object)new DebugMode());
        MinecraftForge.EVENT_BUS.register((Object)new GalathEntity.a_inner298());
        MinecraftForge.EVENT_BUS.register((Object)new GalathMangTracker());
        MinecraftForge.EVENT_BUS.register((Object) GalathCoin.GALATH_COIN);
        MinecraftForge.EVENT_BUS.register((Object) NYIWinchesterItem.NYI_WINCHESTER_ITEM);
        MinecraftForge.EVENT_BUS.register((Object)new fq_class325());
        MinecraftForge.EVENT_BUS.register((Object)new NameStorage());
        MinecraftForge.EVENT_BUS.register((Object)new bj_class84());
        MinecraftForge.EVENT_BUS.register((Object) WorldGeneration.Generate());
        MinecraftForge.EVENT_BUS.register((Object)new ManglelieEntity.ArrowLogic());
        MinecraftForge.EVENT_BUS.register((Object)new f4_class289());
        if (bl) {
            com.trolmastercard.sexmod.util.Handlers.EventHandler.RegisterIfAllowed();
        }
    }

    @SideOnly(value=Side.CLIENT)
    static void RegisterIfAllowed() {
        if (com.trolmastercard.sexmod.util.Handlers.EventHandler.checkIfAsked()) {
            MinecraftForge.EVENT_BUS.register((Object)new ShowWarning());
        } else {
            AdultContentWarning.isAdult = false;
        }
        MinecraftForge.EVENT_BUS.register((Object)new SexUI());
        MinecraftForge.EVENT_BUS.register((Object)new fh_class313());
        MinecraftForge.EVENT_BUS.register((Object)new HandlePlayerMovement());
        MinecraftForge.EVENT_BUS.register((Object)new GirlRenderEvent());
        MinecraftForge.EVENT_BUS.register((Object)new bq_class93());
        MinecraftForge.EVENT_BUS.register((Object)new InHandMapRenderer());
        MinecraftForge.EVENT_BUS.register((Object)new e__class234());
        MinecraftForge.EVENT_BUS.register((Object)new InteractionPrompt());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerAllieRenderer.a_inner205());
        MinecraftForge.EVENT_BUS.register((Object)new gm_class376());
        MinecraftForge.EVENT_BUS.register((Object)new DeprecatedCheckForUpdates());
        MinecraftForge.EVENT_BUS.register((Object)new ClothingGui.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new CustomModel.a_inner95());
        MinecraftForge.EVENT_BUS.register((Object)new EscapeMinigameUI());
        MinecraftForge.EVENT_BUS.register((Object)new ga_class358());
        MinecraftForge.EVENT_BUS.register((Object)new GalathFlightUI());
    }

    static boolean checkIfAsked() {
        File file = new File("sexmod/dontAskAgain");
        file.getParentFile().mkdirs();
        return !file.exists();
    }
}

