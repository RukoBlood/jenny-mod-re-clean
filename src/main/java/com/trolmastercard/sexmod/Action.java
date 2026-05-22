/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.controller.AnimationController;

//fp_class324
public enum Action {
    NULL(0, false, true),
    STARTBLOWJOB(2, true, false),
    SUCKBLOWJOB(2, true, false),
    SUCKBLOWJOB_BLINK(2, true, true),
    CUMBLOWJOB(0, true, false),
    THRUSTBLOWJOB(2, true, false),
    PAYMENT(5, true, false),
    STARTDOGGY(2, false, false),
    WAITDOGGY(0, false, true),
    DOGGYSTART(0, true, false),
    DOGGYSLOW(2, true, false),
    DOGGYFAST(2, true, false),
    DOGGYCUM(2, true, false),
    STRIP(5, false, false),
    DASH(2, false, false),
    HUG(2, true, false),
    HUGIDLE(0, true, true),
    HUGSELECTED(0, true, false),
    UNDRESS(2, false, true),
    DRESS(2, false, true),
    SITDOWN(2, false, false, 60.0f, -90.0f, true),
    SITDOWNIDLE(0, false, true, 60.0f, -60.0f, true),
    COWGIRLSTART(0, true, false, 60.0f, -60.0f, false),
    COWGIRLSLOW(10, true, false, 60.0f, -60.0f, false),
    COWGIRLFAST(10, true, false, 60.0f, -60.0f, false),
    COWGIRLCUM(2, true, false, 60.0f, -60.0f, false),
    ATTACK(0, false, true),
    BOW(2, false, true),
    RIDE(0, false, true),
    SIT(0, false, true),
    THROW_PEARL(0, false, false),
    DOWNED(7, false, true),
    PAIZURI_START(0, true, false, -56.0f, -90.0f, false, true),
    PAIZURI_IDLE(0, true, false, -56.0f, -90.0f, false, true),
    PAIZURI_SLOW(0, true, true, -56.0f, -90.0f, false, true),
    PAIZURI_FAST(0, true, false, -56.0f, -90.0f, false, true),
    PAIZURI_FAST_CONTINUES(0, true, false, -56.0f, -90.0f, false, true),
    PAIZURI_CUM(0, true, false, -56.0f, -90.0f, false, true),
    MISSIONARY_START(0, true, false, 30.0f, -90.0f, true),
    MISSIONARY_SLOW(2, true, false, 30.0f, -90.0f, true),
    MISSIONARY_FAST(2, true, false, 30.0f, -90.0f, true),
    MISSIONARY_CUM(2, true, false, 30.0f, -90.0f, true),
    TALK_HORNY(5, true, false),
    TALK_IDLE(0, true, true),
    TALK_RESPONSE(2, true, false),
    ANAL_PREPARE(5, false, false),
    ANAL_WAIT(0, false, true),
    ANAL_START(0, true, false),
    ANAL_SLOW(2, true, true),
    ANAL_FAST(0, true, false),
    ANAL_CUM(2, true, false),
    KOBOLD_ANAL_START(0, true, false, false, 4.0f, -80.0f, true),
    KOBOLD_ANAL_SLOW(0, true, true, false, 4.0f, -80.0f, true),
    KOBOLD_ANAL_FAST(0, true, false, false, 4.0f, -80.0f, true),
    KOBOLD_ANAL_CUM(2, true, false, false, 4.0f, -80.0f, true),
    SUMMON(0, false, false, false, true),
    SUMMON_WAIT(0, false, true, false, true),
    HEAD_PAT(0, true, false),
    ALLIE_PREPARE_FIRST_TIME(0, false, false, 40.0f, -40.0f, false),
    DEEPTHROAT_START(0, true, false, true, 40.0f, -40.0f, false),
    DEEPTHROAT_SLOW(2, true, false, true, 40.0f, -40.0f, false),
    DEEPTHROAT_FAST(2, true, false, true, 40.0f, -40.0f, false),
    DEEPTHROAT_CUM(2, true, false, true, 40.0f, -40.0f, false),
    ALLIE_PREPARE_NORMAL(2, false, false, 40.0f, -40.0f, false),
    SUMMON_NORMAL(0, false, false),
    SUMMON_SAND(0, false, false),
    SUMMON_NORMAL_WAIT(2, false, true),
    RICH_FIRST_TIME(0, false, false),
    RICH_NORMAL(0, false, false),
    CITIZEN_START(0, true, false, 10.0f, -90.0f, false),
    CITIZEN_SLOW(0, true, false, 10.0f, -90.0f, false),
    CITIZEN_FAST(0, true, false, 10.0f, -90.0f, false),
    CITIZEN_CUM(2, true, false, 10.0f, -90.0f, false),
    FISHING_START(5, false, false),
    FISHING_IDLE(0, false, true),
    FISHING_EAT(0, false, false),
    FISHING_THROW_AWAY(0, false, false),
    TOUCH_BOOBS_INTRO(0, true, false),
    TOUCH_BOOBS_SLOW(2, true, false),
    TOUCH_BOOBS_FAST(2, true, false),
    TOUCH_BOOBS_CUM(2, true, false),
    WAIT_CAT(0, false, false, 30.0f, -90.0f, true),
    COWGIRL_SITTING_INTRO(0, true, false),
    COWGIRL_SITTING_SLOW(5, true, false),
    COWGIRL_SITTING_FAST(5, true, false),
    COWGIRL_SITTING_CUM(5, true, false),
    MINE(0, false, false),
    SLEEP(5, false, false),
    MATING_PRESS_START(0, true, false, false, -50.0f, -90.0f, false),
    MATING_PRESS_SOFT(0, true, false, -50.0f, -90.0f, false),
    MATING_PRESS_HARD(0, true, false, -50.0f, -90.0f, false),
    MATING_PRESS_CUM(2, true, false, -30.0f, -90.0f, false),
    SHOULDER_IDLE(0, false, true, false, true),
    PICK_UP(0, true, false, 10.0f, -90.0f, true, true),
    RUN(5, false, true),
    CATCH(0, true, false),
    CATCH_BJ(0, true, false),
    CATCH_BJ_IDLE(0, true, false),
    START_THROWING(0, true, true),
    THROWN(0, false, true),
    JUMP_0(0, true, false),
    JUMP_1(0, false, false),
    JUMP_2(0, false, false),
    BREEDING_INTRO_0(0, true, false),
    BREEDING_INTRO_1(0, false, false),
    BREEDING_INTRO_2(0, false, false),
    BREEDING_SLOW_0(0, true, false),
    BREEDING_1(0, false, false),
    BREEDING_SLOW_2(5, false, false),
    BREEDING_FAST_0(0, true, false),
    BREEDING_FAST_2(5, false, false),
    BREEDING_CUM_0(0, true, false),
    BREEDING_CUM_1(0, false, false),
    BREEDING_CUM_2(0, false, false),
    AWAIT_PICK_UP(0, false, true),
    VANISH(0, false, true),
    STAND_UP(0, false, false),
    NELSON_INTRO(0, true, false, 30.0f, -20.0f, false, true),
    NELSON_SLOW(0, true, false, 30.0f, -20.0f, false, true),
    NELSON_FAST(0, true, false, 30.0f, -20.0f, false, true),
    NELSON_CUM(0, true, false, 30.0f, -20.0f, false, true),
    CARRY_SLOW(0, true, false, true, true),
    CARRY_FAST(0, true, false, true, true),
    CARRY_CUM(0, true, false, true, true),
    CARRY_INTRO(0, true, false, true, true, 191, CARRY_SLOW),
    PRONE_DOGGY_INTRO(0, true, false, true, true),
    PRONE_DOGGY_SOFT(0, true, false, true, true),
    PRONE_DOGGY_HARD(0, true, false, true, true, 34, PRONE_DOGGY_SOFT),
    PRONE_DOGGY_INSERT(2, true, false, true, true, 42, PRONE_DOGGY_SOFT),
    PRONE_DOGGY_CUM(0, true, false, true, true),
    REVERSE_COWGIRL_SLOW(0, true, false, true, 30.0f, -90.0f, true),
    REVERSE_COWGIRL_FAST_START(0, true, false, true, 34, REVERSE_COWGIRL_SLOW, 30.0f, -90.0f, true),
    REVERSE_COWGIRL_FAST_CONTINUES(0, true, false, true, 39, REVERSE_COWGIRL_SLOW, 30.0f, -90.0f, true),
    REVERSE_COWGIRL_CUM(0, true, false, true, 30.0f, -90.0f, true),
    REVERSE_COWGIRL_START(0, true, false, true, 88, REVERSE_COWGIRL_SLOW, 30.0f, -90.0f, true),
    WAVE_IDLE(0, false, false, false, true),
    WAVE(0, false, false, true, false, 71, WAVE_IDLE),
    FLY(0, false, true),
    SUMMON_SKELETON(0, false, false),
    ATTACK_SWORD(0, false, false),
    KNOCK_OUT_FLY(5, false, false),
    KNOCK_OUT_GROUND(3, false, false),
    KNOCK_OUT_STAND_UP(0, false, false),
    RAPE_PREPARE(0, false, false),
    RAPE_CHARGE(0, false, false),
    RAPE_ON_GOING(0, true, false, true, 60.0f, -30.0f, false),
    RAPE_INTRO(0, true, false, false, true, 46, RAPE_ON_GOING),
    RAPE_CUM_IDLE(0, true, false, true),
    RAPE_CUM(0, true, false, true, 34, RAPE_CUM_IDLE, 60.0f, -30.0f, false),
    CORRUPT_SLOW(0, true, false, -30.0f, -90.0f, false),
    CORRUPT_FAST(0, true, false, -30.0f, -90.0f, false),
    CORRUPT_CUM(0, true, false, false, -30.0f, -90.0f, false),
    CORRUPT_INTRO(0, true, false, true, 29, CORRUPT_SLOW),
    CONTROLLED_FLIGHT(0, true, true, true, true),
    BOOST(3, true, false, true, true, 43, CONTROLLED_FLIGHT),
    GALATH_SUMMON(0, false, false, false, true, 15, NULL),
    GALATH_DE_SUMMON(0, false, false, false, true),
    GIVE_COIN(0, true, false, true, true, 140, NULL),
    MASTERBATE(0, false, false),
    HUG_MANG(0, false, false, 239, NULL),
    RIDE_MOMMY_HEAD(0, false, true),
    THREESOME_SLOW(0, true, false, false, true),
    THREESOME_FAST(0, true, false, false, true),
    THREESOME_CUM(0, true, false, false, true),
    PUSSY_LICKING(0, false, true, false),
    MASTERBATE_SITTING(0, false, true, false),
    MASTERBATE_SITTING_CUM(0, false, false, false),
    MORNING_BLOWJOB_SLOW(0, true, true, true),
    MORNING_BLOWJOB_FAST(0, true, true, true),
    MORNING_BLOWJOB_CUM(0, true, false, true);

    final public int transitionTick;
    final public boolean hasPlayer;
    final public boolean autoBlink;
    final public float maxGirlPitch;
    final public float minGirlPitch;
    final public boolean flipGirlYaw;
    public int length;
    public int[] ticksPlaying = new int[]{0, 0};
    public Action followUp = null;
    public boolean useBoyCam;
    public boolean hideNameTag;

    private Action(int n2, boolean bl, boolean bl2) {
        this.transitionTick = n2;
        this.hasPlayer = bl;
        this.autoBlink = bl2;
        this.maxGirlPitch = 30.0f;
        this.minGirlPitch = -90.0f;
        this.flipGirlYaw = false;
        this.useBoyCam = false;
        this.hideNameTag = false;
    }

    private Action(int n2, boolean bl, boolean bl2, boolean bl3) {
        this(n2, bl, bl2);
        this.useBoyCam = bl3;
    }

    private Action(int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        this(n2, bl, bl2);
        this.useBoyCam = bl3;
        this.hideNameTag = bl4;
    }

    private Action(int n2, boolean bl, boolean bl2, float f, float f2, boolean bl3) {
        this.transitionTick = n2;
        this.hasPlayer = bl;
        this.autoBlink = bl2;
        this.maxGirlPitch = f;
        this.minGirlPitch = f2;
        this.flipGirlYaw = bl3;
        this.useBoyCam = false;
        this.hideNameTag = false;
    }

    private Action(int n2, boolean bl, boolean bl2, float f, float f2, boolean bl3, boolean bl4) {
        this.transitionTick = n2;
        this.hasPlayer = bl;
        this.autoBlink = bl2;
        this.maxGirlPitch = f;
        this.minGirlPitch = f2;
        this.flipGirlYaw = bl3;
        this.useBoyCam = false;
        this.hideNameTag = bl4;
    }

    private Action(int n2, boolean bl, boolean bl2, boolean bl3, float f, float f2, boolean bl4) {
        this.transitionTick = n2;
        this.hasPlayer = bl;
        this.autoBlink = bl2;
        this.maxGirlPitch = f;
        this.minGirlPitch = f2;
        this.flipGirlYaw = bl4;
        this.hideNameTag = false;
        this.useBoyCam = bl3;
    }

    private Action(int n2, boolean bl, boolean bl2, int n3, Action fp_class3242) {
        this(n2, bl, bl2);
        this.length = n3;
        this.followUp = fp_class3242;
    }

    private Action(int n2, boolean bl, boolean bl2, boolean bl3, int n3, Action fp_class3242) {
        this(n2, bl, bl2);
        this.length = n3;
        this.followUp = fp_class3242;
        this.useBoyCam = bl3;
    }

    private Action(int n2, boolean bl, boolean bl2, boolean bl3, int n3, Action fp_class3242, float f, float f2, boolean bl4) {
        this.transitionTick = n2;
        this.hasPlayer = bl;
        this.autoBlink = bl2;
        this.length = n3;
        this.followUp = fp_class3242;
        this.useBoyCam = bl3;
        this.minGirlPitch = f;
        this.maxGirlPitch = f2;
        this.flipGirlYaw = bl4;
    }

    private Action(int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4, int n3, Action fp_class3242) {
        this(n2, bl, bl2);
        this.length = n3;
        this.followUp = fp_class3242;
        this.useBoyCam = bl4;
        this.hideNameTag = bl3;
    }

    public static boolean a(Action fp_class3242, Action... actionArray) {
        for (Action fp_class3243 : actionArray) {
            if (fp_class3242 != fp_class3243) continue;
            return true;
        }
        return false;
    }

    public static boolean a(GirlEntity em_class2582, Action... actionArray) {
        return Action.a(em_class2582.currentAction(), actionArray);
    }

    public static double a(AnimationController animationController) {
        if (animationController == null) {
            return 0.0;
        }
        Animation animation = animationController.getCurrentAnimation();
        if (animation == null) {
            return 0.0;
        }
        return animation.animationLength;
    }

    @SideOnly(value=Side.CLIENT)
    public static float a(GirlEntity em_class2582) {
        return (float) Action.a(em_class2582.C);
    }

    @SideOnly(value=Side.CLIENT)
    public static float c(GirlEntity em_class2582, float f) {
        return (float)(em_class2582.getFactory().getOrCreateAnimationData((Integer)Integer.valueOf((int)em_class2582.getUniqueID().hashCode())).tick + (double)f - em_class2582.C.tickOffset);
    }

    @SideOnly(value=Side.CLIENT)
    public static float a(GirlEntity em_class2582, float f) {
        return Action.c(em_class2582, f) / 20.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public static float d(GirlEntity em_class2582, float f) {
        float f2 = Action.a(em_class2582);
        if (f2 <= 0.0f) {
            return 0.0f;
        }
        return be_class78.b(Action.c(em_class2582, f) / f2, 0.0f, 1.0f);
    }

    @SideOnly(value=Side.CLIENT)
    public static boolean b(GirlEntity em_class2582, float f) {
        return Action.d(em_class2582, f) == 1.0f;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

