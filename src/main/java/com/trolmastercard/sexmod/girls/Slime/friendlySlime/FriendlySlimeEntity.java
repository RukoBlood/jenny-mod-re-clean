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
public class FriendlySlimeEntity
extends EntityLiving {
    static public int b = 8400;
    static public List<FriendlySlimeEntity> g = new ArrayList<FriendlySlimeEntity>();
    final static private DataParameter<Integer> d = EntityDataManager.createKey(FriendlySlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    final static private DataParameter<Integer> c = EntityDataManager.createKey(FriendlySlimeEntity.class, DataSerializers.VARINT).getSerializer().createKey(110);
    public float a;
    public float e;
    public float h;
    private boolean f;

    public FriendlySlimeEntity(World world) {
        super(world);
        this.moveHelper = new b_inner53(this);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new d_inner55(this));
        this.tasks.addTask(5, new c_inner54(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(c, 1);
        this.dataManager.register(d, 0);
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    protected void a(int n, boolean bl) {
        this.dataManager.set(c, n);
        this.setSize(0.51000005f * (float)n, 0.51000005f * (float)n);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(n * n);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * (float)n);
        if (bl) {
            this.setHealth(this.getMaxHealth());
        }
        this.experienceValue = n;
    }

    public int h() {
        return this.dataManager.get(c);
    }

    public static void a(DataFixer dataFixer) {
        EntityLiving.registerFixesMob(dataFixer, FriendlySlimeEntity.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("Size", this.h() - 1);
        nBTTagCompound.setBoolean("wasOnGround", this.f);
        nBTTagCompound.setInteger("ageInTicks", this.dataManager.get(d));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        int n = nBTTagCompound.getInteger("Size");
        if (n < 0) {
            n = 0;
        }
        this.a(n + 1, false);
        this.f = nBTTagCompound.getBoolean("wasOnGround");
        this.dataManager.set(d, nBTTagCompound.getInteger("ageInTicks"));
    }

    public boolean j() {
        return this.h() <= 1;
    }

    protected EnumParticleTypes g() {
        return EnumParticleTypes.SLIME;
    }

    public static ArrayList<FriendlySlimeEntity> a(Vec3d vec3d) {
        ArrayList<FriendlySlimeEntity> arrayList = FriendlySlimeEntity.a(vec3d, 0.1);
        if (arrayList.isEmpty()) {
            arrayList = FriendlySlimeEntity.a(vec3d, 0.5);
        }
        return arrayList;
    }

    private static ArrayList<FriendlySlimeEntity> a(Vec3d vec3d, double d) {
        ArrayList<FriendlySlimeEntity> arrayList = new ArrayList<FriendlySlimeEntity>();
        try {
            for (FriendlySlimeEntity ay_class512 : g) {
                if (ay_class512 == null) continue;
                double d2 = Math.abs(ay_class512.prevPosX - vec3d.x) + Math.abs(ay_class512.prevPosY - vec3d.y) + Math.abs(ay_class512.prevPosZ - vec3d.z);
                if (ay_class512.world == null || !(d2 < d)) continue;
                arrayList.add(ay_class512);
            }
        } catch (Exception exception) {
            System.out.println("couldnt find slimes at distance " + d);
        }
        return arrayList;
    }

    public Vec3d e() {
        return new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ);
    }

    void a(EnumParticleTypes enumParticleTypes) {
        double d = Reference.RANDOM.nextGaussian() * 0.02;
        double d2 = Reference.RANDOM.nextGaussian() * 0.02;
        double d3 = Reference.RANDOM.nextGaussian() * 0.02;
        this.world.spawnParticle(enumParticleTypes, this.posX + (double)(Reference.RANDOM.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.15 + (double)(Reference.RANDOM.nextFloat() * this.height), this.posZ + (double)(Reference.RANDOM.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3, new int[0]);
    }

    @Override
    public void onUpdate() {
        this.dataManager.set(d, this.dataManager.get(d) + 1);
        if (this.world.isRemote) {
            if ((double)this.dataManager.get(d).intValue() > (double)b * 0.95) {
                this.a(EnumParticleTypes.CLOUD);
            } else if ((double)this.dataManager.get(d).intValue() > (double)b * 0.7 && this.ticksExisted % 10 == 0) {
                this.a(EnumParticleTypes.VILLAGER_HAPPY);
            }
        } else if (this.dataManager.get(d) > b) {
            SlimeEntity fn_class3202 = new SlimeEntity(this.world);
            fn_class3202.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.world.spawnEntity(fn_class3202);
            fn_class3202.PlaySound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
            this.world.removeEntity(this);
        }
        this.e += (this.a - this.e) * 0.5f;
        this.h = this.e;
        super.onUpdate();
        if (this.onGround && !this.f) {
            int n = this.h();
            if (this.k()) {
                n = 0;
            }
            for (int i = 0; i < n * 8; ++i) {
                float f = this.rand.nextFloat() * ((float)Math.PI * 2);
                float f2 = this.rand.nextFloat() * 0.5f + 0.5f;
                float f3 = MathHelper.sin(f) * (float)n * 0.5f * f2;
                float f4 = MathHelper.cos(f) * (float)n * 0.5f * f2;
                World world = this.world;
                EnumParticleTypes enumParticleTypes = this.g();
                double d = this.posX + (double)f3;
                double d2 = this.posZ + (double)f4;
                world.spawnParticle(enumParticleTypes, d, this.getEntityBoundingBox().minY, d2, 0.0, 0.0, 0.0, new int[0]);
            }
            this.playSound(this.f(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.a = -0.5f;
        } else if (!this.onGround && this.f) {
            this.a = 1.0f;
        }
        this.f = this.onGround;
        this.b();
    }

    protected void b() {
        this.a *= 0.6f;
    }

    protected int a() {
        return this.rand.nextInt(100) + 50;
    }

    protected FriendlySlimeEntity d() {
        return new FriendlySlimeEntity(this.world);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> dataParameter) {
        if (c.equals(dataParameter)) {
            int n = this.h();
            this.setSize(0.51000005f * (float)n, 0.51000005f * (float)n);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.notifyDataManagerChange(dataParameter);
    }

    @Override
    public void setDead() {
        int n = this.h();
        if (!this.world.isRemote && n > 1 && this.getHealth() <= 0.0f) {
            int n2 = 2 + this.rand.nextInt(3);
            for (int i = 0; i < n2; ++i) {
                float f = ((float)(i % 2) - 0.5f) * (float)n / 4.0f;
                float f2 = ((float)(i / 2) - 0.5f) * (float)n / 4.0f;
                FriendlySlimeEntity ay_class512 = this.d();
                if (this.hasCustomName()) {
                    ay_class512.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.isNoDespawnRequired()) {
                    ay_class512.enablePersistence();
                }
                ay_class512.a(n / 2, true);
                ay_class512.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5, this.posZ + (double)f2, this.rand.nextFloat() * 360.0f, 0.0f);
                this.world.spawnEntity(ay_class512);
            }
        }
        super.setDead();
    }

    @Override
    public float getEyeHeight() {
        return 0.625f * this.height;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.j() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.j() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
    }

    protected SoundEvent f() {
        return this.j() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
    }

    @Override
    protected Item getDropItem() {
        return this.h() == 1 ? Items.SLIME_BALL : null;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return this.h() == 1 ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f * (float)this.h();
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }

    protected boolean i() {
        return this.h() > 0;
    }

    @Override
    protected void jump() {
        this.motionY = 0.42f;
        this.isAirBorne = true;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, @Nullable IEntityLivingData iEntityLivingData) {
        this.a(1, true);
        return super.onInitialSpawn(difficultyInstance, iEntityLivingData);
    }

    protected SoundEvent c() {
        return this.j() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
    }

    protected boolean k() {
        return false;
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    static class b_inner53
    extends EntityMoveHelper {
        private float b;
        private int c;
        final private FriendlySlimeEntity d;
        private boolean a;

        public b_inner53(FriendlySlimeEntity ay_class512) {
            super(ay_class512);
            this.d = ay_class512;
            this.b = 180.0f * ay_class512.rotationYaw / (float)Math.PI;
        }

        public void a(float f, boolean bl) {
            this.b = f;
            this.a = bl;
        }

        public void a(double d) {
            this.speed = d;
            this.action = EntityMoveHelper.Action.MOVE_TO;
        }

        @Override
        public void onUpdateMoveHelper() {
            this.entity.rotationYawHead = this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.b, 90.0f);
            this.entity.renderYawOffset = this.entity.rotationYaw;
            if (this.action != EntityMoveHelper.Action.MOVE_TO) {
                this.entity.setMoveForward(0.0f);
            } else {
                this.action = EntityMoveHelper.Action.WAIT;
                if (this.entity.onGround) {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                    if (this.c-- <= 0) {
                        this.c = this.d.a();
                        if (this.a) {
                            this.c /= 3;
                        }
                        float f = Reference.RANDOM.nextInt(360);
                        ((b_inner53)this.d.getMoveHelper()).a(f, false);
                        this.d.getJumpHelper().setJumping();
                        if (this.d.i()) {
                            this.d.playSound(this.d.c(), this.d.getSoundVolume(), ((this.d.getRNG().nextFloat() - this.d.getRNG().nextFloat()) * 0.2f + 1.0f) * 0.8f);
                        }
                    } else {
                        this.d.moveStrafing = 0.0f;
                        this.d.moveForward = 0.0f;
                        this.entity.setAIMoveSpeed(0.0f);
                    }
                } else {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                }
            }
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }

    static class c_inner54
    extends EntityAIBase {
        final private FriendlySlimeEntity a;

        public c_inner54(FriendlySlimeEntity ay_class512) {
            this.a = ay_class512;
            this.setMutexBits(5);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            ((b_inner53)this.a.getMoveHelper()).a(1.0);
        }
    }

    static class d_inner55
    extends EntityAIBase {
        final private FriendlySlimeEntity a;

        public d_inner55(FriendlySlimeEntity ay_class512) {
            this.a = ay_class512;
            this.setMutexBits(5);
            ((PathNavigateGround)ay_class512.getNavigator()).setCanSwim(true);
        }

        @Override
        public boolean shouldExecute() {
            return this.a.isInWater() || this.a.isInLava();
        }

        @Override
        public void updateTask() {
            if (this.a.getRNG().nextFloat() < 0.8f) {
                this.a.getJumpHelper().setJumping();
            }
            ((b_inner53)this.a.getMoveHelper()).a(1.2);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }

    static class a_inner52
    extends EntityAIBase {
        final private FriendlySlimeEntity b;
        private float a;
        private int c;

        public a_inner52(FriendlySlimeEntity ay_class512) {
            this.b = ay_class512;
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute() {
            return this.b.getAttackTarget() == null && (this.b.onGround || this.b.isInWater() || this.b.isInLava() || this.b.isPotionActive(MobEffects.LEVITATION));
        }

        @Override
        public void updateTask() {
            if (--this.c <= 0) {
                this.c = 40 + this.b.getRNG().nextInt(60);
                this.a = this.b.getRNG().nextInt(360);
            }
            ((b_inner53)this.b.getMoveHelper()).a(this.a, false);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

