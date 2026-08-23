/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4d
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bia;

import java.util.UUID;
import javax.vecmath.Vector4d;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.Packets.SendGirlToSex;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
import com.trolmastercard.sexmod.util.interfaces.IEllie;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class BiaEntity extends Fighter implements IEllie, IBeddableSexGirl {
    //final static int ae = 3;
    public boolean dismounted = false;
    int dismountTicks = 0;
    boolean isWalkingToBed = false;
    int bedWalkTicks = 0;
    boolean isGravityNotInit = true;
    int pickupCooldown = -1;
    boolean supressCameraReset = false;
    final int[] yaws = new int[]{0, 180, -90, 90};
    final Vec3d[][] positions = new Vec3d[][]{{new Vec3d(0.5, 0.0, -0.5), new Vec3d(0.0, 0.0, -1.0)}, {new Vec3d(0.5, 0.0, 1.5), new Vec3d(0.0, 0.0, 1.0)}, {new Vec3d(-0.5, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0)}, {new Vec3d(1.5, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0)}};
    int state = 1;

    public BiaEntity(World world) {
        super(world);
        this.setSize(0.49f, 1.65f);
        this.slashSwordRot = 140;
        this.stabSwordRot = 50;
        this.holdBowRot = 140;
        this.swordOffsetStab = new Vec3d(0.0, -0.029999997854232782, -0.2);
    }

    @Override
    public String getGirlName() {
        return "Bia";
    }

    @Override
    public float getScaleFactor() {
        return -0.2f;
    }

    @Override
    public void SetHome() {
        this.sendChatMessage("I am living here now nya~");
        this.playRandomSound(SoundsHandler.GIRLS_BIA_BREATH);
    }

    @Override
    public void setDismounted() {
        this.dismounted = true;
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (currentAction == Action.ANAL_CUM || currentAction == Action.PRONE_DOGGY_CUM) {
            this.entityDataManager.set(GIRL_HAND_STATES, "");
        }
        if (currentAction != Action.ANAL_CUM || (action != Action.ANAL_FAST && action != Action.ANAL_SLOW)) {
            if (currentAction != Action.PRONE_DOGGY_CUM || (action != Action.PRONE_DOGGY_HARD && action != Action.PRONE_DOGGY_SOFT)) {
                super.setCurrentAction(action);
                //System.out.printf("BiaEntity setCurrentAction: actionInput: %s, currentAction: %s %n", action, this.getCurrentAction());
            }
        }
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.BIA_LOOT_TABLE;
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.isGravityNotInit) {
            this.setNoGravity(false);
            this.noClip = false;
            this.isGravityNotInit = false;
        }
        if (this.dismounted) {
            ++this.dismountTicks;
            if (this.getPositionVector().equals(this.getTargetPosition()) || this.dismountTicks > 40) {
                this.dismounted = false;
                this.dismountTicks = 0;
                this.setYawRotation(this.world.getMinecraftServer().getPlayerList().getPlayerByUUID(this.getInteractionPlayerUUID()).rotationYaw + 180.0f);
                this.entityDataManager.set(IS_ANCHORED, true);
                this.getNavigator().clearPath();
                this.doSubAction();
            } else {
                this.rotationYaw = this.getYawRotation();
                try {
                    TARGET_POS.equals(null);
                } catch (NullPointerException e) {
                    this.setTargetPosition(this.getFrontOffsetVector());
                }
                this.setNoGravity(false);
                Vec3d lerpPlayer = RotationHelper.lerpVec3d(this.getPositionVector(), this.getTargetPosition(), 40 - this.dismountTicks);
                this.setPosition(lerpPlayer.x, lerpPlayer.y, lerpPlayer.z);
            }
        }
        if (this.isWalkingToBed) {
            if (this.getPositionVector().distanceTo(this.getTargetPosition()) < 0.6 || this.bedWalkTicks > 200) {
                this.isWalkingToBed = false;
                this.entityDataManager.set(IS_ANCHORED, true);
                this.bedWalkTicks = 0;
                this.noClip = true;
                this.setNoGravity(true);
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                if ("anal".equals(this.entityDataManager.get(GIRL_HAND_STATES))) {
                    this.setCurrentAction(Action.ANAL_PREPARE);
                    this.setOutfitIndex(0);
                } else {
                    this.setCurrentAction(Action.SITDOWN);
                }
            } else {
                ++this.bedWalkTicks;
                if (this.bedWalkTicks == 60 || this.bedWalkTicks == 120) {
                    this.getNavigator().clearPath();
                    this.getNavigator().tryMoveToXYZ(this.getTargetPosition().x, this.getTargetPosition().y, this.getTargetPosition().z, 0.35);
                }
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        //boolean bl;
        if (super.processInteract(player, hand)) {
            return true;
        }
        if (this.getCurrentAction() == Action.SITDOWNIDLE) {
            return true;
        }

        ItemStack heldItem = player.getHeldItem(hand);
        boolean isNameTag = heldItem.getItem() == Items.NAME_TAG;

        if (isNameTag) {
            heldItem.interactWithEntity(player, this, hand);
            return true;
        }
        if (this.world.isRemote && !this.openInteractionMenu(player)) {
            this.sendChatMessage(I18n.format("bia.dialogue.busy"));
        }
        return true;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        if (this.getInteractionPlayerUUID() == null && (!this.hasMaster() || this.entityDataManager.get(MASTER).equals(Minecraft.getMinecraft().player.getPersistentID().toString()))) {
            String[] options = new String[]{this.entityDataManager.get(OUTFIT_INDEX) == 1 ? "action.names.strip" : "action.names.dressup", "action.names.talk", "action.names.headpat"};
            openInventoryGui(player, this, options, true);
            return true;
        }
        return false;
    }

    void openBiaInventory(EntityPlayer entityPlayer) {
        BiaEntity.openInventoryGui(entityPlayer, this, new String[]{"action.names.anal", "doggy"}, false);
    }

    @Override
    public void AcSomeUnknownClass() {
        if (this.isAnchored() && !this.supressCameraReset) {
            this.resetCameraAndPhysics();
        }
        this.supressCameraReset = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.isControlledByLocalPlayer() && this.getCurrentAction() == Action.PRONE_DOGGY_INTRO && !BlackScreenUI.getActive()) {
            SexUI.showUI();
        }

        this.handleAnalState();
    }

    @Override
    protected void resetLocalPlayerClientState() {
        super.resetLocalPlayerClientState();
        this.pickupCooldown = -1;
    }

    void handleAnalState() {
        float yaw  = this.getYawRotation();
        Action action = this.getCurrentAction();

        if (action == Action.ANAL_WAIT || action == Action.SITDOWNIDLE) {
            EntityPlayer player = this.world.getClosestPlayerToEntity(this, 10.0);
            if (player != null) {
                if (!(player.getDistance(this) > 1.0f)) {
                    if (this.pickupCooldown == -1) {
                        if (this.world.isRemote) {
                            BlackScreenUI.run();
                            HandlePlayerMovement.setMovementLock(false);
                        } else {
                            this.setInteractionPlayerUUID(player.getPersistentID());
                        }
                        this.pickupCooldown = maxAgeInTicks;
                        return;
                    }

                    if (--this.pickupCooldown <= 0) {
                        this.pickupCooldown = -1;
                        player.noClip = true;
                        player.setNoGravity(true);
                        if (action == Action.ANAL_WAIT) {
                            if (!this.world.isRemote) {
                                this.setCurrentAction(Action.ANAL_START);
                                Vec3d pos = this.getTargetPosition().add(VectorMath.rotateByYaw(-0.3, -1.0, -0.5, this.getYawRotation()));
                                player.setPositionAndUpdate(pos.x, pos.y, pos.z);
                            } else if (this.isControlledByLocalPlayer()) {
                                SexUI.showUI();
                            }
                            return;
                        }
                        player.rotationYaw = yaw;
                        player.rotationPitch = 60.0f;
                        if (!this.world.isRemote) {
                            this.setOutfitIndex(0);
                            this.setCurrentAction(Action.PRONE_DOGGY_INTRO);
                            Vec3d targetPos = this.getTargetPosition();
                            Vec3d followPos = targetPos.add(VectorMath.rotateByYaw(0.0, 0.0, 1.0, yaw));
                            this.setTargetPosition(followPos);
                            Vec3d playerPos = targetPos.add(VectorMath.rotateByYaw(0.0, 1.1875 - (double) player.getEyeHeight(), 0.5, yaw));
                            player.setPositionAndUpdate(playerPos.x, playerPos.y, playerPos.z);
                            this.setAnchored(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void resetAnimationControllerTicks() {
        super.resetAnimationControllerTicks();
        if (this.getCurrentAction() == Action.PRONE_DOGGY_HARD) {
            int oldState = this.state;
            do {
                this.state = this.getRNG().nextInt(3) + 1;
            } while (oldState == this.state);
        }
    }

    @Override
    public void reInitTasks() {
        this.aiWander = new EntityAIWanderAvoidWater(this, 0.35);
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.watchClosestGirlGoal);
        this.tasks.addTask(5, this.aiWander);
    }

    @Override
    public void doAction(String action, UUID player) {
        super.doAction(action, player);
        switch (action) {
            case "action.names.talk": {
                this.setInteractionPlayerUUID(Minecraft.getMinecraft().player.getPersistentID());
                this.changeDataParameterFromClient("playerSheHasSexWith", Minecraft.getMinecraft().player.getPersistentID().toString());
                this.changeDataParameterFromClient("animationFollowUp", "talkHorny");
                this.triggerAnalAction(player);
                break;
            }
            case "action.names.headpat": {
                this.setInteractionPlayerUUID(Minecraft.getMinecraft().player.getPersistentID());
                this.changeDataParameterFromClient("playerSheHasSexWith", Minecraft.getMinecraft().player.getPersistentID().toString());
                this.changeDataParameterFromClient("animationFollowUp", "Headpat");
                this.triggerAnalAction(player);
                break;
            }
            case "action.names.anal": {
                this.changeDataParameterFromClient("animationFollowUp", "anal");
                this.setCurrentAction(Action.TALK_RESPONSE);
                this.supressCameraReset = true;
                break;
            }
            case "doggy": {
                this.changeDataParameterFromClient("animationFollowUp", "doggy");
                this.setCurrentAction(Action.TALK_RESPONSE);
                this.supressCameraReset = true;
                break;
            }
            case "action.names.dressup": 
            case "action.names.strip": {
                this.setCurrentAction(Action.STRIP);
            }
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote) {
            EntityItem drop = new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(Blocks.WOOL, this.getRNG().nextInt(4), 12));
            this.world.spawnEntity(drop);
        }
    }

    void triggerAnalAction(UUID uUID) {
        this.triggerActionSync(true, true, uUID);
        HandlePlayerMovement.setMovementLock(false);
    }

    Vector4d getBedVector() {
        BlockPos bedPos = null;
        int attempts = 0;

        while (!this.isValidBed(bedPos)) {
            bedPos = this.findNearestBed(this.getPosition(), attempts);
            if (++attempts == 50) {
                break;
            };
        }

        if (bedPos != null && attempts != 50) {
            this.tasks.removeTask(this.aiWander);
            this.tasks.removeTask(this.watchClosestGirlGoal);
            Vec3d bedVec = new Vec3d(bedPos.getX(), bedPos.getY(), bedPos.getZ());
            int bestIndex = -1;
            for (int i = 0; i < this.positions.length; ++i) {
                Vec3d offsetPos = bedVec.add(this.positions[i][1]);
                Vec3d offsetNeg = bedVec.subtract(this.positions[i][1]);
                Block block = this.world.getBlockState(new BlockPos(offsetPos.x, offsetPos.y, offsetPos.z)).getBlock();
                if (block == Blocks.AIR && WorldUtils.canPlaceStructure(this.world, new BlockPos(offsetNeg))) {
                    if (bestIndex == -1) {
                        bestIndex = i;
                    } else {
                        double bestDist = this.getPosition().distanceSq(bedVec.add(this.positions[bestIndex][0]).x, bedVec.add(this.positions[bestIndex][0]).y, bedVec.add(this.positions[bestIndex][0]).z);
                        double dist = this.getPosition().distanceSq(bedVec.add(this.positions[i][0]).x, bedVec.add(this.positions[i][0]).y, bedVec.add(this.positions[i][0]).z);
                        if (dist < bestDist) {
                            bestIndex = i;
                        }
                    }
                }
            }
            if (bestIndex == -1) {
                this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
                this.sendChatMessage(I18n.format("jenny.dialogue.nobedinsight"));
                return null;
            }
            Vec3d bestBed = bedVec.add(this.positions[bestIndex][0]);
            return new Vector4d(bestBed.x, bestBed.y, bestBed.z, this.yaws[bestIndex]);
        } else {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.sendChatMessage(I18n.format("jenny.dialogue.nobedinsight"));
            return null;
        }
    }

    boolean isValidBed(BlockPos pos) {
        if (pos == null) {
            return false;
        }
        if (WorldUtils.canPlaceStructure(this.world, pos.north()) && this.world.isAirBlock(pos.south())) {
            return true;
        }
        if (WorldUtils.canPlaceStructure(this.world, pos.east()) && this.world.isAirBlock(pos.west())) {
            return true;
        }
        if (WorldUtils.canPlaceStructure(this.world, pos.south()) && this.world.isAirBlock(pos.north())) {
            return true;
        }
        return WorldUtils.canPlaceStructure(this.world, pos.west()) && this.world.isAirBlock(pos.east());
    }

    Vector4d findNearestBedVector() {
        BlockPos bedPos = this.getNearestBed(this.getPosition());
        if (bedPos == null) {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.sendChatMessage(I18n.format("jenny.dialogue.nobedinsight"));
            return null;
        }

        this.tasks.removeTask(this.aiWander);
        this.tasks.removeTask(this.watchClosestGirlGoal);
        Vec3d bedVec = new Vec3d(bedPos.getX(), bedPos.getY(), bedPos.getZ());

        int bestIndex = -1;
        for (int i = 0; i < this.positions.length; ++i) {
            Vec3d offsetPos = bedVec.add(this.positions[i][1]);
            
            if (this.world.getBlockState(new BlockPos(offsetPos.x, offsetPos.y, offsetPos.z)).getBlock() != Blocks.AIR) continue;
            if (bestIndex == -1) {
                bestIndex = i;
            } else {
                double bestDist = this.getPosition().distanceSq(bedVec.add(this.positions[bestIndex][0]).x, bedVec.add(this.positions[bestIndex][0]).y, bedVec.add(this.positions[bestIndex][0]).z);
                double dist = this.getPosition().distanceSq(bedVec.add(this.positions[i][0]).x, bedVec.add(this.positions[i][0]).y, bedVec.add(this.positions[i][0]).z);
                if (dist < bestDist) {
                    bestIndex = i;
                }
            }
        }
        if (bestIndex == -1) {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.sendChatMessage(I18n.format("jenny.dialogue.bedobscured"));
            return null;
        } else {
            Vec3d bedOffset = bedVec.add(this.positions[bestIndex][0]);
            return new Vector4d(bedOffset.x, bedOffset.y, bedOffset.z, this.yaws[bestIndex]);
        }
    }

    @Override
    public void goToSexBed() {
        String stateStr = this.entityDataManager.get(GIRL_HAND_STATES);
        Vector4d bedVec = stateStr.equals("anal") ? this.findNearestBedVector() : this.getBedVector();
        if (bedVec != null) {
            Vec3d vec3d = new Vec3d(bedVec.getX(), bedVec.getY(), bedVec.getZ());
            this.setYawRotation((float) bedVec.getW());
            this.setTargetPosition(vec3d);
            this.cameraYaw = this.getYawRotation();
            this.getNavigator().clearPath();
            this.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.35);
            this.isWalkingToBed = true;
            this.bedWalkTicks = 0;
        }
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
    protected void doSubAction() {
        switch (this.entityDataManager.get(GIRL_HAND_STATES)) {
            case "talkHorny": {
                this.setCurrentAction(Action.TALK_HORNY);
                break;
            }
            case "Headpat": {
                this.setCurrentAction(Action.HEAD_PAT);
                break;
            }
            case "doggy":
            case "anal": {
                this.resetCameraAndPhysics();
                PacketHandler.INSTANCE.sendToServer(new SendGirlToSex(this.girlID()));
                return;
            }
        }
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("animationFollowUp", "");
        } else {
            this.entityDataManager.set(GIRL_HAND_STATES, "");
        }
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
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
                if (this.isRiding()) {
                    this.createAnimation("animation.bia.sit", true, event);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    switch (this.getWalkType()) {
                        case RUN: {
                            this.createAnimation("animation.bia.run", true, event);
                            break;
                        }
                        case FAST_WALK: {
                            this.createAnimation("animation.bia.fastwalk", true, event);
                            break;
                        }
                        case WALK: {
                            this.createAnimation("animation.bia.walk", true, event);
                        }
                    }
                    this.rotationYaw = this.rotationYawHead;
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
                        this.createAnimation("animation.bia.talk_horny2", true, event);
                        break;
                    }
                    case TALK_IDLE: {
                        this.createAnimation("animation.bia.talk_idle2", true, event);
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
                        this.createAnimation("animation.bia.anal_wait", false, event);
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
                        this.createAnimation("animation.bia.prone_doggy_hard" + this.state, true, event);
                        break;
                    }
                    case PRONE_DOGGY_CUM: {
                        this.createAnimation("animation.bia.prone_doggy_cum", true, event);
                        break;
                    }
                    case WAVE_IDLE: {
                        this.createAnimation("animation.bia.wave_idle", true, event);
                        break;
                    }
                    case WAVE: {
                        this.createAnimation("animation.bia.wave", true, event);
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
        AnimationController.ISoundListener soundListener = sound -> {
            switch (sound.sound) {
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "becomeNude": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.changeDataParameterFromClient("currentModel", this.entityDataManager.get(OUTFIT_INDEX) == 1 ? "0" : "1");
                    break;
                }
                case "stripDone": {
                        this.resetCameraAndPhysics();
                        this.doSubAction();
                    break;
                }
                case "stripMSG1": {
                    this.sendChatMessage(I18n.format("bia.dialogue.hihi"));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_BIA_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "pearl": {
                    PacketHandler.INSTANCE.sendToServer(new SendCompanionHome(this.girlID()));
                    break;
                }
                case "talk_hornyMSG1": {
                    this.sendChatMessage(I18n.format("bia.dialogue.heya"));
                    this.playRandomSound(SoundsHandler.GIRLS_BIA_HEY);
                    break;
                }
                case "talk_hornyMSG2": {
                    this.sendChatMessage(I18n.format("bia.dialogue.horny"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[2]);
                    break;
                }
                case "talk_hornyMSG3": {
                    this.sendChatMessage(I18n.format("bia.dialogue.so"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "talk_hornyMSG4": {
                    this.sendChatMessage(I18n.format("bia.dialogue.fun"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "talk_hornyDone": {
                    this.setCurrentAction(Action.TALK_IDLE);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.openBiaInventory(Minecraft.getMinecraft().player);
                    break;
                }
                case "talk_responseMSG1": {
                    this.sendChatMessage(I18n.format("bia.dialogue.huh"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[2]);
                    break;
                }
                case "talk_responseMSG2": {
                    this.sendChatMessage(I18n.format("bia.dialogue.iuhm"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[1]);
                    break;
                }
                case "talk_responseMSG3": {
                    this.sendChatMessage(I18n.format("bia.dialogue.yes"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[0]);
                    break;
                }
                case "talk_responseDone": {
                    if (this.isControlledByLocalPlayer()) {
                        this.resetGirlState();
                    }
                    this.doSubAction();
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
                }
                case "anal_slowMSG1": 
                case "anal_startMSG2": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.5f);
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
                case "blackScreen": 
                case "anal_cumBlackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "doggy_cumDone": 
                case "anal_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "headpatMSG1": {
                    this.sendChatMessage(I18n.format("bia.dialogue.headpats"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "headpatMSG2": {
                    this.sendChatMessage(I18n.format("bia.dialogue.hmm"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[0]);
                    break;
                }
                case "headpatMSG3": {
                    this.sendChatMessage(I18n.format("bia.dialogue.huh2"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "headpatMSG4": {
                    this.sendChatMessage(I18n.format("bia.dialogue.thankyou"));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[1]);
                    break;
                }
                case "headpatDone": {
                    this.resetCameraAndPhysics();
                    break;
                }
                case "sitdownMSG1": {
                    this.sendChatMessage("come here big boy~");
                    this.playRandomSound(SoundsHandler.GIRLS_BIA_BREATH);
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
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "doggyMoan": {
                    this.playRandomSound(this.getRNG().nextBoolean() ? SoundsHandler.GIRLS_BIA_AHH : SoundsHandler.GIRLS_BIA_MMM);
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
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_INSERTS, 6.0f);
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
            }
        };
        this.actionController.registerSoundListener(soundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

