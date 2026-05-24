/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.EnderTeleportEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.GirlEntity;
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

public class ho_class404
extends EntityEnderPearl {
    public ho_class404(World world) {
        super(world);
    }

    public ho_class404(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
    }

    @Override
    protected void onImpact(RayTraceResult rayTraceResult) {
        BlockPos blockPos;
        TileEntity tileEntity;
        EntityLivingBase entityLivingBase = this.getThrower();
        if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK && (tileEntity = this.world.getTileEntity(blockPos = rayTraceResult.getBlockPos())) instanceof TileEntityEndGateway) {
            TileEntityEndGateway tileEntityEndGateway = (TileEntityEndGateway)tileEntity;
            if (entityLivingBase != null) {
                if (entityLivingBase instanceof EntityPlayerMP) {
                    CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)entityLivingBase, this.world.getBlockState(blockPos));
                }
                tileEntityEndGateway.teleportEntity(entityLivingBase);
                this.setDead();
                return;
            }
            tileEntityEndGateway.teleportEntity(this);
            return;
        }
        for (int i = 0; i < 32; ++i) {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian(), new int[0]);
        }
        if (!this.world.isRemote) {
            if (entityLivingBase != null) {
                GirlEntity em_class2582 = (GirlEntity)entityLivingBase;
                EnderTeleportEvent event = new EnderTeleportEvent(entityLivingBase, this.posX, this.posY, this.posZ, 5.0f);
                if (em_class2582.homeCoords.distanceTo(this.getPositionVector()) < 5.0
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

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner405 {
        @SubscribeEvent
        public void a(EnderTeleportEvent enderTeleportEvent) {
            if (enderTeleportEvent.getEntityLiving() instanceof GirlEntity) {
                GirlEntity em_class2582 = (GirlEntity)enderTeleportEvent.getEntityLiving();
                em_class2582.q = null;
                em_class2582.setCurrentAction(Action.NULL);
                em_class2582.getDataManager().set(GirlEntity.G, false);
                em_class2582.goHome();
            }
        }
    }
}

