/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  org.apache.logging.log4j.Level
 */
package com.trolmastercard.sexmod.girls.Custom;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.Packets.RequestServerModelAvailability;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.CustomPartCategory;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.geo.raw.pojo.Converter;
import software.bernie.geckolib3.geo.raw.pojo.RawGeoModel;
import software.bernie.geckolib3.geo.raw.tree.RawGeometryTree;
import software.bernie.geckolib3.geo.render.GeoBuilder;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class CustomModel {
    final static public String a = "sexmod/custom_models";
    final static String b = "sexmod/custom_models/whitelisted_servers.txt";
    final static public String f = "sexmod_custom_models";
    static Map<String, ModelData> c = new HashMap<String, ModelData>();
    static public boolean isGlobalRenderingDisabled = false;
    static public boolean isLoaded = false;

    public static Map<String, ModelData> i() {
        return c;
    }

    public static boolean isModelDisabled(String string) {
        return c.get(string) != null;
    }

    public static int b(boolean bl) {
        CustomModel.a(bl);
        return CustomModel.LoadModels(bl);
    }

    static void log(Level level, String string) {
        if (Main.proxy instanceof ClientProxy) {
            CustomModel.a(level, string);
        } else {
            Main.LOGGER.log(level, string);
        }
    }

    public static void a(boolean bl) {
        if (bl) {
            CustomModel.c();
        }
        c.clear();
    }

    public static void a() {
        PackageHandler.INSTANCE.sendToServer((IMessage)new RequestServerModelAvailability());
    }

    @SideOnly(value=Side.CLIENT)
    public static boolean b() {
        String string = CustomModel.getGlobalModelOverride();
        if (string == null) {
            return false;
        }
        return CustomModel.l(string);
    }

    public static void h(String string) {
        File file = new File(b);
        file.mkdirs();
        HashSet<String> hashSet = new HashSet<>();
        if (file.exists()) {
            hashSet = CustomModel.f();
        }
        hashSet.add(string);
        file.delete();
        file = new File(b);
        try {
            FileWriter fileWriter = new FileWriter(file);
            Throwable throwable = null;
            try {
                for (String string2 : hashSet) {
                    fileWriter.write(string2 + "\n");
                }
            } catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            } finally {
                // TODO try with resources
                if (throwable != null) {
                    try {
                        fileWriter.close();
                    } catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                } else {
                    fileWriter.close();
                }
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static boolean l(String string) {
        return CustomModel.f().contains(string);
    }

    static HashSet<String> f() {
        File file = new File(b);
        try {
            file.createNewFile();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        HashSet<String> hashSet = new HashSet<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            Throwable throwable = null;
            try {
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    hashSet.add(string);
                }
            } catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            } finally {
                // TODO try with resources
                if (throwable != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                } else {
                    bufferedReader.close();
                }
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return new HashSet<String>();
        }
        return hashSet;
    }

    public static float i(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            return 0.0f;
        }
        return b_inner962.f();
    }

    @SideOnly(value=Side.CLIENT)
    static void c() {
        for (Map.Entry<String, ModelData> entry : c.entrySet()) {
            ModelData b_inner962 = entry.getValue();
            if (b_inner962 == null) continue;
            ResourceLocation resourceLocation = b_inner962.c();
            ResourceLocation resourceLocation2 = b_inner962.k();
            if (resourceLocation != null) {
                GeckoLibCache.getInstance().getGeoModels().remove(resourceLocation);
            }
            if (resourceLocation2 == null) continue;
            Minecraft.getMinecraft().renderEngine.deleteTexture(resourceLocation2);
        }
    }

    @SideOnly(value=Side.CLIENT)
    static void a(Level level, String string) {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (entityPlayerSP == null) {
            Main.LOGGER.log(level, string);
            return;
        }
        TextFormatting textFormatting = Level.DEBUG.equals((Object)level) ? TextFormatting.DARK_GREEN : (Level.ERROR.equals((Object)level) ? TextFormatting.RED : TextFormatting.WHITE);
        ((Entity)entityPlayerSP).sendMessage(new TextComponentString(textFormatting.toString() + string));
    }

    public static String getCurrentGroup() {
        if (Main.proxy instanceof ClientProxy) {
            return CustomModel.d();
        }
        return f;
    }

    @SideOnly(value=Side.CLIENT)
    public static String d() {
        String string = CustomModel.getGlobalModelOverride();
        if (string == null) {
            return "sexmod/custom_models/singleplayer";
        }
        return "sexmod/custom_models/" + string;
    }

    @SideOnly(value=Side.CLIENT)
    @Nullable
    public static String getGlobalModelOverride() {
        Minecraft mc = Minecraft.getMinecraft();
        ServerData serverData = mc.getCurrentServerData();
        if (serverData == null) {
            return null;
        }
        String ip = serverData.serverIP;
        int n = ip.indexOf(":");
        if (n != -1) {
            ip = ip.substring(0, n);
        }
        return ip;
    }

    public static int LoadModels(boolean bl) {
        CustomModel.log(Level.INFO, "loading up custom models...");
        String string2 = CustomModel.getCurrentGroup();
        File file2 = new File(string2);
        file2.mkdirs();
        String[] stringArray = file2.list((file, string) -> new File(file, string).isDirectory());
        if (stringArray == null) {
            CustomModel.log(Level.ERROR, String.format("Something is wrong with the custom models folder at '%s'. Check if it exists, if not - make the directory yourself because Minecraft cannot do it itself for some reason", file2.getAbsolutePath()));
            return -1;
        }
        CustomModel.log(Level.INFO, String.format("found %s custom model(s)", stringArray.length));
        int n = 0;
        for (String string3 : stringArray) {
            String string4 = CustomModel.getPartName(string3, string2);
            if (!"".equals(string4)) {
                CustomModel.log(Level.ERROR, string4);
                return -1;
            }
            string4 = CustomModel.a(string3, string2, bl);
            if (!"".equals(string4)) {
                CustomModel.log(Level.ERROR, string4);
                return -1;
            }
            ++n;
        }
        CustomModel.log(Level.DEBUG, String.format("successfully registered %s custom models", n));
        isLoaded = true;
        return 0;
    }

    public static String getPartName(String string, String string2) {
        String string3 = String.format("%s/%s", string2, string);
        File file = new File(String.format("%s/%s.geo.json", string3, string));
        File file2 = new File(String.format("%s/%s.png", string3, string));
        File file3 = new File(String.format("%s/%s.cfg", string3, string));
        if (!file.exists()) {
            return String.format("couldn't find model File for '%s'. It should have been at '%s'. Are you sure it exists?", string, file.getAbsolutePath());
        }
        if (!file2.exists()) {
            return String.format("couldn't find texture File for '%s'. It should have been at '%s'. Are you sure it exists?", string, file2.getAbsolutePath());
        }
        if (!file3.exists()) {
            return String.format("couldn't find cfg File for '%s'. It should have been at '%s'. Are you sure it exists?", string, file3.getAbsolutePath());
        }
        return "";
    }

    @SideOnly(value=Side.CLIENT)
    static ResourceLocation a(String string, File file) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(file);
        return Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(string, new DynamicTexture(bufferedImage));
    }

    @SideOnly(value=Side.CLIENT)
    static RawGeoModel a(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Object object = new BufferedReader(new FileReader(file));
        Throwable throwable = null;
        try {
            String string;
            while ((string = ((BufferedReader)object).readLine()) != null) {
                stringBuilder.append(string);
            }
        } catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        } finally {
            if (object != null) {
                if (throwable != null) {
                    try {
                        ((BufferedReader)object).close();
                    } catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                } else {
                    ((BufferedReader)object).close();
                }
            }
        }
        object = stringBuilder.toString();
        return Converter.fromJsonString((String)object);
    }

    public static String a(String string, String string2, boolean bl) {
        if (c.get(string) != null) {
            return String.format("already registered '%s'... honestly, unsure how this could happen lol", string);
        }
        String string3 = String.format("%s/%s/", string2, string);
        String string4 = string3 + string + ".cfg";
        File file = new File(string4);
        if (!file.exists()) {
            return String.format("couldn't find cfg File for '%s'. It should have been at '%s'. Are you sure it exists?", string, string4);
        }
        ModelData b_inner962 = new ModelData(file, string);
        if (b_inner962.h != null) {
            return b_inner962.h;
        }
        String string5 = string3 + string + ".png";
        File file2 = new File(string5);
        if (!file2.exists()) {
            return String.format("The texture for the custom model '%s' couldn't be found at '%s' are you sure it exists?", string, string5);
        }
        ResourceLocation resourceLocation = null;
        if (bl) {
            try {
                resourceLocation = CustomModel.a(string, file2);
            } catch (IOException iOException) {
                return String.format("The texture for the custom model '%s' at '%s' appears to be corrupted. Try making a new one", string, string5);
            } catch (Exception exception) {
                return String.format("Couldn't load the texture for the custom model '%s' at '%s'. Maybe try increasing the amount of RAM of ur Minecraft client", string, file2);
            }
        }
        ResourceLocation resourceLocation2 = new ResourceLocation("sexmod", string + "Model");
        String string6 = string3 + string + ".geo.json";
        File file3 = new File(string6);
        if (!file3.exists()) {
            return String.format("The geo model for the custom model '%s' couldn't be found at '%s' are you sure it exists?", string, string6);
        }
        if (bl) {
            RawGeoModel rawGeoModel;
            try {
                rawGeoModel = CustomModel.a(file3);
            } catch (IOException iOException) {
                return String.format("The geo model for the custom model '%s' at '%s' appears to be corrupted. Try replacing it.", string, string6);
            }
            try {
                RawGeometryTree rawGeometryTree = RawGeometryTree.parseHierarchy(rawGeoModel, resourceLocation2);
                GeoModel geoModel = GeoBuilder.getGeoBuilder(resourceLocation2.getNamespace()).constructGeoModel(rawGeometryTree);
                GeckoLibCache.getInstance().getGeoModels().put(resourceLocation2, geoModel);
            } catch (Exception exception) {
                return String.format("The geo model for the custom model '%s' at '%s' appears to be corrupted. Try replacing it.", string, string6);
            }
        }
        if (bl) {
            b_inner962.b(resourceLocation2);
            b_inner962.a(resourceLocation);
        }
        c.put(string, b_inner962);
        CustomModel.log(Level.DEBUG, String.format("successfully registered custom model '%s'", string));
        return "";
    }

    public static ResourceLocation k(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            if (!string.equals("cross")) {
                System.out.printf("The custom model for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return null;
        }
        return b_inner962.c();
    }

    public static ResourceLocation c(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            if (!string.equals("cross")) {
                System.out.printf("The custom texture for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return null;
        }
        return b_inner962.k();
    }

    public static GeoModel j(String string) {
        return GeckoLibCache.getInstance().getGeoModels().get(CustomModel.k(string));
    }

    public static CustomPartCategory e(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            if (!string.equals("cross")) {
                System.out.printf("The ClothingType for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return CustomPartCategory.HEAD;
        }
        return b_inner962.category;
    }

    public static HashSet<PlayerGirlEntity> getAllowedEntities(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            if (!string.equals("cross")) {
                System.out.printf("The HashSet<GirlType> for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return null;
        }
        return b_inner962.g;
    }

    public static HashSet<String> g(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            if (!string.equals("cross")) {
                System.out.printf("The HashSet<String> for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return new HashSet<String>();
        }
        return b_inner962.b;
    }

    public static String d(String string) {
        ModelData b_inner962 = c.get(string);
        if (b_inner962 == null) {
            if (!string.equals("cross")) {
                System.out.printf("The author for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return "";
        }
        return b_inner962.k;
    }

    @Nullable
    public static ModelData getModelData(String string) {
        return c.get(string);
    }

    public static HashMap<CustomPartCategory, List<String>> a(GirlEntity em_class2582) {
        HashMap<CustomPartCategory, List<String>> hashMap = new HashMap<CustomPartCategory, List<String>>();
        for (Object object : CustomPartCategory.values()) {
            hashMap.put((CustomPartCategory)((Object)object), new ArrayList());
        }
        for (Map.Entry entry : c.entrySet()) {
            Object object;
            String string = (String)entry.getKey();
            object = (ModelData)entry.getValue();
            CustomPartCategory gw_class3892 = ((ModelData)object).category;
            List<String> list = hashMap.get((Object)gw_class3892);
            if (!((ModelData)object).g.isEmpty() && !((ModelData)object).g.contains((Object) PlayerGirlEntity.fromGirl(em_class2582))) continue;
            list.add(string);
            hashMap.put(gw_class3892, list);
        }
        return hashMap;
    }

    public static HashMap<String, Float> e() {
        HashMap<String, Float> hashMap = new HashMap<String, Float>();
        for (Map.Entry<String, ModelData> entry : CustomModel.i().entrySet()) {
            hashMap.put(entry.getKey(), Float.valueOf(entry.getValue().f()));
        }
        return hashMap;
    }

    public static class ModelData {
        CustomPartCategory category;
        HashSet<PlayerGirlEntity> g = new HashSet();
        HashSet<String> b = new HashSet();
        String k;
        String j;
        boolean c;
        LightingType e;
        float m = 1.0f;
        float a = 0.0f;
        ResourceLocation i;
        ResourceLocation f;
        public String h = null;
        float l;

        public ModelData(File file, String string) {
            String string2;
            String string3;
            FileInputStream fileInputStream;
            if (string.contains(" ") || string.contains("#") || string.contains("$")) {
                this.h = String.format("You cannot call your custom model '%s'. '#', '$' and spaces are illegal characters", string);
                return;
            }
            if ("cross".equalsIgnoreCase(string)) {
                this.h = "You cannot call your custom model 'cross'. Im sorry, but I need that specific name for internal stuff";
                return;
            }
            Properties properties = new Properties();
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException fileNotFoundException) {
                this.h = String.format("couldn't find cfg File for '%s'. It should have been at '%s'. Are you sure it exists?", string, file.getAbsolutePath());
                return;
            }
            try {
                properties.load(fileInputStream);
            } catch (IOException iOException) {
                this.h = String.format("couldn't read the cfg File for '%s' at '%s'. It appears to be corrupted. Try making a new one", string, file.getAbsolutePath());
                return;
            }
            String string32 = properties.getProperty("wear_type");
            if (string32 == null) {
                this.h = String.format("The cfg File for the model '%s' at '%s' is missing the 'wear_type'. Go to the bottom of the cfg File and write 'wear_type=HEAD'. Check the cfg files of my examples to see what values for 'wear_type' are possible", string, file.getAbsolutePath());
                return;
            }
            try {
                string32 = string32.replace(" ", "");
                this.category = CustomPartCategory.valueOf(string32);
            } catch (IllegalArgumentException illegalArgumentException) {
                this.h = String.format("you entered '%s' into the 'wear_type' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'wear_type", string32, string, file.getAbsolutePath());
                return;
            }
            if (CustomPartCategory.CUSTOM_BONE.equals((Object)this.category)) {
                this.j = properties.getProperty("custom_bone");
                if ("".equals(this.j)) {
                    this.h = String.format("You selected CUSTOM_BONE as the 'wear_type' in the cfg file for '%s' at '%s', yet you left the 'custom_bone' field right underneath it empty. If you want ur model to be parented to a specific bone, you have to enter the name of that bone at the field 'custom_bone'.", string, file.getAbsolutePath());
                    return;
                }
            }
            String string4 = properties.getProperty("which_girls");
            string4 = string4.replace(" ", "");
            String[] stringArray2 = string4.split(",");
            for (String stringArray3 : stringArray2) {
                try {
                    if ("".equals(stringArray3)) continue;
                    this.g.add(PlayerGirlEntity.valueOf(stringArray3));
                } catch (IllegalArgumentException illegalArgumentException) {
                    this.h = String.format("you entered '%s' as one of the girls, you put into the 'which_girls' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'which_girls'.", stringArray3, string, file.getAbsolutePath());
                    return;
                }
            }
            Object object = properties.getProperty("which_lighting");
            if (object == null) {
                this.h = String.format("The %s's cfg file at '%s' doesn't contain the field 'which_lighting'. Go to the bottom of the cfg file and write either 'which_lighting=DEFAULT', 'which_lighting=SEXMOD', or 'which_lighting=NONE'.", string, file.getAbsolutePath());
                return;
            }
            object = ((String)object).replace(" ", "");
            try {
                this.e = LightingType.valueOf((String)object);
            } catch (IllegalArgumentException illegalArgumentException) {
                this.h = String.format("you entered '%s' into the 'which_lighting' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'which_lighting'.", object, string, file.getAbsolutePath());
            }
            String string5 = properties.getProperty("author");
            this.k = string5 == null || "".equals(string5) ? "anon" : string5;
            String string6 = properties.getProperty("bones_to_hide");
            if (string6 != null && !"".equals(string6)) {
                string6 = string6.replace(" ", "");
                String[] stringArray = string6.split(",");
                this.b.addAll(Arrays.asList(stringArray));
            }
            if ((string3 = properties.getProperty("enable_when_nude")) == null) {
                this.c = false;
            } else {
                String string7 = string3.replace(" ", "");
                this.c = string7.equalsIgnoreCase("yes");
            }
            String string8 = properties.getProperty("gui_size_factor");
            if (string8 != null && !"".equals(string8)) {
                string8 = string8.replace(" ", "");
                string8 = string8.replace(",", ".");
                try {
                    this.m = Float.parseFloat(string8);
                } catch (NumberFormatException numberFormatException) {
                    this.h = String.format("you entered '%s' into the 'gui_size_factor' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'gui_size_factor'.", string8, string, file.getAbsolutePath());
                }
            }
            if ((string2 = properties.getProperty("gui_vertical_positioning")) != null && !"".equals(string2)) {
                string2 = string2.replace(" ", "");
                string2 = string2.replace(",", ".");
                try {
                    this.a = Float.parseFloat(string2);
                } catch (NumberFormatException numberFormatException) {
                    this.h = String.format("you entered '%s' into the 'gui_vertical_positioning' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'gui_vertical_positioning'.", string2, string, file.getAbsolutePath());
                }
            }
            String string9 = properties.getProperty("version");
            string9 = string9.replace(" ", "");
            string9 = string9.replace(",", ".");
            try {
                this.l = Float.parseFloat(string9);
            } catch (NumberFormatException numberFormatException) {
                this.h = String.format("you entered '%s' into the 'versionString' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'versionString'.", string9, string, file.getAbsolutePath());
            }
        }

        public String getCustomBoneName() {
            return this.j;
        }

        public LightingType getLightingType() {
            return this.e;
        }

        public float g() {
            return this.a;
        }

        public float d() {
            return this.m;
        }

        public CustomPartCategory getCategory() {
            return this.category;
        }

        public HashSet<PlayerGirlEntity> l() {
            return this.g;
        }

        public String e() {
            return this.k;
        }

        public boolean isAlwaysVisible() {
            return this.c;
        }

        public HashSet<String> h() {
            return this.b;
        }

        public ResourceLocation k() {
            return this.i;
        }

        public void a(ResourceLocation resourceLocation) {
            this.i = resourceLocation;
        }

        public ResourceLocation c() {
            return this.f;
        }

        public void b(ResourceLocation resourceLocation) {
            this.f = resourceLocation;
        }

        public float f() {
            return this.l;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static class a_inner95 {
        boolean a = false;

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(ClientChatEvent clientChatEvent) {
            String string = clientChatEvent.getOriginalMessage();
            if (!"id".equals(string)) {
                return;
            }
            EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
            List<GirlEntity> list = entityPlayerSP.world.getEntitiesWithinAABB(GirlEntity.class, entityPlayerSP.getEntityBoundingBox().grow(10.0));
            GirlEntity em_class2582 = null;
            for (GirlEntity em_class2583 : list) {
                if (em_class2582 == null) {
                    em_class2582 = em_class2583;
                    continue;
                }
                if (!(entityPlayerSP.getDistance(em_class2583) < entityPlayerSP.getDistance(em_class2582))) continue;
                em_class2582 = em_class2583;
            }
            if (em_class2582 == null) {
                return;
            }
            ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString(em_class2582.girlID().toString()), false);
            clientChatEvent.setCanceled(true);
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(FMLNetworkEvent.ClientConnectedToServerEvent clientConnectedToServerEvent) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.addScheduledTask(() -> CustomModel.LoadModels(true));
            this.a = false;
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(EntityJoinWorldEvent entityJoinWorldEvent) {
            if (!entityJoinWorldEvent.getEntity().equals(Minecraft.getMinecraft().player)) {
                return;
            }
            if (this.a) {
                return;
            }
            this.a = true;
            if (CustomModel.b()) {
                CustomModel.a();
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(FMLNetworkEvent.ClientDisconnectionFromServerEvent clientDisconnectionFromServerEvent) {
            Minecraft.getMinecraft().addScheduledTask(() -> CustomModel.a(true));
            this.a = false;
        }
    }
}

