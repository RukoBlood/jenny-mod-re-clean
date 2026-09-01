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

import com.trolmastercard.sexmod.world.WorldUtils;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.Kobold.DragonStaff.StructureMarkerRenderer;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendBlocks implements IMessage {
    boolean isValid = false;
    HashSet<BlockPos> blocks = new HashSet();
    boolean shouldMark;

    public SendBlocks() {
    }

    public SendBlocks(HashSet<BlockPos> hashSet, boolean bl) {
        this.blocks = hashSet;
        this.shouldMark = bl;
    }

    public SendBlocks(BlockPos blockPos, boolean bl) {
        this.blocks.add(blockPos);
        this.shouldMark = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.shouldMark = byteBuf.readBoolean();
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.blocks.add(new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()));
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.shouldMark);
        byteBuf.writeInt(this.blocks.size());
        for (BlockPos blockPos : this.blocks) {
            byteBuf.writeInt(blockPos.getX());
            byteBuf.writeInt(blockPos.getY());
            byteBuf.writeInt(blockPos.getZ());
        }
    }

    public static class Handler implements IMessageHandler<SendBlocks, IMessage> {
        @Override
        public IMessage onMessage(SendBlocks msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("received an invalid Message @SendBlocks :(");
                return null;
            }
            if (ctx.side.isClient()) {
                if (msg.shouldMark) {
                    StructureMarkerRenderer.addMarkers(msg.blocks);
                } else {
                    StructureMarkerRenderer.removeMarkers(msg.blocks);
                }
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = ctx.getServerHandler().player.getPersistentID();
                UUID uUID2 = KoboldManager.findTribeIdWith(uUID);
                if (uUID2 != null) {
                    if (msg.blocks.size() == 1) {
                        World world = ctx.getServerHandler().player.world;
                        for (BlockPos blockPos : msg.blocks) {
                            BlockChest.Type chestType;
                            IBlockState iBlockState = world.getBlockState(blockPos);
                            BlockPos blockPos2 = null;
                            if (iBlockState.getBlock() instanceof BlockBed) {
                                blockPos2 = WorldUtils.getBedPairPosition(blockPos, iBlockState);
                            }
                            if (iBlockState.getBlock() instanceof BlockChest) {
                                chestType = ((BlockChest) iBlockState.getBlock()).chestType;
                                if (world.getBlockState(blockPos.north()).getBlock() instanceof BlockChest && chestType.equals(((BlockChest) world.getBlockState(blockPos.north()).getBlock()).chestType)) {
                                    blockPos2 = blockPos.north();
                                }
                                if (world.getBlockState(blockPos.east()).getBlock() instanceof BlockChest && chestType.equals(((BlockChest) world.getBlockState(blockPos.east()).getBlock()).chestType)) {
                                    blockPos2 = blockPos.east();
                                }
                                if (world.getBlockState(blockPos.south()).getBlock() instanceof BlockChest && chestType.equals(((BlockChest) world.getBlockState(blockPos.south()).getBlock()).chestType)) {
                                    blockPos2 = blockPos.south();
                                }
                                if (world.getBlockState(blockPos.west()).getBlock() instanceof BlockChest && chestType.equals(((BlockChest) world.getBlockState(blockPos.west()).getBlock()).chestType)) {
                                    blockPos2 = blockPos.west();
                                }
                            }
                            if (blockPos2 != null || !(iBlockState.getBlock() instanceof BlockBed)) {
                                if (msg.shouldMark) {
                                    if (iBlockState.getBlock() instanceof BlockBed) {
                                        KoboldManager.registerBed(uUID2, blockPos);
                                        KoboldManager.registerBed(uUID2, blockPos2);
                                    } else {
                                        KoboldManager.registerChest(uUID2, blockPos);
                                        KoboldManager.registerChest(uUID2, blockPos2);
                                    }
                                } else if (iBlockState.getBlock() instanceof BlockBed) {
                                    KoboldManager.unregisterBed(uUID2, blockPos);
                                    KoboldManager.unregisterBed(uUID2, blockPos2);
                                } else {
                                    KoboldManager.unregisterChest(uUID2, blockPos);
                                    KoboldManager.unregisterChest(uUID2, blockPos2);
                                }
                                HashSet<BlockPos> positions = new HashSet<>();
                                positions.add(blockPos);
                                if (blockPos2 != null) {
                                    positions.add(blockPos2);
                                }
                                PacketHandler.INSTANCE.sendTo(new SendBlocks(positions, msg.shouldMark), ctx.getServerHandler().player);
                            } else {
                                return;
                            }
                        }
                    }
                }
            });
            return null;
        }
    }
}

