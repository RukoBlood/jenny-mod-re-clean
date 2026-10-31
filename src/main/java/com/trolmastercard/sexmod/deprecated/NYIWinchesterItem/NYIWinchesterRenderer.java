/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Tuple4f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4f
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.deprecated.NYIWinchesterItem;

import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.util.anim.BoneDeformProcessor;
import com.trolmastercard.sexmod.events.DebugMode;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class NYIWinchesterRenderer extends GeoItemRenderer<NYIWinchesterItem> {
    final static Vec3d offset = new Vec3d(0.0, 1.0, 0.0);

    public NYIWinchesterRenderer() {
        super(new NYIWinchesterModel());
    }

    //a
    @Override
    public void render(NYIWinchesterItem item, ItemStack stack) {
        if (DebugMode.devDebugFloats[0] == 0.0f) {
            GL11.glDisable(2896);
        }
        super.render(item, stack);
        GL11.glEnable(2896);
    }

    @Override
    public void renderCube(BufferBuilder buffer, GeoCube cube, float red, float green, float blue, float alpha) {
        MATRIX_STACK.moveToPivot(cube);
        MATRIX_STACK.rotate(cube);
        MATRIX_STACK.moveBackFromPivot(cube);
        for (GeoQuad quad : cube.quads) {
            if (quad == null) continue;
            Vector3f normalVec = new Vector3f((float)quad.normal.getX(), (float)quad.normal.getY(), (float)quad.normal.getZ());
            MATRIX_STACK.getNormalMatrix().transform(normalVec);
            if ((cube.size.y == 0.0f || cube.size.z == 0.0f) && normalVec.getX() < 0.0f) {
                normalVec.x *= -1.0f;
            }
            if ((cube.size.x == 0.0f || cube.size.z == 0.0f) && normalVec.getY() < 0.0f) {
                normalVec.y *= -1.0f;
            }
            if ((cube.size.x == 0.0f || cube.size.y == 0.0f) && normalVec.getZ() < 0.0f) {
                normalVec.z *= -1.0f;
            }
            Vec3d vec3d = DebugMode.devDebugFloats[0] == 0.0f ? BoneDeformProcessor.calculatePhysicsVector(new Vec3d(red, green, blue), normalVec, offset) : new Vec3d(red, green, blue);
            for (GeoVertex geoVertex : quad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform(vector4f);
                buffer.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex(geoVertex.textureU, geoVertex.textureV).color((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, alpha).normal(normalVec.getX(), normalVec.getY(), normalVec.getZ()).endVertex();
            }
        }
    }

}

