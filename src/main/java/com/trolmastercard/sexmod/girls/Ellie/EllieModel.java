/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Ellie;

import java.util.HashMap;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class EllieModel extends GirlModel<GirlEntity> {
    HashMap<Integer, float[]> headYawOffsets = new HashMap<Integer, float[]>(){
        {
            this.put(0, new float[]{0.0f, -1.2f, 1.2f});
            this.put(-90, new float[]{2.0f, -71.56f, -68.0f});
            this.put(90, new float[]{-2.0f, 68.0f, 70.5f});
        }
    };

    public EllieModel() {
        this.modelLocations = this.getAnimationResource();
    }

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{
                new ResourceLocation("sexmod", "geo/ellie/nude.geo.json"),
                new ResourceLocation("sexmod", "geo/ellie/dressed.geo.json")};
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/ellie/ellie.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/ellie/ellie.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        float minClamp;
        float headYaw;
        super.setLivingAnimations(girl, instanceID, event);
        if (!(girl.world instanceof FakeWorld)) {
            if (!(girl instanceof PlayerGirl)) {
                if (girl.getCurrentAction() == Action.SITDOWNIDLE) {
                    EntityPlayer player = girl.world.getClosestPlayerToEntity(girl, 15.0);
                    if (player != null) {
                        IBone headBone = this.getAnimationProcessor().getBone("head");
                        Vec3d toPlayer = girl.getPositionVector().subtract(player.getPositionVector());
                        int facingYaw = Math.round(girl.getYawRotation());
                        if (facingYaw == 180) {
                            headYaw = (float) Math.atan2(toPlayer.x, toPlayer.z) * 1.2f;
                            headYaw = headYaw > 0.0f ? Math.max(1.5f, Math.min(3.14f, headYaw)) : Math.max(-3.14f, Math.min(-1.5f, headYaw));
                            headYaw = headYaw == 1.5f || headYaw == 3.14f || headYaw == -3.14f || headYaw == -1.5f ? 0.0f : (headYaw += 3.0f);
                        } else {
                            minClamp = this.headYawOffsets.get(facingYaw)[1];
                            float maxClamp = this.headYawOffsets.get(facingYaw)[2];
                            headYaw = ((float) (Math.atan2(toPlayer.x, toPlayer.z) + (double) this.headYawOffsets.get(facingYaw)[0]) + girl.getYawRotation()) * 0.8f;
                            if ((headYaw = ThreadNames.clamp(headYaw, minClamp, maxClamp)) == minClamp || headYaw == maxClamp) {
                                headYaw = 0.0f;
                            }
                        }
                        minClamp = headYaw == 0.0f ? 0.0f : ThreadNames.clamp((float) ((player.posY - girl.posY) * 0.5), -0.75f, 0.75f);
                        headBone.setRotationY(headYaw);
                        headBone.setRotationX(minClamp);
                    }
                }
            }
        }
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    @Override
    public String[] Attachments() {
        return new String[]{"headband"};
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
        return new String[]{"fleshL", "fleshR", "vagina", "hotpants", "slip", "curvesL", "curvesR", "kneeL", "kneeR"};
    }

    @Override
    public String[] ShoesArmor() {
        return new String[]{"armorShoesL", "armorShoesR"};
    }
}

