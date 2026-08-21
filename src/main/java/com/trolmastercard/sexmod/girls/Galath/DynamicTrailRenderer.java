/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.ArrayList;
import java.util.List;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.interfaces.IPositionProvider;
import com.trolmastercard.sexmod.util.interfaces.ITargetProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class DynamicTrailRenderer {
    final static int MAX_TICKS_AGE = 30;
    final static int SEGMENTS_PER_TICK = 6;
    final static int BATCH_SIZE = 6;
    final static float ALPHA_THRESHOLD = 0.15f;
    List<TrailSegment> segments = new ArrayList<TrailSegment>();
    final int maxSegmentsCount;
    final IPositionProvider sourcePositionProvider;
    final ITargetProvider targetPositionProvider;
    final GirlEntity ownerEntity;
    final float randomnessRadius;
    final float maxSegmentsDistance;

    public DynamicTrailRenderer(int maxSegments, IPositionProvider posProvides, ITargetProvider targetProvider, GirlEntity entity, float randRadius, float maxDist) {
        this.maxSegmentsCount = maxSegments;
        this.sourcePositionProvider = posProvides;
        this.targetPositionProvider = targetProvider;
        this.ownerEntity = entity;
        this.randomnessRadius = randRadius;
        this.maxSegmentsDistance = maxDist;
    }

    public void renderTrail(Minecraft mc, Tessellator tessellator, BufferBuilder bufferBuilder, float partialTicks) {
        Vec3d lastNodePos;
        if (this.segments.size() < this.maxSegmentsCount) {
            for (int i = 0; i < BATCH_SIZE; ++i) {
                lastNodePos = this.sourcePositionProvider.getPosition(this.ownerEntity);
                this.segments.add(new TrailSegment(mc.world, this.targetPositionProvider.getTargetPosition(this.ownerEntity), new Vec3d(lastNodePos.x + (double)((Reference.RANDOM.nextFloat() * 2.0f - 1.0f) * this.randomnessRadius), lastNodePos.y + (double)((Reference.RANDOM.nextFloat() * 2.0f - 1.0f) * this.randomnessRadius), lastNodePos.z + (double)((Reference.RANDOM.nextFloat() * 2.0f - 1.0f) * this.randomnessRadius))));
            }
        }
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        Vec3d cameraPos = RotationHelper.LerpVec3d(new Vec3d(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ), mc.player.getPositionVector(), (double)partialTicks);
        bufferBuilder.begin(9, DefaultVertexFormats.POSITION_COLOR);
        this.sortSegmentsByDistance();
        lastNodePos = null;
        for (TrailSegment segment : this.segments) {
            Vec3d interpolatedPos = RotationHelper.LerpVec3d(segment.prevPosition, segment.currentPosition, (double)partialTicks);
            if (lastNodePos == null) {
                lastNodePos = interpolatedPos;
            }
            if (lastNodePos.distanceTo(interpolatedPos) > (double)this.maxSegmentsDistance) {
                tessellator.draw();
                bufferBuilder.begin(9, DefaultVertexFormats.POSITION_COLOR);
            }
            bufferBuilder.pos(interpolatedPos.x - cameraPos.x, interpolatedPos.y - cameraPos.y, interpolatedPos.z - cameraPos.z).color(255, 255, 255, 255).endVertex();
            lastNodePos = interpolatedPos;
        }
        tessellator.draw();
        GlStateManager.enableCull();
    }

    void updateTrails() {
        for (TrailSegment segment : this.segments) {
            segment.onUpdate();
        }
    }

    void sortSegmentsByDistance() {
        if (this.segments.isEmpty() || this.segments.size() <= 1) {
            return;
        }
        for (int i = 1; i < this.segments.size(); ++i) {
            int j;
            TrailSegment segment = this.segments.get(i);
            Vec3d pos = segment.currentPosition;
            for (j = i - 1; j >= 0 && pos.distanceTo(this.segments.get((int)j).currentPosition) < pos.distanceTo(this.segments.get((int)(j + 1)).currentPosition); --j) {
                this.segments.set(j + 1, this.segments.get(j));
            }
            this.segments.set(j + 1, segment);
        }
    }
}

