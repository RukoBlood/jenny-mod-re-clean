/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 */
package com.trolmastercard.sexmod.girls.Mangelie;

import javax.annotation.Nonnull;

import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;

public class ManglelieModel extends GirlModel<GirlEntity> {
    final static public float HEAD_SMOOTH_SPEED = 7.0f;
    final static public float SCALE_FACTOR = 0.75f;
    final static float RAD_140 = TrigMath.wrapDegrees(140.0f);
    final static float RAD_35 = TrigMath.wrapDegrees(35.0f);
    final static float DEG_90 = 90.0f;
    final static float RAD_45 = TrigMath.wrapDegrees(45.0f);
    final static float RAD_NEGA_45 = TrigMath.wrapDegrees(-45.0f);

    final static public ResourceLocation TEXTURE_MANGELIE;

    static {
        TEXTURE_MANGELIE = new ResourceLocation("sexmod", "textures/entity/manglelie/manglelie.png");
    }

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
        if (ManglelieModel.isThreesomeAction(girl)) {
            return this.modelLocations[2];
        }
        return this.modelLocations[girl.getDataManager().get(GirlEntity.OUTFIT_INDEX)];
    }

    public static boolean isThreesomeAction(GirlEntity girl) {
        return Action.isAnyAction(girl, Action.THREESOME_SLOW, Action.THREESOME_FAST, Action.THREESOME_CUM);
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return TEXTURE_MANGELIE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/manglelie/manglelie.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);

        ManglelieModel.updateClothAndCockVisibility(girl, this.getAnimationProcessor(), event.getPartialTick());
        this.updateHeadRotation(girl);
        this.updateArmIK(girl);
        this.updateBodySync(girl);
        //this.e(em_class2582); // NOTTODO
        this.updateCorruptedAnimation(girl);
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

    void updateCorruptedAnimation(GirlEntity girl) {
        if (this.mc.isGamePaused()) {
            return;
        }
        if (!(girl instanceof ManglelieEntity)) {
            return;
        }

        ManglelieEntity manglelie = (ManglelieEntity) girl;
        GalathEntity galath = manglelie.getMommyGalath(false);

        if (galath == null) {
            return;
        }

        // Проверяем, находится ли Галат в состоянии анимации осквернения
        if (!Action.isAnyAction(galath, Action.CORRUPT_CUM, Action.CARRY_FAST, Action.CORRUPT_INTRO, Action.CORRUPT_SLOW)) {
            return;
        }

        AnimationProcessor processor = this.getAnimationProcessor();

        IBone legR = processor.getBone("legR");
        if (legR != null) {
            legR.setRotationY(legR.getRotationY() + RAD_NEGA_45);
        }

        IBone lowerArmR = processor.getBone("lowerArmR");
        if (lowerArmR != null) {
            lowerArmR.setRotationX(lowerArmR.getRotationX() + RAD_NEGA_45);
        }

        IBone lowerArmL = processor.getBone("lowerArmL");
        if (lowerArmL != null) {
            lowerArmL.setRotationX(lowerArmL.getRotationX() + RAD_NEGA_45);
        }
    }

    void updateBodySync(GirlEntity girl) {
        if (!(girl instanceof ManglelieEntity)) {
            return;
        }
        if (ManglelieModel.isThreesomeAction(girl)) {
            return;
        }

        ManglelieEntity manglelie = (ManglelieEntity)girl;
        GalathEntity galath = manglelie.getMommyGalath(false);
        if (galath == null) {
            return;
        }
        IBone bodyBone = this.getAnimationProcessor().getBone("body");
        bodyBone.setRotationY(galath.bodyRotationY + (this.mc.isGamePaused() ? 0.0f : bodyBone.getRotationY()));
        bodyBone.setScaleX(galath.bodyScaleY);
        bodyBone.setScaleY(galath.bodyScaleY);
        bodyBone.setScaleZ(galath.bodyScaleY);
    }

    Vec3d getEntityEyePosition(@Nonnull Entity entity) {
        return Utils.getInterpolatedPosition(entity, this.mc.getRenderPartialTicks()).add(0.0, entity.getEyeHeight(), 0.0);
    }

    void updateArmIK(GirlEntity girl) {
        //float fps;
        //boolean hasNoTarget;
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        if (ManglelieModel.isThreesomeAction(girl)) {
            return;
        }
        if (this.mc.isGamePaused()) {
            return;
        }

        ManglelieEntity manglelie = (ManglelieEntity)girl;
        if (!manglelie.isAttachedToMommy()) {
            return;
        }

        GalathEntity galath = manglelie.getMommyGalath(false);
        if (galath == null) {
            return;
        }

        AnimationProcessor processor = this.getAnimationProcessor();
        IBone armL = processor.getBone("armL");
        IBone armR = processor.getBone("armR");
        IBone lowerArmL = processor.getBone("lowerArmL");
        IBone lowerArmR = processor.getBone("lowerArmR");
        IBone elbowR = processor.getBone("elbowR");
        IBone elbowL = processor.getBone("elbowL");
        Entity targetEntity = manglelie.getTargetEntity();

        boolean hasNoTarget = targetEntity == null;
        if (!hasNoTarget) {
            manglelie.ikTargetPos = this.getEntityEyePosition(targetEntity);
        }

        float fps = (float)Minecraft.getDebugFPS();
        if (fps == 0.0f) {
            fps = 1.0f;
        }
        manglelie.ikProgress = manglelie.isTargetHandRight == hasNoTarget ? 0.0f : (manglelie.ikProgress += 1.5f / fps);
        if (manglelie.ikProgress >= 1.0f) {
            manglelie.ikProgress = 0.0f;
            manglelie.isTargetHandRight = hasNoTarget;
        }

        ArmTransformState state = manglelie.ikProgress == 0.0f
                ? (hasNoTarget
                ? this.calculateIdleArmState(galath, armR, armL, lowerArmL, lowerArmR)
                : this.calculateTargetedArmState(manglelie, galath, lowerArmR, lowerArmL, processor))
                : ArmTransformState.lerp(this.calculateIdleArmState(galath, armR, armL, lowerArmL, lowerArmR),
                this.calculateTargetedArmState(manglelie, galath, lowerArmR, lowerArmL, processor),
                (float)(manglelie.isTargetHandRight
                        ? ReferenceAndRotationHelper.EaseOutBack(manglelie.ikProgress)
                        : 1.0 - ReferenceAndRotationHelper.EaseOutBack(manglelie.ikProgress)
                )
        );

        armR.setRotationX(state.armRRot.x);
        armR.setRotationY(state.armRRot.y);
        armR.setRotationZ(state.armRRot.z);

        armL.setRotationX(state.armLRot.x);
        armL.setRotationY(state.armLRot.y);
        armL.setRotationZ(state.armLRot.z);

        lowerArmL.setRotationX(state.lowerArmLRot.x);
        lowerArmL.setRotationY(state.lowerArmLRot.y);
        lowerArmL.setRotationZ(state.lowerArmLRot.z);

        lowerArmR.setRotationX(state.lowerArmRRot.x);
        lowerArmR.setRotationY(state.lowerArmRRot.y);
        lowerArmR.setRotationZ(state.lowerArmRRot.z);

        armL.setScaleY(state.armLScaleY);
        armR.setScaleY(state.armRScaleY);
        elbowR.setRotationY(state.elbowRRotY);
        elbowL.setRotationY(state.elbowLRotY);
    }

    ArmTransformState calculateTargetedArmState(@Nonnull ManglelieEntity manglelie, @Nonnull GalathEntity galath, IBone lowerArmR, IBone lowerArmL, AnimationProcessor processor) {

        //a_inner128 state = new a_inner128(null); // TODO weird synthetic inners...
        ArmTransformState state = new ArmTransformState();
        //ArmTransformState.access$202(state, new Vector3fSexmodSpecial(RAD_35, 0.0f, lowerArmR.getRotationZ()));
        //ArmTransformState.access$302(state, new Vector3fSexmodSpecial(RAD_140, 0.0f, lowerArmL.getRotationZ()));
        state.lowerArmLRot = new Vector3fSexmodSpecial(RAD_35, 0.0f, lowerArmR.getRotationZ());
        state.lowerArmRRot = new Vector3fSexmodSpecial(RAD_140, 0.0f, lowerArmL.getRotationZ());

        float headOffset = galath.cachedHeadRotationX + processor.getBone("upperBody").getRotationX();
        float partialTicks = this.mc.getRenderPartialTicks();

        Vec3d renderPos = ManglelieRenderer.a(galath, partialTicks);
        Vec3d armRPos = manglelie.getCachedBoneOffset("armR").add(renderPos);
        Vec3d armLPos = manglelie.getCachedBoneOffset("armL").add(renderPos);

        Rotation2f lookR = ThreadNames.CalculateLookAngles(armRPos, manglelie.ikTargetPos);
        Rotation2f lookL = ThreadNames.CalculateLookAngles(armLPos, manglelie.ikTargetPos);

        Float headPos = GalathEntity.getAimYaw(galath, partialTicks);
        float yawHead = headPos == null ? ReferenceAndRotationHelper.LerpAngleDegrees(galath.prevRotationYawHead, galath.rotationYawHead, (double)partialTicks) : headPos;
        float radYawHead = TrigMath.wrapDegrees(yawHead);

        float progressRaw = manglelie.getAttackProgress(partialTicks);
        float progressQuart = (float) ReferenceAndRotationHelper.EaseOutQuart(Math.min(1.0f, progressRaw));

        float factor = 0.0f;
        if (progressQuart == 1.0f) {
            factor = (progressRaw * 28.0f - 28.0f) / 32.0f;
            factor = Math.max(0.0f, factor - 0.5f) * 2.0f;
        }

        float f9 = (float) ReferenceAndRotationHelper.h(factor); //factorH

        float lerpedRot = TrigMath.wrapDegrees(ReferenceAndRotationHelper.LerpFloat(0.0f, 90.0f, progressQuart));

        boolean isRightHandDominant = manglelie.isVectorRightOfMommy(manglelie.ikTargetPos, partialTicks);
        if (isRightHandDominant) {
            //ArmTransformState.access$002(state, new Vector3fSexmodSpecial(-headOffset + lookR.yaw + TrigMath.toRadians(90.0f), lookR.pitch, 0.0f));
            //ArmTransformState.access$102(state, new Vector3fSexmodSpecial(-headOffset + lookL.yaw + TrigMath.toRadians(90.0f), (float)((double)lookL.pitch + (double) TrigMath.toRadians(-20.0f) * Math.cos(lookR.pitch + radYawHead * 1.0f) + (double) Reference.LerpFloat(lerpedRot / 2.0f, 0.0f, f9)), 0.0f));
            //ArmTransformState.access$402(state, 1.0f + Math.abs(Math.abs(lookR.pitch) - Math.abs(radYawHead)) * 0.1909f);
            //ArmTransformState.access$702(state, TrigMath.toRadians(90.0f));
            //ArmTransformState.access$200((ArmTransformState)state).z = Reference.LerpFloat(lerpedRot, 0.0f, f9);

            state.armRRot = new Vector3fSexmodSpecial(-headOffset + lookR.yaw + TrigMath.wrapDegrees(90.0f), lookR.pitch, 0.0f);
            state.armLRot = new Vector3fSexmodSpecial(-headOffset + lookL.yaw + TrigMath.wrapDegrees(90.0f), (float) (lookL.pitch + TrigMath.wrapDegrees(-20.0f) * Math.cos(lookR.pitch + radYawHead) + ReferenceAndRotationHelper.LerpFloat(lerpedRot / 2.0f, 0.0f, f9)), 0.0f);
            state.armLScaleY = 1.0f + Math.abs(Math.abs(lookR.pitch) - Math.abs(radYawHead)) * 0.1909f;
            state.elbowLRotY = TrigMath.wrapDegrees(90.0f);
            state.lowerArmLRot.z = ReferenceAndRotationHelper.LerpFloat(lerpedRot, 0.0f, f9);

            if ((double)factor > 0.5) {
                state.lowerArmLRot.x = RAD_35 + (float) ReferenceAndRotationHelper.LerpDouble(RAD_45, 0.0, ReferenceAndRotationHelper.h((factor - 0.5f) * 2.0f));
                //ArmTransformState.access$200((ArmTransformState)state).x = RAD_35 + (float) Reference.LerpDouble((double) RAD_45, 0.0, Reference.h((factor - 0.5f) * 2.0f));
            } else if (factor != 0.0f && (double)factor < 0.5) {
                //ArmTransformState.access$200((ArmTransformState)state).x = RAD_35 + (float) Reference.LerpDouble(0.0, (double) RAD_45, Reference.h(factor * 2.0f));
                state.lowerArmLRot.x = RAD_35 + (float) ReferenceAndRotationHelper.LerpDouble(0.0, RAD_45, ReferenceAndRotationHelper.h(factor * 2.0f));
            }
        } else {
//            ArmTransformState.access$102(state, new Vector3fSexmodSpecial(-headOffset + lookL.yaw + TrigMath.toRadians(90.0f), lookL.pitch, 0.0f));
//            ArmTransformState.access$002(state, new Vector3fSexmodSpecial(-headOffset + lookR.yaw + TrigMath.toRadians(90.0f), (float)((double)lookR.pitch + (double) TrigMath.toRadians(20.0f) * Math.cos(lookL.pitch + radYawHead * 1.0f)) - Reference.LerpFloat(lerpedRot / 2.0f, 0.0f, f9), 0.0f));
//            ArmTransformState.access$502(state, 1.0f + Math.abs(Math.abs(lookL.pitch) - Math.abs(radYawHead)) * 0.1909f);
//            ArmTransformState.access$602(state, TrigMath.toRadians(90.0f));
//            ArmTransformState.access$300((ArmTransformState)state).z = -Reference.LerpFloat(lerpedRot, 0.0f, f9);
            state.armLRot = new Vector3fSexmodSpecial(-headOffset + lookL.yaw + TrigMath.wrapDegrees(90.0f), lookL.pitch, 0.0f);
            state.armRRot = new Vector3fSexmodSpecial(-headOffset + lookR.yaw + TrigMath.wrapDegrees(90.0f), (float) (lookR.pitch + TrigMath.wrapDegrees(20.0f) * Math.cos(lookL.pitch + radYawHead)) - ReferenceAndRotationHelper.LerpFloat(lerpedRot / 2.0f, 0.0f, f9), 0.0f);
            state.armRScaleY = 1.0f + Math.abs(Math.abs(lookL.pitch) - Math.abs(radYawHead)) * 0.1909f;
            state.elbowRRotY = TrigMath.wrapDegrees(90.0f);
            state.lowerArmRRot.z = -ReferenceAndRotationHelper.LerpFloat(lerpedRot, 0.0f, f9);

            if ((double)factor > 0.5) {
                //ArmTransformState.access$300((ArmTransformState)state).x = RAD_140 + (float) Reference.LerpDouble((double) RAD_45, 0.0, Reference.h((factor - 0.5f) * 2.0f));
                state.lowerArmRRot.x = RAD_140 + (float) ReferenceAndRotationHelper.LerpDouble(RAD_45, 0.0, ReferenceAndRotationHelper.h((factor - 0.5f) * 2.0f));
            } else if (factor != 0.0f && (double)factor < 0.5) {
                //ArmTransformState.access$300((ArmTransformState)state).x = RAD_140 + (float) Reference.LerpDouble(0.0, (double) RAD_45, Reference.h(factor * 2.0f));
                state.lowerArmRRot.x = RAD_140 + (float) ReferenceAndRotationHelper.LerpDouble(0.0, RAD_45, ReferenceAndRotationHelper.h(factor * 2.0f));
            }
        }
        //ArmTransformState.access$000((ArmTransformState)state).y += radYawHead;
        //ArmTransformState.access$100((ArmTransformState)state).y += radYawHead;
        state.armRRot.y += radYawHead;
        state.armLRot.y += radYawHead;
        return state;
    }

    ArmTransformState calculateIdleArmState(GalathEntity galath, IBone armR, IBone armL, IBone lowerArmL, IBone lowerArmR) {
        float headRotX = galath.cachedHeadRotationX;
        //a_inner128 state = new a_inner128(null);
        ArmTransformState state = new ArmTransformState(); // TODO weird synthetic inners...

        if (headRotX > 0.0f) {
            //ArmTransformState.access$002(state, new Vector3fSexmodSpecial(armR.getRotationX() - headRotX, armR.getRotationY() - headRotX * -25.0f / 45.0f, armR.getRotationZ() + headRotX * 12.5f / 45.0f));
            //ArmTransformState.access$102(state, new Vector3fSexmodSpecial(armL.getRotationX() - headRotX, armL.getRotationY() + headRotX * 15.0f / 45.0f, armL.getRotationZ()));
            //ArmTransformState.access$202(state, new Vector3fSexmodSpecial(lowerArmL.getRotationX(), lowerArmL.getRotationY(), lowerArmL.getRotationZ()));
            //ArmTransformState.access$302(state, new Vector3fSexmodSpecial(lowerArmR.getRotationX(), lowerArmR.getRotationY(), lowerArmR.getRotationZ()));
            state.armRRot = new Vector3fSexmodSpecial(armR.getRotationX() - headRotX, armR.getRotationY() - headRotX * -25.0f / 45.0f, armR.getRotationZ() + headRotX * 12.5f / 45.0f);
            state.armLRot = new Vector3fSexmodSpecial(armL.getRotationX() - headRotX, armL.getRotationY() + headRotX * 15.0f / 45.0f, armL.getRotationZ());
            state.lowerArmLRot = new Vector3fSexmodSpecial(lowerArmL.getRotationX(), lowerArmL.getRotationY(), lowerArmL.getRotationZ());
            state.lowerArmRRot = new Vector3fSexmodSpecial(lowerArmR.getRotationX(), lowerArmR.getRotationY(), lowerArmR.getRotationZ());
            return state;
        }

        //ArmTransformState.access$302(state, new Vector3fSexmodSpecial(lowerArmR.getRotationX() + 2.0f * headRotX, lowerArmR.getRotationY(), lowerArmR.getRotationZ()));
        //ArmTransformState.access$202(state, new Vector3fSexmodSpecial(lowerArmL.getRotationX() + 2.2222223f * headRotX, lowerArmL.getRotationY(), lowerArmL.getRotationZ()));
        //ArmTransformState.access$002(state, new Vector3fSexmodSpecial(armR.getRotationX() - headRotX, armR.getRotationY(), armR.getRotationZ() + headRotX * 5.0f / 45.0f));
        //ArmTransformState.access$102(state, new Vector3fSexmodSpecial(armL.getRotationX() - headRotX, armL.getRotationY(), armL.getRotationZ() - headRotX * 5.0f / 45.0f));
        state.lowerArmRRot = new Vector3fSexmodSpecial(lowerArmR.getRotationX() + 2.0f * headRotX, lowerArmR.getRotationY(), lowerArmR.getRotationZ());
        state.lowerArmLRot = new Vector3fSexmodSpecial(lowerArmL.getRotationX() + 2.2222223f * headRotX, lowerArmL.getRotationY(), lowerArmL.getRotationZ());
        state.armRRot = new Vector3fSexmodSpecial(armR.getRotationX() - headRotX, armR.getRotationY(), armR.getRotationZ() + headRotX * 5.0f / 45.0f);
        state.armLRot = new Vector3fSexmodSpecial(armL.getRotationX() - headRotX, armL.getRotationY(), armL.getRotationZ() - headRotX * 5.0f / 45.0f);
        return state;
    }

    void updateHeadRotation(GirlEntity girl) {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        if (this.mc.isGamePaused()) {
            return;
        }
        ManglelieEntity manglelie = (ManglelieEntity)girl;

        if (!ManglelieRenderer.hasValidModel(manglelie)) {
            return;
        }
        GalathEntity galath = manglelie.getMommyGalath(false);
        if (galath == null) {
            return;
        }

        AnimationProcessor processor = this.getAnimationProcessor();
        float headRotX = galath.cachedHeadRotationX;

        processor.getBone("rotationTool").setRotationX(headRotX);
        IBone head = processor.getBone("head");
        IBone upperBody = processor.getBone("upperBody");
        IBone boobs = processor.getBone("boobs");

        if (headRotX > 0.0f) {
            upperBody.setRotationX(-1.1111112f * headRotX);
            head.setRotationX(0.1333f * headRotX);
            boobs.setRotationX(headRotX * 22.5f / 45.0f);
        } else {
            upperBody.setRotationX(-1.6666666f * headRotX);
            head.setRotationX(headRotX * 0.666f);
        }

        float diffY = ThreadNames.getAngleDifferences((double)manglelie.T, manglelie.targetHeadYaw);
        float diffX = ThreadNames.getAngleDifferences((double)manglelie.ai, manglelie.targetHeadPitch);

        float fps = Minecraft.getDebugFPS();
        if (fps == 0.0f) {
            fps = 1.0f;
        }

        float stepY = 7.0f * (Math.abs(diffY) < 7.0f ? diffY : (diffY > 0.0f ? 7.0f : -7.0f)) * (1.0f / fps);
        float stepX = 7.0f * (Math.abs(diffX) < 7.0f ? diffX : (diffX > 0.0f ? 7.0f : -7.0f)) * (1.0f / fps);

        float finalY = manglelie.T + stepY;
        float finalX = manglelie.ai + stepX;

        head.setRotationY(head.getRotationY() + finalY);
        head.setRotationX(head.getRotationX() + finalX);
        manglelie.T = finalY;
        manglelie.ai = finalX;
    }

    public static void updateClothAndCockVisibility(GirlEntity girl, AnimationProcessor processor, float partialTicks) {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        boolean hasSkirt = ManglelieRenderer.a_5(girl);
        ManglelieModel.setSkirtVisible(processor, hasSkirt);
        ManglelieModel.setSkirtDetailsVisible(processor, hasSkirt);
        ManglelieModel.updateCockStages(girl, processor);
    }

    static void updateCockStages(GirlEntity girl, AnimationProcessor processor) {
        if (!(girl instanceof ManglelieEntity)) {
            return;
        }
        for (int i = 0; i < 3; ++i) {
            IBone cockBone = processor.getBone("cockStage" + i);
            if (cockBone == null) continue;
            cockBone.setHidden(i > ((ManglelieEntity)girl).cockStage);
        }
    }

    static void setSkirtDetailsVisible(AnimationProcessor processor, boolean visible) {
        processor.getBone("skirt").setHidden(!visible);
    }

    static void setSkirtVisible(AnimationProcessor processor, boolean visible) {
        processor.getBone("cheekRBelowSkirt").setHidden(visible);
        processor.getBone("cheekLBelowSkirt").setHidden(visible);
        processor.getBone("sideRNoSkirt").setHidden(visible);
        processor.getBone("sideRSkirt").setHidden(!visible);
        processor.getBone("sideLNoSkirt").setHidden(visible);
        processor.getBone("sideLSkirt").setHidden(!visible);
    }

//    private static RuntimeException a(RuntimeException runtimeException) {
//        return runtimeException;
//    }

    private static class ArmTransformState {
        private Vector3fSexmodSpecial armRRot;
        private Vector3fSexmodSpecial armLRot;
        private Vector3fSexmodSpecial lowerArmRRot;
        private Vector3fSexmodSpecial lowerArmLRot;

        private float armRScaleY = 1.0f;
        private float armLScaleY = 1.0f;
        private float elbowLRotY = 0.0f;
        private float elbowRRotY = 0.0f;

        private ArmTransformState() {
        }

        static ArmTransformState lerp(ArmTransformState start, ArmTransformState end, float step) {
            ArmTransformState result = new ArmTransformState();
            result.armRRot = ReferenceAndRotationHelper.LerpVector3f(start.armRRot, end.armRRot, (double)step);
            result.armLRot = ReferenceAndRotationHelper.LerpVector3f(start.armLRot, end.armLRot, (double)step);
            result.lowerArmRRot = ReferenceAndRotationHelper.LerpVector3f(start.lowerArmRRot, end.lowerArmRRot, (double)step);
            result.lowerArmLRot = ReferenceAndRotationHelper.LerpVector3f(start.lowerArmLRot, end.lowerArmLRot, (double)step);
            result.armRScaleY = ReferenceAndRotationHelper.LerpFloat(start.armRScaleY, end.armRScaleY, step);
            result.armLScaleY = ReferenceAndRotationHelper.LerpFloat(start.armLScaleY, end.armLScaleY, step);
            result.elbowLRotY = ReferenceAndRotationHelper.LerpFloat(start.elbowLRotY, end.elbowLRotY, step);
            result.elbowRRotY = ReferenceAndRotationHelper.LerpFloat(start.elbowRRotY, end.elbowRRotY, step);
            return result;
        }

//        static Vector3fSexmodSpecial access$000(ArmTransformState state) {
//            return state.armRRot;
//        }

//        static Vector3fSexmodSpecial access$100(ArmTransformState state) {
//            return state.armLRot;
//        }

//        static Vector3fSexmodSpecial access$200(ArmTransformState a_inner1282) {
//            return a_inner1282.lowerArmLRot;
//        }

//        static Vector3fSexmodSpecial access$300(ArmTransformState a_inner1282) {
//            return a_inner1282.lowerArmRRot;
//        }

//        static float access$400(ArmTransformState a_inner1282) {
//            return a_inner1282.armLScaleY;
//        }

//        static float access$500(ArmTransformState a_inner1282) {
//            return a_inner1282.armRScaleY;
//        }

//        static float access$600(ArmTransformState a_inner1282) {
//            return a_inner1282.elbowRRotY;
//        }

//        static float access$700(ArmTransformState a_inner1282) {
//            return a_inner1282.elbowLRotY;
//        }

        //a_inner128(b_inner129 b_inner1292) {
        //    this();
        //}

//        static Vector3fSexmodSpecial access$202(ArmTransformState a_inner1282, Vector3fSexmodSpecial f7_class2922) {
//            a_inner1282.lowerArmLRot = f7_class2922;
//            return a_inner1282.lowerArmLRot;
//        }

//        static Vector3fSexmodSpecial access$302(ArmTransformState a_inner1282, Vector3fSexmodSpecial f7_class2922) {
//            a_inner1282.lowerArmRRot = f7_class2922;
//            return a_inner1282.lowerArmRRot;
//        }

//        static Vector3fSexmodSpecial access$002(ArmTransformState a_inner1282, Vector3fSexmodSpecial f7_class2922) {
//            a_inner1282.armRRot = f7_class2922;
//            return a_inner1282.armRRot;
//        }

//        static Vector3fSexmodSpecial access$102(ArmTransformState a_inner1282, Vector3fSexmodSpecial f7_class2922) {
//            a_inner1282.armLRot = f7_class2922;
//            return a_inner1282.armLRot;
//        }

//        static float access$402(ArmTransformState a_inner1282, float f) {
//            a_inner1282.armLScaleY = f;
//            return a_inner1282.armLScaleY;
//        }

//        static float access$702(ArmTransformState a_inner1282, float f) {
//            a_inner1282.elbowLRotY = f;
//            return a_inner1282.elbowLRotY;
//        }

//        static float access$502(ArmTransformState a_inner1282, float f) {
//            a_inner1282.armRScaleY = f;
//            return a_inner1282.armRScaleY;
//        }

//        static float access$602(ArmTransformState a_inner1282, float f) {
//            a_inner1282.elbowRRotY = f;
//            return a_inner1282.elbowRRotY;
//        }
    }
}

