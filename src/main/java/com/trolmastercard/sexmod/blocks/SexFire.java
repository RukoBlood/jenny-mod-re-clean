/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/*
* SexFire block
* Inert fire block that doesn't spread and burns
* Used by the galath
*/

public class SexFire extends BlockFire {
    final static public Block FIRE = new SexFire();

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    public static void RegisterFire() {
        FIRE.setRegistryName("sexmod", "fire");
        FIRE.setTranslationKey("fire");
        MinecraftForge.EVENT_BUS.register(SexFire.class);
    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> register) {
        register.getRegistry().register(FIRE);
    }
}

