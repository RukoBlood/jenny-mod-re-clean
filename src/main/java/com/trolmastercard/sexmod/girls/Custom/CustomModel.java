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
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
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
    final static public String CUSTOM_MODELS_DIR = "sexmod/custom_models";
    final static String WHITELIST_FILE = "sexmod/custom_models/whitelisted_servers.txt";
    final static public String CUSTOM_MODEL_KEY = "sexmod_custom_models";
    static Map<String, ModelData> modelDataMap = new HashMap<String, ModelData>();
    static public boolean isGlobalRenderingDisabled = false;
    static public boolean isLoaded = false;

    public static Map<String, ModelData> getModelDataMap() {
        return modelDataMap;
    }

    public static boolean isModelDisabled(String string) {
        return modelDataMap.get(string) != null;
    }

    public static int getModelCount(boolean disableRendering) {
        setGlobalRenderingDisabled(disableRendering);
        return LoadModels(disableRendering);
    }

    static void logError(Level level, String message) {
        if (Main.proxy instanceof ClientProxy) {
            logInfo(level, message);
        } else {
            Main.LOGGER.log(level, message);
        }
    }

    public static void setGlobalRenderingDisabled(boolean disabled) {
        if (disabled) {
            syncModelData();
        }
        modelDataMap.clear();
    }

    public static void reloadCustomModels() {
        PacketHandler.INSTANCE.sendToServer(new RequestServerModelAvailability());
    }

    @SideOnly(value=Side.CLIENT)
    public static boolean isGlobalRenderingDisabled() {
        String string = getCustomModelsKey();
        return string != null && isModelWhitelisted(string);
    }

    public static void initWhitelistFile(String serverName) {
        File file = new File(WHITELIST_FILE);
        file.mkdirs();
        HashSet<String> whitelist = new HashSet<>();
        if (file.exists()) {
            whitelist = loadWhitelistedServer();
        }

        whitelist.add(serverName);
        file.delete();
        file = new File(WHITELIST_FILE);
        try {
            FileWriter writer = new FileWriter(file);
            Throwable throwable = null;
            try {
                for (String server : whitelist) {
                    writer.write(server + "\n");
                }
            } catch (Throwable caughtThrowable) {
                throwable = caughtThrowable;
                throw caughtThrowable;
            } finally {
                if (throwable != null) {
                    try {
                        writer.close();
                    } catch (Throwable suppressed) {
                        throwable.addSuppressed(suppressed);
                    }
                } else {
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isModelWhitelisted(String server) {
        return loadWhitelistedServer().contains(server);
    }

    static HashSet<String> loadWhitelistedServer() {
        File file = new File(WHITELIST_FILE);

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
            } catch (Throwable t) {
                throwable = t;
                throw t;
            } finally {
                if (throwable != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable supressed) {
                        throwable.addSuppressed(supressed);
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

    public static float getModelZOffset(String modelName) {
        ModelData data = modelDataMap.get(modelName);
        return data == null ? 0.0f : data.getZOffset();
    }

    @SideOnly(value=Side.CLIENT)
    static void syncModelData() {
        for (Map.Entry<String, ModelData> entry : modelDataMap.entrySet()) {
            ModelData modelData = entry.getValue();
            if (modelData != null) {
                ResourceLocation fallbackTex = modelData.getFallbackTexture();
                ResourceLocation resLoc = modelData.getTextureLocation();
                if (fallbackTex != null) {
                    GeckoLibCache.getInstance().getGeoModels().remove(fallbackTex);
                }
                if (resLoc != null) {
                    Minecraft.getMinecraft().renderEngine.deleteTexture(resLoc);
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    static void logInfo(Level level, String string) {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (entityPlayerSP == null) {
            Main.LOGGER.log(level, string);
        } else {
            TextFormatting color = Level.DEBUG.equals(level) ? TextFormatting.DARK_GREEN : (Level.ERROR.equals(level) ? TextFormatting.RED : TextFormatting.WHITE);
            entityPlayerSP.sendMessage(new TextComponentString(color + string));
        }
    }

    public static String getCurrentGroup() {
        return Main.proxy instanceof ClientProxy ? CustomModel.getGlobalModelOverride() : CUSTOM_MODEL_KEY;
    }

    @SideOnly(value=Side.CLIENT)
    public static String getGlobalModelOverride() {
        String customModelsKey = CustomModel.getCustomModelsKey();
        return customModelsKey == null ? "sexmod/custom_models/singleplayer" : CUSTOM_MODELS_DIR + customModelsKey;
    }

    @SideOnly(value=Side.CLIENT)
    @Nullable
    public static String getCustomModelsKey() {
        Minecraft mc = Minecraft.getMinecraft();
        ServerData serverData = mc.getCurrentServerData();
        if (serverData == null) {
            return null;
        }
        String ip = serverData.serverIP;
        int portIndex = ip.indexOf(":");
        if (portIndex != -1) {
            ip = ip.substring(0, portIndex);
        }
        return ip;
    }

    public static int LoadModels(boolean bl) {
        logError(Level.INFO, "loading up custom models...");
        String group = getCurrentGroup();
        File dir = new File(group);
        dir.mkdirs();
        String[] modelNames = dir.list((file, string) -> new File(file, string).isDirectory());

        if (modelNames == null) {
            logError(Level.ERROR, String.format("Something is wrong with the custom models folder at '%s'. Check if it exists, if not - make the directory yourself because Minecraft cannot do it itself for some reason", dir.getAbsolutePath()));
            return -1;
        }

        logError(Level.INFO, String.format("found %s custom model(s)", modelNames.length));
        int count = 0;
        for (String modelName : modelNames) {
            String error = CustomModel.getPartName(modelName, group);
            if (!error.isEmpty()) {
                CustomModel.logError(Level.ERROR, error);
                return -1;
            }

            error = registerModel(modelName, group, bl);
            if (!error.isEmpty()) {
                CustomModel.logError(Level.ERROR, error);
                return -1;
            }
            ++count;
        }
        CustomModel.logError(Level.DEBUG, String.format("successfully registered %s custom models", count));
        isLoaded = true;
        return 0;
    }

    public static String getPartName(String modelName, String group) {
        String path = String.format("%s/%s", group, modelName);
        File geoFile = new File(String.format("%s/%s.geo.json", path, modelName));
        File textureFile = new File(String.format("%s/%s.png", path, modelName));
        File cfgFile = new File(String.format("%s/%s.cfg", path, modelName));
        if (!geoFile.exists()) {
            return String.format("couldn't find model File for '%s'. It should have been at '%s'. Are you sure it exists?", modelName, geoFile.getAbsolutePath());
        }
        if (!textureFile.exists()) {
            return String.format("couldn't find texture File for '%s'. It should have been at '%s'. Are you sure it exists?", modelName, textureFile.getAbsolutePath());
        }
        if (!cfgFile.exists()) {
            return String.format("couldn't find cfg File for '%s'. It should have been at '%s'. Are you sure it exists?", modelName, cfgFile.getAbsolutePath());
        }
        return "";
    }

    @SideOnly(value=Side.CLIENT)
    static ResourceLocation loadTexture(String name, File file) throws Exception {
        BufferedImage image = ImageIO.read(file);
        return Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(name, new DynamicTexture(image));
    }

    @SideOnly(value=Side.CLIENT)
    static RawGeoModel loadGeoModel(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Throwable throwable = null;
        try {
            String string;
            while ((string = reader.readLine()) != null) {
                stringBuilder.append(string);
            }
        } catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        } finally {
            if (reader != null) {
                if (throwable != null) {
                    try {
                        reader.close();
                    } catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                } else {
                    reader.close();
                }
            }
        }
        String json = stringBuilder.toString();
        return Converter.fromJsonString(json);
    }

    public static String registerModel(String modelName, String group, boolean textureFlag) {
        if (modelDataMap.get(modelName) != null) {
            return String.format("already registered '%s'... honestly, unsure how this could happen lol", modelName);
        }
        String modelDir = String.format("%s/%s/", group, modelName);
        String cfgPath = modelDir + modelName + ".cfg";
        File cfg = new File(cfgPath);

        if (!cfg.exists()) {
            return String.format("couldn't find cfg File for '%s'. It should have been at '%s'. Are you sure it exists?", modelName, cfgPath);
        }
        ModelData data = new ModelData(cfg, modelName);
        if (data.errorMessage != null) {
            return data.errorMessage;
        }
        String string5 = modelDir + modelName + ".png";
        File file2 = new File(string5);
        if (!file2.exists()) {
            return String.format("The texture for the custom model '%s' couldn't be found at '%s' are you sure it exists?", modelName, string5);
        }
        ResourceLocation resourceLocation = null;
        if (textureFlag) {
            try {
                resourceLocation = CustomModel.loadTexture(modelName, file2);
            } catch (IOException iOException) {
                return String.format("The texture for the custom model '%s' at '%s' appears to be corrupted. Try making a new one", modelName, string5);
            } catch (Exception exception) {
                return String.format("Couldn't load the texture for the custom model '%s' at '%s'. Maybe try increasing the amount of RAM of ur Minecraft client", modelName, file2);
            }
        }
        ResourceLocation resourceLocation2 = new ResourceLocation("sexmod", modelName + "Model");
        String string6 = modelDir + modelName + ".geo.json";
        File file3 = new File(string6);
        if (!file3.exists()) {
            return String.format("The geo model for the custom model '%s' couldn't be found at '%s' are you sure it exists?", modelName, string6);
        }
        if (textureFlag) {
            RawGeoModel rawGeoModel;
            try {
                rawGeoModel = CustomModel.loadGeoModel(file3);
            } catch (IOException iOException) {
                return String.format("The geo model for the custom model '%s' at '%s' appears to be corrupted. Try replacing it.", modelName, string6);
            }
            try {
                RawGeometryTree rawGeometryTree = RawGeometryTree.parseHierarchy(rawGeoModel, resourceLocation2);
                GeoModel geoModel = GeoBuilder.getGeoBuilder(resourceLocation2.getNamespace()).constructGeoModel(rawGeometryTree);
                GeckoLibCache.getInstance().getGeoModels().put(resourceLocation2, geoModel);
            } catch (Exception exception) {
                return String.format("The geo model for the custom model '%s' at '%s' appears to be corrupted. Try replacing it.", modelName, string6);
            }
        }
        if (textureFlag) {
            data.setFallBackTexture(resourceLocation2);
            data.setTextureLocation(resourceLocation);
        }
        modelDataMap.put(modelName, data);
        CustomModel.logError(Level.DEBUG, String.format("successfully registered custom model '%s'", modelName));
        return "";
    }

    public static ResourceLocation getModelResource(String string) {
        ModelData data = modelDataMap.get(string);
        if (data == null) {
            if (!string.equals("cross")) {
                System.out.printf("The custom model for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return null;
        }
        return data.getFallbackTexture();
    }

    public static ResourceLocation getTextureResource(String modelName) {
        ModelData data = modelDataMap.get(modelName);
        if (data == null) {
            if (!modelName.equals("cross")) {
                System.out.printf("The custom texture for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", modelName);
            }
            return null;
        }
        return data.getTextureLocation();
    }

    public static GeoModel getGeoModel(String string) {
        return GeckoLibCache.getInstance().getGeoModels().get(CustomModel.getModelResource(string));
    }

    public static CustomPartCategory getClothingType(String string) {
        ModelData data = modelDataMap.get(string);
        if (data == null) {
            if (!string.equals("cross")) {
                System.out.printf("The ClothingType for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return CustomPartCategory.HEAD;
        }
        return data.category;
    }

    public static HashSet<PlayerGirlEntity> getAllowedEntities(String string) {
        ModelData data = modelDataMap.get(string);
        if (data == null) {
            if (!string.equals("cross")) {
                System.out.printf("The HashSet<GirlType> for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return null;
        }
        return data.allowedNpcTypes;
    }

    public static HashSet<String> getCustomPartBones(String string) {
        ModelData data = modelDataMap.get(string);
        if (data == null) {
            if (!string.equals("cross")) {
                System.out.printf("The HashSet<String> for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return new HashSet<String>();
        }
        return data.customPartBones;
    }

    public static String getModelCode(String string) {
        ModelData data = modelDataMap.get(string);
        if (data == null) {
            if (!string.equals("cross")) {
                System.out.printf("The author for '%s', hasn't been registered, but gamers tried to use it anyways. Crash is imminent%n", string);
            }
            return "";
        }
        return data.modelCode;
    }

    @Nullable
    public static ModelData getModelDataForGirl(String data) {
        return modelDataMap.get(data);
    }

    public static HashMap<CustomPartCategory, List<String>> getModelTypes(GirlEntity girl) {
        HashMap<CustomPartCategory, List<String>> partsMap = new HashMap<CustomPartCategory, List<String>>();
        for (CustomPartCategory category : CustomPartCategory.values()) {
            partsMap.put(category, new ArrayList());
        }

        for (Map.Entry entry : modelDataMap.entrySet()) {
            //ModelData data;
            String modelName = (String)entry.getKey();
            ModelData data = (ModelData) entry.getValue();
            CustomPartCategory category = data.category;

            List<String> models = partsMap.get(category);
            if (data.allowedNpcTypes.isEmpty() || data.allowedNpcTypes.contains(PlayerGirlEntity.getGirlType(girl))) {
                models.add(modelName);
                partsMap.put(category, models);
            }
        }
        return partsMap;
    }

    public static HashMap<String, Float> getModelScales() {
        HashMap<String, Float> scales = new HashMap<String, Float>();
        for (Map.Entry<String, ModelData> entry : CustomModel.getModelDataMap().entrySet()) {
            scales.put(entry.getKey(), entry.getValue().getZOffset());
        }
        return scales;
    }

    public static class ModelData {
        CustomPartCategory category;
        HashSet<PlayerGirlEntity> allowedNpcTypes = new HashSet();
        HashSet<String> customPartBones = new HashSet();
        String modelCode;
        String modelName;
        boolean disabled;
        LightingType lightingType;
        float scale = 1.0f;
        float xOffset = 0.0f;
        ResourceLocation textureLocation;
        ResourceLocation fallbackTexture;
        public String errorMessage = null;
        float zOffset;

        public ModelData(File file, String modelName) {
            String posStr;
            String nudeStr;
            FileInputStream inputStream;
            if (modelName.contains(" ") || modelName.contains("#") || modelName.contains("$")) {
                this.errorMessage = String.format("You cannot call your custom model '%s'. '#', '$' and spaces are illegal characters", modelName);
                return;
            }
            if ("cross".equalsIgnoreCase(modelName)) {
                this.errorMessage = "You cannot call your custom model 'cross'. Im sorry, but I need that specific name for internal stuff";
                return;
            }

            Properties properties = new Properties();
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException fileNotFoundException) {
                this.errorMessage = String.format("couldn't find cfg File for '%s'. It should have been at '%s'. Are you sure it exists?", modelName, file.getAbsolutePath());
                return;
            }
            try {
                properties.load(inputStream);
            } catch (IOException iOException) {
                this.errorMessage = String.format("couldn't read the cfg File for '%s' at '%s'. It appears to be corrupted. Try making a new one", modelName, file.getAbsolutePath());
                return;
            }
            String wearTypeStr = properties.getProperty("wear_type");
            if (wearTypeStr == null) {
                this.errorMessage = String.format("The cfg File for the model '%s' at '%s' is missing the 'wear_type'. Go to the bottom of the cfg File and write 'wear_type=HEAD'. Check the cfg files of my examples to see what values for 'wear_type' are possible", modelName, file.getAbsolutePath());
                return;
            }
            try {
                wearTypeStr = wearTypeStr.replace(" ", "");
                this.category = CustomPartCategory.valueOf(wearTypeStr);
            } catch (IllegalArgumentException illegalArgumentException) {
                this.errorMessage = String.format("you entered '%s' into the 'wear_type' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'wear_type", wearTypeStr, modelName, file.getAbsolutePath());
                return;
            }
            if (CustomPartCategory.CUSTOM_BONE.equals(this.category)) {
                this.modelName = properties.getProperty("custom_bone");
                if ("".equals(this.modelName)) {
                    this.errorMessage = String.format("You selected CUSTOM_BONE as the 'wear_type' in the cfg file for '%s' at '%s', yet you left the 'custom_bone' field right underneath it empty. If you want ur model to be parented to a specific bone, you have to enter the name of that bone at the field 'custom_bone'.", modelName, file.getAbsolutePath());
                    return;
                }
            }
            String girlsStr = properties.getProperty("which_girls");
            girlsStr = girlsStr.replace(" ", "");
            String[] girlsArray = girlsStr.split(",");
            for (String girl : girlsArray) {
                try {
                    if ("".equals(girl)) continue;
                    this.allowedNpcTypes.add(PlayerGirlEntity.valueOf(girl));
                } catch (IllegalArgumentException illegalArgumentException) {
                    this.errorMessage = String.format("you entered '%s' as one of the girls, you put into the 'which_girls' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'which_girls'.", girl, modelName, file.getAbsolutePath());
                    return;
                }
            }
            String lightingStr = properties.getProperty("which_lighting");
            if (lightingStr == null) {
                this.errorMessage = String.format("The %s's cfg file at '%s' doesn't contain the field 'which_lighting'. Go to the bottom of the cfg file and write either 'which_lighting=DEFAULT', 'which_lighting=SEXMOD', or 'which_lighting=NONE'.", modelName, file.getAbsolutePath());
                return;
            }
            lightingStr = lightingStr.replace(" ", "");
            try {
                this.lightingType = LightingType.valueOf((String)lightingStr);
            } catch (IllegalArgumentException illegalArgumentException) {
                this.errorMessage = String.format("you entered '%s' into the 'which_lighting' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'which_lighting'.", lightingStr, modelName, file.getAbsolutePath());
            }
            String authorStr = properties.getProperty("author");
            this.modelCode = authorStr == null || "".equals(authorStr) ? "anon" : authorStr;
            String bonesToHideStr = properties.getProperty("bones_to_hide");
            if (bonesToHideStr != null && !"".equals(bonesToHideStr)) {
                bonesToHideStr = bonesToHideStr.replace(" ", "");
                String[] boneNames = bonesToHideStr.split(",");
                this.customPartBones.addAll(Arrays.asList(boneNames));
            }
            if ((nudeStr = properties.getProperty("enable_when_nude")) == null) {
                this.disabled = false;
            } else {
                String nudeStrConvert = nudeStr.replace(" ", "");
                this.disabled = nudeStrConvert.equalsIgnoreCase("yes");
            }
            String sizeStr = properties.getProperty("gui_size_factor");
            if (sizeStr != null && !sizeStr.isEmpty()) {
                sizeStr = sizeStr.replace(" ", "");
                sizeStr = sizeStr.replace(",", ".");
                try {
                    this.scale = Float.parseFloat(sizeStr);
                } catch (NumberFormatException numberFormatException) {
                    this.errorMessage = String.format("you entered '%s' into the 'gui_size_factor' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'gui_size_factor'.", sizeStr, modelName, file.getAbsolutePath());
                }
            }
            if ((posStr = properties.getProperty("gui_vertical_positioning")) != null && !posStr.isEmpty()) {
                posStr = posStr.replace(" ", "");
                posStr = posStr.replace(",", ".");
                try {
                    this.xOffset = Float.parseFloat(posStr);
                } catch (NumberFormatException numberFormatException) {
                    this.errorMessage = String.format("you entered '%s' into the 'gui_vertical_positioning' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'gui_vertical_positioning'.", posStr, modelName, file.getAbsolutePath());
                }
            }
            String versionStr = properties.getProperty("version");
            versionStr = versionStr.replace(" ", "");
            versionStr = versionStr.replace(",", ".");
            try {
                this.zOffset = Float.parseFloat(versionStr);
            } catch (NumberFormatException numberFormatException) {
                this.errorMessage = String.format("you entered '%s' into the 'versionString' field of the %s's cfg file at '%s'. This is not a valid value. Check my examples on what valid values are to enter into the field 'versionString'.", versionStr, modelName, file.getAbsolutePath());
            }
        }

        public String getModelName() {
            return this.modelName;
        }

        public LightingType getLightingType() {
            return this.lightingType;
        }

        public float getXOffset() {
            return this.xOffset;
        }

        public float getScale() {
            return this.scale;
        }

        public CustomPartCategory getCategory() {
            return this.category;
        }

        public HashSet<PlayerGirlEntity> getAllowedNpcTypes() {
            return this.allowedNpcTypes;
        }

        public String getModelCode() {
            return this.modelCode;
        }

        public boolean isDisabled() {
            return this.disabled;
        }

        public HashSet<String> getCustomPartBones() {
            return this.customPartBones;
        }

        public ResourceLocation getTextureLocation() {
            return this.textureLocation;
        }

        public void setTextureLocation(ResourceLocation location) {
            this.textureLocation = location;
        }

        public ResourceLocation getFallbackTexture() {
            return this.fallbackTexture;
        }

        public void setFallBackTexture(ResourceLocation location) {
            this.fallbackTexture = location;
        }

        public float getZOffset() {
            return this.zOffset;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static class ChatHandler {
        boolean hasSentId = false;

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onClientChat(ClientChatEvent event) {
            String message = event.getOriginalMessage();
            if (message.equals("id")) {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                List<GirlEntity> girls = player.world.getEntitiesWithinAABB(GirlEntity.class, player.getEntityBoundingBox().grow(10.0));
                GirlEntity selected = null;
                for (GirlEntity girl : girls) {
                    if (selected == null) {
                        selected = girl;
                    } else {
                        if (player.getDistance(girl) < player.getDistance(selected)) {
                            selected = girl;
                        }
                    }
                }
                if (selected != null) {
                    player.sendStatusMessage(new TextComponentString(selected.girlID().toString()), false);
                    event.setCanceled(true);
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> CustomModel.LoadModels(true));
            this.hasSentId = false;
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onJoinWorld(EntityJoinWorldEvent event) {
            if (event.getEntity().equals(Minecraft.getMinecraft().player)) {
                if (!this.hasSentId) {
                    this.hasSentId = true;
                    if (CustomModel.isGlobalRenderingDisabled()) {
                        CustomModel.reloadCustomModels();
                    }
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
            Minecraft.getMinecraft().addScheduledTask(() -> CustomModel.setGlobalRenderingDisabled(true));
            this.hasSentId = false;
        }
    }
}

