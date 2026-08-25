/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Allie;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SyncActionPacket;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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
    public float getScaleFactor() {
        return 1.9f + this.aq;
    }

    @Override
    public float getEyeHeight() {
        return 1.63f;
    }

    @Override
    public boolean canBeInteracted() {
        return false;
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new AllieLimb();
    }

    @Override
    public String getHandTexture(int index) {
        return "textures/entity/allie/hand.png";
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        if ("action.names.deepthroat".equals(command)) {
            this.setCurrentAction(Action.DEEPTHROAT_START);
            this.sendActionPacket(this.getOutfitIndex(), Action.DEEPTHROAT_START);
            this.teleportPlayerToGirl(partnerUUID);
        }
        if ("Reverse cowgirl".equals(command)) {
            this.setCurrentAction(Action.REVERSE_COWGIRL_START);
            this.sendActionPacket(0, Action.REVERSE_COWGIRL_START);
            this.teleportPlayerToGirl(partnerUUID);
        }
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        PlayerAllie.openInventoryGui(player, this, new String[]{"action.names.deepthroat", "Reverse cowgirl"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.DEEPTHROAT_CUM && (action == Action.DEEPTHROAT_FAST || action == Action.DEEPTHROAT_SLOW)) {
            return;
        }
        if (this.getCurrentAction() == Action.REVERSE_COWGIRL_CUM && (action == Action.REVERSE_COWGIRL_SLOW || action == Action.REVERSE_COWGIRL_FAST_START || action == Action.REVERSE_COWGIRL_FAST_CONTINUES)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public boolean FAllieBoolean() {
        switch (this.getCurrentAction()) {
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
            this.handleOwnerUUID(true);
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

    // TODO Rename
    @SideOnly(value=Side.CLIENT)
    void a_9() {
        if (this.ticksExisted % 10 != 0) {
            return;
        }
        int n = this.getRNG().nextInt(8);
        Vec3d vec3d = this.getCachedBoneOffset("tail" + n).add(this.getPositionVector());
        this.world.spawnParticle(EnumParticleTypes.PORTAL, vec3d.x, vec3d.y, vec3d.z, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, new int[0]);
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
    protected Action getNextAction(Action action) {
        if (action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_FAST;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW) {
            return Action.REVERSE_COWGIRL_FAST_START;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.DEEPTHROAT_FAST || action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_CUM;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW || action == Action.REVERSE_COWGIRL_FAST_START || action == Action.REVERSE_COWGIRL_FAST_CONTINUES) {
            return Action.REVERSE_COWGIRL_CUM;
        }
        return null;
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
                case "deepthroat_prepareMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "deepthroat_prepareMSG2": {
                    this.sendChatMessage(I18n.format("allie.dialogue.boys", new Object[0]));
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "blackscreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "deepthroat_prepareDone": {
                    this.setCurrentAction(Action.DEEPTHROAT_START);
                    if (!this.isControlledByLocalPlayer()) break;
                    PacketHandler.INSTANCE.sendToServer((IMessage)new SyncActionPacket(this.girlID(), this.getInteractionPlayerUUID(), false, true));
                    this.cameraYaw = this.rotationYaw + 180.0f;
                    this.moveCamera(0.0, 0.0, (double)1.35f, 0.0f, 30.0f);
                    SexUI.resetCumPercentage();
                    break;
                }
                case "deepthroat_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_BJMOAN));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "deepthroat_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_startDone": {
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_slowMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "deepthroat_cumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_CUMINFLATION), 1.5f);
                    break;
                }
                case "cowgirl_cumDone": 
                case "deepthroat_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    break;
                }
                case "deepthroat_normal_prepareMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.alright", new Object[0]));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_PLOB));
                    break;
                }
                case "giggle": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_GIGGLE);
                    break;
                }
                case "pounding": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "moan": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_MOAN);
                    break;
                }
                case "mmm": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MMM));
                    break;
                }
                case "slide": {
                    this.playRandomSound(SoundsHandler.MISC_SLIDE, 0, 1, 4, 6);
                    break;
                }
                case "slowMoan": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_AHH));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
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
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.ap) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MOAN));
                        this.ap = true;
                        break;
                    }
                    this.ap = false;
                    break;
                }
                case "fastSwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    Action fp_class3242 = this.getCurrentAction();
                    if (fp_class3242 == Action.REVERSE_COWGIRL_FAST_START) {
                        this.setCurrentAction(Action.REVERSE_COWGIRL_FAST_CONTINUES);
                        break;
                    }
                    this.resetAnimationControllerOffset();
                    int n = this.av;
                    do {
                        this.av = this.getRNG().nextInt(3) + 1;
                    } while (this.av == n);
                    break;
                }
                case "openSexUi": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "cum": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "aftermoan": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_AFTERSESSIONMOAN);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.allie.null", true, event);
                    break;
                }
                this.createAnimation("animation.bia.blink", true, event);
                break;
            }
            case "movement": {
                double d = 4.0 * (Math.abs(this.posX - this.lastTickPosX) + Math.abs(this.posY - this.lastTickPosY) + Math.abs(this.posZ - this.lastTickPosZ));
                d = Math.min(1.0 + d, 4.0);
                this.movementController.setAnimationSpeed(d);
                this.createAnimation("animation.allie.tail", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.allie.null", true, event);
                        break;
                    }
                    case SUMMON: {
                        this.createAnimation("animation.allie.summon", false, event);
                        break;
                    }
                    case SUMMON_NORMAL: {
                        this.createAnimation("animation.allie.summon_normal", false, event);
                        break;
                    }
                    case SUMMON_NORMAL_WAIT: {
                        this.createAnimation("animation.allie.summon_normal_wait", true, event);
                        break;
                    }
                    case SUMMON_WAIT: {
                        this.createAnimation("animation.allie.summon_wait", true, event);
                        break;
                    }
                    case ALLIE_PREPARE_FIRST_TIME: {
                        this.createAnimation("animation.allie.deepthroat_prepare", false, event);
                        break;
                    }
                    case ALLIE_PREPARE_NORMAL: {
                        this.createAnimation("animation.allie.deepthroat_normal_prepare", false, event);
                        break;
                    }
                    case DEEPTHROAT_START: {
                        this.createAnimation("animation.allie.deepthroat_start", false, event);
                        break;
                    }
                    case DEEPTHROAT_SLOW: {
                        this.createAnimation("animation.allie.deepthroat_slow", true, event);
                        break;
                    }
                    case DEEPTHROAT_FAST: {
                        this.createAnimation("animation.allie.deepthroat_fast", true, event);
                        break;
                    }
                    case DEEPTHROAT_CUM: {
                        this.createAnimation("animation.allie.deepthroat_cum", false, event);
                        break;
                    }
                    case RICH_FIRST_TIME: {
                        this.createAnimation("animation.allie.rich", false, event);
                        break;
                    }
                    case RICH_NORMAL: {
                        this.createAnimation("animation.allie.rich_normal", false, event);
                        break;
                    }
                    case SUMMON_SAND: {
                        this.createAnimation("animation.allie.summon_sand", false, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.allie.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.allie.bowcharge", false, event);
                        break;
                    }
                    case REVERSE_COWGIRL_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_start", true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_SLOW: {
                        this.createAnimation("animation.allie.reverse_cowgirl_slow" + this.ar, true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_FAST_CONTINUES: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fastc" + this.av, true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_FAST_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fasts", true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_CUM: {
                        this.createAnimation("animation.allie.reverse_cowgirl_cum", true, event);
                        break;
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }
}

