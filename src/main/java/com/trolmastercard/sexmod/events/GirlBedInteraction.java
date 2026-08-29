/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent
 *  net.minecraftforge.event.world.BlockEvent$BreakEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.events;

import java.util.List;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
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

public class GirlBedInteraction {
    final static int SEARCH_RADIUS = 3;

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent breakEvent) {
        Block block = breakEvent.getState().getBlock();
        if (block == Blocks.BED) {
            BlockPos pos = breakEvent.getPos();
            AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3, pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3);
            List<GirlEntity> girlsNearby = breakEvent.getWorld().getEntitiesWithinAABB(GirlEntity.class, aabb);
            boolean isBedOccupied = false;
            for (GirlEntity girl : girlsNearby) {
                if (girl.isDead || !girl.getDataManager().get(GirlEntity.IS_ANCHORED)) continue;
                isBedOccupied = true;
                break;
            }
            if (isBedOccupied) {
                breakEvent.getPlayer().sendStatusMessage(new TextComponentString("this bed is currently used by a girl.. pls don't disturb okay? ... you are kinda mean rn"), true);
                breakEvent.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(value=Side.CLIENT)
    public void onPlayerPushOutOfBlocks(PlayerSPPushOutOfBlocksEvent event) {
        if (GirlEntity.getActiveSceneInfo(event.getEntityPlayer()) != null) {
            event.setCanceled(true);
        }
    }
}

