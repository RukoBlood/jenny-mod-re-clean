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

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.GirlEntity;
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

public class KoboldRenderer
extends d6_class165<KoboldEntity> {
    final static HashSet<String> t = new HashSet<String>(Arrays.asList("colorSpots", "neck", "head", "snout", "midSectionR", "midSectionL", "innerCheekLR", "innerCheekRR", "gayL", "gayR", "legR", "legL", "shinL", "toesL", "kneeL", "curvesL", "shinR", "toesR", "kneeR", "curvesR", "sideL", "sideR", "hip", "torsoL", "torsoR", "armR", "lowerArmR", "ellbowR", "armL", "lowerArmL", "ellbowL", "hornUL", "hornUR", "tail", "tail2", "tail3", "tail4", "tail5", "hornDL2", "hornDR2", "hornDR3M", "hornDL3M", "frecklesAL1", "frecklesAL2", "frecklesAR1", "frecklesAR2", "frecklesHL1", "frecklesHL2", "frecklesHR1", "frecklesHR2"));
    final static HashSet<String> u = new HashSet<String>(Arrays.asList("boobR", "boobL", "frontNeck", "Rside", "Lside", "frontAndInside", "innerCheekLL", "innerCheekRL", "layer", "layer2", "down", "down2", "down3", "down4", "down5", "fuckhole", "hornDR3S", "hornDL3S", "assholeCoverUp", "assholeCoverUp2"));
    Minecraft w = Minecraft.getMinecraft();
    Vector3f v;

    public KoboldRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    protected Vec3i a(String string) {
        EntityDataManager entityDataManager = ((KoboldEntity)this.j).getDataManager();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.N));
        BlockPos blockPos = (BlockPos)entityDataManager.get(KoboldEntity.K);
        if (t.contains(string)) {
            return eyeAndKoboldColor_.getMainColor();
        }
        if (u.contains(string)) {
            return eyeAndKoboldColor_.getSecondaryColor();
        }
        if ("irisR".equals(string) || "irisL".equals(string)) {
            return blockPos;
        }
        return r;
    }

    @Override
    protected ItemStack net_minecraft_item_ItemStack_a(@Nullable ItemStack itemStack) {
        switch (((KoboldEntity)this.j).currentAction()) {
            case MINE: {
                if (((KoboldEntity)this.j).getDataManager().get(KoboldEntity.at).booleanValue()) {
                    return new ItemStack(Items.IRON_AXE);
                }
                return new ItemStack(Items.IRON_PICKAXE);
            }
            case NULL: {
                if (!((KoboldEntity)this.j).getDataManager().get(KoboldEntity.aC).booleanValue()) break;
                return new ItemStack(Items.IRON_SWORD);
            }
            case ATTACK: {
                return new ItemStack(Items.IRON_SWORD);
            }
        }
        return itemStack;
    }

    @Override
    public void a(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
        String[] stringArray;
        int n;
        if (((KoboldEntity)this.j).world instanceof FakeWorld) {
            return;
        }
        String string = geoBone.getName();
        if ("blowOpening".equals(string)) {
            d = 0.0;
        }
        if ("mouth".equals(string) && (n = Integer.parseInt((stringArray = e4_class223.java_lang_String_arr_a(this.j))[7])) == 1) {
            d = -0.078125;
        }
        super.a(bufferBuilder, geoBone, f, f2, f3, f4, d);
    }

    @Override
    protected void d() {
        float f = 0.25f - ((KoboldEntity)this.j).getDataManager().get(PlayerKobold.aA).floatValue();
        GlStateManager.scale(1.0f - f, 1.0f - f, 1.0f - f);
    }

    @Override
    protected void void_b() {
        float f = 0.25f - ((KoboldEntity)this.j).getDataManager().get(PlayerKobold.aA).floatValue();
        double d = 1.0 / (1.0 - (double)f);
        GlStateManager.scale(d, d, d);
    }

    @Override
    protected ItemStack net_minecraft_item_ItemStack_a() {
        String string = ((KoboldEntity)this.j).getDataManager().get(GirlEntity.h);
        if ("STARTBLOWJOB".equals(string)) {
            return new ItemStack(Items.IRON_PICKAXE);
        }
        if ("ANAL_START".equals(string)) {
            return new ItemStack(Items.GOLD_INGOT, 3);
        }
        return null;
    }

    @Override
    public void doRender(KoboldEntity ff_class3082, double d, double d2, double d3, float f, float f2) {
        String string = ff_class3082.getDataManager().get(e4_class223.N);
        if (ff_class3082.as == null) {
            ff_class3082.as = string;
        }
        if (!ff_class3082.as.equals(string)) {
            KoboldRenderer.c();
            ff_class3082.as = string;
        }
        this.v = new Vector3f((float)d, (float)d2, (float)d3);
        super.doRender(ff_class3082, d, d2, d3, f, f2);
    }

    @Override
    protected void a(double d, double d2, double d3) {
        EntityDataManager entityDataManager = ((KoboldEntity)this.j).getDataManager();
        String string = entityDataManager.get(KoboldEntity.aU);
        if ("null".equals(string)) {
            super.a(d, d2, d3);
            return;
        }
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)entityDataManager.get(KoboldEntity.N));
        string = (Object)((Object) eyeAndKoboldColor_.getTextColor()) + " -" + string + "-";
        this.renderLivingLabel(this.j, ((KoboldEntity)this.j).java_lang_String_ab() + string, d, d2 + (double)((KoboldEntity)this.j).float_i(), d3, 300);
    }
}

