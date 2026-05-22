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
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class au_class44
implements IMessage {
    boolean a = false;
    BlockPos b;

    public au_class44() {
    }

    public au_class44(BlockPos blockPos) {
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
    implements IMessageHandler<au_class44, IMessage> {
        public IMessage a(au_class44 au_class442, MessageContext messageContext) {
            if (!au_class442.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid Message @CancelTask :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = KoboldManager.findTribeIdWith(messageContext.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    return;
                }
                HashSet<BlockPos> hashSet = KoboldManager.c(uUID, au_class442.b);
                if (hashSet.isEmpty()) {
                    return;
                }
                NetworkRegistry.networkWrapper.sendTo((IMessage)new h6_class397(hashSet, false), messageContext.getServerHandler().player);
            });
            return null;
        }

                @Override
        public IMessage onMessage(au_class44 iMessage, MessageContext messageContext) {
            return this.a((au_class44)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

