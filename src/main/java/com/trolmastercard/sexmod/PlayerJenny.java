/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerJenny
extends PlayerGirl {
    boolean ap = false;
    boolean ar = false;
    int aq = 0;
    boolean as = false;

    protected PlayerJenny(World world) {
        super(world);
    }

    public PlayerJenny(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 1.75f;
    }

    @Override
    public float float_T() {
        return 35.0f;
    }

    @Override
    public float float_ai() {
        return 140.0f;
    }

    @Override
    public float getEyeHeight() {
        return 1.64f;
    }

    @Override
    public void u_() {
        this.setCurrentAction(Action.STARTDOGGY);
        this.entityDataManager.set(GirlEntity.D, 0);
        this.r = this.entityDataManager.get(GirlEntity.w).floatValue();
    }

    @Override
    public boolean boolean_A() {
        return false;
    }

    @Override
    public at_class43 com_trolmastercard_sexmod_at_class43_a(int n) {
        return new a0_class6();
    }

    @Override
    public String HandTexture(int hasClothes) {
        if (hasClothes == 0) {
            return "textures/entity/jenny/hand_nude.png";
        }
        return "textures/entity/jenny/hand.png";
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("action.names.boobjob".equals(string)) {
            this.entityDataManager.set(GirlEntity.D, 0);
            this.setCurrentAction(Action.PAIZURI_START);
            this.a(0, Action.PAIZURI_START);
            this.void_b(uUID);
        }
        if ("action.names.blowjob".equals(string)) {
            this.setCurrentAction(Action.STARTBLOWJOB);
            this.a(this.int_ah(), Action.PAIZURI_START);
            this.void_b(uUID);
        }
    }

    @Override
    public void updateAITasks() {
        EntityPlayer entityPlayer;
        super.updateAITasks();
        if (this.currentAction() == Action.WAITDOGGY && (entityPlayer = this.net_minecraft_entity_player_EntityPlayer_j()) != null && entityPlayer.getDistance(this.net_minecraft_util_math_Vec3d_w().x, this.net_minecraft_util_math_Vec3d_w().y, this.net_minecraft_util_math_Vec3d_w().z) < 1.0) {
            if (this.boolean_c(entityPlayer.getPersistentID())) {
                entityPlayer.sendMessage(new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + "sowy no lesbo action yet uwu"));
                return;
            }
            this.void_e(entityPlayer.getPersistentID());
            entityPlayer.setPositionAndUpdate(this.getPositionVector().x, this.net_minecraft_util_math_Vec3d_w().y, this.getPositionVector().z);
            this.a((EntityPlayerMP)entityPlayer, false);
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayer.capabilities.isFlying = true;
            this.world.getPlayerEntityByUUID((UUID)this.java_util_UUID_m()).capabilities.isFlying = true;
            this.a(0.0, 0.0, 0.4, 0.0f, 60.0f);
            this.B = null;
            this.setCurrentAction(Action.DOGGYSTART);
            NetworkRegistry.networkWrapper.sendTo((IMessage)new gz_class393(false), (EntityPlayerMP)entityPlayer);
        }
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        GirlEntity.a(entityPlayer, this, new String[]{"action.names.blowjob", "action.names.boobjob"}, false);
        return true;
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
                if (this.as) {
                    this.as = false;
                    this.a(0.0, 0.0, 0.0, 0.0f, 70.0f);
                }
                return Action.PAIZURI_FAST;
            }
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB) {
            this.a(0.0, 0.0, 0.0, 0.0f, 70.0f);
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
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
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
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.jenny.null", true, animationEvent);
                    break;
                }
                if (this.ak) {
                    this.createAnimation("animation.jenny.sit", true, animationEvent);
                    break;
                }
                if (this.E.getCurrentAnimation() != null && this.E.getCurrentAnimation().animationName.contains("fly") && this.af) {
                    boolean bl = this.ap = !this.ap;
                }
                if (!this.af) {
                    this.createAnimation("animation.jenny.fly" + (this.ap ? "2" : ""), true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.aj) {
                        this.E.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.jenny.run", true, animationEvent);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.E.setAnimationSpeed(1.5);
                        this.createAnimation("animation.jenny.fastwalk", true, animationEvent);
                        break;
                    }
                    this.E.setAnimationSpeed(1.2f);
                    this.createAnimation("animation.jenny.backwards_walk", true, animationEvent);
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
                        this.createAnimation("animation.jenny.doggyfast_" + (this.ar ? "hard" : "soft"), true, animationEvent);
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
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        if (this.C == null) {
            this.void_p();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            block68 : switch (soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "stripMSG1": {
                    this.h("Hihi~");
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "paymentMSG1": {
                    this.h("Huh?");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_HUH[1]);
                    break;
                }
                case "paymentMSG2": {
                    this.a(SoundEventHandler.MISC_PLOB[0], 0.5f);
                    String string = "<" + Minecraft.getMinecraft().player.getName() + "> ";
                    switch (this.entityDataManager.get(GirlEntity.h)) {
                        case "strip": {
                            this.b(string + "show Bobs and vegana pls", true);
                            break block68;
                        }
                        case "blowjob": {
                            this.b(string + "Give me the sucky sucky and these are yours", true);
                            break block68;
                        }
                        case "doggy": {
                            this.b(string + "Give me the sex pls :)", true);
                            break block68;
                        }
                        case "boobjob": {
                            this.b(string + "gib boba OwO", true);
                            break block68;
                        }
                    }
                    this.b(string + "sex pls", true);
                    break;
                }
                case "paymentMSG3": {
                    this.h("Hehe~");
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.boolean_n()) break;
                    ds_class200.d();
                    break;
                }
                case "paymentMSG4": {
                    this.a(SoundEventHandler.MISC_PLOB[0], 0.25f);
                    break;
                }
                case "paymentDone": {
                    this.U();
                    break;
                }
                case "bjiMSG1": {
                    this.h("What are you...");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_MMM[8]);
                    this.r = 180.0f;
                    if (!this.boolean_n()) break;
                    ds_class200.b();
                    break;
                }
                case "bjiMSG2": {
                    this.h("eh... boys...");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG3": {
                    this.h("OHOhh...!");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_AFTERSESSIONMOAN[0]);
                    break;
                }
                case "bjiMSG4": {
                    this.PlaySound(SoundEventHandler.MISC_BELLJINGLE[0]);
                    break;
                }
                case "bjiMSG5": {
                    this.h("Was this really necessary?!");
                    this.a(SoundEventHandler.GIRLS_JENNY_HMPH[1], 0.5f);
                    if (!this.boolean_n()) break;
                    ds_class200.b();
                    break;
                }
                case "bjiMSG6": {
                    this.h("Oh~");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG7": {
                    this.h("You like it?~");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_GIGGLE[4]);
                    break;
                }
                case "bjiMSG8": {
                    this.b("<" + Minecraft.getMinecraft().player.getName() + "> Yee", true);
                    this.a(SoundEventHandler.MISC_PLOB[0], 0.5f);
                    break;
                }
                case "bjiMSG9": {
                    this.h("Hihihi~");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_GIGGLE[2]);
                    break;
                }
                case "bjiMSG10": {
                    if (!this.boolean_n()) break;
                    this.a(-0.4, -0.8, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.boolean_n()) break;
                    ds_class200.a(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (ModInfo.f.nextInt(5) == 0) {
                        this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_BJMOAN));
                    }
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.boolean_n()) break;
                    ds_class200.a(0.02);
                    break;
                }
                case "bjtMSG1": {
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MMM));
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.boolean_n()) break;
                    ds_class200.a(0.04);
                    break;
                }
                case "bjiDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    if (!this.boolean_n()) break;
                    ds_class200.d();
                    break;
                }
                case "bjtDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB);
                    break;
                }
                case "doggyfastReady": {
                    if (!this.boolean_n() || !d3_class161.d) break;
                    this.N();
                    this.ar = true;
                    break;
                }
                case "bjtReady": 
                case "paizuriReady": {
                    if (!this.boolean_n() || !d3_class161.d) break;
                    this.N();
                    break;
                }
                case "bjcMSG1": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_BJMOAN[1]);
                    break;
                }
                case "bjcMSG2": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_BJMOAN[7]);
                    if (!this.boolean_n()) break;
                    ds_class200.c();
                    break;
                }
                case "bjcMSG3": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_AFTERSESSIONMOAN[1]);
                    break;
                }
                case "bjcMSG4": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[0]);
                    break;
                }
                case "bjcMSG5": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[1]);
                    break;
                }
                case "bjcMSG6": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[2]);
                    break;
                }
                case "bjcMSG7": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[3]);
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
                    ds_class200.b();
                    this.void_r();
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundEventHandler.MISC_BEDRUSTLE[0]);
                    this.r = this.rotationYaw;
                    break;
                }
                case "doggyGoOnBedMSG2": {
                    this.void_a("what are you waiting for?~");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING[9]);
                    break;
                }
                case "doggyGoOnBedMSG3": {
                    this.void_a("this ass ain't gonna fuck itself...");
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_GIGGLE[0]);
                    break;
                }
                case "doggyGoOnBedMSG4": {
                    this.a(SoundEventHandler.MISC_SLAP[0], 0.75f);
                    break;
                }
                case "doggyGoOnBedDone": {
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new b0_class58(this.girlID(), Minecraft.getMinecraft().player.getPersistentID()));
                    this.setCurrentAction(Action.WAITDOGGY);
                    break;
                }
                case "doggystartMSG1": {
                    this.PlaySound(SoundEventHandler.MISC_TOUCH[0]);
                    break;
                }
                case "doggystartMSG2": {
                    this.PlaySound(SoundEventHandler.MISC_TOUCH[1]);
                    break;
                }
                case "doggystartMSG3": {
                    this.a(SoundEventHandler.MISC_BEDRUSTLE[1], 0.5f);
                    break;
                }
                case "doggystartMSG4": {
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.MISC_SMALLINSERTS));
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_MMM[1]);
                    if (!this.boolean_n()) break;
                    ds_class200.b();
                    break;
                }
                case "doggystartMSG5": {
                    this.a(SoundEventHandler.a(SoundEventHandler.MISC_POUNDING), 0.33f);
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggystartDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    if (!this.boolean_n()) break;
                    ds_class200.d();
                    break;
                }
                case "doggyslowMSG1": {
                    this.ar = false;
                    this.a(SoundEventHandler.a(SoundEventHandler.MISC_POUNDING), 0.33f);
                    int n = ModInfo.f.nextInt(4);
                    if (n == 0) {
                        n = ModInfo.f.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MMM));
                        } else {
                            this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MOAN));
                        }
                    } else {
                        this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_HEAVYBREATHING));
                    }
                    if (!this.boolean_n()) break;
                    ds_class200.a(0.00666);
                    break;
                }
                case "doggyslowMSG2": {
                    this.a(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_LIGHTBREATHING), 0.5f);
                    break;
                }
                case "doggyfastMSG1": {
                    this.a(SoundEventHandler.a(SoundEventHandler.MISC_POUNDING), 0.75f);
                    if (this.boolean_n()) {
                        ds_class200.a(0.02);
                    }
                    ++this.aq;
                    if (this.aq % 2 == 0) {
                        int n = ModInfo.f.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MOAN));
                            break;
                        }
                        this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_HEAVYBREATHING));
                        break;
                    }
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_AHH));
                    break;
                }
                case "doggyfastDone": {
                    this.ar = false;
                    this.setCurrentAction(Action.DOGGYSLOW);
                    break;
                }
                case "doggycumMSG1": {
                    this.a(SoundEventHandler.MISC_CUMINFLATION[0], 2.0f);
                    this.a(SoundEventHandler.a(SoundEventHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggycumMSG2": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_HEAVYBREATHING[4]);
                    break;
                }
                case "doggycumMSG3": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_HEAVYBREATHING[5]);
                    break;
                }
                case "doggycumMSG4": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_HEAVYBREATHING[6]);
                    break;
                }
                case "doggycumMSG5": {
                    this.PlaySound(SoundEventHandler.GIRLS_JENNY_HEAVYBREATHING[7]);
                    break;
                }
                case "pearl": {
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new gg_class366(this.girlID()));
                    break;
                }
                case "boobjob_camera": {
                    if (!this.boolean_n() || this.as) break;
                    this.as = true;
                    this.r = 180.0f;
                    this.a(-0.7, -0.6, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    ds_class200.b();
                    ds_class200.d();
                    break;
                }
                case "paizuriFastMSG1": {
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_MMM));
                    } else {
                        this.PlaySound(SoundEventHandler.a(SoundEventHandler.GIRLS_JENNY_AHH));
                    }
                    if (!this.boolean_n()) break;
                    ds_class200.a(0.04);
                    break;
                }
                case "paizuriSlowMSG1": 
                case "paizuriStartMSG1": {
                    this.PlaySound(SoundEventHandler.a(SoundEventHandler.MISC_POUNDING));
                    if (!this.boolean_n()) break;
                    ds_class200.a(0.02);
                    break;
                }
                case "paizuri_fastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    if (!this.boolean_n() || this.as) break;
                    this.as = true;
                    this.a(-0.7, -0.6, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startStep": {
                    IBlockState iBlockState = this.world.getBlockState(this.getPosition().subtract(new Vec3i(0, 1, 0)));
                    this.PlaySound(iBlockState.getBlock().getSoundType(iBlockState, this.world, this.getPosition(), this).getStepSound());
                    break;
                }
                case "paizuri_cumStart": {
                    if (!this.boolean_n() || this.as) break;
                    this.a(-0.7, -0.6, -0.2, 60.0f, -3.0f);
                }
            }
        };
        this.C.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.C);
        animationData.addAnimationController(this.E);
        animationData.addAnimationController(this.s);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

