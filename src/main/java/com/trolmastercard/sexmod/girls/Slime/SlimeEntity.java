/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Slime;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class SlimeEntity
extends GirlEntity {
    final static double Q = (double)0.7f;
    final static float W = 0.9f;
    final static double M = 100.0;
    final static float L = 0.1f;
    final static int O = 2400;
    SlimeActions slimeActions = SlimeActions.IDLE;
    static public DataParameter<Integer> TicksUntilBirth = EntityDataManager.createKey(SlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(113);
    static public DataParameter<Float> R = EntityDataManager.createKey(SlimeEntity.class, DataSerializers.FLOAT).getSerializer().createKey(112);
    static public DataParameter<Integer> HornyLevel = EntityDataManager.createKey(SlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    int N = 0;
    boolean K = true;
    boolean V = false;
    int P = 0;

    public SlimeEntity(World world) {
        super(world);
    }

    @Override
    public String getGirlName() {
        return "Slime";
    }

    @Override
    public float float_i() {
        return 1.6f;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.currentAction() == Action.CUMBLOWJOB && (action == Action.THRUSTBLOWJOB || action == Action.SUCKBLOWJOB)) {
            return;
        }
        if (this.currentAction() == Action.DOGGYCUM && (action == Action.DOGGYFAST || action == Action.DOGGYSLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean boolean_t() {
        return false;
    }

    @Override
    protected void initEntityAI() {
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(HornyLevel, 0);
        this.getDataManager().register(R, Float.valueOf(0.0f));
        this.getDataManager().register(TicksUntilBirth, -1);
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB) {
            return Action.CUMBLOWJOB;
        }
        if (action == Action.DOGGYSLOW || action == Action.DOGGYFAST) {
            return Action.DOGGYCUM;
        }
        return null;
    }

    @Override
    protected Action FastSexAction(Action action) {
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
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("hornyLevel", this.entityDataManager.get(HornyLevel));
        nBTTagCompound.setInteger("ticksUntilBirth", this.entityDataManager.get(TicksUntilBirth));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.entityDataManager.set(HornyLevel, nBTTagCompound.getInteger("hornyLevel"));
        this.entityDataManager.set(TicksUntilBirth, nBTTagCompound.getInteger("ticksUntilBirth"));
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
    public void void_g() {
        this.entityDataManager.set(HornyLevel, 0);
        this.entityDataManager.set(OUTFIT_INDEX, 1);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.a_19();
        this.SpawnFriendlySlime();
        if (this.isPotionActive(HornyPotion.HORNY_POTION) && this.slimeActions == SlimeActions.IDLE && this.entityDataManager.get(TicksUntilBirth) == -1) {
            this.entityDataManager.set(HornyLevel, 2);
            if ((Integer)this.entityDataManager.get(OUTFIT_INDEX) == 1) {
                this.setCurrentAction(Action.UNDRESS);
            }
            this.removePotionEffect(HornyPotion.HORNY_POTION);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.currentAction() == Action.NULL) {
            this.b_42();
        }
        if (this.entityDataManager.get(HornyLevel) >= 2 && this.ticksExisted % 10 == 0) {
            SlimeEntity.a(EnumParticleTypes.HEART, (GirlEntity)this);
        }
        if (this.world.isRemote) {
            this.void_d();
            this.void_i();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void void_i() {
        if (this.getID() == null) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (!this.getID().equals(entityPlayerSP.getPersistentID())) {
            return;
        }
        Vec3d vec3d = this.getPositionVector();
        Vec3d vec3d2 = VectorMath.rotate(new Vec3d(0.0, 0.0, 0.65f), this.java_lang_Float_I().floatValue());
        vec3d = vec3d.add(vec3d2);
        entityPlayerSP.setPosition(vec3d.x, vec3d.y, vec3d.z);
        entityPlayerSP.setVelocity(0.0, 0.0, 0.0);
    }

    void void_d() {
        int ticks_birth = this.entityDataManager.get(TicksUntilBirth);
        if (ticks_birth == -1) {
            return;
        }
        SlimeEntity.a(EnumParticleTypes.SPELL_WITCH, (GirlEntity)this);
        if (ticks_birth == 0) {
            this.PlaySound(SoundsHandler.MISC_PLOB[0]);
        }
    }

    void SpawnFriendlySlime() {
        int n = this.entityDataManager.get(TicksUntilBirth);
        if (n == -1) {
            return;
        }
        this.entityDataManager.set(TicksUntilBirth, n - 1);
        if (--n >= 0) {
            return;
        }
        FriendlySlimeEntity friendlySlimeEntity = new FriendlySlimeEntity(this.world);
        friendlySlimeEntity.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(friendlySlimeEntity);
        this.entityDataManager.set(TicksUntilBirth, -1);
    }

    void a_19() {
        int n = this.entityDataManager.get(HornyLevel);
        if (n < 2) {
            return;
        }
        if (n >= 4 && this.onGround && this.currentAction() == Action.NULL) {
            this.setTargetPosition(this.getPositionVector());
            this.void_b(this.rotationYaw);
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setNoGravity(true);
            this.noClip = true;
            this.setCurrentAction(Action.STARTDOGGY);
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 1.0);
        if (entityPlayer == null || !entityPlayer.onGround || SlimeEntity.getActiveSceneInfo(entityPlayer) != null) {
            return;
        }
        this.setTargetPosition(this.getPositionVector());
        this.void_b(this.rotationYaw);
        this.entityDataManager.set(IS_ANCHORED, true);
        this.setNoGravity(true);
        this.noClip = true;
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
        entityPlayer.rotationYaw = this.java_lang_Float_I().floatValue();
        Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, 0.65f), this.java_lang_Float_I().floatValue());
        entityPlayer.setPosition(this.posX + vec3d.x, this.posY, this.posZ + vec3d.z);
        if (this.currentAction() == Action.WAITDOGGY) {
            this.setCurrentAction(Action.DOGGYSTART);
        } else {
            this.setCurrentAction(Action.SUCKBLOWJOB);
        }
    }

    void b_42() {
        if (this.world.isRemote) {
            float f;
            if ((double)this.N == 90.0) {
                this.slimeActions = SlimeActions.JUMP_START;
            }
            if (!this.K && this.onGround) {
                this.slimeActions = SlimeActions.JUMP_END;
                this.N = 0;
            }
            this.rotationYaw = f = this.entityDataManager.get(R).floatValue();
            this.rotationYawHead = f;
            this.renderYawOffset = f;
        } else {
            if ((double)this.N == 85.0) {
                this.entityDataManager.set(R, Float.valueOf(this.float_e()));
            }
            if ((double)this.N == 100.0) {
                this.void_h();
            }
            if (!this.K && this.onGround) {
                boolean bl = this.V = this.entityDataManager.get(TicksUntilBirth) == -1 && this.getRNG().nextFloat() < 0.1f;
            }
            if (this.V && this.N == 50) {
                int n = this.entityDataManager.get(HornyLevel);
                int n2 = n + 1;
                this.entityDataManager.set(HornyLevel, n2);
                if (n2 == 1) {
                    this.setCurrentAction(Action.UNDRESS);
                }
            }
        }
        if (this.onGround) {
            ++this.N;
        }
        this.K = this.onGround;
    }

    void void_h() {
        float f;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.jump();
        this.rotationYaw = f = this.entityDataManager.get(R).floatValue();
        this.prevRotationYaw = f;
        Vec3d vec3d = new Vec3d(0.0, 0.0, 0.7f);
        vec3d = VectorMath.rotate(vec3d, f);
        this.motionX = vec3d.x;
        this.motionZ = vec3d.z;
        this.N = 0;
    }

    float float_e() {
        int n = this.entityDataManager.get(HornyLevel);
        if (this.entityDataManager.get(TicksUntilBirth) != -1) {
            return this.float_f();
        }
        if (n < 2) {
            return this.float_f();
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 30.0);
        if (entityPlayer == null) {
            return this.float_f();
        }
        if (SlimeEntity.getActiveSceneInfo(entityPlayer) != null) {
            return this.float_f();
        }
        return (float)Math.atan2(this.posZ - entityPlayer.posZ, this.posX - entityPlayer.posX) * 57.29578f + 90.0f;
    }

    float float_f() {
        return Reference.RANDOM.nextFloat() * 360.0f;
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        block4 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() == Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.slime.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.slime.fhappy", true, animationEvent);
                break;
            }
            case "action": {
                if (this.currentAction() == Action.NULL) {
                    this.createAnimation(this.slimeActions.a, true, animationEvent);
                    break;
                }
                switch (this.currentAction()) {
                    case UNDRESS: {
                        this.createAnimation("animation.slime.undress", false, animationEvent);
                        break block4;
                    }
                    case DRESS: {
                        this.createAnimation("animation.slime.dress", false, animationEvent);
                        break block4;
                    }
                    case STRIP: {
                        this.createAnimation("animation.slime.strip", false, animationEvent);
                        break block4;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobintro", false, animationEvent);
                        break block4;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobsuck", true, animationEvent);
                        break block4;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobthrust", true, animationEvent);
                        break block4;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobcum", false, animationEvent);
                        break block4;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.slime.doggygoonbed", false, animationEvent);
                        break block4;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.slime.doggywait", true, animationEvent);
                        break block4;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.slime.doggystart", false, animationEvent);
                        break block4;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.slime.doggyslow", true, animationEvent);
                        break block4;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.slime.doggyfast", true, animationEvent);
                        break block4;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.slime.doggycum", false, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "undress": {
                    if (!this.boolean_e()) break;
                    this.changeDataParameterFromClient("currentModel", "0");
                    this.setCurrentAction(Action.NULL);
                    break;
                }
                case "dress": {
                    if (!this.boolean_e()) break;
                    this.entityDataManager.set(OUTFIT_INDEX, 1);
                    this.setCurrentAction((Action)null);
                    this.void_r();
                    break;
                }
                case "becomeNude": {
                    this.entityDataManager.set(OUTFIT_INDEX, 0);
                    break;
                }
                case "sexUiOn": {
                    if (!this.boolean_n() || SexUI.shouldBeRendered) break;
                    SexUI.init();
                    break;
                }
                case "bjiMSG10": {
                    if (!this.boolean_n()) break;
                    this.moveCamera(-0.4, -0.8, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    this.a(SoundEvents.ENTITY_SLIME_SQUISH, 0.5f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (Reference.RANDOM.nextInt(5) == 0) {
                        this.a(SoundEvents.ENTITY_SLIME_JUMP, 0.5f);
                    }
                    this.a(SoundEvents.ENTITY_SLIME_SQUISH, 0.5f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjtMSG1": {
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    this.PlaySound(SoundEvents.ENTITY_SLIME_DEATH);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "bjiDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "bjtDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    break;
                }
                case "bjtReady": 
                case "doggyfastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "bjcMSG1": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                    break;
                }
                case "bjcMSG2": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                    if (!this.boolean_n()) break;
                    SexUI.hide();
                    break;
                }
                case "doggyslowMSG2": {
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    break;
                }
                case "bjcBlackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "bjcDone": 
                case "doggyCumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
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
                    this.a(SoundEvents.ENTITY_SLIME_SQUISH, 0.25f);
                    break;
                }
                case "doggystartMSG4": {
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_SMALLINSERTS), 1.5f);
                    break;
                }
                case "doggystartMSG5": {
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING), 0.33f);
                    this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    break;
                }
                case "doggystartDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "doggyslowMSG1": {
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING), 0.33f);
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
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "doggyfastMSG1": {
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.04);
                    }
                    ++this.P;
                    if (this.P % 2 == 0) {
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
                    this.a(SoundsHandler.MISC_CUMINFLATION[0], 4.0f);
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundEvents.ENTITY_SLIME_DEATH);
                    break;
                }
                case "jumpStart": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                    break;
                }
                case "jumpStartDone": {
                    this.slimeActions = SlimeActions.JUMP_AIR;
                    break;
                }
                case "jumpEndSound": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                    break;
                }
                case "jumpEndDone": {
                    this.slimeActions = SlimeActions.IDLE;
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.eyesController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    static enum SlimeActions {
        IDLE("animation.slime.idle"),
        JUMP_START("animation.slime.jumpstart"),
        JUMP_AIR("animation.slime.jumpair"),
        JUMP_END("animation.slime.jumpend");

        String a;

        public String a() {
            return this.a;
        }

        private SlimeActions(String string2) {
            this.a = string2;
        }
    }
}

