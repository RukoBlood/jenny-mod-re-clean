/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Slime;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.Packets.SetPlayerForGirl;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerSlime extends PlayerGirl {
    boolean flySwitch = false;
    int thrustSoundCounter = 0;

    protected PlayerSlime(World world) {
        super(world);
    }

    public PlayerSlime(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float getScaleFactor() {
        return 1.6f;
    }

    @Override
    public float getEyeHeight() {
        return 1.64f;
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
    public IRenderer getHandRenderer(int limbIndex) {
        return new SlimeHand();
    }

    @Override
    public String HandTexture(int handIndex) {
        return "textures/entity/slime/hand.png";
    }

    @Override
    public void onGuiActionSelected(String actionName, UUID partnerUUID) {
        if ("action.names.blowjob".equals(actionName)) {
            this.initActionState(0, Action.SUCKBLOWJOB);
            this.setCurrentAction(Action.SUCKBLOWJOB);
            this.bindPlayerPartner(partnerUUID);
        }
    }

    @Override
    public boolean openGuiForPlayer(EntityPlayer player) {
        PlayerSlime.openInventoryGui(player, this, new String[]{"action.names.blowjob"}, false);
        return true;
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
    public void updateAITasks() {
        super.updateAITasks();
        if (this.getCurrentAction() != Action.WAITDOGGY) {
            return;
        }
        EntityPlayer partner = this.getPlayerPartner();
        if (partner == null) {
            return;
        }
        if (partner.getPositionVector().distanceTo(this.getTargetScenePosition()) > 1.0) {
            return;
        }
        PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)partner);
        this.setInteractionPlayerUUID(partner.getPersistentID());
        partner.rotationYaw = this.getYawRotation();
        this.cameraYaw = this.getYawRotation();
        partner.setPosition(this.getTargetScenePosition().x, this.getTargetScenePosition().y, this.getTargetScenePosition().z);
        partner.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
        this.moveCamera(0.0, 0.0, 0.4, 0.0f, 60.0f);
        this.setCurrentAction(Action.DOGGYSTART);
        partner.setNoGravity(true);
        partner.noClip = true;
        EntityPlayer entityPlayer2 = this.world.getPlayerEntityByUUID(this.getOwnerUserUUID());
        assert entityPlayer2 != null;
        entityPlayer2.setNoGravity(true);
        partner.noClip = true;
        partner.capabilities.isFlying = true;
        entityPlayer2.capabilities.isFlying = true;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        block5 : switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() == Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.slime.null", true, event);
                    break;
                }
                this.createAnimation("animation.slime.fhappy", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.slime.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.slime.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.flySwitch = !this.flySwitch;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.slime.fly" + (this.flySwitch ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.createAnimation("animation.slime.run", true, event);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.createAnimation("animation.slime.walk", true, event);
                        break;
                    }
                    this.createAnimation("animation.slime.backwards_walk", true, event);
                    break;
                }
                this.createAnimation("animation.slime.idle", true, event);
                break;
            }
            case "action": {
                if (this.getCurrentAction() == Action.NULL) {
                    this.createAnimation("animation.slime.null", true, event);
                    break;
                }
                switch (this.getCurrentAction()) {
                    case UNDRESS: {
                        this.createAnimation("animation.slime.undress", false, event);
                        break block5;
                    }
                    case DRESS: {
                        this.createAnimation("animation.slime.dress", false, event);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.slime.strip", false, event);
                        break block5;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobsuck", true, event);
                        break block5;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobthrust", true, event);
                        break block5;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobcum", false, event);
                        break block5;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.slime.doggygoonbed", false, event);
                        break block5;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.slime.doggywait", true, event);
                        break block5;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.slime.doggystart", false, event);
                        break block5;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.slime.doggyslow", true, event);
                        break block5;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.slime.doggyfast", true, event);
                        break block5;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.slime.doggycum", false, event);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.slime.attack" + this.nextAttack, false, event);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.slime.bowcharge", false, event);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.slime.ride", true, event);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.slime.sit", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener soundListener = soundKeyframeEvent -> {
            String string;
            switch (string = soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "undress": {
                    if (!this.getClosestPlayerID()) break;
                    this.entityDataManager.set(OUTFIT_INDEX, 0);
                    this.resetCameraAndPhysics();
                    break;
                }
                case "dress": {
                    if (!this.getClosestPlayerID()) break;
                    this.entityDataManager.set(OUTFIT_INDEX, 1);
                    this.setCurrentAction((Action)null);
                    this.resetCameraAndPhysics();
                    break;
                }
                case "sexUiOn": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "bjiMSG10": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.moveCamera(-0.4, -0.8, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH, 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (ReferenceAndRotationHelper.RANDOM.nextInt(5) == 0) {
                        this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP, 0.5f);
                    }
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH, 0.5f);
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
                case "doggyfastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "bjtReady": {
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
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                    this.cameraYaw = this.rotationYaw;
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
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH, 0.25f);
                    break;
                }
                case "doggystartMSG4": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS), 1.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "doggystartMSG5": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
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
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    int n = ReferenceAndRotationHelper.RANDOM.nextInt(4);
                    if (n == 0) {
                        n = ReferenceAndRotationHelper.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundEvents.ENTITY_SLIME_JUMP);
                        } else {
                            this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                        }
                    } else {
                        this.PlaySound(SoundEvents.BLOCK_SLIME_HIT);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.00666);
                    break;
                }
                case "doggyfastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    ++this.thrustSoundCounter;
                    if (this.thrustSoundCounter % 2 == 0) {
                        int n = ReferenceAndRotationHelper.RANDOM.nextInt(2);
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
                    this.PlaySound(SoundsHandler.MISC_CUMINFLATION[0], 4.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundEvents.ENTITY_SLIME_DEATH);
                }
            }
        };
        this.actionController.registerSoundListener(soundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.eyesController);
        data.addAnimationController(this.movementController);
    }
}

