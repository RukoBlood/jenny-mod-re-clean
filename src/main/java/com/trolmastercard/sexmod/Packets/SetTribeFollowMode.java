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
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetTribeFollowMode implements IMessage {
    boolean isValid = false;
    boolean b;

    public SetTribeFollowMode() {
    }

    public SetTribeFollowMode(boolean bl) {
        this.b = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
    }

    public static class Handler implements IMessageHandler<SetTribeFollowMode, IMessage> {

        @Override
        public IMessage onMessage(SetTribeFollowMode msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side.isClient()) {
                System.out.println("received an invalid message @SetTribeFollowMode :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = KoboldManager.findTribeIdWith(ctx.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    return;
                }
                KoboldManager.setTribeAlerted(uUID, msg.b);
            });
            return null;
        }
    }
}

