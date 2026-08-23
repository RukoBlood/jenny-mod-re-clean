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
    HashSet<BlockPos> c = new HashSet();
    boolean a;

    public SendBlocks() {
    }

    public SendBlocks(HashSet<BlockPos> hashSet, boolean bl) {
        this.c = hashSet;
        this.a = bl;
    }

    public SendBlocks(BlockPos blockPos, boolean bl) {
        this.c.add(blockPos);
        this.a = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = byteBuf.readBoolean();
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.c.add(new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()));
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.a);
        byteBuf.writeInt(this.c.size());
        for (BlockPos blockPos : this.c) {
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
                if (msg.a) {
                    StructureMarkerRenderer.AddList(msg.c);
                } else {
                    StructureMarkerRenderer.CleanList(msg.c);
                }
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = ctx.getServerHandler().player.getPersistentID();
                UUID uUID2 = KoboldManager.findTribeIdWith(uUID);
                if (uUID2 == null) {
                    return;
                }
                if (msg.c.size() != 1) {
                    return;
                }
                World world = ctx.getServerHandler().player.world;
                for (BlockPos blockPos : msg.c) {
                    Object object;
                    IBlockState iBlockState = world.getBlockState(blockPos);
                    BlockPos blockPos2 = null;
                    if (iBlockState.getBlock() instanceof BlockBed) {
                        blockPos2 = WorldUtils.getBedPairPosition(blockPos, iBlockState);
                    }
                    if (iBlockState.getBlock() instanceof BlockChest) {
                        object = ((BlockChest)iBlockState.getBlock()).chestType;
                        if (world.getBlockState(blockPos.north()).getBlock() instanceof BlockChest && object.equals(((BlockChest)world.getBlockState(blockPos.north()).getBlock()).chestType)) {
                            blockPos2 = blockPos.north();
                        }
                        if (world.getBlockState(blockPos.east()).getBlock() instanceof BlockChest && object.equals(((BlockChest)world.getBlockState(blockPos.east()).getBlock()).chestType)) {
                            blockPos2 = blockPos.east();
                        }
                        if (world.getBlockState(blockPos.south()).getBlock() instanceof BlockChest && object.equals(((BlockChest)world.getBlockState(blockPos.south()).getBlock()).chestType)) {
                            blockPos2 = blockPos.south();
                        }
                        if (world.getBlockState(blockPos.west()).getBlock() instanceof BlockChest && object.equals(((BlockChest)world.getBlockState(blockPos.west()).getBlock()).chestType)) {
                            blockPos2 = blockPos.west();
                        }
                    }
                    if (blockPos2 == null && iBlockState.getBlock() instanceof BlockBed) {
                        return;
                    }
                    if (msg.a) {
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
                    object = new HashSet();
                    ((HashSet)object).add(blockPos);
                    if (blockPos2 != null) {
                        ((HashSet)object).add(blockPos2);
                    }
                    PacketHandler.INSTANCE.sendTo(new SendBlocks((HashSet<BlockPos>)object, msg.a), ctx.getServerHandler().player);
                }
            });
            return null;
        }
    }
}

