/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.EnderTeleportEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.companion;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompanionPearl extends EntityEnderPearl {
    public CompanionPearl(World world) {
        super(world);
    }

    public CompanionPearl(World world, EntityLivingBase throwerIn) {
        super(world, throwerIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        BlockPos blockPos;
        TileEntity tileEntity;
        EntityLivingBase entityLivingBase = this.getThrower();
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && (tileEntity = this.world.getTileEntity(blockPos = result.getBlockPos())) instanceof TileEntityEndGateway) {
            TileEntityEndGateway gateway = (TileEntityEndGateway)tileEntity;
            if (entityLivingBase != null) {
                if (entityLivingBase instanceof EntityPlayerMP) {
                    CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)entityLivingBase, this.world.getBlockState(blockPos));
                }
                gateway.teleportEntity(entityLivingBase);
                this.setDead();
                return;
            }
            gateway.teleportEntity(this);
            return;
        }

        for (int i = 0; i < 32; ++i) {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian(), new int[0]);
        }

        if (!this.world.isRemote) {
            if (entityLivingBase != null) {
                GirlEntity girl = (GirlEntity)entityLivingBase;
                EnderTeleportEvent event = new EnderTeleportEvent(entityLivingBase, this.posX, this.posY, this.posZ, 5.0f);
                if (girl.homeCoords.distanceTo(this.getPositionVector()) < 5.0
                        && !MinecraftForge.EVENT_BUS.post(event)) {
                    if (entityLivingBase.isRiding()) {
                        entityLivingBase.dismountRidingEntity();
                    }
                    entityLivingBase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    entityLivingBase.fallDistance = 0.0f;
                }
            }
            this.setDead();
        }
    }

    public static class EventHandler {
        @SubscribeEvent
        public void arrive(EnderTeleportEvent enderTeleportEvent) {
            if (enderTeleportEvent.getEntityLiving() instanceof GirlEntity) {
                GirlEntity girl = (GirlEntity)enderTeleportEvent.getEntityLiving();
                girl.activePearl = null;
                girl.setCurrentAction(Action.NULL);
                girl.getDataManager().set(GirlEntity.IS_ANCHORED, false);
                girl.goHome();
            }
        }
    }
}

