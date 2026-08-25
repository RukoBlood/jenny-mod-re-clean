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

public class GirlGUIContainer extends GuiContainer {
    final static private ResourceLocation f = new ResourceLocation("textures/gui/container/generic_54.png");
    final private IInventory e;
    final private IInventory d;
    final private int g;
    UUID c;
    GirlEntity b;
    UUID a;

    public GirlGUIContainer(EntityPlayer player, GirlEntity girl, UUID uUID) {
        super(new GirlInventory(player.inventory, (IInventory) girl, player, uUID));
        this.c = uUID;
        this.b = girl;
        this.a = player.getPersistentID();
        this.e = player.inventory;
        this.d = (IInventory) girl;
        this.allowUserInput = false;
        this.g = ((IInventory) girl).getSizeInventory() / 9;
        this.ySize = 114 + this.g * 18;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.b.getGirlName(), 8, 6, 0x404040);
        this.fontRenderer.drawString(this.e.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GirlGUIContainer.f);
        int n3 = (this.width - this.xSize) / 2;
        int n4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.g * 18 + 17);
        this.drawTexturedModalRect(n3, n4 + this.g * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (GirlContainer container : GirlContainer.OPEN_CONTAINERS) {
            if (container.containerID.equals(this.c)) {
                ItemStack[] itemStackArray = new ItemStack[63];
                Minecraft.getMinecraft().player.inventory.mainInventory.toArray(itemStackArray);
                for (int i = 0; i < 27; ++i) {
                    itemStackArray[i + 36] = container.getSlot(i).getStack();
                }
                PacketHandler.INSTANCE.sendToServer(new UploadInventoryToServer(this.b.girlID(), this.a, itemStackArray));
            }
        }
    }
}

