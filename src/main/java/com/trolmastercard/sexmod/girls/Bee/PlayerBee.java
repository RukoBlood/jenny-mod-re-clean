/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bee;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerBee extends PlayerGirl {
    protected PlayerBee(World world) {
        super(world);
    }

    public PlayerBee(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public void spawnHitboxHelper() {
        this.handleOwnerUUID(true);
    }

    @Override
    public void onTickClient() {
        this.handleOwnerUUID(false);
    }

    @Override
    public float getScaleFactor() {
        return 1.4f;
    }

    @Override
    public float getEyeHeight() {
        return 1.3f;
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new BeeHand();
    }

    @Override
    public String getHandTexture(int index) {
        return "textures/entity/bee/hand.png";
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        this.sendActionPacket(0, Action.CITIZEN_START);
        this.setOutfitIndex(0);
        this.setCurrentAction(Action.CITIZEN_START);
        this.teleportPlayerToGirl(partnerUUID);
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(partnerUUID);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = this.getFrontOffsetVector(-0.2);
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        PlayerBee.openInventoryGui(player, this, new String[]{"action.names.sex"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.CITIZEN_CUM && (action == Action.CITIZEN_FAST || action == Action.COWGIRLSLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean canBeInteracted() {
        return false;
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.CITIZEN_FAST || action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_CUM;
        }
        return null;
    }

    @Override
    public void reInitTasks() {
        super.reInitTasks();
        this.setOutfitIndex(1);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (event.getController().getName()) {
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.bee.null", true, event);
                    break;
                }
                this.createAnimation("animation.bee.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.bee.null", false, event);
                        break;
                    }
                    case CITIZEN_START: {
                        this.createAnimation("animation.bee.sex_start", false, event);
                        break;
                    }
                    case CITIZEN_SLOW: {
                        this.createAnimation("animation.bee.sex_slow", true, event);
                        break;
                    }
                    case CITIZEN_FAST: {
                        this.createAnimation("animation.bee.sex_fast", true, event);
                        break;
                    }
                    case CITIZEN_CUM: {
                        this.createAnimation("animation.bee.sex_cum", false, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bee.throw_pearl", true, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.bee.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.bee.bowcharge", false, event);
                        break;
                    }
                    case RIDE: {
                        this.createAnimation("animation.bee.ride", true, event);
                        break;
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
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "pearl": {
                    if (!this.isLocalPlayerNearby() || this.getCurrentAction() != Action.THROW_PEARL) break;
                    PacketHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "resetCumPercentage": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "sex_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "sex_startMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "sex_fastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "sex_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                }
                case "sex_startDone": {
                    this.setCurrentAction(Action.CITIZEN_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "sex_cumMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_CUMINFLATION), 2.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "blackscreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "sex_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
    }
}

