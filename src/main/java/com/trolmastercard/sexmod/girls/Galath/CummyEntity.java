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
package com.trolmastercard.sexmod.girls.Galath;

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
public class CummyEntity {
    final static ResourceLocation CUMMY_TEXTURE = new ResourceLocation("sexmod", "textures/cummy.png");
    static Minecraft mc = Minecraft.getMinecraft();
    static List<DynamicTrailRenderer> trailRenderers = new ArrayList<DynamicTrailRenderer>();

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        CummyEntity.mc.renderEngine.bindTexture(CUMMY_TEXTURE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        float partialTicks = event.getPartialTicks();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        if (CummyEntity.mc.player != null) {
            for (DynamicTrailRenderer trailRenderer : trailRenderers) {
                trailRenderer.renderTrail(mc, tessellator, buffer, partialTicks);
            }
            GlStateManager.enableDepth();
            GlStateManager.enableLighting();
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            for (DynamicTrailRenderer trailRenderer : trailRenderers) {
                trailRenderer.updateTrails();
            }
        }
    }

    public static void registerTrail(DynamicTrailRenderer renderer) {
        trailRenderers.add(renderer);
    }

    public static void createTrail(int maxSegmentsCount, IPositionProvider positionProvider, ITargetProvider targetProvider, GirlEntity girl, float randomnessRadus, float maxDistance) {
        trailRenderers.add(new DynamicTrailRenderer(maxSegmentsCount, positionProvider, targetProvider, girl, randomnessRadus, maxDistance));
    }

    public static void spawnSexParticles(@Nonnull GirlEntity girl) {
        ArrayList<DynamicTrailRenderer> renderers = new ArrayList<DynamicTrailRenderer>();
        for (DynamicTrailRenderer renderer : trailRenderers) {
            if (!renderer.ownerEntity.girlID().equals(girl.girlID())) continue;
            renderers.add(renderer);
        }
        trailRenderers.removeAll(renderers);
    }
}

