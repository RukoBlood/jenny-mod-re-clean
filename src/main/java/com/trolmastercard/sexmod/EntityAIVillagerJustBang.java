/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.BabyEntitySpawnEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntityAIVillagerJustBang
extends EntityAIBase {
    final private EntityVillager c;
    private EntityVillager d;
    final private World a;
    private int b;

    public EntityAIVillagerJustBang(EntityVillager entityVillager) {
        this.c = entityVillager;
        this.a = entityVillager.world;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.b != 0) {
            return false;
        }
        EntityVillager entityVillager = this.a.findNearestEntityWithinAABB(EntityVillager.class, this.c.getEntityBoundingBox().grow(8.0, 3.0, 8.0), this.c);
        if (entityVillager == null) {
            return false;
        }
        this.d = entityVillager;
        return true;
    }

    @Override
    public void startExecuting() {
        this.b = 300;
        this.c.setMating(true);
    }

    @Override
    public void resetTask() {
    }

    @Override
    public boolean shouldContinueExecuting() {
        return true;
    }

    @Override
    public void updateTask() {
        --this.b;
        this.c.getLookHelper().setLookPositionWithEntity(this.d, 10.0f, 30.0f);
        if (this.c.getDistanceSq(this.d) > 2.25) {
            this.c.getNavigator().tryMoveToEntityLiving(this.d, 0.25);
        }
        if (this.b <= 0) {
            this.a();
            this.c.tasks.removeTask(this);
        }
        if (this.c.getRNG().nextInt(35) == 0) {
            this.a.setEntityState(this.c, (byte)12);
        }
    }

    private void a() {
        EntityAgeable entityAgeable = this.c.createChild(this.d);
        this.d.setGrowingAge(6000);
        this.c.setGrowingAge(6000);
        this.d.setIsWillingToMate(false);
        this.c.setIsWillingToMate(false);
        BabyEntitySpawnEvent babyEntitySpawnEvent = new BabyEntitySpawnEvent((EntityLiving)this.c, (EntityLiving)this.d, entityAgeable);
        if (MinecraftForge.EVENT_BUS.post((Event)babyEntitySpawnEvent) || babyEntitySpawnEvent.getChild() == null) {
            return;
        }
        entityAgeable = babyEntitySpawnEvent.getChild();
        entityAgeable.setGrowingAge(-24000);
        entityAgeable.setLocationAndAngles(this.c.posX, this.c.posY, this.c.posZ, 0.0f, 0.0f);
        this.a.spawnEntity(entityAgeable);
        this.a.setEntityState(entityAgeable, (byte)12);
    }
}

