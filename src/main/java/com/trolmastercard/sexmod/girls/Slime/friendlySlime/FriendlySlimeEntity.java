/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Slime.friendlySlime;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.Slime.SlimeEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
//ay
public class FriendlySlimeEntity extends EntityLiving {
    static public int MAX_AGE = 8400;
    static public List<FriendlySlimeEntity> ALL_SLIMES = new ArrayList<>();
    final static private DataParameter<Integer> AGE_IN_TICKS = EntityDataManager.createKey(FriendlySlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    final static private DataParameter<Integer> SIZE = EntityDataManager.createKey(FriendlySlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(110);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public FriendlySlimeEntity(World world) {
        super(world);
        this.moveHelper = new SlimeMoveHelper(this);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new SlimeWanderAI(this));
        this.tasks.addTask(5, new SlimeJumpAI(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SIZE, 1);
        this.dataManager.register(AGE_IN_TICKS, 0);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    protected void setSlimeSize(int size, boolean flag) {
        this.dataManager.set(SIZE, size);
        this.setSize(0.51000005f * (float)size, 0.51000005f * (float)size);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(size * size);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * (float)size);
        if (flag) {
            this.setHealth(this.getMaxHealth());
        }
        this.experienceValue = size;
    }

    public int getSquishFactor() {
        return this.dataManager.get(SIZE);
    }

    public static void slimeRegisterFixes(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, FriendlySlimeEntity.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Size", this.getSquishFactor() - 1);
        nbt.setBoolean("wasOnGround", this.wasOnGround);
        nbt.setInteger("ageInTicks", this.dataManager.get(AGE_IN_TICKS));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        int size = nbt.getInteger("Size");
        if (size < 0) {
            size = 0;
        }
        this.setSlimeSize(size + 1, false);
        this.wasOnGround = nbt.getBoolean("wasOnGround");
        this.dataManager.set(AGE_IN_TICKS, nbt.getInteger("ageInTicks"));
    }

    public boolean isSmallSlime() {
        return this.getSquishFactor() <= 1;
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SLIME;
    }

    public static ArrayList<FriendlySlimeEntity> findSlimesNear(Vec3d pos) {
        ArrayList<FriendlySlimeEntity> slimes = FriendlySlimeEntity.findSlimesNearRadius(pos, 0.1);
        if (slimes.isEmpty()) {
            slimes = FriendlySlimeEntity.findSlimesNearRadius(pos, 0.5);
        }
        return slimes;
    }

    private static ArrayList<FriendlySlimeEntity> findSlimesNearRadius(Vec3d pos, double radius) {
        ArrayList<FriendlySlimeEntity> found = new ArrayList<>();
        try {
            for (FriendlySlimeEntity slime : ALL_SLIMES) {
                if (slime != null) {
                    double dist = Math.abs(slime.prevPosX - pos.x) + Math.abs(slime.prevPosY - pos.y) + Math.abs(slime.prevPosZ - pos.z);
                    if (slime.world != null && dist < radius) {
                        found.add(slime);
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("couldnt find slimes at distance " + radius);
        }
        return found;
    }

    public Vec3d getPrevPosition() {
        return new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ);
    }

    void spawnParticle(EnumParticleTypes particleTypes) {
        double vx = Reference.RANDOM.nextGaussian() * 0.02;
        double vy = Reference.RANDOM.nextGaussian() * 0.02;
        double vz = Reference.RANDOM.nextGaussian() * 0.02;
        this.world.spawnParticle(particleTypes, this.posX + (double)(Reference.RANDOM.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.15 + (double)(Reference.RANDOM.nextFloat() * this.height), this.posZ + (double)(Reference.RANDOM.nextFloat() * this.width * 2.0f) - (double)this.width, vx, vy, vz);
    }

    @Override
    public void onUpdate() {
        this.dataManager.set(AGE_IN_TICKS, this.dataManager.get(AGE_IN_TICKS) + 1);
        if (this.world.isRemote) {
            if ((double) this.dataManager.get(AGE_IN_TICKS) > (double) MAX_AGE * 0.95) {
                this.spawnParticle(EnumParticleTypes.CLOUD);
            } else if ((double) this.dataManager.get(AGE_IN_TICKS) > (double) MAX_AGE * 0.7 && this.ticksExisted % 10 == 0) {
                this.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY);
            }
        } else if (this.dataManager.get(AGE_IN_TICKS) > MAX_AGE) {
            SlimeEntity slime = new SlimeEntity(this.world);
            slime.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.world.spawnEntity(slime);
            slime.PlaySound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
            this.world.removeEntity(this);
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();
        if (this.onGround && !this.wasOnGround) {
            int squish = this.getSquishFactor();
            if (this.canDrop()) {
                squish = 0;
            }
            for (int i = 0; i < squish * 8; ++i) {
                float theta = this.rand.nextFloat() * ((float)Math.PI * 2);
                float scale = this.rand.nextFloat() * 0.5f + 0.5f;
                float xOffset = MathHelper.sin(theta) * (float)squish * 0.5f * scale;
                float zOffset = MathHelper.cos(theta) * (float)squish * 0.5f * scale;
                World world = this.world;
                EnumParticleTypes particleType = this.getParticleType();
                double x = this.posX + (double)xOffset;
                double z = this.posZ + (double)zOffset;
                world.spawnParticle(particleType, x, this.getEntityBoundingBox().minY, z, 0.0, 0.0, 0.0);
            }
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.squishAmount = -0.5f;
        } else if (!this.onGround && this.wasOnGround) {
            this.squishAmount = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.decaySquish();
    }

    protected void decaySquish() {
        this.squishAmount *= 0.6f;
    }

    protected int getRandomDespawnDelay() {
        return this.rand.nextInt(100) + 50;
    }

    protected FriendlySlimeEntity createChild() {
        return new FriendlySlimeEntity(this.world);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (SIZE.equals(key)) {
            int squish = this.getSquishFactor();
            this.setSize(0.51000005f * (float)squish, 0.51000005f * (float)squish);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public void setDead() {
        int squish = this.getSquishFactor();
        if (!this.world.isRemote && squish > 1 && this.getHealth() <= 0.0f) {
            int count = 2 + this.rand.nextInt(3);
            for (int i = 0; i < count; ++i) {
                float xOffset = ((float)(i % 2) - 0.5f) * (float)squish / 4.0f;
                float zOffset = ((float)(i / 2) - 0.5f) * (float)squish / 4.0f;
                FriendlySlimeEntity child = this.createChild();
                if (this.hasCustomName()) {
                    child.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.isNoDespawnRequired()) {
                    child.enablePersistence();
                }
                child.setSlimeSize(squish / 2, true);
                child.setLocationAndAngles(this.posX + (double)xOffset, this.posY + 0.5, this.posZ + (double)zOffset, this.rand.nextFloat() * 360.0f, 0.0f);
                this.world.spawnEntity(child);
            }
        }
        super.setDead();
    }

    @Override
    public float getEyeHeight() {
        return 0.625f * this.height;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
    }

    @Override
    protected Item getDropItem() {
        return this.getSquishFactor() == 1 ? Items.SLIME_BALL : null;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return this.getSquishFactor() == 1 ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f * (float)this.getSquishFactor();
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }

    protected boolean canSquish() {
        return this.getSquishFactor() > 0;
    }

    @Override
    protected void jump() {
        this.motionY = 0.42f;
        this.isAirBorne = true;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setSlimeSize(1, true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    protected SoundEvent getJumpSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
    }

    protected boolean canDrop() {
        return false;
    }

    static class SlimeMoveHelper extends EntityMoveHelper {
        private float rotationYaw;
        private int squishDelay;
        final private FriendlySlimeEntity slime;
        private boolean wasOnGround;

        public SlimeMoveHelper(FriendlySlimeEntity slime) {
            super(slime);
            this.slime = slime;
            this.rotationYaw = 180.0f * slime.rotationYaw / (float)Math.PI;
        }

        public void setMoveHelperTarget(float yaw, boolean onGround) {
            this.rotationYaw = yaw;
            this.wasOnGround = onGround;
        }

        public void setMoveHelperSpeed(double speed) {
            this.speed = speed;
            this.action = EntityMoveHelper.Action.MOVE_TO;
        }

        @Override
        public void onUpdateMoveHelper() {
            this.entity.rotationYawHead = this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.rotationYaw, 90.0f);
            this.entity.renderYawOffset = this.entity.rotationYaw;
            if (this.action != EntityMoveHelper.Action.MOVE_TO) {
                this.entity.setMoveForward(0.0f);
            } else {
                this.action = EntityMoveHelper.Action.WAIT;
                if (this.entity.onGround) {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                    if (this.squishDelay-- <= 0) {
                        this.squishDelay = this.slime.getRandomDespawnDelay();
                        if (this.wasOnGround) {
                            this.squishDelay /= 3;
                        }
                        float yaw = Reference.RANDOM.nextInt(360);
                        ((SlimeMoveHelper)this.slime.getMoveHelper()).setMoveHelperTarget(yaw, false);
                        this.slime.getJumpHelper().setJumping();
                        if (this.slime.canSquish()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2f + 1.0f) * 0.8f);
                        }
                    } else {
                        this.slime.moveStrafing = 0.0f;
                        this.slime.moveForward = 0.0f;
                        this.entity.setAIMoveSpeed(0.0f);
                    }
                } else {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                }
            }
        }
    }

    static class SlimeJumpAI extends EntityAIBase {
        final private FriendlySlimeEntity squishAmount;

        public SlimeJumpAI(FriendlySlimeEntity slime) {
            this.squishAmount = slime;
            this.setMutexBits(5);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            ((SlimeMoveHelper)this.squishAmount.getMoveHelper()).setMoveHelperSpeed(1.0);
        }
    }

    static class SlimeWanderAI extends EntityAIBase {
        final private FriendlySlimeEntity squishAmonut;

        public SlimeWanderAI(FriendlySlimeEntity slime) {
            this.squishAmonut = slime;
            this.setMutexBits(5);
            ((PathNavigateGround)slime.getNavigator()).setCanSwim(true);
        }

        @Override
        public boolean shouldExecute() {
            return this.squishAmonut.isInWater() || this.squishAmonut.isInLava();
        }

        @Override
        public void updateTask() {
            if (this.squishAmonut.getRNG().nextFloat() < 0.8f) {
                this.squishAmonut.getJumpHelper().setJumping();
            }
            ((SlimeMoveHelper)this.squishAmonut.getMoveHelper()).setMoveHelperSpeed(1.2);
        }
    }

    static class SlimeFloatAI extends EntityAIBase {
        final private FriendlySlimeEntity ownerSlime;
        private float squishAngle;
        private int floatDelay;

        public SlimeFloatAI(FriendlySlimeEntity ay_class512) {
            this.ownerSlime = ay_class512;
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute() {
            return this.ownerSlime.getAttackTarget() == null && (this.ownerSlime.onGround || this.ownerSlime.isInWater() || this.ownerSlime.isInLava() || this.ownerSlime.isPotionActive(MobEffects.LEVITATION));
        }

        @Override
        public void updateTask() {
            if (--this.floatDelay <= 0) {
                this.floatDelay = 40 + this.ownerSlime.getRNG().nextInt(60);
                this.squishAngle = this.ownerSlime.getRNG().nextInt(360);
            }
            ((SlimeMoveHelper)this.ownerSlime.getMoveHelper()).setMoveHelperTarget(this.squishAngle, false);
        }
    }
}

