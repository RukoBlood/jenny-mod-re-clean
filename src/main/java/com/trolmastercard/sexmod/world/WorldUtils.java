/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Sets
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package com.trolmastercard.sexmod.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldUtils {
    public static float CalculateAngleDifferences(float angleA, float angleB) {
        angleA = TrigMath.NormalizeAngle(angleA);
        angleB = TrigMath.NormalizeAngle(angleB);
        float diff = Math.abs(angleA - angleB);
        float wrappedDiff = 360.0f - diff;
        float shortestDist = Math.min(diff, wrappedDiff);
        if (angleA > angleB) {
            return -shortestDist;
        }
        return shortestDist;
    }

    public static Vec3d getLightDirectionVector(EntityLivingBase entity, float partialTicks) {
        World world = entity.world;
        if (world instanceof FakeWorld) {
            return new Vec3d(0.0, 1.0, 0.0);
        }
        BlockPos centerPos = new BlockPos(Math.floor(entity.posX), Math.floor(entity.posY), Math.floor(entity.posZ));
        HashMap<Vec3d, Integer> lightMap = new HashMap<Vec3d, Integer>();
        int maxLightValue = 0;
        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                for (int z = -1; z < 2; ++z) {
                    int light = world.getLight(centerPos.add(x, y, z), false);
                    lightMap.put(new Vec3d(x, y, z), light);
                    if (light <= maxLightValue) continue;
                    maxLightValue = light;
                }
            }
        }

        Vec3d targetDirection = null;
        for (Map.Entry entry : lightMap.entrySet()) {
            if ((Integer)entry.getValue() != maxLightValue) continue;
            if (targetDirection == null) {
                targetDirection = (Vec3d)entry.getKey();
                continue;
            }
            targetDirection = null;
            break;
        }
        if (targetDirection == null) {
            targetDirection = new Vec3d(0.2, 0.8, 0.0);
        } else {
            targetDirection = new Vec3d(targetDirection.x, targetDirection.y, -targetDirection.z);
            float lerpedYaw = -RotationHelper.LerpFloat(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            targetDirection = VectorMath.rotateByYaw(targetDirection, lerpedYaw);
        }
        return targetDirection.normalize();
    }

    public static int getHeightAt(World world, int x, int z) {
        HashSet surfaceBlocks = Sets.newHashSet((Object[])new Block[]{Blocks.GRASS, Blocks.SAND, Blocks.RED_SANDSTONE, Blocks.WATER, Blocks.STONE, Blocks.COBBLESTONE});
        int currentY = world.getHeight();
        boolean isSurfaceFound = false;
        while (!isSurfaceFound && currentY-- >= 0) {
            Block block = world.getBlockState(new BlockPos(x, currentY, z)).getBlock();
            isSurfaceFound = surfaceBlocks.contains(block);
        }
        return currentY;
    }

    public static BlockPos getSurfacePosition(World world, BlockPos blockPos) {
        return new BlockPos(blockPos.getX(), WorldUtils.getHeightAt(world, blockPos.getX(), blockPos.getZ()), blockPos.getZ());
    }

    public static boolean b(World world, BlockPos blockPos) {
        return WorldUtils.checkBedBlock(world, blockPos, null, null, null);
    }

    public static boolean checkBedBlock(World world, BlockPos pos, Vec3d hitVec, EnumFacing facing, EntityPlayer player) {
        Object name;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isBed(state, world, pos, null)) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && (name = tileEntity.getDisplayName()) != null && (name.toString().contains(" bed") || name.toString().contains("bed "))) {
            return true;
        }
        if (facing == null || hitVec == null) {
            return false;
        }
        name = block.getPickBlock(state, new RayTraceResult(hitVec, facing), world, pos, player).getDisplayName().toLowerCase();
        return ((String)name).contains(" bed") || ((String)name).contains("bed ");
    }

    public static void SpawnParticleRing(World world, EnumParticleTypes particleTypes, Vec3d center, int count, double radius, double speedY) {
        for (int i = 0; i < count; ++i) {
            float progress = (float)i / (float)count;
            double angle = Math.PI * 2 * (double)progress;
            double posX = Math.sin(angle);
            double posZ = Math.cos(angle);
            world.spawnParticle(particleTypes, center.x + (posX *= radius), center.y, center.z + (posZ *= radius), 0.0, (double) Reference.RANDOM.nextFloat() * speedY, 0.0, new int[0]);
        }
    }

    public static BlockPos getBedPairPosition(BlockPos pos, IBlockState state) {
        ImmutableMap<IProperty<?>, Comparable<?>> properties = state.getProperties();
        EnumFacing facing = null;
        BlockBed.EnumPartType partType = null;
        for (Map.Entry entry : properties.entrySet()) {
            if (entry.getKey() instanceof PropertyDirection) {
                facing = (EnumFacing)entry.getValue();
                continue;
            }
            if (!(entry.getKey() instanceof PropertyEnum)) continue;
            partType = (BlockBed.EnumPartType)entry.getValue();
        }
        if (facing == null) {
            System.out.println("bed is fucked up - it has no facing value");
            return null;
        }
        if (partType == null) {
            System.out.println("bed is fucked up - it has no partType value");
            return null;
        }
        BlockPos neighborPos = null;
        if (partType == BlockBed.EnumPartType.FOOT) {
            if (facing == EnumFacing.NORTH) {
                neighborPos = pos.north();
            }
            if (facing == EnumFacing.EAST) {
                neighborPos = pos.east();
            }
            if (facing == EnumFacing.SOUTH) {
                neighborPos = pos.south();
            }
            if (facing == EnumFacing.WEST) {
                neighborPos = pos.west();
            }
        } else {
            if (facing == EnumFacing.NORTH) {
                neighborPos = pos.south();
            }
            if (facing == EnumFacing.EAST) {
                neighborPos = pos.west();
            }
            if (facing == EnumFacing.SOUTH) {
                neighborPos = pos.north();
            }
            if (facing == EnumFacing.WEST) {
                neighborPos = pos.east();
            }
        }
        if (neighborPos == null) {
            System.out.println("bed is fucked up - it appears to be positioned vertically (wtf?)");
            return null;
        }
        return neighborPos;
    }

    public static Set<? extends EntityPlayer> getPlayersTrackingEntity(Entity entity) {
        if (entity == null) {
            return Collections.emptySet();
        }
        return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(entity.dimension).getEntityTracker().getTrackingPlayers(entity);
    }
}

