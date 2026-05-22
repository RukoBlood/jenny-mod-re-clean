/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent
 *  net.minecraftforge.event.world.BlockEvent$BreakEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ey_class280 {
    final static int a = 3;

    @SubscribeEvent
    public void a(BlockEvent.BreakEvent breakEvent) {
        Block block = breakEvent.getState().getBlock();
        if (block != Blocks.BED) {
            return;
        }
        BlockPos blockPos = breakEvent.getPos();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - 3, blockPos.getY() - 3, blockPos.getZ() - 3, blockPos.getX() + 3, blockPos.getY() + 3, blockPos.getZ() + 3);
        List<GirlEntity> list = breakEvent.getWorld().getEntitiesWithinAABB(GirlEntity.class, axisAlignedBB);
        boolean bl = false;
        for (GirlEntity em_class2582 : list) {
            if (em_class2582.isDead || !em_class2582.getDataManager().get(GirlEntity.G).booleanValue()) continue;
            bl = true;
            break;
        }
        if (!bl) {
            return;
        }
        breakEvent.getPlayer().sendStatusMessage(new TextComponentString("this bed is currently used by a girl.. pls don't disturb okay? ... you are kinda mean rn"), true);
        breakEvent.setCanceled(true);
    }

    @SubscribeEvent
    @SideOnly(value=Side.CLIENT)
    public void a(PlayerSPPushOutOfBlocksEvent playerSPPushOutOfBlocksEvent) {
        if (GirlEntity.com_trolmastercard_sexmod_em_class258_d(playerSPPushOutOfBlocksEvent.getEntityPlayer()) != null) {
            playerSPPushOutOfBlocksEvent.setCanceled(true);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

