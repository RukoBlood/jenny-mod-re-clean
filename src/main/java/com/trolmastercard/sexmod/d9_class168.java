/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Tuple4f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class d9_class168
extends PlayerGirlRenderer {
    final static protected Vec3i z = new Vec3i(255, 255, 255);
    static HashMap<Integer, Vec3i> A = new HashMap();

    public d9_class168(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    public static void e() {
        A.clear();
    }

    protected Vec3i a(GeoBone geoBone) {
        String string = geoBone.getName();
        int n = string.hashCode() + this.j.getPersistentID().hashCode();
        Vec3i vec3i = A.get(n);
        if (vec3i != null) {
            return vec3i;
        }
        vec3i = this.net_minecraft_util_math_Vec3i_a(string);
        A.put(n, vec3i);
        return vec3i;
    }

    protected abstract Vec3i net_minecraft_util_math_Vec3i_a(String var1);

    protected void b(GeoBone geoBone, int n) {
        List<GeoBone> list = geoBone.childBones;
        for (int i = 0; i < list.size(); ++i) {
            GeoBone geoBone2 = list.get(i);
            if (n != i) continue;
            GeoBone geoBone3 = geoBone2;
            geoBone3.setHidden(false);
            return;
        }
    }

    protected float float_a() {
        return 1.0f;
    }

    protected Vec3d net_minecraft_util_math_Vec3d_a(ItemStack itemStack) {
        return new Vec3d(-90.0, 0.0, 0.0);
    }

    protected GeoBone a(GeoBone geoBone, int n) {
        List<GeoBone> list = geoBone.childBones;
        GeoBone geoBone2 = null;
        list.sort(Comparator.comparingDouble(GeoBone::getPivotY));
        for (int i = 0; i < list.size(); ++i) {
            GeoBone geoBone3 = list.get(i);
            if (n == i) {
                geoBone2 = geoBone3;
                geoBone2.setHidden(false);
                continue;
            }
            geoBone3.setHidden(true);
        }
        return geoBone2;
    }

    protected Vec3i a(Vec3i vec3i) {
        return vec3i;
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        //ItemStack itemStack;
        String string = geoBone.getName();
        if (this.r) {
            if (string.equals("upperBody")) {
                geoBone.setRotationX(geoBone.getRotationX() - 0.5f);
            }
            if (string.equals("head")) {
                geoBone.setRotationX(geoBone.getRotationX() + 0.5f);
            }
            if (string.equals("legL") || string.equals("legR")) {
                geoBone.setPositionZ(geoBone.getPositionZ() + 1.0f);
            }
        }
        if (string.equals("head")) {
            this.a(bufferBuilder, geoBone, Color.ofRGB(f, f2, f3));
        }
        this.a(string, geoBone);
        this.a(string, geoBone, this.w, bufferBuilder);
        if (this.u && (this.s.getItem() instanceof ItemBow || this.x.getItem() instanceof ItemBow)) {
            if (string.equals("armR")) {
                geoBone.setRotationX(geoBone.getRotationX() - this.j.rotationPitch / 50.0f);
            }
            if (string.equals("armL")) {
                geoBone.setRotationY(geoBone.getRotationY() - this.j.rotationPitch / 50.0f);
            }
            if (this.x.getItem() instanceof ItemBow) {
                ItemStack itemStack = this.x;
                this.x = this.s;
                this.s = itemStack;
            }
        }
        if (this.u && this.s.getItem() instanceof ItemShield) {
            if (string.equals("armR")) {
                geoBone.setRotationZ(0.0f);
                geoBone.setRotationX(0.5f);
            } else if (this.x.getItem() instanceof ItemShield && string.equals("armL")) {
                geoBone.setRotationZ(0.0f);
                geoBone.setRotationX(0.5f);
            }
        }
        if (string.equals("weapon") && !this.s.isEmpty()) {
            this.a(bufferBuilder, geoBone, false);
        }
        if (string.equals("offhand") && !this.x.isEmpty()) {
            this.a(bufferBuilder, geoBone, true);
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        if ("Head2".equals(string) && !this.boolean_c()) {
            MATRIX_STACK.pop();
            return;
        }
        if (("neck".equals(string) || "head".equals(string)) && !this.boolean_a()) {
            MATRIX_STACK.pop();
            return;
        }
        if (!geoBone.isHidden) {
            Vector4f vec4f = this.a(string, f, f2, f3);
            f = ((Vector4f)vec4f).x;
            f2 = ((Vector4f)vec4f).y;
            f3 = ((Vector4f)vec4f).z;
            double d = ((Vector4f)vec4f).w;
            if (!this.p.contains(string)) {
                for (GeoCube object : geoBone.childCubes) {
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.q = geoBone;
                    this.a(bufferBuilder, object, geoBone, f, f2, f3, f4, d);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }
            for (GeoBone geoBone2 : geoBone.childBones) {
                if (d == 0.0) {
                    this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
                    continue;
                }
                this.a(bufferBuilder, geoBone2, f, f2, f3, f4, d);
            }
        }
        try {
            MATRIX_STACK.pop();
        } catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
    }

    public void a(BufferBuilder bufferBuilder, GeoCube geoCube, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
        MATRIX_STACK.moveToPivot(geoCube);
        MATRIX_STACK.rotate(geoCube);
        MATRIX_STACK.moveBackFromPivot(geoCube);
        for (GeoQuad geoQuad : geoCube.quads) {
            Vec3d vec3d;
            if (geoQuad == null) continue;
            Vector3f vector3f = new Vector3f((float)geoQuad.normal.getX(), (float)geoQuad.normal.getY(), (float)geoQuad.normal.getZ());
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
            if (this.c(geoBone.getName())) {
                vec3d = new Vec3d(f, f2, f3);
            } else {
                Vec3i geoVertexArray = this.a(geoBone);
                geoVertexArray = this.a((Vec3i)geoVertexArray);
                vec3d = gx_class390.a(this, geoBone, new Vec3d((float)geoVertexArray.getX() / 255.0f, (float)geoVertexArray.getY() / 255.0f, (float)geoVertexArray.getZ() / 255.0f), vector3f);
            }
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex((double)geoVertex.textureU + d, geoVertex.textureV).color((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, f4).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }

    protected boolean c(String string) {
        return string.startsWith("armor");
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

