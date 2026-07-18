/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.Kobold;

import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.girls.d9_class168;
import com.trolmastercard.sexmod.AbstractGoblinKoboldEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerKoboldRenderer
extends d9_class168 {
    public PlayerKoboldRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected Vec3i net_minecraft_util_math_Vec3i_a(String string) {
        EntityDataManager entityDataManager = this.j.getDataManager();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.CURRENT_ACTION));
        BlockPos blockPos = (BlockPos)entityDataManager.get(KoboldEntity.ACTION_TARGET_POS);
        if (KoboldRenderer.t.contains(string)) {
            return eyeAndKoboldColor_.getMainColor();
        }
        if (KoboldRenderer.u.contains(string)) {
            return eyeAndKoboldColor_.getSecondaryColor();
        }
        if ("irisR".equals(string) || "irisL".equals(string)) {
            return blockPos;
        }
        return z;
    }

    @Override
    protected Vector4f a(String string, float f, float f2, float f3) {
        String[] stringArray;
        int n;
        if ("mouth".equals(string) && (n = Integer.parseInt((stringArray = AbstractGoblinKoboldEntity.SplitDnaIntoGenes(this.j))[7])) == 1) {
            return new Vector4f(f, f2, f3, -0.078125f);
        }
        return super.a(string, f, f2, f3);
    }

    @Override
    protected void d() {
        float f = 0.25f - this.j.getDataManager().get(PlayerKobold.aA).floatValue();
        GlStateManager.scale(1.0f - f, 1.0f - f, 1.0f - f);
    }

    @Override
    protected void void_b() {
        float f = 0.25f - this.j.getDataManager().get(PlayerKobold.aA).floatValue();
        double d = 1.0 / (1.0 - (double)f);
        GlStateManager.scale(d, d, d);
    }

    @Override
    protected void void_c() {
        GlStateManager.translate(0.0, -0.8f, 0.05);
        GlStateManager.scale(0.5, 0.5, 0.5);
    }

    @Override
    protected void a(boolean bl, ItemStack itemStack) {
        super.a(bl, itemStack);
        if (itemStack.getItem().getItemUseAction(itemStack) == EnumAction.BOW) {
            if (!bl) {
                GlStateManager.rotate(170.0f, 1.0f, 0.0f, 0.0f);
            }
            if (bl) {
                GlStateManager.translate(0.1f, 0.0f, 0.0f);
            }
            return;
        }
        GlStateManager.rotate(bl ? 80.0f : 180.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        super.a(bl, bl2);
        if (bl) {
            if (bl2) {
                GlStateManager.translate(0.06, 0.0, -0.13);
                GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(38.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            } else {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, -0.3f, -0.13);
            }
        } else if (bl2) {
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

