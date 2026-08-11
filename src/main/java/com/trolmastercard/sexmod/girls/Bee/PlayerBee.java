/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bee;

import java.util.UUID;

import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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
        this.c(true);
    }

    @Override
    public void void_y() {
        this.c(false);
    }

    @Override
    public float getNameTagHeightOffset() {
        return 1.4f;
    }

    @Override
    public float getEyeHeight() {
        return 1.3f;
    }

    @Override
    public IRenderer getHandRenderer(int n) {
        return new BeeLimb();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/bee/hand.png";
    }

    @Override
    public void onGuiActionSelected(String actionName, UUID partnerUUID) {
        this.initActionState(0, Action.CITIZEN_START);
        this.setOutfitIndex(0);
        this.setCurrentAction(Action.CITIZEN_START);
        this.bindPlayerPartner(partnerUUID);
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(partnerUUID);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = this.getFrontOffsetVector(-0.2);
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean openGuiForPlayer(EntityPlayer player) {
        PlayerBee.openInventoryGui(player, this, new String[]{"action.names.sex"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.currentAction() == Action.CITIZEN_CUM && (action == Action.CITIZEN_FAST || action == Action.COWGIRLSLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean shouldRenderArmor() {
        return false;
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_FAST;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.CITIZEN_FAST || action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_CUM;
        }
        return null;
    }

    @Override
    public void ResetNPCTasks() {
        super.ResetNPCTasks();
        this.setOutfitIndex(1);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        block4 : switch (event.getController().getName()) {
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.bee.null", true, event);
                    break;
                }
                this.createAnimation("animation.bee.idle", true, event);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.bee.null", false, event);
                        break block4;
                    }
                    case CITIZEN_START: {
                        this.createAnimation("animation.bee.sex_start", false, event);
                        break block4;
                    }
                    case CITIZEN_SLOW: {
                        this.createAnimation("animation.bee.sex_slow", true, event);
                        break block4;
                    }
                    case CITIZEN_FAST: {
                        this.createAnimation("animation.bee.sex_fast", true, event);
                        break block4;
                    }
                    case CITIZEN_CUM: {
                        this.createAnimation("animation.bee.sex_cum", false, event);
                        break block4;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bee.throw_pearl", true, event);
                        break block4;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.bee.attack" + this.nextAttack, false, event);
                        break block4;
                    }
                    case BOW: {
                        this.createAnimation("animation.bee.bowcharge", false, event);
                        break block4;
                    }
                    case RIDE: {
                        this.createAnimation("animation.bee.ride", true, event);
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
                    if (!this.getClosestPlayerID() || this.currentAction() != Action.THROW_PEARL) break;
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
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
                    SexUI.init();
                    break;
                }
                case "sex_cumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_CUMINFLATION), 2.0f);
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
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

