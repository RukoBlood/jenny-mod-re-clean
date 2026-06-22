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
package com.trolmastercard.sexmod;

import com.google.common.collect.Multimap;

import java.util.List;

import com.trolmastercard.sexmod.girls.GirlEntity;
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

public class FighterAI extends f_class282 {
    Fighter fighter;
    EntityLivingBase target;
    Entity o;
    double l = 3.4028234663852886E38;
    Vec3d i = Vec3d.ZERO;
    int j = 0;
    int n = 0;
    int k = 0;
    int p = 0;
    int m = 0;

    public FighterAI(Fighter fighter) {
        super(fighter);
        this.fighter = fighter;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.l = this.fighter.getDistance(this.player);
        this.i = this.player.getPositionVector();
        if (this.fighter.currentAction() == Action.BOW) {
            this.fighter.setCurrentAction(Action.NULL);
        }
    }

    boolean CompanionStates(EntityLivingBase entityLivingBase) {
        Vec3d positionVector = this.fighter.getPositionVector();
        return !(entityLivingBase instanceof GirlEntity) && this.n <= 0 && entityLivingBase != null && entityLivingBase.world != null && !this.fighter.equals(entityLivingBase) && entityLivingBase.isEntityAlive() && positionVector.distanceTo(this.player.getPositionVector()) < 15.0 && positionVector.distanceTo(entityLivingBase.getPositionVector()) < 20.0 && !entityLivingBase.equals(this.player);
    }

    @Override
    protected void CompanionStates(States companionStates) {
        switch (companionStates) {
            case ATTACK: {
                this.fighter.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
                double distance = this.fighter.getDistance(this.target);
                this.pathNavigate.clearPath();
                if (distance < 1.9 && --this.k <= 0) {
                    this.d();
                    break;
                }
                if (this.fighter.items.getStackInSlot(1).getItem() instanceof ItemBow && this.fighter.getEntitySenses().canSee(this.target) && ++this.p > 0 && distance > 6.0) {
                    this.dataManager.set(Fighter.M, 2);
                    this.fighter.setCurrentAction(Action.BOW);
                    if (++this.p >= 32) {
                        this.p = -20;
                        this.ShootArrows();
                        this.fighter.setCurrentAction(Action.NULL);
                    }
                    this.l = this.fighter.getDistance(this.player);
                    this.i = this.player.getPositionVector();
                    return;
                }
                if (distance < 2.0) {
                    this.dataManager.set(Fighter.M, 1);
                    this.pathNavigate.tryMoveToEntityLiving(this.target, 0.5);
                    this.fighter.a(GirlEntity.WalkTypes.WALK);
                    break;
                }
                this.dataManager.set(Fighter.M, 1);
                this.pathNavigate.tryMoveToEntityLiving(this.target, 0.7);
                this.fighter.a(GirlEntity.WalkTypes.RUN);
                break;
            }
            case FOLLOW: {
                this.dataManager.set(Fighter.M, 0);
                double d = this.fighter.getDistance(this.player);
                if ((double)this.pathNavigate.getPathSearchRange() > d) {
                    this.pathNavigate.clearPath();
                    if (!this.fighter.N) {
                        this.pathNavigate.tryMoveToEntityLiving(this.player, 0.5);
                        this.void_a();
                    }
                } else {
                    this.c();
                }
                this.j = 300;
                this.double_b();
                break;
            }
            case IDLE: {
                this.dataManager.set(Fighter.M, 0);
                if (!this.fighter.N) {
                    if (++this.j > 200 + Reference.RANDOM.nextInt(100)) {
                        this.j = 0;
                        Vec3d vec3d = this.player.getPositionVector();
                        Vec3d vec3d2 = new Vec3d(vec3d.x + 1.0 + (double)(Reference.RANDOM.nextFloat() * 3.0f), vec3d.y, vec3d.z + 1.0 + (double)(Reference.RANDOM.nextFloat() * 3.0f));
                        this.pathNavigate.clearPath();
                        this.pathNavigate.tryMoveToXYZ(vec3d2.x, vec3d2.y, vec3d2.z, 0.5);
                    }
                    this.double_b();
                    break;
                }
                if (!(this.fighter.getDistance(this.player) > 10.0f)) break;
                this.c();
                break;
            }
            case RIDE: {
                if (this.fighter.isRiding()) {
                    this.fighter.setCurrentAction(Action.SIT);
                    break;
                }
                this.fighter.setNoGravity(true);
                this.fighter.noClip = true;
                Vec3d vec3d = this.player.getPositionVector().subtract(this.o.getLookVec().x * 0.5, 0.0, this.o.getLookVec().z * 0.5);
                this.fighter.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, 0.0f, 0.0f);
                this.fighter.motionX = 0.0;
                this.fighter.motionY = 0.0;
                this.fighter.motionZ = 0.0;
                this.fighter.setCurrentAction(Action.RIDE);
                break;
            }
            case DOWNED: {
                this.pathNavigate.clearPath();
            }
        }
    }

    @Override
    protected States abstractStates() {
        float f;
        boolean bl;
        //Entity entity;
        --this.n;
        if (this.fighter.N || this.fighter.getID() != null) {
            return States.DOWNED;
        }
        if (this.player.isRiding()) {
            Entity entity = this.player.getRidingEntity();
            if (this.fighter.isRiding() || this.fighter.startRiding(entity) || entity instanceof EntityHorse && ((EntityHorse)entity).isHorseSaddled()) {
                this.o = entity;
                return States.RIDE;
            }
        } else if (!this.player.isRiding() && this.fighter.isRiding() || this.f == States.RIDE && !this.player.isRiding()) {
            this.fighter.setCurrentAction(Action.NULL);
            this.fighter.dismountRidingEntity();
            this.fighter.noClip = false;
            this.fighter.setNoGravity(false);
        }
        if (this.CompanionStates(this.target)) {
            return States.ATTACK;
        }
        DamageSource damageSource = this.fighter.getLastDamageSource();
        if (damageSource != null) {
            EntityLivingBase entity = (EntityLivingBase) damageSource.getTrueSource();
            if (this.CompanionStates(entity)) {
                this.target = entity;
                return States.ATTACK;
            }
        }
        EntityLivingBase entity = this.player.getLastAttackedEntity();
        if (this.player.ticksExisted - this.player.getLastAttackedEntityTime() < 140 && this.CompanionStates((EntityLivingBase)entity)) {
            this.target = entity;
            return States.ATTACK;
        }
        if (this.f != States.FOLLOW) {
            damageSource = this.player.getLastDamageSource();
            if (damageSource != null && this.CompanionStates((EntityLivingBase)(entity = (EntityLivingBase)damageSource.getTrueSource()))) {
                this.target = entity;
                return States.ATTACK;
            }
            Vec3d vec3d = this.fighter.getPositionVector();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d.x - 5.0, vec3d.y - 2.0, vec3d.z - 5.0, vec3d.x + 5.0, vec3d.y + 2.0, vec3d.z + 5.0);
            List<EntityMob> list = this.fighter.world.getEntitiesWithinAABB(EntityMob.class, axisAlignedBB);
            list.sort((entityMob, entityMob2) -> {
                double d;
                double d2 = entityMob.getDistance(this.fighter);
                if (d2 == (d = (double)entityMob2.getDistance(this.fighter))) {
                    return 0;
                }
                return d2 < d ? -1 : 1;
            });
            for (EntityMob entityMob3 : list) {
                if (!this.CompanionStates(entityMob3) || entityMob3 instanceof EntityCreeper) continue;
                this.target = entityMob3;
                return States.ATTACK;
            }
        }
        boolean bl2 = bl = (f = this.fighter.getDistance(this.player)) > 5.0f;
        if (!bl && this.f == States.FOLLOW) {
            if (++this.m > 60) {
                bl = false;
                this.m = 0;
            } else {
                bl = true;
            }
        }
        if (bl && this.f == States.ATTACK) {
            this.n = 60;
        }
        if (bl) {
            return States.FOLLOW;
        }
        return States.IDLE;
    }

    public void ShootArrows() {
        EntityArrow arrow = this.Arrow();
        double d = this.target.posX - this.fighter.posX;
        double d2 = this.target.getEntityBoundingBox().minY + (double)(this.target.height / 3.0f) - arrow.posY;
        double d3 = this.target.posZ - this.fighter.posZ;
        double d4 = MathHelper.sqrt(d * d + d3 * d3);
        arrow.shoot(d, d2 + d4 * (double)0.2f, d3, 1.6f, 2.0f);
        this.fighter.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.fighter.getRNG().nextFloat() * 0.4f + 0.8f));
        this.fighter.world.spawnEntity(arrow);
        arrow.setDamage(4.5);
    }

    protected EntityArrow Arrow() {
        EntityTippedArrow entityTippedArrow = new EntityTippedArrow(this.fighter.world, this.fighter);
        ItemStack itemStack = this.fighter.items.getStackInSlot(1);
        double d = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemStack);
        int n = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, itemStack);
        int n2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack);
        if (d != 0.0) {
            entityTippedArrow.setDamage(entityTippedArrow.getDamage() + d * 0.5 + 0.5);
        }
        if (n != 0) {
            entityTippedArrow.setKnockbackStrength(n);
        }
        if (n2 != 0) {
            entityTippedArrow.setFire(100);
        }
        return entityTippedArrow;
    }

    void d() {
        this.fighter.setCurrentAction(Action.ATTACK);
        this.dataManager.set(Fighter.M, 1);
        ItemStack itemStack = this.fighter.items.getStackInSlot(0);
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        float f = 0.0f;
        float f2 = 0.0f;
        for (AttributeModifier attributeModifier : multimap.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
            f = (float)attributeModifier.getAmount();
        }
        for (AttributeModifier attributeModifier : multimap.get(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
            f2 = (float)attributeModifier.getAmount();
        }
        f2 = Math.max(f2, 0.5f);
        float f3 = EnchantmentHelper.getModifierForCreature(itemStack, this.target.getCreatureAttribute());
        int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, itemStack);
        int fire_aspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, itemStack);
        int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, itemStack);
        this.target.knockBack(this.fighter, (float)knockback * 0.5f, MathHelper.sin(this.fighter.rotationYaw * ((float)Math.PI / 180)), -MathHelper.cos(this.fighter.rotationYaw * ((float)Math.PI / 180)));
        this.target.setFire(fire_aspect * 4);
        if (sweeping != 0) {
            float f4 = 0.5f;
            if (sweeping == 2) {
                f4 = 0.67f;
            } else if (sweeping == 3) {
                f4 = 0.75f;
            }
            for (EntityLivingBase entityLivingBase : this.fighter.world.getEntitiesWithinAABB(EntityLivingBase.class, this.target.getEntityBoundingBox().grow(1.0, 0.25, 1.0))) {
                if (entityLivingBase == this.fighter || entityLivingBase == this.player || entityLivingBase == this.target || this.fighter.isOnSameTeam(entityLivingBase) || !(this.fighter.getDistanceSq(entityLivingBase) < 9.0)) continue;
                entityLivingBase.knockBack(this.fighter, 0.4f, MathHelper.sin(this.fighter.rotationYaw * ((float)Math.PI / 180)), -MathHelper.cos(this.fighter.rotationYaw * ((float)Math.PI / 180)));
                entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this.fighter), (f + f3) * f4);
            }
        }
        this.target.attackEntityFrom(DamageSource.causeMobDamage(this.fighter), f + f3);
        this.k = Math.round(Math.abs(f2) / 3.373494f * 20.0f);
    }

    @Override
    protected double double_b() {
        double d = super.double_b();
        if (this.fighter.N) {
            d = 0.0;
        }
        this.pathNavigate.setSpeed(d);
        this.fighter.a(this.fighter.com_trolmastercard_sexmod_em_class258$a_inner259_q());
        return d;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.fighter.getDataManager().set(Fighter.M, 0);
    }

    void void_a() {
        if (this.fighter.onGround || this.fighter.isInWater() || this.fighter.motionX + this.fighter.motionZ != 0.0 || this.fighter.motionY <= 0.0) {
            return;
        }
        Vec3d vec3d = new Vec3d(0.0, 0.0, 0.1f);
        vec3d = VectorMath.rotate(vec3d, this.fighter.rotationYaw);
        this.fighter.motionX = vec3d.x;
        this.fighter.motionZ = vec3d.z;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner339 {
        @SubscribeEvent
        public void a(LivingHurtEvent livingHurtEvent) {
            if (livingHurtEvent.getEntityLiving() instanceof Fighter) {
                Fighter e2_class2182 = (Fighter)livingHurtEvent.getEntityLiving();
                if (e2_class2182.N) {
                    livingHurtEvent.setCanceled(true);
                } else if (e2_class2182.getHealth() - livingHurtEvent.getAmount() < 0.0f && !((String)e2_class2182.getDataManager().get(Fighter.v)).equals("")) {
                    e2_class2182.N = true;
                    e2_class2182.setCurrentAction(Action.DOWNED);
                    livingHurtEvent.setAmount(e2_class2182.getHealth() - 1.0f);
                    e2_class2182.getNavigator().clearPath();
                }
            }
        }

        @SubscribeEvent
        public void a(LivingHealEvent livingHealEvent) {
            if (livingHealEvent.getEntityLiving() instanceof Fighter) {
                Fighter currentFighter = (Fighter)livingHealEvent.getEntityLiving();
                if (currentFighter.N && currentFighter.getHealth() + livingHealEvent.getAmount() >= currentFighter.getMaxHealth()) {
                    currentFighter.N = false;
                    currentFighter.setCurrentAction(Action.NULL);
                }
            }
        }

        @SubscribeEvent
        public void DropItems(LivingDeathEvent livingDeathEvent) {
            if (livingDeathEvent.getEntityLiving() instanceof Fighter) {
                Fighter thisfighter = (Fighter)livingDeathEvent.getEntityLiving();
                if (thisfighter.world.isRemote) {
                    return;
                }
                for (int i = 0; i < 6; ++i) {
                    Item item = thisfighter.items.getStackInSlot(i).getItem();
                    if (item == Items.AIR) continue;
                    thisfighter.dropItem(item, 1);
                }
            }
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

