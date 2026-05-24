/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class KoboldModel
extends GirlModel<GirlEntity> {
    final static float g = 1.2f;
    final static float f = 1.0f;

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{new ResourceLocation("sexmod", "geo/kobold/kobold.geo.json"), new ResourceLocation("sexmod", "geo/kobold/armored.geo.json")};
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/kobold/kobold.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girlEntity) {
        return new ResourceLocation("sexmod", "animations/kobold/kobold.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity em_class2582, Integer n, AnimationEvent animationEvent) {
        super.setLivingAnimations(em_class2582, n, animationEvent);
        if (em_class2582.world instanceof FakeWorld) {
            return;
        }
        AnimationProcessor<GirlEntity> animationProcessor = this.getAnimationProcessor();
        if (!em_class2582.boolean_h() && em_class2582 instanceof KoboldEntity) {
            animationProcessor.getBone("crown").setHidden(em_class2582.getDataManager().get(KoboldEntity.aZ) == false);
            animationProcessor.getBone("egg").setHidden(!((KoboldEntity)em_class2582).Q);
        } else {
            animationProcessor.getBone("crown").setHidden(true);
            animationProcessor.getBone("egg").setHidden(true);
        }
        String[] stringArray = e4_class223.java_lang_String_arr_a(em_class2582);
        this.b(animationProcessor, stringArray[0]);
        this.e(animationProcessor, stringArray[1]);
        this.a(animationProcessor, stringArray[2], 0.75f, 1.35f, "boobL", "boobR", "armorBoobs");
        this.a(animationProcessor, stringArray[3], 1.0f, 1.2f, "eyeL", "eyeR");
        this.a(animationProcessor, stringArray[3], 1.0f, 1.2f);
        this.a(animationProcessor, stringArray[4]);
        this.d(animationProcessor, stringArray[5]);
        this.a(em_class2582, animationProcessor, stringArray[6]);
        switch (em_class2582.currentAction()) {
            case STARTBLOWJOB: 
            case SUCKBLOWJOB_BLINK: 
            case THRUSTBLOWJOB: 
            case CUMBLOWJOB: {
                animationProcessor.getBone("tounge").setHidden(false);
                break;
            }
            default: {
                animationProcessor.getBone("tounge").setHidden(true);
            }
        }
        this.b(em_class2582, animationProcessor);
    }

    void b(GirlEntity em_class2582, AnimationProcessor<GirlEntity> animationProcessor) {
        if (em_class2582.actionController.getAnimationState() != AnimationState.Transitioning) {
            return;
        }
        float f = em_class2582.getDataManager().get(KoboldEntity.aE);
        f = 0.25f - f;
        switch (em_class2582.currentAction()) {
            case SUCKBLOWJOB_BLINK: 
            case THRUSTBLOWJOB: 
            case CUMBLOWJOB: {
                IBone iBone = animationProcessor.getBone("body");
                iBone.setPositionZ(11.43f + f * -7.0f);
                return;
            }
            case KOBOLD_ANAL_SLOW: 
            case ANAL_FAST: 
            case ANAL_CUM: 
            case ANAL_START: {
                IBone iBone = animationProcessor.getBone("body");
                iBone.setPositionX(1.78f + f * -1.5f);
                iBone.setPositionY(13.07f + f * -11.0f);
                iBone.setPositionZ(2.05f + f * -8.0f);
                return;
            }
            case MATING_PRESS_CUM: 
            case MATING_PRESS_HARD: 
            case MATING_PRESS_SOFT: 
            case MATING_PRESS_START: {
                IBone iBone = animationProcessor.getBone("body");
                iBone.setPositionX(0.0f);
                iBone.setPositionY(2.85f);
                iBone.setPositionZ(-7.0f + f * 4.7f);
                return;
            }
        }
    }

    void a(GirlEntity em_class2582, AnimationProcessor<GirlEntity> animationProcessor, String string) {
        int n = Integer.parseInt(string);
        IBone iBone = animationProcessor.getBone("backpack");
        IBone iBone2 = animationProcessor.getBone("tailpack");
        switch (n) {
            case 0: {
                iBone.setHidden(false);
                iBone2.setHidden(true);
                break;
            }
            case 1: {
                iBone.setHidden(false);
                iBone2.setHidden(false);
                break;
            }
            case 2: {
                iBone.setHidden(true);
                iBone2.setHidden(false);
                break;
            }
            case 3: {
                iBone.setHidden(true);
                iBone2.setHidden(true);
            }
        }
        if (em_class2582.currentAction() == Action.PAYMENT) {
            iBone.setHidden(false);
        }
    }

    void d(AnimationProcessor<GirlEntity> animationProcessor, String string) {
        int n = Integer.parseInt(string);
        IBone iBone = animationProcessor.getBone("frecklesHR1");
        IBone iBone2 = animationProcessor.getBone("frecklesHR2");
        IBone iBone3 = animationProcessor.getBone("frecklesHL1");
        IBone iBone4 = animationProcessor.getBone("frecklesHL2");
        iBone3.setHidden(n != 1);
        iBone.setHidden(n != 1);
        iBone4.setHidden(n != 2);
        iBone2.setHidden(n != 2);
    }

    void a(AnimationProcessor<GirlEntity> animationProcessor, String string) {
        int n = Integer.parseInt(string);
        IBone iBone = animationProcessor.getBone("frecklesAR1");
        IBone iBone2 = animationProcessor.getBone("frecklesAR2");
        IBone iBone3 = animationProcessor.getBone("frecklesAL1");
        IBone iBone4 = animationProcessor.getBone("frecklesAL2");
        iBone3.setHidden(n != 1);
        iBone.setHidden(n != 1);
        iBone4.setHidden(n != 2);
        iBone2.setHidden(n != 2);
    }

    void a(AnimationProcessor<GirlEntity> animationProcessor, String string, float f, float f2) {
        if (Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        float f3 = Float.parseFloat(string);
        f3 /= 100.0f;
        f3 = f + (f2 - f) * f3 - 1.0f;
        IBone iBone = animationProcessor.getBone("eyeL");
        iBone.setPositionX(iBone.getPositionX() + f3);
        IBone iBone2 = animationProcessor.getBone("eyeR");
        iBone2.setPositionX(iBone2.getPositionX() - f3);
    }

    void a(AnimationProcessor<GirlEntity> animationProcessor, String string, float f, float f2, String ... stringArray) {
        float f3 = Float.parseFloat(string);
        f3 /= 100.0f;
        f3 = f + (f2 - f) * f3;
        for (String string2 : stringArray) {
            IBone iBone = animationProcessor.getBone(string2);
            if (iBone == null) continue;
            iBone.setScaleX(f3);
            iBone.setScaleY(f3);
            iBone.setScaleZ(f3);
        }
    }

    void e(AnimationProcessor<GirlEntity> animationProcessor, String string) {
        List<IBone> list = this.c(animationProcessor, "hornDL");
        List<IBone> list2 = this.c(animationProcessor, "hornDR");
        this.a(list);
        this.a(list2);
        int n = new Integer(string);
        animationProcessor.getBone("hornDL" + n).setHidden(false);
        animationProcessor.getBone("hornDR" + n).setHidden(false);
    }

    void b(AnimationProcessor<GirlEntity> animationProcessor, String string) {
        List<IBone> list = this.c(animationProcessor, "hornUL");
        List<IBone> list2 = this.c(animationProcessor, "hornUR");
        this.a(list);
        this.a(list2);
        int n = new Integer(string);
        animationProcessor.getBone("hornUL" + n).setHidden(false);
        animationProcessor.getBone("hornUR" + n).setHidden(false);
    }

    List<IBone> c(AnimationProcessor<GirlEntity> animationProcessor, String string) {
        IBone iBone;
        ArrayList<IBone> arrayList = new ArrayList<IBone>();
        int n = 0;
        while ((iBone = animationProcessor.getBone(string + n)) != null) {
            arrayList.add(iBone);
            ++n;
        }
        return arrayList;
    }

    void a(List<IBone> list) {
        for (IBone iBone : list) {
            iBone.setHidden(true);
        }
    }

    @Override
    protected void a(GirlEntity em_class2582, AnimationProcessor<GirlEntity> animationProcessor, AnimationEvent<GirlEntity> animationEvent) {
        if (em_class2582.world instanceof FakeWorld) {
            return;
        }
        switch (em_class2582.currentAction()) {
            case NULL: {
                if (Math.abs(em_class2582.prevPosX - em_class2582.posX) + Math.abs(em_class2582.prevPosZ - em_class2582.posZ) < 0.0 || em_class2582.onGround && Math.abs(Math.abs(em_class2582.prevPosY) - Math.abs(em_class2582.posY)) > (double)0.1f || !((dr_class199)((Object)em_class2582)).boolean_a()) break;
            }
            default: {
                return;
            }
        }
        EntityModelData entityModelData = animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        IBone iBone = animationProcessor.getBone("head");
        iBone.setRotationY(entityModelData.netHeadYaw * ((float)Math.PI / 180));
        iBone.setRotationX(entityModelData.headPitch * ((float)Math.PI / 180));
        IBone iBone2 = animationProcessor.getBone("body") == null ? animationProcessor.getBone("dd") : animationProcessor.getBone("body");
        iBone2.setRotationY(0.0f);
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

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

