/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Matrix4f
 *  org.lwjgl.BufferUtils
 */
package com.trolmastercard.sexmod;

import java.nio.FloatBuffer;
import javax.vecmath.Matrix4f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.util.MatrixStack;

public class p_class418 {
    final static public float[] b = new float[16];
    final static public FloatBuffer c = BufferUtils.createFloatBuffer(16);
    final static private Matrix4f a = new Matrix4f();

    public static void a(MatrixStack matrixStack, GeoBone geoBone) {
        a.set(matrixStack.getModelMatrix());
        a.transpose();
        p_class418.a(b, a);
        c.clear();
        c.put(b);
        c.flip();
        GlStateManager.multMatrix(c);
        GlStateManager.translate(geoBone.rotationPointX / 16.0f, geoBone.rotationPointY / 16.0f, geoBone.rotationPointZ / 16.0f);
    }

    public static void a(float[] fArray, Matrix4f matrix4f) {
        fArray[0] = matrix4f.m00;
        fArray[1] = matrix4f.m01;
        fArray[2] = matrix4f.m02;
        fArray[3] = matrix4f.m03;
        fArray[4] = matrix4f.m10;
        fArray[5] = matrix4f.m11;
        fArray[6] = matrix4f.m12;
        fArray[7] = matrix4f.m13;
        fArray[8] = matrix4f.m20;
        fArray[9] = matrix4f.m21;
        fArray[10] = matrix4f.m22;
        fArray[11] = matrix4f.m23;
        fArray[12] = matrix4f.m30;
        fArray[13] = matrix4f.m31;
        fArray[14] = matrix4f.m32;
        fArray[15] = matrix4f.m33;
    }

    public static Matrix4f a(Matrix4f matrix4f, Matrix4f matrix4f2) {
        Matrix4f matrix4f3 = (Matrix4f)matrix4f2.clone();
        matrix4f3.mul(matrix4f);
        return matrix4f3;
    }
}

