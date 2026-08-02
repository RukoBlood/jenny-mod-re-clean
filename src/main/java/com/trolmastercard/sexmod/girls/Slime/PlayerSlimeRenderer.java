/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod.girls.Slime;

import java.util.HashSet;
import javax.vecmath.Vector3f;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerSlimeRenderer
extends PlayerGirlRenderer {
    Vector3f A = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f D = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f F = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f E = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f z = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f B = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f C = new Vector3f(0.0f, 0.0f, 0.0f);

    public PlayerSlimeRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0f, -1.25f, 0.0f);
        GlStateManager.scale(0.8f, 0.8f, 0.8f);
    }

    @Override
    protected void onBoneRenderStart(String boneName, GeoBone geoBone) {
        if ("slime".equals(boneName)) {
            this.F = new Vector3f(geoBone.getRotationX(), geoBone.getRotationY(), geoBone.getRotationZ());
            this.A = new Vector3f(geoBone.getScaleX(), geoBone.getScaleY(), geoBone.getScaleZ());
            this.D = new Vector3f(geoBone.getPositionX(), geoBone.getPositionY(), geoBone.getPositionZ());
        }
        if ("upperBody".equals(boneName)) {
            this.B = new Vector3f(geoBone.getRotationX(), geoBone.getRotationY(), geoBone.getRotationZ());
        }
        if ("torso".equals(boneName)) {
            this.E = new Vector3f(geoBone.getRotationX(), geoBone.getRotationY(), geoBone.getRotationZ());
        }
        if ("head".equals(boneName)) {
            this.C = new Vector3f(geoBone.getRotationX(), geoBone.getRotationY(), geoBone.getRotationZ());
        }
        if ("boobs".equals(boneName)) {
            this.z = new Vector3f(geoBone.getRotationX(), geoBone.getRotationY(), geoBone.getRotationZ());
        }
        if ("figure".equals(boneName)) {
            geoBone.setRotationX(this.F.x);
            geoBone.setRotationY(this.F.y);
            geoBone.setRotationZ(this.F.z);
            geoBone.setScaleX(this.A.x);
            geoBone.setScaleY(this.A.y);
            geoBone.setScaleZ(this.A.z);
            geoBone.setPositionX(this.D.x);
            geoBone.setPositionY(this.D.y);
            geoBone.setPositionZ(this.D.z);
        }
        if ("dress".equals(boneName)) {
            geoBone.setRotationX(this.B.x);
            geoBone.setRotationY(this.B.y);
            geoBone.setRotationZ(this.B.z);
        }
        if ("hat".equals(boneName)) {
            geoBone.setRotationX(this.C.x);
            geoBone.setRotationY(this.C.y);
            geoBone.setRotationZ(this.C.z);
        }
        if ("boobsSlime".equals(boneName)) {
            geoBone.setRotationX(this.z.x);
            geoBone.setRotationY(this.z.y);
            geoBone.setRotationZ(this.z.z);
        }
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        super.applyBowRotation(isLeftHand);
        if (isLeftHand) {
            GlStateManager.translate(0.15f, 0.0f, 0.0f);
        } else {
            GlStateManager.translate(-0.02, 0.0, 0.0);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
        }
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        HashSet<String> hashSet = super.getBlacklistedBoneNames();
        hashSet.add("figure");
        return hashSet;
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (isLeftHand && !isActive) {
            GlStateManager.translate(-0.025, -0.025, 0.0);
            return;
        }
        if (!isLeftHand && isActive) {
            GlStateManager.rotate(120.0f, 0.0f, 1.0f, 0.0f);
            return;
        }
        if (!isLeftHand && !isActive) {
            GlStateManager.translate(0.0, 0.4, -0.1);
            GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
        }
    }

    @Override
    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        super.applyItemPostRotation(isLeftHand, stack);
        switch (stack.getItem().getItemUseAction(stack)) {
            case BLOCK: 
            case BOW: {
                break;
            }
            default: {
                GlStateManager.rotate(isLeftHand ? 30.0f : 135.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, 0.05, -0.05);
            }
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

