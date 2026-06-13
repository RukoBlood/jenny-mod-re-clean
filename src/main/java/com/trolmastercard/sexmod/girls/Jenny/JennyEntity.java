/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Jenny;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SendGirlToSex;
import com.trolmastercard.sexmod.Packages.SetPlayerForGirl;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
import com.trolmastercard.sexmod.util.interfaces.bh_class82;
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

public class JennyEntity extends Fighter implements bh_class82, IBeddableSexGirl {
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
        this.P = 140;
        this.O = 50;
        this.K = 140;
        this.V = new Vec3d(0.0, -0.029999997854232782, -0.2);
    }

    public static JennyEntity a(World world) {
        JennyEntity jennyEntity = new JennyEntity(world);
        jennyEntity.F = true;
        return jennyEntity;
    }

    @Override
    public String getGirlName() {
        return "Jenny";
    }

    @Override
    public float float_i() {
        return -0.2f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(Y, false);
    }

    @Override
    public void SetHome() {
        this.void_a("Alright, this is my new Home~");
        this.PlaySound(SoundsHandler.GIRLS_JENNY_HAPPYOH[1]);
    }

    @Override
    public float getEyeHeight() {
        return 1.64f;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.a(SoundsHandler.GIRLS_JENNY_SIGH);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Override
    public void updateAITasks() {
        //Object object;
        super.updateAITasks();
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
        if (this.af && entityPlayer != null && entityPlayer.getPositionVector().distanceTo(this.getPositionVector()) < 0.5) {
            this.af = false;
            this.entityDataManager.set(GirlEntity.y, this.world.getClosestPlayerToEntity(this, 15.0).getPersistentID().toString());
            EntityPlayerMP object = this.getServer().getPlayerList().getPlayerByUUID(this.getID());
            this.entityDataManager.set(GirlEntity.y, object.getPersistentID().toString());
            ((EntityPlayerMP)object).setPositionAndUpdate(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z);
            this.a((EntityPlayerMP)object, false);
            ((Entity)object).moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            this.moveCamera(0.0, 0.0, 0.4, 0.0f, 60.0f);
            this.playerCamPos = null;
            this.setCurrentAction(Action.DOGGYSTART);
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)object);
        }
        if (this.Z) {
            if (this.getPositionVector().distanceTo(this.net_minecraft_util_math_Vec3d_o()) < 0.6 || this.ad > 200) {
                this.Z = false;
                this.entityDataManager.set(GirlEntity.G, true);
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
                    this.getNavigator().tryMoveToXYZ(this.net_minecraft_util_math_Vec3d_o().x, this.net_minecraft_util_math_Vec3d_o().y, this.net_minecraft_util_math_Vec3d_o().z, 0.35);
                }
            }
        }
        if (this.ab) {
            ++this.ac;
            if (this.getPositionVector().equals(GirlEntity.e) || this.ac > 40) {
                this.ab = false;
                this.ac = 0;
                this.void_b(this.world.getMinecraftServer().getPlayerList().getPlayerByUUID((UUID)this.getID()).rotationYaw + 180.0f);
                this.entityDataManager.set(GirlEntity.G, true);
                this.getNavigator().clearPath();
                if (this.entityDataManager.get(Y).booleanValue()) {
                    this.U();
                    return;
                }
                this.setCurrentAction(Action.PAYMENT);
            } else {
                this.rotationYaw = this.java_lang_Float_I().floatValue();
                this.c(this.net_minecraft_util_math_Vec3d_aa());
                this.setNoGravity(false);
                Vec3d object = Reference.a(this.getPositionVector(), this.net_minecraft_util_math_Vec3d_o(), 40 - this.ac);
                this.setPosition(((Vec3d)object).x, ((Vec3d)object).y, ((Vec3d)object).z);
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (super.processInteract(entityPlayer, enumHand)) {
            return true;
        }
        if (this.world.isRemote && !this.boolean_b(entityPlayer)) {
            this.void_a(I18n.format("jenny.dialogue.busy", new Object[0]));
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
    public boolean boolean_b(EntityPlayer entityPlayer) {
        if (this.getID() == null && (!this.boolean_J() || this.entityDataManager.get(GirlEntity.v).equals(Minecraft.getMinecraft().player.getPersistentID().toString()))) {
            String[] stringArray = new String[]{"action.names.blowjob", "action.names.boobjob", "action.names.doggy", this.entityDataManager.get(GirlEntity.D) == 1 ? "action.names.strip" : "action.names.dressup"};
            if (this.entityDataManager.get(Y).booleanValue()) {
                GirlEntity.a(entityPlayer, this, stringArray, true);
                return true;
            }
            GirlEntity.a(entityPlayer, this, stringArray, new ItemStack[]{new ItemStack(Items.EMERALD, 3), new ItemStack(Items.ENDER_PEARL, 2), new ItemStack(Items.DIAMOND, 2), this.entityDataManager.get(GirlEntity.D) == 1 ? new ItemStack(Items.GOLD_INGOT, 1) : new ItemStack(Items.AIR, 0)}, true);
            return true;
        }
        return false;
    }

    @Override
    public void a(String string, UUID uUID) {
        super.a(string, uUID);
        if ("action.names.blowjob".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "blowjob");
            this.a(true, uUID);
        } else if ("action.names.boobjob".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "boobjob");
            this.a(true, uUID);
        } else if ("action.names.doggy".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "doggy");
            this.a(true, uUID);
        } else if ("action.names.strip".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "strip");
            this.a(true, uUID);
        } else if ("action.names.dressup".equals(string)) {
            this.setCurrentAction(Action.STRIP);
        }
    }

    protected void a(boolean bl, UUID uUID) {
        super.a(bl, true, uUID);
        HandlePlayerMovement.a(false);
    }

    @Override
    public void goToSexBed() {
        BlockPos blockPos = this.net_minecraft_util_math_BlockPos_a(this.getPosition());
        if (blockPos == null) {
            this.PlaySound(SoundsHandler.GIRLS_JENNY_HMPH[2]);
            this.void_a(I18n.format("jenny.dialogue.nobedinsight", new Object[0]));
        } else {
            this.tasks.removeTask(this.avoidWater);
            this.tasks.removeTask(this.o);
            Vec3d vec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
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
                this.void_a(I18n.format("jenny.dialogue.bedobscured", new Object[0]));
                return;
            }
            Vec3d vec3d3 = vec3d.add(vec3dArrayArray[n][0]);
            this.void_a(false);
            this.void_b(nArray[n]);
            this.c(new Vec3d(vec3d3.x, vec3d3.y, vec3d3.z));
            this.r = this.java_lang_Float_I().floatValue();
            this.getNavigator().clearPath();
            this.getNavigator().tryMoveToXYZ(vec3d3.x, vec3d3.y, vec3d3.z, 0.35);
            this.Z = true;
            this.ad = 0;
        }
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.DOGGYCUM && (fp_class3242 == Action.DOGGYSLOW || fp_class3242 == Action.DOGGYFAST)) {
            return;
        }
        if (fp_class3243 == Action.CUMBLOWJOB && (fp_class3242 == Action.THRUSTBLOWJOB || fp_class3242 == Action.SUCKBLOWJOB)) {
            return;
        }
        if (fp_class3243 == Action.PAIZURI_CUM && (fp_class3242 == Action.PAIZURI_SLOW || fp_class3242 == Action.PAIZURI_FAST)) {
            return;
        }
        super.setCurrentAction(fp_class3242);
        if (fp_class3243 != Action.STARTBLOWJOB && fp_class3243 != Action.PAIZURI_START) {
            return;
        }
        UUID uUID = this.getID();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = ck_class135.a(new Vec3d(0.0, 0.0, 0.2), this.java_lang_Float_I().floatValue() + 180.0f);
        entityPlayer.setPositionAndUpdate(entityPlayer.posX + vec3d.x, entityPlayer.posY, entityPlayer.posZ + vec3d.z);
    }

    @Override
    protected Action CumAction(Action action) {
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
    protected Action FastSexAction(Action action) {
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
    public void void_b() {
        this.ab = true;
    }

    @Override
    public void void_g() {
        this.avoidWater = new EntityAIWanderAvoidWater(this, 0.35);
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.o);
        this.tasks.addTask(5, this.avoidWater);
    }

    @Override
    protected void U() {
        switch (this.entityDataManager.get(GirlEntity.h)) {
            case "strip": {
                this.s();
                this.setCurrentAction(Action.STRIP);
                break;
            }
            case "blowjob": {
                this.setCurrentAction(Action.STARTBLOWJOB);
                break;
            }
            case "boobjob": {
                if (this.entityDataManager.get(GirlEntity.D) != 0) {
                    this.setCurrentAction(Action.STRIP);
                    return;
                }
                this.setCurrentAction(Action.PAIZURI_START);
                break;
            }
            case "doggy": {
                if (this.entityDataManager.get(GirlEntity.D) != 0) {
                    this.setCurrentAction(Action.STRIP);
                    this.s();
                    return;
                }
                this.void_r();
                if (this.world.isRemote) {
                    PackageHandler.networkWrapper.sendToServer(new SendGirlToSex(this.girlID()));
                    break;
                }
                this.s();
                this.goToSexBed();
            }
        }
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("animationFollowUp", "");
        } else {
            this.entityDataManager.set(GirlEntity.h, "");
        }
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.jenny.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.jenny.fhappy", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL && this.currentAction() != null) {
                    this.createAnimation("animation.jenny.null", true, animationEvent);
                    break;
                }
                if (this.isRiding()) {
                    this.createAnimation("animation.jenny.sit", true, animationEvent);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    switch (this.com_trolmastercard_sexmod_em_class258$a_inner259_q()) {
                        case RUN: {
                            this.createAnimation("animation.jenny.run", true, animationEvent);
                            break;
                        }
                        case FAST_WALK: {
                            this.createAnimation("animation.jenny.fastwalk", true, animationEvent);
                            break;
                        }
                        case WALK: {
                            this.createAnimation("animation.jenny.walk", true, animationEvent);
                        }
                    }
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation("animation.jenny.idle", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.jenny.null", true, animationEvent);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.jenny.strip", false, animationEvent);
                        break block5;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.jenny.payment", false, animationEvent);
                        break block5;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobintro", false, animationEvent);
                        break block5;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobsuck", true, animationEvent);
                        break block5;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobthrust", true, animationEvent);
                        break block5;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobcum", false, animationEvent);
                        break block5;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.jenny.doggygoonbed", false, animationEvent);
                        break block5;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.jenny.doggywait", true, animationEvent);
                        break block5;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.jenny.doggystart", false, animationEvent);
                        break block5;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.jenny.doggyslow", true, animationEvent);
                        break block5;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.jenny.doggyfast_" + (this.aa ? "hard" : "soft"), true, animationEvent);
                        break block5;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.jenny.doggycum", false, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.jenny.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.jenny.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.jenny.ride", true, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.jenny.sit", true, animationEvent);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.jenny.throwpearl", false, animationEvent);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.jenny.downed", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.jenny.paizuri_start", false, animationEvent);
                        break block5;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.jenny.paizuri_slow", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.jenny.paizuri_fast", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.jenny.paizuri_cum", false, animationEvent);
                        break block5;
                    }
                    case WAVE: {
                        this.createAnimation("animation.jenny.wave", true, animationEvent);
                        break block5;
                    }
                    case WAVE_IDLE: {
                        this.createAnimation("animation.jenny.wave_idle", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData animationData) {
        if (this.actionController == null) {
            this.void_p();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            block71 : switch (soundKeyframeEvent.sound) {
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "becomeNude": {
                    if (!this.boolean_e()) break;
                    this.changeDataParameterFromClient("currentModel", this.entityDataManager.get(GirlEntity.D) == 1 ? "0" : "1");
                    break;
                }
                case "stripDone": {
                    if (!this.entityDataManager.get(GirlEntity.h).equals("boobjob")) {
                        this.void_r();
                    }
                    this.U();
                    break;
                }
                case "stripMSG1": {
                    this.h(I18n.format("jenny.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "paymentMSG1": {
                    this.h(I18n.format("jenny.dialogue.huh", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HUH[1]);
                    break;
                }
                case "paymentMSG2": {
                    this.a(SoundsHandler.MISC_PLOB[0], 0.5f);
                    String string = "<" + Minecraft.getMinecraft().player.getName() + "> ";
                    switch (this.entityDataManager.get(GirlEntity.h)) {
                        case "strip": {
                            this.b(string + I18n.format("jenny.dialogue.showBobsandveganapls", new Object[0]), true);
                            break block71;
                        }
                        case "blowjob": {
                            this.b(string + I18n.format("jenny.dialogue.giveblowjob", new Object[0]), true);
                            break block71;
                        }
                        case "doggy": {
                            this.b(string + I18n.format("jenny.dialogue.givesex", new Object[0]), true);
                            break block71;
                        }
                        case "boobjob": {
                            this.b(string + I18n.format("jenny.dialogue.givebooba", new Object[0]), true);
                            break block71;
                        }
                    }
                    this.b(string + "sex pls", true);
                    break;
                }
                case "paymentMSG3": {
                    this.h(I18n.format("jenny.dialogue.hehe", new Object[0]));
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "paymentMSG4": {
                    this.a(SoundsHandler.MISC_PLOB[0], 0.25f);
                    break;
                }
                case "paymentDone": {
                    this.U();
                    break;
                }
                case "bjiMSG1": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext1", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_MMM[8]);
                    this.r = this.rotationYaw + 180.0f;
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "bjiMSG2": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG3": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext3", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[0]);
                    break;
                }
                case "bjiMSG4": {
                    this.PlaySound(SoundsHandler.MISC_BELLJINGLE[0]);
                    break;
                }
                case "bjiMSG5": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext4", new Object[0]));
                    this.a(SoundsHandler.GIRLS_JENNY_HMPH[1], 0.5f);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "bjiMSG6": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext5", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG7": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext6", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[4]);
                    break;
                }
                case "bjiMSG8": {
                    this.b("<" + Minecraft.getMinecraft().player.getName() + "> " + I18n.format("jenny.dialogue.blowjobtext7", new Object[0]), true);
                    this.a(SoundsHandler.MISC_PLOB[0], 0.5f);
                    break;
                }
                case "bjiMSG9": {
                    this.h(I18n.format("jenny.dialogue.blowjobtext8", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[2]);
                    break;
                }
                case "bjiMSG10": {
                    if (!this.boolean_n()) break;
                    this.moveCamera(-0.65, -0.8, -0.25, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    if (this.boolean_n() && HandlePlayerMovement.isThrusting) {
                        this.N();
                    }
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (Reference.RANDOM.nextInt(5) == 0) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_BJMOAN));
                    }
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjtMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MMM));
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "bjiDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "bjtDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    break;
                }
                case "doggyfastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    this.aa = true;
                    break;
                }
                case "bjtReady": 
                case "paizuriReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "bjcMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_BJMOAN[1]);
                    break;
                }
                case "bjcMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_BJMOAN[7]);
                    if (!this.boolean_n()) break;
                    SexUI.c();
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
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "bjcDone": 
                case "paizuri_cumDone": 
                case "doggyCumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    this.r = this.rotationYaw;
                    break;
                }
                case "doggyGoOnBedMSG2": {
                    this.void_a(I18n.format("jenny.dialogue.doggytext1", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[9]);
                    break;
                }
                case "doggyGoOnBedMSG3": {
                    this.void_a(I18n.format("jenny.dialogue.doggytext2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
                    break;
                }
                case "doggyGoOnBedMSG4": {
                    this.a(SoundsHandler.MISC_SLAP[0], 0.75f);
                    break;
                }
                case "doggyGoOnBedDone": {
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SetPlayerForGirl(this.girlID(), Minecraft.getMinecraft().player.getPersistentID()));
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
                    this.a(SoundsHandler.MISC_BEDRUSTLE[1], 0.5f);
                    break;
                }
                case "doggystartMSG4": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_SMALLINSERTS));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_MMM[1]);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "doggystartMSG5": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.33f);
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggystartDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    if (!this.boolean_n()) break;
                    SexUI.d();
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
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.33f);
                    int n = Reference.RANDOM.nextInt(4);
                    if (n == 0) {
                        n = Reference.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MMM));
                        } else {
                            this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MOAN));
                        }
                    } else {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.00666);
                    break;
                }
                case "doggyslowMSG2": {
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING), 0.5f);
                    break;
                }
                case "doggyfastMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    ++this.ag;
                    if (this.ag % 2 == 0) {
                        int n = Reference.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MOAN));
                            break;
                        }
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                        break;
                    }
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_AHH));
                    break;
                }
                case "doggyfastDone": {
                    this.aa = false;
                    this.setCurrentAction(Action.DOGGYSLOW);
                    break;
                }
                case "doggycumMSG1": {
                    this.a(SoundsHandler.MISC_CUMINFLATION[0], 2.0f);
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MOAN));
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
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "boobjob_camera": {
                    UUID uUID = Minecraft.getMinecraft().player.getPersistentID();
                    if (!uUID.equals(this.world.getClosestPlayerToEntity(this.com_trolmastercard_sexmod_em_class258_af(), 2.0).getPersistentID())) break;
                    this.r = this.world.getPlayerEntityByUUID((UUID)uUID).rotationYaw;
                    this.void_e(uUID);
                    if (this.ae) break;
                    this.ae = true;
                    this.moveCamera(-0.7, -0.6, 0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.d();
                    break;
                }
                case "paizuriFastMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_MMM));
                    } else {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_JENNY_AHH));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "paizuriSlowMSG1": 
                case "paizuriStartMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "paizuri_fastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    if (!this.boolean_n() || this.ae) break;
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
                    if (!this.boolean_n() || this.ae) break;
                    this.moveCamera(-0.7, -0.6, 0.2, 60.0f, -3.0f);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }
}

