/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Luna;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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

public class PlayerLuna
extends PlayerGirl {
    int ar = 0;
    boolean aq = false;
    boolean ap = false;
    boolean as = false;

    protected PlayerLuna(World world) {
        super(world);
    }

    public PlayerLuna(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float getScaleFactor() {
        return 1.6f;
    }

    @Override
    public float getEyeHeight() {
        return 1.34f;
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new LunaHand();
    }

    @Override
    public String getHandTexture(int index) {
        return "textures/entity/cat/hand.png";
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        if ("action.names.touchboobs".equals(command)) {
            this.sendActionPacket(0, Action.TOUCH_BOOBS_INTRO);
            this.setCurrentAction(Action.TOUCH_BOOBS_INTRO);
            this.entityDataManager.set(OUTFIT_INDEX, 0);
            this.teleportPlayerToGirl(partnerUUID);
        }
        if ("action.names.headpat".equals(command)) {
            this.setCurrentAction(Action.HEAD_PAT);
            this.teleportPlayerToGirl(partnerUUID);
        }
    }

    @Override
    public void handleInteraction() {
        this.setCurrentAction(Action.WAIT_CAT);
    }

    @Override
    public boolean canBeInteracted() {
        return true;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        PlayerLuna.openInventoryGui(player, this, new String[]{"action.names.touchboobs", "action.names.headpat"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.COWGIRL_SITTING_CUM && (action == Action.COWGIRL_SITTING_SLOW || action == Action.COWGIRL_SITTING_FAST)) {
            return;
        }
        if (this.getCurrentAction() == Action.TOUCH_BOOBS_CUM && (action == Action.TOUCH_BOOBS_FAST || action == Action.TOUCH_BOOBS_SLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (Action.WAIT_CAT.equals((Object)this.getCurrentAction())) {
            this.a_();
        } else {
            this.ar = 0;
        }
    }

    void a_() {
        EntityPlayer entityPlayer = this.getPlayerPartner();
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getDistance(this.posX, this.getTargetScenePosition().y, this.posZ) > 1.25) {
            return;
        }
        if (this.world.isRemote) {
            this.a(entityPlayer, this.ar);
        } else if (this.ar == 25) {
            this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayer.setPositionAndUpdate(this.getPositionVector().x, this.getTargetScenePosition().y, this.getPositionVector().z);
            this.setCurrentAction(Action.COWGIRL_SITTING_INTRO);
            entityPlayer.setRotationYawHead(this.getYawRotation().floatValue() + 180.0f);
            entityPlayer.rotationYaw = this.getYawRotation().floatValue() + 180.0f;
            entityPlayer.prevRotationYaw = this.getYawRotation().floatValue() + 180.0f;
            this.cameraYaw = this.getYawRotation().floatValue() + 180.0f;
            this.moveCamera(0.0, -0.075f, -0.7109375, 0.0f, 0.0f);
            this.entityDataManager.set(OUTFIT_INDEX, 0);
        }
        ++this.ar;
    }

    @SideOnly(value=Side.CLIENT)
    void a(EntityPlayer entityPlayer, int n) {
        EntityPlayerSP entityPlayerSP;
        if (n == 0 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            BlackScreenUI.run();
            entityPlayerSP.setVelocity(0.0, 0.0, 0.0);
            HandlePlayerMovement.setMovementLock(false);
        }
        if (n == 25 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        }
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW) {
            return Action.TOUCH_BOOBS_FAST;
        }
        if (action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW || action == Action.TOUCH_BOOBS_FAST) {
            return Action.TOUCH_BOOBS_CUM;
        }
        if (action == Action.COWGIRL_SITTING_FAST || action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_CUM;
        }
        return null;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.cat.null", true, event);
                    break;
                }
                this.createAnimation("animation.cat.blink", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.cat.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.cat.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.aq = !this.aq;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.cat.fly" + (this.aq ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.5);
                        this.createAnimation("animation.cat.run", true, event);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation("animation.cat.fastwalk", true, event);
                        break;
                    }
                    this.movementController.setAnimationSpeed(2.0);
                    this.createAnimation("animation.cat.backwards_walk", true, event);
                    break;
                }
                this.createAnimation("animation.cat.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.cat.null", true, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.cat.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case RIDE: 
                    case SIT: {
                        this.createAnimation("animation.cat.sit", true, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.cat.bowcharge", false, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.cat.throwpearl", true, event);
                        break;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.cat.downed", true, event);
                        break;
                    }
                    case FISHING_START: {
                        this.createAnimation("animation.cat.start_fishing", false, event);
                        break;
                    }
                    case FISHING_IDLE: {
                        this.createAnimation("animation.cat.idle_fishing", true, event);
                        break;
                    }
                    case FISHING_EAT: {
                        this.createAnimation("animation.cat.eat_fishing", false, event);
                        break;
                    }
                    case FISHING_THROW_AWAY: {
                        this.createAnimation("animation.cat.throw_away", false, event);
                        break;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.cat.payment", false, event);
                        break;
                    }
                    case TOUCH_BOOBS_INTRO: {
                        this.createAnimation("animation.cat.touch_boobs_intro", false, event);
                        break;
                    }
                    case TOUCH_BOOBS_SLOW: {
                        this.createAnimation("animation.cat.touch_boobs_slow" + (this.ap ? "1" : ""), true, event);
                        break;
                    }
                    case TOUCH_BOOBS_FAST: {
                        this.createAnimation("animation.cat.touch_boobs_fast", true, event);
                        break;
                    }
                    case TOUCH_BOOBS_CUM: {
                        this.createAnimation("animation.cat.touch_boobs_cum", false, event);
                        break;
                    }
                    case WAIT_CAT: {
                        this.createAnimation("animation.cat.wait", false, event);
                        break;
                    }
                    case COWGIRL_SITTING_INTRO: {
                        this.createAnimation("animation.cat.sitting_intro", false, event);
                        break;
                    }
                    case COWGIRL_SITTING_SLOW: {
                        this.createAnimation("animation.cat.sitting_slow", true, event);
                        break;
                    }
                    case COWGIRL_SITTING_FAST: {
                        this.createAnimation("animation.cat.sitting_fast", true, event);
                        break;
                    }
                    case COWGIRL_SITTING_CUM: {
                        this.createAnimation("animation.cat.sitting_cum", true, event);
                        break;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.cat.head_pat", true, event);
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
                case "idleDone": {
                    this.as = this.getRNG().nextInt(10) == 0;
                    break;
                }
                case "idle2Done": {
                    this.as = false;
                    break;
                }
                case "pearl": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "paymentMSG1": {
                    this.sendChatMessageToPlayer(this.getInteractionPlayerUUID(), "Here, I know u like fish and yea.. these are for you");
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "paymentMSG2": {
                    this.sendChatMessage("huh~?");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "paymentMSG3": {
                    this.sendChatMessage("nyyyaaaa~ :D");
                    int[] nArray = new int[]{1, 7, 10, 11};
                    int n = nArray[this.getRNG().nextInt(nArray.length)];
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[n]);
                    break;
                }
                case "paymentMSG4": {
                    this.sendChatMessage("tankuuuu owowowo");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_OWO);
                    break;
                }
                case "paymentDone": {
                    if (this.isLocalPlayerNearby()) {
                        this.doAction();
                    }
                    this.scaleFactor = 1.0f;
                    break;
                }
                case "breath": 
                case "rod_breath": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING);
                    break;
                }
                case "happyOh": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HAPPYOH);
                    break;
                }
                case "cutenya3": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[3]);
                    break;
                }
                case "cutenya2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[2]);
                    break;
                }
                case "huh": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "hmph": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HMPH);
                    break;
                }
                case "hehe": 
                case "giggle": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    break;
                }
                case "singing": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_SINGING);
                    break;
                }
                case "touch_boobsMSG1": {
                    this.sendChatMessage("comon~ touch me hihi~");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    break;
                }
                case "touch": {
                    this.playRandomSound(SoundsHandler.MISC_TOUCH);
                    break;
                }
                case "jump": {
                    this.playSoundAtVolume(SoundsHandler.MISC_JUMP[0], 0.2f);
                    break;
                }
                case "horninya": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HORNINYA);
                    break;
                }
                case "horninya2": 
                case "touch_boobs_cumMSG3": 
                case "sitting_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[1]);
                    this.playSoundAtVolume(SoundsHandler.MISC_CUMINFLATION[0], 5.0f);
                    break;
                }
                case "moan": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                    break;
                }
                case "touch_boobs_introDone": {
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    SexUI.showUI();
                    HandlePlayerMovement.setMovementLock(false);
                    break;
                }
                case "touch_boobs_slowDone": {
                    if (this.ap) {
                        this.ap = false;
                        break;
                    }
                    this.ap = Math.random() < 0.5;
                    break;
                }
                case "addCumSlow": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "addCumFast": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    break;
                }
                case "moanOrNya": {
                    if (Math.random() > 0.5) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                        break;
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "touch_boobs_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "resetGirl": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    break;
                }
                case "touch_boobs_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[3]);
                    break;
                }
                case "touch_boobs_cumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[9]);
                    break;
                }
                case "call_playerMSG1": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    this.sendChatMessage("come here - big guy hehe~");
                    break;
                }
                case "pounding": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "sitting_introMSG1": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    this.sendChatMessage("hehe~");
                    break;
                }
                case "sitting_introDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.showUI();
                    break;
                }
                case "sitting_slowMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        if (this.getRNG().nextBoolean()) {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_HORNINYA));
                            break;
                        }
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "sitting_fastMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "sitting_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    Vec3d vec3d = new Vec3d(0.0, -0.075f, -0.7109375);
                    Vec3d vec3d2 = VectorMath.rotateByYaw(vec3d, this.getYawRotation().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.getTargetPosition().x + vec3d2.x, this.getTargetPosition().y - 0.0 + vec3d2.y, this.getTargetPosition().z + vec3d2.z);
                    break;
                }
                case "sitting_fastTp": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Vec3d vec3d = new Vec3d(0.0, -0.160625, -0.9925);
                    Vec3d vec3d3 = VectorMath.rotateByYaw(vec3d, this.getYawRotation().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.getTargetPosition().x + vec3d3.x, this.getTargetPosition().y - 0.0 + vec3d3.y, this.getTargetPosition().z + vec3d3.z);
                    break;
                }
                case "headpatMSG1": {
                    this.sendChatMessage("huh?~");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "headpatMSG2": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_MMM);
                    break;
                }
                case "headpatMSG3": {
                    this.sendChatMessage("nya~");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[0]);
                }
            }
        };
        this.movementController.transitionLengthTicks = 10.0;
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

