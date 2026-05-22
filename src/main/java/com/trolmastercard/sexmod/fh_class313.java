/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.b6_class67;
import com.trolmastercard.sexmod.be_class78;
import com.trolmastercard.sexmod.ds_class200;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class fh_class313
extends GuiScreen {
    final static public int d = 1200;
    static private boolean b = false;
    static private double e = 0.0;
    static ResourceLocation c = new ResourceLocation("sexmod", "textures/gui/transitionscreen.png");
    static ResourceLocation f = new ResourceLocation("sexmod", "textures/gui/mirroredtransitionscreen.png");
    static ResourceLocation a = new ResourceLocation("sexmod", "textures/gui/blackscreen.png");

    public static boolean a() {
        return b;
    }

    public static void b() {
        b = true;
    }

    public static void a(Runnable runnable) {
        b = true;
        be_class78.a(1200, runnable);
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
        float f = n == 1 ? (float)b6_class67.b(-1800.0, 1000.0, 0.5 * Math.cos(e / 25.0) + 0.5) : (n == 2 ? (float)b6_class67.b(-900.0, 750.0, 0.5 * Math.cos(e / 25.0) + 0.5) : (float)b6_class67.b(-900.0, 600.0, 0.5 * Math.cos((e += (double)(minecraft.getTickLength() * 0.75f)) / 25.0) + 0.5));
        GlStateManager.pushMatrix();
        if (n == 1) {
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
        }
        if (n == 2) {
            GlStateManager.scale(1.5, 1.5, 1.5);
        }
        minecraft.renderEngine.bindTexture(c);
        this.drawTexturedModalRect(f, 0.0f, 0, (int)(e * 1.5), 256, 256);
        this.drawTexturedModalRect(f, 256.0f, 0, (int)(e * 1.5), 256, 256);
        this.drawTexturedModalRect(f, 512.0f, 0, (int)(e * 1.5), 256, 256);
        minecraft.renderEngine.bindTexture(fh_class313.f);
        this.drawTexturedModalRect(f + 600.0f, 0.0f, 0, (int)(e * 1.5), 256, 256);
        this.drawTexturedModalRect(f + 600.0f, 256.0f, 0, (int)(e * 1.5), 256, 256);
        this.drawTexturedModalRect(f + 600.0f, 512.0f, 0, (int)(e * 1.5), 256, 256);
        minecraft.renderEngine.bindTexture(a);
        this.drawTexturedModalRect(f + 200.0f, 0.0f, 0, 0, 400, 256);
        this.drawTexturedModalRect(f + 200.0f, 256.0f, 0, 0, 400, 256);
        this.drawTexturedModalRect(f + 200.0f, 512.0f, 0, 0, 400, 256);
        if (e > 30.0) {
            ds_class200.c();
        }
        if (e > 69.0) {
            e = 0.0;
            b = false;
        }
        GlStateManager.popMatrix();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

