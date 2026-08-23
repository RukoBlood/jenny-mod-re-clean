/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Slime;

import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Slime.friendlySlime.FriendlySlimeEntity;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class SlimeEntity extends GirlEntity {
    final static double Q = 0.7f;
    final static float W = 0.9f;
    final static double M = 100.0;
    final static float L = 0.1f;
    final static int O = 2400;
    SlimeActions slimeMovementState = SlimeActions.IDLE;
    static public DataParameter<Integer> TicksUntilBirth = EntityDataManager.createKey(SlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(113);
    static public DataParameter<Float> TARGET_YAW = EntityDataManager.createKey(SlimeEntity.class, DataSerializers.FLOAT).getSerializer().createKey(112);
    static public DataParameter<Integer> HornyLevel = EntityDataManager.createKey(SlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    int jumpTicks = 0;
    boolean wasOnGroundLastTick = true;
    boolean shouldIncreaseHorny = false;
    int performJump = 0;

    public SlimeEntity(World world) {
        super(world);
    }

    @Override
    public String getGirlName() {
        return "Slime";
    }

    @Override
    public float getScaleFactor() {
        return 1.6f;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.CUMBLOWJOB && (action == Action.THRUSTBLOWJOB || action == Action.SUCKBLOWJOB)) {
            return;
        }
        if (this.getCurrentAction() == Action.DOGGYCUM && (action == Action.DOGGYFAST || action == Action.DOGGYSLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean shouldRenderNameTag() {
        return false;
    }

    @Override
    protected void initEntityAI() {
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(HornyLevel, 0);
        this.getDataManager().register(TARGET_YAW, 0.0f);
        this.getDataManager().register(TicksUntilBirth, -1);
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB) {
            return Action.CUMBLOWJOB;
        }
        if (action == Action.DOGGYSLOW || action == Action.DOGGYFAST) {
            return Action.DOGGYCUM;
        }
        return null;
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.SUCKBLOWJOB) {
            return Action.THRUSTBLOWJOB;
        }
        if (action == Action.DOGGYSLOW) {
            return Action.DOGGYFAST;
        }
        return null;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return 0.9f;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("hornyLevel", this.entityDataManager.get(HornyLevel));
        nbt.setInteger("ticksUntilBirth", this.entityDataManager.get(TicksUntilBirth));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.entityDataManager.set(HornyLevel, nbt.getInteger("hornyLevel"));
        this.entityDataManager.set(TicksUntilBirth, nbt.getInteger("ticksUntilBirth"));
        if (this.entityDataManager.get(HornyLevel) != 0) {
            this.entityDataManager.set(OUTFIT_INDEX, 0);
        }
        this.noClip = false;
        this.setNoGravity(false);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.SLIME_LOOT_TABLE;
    }

    @Override
    public void reInitTasks() {
        this.entityDataManager.set(HornyLevel, 0);
        this.entityDataManager.set(OUTFIT_INDEX, 1);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.checkInteractionTrigger();
        this.SpawnFriendlySlime();
        if (this.isPotionActive(HornyPotion.HORNY_POTION) && this.slimeMovementState == SlimeActions.IDLE && this.entityDataManager.get(TicksUntilBirth) == -1) {
            this.entityDataManager.set(HornyLevel, 2);
            if (this.entityDataManager.get(OUTFIT_INDEX) == 1) {
                this.setCurrentAction(Action.UNDRESS);
            }
            this.removePotionEffect(HornyPotion.HORNY_POTION);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getCurrentAction() == Action.NULL) {
            this.updateSlimeMovementAndState();
        }
        if (this.entityDataManager.get(HornyLevel) >= 2 && this.ticksExisted % 10 == 0) {
            SlimeEntity.spawnParticlesAround(EnumParticleTypes.HEART, this);
        }
        if (this.world.isRemote) {
            this.spawnBirthParticlesClient();
            this.updateClientPlayerPosition();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void updateClientPlayerPosition() {
        if (this.getInteractionPlayerUUID() == null) {
            return;
        }
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        if (!this.getInteractionPlayerUUID().equals(clientPlayer.getPersistentID())) {
            return;
        }
        Vec3d offset = this.getPositionVector();
        Vec3d targetPos = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 0.65f), this.getYawRotation());
        offset = offset.add(targetPos);
        clientPlayer.setPosition(offset.x, offset.y, offset.z);
        clientPlayer.setVelocity(0.0, 0.0, 0.0);
    }

    void spawnBirthParticlesClient() {
        int ticks_birth = this.entityDataManager.get(TicksUntilBirth);
        if (ticks_birth == -1) {
            return;
        }
        SlimeEntity.spawnParticlesAround(EnumParticleTypes.SPELL_WITCH, this);
        if (ticks_birth == 0) {
            this.PlaySound(SoundsHandler.MISC_PLOB[0]);
        }
    }

    void SpawnFriendlySlime() {
        int ticks = this.entityDataManager.get(TicksUntilBirth);
        if (ticks == -1) {
            return;
        }
        this.entityDataManager.set(TicksUntilBirth, ticks - 1);
        if (--ticks >= 0) {
            return;
        }
        FriendlySlimeEntity friendlySlime = new FriendlySlimeEntity(this.world);
        friendlySlime.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(friendlySlime);
        this.entityDataManager.set(TicksUntilBirth, -1);
    }

    void checkInteractionTrigger() {
        int hornyLevel = this.entityDataManager.get(HornyLevel);
        if (hornyLevel < 2) {
            return;
        }
        if (hornyLevel >= 4 && this.onGround && this.getCurrentAction() == Action.NULL) {
            this.setTargetPosition(this.getPositionVector());
            this.setYawRotation(this.rotationYaw);
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setNoGravity(true);
            this.noClip = true;
            this.setCurrentAction(Action.STARTDOGGY);
            return;
        }
        EntityPlayer player = this.world.getClosestPlayerToEntity(this, 1.0);
        if (player == null || !player.onGround || SlimeEntity.getActiveSceneInfo(player) != null) {
            return;
        }
        this.setTargetPosition(this.getPositionVector());
        this.setYawRotation(this.rotationYaw);
        this.entityDataManager.set(IS_ANCHORED, true);
        this.setNoGravity(true);
        this.noClip = true;
        player.setNoGravity(true);
        player.noClip = true;
        PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), (EntityPlayerMP)player);
        this.setInteractionPlayerUUID(player.getPersistentID());
        player.rotationYaw = this.getYawRotation();

        Vec3d offset = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 0.65f), this.getYawRotation());
        player.setPosition(this.posX + offset.x, this.posY, this.posZ + offset.z);
        if (this.getCurrentAction() == Action.WAITDOGGY) {
            this.setCurrentAction(Action.DOGGYSTART);
        } else {
            this.setCurrentAction(Action.SUCKBLOWJOB);
        }
    }

    void updateSlimeMovementAndState() {
        if (this.world.isRemote) {
            float yaw;
            if ((double)this.jumpTicks == 90.0) {
                this.slimeMovementState = SlimeActions.JUMP_START;
            }
            if (!this.wasOnGroundLastTick && this.onGround) {
                this.slimeMovementState = SlimeActions.JUMP_END;
                this.jumpTicks = 0;
            }
            this.rotationYaw = yaw = this.entityDataManager.get(TARGET_YAW);
            this.rotationYawHead = yaw;
            this.renderYawOffset = yaw;
        } else {
            if ((double)this.jumpTicks == 85.0) {
                this.entityDataManager.set(TARGET_YAW, this.calculateTargetYaw());
            }
            if ((double)this.jumpTicks == 100.0) {
                this.performJump();
            }
            if (!this.wasOnGroundLastTick && this.onGround) {
                boolean bl = this.shouldIncreaseHorny = this.entityDataManager.get(TicksUntilBirth) == -1 && this.getRNG().nextFloat() < 0.1f;
            }
            if (this.shouldIncreaseHorny && this.jumpTicks == 50) {
                int curHorny = this.entityDataManager.get(HornyLevel);
                int newHorny = curHorny + 1;
                this.entityDataManager.set(HornyLevel, newHorny);
                if (newHorny == 1) {
                    this.setCurrentAction(Action.UNDRESS);
                }
            }
        }
        if (this.onGround) {
            ++this.jumpTicks;
        }
        this.wasOnGroundLastTick = this.onGround;
    }

    void performJump() {
        float targetYaw;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.jump();
        this.rotationYaw = targetYaw = this.entityDataManager.get(TARGET_YAW);
        this.prevRotationYaw = targetYaw;
        Vec3d jumpVelocity = new Vec3d(0.0, 0.0, 0.7f);
        jumpVelocity = VectorMath.rotateByYaw(jumpVelocity, targetYaw);
        this.motionX = jumpVelocity.x;
        this.motionZ = jumpVelocity.z;
        this.jumpTicks = 0;
    }

    float calculateTargetYaw() {
        int hornyLevel = this.entityDataManager.get(HornyLevel);
        if (this.entityDataManager.get(TicksUntilBirth) != -1) {
            return this.getRandomYaw();
        }
        if (hornyLevel < 2) {
            return this.getRandomYaw();
        }
        EntityPlayer player = this.world.getClosestPlayerToEntity(this, 30.0);
        if (player == null) {
            return this.getRandomYaw();
        }
        if (SlimeEntity.getActiveSceneInfo(player) != null) {
            return this.getRandomYaw();
        }
        return (float)Math.atan2(this.posZ - player.posZ, this.posX - player.posX) * 57.29578f + 90.0f;
    }

    float getRandomYaw() {
        return Reference.RANDOM.nextFloat() * 360.0f;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() == Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.slime.null", true, event);
                    break;
                }
                this.createAnimation("animation.slime.fhappy", true, event);
                break;
            }
            case "action": {
                if (this.getCurrentAction() == Action.NULL) {
                    this.createAnimation(this.slimeMovementState.animationPath, true, event);
                    break;
                }
                switch (this.getCurrentAction()) {
                    case UNDRESS: {
                        this.createAnimation("animation.slime.undress", false, event);
                        break;
                    }
                    case DRESS: {
                        this.createAnimation("animation.slime.dress", false, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.slime.strip", false, event);
                        break;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobintro", false, event);
                        break;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobsuck", true, event);
                        break;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobthrust", true, event);
                        break;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobcum", false, event);
                        break;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.slime.doggygoonbed", false, event);
                        break;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.slime.doggywait", true, event);
                        break;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.slime.doggystart", false, event);
                        break;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.slime.doggyslow", true, event);
                        break;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.slime.doggyfast", true, event);
                        break;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.slime.doggycum", false, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController.ISoundListener soundListener  = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "undress": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.changeDataParameterFromClient("currentModel", "0");
                    this.setCurrentAction(Action.NULL);
                    break;
                }
                case "dress": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.entityDataManager.set(OUTFIT_INDEX, 1);
                    this.setCurrentAction(null);
                    this.resetCameraAndPhysics();
                    break;
                }
                case "becomeNude": {
                    this.entityDataManager.set(OUTFIT_INDEX, 0);
                    break;
                }
                case "sexUiOn": {
                    if (!this.isControlledByLocalPlayer() || SexUI.isVisible) break;
                    SexUI.showUI();
                    break;
                }
                case "bjiMSG10": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.moveCamera(-0.4, -0.8, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    this.playSoundAtVolume(SoundEvents.ENTITY_SLIME_SQUISH, 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (Reference.RANDOM.nextInt(5) == 0) {
                        this.playSoundAtVolume(SoundEvents.ENTITY_SLIME_JUMP, 0.5f);
                    }
                    this.playSoundAtVolume(SoundEvents.ENTITY_SLIME_SQUISH, 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjtMSG1": {
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    this.PlaySound(SoundEvents.ENTITY_SLIME_DEATH);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "bjiDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "bjtDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    break;
                }
                case "bjtReady": 
                case "doggyfastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "bjcMSG1": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                    break;
                }
                case "bjcMSG2": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.hide();
                    break;
                }
                case "doggyslowMSG2": {
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    break;
                }
                case "bjcBlackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "bjcDone": 
                case "doggyCumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    this.changeDataParameterFromClient("pregnant", String.valueOf(2400));
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                    this.cameraYaw = this.rotationYaw;
                    break;
                }
                case "doggyGoOnBedDone": {
                    this.setCurrentAction(Action.WAITDOGGY);
                    break;
                }
                case "doggystartMSG1": {
                    this.PlaySound(SoundsHandler.MISC_TOUCH[0]);
                    break;
                }
                case "doggystartMSG2": {
                    this.PlaySound(SoundsHandler.MISC_TOUCH[1]);
                    break;
                }
                case "doggystartMSG3": {
                    this.playSoundAtVolume(SoundEvents.ENTITY_SLIME_SQUISH, 0.25f);
                    break;
                }
                case "doggystartMSG4": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS), 1.5f);
                    break;
                }
                case "doggystartMSG5": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    break;
                }
                case "doggystartDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "doggyslowMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    int n = Reference.RANDOM.nextInt(4);
                    if (n == 0) {
                        n = Reference.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                        } else {
                            this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                        }
                    } else {
                        this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "doggyfastMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04);
                    }
                    ++this.performJump;
                    if (this.performJump % 2 == 0) {
                        int n = Reference.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                            break;
                        }
                        this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                        break;
                    }
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    break;
                }
                case "doggyfastDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    break;
                }
                case "doggycumMSG1": {
                    this.playSoundAtVolume(SoundsHandler.MISC_CUMINFLATION[0], 4.0f);
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundEvents.ENTITY_SLIME_DEATH);
                    break;
                }
                case "jumpStart": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                    break;
                }
                case "jumpStartDone": {
                    this.slimeMovementState = SlimeActions.JUMP_AIR;
                    break;
                }
                case "jumpEndSound": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                    break;
                }
                case "jumpEndDone": {
                    this.slimeMovementState = SlimeActions.IDLE;
                }
            }
        };
        this.actionController.registerSoundListener(soundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.eyesController);
    }

    enum SlimeActions {
        IDLE("animation.slime.idle"),
        JUMP_START("animation.slime.jumpstart"),
        JUMP_AIR("animation.slime.jumpair"),
        JUMP_END("animation.slime.jumpend");

        final String animationPath;

        public String getAnimationPath() {
            return this.animationPath;
        }

        SlimeActions(String animationPath) {
            this.animationPath = animationPath;
        }
    }
}

