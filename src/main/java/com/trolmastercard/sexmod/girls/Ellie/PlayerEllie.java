/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Ellie;

import com.google.common.base.Optional;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.*;
import com.trolmastercard.sexmod.Packets.SendGirlToSex;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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

public class PlayerEllie
extends PlayerGirl {
    boolean ar = false;
    boolean aq = false;
    int ap = 1;

    protected PlayerEllie(World world) {
        super(world);
    }

    public PlayerEllie(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float getScaleFactor() {
        return 2.05f;
    }

    @Override
    public float getEyeHeight() {
        return this.a_14() ? 1.53f : 1.9f;
    }

    @Override
    public void handleInteraction() {
        this.setCurrentAction(Action.SITDOWN);
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        if ("Face fuck".equals(command)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.CARRY_INTRO);
            this.sendActionPacket(this.getOutfitIndex(), Action.CARRY_INTRO);
        }
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new EllieLimb();
    }

    @Override
    public String getHandTexture(int index) {
        if (index == 0) {
            return "textures/entity/ellie/hand_nude.png";
        }
        return "textures/entity/ellie/hand.png";
    }

    @Override
    public boolean canOpenInteractionMenu() {
        return true;
    }

    @Override
    public void doAction(String action, UUID player) {
        if ("action.names.cowgirl".equals(action)) {
            this.changeDataParameterFromClient("animationFollowUp", "Cowgirl");
            return;
        }
        if ("action.names.missionary".equals(action)) {
            this.changeDataParameterFromClient("animationFollowUp", "Missionary");
            return;
        }
        if (!((Optional)this.entityDataManager.get(OWNER)).isPresent()) {
            return;
        }
        PackageHandler.INSTANCE.sendToServer((IMessage)new SexPrompt(action, player, (UUID)((Optional)this.entityDataManager.get(OWNER)).get(), this.guiPending));
        this.guiPending = true;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        PlayerEllie.openInventoryGui(player, this, new String[]{"Face fuck"}, false);
        return true;
    }

    void void_c(EntityPlayer entityPlayer) {
        PlayerEllie.openInventoryGui(entityPlayer, this, new String[]{"action.names.cowgirl", "action.names.missionary"}, false);
    }

    @Override
    public boolean useVanillaItemHolding() {
        return false;
    }

    @Override
    public void setCurrentAction(Action action) {
        Action fp_class3243 = this.getCurrentAction();
        if (fp_class3243 == Action.MISSIONARY_CUM && (action == Action.MISSIONARY_FAST || action == Action.MISSIONARY_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.COWGIRLCUM && (action == Action.COWGIRLSLOW || action == Action.COWGIRLFAST)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.COWGIRLSLOW) {
            return Action.COWGIRLFAST;
        }
        if (action == Action.MISSIONARY_SLOW) {
            return Action.MISSIONARY_FAST;
        }
        if (action == Action.CARRY_SLOW) {
            return Action.CARRY_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.COWGIRLFAST || action == Action.COWGIRLSLOW) {
            return Action.COWGIRLCUM;
        }
        if (action == Action.MISSIONARY_FAST || action == Action.MISSIONARY_SLOW) {
            return Action.MISSIONARY_CUM;
        }
        if (action == Action.CARRY_SLOW || action == Action.CARRY_FAST) {
            return Action.CARRY_CUM;
        }
        return null;
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.getCurrentAction() == Action.SITDOWNIDLE) {
            String string = this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES);
            if (!"Missionary".equals(string) && !"Cowgirl".equals(string)) {
                return;
            }
            EntityPlayer entityPlayer = this.getPlayerPartner();
            if (entityPlayer == null || entityPlayer.getDistance(this.getTargetScenePosition().x, this.getTargetScenePosition().y, this.getTargetScenePosition().z) > 1.0) {
                return;
            }
            this.entityDataManager.set(GirlEntity.GIRL_HAND_STATES, "");
            this.entityDataManager.set(GirlEntity.OUTFIT_INDEX, 0);
            this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)this.world.getPlayerEntityByUUID((UUID)((Optional)this.entityDataManager.get(OWNER)).get());
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), entityPlayerMP);
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayerMP.capabilities.isFlying = true;
            entityPlayer.capabilities.isFlying = true;
            entityPlayerMP.noClip = true;
            entityPlayer.noClip = true;
            entityPlayerMP.setNoGravity(true);
            entityPlayer.setNoGravity(true);
            if ("Missionary".equals(string)) {
                this.setCurrentAction(Action.MISSIONARY_START);
                Vec3d vec3d = this.getTargetScenePosition().subtract(0.0, 0.1, 0.0);
                entityPlayer.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, this.getYawRotation().floatValue(), 60.0f);
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            } else {
                this.setCurrentAction(Action.COWGIRLSTART);
                Vec3d vec3d = this.getTargetScenePosition().add(new Vec3d(-Math.sin((double)this.getYawRotation().floatValue() * (Math.PI / 180)) * 1.8, -0.65, Math.cos((double)this.getYawRotation().floatValue() * (Math.PI / 180)) * 1.8));
                entityPlayer.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, 180.0f + this.getYawRotation().floatValue(), -30.0f);
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            }
        }
    }

    boolean a_14() {
        EntityPlayer entityPlayer = this.getOwnerPlayer();
        if (entityPlayer == null) {
            return false;
        }
        return this.world.getBlockState(entityPlayer.getPosition().up().up()).getBlock() != Blocks.AIR;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                this.createAnimation("animation.ellie.eyes", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.ellie.ride", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.ar = !this.ar;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.ellie.fly" + (this.ar ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.5);
                        this.createAnimation(this.a_14() ? "animation.ellie.crouchwalk" : "animation.ellie.run", true, event);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation(this.a_14() ? "animation.ellie.crouchwalk" : "animation.ellie.fastwalk", true, event);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation(this.a_14() ? "animation.ellie.crouchwalk" : "animation.ellie.backwards_walk", true, event);
                    break;
                }
                this.createAnimation(this.a_14() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.ellie.null", true, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.ellie.strip", false, event);
                        break;
                    }
                    case DASH: {
                        this.createAnimation("animation.ellie.dash", false, event);
                        break;
                    }
                    case HUG: {
                        this.createAnimation("animation.ellie.hug", false, event);
                        break;
                    }
                    case HUGIDLE: {
                        this.createAnimation("animation.ellie.hugidle", true, event);
                        break;
                    }
                    case HUGSELECTED: {
                        this.createAnimation("animation.ellie.hugselected", false, event);
                        break;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.ellie.sitdown", false, event);
                        break;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.ellie.sitdownidle", true, event);
                        break;
                    }
                    case COWGIRLSTART: {
                        this.createAnimation("animation.ellie.cowgirlstart", false, event);
                        break;
                    }
                    case COWGIRLSLOW: {
                        this.createAnimation("animation.ellie.cowgirlslow2", true, event);
                        break;
                    }
                    case COWGIRLFAST: {
                        this.createAnimation("animation.ellie.cowgirlfast", true, event);
                        break;
                    }
                    case COWGIRLCUM: {
                        this.createAnimation("animation.ellie.cowgirlcum", true, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.ellie.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.ellie.bowcharge", false, event);
                        break;
                    }
                    case RIDE: {
                        this.createAnimation("animation.ellie.ride", true, event);
                        break;
                    }
                    case SIT: {
                        this.createAnimation("animation.ellie.sit", true, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.ellie.throwpearl", false, event);
                        break;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.ellie.downed", true, event);
                        break;
                    }
                    case MISSIONARY_START: {
                        this.createAnimation("animation.ellie.missionary_start", false, event);
                        break;
                    }
                    case MISSIONARY_SLOW: {
                        this.createAnimation("animation.ellie.missionary_slow", true, event);
                        break;
                    }
                    case MISSIONARY_FAST: {
                        this.createAnimation("animation.ellie.missionary_fast", true, event);
                        break;
                    }
                    case MISSIONARY_CUM: {
                        this.createAnimation("animation.ellie.missionary_cum", false, event);
                        break;
                    }
                    case CARRY_INTRO: {
                        this.createAnimation("animation.ellie.carry_intro", false, event);
                        break;
                    }
                    case CARRY_SLOW: {
                        this.createAnimation("animation.ellie.carry_slow" + this.ap, true, event);
                        break;
                    }
                    case CARRY_FAST: {
                        this.createAnimation("animation.ellie.carry_fast", true, event);
                        break;
                    }
                    case CARRY_CUM: {
                        this.createAnimation("animation.ellie.carry_cum", true, event);
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
                case "dashMSG1": {
                    float f;
                    EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
                    if (entityPlayer == null) break;
                    Vec3d vec3d = this.getPositionVector().subtract(entityPlayer.getPositionVector());
                    this.rotationYaw = f = (float)Math.atan2(vec3d.z, vec3d.x) * 57.29578f;
                    this.rotationYawHead = f;
                    this.renderYawOffset = f;
                    break;
                }
                case "dashReady": {
                    if (!this.isLocalPlayerNearby()) break;
                    break;
                }
                case "dashDone": {
                    float f;
                    this.setCurrentAction(Action.HUG);
                    EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
                    if (entityPlayer == null) break;
                    this.rotationYaw = f = entityPlayer.rotationYaw;
                    this.rotationYawHead = f;
                    this.renderYawOffset = f;
                    break;
                }
                case "hugMSG1": {
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    if (!entityPlayerSP.getPersistentID().equals(this.getInteractionPlayerUUID()) && !entityPlayerSP.getUniqueID().equals(this.getInteractionPlayerUUID())) break;
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getUniqueID().toString(), entityPlayerSP.getPositionVector(), entityPlayerSP.rotationYaw - 80.0f, entityPlayerSP.rotationPitch));
                    break;
                }
                case "hugMSG2": {
                    this.sendGirlChatMessage("Hmm...");
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_HMPH[3], 3.0f);
                    break;
                }
                case "hugMSG3": {
                    this.sendGirlChatMessage("Hey!");
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_AHH[2], 3.0f);
                    break;
                }
                case "hugMSG4": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.mommyhorny", new Object[0]));
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_GIGGLE[0], 3.0f);
                    break;
                }
                case "hugMSG5": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.whattodo", new Object[0]));
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_HUH[1], 3.0f);
                    break;
                }
                case "hugDone": {
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    if (!entityPlayerSP.getPersistentID().equals(this.getInteractionPlayerUUID())) break;
                    this.setCurrentAction(Action.HUGIDLE);
                    this.void_c(entityPlayerSP);
                    break;
                }
                case "hugselectedMSG1": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.iknow", new Object[0]));
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_MMM[0], 3.0f);
                    break;
                }
                case "hugselectedMSG2": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.followmedarling", new Object[0]));
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 3.0f);
                    break;
                }
                case "hugselectedDone": {
                    if (!this.isLocalPlayerNearby()) break;
                    Vec3d vec3d = this.getPositionVector();
                    vec3d = vec3d.add(-Math.sin((double)(this.rotationYaw + 90.0f) * (Math.PI / 180)) * -0.7803124785423279, 0.0, Math.cos((double)(this.rotationYaw + 90.0f) * (Math.PI / 180)) * -0.7803124785423279);
                    vec3d = vec3d.add(-Math.sin((double)this.rotationYaw * (Math.PI / 180)) * 0.5296875238418579, 0.0, Math.cos((double)this.rotationYaw * (Math.PI / 180)) * 0.5296875238418579);
                    String string = vec3d.x + "f" + vec3d.y + "f" + vec3d.z + "f";
                    PackageHandler.INSTANCE.sendToServer((IMessage)new ChangeDataParameter(this.girlID(), "targetPos", string));
                    this.resetCameraAndPhysics();
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendGirlToSex(this.girlID()));
                    this.setCurrentAction(Action.NULL);
                    break;
                }
                case "sitdownMSG1": {
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 3.0f);
                    if (!this.isLocalPlayerNearby()) break;
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.cometomommy", new Object[0]));
                    break;
                }
                case "sitdownDone": {
                    if (!this.hasOwnerUUID()) break;
                    this.setCurrentAction(Action.SITDOWNIDLE);
                    this.void_c(this.world.getPlayerEntityByUUID(this.getOwnerUserUUID()));
                    break;
                }
                case "missionary_startDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    SexUI.showUI();
                    break;
                }
                case "cowgirlStartMSG0": {
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 3.0f);
                    break;
                }
                case "cowgirlStartMSG1": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.sendChatMessage(I18n.format("ellie.dialogue.like", new Object[0]));
                    SexUI.resetCumPercentage();
                    break;
                }
                case "cowgirlStartMSG2": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cowgirlStartDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.COWGIRLSLOW);
                    SexUI.showUI();
                    break;
                }
                case "cowgirlfastMSG1": {
                    if (this.aq) {
                        this.aq = false;
                    } else {
                        this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    }
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "cowgirlfastReady": {
                    if (!this.isControlledByLocalPlayer()) break;
                    if (!HandlePlayerMovement.isThrusting) {
                        this.setCurrentAction(Action.COWGIRLSLOW);
                        break;
                    }
                    if (Reference.RANDOM.nextInt(4) == 1) break;
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "cowgirlfastdomMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.2);
                    break;
                }
                case "cowgirlcumMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG2": {
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_MOAN[5], 3.0f);
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG3": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG4": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.hide();
                    break;
                }
                case "cowgirlcumMSG5": 
                case "missionary_cumMSG2": {
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 3.0f);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.sendChatMessage(I18n.format("ellie.dialogue.goodboy", new Object[0]));
                    break;
                }
                case "cowgirlcumMSG6": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "missionary_cumDone": 
                case "cowgirlcumDone": 
                case "carry_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "attackDone": {
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "pearl": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "openSexUi": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "missionary_slowMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() && this.getRNG().nextBoolean()) {
                        this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_MOAN), 3.0f);
                    } else {
                        this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "missionary_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() || this.getRNG().nextBoolean()) {
                        this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_MOAN), 3.0f);
                    } else {
                        this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.05);
                    break;
                }
                case "missionary_fastDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    if (HandlePlayerMovement.isThrusting) {
                        this.setCurrentAction(Action.MISSIONARY_FAST);
                        break;
                    }
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    break;
                }
                case "bedRustle": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    break;
                }
                case "bedRustle1": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[1]);
                    break;
                }
                case "missionary_cumMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    break;
                }
                case "carry_introMSG1": {
                    this.sendChatMessage("I'm hungry..");
                    this.playRandomSoundAtVolume(SoundsHandler.GIRLS_ELLIE_HMPH, 6.0f);
                    break;
                }
                case "carry_introMSG2": {
                    this.sendChatMessage("heh~");
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    break;
                }
                case "lipsound": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_LIPSOUND);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cum": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "pound": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "carry_slowDone": {
                    int n = this.ap;
                    do {
                        this.ap = this.getRNG().nextInt(4) + 1;
                    } while (this.ap == n);
                    break;
                }
                case "carry_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CARRY_SLOW);
                    break;
                }
                case "sexUI": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();

                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

