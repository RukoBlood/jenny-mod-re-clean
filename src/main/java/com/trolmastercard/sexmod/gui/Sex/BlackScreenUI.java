/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.gui.Sex;

import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.util.RotationHelper;
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
        ThreadNames.createDaemonThread(DELAY, runnable);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent
    public void Render(RenderGameOverlayEvent event) {
        if (active) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                Minecraft mc = Minecraft.getMinecraft();
                int guiScale = mc.gameSettings.guiScale;
                float offsetX = guiScale == 1
                        ? (float) RotationHelper.LerpDouble(-1800.0, 1000.0, 0.5 * Math.cos(step / 25.0) + 0.5)
                        : (guiScale == 2 ? (float) RotationHelper.LerpDouble(-900.0, 750.0, 0.5 * Math.cos(step / 25.0) + 0.5)
                        : (float) RotationHelper.LerpDouble(-900.0, 600.0, 0.5 * Math.cos((step += mc.getTickLength() * 0.75f) / 25.0) + 0.5));

                GlStateManager.pushMatrix();

                if (guiScale == 1) {
                    GlStateManager.scale(2.0f, 2.0f, 2.0f);
                }
                if (guiScale == 2) {
                    GlStateManager.scale(1.5, 1.5, 1.5);
                }

                mc.renderEngine.bindTexture(transitionScreen);
                this.drawTexturedModalRect(offsetX, 0.0f, 0, (int) (step * 1.5), 256, 256);
                this.drawTexturedModalRect(offsetX, 256.0f, 0, (int) (step * 1.5), 256, 256);
                this.drawTexturedModalRect(offsetX, 512.0f, 0, (int) (step * 1.5), 256, 256);
                mc.renderEngine.bindTexture(mirroredTransitionScreen);
                this.drawTexturedModalRect(offsetX + 600.0f, 0.0f, 0, (int) (step * 1.5), 256, 256);
                this.drawTexturedModalRect(offsetX + 600.0f, 256.0f, 0, (int) (step * 1.5), 256, 256);
                this.drawTexturedModalRect(offsetX + 600.0f, 512.0f, 0, (int) (step * 1.5), 256, 256);
                mc.renderEngine.bindTexture(blackScreen);
                this.drawTexturedModalRect(offsetX + 200.0f, 0.0f, 0, 0, 400, 256);
                this.drawTexturedModalRect(offsetX + 200.0f, 256.0f, 0, 0, 400, 256);
                this.drawTexturedModalRect(offsetX + 200.0f, 512.0f, 0, 0, 400, 256);
                if (step > 30.0) {
                    SexUI.hide();
                }
                if (step > 69.0) {
                    step = 0.0;
                    active = false;
                    SexUI.showUI(); //TODO possible bugfix
                }
                GlStateManager.popMatrix();
            }
        }
    }
}

