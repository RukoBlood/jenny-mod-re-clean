/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
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

public class GirlGUI
extends GuiScreen {
    final GirlEntity girlEntity;
    final EntityPlayer entityPlayer;
    final String[] h;
    @Nullable
    final ItemStack[] f;
    final static ResourceLocation girlInventoryGUITexture = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    EntityDataManager entityDataManager;
    final boolean k;
    float m = 0.0f;
    float n = 0.0f;
    String[] girlActions = new String[]{"action.names.followme", "action.names.stopfollowme", "action.names.gohome", "action.names.setnewhome", "action.names.equipment"};
    int[] d = new int[]{0, 0, 0, 0, 0};
    int[] j = new int[]{64, 80, 47, 32, 96};
    int[] b = new int[]{4, 4, 5, 5, 4};
    int[] e = new int[]{50, 90, 50, 80, 60};

    public GirlGUI(GirlEntity girlEntity, EntityPlayer entityPlayer) {
        this.girlEntity = girlEntity;
        this.entityPlayer = entityPlayer;
        this.h = new String[0];
        this.f = new ItemStack[0];
        this.k = true;
        this.entityDataManager = girlEntity.getDataManager();
    }

    public GirlGUI(GirlEntity girlEntity, EntityPlayer entityPlayer, String[] stringArray, @Nullable ItemStack[] itemStackArray, boolean bl) {
        this.girlEntity = girlEntity;
        this.entityPlayer = entityPlayer;
        this.h = stringArray;
        this.f = itemStackArray;
        this.k = bl;
        this.entityDataManager = girlEntity.getDataManager();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void onGuiClosed() {
        super.onGuiClosed();
        this.girlEntity.ac();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id < 5 || this.f == null || this.f[guiButton.id - 5] == null || this.entityPlayer.capabilities.isCreativeMode) {
            this.a(guiButton);
            return;
        }
        for (ItemStack itemStack : this.entityPlayer.inventory.mainInventory) {
            if (!itemStack.getItem().equals(this.f[guiButton.id - 5].getItem()) || itemStack.getCount() < this.f[guiButton.id - 5].getCount() || itemStack.getMetadata() != this.f[guiButton.id - 5].getMetadata()) continue;
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new t_class423(this.entityPlayer.getPersistentID(), this.f[guiButton.id - 5]));
            this.a(guiButton);
            return;
        }
        this.entityPlayer.sendMessage(new TextComponentString("<" + this.girlEntity.getName() + "> you cannot afford that..."));
        this.girlEntity.PlaySound(SoundEventHandler.GIRLS_JENNY_SADOH[1]);
    }

    void a(GuiButton guiButton) {
        String string = guiButton.id < 5 ? this.girlActions[guiButton.id] : this.h[guiButton.id - 5];
        this.girlEntity.a(string, this.entityPlayer.getPersistentID());
        Minecraft.getMinecraft().player.closeScreen();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        this.buttonList.clear();
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n3 = scaledResolution.getScaledWidth();
        int n4 = scaledResolution.getScaledHeight();
        this.m = Math.min(1.0f, this.m + this.mc.getTickLength() / 5.0f);
        if (this.m == 1.0f) {
            this.n = Math.min(1.0f, this.n + this.mc.getTickLength() / 5.0f);
        }
        int n5 = (int)b6_class67.a(115.0f, 161.0f, this.n);
        int n6 = (int)b6_class67.a(91.0f, 137.0f, this.n);
        int n7 = (int)b6_class67.a(-30.0f, 120.0f, this.m);
        int n8 = 70;
        int n9 = 52;
        int n10 = 68;
        for (int i = 5; i < this.h.length + 5; ++i) {
            if (this.n > 0.0f && this.f != null && this.f[i - 5] != null && this.f[i - 5].getCount() != 0) {
                this.zLevel = -300.0f;
                this.itemRender.zLevel = -300.0f;
                this.a(Arrays.asList(this.f[i - 5].getCount() + "x    "), n3 - n5, n4 - n9, this.fontRenderer);
                this.itemRender.renderItemIntoGUI(this.f[i - 5], n3 - n6, n4 - n10);
                this.zLevel = 0.0f;
                this.itemRender.zLevel = 0.0f;
            }
            this.buttonList.add(new GuiButton(i, n3 - n7, n4 - n8, 100, 20, I18n.format(this.h[i - 5], new Object[0])));
            n8 += 30;
            n9 += 30;
            n10 += 30;
        }
        if (this.k) {
            this.a(n, n2);
        }
    }

    void a(int n, int n2) {
        int n3 = (int)b6_class67.a(-30.0f, 120.0f, this.m);
        this.itemRender.renderItemIntoGUI(this.entityDataManager.get(Fighter.ITEM_SLOT_1), n3 - 105, 68);
        this.itemRender.renderItemIntoGUI(this.entityDataManager.get(Fighter.ITEM_SLOT_2), n3 - 105, 87);
        this.itemRender.renderItemIntoGUI(this.entityDataManager.get(Fighter.ITEM_SLOT_3), n3 - 105, 109);
        this.itemRender.renderItemIntoGUI(this.entityDataManager.get(Fighter.ITEM_SLOT_4), n3 - 105, 127);
        this.itemRender.renderItemIntoGUI(this.entityDataManager.get(Fighter.ITEM_SLOT_5), n3 - 105, 146);
        this.itemRender.renderItemIntoGUI(this.entityDataManager.get(Fighter.ITEM_SLOT_6), n3 - 105, 166);
        if (this.n == 0.0f) {
            return;
        }
        boolean bl = !this.entityDataManager.get(GirlEntity.v).equals("");
        int n4 = 35;
        int n5 = 70;
        for (int i = 0; i < 5; ++i) {
            if (i == 0 && bl) {
                i = 1;
            } else if (i == 1 && !bl) {
                i = 2;
            }
            this.d[i] = n >= n4 && n <= n4 + 23 + this.d[i] && n2 >= n5 && n2 <= n5 + 20 ? Math.min(this.e[i], this.d[i] + 7) : Math.max(0, this.d[i] - 7);
            StringBuilder stringBuilder = new StringBuilder(I18n.format(this.girlActions[i], new Object[0]));
            for (int j = 0; j < this.b[i]; ++j) {
                stringBuilder.append(" ");
            }
            this.mc.renderEngine.bindTexture(girlInventoryGUITexture);
            this.drawTexturedModalRect(this.d[i] + n4 - 18 + (int)b6_class67.a(0.0f, 23.0f, this.n), n5 + 2, this.j[i], 0, 16, 16);
            this.buttonList.add(new GuiButton(i, n4 + 1, n5, (int)(b6_class67.a(0.0f, 23.0f, this.n) + (float)this.d[i]), 20, this.d[i] <= 14 ? "" : stringBuilder.toString()));
            n5 += 30;
        }
        this.mc.renderEngine.bindTexture(girlInventoryGUITexture);
        this.drawTexturedModalRect(n3 - 113, 60, 0, 0, 32, 130);
    }

    void a(List<String> list, int n, int n2, FontRenderer fontRenderer) {
        int n3;
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        int n4 = 0;
        for (String string : list) {
            n3 = this.fontRenderer.getStringWidth(string);
            if (n3 <= n4) continue;
            n4 = n3;
        }
        int n5 = n + 12;
        int n6 = n2 - 12;
        n3 = 8;
        if (list.size() > 1) {
            n3 += 2 + (list.size() - 1) * 10;
        }
        if (n5 + n4 > this.width) {
            n5 -= 28 + n4;
        }
        if (n6 + n3 + 6 > this.height) {
            n6 = this.height - n3 - 6;
        }
        this.drawGradientRect(n5 - 3, n6 - 4, n5 + n4 + 3, n6 - 3, -267386864, -267386864);
        this.drawGradientRect(n5 - 3, n6 + n3 + 3, n5 + n4 + 3, n6 + n3 + 4, -267386864, -267386864);
        this.drawGradientRect(n5 - 3, n6 - 3, n5 + n4 + 3, n6 + n3 + 3, -267386864, -267386864);
        this.drawGradientRect(n5 - 4, n6 - 3, n5 - 3, n6 + n3 + 3, -267386864, -267386864);
        this.drawGradientRect(n5 + n4 + 3, n6 - 3, n5 + n4 + 4, n6 + n3 + 3, -267386864, -267386864);
        this.drawGradientRect(n5 - 3, n6 - 3 + 1, n5 - 3 + 1, n6 + n3 + 3 - 1, 0x505000FF, 1344798847);
        this.drawGradientRect(n5 + n4 + 2, n6 - 3 + 1, n5 + n4 + 3, n6 + n3 + 3 - 1, 0x505000FF, 1344798847);
        this.drawGradientRect(n5 - 3, n6 - 3, n5 + n4 + 3, n6 - 3 + 1, 0x505000FF, 0x505000FF);
        this.drawGradientRect(n5 - 3, n6 + n3 + 2, n5 + n4 + 3, n6 + n3 + 3, 1344798847, 1344798847);
        for (int i = 0; i < list.size(); ++i) {
            String string = list.get(i);
            this.fontRenderer.drawStringWithShadow(string, n5, n6, -1);
            if (i == 0) {
                n6 += 2;
            }
            n6 += 10;
        }
        GlStateManager.enableLighting();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

