/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingHealEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.companion.fighter;

import com.google.common.collect.Multimap;

import java.util.List;

import com.trolmastercard.sexmod.companion.CompanionBase;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FighterCompanion extends CompanionBase {
    Fighter girl;
    EntityLivingBase target;
    Entity ridingEntity;
    double lastDistance = 3.4028234663852886E38;
    Vec3d lastMasterPos = Vec3d.ZERO;
    int idlePosChangeTick;
    int attackModeCoolDown;
    int attackCoolDown;
    int chargingTicks;
    int shouldntFollowAnymoreTick;

    public FighterCompanion(Fighter girl) {
        super(girl);
        this.girl = girl;
        idlePosChangeTick = 0;
        attackModeCoolDown = 0;
        attackCoolDown = 0;
        chargingTicks = 0;
        shouldntFollowAnymoreTick = 0;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.lastDistance = this.girl.getDistance(this.master);
        this.lastMasterPos = this.master.getPositionVector();
        if (this.girl.currentAction() == Action.BOW) {
            this.girl.setCurrentAction(Action.NULL);
        }
    }

    boolean shouldAttackTarget(EntityLivingBase target) {
        Vec3d girlPos = this.girl.getPositionVector();
        return !(target instanceof GirlEntity)
                && this.attackModeCoolDown <= 0
                && target != null
                && target.world != null
                && !this.girl.equals(target)
                && target.isEntityAlive()
                && girlPos.distanceTo(this.master.getPositionVector()) < 15.0
                && girlPos.distanceTo(target.getPositionVector()) < 20.0
                && !target.equals(this.master);
    }

    @Override
    protected void CompanionStates(Mode mode) {
        switch (mode) {
            case ATTACK: {
                this.girl.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                double distance = this.girl.getDistance(this.target);
                this.navigator.clearPath();
                if (distance < 1.9 && --this.attackCoolDown <= 0) {
                    this.attack();
                    break;
                }

                if (this.girl.inventory.getStackInSlot(1).getItem() instanceof ItemBow && this.girl.getEntitySenses().canSee(this.target) && ++this.chargingTicks > 0 && distance > 6.0) {
                    this.dataManager.set(Fighter.ATTACK_MODE, 2);
                    this.girl.setCurrentAction(Action.BOW);
                    if (++this.chargingTicks >= 32) {
                        this.chargingTicks = -20;
                        this.attackTargetWithRangedAttack();
                        this.girl.setCurrentAction(Action.NULL);
                    }
                    this.lastDistance = this.girl.getDistance(this.master);
                    this.lastMasterPos = this.master.getPositionVector();
                    return;
                }

                if (distance < 2.0) {
                    this.dataManager.set(Fighter.ATTACK_MODE, 1);
                    this.navigator.tryMoveToEntityLiving(this.target, 0.5);
                    this.girl.setWalkSpeed(GirlEntity.WalkSpeed.WALK);
                    break;
                }
                this.dataManager.set(Fighter.ATTACK_MODE, 1);
                this.navigator.tryMoveToEntityLiving(this.target, 0.7);
                this.girl.setWalkSpeed(GirlEntity.WalkSpeed.RUN);
                break;
            }

            case FOLLOW: {
                this.dataManager.set(Fighter.ATTACK_MODE, 0);
                double distance = this.girl.getDistance(this.master);
                if ((double)this.navigator.getPathSearchRange() > distance) {
                    this.navigator.clearPath();
                    if (!this.girl.downed) {
                        this.navigator.tryMoveToEntityLiving(this.master, 0.5);
                        this.void_a(); //not in 1.5.2 version of mod
                    }
                } else {
                    this.tpToPlayer();
                }
                this.idlePosChangeTick = 300;
                this.setMovementSpeed();
                break;
            }

            case IDLE: {
                this.dataManager.set(Fighter.ATTACK_MODE, 0);
                if (!this.girl.downed) {
                    if (++this.idlePosChangeTick > 200 + Reference.RANDOM.nextInt(100)) {
                        this.idlePosChangeTick = 0;
                        Vec3d masterPos = this.master.getPositionVector();
                        Vec3d idlePos = new Vec3d(masterPos.x + 1.0 + (double)(Reference.RANDOM.nextFloat() * 3.0f), masterPos.y, masterPos.z + 1.0 + (double)(Reference.RANDOM.nextFloat() * 3.0f));
                        this.navigator.clearPath();
                        this.navigator.tryMoveToXYZ(idlePos.x, idlePos.y, idlePos.z, 0.5);
                    }
                    this.setMovementSpeed();
                    break;
                }
                else if (!(this.girl.getDistance(this.master) > 10.0f)) {
                    this.tpToPlayer();
                }
                break;
            }
            case RIDE: {
                if (this.girl.isRiding()) {
                    this.girl.setCurrentAction(Action.SIT);
                    break;
                }
                this.girl.setNoGravity(true);
                this.girl.noClip = true;
                Vec3d vec3d = this.master.getPositionVector().subtract(this.ridingEntity.getLookVec().x * 0.5, 0.0, this.ridingEntity.getLookVec().z * 0.5);
                this.girl.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, 0.0f, 0.0f);
                this.girl.motionX = 0.0;
                this.girl.motionY = 0.0;
                this.girl.motionZ = 0.0;
                this.girl.setCurrentAction(Action.RIDE);
                break;
            }
            case DOWNED: {
                this.navigator.clearPath();
            }
        }
    }

    @Override
    protected Mode updateMode() {
        float f = this.girl.getDistance(this.master);
        boolean bl = f > 5.0f;
        //Entity entity;
        --this.attackModeCoolDown;
        if (this.girl.downed || this.girl.playerSheHasSexWith() != null) {
            return Mode.DOWNED;
        }
        if (this.master.isRiding()) {
            Entity ridingEntity1 = this.master.getRidingEntity();
            if (this.girl.isRiding() || this.girl.startRiding(ridingEntity1) || ridingEntity1 instanceof EntityHorse && ((EntityHorse)ridingEntity1).isHorseSaddled()) {
                this.ridingEntity = ridingEntity1;
                return Mode.RIDE;
            }
        } else if (!this.master.isRiding() && this.girl.isRiding() || this.CurState == Mode.RIDE && !this.master.isRiding()) {
            this.girl.setCurrentAction(Action.NULL);
            this.girl.dismountRidingEntity();
            this.girl.noClip = false;
            this.girl.setNoGravity(false);
        }
        if (this.shouldAttackTarget(this.target)) {
            return Mode.ATTACK;
        }
        DamageSource damageSource = this.girl.getLastDamageSource();
        if (damageSource != null) {
            EntityLivingBase entity = (EntityLivingBase) damageSource.getTrueSource();
            if (this.shouldAttackTarget(entity)) {
                this.target = entity;
                return Mode.ATTACK;
            }
        }
        EntityLivingBase entity = this.master.getLastAttackedEntity();
        if (this.master.ticksExisted - this.master.getLastAttackedEntityTime() < 140 && this.shouldAttackTarget((EntityLivingBase)entity)) {
            this.target = entity;
            return Mode.ATTACK;
        }
        if (this.CurState != Mode.FOLLOW) {
            damageSource = this.master.getLastDamageSource();
            if (damageSource != null && this.shouldAttackTarget((EntityLivingBase)(entity = (EntityLivingBase)damageSource.getTrueSource()))) {
                this.target = entity;
                return Mode.ATTACK;
            }
            Vec3d vec3d = this.girl.getPositionVector();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d.x - 5.0, vec3d.y - 2.0, vec3d.z - 5.0, vec3d.x + 5.0, vec3d.y + 2.0, vec3d.z + 5.0);
            List<EntityMob> list = this.girl.world.getEntitiesWithinAABB(EntityMob.class, axisAlignedBB);
            list.sort((entityMob, entityMob2) -> {
                double d;
                double d2 = entityMob.getDistance(this.girl);
                if (d2 == (d = (double)entityMob2.getDistance(this.girl))) {
                    return 0;
                }
                return d2 < d ? -1 : 1;
            });
            for (EntityMob entityMob3 : list) {
                if (!this.shouldAttackTarget(entityMob3) || entityMob3 instanceof EntityCreeper) continue;
                this.target = entityMob3;
                return Mode.ATTACK;
            }
        }

        if (!bl && this.CurState == Mode.FOLLOW) {
            if (++this.shouldntFollowAnymoreTick > 60) {
                bl = false;
                this.shouldntFollowAnymoreTick = 0;
            } else {
                bl = true;
            }
        }
        if (bl && this.CurState == Mode.ATTACK) {
            this.attackModeCoolDown = 60;
        }
        if (bl) {
            return Mode.FOLLOW;
        }
        return Mode.IDLE;
    }

    public void attackTargetWithRangedAttack() {
        EntityArrow arrow = this.getArrow();
        double distance = this.target.posX - this.girl.posX;
        double d2 = this.target.getEntityBoundingBox().minY + (double)(this.target.height / 3.0f) - arrow.posY;
        double d3 = this.target.posZ - this.girl.posZ;
        double hypotenuse = MathHelper.sqrt(distance * distance + d3 * d3);
        arrow.shoot(distance, d2 + hypotenuse * (double)0.2f, d3, 1.6f, 2.0f);
        this.girl.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.girl.getRNG().nextFloat() * 0.4f + 0.8f));
        this.girl.world.spawnEntity(arrow);
        arrow.setDamage(4.5);
    }

    protected EntityArrow getArrow() {
        EntityTippedArrow entityTippedArrow = new EntityTippedArrow(this.girl.world, this.girl);
        ItemStack bow = this.girl.inventory.getStackInSlot(1);
        double power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);
        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);
        int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow);
        if (power != 0.0) {
            entityTippedArrow.setDamage(entityTippedArrow.getDamage() + power * 0.5 + 0.5);
        }
        if (punch != 0) {
            entityTippedArrow.setKnockbackStrength(punch);
        }
        if (flame != 0) {
            entityTippedArrow.setFire(100);
        }
        return entityTippedArrow;
    }

    void attack() {
        this.girl.setCurrentAction(Action.ATTACK);
        this.dataManager.set(Fighter.ATTACK_MODE, 1);
        ItemStack weapon = this.girl.inventory.getStackInSlot(0);
        Multimap<String, AttributeModifier> modifiers = weapon.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        float damage = 0.0f;
        float attackSpeed = 0.0f;
        for (AttributeModifier attributeModifier : modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
            damage = (float)attributeModifier.getAmount();
        }
        for (AttributeModifier attributeModifier : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
            attackSpeed = (float)attributeModifier.getAmount();
        }

        attackSpeed = Math.max(attackSpeed, 0.5f);

        float extraDamage = EnchantmentHelper.getModifierForCreature(weapon, this.target.getCreatureAttribute());
        int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, weapon);
        int fire_aspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, weapon);
        int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, weapon);
        this.target.knockBack(this.girl, (float)knockback * 0.5f, MathHelper.sin(this.girl.rotationYaw * ((float)Math.PI / 180)), -MathHelper.cos(this.girl.rotationYaw * ((float)Math.PI / 180)));
        this.target.setFire(fire_aspect * 4);
        if (sweeping != 0) {
            float f4 = 0.5f;
            if (sweeping == 2) {
                f4 = 0.67f;
            } else if (sweeping == 3) {
                f4 = 0.75f;
            }
            for (EntityLivingBase entityLivingBase : this.girl.world.getEntitiesWithinAABB(EntityLivingBase.class, this.target.getEntityBoundingBox().grow(1.0, 0.25, 1.0))) {
                if (entityLivingBase == this.girl || entityLivingBase == this.master || entityLivingBase == this.target || this.girl.isOnSameTeam(entityLivingBase) || !(this.girl.getDistanceSq(entityLivingBase) < 9.0)) continue;
                entityLivingBase.knockBack(this.girl, 0.4f, MathHelper.sin(this.girl.rotationYaw * ((float)Math.PI / 180)), -MathHelper.cos(this.girl.rotationYaw * ((float)Math.PI / 180)));
                entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this.girl), (damage + extraDamage) * f4);
            }
        }
        this.target.attackEntityFrom(DamageSource.causeMobDamage(this.girl), damage + extraDamage);
        this.attackCoolDown = Math.round(Math.abs(attackSpeed) / 3.373494f * 20.0f);
    }

    @Override
    protected double setMovementSpeed() {
        double speed = super.setMovementSpeed();
        if (this.girl.downed) {
            speed = 0.0;
        }
        this.navigator.setSpeed(speed);
        this.girl.setWalkSpeed(this.girl.getWalkType());
        return speed;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.girl.getDataManager().set(Fighter.ATTACK_MODE, 0);
    }

    void void_a() {
        if (this.girl.onGround || this.girl.isInWater() || this.girl.motionX + this.girl.motionZ != 0.0 || this.girl.motionY <= 0.0) {
            return;
        }
        Vec3d vec3d = new Vec3d(0.0, 0.0, 0.1f);
        vec3d = VectorMath.rotate(vec3d, this.girl.rotationYaw);
        this.girl.motionX = vec3d.x;
        this.girl.motionZ = vec3d.z;
    }

    public static class EventHandler {
        @SubscribeEvent
        public void downed(LivingHurtEvent event) {
            if (event.getEntityLiving() instanceof Fighter) {
                Fighter girl = (Fighter)event.getEntityLiving();
                if (girl.downed) {
                    event.setCanceled(true);
                } else if (girl.getHealth() - event.getAmount() < 0.0f && !((String) girl.getDataManager().get(Fighter.MASTER_UUID)).isEmpty()) {
                    girl.downed = true;
                    girl.setCurrentAction(Action.DOWNED);
                    event.setAmount(girl.getHealth() - 1.0f);
                    girl.getNavigator().clearPath();
                }
            }
        }

        @SubscribeEvent
        public void healDowned(LivingHealEvent event) {
            if (event.getEntityLiving() instanceof Fighter) {
                Fighter currentFighter = (Fighter)event.getEntityLiving();
                if (currentFighter.downed && currentFighter.getHealth() + event.getAmount() >= currentFighter.getMaxHealth()) {
                    currentFighter.downed = false;
                    currentFighter.setCurrentAction(Action.NULL);
                }
            }
        }

        @SubscribeEvent
        public void DropItems(LivingDeathEvent event) {
            if (event.getEntityLiving() instanceof Fighter) {
                Fighter thisfighter = (Fighter)event.getEntityLiving();
                if (thisfighter.world.isRemote) {
                    return;
                }
                for (int i = 0; i < 6; ++i) {
                    Item item = thisfighter.inventory.getStackInSlot(i).getItem();
                    if (item == Items.AIR) continue;
                    thisfighter.dropItem(item, 1);
                }
            }
        }
    }
}

