/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.List;
import java.util.Random;

import com.trolmastercard.sexmod.SoundsHandler;
import com.trolmastercard.sexmod.Packages.SpawnEnergyBallParticlesAlt;
import com.trolmastercard.sexmod.gc_class360;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnergyBallEntity
extends EntityLiving {
    final static public float d = 0.4f;
    final static public float e = 0.3f;
    final static int b = 200;
    final static int k = 100;
    final static float a = 0.5f;
    final static float l = 0.15f;
    final static public float j = 0.75f;
    public double g = 1.0;
    Vec3d h = Vec3d.ZERO;
    boolean c = false;
    boolean i = true;
    GalathEntity f;

    public EnergyBallEntity(World world) {
        super(world);
        this.setSize(0.5f, 0.5f);
    }

    public EnergyBallEntity(World world, GalathEntity f__class2972) {
        super(world);
        this.setSize(0.5f, 0.5f);
        this.f = f__class2972;
    }

    public EnergyBallEntity(World world, GalathEntity f__class2972, Vec3d vec3d) {
        this(world);
        this.h = vec3d;
        this.f = f__class2972;
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
        if (this.isDead) {
            return;
        }
        this.noClip = true;
        this.setNoGravity(true);
        this.motionX = this.h.x;
        this.motionY = this.h.y;
        this.motionZ = this.h.z;
        super.onUpdate();
        if (this.world.isRemote) {
            this.a();
        }
        this.c();
        if (this.world.isAirBlock(this.getPosition())) {
            return;
        }
        this.b();
        this.world.removeEntity(this);
    }

    void c() {
        if (this.world.isRemote) {
            return;
        }
        if (!this.c) {
            return;
        }
        Vec3d vec3d = this.getPositionVector();
        Vec3d vec3d2 = vec3d.subtract(0.75, 0.75, 0.75);
        Vec3d vec3d3 = vec3d.add(0.75, 0.75, 0.75);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d2.x, vec3d2.y, vec3d2.z, vec3d3.x, vec3d3.y, vec3d3.z);
        List<GalathEntity> list = this.world.getEntitiesWithinAABB(GalathEntity.class, axisAlignedBB);
        if (list.isEmpty()) {
            return;
        }
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.0f, true);
        for (GalathEntity f__class2972 : list) {
            f__class2972.f(this.getPositionVector());
        }
        this.world.removeEntity(this);
    }

    void a() {
        this.a(Reference.Lerp(this.lastTickPosX, this.posX, 0.5), Reference.Lerp(this.lastTickPosY, this.posY, 0.5), Reference.Lerp(this.lastTickPosZ, this.posZ, 0.5));
        this.a(this.posX, this.posY, this.posZ);
    }

    void a(double d, double d2, double d3) {
        Random random = this.getRNG();
        this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, d + random.nextDouble() * (double)0.3f, d2 + 0.25 + random.nextDouble() * (double)0.3f, d3 + random.nextDouble() * (double)0.3f, 0.0, 0.0, 0.0, new int[0]);
    }

    void b() {
        if (this.world.isRemote) {
            return;
        }
        if (this.isDead) {
            return;
        }
        if (!this.i) {
            return;
        }
        Vec3d vec3d = new Vec3d(this.posX, this.getPosition().getY() + 1, this.posZ);
        if (!this.b(vec3d)) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.0f, true);
            this.i = false;
            return;
        }
        EntityWitherSkeleton entityWitherSkeleton = new EntityWitherSkeleton(this.world);
        entityWitherSkeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
        entityWitherSkeleton.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        this.world.spawnEntity(entityWitherSkeleton);
        PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(vec3d, true), (Entity)this);
        this.f.bI.add(entityWitherSkeleton);
    }

    boolean b(Vec3d vec3d) {
        if (this.f == null) {
            return true;
        }
        EntityLivingBase entityLivingBase = this.f.net_minecraft_entity_EntityLivingBase_M();
        if (entityLivingBase == null) {
            return true;
        }
        return entityLivingBase.getDistance(vec3d.x, vec3d.y, vec3d.z) < 15.0;
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(Vec3d vec3d) {
        WorldClient worldClient = Minecraft.getMinecraft().world;
        float f = gc_class360.c(1.8f);
        Random random = Reference.RANDOM;
        float f2 = 0.0f;
        while ((double)f2 < Math.PI * 2) {
            double d = Math.sin(f2);
            double d2 = Math.cos(f2);
            double d3 = vec3d.x + d * 0.5;
            double d4 = d * (double)0.15f;
            double d5 = vec3d.z + d2 * 0.5;
            double d6 = d2 * (double)0.15f;
            double d7 = vec3d.y;
            double d8 = random.nextDouble() * (double)0.15f;
            worldClient.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d7, d5, d4, d8, d6, new int[0]);
            f2 += f;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void c(Vec3d vec3d) {
        WorldClient worldClient = Minecraft.getMinecraft().world;
        Random random = Reference.RANDOM;
        for (int i = 0; i < 100; ++i) {
            worldClient.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d.x, vec3d.y, vec3d.z, random.nextDouble() * (double)0.15f, random.nextDouble() * (double)0.15f, random.nextDouble() * (double)0.15f, new int[0]);
        }
        ((World)worldClient).playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.MISC_SHATTER[0], SoundCategory.AMBIENT, 0.7f, 1.0f, false);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (DamageSource.OUT_OF_WORLD.equals(damageSource)) {
            this.setHealth(0.0f);
            this.i = false;
            this.world.removeEntity(this);
            return true;
        }
        if (!this.world.isRemote && "arrow".equals(damageSource.damageType)) {
            this.setHealth(0.0f);
            this.i = false;
            PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(this.getPositionVector(), false), (Entity)this);
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
        this.h = entity.getLookVec();
        this.c = true;
        return true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.world.removeEntity(this);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

