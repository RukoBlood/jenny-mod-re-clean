/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.trolmastercard.sexmod.Packets.ResetController;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.Packets.SpawnEnergyBallParticlesPacket2;
import com.trolmastercard.sexmod.girls.Galath.EnergyBall.EnergyBallEntity;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.interfaces.*;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public enum GalathFlightData {
    CHANGE_POSITION(
            galath -> {
        World world = galath.world;
        BlockPos currentPos = galath.getPosition();
        BlockPos targetPos = galath.getAttackTarget().getPosition();
        ArrayList<BlockPos> candidatePositions = new ArrayList<>();
        HashMap<BlockPos, Integer> positionScores = new HashMap<>();
        int maxScore = 0;
        boolean isOnGround = !world.isAirBlock(currentPos.down());

        for (int x = -10; x < 10; ++x) {
            for (int y = -10; y < 10; ++y) {
                for (int z = -10; z < 10; ++z) {
                    RayTraceResult rayTrace;
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos testPos = targetPos.add(new BlockPos(x, y, z));

                    if ((!isOnGround || currentPos.getY() < testPos.getY())
                            && world.isAirBlock(testPos)
                            && world.isAirBlock(testPos.up())
                            && world.isAirBlock(testPos.up().up())
                            && (rayTrace = world.rayTraceBlocks(new Vec3d(currentPos), new Vec3d(testPos), true, true, true)) == null) {
                        int groundY = testPos.getY();
                        while (--groundY >= 0 && world.getBlockState(new BlockPos(testPos.getX(), groundY, testPos.getZ())).getBlock() instanceof BlockAir) {
                        }

                        if (world.getBlockState(new BlockPos(testPos.getX(), groundY, testPos.getZ())).getBlock() instanceof BlockLiquid)
                            continue;
                        candidatePositions.add(testPos);

                        if (world.isAirBlock(testPos.down())
                                && world.isAirBlock(testPos.down().down())
                                && !(targetPos.getDistance(testPos.getX(), testPos.getY(), testPos.getZ()) < 5.0)
                                && !(currentPos.getDistance(testPos.getX(), testPos.getY(), testPos.getZ()) < 3.0)) {
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
            }
        }

        if (!positionScores.isEmpty()) {
            ArrayList<Map.Entry<BlockPos, Integer>> sortedPositions = new ArrayList<>(positionScores.entrySet());
            sortedPositions.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
            galath.flightTargetPosition = new Vec3d((sortedPositions.get(ThreadNames.getWeightedRandomInt(sortedPositions.size() - 1))).getKey());
        } else {
            galath.flightTargetPosition = candidatePositions.isEmpty()
                    ? new Vec3d(targetPos.add(ThreadNames.getRandomFloat(10.0f, true), ThreadNames.getRandomFloat(10.0f, false), ThreadNames.getRandomFloat(10.0f, true)))
                    : new Vec3d(candidatePositions.get(Reference.RANDOM.nextInt(candidatePositions.size())));
        }

        galath.previousPos = null;
        galath.setFlyTicks(0);
        galath.setCurrentAction(Action.FLY);
        PacketHandler.INSTANCE.sendToAllTracking(new ResetController(galath.girlID()), galath);
    },
            galath -> {
        Vec3d currentVec = galath.getPositionVector();
        Vec3d targetVec = galath.flightTargetPosition;
                if (targetVec != null) {
                    galath.previousPos = currentVec;
                    int ticks = galath.getFlyTicks();
                    galath.setFlyTicks(ticks + 1);
                    if (ticks == 0) {
                        Vec3d direction = targetVec.subtract(currentVec);
                        Vec3d normalizedDir = direction.normalize();
                        galath.motionX = normalizedDir.x * (double) 0.6f;
                        galath.motionZ = normalizedDir.z * (double) 0.6f;
                        galath.motionY = ThreadNames.clamp(direction.y * (double) 0.6f, -0.6f, 0.6f);
                    }

                }

            },
            galath -> galath.getFlyTicks() > 23, galath -> {
        galath.setVelocity(Vec3d.ZERO);
        galath.setFlyTicks(0);
        galath.previousPos = null;
    }, false, galath -> true, false),

    SUMMON_SKELETON(galath -> {
        galath.setCurrentAction(Action.SUMMON_SKELETON);
        galath.energyBallChargeTicks = 0;
        EntityDataManager dataManager = galath.getDataManager();
        dataManager.set(GalathEntity.IS_RIGHT_ENERGY_BALL_ACTIVE, true);
        dataManager.set(GalathEntity.IS_LEFT_ENERGY_BALL_ACTIVE, true);
        dataManager.set(GalathEntity.MIRROR_ENERGY_BALLS, galath.getRNG().nextBoolean());
        GirlEntity.playRandomSound(galath, SoundsHandler.GIRLS_GALATH_STRONGCHARGE, true);
    }, galath -> {
        EnergyBallEntity energyBallEntity;
        Vec3d headVel;
        Vec3d aim;
        Vec3d headAnchor;
        galath.setVelocity(Vec3d.ZERO);
        if ((float) galath.energyBallChargeTicks == 30.0f) {
            GalathEntity.getAimYaw(galath, 0.0f);
            Vec3d pos = galath.getPositionVector();
            Vec3d targetPos = galath.getAttackTarget().getPositionVector();
            Random random = galath.getRNG();
            boolean bl = galath.getDataManager().get(GalathEntity.MIRROR_ENERGY_BALLS);
            if (galath.getDataManager().get(GalathEntity.IS_RIGHT_ENERGY_BALL_ACTIVE)) {
                headAnchor = pos.add(VectorMath.rotateByYaw(bl ? VectorMath.MirrorXZ(GalathEntity.ENERGY_BALL_OFFSET_LEFT) : GalathEntity.ENERGY_BALL_OFFSET_LEFT, 180.0f + galath.renderYawOffset));
                aim = targetPos.subtract(headAnchor).normalize();
                aim = new Vec3d(aim.x + random.nextDouble() * (double) 0.3f, aim.y + random.nextDouble() * (double) 0.3f, aim.z + random.nextDouble() * (double) 0.3f);
                aim = aim.normalize();
                headVel = new Vec3d(aim.x * (double) 0.4f, aim.y * (double) 0.4f, aim.z * (double) 0.4f);
                energyBallEntity = new EnergyBallEntity(galath.world, galath, headVel);
                energyBallEntity.setPositionAndUpdate(headAnchor.x, headAnchor.y, headAnchor.z);
                galath.world.spawnEntity(energyBallEntity);
            }
            if (galath.getDataManager().get(GalathEntity.IS_LEFT_ENERGY_BALL_ACTIVE)) {
                headAnchor = pos.add(VectorMath.rotateByYaw(bl ? VectorMath.MirrorXZ(GalathEntity.ENERGY_BALL_OFFSET_RIGHT) : GalathEntity.ENERGY_BALL_OFFSET_RIGHT, 180.0f + galath.renderYawOffset));
                aim = targetPos.subtract(headAnchor).normalize();
                aim = new Vec3d(aim.x + random.nextDouble() * (double) 0.3f, aim.y + random.nextDouble() * (double) 0.3f, aim.z + random.nextDouble() * (double) 0.3f);
                aim = aim.normalize();
                headVel = new Vec3d(aim.x * (double) 0.4f, aim.y * (double) 0.4f, aim.z * (double) 0.4f);
                energyBallEntity = new EnergyBallEntity(galath.world, galath, headVel);
                energyBallEntity.setPositionAndUpdate(headAnchor.x, headAnchor.y, headAnchor.z);
                galath.world.spawnEntity(energyBallEntity);
            }
        }
    }, galath -> galath.energyBallChargeTicks >= 45, galath -> {
        galath.energyBallChargeTicks = 0;
    }, true, galath -> galath.witherSkeletons.size() < 2, true),

    ATTACK_SWORD(galath -> {
        galath.setSwordAttackProgress(0);
        galath.setCurrentAction(Action.ATTACK_SWORD);
        galath.setVelocity(Vec3d.ZERO);
        Vec3d pos = galath.getPositionVector();
        galath.setFlightTargetPos(pos);
        Vec3d targetPos = galath.getAttackTarget().getPositionVector();
        Vector2d delta = new Vector2d(targetPos.x - pos.x, targetPos.z - pos.z);
        double yaw = TrigMath.sinDegrees(Math.atan2(delta.y, delta.x)) - 90.0;
        galath.setAnchored(true);
        galath.setTargetPosition(pos);
        galath.setYawRotation((float)yaw);
        GirlEntity.playRandomSound(galath, SoundsHandler.GIRLS_GALATH_STRONGCHARGE, true);
    },
            galath -> {
        EntityLivingBase target = galath.getAttackTarget();
        int attackProgress = galath.getSwordAttackProgress() + 1;
        galath.setSwordAttackProgress(attackProgress);
        if (ThreadNames.isValueInBounds(attackProgress, 24.0, 32.0)) {
            Vec3d eyePos = target.getPositionVector().add(0.0, target.getEyeHeight(), 0.0);
            Vector2d delta2 = new Vector2d(eyePos.x - galath.posX, eyePos.z - galath.posZ);
            double d = TrigMath.sinDegrees(Math.atan2(delta2.y, delta2.x)) - 90.0;
            galath.setYawRotation((float)d);
            Vec3d forward = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 3.0), (float)(d + 180.0));
            Vec3d from = galath.getAnchorTargetPosition();
            Vec3d to = eyePos.add(forward);
            float progress = (float)(attackProgress - 24) / 8.0f;
            Vec3d Lerped = RotationHelper.LerpVec3d(from, to, progress);
            galath.setTargetPosition(Lerped);
        } else if (ThreadNames.isValueInBounds(attackProgress, 32.0, 54.0)) {
            Vec3d behind = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 1.5), galath.getYawRotation() + 180.0f);
            Vec3d targetPos2 = target.getPositionVector().add(behind);
            galath.setTargetPosition(targetPos2);
            GalathDamageSource damageSource = new GalathDamageSource(galath);
            target.hurtTime = 0;
            target.hurtResistantTime = 0;
            if (attackProgress == 36) {
                target.attackEntityFrom(damageSource, 5.0f);
            }
            if (attackProgress == 40) {
                target.attackEntityFrom(damageSource, 5.0f);
            }
        } else if (attackProgress == 54) {
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
    }, galath -> galath.getFlyTicks() > 23, galath -> {
        galath.setFlyTicks(0);
        galath.setVelocity(Vec3d.ZERO);
        galath.setSwordAttackProgress(-1);
        galath.setAnchored(false);
    }, true, galath -> true, false),

    RAPE(galath -> {
        galath.setCurrentAction(Action.RAPE_PREPARE);
        galath.spellCastTimer = 0;
        galath.lastTargetDashPos = null;
        galath.flightTargetPosition = null;
        galath.getDataManager().set(GalathEntity.SPIN_YAW_FACTOR, 0.0f);
    }, galath -> {
        double totalDist;
        boolean isPast;
        double dist;
        Vec3d toPlayer;
        Vec3d playerPos;
        Vec3d dir;
        if (++galath.spellCastTimer >= 48) {
            galath.setCurrentAction(Action.RAPE_CHARGE);
            EntityLivingBase target = galath.getAttackTarget();
            if (galath.lastTargetDashPos == null) {
                galath.flightTargetPosition = target.getPositionVector().add(0.0, target.getEyeHeight() / 2.0f, 0.0);
                galath.lastTargetDashPos = galath.getPositionVector();
                dir = target.getPositionVector().subtract(galath.getPositionVector()).normalize();
                galath.setYawRotation((float) (TrigMath.sinDegrees(Math.atan2(dir.z, dir.x)) - 90.0));
            }
            dir = galath.getPositionVector();
            Vec3d min = dir.subtract(0.65f, 0.65f, 0.65f);
            Vec3d max = dir.add(0.65f, 0.65f, 0.65f);
            AxisAlignedBB aabb = new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z);
            List<EntityPlayer> players = galath.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
            for (EntityPlayer player : players) {
                if (!player.isDead && player.onGround && GirlEntity.getGirlByUUID(player.getPersistentID(), true) == null) {
                    playerPos = player.getPositionVector();
                    toPlayer = dir.subtract(playerPos);
                    Vec3d rotated = VectorMath.rotateByYaw(toPlayer, galath.getYawRotation());
                    dist = Math.abs(rotated.x);
                    if (!(dist > 0.65f)) {
                        for (EntityWitherSkeleton skeleton : galath.witherSkeletons) {
                            Vec3d skeletonPos = skeleton.getPositionVector();
                            skeleton.world.removeEntity(skeleton);
                            PacketHandler.INSTANCE.sendToAllTracking(new SpawnEnergyBallParticlesPacket2(skeletonPos, true), new NetworkRegistry.TargetPoint(skeleton.dimension, skeletonPos.x, skeletonPos.y, skeletonPos.z, 50.0));
                        }
                        galath.witherSkeletons.clear();
                        EntityPlayerMP playerMP = (EntityPlayerMP) player;
                        galath.setTargetPosition(player.getPositionVector());
                        galath.setInteractionPlayerUUID(player.getPersistentID());
                        galath.setAnchored(true);
                        galath.setCurrentAction(Action.RAPE_INTRO);
                        byte yawByte = (byte) MathHelper.floor((galath.getYawRotation() + 180.0f) * 256.0f / 360.0f); //this is only byte usage that I saw
                        PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), playerMP);
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(playerMP.getEntityId(), 0.0, 0.0, 0.0));
                        playerMP.connection.sendPacket(new SPacketEntity.S16PacketEntityLook(playerMP.getEntityId(), yawByte, (byte) -14, true));
                        return;
                    }
                }
            }

            Vec3d from = galath.lastTargetDashPos;
            Vec3d to = galath.flightTargetPosition;
            playerPos = to.subtract(from);
            toPlayer = to.add(playerPos);
            toPlayer = new Vec3d(toPlayer.x, from.y, toPlayer.z);
            isPast = dir.distanceTo(new Vec3d(from.x, dir.y, from.z)) > dir.distanceTo(new Vec3d(toPlayer.x, dir.y, toPlayer.z));
            if (isPast) {
                dist = VectorMath.getLinearFactor(to, toPlayer, dir);
                totalDist = to.distanceTo(toPlayer);
            } else {
                dist = VectorMath.getLinearFactor(from, to, dir);
                totalDist = from.distanceTo(to);
            }
            double steps = totalDist / (double) 0.05f;
            double speedFactor = 1.0 / steps * 20.0;
            dist += speedFactor;
            if (!isPast && dist < (double) 0.9f) {
                galath.flightTargetPosition = target.getPositionVector().add(0.0, target.getEyeHeight() / 2.0f, 0.0);
            }
            dir = isPast ? new Vec3d(RotationHelper.LerpDouble(to.x, toPlayer.x, Math.min(1.0, dist)), RotationHelper.LerpDouble(to.y, toPlayer.y, Math.min(1.0, RotationHelper.EaseInCubic(dist))), RotationHelper.LerpDouble(to.z, toPlayer.z, Math.min(1.0, dist))) : new Vec3d(RotationHelper.LerpDouble(from.x, to.x, dist), RotationHelper.LerpDouble(from.y, to.y, RotationHelper.EaseOutCubic(dist)), RotationHelper.LerpDouble(from.z, to.z, dist));
            galath.setPosition(dir.x, dir.y, dir.z);
            if (isPast) {
                galath.getDataManager().set(GalathEntity.SPIN_YAW_FACTOR, (float) dist);
            }
        } else {
        }
    }, galath -> {
        if (galath.getCurrentAction() == Action.RAPE_INTRO) {
            return true;
        }

        Vec3d from = galath.lastTargetDashPos;
        Vec3d to = galath.flightTargetPosition;
        if (from == null) {
            return false;
        }
        Vec3d delta = to.subtract(from);
        Vec3d newPos = to.add(delta);
        newPos = new Vec3d(newPos.x, from.y, newPos.z);
        return galath.getDistance(newPos.x, newPos.y, newPos.z) < (double)0.1f;
    }, galath -> {
        galath.flightTargetPosition = null;
        galath.lastTargetDashPos = null;
        galath.spellCastTimer = 0;
        galath.getDataManager().set(GalathEntity.SPIN_YAW_FACTOR, 0.0f);
    }, true, galath -> true, true);

    final IGalathUpdate onUpdateAction;
    final IGalathStart onStartAction;
    final IGalathFinish isFinishedCondition;
    final IGalathStop onStopAction;
    final IGalathExecute canExecuteAction;
    final public boolean applyAttackCoolDown;
    final public boolean onlyDoThisOnPlayers;

    GalathFlightData(IGalathStart onStartAction, IGalathFinish isFinishedCondition, IGalathUpdate onUpdateAction, IGalathStop onStopAction, boolean applyAttackCoolDown, IGalathExecute canExecuteAction, boolean onlyDoThisOnPlayers) {
        this.onUpdateAction = onUpdateAction;
        this.onStartAction = onStartAction;
        this.isFinishedCondition = isFinishedCondition;
        this.onStopAction = onStopAction;
        this.applyAttackCoolDown = applyAttackCoolDown;
        this.canExecuteAction = canExecuteAction;
        this.onlyDoThisOnPlayers = onlyDoThisOnPlayers;
    }

    public void executeStart(GalathEntity galath) {
        this.onStartAction.start(galath);
    }

    public boolean executeUpdate(GalathEntity galath) {
        return this.onUpdateAction.execute(galath);
    }

    public void checkFinished(GalathEntity galath) {
        this.isFinishedCondition.finish(galath);
    }

    public void executeStop(GalathEntity galath) {
        this.onStopAction.stop(galath);
    }

    public boolean canExecute(GalathEntity galath) {
        return this.canExecuteAction.canExecute(galath);
    }
}

