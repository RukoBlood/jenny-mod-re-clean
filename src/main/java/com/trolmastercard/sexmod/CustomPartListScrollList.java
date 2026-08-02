/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CustomPartListScrollList extends GuiListExtended {
    final static int TEXT_COLOR = 3809871;
    final static List<CustomPartCategory> CATEGORY_ORDER = Arrays.asList(CustomPartCategory.values());
    final static String TRUNCATE_TEMPLATE = "MMMMMMMMMM";
    static protected int xOffset = 5;
    static protected int listWidth = 200;
    private final List<PartListEntry> entries = new ArrayList<PartListEntry>();
    public ClothingGui parentGUI;
    boolean needsScrollToBottom = false;
    float g = 0.0f;

    public CustomPartListScrollList(Minecraft mc, ClothingGui parentGui) {
        super(mc, parentGui.width / 2, parentGui.height, 0, parentGui.height, 30);
        listWidth = parentGui.width / 2;
        this.parentGUI = parentGui;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        return this.entries.get(index);
    }

    @Override
    protected int getSize() {
        return this.entries.size();
    }

    @Override
    protected int getScrollBarX() {
        return 0;
    }

    protected void drawContainerBackground(Tessellator tessellator) {
    }

    @Override
    public void handleMouseInput() {
        if (!this.isMouseYWithinSlotBounds(this.mouseY)) {
            return;
        }
        int wheelDelta = Mouse.getEventDWheel();
        if (wheelDelta == 0) {
            return;
        }
        wheelDelta = wheelDelta > 0 ? -1 : 1;
        this.amountScrolled += (float)(wheelDelta * this.slotHeight / 2);
    }

    @Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
    }

    void updateTopPadding() {
        int totalContentHeight = this.entries.size() * this.slotHeight;
        if (totalContentHeight > this.height) {
            this.top = 0;
            return;
        }
        int remainingSpace = this.height - totalContentHeight;
        this.top = remainingSpace / 2;
    }

    @Override
    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        this.entries.clear();
        int customBoneCount = 0;

        for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : ClothingGui.activeCategories) {
            CustomPartCategory category = entry.getKey();
            Map.Entry<List<String>, Integer> modelData = entry.getValue();
            this.entries.add(new PartListEntry(category, modelData.getKey(), modelData.getValue()));

            if (!CustomPartCategory.CUSTOM_BONE.equals((Object)entry.getKey())) continue;
            customBoneCount++;
        }

        this.entries.sort(Comparator.comparingInt(entry -> CATEGORY_ORDER.indexOf(entry.category)));
        List<String> boneModels = CustomModel.a(this.parentGUI.previewGirl).get((Object) CustomPartCategory.CUSTOM_BONE);
        boneModels.add(0, "cross");
        this.entries.add(new PartListEntry(customBoneCount > 1));
        this.updateTopPadding();
        this.renderListContent(mouseXIn, mouseYIn, partialTicks);
        if (!this.needsScrollToBottom) {
            return;
        }
        this.scrollBy(999999);
        this.needsScrollToBottom = false;
    }

    void renderListContent(int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();

        int scrollBarLeft = this.getScrollBarX();
        int scrollBarRight = scrollBarLeft + 6;
        this.bindAmountScrolled();

        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        this.drawContainerBackground(tessellator);

        int contentX = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        int contentY = this.top + 4 - (int)this.amountScrolled;
        if (this.hasListHeader) {
            this.drawListHeader(contentX, contentY, tessellator);
        }

        this.drawSelectionBox(contentX, contentY, mouseX, mouseY, partialTicks);
        GlStateManager.disableDepth();
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableTexture2D();

        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            int scrollBarHeight = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            int scrollBarTop = (int)this.amountScrolled * (this.bottom - this.top - (scrollBarHeight = MathHelper.clamp(scrollBarHeight, 32, this.bottom - this.top - 8))) / maxScroll + this.top;
            if (scrollBarTop < this.top) {
                scrollBarTop = this.top;
            }

            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(scrollBarLeft, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            bufferBuilder.pos(scrollBarRight, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            bufferBuilder.pos(scrollBarRight, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            bufferBuilder.pos(scrollBarLeft, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            tessellator.draw();

            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(scrollBarLeft, scrollBarTop + scrollBarHeight, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            bufferBuilder.pos(scrollBarRight, scrollBarTop + scrollBarHeight, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            bufferBuilder.pos(scrollBarRight, scrollBarTop, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            bufferBuilder.pos(scrollBarLeft, scrollBarTop, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            tessellator.draw();

            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(scrollBarLeft, scrollBarTop + scrollBarHeight - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
            bufferBuilder.pos(scrollBarRight - 1, scrollBarTop + scrollBarHeight - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
            bufferBuilder.pos(scrollBarRight - 1, scrollBarTop, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
            bufferBuilder.pos(scrollBarLeft, scrollBarTop, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
            tessellator.draw();

        }

        this.renderDecorations(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        this.dispatchItemClick(mouseX, mouseY, mouseEvent);
        return super.mouseClicked(mouseX, mouseY, mouseEvent);
    }

    void dispatchItemClick(int mouseX, int mouseY, int mouseEvent) {
        if (mouseX > this.width) {
            return;
        }

        int scrolled = this.getAmountScrolled();
        float relativeY = scrolled + mouseY - 5 - this.top;

        int selectedIndex = Math.round((float)Math.floor(relativeY / (float)this.slotHeight));
        int slotClickY = (int)Math.round(((double)(relativeY / (float)this.slotHeight) - Math.floor(relativeY / (float)this.slotHeight)) * (double)this.slotHeight);
        if (selectedIndex < 0) {
            return;
        }
        if (selectedIndex < this.entries.size()) {
            this.entries.get(selectedIndex).handleEntryClick(mouseX, slotClickY, mouseEvent, selectedIndex);
        }
    }

    /*Gay java optimization. CFR did bad job cleaning.*/

//    static Minecraft access$000(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$100(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$200(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$300(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$400(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$500(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$600(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }

//    static Minecraft access$700(CustomPartListScrollList gq_class3812) {
//        return gq_class3812.mc;
//    }


    @SideOnly(value=Side.CLIENT)
    public class PartListEntry implements GuiListExtended.IGuiListEntry {
        final static int g = 4;
        public CustomPartCategory category;
        public List<String> modelList;
        public int selectedIndex;
        FontRenderer fontRenderer;
        boolean isBoneControlButton = false;
        boolean canRemoveBone = false;
        //final CustomPartListScrollList this$0;

        public PartListEntry(CustomPartCategory category, List<String> modelList, int selectedIndex) {

            this.category = category;
            this.modelList = modelList;
            this.selectedIndex = selectedIndex;
            this.fontRenderer = CustomPartListScrollList.this.mc.fontRenderer;
        }

        public PartListEntry(boolean canRemoveBone) {

            this.canRemoveBone = canRemoveBone;
            this.isBoneControlButton = true;
            this.fontRenderer = CustomPartListScrollList.this.mc.fontRenderer;
        }


        boolean isHovered(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
            return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
        }

        void renderBoneControls(int y, int mouseX, int mouseY) {
            int buttonX = 30;
            CustomPartListScrollList.this.mc.renderEngine.bindTexture(ClothingGui.CLOTHING_ICONS_TEX);
            CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(buttonX, y, 40, this.isHovered(mouseX, mouseY, buttonX, y += 5, buttonX + 20, y + 20) ? 40 : 20, 20, 20);
            CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(buttonX, y, this.canRemoveBone ? 60 : 80, this.canRemoveBone && this.isHovered(mouseX, mouseY, buttonX += 40, y, buttonX + 20, y + 20) ? 40 : 20, 20, 20);
        }

        void renderModelSlot(int y, int mouseX, int mouseY) {
            CustomPartListScrollList.this.mc.renderEngine.bindTexture(ClothingGui.CLOTHING_ICONS_TEX);
            CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(xOffset, y, 0, 60, this.selectedIndex == 0 ? 119 : 256, 30);
            int currentX = xOffset + 10;
            CustomPartListScrollList.this.parentGUI.drawDefaultIcon(currentX, y += 5, this.category.iconXPos);

            currentX += 25;
            currentX = this.renderArrowButtons(currentX, y, mouseX, mouseY);
            
            GirlEntity previewGirl = CustomPartListScrollList.this.parentGUI.getPreviewGirl();
            CustomModelEntity modelEntity = this.selectedIndex == 0 ? CustomModelEntity.a(CustomPartListScrollList.this.mc.world, previewGirl.girlID(), this.category) : new CustomModelEntity(previewGirl.world, previewGirl.girlID(), this.modelList.get(this.selectedIndex));
            CustomModel.ModelData modelData = CustomModel.getModelData(modelEntity.getModelName());

            float scaleFactor = modelEntity.isItemModel || modelData == null ? 1.0f : modelData.d();
            int yOffset = modelData == null ? 0 : (int)(-modelData.g());

            CustomPartListScrollList.this.parentGUI.renderCustomModel(currentX, y + 10 + (modelEntity.isItemModel ? 0 : 6) + yOffset, 30.0f * scaleFactor, modelEntity);
            if (this.selectedIndex != 0) {
                CustomPartListScrollList.this.parentGUI.renderItemModel(modelEntity);
            }
            CustomPartListScrollList.this.mc.world.removeEntityDangerously(modelEntity);
            currentX += 30;

            if (this.selectedIndex == 0) {
                return;
            }

            int nameX = currentX;
            String fullModelName = this.modelList.get(this.selectedIndex);
            String truncatedModelName = fullModelName.length() > CustomPartListScrollList.TRUNCATE_TEMPLATE.length()
                    ? fullModelName.substring(0, CustomPartListScrollList.TRUNCATE_TEMPLATE.length() - 3) + "..."
                    : fullModelName;

            this.drawString(truncatedModelName, currentX, y + 10);
            int nameWidth = currentX += this.fontRenderer.getStringWidth(CustomPartListScrollList.TRUNCATE_TEMPLATE);
            int authorX = currentX;
            String authorName = CustomModel.d(fullModelName);
            String truncatedAuthorName = authorName.length() > CustomPartListScrollList.TRUNCATE_TEMPLATE.length()
                    ? authorName.substring(0, CustomPartListScrollList.TRUNCATE_TEMPLATE.length() - 3) + "..."
                    : authorName;

            this.drawString(truncatedAuthorName, currentX, y + 10);
            int authorWidth = currentX + this.fontRenderer.getStringWidth(CustomPartListScrollList.TRUNCATE_TEMPLATE);

            if (this.isHovered(mouseX, mouseY, nameX, y + 10, nameWidth, y + 10 + this.fontRenderer.FONT_HEIGHT)) {
                CustomPartListScrollList.this.parentGUI.drawHoverText(fullModelName, mouseX, mouseY);
            }

            if (this.isHovered(mouseX, mouseY, authorX, y + 10, authorWidth, y + 10 + this.fontRenderer.FONT_HEIGHT)) {
                CustomPartListScrollList.this.parentGUI.drawHoverText(authorName, mouseX, mouseY);
            }

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.color(255.0f, 255.0f, 255.0f, 255.0f);
        }

        int renderArrowButtons(int x, int y, int mouseX, int mouseY) {
            CustomPartListScrollList.this.parentGUI.drawIcon(x, y, 0, 20 * (this.isHovered(mouseX, mouseY, x, y, x + 20, y + 20) ? 2 : 1));
            CustomPartListScrollList.this.parentGUI.drawIcon(x, y, 20, 20 * (this.isHovered(mouseX, mouseY, x += 20, y, x + 20, y + 20) ? 2 : 1));
            return x + 40;
        }

        void renderGirlSpecificSlider(int x, int y, int mouseX, int mouseY, int globalIndex) {
            CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(x, y, 140, 20, 79, 20);
            int sliderMinX = x += 4;
            int sliderMaxX = x + 67; //x + 71 - 4
            float progress = this.calculateSliderProgress(y, sliderMinX, sliderMaxX, mouseX, mouseY, globalIndex);
            int handleX = (int) Reference.LerpFloat((float)sliderMinX, (float)sliderMaxX, progress);
            CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(handleX, y, this.isHovered(mouseX, mouseY, handleX, y, handleX + 4, y + 20) ? 223 : 219, 20, 4, 20);
            CustomPartListScrollList.this.parentGUI.previewGirl.a(globalIndex, (int)(progress * 100.0f));
        }

        float calculateSliderProgress(int y, int minX, int maxX, int mouseX, int mouseY, int globalIndex) {
            if (!CustomPartListScrollList.this.parentGUI.isClickHeld) {
                return this.getSliderProgressValue(globalIndex);
            }
            if ((float)mouseX > 0.33333334f * (float)CustomPartListScrollList.this.parentGUI.width) {
                return this.getSliderProgressValue(globalIndex);
            }
            if (mouseY < y || mouseY > y + 20) {
                return this.getSliderProgressValue(globalIndex);
            }
            if (mouseX < minX) {
                return 0.0f;
            }
            if (mouseX > maxX) {
                return 1.0f;
            }
            return (float)(mouseX -= minX) / (float)(maxX -= minX);
        }

        float getSliderProgressValue(int globalIndex) {
            Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry =
                    CustomPartListScrollList.this.parentGUI.previewGirl.d(CustomPartListScrollList.this.parentGUI.ID).get(globalIndex);
            return (float) entry.getValue().getValue() / 100.0f;
        }

        void renderGirlSpecificSlot(int y, int mouseX, int mouseY, int globalIndex) {
            boolean isSliderActive = CustomPartListScrollList.this.parentGUI.previewGirl.h(globalIndex);
            CustomPartListScrollList.this.mc.renderEngine.bindTexture(ClothingGui.CLOTHING_ICONS_TEX);

            if (isSliderActive) {
                CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(xOffset, y, 0, 60, 119, 30);
            } else {
                CustomPartListScrollList.this.parentGUI.drawTexturedModalRect(xOffset, y, 0, 90, 95, 30);
            }

            int currentX = xOffset + 10;
            CustomPartListScrollList.this.parentGUI.drawIcon(currentX, y += 5, CustomPartListScrollList.this.parentGUI.previewGirl.g(globalIndex));
            currentX += 25;

            if (isSliderActive) {
                this.renderGirlSpecificSlider(currentX, y, mouseX, mouseY, globalIndex);
            } else {
                this.renderArrowButtons(currentX, y, mouseX, mouseY);
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            if (this.isBoneControlButton) {
                this.renderBoneControls(y, mouseX, mouseY);
            } else if (this.category == CustomPartCategory.GIRL_SPECIFIC) {
                this.renderGirlSpecificSlot(y, mouseX, mouseY, slotIndex);
            } else {
                this.renderModelSlot(y, mouseX, mouseY);
            }
        }

        void drawString(String text, int x, int y) {
            this.fontRenderer.drawString(text, x, y, TEXT_COLOR);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        void handleBoneControlClick(int relativeX) {
            int buttonX = 30;
            if (relativeX > buttonX && relativeX < buttonX + 20) {
                CustomPartListScrollList.this.needsScrollToBottom = true;
                CustomPartListScrollList.this.mc.getSoundHandler().playSound(
                        PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add("cross");
                arrayList.addAll(CustomModel.a(CustomPartListScrollList.this.parentGUI.previewGirl).get((Object) CustomPartCategory.CUSTOM_BONE));
                ClothingGui.activeCategories.add(ClothingGui.createCustomBoneEntry(CustomPartListScrollList.this.parentGUI.previewGirl));
            }
            if (!this.canRemoveBone) {
                return;
            }
            if (relativeX > (buttonX += 40) && relativeX < buttonX + 20) {
                CustomPartListScrollList.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                ClothingGui.activeCategories.remove(ClothingGui.activeCategories.size() - 1);
            }
        }

        void handleCategoryCycleClick(int relativeX, int globalIndex) {
            if (relativeX > 40 && relativeX < 60) {
                CustomPartListScrollList.this.parentGUI.cycleCategoryOption(this.category, false, globalIndex);
            }
            if (relativeX > 60 && relativeX < 80) {
                CustomPartListScrollList.this.parentGUI.cycleCategoryOption(this.category, true, globalIndex);
            }
        }

        void handleGirlSpecificClick(int relativeX, int relativeY) {
            if (!CustomPartListScrollList.this.parentGUI.previewGirl.h(relativeY)) {
                this.handleCategoryCycleClick(relativeX, relativeY);
            }
        }

        public void handleEntryClick(int mouseX, int relativeY, int mouseButton, int globalIndex) {
            if (mouseButton != 0) {
                return;
            }
            if (relativeY < 5) {
                return;
            }
            if (relativeY > 25) {
                return;
            }
            if (this.isBoneControlButton) {
                this.handleBoneControlClick(mouseX);
            } else if (this.category == CustomPartCategory.GIRL_SPECIFIC) {
                this.handleGirlSpecificClick(mouseX, globalIndex);
            } else {
                this.handleCategoryCycleClick(mouseX, globalIndex);
            }
        }

        @Override
        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }
    }
}

