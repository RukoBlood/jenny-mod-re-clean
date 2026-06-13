/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.gui;

import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class SexUI extends Gui {
    static ResourceLocation buttons = new ResourceLocation("sexmod", "textures/gui/buttons.png");
    static ResourceLocation hornyMeter = new ResourceLocation("sexmod", "textures/gui/hornymeter.png");
    static public boolean shouldBeRendered = false;
    static public double cumPercentage;
    static double drawCumPercentage;
    static float transitionStep;
    static float cumStep;
    static boolean keepSpacePressed;
    static boolean h;

    public static void d() {
        if (shouldBeRendered) {
            return;
        }
        SexUI.resetCumPercentage();
        shouldBeRendered = true;
        h = true;
    }

    public static void a(boolean bl) {
        if (shouldBeRendered) {
            return;
        }
        SexUI.resetCumPercentage();
        shouldBeRendered = true;
        h = bl;
    }

    public static void c() {
        SexUI.resetCumPercentage();
        shouldBeRendered = false;
        h = true;
    }

    public static boolean getShouldBeRendered() {
        return shouldBeRendered;
    }

    @SubscribeEvent
    public void RenderUI(RenderGameOverlayEvent event) {
        if (shouldBeRendered && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            int height;
            Minecraft minecraft = Minecraft.getMinecraft();
            transitionStep = transitionStep < 1.0f ? (transitionStep += minecraft.getTickLength() / 25.0f) : 1.0f;
            GL11.glPushMatrix();
            minecraft.renderEngine.bindTexture(buttons);
            GL11.glScalef(0.35f, 0.35f, 0.35f);
            if (cumPercentage >= 1.0) {
                if (HandlePlayerMovement.isCumming) {
                    keepSpacePressed = true;
                }
                height = keepSpacePressed ? 54 : 0;
                this.drawTexturedModalRect(240, 160, 0, 108 + height, 256, 52);
            }
            if (h && !keepSpacePressed) {
                height = HandlePlayerMovement.isThrusting ? 54 : 0;
                this.drawTexturedModalRect((int) Reference.LerpFloat(-200.0f, 98.0f, transitionStep), 405, 0, height, 158, 54);
            }
            GL11.glScalef(2.857143f, 2.857143f, 2.857143f);
            minecraft.renderEngine.bindTexture(hornyMeter);
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            this.drawTexturedModalRect(10, (int) Reference.LerpFloat(-200.0f, 10.0f, transitionStep), 0, 0, 146, 175);
            drawCumPercentage = Reference.LerpDouble(drawCumPercentage, cumPercentage, (double)minecraft.getTickLength());
            height = (int) Reference.LerpDouble(0.0, 160.0, drawCumPercentage);
            int textureY = (int) Reference.LerpDouble(167.0, 8.0, drawCumPercentage);
            double y = Reference.LerpDouble(178.0, 18.0, drawCumPercentage);
            if (!keepSpacePressed) {
                this.drawTexturedModalRect(67, (int) Reference.LerpDouble(-45.0, y, (double) transitionStep), 159, textureY, 32, height);
                this.drawTexturedModalRect(120, (int) Reference.LerpDouble(-58.0, Reference.LerpDouble(178.0, 149.0, 1.0 - drawCumPercentage), (double) transitionStep), 212, (int) Reference.LerpDouble(169.0, 141.0, 1.0 - drawCumPercentage), 28, (int) Reference.LerpDouble(1.0, 29.0, 1.0 - drawCumPercentage));
                this.drawTexturedModalRect(18, (int) Reference.LerpDouble(-58.0, Reference.LerpDouble(178.0, 149.0, 1.0 - drawCumPercentage), (double) transitionStep), 212, (int) Reference.LerpDouble(169.0, 141.0, 1.0 - drawCumPercentage), 28, (int) Reference.LerpDouble(1.0, 29.0, 1.0 - drawCumPercentage));
            } else {
                this.drawTexturedModalRect(67, (int) Reference.LerpFloat(18.0f, -300.0f, cumStep += minecraft.getTickLength() / 15.0f), 159, 8, 32, 160);
            }
            GL11.glPopMatrix();
        }
    }

    public static void addCumPercentage(double d) {
        cumPercentage = (cumPercentage += d) > 1.0 ? 1.0 : cumPercentage;
    }

    public static void resetCumPercentage() {
        cumPercentage = 0.0;
        keepSpacePressed = false;
    }

    static {
        drawCumPercentage = cumPercentage = 0.0;
        transitionStep = 0.0f;
        cumStep  = 0.0f;
        keepSpacePressed = false;
        h = true;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

