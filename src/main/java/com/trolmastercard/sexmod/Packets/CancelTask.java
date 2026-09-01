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

import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CancelTask
implements IMessage {
    boolean isValid = false;
    BlockPos taskPos;

    public CancelTask() {
    }

    public CancelTask(BlockPos blockPos) {
        this.taskPos = blockPos;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.taskPos = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.taskPos.getX());
        byteBuf.writeInt(this.taskPos.getY());
        byteBuf.writeInt(this.taskPos.getZ());
    }

    public static class handler implements IMessageHandler<CancelTask, IMessage> {
        @Override
        public IMessage onMessage(CancelTask msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid Message @CancelTask :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID tribeId = KoboldManager.findTribeIdWith(ctx.getServerHandler().player.getPersistentID());
                if (tribeId != null) {
                    HashSet<BlockPos> hashSet = KoboldManager.removeTaskByBlockPos(tribeId, msg.taskPos);
                    if (!hashSet.isEmpty()) {
                        PacketHandler.INSTANCE.sendTo(new SendBlocks(hashSet, false), ctx.getServerHandler().player);
                    }
                }
            });
            return null;
        }
    }
}

