/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Allie;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.dc_class174;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
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

public class PlayerAllie
extends PlayerGirl {
    final static double au = 4.0;
    final static double at = 4.0;
    public float aq = 0.0f;
    EntityPlayer as = null;
    boolean ap = false;
    int ar = 1;
    int av = 1;

    protected PlayerAllie(World world) {
        super(world);
    }

    public PlayerAllie(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 1.9f + this.aq;
    }

    @Override
    public float getEyeHeight() {
        return 1.63f;
    }

    @Override
    public boolean boolean_v() {
        return false;
    }

    @Override
    public IRenderer getLimbRenderer(int n) {
        return new AllieLimb();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/allie/hand.png";
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("action.names.deepthroat".equals(string)) {
            this.setCurrentAction(Action.DEEPTHROAT_START);
            this.a(this.int_ah(), Action.DEEPTHROAT_START);
            this.void_b(uUID);
        }
        if ("Reverse cowgirl".equals(string)) {
            this.setCurrentAction(Action.REVERSE_COWGIRL_START);
            this.a(0, Action.REVERSE_COWGIRL_START);
            this.void_b(uUID);
        }
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        PlayerAllie.a(entityPlayer, this, new String[]{"action.names.deepthroat", "Reverse cowgirl"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.currentAction() == Action.DEEPTHROAT_CUM && (action == Action.DEEPTHROAT_FAST || action == Action.DEEPTHROAT_SLOW)) {
            return;
        }
        if (this.currentAction() == Action.REVERSE_COWGIRL_CUM && (action == Action.REVERSE_COWGIRL_SLOW || action == Action.REVERSE_COWGIRL_FAST_START || action == Action.REVERSE_COWGIRL_FAST_CONTINUES)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public boolean boolean_F() {
        switch (this.currentAction()) {
            case ALLIE_PREPARE_NORMAL:
            case DEEPTHROAT_START:
            case DEEPTHROAT_CUM:
            case DEEPTHROAT_FAST:
            case ALLIE_PREPARE_FIRST_TIME:
            case DEEPTHROAT_SLOW: {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.getOwnerUserUUID() == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getOwnerUserUUID());
        if (entityPlayer != null && this.as == null) {
            this.c(true);
        }
        this.as = entityPlayer;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            this.a_9();
        }
    }

    // TODO clash with 'AnimatedGeo a()'
    @SideOnly(value=Side.CLIENT)
    void a_9() {
        if (this.ticksExisted % 10 != 0) {
            return;
        }
        int n = this.getRNG().nextInt(8);
        Vec3d vec3d = this.b("tail" + n).add(this.getPositionVector());
        this.world.spawnParticle(EnumParticleTypes.PORTAL, vec3d.x, vec3d.y, vec3d.z, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, new int[0]);
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
    protected Action FastSexAction(Action action) {
        if (action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_FAST;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW) {
            return Action.REVERSE_COWGIRL_FAST_START;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.DEEPTHROAT_FAST || action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_CUM;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW || action == Action.REVERSE_COWGIRL_FAST_START || action == Action.REVERSE_COWGIRL_FAST_CONTINUES) {
            return Action.REVERSE_COWGIRL_CUM;
        }
        return null;
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
                case "deepthroat_prepareMSG1": {
                    this.void_a(I18n.format("allie.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "deepthroat_prepareMSG2": {
                    this.void_a(I18n.format("allie.dialogue.boys", new Object[0]));
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "blackscreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "deepthroat_prepareDone": {
                    this.setCurrentAction(Action.DEEPTHROAT_START);
                    if (!this.boolean_n()) break;
                    PackageHandler.networkWrapper.sendToServer((IMessage)new dc_class174(this.girlID(), this.getID(), false, true));
                    this.cameraYaw = this.rotationYaw + 180.0f;
                    this.moveCamera(0.0, 0.0, (double)1.35f, 0.0f, 30.0f);
                    SexUI.resetCumPercentage();
                    break;
                }
                case "deepthroat_fastMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_BJMOAN));
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "deepthroat_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_startDone": {
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_slowMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "deepthroat_cumMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    this.a(SoundsHandler.a(SoundsHandler.MISC_CUMINFLATION), 1.5f);
                    break;
                }
                case "cowgirl_cumDone": 
                case "deepthroat_cumDone": {
                    if (!this.boolean_n()) break;
                    this.void_r();
                    break;
                }
                case "deepthroat_normal_prepareMSG1": {
                    this.void_a(I18n.format("allie.dialogue.alright", new Object[0]));
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_PLOB));
                    break;
                }
                case "giggle": {
                    this.a(SoundsHandler.GIRLS_ALLIE_GIGGLE);
                    break;
                }
                case "pounding": {
                    this.a(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "moan": {
                    this.a(SoundsHandler.GIRLS_ALLIE_MOAN);
                    break;
                }
                case "mmm": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_MMM));
                    break;
                }
                case "slide": {
                    this.PlaySound(SoundsHandler.MISC_SLIDE, 0, 1, 4, 6);
                    break;
                }
                case "slowMoan": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_AHH));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "cowgirlSlowDone": {
                    int n = this.ar;
                    do {
                        this.ar = this.getRNG().nextInt(3) + 1;
                    } while (this.ar == n);
                    break;
                }
                case "fastMoan": {
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.ap) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_MOAN));
                        this.ap = true;
                        break;
                    }
                    this.ap = false;
                    break;
                }
                case "fastSwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    Action fp_class3242 = this.currentAction();
                    if (fp_class3242 == Action.REVERSE_COWGIRL_FAST_START) {
                        this.setCurrentAction(Action.REVERSE_COWGIRL_FAST_CONTINUES);
                        break;
                    }
                    this.N();
                    int n = this.av;
                    do {
                        this.av = this.getRNG().nextInt(3) + 1;
                    } while (this.av == n);
                    break;
                }
                case "openSexUi": {
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "aftermoan": {
                    this.a(SoundsHandler.GIRLS_ALLIE_AFTERSESSIONMOAN);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.allie.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.bia.blink", true, animationEvent);
                break;
            }
            case "movement": {
                double d = 4.0 * (Math.abs(this.posX - this.lastTickPosX) + Math.abs(this.posY - this.lastTickPosY) + Math.abs(this.posZ - this.lastTickPosZ));
                d = Math.min(1.0 + d, 4.0);
                this.movementController.setAnimationSpeed(d);
                this.createAnimation("animation.allie.tail", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.allie.null", true, animationEvent);
                        break block5;
                    }
                    case SUMMON: {
                        this.createAnimation("animation.allie.summon", false, animationEvent);
                        break block5;
                    }
                    case SUMMON_NORMAL: {
                        this.createAnimation("animation.allie.summon_normal", false, animationEvent);
                        break block5;
                    }
                    case SUMMON_NORMAL_WAIT: {
                        this.createAnimation("animation.allie.summon_normal_wait", true, animationEvent);
                        break block5;
                    }
                    case SUMMON_WAIT: {
                        this.createAnimation("animation.allie.summon_wait", true, animationEvent);
                        break block5;
                    }
                    case ALLIE_PREPARE_FIRST_TIME: {
                        this.createAnimation("animation.allie.deepthroat_prepare", false, animationEvent);
                        break block5;
                    }
                    case ALLIE_PREPARE_NORMAL: {
                        this.createAnimation("animation.allie.deepthroat_normal_prepare", false, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_START: {
                        this.createAnimation("animation.allie.deepthroat_start", false, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_SLOW: {
                        this.createAnimation("animation.allie.deepthroat_slow", true, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_FAST: {
                        this.createAnimation("animation.allie.deepthroat_fast", true, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_CUM: {
                        this.createAnimation("animation.allie.deepthroat_cum", false, animationEvent);
                        break block5;
                    }
                    case RICH_FIRST_TIME: {
                        this.createAnimation("animation.allie.rich", false, animationEvent);
                        break block5;
                    }
                    case RICH_NORMAL: {
                        this.createAnimation("animation.allie.rich_normal", false, animationEvent);
                        break block5;
                    }
                    case SUMMON_SAND: {
                        this.createAnimation("animation.allie.summon_sand", false, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.allie.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.allie.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_start", true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_SLOW: {
                        this.createAnimation("animation.allie.reverse_cowgirl_slow" + this.ar, true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_FAST_CONTINUES: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fastc" + this.av, true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_FAST_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fasts", true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_CUM: {
                        this.createAnimation("animation.allie.reverse_cowgirl_cum", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

