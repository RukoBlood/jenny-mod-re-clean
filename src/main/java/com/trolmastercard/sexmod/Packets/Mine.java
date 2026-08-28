/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.Kobold.KoboldTask;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class Mine implements IMessage {
    boolean isValid = false;
    BlockPos targetPos;
    EnumFacing facing;

    public Mine() {
    }

    public Mine(BlockPos pos, EnumFacing facing) {
        this.targetPos = pos;
        this.facing = facing;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.targetPos = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.facing = EnumFacing.byName(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.targetPos.getX());
        byteBuf.writeInt(this.targetPos.getY());
        byteBuf.writeInt(this.targetPos.getZ());
        ByteBufUtils.writeUTF8String(byteBuf, this.facing.getName());
    }

    public static class Handler implements IMessageHandler<Mine, IMessage> {
        HashSet<BlockPos> getMineableBlocks(BlockPos pos, EnumFacing facing) {
            HashSet<BlockPos> positions = new HashSet<BlockPos>();
            BlockPos Pos = pos;
            for (int i = 0; i < 30; ++i) {
                positions.add(Pos.subtract(this.getNextBlock(facing)));
                positions.add(Pos.subtract(this.getNextBlock(facing)).up());
                positions.add(Pos.subtract(this.getNextBlock(facing)).up().up());
                positions.add(Pos);
                positions.add(Pos.up());
                positions.add(Pos.up().up());
                positions.add(Pos.add(this.getNextBlock(facing)));
                positions.add(Pos.add(this.getNextBlock(facing)).up());
                positions.add(Pos.add(this.getNextBlock(facing)).up().up());
                Pos = Pos.add(facing.getDirectionVec());
            }
            return positions;
        }

        BlockPos getNextBlock(EnumFacing facing) {
            Vec3i dirVec = facing.getDirectionVec();
            return new BlockPos(dirVec.getZ(), dirVec.getY(), -dirVec.getX());
        }

        @Override
        public IMessage onMessage(Mine msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid Message @Mine :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                int n;
                EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
                UUID uUID = KoboldManager.findTribeIdWith(entityPlayerMP.getPersistentID());
                if (uUID == null) {
                    return;
                }
                int n2 = KoboldManager.getTribeMemberCount(uUID);
                if (n2 > (n = (int)Math.floor((double) KoboldManager.getTribeBeds(uUID).size() / 2.0))) {
                    entityPlayerMP.sendMessage(new TextComponentString(String.format("sUr Tribe will only work for you, if %severyone%s of them has a %sbed", TextFormatting.RED, TextFormatting.WHITE, TextFormatting.RED)));
                    entityPlayerMP.sendMessage(new TextComponentString(String.format("%s%d/%d Beds", TextFormatting.YELLOW, n, n2)));
                    return;
                }
                HashSet<BlockPos> hashSet = this.getMineableBlocks(msg.targetPos, msg.facing);
                World world = ctx.getServerHandler().player.world;
                for (BlockPos blockPos : hashSet) {
                    IBlockState iBlockState = world.getBlockState(blockPos);
                    if (iBlockState.getBlock().getBlockHardness(iBlockState, world, blockPos) < 0.0f) {
                        entityPlayerMP.sendStatusMessage(new TextComponentString("This area contains Bedrock and cannot be mined"), true);
                        return;
                    }
                }
                KoboldTask task = new KoboldTask(msg.targetPos, KoboldTask.KoboldTasks.MINE, hashSet, msg.facing);
                KoboldManager.addTaskToTribe(uUID, task);
                PacketHandler.INSTANCE.sendTo(new SendBlocks(hashSet, true), ctx.getServerHandler().player);
            });
            return null;
        }
    }
}

