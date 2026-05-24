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
package com.trolmastercard.sexmod.gui;

import java.util.Random;

import com.trolmastercard.sexmod.Packages.GalathBackOffRape;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
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
    final static ResourceLocation l = new ResourceLocation("sexmod", "textures/gui/escape_minigame_ui.png");
    final static int f = 52;
    final static float a = 20.0f;
    final static int p = 35;
    final static float n = 0.08f;
    final static float h = 0.006f;
    final static int m = 2;
    final static float i = 0.33f;
    static boolean g = false;
    static EscapeMinigameUIKeybinds q = null;
    static float k = 0.0f;
    static float j = 0.0f;
    static boolean b = true;
    static float d = 0.0f;
    static boolean c = false;
    static Minecraft minecraft = Minecraft.getMinecraft();
    static boolean o = false;

    public static void e() {
        if (!g) {
            return;
        }
        if (EscapeMinigameUI.minecraft.world == null) {
            g = false;
            o = false;
            j = 0.0f;
            k = 0.0f;
            d = 0.0f;
            c = false;
        }
        if (c) {
            b = false;
            if ((d += 1.0f) >= 20.0f) {
                g = false;
            }
            return;
        }
        if ((j += 1.0f) % (float)Math.max(1, 2) == 0.0f) {
            b = !b;
        }
        k = Math.max(0.0f, k - 0.006f);
        if (j < 20.0f) {
            return;
        }
        if (j % 35.0f == 0.0f || q == null) {
            EscapeMinigameUI.b();
        }
    }

    static void b() {
        EscapeMinigameUIKeybinds keybinds = q;
        Random random = new Random();
        while (keybinds == (q = EscapeMinigameUIKeybinds.values()[random.nextInt(EscapeMinigameUIKeybinds.values().length)])) {
        }
    }

    static void c() {
        if (!g) {
            return;
        }
        if (o) {
            return;
        }
        o = true;
        PackageHandler.networkWrapper.sendToServer((IMessage)new GalathBackOffRape());
        EscapeMinigameUI.d();
    }

    public static void a() {
        g = true;
        o = false;
        j = 0.0f;
        k = 0.0f;
        d = 0.0f;
        c = false;
    }

    public static void d() {
        c = true;
        d = 0.0f;
    }

    @SubscribeEvent
    public void a(RenderGameOverlayEvent renderGameOverlayEvent) {
        if (!g) {
            return;
        }
        if (renderGameOverlayEvent.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        int n = renderGameOverlayEvent.getResolution().getScaledWidth();
        int n2 = renderGameOverlayEvent.getResolution().getScaledHeight();
        float f = renderGameOverlayEvent.getPartialTicks();
        minecraft.getTextureManager().bindTexture(l);
        double d = c ? 1.0 - Reference.d((EscapeMinigameUI.d + f) / 20.0f) : Math.min(1.0, Reference.c((j + f) / 20.0f));
        int n3 = n2 + 385;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.33f, 0.33f, 0.33f);
        GlStateManager.translate(485.0f, 0.0f, 0.0f);
        int n4 = 4 * n2;
        this.drawTexturedModalRect(n / 2 - 87, (int) Reference.Lerp((double)n4, (double)n3, d), 0, 104, 174, 48);
        this.drawTexturedModalRect((int)((float)n / 2.0f - 78.0f), (int) Reference.Lerp((double)n4, (double)(n3 - 52), d), 52, b && q == EscapeMinigameUIKeybinds.A ? 52 : 0, 52, 52);
        this.drawTexturedModalRect((int)((float)n / 2.0f - 26.0f), (int) Reference.Lerp((double)n4, (double)(n3 - 52), d), 104, b && q == EscapeMinigameUIKeybinds.S ? 52 : 0, 52, 52);
        this.drawTexturedModalRect((int)((float)n / 2.0f + 26.0f), (int) Reference.Lerp((double)n4, (double)(n3 - 52), d), 156, b && q == EscapeMinigameUIKeybinds.D ? 52 : 0, 52, 52);
        this.drawTexturedModalRect((int)((float)n / 2.0f - 26.0f), (int) Reference.Lerp((double)n4, (double)(n3 - 104), d), 0, b && q == EscapeMinigameUIKeybinds.W ? 52 : 0, 52, 52);
        this.drawTexturedModalRect(n / 2 - 87 + 8, (int) Reference.Lerp((double)(n4 - 8), (double)(n3 + 8), d), 8, 152, (int)(158.0f * k), 32);
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void a(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.END) {
            return;
        }
        EscapeMinigameUI.e();
    }

    @SubscribeEvent
    public void a(InputEvent.KeyInputEvent keyInputEvent) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        if (GameSettings.isKeyDown(gameSettings.keyBindLeft)) {
            k = q == EscapeMinigameUIKeybinds.A ? (k += 0.08f) : (k -= 0.04f);
            return;
        }
        if (GameSettings.isKeyDown(gameSettings.keyBindRight)) {
            k = q == EscapeMinigameUIKeybinds.D ? (k += 0.08f) : (k -= 0.04f);
            return;
        }
        if (GameSettings.isKeyDown(gameSettings.keyBindForward)) {
            k = q == EscapeMinigameUIKeybinds.W ? (k += 0.08f) : (k -= 0.04f);
            return;
        }
        if (GameSettings.isKeyDown(gameSettings.keyBindBack)) {
            k = q == EscapeMinigameUIKeybinds.S ? (k += 0.08f) : (k -= 0.04f);
            return;
        }
        if (k >= 1.0f) {
            EscapeMinigameUI.c();
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

