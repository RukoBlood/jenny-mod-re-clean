/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui.Menu;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.Packets.RemoveItems;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FighterUI extends GuiScreen {
    final GirlEntity girl;
    final EntityPlayer player;
    final String[] actions;
    @Nullable
    final ItemStack[] prices;
    final static ResourceLocation girlInventoryGUITexture = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    EntityDataManager dataManager;
    final boolean DRAW_EQUIPMENT;
    float firstTransition = 0.0f;
    float secondTransition = 0.0f;
    String[] companionButtonTexts = new String[]{"action.names.followme", "action.names.stopfollowme", "action.names.gohome", "action.names.setnewhome", "action.names.equipment"};
    int[] extraButtonWidth = new int[]{0, 0, 0, 0, 0};
    int[] textureXOffset = new int[]{64, 80, 47, 32, 96};
    int[] spaces = new int[]{4, 4, 5, 5, 4};
    int[] sizes = new int[]{50, 90, 50, 80, 60};

    public FighterUI(GirlEntity girl, EntityPlayer player) {
        this.girl = girl;
        this.player = player;
        this.actions = new String[0];
        this.prices = new ItemStack[0];
        this.DRAW_EQUIPMENT = true;
        this.dataManager = girl.getDataManager();
    }

    public FighterUI(GirlEntity girl, EntityPlayer player, String[] actions, @Nullable ItemStack[] prices, boolean drawEquipment) {
        this.girl = girl;
        this.player = player;
        this.actions = actions;
        this.prices = prices;
        this.DRAW_EQUIPMENT = drawEquipment;
        this.dataManager = girl.getDataManager();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void onGuiClosed() {
        super.onGuiClosed();
        this.girl.ac();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id < 5 || this.prices == null || this.prices[button.id - 5] == null || this.player.capabilities.isCreativeMode) {
            this.doAction(button);
            return;
        }
        for (ItemStack itemStack : this.player.inventory.mainInventory) {
            if (!itemStack.getItem().equals(this.prices[button.id - 5].getItem()) || itemStack.getCount() < this.prices[button.id - 5].getCount() || itemStack.getMetadata() != this.prices[button.id - 5].getMetadata()) continue;
            PackageHandler.INSTANCE.sendToServer((IMessage)new RemoveItems(this.player.getPersistentID(), this.prices[button.id - 5]));
            this.doAction(button);
            return;
        }
        this.player.sendMessage(new TextComponentString("<" + this.girl.getName() + "> you cannot afford that..."));
        this.girl.PlaySound(SoundsHandler.GIRLS_JENNY_SADOH[1]);
    }

    void doAction(GuiButton button) {
        String text = button.id < 5 ? this.companionButtonTexts[button.id] : this.actions[button.id - 5];
        this.girl.doAction(text, this.player.getPersistentID());
        Minecraft.getMinecraft().player.closeScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.buttonList.clear();
        ScaledResolution resolution = new ScaledResolution(this.mc);
        int screenWidth = resolution.getScaledWidth();
        int screenHeight = resolution.getScaledHeight();
        this.firstTransition = Math.min(1.0f, this.firstTransition + this.mc.getTickLength() / 5.0f);
        if (this.firstTransition == 1.0f) {
            this.secondTransition = Math.min(1.0f, this.secondTransition + this.mc.getTickLength() / 5.0f);
        }
        int xText = (int) RotationHelper.LerpFloat(115.0f, 161.0f, this.secondTransition);
        int xItem = (int) RotationHelper.LerpFloat(91.0f, 137.0f, this.secondTransition);
        int x = (int) RotationHelper.LerpFloat(-30.0f, 120.0f, this.firstTransition);
        int y = 70;
        int yText = 52;
        int yItem = 68;

        for (int i = 5; i < this.actions.length + 5; ++i) {
            if (this.secondTransition > 0.0f && this.prices != null && this.prices[i - 5] != null && this.prices[i - 5].getCount() != 0) {
                this.zLevel = -300.0f;
                this.itemRender.zLevel = -300.0f;
                this.drawHoveringTextWithZ(Arrays.asList(this.prices[i - 5].getCount() + "x    "), screenWidth - xText, screenHeight - yText, this.fontRenderer);
                this.itemRender.renderItemIntoGUI(this.prices[i - 5], screenWidth - xItem, screenHeight - yItem);
                this.zLevel = 0.0f;
                this.itemRender.zLevel = 0.0f;
            }
            this.buttonList.add(new GuiButton(i, screenWidth - x, screenHeight - y, 100, 20, I18n.format(this.actions[i - 5], new Object[0])));
            y += 30;
            yText += 30;
            yItem += 30;
        }

        if (this.DRAW_EQUIPMENT) {
            this.drawEquipment(mouseX, mouseY);
        }
    }

    void drawEquipment(int mouseX, int mouseY) {
        int x = (int) RotationHelper.LerpFloat(-30.0f, 120.0f, this.firstTransition);
        this.itemRender.renderItemIntoGUI(this.dataManager.get(Fighter.WEAPON), x - 105, 68);
        this.itemRender.renderItemIntoGUI(this.dataManager.get(Fighter.BOW), x - 105, 87);
        this.itemRender.renderItemIntoGUI(this.dataManager.get(Fighter.HELMET_SLOT), x - 105, 109);
        this.itemRender.renderItemIntoGUI(this.dataManager.get(Fighter.CHEST_SLOT), x - 105, 127);
        this.itemRender.renderItemIntoGUI(this.dataManager.get(Fighter.LEGS_SLOT), x - 105, 146);
        this.itemRender.renderItemIntoGUI(this.dataManager.get(Fighter.BOOTS_SLOT), x - 105, 166);
        if (this.secondTransition == 0.0f) {
            return;
        }
        boolean hasMaster = !this.dataManager.get(GirlEntity.MASTER).isEmpty();
        int buttonX = 35;
        int buttonY = 70;
        for (int i = 0; i < 5; ++i) {
            if (i == 0 && hasMaster) {
                i = 1;
            } else if (i == 1 && !hasMaster) {
                i = 2;
            }
            this.extraButtonWidth[i] = mouseX >= buttonX && mouseX <= buttonX + 23 + this.extraButtonWidth[i] && mouseY >= buttonY && mouseY <= buttonY + 20 ? Math.min(this.sizes[i], this.extraButtonWidth[i] + 7) : Math.max(0, this.extraButtonWidth[i] - 7);
            StringBuilder stringBuilder = new StringBuilder(I18n.format(this.companionButtonTexts[i], new Object[0]));
            for (int j = 0; j < this.spaces[i]; ++j) {
                stringBuilder.append(" ");
            }
            this.mc.renderEngine.bindTexture(girlInventoryGUITexture);
            this.drawTexturedModalRect(this.extraButtonWidth[i] + buttonX - 18 + (int) RotationHelper.LerpFloat(0.0f, 23.0f, this.secondTransition), buttonY + 2, this.textureXOffset[i], 0, 16, 16);
            this.buttonList.add(new GuiButton(i, buttonX + 1, buttonY, (int)(RotationHelper.LerpFloat(0.0f, 23.0f, this.secondTransition) + (float)this.extraButtonWidth[i]), 20, this.extraButtonWidth[i] <= 14 ? "" : stringBuilder.toString()));
            buttonY += 30;
        }
        this.mc.renderEngine.bindTexture(girlInventoryGUITexture);
        this.drawTexturedModalRect(x - 113, 60, 0, 0, 32, 130);
    }

    void drawHoveringTextWithZ(List<String> textLines, int x, int y, FontRenderer font) {
        int n3;
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        int n4 = 0;

        for (String string : textLines) {
            n3 = this.fontRenderer.getStringWidth(string);
            if (n3 <= n4) continue;
            n4 = n3;
        }
        int l1 = x + 12;
        int i2 = y - 12;
        n3 = 8;
        if (textLines.size() > 1) {
            n3 += 2 + (textLines.size() - 1) * 10;
        }
        if (l1 + n4 > this.width) {
            l1 -= 28 + n4;
        }
        if (i2 + n3 + 6 > this.height) {
            i2 = this.height - n3 - 6;
        }
        this.drawGradientRect(l1 - 3, i2 - 4, l1 + n4 + 3, i2 - 3, 0xf0100010, 0xf0100010);
        this.drawGradientRect(l1 - 3, i2 + n3 + 3, l1 + n4 + 3, i2 + n3 + 4, 0xf0100010, 0xf0100010);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + n4 + 3, i2 + n3 + 3, 0xf0100010, 0xf0100010);
        this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + n3 + 3, 0xf0100010, 0xf0100010);
        this.drawGradientRect(l1 + n4 + 3, i2 - 3, l1 + n4 + 4, i2 + n3 + 3, 0xf0100010, 0xf0100010);
        this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + n3 + 3 - 1, 0x505000FF, 0x5028007f);
        this.drawGradientRect(l1 + n4 + 2, i2 - 3 + 1, l1 + n4 + 3, i2 + n3 + 3 - 1, 0x505000FF, 0x5028007f);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + n4 + 3, i2 - 3 + 1, 0x505000FF, 0x505000FF);
        this.drawGradientRect(l1 - 3, i2 + n3 + 2, l1 + n4 + 3, i2 + n3 + 3, 0x5028007f, 0x5028007f);

        for (int i = 0; i < textLines.size(); ++i) {
            String sl = textLines.get(i);
            this.fontRenderer.drawStringWithShadow(sl, l1, i2, -1);
            if (i == 0) {
                i2 += 2;
            }
            i2 += 10;
        }
        GlStateManager.enableLighting();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}

