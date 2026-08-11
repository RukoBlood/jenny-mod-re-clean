/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.BabyEntitySpawnEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.trolmastercard.sexmod.gender_change.hornypotion;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/*
* Breed Villagers when horny potion is used on them
* */
public class EntityAIVillagerJustBangHerWithoutThinking extends EntityAIBase {
    final private EntityVillager villager;
    private EntityVillager targetVillager;
    final private World world;
    private int b;

    public EntityAIVillagerJustBangHerWithoutThinking(EntityVillager villager) {
        this.villager = villager;
        this.world = villager.world;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.b != 0) {
            return false;
        }
        EntityVillager villagersInArea = this.world.findNearestEntityWithinAABB(EntityVillager.class, this.villager.getEntityBoundingBox().grow(8.0, 3.0, 8.0), this.villager);
        if (villagersInArea == null) {
            return false;
        }
        this.targetVillager = villagersInArea;
        return true;
    }

    @Override
    public void startExecuting() {
        this.b = 300;
        this.villager.setMating(true);
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
        this.villager.getLookHelper().setLookPositionWithEntity(this.targetVillager, 10.0f, 30.0f);
        if (this.villager.getDistanceSq(this.targetVillager) > 2.25) {
            this.villager.getNavigator().tryMoveToEntityLiving(this.targetVillager, 0.25);
        }
        if (this.b <= 0) {
            this.spawnBaby();
            this.villager.tasks.removeTask(this);
        }
        if (this.villager.getRNG().nextInt(35) == 0) {
            this.world.setEntityState(this.villager, (byte)12);
        }
    }

    private void spawnBaby() {
        EntityAgeable entityAgeable = this.villager.createChild(this.targetVillager);
        this.targetVillager.setGrowingAge(6000);
        this.villager.setGrowingAge(6000);
        this.targetVillager.setIsWillingToMate(false);
        this.villager.setIsWillingToMate(false);
        BabyEntitySpawnEvent babyEntitySpawnEvent = new BabyEntitySpawnEvent((EntityLiving)this.villager, (EntityLiving)this.targetVillager, entityAgeable);
        if (MinecraftForge.EVENT_BUS.post((Event)babyEntitySpawnEvent) || babyEntitySpawnEvent.getChild() == null) {
            return;
        }
        entityAgeable = babyEntitySpawnEvent.getChild();
        entityAgeable.setGrowingAge(-24000);
        entityAgeable.setLocationAndAngles(this.villager.posX, this.villager.posY, this.villager.posZ, 0.0f, 0.0f);
        this.world.spawnEntity(entityAgeable);
        this.world.setEntityState(entityAgeable, (byte)12);
    }
}

