/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Ellie;

import java.util.HashMap;

import com.trolmastercard.sexmod.Action;
import com.trolmastercard.sexmod.FakeWorld;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.be_class78;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlModel;
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
        this.c = this.getAnimationResource();
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
    public ResourceLocation getAnimationFileLocation(GirlEntity girlEntity) {
        return new ResourceLocation("sexmod", "animations/ellie/ellie.animation.json");
    }

    @Override
    public void setLivingAnimations(GirlEntity em_class2582, Integer n, AnimationEvent animationEvent) {
        float f;
        float f2;
        super.setLivingAnimations(em_class2582, n, animationEvent);
        if (em_class2582.world instanceof FakeWorld) {
            return;
        }
        if (em_class2582 instanceof PlayerGirl) {
            return;
        }
        if (em_class2582.currentAction() != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = em_class2582.world.getClosestPlayerToEntity(em_class2582, 15.0);
        if (entityPlayer == null) {
            return;
        }
        IBone iBone = this.getAnimationProcessor().getBone("head");
        Vec3d vec3d = em_class2582.getPositionVector().subtract(entityPlayer.getPositionVector());
        int n2 = Math.round(em_class2582.java_lang_Float_I().floatValue());
        if (n2 == 180) {
            f2 = (float)Math.atan2(vec3d.x, vec3d.z) * 1.2f;
            f2 = f2 > 0.0f ? Math.max(1.5f, Math.min(3.14f, f2)) : Math.max(-3.14f, Math.min(-1.5f, f2));
            f2 = f2 == 1.5f || f2 == 3.14f || f2 == -3.14f || f2 == -1.5f ? 0.0f : (f2 += 3.0f);
        } else {
            f = this.f.get(n2)[1];
            float f3 = this.f.get(n2)[2];
            f2 = ((float)(Math.atan2(vec3d.x, vec3d.z) + (double)this.f.get(n2)[0]) + em_class2582.java_lang_Float_I().floatValue()) * 0.8f;
            if ((f2 = be_class78.b(f2, f, f3)) == f || f2 == f3) {
                f2 = 0.0f;
            }
        }
        f = f2 == 0.0f ? 0.0f : be_class78.b((float)((entityPlayer.posY - em_class2582.posY) * 0.5), -0.75f, 0.75f);
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

