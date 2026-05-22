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
package com.trolmastercard.sexmod;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public abstract class d6_class165<G extends e4_class223>
extends GirlRenderer<G> {
    final static protected Vec3i r = new Vec3i(255, 255, 255);
    static HashMap<Integer, Vec3i> s = new HashMap();

    public d6_class165(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    public static void c() {
        s.clear();
    }

    protected Vec3i a(GeoBone geoBone) {
        String string = geoBone.getName();
        int n = string.hashCode() + ((e4_class223)this.j).getPersistentID().hashCode();
        Vec3i vec3i = s.get(n);
        if (vec3i != null) {
            return vec3i;
        }
        vec3i = this.a(string);
        s.put(n, vec3i);
        return vec3i;
    }

    protected abstract Vec3i a(String var1);

    protected static void b(GeoBone geoBone, int n) {
        List<GeoBone> list = geoBone.childBones;
        for (int i = 0; i < list.size(); ++i) {
            GeoBone geoBone2 = list.get(i);
            if (n != i) continue;
            GeoBone geoBone3 = geoBone2;
            geoBone3.setHidden(false);
            return;
        }
    }

    @Override
    protected void a(BufferBuilder bufferBuilder, GeoBone geoBone) {
        ItemStack itemStack = this.net_minecraft_item_ItemStack_a((ItemStack)null);
        float f = this.a__();
        Vec3d vec3d = this.a(itemStack);
        if (itemStack == null) {
            return;
        }
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        p_class418.a(IGeoRenderer.MATRIX_STACK, geoBone);
        GL11.glEnable(2896);
        GlStateManager.scale(f, f, f);
        GlStateManager.rotate((float)vec3d.x, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((float)vec3d.y, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)vec3d.z, 0.0f, 0.0f, 1.0f);
        Minecraft.getMinecraft().getItemRenderer().renderItem(this.j, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
    }

    protected float a__() {
        return 1.0f;
    }

    protected Vec3d a(ItemStack itemStack) {
        return new Vec3d(-90.0, 0.0, 0.0);
    }

    protected static GeoBone a(GeoBone geoBone, int n) {
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
    public void a(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
        if (((e4_class223)this.j).world instanceof FakeWorld) {
            return;
        }
        String string = geoBone.getName();
        if (string.equals("weapon")) {
            this.a(bufferBuilder, geoBone);
        }
        if (string.equals("itemRenderer") && ((e4_class223)this.j).currentAction() == Action.PAYMENT) {
            this.b(bufferBuilder, geoBone);
        }
        this.a(bufferBuilder, geoBone.getName(), geoBone);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        if (!geoBone.isHidden) {
            for (GeoCube object : geoBone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.q = geoBone;
                this.a(bufferBuilder, object, geoBone, f, f2, f3, f4, d);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
            for (GeoBone geoBone2 : geoBone.childBones) {
                this.a(bufferBuilder, geoBone2, f, f2, f3, f4, d);
            }
        }
        MATRIX_STACK.pop();
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        this.a(bufferBuilder, geoBone, f, f2, f3, f4, 0.0);
    }

    public void a(BufferBuilder bufferBuilder, GeoCube geoCube, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
        MATRIX_STACK.moveToPivot(geoCube);
        MATRIX_STACK.rotate(geoCube);
        MATRIX_STACK.moveBackFromPivot(geoCube);
        for (GeoQuad geoQuad : geoCube.quads) {
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
            Vec3i vec3i = this.a(geoBone);
            vec3i = this.a(vec3i);
            Vec3d vec3d = gx_class390.a(this, geoBone, new Vec3d((float)vec3i.getX() / 255.0f, (float)vec3i.getY() / 255.0f, (float)vec3i.getZ() / 255.0f), vector3f);
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex((double)geoVertex.textureU + d, geoVertex.textureV).color((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, f4).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }

    private static RuntimeException c(RuntimeException runtimeException) {
        return runtimeException;
    }
}

