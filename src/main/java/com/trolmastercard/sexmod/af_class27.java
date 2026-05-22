/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.processor.IBone;

public class af_class27 {
    public static Vec3d[][] a(GirlEntity em_class2582, float f, String string, String string2, String string3, float f2, float f3, float f4, float f5, String string4) {
        Vec3d[] vec3dArray = af_class27.b(em_class2582, f, string, string2, string3, f2, f3, f4, f5, string4);
        return af_class27.a(vec3dArray);
    }

    public static Vec3d[][] a(GirlEntity em_class2582, float f, String string, String string2, f7_class292 f7_class2922, f7_class292 f7_class2923) {
        Vec3d[] vec3dArray = af_class27.b(em_class2582, f, string, string2, f7_class2922, f7_class2923);
        return af_class27.b(vec3dArray);
    }

    static Vec3d[] b(GirlEntity em_class2582, float f, String string, String string2, f7_class292 f7_class2922, f7_class292 f7_class2923) {
        int n;
        Vec3d vec3d = em_class2582.b(string);
        Vec3d vec3d2 = em_class2582.b(string2);
        Vec3d[] vec3dArray = new Vec3d[8];
        if (f7_class2922.a == 0.0f && f7_class2923.a == 0.0f) {
            vec3dArray[0] = new Vec3d(0.0, f7_class2922.c, f7_class2922.b);
            vec3dArray[1] = new Vec3d(0.0, -f7_class2922.c, f7_class2922.b);
            vec3dArray[2] = new Vec3d(0.0, -f7_class2922.c, -f7_class2922.b);
            vec3dArray[3] = new Vec3d(0.0, f7_class2922.c, -f7_class2922.b);
            vec3dArray[4] = new Vec3d(0.0, f7_class2923.c, f7_class2923.b);
            vec3dArray[5] = new Vec3d(0.0, -f7_class2923.c, f7_class2923.b);
            vec3dArray[6] = new Vec3d(0.0, -f7_class2923.c, -f7_class2923.b);
            vec3dArray[7] = new Vec3d(0.0, f7_class2923.c, -f7_class2923.b);
        } else {
            vec3dArray[0] = new Vec3d(f7_class2922.a, f7_class2922.c, 0.0);
            vec3dArray[1] = new Vec3d(-f7_class2922.a, f7_class2922.c, 0.0);
            vec3dArray[2] = new Vec3d(-f7_class2922.a, -f7_class2922.c, 0.0);
            vec3dArray[3] = new Vec3d(f7_class2922.a, -f7_class2922.c, 0.0);
            vec3dArray[4] = new Vec3d(f7_class2923.a, f7_class2923.c, 0.0);
            vec3dArray[5] = new Vec3d(-f7_class2923.a, f7_class2923.c, 0.0);
            vec3dArray[6] = new Vec3d(-f7_class2923.a, -f7_class2923.c, 0.0);
            vec3dArray[7] = new Vec3d(f7_class2923.a, -f7_class2923.c, 0.0);
        }
        for (n = 0; n < vec3dArray.length; ++n) {
            vec3dArray[n] = ck_class135.a(vec3dArray[n], f);
        }
        for (n = 0; n < 4; ++n) {
            vec3dArray[n] = vec3dArray[n].add(vec3d);
        }
        for (n = 4; n < 8; ++n) {
            vec3dArray[n] = vec3dArray[n].add(vec3d2);
        }
        return vec3dArray;
    }

    static Vec3d[][] b(Vec3d[] vec3dArray) {
        Vec3d[][] vec3dArray2 = new Vec3d[6][4];
        vec3dArray2[0][0] = vec3dArray[0];
        vec3dArray2[0][1] = vec3dArray[1];
        vec3dArray2[0][2] = vec3dArray[2];
        vec3dArray2[0][3] = vec3dArray[3];
        vec3dArray2[1][0] = vec3dArray[4];
        vec3dArray2[1][1] = vec3dArray[5];
        vec3dArray2[1][2] = vec3dArray[6];
        vec3dArray2[1][3] = vec3dArray[7];
        vec3dArray2[2][0] = vec3dArray[1];
        vec3dArray2[2][1] = vec3dArray[2];
        vec3dArray2[2][2] = vec3dArray[6];
        vec3dArray2[2][3] = vec3dArray[5];
        vec3dArray2[3][0] = vec3dArray[3];
        vec3dArray2[3][1] = vec3dArray[7];
        vec3dArray2[3][2] = vec3dArray[4];
        vec3dArray2[3][3] = vec3dArray[0];
        vec3dArray2[4][0] = vec3dArray[1];
        vec3dArray2[4][1] = vec3dArray[0];
        vec3dArray2[4][2] = vec3dArray[4];
        vec3dArray2[4][3] = vec3dArray[5];
        vec3dArray2[5][0] = vec3dArray[2];
        vec3dArray2[5][1] = vec3dArray[3];
        vec3dArray2[5][2] = vec3dArray[7];
        vec3dArray2[5][3] = vec3dArray[6];
        return vec3dArray2;
    }

    static Vec3d[] b(GirlEntity em_class2582, float f, String string, String string2, String string3, float f2, float f3, float f4, float f5, String string4) {
        int n;
        IBone iBone = em_class2582.b().getBone(string4);
        if (iBone == null) {
            Vec3d[] objectArray = new Vec3d[12];
            Arrays.fill(objectArray, Vec3d.ZERO);
            return objectArray;
        }
        float f6 = gc_class360.d(iBone.getRotationY());
        float f7 = gc_class360.d(iBone.getRotationZ());
        Vec3d vec3d = em_class2582.b(string);
        Vec3d vec3d2 = em_class2582.b(string2);
        Vec3d vec3d3 = em_class2582.b(string3);
        Vec3d[] vec3dArray = new Vec3d[]{new Vec3d(f2, 0.0, -f3), new Vec3d(-f2, 0.0, -f3), new Vec3d(-f2, 0.0, f3), new Vec3d(f2, 0.0, f3), new Vec3d(f2, f3, 0.0), new Vec3d(-f2, f3, 0.0), new Vec3d(-f2, -f3, 0.0), new Vec3d(f2, -f3, 0.0), new Vec3d(f4, 0.0, -f5), new Vec3d(-f4, 0.0, -f5), new Vec3d(-f4, 0.0, f5), new Vec3d(f4, 0.0, f5)};
        for (n = 0; n < vec3dArray.length; ++n) {
            vec3dArray[n] = ck_class135.a(vec3dArray[n], f);
        }
        for (n = 0; n < 4; ++n) {
            vec3dArray[n] = ck_class135.a(vec3dArray[n], 0.0f, f6, f7);
        }
        for (n = 0; n < 4; ++n) {
            vec3dArray[n] = vec3dArray[n].add(vec3d);
        }
        for (n = 4; n < 8; ++n) {
            vec3dArray[n] = vec3dArray[n].add(vec3d2);
        }
        for (n = 8; n < 12; ++n) {
            vec3dArray[n] = vec3dArray[n].add(vec3d3);
        }
        return vec3dArray;
    }

    static Vec3d[][] a(Vec3d[] vec3dArray) {
        Vec3d[][] vec3dArray2 = new Vec3d[10][4];
        vec3dArray2[0][0] = vec3dArray[0];
        vec3dArray2[0][1] = vec3dArray[1];
        vec3dArray2[0][2] = vec3dArray[5];
        vec3dArray2[0][3] = vec3dArray[4];
        vec3dArray2[1][0] = vec3dArray[1];
        vec3dArray2[1][1] = vec3dArray[2];
        vec3dArray2[1][2] = vec3dArray[6];
        vec3dArray2[1][3] = vec3dArray[5];
        vec3dArray2[2][0] = vec3dArray[3];
        vec3dArray2[2][1] = vec3dArray[2];
        vec3dArray2[2][2] = vec3dArray[6];
        vec3dArray2[2][3] = vec3dArray[7];
        vec3dArray2[3][0] = vec3dArray[0];
        vec3dArray2[3][1] = vec3dArray[4];
        vec3dArray2[3][2] = vec3dArray[7];
        vec3dArray2[3][3] = vec3dArray[3];
        vec3dArray2[4][0] = vec3dArray[0];
        vec3dArray2[4][1] = vec3dArray[1];
        vec3dArray2[4][2] = vec3dArray[2];
        vec3dArray2[4][3] = vec3dArray[3];
        vec3dArray2[5][0] = vec3dArray[4];
        vec3dArray2[5][1] = vec3dArray[5];
        vec3dArray2[5][2] = vec3dArray[9];
        vec3dArray2[5][3] = vec3dArray[8];
        vec3dArray2[6][0] = vec3dArray[9];
        vec3dArray2[6][1] = vec3dArray[10];
        vec3dArray2[6][2] = vec3dArray[6];
        vec3dArray2[6][3] = vec3dArray[5];
        vec3dArray2[7][0] = vec3dArray[10];
        vec3dArray2[7][1] = vec3dArray[11];
        vec3dArray2[7][2] = vec3dArray[7];
        vec3dArray2[7][3] = vec3dArray[6];
        vec3dArray2[8][0] = vec3dArray[4];
        vec3dArray2[8][1] = vec3dArray[7];
        vec3dArray2[8][2] = vec3dArray[11];
        vec3dArray2[8][3] = vec3dArray[8];
        vec3dArray2[9][0] = vec3dArray[8];
        vec3dArray2[9][1] = vec3dArray[9];
        vec3dArray2[9][2] = vec3dArray[10];
        vec3dArray2[9][3] = vec3dArray[11];
        return vec3dArray2;
    }

    public static void a(BufferBuilder bufferBuilder, Vec3d[][] vec3dArray, gv_class388 gv_class3882) {
        Vec3d[][] vec3dArray2 = vec3dArray;
        int n = vec3dArray2.length;
        for (int i = 0; i < n; ++i) {
            Vec3d[] vec3dArray3;
            for (Vec3d vec3d : vec3dArray3 = vec3dArray2[i]) {
                bufferBuilder.pos(vec3d.x, vec3d.y, vec3d.z).tex(0.0, 0.0).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
            }
        }
    }

    public static void a(Minecraft minecraft, GirlEntity em_class2582, float f) {
        EntityPlayerSP entityPlayerSP = minecraft.player;
        if (entityPlayerSP == null) {
            return;
        }
        GlStateManager.translate(0.0, 0.01, 0.0);
        Entity entity = ((GirlRenderer)minecraft.getRenderManager().getEntityRenderObject(em_class2582)).c(em_class2582);
        Vec3d vec3d = em_class2582.boolean_Q() ? em_class2582.net_minecraft_util_math_Vec3d_o() : b6_class67.a(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)f);
        Vec3d vec3d2 = b6_class67.a(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        vec3d3 = em_class2582.net_minecraft_util_math_Vec3d_a(vec3d3, f);
        GlStateManager.translate(vec3d3.x, vec3d3.y, vec3d3.z);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

