/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Goblin;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlModel;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.util.interfaces.IGoblin;
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

public class GoblinModel extends GirlModel<GirlEntity> {
    final float legSwingAngle = 60.0f;
    Minecraft mc3 = Minecraft.getMinecraft();

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
    protected boolean canRender(GirlEntity girl) {
        if (!(girl instanceof GoblinEntity)) {
            return super.canRender(girl);
        }

        GoblinEntity goblin = (GoblinEntity) girl;
        UUID uuid = goblin.getInteractionPlayerUUID();
        if (uuid == null) {
            uuid = goblin.getOwnerUUID();
        }
        if (uuid == null) {
            return true;
        }
        World world = goblin.world;
        AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)world.getPlayerEntityByUUID(uuid);
        if (abstractClientPlayer == null) {
            return true;
        }
        return "default".equals(abstractClientPlayer.getSkinType());
    }

    @Override
    public void setLivingAnimations(GirlEntity girl, Integer instanceID, AnimationEvent event) {
        super.setLivingAnimations(girl, instanceID, event);
        if (!(girl.world instanceof FakeWorld)) {
            AnimationProcessor processor = this.getAnimationProcessor();
            boolean isGoblin = girl instanceof GoblinEntity;
            IBone preggy = processor.getBone("preggy");
            preggy.setHidden(!girl.getDataManager().get(GoblinEntity.IS_PREGNANT));
            IBone body = processor.getBone("body");
            IBone head = processor.getBone("head");
            Action action = girl.getCurrentAction();

            if ((action == Action.BREEDING_SLOW_2 || action == Action.BREEDING_FAST_2 || action == Action.BREEDING_CUM_2) && this.mc3.gameSettings.thirdPersonView == 0) {
                body.setPositionY(body.getPositionY() + 1.5f);
            }

            IGoblin goblin = (IGoblin) girl;
            if (isGoblin && action == Action.AWAIT_PICK_UP || action == Action.VANISH) {
                this.updateBoneLook(girl, body, head);
            }
            if (isGoblin && action == Action.SIT) {
                this.updateBoneLook2(girl, head);
            }

            if (action == Action.START_THROWING) {
                if (this.mc3.player.getPersistentID().equals(goblin.getOwnerUUID())) {
                    this.applyGoblinBone(body, processor, girl, goblin);
                } else {
                    this.applyBoneState(body, processor, girl);
                }
            } else {
                body.setHidden(false);
            }
            if (!body.isHidden() && action == Action.START_THROWING || action == Action.THROWN) {
                Vec3d interpolated = GoblinModel.calculateMovementDelta(girl);
                body.setRotationX((float) interpolated.x);
                body.setPositionY((float) interpolated.y);
                body.setPositionZ((float) interpolated.z);
            }
            if (action == Action.START_THROWING || action == Action.PICK_UP) {
                this.updateThrowPose(processor, goblin, girl);
            }
            if (!isGoblin) {
                this.updateWalkPose(processor, girl);
                this.updateIdlePose(processor, girl);
            }
        }
    }

    void updateIdlePose(AnimationProcessor<GirlEntity> processor, GirlEntity girl) {
        if (girl.getCurrentAction() == Action.START_THROWING) {
            if (this.mc3.gameSettings.thirdPersonView == 0 && this.mc3.player.getPersistentID().equals(((PlayerGirl) girl).getOwnerUserUUID())) {
                IBone iBone = processor.getBone("body");
                if (iBone != null) {
                    iBone.setHidden(true);
                }
            }
        }
    }

    void updateWalkPose(AnimationProcessor processor, GirlEntity girl) {
        if (girl.getCurrentAction() == Action.PICK_UP) {
            if (this.mc3.gameSettings.thirdPersonView == 0 && this.mc3.player.getPersistentID().equals(((IGoblin) girl).getOwnerUUID())) {
                return;
            }
            IBone iBone = processor.getBone("body");
            if (iBone != null) {
                IBone iBone2 = processor.getBone("steve");
                if (iBone2 != null) {
                    iBone.setPositionY(iBone.getPositionY() - 32.0f);
                    iBone2.setPositionY(iBone2.getPositionY() - 32.0f);
                }
            }
        }
    }

    void updateThrowPose(AnimationProcessor processor, IGoblin goblin, GirlEntity girl) {
        UUID uUID = goblin.getOwnerUUID();
        if (uUID == null) {
            girl.getInteractionPlayerUUID();
        }
        if (uUID != null) {
            EntityPlayer player = girl.world.getPlayerEntityByUUID(uUID);
            if (player != null) {
                float limbSwingAmount = RotationHelper.LerpFloat(player.prevLimbSwingAmount, player.limbSwingAmount, this.mc3.getRenderPartialTicks());
                float limbSwing = player.limbSwing;
                float swingSin = (float) Math.sin(limbSwing);
                IBone leftLeg = processor.getBone("LeftLeg");
                IBone rightLeg = processor.getBone("RightLeg");
                float swingAngle = TrigMath.wrapDegrees(60.0f * swingSin * limbSwingAmount);
                leftLeg.setRotationX(swingAngle);
                rightLeg.setRotationX(-swingAngle);
            }
        }
    }

    void updateBoneLook2(GirlEntity girl, IBone bone) {
        EntityPlayer player = girl.world.getClosestPlayerToEntity(girl, 15.0);
        if (player != null) {
            Vec3d playerPos = player.getPositionVector();
            Vec3d girlPos = girl.getPositionVector();
            Vec3d delta = playerPos.subtract(girlPos);
            float yaw = girl.rotationYaw;
            boolean inFront = false;

            switch ((int) yaw) {
                case 0: {
                    inFront = playerPos.z > girlPos.z;
                    break;
                }
                case 180: {
                    inFront = playerPos.z < girlPos.z;
                    break;
                }
                case 90: {
                    inFront = playerPos.x < girlPos.x;
                    break;
                }
                case -90: {
                    inFront = playerPos.x > girlPos.x;
                }
            }
            if (!inFront) {
                bone.setRotationY(0.0f);
            } else {
                float facingOffset = 0.0f;
                switch ((int) yaw) {
                    case 180: {
                        facingOffset = 90.0f;
                        break;
                    }
                    case 90: {
                        facingOffset = 180.0f;
                        break;
                    }
                    case 0: {
                        facingOffset = -90.0f;
                    }
                }
                float yawAngle = (float) (-(MathHelper.atan2(delta.z, delta.x) * 57.29577951308232 + (double) facingOffset));
                float pitch = ThreadNames.clamp((float) ((double) player.getEyeHeight() + playerPos.y - ((double) girl.getEyeHeight() + girlPos.y)), -0.75f, 0.75f);
                bone.setRotationY(TrigMath.wrapDegrees(yawAngle));
                bone.setRotationX(pitch);
            }
        }
    }

    void updateBoneLook(GirlEntity girl, IBone body, IBone head) {
        EntityPlayer player = girl.world.getClosestPlayerToEntity(girl, 15.0);
        if (player != null) {
            Vec3d vec3d = player.getPositionVector();
            Vec3d vec3d2 = girl.getPositionVector();
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            float f = (float) (-(Math.atan2(vec3d3.z, vec3d3.x) * 57.29577951308232)) + 90.0f;
            float f2 = ThreadNames.clamp((float) ((double) player.getEyeHeight() + vec3d.y - ((double) girl.getEyeHeight() + vec3d2.y)), -0.75f, 0.75f);
            body.setRotationY(TrigMath.wrapDegrees(f));
            head.setRotationX(f2);
        }
    }

    void applyBoneState(IBone iBone, AnimationProcessor processor, GirlEntity girl) {
        if (girl.isLocallyRegistered()) {
            iBone.setHidden(true);
        } else {
            iBone.setHidden(false);
            processor.getBone("steve").setHidden(true);
        }
    }

    void applyGoblinBone(IBone iBone, AnimationProcessor processor, GirlEntity girl, IGoblin goblin) {
        if (girl.isLocallyRegistered()) {
            iBone.setHidden(true);
        } else {
            iBone.setHidden(goblin.getThrowProgress() < 15);
        }
        if (!girl.isLocallyRegistered()) {
            processor.getBone("steve").setHidden(true);
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
}

