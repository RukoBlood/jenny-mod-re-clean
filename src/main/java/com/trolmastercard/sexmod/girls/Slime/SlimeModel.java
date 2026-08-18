/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod.girls.Slime;

import java.util.Arrays;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;

//cr
public class SlimeModel
extends GirlModel<GirlEntity> {
    // f
    Action[] actionsWithSlime = new Action[]{Action.STARTDOGGY, Action.DOGGYCUM, Action.DOGGYSLOW, Action.DOGGYFAST, Action.DOGGYCUM, Action.DOGGYSTART, Action.WAITDOGGY};

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{new ResourceLocation("sexmod", "geo/slime/nude.geo.json"), new ResourceLocation("sexmod", "geo/slime/armored.geo.json"), new ResourceLocation("sexmod", "geo/slime/dressed.geo.json")};
    }

    //net_minecraft_util_ResourceLocation_a
    // getModelLocation
    @Override
    public ResourceLocation getModelLocation(GirlEntity girl) {
        if (girl.world instanceof FakeWorld) {
            return this.modelLocations[0];
        }
        if (girl.getDataManager().get(GirlEntity.OUTFIT_INDEX) > this.modelLocations.length) {
            System.out.println("Girl doesn't have an outfit Nr." + girl.getDataManager().get(GirlEntity.OUTFIT_INDEX) + " so im just making her nude lol");
            return this.modelLocations[0];
        }
        if (girl instanceof PlayerSlime) {
            return this.modelLocations[girl.getDataManager().get(GirlEntity.OUTFIT_INDEX)];
        }
        if (girl.getDataManager().get(GirlEntity.OUTFIT_INDEX) == 1) {
            return this.modelLocations[2];
        }
        return this.modelLocations[0];
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/slime/slime.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/slime/slime.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);
        AnimationProcessor<SlimeEntity> animationProcessor = this.getAnimationProcessor();
        if (!(girl.world instanceof FakeWorld) && animationProcessor.getBone("bedSlime") != null && animationProcessor.getBone("bedSlimeLayer") != null) {
            animationProcessor.getBone("bedSlime").setHidden(!Arrays.asList(this.actionsWithSlime).contains((Object) girl.getCurrentAction()));
            animationProcessor.getBone("bedSlimeLayer").setHidden(!Arrays.asList(this.actionsWithSlime).contains((Object) girl.getCurrentAction()));
        }
        if (girl instanceof PlayerGirl) {
            return;
        }
        this.a(new String[]{"head"}, "hat");
    }

    void a(String[] stringArray, String string) {
        AnimationProcessor<GirlEntity> animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone(string);
        IBone[] iBoneArray = new IBone[stringArray.length];
        for (int i = 0; i < iBoneArray.length; ++i) {
            iBoneArray[i] = animationProcessor.getBone(stringArray[i]);
        }
        Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f vector3f2 = new Vector3f(0.0f, 0.0f, 0.0f);
        for (IBone iBone2 : iBoneArray) {
            vector3f.add((Tuple3f)new Vector3f(iBone2.getRotationX(), iBone2.getRotationY(), iBone2.getRotationZ()));
            vector3f2.add((Tuple3f)new Vector3f(iBone2.getPositionX(), iBone2.getPositionY(), iBone2.getPositionZ()));
        }
        iBone.setRotationX(vector3f.x);
        iBone.setRotationY(vector3f.y);
        iBone.setRotationZ(vector3f.z);
        iBone.setPositionX(vector3f2.x);
        iBone.setPositionY(vector3f2.y);
        iBone.setPositionZ(vector3f2.z);
        iBone.setPositionZ(vector3f2.z);
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    @Override
    public String[] Attachments() {
        return new String[]{"bigblob"};
    }

    @Override
    public String[] TopArmor() {
        return new String[]{"armorShoulderR", "armorShoulderL", "armorChest", "armorBoobs"};
    }

    @Override
    public String[] Top() {
        return new String[]{"boobsFlesh", "upperBodyL", "upperBodyR", "cloth"};
    }

    @Override
    public String[] BottomArmor() {
        return new String[]{"armorBootyR", "armorBootyL", "armorPantsLowL", "armorPantsLowR", "armorPantsLowR", "armorPantsUpR", "armorPantsUpL", "armorHip"};
    }

    @Override
    public String[] Bottom() {
        return new String[]{"fleshL", "fleshR", "vagina", "curvesL", "curvesR", "kneeL", "kneeR"};
    }

    @Override
    public String[] ShoesArmor() {
        return new String[]{"armorShoesL", "armorShoesR"};
    }
}

