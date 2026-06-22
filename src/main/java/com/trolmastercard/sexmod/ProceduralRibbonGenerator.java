/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;

import com.trolmastercard.sexmod.util.ColorRGBA;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
/*
* ProceduralRibbonGenerator
* (Original name ef_class245)
* It's probably for tentacles or something else
* Gemini by Google gave that name.
* */
public class ProceduralRibbonGenerator {
    public static void renderRibbon(BufferBuilder bufferBuilder, Tessellator tessellator, Minecraft mc, RibbonSettings settings) {
        Vec3d[] segmentedVertices;
        int n;

        Vec3d[] baseVertices = new Vec3d[]{
                new Vec3d(-settings.width, -settings.height, 0.0),
                new Vec3d(-settings.width, settings.height, 0.0),
                new Vec3d(settings.width, settings.height, 0.0),
                new Vec3d(settings.width, -settings.height, 0.0)
        };

        Vec3d direction = new Vec3d(0.0, 0.0, -settings.LengthStep);
        Vec3d currentPos = VectorMath.scale(direction.normalize(), (double)settings.initialOffset);

        Vec3d[] firstSegment = new Vec3d[4];
        System.arraycopy(baseVertices, 0, firstSegment, 0, 4);

        ArrayList<Vec3d[]> segments = new ArrayList<Vec3d[]>();
        float renderTime = (float)mc.player.ticksExisted + mc.getRenderPartialTicks();

        for (n = 0; n <= settings.maxSegments; ++n) {
            segmentedVertices = new Vec3d[4];
            float taperFactor = 1.0f - (float)n / (float)settings.maxSegments;

            for (int i = 0; i < 4; ++i) {
                Vec3d vertex = baseVertices[i];
                segmentedVertices[i] = new Vec3d(vertex.x * (double)taperFactor, vertex.y, vertex.z).add(currentPos);
            }

            segments.add(segmentedVertices);

            direction = VectorMath.rotateEuler(direction,
                    settings.waveX.getOffset(n, renderTime),
                    settings.WaveY.getOffset(n, renderTime),
                    settings.WaveZ.getOffset(n, renderTime)
            );

            currentPos = currentPos.add(direction);
        }

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

        ProceduralRibbonGenerator.renderQuadBridge(bufferBuilder, firstSegment, (Vec3d[])segments.get(0), settings.color);

        for (n = 0; n < settings.maxSegments - 1; ++n) {
            segmentedVertices = (Vec3d[])segments.get(n); //current segment
            Vec3d[] nextSegment = (Vec3d[])segments.get(n + 1);
            ProceduralRibbonGenerator.renderQuadBridge(bufferBuilder, segmentedVertices, nextSegment, settings.color);
        }

        tessellator.draw();
    }

    static float CalculateWave(float time, float speed, float wavelength, int segmentedIndex, float Amplitude) {
        return (float)(Math.sin(time * speed + wavelength * (float)segmentedIndex) * (double)Amplitude);
    }

    static void renderQuadBridge(BufferBuilder buf, Vec3d[] segA, Vec3d[] segB, ColorRGBA col) {
        buf.pos(segA[1].x, segA[1].y, segA[1].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[2].x, segA[2].y, segA[2].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[2].x, segB[2].y, segB[2].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[1].x, segB[1].y, segB[1].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[0].x, segA[0].y, segA[0].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[1].x, segA[1].y, segA[1].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[1].x, segB[1].y, segB[1].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[0].x, segB[0].y, segB[0].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[2].x, segA[2].y, segA[2].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[3].x, segA[3].y, segA[3].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[3].x, segB[3].y, segB[3].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[2].x, segB[2].y, segB[2].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[0].x, segA[0].y, segA[0].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segA[3].x, segA[3].y, segA[3].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[3].x, segB[3].y, segB[3].z).color(col.r, col.g, col.b, col.a).endVertex();
        buf.pos(segB[0].x, segB[0].y, segB[0].z).color(col.r, col.g, col.b, col.a).endVertex();
    }

    public static class RibbonSettings {
        public ColorRGBA color;
        public float initialOffset;
        public int maxSegments;
        public float LengthStep;
        public WaveController waveX;
        public WaveController WaveY;
        public WaveController WaveZ;
        public float width;
        public float height;

        public RibbonSettings(ColorRGBA col, float offset, int segments, float step, WaveController x, WaveController y, WaveController z, float w, float h) {
            this.color = col;
            this.initialOffset = offset;
            this.maxSegments = segments;
            this.LengthStep = step;
            this.waveX = x;
            this.WaveY = y;
            this.WaveZ = z;
            this.width = w;
            this.height = h;
        }

        public RibbonSettings getSettings() {
            return new RibbonSettings(this.color, this.initialOffset, this.maxSegments, this.LengthStep, this.waveX, this.WaveY, this.WaveZ, this.width, this.height);
        }

        public Object clone() throws CloneNotSupportedException {
            return this.getSettings();
        }
    }

    @FunctionalInterface
    public static interface WaveController {
        public float getOffset(int var1, float var2);
    }
}

