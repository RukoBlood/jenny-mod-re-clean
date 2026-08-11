/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui;

import java.util.UUID;

import com.trolmastercard.sexmod.Packages.UploadInventoryToServer;
import com.trolmastercard.sexmod.LunaContainer;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class LunaGUIContainer extends GuiContainer {
    final static ResourceLocation b = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    UUID ID;
    LunaEntity lunaEntity;
    UUID invID;

    public LunaGUIContainer(LunaEntity luna, InventoryPlayer invPlayer, UUID ID) {
        super(new LunaContainer(luna, invPlayer, ID));
        this.ID = ID;
        this.lunaEntity = luna;
        this.invID = invPlayer.player.getPersistentID();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (LunaContainer ca_class1212 : LunaContainer.OPEN_CONTAINERS) {
            if (!ca_class1212.containerID.equals(this.ID)) continue;
            ItemStack[] itemStackArray = new ItemStack[43];
            Minecraft.getMinecraft().player.inventory.mainInventory.toArray(itemStackArray);
            itemStackArray[36] = ca_class1212.getSlot(0).getStack();
            itemStackArray[37] = ca_class1212.getSlot(1).getStack();
            itemStackArray[38] = ca_class1212.getSlot(2).getStack();
            itemStackArray[39] = ca_class1212.getSlot(3).getStack();
            itemStackArray[40] = ca_class1212.getSlot(4).getStack();
            itemStackArray[41] = ca_class1212.getSlot(5).getStack();
            itemStackArray[42] = ca_class1212.getSlot(6).getStack();
            PackageHandler.INSTANCE.sendToServer((IMessage)new UploadInventoryToServer(this.lunaEntity.girlID(), this.invID, itemStackArray));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(b);
        this.drawTexturedModalRect(this.width / 2 - 88, this.height / 2 - 7 - 24, 80, 142, 176, 114);
    }
}

