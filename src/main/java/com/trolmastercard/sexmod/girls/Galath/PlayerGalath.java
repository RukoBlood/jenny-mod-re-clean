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
import com.trolmastercard.sexmod.Packets.TeleportPlayer;
import com.trolmastercard.sexmod.Packets.UpdateVelocity;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Galath.GalathFlightUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.AnimationStateHolder;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.interfaces.IWingsOwner;
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
implements IWingsOwner {
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
    public IRenderer getHandRenderer(int n) {
        return new GalathHand();
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
    public float getNameTagHeightOffset() {
        return 2.3f;
    }

    @Override
    public void onGuiActionSelected(String actionName, UUID partnerUUID) {
        if ("cowgirl".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.RAPE_INTRO);
            this.initActionState(this.getOutfitIndex(), Action.RAPE_INTRO);
            return;
        }
        if ("mating press".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.CORRUPT_SLOW);
            this.initActionState(this.getOutfitIndex(), Action.CORRUPT_SLOW);
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
        Vec3d vec3d = VectorMath.rotate(new Vec3d(0.5, 0.5f - entityPlayer.getEyeHeight(), 0.4f), this.getYawRotation().floatValue()).add(this.getTargetPosition());
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean isWingsAnimated() {
        return false;
    }

    @Override
    public boolean openGuiForPlayer(EntityPlayer player) {
        PlayerGalath.openInventoryGui(player, this, new String[]{"cowgirl", "mating press", "ride"}, false);
        return true;
    }

    @Override
    public boolean shouldRenderArmor() {
        return false;
    }

    @Override
    public boolean useVanillaItemHolding() {
        return false;
    }

    @Override
    public AnimationStateHolder getWingAnimationState() {
        return new AnimationStateHolder(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean hasWingState() {
        return this.getOutfitIndex() == 0 || this.ap;
    }

    @Override
    public boolean isWingsVisible() {
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
    public void spawnHitboxHelper() {
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
        if (!this.isControlledByLocalPlayer()) {
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
        EntityPlayer entityPlayer = this.getOwnerPlayerEntity();
        if (entityPlayer == null) {
            return false;
        }
        return this.world.getBlockState(entityPlayer.getPosition().up().up()).getBlock() != Blocks.AIR;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        block5 : switch (event.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.galath.null", true, event);
                    break;
                }
                this.createAnimation("animation.galath.blink", true, event);
                break;
            }
            case "movement": {
                this.movementController.setAnimationSpeed(1.0);
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.galath.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.galath.sit", true, event);
                    break;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.galath.controlled_flight", true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) == 0.0f) {
                    this.createAnimation(this.boolean_g() ? "animation.galath.crouchidle" : "animation.galath.idle", true, event);
                    break;
                }
                if (this.isPlayerSprinting) {
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation(this.boolean_g() ? "animation.galath.crouchwalk" : "animation.galath.run", true, event);
                    break;
                }
                if (this.ao.y >= -0.1f) {
                    this.movementController.setAnimationSpeed(2.0);
                    this.createAnimation(this.boolean_g() ? "animation.galath.crouchwalk" : "animation.galath.walk", true, event);
                    break;
                }
                this.movementController.setAnimationSpeed(1.5);
                this.createAnimation(this.boolean_g() ? "animation.galath.crouchwalk" : "animation.galath.backwards_walk", true, event);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        return PlayState.STOP;
                    }
                    case STRIP: {
                        this.createAnimation("animation.galath.strip", true, event);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.galath.attack" + this.nextAttack, true, event);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.galath.bowcharge", true, event);
                        break block5;
                    }
                    case RIDE:
                    case SIT: {
                        this.createAnimation("animation.galath.sit", true, event);
                        break block5;
                    }
                    case RAPE_INTRO: {
                        this.createAnimation("animation.galath.rape_intro", true, event);
                        break block5;
                    }
                    case RAPE_ON_GOING: {
                        this.createAnimation("animation.galath.rape" + this.ar, true, event);
                        break block5;
                    }
                    case RAPE_CUM: {
                        this.createAnimation("animation.galath.rape_cum", true, event);
                        break block5;
                    }
                    case RAPE_CUM_IDLE: {
                        this.createAnimation("animation.galath.rape_cum_idle", true, event);
                        break block5;
                    }
                    case CORRUPT_FAST: {
                        this.createAnimation("animation.galath.corrupt_" + (this.as ? "hard" : "soft"), true, event);
                        break block5;
                    }
                    case CORRUPT_SLOW: {
                        this.createAnimation("animation.galath.corrupt_slow", true, event);
                        break block5;
                    }
                    case CORRUPT_INTRO: {
                        this.createAnimation("animation.galath.corrupt_intro", true, event);
                        break block5;
                    }
                    case CORRUPT_CUM: {
                        this.createAnimation("animation.galath.corrupt_cum", true, event);
                        break block5;
                    }
                    case CONTROLLED_FLIGHT: {
                        this.createAnimation("animation.galath.controlled_flight", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        this.initAnimationControllers();
        this.actionController.registerSoundListener(soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "pound": {
                    this.playSoundAroundHer(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "flap": {
                    this.playSoundAroundHer(SoundsHandler.MISC_FLAP);
                    break;
                }
                case "setNude": {
                    this.ap = true;
                    Vec3d vec3d = this.getPositionVector();
                    Vec3d vec3d2 = this.getCachedBoneOffset("slipR").add(vec3d);
                    Vec3d vec3d3 = this.getCachedBoneOffset("slipL").add(vec3d);
                    Vec3d vec3d4 = this.getCachedBoneOffset("turnable").add(vec3d);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d2.x, vec3d2.y, vec3d2.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d3.x, vec3d3.y, vec3d3.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d4.x, vec3d4.y, vec3d4.z, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case "rapeIntroDone": {
                    if (!this.isControlledByLocalPlayer()) break;
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
                    this.playSoundAroundHer(SoundsHandler.MISC_POUNDING);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.03f);
                    break;
                }
                case "enableRapeUI": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.a(false);
                    break;
                }
                case "reloadRenderer": {
                    if (!this.isControlledByLocalPlayer()) {
                        return;
                    }
                    Minecraft minecraft = Minecraft.getMinecraft();
                    if (minecraft.gameSettings.thirdPersonView == 0) break;
                    minecraft.renderGlobal.loadRenderers();
                    break;
                }
                case "corruptSwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CORRUPT_FAST);
                    break;
                }
                case "corrupt_hard": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.as = true;
                    this.resetAnimationControllerOffset();
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
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    break;
                }
                case "setCamCorrupt": {
                    if (!this.isControlledByLocalPlayer()) {
                        return;
                    }
                    this.aq = true;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    float f = this.getYawRotation().floatValue() + 220.0f;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.5, 0.5f - entityPlayerSP.getEyeHeight(), 0.4f), this.getYawRotation().floatValue()).add(this.getTargetPosition());
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, f, 15.0f));
                    SexUI.init();
                    break;
                }
                case "enableBoyCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.aq = false;
                    break;
                }
                case "creampie": {
                    ga_class358.a(new DynamicTrailRenderer(130, em_class2582 -> {
                        Vec3d vec3d = em_class2582.getBoneWorldPosition("futaCockTip");
                        Vec3d vec3d2 = em_class2582.getBoneWorldPosition("futaCockTipDirHelp");
                        return vec3d.subtract(vec3d2).normalize();
                    }, em_class2582 -> em_class2582.getCachedBoneOffset("futaCockTip").add(em_class2582.getTargetPosition()), this, 0.3f, 0.3f));
                    ga_class358.a(new DynamicTrailRenderer(100, em_class2582 -> VectorMath.rotate(new Vec3d(0.0, 0.0, 0.6f), this.getYawRotation().floatValue()), em_class2582 -> em_class2582.getCachedBoneOffset("creampiePos").add(em_class2582.getTargetPosition()), this, 0.6f, 0.5f));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS), 3.0f);
                    break;
                }
                case "blackScreenTamed": 
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "flapControlled": {
                    if (!this.isControlledByLocalPlayer()) break;
                    GalathFlightUI.showUI();
                    this.playSoundAroundHer(SoundsHandler.MISC_FLAP);
                    Minecraft minecraft = Minecraft.getMinecraft();
                    EntityPlayerSP entityPlayerSP = minecraft.player;
                    MovementInput movementInput = entityPlayerSP.movementInput;
                    Vec2f vec2f = movementInput.getMoveVector();
                    if (vec2f.x == 0.0f && vec2f.y == 0.0f) break;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(-vec2f.x, 0.0, vec2f.y), Reference.LerpFloat(entityPlayerSP.prevRotationPitch, entityPlayerSP.rotationPitch, minecraft.getRenderPartialTicks()), Reference.LerpFloat(entityPlayerSP.prevRotationYawHead, entityPlayerSP.rotationYawHead, minecraft.getRenderPartialTicks()));
                    PackageHandler.INSTANCE.sendToServer((IMessage)new UpdateVelocity(vec3d, this.girlID()));
                    break;
                }
                case "clap": {
                    this.playSoundAroundHer(SoundsHandler.MISC_CLAP);
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
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.init();
                }
            }
        });
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.eyesController);
        data.addAnimationController(this.movementController);
    }
}

