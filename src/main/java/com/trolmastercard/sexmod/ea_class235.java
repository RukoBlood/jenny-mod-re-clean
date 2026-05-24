/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import java.io.IOException;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ea_class235
extends GuiScreen {
    final static float j = 100.0f;
    final static float c = 15.0f;
    final static float k = 5.0f;
    final static float l = 0.5f;
    final static float b = 0.5f;
    final static ResourceLocation i = new ResourceLocation("sexmod", "textures/gui/command.png");
    float a = 0.0f;
    float g = 0.0f;
    float e = 0.0f;
    float d = 0.0f;
    float m = 0.0f;
    GirlEntity f;
    boolean h = false;

    public ea_class235(GirlEntity em_class2582) {
        this.f = em_class2582;
        this.h = em_class2582 instanceof GoblinEntity;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.d == 0.0f && this.m == 0.0f && this.g == 0.0f) {
            return;
        }
        if (this.g > 0.0f) {
            this.c();
            return;
        }
        if (!this.h) {
            return;
        }
        if (this.d > this.m) {
            this.a();
        } else {
            this.b();
        }
    }

    void a() {
        if (this.h) {
            ((GoblinEntity)this.f).void_c(Minecraft.getMinecraft().player.getPersistentID());
        }
    }

    void b() {
        ((GoblinEntity)this.f).void_b(Minecraft.getMinecraft().player.getPersistentID());
    }

    void c() {
        if (this.f.getID() != null) {
            return;
        }
        this.f.setCurrentAction(Action.START_THROWING);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (ClientProxy.keyBindings[0].getKeyCode() == Keyboard.getEventKey() && !Keyboard.getEventKeyState()) {
            Minecraft.getMinecraft().player.closeScreen();
            return;
        }
        super.handleKeyboardInput();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);
        try {
            this.a = Math.min(1.0f, this.a + this.mc.getTickLength() / 5.0f);
        } catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        float f2 = (float)this.a(this.a);
        float f3 = (1.0f - f2) * 100.0f;
        this.g += (float)(n < this.width / 2 ? 1 : -1) * this.mc.getTickLength();
        this.e += (float)(n > this.width / 2 ? 1 : -1) * this.mc.getTickLength();
        this.d += (float)(n2 < this.height / 2 - 1 ? 1 : -1) * this.mc.getTickLength();
        this.m += (float)(n2 > this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.g = be_class78.b(this.g, 0.0f, 1.0f);
        this.e = be_class78.b(this.e, 0.0f, 1.0f);
        this.d = be_class78.b(this.d, 0.0f, 1.0f);
        this.m = be_class78.b(this.m, 0.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)this.width / 2.0f, (float)this.height / 2.0f, 0.0f);
        GlStateManager.scale(f2, f2, f2);
        this.mc.renderEngine.bindTexture(i);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f + this.g * 0.5f, 1.0f + this.g * 0.5f, 1.0f);
        this.drawTexturedModalRect(-62.0f + f3 - this.g * 15.0f, f3 - 32.0f, 0, 0, 64, 64);
        this.drawTexturedModalRect(-62.0f + f3 - this.g * 15.0f, f3 - 32.0f, 64, 128, 64, 64);
        GlStateManager.popMatrix();
        if (!this.h) {
            GlStateManager.popMatrix();
            GL11.glDisable(3042);
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f - this.e, 1.0f - this.e, 1.0f);
        this.drawTexturedModalRect(-2.0f - f3 + this.e * 32.0f, -f3 - 32.0f, 0, 0, 64, 64);
        this.drawTexturedModalRect(-2.0f - f3 + this.e * 32.0f, -f3 - 32.0f, 0, 128, 64, 64);
        GlStateManager.popMatrix();
        if (this.e > 0.0f) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(-1.0f + this.e + 1.0f + this.d * 0.5f, -1.0f + this.e + 1.0f + this.d * 0.5f, 1.0f);
            this.drawTexturedModalRect(-2.0f - f3 + this.d * 5.0f, -f3 - 64.0f - this.d * 5.0f / 2.0f, 0, 0, 64, 64);
            this.drawTexturedModalRect(-2.0f - f3 + this.d * 5.0f, -f3 - 64.0f - this.d * 5.0f / 2.0f, 128, 128, 64, 64);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(-1.0f + this.e + 1.0f + this.m * 0.5f, -1.0f + this.e + 1.0f + this.m * 0.5f, 1.0f);
            this.drawTexturedModalRect(-2.0f - f3 + this.m * 5.0f, -f3 + this.m * 5.0f / 2.0f, 0, 0, 64, 64);
            this.drawTexturedModalRect(-2.0f - f3 + this.m * 5.0f, -f3 + this.m * 5.0f / 2.0f, 192, 128, 64, 64);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GL11.glDisable(3042);
    }

    double a(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return 1.0 + d3 * Math.pow(d - 1.0, 3.0) + d2 * Math.pow(d - 1.0, 2.0);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

