/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui;

import java.io.IOException;
import java.util.UUID;

import com.trolmastercard.sexmod.Packages.ClaimTribe;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class g7_class352 extends GuiScreen {
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
        this.d = new GuiTextField(0, this.mc.fontRenderer, this.width / 2 - 50, this.height / 2 - 10, a, c);
        this.d.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 25, this.height / 2 + 20, 50, c, "set"));
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
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        String string = this.d.getText().trim();
        if (string.isEmpty()) {
            return;
        }
        PackageHandler.INSTANCE.sendToServer((IMessage)new ClaimTribe(this.e, Minecraft.getMinecraft().player.getPersistentID(), string));
        Minecraft.getMinecraft().player.closeScreen();
    }

}

