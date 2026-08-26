/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Galath.EnergyBall;

import java.util.List;
import java.util.Random;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.Packets.SpawnEnergyBallParticlesPacket2;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnergyBallEntity
extends EntityLiving {
    final static public float SCALE_1 = 0.4f;
    final static public float SCALE_2 = 0.3f;
    final static int MAx_TICKS = 200;
    final static int MIN_TICKS = 100;
    final static float SIZE = 0.5f;
    final static float SCALE_3 = 0.15f;
    final static public float THREE_HALFS = 0.75f;
    public double SCALE_1_0 = 1.0;
    Vec3d direction = Vec3d.ZERO;
    boolean isCharging = false;
    boolean shouldSpawnSkeleton = true;
    GalathEntity ownerGalath;

    public EnergyBallEntity(World world) {
        super(world);
        this.setSize(SIZE, SIZE);
    }

    public EnergyBallEntity(World world, GalathEntity galath) {
        super(world);
        this.setSize(SIZE, SIZE);
        this.ownerGalath = galath;
    }

    public EnergyBallEntity(World world, GalathEntity galath, Vec3d vec3d) {
        this(world);
        this.direction = vec3d;
        this.ownerGalath = galath;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public void onUpdate() {
        if (!this.isDead) {
            this.noClip = true;
            this.setNoGravity(true);
            this.motionX = this.direction.x;
            this.motionY = this.direction.y;
            this.motionZ = this.direction.z;
            super.onUpdate();
            if (this.world.isRemote) {
                this.spawnChargedBreath();
            }
            this.tickChargeState();
            if (this.world.isAirBlock(this.getPosition())) {
                return;
            }
            this.tickBallLife();
            this.world.removeEntity(this);
        }
    }

    void tickChargeState() {
        if (!this.world.isRemote) {
            if (this.isCharging) {
                Vec3d pos = this.getPositionVector();
                Vec3d offsetStart = pos.subtract(0.75, 0.75, 0.75);
                Vec3d offsetEnd = pos.add(0.75, 0.75, 0.75);
                AxisAlignedBB aabb = new AxisAlignedBB(offsetStart.x, offsetStart.y, offsetStart.z, offsetEnd.x, offsetEnd.y, offsetEnd.z);
                List<GalathEntity> galathes = this.world.getEntitiesWithinAABB(GalathEntity.class, aabb);
                if (!galathes.isEmpty()) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.0f, true);
                    for (GalathEntity galath : galathes) {
                        galath.setFlightVelocity(this.getPositionVector());
                    }
                    this.world.removeEntity(this);
                }
            }
        }
    }

    void spawnChargedBreath() {
        this.spawnBreathParticles(RotationHelper.LerpDouble(this.lastTickPosX, this.posX, 0.5), RotationHelper.LerpDouble(this.lastTickPosY, this.posY, 0.5), RotationHelper.LerpDouble(this.lastTickPosZ, this.posZ, 0.5));
        this.spawnBreathParticles(this.posX, this.posY, this.posZ);
    }

    void spawnBreathParticles(double x, double y, double z) {
        Random random = this.getRNG();
        this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, x + random.nextDouble() * (double)0.3f, y + 0.25 + random.nextDouble() * (double)0.3f, z + random.nextDouble() * (double)0.3f, 0.0, 0.0, 0.0);
    }

    void tickBallLife() {
        if (!this.world.isRemote) {
            if (!this.isDead) {
                if (this.shouldSpawnSkeleton) {
                    Vec3d vec3d = new Vec3d(this.posX, this.getPosition().getY() + 1, this.posZ);
                    if (!this.isInRangeOfTarget(vec3d)) {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.0f, true);
                        this.shouldSpawnSkeleton = false;
                        return;
                    }
                    EntityWitherSkeleton entityWitherSkeleton = new EntityWitherSkeleton(this.world);
                    entityWitherSkeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
                    entityWitherSkeleton.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                    this.world.spawnEntity(entityWitherSkeleton);
                    PacketHandler.INSTANCE.sendToAllTracking(new SpawnEnergyBallParticlesPacket2(vec3d, true), this);
                    this.ownerGalath.witherSkeletons.add(entityWitherSkeleton);
                }
            }
        }
    }

    boolean isInRangeOfTarget(Vec3d vec3d) {
        if (this.ownerGalath == null) {
            return true;
        }
        EntityLivingBase target = this.ownerGalath.getAttackTarget();
        return target == null || target.getDistance(vec3d.x, vec3d.y, vec3d.z) < 15.0;
    }

    @SideOnly(value=Side.CLIENT)
    public static void spawnDragonBreath(Vec3d pos) {
        WorldClient world = Minecraft.getMinecraft().world;
        float step = TrigMath.wrapDegrees(1.8f);
        Random random = Reference.RANDOM;
        float angle = 0.0f;
        while ((double)angle < Math.PI * 2) {
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            double x = pos.x + sin * 0.5;
            double vx = sin * (double)0.15f;
            double z = pos.z + cos * 0.5;
            double vz = cos * (double)0.15f;
            double y = pos.y;
            double vy = random.nextDouble() * (double)0.15f;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, vx, vy, vz);
            angle += step;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void spawnDragonBreathRandom(Vec3d vec3d) {
        WorldClient worldClient = Minecraft.getMinecraft().world;
        Random random = Reference.RANDOM;
        for (int i = 0; i < 100; ++i) {
            worldClient.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d.x, vec3d.y, vec3d.z, random.nextDouble() * (double)0.15f, random.nextDouble() * (double)0.15f, random.nextDouble() * (double)0.15f);
        }
        worldClient.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.MISC_SHATTER[0], SoundCategory.AMBIENT, 0.7f, 1.0f, false);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (DamageSource.OUT_OF_WORLD.equals(damageSource)) {
            this.setHealth(0.0f);
            this.shouldSpawnSkeleton = false;
            this.world.removeEntity(this);
            return true;
        }
        if (!this.world.isRemote && "arrow".equals(damageSource.damageType)) {
            this.setHealth(0.0f);
            this.shouldSpawnSkeleton = false;
            PacketHandler.INSTANCE.sendToAllTracking(new SpawnEnergyBallParticlesPacket2(this.getPositionVector(), false), this);
            Entity entity = damageSource.getImmediateSource();
            if (entity != null) {
                this.world.removeEntity(entity);
            }
            this.world.removeEntity(this);
            return true;
        }
        Entity entity = damageSource.getTrueSource();
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        this.direction = entity.getLookVec();
        this.isCharging = true;
        return true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.world.removeEntity(this);
    }
}

