/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Pyrocynical;

import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PyrocynicalEntity extends EntityLiving {
    final static public long DISAPPEAR_DELAY = 60000L;
    final static public float STOP_DISTANCE_TO_PLAYER = 3.0f;
    final static float MAX_WANDER_RADIUS = 30.0f;
    final static int MAX_STUCK_TICKS = 175;
    final static int MOVEMENT_RANGE = 10;
    BlockPos targetWanderPos = null;
    int stuckTicksCounter = 0;
    boolean isDissapearing = false;
    public int triggerTick = -1;

    public PyrocynicalEntity(World world) {
        super(world);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.updateWanderAndFollowAI();
    }

    void updateWanderAndFollowAI() {
        if (this.isDissapearing) {
            this.getNavigator().clearPath();
            return;
        }

        EntityPlayer closestPlayer = this.world.getClosestPlayerToEntity(this, 15.0);

        if (closestPlayer != null && closestPlayer.getDistance(this) < STOP_DISTANCE_TO_PLAYER) {
            this.getNavigator().clearPath();
            return;
        }

        if (this.targetWanderPos == null || this.getDistance(this.targetWanderPos.getX(), this.targetWanderPos.getY(), this.targetWanderPos.getZ()) > this.getMaxWanderDistance() || this.stuckTicksCounter > MAX_STUCK_TICKS) {
            int offsetX = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(MOVEMENT_RANGE);
            int offsetZ = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(MOVEMENT_RANGE);
            int targetY = this.world.provider.getDimensionType() == DimensionType.NETHER ? (int)Math.ceil(this.posY) : WorldUtils.getSurfaceHeight(this.world, this.getPosition().getX() + offsetX, this.getPosition().getZ() + offsetZ);
            this.targetWanderPos = new BlockPos(this.getPosition().getX() + offsetX, targetY, this.getPosition().getZ() + offsetZ);
            this.stuckTicksCounter = 0;
        }
        if (Math.sqrt(this.targetWanderPos.distanceSq(this.getPosition())) > 2.0) {
            this.getNavigator().tryMoveToXYZ(this.targetWanderPos.getX(), this.targetWanderPos.getY(), this.targetWanderPos.getZ(), 0.35f);
            this.adjustInAirVelocity();
        } else {
            ++this.stuckTicksCounter;
        }
    }

    protected void adjustInAirVelocity() {
        Path currentPath = this.getNavigator().getPath();
        if (currentPath == null) {
            return;
        }
        if (this.onGround || this.isInWater()) {
            return;
        }
        int currentPathIndex = currentPath.getCurrentPathIndex();
        int currentPathLength = currentPath.getCurrentPathLength();
        if (currentPathLength == currentPathIndex || currentPathLength - 1 == currentPathIndex) {
            return;
        }
        PathPoint currentPoint = currentPath.getPathPointFromIndex(currentPathIndex);
        PathPoint nextPoint = currentPath.getPathPointFromIndex(currentPathIndex + 1);
        Vec3d direction = new Vec3d(nextPoint.x - currentPoint.x, nextPoint.y - currentPoint.y, nextPoint.z - currentPoint.z);
        this.motionX = direction.x / 7.0;
        this.motionZ = direction.z / 7.0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            this.world.removeEntity(this);
            return true;
        }
        if (!(source.getTrueSource() instanceof EntityPlayer)) {
            return false;
        }
        if (this.world.isRemote) {
            this.playDissapearEffects();
        }
        this.isDissapearing = true;
        Utils.runDelayedTask(6250, () -> this.world.removeEntity(this));
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    void playDissapearEffects() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        this.triggerTick = player.ticksExisted;
        ((EntityPlayer)player).playSound(SoundsHandler.MISC_WEOWEO[3], 1.0f, 1.0f);
    }

    double getMaxWanderDistance()    {
        return Math.sqrt(1800.0);
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.getRNG().nextInt(100) < 1 && this.getRNG().nextInt(100) < 10) {
            return true;
        }
        this.world.removeEntity(this);
        return false;
    }
}

