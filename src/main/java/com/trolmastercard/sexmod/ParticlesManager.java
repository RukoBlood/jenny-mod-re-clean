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

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.interfaces.IPositionProvider;
import com.trolmastercard.sexmod.util.interfaces.ITargetProvider;
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
public class ParticlesManager {
    final static ResourceLocation CUMMY_TEXTURE = new ResourceLocation("sexmod", "textures/cummy.png");
    static Minecraft minecraft = Minecraft.getMinecraft();
    static List<DynamicTrailRenderer> a = new ArrayList<DynamicTrailRenderer>();

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderWorldLastEvent renderWorldLastEvent) {
        ParticlesManager.minecraft.renderEngine.bindTexture(CUMMY_TEXTURE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float f = renderWorldLastEvent.getPartialTicks();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        if (ParticlesManager.minecraft.player == null) {
            return;
        }
        for (DynamicTrailRenderer ep_class2632 : a) {
            ep_class2632.renderTrail(minecraft, tessellator, bufferBuilder, f);
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
        for (DynamicTrailRenderer ep_class2632 : a) {
            ep_class2632.onTick();
        }
    }

    public static void a(DynamicTrailRenderer ep_class2632) {
        a.add(ep_class2632);
    }

    public static void a(int n, IPositionProvider ar_class412, ITargetProvider b8_class692, GirlEntity em_class2582, float f, float f2) {
        a.add(new DynamicTrailRenderer(n, ar_class412, b8_class692, em_class2582, f, f2));
    }

    public static void spawnSexParticles(@Nonnull GirlEntity girl) {
        ArrayList<DynamicTrailRenderer> arrayList = new ArrayList<DynamicTrailRenderer>();
        for (DynamicTrailRenderer ep_class2632 : a) {
            if (!ep_class2632.ownerEntity.girlID().equals(girl.girlID())) continue;
            arrayList.add(ep_class2632);
        }
        a.removeAll(arrayList);
    }
}

