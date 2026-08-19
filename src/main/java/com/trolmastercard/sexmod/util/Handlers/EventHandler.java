/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 */
package com.trolmastercard.sexmod.util.Handlers;

import java.io.File;
import java.io.IOException;

import com.trolmastercard.sexmod.blocks.SexFire;
import com.trolmastercard.sexmod.companion.CompanionPearl;
import com.trolmastercard.sexmod.companion.fighter.DamageCalculation;
import com.trolmastercard.sexmod.companion.fighter.FighterCompanion;
import com.trolmastercard.sexmod.deprecated.NYIWinchesterItem.NYIWinchesterItem;
import com.trolmastercard.sexmod.events.*;
import com.trolmastercard.sexmod.gender_change.RenderPlayerGirl;
import com.trolmastercard.sexmod.gender_change.SexPromptManager;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.Allie.lamp.LampItem;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Galath.CummyEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathCoin.GalathCoin;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.girls.Goblin.GoblinFirstPersonRenderer;
import com.trolmastercard.sexmod.girls.Kobold.DragonStaff.DragonStaffItem;
import com.trolmastercard.sexmod.girls.Kobold.DragonStaff.StructureMarkerRenderer;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEgg.KoboldEggItem;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.girls.Luna.FishingRod.LunaRod;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.Allie.PlayerAllieRenderer;
import com.trolmastercard.sexmod.girls.Goblin.PlayerGoblin;
import com.trolmastercard.sexmod.girls.base.EditorWand.EditorWand;
import com.trolmastercard.sexmod.gui.*;
import com.trolmastercard.sexmod.gui.CustomModel.ClothingGui;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.gui.Galath.EscapeMinigameUI;
import com.trolmastercard.sexmod.gui.Galath.GalathFlightUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.util.StructureTracker;
import com.trolmastercard.sexmod.world.AllieWorldData;
import com.trolmastercard.sexmod.world.ConfigWorldGenHandler;
import com.trolmastercard.sexmod.world.GirlWorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//bn.class
//Registers Events

public class EventHandler {
    public static void Register(boolean showDebug) throws IOException {
        MinecraftForge.EVENT_BUS.register((Object)new GirlCombatProtection());
        MinecraftForge.EVENT_BUS.register((Object)new LivingDeathHandler());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerConnectionEvents());
        MinecraftForge.EVENT_BUS.register((Object)new HornyPotion());
        MinecraftForge.EVENT_BUS.register((Object)new DamageCalculation());
        MinecraftForge.EVENT_BUS.register((Object)new CompanionPearl.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new FighterCompanion.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object) LampItem.LAMP_ITEM);
        MinecraftForge.EVENT_BUS.register((Object) DragonStaffItem.DRAGON_STAFF);
        MinecraftForge.EVENT_BUS.register((Object) EditorWand.EDITOR_WAND);
        MinecraftForge.EVENT_BUS.register((Object)new LunaRod());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerGirlEvents());
        MinecraftForge.EVENT_BUS.register((Object)new LunaEntity.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new GirlBedInteraction());
        MinecraftForge.EVENT_BUS.register((Object) SexFire.FIRE);
        MinecraftForge.EVENT_BUS.register((Object)new KoboldEntity.c_inner311());
        MinecraftForge.EVENT_BUS.register((Object)new DragonStaffItem.a_inner408());
        MinecraftForge.EVENT_BUS.register((Object)new KoboldManager.KoboldSavedData("tribes"));
        MinecraftForge.EVENT_BUS.register((Object)new KoboldEggItem());
        MinecraftForge.EVENT_BUS.register((Object)new GoblinFirstPersonRenderer());
        MinecraftForge.EVENT_BUS.register((Object)new GoblinEntity.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerGoblin.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new LampItem.a_inner38());
        MinecraftForge.EVENT_BUS.register((Object)new DebugMode());
        MinecraftForge.EVENT_BUS.register((Object)new GalathEntity.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new GalathMangTracker());
        MinecraftForge.EVENT_BUS.register((Object) GalathCoin.GALATH_COIN);
        MinecraftForge.EVENT_BUS.register((Object) NYIWinchesterItem.NYI_WINCHESTER_ITEM);
        MinecraftForge.EVENT_BUS.register((Object)new StructureTracker());
        MinecraftForge.EVENT_BUS.register((Object)new AllieWorldData());
        MinecraftForge.EVENT_BUS.register((Object)new GirlWorldData());
        MinecraftForge.EVENT_BUS.register((Object) ConfigWorldGenHandler.Generate());
        MinecraftForge.EVENT_BUS.register((Object)new ManglelieEntity.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new NameTagInteractHandler());
        if (showDebug) {
            RegisterIfAllowed();
        }
    }

    @SideOnly(value=Side.CLIENT)
    static void RegisterIfAllowed() {
        if (needsPornWarning()) {
            MinecraftForge.EVENT_BUS.register((Object)new PornWarning());
        } else {
            PornWarningWindow.isAdult = false;
        }
        MinecraftForge.EVENT_BUS.register((Object)new SexUI());
        MinecraftForge.EVENT_BUS.register((Object)new BlackScreenUI());
        MinecraftForge.EVENT_BUS.register((Object)new HandlePlayerMovement());
        MinecraftForge.EVENT_BUS.register((Object)new GirlRenderEvent());
        MinecraftForge.EVENT_BUS.register((Object)new GuiOpenHandler());
        MinecraftForge.EVENT_BUS.register((Object)new InHandMapRenderer());
        MinecraftForge.EVENT_BUS.register((Object)new RenderPlayerGirl());
        MinecraftForge.EVENT_BUS.register((Object)new SexPromptManager());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerAllieRenderer.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new StructureMarkerRenderer());
        MinecraftForge.EVENT_BUS.register((Object)new DeprecatedCheckForUpdates());
        MinecraftForge.EVENT_BUS.register((Object)new ClothingGui.EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new CustomModel.a_inner95());
        MinecraftForge.EVENT_BUS.register((Object)new EscapeMinigameUI());
        MinecraftForge.EVENT_BUS.register((Object)new CummyEntity());
        MinecraftForge.EVENT_BUS.register((Object)new GalathFlightUI());
    }

    static boolean needsPornWarning() {
        File save = new File("sexmod/dontAskAgain");
        save.getParentFile().mkdirs();
        return !save.exists();
    }
}

