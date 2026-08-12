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
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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
    boolean a = false;
    BlockPos b;

    public CancelTask() {
    }

    public CancelTask(BlockPos blockPos) {
        this.b = blockPos;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.b.getX());
        byteBuf.writeInt(this.b.getY());
        byteBuf.writeInt(this.b.getZ());
    }

    public static class a_inner45
    implements IMessageHandler<CancelTask, IMessage> {
        public IMessage a(CancelTask au_class442, MessageContext messageContext) {
            if (!au_class442.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid Message @CancelTask :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = KoboldManager.findTribeIdWith(messageContext.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    return;
                }
                HashSet<BlockPos> hashSet = KoboldManager.removeTaskByBlockPos(uUID, au_class442.b);
                if (hashSet.isEmpty()) {
                    return;
                }
                PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet, false), messageContext.getServerHandler().player);
            });
            return null;
        }

                @Override
        public IMessage onMessage(CancelTask iMessage, MessageContext messageContext) {
            return this.a((CancelTask)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

