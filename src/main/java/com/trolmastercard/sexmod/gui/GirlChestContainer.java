/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.UploadInventoryToServer;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.gui.Menu.GirlContainer;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GirlChestContainer extends GuiContainer {
    private static final ResourceLocation CHEST_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    final private IInventory playerInventory;
    final private int rowCount;
    UUID containerID;
    GirlEntity girl;
    UUID girlId;

    public GirlChestContainer(EntityPlayer player, GirlEntity girl, UUID uUID) {
        super(new GirlInventory(player.inventory, (IInventory) girl, player, uUID));
        this.containerID = uUID;
        this.girl = girl;
        this.girlId = player.getPersistentID();
        this.playerInventory = player.inventory;
        IInventory chestInventory = (IInventory) girl;
        this.allowUserInput = false;
        this.rowCount = ((IInventory) girl).getSizeInventory() / 9;
        this.ySize = 114 + this.rowCount * 18;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.girl.getGirlName(), 8, 6, 0x404040);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GirlChestContainer.CHEST_TEXTURE);
        int left = (this.width - this.xSize) / 2;
        int top = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.rowCount * 18 + 17);
        this.drawTexturedModalRect(left, top + this.rowCount * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (GirlContainer container : GirlContainer.OPEN_CONTAINERS) {
            if (container.containerID.equals(this.containerID)) {
                ItemStack[] stacks = new ItemStack[63];
                Minecraft.getMinecraft().player.inventory.mainInventory.toArray(stacks);
                for (int i = 0; i < 27; ++i) {
                    stacks[i + 36] = container.getSlot(i).getStack();
                }
                PacketHandler.INSTANCE.sendToServer(new UploadInventoryToServer(this.girl.girlID(), this.girlId, stacks));
            }
        }
    }
}

