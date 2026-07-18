/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Slime;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.Packages.SetPlayerForGirl;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.Reference;
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

public class PlayerSlime
extends PlayerGirl {
    boolean ap = false;
    int aq = 0;

    protected PlayerSlime(World world) {
        super(world);
    }

    public PlayerSlime(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 1.6f;
    }

    @Override
    public float getEyeHeight() {
        return 1.64f;
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
    public IRenderer getLimbRenderer(int n) {
        return new SlimeLimb();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/slime/hand.png";
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("action.names.blowjob".equals(string)) {
            this.a(0, Action.SUCKBLOWJOB);
            this.setCurrentAction(Action.SUCKBLOWJOB);
            this.void_b(uUID);
        }
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        PlayerSlime.a(entityPlayer, this, new String[]{"action.names.blowjob"}, false);
        return true;
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
    public void updateAITasks() {
        super.updateAITasks();
        if (this.currentAction() != Action.WAITDOGGY) {
            return;
        }
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_j();
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getPositionVector().distanceTo(this.net_minecraft_util_math_Vec3d_w()) > 1.0) {
            return;
        }
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        this.void_e(entityPlayer.getPersistentID());
        entityPlayer.rotationYaw = this.java_lang_Float_I().floatValue();
        this.r = this.java_lang_Float_I().floatValue();
        entityPlayer.setPosition(this.net_minecraft_util_math_Vec3d_w().x, this.net_minecraft_util_math_Vec3d_w().y, this.net_minecraft_util_math_Vec3d_w().z);
        entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
        this.moveCamera(0.0, 0.0, 0.4, 0.0f, 60.0f);
        this.setCurrentAction(Action.DOGGYSTART);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        EntityPlayer entityPlayer2 = this.world.getPlayerEntityByUUID(this.java_util_UUID_m());
        entityPlayer2.setNoGravity(true);
        entityPlayer.noClip = true;
        entityPlayer.capabilities.isFlying = true;
        entityPlayer2.capabilities.isFlying = true;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() == Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.slime.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.slime.fhappy", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.slime.null", true, animationEvent);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.slime.sit", true, animationEvent);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.ap = !this.ap;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.slime.fly" + (this.ap ? "2" : ""), true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.createAnimation("animation.slime.run", true, animationEvent);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.createAnimation("animation.slime.walk", true, animationEvent);
                        break;
                    }
                    this.createAnimation("animation.slime.backwards_walk", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.slime.idle", true, animationEvent);
                break;
            }
            case "action": {
                if (this.currentAction() == Action.NULL) {
                    this.createAnimation("animation.slime.null", true, animationEvent);
                    break;
                }
                switch (this.currentAction()) {
                    case UNDRESS: {
                        this.createAnimation("animation.slime.undress", false, animationEvent);
                        break block5;
                    }
                    case DRESS: {
                        this.createAnimation("animation.slime.dress", false, animationEvent);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.slime.strip", false, animationEvent);
                        break block5;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobsuck", true, animationEvent);
                        break block5;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobthrust", true, animationEvent);
                        break block5;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.slime.blowjobcum", false, animationEvent);
                        break block5;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.slime.doggygoonbed", false, animationEvent);
                        break block5;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.slime.doggywait", true, animationEvent);
                        break block5;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.slime.doggystart", false, animationEvent);
                        break block5;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.slime.doggyslow", true, animationEvent);
                        break block5;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.slime.doggyfast", true, animationEvent);
                        break block5;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.slime.doggycum", false, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.slime.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.slime.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.slime.ride", true, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.slime.sit", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        if (this.actionController == null) {
            this.void_p();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            String string;
            switch (string = soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "undress": {
                    if (!this.boolean_e()) break;
                    this.entityDataManager.set(D, 0);
                    this.void_r();
                    break;
                }
                case "dress": {
                    if (!this.boolean_e()) break;
                    this.entityDataManager.set(D, 1);
                    this.setCurrentAction((Action)null);
                    this.void_r();
                    break;
                }
                case "sexUiOn": {
                    if (!this.boolean_n()) break;
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
                case "doggyfastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "bjtReady": {
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
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundEvents.ENTITY_SLIME_SQUISH);
                    this.r = this.rotationYaw;
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
                    this.a(SoundEvents.ENTITY_SLIME_SQUISH, 0.25f);
                    break;
                }
                case "doggystartMSG4": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_SMALLINSERTS), 1.5f);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "doggystartMSG5": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.33f);
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
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.33f);
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
                    SexUI.addCumPercentage(0.00666);
                    break;
                }
                case "doggyfastMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    ++this.aq;
                    if (this.aq % 2 == 0) {
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
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundEvents.ENTITY_SLIME_DEATH);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.eyesController);
        animationData.addAnimationController(this.movementController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

