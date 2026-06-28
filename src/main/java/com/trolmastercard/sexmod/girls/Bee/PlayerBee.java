/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bee;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
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

public class PlayerBee
extends PlayerGirl {
    protected PlayerBee(World world) {
        super(world);
    }

    public PlayerBee(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public void void_B() {
        this.c(true);
    }

    @Override
    public void void_y() {
        this.c(false);
    }

    @Override
    public float float_i() {
        return 1.4f;
    }

    @Override
    public float getEyeHeight() {
        return 1.3f;
    }

    @Override
    public IRenderer com_trolmastercard_sexmod_at_class43_a(int n) {
        return new a7_class15();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/bee/hand.png";
    }

    @Override
    public void b(String string, UUID uUID) {
        this.a(0, Action.CITIZEN_START);
        this.f(0);
        this.setCurrentAction(Action.CITIZEN_START);
        this.void_b(uUID);
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = this.a(-0.2);
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        PlayerBee.a(entityPlayer, this, new String[]{"action.names.sex"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        if (this.currentAction() == Action.CITIZEN_CUM && (fp_class3242 == Action.CITIZEN_FAST || fp_class3242 == Action.COWGIRLSLOW)) {
            return;
        }
        super.setCurrentAction(fp_class3242);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean boolean_v() {
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
    public void void_g() {
        super.void_g();
        this.f(1);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        block4 : switch (animationEvent.getController().getName()) {
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.bee.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.bee.idle", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.bee.null", false, animationEvent);
                        break block4;
                    }
                    case CITIZEN_START: {
                        this.createAnimation("animation.bee.sex_start", false, animationEvent);
                        break block4;
                    }
                    case CITIZEN_SLOW: {
                        this.createAnimation("animation.bee.sex_slow", true, animationEvent);
                        break block4;
                    }
                    case CITIZEN_FAST: {
                        this.createAnimation("animation.bee.sex_fast", true, animationEvent);
                        break block4;
                    }
                    case CITIZEN_CUM: {
                        this.createAnimation("animation.bee.sex_cum", false, animationEvent);
                        break block4;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bee.throw_pearl", true, animationEvent);
                        break block4;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.bee.attack" + this.S, false, animationEvent);
                        break block4;
                    }
                    case BOW: {
                        this.createAnimation("animation.bee.bowcharge", false, animationEvent);
                        break block4;
                    }
                    case RIDE: {
                        this.createAnimation("animation.bee.ride", true, animationEvent);
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
            switch (soundKeyframeEvent.sound) {
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "pearl": {
                    if (!this.boolean_e() || this.currentAction() != Action.THROW_PEARL) break;
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "resetCumPercentage": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "sex_fastMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "sex_startMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "sex_fastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "sex_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                }
                case "sex_startDone": {
                    this.setCurrentAction(Action.CITIZEN_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "sex_cumMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_CUMINFLATION), 2.0f);
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "blackscreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "sex_cumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

