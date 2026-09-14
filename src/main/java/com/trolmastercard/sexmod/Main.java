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
        KoboldEntity.ACTIVE_TRIBE_SCREEN_POSITIONS.clear();
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
        event.registerServerCommand(LocateGoblinLairCommand.LOCATE_GOBLIN_LAIR_COMMAND);
        event.registerServerCommand(ReloadCustomModelsCommand.RELOAD_CUSTOM_MODELS_COMMAND);
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

//    public static void setConfigs() throws IOException {
//        //Appendable writer;
//        FileWriter writer;
//
//        File configDir = new File("config");
//        if (!configDir.exists()) {
//            configDir.mkdir();
//        }
//
//        File configFile = new File("config/sexmod.json");
//        if (!configFile.exists()) {
//            configFile.createNewFile();
//            writer = new FileWriter(configFile);
//            writer.write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
//            writer.close();
//        }
//
//        StringBuilder strBuilder = new StringBuilder();
//        Object todo_pleaseDeObjectIt = new BufferedReader(new FileReader(configFile));
//        Object writer2 = null;
//        try {
//            String line;
//            while ((line = ((BufferedReader)todo_pleaseDeObjectIt).readLine()) != null) {
//                strBuilder.append(line);
//            }
//        } catch (Throwable e) {
//            //writer2 = e;
//            //throw e;
//        } finally {
//            if (todo_pleaseDeObjectIt != null) {
//                if (writer2 != null) {
//                    try {
//                        ((BufferedReader)todo_pleaseDeObjectIt).close();
//                    } catch (Throwable throwable) {
//                        ((Throwable)writer2).addSuppressed(throwable);
//                    }
//                } else {
//                    ((BufferedReader)todo_pleaseDeObjectIt).close();
//                }
//            }
//        }
//        todo_pleaseDeObjectIt = strBuilder.toString();
//
//        if (!((String)todo_pleaseDeObjectIt).contains("shouldGenBuildings")) {
//            configFile.delete();
//            configFile = new File("config/sexmod.json");
//            configFile.createNewFile();
//            writer2 = new FileWriter(configFile);
//            ((Writer)writer2).write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
//            ((OutputStreamWriter)writer2).close();
//            ConfigWorldGenHandler.GENERATION_ENABLED = true;
//            GirlModel.enableModelCache = false;
//            PlayerGirl.ALLOW_FLIGHT_SYNC_ENABLED = true;
//            return;
//        }
//        int genIdx = ((String)todo_pleaseDeObjectIt).indexOf("shouldGenBuildings");
//        int n2 = ((String)todo_pleaseDeObjectIt).indexOf("shouldLoadOtherSkins");
//        int n3 = ((String)todo_pleaseDeObjectIt).indexOf("allowFlying");
//        ConfigWorldGenHandler.GENERATION_ENABLED = 't' == ((String)todo_pleaseDeObjectIt).charAt(genIdx + 20);
//        GirlModel.enableModelCache = 't' == ((String)todo_pleaseDeObjectIt).charAt(n2 + 22);
//        PlayerGirl.ALLOW_FLIGHT_SYNC_ENABLED = 't' == ((String)todo_pleaseDeObjectIt).charAt(n3 + 13);
//    }

    //Gemini 3.6 flash generated code
    public static void setConfigs() throws IOException {
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdir();
        }

        File configFile = new File("config/sexmod.json");

        // Если файла нет, создаем с дефолтным содержимым
        if (!configFile.exists()) {
            if (configFile.createNewFile()) {
                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
                }
            }
        }

        // Чтение содержимого файла
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String jsonContent = jsonBuilder.toString();

        // Проверка корректности структуры JSON (пересоздание при валидации)
        if (!jsonContent.contains("shouldGenBuildings")) {
            if (configFile.delete() && configFile.createNewFile()) {
                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
                }
            }
            ConfigWorldGenHandler.GENERATION_ENABLED = true;
            GirlModel.enableModelCache = false;
            PlayerGirl.ALLOW_FLIGHT_SYNC_ENABLED = true;
            return;
        }

        // Парсинг значений ключей
        int genIdx = jsonContent.indexOf("shouldGenBuildings");
        int skinIdx = jsonContent.indexOf("shouldLoadOtherSkins");
        int flyIdx = jsonContent.indexOf("allowFlying");

        ConfigWorldGenHandler.GENERATION_ENABLED = jsonContent.charAt(genIdx + 20) == 't';
        GirlModel.enableModelCache = jsonContent.charAt(skinIdx + 22) == 't';
        PlayerGirl.ALLOW_FLIGHT_SYNC_ENABLED = jsonContent.charAt(flyIdx + 13) == 't';
    }

    static {
        LOGGER = LogManager.getLogger("sexmod");
    }
}

