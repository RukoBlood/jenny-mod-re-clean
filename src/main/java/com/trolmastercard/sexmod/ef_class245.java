/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.ck_class135;
import com.trolmastercard.sexmod.gv_class388;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class ef_class245 {
    public static void a(BufferBuilder bufferBuilder, Tessellator tessellator, Minecraft minecraft, b_inner247 b_inner2472) {
        Vec3d[] vec3dArray;
        int n;
        Vec3d[] vec3dArray2 = new Vec3d[]{new Vec3d(-b_inner2472.f, -b_inner2472.a, 0.0), new Vec3d(-b_inner2472.f, b_inner2472.a, 0.0), new Vec3d(b_inner2472.f, b_inner2472.a, 0.0), new Vec3d(b_inner2472.f, -b_inner2472.a, 0.0)};
        Vec3d vec3d = new Vec3d(0.0, 0.0, -b_inner2472.g);
        Vec3d vec3d2 = ck_class135.a(vec3d.normalize(), (double)b_inner2472.e);
        Vec3d[] vec3dArray3 = new Vec3d[4];
        System.arraycopy(vec3dArray2, 0, vec3dArray3, 0, 4);
        ArrayList<Vec3d[]> arrayList = new ArrayList<Vec3d[]>();
        float f = (float)minecraft.player.ticksExisted + minecraft.getRenderPartialTicks();
        for (n = 0; n <= b_inner2472.c; ++n) {
            vec3dArray = new Vec3d[4];
            float f2 = 1.0f - (float)n / (float)b_inner2472.c;
            for (int i = 0; i < 4; ++i) {
                Vec3d vec3d3 = vec3dArray2[i];
                vec3dArray[i] = new Vec3d(vec3d3.x * (double)f2, vec3d3.y, vec3d3.z).add(vec3d2);
            }
            arrayList.add(vec3dArray);
            vec3d = ck_class135.a(vec3d, b_inner2472.i.a(n, f), b_inner2472.b.a(n, f), b_inner2472.d.a(n, f));
            vec3d2 = vec3d2.add(vec3d);
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        ef_class245.a(bufferBuilder, vec3dArray3, (Vec3d[])arrayList.get(0), b_inner2472.h);
        for (n = 0; n < b_inner2472.c - 1; ++n) {
            vec3dArray = (Vec3d[])arrayList.get(n);
            Vec3d[] vec3dArray4 = (Vec3d[])arrayList.get(n + 1);
            ef_class245.a(bufferBuilder, vec3dArray, vec3dArray4, b_inner2472.h);
        }
        tessellator.draw();
    }

    static float a(float f, float f2, float f3, int n, float f4) {
        return (float)(Math.sin(f * f2 + f3 * (float)n) * (double)f4);
    }

    static void a(BufferBuilder bufferBuilder, Vec3d[] vec3dArray, Vec3d[] vec3dArray2, gv_class388 gv_class3882) {
        bufferBuilder.pos(vec3dArray[1].x, vec3dArray[1].y, vec3dArray[1].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[2].x, vec3dArray[2].y, vec3dArray[2].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[2].x, vec3dArray2[2].y, vec3dArray2[2].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[1].x, vec3dArray2[1].y, vec3dArray2[1].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[0].x, vec3dArray[0].y, vec3dArray[0].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[1].x, vec3dArray[1].y, vec3dArray[1].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[1].x, vec3dArray2[1].y, vec3dArray2[1].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[0].x, vec3dArray2[0].y, vec3dArray2[0].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[2].x, vec3dArray[2].y, vec3dArray[2].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[3].x, vec3dArray[3].y, vec3dArray[3].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[3].x, vec3dArray2[3].y, vec3dArray2[3].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[2].x, vec3dArray2[2].y, vec3dArray2[2].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[0].x, vec3dArray[0].y, vec3dArray[0].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray[3].x, vec3dArray[3].y, vec3dArray[3].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[3].x, vec3dArray2[3].y, vec3dArray2[3].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3dArray2[0].x, vec3dArray2[0].y, vec3dArray2[0].z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
    }

    public static class b_inner247 {
        public gv_class388 h;
        public float e;
        public int c;
        public float g;
        public a_inner246 i;
        public a_inner246 b;
        public a_inner246 d;
        public float f;
        public float a;

        public b_inner247(gv_class388 gv_class3882, float f, int n, float f2, a_inner246 a_inner2462, a_inner246 a_inner2463, a_inner246 a_inner2464, float f3, float f4) {
            this.h = gv_class3882;
            this.e = f;
            this.c = n;
            this.g = f2;
            this.i = a_inner2462;
            this.b = a_inner2463;
            this.d = a_inner2464;
            this.f = f3;
            this.a = f4;
        }

        public b_inner247 a() {
            return new b_inner247(this.h, this.e, this.c, this.g, this.i, this.b, this.d, this.f, this.a);
        }

        public Object clone() throws CloneNotSupportedException {
            return this.a();
        }
    }

    @FunctionalInterface
    public static interface a_inner246 {
        public float a(int var1, float var2);
    }
}

