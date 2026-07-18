/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FakeWorld extends WorldClient {
    public Biome getBiomeForCoordsBody(BlockPos pos) {
        return new BiomePlains(false, new Biome.BiomeProperties("Plains").setBaseHeight(0.125f).setHeightVariation(0.05f).setHeightVariation(0.8f).setRainfall(0.4f));
    }

    @Override
    public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType, boolean updateObservers) {
        super.notifyNeighborsOfStateChange(pos, blockType, updateObservers);
    }

    public void markAndNotifyBlock(BlockPos pos, Chunk chunk, IBlockState state, IBlockState newState, int flags) {
    }

    public float getSunBrightnessFactor(float factor) {
        return 1.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public float getSunBrightnessBody(float body) {
        return 1.0f;
    }

    public void updateWeatherBody() {
    }

    public boolean canBlockFreezeBody(BlockPos pos, boolean bl) {
        return false;
    }

    public boolean canSnowAtBody(BlockPos blockPos, boolean bl) {
        return false;
    }

    public FakeWorld() {
        super(new FakeNetHandler(Minecraft.getMinecraft()), new WorldSettings(0L, GameType.SURVIVAL, false, false, WorldType.FLAT), 0, EnumDifficulty.HARD, new Profiler());
        this.provider.setWorld(this);
    }

    public boolean canMineBlockBody(EntityPlayer entityPlayer, BlockPos blockPos) {
        return false;
    }

    public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing) {
        return blockPos.getY() <= 63;
    }

    public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing, boolean bl) {
        return blockPos.getY() <= 63;
    }

    public int countEntities(EnumCreatureType enumCreatureType, boolean bl) {
        return 0;
    }
}

