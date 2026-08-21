/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.Kobold.KoboldTask;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class FallTree implements IMessage {
    Boolean isVaild = false;
    BlockPos a;

    public FallTree() {
    }

    public FallTree(BlockPos blockPos) {
        this.a = blockPos;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.isVaild = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.a.getX());
        byteBuf.writeInt(this.a.getY());
        byteBuf.writeInt(this.a.getZ());
    }

    public static class Handler implements IMessageHandler<FallTree, IMessage> {

        BlockPos a(World world, BlockPos blockPos) {
            if (world.getBlockState(blockPos.add(0, -1, 0)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(0, -1, 0));
            }
            if (world.getBlockState(blockPos.add(1, -1, 0)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(1, -1, 0));
            }
            if (world.getBlockState(blockPos.add(-1, -1, 0)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(-1, -1, 0));
            }
            if (world.getBlockState(blockPos.add(0, -1, 1)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(0, -1, 1));
            }
            if (world.getBlockState(blockPos.add(0, -1, -1)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(0, -1, -1));
            }
            if (world.getBlockState(blockPos.add(-1, -1, -1)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(-1, -1, -1));
            }
            if (world.getBlockState(blockPos.add(1, -1, 1)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(1, -1, 1));
            }
            if (world.getBlockState(blockPos.add(-1, -1, 1)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(-1, -1, 1));
            }
            if (world.getBlockState(blockPos.add(1, -1, -1)).getBlock() instanceof BlockLog) {
                return this.a(world, blockPos.add(1, -1, -1));
            }
            return blockPos;
        }

        @Override
        public IMessage onMessage(FallTree msg, MessageContext ctx) {
            if (!msg.isVaild || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid Message @FallTree :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                UUID uUID = KoboldManager.findTribeIdWith(player.getPersistentID());
                if (uUID == null) {
                    System.out.println("not tribe for player");
                } else {
                    int memberCount = KoboldManager.getTribeMemberCount(uUID);
                    int bedsCount = (int) Math.floor((double) KoboldManager.getTribeBeds(uUID).size() / 2.0);
                    if (memberCount > bedsCount) {
                        player.sendMessage(new TextComponentString(String.format("Ur Tribe will only work for you, if %severyone%s of them has a %sbed", TextFormatting.RED, TextFormatting.WHITE, TextFormatting.RED)));
                        player.sendMessage(new TextComponentString(String.format("%s%d/%d Beds", TextFormatting.YELLOW, bedsCount, memberCount)));
                    } else {
                        World world = player.world;
                        BlockPos blockPos = this.a(world, msg.a);
                        HashSet<BlockPos> hashSet = KoboldTask.findConnectedBlocks(world, blockPos, uUID);
                        PackageHandler.INSTANCE.sendTo(new SendBlocks(hashSet, true), ctx.getServerHandler().player);
                    }
                }
            });
            return null;
        }
    }
}

