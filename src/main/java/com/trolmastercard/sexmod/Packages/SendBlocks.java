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
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.cj_class134;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.gm_class376;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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

public class SendBlocks
implements IMessage {
    boolean b = false;
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
        this.b = true;
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

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner398
    implements IMessageHandler<SendBlocks, IMessage> {
        public IMessage a(SendBlocks h6_class3972, MessageContext messageContext) {
            if (!h6_class3972.b) {
                System.out.println("received an invalid Message @SendBlocks :(");
                return null;
            }
            if (messageContext.side.isClient()) {
                if (h6_class3972.a) {
                    gm_class376.a(h6_class3972.c);
                } else {
                    gm_class376.b(h6_class3972.c);
                }
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = messageContext.getServerHandler().player.getPersistentID();
                UUID uUID2 = KoboldManager.findTribeIdWith(uUID);
                if (uUID2 == null) {
                    return;
                }
                if (h6_class3972.c.size() != 1) {
                    return;
                }
                World world = messageContext.getServerHandler().player.world;
                for (BlockPos blockPos : h6_class3972.c) {
                    Object object;
                    IBlockState iBlockState = world.getBlockState(blockPos);
                    BlockPos blockPos2 = null;
                    if (iBlockState.getBlock() instanceof BlockBed) {
                        blockPos2 = cj_class134.a(blockPos, iBlockState);
                    }
                    if (iBlockState.getBlock() instanceof BlockChest) {
                        object = ((BlockChest)iBlockState.getBlock()).chestType;
                        if (world.getBlockState(blockPos.north()).getBlock() instanceof BlockChest && ((Enum)object).equals((Object)((BlockChest)world.getBlockState((BlockPos)blockPos.north()).getBlock()).chestType)) {
                            blockPos2 = blockPos.north();
                        }
                        if (world.getBlockState(blockPos.east()).getBlock() instanceof BlockChest && ((Enum)object).equals((Object)((BlockChest)world.getBlockState((BlockPos)blockPos.east()).getBlock()).chestType)) {
                            blockPos2 = blockPos.east();
                        }
                        if (world.getBlockState(blockPos.south()).getBlock() instanceof BlockChest && ((Enum)object).equals((Object)((BlockChest)world.getBlockState((BlockPos)blockPos.south()).getBlock()).chestType)) {
                            blockPos2 = blockPos.south();
                        }
                        if (world.getBlockState(blockPos.west()).getBlock() instanceof BlockChest && ((Enum)object).equals((Object)((BlockChest)world.getBlockState((BlockPos)blockPos.west()).getBlock()).chestType)) {
                            blockPos2 = blockPos.west();
                        }
                    }
                    if (blockPos2 == null && iBlockState.getBlock() instanceof BlockBed) {
                        return;
                    }
                    if (h6_class3972.a) {
                        if (iBlockState.getBlock() instanceof BlockBed) {
                            KoboldManager.a(uUID2, blockPos);
                            KoboldManager.a(uUID2, blockPos2);
                        } else {
                            KoboldManager.f(uUID2, blockPos);
                            KoboldManager.f(uUID2, blockPos2);
                        }
                    } else if (iBlockState.getBlock() instanceof BlockBed) {
                        KoboldManager.e(uUID2, blockPos);
                        KoboldManager.e(uUID2, blockPos2);
                    } else {
                        KoboldManager.d(uUID2, blockPos);
                        KoboldManager.d(uUID2, blockPos2);
                    }
                    object = new HashSet();
                    ((HashSet)object).add(blockPos);
                    if (blockPos2 != null) {
                        ((HashSet)object).add(blockPos2);
                    }
                    PackageHandler.networkWrapper.sendTo((IMessage)new SendBlocks((HashSet<BlockPos>)object, h6_class3972.a), messageContext.getServerHandler().player);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(SendBlocks iMessage, MessageContext messageContext) {
            return this.a((SendBlocks)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

