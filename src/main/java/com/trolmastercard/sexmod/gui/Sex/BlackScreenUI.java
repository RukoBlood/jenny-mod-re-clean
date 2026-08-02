/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.gui.Sex;

import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


//fh.class -> BlackScreenUI
@SideOnly(value=Side.CLIENT)
public class BlackScreenUI extends GuiScreen {
    final static public int d = 1200;
    static private boolean b = false;
    static private double step = 0.0;
    static ResourceLocation transitionScreen = new ResourceLocation("sexmod", "textures/gui/transitionscreen.png");
    static ResourceLocation mirroredTransitionScreen = new ResourceLocation("sexmod", "textures/gui/mirroredtransitionscreen.png");
    static ResourceLocation blackScreen = new ResourceLocation("sexmod", "textures/gui/blackscreen.png");

    public static boolean a() {
        return b;
    }

    public static void b() {
        b = true;
    }

    public static void a(Runnable runnable) {
        b = true;
        Utils.runDelayedTask(1200, runnable);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent
    public void a(RenderGameOverlayEvent renderGameOverlayEvent) {
        if (!b) {
            return;
        }
        if (renderGameOverlayEvent.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        int n = minecraft.gameSettings.guiScale;
        float f = n == 1
                ? (float) Reference.LerpDouble(-1800.0, 1000.0, 0.5 * Math.cos(step / 25.0) + 0.5)
                : (n == 2 ? (float) Reference.LerpDouble(-900.0, 750.0, 0.5 * Math.cos(step / 25.0) + 0.5)
                : (float) Reference.LerpDouble(-900.0, 600.0, 0.5 * Math.cos((step += (double)(minecraft.getTickLength() * 0.75f)) / 25.0) + 0.5));
        GlStateManager.pushMatrix();
        if (n == 1) {
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
        }
        if (n == 2) {
            GlStateManager.scale(1.5, 1.5, 1.5);
        }
        minecraft.renderEngine.bindTexture(transitionScreen);
        this.drawTexturedModalRect(f, 0.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(f, 256.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(f, 512.0f, 0, (int)(step * 1.5), 256, 256);
        minecraft.renderEngine.bindTexture(BlackScreenUI.mirroredTransitionScreen);
        this.drawTexturedModalRect(f + 600.0f, 0.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(f + 600.0f, 256.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(f + 600.0f, 512.0f, 0, (int)(step * 1.5), 256, 256);
        minecraft.renderEngine.bindTexture(blackScreen);
        this.drawTexturedModalRect(f + 200.0f, 0.0f, 0, 0, 400, 256);
        this.drawTexturedModalRect(f + 200.0f, 256.0f, 0, 0, 400, 256);
        this.drawTexturedModalRect(f + 200.0f, 512.0f, 0, 0, 400, 256);
        if (step > 30.0) {
            SexUI.hide();
        }
        if (step > 69.0) {
            step = 0.0;
            b = false;
        }
        GlStateManager.popMatrix();
    }
}

