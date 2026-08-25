/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui.Menu;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.UploadInventoryToServer;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GirlInventoryUI extends GuiContainer {
    final static ResourceLocation ITEMS_BACKGROUND = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    UUID containerUUID;
    GirlEntity girl;
    UUID PlayerUUID;

    public GirlInventoryUI(GirlEntity girl, InventoryPlayer inventoryPlayer, UUID containerID) {
        super(new GirlContainer(girl, inventoryPlayer, containerID));
        this.containerUUID = containerID;
        this.girl = girl;
        this.PlayerUUID = inventoryPlayer.player.getPersistentID();
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
        for (GirlContainer container : GirlContainer.OPEN_CONTAINERS) {
            if (!container.containerID.equals(this.containerUUID)) continue;
            ItemStack[] stacks = new ItemStack[42];
            Minecraft.getMinecraft().player.inventory.mainInventory.toArray(stacks);
            stacks[36] = container.getSlot(0).getStack();
            stacks[37] = container.getSlot(1).getStack();
            stacks[38] = container.getSlot(2).getStack();
            stacks[39] = container.getSlot(3).getStack();
            stacks[40] = container.getSlot(4).getStack();
            stacks[41] = container.getSlot(5).getStack();
            PacketHandler.INSTANCE.sendToServer(new UploadInventoryToServer(this.girl.girlID(), this.PlayerUUID, stacks));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(ITEMS_BACKGROUND);
        this.drawTexturedModalRect(this.width / 2 - 88, this.height / 2 - 7 - 24, 33, 16, 176, 114);
    }
}

