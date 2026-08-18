/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Ellie;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IEllie;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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

public class EllieEntity extends Fighter implements IEllie {
    final static float ad = 10.0f;
    final static int ao = 16;
    final static int ap = 79;
    final static int ag = 109;
    final static int as = 150;
    final static int ar = 20;
    final static int ab = 110;
    final static int an = 4;
    int ak = -1;
    boolean aq = false;
    boolean ae = false;
    boolean ac = false;
    int af = -1;
    int yFlag = -1;
    int al = -1;
    int ai = -1;
    boolean ah = false;
    Object[] am;
    int zFlag = -1;
    int state = 1;
    boolean aj = false;

    public EllieEntity(World world) {
        super(world);
        this.slashSwordRot = -85;
        this.stabSwordRot = -175;
        this.holdBowRot = -85;
        this.swordOffsetStab = new Vec3d(-0.1, 0.05, 0.0);
    }

    @Override
    public void SetHome() {
        this.sendLocalClientMessage("Okay, I will be residing here then..");
        this.PlaySound(SoundsHandler.GIRLS_ELLIE_HUH[0], 6.0f);
    }

    @Override
    public String getGirlName() {
        return "Ellie";
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.ELLIE_LOOT_TABLE;
    }

    boolean isBedBlocked() {
        if (this.isLocallyRegistered()) {
            return false;
        }
        return this.world.getBlockState(this.getPosition().add(0, 2, 0)).getBlock() != Blocks.AIR;
    }

    @Override
    public float getEyeHeight() {
        return this.isBedBlocked() ? 1.53f : 1.9f;
    }

    @Override
    public float getScaleFactor() {
        return 0.4f;
    }

    @Override
    public void setDismounted() {
        UUID uUID = this.getInteractionPlayerUUID();
        if (uUID == null) {
            this.resetSitScale();
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            this.resetSitScale();
            return;
        }
        float yaw = entityPlayer.rotationYaw - 180.0f;
        this.setYawRotation(yaw);
        this.setCurrentAction(Action.CARRY_INTRO);
        this.setAnchored(true);
    }

    @Override
    public boolean shouldRenderNameTag() {
        return this.getCurrentAction() != Action.CARRY_INTRO;
    }

    public boolean canJoinPlayer(EntityPlayer entityPlayer, boolean bl) {
        if (bl) {
            EllieEntity.openInventoryGui(entityPlayer, this, new String[]{"action.names.cowgirl", "action.names.missionary"}, false);
            return true;
        }
        if ((Integer)this.entityDataManager.get(OUTFIT_INDEX) == 0) {
            EllieEntity.openInventoryGui(entityPlayer, this, new String[]{"action.names.dressup"}, true);
            return true;
        }
        EllieEntity.openInventoryGui(entityPlayer, this, new String[]{"Face fuck"}, true);
        return true;
    }

    @Override
    public void goHome() {
        super.goHome();
        this.sendLocalClientMessage("stay safe darling~");
        this.PlaySound(SoundsHandler.GIRLS_ELLIE_SIGH[1], 6.0f);
    }

    @Override
    public void doAction(String string, UUID player) {
        super.doAction(string, player);
        this.aq = true;
        switch (string) {
            case "action.names.missionary": {
                this.setCurrentAction(Action.HUGSELECTED);
                this.changeDataParameterFromClient("animationFollowUp", "Missionary");
                break;
            }
            case "action.names.cowgirl": {
                this.setCurrentAction(Action.HUGSELECTED);
                this.changeDataParameterFromClient("animationFollowUp", "cowgirl");
                break;
            }
            case "action.names.dressup": 
            case "action.names.strip": {
                this.setCurrentAction(Action.STRIP);
                this.changeDataParameterFromClient("animationFollowUp", "");
                break;
            }
            case "Face fuck": {
                this.triggerActionSync(true, true, player);
                HandlePlayerMovement.setMovementLock(false);
            }
        }
    }

    @Override
    protected void alignPlayerToGirl(EntityPlayerMP player, boolean force) {
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (action == Action.HUGSELECTED && !this.world.isRemote) {
            this.ai = 79;
        }
        if (currentAction == Action.MISSIONARY_CUM && (action == Action.MISSIONARY_FAST || action == Action.MISSIONARY_SLOW)) {
            return;
        }
        if (currentAction == Action.COWGIRLCUM && (action == Action.COWGIRLSLOW || action == Action.COWGIRLFAST)) {
            return;
        }
        if (currentAction == Action.CARRY_CUM && (action == Action.CARRY_SLOW || action == Action.CARRY_FAST)) {
            return;
        }
        if (action == Action.CARRY_INTRO) {
            this.ak = 0;
        }
        super.setCurrentAction(action);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void onUpdate() {
        super.onUpdate();
        if (this.ae) {
            this.canJoinPlayer(Minecraft.getMinecraft().player, true);
            this.ae = false;
        }
        this.handleSitIdle();
        this.showHornyMeter();
    }

    void showHornyMeter() {
        if (SexUI.getShouldBeRendered()) {
            return;
        }
        if (this.getCurrentAction() != Action.CARRY_SLOW) {
            return;
        }
        SexUI.showUI();
    }

    void handleSitTimer() {
        if (this.ak == -1) {
            return;
        }
        if (++this.ak < 110) {
            return;
        }
        this.ak = -1;
        if (this.getCurrentAction() != Action.CARRY_INTRO) {
            return;
        }
        UUID uUID = this.getInteractionPlayerUUID();
        if (uUID == null) {
            return;
        }
        EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
        if (player == null) {
            return;
        }
        float yaw = this.getYawRotation();
        Vec3d pos = this.getTargetPosition().add(VectorMath.rotate(new Vec3d(0.0, 2.5625f - player.getEyeHeight(), -0.3125), 180.0f + yaw));
        player.setPositionAndUpdate(pos.x, pos.y, pos.z);
    }

    void handleSitIdle() {
        if (this.getCurrentAction() != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        if (this.getDistance(entityPlayer) > 1.5f) {
            return;
        }
        if (entityPlayer.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            BlackScreenUI.run();
        }
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.setFirstSit();
        this.handleHornyPotion();
        this.handleSitUpFinish();
        this.handleSitUpTimer();
        this.handleSitDownTimer();
        this.handleHugTimer();
        this.handleSitTransition();
        this.handleStandTimer();
    }

    void setFirstSit() {
        if (!this.ac) {
            this.ac = true;
            this.noClip = false;
            this.setNoGravity(false);
        }
    }

    @Override
    protected void U() {
        Vec3d vec3d;
        Vec3d pos;
//        EntityPlayer player;
        UUID uUID;
        String handState = (String)this.entityDataManager.get(GIRL_HAND_STATES);
        if ("Missionary".equals(handState)) {
            this.entityDataManager.set(OUTFIT_INDEX, 0);
            this.setCurrentAction(Action.MISSIONARY_START);
            uUID = this.getInteractionPlayerUUID();
            if (uUID == null) {
                return;
            }

            EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
            if (player == null) {
                this.resetCameraAndPhysics();
                return;
            }

            player.setNoGravity(true);
            player.noClip = true;
            pos = this.getTargetPosition();
            player.rotationYaw = this.getYawRotation();
            vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, 0.1), player.rotationYaw);
            pos = pos.add(vec3d);
            player.setPositionAndUpdate(pos.x, pos.y, pos.z);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)player);
        }
        if ("cowgirl".equals(handState)) {
            this.entityDataManager.set(OUTFIT_INDEX, 0);
            this.setCurrentAction(Action.COWGIRLSTART);
            uUID = this.getInteractionPlayerUUID();
            if (uUID == null) {
                return;
            }
            EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
            if (player == null) {
                this.resetCameraAndPhysics();
                return;
            }
            player.setNoGravity(true);
            player.noClip = true;
            pos = this.getTargetPosition();
            player.rotationYaw = this.getYawRotation() + 180.0f;
            vec3d = VectorMath.rotate(new Vec3d(0.0, 1.0 - (double)player.eyeHeight, -1.8125), player.rotationYaw);
            pos = pos.add(vec3d);
            player.setPositionAndUpdate(pos.x, pos.y, pos.z);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)player);
        }
    }

    void handleStandTimer() {
        if (--this.af == 0) {
            this.U();
        }
    }

    void handleSitTransition() {
        if (this.getCurrentAction() == Action.SITDOWNIDLE && this.af < 0) {
            EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
            if (entityPlayer != null) {
                if (!(this.getDistance(entityPlayer) > 1.5f)) {
                    this.af = 20;
                    this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
                }
            }
        }
    }

    void handleHugTimer() {
        if (--this.yFlag == 0) {
            this.setCurrentAction(Action.HUGIDLE);
        }
    }

    void handleSitDownTimer() {
        if (--this.al == 0) {
            this.setCurrentAction(Action.SITDOWNIDLE);
        }
    }

    void handleSitUpTimer() {
        if (--this.ai == 0 || this.ah) {
            this.ah = true;
            this.entityDataManager.set(IS_ANCHORED, false);
            this.setCurrentAction(Action.NULL);
            this.noClip = false;
            this.setNoGravity(false);
            if (this.am == null) {
                this.am = this.getRandomSitPose();
            }

            if (this.am == null) {
                this.sendGirlChatMessage("no bed in sight...");
                this.world.playSound(null, this.getPosition(), SoundsHandler.GIRLS_ELLIE_SIGH[0], SoundCategory.NEUTRAL, 6.0f, 1.0f);
                this.resetGirlState();
                this.resetSitScale();
                return;
            }else {
                EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
                if (player != null) {
                    player.setNoGravity(false);
                    player.noClip = false;
                }
                Vec3d sitPos = (Vec3d) this.am[0];
                int yaw = (Integer) this.am[1];
                if (sitPos.distanceTo(this.getPositionVector()) > 1.0) {
                    this.getNavigator().tryMoveToXYZ(sitPos.x, sitPos.y, sitPos.z, 0.35f);
                    this.applyCustomPathNodeVelocity();
                    return;
                } else {
                    this.setTargetPosition(sitPos);
                    this.setYawRotation(yaw);
                    this.setCurrentAction(Action.SITDOWN);
                    this.entityDataManager.set(IS_ANCHORED, true);
                    this.al = 109;
                    this.noClip = true;
                    this.setNoGravity(true);
                    this.ah = false;
                    this.am = null;
                }
            }
        }
    }

    @Override
    public void ResetNPCTasks() {
        super.ResetNPCTasks();
        this.yFlag = -1;
    }

    Object[] getRandomSitPose() {
        Vec3d bedVec;
        BlockPos bedPos;
        int bestIndex = -1;
        int attempts = 0;

        Vec3d[][] offsets = new Vec3d[][]{
                {new Vec3d(0.5, 0.0, -0.18), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0)},
                {new Vec3d(0.5, 0.0, 1.18), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0)},
                {new Vec3d(-0.18, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0)},
                {new Vec3d(1.18, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0)}
        };

        int[] yaws = new int[]{0, 180, -90, 90};
        do {
            if ((bedPos = this.findNearestBed(this.getPosition(), ++attempts)) == null) {
                return null;
            }

            bedVec = new Vec3d(bedPos.getX(), bedPos.getY(), ((Vec3i)bedPos).getZ());
            for (int i = 0; i < offsets.length; ++i) {
                Vec3d offsetVec = bedVec.add(offsets[i][1]);
                Block block = this.world.getBlockState(new BlockPos(offsetVec.x, offsetVec.y, offsetVec.z)).getBlock();
                Vec3d headVec = bedVec.add(offsets[i][2]);
                Block headBlock = this.world.getBlockState(new BlockPos(headVec.x, headVec.y, headVec.z)).getBlock();
                if (block == Blocks.AIR && headBlock == Blocks.BED) {
                    if (bestIndex == -1) {
                        bestIndex = i;
                        continue;
                    }
                    double bestDist = this.getPosition().distanceSq(bedVec.add((Vec3d) offsets[bestIndex][0]).x, bedVec.add((Vec3d) offsets[bestIndex][0]).y, bedVec.add((Vec3d) offsets[bestIndex][0]).z);
                    double dist = this.getPosition().distanceSq(bedVec.add((Vec3d) offsets[i][0]).x, bedVec.add((Vec3d) offsets[i][0]).y, bedVec.add((Vec3d) offsets[i][0]).z);
                    if (dist < bestDist) {
                        bestIndex = i;
                    }
                }
            }
        } while (bestIndex == -1);

        Vec3d sitOffset = bedVec.add(offsets[bestIndex][0]);
        return new Object[]{sitOffset, yaws[bestIndex]};
    }

    void handleHornyPotion() {
        if (this.getActivePotionEffect(HornyPotion.HORNY_POTION) != null) {
            EntityPlayer player = this.world.getClosestPlayerToEntity(this, 10.0);
            if (player != null) {
                this.removeActivePotionEffect(HornyPotion.HORNY_POTION);
                this.setInteractionPlayerUUID(player.getPersistentID());
                float yaw = (float) (Math.atan2(this.posZ - player.posZ, this.posX - player.posX) * 57.29577951308232);
                this.setYawRotation(yaw);
                this.setTargetPosition(this.getPositionVector());
                this.entityDataManager.set(IS_ANCHORED, true);
                this.setCurrentAction(Action.DASH);
                this.zFlag = 16;
                this.setNoGravity(true);
                this.noClip = true;
                PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(false), (EntityPlayerMP) player);
                this.tasks.removeTask(this.aiWander);
                this.tasks.removeTask(this.watchClosestGirlGoal);
            }
        }
    }

    void handleSitUpFinish() {
        if (--this.zFlag == 0) {
            UUID uUID = this.getInteractionPlayerUUID();
            if (uUID == null) {
                this.resetSitScale();
            } else {
                EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
                if (entityPlayer == null) {
                    this.resetSitScale();
                } else {
                    entityPlayer.setNoGravity(true);
                    entityPlayer.noClip = true;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, -0.5), entityPlayer.rotationYaw);
                    Vec3d vec3d2 = vec3d.add(entityPlayer.getPositionVector());
                    this.setTargetPosition(vec3d2);
                    this.setYawRotation(entityPlayer.rotationYaw);
                    this.setCurrentAction(Action.HUG);
                    this.yFlag = 150;
                }
            }
        }
    }

    void resetSitScale() {
        this.entityDataManager.set(IS_ANCHORED, false);
        this.setCurrentAction(Action.NULL);
        this.setInteractionPlayerUUID((UUID)null);
        this.noClip = false;
        this.setNoGravity(false);
        this.ah = false;
        this.yFlag = -1;
        this.zFlag = -1;
        this.ai = -1;
        this.am = null;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (EllieEntity.getActiveSceneInfo(player) != null) {
            return false;
        }
        if (this.getInteractionPlayerUUID() != null) {
            return false;
        }
        if (this.world.isRemote) {
            this.canJoinPlayer(player, false);
        }
        return true;
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() == Action.NULL && this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.ellie.eyes", true, event);
                } else {
                    this.createAnimation("animation.ellie.null", true, event);
                }
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.ellie.null", true, event);
                } else {
                    double moved = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                    if (moved == 0.0) {
                        this.createAnimation(this.isBedBlocked() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, event);
                    }
                    if (this.isBedBlocked()) {
                        this.createAnimation("animation.ellie.crouchwalk", true, event);
                    }
                    switch (this.getWalkType()) {
                        case RUN: {
                            this.createAnimation("animation.ellie.run", true, event);
                            return PlayState.CONTINUE;
                        }
                        case FAST_WALK: {
                            this.createAnimation("animation.ellie.fastwalk", true, event);
                            return PlayState.CONTINUE;
                        }
                        case WALK: {
                            this.createAnimation("animation.ellie.walk", true, event);
                        }
                    }
                }
                break;
            }
            case "action":
                switch (this.getCurrentAction()) {
                    case NULL:
                        this.createAnimation("animation.ellie.null", true, event);
                        break;
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
                        break ;
                    }
                    case HUGIDLE: {
                        this.createAnimation("animation.ellie.hugidle", true, event);
                        break ;
                    }
                    case HUGSELECTED: {
                        this.createAnimation("animation.ellie.hugselected", false, event);
                        break ;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.ellie.sitdown", false, event);
                        break ;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.ellie.sitdownidle", true, event);
                        break ;
                    }
                    case COWGIRLSTART: {
                        this.createAnimation("animation.ellie.cowgirlstart", false, event);
                        break ;
                    }
                    case COWGIRLSLOW: {
                        this.createAnimation("animation.ellie.cowgirlslow2", true, event);
                        break ;
                    }
                    case COWGIRLFAST: {
                        this.createAnimation("animation.ellie.cowgirlfast", true, event);
                        break ;
                    }
                    case COWGIRLCUM: {
                        this.createAnimation("animation.ellie.cowgirlcum", true, event);
                        break ;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.ellie.attack" + this.nextAttack, false, event);
                        break ;
                    }
                    case BOW: {
                        this.createAnimation("animation.ellie.bowcharge", false, event);
                        break ;
                    }
                    case RIDE: {
                        this.createAnimation("animation.ellie.ride", true, event);
                        break ;
                    }
                    case SIT: {
                        this.createAnimation("animation.ellie.sit", true, event);
                        break ;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.ellie.throwpearl", false, event);
                        break ;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.ellie.downed", true, event);
                        break ;
                    }
                    case MISSIONARY_START: {
                        this.createAnimation("animation.ellie.missionary_start", false, event);
                        break ;
                    }
                    case MISSIONARY_SLOW: {
                        this.createAnimation("animation.ellie.missionary_slow", true, event);
                        break ;
                    }
                    case MISSIONARY_FAST: {
                        this.createAnimation("animation.ellie.missionary_fast", true, event);
                        break ;
                    }
                    case MISSIONARY_CUM: {
                        this.createAnimation("animation.ellie.missionary_cum", false, event);
                        break ;
                    }
                    case CARRY_INTRO: {
                        this.createAnimation("animation.ellie.carry_intro", false, event);
                        break ;
                    }
                    case CARRY_SLOW: {
                        this.createAnimation("animation.ellie.carry_slow" + this.state, true, event);
                        break ;
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
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener soundListener = sound -> {
            switch (sound.sound) {
                case "becomeNude": {
                    if (this.getClosestPlayerID()) {
                        this.changeDataParameterFromClient("currentModel", (Integer) this.entityDataManager.get(OUTFIT_INDEX) == 1 ? "0" : "1");
                        break;
                    }
                    break;
                }
                case "stripDone": {
                    this.setCurrentAction((Action)null);
                    this.resetCameraAndPhysics();
                    this.U();
                    break;
                }
                case "hugMSG2": {
                    this.sendGirlChatMessage("Hmm...");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HMPH[3], 6.0f);
                    break;
                }
                case "hugMSG3": {
                    this.sendGirlChatMessage("Hey!");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HUH[1], 1.0f);
                    break;
                }
                case "hugMSG4": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.mommyhorny", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_MOMMYHORNY, 0.5f);
                    break;
                }
                case "hugMSG5": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.whattodo", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HUH[1], 6.0f);
                    break;
                }
                case "hugDone": {
                    if (this.isControlledByLocalPlayer()) {
                        this.canJoinPlayer(Minecraft.getMinecraft().player, true);
                        break;
                    }
                    break;
                }
                case "hugselectedMSG1": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.iknow", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HMPH[3], 6.0f);
                    break;
                }
                case "hugselectedMSG2": {
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.followmedarling", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    if (!this.isControlledByLocalPlayer()) break;
                    HandlePlayerMovement.setMovementLock(true);
                    break;
                }
                case "sitdownMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_COMETOMOMMY, 0.5f);
                    if (!this.getClosestPlayerID()) break;
                    this.sendGirlChatMessage(I18n.format("ellie.dialogue.cometomommy", new Object[0]));
                    break;
                }
                case "cowgirlStartMSG0": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 6.0f);
                    break;
                }
                case "cowgirlStartMSG1": {
                    if (!this.getClosestPlayerID()) break;
                    this.sendLocalClientMessage(I18n.format("ellie.dialogue.like", new Object[0]));
                    SexUI.resetCumPercentage();
                    break;
                }
                case "cowgirlStartMSG2": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
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
                    if (this.aj) {
                        this.aj = false;
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "cowgirlfastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRLSLOW);
                    break;
                }
                case "cowgirlfastdomMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.2);
                    break;
                }
                case "cowgirlcumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_MOAN[5], 3.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG3": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG4": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.hide();
                    break;
                }
                case "cowgirlcumMSG5": 
                case "missionary_cumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GOODBOY, 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.sendLocalClientMessage(I18n.format("ellie.dialogue.goodboy", new Object[0]));
                    break;
                }
                case "cowgirlcumMSG6": 
                case "blackScreen": {
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
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "pearl": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "openSexUi": {
                    if (!this.getClosestPlayerID()) break;
                    SexUI.showUI();
                    break;
                }
                case "missionary_slowMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() && this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_MOAN), 6.0f);
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "missionary_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() || this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_MOAN), 6.0f);
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.05);
                    break;
                }
                case "missionary_startDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    SexUI.showUI();
                    break;
                }
                case "missionary_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
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
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    break;
                }
                case "carry_introMSG1": {
                    this.sendLocalClientMessage("I'm hungry..");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HMPH, 6.0f);
                    break;
                }
                case "carry_introMSG2": {
                    this.sendLocalClientMessage("heh~");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    break;
                }
                case "lipsound": {
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIPSOUND, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "carry_slowDone": {
                    int oldstate = this.state;
                    do {
                        this.state = this.getRNG().nextInt(4) + 1;
                    } while (this.state == oldstate);
                    break;
                }
                case "carry_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CARRY_SLOW);
                }
            }
        };
        this.actionController.registerSoundListener(soundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

//    private static RuntimeException ZelixClassMaster(RuntimeException zelixClassMaster) {
//        return zelixClassMaster;
//    }
}

