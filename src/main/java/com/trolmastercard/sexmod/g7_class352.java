/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class g7_class352
extends GuiScreen {
    final static int b = 15;
    final static int a = 100;
    final static int c = 20;
    UUID e;
    GuiTextField d;

    public g7_class352(UUID uUID) {
        this.e = uUID;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.d = new GuiTextField(0, this.mc.fontRenderer, this.width / 2 - 50, this.height / 2 - 10, 100, 20);
        this.d.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 25, this.height / 2 + 20, 50, 20, "set"));
    }

    @Override
    public void updateScreen() {
        this.d.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawHoveringText("Name Tribe", this.width / 2 - 39, this.height / 2 - 10);
        this.d.drawTextBox();
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.d.textboxKeyTyped(c, n);
        String string = this.d.getText();
        if (string.length() > 15) {
            this.d.setText(string.substring(0, 15));
        }
        super.keyTyped(c, n);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        super.actionPerformed(guiButton);
        String string = this.d.getText().trim();
        if (string.length() == 0) {
            return;
        }
        NetworkRegistry.networkWrapper.sendToServer((IMessage)new g9_class354(this.e, Minecraft.getMinecraft().player.getPersistentID(), string));
        Minecraft.getMinecraft().player.closeScreen();
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

