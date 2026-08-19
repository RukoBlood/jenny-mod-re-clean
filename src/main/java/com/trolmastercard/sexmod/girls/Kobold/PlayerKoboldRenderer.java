/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.Kobold;

import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.WorkerPlayerRenderer;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerKoboldRenderer
extends WorkerPlayerRenderer {
    public PlayerKoboldRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected Vec3i resolveBoneColor(String name) {
        EntityDataManager entityDataManager = this.renderEntity.getDataManager();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.CURRENT_ACTION));
        BlockPos blockPos = (BlockPos)entityDataManager.get(KoboldEntity.ACTION_TARGET_POS);
        if (KoboldRenderer.t.contains(name)) {
            return eyeAndKoboldColor_.getMainColor();
        }
        if (KoboldRenderer.u.contains(name)) {
            return eyeAndKoboldColor_.getSecondaryColor();
        }
        if ("irisR".equals(name) || "irisL".equals(name)) {
            return blockPos;
        }
        return DEFAULT_COLOR;
    }

    @Override
    protected Vector4f calculateBoneArmorColor(String boneName, float r, float g, float b) {
        String[] stringArray;
        int n;
        if ("mouth".equals(boneName) && (n = Integer.parseInt((stringArray = AbstractNpcOnlyEntity.getModelCodeParts(this.renderEntity))[7])) == 1) {
            return new Vector4f(r, g, b, -0.078125f);
        }
        return super.calculateBoneArmorColor(boneName, r, g, b);
    }

    @Override
    protected void onRenderSetup() {
        float f = 0.25f - this.renderEntity.getDataManager().get(PlayerKobold.aA).floatValue();
        GlStateManager.scale(1.0f - f, 1.0f - f, 1.0f - f);
    }

    @Override
    protected void onRenderCleanup() {
        float f = 0.25f - this.renderEntity.getDataManager().get(PlayerKobold.aA).floatValue();
        double d = 1.0 / (1.0 - (double)f);
        GlStateManager.scale(d, d, d);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0, -0.8f, 0.05);
        GlStateManager.scale(0.5, 0.5, 0.5);
    }

    @Override
    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        super.applyItemPostRotation(isLeftHand, stack);
        if (stack.getItem().getItemUseAction(stack) == EnumAction.BOW) {
            if (!isLeftHand) {
                GlStateManager.rotate(170.0f, 1.0f, 0.0f, 0.0f);
            }
            if (isLeftHand) {
                GlStateManager.translate(0.1f, 0.0f, 0.0f);
            }
            return;
        }
        GlStateManager.rotate(isLeftHand ? 80.0f : 180.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (isLeftHand) {
            if (isActive) {
                GlStateManager.translate(0.06, 0.0, -0.13);
                GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(38.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            } else {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, -0.3f, -0.13);
            }
        } else if (isActive) {
            GlStateManager.rotate(150.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0, -0.35, 0.0);
        } else {
            GlStateManager.translate(0.0, -0.1, -0.083f);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

