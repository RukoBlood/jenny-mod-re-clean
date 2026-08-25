/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Kobold;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.Packets.TeleportPlayer;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.WorkerPlayerEntity;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IKobold;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.geckolib3.util.MatrixStack;

public class PlayerKobold extends WorkerPlayerEntity implements IKobold {
    final static public EyeAndKoboldColor DEFAULT_COLOR = EyeAndKoboldColor.PURPLE;
    final static public DataParameter<Float> SCALE_OFFSET = EntityDataManager.createKey(PlayerKobold.class, DataSerializers.FLOAT).getSerializer().createKey(122);
    boolean flyVariantToggle = false;
    boolean blowjobSideRight = true;
    boolean blowjobSwitching = false;
    int moanCounter = 0;

    protected PlayerKobold(World world) {
        super(world);
    }

    public PlayerKobold(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        EyeAndKoboldColor color = EyeAndKoboldColor.values()[this.getRNG().nextInt(EyeAndKoboldColor.values().length)];
        this.entityDataManager.register(WORK_POS, new BlockPos(color.getMainColor()));
        this.entityDataManager.register(MODEL_CODE, DEFAULT_COLOR.name());
        this.entityDataManager.register(SCALE_OFFSET, 0.0f);
    }

    @Override
    public AxisAlignedBB getPlayerCollisionBox(EntityPlayer player) {
        float base = 0.6f;
        float offsetY = 0.9f;
        float offsetXZ = base / 2.0f;
        return new AxisAlignedBB(
                player.posX - (double)offsetXZ,
                player.posY,
                player.posZ - (double)offsetXZ,
                player.posX + (double)offsetXZ,
                player.posY + (double)offsetY,
                player.posZ + (double)offsetXZ);
    }

    @Override
    public void setCustomPartList(List<Integer> parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); ++i) {
            int partId = parts.get(i);
            switch (i) {
                case 0: {
                    this.entityDataManager.set(SCALE_OFFSET, (float) partId / 100.0f * 0.25f);
                    continue;
                }
                case 1: {
                    this.entityDataManager.set(MODEL_CODE, EyeAndKoboldColor.values()[partId].toString());
                    continue;
                }
                case 2: {
                    this.entityDataManager.set(WORK_POS, new BlockPos(EyeAndKoboldColor.values()[partId].getMainColor()));
                    continue;
                }
                default: {
                    AbstractNpcOnlyEntity.appendPaddedNumberWithFixedValue(sb, partId);
                }
            }
        }
        this.entityDataManager.set(DNA_CODE, sb.toString());
        if (this.world.isRemote) {
            PlayerKoboldRenderer.ResetColors();
        }
    }

    @Override
    public ArrayList<Integer> getBasePartIdList() {
        ArrayList<Integer> parts = new ArrayList<>();
        parts.add(Math.round(this.entityDataManager.get(SCALE_OFFSET) * 100.0f / 0.25f));
        parts.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf(this.entityDataManager.get(MODEL_CODE))));
        parts.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf(this.entityDataManager.get(WORK_POS))));
        return parts;
    }

    @Override
    protected String buildModelCodeDNA(StringBuilder builder) {
        AbstractNpcOnlyEntity.appendPaddedLetter(builder, 8);
        AbstractNpcOnlyEntity.appendPaddedLetter(builder, 3);
        AbstractNpcOnlyEntity.appendRandomGene(builder);
        AbstractNpcOnlyEntity.appendRandomGene(builder);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 2);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 2);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 1);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 1);
        return builder.toString();
    }

    @Override
    public ArrayList<Integer> getCustomPartIdList() {
        return new ArrayList<Integer>(){
            {
                this.add(101);
                this.add(EyeAndKoboldColor.values().length);
                this.add(EyeAndKoboldColor.values().length);
                this.add(8);
                this.add(3);
                this.add(101);
                this.add(101);
                this.add(3);
                this.add(3);
                this.add(4);
                this.add(2);
            }
        };
    }

    @Override
    protected void ResetColors() {
        PlayerKoboldRenderer.ResetColors();
        KoboldRenderer.clearBoneColors();
    }

    @Override
    public float getScaleFactor() {
        float shrink = 0.25f - this.entityDataManager.get(SCALE_OFFSET);
        return 1.4f - shrink;
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        if ("anal".equals(command)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.KOBOLD_ANAL_START);
            this.sendActionPacket(this.getOutfitIndex(), Action.KOBOLD_ANAL_START);
            this.setOutfitIndex(0);
        }
        if ("oral".equals(command)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.STARTBLOWJOB);
            this.sendActionPacket(this.getOutfitIndex(), Action.STARTBLOWJOB);
            this.setOutfitIndex(0);
        }
        if ("mating".equals(command)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.MATING_PRESS_START);
            this.sendActionPacket(this.getOutfitIndex(), Action.MATING_PRESS_START);
            this.setOutfitIndex(0);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean openInteractionMenu(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "oral", "mating"}, null, false));
        return true;
    }

    @Override
    public boolean IsBlockedByCeiling() {
        Block block = this.world.getBlockState(this.getPosition().add(0, 1, 0)).getBlock();
        return !block.isPassable(this.world, this.getPosition().add(0, 1, 0));
    }

    @Override
    protected MatrixStack applyAdditionalMatrixTransformations(MatrixStack stack) {
        float f = 0.25f - this.entityDataManager.get(SCALE_OFFSET);
        stack.scale(1.0f - f, 1.0f - f, 1.0f - f);
        return stack;
    }

    @Override
    protected float transformCameraPivotY(float pivotY) {
        float f2 = 1.0f - (0.25f - this.entityDataManager.get(SCALE_OFFSET));
        return pivotY * f2;
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new KoboldHand();
    }

    @Override
    public String getHandTexture(int index) {
        return "textures/entity/kobold/hand.png";
    }

    @Override
    public Vec3i getHandColor(int index) {
        try {
            return EyeAndKoboldColor.valueOf(this.entityDataManager.get(MODEL_CODE)).getMainColor();
        } catch (Exception e) {
            e.printStackTrace();
            return super.getHandColor(index);
        }
    }

    @Override
    @Nullable
    protected Action getNextAction(Action action) {
        if (action == Action.SUCKBLOWJOB_BLINK) {
            return Action.THRUSTBLOWJOB;
        }
        if (action == Action.KOBOLD_ANAL_SLOW) {
            return Action.KOBOLD_ANAL_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.THRUSTBLOWJOB || action == Action.SUCKBLOWJOB_BLINK) {
            return Action.CUMBLOWJOB;
        }
        if (action == Action.KOBOLD_ANAL_SLOW || action == Action.KOBOLD_ANAL_FAST) {
            return Action.KOBOLD_ANAL_CUM;
        }
        if (action == Action.MATING_PRESS_HARD || action == Action.MATING_PRESS_SOFT) {
            return Action.MATING_PRESS_CUM;
        }
        return null;
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (currentAction != Action.MATING_PRESS_CUM || (action != Action.MATING_PRESS_SOFT && action != Action.MATING_PRESS_HARD)) {
            if (currentAction != Action.KOBOLD_ANAL_CUM || (action != Action.KOBOLD_ANAL_SLOW && action != Action.KOBOLD_ANAL_FAST)) {
                if (currentAction != Action.CUMBLOWJOB || (action != Action.SUCKBLOWJOB && action != Action.THRUSTBLOWJOB)) {
                    super.setCurrentAction(action);
                }
            }
        }
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        float f = 0.25f - this.getDataManager().get(KoboldEntity.SIZE);
        GeckoLibCache.getInstance().parser.setValue("size", f);
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.kobold.null", true, event);
                    break;
                }
                this.createAnimation("animation.kobold.blink", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.kobold.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.kobold.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    this.flyVariantToggle = !this.flyVariantToggle;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.kobold.fly" + (this.flyVariantToggle ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.moveInputVector.x) + Math.abs(this.moveInputVector.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.kobold.run", true, event);
                        break;
                    }
                    if (this.moveInputVector.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation("animation.kobold.walk", true, event);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.75);
                    this.createAnimation("animation.kobold.backwards_walk", true, event);
                    break;
                }
                this.createAnimation("animation.kobold.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.kobold.null", true, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.kobold.strip", false, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.kobold.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.kobold.bowcharge", false, event);
                        break;
                    }
                    case SIT: {
                        this.createAnimation("animation.kobold.sit", true, event);
                        break;
                    }
                    case MINE: {
                        this.createAnimation("animation.kobold.fall_tree", true, event);
                        break;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.kobold.paymentBackpack", true, event);
                        break;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobStart", false, event);
                        break;
                    }
                    case SUCKBLOWJOB_BLINK: {
                        String side = this.blowjobSideRight ? "R" : "L";
                        String isSwitch = this.blowjobSwitching ? "Switch" : "";
                        this.createAnimation("animation.kobold.blowjobSlow" + side + isSwitch, true, event);
                        break;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobFast", true, event);
                        break;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobCum", false, event);
                        break;
                    }
                    case KOBOLD_ANAL_START: {
                        this.createAnimation("animation.kobold.analStart", false, event);
                        break;
                    }
                    case KOBOLD_ANAL_SLOW: {
                        this.createAnimation("animation.kobold.analSoft", true, event);
                        break;
                    }
                    case KOBOLD_ANAL_FAST: {
                        this.createAnimation("animation.kobold.analHard", true, event);
                        break;
                    }
                    case KOBOLD_ANAL_CUM: {
                        this.createAnimation("animation.kobold.analCum", true, event);
                        break;
                    }
                    case SLEEP: {
                        this.createAnimation("animation.kobold.sleep", true, event);
                        break;
                    }
                    case MATING_PRESS_START: {
                        this.createAnimation("animation.kobold.mating_press_start", false, event);
                        break;
                    }
                    case MATING_PRESS_SOFT: {
                        this.createAnimation("animation.kobold.mating_press_soft", true, event);
                        break;
                    }
                    case MATING_PRESS_HARD: {
                        this.createAnimation("animation.kobold.mating_press_hard", true, event);
                        break;
                    }
                    case MATING_PRESS_CUM: {
                        this.createAnimation("animation.kobold.mating_press_cum", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    void playSound(SoundEvent sound) {
        this.playKoboldSoundAtVolume(sound, 1.0f);
    }

    void kbPlayRandomSound(SoundEvent[] sounds) {
        this.kbPlayRandomSoundAtVolume(sounds, 1.0f);
    }

    void kbPlayRandomSoundAtVolume(SoundEvent[] sounds, float volume) {
        this.playKoboldSoundAtVolume(sounds[this.getRNG().nextInt(sounds.length)], volume);
    }


    void playKoboldSoundAtVolume(SoundEvent sound, float volume) {
        float size = 0.25f - this.entityDataManager.get(SCALE_OFFSET);
        double step = size / 0.25f;
        float pitch = (float) RotationHelper.LerpDouble(0.9f, 1.1f, step);
        this.PlaySoundAtPosition(sound, volume, pitch);
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
                case "paymentMSG1": {
                    this.sendChatMessageToPlayer(this.getInteractionPlayerUUID(), "I'd like to use ur services owo");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "plob": {
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "paymentDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.doSubAction();
                    break;
                }
                case "blowjobStartMSG1": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.0, 0.625 - (double)entityPlayerSP.getEyeHeight(), -1.0), this.getYawRotation() + 180.0f);
                    PacketHandler.INSTANCE.sendToServer(new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation() + 180.0f, 0.0f));
                    break;
                }
                case "blowjobStartMSG2": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP player = Minecraft.getMinecraft().player;
                    Vec3d pos = VectorMath.rotateByYaw(new Vec3d(0.5, 0.5 - (double)player.getEyeHeight(), -0.6875), this.getYawRotation() + 180.0f);
                    PacketHandler.INSTANCE.sendToServer(new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(pos), this.getYawRotation() + 180.0f - 40.0f, 0.0f));
                    break;
                }
                case "lipsound": {
                    if (this.getRNG().nextBoolean()) {
                        this.playRandomSoundAtVolume(SoundsHandler.GIRLS_ALLIE_LIPSOUND, 1.5f);
                    } else {
                        this.playRandomSoundAtVolume(SoundsHandler.GIRLS_JENNY_LIPSOUND, 1.5f);
                    }
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "touch": {
                    this.playRandomSound(SoundsHandler.MISC_TOUCH);
                    break;
                }
                case "blowjobStartDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB_BLINK);
                    this.blowjobSwitching = false;
                    this.blowjobSideRight = true;
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "switch": {
                    this.blowjobSwitching = this.getRNG().nextBoolean();
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "endSwitch": {
                    this.blowjobSwitching = false;
                    this.blowjobSideRight = !this.blowjobSideRight;
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "blowjobFastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.SUCKBLOWJOB_BLINK);
                    break;
                }
                case "cumLoud": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "cumQuiet": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 1.5f);
                    break;
                }
                case "analCumDone": 
                case "blowjobCumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    SexUI.hide();
                    break;
                }
                case "analStartDone": {
                    this.setCurrentAction(Action.KOBOLD_ANAL_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "analStartCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.0, 0.5625 - (double)entityPlayerSP.getEyeHeight(), 0.5625), this.getYawRotation() + 180.0f);
                    PacketHandler.INSTANCE.sendToServer(new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation(), 0.0f));
                    break;
                }
                case "pounding": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "analFastRapid": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    if (this.getCurrentAction() == Action.KOBOLD_ANAL_FAST) {
                        this.resetAnimationControllerOffset();
                        break;
                    }
                    this.setCurrentAction(Action.KOBOLD_ANAL_FAST);
                    break;
                }
                case "analDone": {
                    if (this.getCurrentAction() != Action.KOBOLD_ANAL_FAST) break;
                    this.setCurrentAction(Action.KOBOLD_ANAL_SLOW);
                    break;
                }
                case "analHard": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "analSoft": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "cum": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "giggle": {
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_GIGGLE);
                    break;
                }
                case "moan": {
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "moanMating": {
                    --this.moanCounter;
                    if (this.moanCounter > 0) break;
                    this.moanCounter = 3;
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "analHardMSG1": {
                    --this.moanCounter;
                    if (this.moanCounter > 0) break;
                    this.moanCounter = 4;
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "orgasm": {
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_ORGASM);
                    break;
                }
                case "breath": {
                    this.kbPlayRandomSoundAtVolume(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING, 0.5f);
                    break;
                }
                case "haa": {
                    this.kbPlayRandomSoundAtVolume(SoundsHandler.GIRLS_KOBOLD_HAA, 0.7f);
                    break;
                }
                case "interested": {
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_INTERESTED);
                    break;
                }
                case "yep": {
                    this.kbPlayRandomSound(SoundsHandler.GIRLS_KOBOLD_YEP);
                    break;
                }
                case "bjmoan": {
                    this.playSound(SoundsHandler.random(SoundsHandler.GIRLS_KOBOLD_BJMOAN));
                    break;
                }
                case "blowjobStartbreath": {
                    int n = this.getRNG().nextInt(3);
                    this.playSound(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING[n]);
                    break;
                }
                case "matingCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = new Vec3d(0.0, 0.4375 - (double)entityPlayerSP.eyeHeight, -0.6875);
                    vec3d = VectorMath.rotateByYaw(vec3d, this.getYawRotation() + 180.0f);
                    vec3d = vec3d.add(this.getTargetPosition());
                    PacketHandler.INSTANCE.sendToServer(new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, this.getYawRotation() + 180.0f, 10.0f));
                    break;
                }
                case "mating_press_startDone": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.showUI();
                    }
                }
                case "mating_press_hardDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.MATING_PRESS_SOFT);
                    break;
                }
                case "mating_press_softReady": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.MATING_PRESS_HARD);
                    break;
                }
                case "mating_press_hardReady": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "mating_cum_cam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = new Vec3d(0.0, 1.1875 - (double)entityPlayerSP.eyeHeight, 0.125);
                    vec3d = VectorMath.rotateByYaw(vec3d, this.getYawRotation() + 180.0f);
                    vec3d = vec3d.add(this.getTargetPosition());
                    PacketHandler.INSTANCE.sendToServer(new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, this.getYawRotation() + 180.0f, 70.0f));
                    break;
                }
                case "cumMsg": {
                    this.sendChatMessage("I.. hope I am satisfying you sir");
                    this.playSound(SoundsHandler.GIRLS_KOBOLD_SAD[this.getRNG().nextInt(1)]);
                    break;
                }
                case "mating_press_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                }
            }
        };
        this.movementController.transitionLengthTicks = 3.0;
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

