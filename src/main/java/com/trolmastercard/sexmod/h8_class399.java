/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.trolmastercard.sexmod.Packages.ResetController;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.Packages.SpawnEnergyBallParticlesAlt;
import com.trolmastercard.sexmod.girls.Galath.EnergyBallEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public enum h8_class399 {
    CHANGE_POSITION(galath -> {
        World world = galath.world;
        BlockPos blockPos = galath.getPosition();
        BlockPos blockPos2 = galath.net_minecraft_entity_EntityLivingBase_M().getPosition();
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        HashMap<BlockPos, Integer> hashMap = new HashMap<BlockPos, Integer>();
        int n = 0;
        boolean bl = !world.isAirBlock(blockPos.down());
        for (int i = -10; i < 10; ++i) {
            for (int j = -10; j < 10; ++j) {
                for (int k = -10; k < 10; ++k) {
                    RayTraceResult rayTraceResult;
                    if (i == 0 && j == 0 && k == 0) continue;
                    BlockPos blockPos3 = blockPos2.add(new BlockPos(i, j, k));
                    if (bl && blockPos.getY() >= blockPos3.getY() || !world.isAirBlock(blockPos3) || !world.isAirBlock(blockPos3.up()) || !world.isAirBlock(blockPos3.up().up()) || (rayTraceResult = world.rayTraceBlocks(new Vec3d(blockPos), new Vec3d(blockPos3), true, true, true)) != null) continue;
                    int n2 = blockPos3.getY();
                    while (--n2 >= 0 && world.getBlockState(new BlockPos(blockPos3.getX(), n2, blockPos3.getZ())).getBlock() instanceof BlockAir) {
                    }
                    if (world.getBlockState(new BlockPos(blockPos3.getX(), n2, blockPos3.getZ())).getBlock() instanceof BlockLiquid) continue;
                    arrayList.add(blockPos3);
                    if (!world.isAirBlock(blockPos3.down()) || !world.isAirBlock(blockPos3.down().down()) || blockPos2.getDistance(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ()) < 5.0 || blockPos.getDistance(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ()) < 3.0) continue;
                    int n3 = 0;
                    for (int i2 = -1; i2 < 2; ++i2) {
                        for (int i3 = -1; i3 < 2; ++i3) {
                            for (int i4 = -1; i4 < 4; ++i4) {
                                if (!world.isAirBlock(blockPos3.add(i2, i4, i3))) continue;
                                ++n3;
                            }
                        }
                    }
                    if (n3 < 25) continue;
                    hashMap.put(blockPos3, n3);
                    if (n3 <= n) continue;
                    n = n3;
                }
            }
        }
        if (!hashMap.isEmpty()) {
            ArrayList<Map.Entry<BlockPos, Integer>> arrayList2 = new ArrayList<>(hashMap.entrySet());
            arrayList2.sort((entry, entry2) -> ((Integer)entry2.getValue()).compareTo((Integer)entry.getValue()));
            galath.O = new Vec3d((Vec3i)(arrayList2.get(Utils.getWeightedRandomInt(arrayList2.size() - 1))).getKey());
        } else {
            galath.O = arrayList.isEmpty() ? new Vec3d(blockPos2.add(Utils.getRandomFloat(10.0f, true), Utils.getRandomFloat(10.0f, false), Utils.getRandomFloat(10.0f, true))) : new Vec3d((Vec3i)arrayList.get(Reference.RANDOM.nextInt(arrayList.size())));
        }
        galath.bL = null;
        galath.b(0);
        galath.setCurrentAction(Action.FLY);
        PackageHandler.networkWrapper.sendToAllTracking((IMessage)new ResetController(galath.girlID()), (Entity)galath);
    }, f__class2972 -> {
        Vec3d vec3d = f__class2972.getPositionVector();
        Vec3d vec3d2 = f__class2972.O;
        if (vec3d2 == null) {
            return;
        }
        f__class2972.bL = vec3d;
        int n = f__class2972.ar();
        f__class2972.b(n + 1);
        if (n != 0) {
            return;
        }
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        Vec3d vec3d4 = vec3d3.normalize();
        f__class2972.motionX = vec3d4.x * (double)0.6f;
        f__class2972.motionZ = vec3d4.z * (double)0.6f;
        f__class2972.motionY = Utils.clamp(vec3d3.y * (double)0.6f, (double)-0.6f, (double)0.6f);
    }, f__class2972 -> f__class2972.ar() > 23, f__class2972 -> {
        f__class2972.b(Vec3d.ZERO);
        f__class2972.b(0);
        f__class2972.bL = null;
    }, false, f__class2972 -> true, false),
    SUMMON_SKELETON(f__class2972 -> {
        f__class2972.setCurrentAction(Action.SUMMON_SKELETON);
        f__class2972.ad = 0;
        EntityDataManager entityDataManager = f__class2972.getDataManager();
        entityDataManager.set(GalathEntity.bN, true);
        entityDataManager.set(GalathEntity.b7, true);
        entityDataManager.set(GalathEntity.ay, f__class2972.getRNG().nextBoolean());
        GirlEntity.a((GirlEntity)f__class2972, SoundsHandler.GIRLS_GALATH_STRONGCHARGE, true);
    }, f__class2972 -> {
        EnergyBallEntity c4_class1132;
        Vec3d vec3d;
        Vec3d vec3d2;
        Vec3d vec3d3;
        f__class2972.b(Vec3d.ZERO);
        if ((float)f__class2972.ad != 30.0f) {
            return;
        }
        GalathEntity.a(f__class2972, 0.0f);
        Vec3d vec3d4 = f__class2972.getPositionVector();
        Vec3d vec3d5 = f__class2972.net_minecraft_entity_EntityLivingBase_M().getPositionVector();
        Random random = f__class2972.getRNG();
        boolean bl = f__class2972.getDataManager().get(GalathEntity.ay);
        if (f__class2972.getDataManager().get(GalathEntity.bN).booleanValue()) {
            vec3d3 = vec3d4.add(VectorMath.rotate(bl ? VectorMath.MirrorXZ(GalathEntity.bz) : GalathEntity.bz, 180.0f + f__class2972.renderYawOffset));
            vec3d2 = vec3d5.subtract(vec3d3).normalize();
            vec3d2 = new Vec3d(vec3d2.x + random.nextDouble() * (double)0.3f, vec3d2.y + random.nextDouble() * (double)0.3f, vec3d2.z + random.nextDouble() * (double)0.3f);
            vec3d2 = vec3d2.normalize();
            vec3d = new Vec3d(vec3d2.x * (double)0.4f, vec3d2.y * (double)0.4f, vec3d2.z * (double)0.4f);
            c4_class1132 = new EnergyBallEntity(f__class2972.world, f__class2972, vec3d);
            c4_class1132.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            f__class2972.world.spawnEntity(c4_class1132);
        }
        if (f__class2972.getDataManager().get(GalathEntity.b7).booleanValue()) {
            vec3d3 = vec3d4.add(VectorMath.rotate(bl ? VectorMath.MirrorXZ(GalathEntity.bC) : GalathEntity.bC, 180.0f + f__class2972.renderYawOffset));
            vec3d2 = vec3d5.subtract(vec3d3).normalize();
            vec3d2 = new Vec3d(vec3d2.x + random.nextDouble() * (double)0.3f, vec3d2.y + random.nextDouble() * (double)0.3f, vec3d2.z + random.nextDouble() * (double)0.3f);
            vec3d2 = vec3d2.normalize();
            vec3d = new Vec3d(vec3d2.x * (double)0.4f, vec3d2.y * (double)0.4f, vec3d2.z * (double)0.4f);
            c4_class1132 = new EnergyBallEntity(f__class2972.world, f__class2972, vec3d);
            c4_class1132.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            f__class2972.world.spawnEntity(c4_class1132);
        }
    }, f__class2972 -> f__class2972.ad >= 45, f__class2972 -> {
        f__class2972.ad = 0;
    }, true, f__class2972 -> f__class2972.bI.size() < 2, true),
    ATTACK_SWORD(f__class2972 -> {
        f__class2972.a(0);
        f__class2972.setCurrentAction(Action.ATTACK_SWORD);
        f__class2972.b(Vec3d.ZERO);
        Vec3d vec3d = f__class2972.getPositionVector();
        f__class2972.e(vec3d);
        Vec3d vec3d2 = f__class2972.net_minecraft_entity_EntityLivingBase_M().getPositionVector();
        g8_class353 g8_class3532 = new g8_class353(vec3d2.x - vec3d.x, vec3d2.z - vec3d.z);
        double d = TrigMath.toDegrees(Math.atan2(g8_class3532.a, g8_class3532.b)) - 90.0;
        f__class2972.void_a(true);
        f__class2972.c(vec3d);
        f__class2972.void_b((float)d);
        GirlEntity.a((GirlEntity)f__class2972, SoundsHandler.GIRLS_GALATH_STRONGCHARGE, true);
    }, f__class2972 -> {
        EntityLivingBase entityLivingBase = f__class2972.net_minecraft_entity_EntityLivingBase_M();
        int n = f__class2972.az() + 1;
        f__class2972.a(n);
        if (Utils.isValueInBounds((double)n, 24.0, 32.0)) {
            Vec3d vec3d = entityLivingBase.getPositionVector().add(0.0, entityLivingBase.getEyeHeight(), 0.0);
            g8_class353 g8_class3532 = new g8_class353(vec3d.x - f__class2972.posX, vec3d.z - f__class2972.posZ);
            double d = TrigMath.toDegrees(Math.atan2(g8_class3532.a, g8_class3532.b)) - 90.0;
            f__class2972.void_b((float)d);
            Vec3d vec3d2 = VectorMath.rotate(new Vec3d(0.0, 0.0, 3.0), (float)(d + 180.0));
            Vec3d vec3d3 = f__class2972.net_minecraft_util_math_Vec3d_B();
            Vec3d vec3d4 = vec3d.add(vec3d2);
            float f = (float)(n - 24) / 8.0f;
            Vec3d vec3d5 = Reference.LerpVec3d(vec3d3, vec3d4, (double)f);
            f__class2972.c(vec3d5);
        } else if (Utils.isValueInBounds((double)n, 32.0, 54.0)) {
            Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, 1.5), f__class2972.java_lang_Float_I().floatValue() + 180.0f);
            Vec3d vec3d6 = entityLivingBase.getPositionVector().add(vec3d);
            f__class2972.c(vec3d6);
            GalathDamageSource damageSource = new GalathDamageSource(f__class2972);
            entityLivingBase.hurtTime = 0;
            entityLivingBase.hurtResistantTime = 0;
            if (n == 36) {
                entityLivingBase.attackEntityFrom(damageSource, 5.0f);
            }
            if (n == 40) {
                entityLivingBase.attackEntityFrom(damageSource, 5.0f);
            }
        } else if (n == 54) {
            f__class2972.void_a(false);
            f__class2972.setCurrentAction(Action.FLY);
            Vec3d vec3d = f__class2972.net_minecraft_util_math_Vec3d_B().subtract(f__class2972.getPositionVector()).normalize();
            f__class2972.motionX = vec3d.x * (double)0.6f;
            f__class2972.motionY = vec3d.y * (double)0.6f;
            f__class2972.motionZ = vec3d.z * (double)0.6f;
            f__class2972.b(1);
        } else {
            f__class2972.b(f__class2972.ar() + 1);
        }
    }, f__class2972 -> f__class2972.ar() > 23, f__class2972 -> {
        f__class2972.b(0);
        f__class2972.b(Vec3d.ZERO);
        f__class2972.a(-1);
        f__class2972.void_a(false);
    }, true, f__class2972 -> true, false),
    RAPE(f__class2972 -> {
        f__class2972.setCurrentAction(Action.RAPE_PREPARE);
        f__class2972.aF = 0;
        f__class2972.bd = null;
        f__class2972.O = null;
        f__class2972.getDataManager().set(GalathEntity.bO, Float.valueOf(0.0f));
    }, f__class2972 -> {
        double d;
        boolean bl;
        double d2;
        Vec3d vec3d;
        Vec3d vec3d2;
        Vec3d vec3d3;
        if (++f__class2972.aF < 48) {
            return;
        }
        f__class2972.setCurrentAction(Action.RAPE_CHARGE);
        EntityLivingBase entityLivingBase = f__class2972.net_minecraft_entity_EntityLivingBase_M();
        if (f__class2972.bd == null) {
            f__class2972.O = entityLivingBase.getPositionVector().add(0.0, entityLivingBase.getEyeHeight() / 2.0f, 0.0);
            f__class2972.bd = f__class2972.getPositionVector();
            vec3d3 = entityLivingBase.getPositionVector().subtract(f__class2972.getPositionVector()).normalize();
            f__class2972.void_b((float)(TrigMath.toDegrees(Math.atan2(vec3d3.z, vec3d3.x)) - 90.0));
        }
        vec3d3 = f__class2972.getPositionVector();
        Vec3d vec3d4 = vec3d3.subtract(0.65f, 0.65f, 0.65f);
        Vec3d vec3d5 = vec3d3.add(0.65f, 0.65f, 0.65f);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d4.x, vec3d4.y, vec3d4.z, vec3d5.x, vec3d5.y, vec3d5.z);
        List<EntityPlayer> list = f__class2972.world.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB);
        for (EntityPlayer object2 : list) {
            if (object2.isDead || !object2.onGround || GirlEntity.a(object2.getPersistentID(), true) != null) continue;
            vec3d2 = object2.getPositionVector();
            vec3d = vec3d3.subtract(vec3d2);
            Vec3d bl2 = VectorMath.rotate(vec3d, f__class2972.java_lang_Float_I().floatValue());
            d2 = Math.abs(bl2.x);
            if (d2 > (double)0.65f) continue;
            for (EntityWitherSkeleton by : f__class2972.bI) {
                Vec3d d3 = by.getPositionVector();
                by.world.removeEntity(by);
                PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(d3, true), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(by.dimension, d3.x, d3.y, d3.z, 50.0));
            }
            f__class2972.bI.clear();
            EntityPlayerMP d3 = (EntityPlayerMP)object2;
            f__class2972.c(object2.getPositionVector());
            f__class2972.void_e(object2.getPersistentID());
            f__class2972.void_a(true);
            f__class2972.setCurrentAction(Action.RAPE_INTRO);
            byte by = (byte)MathHelper.floor((f__class2972.java_lang_Float_I().floatValue() + 180.0f) * 256.0f / 360.0f);
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), d3);
            d3.connection.sendPacket(new SPacketEntityVelocity(d3.getEntityId(), 0.0, 0.0, 0.0));
            d3.connection.sendPacket(new SPacketEntity.S16PacketEntityLook(d3.getEntityId(), (byte)by, (byte)-14, true));
            return;
        }
        Vec3d vec3d8 = f__class2972.bd;
        Vec3d vec3d6 = f__class2972.O;
        vec3d2 = vec3d6.subtract(vec3d8);
        vec3d = vec3d6.add(vec3d2);
        vec3d = new Vec3d(vec3d.x, vec3d8.y, vec3d.z);
        boolean bl3 = bl = vec3d3.distanceTo(new Vec3d(vec3d8.x, vec3d3.y, vec3d8.z)) > vec3d3.distanceTo(new Vec3d(vec3d.x, vec3d3.y, vec3d.z));
        if (bl) {
            d2 = VectorMath.getLinearFactor(vec3d6, vec3d, vec3d3);
            d = vec3d6.distanceTo(vec3d);
        } else {
            d2 = VectorMath.getLinearFactor(vec3d8, vec3d6, vec3d3);
            d = vec3d8.distanceTo(vec3d6);
        }
        double d4 = d / (double)0.05f;
        double d5 = 1.0 / d4 * 20.0;
        d2 += d5;
        if (!bl && d2 < (double)0.9f) {
            f__class2972.O = entityLivingBase.getPositionVector().add(0.0, entityLivingBase.getEyeHeight() / 2.0f, 0.0);
        }
        vec3d3 = bl ? new Vec3d(Reference.LerpDouble(vec3d6.x, vec3d.x, Math.min(1.0, d2)), Reference.LerpDouble(vec3d6.y, vec3d.y, Math.min(1.0, Reference.EaseInCubic(d2))), Reference.LerpDouble(vec3d6.z, vec3d.z, Math.min(1.0, d2))) : new Vec3d(Reference.LerpDouble(vec3d8.x, vec3d6.x, d2), Reference.LerpDouble(vec3d8.y, vec3d6.y, Reference.EaseOutCubic(d2)), Reference.LerpDouble(vec3d8.z, vec3d6.z, d2));
        f__class2972.setPosition(vec3d3.x, vec3d3.y, vec3d3.z);
        if (bl) {
            f__class2972.getDataManager().set(GalathEntity.bO, Float.valueOf((float)d2));
        }
    }, f__class2972 -> {
        if (f__class2972.currentAction() == Action.RAPE_INTRO) {
            return true;
        }
        Vec3d vec3d = f__class2972.bd;
        Vec3d vec3d2 = f__class2972.O;
        if (vec3d == null) {
            return false;
        }
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        Vec3d vec3d4 = vec3d2.add(vec3d3);
        vec3d4 = new Vec3d(vec3d4.x, vec3d.y, vec3d4.z);
        return f__class2972.getDistance(vec3d4.x, vec3d4.y, vec3d4.z) < (double)0.1f;
    }, f__class2972 -> {
        f__class2972.O = null;
        f__class2972.bd = null;
        f__class2972.aF = 0;
        f__class2972.getDataManager().set(GalathEntity.bO, Float.valueOf(0.0f));
    }, true, f__class2972 -> true, true);

    final h__class400 a;
    final b2_class62 f;
    final ao_class36 c;
    final u_class425 b;
    final g1_class342 d;
    final public boolean applyAttackCoolDown;
    final public boolean onlyDoThisOnPlayers;

    private h8_class399(b2_class62 b2_class622, ao_class36 ao_class362, h__class400 h__class4002, u_class425 u_class4252, boolean bl, g1_class342 g1_class3422, boolean bl2) {
        this.a = h__class4002;
        this.f = b2_class622;
        this.c = ao_class362;
        this.b = u_class4252;
        this.applyAttackCoolDown = bl;
        this.d = g1_class3422;
        this.onlyDoThisOnPlayers = bl2;
    }

    public void b(GalathEntity f__class2972) {
        this.f.a(f__class2972);
    }

    public boolean c(GalathEntity f__class2972) {
        return this.a.a(f__class2972);
    }

    public void a(GalathEntity f__class2972) {
        this.c.a(f__class2972);
    }

    public void e(GalathEntity f__class2972) {
        this.b.a(f__class2972);
    }

    public boolean d(GalathEntity f__class2972) {
        return this.d.a(f__class2972);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

