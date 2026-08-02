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
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Galath.EnergyBallEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathDamageSource;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.*;
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

public enum GalathAttackAction {
    CHANGE_POSITION(galath -> {
        World world = galath.world;
        BlockPos currentPos = galath.getPosition();
        BlockPos targetPos = galath.getAttackTarget().getPosition();
        ArrayList<BlockPos> candidatePositions = new ArrayList<BlockPos>();
        HashMap<BlockPos, Integer> positionScores = new HashMap<BlockPos, Integer>();
        int maxScore = 0;
        boolean isOnGround = !world.isAirBlock(currentPos.down());

        for (int x = -10; x < 10; ++x) {
            for (int y = -10; y < 10; ++y) {
                for (int z = -10; z < 10; ++z) {
                    RayTraceResult rayTrace;
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos testPos = targetPos.add(new BlockPos(x, y, z));

                    if (isOnGround && currentPos.getY() >= testPos.getY()
                            || !world.isAirBlock(testPos)
                            || !world.isAirBlock(testPos.up())
                            || !world.isAirBlock(testPos.up().up())
                            || (rayTrace = world.rayTraceBlocks(new Vec3d(currentPos), new Vec3d(testPos), true, true, true)) != null) continue;
                    int groundY = testPos.getY();
                    while (--groundY >= 0 && world.getBlockState(new BlockPos(testPos.getX(), groundY, testPos.getZ())).getBlock() instanceof BlockAir) {
                    }

                    if (world.getBlockState(new BlockPos(testPos.getX(), groundY, testPos.getZ())).getBlock() instanceof BlockLiquid) continue;
                    candidatePositions.add(testPos);

                    if (!world.isAirBlock(testPos.down())
                            || !world.isAirBlock(testPos.down().down())
                            || targetPos.getDistance(testPos.getX(), testPos.getY(), testPos.getZ()) < 5.0
                            || currentPos.getDistance(testPos.getX(), testPos.getY(), testPos.getZ()) < 3.0)
                        continue;

                    int openAirCount = 0;
                    for (int dx = -1; dx < 2; ++dx) {
                        for (int dz = -1; dz < 2; ++dz) {
                            for (int dy = -1; dy < 4; ++dy) {
                                if (!world.isAirBlock(testPos.add(dx, dy, dz))) continue;
                                ++openAirCount;
                            }
                        }
                    }

                    if (openAirCount < 25) continue;
                    positionScores.put(testPos, openAirCount);
                    if (openAirCount <= maxScore) continue;
                    maxScore = openAirCount;
                }
            }
        }

        if (!positionScores.isEmpty()) {
            ArrayList<Map.Entry<BlockPos, Integer>> sortedPositions = new ArrayList<>(positionScores.entrySet());
            sortedPositions.sort((e1, e2) -> ((Integer)e2.getValue()).compareTo((Integer)e1.getValue()));
            galath.targetFlyPos = new Vec3d((Vec3i)(sortedPositions.get(Utils.getWeightedRandomInt(sortedPositions.size() - 1))).getKey());
        } else {
            galath.targetFlyPos = candidatePositions.isEmpty()
                    ? new Vec3d(targetPos.add(Utils.getRandomFloat(10.0f, true), Utils.getRandomFloat(10.0f, false), Utils.getRandomFloat(10.0f, true)))
                    : new Vec3d((Vec3i)candidatePositions.get(Reference.RANDOM.nextInt(candidatePositions.size())));
        }

        galath.previousPos = null;
        galath.setFlyTicks(0);
        galath.setCurrentAction(Action.FLY);
        PackageHandler.INSTANCE.sendToAllTracking((IMessage)new ResetController(galath.girlID()), (Entity)galath);
    },
            galath -> {
        Vec3d currentVec = galath.getPositionVector();
        Vec3d targetVec = galath.targetFlyPos;
        if (targetVec == null) {
            return;
        }

        galath.previousPos = currentVec;
        int ticks = galath.getFlyTicks();
        galath.setFlyTicks(ticks + 1);
        if (ticks != 0) {
            return;
        }

        Vec3d direction = targetVec.subtract(currentVec);
        Vec3d normalizedDir = direction.normalize();
        galath.motionX = normalizedDir.x * (double)0.6f;
        galath.motionZ = normalizedDir.z * (double)0.6f;
        galath.motionY = Utils.clamp(direction.y * (double)0.6f, (double)-0.6f, (double)0.6f);
    }, galath -> galath.getFlyTicks() > 23, galath -> {
        galath.setMotionVector(Vec3d.ZERO);
        galath.setFlyTicks(0);
        galath.previousPos = null;
    }, false, galath -> true, false),

    SUMMON_SKELETON(galath -> {
        galath.setCurrentAction(Action.SUMMON_SKELETON);
        galath.ad = 0;
        EntityDataManager entityDataManager = galath.getDataManager();
        entityDataManager.set(GalathEntity.bN, true);
        entityDataManager.set(GalathEntity.b7, true);
        entityDataManager.set(GalathEntity.ay, galath.getRNG().nextBoolean());
        GirlEntity.playRandomSound((GirlEntity)galath, SoundsHandler.GIRLS_GALATH_STRONGCHARGE, true);
    }, galath -> {
        EnergyBallEntity c4_class1132;
        Vec3d vec3d;
        Vec3d vec3d2;
        Vec3d vec3d3;
        galath.setMotionVector(Vec3d.ZERO);
        if ((float)galath.ad != 30.0f) {
            return;
        }
        GalathEntity.updateRenderPositions(galath, 0.0f);
        Vec3d vec3d4 = galath.getPositionVector();
        Vec3d vec3d5 = galath.getAttackTarget().getPositionVector();
        Random random = galath.getRNG();
        boolean bl = galath.getDataManager().get(GalathEntity.ay);
        if (galath.getDataManager().get(GalathEntity.bN)) {
            vec3d3 = vec3d4.add(VectorMath.rotate(bl ? VectorMath.MirrorXZ(GalathEntity.bz) : GalathEntity.bz, 180.0f + galath.renderYawOffset));
            vec3d2 = vec3d5.subtract(vec3d3).normalize();
            vec3d2 = new Vec3d(vec3d2.x + random.nextDouble() * (double)0.3f, vec3d2.y + random.nextDouble() * (double)0.3f, vec3d2.z + random.nextDouble() * (double)0.3f);
            vec3d2 = vec3d2.normalize();
            vec3d = new Vec3d(vec3d2.x * (double)0.4f, vec3d2.y * (double)0.4f, vec3d2.z * (double)0.4f);
            c4_class1132 = new EnergyBallEntity(galath.world, galath, vec3d);
            c4_class1132.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            galath.world.spawnEntity(c4_class1132);
        }
        if (galath.getDataManager().get(GalathEntity.b7)) {
            vec3d3 = vec3d4.add(VectorMath.rotate(bl ? VectorMath.MirrorXZ(GalathEntity.bC) : GalathEntity.bC, 180.0f + galath.renderYawOffset));
            vec3d2 = vec3d5.subtract(vec3d3).normalize();
            vec3d2 = new Vec3d(vec3d2.x + random.nextDouble() * (double)0.3f, vec3d2.y + random.nextDouble() * (double)0.3f, vec3d2.z + random.nextDouble() * (double)0.3f);
            vec3d2 = vec3d2.normalize();
            vec3d = new Vec3d(vec3d2.x * (double)0.4f, vec3d2.y * (double)0.4f, vec3d2.z * (double)0.4f);
            c4_class1132 = new EnergyBallEntity(galath.world, galath, vec3d);
            c4_class1132.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            galath.world.spawnEntity(c4_class1132);
        }
    }, f__class2972 -> f__class2972.ad >= 45, f__class2972 -> {
        f__class2972.ad = 0;
    }, true, f__class2972 -> f__class2972.witherSkeletons.size() < 2, true),

    ATTACK_SWORD(f__class2972 -> {
        f__class2972.a(0);
        f__class2972.setCurrentAction(Action.ATTACK_SWORD);
        f__class2972.setMotionVector(Vec3d.ZERO);
        Vec3d vec3d = f__class2972.getPositionVector();
        f__class2972.e(vec3d);
        Vec3d vec3d2 = f__class2972.getAttackTarget().getPositionVector();
        g8_class353 g8_class3532 = new g8_class353(vec3d2.x - vec3d.x, vec3d2.z - vec3d.z);
        double d = TrigMath.toDegrees(Math.atan2(g8_class3532.a, g8_class3532.b)) - 90.0;
        f__class2972.setAnchored(true);
        f__class2972.setTargetPosition(vec3d);
        f__class2972.setYawRotation((float)d);
        GirlEntity.playRandomSound((GirlEntity)f__class2972, SoundsHandler.GIRLS_GALATH_STRONGCHARGE, true);
    }, galath -> {
        EntityLivingBase entityLivingBase = galath.getAttackTarget();
        int n = galath.getSwordAttackProgress() + 1;
        galath.a(n);
        if (Utils.isValueInBounds((double)n, 24.0, 32.0)) {
            Vec3d vec3d = entityLivingBase.getPositionVector().add(0.0, entityLivingBase.getEyeHeight(), 0.0);
            g8_class353 g8_class3532 = new g8_class353(vec3d.x - galath.posX, vec3d.z - galath.posZ);
            double d = TrigMath.toDegrees(Math.atan2(g8_class3532.a, g8_class3532.b)) - 90.0;
            galath.setYawRotation((float)d);
            Vec3d vec3d2 = VectorMath.rotate(new Vec3d(0.0, 0.0, 3.0), (float)(d + 180.0));
            Vec3d vec3d3 = galath.getAnchorTargetPosition();
            Vec3d vec3d4 = vec3d.add(vec3d2);
            float f = (float)(n - 24) / 8.0f;
            Vec3d vec3d5 = Reference.LerpVec3d(vec3d3, vec3d4, (double)f);
            galath.setTargetPosition(vec3d5);
        } else if (Utils.isValueInBounds((double)n, 32.0, 54.0)) {
            Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, 1.5), galath.getYawRotation().floatValue() + 180.0f);
            Vec3d vec3d6 = entityLivingBase.getPositionVector().add(vec3d);
            galath.setTargetPosition(vec3d6);
            GalathDamageSource damageSource = new GalathDamageSource(galath);
            entityLivingBase.hurtTime = 0;
            entityLivingBase.hurtResistantTime = 0;
            if (n == 36) {
                entityLivingBase.attackEntityFrom(damageSource, 5.0f);
            }
            if (n == 40) {
                entityLivingBase.attackEntityFrom(damageSource, 5.0f);
            }
        } else if (n == 54) {
            galath.setAnchored(false);
            galath.setCurrentAction(Action.FLY);
            Vec3d vec3d = galath.getAnchorTargetPosition().subtract(galath.getPositionVector()).normalize();
            galath.motionX = vec3d.x * (double)0.6f;
            galath.motionY = vec3d.y * (double)0.6f;
            galath.motionZ = vec3d.z * (double)0.6f;
            galath.setFlyTicks(1);
        } else {
            galath.setFlyTicks(galath.getFlyTicks() + 1);
        }
    }, f__class2972 -> f__class2972.getFlyTicks() > 23, f__class2972 -> {
        f__class2972.setFlyTicks(0);
        f__class2972.setMotionVector(Vec3d.ZERO);
        f__class2972.a(-1);
        f__class2972.setAnchored(false);
    }, true, f__class2972 -> true, false),

    RAPE(galath -> {
        galath.setCurrentAction(Action.RAPE_PREPARE);
        galath.aF = 0;
        galath.bd = null;
        galath.targetFlyPos = null;
        galath.getDataManager().set(GalathEntity.SPIN_YAW_FACTOR, 0.0f);
    }, galath -> {
        double d;
        boolean bl;
        double d2;
        Vec3d vec3d;
        Vec3d vec3d2;
        Vec3d vec3d3;
        if (++galath.aF < 48) {
            return;
        }
        galath.setCurrentAction(Action.RAPE_CHARGE);
        EntityLivingBase entityLivingBase = galath.getAttackTarget();
        if (galath.bd == null) {
            galath.targetFlyPos = entityLivingBase.getPositionVector().add(0.0, entityLivingBase.getEyeHeight() / 2.0f, 0.0);
            galath.bd = galath.getPositionVector();
            vec3d3 = entityLivingBase.getPositionVector().subtract(galath.getPositionVector()).normalize();
            galath.setYawRotation((float)(TrigMath.toDegrees(Math.atan2(vec3d3.z, vec3d3.x)) - 90.0));
        }
        vec3d3 = galath.getPositionVector();
        Vec3d vec3d4 = vec3d3.subtract(0.65f, 0.65f, 0.65f);
        Vec3d vec3d5 = vec3d3.add(0.65f, 0.65f, 0.65f);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d4.x, vec3d4.y, vec3d4.z, vec3d5.x, vec3d5.y, vec3d5.z);
        List<EntityPlayer> list = galath.world.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB);
        for (EntityPlayer object2 : list) {
            if (object2.isDead || !object2.onGround || GirlEntity.getGirlByUUID(object2.getPersistentID(), true) != null) continue;
            vec3d2 = object2.getPositionVector();
            vec3d = vec3d3.subtract(vec3d2);
            Vec3d bl2 = VectorMath.rotate(vec3d, galath.getYawRotation().floatValue());
            d2 = Math.abs(bl2.x);
            if (d2 > (double)0.65f) continue;
            for (EntityWitherSkeleton by : galath.witherSkeletons) {
                Vec3d d3 = by.getPositionVector();
                by.world.removeEntity(by);
                PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(d3, true), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(by.dimension, d3.x, d3.y, d3.z, 50.0));
            }
            galath.witherSkeletons.clear();
            EntityPlayerMP d3 = (EntityPlayerMP)object2;
            galath.setTargetPosition(object2.getPositionVector());
            galath.setInteractionPlayerUUID(object2.getPersistentID());
            galath.setAnchored(true);
            galath.setCurrentAction(Action.RAPE_INTRO);
            byte by = (byte)MathHelper.floor((galath.getYawRotation().floatValue() + 180.0f) * 256.0f / 360.0f);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), d3);
            d3.connection.sendPacket(new SPacketEntityVelocity(d3.getEntityId(), 0.0, 0.0, 0.0));
            d3.connection.sendPacket(new SPacketEntity.S16PacketEntityLook(d3.getEntityId(), (byte)by, (byte)-14, true));
            return;
        }
        Vec3d vec3d8 = galath.bd;
        Vec3d vec3d6 = galath.targetFlyPos;
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
            galath.targetFlyPos = entityLivingBase.getPositionVector().add(0.0, entityLivingBase.getEyeHeight() / 2.0f, 0.0);
        }
        vec3d3 = bl ? new Vec3d(Reference.LerpDouble(vec3d6.x, vec3d.x, Math.min(1.0, d2)), Reference.LerpDouble(vec3d6.y, vec3d.y, Math.min(1.0, Reference.EaseInCubic(d2))), Reference.LerpDouble(vec3d6.z, vec3d.z, Math.min(1.0, d2))) : new Vec3d(Reference.LerpDouble(vec3d8.x, vec3d6.x, d2), Reference.LerpDouble(vec3d8.y, vec3d6.y, Reference.EaseOutCubic(d2)), Reference.LerpDouble(vec3d8.z, vec3d6.z, d2));
        galath.setPosition(vec3d3.x, vec3d3.y, vec3d3.z);
        if (bl) {
            galath.getDataManager().set(GalathEntity.SPIN_YAW_FACTOR, (float) d2);
        }
    }, galath -> {
        if (galath.currentAction() == Action.RAPE_INTRO) {
            return true;
        }
        Vec3d vec3d = galath.bd;
        Vec3d vec3d2 = galath.targetFlyPos;
        if (vec3d == null) {
            return false;
        }
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        Vec3d vec3d4 = vec3d2.add(vec3d3);
        vec3d4 = new Vec3d(vec3d4.x, vec3d.y, vec3d4.z);
        return galath.getDistance(vec3d4.x, vec3d4.y, vec3d4.z) < (double)0.1f;
    }, galath -> {
        galath.targetFlyPos = null;
        galath.bd = null;
        galath.aF = 0;
        galath.getDataManager().set(GalathEntity.SPIN_YAW_FACTOR, 0.0f);
    }, true, galath -> true, true);

    final IGalathUpdate onUpdateAction;
    final IGalathStart onStartAction;
    final IGalathFinish isFinishedCondition;
    final IGalathStop onStopAction;
    final IGalathExecute canExecuteCondition;
    final public boolean applyAttackCoolDown;
    final public boolean onlyDoThisOnPlayers;

    private GalathAttackAction(IGalathStart onStartAction, IGalathFinish isFinishedCondition, IGalathUpdate onUpdateAction, IGalathStop onStopAction, boolean applyAttackCoolDown, IGalathExecute canExecuteAction, boolean onlyDoThisOnPlayers) {
        this.onUpdateAction = onUpdateAction;
        this.onStartAction = onStartAction;
        this.isFinishedCondition = isFinishedCondition;
        this.onStopAction = onStopAction;
        this.applyAttackCoolDown = applyAttackCoolDown;
        this.canExecuteCondition = canExecuteAction;
        this.onlyDoThisOnPlayers = onlyDoThisOnPlayers;
    }

    public boolean executeStart(GalathEntity galath) {
        this.onStartAction.execute(galath);
        return true;
    }

    public boolean executeUpdate(GalathEntity galath) {
        return this.onUpdateAction.execute(galath);
    }

    public void checkFinished(GalathEntity galath) {
        this.isFinishedCondition.test(galath);
    }

    public void executeStop(GalathEntity galath) {
        this.onStopAction.execute(galath);
    }

    public boolean canExecute(GalathEntity galath) {
        return this.canExecuteCondition.test(galath);
    }
}

