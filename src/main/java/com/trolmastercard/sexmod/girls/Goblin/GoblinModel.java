/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Goblin;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;

public class GoblinModel
extends GirlModel<GirlEntity> {
    final float g = 60.0f;
    Minecraft f = Minecraft.getMinecraft();

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{
                new ResourceLocation("sexmod", "geo/goblin/goblin.geo.json"),
                new ResourceLocation("sexmod", "geo/goblin/armored.geo.json")
        };
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return new ResourceLocation("sexmod", "textures/entity/goblin/goblin.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girl) {
        return new ResourceLocation("sexmod", "animations/goblin/goblin.animation.json");
    }

    @Override
    protected boolean isSteveSkinType(GirlEntity girl) {
        if (!(girl instanceof GoblinEntity)) {
            return super.isSteveSkinType(girl);
        }
        GoblinEntity e3_class2192 = (GoblinEntity) girl;
        UUID uUID = e3_class2192.getID();
        if (uUID == null) {
            uUID = e3_class2192.java_util_UUID_e();
        }
        if (uUID == null) {
            return true;
        }
        World world = e3_class2192.world;
        AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)world.getPlayerEntityByUUID(uUID);
        if (abstractClientPlayer == null) {
            return true;
        }
        return "default".equals(abstractClientPlayer.getSkinType());
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);
        if (girl.world instanceof FakeWorld) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        boolean bl = girl instanceof GoblinEntity;
        IBone iBone = animationProcessor.getBone("preggy");
        iBone.setHidden(girl.getDataManager().get(GoblinEntity.aV) == false);
        IBone iBone2 = animationProcessor.getBone("body");
        IBone iBone3 = animationProcessor.getBone("head");
        Action fp_class3242 = girl.currentAction();
        if ((fp_class3242 == Action.BREEDING_SLOW_2 || fp_class3242 == Action.BREEDING_FAST_2 || fp_class3242 == Action.BREEDING_CUM_2) && this.f.gameSettings.thirdPersonView == 0) {
            iBone2.setPositionY(iBone2.getPositionY() + 1.5f);
        }
        ai_class30 ai_class302 = (ai_class30)((Object) girl);
        if (bl && fp_class3242 == Action.AWAIT_PICK_UP || fp_class3242 == Action.VANISH) {
            this.a(girl, iBone2, iBone3);
        }
        if (bl && fp_class3242 == Action.SIT) {
            this.a(girl, iBone3);
        }
        if (fp_class3242 == Action.START_THROWING) {
            if (this.f.player.getPersistentID().equals(ai_class302.java_util_UUID_e())) {
                this.a(iBone2, animationProcessor, girl, ai_class302);
            } else {
                this.a(iBone2, animationProcessor, girl);
            }
        } else {
            iBone2.setHidden(false);
        }
        if (!iBone2.isHidden() && fp_class3242 == Action.START_THROWING || fp_class3242 == Action.THROWN) {
            Vec3d vec3d = GoblinModel.calculateMovementDelta(girl);
            iBone2.setRotationX((float)vec3d.x);
            iBone2.setPositionY((float)vec3d.y);
            iBone2.setPositionZ((float)vec3d.z);
        }
        if (fp_class3242 == Action.START_THROWING || fp_class3242 == Action.PICK_UP) {
            this.a(animationProcessor, ai_class302, girl);
        }
        if (!bl) {
            this.b(animationProcessor, girl);
            this.a(animationProcessor, girl);
        }
    }

    void a(AnimationProcessor<GirlEntity> animationProcessor, GirlEntity em_class2582) {
        if (em_class2582.currentAction() != Action.START_THROWING) {
            return;
        }
        if (this.f.gameSettings.thirdPersonView != 0 || !this.f.player.getPersistentID().equals(((PlayerGirl)em_class2582).getOwnerUserUUID())) {
            return;
        }
        IBone iBone = animationProcessor.getBone("body");
        if (iBone == null) {
            return;
        }
        iBone.setHidden(true);
    }

    void b(AnimationProcessor animationProcessor, GirlEntity em_class2582) {
        if (em_class2582.currentAction() != Action.PICK_UP) {
            return;
        }
        if (this.f.gameSettings.thirdPersonView == 0 && this.f.player.getPersistentID().equals(((ai_class30)((Object)em_class2582)).java_util_UUID_e())) {
            return;
        }
        IBone iBone = animationProcessor.getBone("body");
        if (iBone == null) {
            return;
        }
        IBone iBone2 = animationProcessor.getBone("steve");
        if (iBone2 == null) {
            return;
        }
        iBone.setPositionY(iBone.getPositionY() - 32.0f);
        iBone2.setPositionY(iBone2.getPositionY() - 32.0f);
    }

    void a(AnimationProcessor animationProcessor, ai_class30 ai_class302, GirlEntity em_class2582) {
        UUID uUID = ai_class302.java_util_UUID_e();
        if (uUID == null) {
            em_class2582.getID();
        }
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        float f = Reference.LerpFloat(entityPlayer.prevLimbSwingAmount, entityPlayer.limbSwingAmount, this.f.getRenderPartialTicks());
        float f2 = entityPlayer.limbSwing;
        float f3 = (float)Math.sin(f2);
        IBone iBone = animationProcessor.getBone("LeftLeg");
        IBone iBone2 = animationProcessor.getBone("RightLeg");
        float f4 = TrigMath.toRadians(60.0f * f3 * f);
        iBone.setRotationX(f4);
        iBone2.setRotationX(-f4);
    }

    void a(GirlEntity em_class2582, IBone iBone) {
        EntityPlayer entityPlayer = em_class2582.world.getClosestPlayerToEntity(em_class2582, 15.0);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = entityPlayer.getPositionVector();
        Vec3d vec3d2 = em_class2582.getPositionVector();
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        float f = em_class2582.rotationYaw;
        boolean bl = false;
        switch ((int)f) {
            case 0: {
                bl = vec3d.z > vec3d2.z;
                break;
            }
            case 180: {
                bl = vec3d.z < vec3d2.z;
                break;
            }
            case 90: {
                bl = vec3d.x < vec3d2.x;
                break;
            }
            case -90: {
                boolean bl2 = bl = vec3d.x > vec3d2.x;
            }
        }
        if (!bl) {
            iBone.setRotationY(0.0f);
            return;
        }
        float f2 = 0.0f;
        switch ((int)f) {
            case 180: {
                f2 = 90.0f;
                break;
            }
            case 90: {
                f2 = 180.0f;
                break;
            }
            case 0: {
                f2 = -90.0f;
            }
        }
        float f3 = (float)(-(MathHelper.atan2(vec3d3.z, vec3d3.x) * 57.29577951308232 + (double)f2));
        float f4 = Utils.clamp((float)((double)entityPlayer.getEyeHeight() + vec3d.y - ((double)em_class2582.getEyeHeight() + vec3d2.y)), -0.75f, 0.75f);
        iBone.setRotationY(TrigMath.toRadians(f3));
        iBone.setRotationX(f4);
    }

    void a(GirlEntity em_class2582, IBone iBone, IBone iBone2) {
        EntityPlayer entityPlayer = em_class2582.world.getClosestPlayerToEntity(em_class2582, 15.0);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = entityPlayer.getPositionVector();
        Vec3d vec3d2 = em_class2582.getPositionVector();
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        float f = (float)(-(Math.atan2(vec3d3.z, vec3d3.x) * 57.29577951308232)) + 90.0f;
        float f2 = Utils.clamp((float)((double)entityPlayer.getEyeHeight() + vec3d.y - ((double)em_class2582.getEyeHeight() + vec3d2.y)), -0.75f, 0.75f);
        iBone.setRotationY(TrigMath.toRadians(f));
        iBone2.setRotationX(f2);
    }

    void a(IBone iBone, AnimationProcessor animationProcessor, GirlEntity em_class2582) {
        if (em_class2582.isLocallyRegistered()) {
            iBone.setHidden(true);
        } else {
            iBone.setHidden(false);
            animationProcessor.getBone("steve").setHidden(true);
        }
    }

    void a(IBone iBone, AnimationProcessor animationProcessor, GirlEntity em_class2582, ai_class30 ai_class302) {
        if (em_class2582.isLocallyRegistered()) {
            iBone.setHidden(true);
        } else {
            iBone.setHidden(ai_class302.int_a() < 15);
        }
        if (!em_class2582.isLocallyRegistered()) {
            animationProcessor.getBone("steve").setHidden(true);
        }
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    @Override
    public String[] TopArmor() {
        return new String[]{"armorBoobL", "armorBoobR"};
    }

    @Override
    public String[] Top() {
        return new String[]{"nippleL", "nippleR"};
    }

    @Override
    public String[] BottomArmor() {
        return new String[]{"armorCheekR", "armorCheekL", "armorLegL", "armorLegR", "armorShinL", "armorShinR", "armorTorso"};
    }

    @Override
    public String[] Bottom() {
        return new String[]{"fuckhole", "vagina", "meatCheekR", "meatCheekL", "meatLegL", "meatLegR", "meatShinL", "meatShinR"};
    }

    @Override
    public String[] ShoesArmor() {
        return new String[]{"armorFootL", "armorFootR"};
    }

    @Override
    public String[] Feet() {
        return new String[]{"meatFootL", "meatFootR"};
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

