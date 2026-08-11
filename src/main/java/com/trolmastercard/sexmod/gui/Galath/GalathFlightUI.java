/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.gui.Galath;

import com.trolmastercard.sexmod.FlightUITextureBounds;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class GalathFlightUI extends Gui {
    final static ResourceLocation UI_TEXTURE = new ResourceLocation("sexmod", "textures/gui/galath_flight_ui.png");
    final static FlightUITextureBounds BACKGROUND_BOUNDS = new FlightUITextureBounds(0, 77, 128, 41);
    final static FlightUITextureBounds CHARGE_ACTIVE_BOUNDS = new FlightUITextureBounds(0, 0, 23, 36);
    final static FlightUITextureBounds CHARGE_BLINK_BOUNDS = new FlightUITextureBounds(0, 36, 23, 36);
    final static FlightUITextureBounds ICON_SHADOWS_BOUNDS = new FlightUITextureBounds(23, 2, 20, 31);
    static long y = 3000L;
    static long n = 5000L;
    final static long FADE_DURATION = 500L;
    final static float ANIMATION_SPEED = 150.0f;
    final static float[] X_OFFSET_SPENT = new float[]{-14.25f, -15.5f, -16.875f};
    final static float[] X_OFFSET_REGEN = new float[]{37.5f, 43.0f, 45.0f};
    final static int UI_Y_OFFSET = 70;
    static boolean isUIVisible = false;
    static Minecraft mc = Minecraft.getMinecraft();
    static int availableCharges = 3;
    static long lastChargeUsedTime = 0L;
    static long lastRegenTime = 0L;
    static long uiFadeInStartTime = 0L;
    static long uiFadeOutStartTime = 9223372036854775307L;

    public static boolean canBoost() {
        if (availableCharges <= 0) {
            return false;
        }
        return System.currentTimeMillis() - lastChargeUsedTime > y;
    }

    public static void consumeCharge() {
        --availableCharges;
        lastChargeUsedTime = System.currentTimeMillis();
    }

    void updateRegen() {
        if (availableCharges == 3) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - Math.max(lastChargeUsedTime, lastRegenTime) < n) {
            return;
        }
        ++availableCharges;
        lastRegenTime = currentTime;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        this.updateRegen();
        if (!isUIVisible) {
            return;
        }
        ScaledResolution resolution = event.getResolution();
        int screenW = resolution.getScaledWidth();
        int screenH = resolution.getScaledHeight();
        int centerX = screenW / 2;
        long curTime = System.currentTimeMillis();
        if (curTime - uiFadeOutStartTime > FADE_DURATION) {
            GalathFlightUI.hideUIImmediately();
            return;
        }
        mc.getTextureManager().bindTexture(UI_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();

        float alpha = curTime < uiFadeInStartTime + FADE_DURATION
                ? (float)(curTime - uiFadeInStartTime) / (float) FADE_DURATION
                : (curTime < uiFadeOutStartTime + FADE_DURATION ? 1.0f + (float)(uiFadeOutStartTime - curTime) / (float) FADE_DURATION : 1.0f);

        alpha = Utils.clamp(alpha, 0.0f, 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);

        this.drawElement(BACKGROUND_BOUNDS, centerX - GalathFlightUI.BACKGROUND_BOUNDS.w / 2, screenH - UI_Y_OFFSET);
        this.drawElement(ICON_SHADOWS_BOUNDS, (int)((float)centerX - 1.5f * (float) GalathFlightUI.CHARGE_ACTIVE_BOUNDS.w + 1.0f), screenH - UI_Y_OFFSET + 3);
        this.drawElement(ICON_SHADOWS_BOUNDS, centerX - GalathFlightUI.CHARGE_ACTIVE_BOUNDS.w / 2 + 1, screenH - UI_Y_OFFSET + 3);
        this.drawElement(ICON_SHADOWS_BOUNDS, centerX + GalathFlightUI.CHARGE_ACTIVE_BOUNDS.w / 2 + 1, screenH - UI_Y_OFFSET + 3);

        float spentProgress = (float) Reference.EaseOutSine(Math.min(1.0f, (float)(curTime - lastChargeUsedTime) / ANIMATION_SPEED));
        float regenProgress = spentProgress == 1.0f ? Utils.clamp(1.0f - (float)(curTime - GalathFlightUI.lastRegenTime) / 500.0f, 0.0f, 1.0f) : 0.0f;
        this.renderDynamicChargeIcon(1, -1.5f * (float) GalathFlightUI.CHARGE_ACTIVE_BOUNDS.w, regenProgress, spentProgress, centerX, screenH, alpha);
        this.renderDynamicChargeIcon(2, (float)(-GalathFlightUI.CHARGE_ACTIVE_BOUNDS.w) / 2.0f, regenProgress, spentProgress, centerX, screenH, alpha);
        this.renderDynamicChargeIcon(3, (float) GalathFlightUI.CHARGE_ACTIVE_BOUNDS.w / 2.0f, regenProgress, spentProgress, centerX, screenH, alpha);
    }

    void renderDynamicChargeIcon(int index, float xOffset, float regenProgress, float spentProgress, int centerX, int screenHeight, float globalAlpha) {
        float isSpent = availableCharges >= index ? 0.0f : (availableCharges < index - 1 ? 1.0f : spentProgress);
        float isRegening = availableCharges == index ? regenProgress : 0.0f;
        float scale = 1.0f + isSpent * 0.075f + isRegening * -0.15f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(isSpent * X_OFFSET_SPENT[index - 1] + isRegening * X_OFFSET_REGEN[index - 1], isSpent * -11.25f + isRegening * 37.5f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, globalAlpha - isSpent - isRegening);
        this.drawElement(CHARGE_ACTIVE_BOUNDS, (int)((float)centerX + xOffset), screenHeight - UI_Y_OFFSET);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, (float)Math.sin(Math.PI * (double)isSpent) * 0.5f);
        this.drawElement(CHARGE_BLINK_BOUNDS, (int)((float)centerX + xOffset), screenHeight - UI_Y_OFFSET);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public static void showUI() {
        if (isUIVisible) {
            return;
        }
        isUIVisible = true;
        uiFadeInStartTime = System.currentTimeMillis();
        uiFadeOutStartTime = Long.MAX_VALUE;
    }

    public static void startFadeOutTimer() {
        uiFadeOutStartTime = System.currentTimeMillis();
    }

    public static void hideUIImmediately() {
        isUIVisible = false;
        uiFadeOutStartTime = Long.MAX_VALUE;
        uiFadeInStartTime = 0L;
    }

    void drawElement(FlightUITextureBounds bounds, int x, int y) {
        this.drawTexturedModalRect(x, y, bounds.u, bounds.v, bounds.w, bounds.h);
    }
}

