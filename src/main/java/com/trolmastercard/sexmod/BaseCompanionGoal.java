/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class BaseCompanionGoal extends EntityAIBase {
    public GirlEntity entity;
    public EntityPlayer player;
    public PathNavigate pathNavigate;
    public EntityDataManager dataManager;
    public States CurState = States.IDLE;
    final static public double BASE_WALK_SPEED = 0.5;
    final static public double BASE_RUN_SPEED = 0.7;
    final static public int DEATH_PREVENTION_TICKS = 60;

    public BaseCompanionGoal(GirlEntity girl) {
        this.entity = girl;
        this.pathNavigate = girl.getNavigator();
        this.dataManager = girl.getDataManager();
    }

    protected void goNearPlayer() {
        BlockPos targetPos;
        int attempts = 0;

        do {
            targetPos = this.player.getPosition().add(Reference.RANDOM.nextInt(10), 0, Reference.RANDOM.nextInt(10));
        }
        while (++attempts < 20 && !this.entity.attemptTeleport(targetPos.getX(), targetPos.getY(), targetPos.getZ()));

        if (attempts >= 20) {
            this.entity.setPosition(this.player.posX, this.player.posY, this.player.posZ);
        }

        this.entity.motionX = 0.0;
        this.entity.motionY = 0.0;
        this.entity.motionZ = 0.0;
    }

    protected double setCompanionSpeed() {
        GirlEntity.WalkTypes walkTypes;
        double speed;
        float distance = this.entity.getDistance(this.player);

        if (this.player.isSprinting()) {
            speed = BASE_RUN_SPEED;
            walkTypes = GirlEntity.WalkTypes.RUN;
        }
        else {
            speed = BASE_WALK_SPEED;
            walkTypes = GirlEntity.WalkTypes.WALK;
        }

        double distBonus = Math.floor(distance / 5.0f) * 0.2;
        speed += distBonus;

        if (this.entity.isInWater()) {
            speed *= 60.0;
            walkTypes = GirlEntity.WalkTypes.WALK;
        }

        this.pathNavigate.setSpeed(speed);
        this.entity.setWalkType(walkTypes);
        return speed;
    }

    @Override
    public void resetTask() {
        this.pathNavigate.clearPath();
        this.CurState = States.IDLE;
        this.entity.setCurrentAction(Action.NULL);
        this.dataManager.set(GirlEntity.MASTER_UUID, "");
        this.pathNavigate = null;
        this.dataManager = null;
        this.player = null;
    }

    @Override
    public boolean shouldExecute() {
        return !this.entity.getDataManager().get(GirlEntity.MASTER_UUID).isEmpty();
    }

    @Override
    public boolean shouldContinueExecuting() {
        String string = this.dataManager.get(GirlEntity.MASTER_UUID);
        return !string.isEmpty() && this.entity.world.getPlayerEntityByUUID(UUID.fromString(string)) != null;
    }

    @Override
    public void startExecuting() {
        this.pathNavigate = this.entity.getNavigator();
        this.dataManager = this.entity.getDataManager();
        this.player = this.entity.world.getPlayerEntityByUUID(UUID.fromString(this.dataManager.get(GirlEntity.MASTER_UUID)));
    }

    @Override
    public void updateTask() {
        this.CurState = this.getNewState();
        if (this.entity.followPlayerGoal != null) {
            this.entity.followPlayerGoal.a = this.CurState == States.IDLE;
        }
        this.CompanionStates(this.CurState);
    }

    protected abstract States getNewState();

    protected abstract void CompanionStates(States states);

    @SubscribeEvent
    public void onGirlDeath(LivingDeathEvent event) {
        GirlEntity girl;
        if (event.getEntityLiving() instanceof GirlEntity && !(girl = (GirlEntity) event.getEntityLiving()).getDataManager().get(GirlEntity.MASTER_UUID).isEmpty()) {
            event.setCanceled(true);
        }
    }

    public static enum States {
        ATTACK,
        FOLLOW,
        IDLE,
        RIDE,
        DOWNED;

    }
}

