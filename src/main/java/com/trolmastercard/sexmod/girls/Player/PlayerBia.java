/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Player;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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

public class PlayerBia
extends PlayerGirl {
    int ar = -1;
    boolean ap = false;
    int aq = 1;

    public PlayerBia(World world) {
        super(world);
    }

    public PlayerBia(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 1.5f;
    }

    @Override
    public float getEyeHeight() {
        return 1.5f;
    }

    @Override
    public void u_() {
    }

    @Override
    public boolean boolean_a(String string) {
        if ("anal".equals(string)) {
            this.setCurrentAction(Action.ANAL_PREPARE);
            this.f(0);
            return true;
        }
        if ("doggy".equals(string)) {
            this.setCurrentAction(Action.SITDOWN);
            this.f(0);
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void void_H() {
        GirlEntity.a(Minecraft.getMinecraft().player, this, new String[]{"anal", "doggy"}, false);
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("action.names.headpat".equals(string)) {
            this.void_b(uUID);
            this.setCurrentAction(Action.HEAD_PAT);
            this.a(this.int_ah(), Action.HEAD_PAT);
        }
    }

    @Override
    public IRenderer com_trolmastercard_sexmod_at_class43_a(int n) {
        return new go_class378();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/bia/hand.png";
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
    public boolean boolean_A() {
        return false;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        GirlEntity.a(entityPlayer, this, new String[]{"action.names.headpat"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        if (this.currentAction() == Action.ANAL_CUM && (fp_class3242 == Action.ANAL_FAST || fp_class3242 == Action.ANAL_SLOW)) {
            return;
        }
        if (this.currentAction() == Action.PRONE_DOGGY_CUM && (fp_class3242 == Action.PRONE_DOGGY_HARD || fp_class3242 == Action.PRONE_DOGGY_SOFT)) {
            return;
        }
        super.setCurrentAction(fp_class3242);
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.ANAL_SLOW) {
            return Action.ANAL_FAST;
        }
        if (action == Action.PRONE_DOGGY_INTRO) {
            return Action.PRONE_DOGGY_INSERT;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.ANAL_SLOW || action == Action.ANAL_FAST) {
            return Action.ANAL_CUM;
        }
        if (action == Action.PRONE_DOGGY_SOFT || action == Action.PRONE_DOGGY_HARD) {
            return Action.PRONE_DOGGY_CUM;
        }
        return null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.a_();
    }

    @Override
    protected void V() {
        super.V();
        this.ar = -1;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean boolean_a(EntityPlayer entityPlayer) {
        return Minecraft.getMinecraft().player.getPersistentID().equals(entityPlayer.getPersistentID());
    }

    void a_() {
        float f;
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 != Action.ANAL_WAIT && fp_class3242 != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_j();
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getDistance(this) > 1.0f) {
            return;
        }
        if (this.world.isRemote && !this.boolean_a(entityPlayer)) {
            return;
        }
        if (this.ar == -1) {
            if (this.world.isRemote) {
                fh_class313.b();
                HandlePlayerMovement.a(false);
            } else {
                this.void_e(entityPlayer.getPersistentID());
            }
            this.ar = GirlEntity.j;
            return;
        }
        if (--this.ar > 0) {
            return;
        }
        this.ar = -1;
        entityPlayer.noClip = true;
        entityPlayer.setNoGravity(true);
        if (fp_class3242 == Action.ANAL_WAIT) {
            if (!this.world.isRemote) {
                this.setCurrentAction(Action.ANAL_START);
                Vec3d vec3d = this.net_minecraft_util_math_Vec3d_o().add(ck_class135.a(-0.3, -1.0, -0.5, this.java_lang_Float_I().floatValue()));
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            } else if (this.boolean_n()) {
                SexUI.d();
            }
            return;
        }
        entityPlayer.rotationYaw = f = this.java_lang_Float_I().floatValue();
        entityPlayer.rotationPitch = 60.0f;
        if (!this.world.isRemote) {
            this.f(0);
            this.setCurrentAction(Action.PRONE_DOGGY_INTRO);
            Vec3d vec3d = this.net_minecraft_util_math_Vec3d_o();
            Vec3d vec3d2 = vec3d.add(ck_class135.a(0.0, 0.0, 1.0, f));
            this.c(vec3d2);
            EntityPlayer entityPlayer2 = this.net_minecraft_entity_player_EntityPlayer_k();
            if (entityPlayer2 != null) {
                entityPlayer2.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
            }
            Vec3d vec3d3 = vec3d.add(ck_class135.a(0.0, 1.1875 - (double)entityPlayer.getEyeHeight(), 0.5, f));
            entityPlayer.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            this.void_a(true);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void ag() {
        super.ag();
        if (this.currentAction() != Action.PRONE_DOGGY_HARD) {
            return;
        }
        int n = this.aq;
        do {
            this.aq = this.getRNG().nextInt(3) + 1;
        } while (n == this.aq);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.bia.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.bia.fhappy", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.bia.null", true, animationEvent);
                    break;
                }
                if (this.ak) {
                    this.createAnimation("animation.bia.sit", true, animationEvent);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.af) {
                    boolean bl = this.ap = !this.ap;
                }
                if (!this.af) {
                    this.createAnimation("animation.bia.fly" + (this.ap ? "2" : ""), true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.aj) {
                        this.movementController.setAnimationSpeed(1.2);
                        this.createAnimation("animation.bia.run", true, animationEvent);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(1.2);
                        this.createAnimation("animation.bia.fastwalk", true, animationEvent);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.2);
                    this.createAnimation("animation.bia.backwards_walk", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.bia.idle", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.bia.null", true, animationEvent);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.bia.strip", false, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.bia.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.bia.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.bia.ride", true, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.bia.sit", true, animationEvent);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bia.throwpearl", false, animationEvent);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.bia.downed", true, animationEvent);
                        break block5;
                    }
                    case TALK_HORNY: {
                        this.createAnimation("animation.bia.talk_horny", false, animationEvent);
                        break block5;
                    }
                    case TALK_IDLE: {
                        this.createAnimation("animation.bia.talk_idle", true, animationEvent);
                        break block5;
                    }
                    case TALK_RESPONSE: {
                        this.createAnimation("animation.bia.talk_response", true, animationEvent);
                        break block5;
                    }
                    case ANAL_PREPARE: {
                        this.createAnimation("animation.bia.anal_prepare", false, animationEvent);
                        break block5;
                    }
                    case ANAL_WAIT: {
                        this.createAnimation("animation.bia.anal_wait", true, animationEvent);
                        break block5;
                    }
                    case ANAL_START: {
                        this.createAnimation("animation.bia.anal_start", true, animationEvent);
                        break block5;
                    }
                    case ANAL_SLOW: {
                        this.createAnimation("animation.bia.anal_slow", true, animationEvent);
                        break block5;
                    }
                    case ANAL_FAST: {
                        this.createAnimation("animation.bia.anal_fast", true, animationEvent);
                        break block5;
                    }
                    case ANAL_CUM: {
                        this.createAnimation("animation.bia.anal_cum", false, animationEvent);
                        break block5;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.bia.headpat", false, animationEvent);
                        break block5;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.bia.sitdown", false, animationEvent);
                        break block5;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.bia.sitdownidle", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_INTRO: {
                        this.createAnimation("animation.bia.prone_doggy_intro", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_INSERT: {
                        this.createAnimation("animation.bia.prone_doggy_insert", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_SOFT: {
                        this.createAnimation("animation.bia.prone_doggy_soft", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_HARD: {
                        this.createAnimation("animation.bia.prone_doggy_hard" + this.aq, true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_CUM: {
                        this.createAnimation("animation.bia.prone_doggy_cum", true, animationEvent);
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
            switch (soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "stripMSG1": {
                    this.h("Hihi~");
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "pearl": {
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "talk_hornyMSG1": {
                    this.void_a("Heyaaa~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HEY[3]);
                    break;
                }
                case "talk_hornyMSG2": {
                    this.void_a("I am Hornyyyyy~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[2]);
                    break;
                }
                case "talk_hornyMSG3": {
                    this.void_a("So...");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "talk_hornyMSG4": {
                    this.void_a("Are we gonna have some fun nyaa?");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "talk_responseMSG1": {
                    this.void_a("Huh?!...");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[2]);
                    break;
                }
                case "talk_responseMSG2": {
                    this.void_a("I... uhm...");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[1]);
                    break;
                }
                case "talk_responseMSG3": {
                    this.void_a("yes~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[0]);
                    break;
                }
                case "talk_responseDone": {
                    this.s();
                    if (this.entityDataManager.get(GirlEntity.D) != 0) {
                        this.setCurrentAction(Action.STRIP);
                        break;
                    }
                    this.U();
                    break;
                }
                case "anal_prepareMSG1": {
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "anal_prepareMSG2": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    break;
                }
                case "anal_prepareDone": {
                    this.setCurrentAction(Action.ANAL_WAIT);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "anal_startMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[3]);
                    this.PlaySound(SoundsHandler.MISC_POUNDING[34]);
                    break;
                }
                case "anal_fastMSG1": {
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.5f);
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_slowMSG1": 
                case "anal_startMSG2": {
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.5f);
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                }
                case "anal_startDone": {
                    this.setCurrentAction(Action.ANAL_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "anal_cumMSG2": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_cumBlackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "doggy_cumDone": 
                case "anal_cumDone": {
                    if (this.boolean_n()) {
                        SexUI.resetCumPercentage();
                    }
                    this.void_r();
                    break;
                }
                case "headpatMSG1": {
                    this.void_a("Ooh headpats!");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "headpatMSG2": {
                    this.void_a("Hmmm.... :D");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[0]);
                    break;
                }
                case "headpatMSG3": {
                    this.void_a("huh...?");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "headpatMSG4": {
                    this.void_a("Tanku hehe");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[1]);
                    break;
                }
                case "headpatDone": {
                    if (!this.boolean_e()) break;
                    this.void_r();
                    break;
                }
                case "sitdownMSG1": {
                    this.void_a("come here big boy~");
                    this.a(SoundsHandler.GIRLS_BIA_BREATH);
                    break;
                }
                case "sitdownDone": {
                    this.setCurrentAction(Action.SITDOWNIDLE);
                    break;
                }
                case "slide": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_SLIDE));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.005);
                    break;
                }
                case "pound": {
                    this.a(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "doggyMoan": {
                    this.a(this.getRNG().nextBoolean() ? SoundsHandler.GIRLS_BIA_AHH : SoundsHandler.GIRLS_BIA_MMM);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "doggySwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PRONE_DOGGY_HARD);
                    break;
                }
                case "doggyReset": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "orgasm1": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[6]);
                    break;
                }
                case "orgasm2": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[7]);
                    break;
                }
                case "openSexUI": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
        animationData.addAnimationController(this.actionController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

