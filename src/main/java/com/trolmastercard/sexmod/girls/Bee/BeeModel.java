/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Bee;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BeeModel extends GirlModel<GirlEntity> {
    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{new ResourceLocation("sexmod", "geo/bee/bee.geo.json"), new ResourceLocation("sexmod", "geo/bee/armored.geo.json")};
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/bee/bee.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/bee/bee.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);
        if (girl.world instanceof FakeWorld) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("chest");
        if (iBone == null) {
            return;
        }
        iBone.setHidden(girl.movementController.getCurrentAnimation() == null || !girl.movementController.getCurrentAnimation().animationName.contains("chest"));
    }

    @Override
    protected void processHeadLookRotation(GirlEntity girl, AnimationProcessor<GirlEntity> processor, AnimationEvent<GirlEntity> event) {
        if (!(girl.world instanceof FakeWorld || girl.currentAction() != Action.NULL && girl.currentAction() != Action.ATTACK && girl.currentAction() != Action.BOW)) {
            EntityModelData entityModelData = event.getExtraDataOfType(EntityModelData.class).get(0);
            IBone iBone = processor.getBone("neck");
            iBone.setRotationY(entityModelData.netHeadYaw * 0.5f * ((float)Math.PI / 180));
            IBone iBone2 = processor.getBone("head");
            iBone2.setRotationY(entityModelData.netHeadYaw * ((float)Math.PI / 180));
            iBone2.setRotationX(1.0f + entityModelData.headPitch * ((float)Math.PI / 180));
            IBone iBone3 = processor.getBone("body") == null ? processor.getBone("dd") : processor.getBone("body");
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

