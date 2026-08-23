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
    BlockPos b;

    public CancelTask() {
    }

    public CancelTask(BlockPos blockPos) {
        this.b = blockPos;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.b.getX());
        byteBuf.writeInt(this.b.getY());
        byteBuf.writeInt(this.b.getZ());
    }

    public static class handler implements IMessageHandler<CancelTask, IMessage> {
        @Override
        public IMessage onMessage(CancelTask msg, MessageContext ctx) {
            if (!msg.isValid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid Message @CancelTask :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = KoboldManager.findTribeIdWith(ctx.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    return;
                }
                HashSet<BlockPos> hashSet = KoboldManager.removeTaskByBlockPos(uUID, msg.b);
                if (hashSet.isEmpty()) {
                    return;
                }
                PacketHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet, false), ctx.getServerHandler().player);
            });
            return null;
        }
    }
}

