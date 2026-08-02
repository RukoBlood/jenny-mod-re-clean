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

import com.trolmastercard.sexmod.girls.base.PlayerGirl.AbstractKoboldGoblinRenderer;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.proxy.CommonProxy;
import com.trolmastercard.sexmod.world.WorldGeneration;
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
* I need help with this project.
* I want to reverse engineer this mod so bad.
* Please, if you know Java, modded 1.12.2 or reverse engineered mods before,
* fork this project on GitHub, and request your bugfixes or reversed code.
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
    public static void onWorldClosed(FMLServerStoppedEvent fMLServerStoppedEvent) {
        GirlEntity.GirlEntityList().clear();
        KoboldManager.clear();
        KoboldEntity.aY.clear();
        GalathMangTracker.clear();
        WorldGeneration.Generate().clear();
        GirlID.ClearGirlList();
        CustomModel.isLoaded = false;
        bj_class84.a();
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            Main.clientReset();
        }
    }

    @Mod.EventHandler
    public static void onWorldStart(FMLServerStartingEvent fMLServerStartingEvent) {
        fMLServerStartingEvent.registerServerCommand((ICommand) LocateGoblinLairCommand.LOCATE_GOBLIN_LAIR_COMMAND);
        fMLServerStartingEvent.registerServerCommand((ICommand) ReloadCustomModelsCommand.RELOAD_CUSTOM_MODELS_COMMAND);
    }

    @SideOnly(value=Side.CLIENT)
    static void clientReset() {
        gm_class376.ClearList();
        AbstractKoboldGoblinRenderer.ResetColors();
    }

    @SideOnly(value=Side.CLIENT)
    @Mod.EventHandler
    public void registerReplacedRenderers(FMLInitializationEvent fMLInitializationEvent) {
        GeckoLib.initialize();
    }

    public static void setConfigs() throws IOException {
        Appendable appendable;
        File configFolder = new File("config");
        configFolder.mkdir();
        File config = new File("config/sexmod.json");
        if (!config.exists()) {
            config.createNewFile();
            appendable = new FileWriter(config);
            ((Writer)appendable).write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
            ((OutputStreamWriter)appendable).close();
        }
        appendable = new StringBuilder();
        Object object = new BufferedReader(new FileReader(config));
        Object object2 = null;
        try {
            String string;
            while ((string = ((BufferedReader)object).readLine()) != null) {
                ((StringBuilder)appendable).append(string);
            }
        } catch (Throwable throwable) {
            object2 = throwable;
            throw throwable;
        } finally {
            if (object != null) {
                if (object2 != null) {
                    try {
                        ((BufferedReader)object).close();
                    } catch (Throwable throwable) {
                        ((Throwable)object2).addSuppressed(throwable);
                    }
                } else {
                    ((BufferedReader)object).close();
                }
            }
        }
        object = ((StringBuilder)appendable).toString();
        if (!((String)object).contains("shouldGenBuildings")) {
            config.delete();
            config = new File("config/sexmod.json");
            config.createNewFile();
            object2 = new FileWriter(config);
            ((Writer)object2).write("{\"shouldGenBuildings\":true,\"shouldLoadOtherSkins\":false,\"allowFlying\":true}");
            ((OutputStreamWriter)object2).close();
            WorldGeneration.i = true;
            GirlModel.enableModelCache = false;
            PlayerGirl.ag = true;
            return;
        }
        int n = ((String)object).indexOf("shouldGenBuildings");
        int n2 = ((String)object).indexOf("shouldLoadOtherSkins");
        int n3 = ((String)object).indexOf("allowFlying");
        WorldGeneration.i = 't' == ((String)object).charAt(n + 20);
        GirlModel.enableModelCache = 't' == ((String)object).charAt(n2 + 22);
        PlayerGirl.ag = 't' == ((String)object).charAt(n3 + 13);
    }

    static {
        LOGGER = LogManager.getLogger((String)"sexmod");
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

