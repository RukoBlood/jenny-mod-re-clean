/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bia;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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

public class PlayerBia
extends PlayerGirl {
    int ar = -1;
    boolean ap = false;
    int aq = 1;

    public PlayerBia(World world) {
        super(world);
    }

    public PlayerBia(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float getScaleFactor() {
        return 1.5f;
    }

    @Override
    public float getEyeHeight() {
        return 1.5f;
    }

    @Override
    public void u_() {
    }

    @Override
    public boolean boolean_a(String string) {
        if ("anal".equals(string)) {
            this.setCurrentAction(Action.ANAL_PREPARE);
            this.setOutfitIndex(0);
            return true;
        }
        if ("doggy".equals(string)) {
            this.setCurrentAction(Action.SITDOWN);
            this.setOutfitIndex(0);
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void beeOpenGUI() {
        GirlEntity.openInventoryGui(Minecraft.getMinecraft().player, this, new String[]{"anal", "doggy"}, false);
    }

    @Override
    public void onGuiActionSelected(String actionName, UUID partnerUUID) {
        if ("action.names.headpat".equals(actionName)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.HEAD_PAT);
            this.sendActionPacket(this.getOutfitIndex(), Action.HEAD_PAT);
        }
    }

    @Override
    public IRenderer getHandRenderer(int n) {
        return new BiaHand();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/bia/hand.png";
    }

    @Override
    public float getLeftArmRotation() {
        return 35.0f;
    }

    @Override
    public float getRightArmRotation() {
        return 140.0f;
    }

    @Override
    public boolean useVanillaItemHolding() {
        return false;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        GirlEntity.openInventoryGui(player, this, new String[]{"action.names.headpat"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.ANAL_CUM && (action == Action.ANAL_FAST || action == Action.ANAL_SLOW)) {
            return;
        }
        if (this.getCurrentAction() == Action.PRONE_DOGGY_CUM && (action == Action.PRONE_DOGGY_HARD || action == Action.PRONE_DOGGY_SOFT)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.ANAL_SLOW) {
            return Action.ANAL_FAST;
        }
        if (action == Action.PRONE_DOGGY_INTRO) {
            return Action.PRONE_DOGGY_INSERT;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.ANAL_SLOW || action == Action.ANAL_FAST) {
            return Action.ANAL_CUM;
        }
        if (action == Action.PRONE_DOGGY_SOFT || action == Action.PRONE_DOGGY_HARD) {
            return Action.PRONE_DOGGY_CUM;
        }
        return null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.a_();
    }

    @Override
    protected void resetLocalPlayerClientState() {
        super.resetLocalPlayerClientState();
        this.ar = -1;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean boolean_a(EntityPlayer entityPlayer) {
        return Minecraft.getMinecraft().player.getPersistentID().equals(entityPlayer.getPersistentID());
    }

    void a_() {
        float f;
        Action fp_class3242 = this.getCurrentAction();
        if (fp_class3242 != Action.ANAL_WAIT && fp_class3242 != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = this.getPlayerPartner();
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getDistance(this) > 1.0f) {
            return;
        }
        if (this.world.isRemote && !this.boolean_a(entityPlayer)) {
            return;
        }
        if (this.ar == -1) {
            if (this.world.isRemote) {
                BlackScreenUI.run();
                HandlePlayerMovement.setMovementLock(false);
            } else {
                this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            }
            this.ar = GirlEntity.maxAgeInTicks;
            return;
        }
        if (--this.ar > 0) {
            return;
        }
        this.ar = -1;
        entityPlayer.noClip = true;
        entityPlayer.setNoGravity(true);
        if (fp_class3242 == Action.ANAL_WAIT) {
            if (!this.world.isRemote) {
                this.setCurrentAction(Action.ANAL_START);
                Vec3d vec3d = this.getTargetPosition().add(VectorMath.rotateByYaw(-0.3, -1.0, -0.5, this.getYawRotation().floatValue()));
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            } else if (this.isControlledByLocalPlayer()) {
                SexUI.showUI();
            }
            return;
        }
        entityPlayer.rotationYaw = f = this.getYawRotation().floatValue();
        entityPlayer.rotationPitch = 60.0f;
        if (!this.world.isRemote) {
            this.setOutfitIndex(0);
            this.setCurrentAction(Action.PRONE_DOGGY_INTRO);
            Vec3d vec3d = this.getTargetPosition();
            Vec3d vec3d2 = vec3d.add(VectorMath.rotateByYaw(0.0, 0.0, 1.0, f));
            this.setTargetPosition(vec3d2);
            EntityPlayer entityPlayer2 = this.getOwnerPlayerEntity();
            if (entityPlayer2 != null) {
                entityPlayer2.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
            }
            Vec3d vec3d3 = vec3d.add(VectorMath.rotateByYaw(0.0, 1.1875 - (double)entityPlayer.getEyeHeight(), 0.5, f));
            entityPlayer.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            this.setAnchored(true);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void resetAnimationControllerTicks() {
        super.resetAnimationControllerTicks();
        if (this.getCurrentAction() != Action.PRONE_DOGGY_HARD) {
            return;
        }
        int n = this.aq;
        do {
            this.aq = this.getRNG().nextInt(3) + 1;
        } while (n == this.aq);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.bia.null", true, event);
                    break;
                }
                this.createAnimation("animation.bia.fhappy", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.bia.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.bia.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.ap = !this.ap;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.bia.fly" + (this.ap ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.2);
                        this.createAnimation("animation.bia.run", true, event);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(1.2);
                        this.createAnimation("animation.bia.fastwalk", true, event);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.2);
                    this.createAnimation("animation.bia.backwards_walk", true, event);
                    break;
                }
                this.createAnimation("animation.bia.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.bia.null", true, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.bia.strip", false, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.bia.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.bia.bowcharge", false, event);
                        break;
                    }
                    case RIDE: {
                        this.createAnimation("animation.bia.ride", true, event);
                        break;
                    }
                    case SIT: {
                        this.createAnimation("animation.bia.sit", true, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bia.throwpearl", false, event);
                        break;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.bia.downed", true, event);
                        break;
                    }
                    case TALK_HORNY: {
                        this.createAnimation("animation.bia.talk_horny", false, event);
                        break;
                    }
                    case TALK_IDLE: {
                        this.createAnimation("animation.bia.talk_idle", true, event);
                        break;
                    }
                    case TALK_RESPONSE: {
                        this.createAnimation("animation.bia.talk_response", true, event);
                        break;
                    }
                    case ANAL_PREPARE: {
                        this.createAnimation("animation.bia.anal_prepare", false, event);
                        break;
                    }
                    case ANAL_WAIT: {
                        this.createAnimation("animation.bia.anal_wait", true, event);
                        break;
                    }
                    case ANAL_START: {
                        this.createAnimation("animation.bia.anal_start", true, event);
                        break;
                    }
                    case ANAL_SLOW: {
                        this.createAnimation("animation.bia.anal_slow", true, event);
                        break;
                    }
                    case ANAL_FAST: {
                        this.createAnimation("animation.bia.anal_fast", true, event);
                        break;
                    }
                    case ANAL_CUM: {
                        this.createAnimation("animation.bia.anal_cum", false, event);
                        break;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.bia.headpat", false, event);
                        break;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.bia.sitdown", false, event);
                        break;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.bia.sitdownidle", true, event);
                        break;
                    }
                    case PRONE_DOGGY_INTRO: {
                        this.createAnimation("animation.bia.prone_doggy_intro", true, event);
                        break;
                    }
                    case PRONE_DOGGY_INSERT: {
                        this.createAnimation("animation.bia.prone_doggy_insert", true, event);
                        break;
                    }
                    case PRONE_DOGGY_SOFT: {
                        this.createAnimation("animation.bia.prone_doggy_soft", true, event);
                        break;
                    }
                    case PRONE_DOGGY_HARD: {
                        this.createAnimation("animation.bia.prone_doggy_hard" + this.aq, true, event);
                        break;
                    }
                    case PRONE_DOGGY_CUM: {
                        this.createAnimation("animation.bia.prone_doggy_cum", true, event);
                        break;
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
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
                case "stripMSG1": {
                    this.sendGirlChatMessage("Hihi~");
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_BIA_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "pearl": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "talk_hornyMSG1": {
                    this.sendLocalClientMessage("Heyaaa~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HEY[3]);
                    break;
                }
                case "talk_hornyMSG2": {
                    this.sendLocalClientMessage("I am Hornyyyyy~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[2]);
                    break;
                }
                case "talk_hornyMSG3": {
                    this.sendLocalClientMessage("So...");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "talk_hornyMSG4": {
                    this.sendLocalClientMessage("Are we gonna have some fun nyaa?");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "talk_responseMSG1": {
                    this.sendLocalClientMessage("Huh?!...");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[2]);
                    break;
                }
                case "talk_responseMSG2": {
                    this.sendLocalClientMessage("I... uhm...");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[1]);
                    break;
                }
                case "talk_responseMSG3": {
                    this.sendLocalClientMessage("yes~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[0]);
                    break;
                }
                case "talk_responseDone": {
                    this.resetGirlState();
                    if (this.entityDataManager.get(GirlEntity.OUTFIT_INDEX) != 0) {
                        this.setCurrentAction(Action.STRIP);
                        break;
                    }
                    this.U();
                    break;
                }
                case "anal_prepareMSG1": {
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "anal_prepareMSG2": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    break;
                }
                case "anal_prepareDone": {
                    this.setCurrentAction(Action.ANAL_WAIT);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "anal_startMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[3]);
                    this.PlaySound(SoundsHandler.MISC_POUNDING[34]);
                    break;
                }
                case "anal_fastMSG1": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.5f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_slowMSG1": 
                case "anal_startMSG2": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.5f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                }
                case "anal_startDone": {
                    this.setCurrentAction(Action.ANAL_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "anal_cumMSG2": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_cumBlackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "doggy_cumDone": 
                case "anal_cumDone": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.resetCumPercentage();
                    }
                    this.resetCameraAndPhysics();
                    break;
                }
                case "headpatMSG1": {
                    this.sendLocalClientMessage("Ooh headpats!");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "headpatMSG2": {
                    this.sendLocalClientMessage("Hmmm.... :D");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[0]);
                    break;
                }
                case "headpatMSG3": {
                    this.sendLocalClientMessage("huh...?");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "headpatMSG4": {
                    this.sendLocalClientMessage("Tanku hehe");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[1]);
                    break;
                }
                case "headpatDone": {
                    if (!this.getClosestPlayerID()) break;
                    this.resetCameraAndPhysics();
                    break;
                }
                case "sitdownMSG1": {
                    this.sendLocalClientMessage("come here big boy~");
                    this.playSoundAroundHer(SoundsHandler.GIRLS_BIA_BREATH);
                    break;
                }
                case "sitdownDone": {
                    this.setCurrentAction(Action.SITDOWNIDLE);
                    break;
                }
                case "slide": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SLIDE));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.005);
                    break;
                }
                case "pound": {
                    this.playSoundAroundHer(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "doggyMoan": {
                    this.playSoundAroundHer(this.getRNG().nextBoolean() ? SoundsHandler.GIRLS_BIA_AHH : SoundsHandler.GIRLS_BIA_MMM);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "doggySwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PRONE_DOGGY_HARD);
                    break;
                }
                case "doggyReset": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "orgasm1": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[6]);
                    break;
                }
                case "orgasm2": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[7]);
                    break;
                }
                case "openSexUI": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
        data.addAnimationController(this.actionController);
    }
}

