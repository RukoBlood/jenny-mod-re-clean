/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Ellie;

import java.util.HashMap;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class EllieModel
extends GirlModel<GirlEntity> {
    HashMap<Integer, float[]> f = new HashMap<Integer, float[]>(){
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
        float f;
        float f2;
        super.setLivingAnimations(girl, instanceID, event);
        if (girl.world instanceof FakeWorld) {
            return;
        }
        if (girl instanceof PlayerGirl) {
            return;
        }
        if (girl.currentAction() != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = girl.world.getClosestPlayerToEntity(girl, 15.0);
        if (entityPlayer == null) {
            return;
        }
        IBone iBone = this.getAnimationProcessor().getBone("head");
        Vec3d vec3d = girl.getPositionVector().subtract(entityPlayer.getPositionVector());
        int n2 = Math.round(girl.getYawRotation().floatValue());
        if (n2 == 180) {
            f2 = (float)Math.atan2(vec3d.x, vec3d.z) * 1.2f;
            f2 = f2 > 0.0f ? Math.max(1.5f, Math.min(3.14f, f2)) : Math.max(-3.14f, Math.min(-1.5f, f2));
            f2 = f2 == 1.5f || f2 == 3.14f || f2 == -3.14f || f2 == -1.5f ? 0.0f : (f2 += 3.0f);
        } else {
            f = this.f.get(n2)[1];
            float f3 = this.f.get(n2)[2];
            f2 = ((float)(Math.atan2(vec3d.x, vec3d.z) + (double)this.f.get(n2)[0]) + girl.getYawRotation().floatValue()) * 0.8f;
            if ((f2 = Utils.clamp(f2, f, f3)) == f || f2 == f3) {
                f2 = 0.0f;
            }
        }
        f = f2 == 0.0f ? 0.0f : Utils.clamp((float)((entityPlayer.posY - girl.posY) * 0.5), -0.75f, 0.75f);
        iBone.setRotationY(f2);
        iBone.setRotationX(f);
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

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

