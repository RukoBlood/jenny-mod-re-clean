/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class fb_class301
extends GuiContainer {
    final static ResourceLocation c = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    UUID a;
    GirlEntity b;
    UUID d;

    public fb_class301(GirlEntity em_class2582, InventoryPlayer inventoryPlayer, UUID uUID) {
        super(new d4_class162(em_class2582, inventoryPlayer, uUID));
        this.a = uUID;
        this.b = em_class2582;
        this.d = inventoryPlayer.player.getPersistentID();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        super.drawScreen(n, n2, f);
        this.renderHoveredToolTip(n, n2);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (d4_class162 d4_class1622 : d4_class162.c) {
            if (!d4_class1622.a.equals(this.a)) continue;
            ItemStack[] itemStackArray = new ItemStack[42];
            Minecraft.getMinecraft().player.inventory.mainInventory.toArray(itemStackArray);
            itemStackArray[36] = d4_class1622.getSlot(0).getStack();
            itemStackArray[37] = d4_class1622.getSlot(1).getStack();
            itemStackArray[38] = d4_class1622.getSlot(2).getStack();
            itemStackArray[39] = d4_class1622.getSlot(3).getStack();
            itemStackArray[40] = d4_class1622.getSlot(4).getStack();
            itemStackArray[41] = d4_class1622.getSlot(5).getStack();
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new b1_class60(this.b.girlID(), this.d, itemStackArray));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(c);
        this.drawTexturedModalRect(this.width / 2 - 88, this.height / 2 - 7 - 24, 33, 16, 176, 114);
    }
}

