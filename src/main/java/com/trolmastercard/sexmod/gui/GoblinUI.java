/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.gui;

import java.io.IOException;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

//ea.class
//goblin related UI
public class GoblinUI extends GuiScreen {
    final static float j = 100.0f;
    final static float c = 15.0f;
    final static float k = 5.0f;
    final static float l = 0.5f;
    final static float b = 0.5f;
    final static ResourceLocation GUI_TEXTURE = new ResourceLocation("sexmod", "textures/gui/command.png");
    float animProgress = 0.0f;
    float animLeft = 0.0f;
    float animRight = 0.0f;
    float animTop = 0.0f;
    float animBottom = 0.0f;
    GirlEntity girl;
    boolean isGoblin = false;

    public GoblinUI(GirlEntity girl) {
        this.girl = girl;
        this.isGoblin = girl instanceof GoblinEntity;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.animTop != 0.0f || this.animBottom != 0.0f || this.animLeft != 0.0f) {
            if (this.animLeft > 0.0f) {
                this.startGoblinThrow();
            } else {
                if (this.isGoblin) {
                    if (this.animTop > this.animBottom) {
                        this.throwGoblin();
                    } else {
                        this.pickupGoblin();
                    }
                }
            }
        }
    }

    void throwGoblin() {
        if (this.isGoblin) {
            ((GoblinEntity)this.girl).setThrowTarget(Minecraft.getMinecraft().player.getPersistentID());
        }
    }

    void pickupGoblin() {
        ((GoblinEntity)this.girl).setPickupTarget(Minecraft.getMinecraft().player.getPersistentID());
    }

    void startGoblinThrow() {
        if (this.girl.getInteractionPlayerUUID() == null) {
            this.girl.setCurrentAction(Action.START_THROWING);
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (ClientProxy.keyBindings[0].getKeyCode() == Keyboard.getEventKey() && !Keyboard.getEventKeyState()) {
            Minecraft.getMinecraft().player.closeScreen();
        } else {
            super.handleKeyboardInput();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);

        this.animProgress = Math.min(1.0f, this.animProgress + this.mc.getTickLength() / 5.0f);

        float scale = (float) this.easeOutBack(this.animProgress);
        float offset = (1.0f - scale) * 100.0f;
        this.animLeft += (float) (mouseX < this.width / 2 ? 1 : -1) * this.mc.getTickLength();
        this.animRight += (float) (mouseX > this.width / 2 ? 1 : -1) * this.mc.getTickLength();
        this.animTop += (float) (mouseY < this.height / 2 - 1 ? 1 : -1) * this.mc.getTickLength();
        this.animBottom += (float) (mouseY > this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.animLeft = ThreadNames.clamp(this.animLeft, 0.0f, 1.0f);
        this.animRight = ThreadNames.clamp(this.animRight, 0.0f, 1.0f);
        this.animTop = ThreadNames.clamp(this.animTop, 0.0f, 1.0f);
        this.animBottom = ThreadNames.clamp(this.animBottom, 0.0f, 1.0f);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) this.width / 2.0f, (float) this.height / 2.0f, 0.0f);
        GlStateManager.scale(scale, scale, scale);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f + this.animLeft * 0.5f, 1.0f + this.animLeft * 0.5f, 1.0f);
        this.drawTexturedModalRect(-62.0f + offset - this.animLeft * 15.0f, offset - 32.0f, 0, 0, 64, 64);
        this.drawTexturedModalRect(-62.0f + offset - this.animLeft * 15.0f, offset - 32.0f, 64, 128, 64, 64);
        GlStateManager.popMatrix();
        if (this.isGoblin) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f - this.animRight, 1.0f - this.animRight, 1.0f);
            this.drawTexturedModalRect(-2.0f - offset + this.animRight * 32.0f, -offset - 32.0f, 0, 0, 64, 64);
            this.drawTexturedModalRect(-2.0f - offset + this.animRight * 32.0f, -offset - 32.0f, 0, 128, 64, 64);
            GlStateManager.popMatrix();
            if (this.animRight > 0.0f) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(-1.0f + this.animRight + 1.0f + this.animTop * 0.5f, -1.0f + this.animRight + 1.0f + this.animTop * 0.5f, 1.0f);
                this.drawTexturedModalRect(-2.0f - offset + this.animTop * 5.0f, -offset - 64.0f - this.animTop * 5.0f / 2.0f, 0, 0, 64, 64);
                this.drawTexturedModalRect(-2.0f - offset + this.animTop * 5.0f, -offset - 64.0f - this.animTop * 5.0f / 2.0f, 128, 128, 64, 64);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(-1.0f + this.animRight + 1.0f + this.animBottom * 0.5f, -1.0f + this.animRight + 1.0f + this.animBottom * 0.5f, 1.0f);
                this.drawTexturedModalRect(-2.0f - offset + this.animBottom * 5.0f, -offset + this.animBottom * 5.0f / 2.0f, 0, 0, 64, 64);
                this.drawTexturedModalRect(-2.0f - offset + this.animBottom * 5.0f, -offset + this.animBottom * 5.0f / 2.0f, 192, 128, 64, 64);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
        GL11.glDisable(3042);
    }

    double easeOutBack(double t) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow(t - 1.0, 3.0) + c1 * Math.pow(t - 1.0, 2.0);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

