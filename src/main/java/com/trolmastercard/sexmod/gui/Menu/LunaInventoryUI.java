/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui.Menu;

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

public class LunaInventoryUI extends GuiContainer {
    final static ResourceLocation ITEMS_BACKGROUND = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    UUID ID;
    LunaEntity lunaEntity;
    UUID invID;

    public LunaInventoryUI(LunaEntity luna, InventoryPlayer invPlayer, UUID ID) {
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
        for (LunaContainer container : LunaContainer.OPEN_CONTAINERS) {
            if (!container.containerID.equals(this.ID)) continue;
            ItemStack[] stacks = new ItemStack[43];
            Minecraft.getMinecraft().player.inventory.mainInventory.toArray(stacks);
            stacks[36] = container.getSlot(0).getStack();
            stacks[37] = container.getSlot(1).getStack();
            stacks[38] = container.getSlot(2).getStack();
            stacks[39] = container.getSlot(3).getStack();
            stacks[40] = container.getSlot(4).getStack();
            stacks[41] = container.getSlot(5).getStack();
            stacks[42] = container.getSlot(6).getStack();
            PackageHandler.INSTANCE.sendToServer((IMessage)new UploadInventoryToServer(this.lunaEntity.girlID(), this.invID, stacks));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(ITEMS_BACKGROUND);
        this.drawTexturedModalRect(this.width / 2 - 88, this.height / 2 - 7 - 24, 80, 142, 176, 114);
    }
}

