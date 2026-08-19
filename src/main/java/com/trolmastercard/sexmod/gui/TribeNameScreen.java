/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui;

import java.io.IOException;
import java.util.UUID;

import com.trolmastercard.sexmod.Packets.ClaimTribe;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TribeNameScreen extends GuiScreen {
    final static int b = 15;
    final static int w = 100;
    final static int h = 20;
    UUID koboldID;
    GuiTextField nameField;

    public TribeNameScreen(UUID uUID) {
        this.koboldID = uUID;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.nameField = new GuiTextField(0, this.mc.fontRenderer, this.width / 2 - 50, this.height / 2 - 10, w, h);
        this.nameField.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 25, this.height / 2 + 20, 50, h, "set"));
    }

    @Override
    public void updateScreen() {
        this.nameField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawHoveringText("Name Tribe", this.width / 2 - 39, this.height / 2 - 10);
        this.nameField.drawTextBox();
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.nameField.textboxKeyTyped(c, n);
        String string = this.nameField.getText();
        if (string.length() > 15) {
            this.nameField.setText(string.substring(0, 15));
        }
        super.keyTyped(c, n);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        String string = this.nameField.getText().trim();
        if (string.isEmpty()) {
            return;
        }
        PackageHandler.INSTANCE.sendToServer((IMessage)new ClaimTribe(this.koboldID, Minecraft.getMinecraft().player.getPersistentID(), string));
        Minecraft.getMinecraft().player.closeScreen();
    }

}

