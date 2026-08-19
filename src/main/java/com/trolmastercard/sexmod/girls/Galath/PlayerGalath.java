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
import com.trolmastercard.sexmod.util.Vector4d;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.util.interfaces.IGalath;
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
implements IGalath {
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
    protected Action getNextAction(Action action) {
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.CORRUPT_FAST || action == Action.CORRUPT_SLOW) {
            return Action.CORRUPT_CUM;
        } else {
            return action == Action.RAPE_ON_GOING ? Action.RAPE_CUM : null;
        }
    }

    @Override
    public float getScaleFactor() {
        return 2.3f;
    }

    @Override
    public void onGuiActionSelected(String command, UUID uuid) {
        if ("cowgirl".equals(command)) {
            this.teleportPlayerToGirl(uuid);
            this.setCurrentAction(Action.RAPE_INTRO);
            this.sendActionPacket(this.getOutfitIndex(), Action.RAPE_INTRO);

        }
        else if ("mating press".equals(command)) {
            this.teleportPlayerToGirl(uuid);
            this.setCurrentAction(Action.CORRUPT_SLOW);
            this.sendActionPacket(this.getOutfitIndex(), Action.CORRUPT_SLOW);
            this.handleGalathPlayerOwner();

        }
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (currentAction != Action.CORRUPT_CUM || (action != Action.CORRUPT_FAST && action != Action.CORRUPT_SLOW)) {
            if (currentAction != Action.RAPE_CUM || action != Action.RAPE_ON_GOING) {
                if (currentAction != Action.RAPE_CUM || action != Action.RAPE_CUM_IDLE) {
                    if (action == Action.CORRUPT_SLOW) {
                        this.as = false;
                    }
                    super.setCurrentAction(action);
                }
            }
        }
    }

    void handleGalathPlayerOwner() {
        EntityPlayer player = this.getPlayerEntity();
        if (player != null) {
            Vec3d pos = VectorMath.rotateByYaw(new Vec3d(0.5, 0.5f - player.getEyeHeight(), 0.4f), this.getYawRotation()).add(this.getTargetPosition());
            player.setPositionAndUpdate(pos.x, pos.y, pos.z);
        }
    }

    @Override
    public boolean isHuggingManglelie() {
        return false;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        openInventoryGui(player, this, new String[]{"cowgirl", "mating press", "ride"}, false);
        return true;
    }

    @Override
    public boolean canBeInteracted() {
        return false;
    }

    @Override
    public boolean useVanillaItemHolding() {
        return false;
    }

    @Override
    public Vector4d getFlightData() {
        return new Vector4d(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean isWingsAnimated() {
        return this.getOutfitIndex() == 0 || this.ap;
    }

    @Override
    public boolean areWingsAnimated() {
        switch (this.getCurrentAction()) {
            case CORRUPT_CUM:
            case CORRUPT_FAST:
            case CORRUPT_SLOW:
            case COWGIRLCUM:
                return false;
            default:
                return true;
        }
        //return true;
    }

    @Override
    public void spawnHitboxHelper() {
        this.handleOwnerUUID(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.handleCumState();
        if (this.world.isRemote) {
            this.handlePlayerAction();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void handlePlayerAction() {
        if (this.isControlledByLocalPlayer() && this.getCurrentAction() == Action.RAPE_INTRO) {
            SexUI.setHornyMeterVisible(false);
        }
    }

    void handleCumState() {
        switch (this.getCurrentAction()) {
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
            default:
                this.ap = false;
        }
        //this.ap = false;
    }

    boolean hasNoGalathOwner() {
        EntityPlayer player = this.getOwnerPlayerEntity();
        return player != null && this.world.getBlockState(player.getPosition().up().up()).getBlock() != Blocks.AIR;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (event.getController().getName()) {
            case "eyes":
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.galath.null", true, event);
                } else {
                    this.createAnimation("animation.galath.blink", true, event);
                }
                break;
            case "movement":
                this.movementController.setAnimationSpeed(1.0);
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.galath.null", true, event);
                }
                else if (this.isPlayerRiding) {
                    this.createAnimation("animation.galath.sit", true, event);
                }
                else if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.galath.controlled_flight", true, event);
                }
                else if (Math.abs(this.ao.x) + Math.abs(this.ao.y) == 0.0f) {
                    this.createAnimation(this.hasNoGalathOwner() ? "animation.galath.crouchidle" : "animation.galath.idle", true, event);
                }
                else if (this.isPlayerSprinting) {
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation(this.hasNoGalathOwner() ? "animation.galath.crouchwalk" : "animation.galath.run", true, event);
                }
                else if (this.ao.y >= -0.1f) {
                    this.movementController.setAnimationSpeed(2.0);
                    this.createAnimation(this.hasNoGalathOwner() ? "animation.galath.crouchwalk" : "animation.galath.walk", true, event);
                } else {
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation(this.hasNoGalathOwner() ? "animation.galath.crouchwalk" : "animation.galath.backwards_walk", true, event);
                }
                break;
            case "action":
                switch (this.getCurrentAction()) {
                    case NULL:
                        return PlayState.STOP;
                    case STRIP:
                        this.createAnimation("animation.galath.strip", true, event);
                        break ;
                    case ATTACK:
                        this.createAnimation("animation.galath.attack" + this.nextAttack, true, event);
                        break ;
                    case BOW:
                        this.createAnimation("animation.galath.bowcharge", true, event);
                        break ;
                    case RIDE:
                    case SIT:
                        this.createAnimation("animation.galath.sit", true, event);
                        break ;
                    case RAPE_INTRO: {
                        this.createAnimation("animation.galath.rape_intro", true, event);
                        break ;
                    }
                    case RAPE_ON_GOING: {
                        this.createAnimation("animation.galath.rape" + this.ar, true, event);
                        break ;
                    }
                    case RAPE_CUM: {
                        this.createAnimation("animation.galath.rape_cum", true, event);
                        break ;
                    }
                    case RAPE_CUM_IDLE: {
                        this.createAnimation("animation.galath.rape_cum_idle", true, event);
                        break ;
                    }
                    case CORRUPT_FAST: {
                        this.createAnimation("animation.galath.corrupt_" + (this.as ? "hard" : "soft"), true, event);
                        break ;
                    }
                    case CORRUPT_SLOW: {
                        this.createAnimation("animation.galath.corrupt_slow", true, event);
                        break ;
                    }
                    case CORRUPT_INTRO: {
                        this.createAnimation("animation.galath.corrupt_intro", true, event);
                        break ;
                    }
                    case CORRUPT_CUM: {
                        this.createAnimation("animation.galath.corrupt_cum", true, event);
                        break ;
                    }
                    case CONTROLLED_FLIGHT: {
                        this.createAnimation("animation.galath.controlled_flight", true, event);
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
                    if (++this.nextAttack == 3) {
                        this.nextAttack = 0;
                    }
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
                    Vec3d pos = this.getPositionVector();
                    Vec3d slipRPos = this.getCachedBoneOffset("slipR").add(pos);
                    Vec3d slipLPos = this.getCachedBoneOffset("slipL").add(pos);
                    Vec3d turnablePos = this.getCachedBoneOffset("turnable").add(pos);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, slipRPos.x, slipRPos.y, slipRPos.z, 0.0, 0.0, 0.0);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, slipLPos.x, slipLPos.y, slipLPos.z, 0.0, 0.0, 0.0);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, turnablePos.x, turnablePos.y, turnablePos.z, 0.0, 0.0, 0.0);
                    break;
                }
                case "rapeIntroDone": {
                    if (this.isControlledByLocalPlayer()) {
                        this.setCurrentAction(Action.RAPE_ON_GOING);
                    }
                    break;
                }
                case "rape_switch": {
                    Random random = this.getRNG();
                    int oldState = this.ar;

                    do {
                        this.ar = random.nextInt(3);
                    } while (this.ar == oldState);

                    break;
                }
                case "poundRape": {
                    this.playSoundAroundHer(SoundsHandler.MISC_POUNDING);
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.03f);
                    }
                    break;
                }
                case "enableRapeUI": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.setHornyMeterVisible(false);
                    }
                    break;
                }
                case "reloadRenderer": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft mc = Minecraft.getMinecraft();
                        if (mc.gameSettings.thirdPersonView != 0) {
                            mc.renderGlobal.loadRenderers();
                        }
                    }
                    break;
                }
                case "corruptSwitch": {
                    if (this.isControlledByLocalPlayer() && HandlePlayerMovement.isThrusting) {
                        this.setCurrentAction(Action.CORRUPT_FAST);
                    }
                    break;
                }
                case "corrupt_hard": {
                    if (this.isControlledByLocalPlayer() && HandlePlayerMovement.isThrusting) {
                        this.as = true;
                        this.resetAnimationControllerOffset();
                    }
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
                    CummyEntity.spawnSexParticles(this);
                }
                case "reset": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    break;
                }
                case "setCamCorrupt": {
                    if (this.isControlledByLocalPlayer()) {
                        this.aq = true;
                        EntityPlayerSP player = Minecraft.getMinecraft().player;
                        float yaw = this.getYawRotation() + 220.0f;
                        Vec3d pos = VectorMath.rotateByYaw(new Vec3d(0.5, 0.5f - player.getEyeHeight(), 0.4f), this.getYawRotation()).add(this.getTargetPosition());
                        PackageHandler.INSTANCE.sendToServer((IMessage) new TeleportPlayer(player.getPersistentID().toString(), pos, yaw, 15.0f));
                        SexUI.showUI();
                    }
                    break;
                }
                case "enableBoyCam": {
                    if (this.isControlledByLocalPlayer()) {
                        this.aq = false;
                    }
                    break;
                }
                case "creampie": {
                    CummyEntity.registerTrail(new DynamicTrailRenderer(130, girl -> {
                        Vec3d cockTipPos = girl.getBoneWorldPosition("futaCockTip");
                        Vec3d tipDir = girl.getBoneWorldPosition("futaCockTipDirHelp");
                        return cockTipPos.subtract(tipDir).normalize();
                    }, girl -> girl.getCachedBoneOffset("futaCockTip").add(girl.getTargetPosition()), this, 0.3f, 0.3f));
                    CummyEntity.registerTrail(new DynamicTrailRenderer(100, girl -> VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 0.6f), this.getYawRotation()),
                            girl -> girl.getCachedBoneOffset("creampiePos").add(girl.getTargetPosition()), this, 0.6f, 0.5f));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS), 3.0f);
                    break;
                }
                case "blackScreenTamed": 
                case "blackScreen": {
                    if (this.isControlledByLocalPlayer()) {
                        BlackScreenUI.run();
                    }
                    break;
                }
                case "flapControlled": {
                    if (this.isControlledByLocalPlayer()) {
                        GalathFlightUI.showUI();
                        this.playSoundAroundHer(SoundsHandler.MISC_FLAP);
                        Minecraft mc = Minecraft.getMinecraft();
                        EntityPlayerSP player = mc.player;
                        MovementInput input = player.movementInput;
                        Vec2f moveVec = input.getMoveVector();
                        if (moveVec.x != 0.0f || moveVec.y != 0.0f) {
                            Vec3d vel = VectorMath.rotate(new Vec3d(-moveVec.x, 0.0, moveVec.y), ReferenceAndRotationHelper.LerpFloat(player.prevRotationPitch, player.rotationPitch, mc.getRenderPartialTicks()), ReferenceAndRotationHelper.LerpFloat(player.prevRotationYawHead, player.rotationYawHead, mc.getRenderPartialTicks()));
                            PackageHandler.INSTANCE.sendToServer((IMessage) new UpdateVelocity(vel, this.girlID()));
                        }
                    }
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
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.showUI();
                    }
                    break;
                }
            }
        });
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.eyesController);
        data.addAnimationController(this.movementController);
    }
}

