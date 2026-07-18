/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.TeleportPlayer;
import com.trolmastercard.sexmod.Packages.UpdateVelocity;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.f2_class286;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerGalath
extends PlayerGirl
implements b7_class68 {
    boolean ap = false;
    int ar = 0;
    boolean as = false;
    boolean aq = false;

    public PlayerGalath(World world) {
        super(world);
    }

    public PlayerGalath(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public IRenderer getLimbRenderer(int n) {
        return new GalathLimb();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/galath/hand.png";
    }

    @Override
    @Nullable
    protected Action FastSexAction(Action action) {
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.CORRUPT_FAST || action == Action.CORRUPT_SLOW) {
            return Action.CORRUPT_CUM;
        }
        if (action == Action.RAPE_ON_GOING) {
            return Action.RAPE_CUM;
        }
        return null;
    }

    @Override
    public float float_i() {
        return 2.3f;
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("cowgirl".equals(string)) {
            this.void_b(uUID);
            this.setCurrentAction(Action.RAPE_INTRO);
            this.a(this.int_ah(), Action.RAPE_INTRO);
            return;
        }
        if ("mating press".equals(string)) {
            this.void_b(uUID);
            this.setCurrentAction(Action.CORRUPT_SLOW);
            this.a(this.int_ah(), Action.CORRUPT_SLOW);
            this.void_a();
            return;
        }
    }

    @Override
    public void setCurrentAction(Action action) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.CORRUPT_CUM && (action == Action.CORRUPT_FAST || action == Action.CORRUPT_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.RAPE_CUM && action == Action.RAPE_ON_GOING) {
            return;
        }
        if (fp_class3243 == Action.RAPE_CUM && action == Action.RAPE_CUM_IDLE) {
            return;
        }
        if (action == Action.CORRUPT_SLOW) {
            this.as = false;
        }
        super.setCurrentAction(action);
    }

    void void_a() {
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = VectorMath.rotate(new Vec3d(0.5, 0.5f - entityPlayer.getEyeHeight(), 0.4f), this.java_lang_Float_I().floatValue()).add(this.net_minecraft_util_math_Vec3d_o());
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean boolean_b() {
        return false;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        PlayerGalath.a(entityPlayer, this, new String[]{"cowgirl", "mating press", "ride"}, false);
        return true;
    }

    @Override
    public boolean boolean_v() {
        return false;
    }

    @Override
    public boolean boolean_A() {
        return false;
    }

    @Override
    public f2_class286 com_trolmastercard_sexmod_f2_class286_d() {
        return new f2_class286(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean boolean_c() {
        return this.int_ah() == 0 || this.ap;
    }

    @Override
    public boolean boolean_a() {
        switch (this.currentAction()) {
            case CORRUPT_CUM:
            case CORRUPT_FAST:
            case CORRUPT_SLOW:
            case COWGIRLCUM: {
                return false;
            }
        }
        return true;
    }

    @Override
    public void void_B() {
        this.c(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.void_b();
        if (this.world.isRemote) {
            this.void_d();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void void_d() {
        if (!this.boolean_n()) {
            return;
        }
        if (this.currentAction() != Action.RAPE_INTRO) {
            return;
        }
        SexUI.a(false);
    }

    void void_b() {
        switch (this.currentAction()) {
            case CORRUPT_CUM:
            case CORRUPT_FAST:
            case CORRUPT_SLOW:
            case RAPE_INTRO:
            case RAPE_ON_GOING:
            case RAPE_CUM:
            case RAPE_CHARGE:
            case RAPE_CUM_IDLE: {
                this.ap = true;
                return;
            }
        }
        this.ap = false;
    }

    boolean boolean_g() {
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return false;
        }
        return this.world.getBlockState(entityPlayer.getPosition().up().up()).getBlock() != Blocks.AIR;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.galath.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.galath.blink", true, animationEvent);
                break;
            }
            case "movement": {
                this.movementController.setAnimationSpeed(1.0);
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.galath.null", true, animationEvent);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.galath.sit", true, animationEvent);
                    break;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.galath.controlled_flight", true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) == 0.0f) {
                    this.createAnimation(this.boolean_g() ? "animation.galath.crouchidle" : "animation.galath.idle", true, animationEvent);
                    break;
                }
                if (this.isPlayerSprinting) {
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation(this.boolean_g() ? "animation.galath.crouchwalk" : "animation.galath.run", true, animationEvent);
                    break;
                }
                if (this.ao.y >= -0.1f) {
                    this.movementController.setAnimationSpeed(2.0);
                    this.createAnimation(this.boolean_g() ? "animation.galath.crouchwalk" : "animation.galath.walk", true, animationEvent);
                    break;
                }
                this.movementController.setAnimationSpeed(1.5);
                this.createAnimation(this.boolean_g() ? "animation.galath.crouchwalk" : "animation.galath.backwards_walk", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        return PlayState.STOP;
                    }
                    case STRIP: {
                        this.createAnimation("animation.galath.strip", true, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.galath.attack" + this.S, true, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.galath.bowcharge", true, animationEvent);
                        break block5;
                    }
                    case RIDE:
                    case SIT: {
                        this.createAnimation("animation.galath.sit", true, animationEvent);
                        break block5;
                    }
                    case RAPE_INTRO: {
                        this.createAnimation("animation.galath.rape_intro", true, animationEvent);
                        break block5;
                    }
                    case RAPE_ON_GOING: {
                        this.createAnimation("animation.galath.rape" + this.ar, true, animationEvent);
                        break block5;
                    }
                    case RAPE_CUM: {
                        this.createAnimation("animation.galath.rape_cum", true, animationEvent);
                        break block5;
                    }
                    case RAPE_CUM_IDLE: {
                        this.createAnimation("animation.galath.rape_cum_idle", true, animationEvent);
                        break block5;
                    }
                    case CORRUPT_FAST: {
                        this.createAnimation("animation.galath.corrupt_" + (this.as ? "hard" : "soft"), true, animationEvent);
                        break block5;
                    }
                    case CORRUPT_SLOW: {
                        this.createAnimation("animation.galath.corrupt_slow", true, animationEvent);
                        break block5;
                    }
                    case CORRUPT_INTRO: {
                        this.createAnimation("animation.galath.corrupt_intro", true, animationEvent);
                        break block5;
                    }
                    case CORRUPT_CUM: {
                        this.createAnimation("animation.galath.corrupt_cum", true, animationEvent);
                        break block5;
                    }
                    case CONTROLLED_FLIGHT: {
                        this.createAnimation("animation.galath.controlled_flight", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData animationData) {
        this.void_p();
        this.actionController.registerSoundListener(soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "pound": {
                    this.a(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "flap": {
                    this.a(SoundsHandler.MISC_FLAP);
                    break;
                }
                case "setNude": {
                    this.ap = true;
                    Vec3d vec3d = this.getPositionVector();
                    Vec3d vec3d2 = this.b("slipR").add(vec3d);
                    Vec3d vec3d3 = this.b("slipL").add(vec3d);
                    Vec3d vec3d4 = this.b("turnable").add(vec3d);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d2.x, vec3d2.y, vec3d2.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d3.x, vec3d3.y, vec3d3.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d4.x, vec3d4.y, vec3d4.z, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case "rapeIntroDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.RAPE_ON_GOING);
                    break;
                }
                case "rape_switch": {
                    Random random = this.getRNG();
                    int n = this.ar;
                    do {
                        this.ar = random.nextInt(3);
                    } while (this.ar == n);
                    break;
                }
                case "poundRape": {
                    this.a(SoundsHandler.MISC_POUNDING);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.03f);
                    break;
                }
                case "enableRapeUI": {
                    if (!this.boolean_n()) break;
                    SexUI.a(false);
                    break;
                }
                case "reloadRenderer": {
                    if (!this.boolean_n()) {
                        return;
                    }
                    Minecraft minecraft = Minecraft.getMinecraft();
                    if (minecraft.gameSettings.thirdPersonView == 0) break;
                    minecraft.renderGlobal.loadRenderers();
                    break;
                }
                case "corruptSwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CORRUPT_FAST);
                    break;
                }
                case "corrupt_hard": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.as = true;
                    this.N();
                    break;
                }
                case "corrupt_hard_end": {
                    this.setCurrentAction(Action.CORRUPT_SLOW);
                    this.as = false;
                    break;
                }
                case "addCum": {
                    SexUI.addCumPercentage(0.03);
                    break;
                }
                case "clearcum": {
                    ga_class358.a(this);
                }
                case "reset": {
                    if (!this.boolean_n()) break;
                    this.void_r();
                    break;
                }
                case "setCamCorrupt": {
                    if (!this.boolean_n()) {
                        return;
                    }
                    this.aq = true;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    float f = this.java_lang_Float_I().floatValue() + 220.0f;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.5, 0.5f - entityPlayerSP.getEyeHeight(), 0.4f), this.java_lang_Float_I().floatValue()).add(this.net_minecraft_util_math_Vec3d_o());
                    PackageHandler.networkWrapper.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, f, 15.0f));
                    SexUI.init();
                    break;
                }
                case "enableBoyCam": {
                    if (!this.boolean_n()) break;
                    this.aq = false;
                    break;
                }
                case "creampie": {
                    ga_class358.a(new DynamicTrailRenderer(130, em_class2582 -> {
                        Vec3d vec3d = em_class2582.d("futaCockTip");
                        Vec3d vec3d2 = em_class2582.d("futaCockTipDirHelp");
                        return vec3d.subtract(vec3d2).normalize();
                    }, em_class2582 -> em_class2582.b("futaCockTip").add(em_class2582.net_minecraft_util_math_Vec3d_o()), this, 0.3f, 0.3f));
                    ga_class358.a(new DynamicTrailRenderer(100, em_class2582 -> VectorMath.rotate(new Vec3d(0.0, 0.0, 0.6f), this.java_lang_Float_I().floatValue()), em_class2582 -> em_class2582.b("creampiePos").add(em_class2582.net_minecraft_util_math_Vec3d_o()), this, 0.6f, 0.5f));
                    this.a(SoundsHandler.a(SoundsHandler.MISC_SMALLINSERTS), 3.0f);
                    break;
                }
                case "blackScreenTamed": 
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "flapControlled": {
                    if (!this.boolean_n()) break;
                    GalathFlightUI.showUI();
                    this.a(SoundsHandler.MISC_FLAP);
                    Minecraft minecraft = Minecraft.getMinecraft();
                    EntityPlayerSP entityPlayerSP = minecraft.player;
                    MovementInput movementInput = entityPlayerSP.movementInput;
                    Vec2f vec2f = movementInput.getMoveVector();
                    if (vec2f.x == 0.0f && vec2f.y == 0.0f) break;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(-vec2f.x, 0.0, vec2f.y), Reference.LerpFloat(entityPlayerSP.prevRotationPitch, entityPlayerSP.rotationPitch, minecraft.getRenderPartialTicks()), Reference.LerpFloat(entityPlayerSP.prevRotationYawHead, entityPlayerSP.rotationYawHead, minecraft.getRenderPartialTicks()));
                    PackageHandler.networkWrapper.sendToServer((IMessage)new UpdateVelocity(vec3d, this.girlID()));
                    break;
                }
                case "clap": {
                    this.a(SoundsHandler.MISC_CLAP);
                    break;
                }
                case "energysound": {
                    this.PlaySound(SoundsHandler.MISC_BEEW[1]);
                    break;
                }
                case "energy2": {
                    this.PlaySound(SoundsHandler.MISC_BEEW[2]);
                    break;
                }
                case "tpSound": {
                    this.PlaySound(SoundsHandler.MISC_WEOWEO[2]);
                    break;
                }
                case "sexui": {
                    if (!this.boolean_n()) break;
                    SexUI.init();
                }
            }
        });
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.eyesController);
        animationData.addAnimationController(this.movementController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

