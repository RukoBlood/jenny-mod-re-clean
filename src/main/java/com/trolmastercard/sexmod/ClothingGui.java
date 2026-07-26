/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import com.trolmastercard.sexmod.Packages.UploadModelString;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//a.class
public class ClothingGui extends GuiScreen {
    final static public ResourceLocation CLOTHING_ICONS_TEX = new ResourceLocation("sexmod", "textures/gui/clothing_icons.png");
    final static int ICON_SIZE = 20;
    final static float ROTATION_DRAG_FACTOR = 0.25f;
    int guiCenterX = 0;
    int guiCenterY = 0;
    float guiScale = 0.0f;
    static public float MODEL_Y_ROTATION = 0.0f;
    static protected List<Integer> mouseDragDeltas = new ArrayList<Integer>();
    static protected int currentDragVelocity = 0;
    static protected int prevDragVelocity = 0;
    public GirlEntity previewGirl;
    boolean isMouseDragging = false;
    CustomPartListScrollList scrollList;
    static public List<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> activeCategories = new ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>>();
    final UUID ID;
    int customCategoryCount;
    int lastMouseX;
    public boolean isClickHeld = false;
    int rotationInertia = 0;
    int autoRotationDirection = 1;

    public ClothingGui(@Nonnull GirlEntity targetGirl) {
        //Object object;
        this.mc = Minecraft.getMinecraft();
        this.ID = targetGirl.girlID();

        PlayerGirlEntity playerGirlEnum = PlayerGirlEntity.fromGirl(targetGirl);
        if (playerGirlEnum == null) {
            playerGirlEnum = PlayerGirlEntity.JENNY;
        }

        try {
            Constructor<?> constructor = playerGirlEnum.npcClass.getConstructor(World.class);
            this.previewGirl = (GirlEntity)((Constructor)constructor).newInstance(this.mc.world);
            this.previewGirl.setLocallyRegistered(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initPartCategories();

        String customModelKey = targetGirl.getCustomModelKey();
        this.previewGirl.getDataManager().set(GirlEntity.CUSTOM_MODEL_KEY, customModelKey);
        int customBoneCount = 0;

        for (String partName : this.previewGirl.getCustomPartsSet()) {
            CustomPartCategory category = CustomModel.e(partName);
            if (CustomPartCategory.CUSTOM_BONE.equals((Object)category)) {
                ++customBoneCount;
            }

            Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> targetEntry = null;

            if (CustomPartCategory.CUSTOM_BONE.equals((Object)category) && customBoneCount > 1) {
                targetEntry = ClothingGui.createCustomBoneEntry(this.previewGirl);
            } else {
                for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : activeCategories) {
                    if (!entry.getKey().equals((Object)category)) continue;
                    targetEntry = entry;
                }
            }

            if (targetEntry == null) continue;
            activeCategories.remove(targetEntry);
            int selectedIndex = targetEntry.getValue().getKey().indexOf(partName);
            if (selectedIndex == -1) {
                selectedIndex = 0;
            }
            targetEntry.getValue().setValue(selectedIndex);
            activeCategories.add(targetEntry);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        //this.scrollList.handleMouseInput();
        if (this.scrollList != null){
            this.scrollList.handleMouseInput();
        }
    }

    //somehow related to GirlRenderer
    public static HashSet<String> getSelectedPartsSet() {
        HashSet<String> selectedParts = new HashSet<String>();
        for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : activeCategories) {
            if (entry.getValue().getKey().size() == 1) continue;

//            Map.Entry<List<String>, Integer> removets = entry.getValue();
//            List<String> modelList = removets.getKey();
            List<String> modelList = entry.getValue().getKey();
            Integer index = entry.getValue().getValue();
            selectedParts.add(modelList.get(index));
        }
        return selectedParts;
    }

    public static Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> createCustomBoneEntry(GirlEntity girl) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("cross");
        arrayList.addAll(CustomModel.a(girl).get((Object) CustomPartCategory.CUSTOM_BONE));
        return new AbstractMap.SimpleEntry<CustomPartCategory, Map.Entry<List<String>, Integer>>(CustomPartCategory.CUSTOM_BONE, new AbstractMap.SimpleEntry<>(arrayList, 0));
    }

    void initPartCategories() {
        activeCategories.clear();
        List<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> girlCategories = this.previewGirl.d(this.ID);
        this.customCategoryCount = girlCategories.size();
        activeCategories.addAll(girlCategories);

        for (CustomPartCategory category : CustomPartCategory.values()) {
            if (category == CustomPartCategory.GIRL_SPECIFIC) continue;
            
            ArrayList<String> defaultList = new ArrayList<String>();
            defaultList.add("cross");
            activeCategories.add(new AbstractMap.SimpleEntry<>(category, new AbstractMap.SimpleEntry<>(defaultList, 0)));
        }

        for (Map.Entry modelEntry : CustomModel.a(this.previewGirl).entrySet()) {
            Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> targetEntry = null;

            for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : activeCategories) {
                if (!((CustomPartCategory)((Object)modelEntry.getKey())).equals((Object)entry.getKey())) continue;
                targetEntry = entry;
            }

            if (targetEntry == null) continue;
            int index = activeCategories.indexOf(targetEntry);
            activeCategories.remove(targetEntry);
            ((List)((Map.Entry)targetEntry.getValue()).getKey()).addAll((Collection)modelEntry.getValue());
            activeCategories.add(index, targetEntry);
        }
    }

    @Override
    public void initGui() {
        this.scrollList = new CustomPartListScrollList(this.mc, this);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        this.guiCenterX = this.getPercentWidth(76.0f);
        this.guiCenterY = this.getPercentHeight(89.0f);
        this.guiScale = 90.0f;
    }

    boolean isHovered(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
//        if (mouseX < x1) {
//            return false;
//        }
//        if (mouseX > x2) {
//            return false;
//        }
//        if (mouseY < y1) {
//            return false;
//        }
//        return mouseY <= y2;
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.isMouseDragging) {
            MODEL_Y_ROTATION += Reference.LerpFloat((float) prevDragVelocity, (float) currentDragVelocity, partialTicks);
        }
        this.updateModelInertiaRotation();
        this.mc.renderEngine.bindTexture(CLOTHING_ICONS_TEX);
        int btnX = this.guiCenterX - this.getPercentWidth(15.0f);
        int btnY = this.guiCenterY - 20;

        this.drawTexturedModalRect(btnX, btnY, 100, this.isHovered(mouseX, mouseY, btnX, btnY, btnX + 20, btnY + 20) ? 40 : 20, 20, 20);
        if (CustomModel.getGlobalModelOverride() == null) {
            this.drawSideButtons(btnX, mouseX, mouseY);
        }
        this.renderEntityInGui(this.guiCenterX, this.guiCenterY, this.guiScale, this.previewGirl, 1.2345679f);
        this.previewGirl.onUpdate();
        //this.scrollList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.scrollList != null) {
            this.scrollList.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    // void b(int n, int n2, int n3) {
    //     int n4;
    //     this.drawTexturedModalRect(n, n4, 120, this.a(n2, n3, n, n4 = this.l - 40, n + 20, n4 + 20) ? 40 : 20, 20, 20);
    //     this.drawTexturedModalRect(n, n4, 20, this.a(n2, n3, n, n4 -= 20, n + 20, n4 + 20) ? 170 : 150, 20, 20);
    //     this.drawTexturedModalRect(n, n4, 0, this.a(n2, n3, n, n4 -= 20, n + 20, n4 + 20) ? 170 : 150, 20, 20);
    // }

    void drawSideButtons(int btnX, int mouseX, int mouseY) {
        int currentY = this.guiCenterY - 40;
        this.drawTexturedModalRect(btnX, currentY, 120, this.isHovered(mouseX, mouseY, btnX, currentY, btnX + 20, currentY + 20) ? 40 : 20, 20, 20);
        currentY -= 20;
        this.drawTexturedModalRect(btnX, currentY, 20, this.isHovered(mouseX, mouseY, btnX, currentY, btnX + 20, currentY + 20) ? 170 : 150, 20, 20);
        currentY -= 20;
        this.drawTexturedModalRect(btnX, currentY, 0, this.isHovered(mouseX, mouseY, btnX, currentY, btnX + 20, currentY + 20) ? 170 : 150, 20, 20);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    void saveAndClose() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));

        HashSet<String> selectedParts = new HashSet<String>();
        ArrayList<Integer> girlSpecificIndices = new ArrayList<Integer>();

        for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : activeCategories) {
            if (entry.getKey() == CustomPartCategory.GIRL_SPECIFIC) {
                girlSpecificIndices.add(entry.getValue().getValue());
                continue;
            }
            Map.Entry<List<String>, Integer> modelData = entry.getValue();

            Integer selectedIndex = modelData.getValue();
            if (selectedIndex == 0) continue;
            String modelName = modelData.getKey().get(selectedIndex);
            selectedParts.add(modelName);
        }

        PackageHandler.networkWrapper.sendToServer(
                (IMessage)new UploadModelString(GirlEntity.serializePartsSet(selectedParts), this.ID, girlSpecificIndices)
        );

        this.mc.player.closeScreen();
    }

    public void cycleCategoryOption(CustomPartCategory category, boolean forward, int itemIndex) {
        int globalListIndex;
        //Object object;
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));

        ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> matchingEntries = new ArrayList<>();
        ArrayList<Integer> matchingGlobalIndices = new ArrayList<>();

        int indexCounter = 0;

        for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : activeCategories) {
            if (entry.getKey().equals((Object)category)) {
                matchingEntries.add(entry);
                matchingGlobalIndices.add(indexCounter);
            }
            ++indexCounter;
        }

        if (matchingEntries.isEmpty()) {
            return;
        }

        Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> selectedEntry;
        if (matchingEntries.size() == 1) {
            selectedEntry = matchingEntries.get(0);
            globalListIndex = (Integer)matchingGlobalIndices.get(0);
        } else {
            int subIndex = this.customCategoryCount == 0 || itemIndex > this.customCategoryCount - 1 + CustomPartCategory.a()
                    ? itemIndex - (this.customCategoryCount + CustomPartCategory.a())
                    : itemIndex;
            selectedEntry = matchingEntries.get(subIndex);
            globalListIndex = (Integer)matchingGlobalIndices.get(subIndex);
        }

        if (selectedEntry == null) {
            return;
        }

        Map.Entry<List<String>, Integer> partData = selectedEntry.getValue();
        int currentOption = (Integer)partData.getValue();
        int totalOptions = ((List)partData.getKey()).size();

        if (forward) {
            if (++currentOption >= totalOptions) {
                currentOption = 0;
            }
        } else if (--currentOption < 0) {
            currentOption = totalOptions - 1;
        }

        activeCategories.set(globalListIndex, new AbstractMap.SimpleEntry<>(
                selectedEntry.getKey(),
                new AbstractMap.SimpleEntry<>((selectedEntry.getValue()).getKey(), currentOption)
        ));

        ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> entries = new ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>>();
        for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry2 : activeCategories) {
            if (entry2.getKey() != CustomPartCategory.GIRL_SPECIFIC) continue;
            entries.add(entry2);
        }
        this.previewGirl.b(entries);
    }

    public void renderCustomModel(int x, int y, float scale, CustomModelEntity entity) {
        this.renderEntityInGui(x, y, scale, entity, 1.876945f);
    }

    public void renderItemModel(CustomModelEntity entity) {
        this.renderEntityInGui(this.guiCenterX, this.guiCenterY, this.guiScale, entity, 2.876945f, entity.isItemModel ? 1 : 0);
    }

    public void drawHoverText(String text, int x, int y) {
        this.drawHoveringText(text, x, y);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
//        if (clickedMouseButton != 0) {
//            return;
//        }
//        if (mouseX < this.width / 2) {
//            return;
//        }
        if (clickedMouseButton != 0 || mouseX < this.width / 2) {
            return;
        }
        int dX = mouseX - this.lastMouseX;
        mouseDragDeltas.add(dX);
        this.lastMouseX = mouseX;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);

        //this.scrollList.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.scrollList != null) {
            this.scrollList.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (mouseButton != 0) {
            return;
        }

        this.isClickHeld = true;
        this.isMouseDragging = true;
        this.lastMouseX = mouseX;

        int btnX = this.guiCenterX - this.getPercentWidth(15.0f);
        int btnY = this.guiCenterY - 20;

        if (this.isHovered(mouseX, mouseY, btnX, btnY, btnX + 20, btnY + 20)) {
            this.saveAndClose();
        }

        if (CustomModel.getGlobalModelOverride() != null) {
            return;
        }

        btnY = this.guiCenterY - 40;
        if (this.isHovered(mouseX, mouseY, btnX, btnY, btnX + 20, btnY + 20)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.mc.player.closeScreen();
            int errCode = CustomModel.b(true);

            if (errCode != 0) {
                CustomModel.isGlobalRenderingDisabled = true;
                return;
            }
            GirlEntity girl = GirlEntity.getClientGirlEntity(this.ID);
            if (girl != null) {
                ClothingGui.openGuiForGirl(girl);
            }
            return;
        }

        if (this.isHovered(mouseX, mouseY, btnX, btnY -= 20, btnX + 20, btnY + 20)) {
            Desktop.getDesktop().open(new File(CustomModel.d()));
            return;
        }

        if (this.isHovered(mouseX, mouseY, btnX, btnY -= 20, btnX + 20, btnY + 20)) {
            try {
                //Desktop.getDesktop().browse(new URI("http://fapcraft.org/assets/video/tutorial/girl_wand.mp4"));
                //This fucking throws 404
            } catch (Exception uRISyntaxException) {
                throw new RuntimeException(uRISyntaxException);
            }
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.isMouseDragging = false;
            this.isClickHeld = false;
        }
        this.rotationInertia = prevDragVelocity;
    }

    int getPercentWidth(float percentage) {
        return Math.round((float)this.width * (percentage / 100.0f));
    }

    int getPercentHeight(float percentage) {
        return Math.round((float)this.height * (percentage / 100.0f));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.previewGirl.world.removeEntityDangerously(this.previewGirl);
        mouseDragDeltas.clear();
        activeCategories.clear();
    }

    public GirlEntity getPreviewGirl() {
        return this.previewGirl;
    }

    public void drawIcon(int x, int y, int u, int v) {
        this.mc.renderEngine.bindTexture(CLOTHING_ICONS_TEX);
        this.drawTexturedModalRect(x, y, u, v, 20, 20);
    }

    public void drawDefaultIcon(int n, int n2, int n3) {
        this.drawIcon(n, n2, n3, 0);
    }

    public void drawIcon(int n, int n2, Point2D e1_class2172) {
        this.drawIcon(n, n2, e1_class2172.x, e1_class2172.y);
    }

    void renderEntityInGui(int x, int y, float scale, EntityLivingBase entity, float partialTicks) {
        this.renderEntityInGui(x, y, scale, entity, partialTicks, 0);
    }

    void renderEntityInGui(int x, int y, float scale, EntityLivingBase entity, float partialTicks, int zOffest) {
        float renderYawOffset = entity.renderYawOffset;
        float prevRotationYaw = entity.rotationYaw;
        float prevRotationPitch = entity.rotationPitch;
        float prevPrevRotationYawHead = entity.prevRotationYawHead;
        float prevRotationYawHead = entity.rotationYawHead;

        entity.renderYawOffset = 0.0f;
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.prevRotationYawHead = 0.0f;
        entity.rotationYawHead = 0.0f;

        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);

        RenderHelper.enableStandardItemLighting();

        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, zOffest);
        GlStateManager.rotate(MODEL_Y_ROTATION, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(0.25f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks, false);
        renderManager.setRenderShadow(true);

        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        entity.renderYawOffset = renderYawOffset;
        entity.rotationYaw = prevRotationYaw;
        entity.rotationPitch = prevRotationPitch;
        entity.prevRotationYawHead = prevPrevRotationYawHead;
        entity.rotationYawHead = prevRotationYawHead;
    }

    void updateModelInertiaRotation() {
        if (this.isMouseDragging) {
            return;
        }
        float fps = Minecraft.getDebugFPS();
        if (fps == 0.0f) {
            fps = 0.1f;
        }//Should be not null 0.1fps at least

        if (this.rotationInertia == 0) {
            MODEL_Y_ROTATION += (float)(this.autoRotationDirection * 10) / fps;
            return;
        }

        MODEL_Y_ROTATION += (float)this.rotationInertia / fps;
        this.rotationInertia = (int)((float)this.rotationInertia * (1.0f - 0.25f / fps));

        if (Math.abs(this.rotationInertia) > 10) {
            return;
        }

        this.autoRotationDirection = this.rotationInertia > 0 ? 1 : -1;
        this.rotationInertia = 0;
    }

    @SideOnly(value=Side.CLIENT)
    public static void openGuiForGirl(@Nonnull GirlEntity girl) {

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen instanceof ClothingGui) {
            return;
        }

        boolean canOpen = CustomModel.getGlobalModelOverride() == null || CustomModel.b();

        if (!canOpen) {
            mc.player.sendStatusMessage(new TextComponentString("You have to whitelist the server to use its custom models. " + (Object)((Object)TextFormatting.YELLOW) + "/whitelistserver"), true);
            return;
        }
        mc.addScheduledTask(() -> mc.displayGuiScreen(new ClothingGui(girl)));
    }


    @SideOnly(value=Side.CLIENT)
    public static class EventHandler {
        @SubscribeEvent
        @SideOnly(value=Side.CLIENT)

        public void onKeyInput(InputEvent.KeyInputEvent event) {
            if (!ClientProxy.keyBindings[1].isPressed()) {
                return;
            }
            if (CustomModel.isGlobalRenderingDisabled) {
                CustomModel.isGlobalRenderingDisabled = 0 != CustomModel.b(true);
                if (CustomModel.isGlobalRenderingDisabled) {
                    return;
                }
            }
            Minecraft mc = Minecraft.getMinecraft();
            PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(mc.player.getPersistentID());
            if (playerGirl == null) {
                mc.player.sendStatusMessage(new TextComponentString("You have to turn into the girl you want to customize"), true);
                return;
            }

            ClothingGui.openGuiForGirl(playerGirl);
        }

        @SubscribeEvent
        @SideOnly(value=Side.CLIENT)
        public void onClientTick(TickEvent.ClientTickEvent event) {
            prevDragVelocity = currentDragVelocity;
            currentDragVelocity = 0;
            for (Integer delta : mouseDragDeltas) {
                currentDragVelocity += delta;
            }
            mouseDragDeltas.clear();
        }
    }
}

