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
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.girls.Kobold.KoboldTaskInfo;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
    boolean c = false;
    BlockPos a;
    EnumFacing b;

    public Mine() {
    }

    public Mine(BlockPos blockPos, EnumFacing enumFacing) {
        this.a = blockPos;
        this.b = enumFacing;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.b = EnumFacing.byName(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.a.getX());
        byteBuf.writeInt(this.a.getY());
        byteBuf.writeInt(this.a.getZ());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.getName());
    }

    public static class a_inner227 implements IMessageHandler<Mine, IMessage> {
        public IMessage a(Mine e6_class2262, MessageContext messageContext) {
            if (!e6_class2262.c || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid Message @Mine :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                int n;
                EntityPlayerMP entityPlayerMP = messageContext.getServerHandler().player;
                UUID uUID = KoboldManager.findTribeIdWith(entityPlayerMP.getPersistentID());
                if (uUID == null) {
                    return;
                }
                int n2 = KoboldManager.h(uUID);
                if (n2 > (n = (int)Math.floor((double) KoboldManager.getTribeBeds(uUID).size() / 2.0))) {
                    ((Entity)entityPlayerMP).sendMessage(new TextComponentString(String.format("sUr Tribe will only work for you, if %severyone%s of them has a %sbed", new Object[]{TextFormatting.RED, TextFormatting.WHITE, TextFormatting.RED})));
                    ((Entity)entityPlayerMP).sendMessage(new TextComponentString(String.format("%s%d/%d Beds", new Object[]{TextFormatting.YELLOW, n, n2})));
                    return;
                }
                HashSet<BlockPos> hashSet = this.a(e6_class2262.a, e6_class2262.b);
                World world = messageContext.getServerHandler().player.world;
                for (BlockPos blockPos : hashSet) {
                    IBlockState iBlockState = world.getBlockState(blockPos);
                    if (!(iBlockState.getBlock().getBlockHardness(iBlockState, world, blockPos) < 0.0f)) continue;
                    ((EntityPlayer)entityPlayerMP).sendStatusMessage(new TextComponentString("This area contains Bedrock and cannot be mined"), true);
                    return;
                }
                KoboldTaskInfo bs_class972 = new KoboldTaskInfo(e6_class2262.a, KoboldTaskInfo.KoboldTask.MINE, hashSet, e6_class2262.b);
                KoboldManager.addTaskToTribe(uUID, bs_class972);
                PackageHandler.networkWrapper.sendTo((IMessage)new SendBlocks(hashSet, true), messageContext.getServerHandler().player);
            });
            return null;
        }

        HashSet<BlockPos> a(BlockPos blockPos, EnumFacing enumFacing) {
            HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
            BlockPos blockPos2 = blockPos;
            for (int i = 0; i < 30; ++i) {
                hashSet.add(blockPos2.subtract(this.a(enumFacing)));
                hashSet.add(blockPos2.subtract(this.a(enumFacing)).up());
                hashSet.add(blockPos2.subtract(this.a(enumFacing)).up().up());
                hashSet.add(blockPos2);
                hashSet.add(blockPos2.up());
                hashSet.add(blockPos2.up().up());
                hashSet.add(blockPos2.add(this.a(enumFacing)));
                hashSet.add(blockPos2.add(this.a(enumFacing)).up());
                hashSet.add(blockPos2.add(this.a(enumFacing)).up().up());
                blockPos2 = blockPos2.add(enumFacing.getDirectionVec());
            }
            return hashSet;
        }

        BlockPos a(EnumFacing enumFacing) {
            Vec3i vec3i = enumFacing.getDirectionVec();
            return new BlockPos(vec3i.getZ(), vec3i.getY(), -vec3i.getX());
        }

                @Override
        public IMessage onMessage(Mine iMessage, MessageContext messageContext) {
            return this.a((Mine)iMessage, messageContext);
        }
    }
}

