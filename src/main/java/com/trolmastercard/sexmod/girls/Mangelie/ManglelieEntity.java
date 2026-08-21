/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.event.entity.ProjectileImpactEvent$Arrow
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.girls.Mangelie;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.companion.AvoidPlayerGoal;
import com.trolmastercard.sexmod.girls.Galath.*;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.world.GirlWorldData;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ManglelieEntity
extends GirlEntity {
    final static public String NBT_MOMMY_KEY = "sexmod:mommy";
    final static public float MAX_ATTACK_DISTANCE = 60.0f;
    final static public float ARROW_SPEED = 4.0f;
    final static public float MOMMY_HEAD_OFFSET_Y = 3.5f;
    final static public float ah = 28.0f;
    final static public float ae = 15.0f;
    final static public float K = 15.0f;
    final static public float L = 0.65f;
    final static public float ao = 3.65f;
    final static public float O = 6.0f;
    final static public float ak = 80.0f;
    final static public float X = 700.0f;
    final static public DataParameter<String> MOMMY_UUID_DATA = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.STRING).getSerializer().createKey(111);
    final static public DataParameter<Boolean> IS_RIDING_MOMMY = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(112);
    final static public DataParameter<Integer> TARGET_ENTITY_ID = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.VARINT).getSerializer().createKey(113);
    final static public DataParameter<String> ATTACK_START_TIME = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.STRING).getSerializer().createKey(114);
    final static public DataParameter<Boolean> IS_SCARED = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(115);
    private UUID pendingMommyUUID = null;

    public boolean isTargetHandRight = true;
    public Vec3d ikTargetPos = Vec3d.ZERO;
    public float ikProgress = 0.0f;

    boolean isWild = true;
    boolean isDespawnedLocal = false;
    boolean hasFiredArrow = false;
    public float targetHeadYaw = 0.0f;
    public float targetHeadPitch = 0.0f;
    public float T = 0.0f;
    public float ai = 0.0f;
    boolean isDespawned = false;
    boolean customModelLoaded = false;
    boolean isThreesomeTransitioning = false;
    boolean isThreesomeHard = false;
    boolean isThreesomeSlowBack = false;
    public int cockStage = 2;

    public ManglelieEntity(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(MOMMY_UUID_DATA, "");
        this.entityDataManager.register(IS_RIDING_MOMMY, false);
        this.entityDataManager.register(TARGET_ENTITY_ID, -1);
        this.entityDataManager.register(ATTACK_START_TIME, "");
        this.entityDataManager.register(IS_SCARED, false);
    }

    @Override
    public String getGirlName() {
        return "Manglelie";
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new AvoidPlayerGoal(this, 20.0f, 1.0, 1.2));
    }

    @Override
    public float getScaleFactor() {
        return 0.0f;
    }

    public void setCorrupting(boolean riding) {
        this.entityDataManager.set(IS_RIDING_MOMMY, riding);
    }

    public boolean isAttachedToMommy() {
        return this.entityDataManager.get(IS_RIDING_MOMMY);
    }

    @Nullable
    public UUID getCorruptPlayerUUID() {
        String uuidStr = this.entityDataManager.get(MOMMY_UUID_DATA);
        if (uuidStr.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(uuidStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean shouldRenderNameTag() {
        return !this.isAttachedToMommy();
    }

    @Nullable
    public GalathEntity getMommyGalath(boolean isServer) {
        //GirlEntity girl;
        UUID uUID = this.getCorruptPlayerUUID();
        if (uUID == null) {
            return null;
        }
        GirlEntity girl = isServer ? GirlEntity.getServerGirlEntity(uUID) : GirlEntity.getClientGirlEntity(uUID);
        if (!(girl instanceof GalathEntity)) {
            return null;
        }
        return (GalathEntity)girl;
    }

    public void setMommyUUID(@Nullable UUID uUID) {
        if (uUID == null) {
            this.entityDataManager.set(MOMMY_UUID_DATA, "");
            return;
        }
        this.entityDataManager.set(MOMMY_UUID_DATA, uUID.toString());
    }

    @Override
    public Float getYawRotation() {
        float yaw = super.getYawRotation();
        if (ManglelieModel.isThreesomeAction(this)) {
            yaw += 180.0f;
        }
        return yaw;
    }

    public void markDespawned() {
        this.isDespawnedLocal = true;
    }

    @Override
    public void updateAITasks() {
        if (this.isDespawned) {
            this.world.removeEntity(this);
            return;
        }
        this.initCustomModel();
        this.updatePositionSyncWithMommy();
        super.updateAITasks();
        this.processPendingMommyLink();
        this.updateNavigationToGalath();
        this.validateCombatTarget();
        this.updateTargetTimeout();
        this.findCombatTarget();
        this.updatePhysicsState();
        this.updateArrowAttack();
        this.checkMommyDisown();
        this.validateWildStatus();
    }

    void validateWildStatus() {
        if (this.getCorruptPlayerUUID() != null) {
            this.isWild = false;
        }
        if (this.isWild) {
            return;
        }
        if (this.getMommyGalath(true) == null) {
            System.out.println("removed non-wild mang for lack of mommy");
            this.world.removeEntity(this);
        }
    }

    void checkMommyDisown() {
        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            return;
        }
        if (galath.getManglelieUUID() == null) {
            return;
        }
        if (this.girlID().equals(galath.getManglelieUUID())) {
            return;
        }
        System.out.println("removed non-wild mang cuz her mommy disowned her and got another mang");
        this.world.removeEntity(this);
    }

    public static GalathEntity getMommyFromEntity(GirlEntity girl, boolean isServer) {
        if (!(girl instanceof ManglelieEntity)) {
            return null;
        }
        return ((ManglelieEntity)girl).getMommyGalath(isServer);
    }

    public long getAttackStartTime() {
        String timeStr = this.entityDataManager.get(ATTACK_START_TIME);
        if (timeStr.isEmpty()) {
            return -1L;
        }
        try {
            return Long.parseLong(timeStr);
        } catch (Exception e) {
            return -1L;
        }
    }

    public void setAttackStartTime(long worldTime) {
        this.entityDataManager.set(ATTACK_START_TIME, Long.toString(worldTime));
        this.hasFiredArrow = false;
    }

    void updateArrowAttack() {
        long startTime = this.getAttackStartTime();
        if (startTime == -1L) {
            return;
        }
        long currentTime = this.world.getTotalWorldTime();
        if ((float)currentTime < 28.0f + (float)startTime) {
            return;
        }
        if (this.hasFiredArrow) {
            return;
        }

        Entity target = this.getTargetEntity();
        if (target == null) {
            return;
        }
        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            return;
        }

        EntityTippedArrow arrow = new EntityTippedArrow(this.world, this);
        Vec3d spawnPos = galath.getPositionVector().add(0.0, MOMMY_HEAD_OFFSET_Y, 0.0);
        arrow.setPositionAndUpdate(spawnPos.x, spawnPos.y, spawnPos.z);

        Vec3d targetPos = target.getPositionVector();
        Vec3d direction = targetPos.subtract(spawnPos).normalize();

        arrow.motionX = direction.x * 4.0;
        arrow.motionY = direction.y * 4.0;
        arrow.motionZ = direction.z * 4.0;
        GirlEntity.girlPlaySound((GirlEntity)galath, SoundEvents.ENTITY_ARROW_SHOOT, true);
        this.world.spawnEntity(arrow);
        this.hasFiredArrow = true;
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect) {
        //do nothing, she is immune
    }

    void updatePhysicsState() {
        boolean attached = this.getCorruptPlayerUUID() != null;
        this.setNoGravity(attached);
        this.noClip = attached;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.getCorruptPlayerUUID() == null;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public Vec3d renderCustomModelTransform(Minecraft mc, CustomModelEntity entity, EntityLivingBase renderEntity, float partialTicks) {
        if (this.isLocallyRegistered()) {
            return super.renderCustomModelTransform(mc, entity, renderEntity, partialTicks);
        }
        if (!this.isAttachedToMommy()) {
            return super.renderCustomModelTransform(mc, entity, renderEntity, partialTicks);
        }

        GalathEntity galath = this.getMommyGalath(false);
        if (galath == null) {
            return super.renderCustomModelTransform(mc, entity, renderEntity, partialTicks);
        }
        ManglelieRenderer.setupModelPosition(galath, partialTicks, entity);
        return ManglelieRenderer.getMommyHeadOffset(galath, partialTicks);
    }

    public float getAttackProgress(float partialTicks) {
        long startTime = this.getAttackStartTime();
        if (startTime == -1L) {
            return 0.0f;
        }
        long currentTime = this.world.getTotalWorldTime();
        float elapsed = currentTime - startTime;
        return (elapsed + partialTicks) / 28.0f;
    }

    @Nullable
    public Entity getTargetEntity() {
        int id = this.entityDataManager.get(TARGET_ENTITY_ID);
        if (id == -1) {
            return null;
        }
        return this.world.getEntityByID(id);
    }

    void setTargetEntityId(int entityID) {
        this.entityDataManager.set(TARGET_ENTITY_ID, entityID);
        this.setAttackStartTime(entityID == -1 ? -1L : this.world.getTotalWorldTime());
    }

    void validateCombatTarget() {
        Entity target = this.getTargetEntity();
        if (target == null) {
            return;
        }

        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            this.setTargetEntityId(-1);
            return;
        }
        if (!this.isAttachedToMommy()) {
            this.setTargetEntityId(-1);
            return;
        }
        if (ManglelieEntity.isInvalidTarget(target, galath)) {
            this.setTargetEntityId(-1);
        }
    }

    public static boolean isInvalidTarget(Entity target, GalathEntity galath) {
        if (target.isDead) {
            return true;
        }
        if (target.dimension != galath.dimension) {
            return true;
        }
        if (!GalathMobTarget.isValidTarget(target)) {
            return true;
        }
        if (!GalathMobTarget.hasLineOfSight(galath.world, galath.getTargetPosition().add(0.0, galath.getEyeHeight(), 0.0), target)) {
            return true;
        }

        Vec3d diff = target.getPositionVector().subtract(galath.getPositionVector());
        if (diff.x * diff.x + diff.z * diff.z > 225.0) {
            return true;
        }

        Float headYaw = GalathEntity.getAimYaw(galath, 0.0f);
        float yaw = headYaw == null ? galath.rotationYawHead : headYaw;
        Vec3d vec3d2 = VectorMath.rotateByYaw(diff, yaw);
        return vec3d2.z < 0.0;
    }

    void findCombatTarget() {
        if (this.getAnimationProcessor() != null) {
            return;
        }
        if (!this.isAttachedToMommy()) {
            return;
        }

        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            return;
        }
        if (galath.getInteractionPlayerUUID() != null) {
            return;
        }
        if (galath.getCurrentAction() == Action.MASTERBATE) {
            return;
        }

        BlockPos pos = galath.getPosition();
        BlockPos radius = new BlockPos(15.0, 15.0, 15.0);
        List<EntityMob> mobs = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(pos.add(radius), pos.subtract(radius)));
        for (EntityMob mob : mobs) {
            if (ManglelieEntity.isInvalidTarget(mob, galath)) continue;
            this.setTargetEntityId(mob.getEntityId());
            return;
        } //probably it was while cycle, but cfr did it foreach

    }

    void updateTargetTimeout() {
        Entity target = this.getTargetEntity();
        if (target == null) {
            return;
        }

        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            return;
        }

        long startTime = this.getAttackStartTime();
        if (startTime == -1L) {
            return;
        }

        long currentTime = this.world.getTotalWorldTime();
        long deltaTime = currentTime - this.getAttackStartTime();

        if ((float)deltaTime < 60.0f) {
            return;
        }

        this.hasFiredArrow = false;
        this.setTargetEntityId(-1);
    }

    void processPendingMommyLink() {
        if (this.pendingMommyUUID == null) {
            return;
        }

        GirlEntity girl = GirlEntity.getServerGirlEntity(this.pendingMommyUUID);
        if (!(girl instanceof GalathEntity)) {
            return;
        }

        GalathEntity galath = (GalathEntity)girl;
        this.setMommyUUID(this.pendingMommyUUID);
        galath.setManglelieUUID(this.girlID());
        this.setCorrupting(true);
        this.setCurrentAction(Action.RIDE_MOMMY_HEAD);
        this.pendingMommyUUID = null;
        if (galath.getCurrentAction() == Action.HUG_MANG) {
            galath.setAnchored(false);
            galath.setCurrentAction((Action)null);
        }
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.THREESOME_CUM && Action.isAny(action, Action.THREESOME_FAST, Action.THREESOME_SLOW)) {
            return;
        }
        if (!this.world.isRemote && action == Action.THREESOME_CUM) {
            GalathMangTracker.saveCumTime(this.getInteractionPlayerUUID(), this.world.getTotalWorldTime());
        }
        super.setCurrentAction(action);
    }

    void updatePositionSyncWithMommy() {
        if (!this.isAttachedToMommy() || Action.isAnyAction((GirlEntity)this, Action.THREESOME_SLOW, Action.THREESOME_CUM, Action.THREESOME_FAST)) {
            return;
        }
        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            return;
        }

        if (galath.isDead || !this.girlID().equals(galath.getManglelieUUID())) {
            Main.LOGGER.warn("A dead mommy has been saved onto a mang. Deleting her and creating a new one");
            this.world.removeEntity(this);
            return;
        }

        this.setYawRotation(0.0f);
        this.setTargetPosition(galath.getPositionVector());
        this.setAnchored(true);
    }

    @Override
    public void setYawRotation(float yaw) {
        super.setYawRotation(yaw);
    }

    @Override
    public Vec3d getInterpolatedRenderPos(Vec3d pos, float partialTicks) {
        if (!this.isAttachedToMommy()) {
            return pos;
        }
        if (ManglelieModel.isThreesomeAction(this)) {
            return pos;
        }

        GalathEntity galath = this.getMommyGalath(false);
        if (galath == null) {
            return pos;
        }
        return ManglelieRenderer.getMommyHeadOffset(galath, partialTicks);
    }

    void updateNavigationToGalath() {
        if (this.isAttachedToMommy()) {
            return;
        }
        if (this.getCorruptPlayerUUID() != null) {
            return;
        }

        BlockPos pos = this.getPosition();
        BlockPos startPos = pos.add(-15.0, -15.0, -15.0);
        BlockPos endPos = pos.add(15.0, 15.0, 15.0);
        AxisAlignedBB searchBox = new AxisAlignedBB(startPos, endPos);

        List<GalathEntity> galaths = this.world.getEntitiesWithinAABB(GalathEntity.class, searchBox);

        Entity targetGalath = null;
        for (GalathEntity galath : galaths) {
            if (galath.isDead || galath.getManglelieUUID(true) != null || !galath.onGround) continue;
            targetGalath = galath;
            break;
        }
        if (targetGalath == null) {
            if (this.getCurrentAction() == Action.RUN) {
                this.setCurrentAction((Action)null);
                this.getNavigator().clearPath();
            }
            return;
        }
        if (this.getCurrentAction() == Action.RIDE_MOMMY_HEAD) {
            return;
        }
        this.setCurrentAction(Action.RUN);

        Vec3d mangPos = this.getPositionVector();
        Vec3d galPos = targetGalath.getPositionVector();
        Vec3d diff = galPos.subtract(mangPos);
        float yaw = (float) TrigMath.sinDegrees(Math.atan2(diff.z, diff.x)) - 90.0f;
        this.setYawRotation(yaw);
        this.pathNavigator = this.getNavigator();
        this.pathNavigator.clearPath();
        this.pathNavigator.tryMoveToEntityLiving(targetGalath, 0.65f);
    }

    public boolean checkRelativeHandPosition(Entity entity, float partialTicks) {
        GalathEntity galath = this.getMommyGalath(partialTicks == 1.0f);
        if (galath == null) {
            return false;
        }
        Vec3d myPos = EntityLookVectorHelper.getInterpolatedPosition(this, partialTicks);
        return this.isVectorRightOfMommy(EntityLookVectorHelper.getInterpolatedPosition(entity, partialTicks).subtract(myPos), galath, partialTicks);
    }

    public boolean isVectorRightOfMommy(Vec3d vec, float partialTicks) {
        GalathEntity galath = this.getMommyGalath(partialTicks == 1.0f);
        if (galath == null) {
            return false;
        }
        Vec3d myPos = EntityLookVectorHelper.getInterpolatedPosition(this, partialTicks);
        return this.isVectorRightOfMommy(vec.subtract(myPos), galath, partialTicks);
    }

    boolean isVectorRightOfMommy(Vec3d vec, GalathEntity galath, float partialTicks) {
        Vec3d rotated = VectorMath.rotateByYaw(vec, RotationHelper.LerpAngleDegrees(galath.prevRotationYawHead, galath.rotationYawHead, (double)partialTicks));
        return rotated.x > 0.35;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            this.updateClientLookAngles();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void updateClientLookAngles() {
        if ((float)Minecraft.getMinecraft().player.ticksExisted % 7.0f != 0.0f) {
            return;
        }
        if (!ManglelieRenderer.hasValidModel(this)) {
            return;
        }
        GalathEntity galath = this.getMommyGalath(false);
        if (galath == null) {
            return;
        }
        Entity target = this.getNearestPlayerOrTarget();
        if (target == null) {
            this.targetHeadYaw = 0.0f;
            this.targetHeadPitch = 0.0f;
            return;
        }
        Vec3d targetEyePos = target.getPositionVector().add(0.0, target.getEyeHeight(), 0.0);
        Vec3d mangleHeadPos = galath.getPositionVector().add(galath.getCachedBoneOffset("mangPos")).add(this.getCachedBoneOffset("head"));
        Vec3d diff = mangleHeadPos.subtract(targetEyePos);

        float yaw = (float)(TrigMath.sinDegrees(Math.atan2(diff.z, diff.x)) + 90.0);
        Float galathHeadPos = GalathEntity.getAimYaw(galath, 0.0f);
        yaw -= galath.rotationYawHead;
        if (galathHeadPos != null) {
            yaw -= galathHeadPos;
        }
        this.targetHeadYaw = Math.abs(WorldUtils.CalculateAngleDifferences(0.0f, yaw)) < 80.0f ? -TrigMath.wrapDegrees(yaw) : 0.0f;
        this.targetHeadPitch = this.targetHeadYaw == 0.0f ? 0.0f : (float) ThreadNames.clamp(-diff.y / 2.0, -0.75, 0.75);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(source, amount);
        }
        GalathEntity galath = this.getMommyGalath(true);
        if (galath == null) {
            return super.attackEntityFrom(source, amount);
        }
        galath.attackEntityFrom(source, amount); //Mangle dies second. Galath dies first. This makes sense.
        return false;
    }

    @Nullable
    Entity getNearestPlayerOrTarget() {
        Entity target = this.getTargetEntity();
        if (target != null) {
            return target;
        }
        for (EntityPlayer player : this.world.playerEntities) {
            float dist = player.getDistance(this);
            if (dist > 6.0f || target != null && !(target.getDistance(this) > dist)) continue;
            target = player;
        }
        return target;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        UUID uUID = this.getCorruptPlayerUUID();
        nbt.setString(NBT_MOMMY_KEY, uUID == null ? "" : uUID.toString());
        nbt.setBoolean("sexmod:iswild", this.isWild);
        if (this.isDespawnedLocal) {
            nbt.setBoolean("sexmod:despawned", true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        String uuidStr = nbt.getString(NBT_MOMMY_KEY);
        if (!uuidStr.isEmpty()) {
            this.pendingMommyUUID = UUID.fromString(uuidStr);
        }
        if (nbt.getBoolean("sexmod:despawned")) {
            this.isDespawned = true;
        }
        this.isWild = nbt.getBoolean("sexmod:iswild");
    }

    @Override
    protected boolean supportsCustomModels() {
        return false;
    }

    @Override
    public void setCustomModelCode(String string) {
        super.setCustomModelCode(string);
        GirlWorldData.setCustomModelCode(this);
    }

    void initCustomModel() {
        if (this.customModelLoaded) {
            return;
        }
        this.setCustomModelCode(GirlWorldData.getCustomModelCode(this));
        this.customModelLoaded = true;
    }

    @Override
    @Nullable
    protected Action getNextAction(Action action) {
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (Action.isAny(action, Action.THREESOME_FAST, Action.THREESOME_SLOW)) {
            this.isThreesomeTransitioning = true;
        }
        return null;
    }

    @Override
    public void reInitTasks() {
        if (this.isAttachedToMommy()) {
            this.setCurrentAction(Action.RIDE_MOMMY_HEAD);
            this.setYawRotation(0.0f);
            this.entityDataManager.setDirty(YAW_ROTATION);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        if (!super.getCanSpawnHere()) {
            return false;
        }

        BlockPos currentPos = this.getPosition();
        ArrayList<BlockPos> restrictedPositions = new ArrayList<BlockPos>();
        restrictedPositions.addAll(StructureTracker.STRUCTURE_POSITIONS);
        restrictedPositions.addAll(StructureTracker.TEMP_POSITIONS);
        for (BlockPos pos : restrictedPositions) {
            if (!(Math.sqrt(currentPos.distanceSq(pos)) < 700.0)) continue;
            return false;
        }
        StructureTracker.addPosInList(currentPos, StructureTracker.TEMP_POSITIONS);
        return true;
    }

    @Override
    protected boolean handleActionAnimationOverrides(Action action, String animName, boolean flag, AnimationEvent event) {
        if (action == Action.THREESOME_CUM) {
            this.isThreesomeTransitioning = false;
            this.isThreesomeHard = false;
            this.isThreesomeSlowBack = false;
            this.cockStage = 2;
            this.resetCameraAndPhysics();

            GalathEntity galath = this.getMommyGalath(false);
            if (galath != null) {
                galath.resetCameraAndPhysics();
                CummyEntity.spawnSexParticles(galath);
            }
            CummyEntity.spawnSexParticles(this);
            return true;
        }

        if (this.isThreesomeTransitioning && action == Action.THREESOME_FAST) {
            this.setCurrentAction(Action.THREESOME_CUM);
            this.createAnimation("animation.shared.double_holding_cum", true, event, true);

            GalathEntity galath = this.getMommyGalath(false);
            if (galath != null) {
                galath.setCurrentAction(Action.MASTERBATE_SITTING_CUM);
            }
            return true;
        }

        if ((this.isThreesomeTransitioning || flag) && action == Action.THREESOME_SLOW) {
            this.isThreesomeHard = false;
            this.setCurrentAction(Action.THREESOME_FAST);
            this.createAnimation("animation.shared.double_holding_soft", true, event, true);
            GalathEntity galath = this.getMommyGalath(false);
            if (galath != null) {
                galath.startFastAction();
            }
            return true;
        }

        if (this.isThreesomeTransitioning) {
            return false;
        }

        if (flag && !this.isThreesomeHard && action == Action.THREESOME_FAST) {
            this.isThreesomeHard = true;
            this.createAnimation("animation.shared.double_holding_hard", true, event, true);
            return true;
        }

        if (!flag && action == Action.THREESOME_FAST) {
            this.isThreesomeSlowBack = true;
            this.setCurrentAction(Action.THREESOME_SLOW);
            this.createAnimation("animation.shared.double_holding_back", true, event, true);
            GalathEntity galath = this.getMommyGalath(false);
            if (galath != null) {
                galath.startSlowAction();
            }
            return true;
        }

        if (this.isThreesomeSlowBack && action == Action.THREESOME_SLOW) {
            this.isThreesomeSlowBack = false;
            this.createAnimation("animation.shared.double_holding_slow", true, event, true);
            return true;
        }
        return false;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        if (this.eyesController == controller) {
            if (this.getAnimationProcessor() == null) {
                return PlayState.STOP;
            }
            this.createAnimation("animation.manglelie.angry_face", true, event);
            return PlayState.CONTINUE;
        }
        if (this.movementController == controller) {
            if (this.getCurrentAction() != Action.NULL || this.isAttachedToMommy()) {
                return PlayState.STOP;
            }
            if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                if (this.entityDataManager.get(IS_SCARED)) {
                    this.createAnimation("animation.manglelie.scared_run", true, event);
                } else {
                    this.createAnimation("animation.manglelie.walk", true, event);
                }
                this.rotationYaw = this.rotationYawHead;
                return PlayState.CONTINUE;
            }
            this.createAnimation("animation.manglelie.idle", true, event);
            return PlayState.CONTINUE;
        }
        switch (this.getCurrentAction()) {
            default: {
                return PlayState.STOP;
            }
            case RUN: {
                this.createAnimation("animation.manglelie.running", true, event);
                break;
            }
            case RIDE_MOMMY_HEAD: {
                this.createAnimation("animation.manglelie.sit_on_galath", true, event);
                break;
            }
            case THREESOME_SLOW: {
                if (this.isThreesomeSlowBack) {
                    this.createAnimation("animation.shared.double_holding_back", true, event);
                    break;
                }
                this.playRandomizedAnimation("animation.shared.double_holding_slow", 4, 0.33f, event);
                break;
            }
            case THREESOME_FAST: {
                if (this.isThreesomeHard) {
                    this.playRandomizedAnimation("animation.shared.double_holding_hard", 3, 0.33f, event);
                    break;
                }
                this.createAnimation("animation.shared.double_holding_soft", true, event);
                break;
            }
            case THREESOME_CUM: {
                this.createAnimation("animation.shared.double_holding_cum", true, event);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
        this.actionController.registerSoundListener(soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "pound": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cs0": {
                    this.cockStage = 0;
                    break;
                }
                case "cs1": {
                    this.cockStage = 1;
                    break;
                }
                case "cs2": {
                    this.cockStage = 2;
                    break;
                }
                case "sexui": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "doubleSemen0": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.playRandomSound(SoundsHandler.MISC_POUNDING, new int[0]);
                }
                case "doubleSemen": {
                    CummyEntity.registerTrail(new DynamicTrailRenderer(10, girl -> {
                        Vec3d vec3d = girl.getBoneWorldPosition("semenEmitter");
                        Vec3d vec3d2 = girl.getBoneWorldPosition("semenDir");
                        return vec3d.subtract(vec3d2).normalize();
                    }, girl -> girl.getCachedBoneOffset("semenEmitter").add(girl.getTargetPosition()), this, 0.3f, 0.3f));
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                }
            }
        });
        data.addAnimationController(this.actionController);
    }

    public static class EventHandler {
        @SubscribeEvent
        public void CancelArrows(ProjectileImpactEvent.Arrow arrow) {
            RayTraceResult rayTraceResult = arrow.getRayTraceResult();
            EntityArrow entityArrow = arrow.getArrow();
            if (!(entityArrow.shootingEntity instanceof ManglelieEntity)) {
                return;
            }
            if (rayTraceResult.entityHit instanceof GirlEntity) {
                arrow.setCanceled(true);
            }
        }
    }
}

