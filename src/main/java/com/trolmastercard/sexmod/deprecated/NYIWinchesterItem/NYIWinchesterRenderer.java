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
    final static Vec3d a = new Vec3d(0.0, 1.0, 0.0);

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
    public void renderCube(BufferBuilder bufferBuilder, GeoCube geoCube, float f, float f2, float f3, float f4) {
        MATRIX_STACK.moveToPivot(geoCube);
        MATRIX_STACK.rotate(geoCube);
        MATRIX_STACK.moveBackFromPivot(geoCube);
        for (GeoQuad quad : geoCube.quads) {
            if (quad == null) continue;
            Vector3f vector3f = new Vector3f((float)quad.normal.getX(), (float)quad.normal.getY(), (float)quad.normal.getZ());
            MATRIX_STACK.getNormalMatrix().transform((Tuple3f)vector3f);
            if ((geoCube.size.y == 0.0f || geoCube.size.z == 0.0f) && vector3f.getX() < 0.0f) {
                vector3f.x *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.z == 0.0f) && vector3f.getY() < 0.0f) {
                vector3f.y *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.y == 0.0f) && vector3f.getZ() < 0.0f) {
                vector3f.z *= -1.0f;
            }
            Vec3d vec3d = DebugMode.devDebugFloats[0] == 0.0f ? BoneDeformProcessor.calculatePhysicsVector(new Vec3d(f, f2, f3), vector3f, a) : new Vec3d(f, f2, f3);
            for (GeoVertex geoVertex : quad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex(geoVertex.textureU, geoVertex.textureV).color((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, f4).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }

    //@Override
    //public void render(Item item, ItemStack itemStack) {
    //    this.a((aj_class31)item, itemStack);
    //}
}

