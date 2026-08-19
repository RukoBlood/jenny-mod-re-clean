/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Tuple4f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.base.PlayerGirl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.BoneDeformProcessor;
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
//d9.class
// * Used by PlayerKoboldRenderer and PlayerGoblinRenderer.
public abstract class WorkerPlayerRenderer extends PlayerGirlRenderer {
    final static protected Vec3i DEFAULT_COLOR = new Vec3i(255, 255, 255);
    static HashMap<Integer, Vec3i> colorCache = new HashMap();

    public WorkerPlayerRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    public static void ResetColors() {
        colorCache.clear();
    }

    protected Vec3i GetBoneColor(GeoBone bone) {
        String boneName = bone.getName();
        int key = boneName.hashCode() + this.renderEntity.getPersistentID().hashCode();
        Vec3i cachedColor = colorCache.get(key);
        if (cachedColor != null) {
            return cachedColor;
        }
        cachedColor = this.resolveBoneColor(boneName);
        colorCache.put(key, cachedColor);
        return cachedColor;
    }

    protected abstract Vec3i resolveBoneColor(String name);

    protected void ShowChildBoneByIndex(GeoBone parent, int index) {
        List<GeoBone> childBones = parent.childBones;
        for (int i = 0; i < childBones.size(); ++i) {
            GeoBone child = childBones.get(i);
            if (index != i) continue;
            GeoBone child1 = child;
            child1.setHidden(false);
            return;
        }
    }

    protected float GetItemRenderScale() {
        return 1.0f;
    }

    protected Vec3d GetItemRenderLocation(ItemStack stack) {
        return new Vec3d(-90.0, 0.0, 0.0);
    }

    protected GeoBone selectAndShowExclusiveChildBone(GeoBone parent, int targetIDX) {
        List<GeoBone> list = parent.childBones;
        GeoBone geoBone2 = null;
        list.sort(Comparator.comparingDouble(GeoBone::getPivotY));
        for (int i = 0; i < list.size(); ++i) {
            GeoBone geoBone3 = list.get(i);
            if (targetIDX == i) {
                geoBone2 = geoBone3;
                geoBone2.setHidden(false);
                continue;
            }
            geoBone3.setHidden(true);
        }
        return geoBone2;
    }

    protected Vec3i filterFinalColor(Vec3i vec3i) {
        return vec3i;
    }

    @Override
    public void renderRecursively(BufferBuilder buffer, GeoBone bone, float red, float green, float blue, float alpha) {
        //ItemStack itemStack;
        String Bone = bone.getName();
        if (this.isSneaking) {
            if (Bone.equals("upperBody")) {
                bone.setRotationX(bone.getRotationX() - 0.5f);
            }
            if (Bone.equals("head")) {
                bone.setRotationX(bone.getRotationX() + 0.5f);
            }
            if (Bone.equals("legL") || Bone.equals("legR")) {
                bone.setPositionZ(bone.getPositionZ() + 1.0f);
            }
        }
        if (Bone.equals("head")) {
            this.renderOverlay(buffer, bone, Color.ofRGB(red, green, blue));
        }
        this.onBoneRenderStart(Bone, bone);
        this.onBoneRenderingLayer(Bone, bone, this.currentGirl, buffer);
        if (this.isUsingItem && (this.mainHandItem.getItem() instanceof ItemBow || this.offHandItem.getItem() instanceof ItemBow)) {
            if (Bone.equals("armR")) {
                bone.setRotationX(bone.getRotationX() - this.renderEntity.rotationPitch / 50.0f);
            }
            if (Bone.equals("armL")) {
                bone.setRotationY(bone.getRotationY() - this.renderEntity.rotationPitch / 50.0f);
            }
            if (this.offHandItem.getItem() instanceof ItemBow) {
                ItemStack itemStack = this.offHandItem;
                this.offHandItem = this.mainHandItem;
                this.mainHandItem = itemStack;
            }
        }
        if (this.isUsingItem && this.mainHandItem.getItem() instanceof ItemShield) {
            if (Bone.equals("armR")) {
                bone.setRotationZ(0.0f);
                bone.setRotationX(0.5f);
            } else if (this.offHandItem.getItem() instanceof ItemShield && Bone.equals("armL")) {
                bone.setRotationZ(0.0f);
                bone.setRotationX(0.5f);
            }
        }
        if (Bone.equals("weapon") && !this.mainHandItem.isEmpty()) {
            this.renderEquippedItem(buffer, bone, false);
        }
        if (Bone.equals("offhand") && !this.offHandItem.isEmpty()) {
            this.renderEquippedItem(buffer, bone, true);
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);
        if ("Head2".equals(Bone) && !this.boolean_c()) {
            MATRIX_STACK.pop();
            return;
        }
        if (("neck".equals(Bone) || "head".equals(Bone)) && this.shouldHideHeadInFirstPerson()) {
            MATRIX_STACK.pop();
            return;
        }
        if (!bone.isHidden) {
            Vector4f vec4f = this.calculateBoneArmorColor(Bone, red, green, blue);
            red = ((Vector4f) vec4f).x;
            green = ((Vector4f) vec4f).y;
            blue = ((Vector4f) vec4f).z;
            double d = ((Vector4f) vec4f).w;
            if (!this.activeCustomPartBones.contains(Bone)) {
                for (GeoCube object : bone.childCubes) {
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.currentRenderingBone = bone;
                    this.renderCubeGeometry(buffer, object, bone, red, green, blue, alpha, d);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }
            for (GeoBone geoBone2 : bone.childBones) {
                if (d == 0.0) {
                    this.renderRecursively(buffer, geoBone2, red, green, blue, alpha);
                    continue;
                }
                this.renderCustomBones(buffer, geoBone2, red, green, blue, alpha, d);
            }
        }
        MATRIX_STACK.pop();
    }

    public void renderCubeGeometry(BufferBuilder bufferBuilder, GeoCube geoCube, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
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
                Vec3i geoVertexArray = this.GetBoneColor(geoBone);
                geoVertexArray = this.filterFinalColor((Vec3i)geoVertexArray);
                vec3d = BoneDeformProcessor.applyBoneDeformation(this, geoBone, new Vec3d((float)geoVertexArray.getX() / 255.0f, (float)geoVertexArray.getY() / 255.0f, (float)geoVertexArray.getZ() / 255.0f), vector3f);
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

}

