/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.UUID;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class f_class282 extends EntityAIBase {
    public GirlEntity entity;
    public EntityPlayer player;
    public PathNavigate pathNavigate;
    public EntityDataManager dataManager;
    public States f = States.IDLE;
    final static public double g = 0.5;
    final static public double h = 0.7;
    final static public int b = 60;

    public f_class282(GirlEntity girlEntity) {
        this.entity = girlEntity;
        this.pathNavigate = girlEntity.getNavigator();
        this.dataManager = girlEntity.getDataManager();
    }

    protected void c() {
        BlockPos blockPos;
        int n = 0;
        do {
            blockPos = this.player.getPosition().add(ModInfo.f.nextInt(10), 0, ModInfo.f.nextInt(10));
        } while (++n < 20 && !this.entity.attemptTeleport(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        if (n >= 20) {
            this.entity.setPosition(this.player.posX, this.player.posY, this.player.posZ);
        }
        this.entity.motionX = 0.0;
        this.entity.motionY = 0.0;
        this.entity.motionZ = 0.0;
    }

    protected double double_b() {
        GirlEntity.WalkTypes walkTypes;
        double d;
        float distance = this.entity.getDistance(this.player);
        if (this.player.isSprinting()) {
            d = 0.7;
            walkTypes = GirlEntity.WalkTypes.RUN;
        } else {
            d = 0.5;
            walkTypes = GirlEntity.WalkTypes.WALK;
        }
        double d2 = Math.floor(distance / 5.0f) * 0.2;
        d += d2;
        if (this.entity.isInWater()) {
            d *= 60.0;
            walkTypes = GirlEntity.WalkTypes.WALK;
        }
        this.pathNavigate.setSpeed(d);
        this.entity.a(walkTypes);
        return d;
    }

    @Override
    public void resetTask() {
        this.pathNavigate.clearPath();
        this.f = States.IDLE;
        this.entity.setCurrentAction(Action.NULL);
        this.dataManager.set(GirlEntity.v, "");
        this.pathNavigate = null;
        this.dataManager = null;
        this.player = null;
    }

    @Override
    public boolean shouldExecute() {
        return !this.entity.getDataManager().get(GirlEntity.v).equals("");
    }

    @Override
    public boolean shouldContinueExecuting() {
        String string = this.dataManager.get(GirlEntity.v);
        return !string.equals("") && this.entity.world.getPlayerEntityByUUID(UUID.fromString(string)) != null;
    }

    @Override
    public void startExecuting() {
        this.pathNavigate = this.entity.getNavigator();
        this.dataManager = this.entity.getDataManager();
        this.player = this.entity.world.getPlayerEntityByUUID(UUID.fromString(this.dataManager.get(GirlEntity.v)));
    }

    @Override
    public void updateTask() {
        this.f = this.abstractStates();
        if (this.entity.o != null) {
            this.entity.o.a = this.f == States.IDLE;
        }
        this.CompanionStates(this.f);
    }

    protected abstract States abstractStates();

    protected abstract void CompanionStates(States states);

    @SubscribeEvent
    public void a(LivingDeathEvent livingDeathEvent) {
        GirlEntity em_class2582;
        if (livingDeathEvent.getEntityLiving() instanceof GirlEntity && !(em_class2582 = (GirlEntity)livingDeathEvent.getEntityLiving()).getDataManager().get(GirlEntity.v).equals("")) {
            livingDeathEvent.setCanceled(true);
        }
    }

    private static RuntimeException b(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static enum States {
        ATTACK,
        FOLLOW,
        IDLE,
        RIDE,
        DOWNED;

    }
}

