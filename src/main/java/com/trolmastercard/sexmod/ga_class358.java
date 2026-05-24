/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ga_class358 {
    final static ResourceLocation CUMMY_TEXTURE = new ResourceLocation("sexmod", "textures/cummy.png");
    static Minecraft minecraft = Minecraft.getMinecraft();
    static List<ep_class263> a = new ArrayList<ep_class263>();

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderWorldLastEvent renderWorldLastEvent) {
        ga_class358.minecraft.renderEngine.bindTexture(CUMMY_TEXTURE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float f = renderWorldLastEvent.getPartialTicks();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        if (ga_class358.minecraft.player == null) {
            return;
        }
        for (ep_class263 ep_class2632 : a) {
            ep_class2632.a(minecraft, tessellator, bufferBuilder, f);
        }
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.END) {
            return;
        }
        for (ep_class263 ep_class2632 : a) {
            ep_class2632.a();
        }
    }

    public static void a(ep_class263 ep_class2632) {
        a.add(ep_class2632);
    }

    public static void a(int n, ar_class41 ar_class412, b8_class69 b8_class692, GirlEntity em_class2582, float f, float f2) {
        a.add(new ep_class263(n, ar_class412, b8_class692, em_class2582, f, f2));
    }

    public static void a(@Nonnull GirlEntity em_class2582) {
        ArrayList<ep_class263> arrayList = new ArrayList<ep_class263>();
        for (ep_class263 ep_class2632 : a) {
            if (!ep_class2632.e.girlID().equals(em_class2582.girlID())) continue;
            arrayList.add(ep_class2632);
        }
        a.removeAll(arrayList);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

