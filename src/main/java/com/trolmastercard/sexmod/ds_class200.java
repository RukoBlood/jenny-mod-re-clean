/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.b6_class67;
import com.trolmastercard.sexmod.d3_class161;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class ds_class200
extends Gui {
    static ResourceLocation e = new ResourceLocation("sexmod", "textures/gui/buttons.png");
    static ResourceLocation b = new ResourceLocation("sexmod", "textures/gui/hornymeter.png");
    static public boolean d = false;
    static public double c;
    static double a;
    static float f;
    static float g;
    static boolean i;
    static boolean h;

    public static void d() {
        if (d) {
            return;
        }
        ds_class200.b();
        d = true;
        h = true;
    }

    public static void a(boolean bl) {
        if (d) {
            return;
        }
        ds_class200.b();
        d = true;
        h = bl;
    }

    public static void c() {
        ds_class200.b();
        d = false;
        h = true;
    }

    public static boolean a() {
        return d;
    }

    @SubscribeEvent
    public void a(RenderGameOverlayEvent renderGameOverlayEvent) {
        if (d && renderGameOverlayEvent.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            int n;
            Minecraft minecraft = Minecraft.getMinecraft();
            f = f < 1.0f ? (f += minecraft.getTickLength() / 25.0f) : 1.0f;
            GL11.glPushMatrix();
            minecraft.renderEngine.bindTexture(e);
            GL11.glScalef(0.35f, 0.35f, 0.35f);
            if (c >= 1.0) {
                if (d3_class161.a) {
                    i = true;
                }
                n = i ? 54 : 0;
                this.drawTexturedModalRect(240, 160, 0, 108 + n, 256, 52);
            }
            if (h && !i) {
                n = d3_class161.d ? 54 : 0;
                this.drawTexturedModalRect((int)b6_class67.a(-200.0f, 98.0f, f), 405, 0, n, 158, 54);
            }
            GL11.glScalef(2.857143f, 2.857143f, 2.857143f);
            minecraft.renderEngine.bindTexture(b);
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            this.drawTexturedModalRect(10, (int)b6_class67.a(-200.0f, 10.0f, f), 0, 0, 146, 175);
            a = b6_class67.b(a, c, (double)minecraft.getTickLength());
            n = (int)b6_class67.b(0.0, 160.0, a);
            int n2 = (int)b6_class67.b(167.0, 8.0, a);
            double d = b6_class67.b(178.0, 18.0, a);
            if (!i) {
                this.drawTexturedModalRect(67, (int)b6_class67.b(-45.0, d, (double)f), 159, n2, 32, n);
                this.drawTexturedModalRect(120, (int)b6_class67.b(-58.0, b6_class67.b(178.0, 149.0, 1.0 - a), (double)f), 212, (int)b6_class67.b(169.0, 141.0, 1.0 - a), 28, (int)b6_class67.b(1.0, 29.0, 1.0 - a));
                this.drawTexturedModalRect(18, (int)b6_class67.b(-58.0, b6_class67.b(178.0, 149.0, 1.0 - a), (double)f), 212, (int)b6_class67.b(169.0, 141.0, 1.0 - a), 28, (int)b6_class67.b(1.0, 29.0, 1.0 - a));
            } else {
                this.drawTexturedModalRect(67, (int)b6_class67.a(18.0f, -300.0f, g += minecraft.getTickLength() / 15.0f), 159, 8, 32, 160);
            }
            GL11.glPopMatrix();
        }
    }

    public static void a(double d) {
        c = (c += d) > 1.0 ? 1.0 : c;
    }

    public static void b() {
        c = 0.0;
        i = false;
    }

    static {
        a = c = 0.0;
        f = 0.0f;
        g = 0.0f;
        i = false;
        h = true;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

