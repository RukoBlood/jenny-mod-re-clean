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
    final static public int DELAY = 1200;
    static private boolean active = false;
    static private double step = 0.0;
    static ResourceLocation transitionScreen = new ResourceLocation("sexmod", "textures/gui/transitionscreen.png");
    static ResourceLocation mirroredTransitionScreen = new ResourceLocation("sexmod", "textures/gui/mirroredtransitionscreen.png");
    static ResourceLocation blackScreen = new ResourceLocation("sexmod", "textures/gui/blackscreen.png");

    public static boolean getActive() {
        return active;
    }

    public static void run() {
        active = true;
    }

    public static void runWithDelay(Runnable runnable) {
        active = true;
        Utils.runDelayedTask(DELAY, runnable);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent
    public void Render(RenderGameOverlayEvent event) {
        if (!active) {
            return;
        }
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        Minecraft mc1 = Minecraft.getMinecraft();
        int guiScale = mc1.gameSettings.guiScale;
        float overlayScale = guiScale == 1
                ? (float) Reference.LerpDouble(-1800.0, 1000.0, 0.5 * Math.cos(step / 25.0) + 0.5)
                : (guiScale == 2 ? (float) Reference.LerpDouble(-900.0, 750.0, 0.5 * Math.cos(step / 25.0) + 0.5)
                : (float) Reference.LerpDouble(-900.0, 600.0, 0.5 * Math.cos((step += (double)(mc1.getTickLength() * 0.75f)) / 25.0) + 0.5));

        GlStateManager.pushMatrix();

        if (guiScale == 1) {
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
        }
        if (guiScale == 2) {
            GlStateManager.scale(1.5, 1.5, 1.5);
        }

        mc1.renderEngine.bindTexture(transitionScreen);
        this.drawTexturedModalRect(overlayScale, 0.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(overlayScale, 256.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(overlayScale, 512.0f, 0, (int)(step * 1.5), 256, 256);
        mc1.renderEngine.bindTexture(BlackScreenUI.mirroredTransitionScreen);
        this.drawTexturedModalRect(overlayScale + 600.0f, 0.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(overlayScale + 600.0f, 256.0f, 0, (int)(step * 1.5), 256, 256);
        this.drawTexturedModalRect(overlayScale + 600.0f, 512.0f, 0, (int)(step * 1.5), 256, 256);
        mc1.renderEngine.bindTexture(blackScreen);
        this.drawTexturedModalRect(overlayScale + 200.0f, 0.0f, 0, 0, 400, 256);
        this.drawTexturedModalRect(overlayScale + 200.0f, 256.0f, 0, 0, 400, 256);
        this.drawTexturedModalRect(overlayScale + 200.0f, 512.0f, 0, 0, 400, 256);
        if (step > 30.0) {
            SexUI.hide();
        }
        if (step > 69.0) {
            step = 0.0;
            active = false;
        }
        GlStateManager.popMatrix();
    }
}

