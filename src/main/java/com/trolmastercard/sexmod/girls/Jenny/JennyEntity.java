/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Jenny;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.Packets.SendGirlToSex;
import com.trolmastercard.sexmod.Packets.SetPlayerForGirl;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
import com.trolmastercard.sexmod.util.interfaces.IEllie;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class JennyEntity extends Fighter implements IEllie, IBeddableSexGirl {
    public boolean Z = false;
    public boolean ab = false;
    public boolean af = false;
    final static public DataParameter<Boolean> Y = EntityDataManager.createKey(GirlEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(118);
    int ac = 0;
    int ad = 0;
    boolean aa = false;
    int ag = 0;
    boolean ae = false;

    public JennyEntity(World world) {
        super(world);
        this.setSize(0.49f, 1.95f);
        this.slashSwordRot = 140;
        this.stabSwordRot = 50;
        this.holdBowRot = 140;
        this.swordOffsetStab = new Vec3d(0.0, -0.029999997854232782, -0.2);
    }

    public static JennyEntity a(World world) {
        JennyEntity jennyEntity = new JennyEntity(world);
        jennyEntity.isSpecialState = true;
        return jennyEntity;
    }

    @Override
    public String getGirlName() {
        return "Jenny";
    }

    @Override
    public float getScaleFactor() {
        return -0.2f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(Y, false);
    }

    @Override
    public void SetHome() {
        this.sendLocalClientMessage("Alright, this is my new Home~");
        this.PlaySound(SoundsHandler.GIRLS_JENNY_HAPPYOH[1]);
    }

    @Override
    public float getEyeHeight() {
        return 1.64f;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.random(SoundsHandler.GIRLS_JENNY_SIGH);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Override
    public void updateAITasks() {
        //Object object;
        super.updateAITasks();
        EntityPlayer player = this.world.getClosestPlayerToEntity(this, 15.0);
        if (this.af && player != null && player.getPositionVector().distanceTo(this.getPositionVector()) < 0.5) {
            this.af = false;
            this.entityDataManager.set(GirlEntity.INTERACTION_PARTNER_UUID, this.world.getClosestPlayerToEntity(this, 15.0).getPersistentID().toString());
            EntityPlayerMP object = this.getServer().getPlayerList().getPlayerByUUID(this.getInteractionPlayerUUID());
            this.entityDataManager.set(GirlEntity.INTERACTION_PARTNER_UUID, object.getPersistentID().toString());
            ((EntityPlayerMP)object).setPositionAndUpdate(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z);
            this.alignPlayerToGirl((EntityPlayerMP)object, false);
            ((Entity)object).moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            this.moveCamera(0.0, 0.0, 0.4, 0.0f, 60.0f);
            this.playerCameraOffsetPos = null;
            this.setCurrentAction(Action.DOGGYSTART);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)object);
        }
        if (this.Z) {
            if (this.getPositionVector().distanceTo(this.getTargetPosition()) < 0.6 || this.ad > 200) {
                this.Z = false;
                this.entityDataManager.set(GirlEntity.IS_ANCHORED, true);
                this.ad = 0;
                this.noClip = true;
                this.setNoGravity(true);
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                this.setCurrentAction(Action.STARTDOGGY);
            } else {
                ++this.ad;
                if (this.ad == 60 || this.ad == 120) {
                    this.getNavigator().clearPath();
                    this.getNavigator().tryMoveToXYZ(this.getTargetPosition().x, this.getTargetPosition().y, this.getTargetPosition().z, 0.35);
                }
            }
        }
        if (this.ab) {
            ++this.ac;
            if (this.getPositionVector().equals(GirlEntity.TARGET_POS) || this.ac > 40) {
                this.ab = false;
                this.ac = 0;
                this.setYawRotation(this.world.getMinecraftServer().getPlayerList().getPlayerByUUID((UUID)this.getInteractionPlayerUUID()).rotationYaw + 180.0f);
                this.entityDataManager.set(GirlEntity.IS_ANCHORED, true);
                this.getNavigator().clearPath();
                if (this.entityDataManager.get(Y)) {
                    this.U();
                    return;
                }
                this.setCurrentAction(Action.PAYMENT);
            } else {
                this.rotationYaw = this.getYawRotation().floatValue();
                this.setTargetPosition(this.getFrontOffsetVector());
                this.setNoGravity(false);
                Vec3d object = ReferenceAndRotationHelper.a(this.getPositionVector(), this.getTargetPosition(), 40 - this.ac);
                this.setPosition(((Vec3d)object).x, ((Vec3d)object).y, ((Vec3d)object).z);
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (super.processInteract(entityPlayer, enumHand)) {
            return true;
        }
        if (this.world.isRemote && !this.openInteractionMenu(entityPlayer)) {
            this.sendLocalClientMessage(I18n.format("jenny.dialogue.busy", new Object[0]));
        }
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.entityDataManager.set(Y, this.isPotionActive(HornyPotion.HORNY_POTION));
        }
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        if (this.getInteractionPlayerUUID() == null && (!this.hasMaster() || this.entityDataManager.get(GirlEntity.MASTER).equals(Minecraft.getMinecraft().player.getPersistentID().toString()))) {
            String[] stringArray = new String[]{"action.names.blowjob", "action.names.boobjob", "action.names.doggy", this.entityDataManager.get(GirlEntity.OUTFIT_INDEX) == 1 ? "action.names.strip" : "action.names.dressup"};
            if (this.entityDataManager.get(Y).booleanValue()) {
                GirlEntity.openInventoryGui(player, this, stringArray, true);
                return true;
            }
            GirlEntity.openInventoryGui(player, this, stringArray, new ItemStack[]{new ItemStack(Items.EMERALD, 3), new ItemStack(Items.ENDER_PEARL, 2), new ItemStack(Items.DIAMOND, 2), this.entityDataManager.get(GirlEntity.OUTFIT_INDEX) == 1 ? new ItemStack(Items.GOLD_INGOT, 1) : new ItemStack(Items.AIR, 0)}, true);
            return true;
        }
        return false;
    }

    @Override
    public void doAction(String string, UUID player) {
        super.doAction(string, player);
        if ("action.names.blowjob".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "blowjob");
            this.a(true, player);
        } else if ("action.names.boobjob".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "boobjob");
            this.a(true, player);
        } else if ("action.names.doggy".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "doggy");
            this.a(true, player);
        } else if ("action.names.strip".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "strip");
            this.a(true, player);
        } else if ("action.names.dressup".equals(string)) {
            this.setCurrentAction(Action.STRIP);
        }
    }

    protected void a(boolean bl, UUID uUID) {
        super.triggerActionSync(bl, true, uUID);
        HandlePlayerMovement.setMovementLock(false);
    }

    @Override
    public void goToSexBed() {
        BlockPos nearestBed = this.getNearestBed(this.getPosition());
        if (nearestBed == null) {
            //no beds nearby
            this.PlaySound(SoundsHandler.GIRLS_JENNY_HMPH[2]);
            this.sendLocalClientMessage(I18n.format("jenny.dialogue.nobedinsight", new Object[0]));
        }
        else {
            this.tasks.removeTask(this.aiWander);
            this.tasks.removeTask(this.watchClosestGirlGoal);
            Vec3d vec3d = new Vec3d(nearestBed.getX(), nearestBed.getY(), nearestBed.getZ());
            int[] nArray = new int[]{0, 180, -90, 90};
            Vec3d[][] vec3dArrayArray = new Vec3d[][]{{new Vec3d(0.5, 0.0, -0.5), new Vec3d(0.0, 0.0, -1.0)}, {new Vec3d(0.5, 0.0, 1.5), new Vec3d(0.0, 0.0, 1.0)}, {new Vec3d(-0.5, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0)}, {new Vec3d(1.5, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0)}};
            int n = -1;
            for (int i = 0; i < vec3dArrayArray.length; ++i) {
                Vec3d vec3d2 = vec3d.add(vec3dArrayArray[i][1]);
                if (this.world.getBlockState(new BlockPos(vec3d2.x, vec3d2.y, vec3d2.z)).getBlock() != Blocks.AIR) continue;
                if (n == -1) {
                    n = i;
                    continue;
                }
                double d = this.getPosition().distanceSq(vec3d.add((Vec3d)vec3dArrayArray[n][0]).x, vec3d.add((Vec3d)vec3dArrayArray[n][0]).y, vec3d.add((Vec3d)vec3dArrayArray[n][0]).z);
                double d2 = this.getPosition().distanceSq(vec3d.add((Vec3d)vec3dArrayArray[i][0]).x, vec3d.add((Vec3d)vec3dArrayArray[i][0]).y, vec3d.add((Vec3d)vec3dArrayArray[i][0]).z);
                if (!(d2 < d)) continue;
                n = i;
            }
            if (n == -1) {
                this.PlaySound(SoundsHandler.GIRLS_JENNY_HMPH[2]);
                this.sendLocalClientMessage(I18n.format("jenny.dialogue.bedobscured", new Object[0]));
                return;
            }
            Vec3d vec3d3 = vec3d.add(vec3dArrayArray[n][0]);
            this.setAnchored(false);
            this.setYawRotation(nArray[n]);
            this.setTargetPosition(new Vec3d(vec3d3.x, vec3d3.y, vec3d3.z));
            this.cameraYaw = this.getYawRotation().floatValue();
            this.getNavigator().clearPath();
            this.getNavigator().tryMoveToXYZ(vec3d3.x, vec3d3.y, vec3d3.z, 0.35);
            this.Z = true;
            this.ad = 0;
        }
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (currentAction == Action.DOGGYCUM && (action == Action.DOGGYSLOW || action == Action.DOGGYFAST)) {
            return;
        }
        if (currentAction == Action.CUMBLOWJOB && (action == Action.THRUSTBLOWJOB || action == Action.SUCKBLOWJOB)) {
            return;
        }
        if (currentAction == Action.PAIZURI_CUM && (action == Action.PAIZURI_SLOW || action == Action.PAIZURI_FAST)) {
            return;
        }
        super.setCurrentAction(action);
        if (currentAction != Action.STARTBLOWJOB && currentAction != Action.PAIZURI_START) {
            return;
        }
        UUID uUID = this.getInteractionPlayerUUID();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 0.2), this.getYawRotation().floatValue() + 180.0f);
        entityPlayer.setPositionAndUpdate(entityPlayer.posX + vec3d.x, entityPlayer.posY, entityPlayer.posZ + vec3d.z);
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB) {
            this.moveCamera(0.0, 0.0, 0.0, 0.0f, 70.0f);
            return Action.CUMBLOWJOB;
        }
        if (action == Action.DOGGYSLOW || action == Action.DOGGYFAST) {
            return Action.DOGGYCUM;
        }
        if (action == Action.PAIZURI_FAST || action == Action.PAIZURI_SLOW) {
            return Action.PAIZURI_CUM;
        }
        return null;
    }

    @Override
    protected Action getNextAction(Action action) {
        switch (action) {
            case SUCKBLOWJOB: {
                return Action.THRUSTBLOWJOB;
            }
            case DOGGYSLOW: {
                return Action.DOGGYFAST;
            }
            case PAIZURI_SLOW: {
                if (this.ae) {
                    this.ae = false;
                    this.moveCamera(0.0, 0.0, (double)0.2f, 0.0f, 70.0f);
                }
                return Action.PAIZURI_FAST;
            }
        }
        return null;
    }

    @Override
    public void setDismounted() {
        this.ab = true;
    }

    @Override
    public void ResetNPCTasks() {
        this.aiWander = new EntityAIWanderAvoidWater(this, 0.35);
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.watchClosestGirlGoal);
        this.tasks.addTask(5, this.aiWander);
    }

    @Override
    protected void U() {
        switch (this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES)) {
            case "strip": {
                this.resetGirlState();
                this.setCurrentAction(Action.STRIP);
                break;
            }
            case "blowjob": {
                this.setCurrentAction(Action.STARTBLOWJOB);
                break;
            }
            case "boobjob": {
                if (this.entityDataManager.get(GirlEntity.OUTFIT_INDEX) != 0) {
                    this.setCurrentAction(Action.STRIP);
                    return;
                }
                this.setCurrentAction(Action.PAIZURI_START);
                break;
            }
            case "doggy": {
                if (this.entityDataManager.get(GirlEntity.OUTFIT_INDEX) != 0) {
                    this.setCurrentAction(Action.STRIP);
                    this.resetGirlState();
                    return;
                }
                this.resetCameraAndPhysics();
                if (this.world.isRemote) {
                    PackageHandler.INSTANCE.sendToServer(new SendGirlToSex(this.girlID()));
                    break;
                }
                this.resetGirlState();
                this.goToSexBed();
            }
        }
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("animationFollowUp", "");
        } else {
            this.entityDataManager.set(GirlEntity.GIRL_HAND_STATES, "");
        }
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                this.createAnimation("animation.jenny.fhappy", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL && this.getCurrentAction() != null) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                if (this.isRiding()) {
                    this.createAnimation("animation.jenny.sit", true, event);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    switch (this.getWalkType()) {
                        case RUN: {
                            this.createAnimation("animation.jenny.run", true, event);
                            break;
                        }
                        case FAST_WALK: {
                            this.createAnimation("animation.jenny.fastwalk", true, event);
                            break;
                        }
                        case WALK: {
                            this.createAnimation("animation.jenny.walk", true, event);
                        }
                    }
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation("animation.jenny.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.jenny.null", true, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.jenny.strip", false, event);
                        break;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.jenny.payment", false, event);
                        break;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobintro", false, event);
                        break;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobsuck", true, event);
                        break;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobthrust", true, event);
                        break;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobcum", false, event);
                        break;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.jenny.doggygoonbed", false, event);
                        break;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.jenny.doggywait", true, event);
                        break;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.jenny.doggystart", false, event);
                        break;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.jenny.doggyslow", true, event);
                        break;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.jenny.doggyfast_" + (this.aa ? "hard" : "soft"), true, event);
                        break;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.jenny.doggycum", false, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.jenny.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.jenny.bowcharge", false, event);
                        break;
                    }
                    case RIDE: {
                        this.createAnimation("animation.jenny.ride", true, event);
                        break;
                    }
                    case SIT: {
                        this.createAnimation("animation.jenny.sit", true, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.jenny.throwpearl", false, event);
                        break;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.jenny.downed", true, event);
                        break;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.jenny.paizuri_start", false, event);
                        break;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.jenny.paizuri_slow", true, event);
                        break;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.jenny.paizuri_fast", true, event);
                        break;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.jenny.paizuri_cum", false, event);
                        break;
                    }
                    case WAVE: {
                        this.createAnimation("animation.jenny.wave", true, event);
                        break;
                    }
                    case WAVE_IDLE: {
                        this.createAnimation("animation.jenny.wave_idle", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "becomeNude": {
                    if (!this.getClosestPlayerID()) break;
                    this.changeDataParameterFromClient("currentModel", this.entityDataManager.get(GirlEntity.OUTFIT_INDEX) == 1 ? "0" : "1");
                    break;
                }
                case "stripDone": {
                    if (!this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES).equals("boobjob")) {
                        this.resetCameraAndPhysics();
                    }
                    this.U();
                    break;
                }
                case "stripMSG1": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "paymentMSG1": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.huh", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HUH[1]);
                    break;
                }
                case "paymentMSG2": {
                    this.PlaySound(SoundsHandler.MISC_PLOB[0], 0.5f);
                    String string = "<" + Minecraft.getMinecraft().player.getName() + "> ";
                    switch (this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES)) {
                        case "strip": {
                            this.b(string + I18n.format("jenny.dialogue.showBobsandveganapls", new Object[0]), true);
                            break;
                        }
                        case "blowjob": {
                            this.b(string + I18n.format("jenny.dialogue.giveblowjob", new Object[0]), true);
                            break;
                        }
                        case "doggy": {
                            this.b(string + I18n.format("jenny.dialogue.givesex", new Object[0]), true);
                            break;
                        }
                        case "boobjob": {
                            this.b(string + I18n.format("jenny.dialogue.givebooba", new Object[0]), true);
                            break;
                        }
                    }
                    this.b(string + "sex pls", true);
                    break;
                }
                case "paymentMSG3": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.hehe", new Object[0]));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "paymentMSG4": {
                    this.PlaySound(SoundsHandler.MISC_PLOB[0], 0.25f);
                    break;
                }
                case "paymentDone": {
                    this.U();
                    break;
                }
                case "bjiMSG1": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext1", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_MMM[8]);
                    this.cameraYaw = this.rotationYaw + 180.0f;
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "bjiMSG2": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG3": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext3", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[0]);
                    break;
                }
                case "bjiMSG4": {
                    this.PlaySound(SoundsHandler.MISC_BELLJINGLE[0]);
                    break;
                }
                case "bjiMSG5": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext4", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HMPH[1], 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "bjiMSG6": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext5", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG7": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext6", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[4]);
                    break;
                }
                case "bjiMSG8": {
                    this.b("<" + Minecraft.getMinecraft().player.getName() + "> " + I18n.format("jenny.dialogue.blowjobtext7", new Object[0]), true);
                    this.PlaySound(SoundsHandler.MISC_PLOB[0], 0.5f);
                    break;
                }
                case "bjiMSG9": {
                    this.sendGirlChatMessage(I18n.format("jenny.dialogue.blowjobtext8", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[2]);
                    break;
                }
                case "bjiMSG10": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.moveCamera(-0.65, -0.8, -0.25, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    if (this.isControlledByLocalPlayer() && HandlePlayerMovement.isThrusting) {
                        this.resetAnimationControllerOffset();
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (ReferenceAndRotationHelper.RANDOM.nextInt(5) == 0) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_BJMOAN));
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjtMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MMM));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
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
                case "doggyfastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    this.aa = true;
                    break;
                }
                case "bjtReady": 
                case "paizuriReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "bjcMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_BJMOAN[1]);
                    break;
                }
                case "bjcMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_BJMOAN[7]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.hide();
                    break;
                }
                case "bjcMSG3": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[1]);
                    break;
                }
                case "bjcMSG4": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[0]);
                    break;
                }
                case "bjcMSG5": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[1]);
                    break;
                }
                case "bjcMSG6": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[2]);
                    break;
                }
                case "bjcMSG7": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[3]);
                    break;
                }
                case "bjcBlackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "bjcDone": 
                case "paizuri_cumDone": 
                case "doggyCumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    this.cameraYaw = this.rotationYaw;
                    break;
                }
                case "doggyGoOnBedMSG2": {
                    this.sendLocalClientMessage(I18n.format("jenny.dialogue.doggytext1", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[9]);
                    break;
                }
                case "doggyGoOnBedMSG3": {
                    this.sendLocalClientMessage(I18n.format("jenny.dialogue.doggytext2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
                    break;
                }
                case "doggyGoOnBedMSG4": {
                    this.PlaySound(SoundsHandler.MISC_SLAP[0], 0.75f);
                    break;
                }
                case "doggyGoOnBedDone": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SetPlayerForGirl(this.girlID(), Minecraft.getMinecraft().player.getPersistentID()));
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
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[1], 0.5f);
                    break;
                }
                case "doggystartMSG4": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_MMM[1]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "doggystartMSG5": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggystartDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                // TODO
                //  because this legit entire fucking mod has every animation sequence split up and manually duplicated
                //  across each girl (very redundant), I am really considering whether to create a central 'thrust'
                //  interpreter / caller, or just go ahead and manually insert my custom motion keyframes for EACH animation.
                //  realistically, the end-user should be allowed to edit their keyframe timing, so...
                //  ??? allow custom keyframe insertion during gameplay, or, hardcode in each keyframe event? (my previous attempt was hardcoded)

                // the below uses "hardcoded" frames, but can easily work for custom user-inserted frames
                //                case "penis_entering": {
                //                    //super.onPenisRetractionStart();
                //                    //super.onPenisInsertionStart();
                //
                //                    // just use the anim length
                //                    long millis = ((long)
                //                            (event.getController().getCurrentAnimation().animationLength * 900.0) / 2) ;
                //                    ToyManager.shoveIn(millis);
                //                    //event.getController().getCurrentAnimation().animationLength
                //                    break;
                //                }
                //                case "penis_exiting": {
                //                    //super.onPenisRetractionStart();
                //                    //super.onPenisInsertionStart();
                //                    long millis = ((long)
                //                            (event.getController().getCurrentAnimation().animationLength * 900.0) / 2);
                //                    ToyManager.pullOut(millis);
                //                    break;
                //                }
                case "doggyslowMSG1": {
                    this.aa = false;
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    int n = ReferenceAndRotationHelper.RANDOM.nextInt(4);
                    if (n == 0) {
                        n = ReferenceAndRotationHelper.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MMM));
                        } else {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                        }
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.00666);
                    break;
                }
                case "doggyslowMSG2": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING), 0.5f);
                    break;
                }
                case "doggyfastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    ++this.ag;
                    if (this.ag % 2 == 0) {
                        int n = ReferenceAndRotationHelper.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                            break;
                        }
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                        break;
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_AHH));
                    break;
                }
                case "doggyfastDone": {
                    this.aa = false;
                    this.setCurrentAction(Action.DOGGYSLOW);
                    break;
                }
                case "doggycumMSG1": {
                    this.PlaySound(SoundsHandler.MISC_CUMINFLATION[0], 2.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggycumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[4]);
                    break;
                }
                case "doggycumMSG3": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[5]);
                    break;
                }
                case "doggycumMSG4": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[6]);
                    break;
                }
                case "doggycumMSG5": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[7]);
                    break;
                }
                case "pearl": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "boobjob_camera": {
                    UUID uUID = Minecraft.getMinecraft().player.getPersistentID();
                    if (!uUID.equals(this.world.getClosestPlayerToEntity(this.com_trolmastercard_sexmod_em_class258_af(), 2.0).getPersistentID())) break;
                    this.cameraYaw = this.world.getPlayerEntityByUUID((UUID)uUID).rotationYaw;
                    this.setInteractionPlayerUUID(uUID);
                    if (this.ae) break;
                    this.ae = true;
                    this.moveCamera(-0.7, -0.6, 0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.showUI();
                    break;
                }
                case "paizuriFastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MMM));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_AHH));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "paizuriSlowMSG1": 
                case "paizuriStartMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "paizuri_fastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    if (!this.isControlledByLocalPlayer() || this.ae) break;
                    this.ae = true;
                    this.moveCamera(-0.7, -0.6, 0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startStep": {
                    IBlockState iBlockState = this.world.getBlockState(this.getPosition().subtract(new Vec3i(0, 1, 0)));
                    this.PlaySound(iBlockState.getBlock().getSoundType(iBlockState, this.world, this.getPosition(), this).getStepSound());
                    break;
                }
                case "paizuri_cumStart": {
                    if (!this.isControlledByLocalPlayer() || this.ae) break;
                    this.moveCamera(-0.7, -0.6, 0.2, 60.0f, -3.0f);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

