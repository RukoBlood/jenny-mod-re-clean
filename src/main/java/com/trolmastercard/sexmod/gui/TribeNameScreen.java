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
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class TribeNameScreen extends GuiScreen {
    final static int MAX_LETTERS = 15;
    final static int TEXT_WIDTH = 100;
    final static int TEXT_HEIGHT = 20;
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
        this.nameField = new GuiTextField(0, this.mc.fontRenderer, this.width / 2 - 50, this.height / 2 - 10, TEXT_WIDTH, TEXT_HEIGHT);
        this.nameField.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 25, this.height / 2 + 20, 50, TEXT_HEIGHT, "set"));
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        String string = this.nameField.getText();
        if (string.length() > MAX_LETTERS) {
            this.nameField.setText(string.substring(0, MAX_LETTERS));
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        String trimmed = this.nameField.getText().trim();
        if (!trimmed.isEmpty()) {
            PacketHandler.INSTANCE.sendToServer(new ClaimTribe(this.koboldID, Minecraft.getMinecraft().player.getPersistentID(), trimmed));
            Minecraft.getMinecraft().player.closeScreen();
        }
    }

}

