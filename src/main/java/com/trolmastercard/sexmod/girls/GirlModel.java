/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.interfaces.IGirlAnimGeoModel;
import com.trolmastercard.sexmod.util.interfaces.IGirlModelParts;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.CheckReturnValue;

public abstract class GirlModel<T extends GirlEntity> extends IGirlAnimGeoModel<T> implements IGirlModelParts {
    final static public List<String> BRA_STRING_BONES = Arrays.asList("braStringMidStartR", "braStringMidMid1R", "braStringMidMid2R", "braStringMidMid3R", "braStringMidEndR", "braStringBackR", "braStringRightEndR", "braStringRightStartR", "braStringRightL", "braStringMidMid1L", "braStringMidMid2L", "braStringMidMid3L", "braStringMidEndL", "braStringBackL", "braStringLeftEndL", "braStringLeftStartL", "braStringMidStartL", "braStringRightR");
    final static public List<String> CAMERA_PLACEMENTS = Arrays.asList("boyCam", "girlCam");
    static public boolean enableModelCache = true;
    protected ResourceLocation[] modelLocations = this.getAnimationResource();
    protected Minecraft mc = Minecraft.getMinecraft();

    protected GirlModel() {
    }

    // TODO fix these override / names clashing synthetics
    protected abstract ResourceLocation[] getAnimationResource();

    public abstract ResourceLocation getSkinLocation();

    public abstract ResourceLocation getAnimationFileLocation(GirlEntity girl);

    @Override
    public ResourceLocation getModelLocation(GirlEntity girlEntity) {
        if (girlEntity.world instanceof FakeWorld) {
            return this.modelLocations[0];
        }
        if (girlEntity.getDataManager().get(GirlEntity.OUTFIT_INDEX) > this.modelLocations.length) {
            System.out.println("Girl doesn't have an outfit Nr." + girlEntity.getDataManager().get(GirlEntity.OUTFIT_INDEX) + " so im just making her nude lol");
            return this.modelLocations[0];
        }
        return this.modelLocations[girlEntity.getDataManager().get(GirlEntity.OUTFIT_INDEX)];
    }

    //net_minecraft_util_ResourceLocation_g
    public ResourceLocation getTextureLocation(GirlEntity girl) {
        //return this.n_et_minecraft_util_ResourceLocation_b();
        return this.getSkinLocation();
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double currentTick) {
        if (Minecraft.getMinecraft().world != null) {
            super.setMolangQueries(animatable, currentTick);
        }
    }

    //a
    @Override
    public void setLivingAnimations(T girl, Integer instanceID, AnimationEvent animationEvent) {
        super.setLivingAnimations(girl, instanceID, animationEvent);
        AnimationProcessor<T> processor = this.getAnimationProcessor();

        this.updateArmModelType(girl, processor);

        if (girl.world instanceof FakeWorld) {
            return;
        }

        if (girl.getDataManager().get(GirlEntity.IS_ANCHORED)) {
            girl.setPositionAndRotationDirect(
                    girl.getTargetPosition().x,
                    girl.getTargetPosition().y,
                    girl.getTargetPosition().z,
                    girl.java_lang_Float_I(),
                    0.0f, 3, true
            );
        }

        if (girl.actionController != null) {
            ((GirlEntity)girl).actionController.transitionLengthTicks = girl.world instanceof FakeWorld || girl.currentAction() == null ? 5.0 : (double) girl.currentAction().transitionTick;
        }

        this.processHeadLookRotation(girl, processor, animationEvent);
        if (!(girl instanceof Fighter) || girl.boolean_h() || girl.getOutfitIndex() == 0) {
            this.resetArmorPartVisibility(processor);
        } else {
            this.updateArmorPartVisibility(processor, girl.entityDataManager.get(Fighter.HELMET_SLOT), girl.entityDataManager.get(Fighter.CHEST_SLOT), girl.entityDataManager.get(Fighter.LEGS_SLOT), girl.entityDataManager.get(Fighter.BOOTS_SLOT));
        }
    }

    @CheckReturnValue
    public static Vec3d calculateMovementDelta(GirlEntity girl) {
        return GirlModel.calculateVectorDelta(new Vec3d(girl.lastTickPosX, girl.lastTickPosY, girl.lastTickPosZ), girl.getPositionVector());
    }

    @CheckReturnValue
    public static Vec3d calculateVectorDelta(GirlEntity girl, Vec3d target) {
        return GirlModel.calculateVectorDelta(target, girl.getPositionVector());
    }

    @CheckReturnValue
    public static Vec3d calculateVectorDelta(Vec3d start, Vec3d end) {
        float yaw;
        float roll;
        Vec3d diff = end.subtract(start);
        Vec3d absDiff = new Vec3d(Math.abs(diff.x), Math.abs(diff.y), Math.abs(diff.z));

        double ratioX = absDiff.x / (absDiff.x + absDiff.y + absDiff.z);
        double ratioY = absDiff.y / (absDiff.x + absDiff.y + absDiff.z);
        double ratioZ = absDiff.z / (absDiff.x + absDiff.y + absDiff.z);

        Vec3d normalized = new Vec3d(
                (double)(diff.x > 0.0 ? 1 : -1) * ratioX,
                (double)(diff.y > 0.0 ? 1 : -1) * ratioY,
                (double)(diff.z > 0.0 ? 1 : -1) * ratioZ
        );

        double halfY = normalized.y / 2.0 + 0.5;

        float pitch = (float) Reference.LerpDouble(-180.0, 0.0, halfY);
        if (Float.isNaN(pitch)) {
            pitch = -90.0f;
        }
        roll = halfY < 0.5 ? 0.0f : (float) Reference.LerpDouble(0.0, 16.0, -halfY);
        if (Float.isNaN(roll)) {
            roll = 0.0f;
        }

        if (Float.isNaN(yaw = (float)(4.0 - Math.sin(1.5707963267948966 + halfY * 2.0 * Math.PI) * 4.0))) {
            yaw = 8.0f;
        }

        return new Vec3d(TrigMath.toRadians(pitch), roll, yaw);
    }

    void updateArmorPartVisibility(AnimationProcessor<T> processor, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.setHeadArmorVisible(processor, !helmet.isEmpty());
        this.setChestArmorVisible(processor, chestplate.getItem() instanceof ItemArmor);
        this.setLegsArmorVisible(processor, !leggings.isEmpty());
        this.setBootsArmorVisible(processor, !boots.isEmpty());
    }

    protected void resetArmorPartVisibility(AnimationProcessor<T> processor) {
        this.setHeadArmorVisible(processor, false);
        this.setChestArmorVisible(processor, false);
        this.setLegsArmorVisible(processor, false);
        this.setBootsArmorVisible(processor, false);
    }

    void setHeadArmorVisible(AnimationProcessor<T> processor, boolean show) {
        this.toggleBonesByName(this.HeadArmor(), show, processor);
        this.toggleBonesByName(this.Attachments(), !show, processor);
    }

    void setChestArmorVisible(AnimationProcessor<T> processor, boolean show) {
        this.toggleBonesByName(this.TopArmor(), show, processor);
        this.toggleBonesByName(this.Top(), !show, processor);
    }

    void setLegsArmorVisible(AnimationProcessor<T> processor, boolean show) {
        this.toggleBonesByName(this.BottomArmor(), show, processor);
        this.toggleBonesByName(this.Bottom(), !show, processor);
    }

    void setBootsArmorVisible(AnimationProcessor<T> processor, boolean show) {
        this.toggleBonesByName(this.ShoesArmor(), show, processor);
        this.toggleBonesByName(this.Feet(), !show, processor);
    }

    void toggleBonesByName(String[] boneNames, boolean visible, AnimationProcessor<T> processor) {
        for (String name : boneNames) {
            this.HideBone(name, visible, processor);
        }
    }

    void HideBone(String boneName, boolean hidden, AnimationProcessor<T> processor) {
        if (processor.getBone(boneName) == null) {
            return;
        }
        processor.getBone(boneName).setHidden(!hidden);
    }

    @CheckReturnValue
    protected boolean isSteveSkinType(T girl) {
        UUID ownerUUID = girl.getID();
        if (ownerUUID == null) {
            return true;
        }
        World world = girl.world;

        AbstractClientPlayer ownerPlayer = (AbstractClientPlayer)world.getPlayerEntityByUUID(ownerUUID);
        if (ownerPlayer == null) {
            return true;
        }
        return "default".equals(ownerPlayer.getSkinType());
    }

    void updateArmModelType(T girl, AnimationProcessor<T> processor) {
        boolean isSteve = this.isSteveSkinType(girl);
        processor.getBone("rightArmAlex").setHidden(isSteve);
        processor.getBone("rightLowerArmAlex").setHidden(isSteve);
        processor.getBone("rightArmSteve").setHidden(!isSteve);
        processor.getBone("rightLowerArmSteve").setHidden(!isSteve);
        processor.getBone("leftArmAlex").setHidden(isSteve);
        processor.getBone("leftLowerArmAlex").setHidden(isSteve);
        processor.getBone("leftArmSteve").setHidden(!isSteve);
        processor.getBone("leftLowerArmSteve").setHidden(!isSteve);
        IBone steveBone = processor.getBone("steve");
        if (steveBone != null) {
            steveBone.setHidden(!girl.currentAction().hasPlayer);
        }
    }

    @CheckReturnValue
    protected boolean shouldEnableHeadRotation(GirlEntity girl) {
        return true;
    }

    protected void processHeadLookRotation(T girl, AnimationProcessor<T> processor, AnimationEvent<T> event) {
        if (girl.world instanceof FakeWorld) {
            return;
        }

        if (!this.shouldEnableHeadRotation(girl)) {
            return;
        }

        if (girl.currentAction() != Action.NULL && girl.currentAction() != Action.ATTACK && girl.currentAction() != Action.BOW) {
            return;
        }

        EntityModelData modelData = event.getExtraDataOfType(EntityModelData.class).get(0);
        
        IBone neck = processor.getBone("neck");
        neck.setRotationY(modelData.netHeadYaw * 0.5f * ((float)Math.PI / 180));
        
        IBone head = processor.getBone("head");
        head.setRotationY(modelData.netHeadYaw * ((float)Math.PI / 180));
        head.setRotationX(modelData.headPitch * ((float)Math.PI / 180));
        
        IBone body = processor.getBone("body") == null ? processor.getBone("dd") : processor.getBone("body");
        body.setRotationY(0.0f);
    }

    @CheckReturnValue
    public ItemStack getArmorStackForBone(GirlEntity girl, String boneName) {
        if (Arrays.asList(this.HeadArmor()).contains(boneName)) {
            return girl.entityDataManager.get(Fighter.HELMET_SLOT);
        }
        if (Arrays.asList(this.TopArmor()).contains(boneName)) {
            return girl.entityDataManager.get(Fighter.CHEST_SLOT);
        }
        if (Arrays.asList(this.BottomArmor()).contains(boneName)) {
            return girl.entityDataManager.get(Fighter.LEGS_SLOT);
        }
        if (Arrays.asList(this.ShoesArmor()).contains(boneName)) {
            return girl.entityDataManager.get(Fighter.BOOTS_SLOT);
        }
        return ItemStack.EMPTY;
    }

    //@Override
    //public void setLivingAnimations(IAnimatable iAnimatable, Integer n, AnimationEvent animationEvent) {
    //    this.a((em_class258)iAnimatable, n, animationEvent);
    //}

    //@Override
    //public void setLivingAnimations(Object object, Integer n, AnimationEvent animationEvent) {
    //    this.a((em_class258)object, n, animationEvent);
    //}

    // TODO override to accept EntityGirl
    //@Override
    //public void setLivingAnimations(GirlEntity iAnimatable, Integer n, AnimationEvent animationEvent) {
    //    this.a(iAnimatable, n, animationEvent);
    //}

    //@Override
    //public ResourceLocation getAnimationFileLocation(GirlEntity object) {
    //    return this.n_et_minecraft_util_ResourceLocation_c((GirlEntity)object);
    //}

    //@Override
    //public ResourceLocation getTextureLocation(GirlEntity object) {
    //    return this.net_minecraft_util_ResourceLocation_g((GirlEntity)object);
    //}

    //@Override
    //public ResourceLocation getModelLocation(GirlEntity object) {
    //    return this.net_minecraft_util_ResourceLocation_a((GirlEntity)object);
    //}
}

