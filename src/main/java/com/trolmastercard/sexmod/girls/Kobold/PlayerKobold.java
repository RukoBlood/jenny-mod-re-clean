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

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.TeleportPlayer;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.ew_class277;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.geckolib3.util.MatrixStack;

public class PlayerKobold extends ew_class277 implements dr_class199 {
    final static public EyeAndKoboldColor aw = EyeAndKoboldColor.PURPLE;
    final static public DataParameter<Float> aA = EntityDataManager.createKey(PlayerKobold.class, DataSerializers.FLOAT).getSerializer().createKey(122);
    boolean aB = false;
    boolean az = true;
    boolean ay = false;
    int ax = 0;

    protected PlayerKobold(World world) {
        super(world);
    }

    public PlayerKobold(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.values()[this.getRNG().nextInt(EyeAndKoboldColor.values().length)];
        this.entityDataManager.register(au, new BlockPos(eyeAndKoboldColor_.getMainColor()));
        this.entityDataManager.register(as, aw.name());
        this.entityDataManager.register(aA, 0.0f);
    }

    @Override
    public AxisAlignedBB getPlayerBB(EntityPlayer entityPlayer) {
        float f = 0.6f;
        float f2 = 0.9f;
        float f3 = f / 2.0f;
        return new AxisAlignedBB(entityPlayer.posX - (double)f3, entityPlayer.posY, entityPlayer.posZ - (double)f3, entityPlayer.posX + (double)f3, entityPlayer.posY + (double)f2, entityPlayer.posZ + (double)f3);
    }

    @Override
    public void setCustomPartList(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        block5: for (int i = 0; i < list.size(); ++i) {
            int n = list.get(i);
            switch (i) {
                case 0: {
                    this.entityDataManager.set(aA, Float.valueOf((float)n / 100.0f * 0.25f));
                    continue block5;
                }
                case 1: {
                    this.entityDataManager.set(as, EyeAndKoboldColor.values()[n].toString());
                    continue block5;
                }
                case 2: {
                    this.entityDataManager.set(au, new BlockPos(EyeAndKoboldColor.values()[n].getMainColor()));
                    continue block5;
                }
                default: {
                    AbstractNpcOnlyEntity.appendPaddedNumber2(stringBuilder, n);
                }
            }
        }
        this.entityDataManager.set(at, stringBuilder.toString());
        if (this.world.isRemote) {
            PlayerKoboldRenderer.ResetColors();
        }
    }

    @Override
    public ArrayList<Integer> L() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(Math.round(this.entityDataManager.get(aA).floatValue() * 100.0f / 0.25f));
        arrayList.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf((String)this.entityDataManager.get(as))));
        arrayList.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf((Vec3i)this.entityDataManager.get(au))));
        return arrayList;
    }

    @Override
    protected String a(StringBuilder stringBuilder) {
        AbstractNpcOnlyEntity.appendRandomGeneExclusive(stringBuilder, 8);
        AbstractNpcOnlyEntity.appendRandomGeneExclusive(stringBuilder, 3);
        AbstractNpcOnlyEntity.appendGaussianBodyGene(stringBuilder);
        AbstractNpcOnlyEntity.appendGaussianBodyGene(stringBuilder);
        AbstractNpcOnlyEntity.appendPaddedNumber(stringBuilder, 2);
        AbstractNpcOnlyEntity.appendPaddedNumber(stringBuilder, 2);
        AbstractNpcOnlyEntity.appendPaddedNumber(stringBuilder, 1);
        AbstractNpcOnlyEntity.appendPaddedNumber(stringBuilder, 1);
        return stringBuilder.toString();
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
        float f = 0.25f - this.entityDataManager.get(aA).floatValue();
        return 1.4f - f;
    }

    @Override
    public void onGuiActionSelected(String actionName, UUID partnerUUID) {
        if ("anal".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.KOBOLD_ANAL_START);
            this.initActionState(this.getOutfitIndex(), Action.KOBOLD_ANAL_START);
            this.setOutfitIndex(0);
        }
        if ("oral".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.STARTBLOWJOB);
            this.initActionState(this.getOutfitIndex(), Action.STARTBLOWJOB);
            this.setOutfitIndex(0);
        }
        if ("mating".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.MATING_PRESS_START);
            this.initActionState(this.getOutfitIndex(), Action.MATING_PRESS_START);
            this.setOutfitIndex(0);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean openGuiForPlayer(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "oral", "mating"}, null, false));
        return true;
    }

    @Override
    public boolean boolean_a() {
        Block block = this.world.getBlockState(this.getPosition().add(0, 1, 0)).getBlock();
        return !block.isPassable(this.world, this.getPosition().add(0, 1, 0));
    }

    @Override
    protected MatrixStack applyAdditionalMatrixTransformations(MatrixStack stack) {
        float f = 0.25f - this.entityDataManager.get(aA);
        stack.scale(1.0f - f, 1.0f - f, 1.0f - f);
        return stack;
    }

    @Override
    protected float transformCameraPivotY(float pivotY) {
        float f2 = 1.0f - (0.25f - this.entityDataManager.get(aA));
        return pivotY * f2;
    }

    @Override
    public IRenderer getHandRenderer(int n) {
        return new KoboldHand();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/kobold/hand.png";
    }

    @Override
    public Vec3i net_minecraft_util_math_Vec3i_b(int n) {
        try {
            return EyeAndKoboldColor.valueOf((String)this.entityDataManager.get(as)).getMainColor();
        } catch (Exception exception) {
            exception.printStackTrace();
            return super.net_minecraft_util_math_Vec3i_b(n);
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
        Action fp_class3243 = this.getCurrentAction();
        if (fp_class3243 == Action.MATING_PRESS_CUM && (action == Action.MATING_PRESS_SOFT || action == Action.MATING_PRESS_HARD)) {
            return;
        }
        if (fp_class3243 == Action.KOBOLD_ANAL_CUM && (action == Action.KOBOLD_ANAL_SLOW || action == Action.KOBOLD_ANAL_FAST)) {
            return;
        }
        if (fp_class3243 == Action.CUMBLOWJOB && (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        float f = 0.25f - this.getDataManager().get(KoboldEntity.aE).floatValue();
        GeckoLibCache.getInstance().parser.setValue("size", f);
        block5 : switch (event.getController().getName()) {
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
                    boolean bl = this.aB = !this.aB;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.kobold.fly" + (this.aB ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.kobold.run", true, event);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
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
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.kobold.strip", false, event);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.kobold.attack" + this.nextAttack, false, event);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.kobold.bowcharge", false, event);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.kobold.sit", true, event);
                        break block5;
                    }
                    case MINE: {
                        this.createAnimation("animation.kobold.fall_tree", true, event);
                        break block5;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.kobold.paymentBackpack", true, event);
                        break block5;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobStart", false, event);
                        break block5;
                    }
                    case SUCKBLOWJOB_BLINK: {
                        String string = this.az ? "R" : "L";
                        String string2 = this.ay ? "Switch" : "";
                        this.createAnimation("animation.kobold.blowjobSlow" + string + string2, true, event);
                        break block5;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobFast", true, event);
                        break block5;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobCum", false, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_START: {
                        this.createAnimation("animation.kobold.analStart", false, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_SLOW: {
                        this.createAnimation("animation.kobold.analSoft", true, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_FAST: {
                        this.createAnimation("animation.kobold.analHard", true, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_CUM: {
                        this.createAnimation("animation.kobold.analCum", true, event);
                        break block5;
                    }
                    case SLEEP: {
                        this.createAnimation("animation.kobold.sleep", true, event);
                        break block5;
                    }
                    case MATING_PRESS_START: {
                        this.createAnimation("animation.kobold.mating_press_start", false, event);
                        break block5;
                    }
                    case MATING_PRESS_SOFT: {
                        this.createAnimation("animation.kobold.mating_press_soft", true, event);
                        break block5;
                    }
                    case MATING_PRESS_HARD: {
                        this.createAnimation("animation.kobold.mating_press_hard", true, event);
                        break block5;
                    }
                    case MATING_PRESS_CUM: {
                        this.createAnimation("animation.kobold.mating_press_cum", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    void b(SoundEvent soundEvent) {
        this.b(soundEvent, 1.0f);
    }

    void b(SoundEvent[] soundEventArray) {
        this.b(soundEventArray, 1.0f);
    }

    void b(SoundEvent[] soundEventArray, float f) {
        this.b(soundEventArray[this.getRNG().nextInt(soundEventArray.length)], f);
    }

    void b(SoundEvent soundEvent, float f) {
        float f2 = 0.25f - this.entityDataManager.get(aA).floatValue();
        double d = f2 / 0.25f;
        float f3 = (float) ReferenceAndRotationHelper.LerpDouble((double)0.9f, (double)1.1f, d);
        this.PlaySoundAtPosition(soundEvent, f, f3);
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
                    this.a(this.getInteractionPlayerUUID(), "I'd like to use ur services owo");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "plob": {
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "paymentDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.U();
                    break;
                }
                case "blowjobStartMSG1": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.625 - (double)entityPlayerSP.getEyeHeight(), -1.0), this.getYawRotation().floatValue() + 180.0f);
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation().floatValue() + 180.0f, 0.0f));
                    break;
                }
                case "blowjobStartMSG2": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.5, 0.5 - (double)entityPlayerSP.getEyeHeight(), -0.6875), this.getYawRotation().floatValue() + 180.0f);
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation().floatValue() + 180.0f - 40.0f, 0.0f));
                    break;
                }
                case "lipsound": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIPSOUND, 1.5f);
                    } else {
                        this.PlaySound(SoundsHandler.GIRLS_JENNY_LIPSOUND, 1.5f);
                    }
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "touch": {
                    this.playSoundAroundHer(SoundsHandler.MISC_TOUCH);
                    break;
                }
                case "blowjobStartDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB_BLINK);
                    this.ay = false;
                    this.az = true;
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "switch": {
                    this.ay = this.getRNG().nextBoolean();
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "endSwitch": {
                    this.ay = false;
                    this.az = !this.az;
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "blowjobFastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.SUCKBLOWJOB_BLINK);
                    break;
                }
                case "cumLoud": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "cumQuiet": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 1.5f);
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
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.5625 - (double)entityPlayerSP.getEyeHeight(), 0.5625), this.getYawRotation().floatValue() + 180.0f);
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation().floatValue(), 0.0f));
                    break;
                }
                case "pounding": {
                    this.playSoundAroundHer(SoundsHandler.MISC_POUNDING);
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
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "giggle": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_GIGGLE);
                    break;
                }
                case "moan": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "moanMating": {
                    --this.ax;
                    if (this.ax > 0) break;
                    this.ax = 3;
                    this.b(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "analHardMSG1": {
                    --this.ax;
                    if (this.ax > 0) break;
                    this.ax = 4;
                    this.b(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "orgasm": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_ORGASM);
                    break;
                }
                case "breath": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING, 0.5f);
                    break;
                }
                case "haa": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_HAA, 0.7f);
                    break;
                }
                case "interested": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_INTERESTED);
                    break;
                }
                case "yep": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_YEP);
                    break;
                }
                case "bjmoan": {
                    this.b(SoundsHandler.random(SoundsHandler.GIRLS_KOBOLD_BJMOAN));
                    break;
                }
                case "blowjobStartbreath": {
                    int n = this.getRNG().nextInt(3);
                    this.b(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING[n]);
                    break;
                }
                case "matingCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = new Vec3d(0.0, 0.4375 - (double)entityPlayerSP.eyeHeight, -0.6875);
                    vec3d = VectorMath.rotate(vec3d, this.getYawRotation().floatValue() + 180.0f);
                    vec3d = vec3d.add(this.getTargetPosition());
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, this.getYawRotation().floatValue() + 180.0f, 10.0f));
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
                    vec3d = VectorMath.rotate(vec3d, this.getYawRotation().floatValue() + 180.0f);
                    vec3d = vec3d.add(this.getTargetPosition());
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, this.getYawRotation().floatValue() + 180.0f, 70.0f));
                    break;
                }
                case "cumMsg": {
                    this.sendLocalClientMessage("I.. hope I am satisfying you sir");
                    this.b(SoundsHandler.GIRLS_KOBOLD_SAD[this.getRNG().nextInt(1)]);
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

