/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.SidedProxy
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLServerStartingEvent
 *  net.minecraftforge.fml.common.event.FMLServerStoppedEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.trolmastercard.sexmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.trolmastercard.sexmod.command.LocateGoblinLairCommand;
import com.trolmastercard.sexmod.command.ReloadCustomModelsCommand;
import com.trolmastercard.sexmod.girls.Kobold.DragonStaff.StructureMarkerRenderer;
import com.trolmastercard.sexmod.girls.base.GirlID;
import com.trolmastercard.sexmod.girls.base.GirlRendererBase;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.proxy.CommonProxy;
import com.trolmastercard.sexmod.world.ConfigWorldGenHandler;
import com.trolmastercard.sexmod.world.GirlWorldData;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

/*
* Thanks for Palkaline for main Zelix KlassMaster deobfuscation and RealCrystalNight for Class names.
*/

@Mod(modid="sexmod", name="Fapcraft", version="1.1.0", dependencies="after:geckolib")
public class Main {
    @Mod.Instance
    static public Main instance;
    @SidedProxy(clientSide="com.trolmastercard.sexmod.proxy.ClientProxy", serverSide="com.trolmastercard.sexmod.proxy.CommonProxy")
    static public CommonProxy proxy;
    final static public Logger LOGGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GeckoLib.initialize();
        proxy.preInitRegistries(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        proxy.initRegistries(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws IOException {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public static void onWorldClosed(FMLServerStoppedEvent event) {
        GirlEntity.getGirlEntityList().clear();
        KoboldManager.clearAll();
        KoboldEntity.aY.clear();
        GalathMangTracker.clear();
        ConfigWorldGenHandler.Generate().clear();
        GirlID.ClearGirlList();
        CustomModel.isLoaded = false;
        GirlWorldData.clearAll();
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            Main.clientReset();
        }
    }

    @Mod.EventHandler
    public static void onWorldStart(FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand) LocateGoblinLairCommand.LOCATE_GOBLIN_LAIR_COMMAND);
        event.registerServerCommand((ICommand) ReloadCustomModelsCommand.RELOAD_CUSTOM_MODELS_COMMAND);
    }

    @SideOnly(value=Side.CLIENT)
    static void clientReset() {
        StructureMarkerRenderer.ClearMarkers();
        GirlRendererBase.clearBoneColors();
    }

    @SideOnly(value=Side.CLIENT)
    @Mod.EventHandler
    public void registerReplacedRenderers(FMLInitializationEvent event) {
        GeckoLib.initialize();
    }

    public static void setConfigs() throws IOException {
        Appendable jsonBuilder;
        File configDir = new File("config");
        configDir.mkdir();
        File configFile = new File("config/sexmod.json");
        if (!configFile.exists()) {
            configFile.createNewFile();
            jsonBuilder = new FileWriter(configFile);
            ((Writer)jsonBuilder).write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
            ((OutputStreamWriter)jsonBuilder).close();
        }
        jsonBuilder = new StringBuilder();
        Object json = new BufferedReader(new FileReader(configFile));
        Object writer2 = null;
        try {
            String line;
            while ((line = ((BufferedReader)json).readLine()) != null) {
                ((StringBuilder)jsonBuilder).append(line);
            }
        } catch (Throwable e) {
            //writer2 = e;
            //throw e;
        } finally {
            if (json != null) {
                if (writer2 != null) {
                    try {
                        ((BufferedReader)json).close();
                    } catch (Throwable throwable) {
                        ((Throwable)writer2).addSuppressed(throwable);
                    }
                } else {
                    ((BufferedReader)json).close();
                }
            }
        }
        json = ((StringBuilder)jsonBuilder).toString();
        if (!((String)json).contains("shouldGenBuildings")) {
            configFile.delete();
            configFile = new File("config/sexmod.json");
            configFile.createNewFile();
            writer2 = new FileWriter(configFile);
            ((Writer)writer2).write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
            ((OutputStreamWriter)writer2).close();
            ConfigWorldGenHandler.GENERATION_ENABLED = true;
            GirlModel.enableModelCache = false;
            PlayerGirl.ag = true;
            return;
        }
        int genIdx = ((String)json).indexOf("shouldGenBuildings");
        int n2 = ((String)json).indexOf("shouldLoadOtherSkins");
        int n3 = ((String)json).indexOf("allowFlying");
        ConfigWorldGenHandler.GENERATION_ENABLED = 't' == ((String)json).charAt(genIdx + 20);
        GirlModel.enableModelCache = 't' == ((String)json).charAt(n2 + 22);
        PlayerGirl.ag = 't' == ((String)json).charAt(n3 + 13);
    }

    static {
        LOGGER = LogManager.getLogger("sexmod");
    }
}

