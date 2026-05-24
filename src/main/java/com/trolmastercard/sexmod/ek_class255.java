/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.UUID;

import com.trolmastercard.sexmod.Packages.UploadInventoryToServer;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ek_class255
extends GuiContainer {
    final static private ResourceLocation f = new ResourceLocation("textures/gui/container/generic_54.png");
    final private IInventory e;
    final private IInventory d;
    final private int g;
    UUID c;
    GirlEntity b;
    UUID a;

    public ek_class255(EntityPlayer entityPlayer, GirlEntity em_class2582, UUID uUID) {
        super(new bx_class105(entityPlayer.inventory, (IInventory)((Object)em_class2582), entityPlayer, uUID));
        this.c = uUID;
        this.b = em_class2582;
        this.a = entityPlayer.getPersistentID();
        this.e = entityPlayer.inventory;
        this.d = (IInventory)((Object)em_class2582);
        this.allowUserInput = false;
        this.g = ((IInventory)((Object)em_class2582)).getSizeInventory() / 9;
        this.ySize = 114 + this.g * 18;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        super.drawScreen(n, n2, f);
        this.renderHoveredToolTip(n, n2);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        this.fontRenderer.drawString(this.b.getGirlName(), 8, 6, 0x404040);
        this.fontRenderer.drawString(this.e.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ek_class255.f);
        int n3 = (this.width - this.xSize) / 2;
        int n4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.g * 18 + 17);
        this.drawTexturedModalRect(n3, n4 + this.g * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (d4_class162 d4_class1622 : d4_class162.c) {
            if (!d4_class1622.a.equals(this.c)) continue;
            ItemStack[] itemStackArray = new ItemStack[63];
            Minecraft.getMinecraft().player.inventory.mainInventory.toArray(itemStackArray);
            for (int i = 0; i < 27; ++i) {
                itemStackArray[i + 36] = d4_class1622.getSlot(i).getStack();
            }
            PackageHandler.networkWrapper.sendToServer((IMessage)new UploadInventoryToServer(this.b.girlID(), this.a, itemStackArray));
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

