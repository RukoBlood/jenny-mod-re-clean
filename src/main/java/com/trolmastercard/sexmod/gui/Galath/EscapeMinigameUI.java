/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui.Galath;

import java.util.Random;

import com.trolmastercard.sexmod.Packets.GalathBackOffRape;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class EscapeMinigameUI extends Gui {
    final static ResourceLocation ESCAPE_MINIGAME_UI = new ResourceLocation("sexmod", "textures/gui/escape_minigame_ui.png");
    final static int BUTTON_SIZE = 52;
    final static float INTRO_OUTRO_DURATION = 20.0f;
    final static int KEY_SWITCH_COOLDOWN = 35;
    final static float PROGRESS_INCREMENT = 0.08f;
    final static float PROGRESS_DECAY = 0.006f;
    final static int BLINK_RATE = 2;
    final static float UI_SCALE = 0.33f;
    static boolean isMinigameActive = false;
    static EscapeMinigameUIKeybinds currentRequiredKey = null;
    static float escapeProgress = 0.0f;
    static float activeTasks = 0.0f;
    static boolean isIndicatorBlinking = true;
    static float closingTicks = 0.0f;
    static boolean isClosing = false;
    static Minecraft mc = Minecraft.getMinecraft();
    static boolean hasSentReleasePacket = false;

    public static void UpdateMinigame() {
        if (!isMinigameActive) {
            return;
        }
        if (EscapeMinigameUI.mc.world == null) {
            isMinigameActive = false;
            hasSentReleasePacket = false;
            activeTasks = 0.0f;
            escapeProgress = 0.0f;
            closingTicks = 0.0f;
            isClosing = false;
        }
        if (isClosing) {
            isIndicatorBlinking = false;
            if ((closingTicks += 1.0f) >= INTRO_OUTRO_DURATION) {
                isMinigameActive = false;
            }
            return;
        }
        if ((activeTasks += 1.0f) % (float)Math.max(1, 2) == 0.0f) {
            isIndicatorBlinking = !isIndicatorBlinking;
        }
        escapeProgress = Math.max(0.0f, escapeProgress - PROGRESS_DECAY);
        if (activeTasks < INTRO_OUTRO_DURATION) {
            return;
        }
        if (activeTasks % (float)KEY_SWITCH_COOLDOWN == 0.0f || currentRequiredKey == null) {
            EscapeMinigameUI.selectRandomKey();
        }
    }

    static void selectRandomKey() {
        EscapeMinigameUIKeybinds prevKey = currentRequiredKey;
        Random random = new Random();
        while (prevKey == (currentRequiredKey = EscapeMinigameUIKeybinds.values()[random.nextInt(EscapeMinigameUIKeybinds.values().length)])) {
            //finding next key
        }
    }

    static void handleSuccessfulEscape() {
        if (!isMinigameActive) {
            return;
        }
        if (hasSentReleasePacket) {
            return;
        }
        hasSentReleasePacket = true;
        PackageHandler.INSTANCE.sendToServer((IMessage)new GalathBackOffRape());
        EscapeMinigameUI.StartClosingAnimation();
    }

    public static void StartMinigame() {
        isMinigameActive = true;
        hasSentReleasePacket = false;
        activeTasks = 0.0f;
        escapeProgress = 0.0f;
        closingTicks = 0.0f;
        isClosing = false;
    }

    public static void StartClosingAnimation() {
        isClosing = true;
        closingTicks = 0.0f;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (!isMinigameActive) {
            return;
        }
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        int width = event.getResolution().getScaledWidth();
        int height = event.getResolution().getScaledHeight();
        float partialTicks = event.getPartialTicks();
        mc.getTextureManager().bindTexture(ESCAPE_MINIGAME_UI);
        double easeOffset = isClosing ? 1.0 - RotationHelper.EaseInBack((EscapeMinigameUI.closingTicks + partialTicks) / INTRO_OUTRO_DURATION) : Math.min(1.0, RotationHelper.EaseOutBack((activeTasks + partialTicks) / INTRO_OUTRO_DURATION));
        int targetY = height + 385;
        GlStateManager.pushMatrix();
        GlStateManager.scale(UI_SCALE, UI_SCALE, UI_SCALE);
        GlStateManager.translate(485.0f, 0.0f, 0.0f);
        int SpawnY = 4 * height;
        this.drawTexturedModalRect(width / 2 - 87, (int) RotationHelper.LerpDouble((double)SpawnY, (double)targetY, easeOffset), 0, 104, 174, 48);
        this.drawTexturedModalRect((int)((float)width / 2.0f - 78.0f), (int) RotationHelper.LerpDouble((double)SpawnY, (double)(targetY - BUTTON_SIZE), easeOffset), BUTTON_SIZE, isIndicatorBlinking && currentRequiredKey == EscapeMinigameUIKeybinds.A ? BUTTON_SIZE : 0, BUTTON_SIZE, BUTTON_SIZE);
        this.drawTexturedModalRect((int)((float)width / 2.0f - 26.0f), (int) RotationHelper.LerpDouble((double)SpawnY, (double)(targetY - BUTTON_SIZE), easeOffset), 2*BUTTON_SIZE, isIndicatorBlinking && currentRequiredKey == EscapeMinigameUIKeybinds.S ? BUTTON_SIZE : 0, BUTTON_SIZE, BUTTON_SIZE);
        this.drawTexturedModalRect((int)((float)width / 2.0f + 26.0f), (int) RotationHelper.LerpDouble((double)SpawnY, (double)(targetY - BUTTON_SIZE), easeOffset), 3*BUTTON_SIZE, isIndicatorBlinking && currentRequiredKey == EscapeMinigameUIKeybinds.D ? BUTTON_SIZE : 0, BUTTON_SIZE, BUTTON_SIZE);
        this.drawTexturedModalRect((int)((float)width / 2.0f - 26.0f), (int) RotationHelper.LerpDouble((double)SpawnY, (double)(targetY - BUTTON_SIZE), easeOffset), 0, isIndicatorBlinking && currentRequiredKey == EscapeMinigameUIKeybinds.W ? BUTTON_SIZE : 0, BUTTON_SIZE, BUTTON_SIZE);
        this.drawTexturedModalRect(width / 2 - 87 + 8, (int) RotationHelper.LerpDouble((double)(SpawnY - 8), (double)(targetY + 8), easeOffset), 8, 152, (int)(158.0f * escapeProgress), 32);
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        EscapeMinigameUI.UpdateMinigame();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent keyInputEvent) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        if (GameSettings.isKeyDown(gameSettings.keyBindLeft)) {
            escapeProgress = currentRequiredKey == EscapeMinigameUIKeybinds.A ? (escapeProgress += PROGRESS_INCREMENT) : (escapeProgress -= PROGRESS_INCREMENT / 2);
            return;
        }
        if (GameSettings.isKeyDown(gameSettings.keyBindRight)) {
            escapeProgress = currentRequiredKey == EscapeMinigameUIKeybinds.D ? (escapeProgress += PROGRESS_INCREMENT) : (escapeProgress -= PROGRESS_INCREMENT / 2);
            return;
        }
        if (GameSettings.isKeyDown(gameSettings.keyBindForward)) {
            escapeProgress = currentRequiredKey == EscapeMinigameUIKeybinds.W ? (escapeProgress += PROGRESS_INCREMENT) : (escapeProgress -= PROGRESS_INCREMENT /2);
            return;
        }
        if (GameSettings.isKeyDown(gameSettings.keyBindBack)) {
            escapeProgress = currentRequiredKey == EscapeMinigameUIKeybinds.S ? (escapeProgress += PROGRESS_INCREMENT) : (escapeProgress -= PROGRESS_INCREMENT /2);
            return;
        }
        if (escapeProgress >= 1.0f) {
            EscapeMinigameUI.handleSuccessfulEscape();
        }
    }
}

