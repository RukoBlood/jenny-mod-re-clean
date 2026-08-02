/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 */
package com.trolmastercard.sexmod.girls.Mangelie;

import javax.annotation.Nonnull;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.util.Vector3fSexmodSpecial;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;

public class ManglelieModel
extends GirlModel<GirlEntity> {
    final static public float h = 7.0f;
    final static public float k = 0.75f;
    final static float l = TrigMath.toRadians(140.0f);
    final static float m = TrigMath.toRadians(35.0f);
    final static float i = 90.0f;
    final static float g = TrigMath.toRadians(45.0f);
    final static float f = TrigMath.toRadians(-45.0f);
    final static public ResourceLocation j = new ResourceLocation("sexmod", "textures/entity/manglelie/manglelie.png");

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{
                new ResourceLocation("sexmod", "geo/manglelie/manglelie.geo.json"),
                new ResourceLocation("sexmod", "geo/manglelie/manglelie.geo.json"),
                new ResourceLocation("sexmod", "geo/galath/galath_con_mang.geo.json")};
    }

    @Override
    public ResourceLocation getModelLocation(GirlEntity girl) {
        if (girl.world instanceof FakeWorld) {
            return this.modelLocations[0];
        }
        if (ManglelieModel.boolean_c(girl)) {
            return this.modelLocations[2];
        }
        return this.modelLocations[girl.getDataManager().get(GirlEntity.OUTFIT_INDEX)];
    }

    public static boolean boolean_c(GirlEntity girl) {
        return Action.a(girl, Action.THREESOME_SLOW, Action.THREESOME_FAST, Action.THREESOME_CUM);
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return j;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/manglelie/manglelie.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);
        ManglelieModel.a(girl, this.getAnimationProcessor(), event.getPartialTick());
        this.void_b(girl);
        this.void_d(girl);
        this.void_a(girl);
        //this.e(em_class2582); // TODO
        assert(false);
    }

    /*
    void e(GirlEntity em_class2582) {
        if (this.a.isGamePaused()) {
            return;
        }
        if (ce_class127.boolean_c(em_class2582)) {
            return;
        }
        GalathEntity f__class2972 = f8_class293.a(em_class2582, false);
        if (f__class2972 == null) {
            return;
        }
        if (!Action.a(f__class2972.com_trolmastercard_sexmod_fp_class324_y(), Action.CORRUPT_CUM, Action.CARRY_FAST, Action.CORRUPT_INTRO, Action.CORRUPT_SLOW)) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("legR");
        iBone.setRotationY(iBone.getRotationY() + f);
        IBone iBone2 = animationProcessor.getBone("lowerArmR");
        IBone iBone3 = animationProcessor.getBone("lowerArmL");
        iBone2.setRotationX(iBone2.getRotationX() + f);
        iBone3.setRotationX(iBone3.getRotationX() + f);
    }*/

    void void_a(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return;
        }
        if (ManglelieModel.boolean_c(em_class2582)) {
            return;
        }
        ManglelieEntity f8_class2932 = (ManglelieEntity)em_class2582;
        GalathEntity f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return;
        }
        IBone iBone = this.getAnimationProcessor().getBone("body");
        iBone.setRotationY(f__class2972.bodyRotationY + (this.mc.isGamePaused() ? 0.0f : iBone.getRotationY()));
        iBone.setScaleX(f__class2972.bodyScaleY);
        iBone.setScaleY(f__class2972.bodyScaleY);
        iBone.setScaleZ(f__class2972.bodyScaleY);
    }

    Vec3d a(@Nonnull Entity entity) {
        return ak_class32.a(entity, this.mc.getRenderPartialTicks()).add(0.0, entity.getEyeHeight(), 0.0);
    }

    void void_d(GirlEntity em_class2582) {
        float f;
        boolean bl;
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        if (ManglelieModel.boolean_c(em_class2582)) {
            return;
        }
        if (this.mc.isGamePaused()) {
            return;
        }
        ManglelieEntity f8_class2932 = (ManglelieEntity)em_class2582;
        if (!f8_class2932.boolean_r()) {
            return;
        }
        GalathEntity f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("armL");
        IBone iBone2 = animationProcessor.getBone("armR");
        IBone iBone3 = animationProcessor.getBone("lowerArmL");
        IBone iBone4 = animationProcessor.getBone("lowerArmR");
        IBone iBone5 = animationProcessor.getBone("elbowR");
        IBone iBone6 = animationProcessor.getBone("elbowL");
        Entity entity = f8_class2932.b_7();
        boolean bl2 = bl = entity == null;
        if (!bl) {
            f8_class2932.R = this.a(entity);
        }
        if ((f = (float)Minecraft.getDebugFPS()) == 0.0f) {
            f = 1.0f;
        }
        f8_class2932.V = f8_class2932.aj == bl ? 0.0f : (f8_class2932.V += 1.5f / f);
        if (f8_class2932.V >= 1.0f) {
            f8_class2932.V = 0.0f;
            f8_class2932.aj = bl;
        }
        a_inner128 a_inner1282 = f8_class2932.V == 0.0f ? (bl ? this.a(f__class2972, iBone2, iBone, iBone3, iBone4) : this.a(f8_class2932, f__class2972, iBone4, iBone3, animationProcessor)) : a_inner128.a(this.a(f__class2972, iBone2, iBone, iBone3, iBone4), this.a(f8_class2932, f__class2972, iBone4, iBone3, animationProcessor), (float)(f8_class2932.aj ? Reference.EaseOutBack(f8_class2932.V) : 1.0 - Reference.EaseOutBack(f8_class2932.V)));
        iBone2.setRotationX(a_inner128.access$000((a_inner128)a_inner1282).x);
        iBone2.setRotationY(a_inner128.access$000((a_inner128)a_inner1282).y);
        iBone2.setRotationZ(a_inner128.access$000((a_inner128)a_inner1282).z);
        iBone.setRotationX(a_inner128.access$100((a_inner128)a_inner1282).x);
        iBone.setRotationY(a_inner128.access$100((a_inner128)a_inner1282).y);
        iBone.setRotationZ(a_inner128.access$100((a_inner128)a_inner1282).z);
        iBone3.setRotationX(a_inner128.access$200((a_inner128)a_inner1282).x);
        iBone3.setRotationY(a_inner128.access$200((a_inner128)a_inner1282).y);
        iBone3.setRotationZ(a_inner128.access$200((a_inner128)a_inner1282).z);
        iBone4.setRotationX(a_inner128.access$300((a_inner128)a_inner1282).x);
        iBone4.setRotationY(a_inner128.access$300((a_inner128)a_inner1282).y);
        iBone4.setRotationZ(a_inner128.access$300((a_inner128)a_inner1282).z);
        iBone.setScaleY(a_inner128.access$400(a_inner1282));
        iBone2.setScaleY(a_inner128.access$500(a_inner1282));
        iBone5.setRotationY(a_inner128.access$600(a_inner1282));
        iBone6.setRotationY(a_inner128.access$700(a_inner1282));
    }

    a_inner128 a(@Nonnull ManglelieEntity f8_class2932, @Nonnull GalathEntity f__class2972, IBone iBone, IBone iBone2, AnimationProcessor animationProcessor) {
        float f;
        //a_inner128 a_inner1282 = new a_inner128(null); // TODO weird synthetic inners...
        a_inner128 a_inner1282 = new a_inner128();
        a_inner128.access$202(a_inner1282, new Vector3fSexmodSpecial(m, 0.0f, iBone.getRotationZ()));
        a_inner128.access$302(a_inner1282, new Vector3fSexmodSpecial(l, 0.0f, iBone2.getRotationZ()));
        float f2 = f__class2972.cachedHeadRotationX + animationProcessor.getBone("upperBody").getRotationX();
        float f3 = this.mc.getRenderPartialTicks();
        Vec3d vec3d = ManglelieRenderer.a(f__class2972, f3);
        Vec3d vec3d2 = f8_class2932.getCachedBoneOffset("armR").add(vec3d);
        Vec3d vec3d3 = f8_class2932.getCachedBoneOffset("armL").add(vec3d);
        Vector2f bm_class882 = Utils.CalculateLookAngles(vec3d2, f8_class2932.R);
        Vector2f bm_class883 = Utils.CalculateLookAngles(vec3d3, f8_class2932.R);
        Float f4 = GalathEntity.updateRenderPositions(f__class2972, f3);
        float f5 = f4 == null ? Reference.LerpAngleDegrees(f__class2972.prevRotationYawHead, f__class2972.rotationYawHead, (double)f3) : f4.floatValue();
        float f6 = TrigMath.toRadians(f5);
        float f7 = f8_class2932.float_b(f3);
        float f8 = (float) Reference.EaseOutQuart(Math.min(1.0f, f7));
        if (f8 != 1.0f) {
            f = 0.0f;
        } else {
            f = (f7 * 28.0f - 28.0f) / 32.0f;
            f = Math.max(0.0f, f - 0.5f) * 2.0f;
        }
        float f9 = (float) Reference.h(f);
        float f10 = TrigMath.toRadians(Reference.LerpFloat(0.0f, 90.0f, f8));
        boolean bl = f8_class2932.boolean_a(f8_class2932.R, f3);
        if (bl) {
            a_inner128.access$002(a_inner1282, new Vector3fSexmodSpecial(-f2 + bm_class882.yaw + TrigMath.toRadians(90.0f), bm_class882.pitch, 0.0f));
            a_inner128.access$102(a_inner1282, new Vector3fSexmodSpecial(-f2 + bm_class883.yaw + TrigMath.toRadians(90.0f), (float)((double)bm_class883.pitch + (double) TrigMath.toRadians(-20.0f) * Math.cos(bm_class882.pitch + f6 * 1.0f) + (double) Reference.LerpFloat(f10 / 2.0f, 0.0f, f9)), 0.0f));
            a_inner128.access$402(a_inner1282, 1.0f + Math.abs(Math.abs(bm_class882.pitch) - Math.abs(f6)) * 0.1909f);
            a_inner128.access$702(a_inner1282, TrigMath.toRadians(90.0f));
            a_inner128.access$200((a_inner128)a_inner1282).z = Reference.LerpFloat(f10, 0.0f, f9);
            if ((double)f > 0.5) {
                a_inner128.access$200((a_inner128)a_inner1282).x = m + (float) Reference.LerpDouble((double)g, 0.0, Reference.h((f - 0.5f) * 2.0f));
            } else if (f != 0.0f && (double)f < 0.5) {
                a_inner128.access$200((a_inner128)a_inner1282).x = m + (float) Reference.LerpDouble(0.0, (double)g, Reference.h(f * 2.0f));
            }
        } else {
            a_inner128.access$102(a_inner1282, new Vector3fSexmodSpecial(-f2 + bm_class883.yaw + TrigMath.toRadians(90.0f), bm_class883.pitch, 0.0f));
            a_inner128.access$002(a_inner1282, new Vector3fSexmodSpecial(-f2 + bm_class882.yaw + TrigMath.toRadians(90.0f), (float)((double)bm_class882.pitch + (double) TrigMath.toRadians(20.0f) * Math.cos(bm_class883.pitch + f6 * 1.0f)) - Reference.LerpFloat(f10 / 2.0f, 0.0f, f9), 0.0f));
            a_inner128.access$502(a_inner1282, 1.0f + Math.abs(Math.abs(bm_class883.pitch) - Math.abs(f6)) * 0.1909f);
            a_inner128.access$602(a_inner1282, TrigMath.toRadians(90.0f));
            a_inner128.access$300((a_inner128)a_inner1282).z = -Reference.LerpFloat(f10, 0.0f, f9);
            if ((double)f > 0.5) {
                a_inner128.access$300((a_inner128)a_inner1282).x = l + (float) Reference.LerpDouble((double)g, 0.0, Reference.h((f - 0.5f) * 2.0f));
            } else if (f != 0.0f && (double)f < 0.5) {
                a_inner128.access$300((a_inner128)a_inner1282).x = l + (float) Reference.LerpDouble(0.0, (double)g, Reference.h(f * 2.0f));
            }
        }
        a_inner128.access$000((a_inner128)a_inner1282).y += f6;
        a_inner128.access$100((a_inner128)a_inner1282).y += f6;
        return a_inner1282;
    }

    a_inner128 a(GalathEntity f__class2972, IBone iBone, IBone iBone2, IBone iBone3, IBone iBone4) {
        float f = f__class2972.cachedHeadRotationX;
        //a_inner128 a_inner1282 = new a_inner128(null);
        a_inner128 a_inner1282 = new a_inner128(); // TODO weird synthetic inners...
        if (f > 0.0f) {
            a_inner128.access$002(a_inner1282, new Vector3fSexmodSpecial(iBone.getRotationX() - f, iBone.getRotationY() - f * -25.0f / 45.0f, iBone.getRotationZ() + f * 12.5f / 45.0f));
            a_inner128.access$102(a_inner1282, new Vector3fSexmodSpecial(iBone2.getRotationX() - f, iBone2.getRotationY() + f * 15.0f / 45.0f, iBone2.getRotationZ()));
            a_inner128.access$202(a_inner1282, new Vector3fSexmodSpecial(iBone3.getRotationX(), iBone3.getRotationY(), iBone3.getRotationZ()));
            a_inner128.access$302(a_inner1282, new Vector3fSexmodSpecial(iBone4.getRotationX(), iBone4.getRotationY(), iBone4.getRotationZ()));
            return a_inner1282;
        }
        a_inner128.access$302(a_inner1282, new Vector3fSexmodSpecial(iBone4.getRotationX() + 2.0f * f, iBone4.getRotationY(), iBone4.getRotationZ()));
        a_inner128.access$202(a_inner1282, new Vector3fSexmodSpecial(iBone3.getRotationX() + 2.2222223f * f, iBone3.getRotationY(), iBone3.getRotationZ()));
        a_inner128.access$002(a_inner1282, new Vector3fSexmodSpecial(iBone.getRotationX() - f, iBone.getRotationY(), iBone.getRotationZ() + f * 5.0f / 45.0f));
        a_inner128.access$102(a_inner1282, new Vector3fSexmodSpecial(iBone2.getRotationX() - f, iBone2.getRotationY(), iBone2.getRotationZ() - f * 5.0f / 45.0f));
        return a_inner1282;
    }

    void void_b(GirlEntity em_class2582) {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        if (this.mc.isGamePaused()) {
            return;
        }
        ManglelieEntity f8_class2932 = (ManglelieEntity)em_class2582;
        if (!ManglelieRenderer.b_4(f8_class2932)) {
            return;
        }
        GalathEntity f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        float f = f__class2972.cachedHeadRotationX;
        animationProcessor.getBone("rotationTool").setRotationX(f);
        IBone iBone = animationProcessor.getBone("head");
        IBone iBone2 = animationProcessor.getBone("upperBody");
        IBone iBone3 = animationProcessor.getBone("boobs");
        if (f > 0.0f) {
            iBone2.setRotationX(-1.1111112f * f);
            iBone.setRotationX(0.1333f * f);
            iBone3.setRotationX(f * 22.5f / 45.0f);
        } else {
            iBone2.setRotationX(-1.6666666f * f);
            iBone.setRotationX(f * 0.666f);
        }
        float f2 = Utils.getAngleDifferences((double)f8_class2932.T, f8_class2932.af);
        float f3 = Utils.getAngleDifferences((double)f8_class2932.ai, f8_class2932.W);
        float f4 = Minecraft.getDebugFPS();
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        float f5 = 7.0f * (Math.abs(f2) < 7.0f ? f2 : (f2 > 0.0f ? 7.0f : -7.0f)) * (1.0f / f4);
        float f6 = 7.0f * (Math.abs(f3) < 7.0f ? f3 : (f3 > 0.0f ? 7.0f : -7.0f)) * (1.0f / f4);
        float f7 = f8_class2932.T + f5;
        float f8 = f8_class2932.ai + f6;
        iBone.setRotationY(iBone.getRotationY() + f7);
        iBone.setRotationX(iBone.getRotationX() + f8);
        f8_class2932.T = f7;
        f8_class2932.ai = f8;
    }

    public static void a(GirlEntity em_class2582, AnimationProcessor animationProcessor, float f) {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        boolean bl = ManglelieRenderer.a_5(em_class2582);
        ManglelieModel.e(animationProcessor, bl);
        ManglelieModel.f(animationProcessor, bl);
        ManglelieModel.b(em_class2582, animationProcessor, f);
    }

    static void b(GirlEntity em_class2582, AnimationProcessor animationProcessor, float f) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return;
        }
        for (int i = 0; i < 3; ++i) {
            IBone iBone = animationProcessor.getBone("cockStage" + i);
            if (iBone == null) continue;
            iBone.setHidden(i > ((ManglelieEntity)em_class2582).an);
        }
    }

    static void f(AnimationProcessor animationProcessor, boolean bl) {
        animationProcessor.getBone("skirt").setHidden(!bl);
    }

    static void e(AnimationProcessor animationProcessor, boolean bl) {
        animationProcessor.getBone("cheekRBelowSkirt").setHidden(bl);
        animationProcessor.getBone("cheekLBelowSkirt").setHidden(bl);
        animationProcessor.getBone("sideRNoSkirt").setHidden(bl);
        animationProcessor.getBone("sideRSkirt").setHidden(!bl);
        animationProcessor.getBone("sideLNoSkirt").setHidden(bl);
        animationProcessor.getBone("sideLSkirt").setHidden(!bl);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    private static class a_inner128 {
        private Vector3fSexmodSpecial c;
        private Vector3fSexmodSpecial g;
        private Vector3fSexmodSpecial h;
        private Vector3fSexmodSpecial b;
        private float f = 1.0f;
        private float a = 1.0f;
        private float e = 0.0f;
        private float d = 0.0f;

        private a_inner128() {
        }

        static a_inner128 a(a_inner128 a_inner1282, a_inner128 a_inner1283, float f) {
            a_inner128 a_inner1284 = new a_inner128();
            a_inner1284.c = Reference.LerpVector3f(a_inner1282.c, a_inner1283.c, (double)f);
            a_inner1284.g = Reference.LerpVector3f(a_inner1282.g, a_inner1283.g, (double)f);
            a_inner1284.h = Reference.LerpVector3f(a_inner1282.h, a_inner1283.h, (double)f);
            a_inner1284.b = Reference.LerpVector3f(a_inner1282.b, a_inner1283.b, (double)f);
            a_inner1284.f = Reference.LerpFloat(a_inner1282.f, a_inner1283.f, f);
            a_inner1284.a = Reference.LerpFloat(a_inner1282.a, a_inner1283.a, f);
            a_inner1284.e = Reference.LerpFloat(a_inner1282.e, a_inner1283.e, f);
            a_inner1284.d = Reference.LerpFloat(a_inner1282.d, a_inner1283.d, f);
            return a_inner1284;
        }

        static Vector3fSexmodSpecial access$000(a_inner128 a_inner1282) {
            return a_inner1282.c;
        }

        static Vector3fSexmodSpecial access$100(a_inner128 a_inner1282) {
            return a_inner1282.g;
        }

        static Vector3fSexmodSpecial access$200(a_inner128 a_inner1282) {
            return a_inner1282.b;
        }

        static Vector3fSexmodSpecial access$300(a_inner128 a_inner1282) {
            return a_inner1282.h;
        }

        static float access$400(a_inner128 a_inner1282) {
            return a_inner1282.a;
        }

        static float access$500(a_inner128 a_inner1282) {
            return a_inner1282.f;
        }

        static float access$600(a_inner128 a_inner1282) {
            return a_inner1282.d;
        }

        static float access$700(a_inner128 a_inner1282) {
            return a_inner1282.e;
        }

        //a_inner128(b_inner129 b_inner1292) {
        //    this();
        //}

        static Vector3fSexmodSpecial access$202(a_inner128 a_inner1282, Vector3fSexmodSpecial f7_class2922) {
            a_inner1282.b = f7_class2922;
            return a_inner1282.b;
        }

        static Vector3fSexmodSpecial access$302(a_inner128 a_inner1282, Vector3fSexmodSpecial f7_class2922) {
            a_inner1282.h = f7_class2922;
            return a_inner1282.h;
        }

        static Vector3fSexmodSpecial access$002(a_inner128 a_inner1282, Vector3fSexmodSpecial f7_class2922) {
            a_inner1282.c = f7_class2922;
            return a_inner1282.c;
        }

        static Vector3fSexmodSpecial access$102(a_inner128 a_inner1282, Vector3fSexmodSpecial f7_class2922) {
            a_inner1282.g = f7_class2922;
            return a_inner1282.g;
        }

        static float access$402(a_inner128 a_inner1282, float f) {
            a_inner1282.a = f;
            return a_inner1282.a;
        }

        static float access$702(a_inner128 a_inner1282, float f) {
            a_inner1282.e = f;
            return a_inner1282.e;
        }

        static float access$502(a_inner128 a_inner1282, float f) {
            a_inner1282.f = f;
            return a_inner1282.f;
        }

        static float access$602(a_inner128 a_inner1282, float f) {
            a_inner1282.d = f;
            return a_inner1282.d;
        }
    }
}

