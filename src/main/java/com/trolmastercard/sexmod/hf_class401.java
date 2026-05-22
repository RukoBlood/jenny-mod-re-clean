/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.b6_class67;
import com.trolmastercard.sexmod.be_class78;
import com.trolmastercard.sexmod.bl_class87;
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
public class hf_class401
extends Gui {
    final static ResourceLocation j = new ResourceLocation("sexmod", "textures/gui/galath_flight_ui.png");
    final static bl_class87 i = new bl_class87(0, 77, 128, 41);
    final static bl_class87 w = new bl_class87(0, 0, 23, 36);
    final static bl_class87 k = new bl_class87(0, 36, 23, 36);
    final static bl_class87 p = new bl_class87(23, 2, 20, 31);
    static long y = 3000L;
    static long n = 5000L;
    final static long l = 500L;
    final static float d = 150.0f;
    final static float m = 0.075f;
    final static float b = -11.25f;
    final static float[] x = new float[]{-14.25f, -15.5f, -16.875f};
    final static float h = 500.0f;
    final static float o = -0.15f;
    final static float r = 37.5f;
    final static float[] t = new float[]{37.5f, 43.0f, 45.0f};
    final static int v = 70;
    final static int a = 70;
    static boolean q = false;
    static Minecraft c = Minecraft.getMinecraft();
    static int e = 3;
    static long s = 0L;
    static long f = 0L;
    static long u = 0L;
    static long g = 9223372036854775307L;

    public static boolean d() {
        if (e <= 0) {
            return false;
        }
        return System.currentTimeMillis() - s > y;
    }

    public static void a() {
        --e;
        s = System.currentTimeMillis();
    }

    void b() {
        if (e == 3) {
            return;
        }
        long l = System.currentTimeMillis();
        if (l - Math.max(s, f) < n) {
            return;
        }
        ++e;
        f = l;
    }

    @SubscribeEvent
    public void a(RenderGameOverlayEvent renderGameOverlayEvent) {
        this.b();
        if (!q) {
            return;
        }
        ScaledResolution scaledResolution = renderGameOverlayEvent.getResolution();
        int n = scaledResolution.getScaledWidth();
        int n2 = scaledResolution.getScaledHeight();
        int n3 = n / 2;
        long l = System.currentTimeMillis();
        if (l - g > 500L) {
            hf_class401.e();
            return;
        }
        c.getTextureManager().bindTexture(j);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        float f = l < u + 500L ? (float)(l - u) / 500.0f : (l < g + 500L ? 1.0f + (float)(g - l) / 500.0f : 1.0f);
        f = be_class78.b(f, 0.0f, 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f);
        this.a(i, n3 - hf_class401.i.c / 2, n2 - 70);
        this.a(p, (int)((float)n3 - 1.5f * (float)hf_class401.w.c + 1.0f), n2 - 70 + 3);
        this.a(p, n3 - hf_class401.w.c / 2 + 1, n2 - 70 + 3);
        this.a(p, n3 + hf_class401.w.c / 2 + 1, n2 - 70 + 3);
        float f2 = (float)b6_class67.b(Math.min(1.0f, (float)(l - s) / 150.0f));
        float f3 = f2 == 1.0f ? be_class78.b(1.0f - (float)(l - hf_class401.f) / 500.0f, 0.0f, 1.0f) : 0.0f;
        this.a(1, -1.5f * (float)hf_class401.w.c, f3, f2, n3, n2, f);
        this.a(2, (float)(-hf_class401.w.c) / 2.0f, f3, f2, n3, n2, f);
        this.a(3, (float)hf_class401.w.c / 2.0f, f3, f2, n3, n2, f);
    }

    void a(int n, float f, float f2, float f3, int n2, int n3, float f4) {
        float f5 = e >= n ? 0.0f : (e < n - 1 ? 1.0f : f3);
        float f6 = e == n ? f2 : 0.0f;
        float f7 = 1.0f + f5 * 0.075f + f6 * -0.15f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(f7, f7, f7);
        GlStateManager.translate(f5 * x[n - 1] + f6 * t[n - 1], f5 * -11.25f + f6 * 37.5f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f4 - f5 - f6);
        this.a(w, (int)((float)n2 + f), n3 - 70);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, (float)Math.sin(Math.PI * (double)f5) * 0.5f);
        this.a(k, (int)((float)n2 + f), n3 - 70);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public static void f() {
        if (q) {
            return;
        }
        q = true;
        u = System.currentTimeMillis();
        g = 9223372036854775307L;
    }

    public static void c() {
        g = System.currentTimeMillis();
    }

    public static void e() {
        q = false;
        g = 9223372036854775307L;
        u = 0L;
    }

    void a(bl_class87 bl_class872, int n, int n2) {
        this.drawTexturedModalRect(n, n2, bl_class872.a, bl_class872.d, bl_class872.c, bl_class872.b);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

