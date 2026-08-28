/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold;

import java.util.ArrayList;
import java.util.List;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.util.interfaces.IKobold;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class KoboldModel extends GirlModel<GirlEntity> {
    final static float swingProgress = 1.2f;
    final static float legSwing = 1.0f;

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{new ResourceLocation("sexmod", "geo/kobold/kobold.geo.json"), new ResourceLocation("sexmod", "geo/kobold/armored.geo.json")};
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/kobold/kobold.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/kobold/kobold.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);
        if (!(girl.world instanceof FakeWorld)) {
            AnimationProcessor<GirlEntity> processor = this.getAnimationProcessor();
            if (!girl.isLocallyRegistered() && girl instanceof KoboldEntity) {
                processor.getBone("crown").setHidden(!girl.getDataManager().get(KoboldEntity.IS_TRIBE_MEMBER));
                processor.getBone("egg").setHidden(!((KoboldEntity) girl).isRenderEgg);
            } else {
                processor.getBone("crown").setHidden(true);
                processor.getBone("egg").setHidden(true);
            }
            String[] modelCodeParts = AbstractNpcOnlyEntity.getModelCodeParts(girl);
            this.getHornsUp(processor, modelCodeParts[0]);
            this.getHornsDown(processor, modelCodeParts[1]);
            this.setBoneRotationMulti(processor, modelCodeParts[2], 0.75f, 1.35f, "boobL", "boobR", "armorBoobs");
            this.setBoneRotationMulti(processor, modelCodeParts[3], 1.0f, 1.2f, "eyeL", "eyeR");
            this.setBoneRotation(processor, modelCodeParts[3], 1.0f, 1.2f);
            this.getBoneData(processor, modelCodeParts[4]);
            this.parseBoneColor(processor, modelCodeParts[5]);
            this.updateBonePose(girl, processor, modelCodeParts[6]);
            switch (girl.getCurrentAction()) {
                case STARTBLOWJOB:
                case SUCKBLOWJOB_BLINK:
                case THRUSTBLOWJOB:
                case CUMBLOWJOB: {
                    processor.getBone("tounge").setHidden(false);
                    break;
                }
                default: {
                    processor.getBone("tounge").setHidden(true);
                }
            }
            this.handleSwingAnimation(girl, processor);
        }
    }

    void handleSwingAnimation(GirlEntity girl, AnimationProcessor<GirlEntity> processor) {
        if (girl.actionController.getAnimationState() == AnimationState.Transitioning) {
            float transititonValue = girl.getDataManager().get(KoboldEntity.SIZE);
            transititonValue = 0.25f - transititonValue;
            switch (girl.getCurrentAction()) {
                case SUCKBLOWJOB_BLINK:
                case THRUSTBLOWJOB:
                case CUMBLOWJOB: {
                    IBone body = processor.getBone("body");
                    body.setPositionZ(11.43f + transititonValue * -7.0f);
                    return;
                }
                case KOBOLD_ANAL_SLOW:
                case ANAL_FAST:
                case ANAL_CUM:
                case ANAL_START: {
                    IBone body = processor.getBone("body");
                    body.setPositionX(1.78f + transititonValue * -1.5f);
                    body.setPositionY(13.07f + transititonValue * -11.0f);
                    body.setPositionZ(2.05f + transititonValue * -8.0f);
                    return;
                }
                case MATING_PRESS_CUM:
                case MATING_PRESS_HARD:
                case MATING_PRESS_SOFT:
                case MATING_PRESS_START: {
                    IBone body = processor.getBone("body");
                    body.setPositionX(0.0f);
                    body.setPositionY(2.85f);
                    body.setPositionZ(-7.0f + transititonValue * 4.7f);
                }
            }
        }
    }

    void updateBonePose(GirlEntity girl, AnimationProcessor<GirlEntity> processor, String modelCode) {
        int variant = Integer.parseInt(modelCode);
        IBone backpack = processor.getBone("backpack");
        IBone tailpack = processor.getBone("tailpack");
        switch (variant) {
            case 0: {
                backpack.setHidden(false);
                tailpack.setHidden(true);
                break;
            }
            case 1: {
                backpack.setHidden(false);
                tailpack.setHidden(false);
                break;
            }
            case 2: {
                backpack.setHidden(true);
                tailpack.setHidden(false);
                break;
            }
            case 3: {
                backpack.setHidden(true);
                tailpack.setHidden(true);
            }
        }
        if (girl.getCurrentAction() == Action.PAYMENT) {
            backpack.setHidden(false);
        }
    }

    void parseBoneColor(AnimationProcessor<GirlEntity> processor, String modelCode) {
        int variant = Integer.parseInt(modelCode);
        IBone frecklesHR1 = processor.getBone("frecklesHR1");
        IBone frecklesHR2 = processor.getBone("frecklesHR2");
        IBone frecklesHL1 = processor.getBone("frecklesHL1");
        IBone frecklesHL2 = processor.getBone("frecklesHL2");
        frecklesHL1.setHidden(variant != 1);
        frecklesHR1.setHidden(variant != 1);
        frecklesHL2.setHidden(variant != 2);
        frecklesHR2.setHidden(variant != 2);
    }

    void getBoneData(AnimationProcessor<GirlEntity> animationProcessor, String string) {
        int variant = Integer.parseInt(string);
        IBone frecklesAR1 = animationProcessor.getBone("frecklesAR1");
        IBone frecklesAR2 = animationProcessor.getBone("frecklesAR2");
        IBone frecklesAL1 = animationProcessor.getBone("frecklesAL1");
        IBone frecklesAL2 = animationProcessor.getBone("frecklesAL2");
        frecklesAL1.setHidden(variant != 1);
        frecklesAR1.setHidden(variant != 1);
        frecklesAL2.setHidden(variant != 2);
        frecklesAR2.setHidden(variant != 2);
    }

    void setBoneRotation(AnimationProcessor<GirlEntity> processor, String modelCode, float min, float max) {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            float eyeSpacing = Float.parseFloat(modelCode);
            eyeSpacing /= 100.0f;
            eyeSpacing = min + (max - min) * eyeSpacing - 1.0f;
            IBone eyeL = processor.getBone("eyeL");
            eyeL.setPositionX(eyeL.getPositionX() + eyeSpacing);
            IBone eyeR = processor.getBone("eyeR");
            eyeR.setPositionX(eyeR.getPositionX() - eyeSpacing);
        }
    }

    void setBoneRotationMulti(AnimationProcessor<GirlEntity> processor, String modelCode, float min, float max, String ... boneNames) {
        float scale = Float.parseFloat(modelCode);
        scale /= 100.0f;
        scale = min + (max - min) * scale;
        for (String boneName : boneNames) {
            IBone bone = processor.getBone(boneName);
            if (bone != null) {
                bone.setScaleX(scale);
                bone.setScaleY(scale);
                bone.setScaleZ(scale);
            }
        }
    }

    void getHornsDown(AnimationProcessor<GirlEntity> processor, String modelCode) {
        List<IBone> hornDL = this.getHornBones(processor, "hornDL");
        List<IBone> hornDR = this.getHornBones(processor, "hornDR");
        this.hideAllBones(hornDL);
        this.hideAllBones(hornDR);
        int variant = new Integer(modelCode);
        processor.getBone("hornDL" + variant).setHidden(false);
        processor.getBone("hornDR" + variant).setHidden(false);
    }

    void getHornsUp(AnimationProcessor<GirlEntity> processor, String modelCode) {
        List<IBone> hornUL = this.getHornBones(processor, "hornUL");
        List<IBone> hornUR = this.getHornBones(processor, "hornUR");
        this.hideAllBones(hornUL);
        this.hideAllBones(hornUR);
        int variant = new Integer(modelCode);
        processor.getBone("hornUL" + variant).setHidden(false);
        processor.getBone("hornUR" + variant).setHidden(false);
    }

    List<IBone> getHornBones(AnimationProcessor<GirlEntity> processor, String prefix) {
        IBone bone;
        ArrayList<IBone> bones = new ArrayList<>();
        int i = 0;
        while ((bone = processor.getBone(prefix + i)) != null) {
            bones.add(bone);
            ++i;
        }
        return bones;
    }

    void hideAllBones(List<IBone> bones) {
        for (IBone bone : bones) {
            bone.setHidden(true);
        }
    }

    @Override
    protected void processHeadLookRotation(GirlEntity girl, AnimationProcessor<GirlEntity> processor, AnimationEvent<GirlEntity> event) {
        if (!(girl.world instanceof FakeWorld)) {
            switch (girl.getCurrentAction()) {
                case NULL: {
                    if (Math.abs(girl.prevPosX - girl.posX) + Math.abs(girl.prevPosZ - girl.posZ) < 0.0 || girl.onGround && Math.abs(Math.abs(girl.prevPosY) - Math.abs(girl.posY)) > (double) 0.1f || !((IKobold) girl).IsBlockedByCeiling())
                        break;
                }
                default: {
                    return;
                }
            }
            EntityModelData modelData = event.getExtraDataOfType(EntityModelData.class).get(0);
            IBone head = processor.getBone("head");
            head.setRotationY(modelData.netHeadYaw * ((float) Math.PI / 180));
            head.setRotationX(modelData.headPitch * ((float) Math.PI / 180));
            IBone body = processor.getBone("body") == null ? processor.getBone("dd") : processor.getBone("body");
            body.setRotationY(0.0f);
        }
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    @Override
    public String[] TopArmor() {
        return new String[]{"armorShoulderR", "armorShoulderL", "armorChest", "armorBoobs"};
    }

    @Override
    public String[] Top() {
        return new String[]{"boobsFlesh", "upperBodyL", "upperBodyR"};
    }

    @Override
    public String[] BottomArmor() {
        return new String[]{"armorBootyR", "armorBootyL", "armorPantsLowL", "armorPantsLowR", "armorPantsLowR", "armorPantsUpR", "armorPantsUpL", "armorHip", "armorKneeR", "armorKneeL"};
    }

    @Override
    public String[] Bottom() {
        return new String[]{"fleshL", "fleshR", "vagina", "fuckhole", "curvesL", "curvesR", "kneeL", "kneeR"};
    }

    @Override
    public String[] ShoesArmor() {
        return new String[]{"armorShoesL", "armorShoesR"};
    }

    @Override
    public String[] Feet() {
        return new String[]{"toesR", "toesL"};
    }
}

