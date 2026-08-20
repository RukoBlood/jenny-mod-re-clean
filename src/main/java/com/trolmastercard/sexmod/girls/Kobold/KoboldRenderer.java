/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod.girls.Kobold;

import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.Nullable;
import javax.vecmath.Vector3f;

import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.GirlRendererBase;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KoboldRenderer extends GirlRendererBase<KoboldEntity> {
    final static HashSet<String> t = new HashSet<String>(Arrays.asList("colorSpots", "neck", "head", "snout", "midSectionR", "midSectionL", "innerCheekLR", "innerCheekRR", "gayL", "gayR", "legR", "legL", "shinL", "toesL", "kneeL", "curvesL", "shinR", "toesR", "kneeR", "curvesR", "sideL", "sideR", "hip", "torsoL", "torsoR", "armR", "lowerArmR", "ellbowR", "armL", "lowerArmL", "ellbowL", "hornUL", "hornUR", "tail", "tail2", "tail3", "tail4", "tail5", "hornDL2", "hornDR2", "hornDR3M", "hornDL3M", "frecklesAL1", "frecklesAL2", "frecklesAR1", "frecklesAR2", "frecklesHL1", "frecklesHL2", "frecklesHR1", "frecklesHR2"));
    final static HashSet<String> u = new HashSet<String>(Arrays.asList("boobR", "boobL", "frontNeck", "Rside", "Lside", "frontAndInside", "innerCheekLL", "innerCheekRL", "layer", "layer2", "down", "down2", "down3", "down4", "down5", "fuckhole", "hornDR3S", "hornDL3S", "assholeCoverUp", "assholeCoverUp2"));
    Minecraft w = Minecraft.getMinecraft();
    Vector3f v;

    public KoboldRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    protected Vec3i resolveBoneColor(String boneName) {
        EntityDataManager entityDataManager = ((KoboldEntity)this.renderEntity).getDataManager();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.CURRENT_ACTION));
        BlockPos blockPos = (BlockPos)entityDataManager.get(KoboldEntity.ACTION_TARGET_POS);
        if (t.contains(boneName)) {
            return eyeAndKoboldColor_.getMainColor();
        }
        if (u.contains(boneName)) {
            return eyeAndKoboldColor_.getSecondaryColor();
        }
        if ("irisR".equals(boneName) || "irisL".equals(boneName)) {
            return blockPos;
        }
        return DEFAULT_COLOR;
    }

    @Override
    protected ItemStack getHeldItem(@Nullable ItemStack input) {
        switch (((KoboldEntity)this.renderEntity).getCurrentAction()) {
            case MINE: {
                if (((KoboldEntity)this.renderEntity).getDataManager().get(KoboldEntity.at).booleanValue()) {
                    return new ItemStack(Items.IRON_AXE);
                }
                return new ItemStack(Items.IRON_PICKAXE);
            }
            case NULL: {
                if (!((KoboldEntity)this.renderEntity).getDataManager().get(KoboldEntity.aC).booleanValue()) break;
                return new ItemStack(Items.IRON_SWORD);
            }
            case ATTACK: {
                return new ItemStack(Items.IRON_SWORD);
            }
        }
        return input;
    }

    @Override
    public void renderCustomBones(BufferBuilder buffer, GeoBone bone, float r, float g, float b, float a, double uOffset) {
        String[] stringArray;
        int n;
        if (((KoboldEntity)this.renderEntity).world instanceof FakeWorld) {
            return;
        }
        String string = bone.getName();
        if ("blowOpening".equals(string)) {
            uOffset = 0.0;
        }
        if ("mouth".equals(string) && (n = Integer.parseInt((stringArray = AbstractNpcOnlyEntity.getModelCodeParts(this.renderEntity))[7])) == 1) {
            uOffset = -0.078125;
        }
        super.renderCustomBones(buffer, bone, r, g, b, a, uOffset);
    }

    @Override
    protected void onRenderSetup() {
        float f = 0.25f - ((KoboldEntity) this.renderEntity).getDataManager().get(PlayerKobold.aA);
        GlStateManager.scale(1.0f - f, 1.0f - f, 1.0f - f);
    }

    @Override
    protected void onRenderCleanup() {
        float f = 0.25f - ((KoboldEntity) this.renderEntity).getDataManager().get(PlayerKobold.aA);
        double d = 1.0 / (1.0 - (double)f);
        GlStateManager.scale(d, d, d);
    }

    @Override
    protected ItemStack resolveTradePaymentItemStack() {
        String string = ((KoboldEntity)this.renderEntity).getDataManager().get(GirlEntity.GIRL_HAND_STATES);
        if ("STARTBLOWJOB".equals(string)) {
            return new ItemStack(Items.IRON_PICKAXE);
        }
        if ("ANAL_START".equals(string)) {
            return new ItemStack(Items.GOLD_INGOT, 3);
        }
        return null;
    }

    @Override
    public void doRender(KoboldEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        String string = entity.getDataManager().get(AbstractNpcOnlyEntity.CURRENT_ACTION);
        if (entity.as == null) {
            entity.as = string;
        }
        if (!entity.as.equals(string)) {
            KoboldRenderer.clearBoneColors();
            entity.as = string;
        }
        this.v = new Vector3f((float) x, (float) y, (float) z);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void renderNameTag(double x, double y, double z) {
        EntityDataManager entityDataManager = ((KoboldEntity)this.renderEntity).getDataManager();
        String string = entityDataManager.get(KoboldEntity.TRIBE_NAME);
        if ("null".equals(string)) {
            super.renderNameTag(x, y, z);
            return;
        }
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.CURRENT_ACTION));
        string = (Object)((Object) eyeAndKoboldColor_.getTextColor()) + " -" + string + "-";
        this.renderLivingLabel(this.renderEntity, ((KoboldEntity)this.renderEntity).getDisplayNameText() + string, x, y + (double)((KoboldEntity)this.renderEntity).getScaleFactor(), z, 300);
    }
}

