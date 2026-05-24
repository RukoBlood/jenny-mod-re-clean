/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.UUID;

import com.trolmastercard.sexmod.Packages.UploadInventoryToServer;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class az_class56
extends GuiContainer {
    final static ResourceLocation b = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    UUID c;
    LunaEntity d;
    UUID a;

    public az_class56(LunaEntity eb_class2362, InventoryPlayer inventoryPlayer, UUID uUID) {
        super(new ca_class121(eb_class2362, inventoryPlayer, uUID));
        this.c = uUID;
        this.d = eb_class2362;
        this.a = inventoryPlayer.player.getPersistentID();
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
        for (ca_class121 ca_class1212 : ca_class121.c) {
            if (!ca_class1212.a.equals(this.c)) continue;
            ItemStack[] itemStackArray = new ItemStack[43];
            Minecraft.getMinecraft().player.inventory.mainInventory.toArray(itemStackArray);
            itemStackArray[36] = ca_class1212.getSlot(0).getStack();
            itemStackArray[37] = ca_class1212.getSlot(1).getStack();
            itemStackArray[38] = ca_class1212.getSlot(2).getStack();
            itemStackArray[39] = ca_class1212.getSlot(3).getStack();
            itemStackArray[40] = ca_class1212.getSlot(4).getStack();
            itemStackArray[41] = ca_class1212.getSlot(5).getStack();
            itemStackArray[42] = ca_class1212.getSlot(6).getStack();
            PackageHandler.networkWrapper.sendToServer((IMessage)new UploadInventoryToServer(this.d.girlID(), this.a, itemStackArray));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(b);
        this.drawTexturedModalRect(this.width / 2 - 88, this.height / 2 - 7 - 24, 80, 142, 176, 114);
    }
}

