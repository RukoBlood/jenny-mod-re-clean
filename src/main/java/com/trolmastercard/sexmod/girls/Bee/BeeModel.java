/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Bee;

import com.trolmastercard.sexmod.Action;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BeeModel
extends GirlModel<GirlEntity> {
    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{new ResourceLocation("sexmod", "geo/bee/bee.geo.json"), new ResourceLocation("sexmod", "geo/bee/armored.geo.json")};
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/bee/bee.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girlEntity) {
        return new ResourceLocation("sexmod", "animations/bee/bee.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity em_class2582, Integer n, AnimationEvent animationEvent) {
        super.setLivingAnimations(em_class2582, n, animationEvent);
        if (em_class2582.world instanceof FakeWorld) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("chest");
        if (iBone == null) {
            return;
        }
        iBone.setHidden(em_class2582.movementController.getCurrentAnimation() == null || !em_class2582.movementController.getCurrentAnimation().animationName.contains("chest"));
    }

    @Override
    protected void a(GirlEntity em_class2582, AnimationProcessor<GirlEntity> animationProcessor, AnimationEvent<GirlEntity> animationEvent) {
        if (!(em_class2582.world instanceof FakeWorld || em_class2582.currentAction() != Action.NULL && em_class2582.currentAction() != Action.ATTACK && em_class2582.currentAction() != Action.BOW)) {
            EntityModelData entityModelData = animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
            IBone iBone = animationProcessor.getBone("neck");
            iBone.setRotationY(entityModelData.netHeadYaw * 0.5f * ((float)Math.PI / 180));
            IBone iBone2 = animationProcessor.getBone("head");
            iBone2.setRotationY(entityModelData.netHeadYaw * ((float)Math.PI / 180));
            iBone2.setRotationX(1.0f + entityModelData.headPitch * ((float)Math.PI / 180));
            IBone iBone3 = animationProcessor.getBone("body") == null ? animationProcessor.getBone("dd") : animationProcessor.getBone("body");
            iBone3.setRotationY(0.0f);
        }
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    @Override
    public String[] Attachments() {
        return new String[]{"band", "feeler", "feeler2", "brow", "brow2", "brow3", "brow4"};
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
        return new String[]{"armorBootyR", "armorBootyL", "armorPantsLowL", "armorPantsLowR", "armorPantsLowR", "armorPantsUpR", "armorPantsUpL", "armorHip"};
    }

    @Override
    public String[] Bottom() {
        return new String[]{"sideL", "sideR", "fleshL", "fleshR", "vagina", "curvesL", "curvesR", "kneeL", "kneeR"};
    }

    @Override
    public String[] ShoesArmor() {
        return new String[]{"armorShoesL", "armorShoesR"};
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

