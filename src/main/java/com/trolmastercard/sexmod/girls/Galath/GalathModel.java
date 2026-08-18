/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Galath;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Utils;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieModel;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.interfaces.IGalath;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public class GalathModel extends GirlModel<GirlEntity> {
    static public ResourceLocation GALATH_TEXTURE = new ResourceLocation("sexmod", "textures/entity/galath/galath.png");
    float lastPussyLickingWav = 0.0f;
    long swordDashStartTime = -1L;
    long swordDashEndTime = -1L;

    public GalathModel() {
        this.modelLocations = this.getAnimationResource();
    }

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{
                new ResourceLocation("sexmod", "geo/galath/galath.geo.json"),
                new ResourceLocation("sexmod", "geo/galath/galath.geo.json"),
                new ResourceLocation("sexmod", "geo/galath/galath_con_mang.geo.json")};
    }

    @Override
    public ResourceLocation getModelLocation(GirlEntity girl) {
        if (girl.world instanceof FakeWorld) {
            return this.modelLocations[0];
        }
        if (((IGalath)((Object) girl)).isHuggingManglelie()) {
            return this.modelLocations[2];
        }
        return this.modelLocations[girl.getDataManager().get(GirlEntity.OUTFIT_INDEX)];
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return GALATH_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/galath/galath.animation.json");
    }

    protected boolean shouldCancelHeadTracking(GirlEntity girl) {
        if (!(girl instanceof GalathEntity)) {
            return true;
        }
        GalathEntity galath = (GalathEntity)girl;
        if (galath.hasMasterOAlgo()) {
            return true;
        }
        return galath.getAttackTarget() == null;
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        this.updateMasturbationHeadLook(girl);
        super.setLivingAnimations(girl, instanceID, event);

        this.updateFlightRotation(girl);
        this.updateRapeChargeBodyRotation(girl);
        //this.f(em_class2582); // TODO fix
        //assert(false);
        this.updateSwordAttackBodyOffset(girl);
        this.updateKnockoutFlightPose(girl);
        this.updateOutfitBonesVisibility(girl);
        this.updateWingsVisibility(girl);
        this.updatePlayerGirlBones(girl);
        this.updateFutaBonesVisibility();
        this.updateBodyRotationData(girl);
        this.updatePussyLickingHeadAnimation(girl);
        this.updateManglelieHugOffset(girl);

        if (!(girl instanceof GalathEntity)) {
            return;
        }
        GalathEntity galath = (GalathEntity) girl;
        galath.cachedHeadRotationX = this.getAnimationProcessor().getBone("head").getRotationX();
        if (galath.isHuggingManglelie()) {
            ManglelieModel.updateClothAndCockVisibility(galath, this.getAnimationProcessor(), event.getPartialTick());
        }
    }

    void updatePussyLickingHeadAnimation(GirlEntity girl) {
        if (!Action.isAnyAction(girl, Action.PUSSY_LICKING)) {
            return;
        }
        if (!(girl instanceof GalathEntity)) {
            return;
        }
        if (this.mc.isGamePaused()) {
            return;
        }
        AnimationProcessor<GirlEntity> animationProcessor = this.getAnimationProcessor();
        IBone headBone = animationProcessor.getBone("head");
        float partialTicks = this.mc.getRenderPartialTicks() + (float)this.mc.player.ticksExisted;
        Vector3fSexmodSpecial f7_class2922 = this.calculatePussyLickingRotationOffset((GalathEntity)girl, partialTicks);
        headBone.setRotationX(headBone.getRotationX() + f7_class2922.x);
        headBone.setRotationY(headBone.getRotationY() + f7_class2922.y);
        headBone.setRotationZ(headBone.getRotationZ() + f7_class2922.z);
        if (girl.getCurrentAction() != Action.PUSSY_LICKING || ((GalathEntity)girl).a5) {
            return;
        }
        float currentWave = (float)(Math.sin(partialTicks * 0.3f) * 10.0);
        if (currentWave > 0.0f && this.lastPussyLickingWav < 0.0f || currentWave < 0.0f && this.lastPussyLickingWav > 0.0f) {
            girl.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
        }
        this.lastPussyLickingWav = currentWave;
    }

    Vector3fSexmodSpecial calculatePussyLickingRotationOffset(GalathEntity galath, float renderTicks) {
        return ReferenceAndRotationHelper.LerpVector3f(this.getPussyLickingWaveAngles(renderTicks), Vector3fSexmodSpecial.ZERO, (double)galath.getSwordAttackProgres(this.mc.getRenderPartialTicks()));
    }

    Vector3fSexmodSpecial getPussyLickingWaveAngles(float renderTicks) {
        return new Vector3fSexmodSpecial(
                (float)Math.sin(renderTicks * 0.3f) * TrigMath.wrapDegrees(10.0f),
                (float)Math.sin(renderTicks * 0.15f) * TrigMath.wrapDegrees(7.0f),
                (float)Math.sin((double)renderTicks * -0.15) * TrigMath.wrapDegrees(7.0f)
        );
    }

    void updateBodyRotationData(GirlEntity girl) {
        if (!(girl instanceof GalathEntity)) {
            return;
        }
        GalathEntity galath = (GalathEntity)girl;
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone bodyBone = animationProcessor.getBone("body");
        galath.bodyRotationY = bodyBone.getRotationY();
        galath.bodyScaleY = bodyBone.getScaleY();
    }

    void updateManglelieHugOffset(GirlEntity girl) {
        if (girl.actionController.getAnimationState() != AnimationState.Transitioning) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        Action currentAction = girl.getCurrentAction();
        if (currentAction == Action.HUG_MANG) {
            IBone body2Bone = animationProcessor.getBone("body2");
            if (body2Bone == null) {
                return;
            }
            body2Bone.setPositionX(0.0f);
            body2Bone.setPositionY(-0.53f);
            body2Bone.setPositionZ(-40.05f);
        }
    }

    void updateMasturbationHeadLook(GirlEntity girl) {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        if (girl.getCurrentAction() != Action.MASTERBATE) {
            return;
        }
        EntityPlayer masterPlayer = girl.getMasterPlayer();
        if (masterPlayer == null) {
            masterPlayer = this.mc.player;
        }
        MolangParser parser = GeckoLibCache.getInstance().parser;
        Vec3d targetOffset = Utils.getVectorToPlayer(girl, masterPlayer, this.mc.getRenderPartialTicks()).add(girl.getCachedBoneOffset("head"));
        float relativeYaw = (float) TrigMath.sinDegrees(Math.atan2(targetOffset.z, targetOffset.x)) - girl.getYawRotation();
        float pitchAngle = (float) TrigMath.sinDegrees(Math.atan2(targetOffset.y, Math.sqrt(targetOffset.x * targetOffset.x + targetOffset.z * targetOffset.z)));

        double totalDistance = Math.abs(targetOffset.x) + Math.abs(targetOffset.y) + Math.abs(targetOffset.z);
        double calculatedPitch = totalDistance * 7.0 + -20.0;
        double calculatedArmPitch = totalDistance * 5.0 + -20.0;

        parser.setValue("pitch", calculatedPitch + (double)pitchAngle - 80.0);
        parser.setValue("armpitch", calculatedArmPitch + (double)pitchAngle + -110.0);
        parser.setValue("armyaw", relativeYaw + 80.0f);
        parser.setValue("yaw", relativeYaw + 90.0f);
    }

    void updateFutaBonesVisibility() {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        this.getAnimationProcessor().getBone("futaCock").setHidden(!FutaCommand.enabled);
        this.getAnimationProcessor().getBone("futaBallLL").setHidden(!FutaCommand.enabled);
        this.getAnimationProcessor().getBone("futaBallLR").setHidden(!FutaCommand.enabled);
    }

    void updatePlayerGirlBones(GirlEntity girl) {
        if (!(girl instanceof PlayerGirl)) {
            return;
        }
        this.getAnimationProcessor().getBone("coin").setHidden(true);
    }

    void updateWingsVisibility(GirlEntity girl) {
        this.getAnimationProcessor().getBone("wings").setHidden(!((IGalath)((Object)girl)).isWingsVisible());
    }

    void updateOutfitBonesVisibility(GirlEntity girl) {
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone nippleR = animationProcessor.getBone("nippleR");
        IBone nippleL = animationProcessor.getBone("nippleL");
        IBone braBoobL = animationProcessor.getBone("braBoobL");
        IBone braBoobR = animationProcessor.getBone("braBoobR");
        IBone slip = animationProcessor.getBone("slip");

        boolean hasWings = ((IGalath)((Object)girl)).isWingsAnimated();
        boolean isLickingOrSitting = Action.isAnyAction(girl, Action.PUSSY_LICKING, Action.MASTERBATE_SITTING, Action.MASTERBATE_SITTING_CUM);
        if (nippleR == null) {
            return;
        }
        if (braBoobL == null) {
            return;
        }

        nippleR.setHidden(!hasWings);
        nippleL.setHidden(!hasWings);
        braBoobL.setHidden(hasWings);
        braBoobR.setHidden(hasWings);
        slip.setHidden(hasWings || isLickingOrSitting);
    }

    void updateKnockoutFlightPose(GirlEntity girl) {
        //boolean isStationary;
        if (!(girl instanceof GalathEntity)) {
            return;
        }
        if (!girl.getDataManager().get(GalathEntity.IS_FLYING_FLAG)) {
            return;
        }
        if (girl.getCurrentAction() != Action.KNOCK_OUT_FLY) {
            return;
        }

        IBone bodyBone = this.getAnimationProcessor().getBone("body");
        Vec3d prevPos = new Vec3d(girl.lastTickPosX, girl.lastTickPosY, girl.lastTickPosZ);
        Vec3d motionVec = girl.getPositionVector().subtract(prevPos);
        boolean isStationary = Math.abs(motionVec.x) + Math.abs(motionVec.z) < (double)0.01f;
        if (isStationary) {
            bodyBone.setRotationX(TrigMath.wrapDegrees(-90.0f));
            bodyBone.setPositionY(0.0f);
            bodyBone.setPositionZ(0.0f);
        } else {
            Vec3d motionDelta = GalathModel.calculateMovementDelta(girl);
            bodyBone.setRotationX(-((float)motionDelta.x));
            bodyBone.setPositionY((float)motionDelta.y);
            bodyBone.setPositionZ((float)motionDelta.z);
        }
    }

    void updateRapeChargeBodyRotation(GirlEntity girl) {
        if (!(girl instanceof GalathEntity)) {
            return;
        }
        if (girl.getCurrentAction() != Action.RAPE_CHARGE) {
            return;
        }

        Vec3d motionDelta = GalathModel.calculateMovementDelta(girl);
        IBone bodyBone = this.getAnimationProcessor().getBone("body");
        IBone rotationToolBone = this.getAnimationProcessor().getBone("rotationTool");
        rotationToolBone.setRotationX((float)motionDelta.x);
        bodyBone.setPositionY((float)motionDelta.y);
        bodyBone.setPositionZ((float)motionDelta.z);
        float spinYawFactor = girl.getDataManager().get(GalathEntity.SPIN_YAW_FACTOR);
        bodyBone.setRotationY(TrigMath.wrapDegrees(spinYawFactor * 180.0f));
    }

    /*
    void f(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathEntity f__class2972 = (GalathEntity)em_class2582;
        if (f__class2972.com_trolmastercard_sexmod_fp_class324_y() != fp_class324.ATTACK_SWORD) {
            this.f = -1L;
            this.i = -1L;
            return;
        }
        int n = f__class2972.az();
        if (n == 24 && this.f == -1L) {
            this.f = this.a.world.getTotalWorldTime();
            this.i = this.f + 8L;
        }
        if (!be_class78.a((double)n, 24.0, 32.0)) {
            return;
        }
        IBone iBone = this.getAnimationProcessor().getBone("body");
        Vec3d vec3d = GalathModel.a(f__class2972, f__class2972.net_minecraft_util_math_Vec3d_B());
        float f = ((float)Minecraft.getMinecraft().world.getTotalWorldTime() + this.a.getRenderPartialTicks() - (float)this.f) / (float)(this.i - this.f);
        vec3d = b6_class67.a(vec3d, Vec3d.ZERO, (double)f);
        iBone.setRotationX((float)vec3d.x);
        iBone.setPositionY((float)vec3d.y);
        iBone.setPositionZ((float)vec3d.z);
    }*/

    void updateSwordAttackBodyOffset(GirlEntity girl) {
        if (!(girl instanceof GalathEntity)) {
            return;
        }
        GalathEntity galath = (GalathEntity) girl;
        if (galath.getCurrentAction() != Action.ATTACK_SWORD) {
            this.swordDashStartTime = -1L;
            this.swordDashEndTime = -1L;
            return;
        }

        int attackProgress = (int) galath.getSwordAttackProgress();
        if (attackProgress == 24 && this.swordDashStartTime == -1L) {
            this.swordDashStartTime = Minecraft.getMinecraft().world.getTotalWorldTime();
            this.swordDashEndTime = this.swordDashStartTime + 8L;
        }

        if (!ThreadNames.isValueInBounds((double) attackProgress, 24.0D, 32.0D)) {
            return;
        }

        IBone bodyBone = this.getAnimationProcessor().getBone("body");
        Vec3d movementOffset = GalathModel.calculateMovementDelta(galath);

        float progress = ((float) Minecraft.getMinecraft().world.getTotalWorldTime() + Minecraft.getMinecraft().getRenderPartialTicks() - (float) this.swordDashStartTime)
                / (float) (this.swordDashEndTime - this.swordDashStartTime);

        movementOffset = ReferenceAndRotationHelper.LerpVec3d(movementOffset, Vec3d.ZERO, (double) progress);

        bodyBone.setRotationX((float) movementOffset.x);
        bodyBone.setPositionY((float) movementOffset.y);
        bodyBone.setPositionZ((float) movementOffset.z);
    }

    void updateFlightRotation(GirlEntity girl) {
        float extraPitch = 0.0f;
        switch (girl.getCurrentAction()) {
            case BOOST: {
                if (Action.BOOST.ticksPlaying[1] > 13 && Action.BOOST.ticksPlaying[1] < 40) {
                    extraPitch = 45.0f;
                }
            }
            case FLY: 
            case CONTROLLED_FLIGHT: {
                break;
            }
            default: {
                return;
            }
        }
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        IBone iBone = this.getAnimationProcessor().getBone("rotationTool");
        Vector4d stateHolder = ((IGalath)((Object)girl)).getFlightData();
        iBone.setRotationX((float) ReferenceAndRotationHelper.LerpDouble(stateHolder.prevPitch + (double)extraPitch, stateHolder.pitch + (double)extraPitch, (double)partialTicks));
        iBone.setRotationZ((float) ReferenceAndRotationHelper.LerpDouble(stateHolder.prevRoll, stateHolder.roll, (double)partialTicks));
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }
}

