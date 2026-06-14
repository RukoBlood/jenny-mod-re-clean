/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Matrix4f
 *  org.lwjgl.BufferUtils
 */
package com.trolmastercard.sexmod.util;

import java.nio.FloatBuffer;
import javax.vecmath.Matrix4f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.util.MatrixStack;

public class GeckoMatrixBridge {
    final static public float[] MATRIX_ARRAY = new float[16];
    final static public FloatBuffer GL_MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);
    final static private Matrix4f TEMP_MATRIX = new Matrix4f();

    public static void bindOpenGLToBone(MatrixStack matrixStack, GeoBone geoBone) {
        TEMP_MATRIX.set(matrixStack.getModelMatrix());
        TEMP_MATRIX.transpose();
        GeckoMatrixBridge.serializeMatrix(MATRIX_ARRAY, TEMP_MATRIX);
        GL_MATRIX_BUFFER.clear();
        GL_MATRIX_BUFFER.put(MATRIX_ARRAY);
        GL_MATRIX_BUFFER.flip();
        GlStateManager.multMatrix(GL_MATRIX_BUFFER);
        GlStateManager.translate(
                geoBone.rotationPointX / 16.0f,
                geoBone.rotationPointY / 16.0f,
                geoBone.rotationPointZ / 16.0f
        );
    }

    public static void serializeMatrix(float[] targetArray, Matrix4f matrix) {
        targetArray[0] = matrix.m00;
        targetArray[1] = matrix.m01;
        targetArray[2] = matrix.m02;
        targetArray[3] = matrix.m03;

        targetArray[4] = matrix.m10;
        targetArray[5] = matrix.m11;
        targetArray[6] = matrix.m12;
        targetArray[7] = matrix.m13;

        targetArray[8] = matrix.m20;
        targetArray[9] = matrix.m21;
        targetArray[10] = matrix.m22;
        targetArray[11] = matrix.m23;

        targetArray[12] = matrix.m30;
        targetArray[13] = matrix.m31;
        targetArray[14] = matrix.m32;
        targetArray[15] = matrix.m33;
    }

    public static Matrix4f MultiplyMatrices(Matrix4f base, Matrix4f mul) {
        Matrix4f result = (Matrix4f)mul.clone();
        result.mul(base);
        return result;
    }
}

