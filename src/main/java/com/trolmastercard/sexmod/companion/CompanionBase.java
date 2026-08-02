package com.trolmastercard.sexmod.companion;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class CompanionBase extends EntityAIBase {
    public GirlEntity entity;
    public EntityPlayer master;
    public PathNavigate navigator;
    public EntityDataManager dataManager;
    public Mode CurState = Mode.IDLE;
    final static public double BASE_WALK_SPEED = 0.5;
    final static public double BASE_RUN_SPEED = 0.7;
    final static public int DEATH_PREVENTION_TICKS = 60;

    public CompanionBase(GirlEntity girl) {
        this.entity = girl;
        this.navigator = girl.getNavigator();
        this.dataManager = girl.getDataManager();
    }

    protected void tpToPlayer() {
        BlockPos targetPos;
        int attempts = 0;

        do {
            targetPos = this.master.getPosition().add(Reference.RANDOM.nextInt(10), 0, Reference.RANDOM.nextInt(10));
        }
        while (++attempts < 20 && !this.entity.attemptTeleport(targetPos.getX(), targetPos.getY(), targetPos.getZ()));

        if (attempts >= 20) {
            this.entity.setPosition(this.master.posX, this.master.posY, this.master.posZ);
        }

        this.entity.motionX = 0.0;
        this.entity.motionY = 0.0;
        this.entity.motionZ = 0.0;
    }

    protected double setMovementSpeed() {
        GirlEntity.WalkSpeed walkSpeed;
        double speed;
        float distance = this.entity.getDistance(this.master);

        if (this.master.isSprinting()) {
            speed = BASE_RUN_SPEED;
            walkSpeed = GirlEntity.WalkSpeed.RUN;
        }
        else {
            speed = BASE_WALK_SPEED;
            walkSpeed = GirlEntity.WalkSpeed.WALK;
        }

        double distBonus = Math.floor(distance / 5.0f) * 0.2;
        speed += distBonus;

        if (this.entity.isInWater()) {
            speed *= 60.0;
            walkSpeed = GirlEntity.WalkSpeed.WALK;
        }

        this.navigator.setSpeed(speed);
        this.entity.setWalkSpeed(walkSpeed);
        return speed;
    }

    @Override
    public void resetTask() {
        this.navigator.clearPath();
        this.CurState = Mode.IDLE;
        this.entity.setCurrentAction(Action.NULL);
        this.dataManager.set(GirlEntity.MASTER_UUID, "");
        this.navigator = null;
        this.dataManager = null;
        this.master = null;
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
        this.navigator = this.entity.getNavigator();
        this.dataManager = this.entity.getDataManager();
        this.master = this.entity.world.getPlayerEntityByUUID(UUID.fromString(this.dataManager.get(GirlEntity.MASTER_UUID)));
    }

    @Override
    public void updateTask() {
        this.CurState = this.updateMode();
        if (this.entity.aiLookAtPlayer != null) {
            this.entity.aiLookAtPlayer.ShouldLook = this.CurState == Mode.IDLE;
        }
        this.CompanionStates(this.CurState);
    }

    protected abstract Mode updateMode();

    protected abstract void CompanionStates(Mode mode);

    @SubscribeEvent
    public void onGirlDeath(LivingDeathEvent event) {
        GirlEntity girl;
        if (event.getEntityLiving() instanceof GirlEntity && !(girl = (GirlEntity) event.getEntityLiving()).getDataManager().get(GirlEntity.MASTER_UUID).isEmpty()) {
            event.setCanceled(true);
        }
    }

    public static enum Mode {
        ATTACK,
        FOLLOW,
        IDLE,
        RIDE,
        DOWNED;

    }
}

