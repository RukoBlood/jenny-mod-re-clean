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
package com.trolmastercard.sexmod.girls.base;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.BoneDeformProcessor;
import com.trolmastercard.sexmod.util.MatrixHelper;
import com.trolmastercard.sexmod.world.FakeWorld;
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

public abstract class GirlRendererBase<G extends AbstractGoblinKoboldEntity> extends GirlRenderer<G> {
    final static protected Vec3i DEFAULT_COLOR = new Vec3i(255, 255, 255);
    static HashMap<Integer, Vec3i> colorCache = new HashMap();

    public GirlRendererBase(RenderManager renderManager, AnimatedGeoModel modelProvider, double shadowSize) {
        super(renderManager, modelProvider, shadowSize);
    }

    public static void clearBoneColors() {
        colorCache.clear();
    }

    protected Vec3i getBoneColor(GeoBone bone) {
        String boneName = bone.getName();
        int compositeKey = boneName.hashCode() + ((AbstractGoblinKoboldEntity)this.renderEntity).getPersistentID().hashCode();
        Vec3i cachedColor = colorCache.get(compositeKey);
        if (cachedColor != null) {
            return cachedColor;
        }
        cachedColor = this.resolveBoneColor(boneName);
        colorCache.put(compositeKey, cachedColor);
        return cachedColor;
    }

    protected abstract Vec3i resolveBoneColor(String boneName);

    protected static void ShowChildBoneByIndex(GeoBone bone, int index) {
        List<GeoBone> childList = bone.childBones;
        for (int i = 0; i < childList.size(); ++i) {
            GeoBone child = childList.get(i);
            if (index != i) continue;
            child.setHidden(false);
            return;
        }
    }

    @Override
    protected void RenderHeldItem(BufferBuilder buffer, GeoBone bone) {
        ItemStack heldItem = this.getHeldItem((ItemStack)null);
        float scale = this.getRenderItemScale();
        Vec3d rotation = this.getItemRenderRotation(heldItem);
        if (heldItem == null) {
            return;
        }
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        MatrixHelper.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, bone);
        GL11.glEnable(2896);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate((float)rotation.x, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((float)rotation.y, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)rotation.z, 0.0f, 0.0f, 1.0f);
        Minecraft.getMinecraft().getItemRenderer().renderItem(this.renderEntity, heldItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.popMatrix();
    }

    protected float getRenderItemScale() {
        return 1.0f;
    }

    protected Vec3d getItemRenderRotation(ItemStack item) {
        return new Vec3d(-90.0, 0.0, 0.0);
    }

    protected static GeoBone selectAndShowExclusiveChildBone(GeoBone parentBone, int targetIndex) {
        List<GeoBone> childList = parentBone.childBones;
        GeoBone selectedBone = null;
        childList.sort(Comparator.comparingDouble(GeoBone::getPivotY));
        for (int i = 0; i < childList.size(); ++i) {
            GeoBone child = childList.get(i);
            if (targetIndex == i) {
                selectedBone = child;
                selectedBone.setHidden(false);
                continue;
            }
            child.setHidden(true);
        }
        return selectedBone;
    }

    protected Vec3i filterFinalColor(Vec3i inputColor) {
        return inputColor;
    }

    @Override
    public void renderCustomBones(BufferBuilder buffer, GeoBone bone, float r, float g, float b, float a, double uOffset) {
        if (((AbstractGoblinKoboldEntity)this.renderEntity).world instanceof FakeWorld) {
            return;
        }
        String boneName = bone.getName();
        if (boneName.equals("weapon")) {
            this.RenderHeldItem(buffer, bone);
        }
        if (boneName.equals("itemRenderer") && ((AbstractGoblinKoboldEntity)this.renderEntity).getCurrentAction() == Action.PAYMENT) {
            this.renderTradeOverlay(buffer, bone);
        }
        this.onBoneProcessing(buffer, bone.getName(), bone);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);
        if (!bone.isHidden) {
            for (GeoCube cube : bone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.currentRenderingBone = bone;
                this.renderCubeGeometry(buffer, cube, bone, r, g, b, a, uOffset);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
            for (GeoBone childBone : bone.childBones) {
                this.renderCustomBones(buffer, childBone, r, g, b, a, uOffset);
            }
        }
        MATRIX_STACK.pop();
    }

    @Override
    public void renderRecursively(BufferBuilder buffer, GeoBone bone, float red, float green, float blue, float alpha) {
        this.renderCustomBones(buffer, bone, red, green, blue, alpha, 0.0);
    }

    public void renderCubeGeometry(BufferBuilder buffer, GeoCube cube, GeoBone bone, float r, float g, float b, float a, double textureOffset) {
        MATRIX_STACK.moveToPivot(cube);
        MATRIX_STACK.rotate(cube);
        MATRIX_STACK.moveBackFromPivot(cube);
        for (GeoQuad geoQuad : cube.quads) {
            if (geoQuad == null) continue;
            Vector3f vector3f = new Vector3f((float)geoQuad.normal.getX(), (float)geoQuad.normal.getY(), (float)geoQuad.normal.getZ());
            MATRIX_STACK.getNormalMatrix().transform((Tuple3f)vector3f);
            if ((cube.size.y == 0.0f || cube.size.z == 0.0f) && vector3f.getX() < 0.0f) {
                vector3f.x *= -1.0f;
            }
            if ((cube.size.x == 0.0f || cube.size.z == 0.0f) && vector3f.getY() < 0.0f) {
                vector3f.y *= -1.0f;
            }
            if ((cube.size.x == 0.0f || cube.size.y == 0.0f) && vector3f.getZ() < 0.0f) {
                vector3f.z *= -1.0f;
            }
            Vec3i vec3i = this.getBoneColor(bone);
            vec3i = this.filterFinalColor(vec3i);
            Vec3d vec3d = BoneDeformProcessor.applyBoneDeformation(this, bone, new Vec3d((float)vec3i.getX() / 255.0f, (float)vec3i.getY() / 255.0f, (float)vec3i.getZ() / 255.0f), vector3f);
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                buffer.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex((double)geoVertex.textureU + textureOffset, geoVertex.textureV).color((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, a).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }
}

