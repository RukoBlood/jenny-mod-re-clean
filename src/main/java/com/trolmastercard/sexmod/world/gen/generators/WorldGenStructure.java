/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.world.gen.generators;

import java.util.Random;

import com.trolmastercard.sexmod.util.interfaces.IStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenStructure extends WorldGenerator implements IStructure {
    public String structureName;

    public WorldGenStructure(String name) {
        this.structureName = name;
    }

    public void a(World world, BlockPos blockPos) {
        ResourceLocation resourceLocation;
        MinecraftServer minecraftServer = world.getMinecraftServer();
        TemplateManager templateManager = worldServer.getStructureTemplateManager();
        Template template = templateManager.get(minecraftServer, resourceLocation = new ResourceLocation("sexmod", this.structureName));
        if (template != null) {
            IBlockState iBlockState = world.getBlockState(blockPos);
            world.notifyBlockUpdate(blockPos, iBlockState, iBlockState, 3);
            template.addBlocksToWorld(world, blockPos, settings);
        }
    }

    public void a(World world, BlockPos blockPos, Rotation rotation) {
        ResourceLocation resourceLocation;
        MinecraftServer minecraftServer = world.getMinecraftServer();
        TemplateManager templateManager = worldServer.getStructureTemplateManager();
        Template template = templateManager.get(minecraftServer, resourceLocation = new ResourceLocation("sexmod", this.structureName));
        if (template != null) {
            IBlockState iBlockState = world.getBlockState(blockPos);
            world.notifyBlockUpdate(blockPos, iBlockState, iBlockState, 2);
            template.addBlocksToWorld(world, blockPos, settings.setRotation(rotation));
        }
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        this.a(world, blockPos);
        return true;
    }
}

