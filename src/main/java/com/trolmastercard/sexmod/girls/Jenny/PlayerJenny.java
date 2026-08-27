/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Jenny;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.Packets.SetPlayerForGirl;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerJenny extends PlayerGirl {
    boolean isFlyAltAnim = false;
    boolean isDoggyHardMode = false;
    int thrustSoundCounter = 0;
    boolean isPaizuriCameraSet = false;

    protected PlayerJenny(World world) {
        super(world);
    }

    public PlayerJenny(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float getScaleFactor() {
        return 1.75f;
    }

    @Override
    public float getLeftArmAngle() {
        return 35.0f;
    }

    @Override
    public float getRightArmAngle() {
        return 140.0f;
    }

    @Override
    public float getEyeHeight() {
        return 1.64f;
    }

    @Override
    public void handleInteraction() {
        this.setCurrentAction(Action.STARTDOGGY);
        this.entityDataManager.set(GirlEntity.OUTFIT_INDEX, 0);
        this.cameraYaw = this.entityDataManager.get(GirlEntity.YAW_ROTATION);
    }

    @Override
    public boolean useVanillaItemHolding() {
        return false;
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new JennyHand();
    }

    @Override
    public String getHandTexture(int index) {
        if (index == 0) {
            return "textures/entity/jenny/hand_nude.png";
        }
        return "textures/entity/jenny/hand.png";
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        if ("action.names.boobjob".equals(command)) {
            this.entityDataManager.set(GirlEntity.OUTFIT_INDEX, 0);
            this.setCurrentAction(Action.PAIZURI_START);
            this.sendActionPacket(0, Action.PAIZURI_START);
            this.teleportPlayerToGirl(partnerUUID);
        }
        if ("action.names.blowjob".equals(command)) {
            this.setCurrentAction(Action.STARTBLOWJOB);
            this.sendActionPacket(this.getOutfitIndex(), Action.PAIZURI_START);
            this.teleportPlayerToGirl(partnerUUID);
        }
    }

    @Override
    public void updateAITasks() {
        EntityPlayer entityPlayer;
        super.updateAITasks();
        if (this.getCurrentAction() == Action.WAITDOGGY && (entityPlayer = this.getPlayerPartner()) != null && entityPlayer.getDistance(this.getTargetScenePosition().x, this.getTargetScenePosition().y, this.getTargetScenePosition().z) < 1.0) {
            if (this.isOwnerUUID(entityPlayer.getPersistentID())) {
                entityPlayer.sendMessage(new TextComponentString((Object)((Object)TextFormatting.DARK_PURPLE) + "sowy no lesbo action yet uwu"));
                return;
            }
            this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            entityPlayer.setPositionAndUpdate(this.getPositionVector().x, this.getTargetScenePosition().y, this.getPositionVector().z);
            this.alignPlayerToGirl((EntityPlayerMP)entityPlayer, false);
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayer.capabilities.isFlying = true;
            this.world.getPlayerEntityByUUID((UUID)this.getOwnerUserUUID()).capabilities.isFlying = true;
            this.moveCamera(0.0, 0.0, 0.4, 0.0f, 60.0f);
            this.cameraOriginPos = null;
            this.setCurrentAction(Action.DOGGYSTART);
            PacketHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        }
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        GirlEntity.openInventoryGui(player, this, new String[]{"action.names.blowjob", "action.names.boobjob"}, false);
        return true;
    }

    @Override
    protected Action getNextAction(Action action) {
        switch (action) {
            case SUCKBLOWJOB: {
                return Action.THRUSTBLOWJOB;
            }
            case DOGGYSLOW: {
                return Action.DOGGYFAST;
            }
            case PAIZURI_SLOW: {
                if (this.isPaizuriCameraSet) {
                    this.isPaizuriCameraSet = false;
                    this.moveCamera(0.0, 0.0, 0.0, 0.0f, 70.0f);
                }
                return Action.PAIZURI_FAST;
            }
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB) {
            this.moveCamera(0.0, 0.0, 0.0, 0.0f, 70.0f);
            return Action.CUMBLOWJOB;
        }
        if (action == Action.DOGGYSLOW || action == Action.DOGGYFAST) {
            return Action.DOGGYCUM;
        }
        if (action == Action.PAIZURI_FAST || action == Action.PAIZURI_SLOW) {
            return Action.PAIZURI_CUM;
        }
        return null;
    }

    @Override
    public void setCurrentAction(Action action) {
        Action fp_class3243 = this.getCurrentAction();
        if (fp_class3243 == Action.DOGGYCUM && (action == Action.DOGGYSLOW || action == Action.DOGGYFAST)) {
            return;
        }
        if (fp_class3243 == Action.CUMBLOWJOB && (action == Action.THRUSTBLOWJOB || action == Action.SUCKBLOWJOB)) {
            return;
        }
        if (fp_class3243 == Action.PAIZURI_CUM && (action == Action.PAIZURI_SLOW || action == Action.PAIZURI_FAST)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                this.createAnimation("animation.jenny.fhappy", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.jenny.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.isFlyAltAnim = !this.isFlyAltAnim;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.jenny.fly" + (this.isFlyAltAnim ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.moveInputVector.x) + Math.abs(this.moveInputVector.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.jenny.run", true, event);
                        break;
                    }
                    if (this.moveInputVector.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(1.5);
                        this.createAnimation("animation.jenny.fastwalk", true, event);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.2f);
                    this.createAnimation("animation.jenny.backwards_walk", true, event);
                    break;
                }
                this.createAnimation("animation.jenny.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.jenny.null", true, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.jenny.strip", false, event);
                        break;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.jenny.payment", false, event);
                        break;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobintro", false, event);
                        break;
                    }
                    case SUCKBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobsuck", true, event);
                        break;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobthrust", true, event);
                        break;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.jenny.blowjobcum", false, event);
                        break;
                    }
                    case STARTDOGGY: {
                        this.createAnimation("animation.jenny.doggygoonbed", false, event);
                        break;
                    }
                    case WAITDOGGY: {
                        this.createAnimation("animation.jenny.doggywait", true, event);
                        break;
                    }
                    case DOGGYSTART: {
                        this.createAnimation("animation.jenny.doggystart", false, event);
                        break;
                    }
                    case DOGGYSLOW: {
                        this.createAnimation("animation.jenny.doggyslow", true, event);
                        break;
                    }
                    case DOGGYFAST: {
                        this.createAnimation("animation.jenny.doggyfast_" + (this.isDoggyHardMode ? "hard" : "soft"), true, event);
                        break;
                    }
                    case DOGGYCUM: {
                        this.createAnimation("animation.jenny.doggycum", false, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.jenny.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.jenny.bowcharge", false, event);
                        break;
                    }
                    case RIDE: {
                        this.createAnimation("animation.jenny.ride", true, event);
                        break;
                    }
                    case SIT: {
                        this.createAnimation("animation.jenny.sit", true, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.jenny.throwpearl", false, event);
                        break;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.jenny.downed", true, event);
                        break;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.jenny.paizuri_start", false, event);
                        break;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.jenny.paizuri_slow", true, event);
                        break;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.jenny.paizuri_fast", true, event);
                        break;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.jenny.paizuri_cum", false, event);
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
                case "stripMSG1": {
                    this.sendGirlChatMessage("Hihi~");
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "paymentMSG1": {
                    this.sendGirlChatMessage("Huh?");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HUH[1]);
                    break;
                }
                case "paymentMSG2": {
                    this.playSoundAtVolume(SoundsHandler.MISC_PLOB[0], 0.5f);
                    String string = "<" + Minecraft.getMinecraft().player.getName() + "> ";
                    switch (this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES)) {
                        case "strip": {
                            this.broadcastChatAround(string + "show Bobs and vegana pls", true);
                            break;
                        }
                        case "blowjob": {
                            this.broadcastChatAround(string + "Give me the sucky sucky and these are yours", true);
                            break;
                        }
                        case "doggy": {
                            this.broadcastChatAround(string + "Give me the sex pls :)", true);
                            break;
                        }
                        case "boobjob": {
                            this.broadcastChatAround(string + "gib boba OwO", true);
                            break;
                        }
                    }
                    this.broadcastChatAround(string + "sex pls", true);
                    break;
                }
                case "paymentMSG3": {
                    this.sendGirlChatMessage("Hehe~");
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "paymentMSG4": {
                    this.playSoundAtVolume(SoundsHandler.MISC_PLOB[0], 0.25f);
                    break;
                }
                case "paymentDone": {
                    this.doSubAction();
                    break;
                }
                case "bjiMSG1": {
                    this.sendGirlChatMessage("What are you...");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_MMM[8]);
                    this.cameraYaw = 180.0f;
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "bjiMSG2": {
                    this.sendGirlChatMessage("eh... boys...");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG3": {
                    this.sendGirlChatMessage("OHOhh...!");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[0]);
                    break;
                }
                case "bjiMSG4": {
                    this.PlaySound(SoundsHandler.MISC_BELLJINGLE[0]);
                    break;
                }
                case "bjiMSG5": {
                    this.sendGirlChatMessage("Was this really necessary?!");
                    this.playSoundAtVolume(SoundsHandler.GIRLS_JENNY_HMPH[1], 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "bjiMSG6": {
                    this.sendGirlChatMessage("Oh~");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                    break;
                }
                case "bjiMSG7": {
                    this.sendGirlChatMessage("You like it?~");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[4]);
                    break;
                }
                case "bjiMSG8": {
                    this.broadcastChatAround("<" + Minecraft.getMinecraft().player.getName() + "> Yee", true);
                    this.playSoundAtVolume(SoundsHandler.MISC_PLOB[0], 0.5f);
                    break;
                }
                case "bjiMSG9": {
                    this.sendGirlChatMessage("Hihihi~");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[2]);
                    break;
                }
                case "bjiMSG10": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.moveCamera(-0.4, -0.8, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "bjiMSG11": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjiMSG12": {
                    if (Reference.RANDOM.nextInt(5) == 0) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_BJMOAN));
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "bjtMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MMM));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
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
                    this.isDoggyHardMode = true;
                    break;
                }
                case "bjtReady": 
                case "paizuriReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "bjcMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_BJMOAN[1]);
                    break;
                }
                case "bjcMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_BJMOAN[7]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.hide();
                    break;
                }
                case "bjcMSG3": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[1]);
                    break;
                }
                case "bjcMSG4": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[0]);
                    break;
                }
                case "bjcMSG5": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[1]);
                    break;
                }
                case "bjcMSG6": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[2]);
                    break;
                }
                case "bjcMSG7": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[3]);
                    break;
                }
                case "bjcBlackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "bjcDone": 
                case "paizuri_cumDone": 
                case "doggyCumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "doggyGoOnBedMSG1": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    this.cameraYaw = this.rotationYaw;
                    break;
                }
                case "doggyGoOnBedMSG2": {
                    this.sendChatMessage("what are you waiting for?~");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[9]);
                    break;
                }
                case "doggyGoOnBedMSG3": {
                    this.sendChatMessage("this ass ain't gonna fuck itself...");
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
                    break;
                }
                case "doggyGoOnBedMSG4": {
                    this.playSoundAtVolume(SoundsHandler.MISC_SLAP[0], 0.75f);
                    break;
                }
                case "doggyGoOnBedDone": {
                    PacketHandler.INSTANCE.sendToServer((IMessage)new SetPlayerForGirl(this.girlID(), Minecraft.getMinecraft().player.getPersistentID()));
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
                    this.playSoundAtVolume(SoundsHandler.MISC_BEDRUSTLE[1], 0.5f);
                    break;
                }
                case "doggystartMSG4": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS));
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_MMM[1]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "doggystartMSG5": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggystartDone": {
                    this.setCurrentAction(Action.DOGGYSLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "doggyslowMSG1": {
                    this.isDoggyHardMode = false;
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.33f);
                    int n = Reference.RANDOM.nextInt(4);
                    if (n == 0) {
                        n = Reference.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MMM));
                        } else {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                        }
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.00666);
                    break;
                }
                case "doggyslowMSG2": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING), 0.5f);
                    break;
                }
                case "doggyfastMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    ++this.thrustSoundCounter;
                    if (this.thrustSoundCounter % 2 == 0) {
                        int n = Reference.RANDOM.nextInt(2);
                        if (n == 0) {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                            break;
                        }
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                        break;
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_AHH));
                    break;
                }
                case "doggyfastDone": {
                    this.isDoggyHardMode = false;
                    this.setCurrentAction(Action.DOGGYSLOW);
                    break;
                }
                case "doggycumMSG1": {
                    this.playSoundAtVolume(SoundsHandler.MISC_CUMINFLATION[0], 2.0f);
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 2.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MOAN));
                    break;
                }
                case "doggycumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[4]);
                    break;
                }
                case "doggycumMSG3": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[5]);
                    break;
                }
                case "doggycumMSG4": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[6]);
                    break;
                }
                case "doggycumMSG5": {
                    this.PlaySound(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[7]);
                    break;
                }
                case "pearl": {
                    PacketHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "boobjob_camera": {
                    if (!this.isControlledByLocalPlayer() || this.isPaizuriCameraSet) break;
                    this.isPaizuriCameraSet = true;
                    this.cameraYaw = 180.0f;
                    this.moveCamera(-0.7, -0.6, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.showUI();
                    break;
                }
                case "paizuriFastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_MMM));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_JENNY_AHH));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "paizuriSlowMSG1": 
                case "paizuriStartMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "paizuri_fastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    if (!this.isControlledByLocalPlayer() || this.isPaizuriCameraSet) break;
                    this.isPaizuriCameraSet = true;
                    this.moveCamera(-0.7, -0.6, -0.2, 60.0f, -3.0f);
                    break;
                }
                case "paizuri_startStep": {
                    IBlockState iBlockState = this.world.getBlockState(this.getPosition().subtract(new Vec3i(0, 1, 0)));
                    this.PlaySound(iBlockState.getBlock().getSoundType(iBlockState, this.world, this.getPosition(), this).getStepSound());
                    break;
                }
                case "paizuri_cumStart": {
                    if (!this.isControlledByLocalPlayer() || this.isPaizuriCameraSet) break;
                    this.moveCamera(-0.7, -0.6, -0.2, 60.0f, -3.0f);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

