/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fire extends BlockFire {
    final static public Block FIRE = new Fire();

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    public static void RegisterFire() {
        FIRE.setRegistryName("sexmod", "fire");
        FIRE.setTranslationKey("fire");
        MinecraftForge.EVENT_BUS.register(Fire.class);
    }

    @SubscribeEvent
    public static void a(RegistryEvent.Register<Block> register) {
        register.getRegistry().register(FIRE);
    }
}

