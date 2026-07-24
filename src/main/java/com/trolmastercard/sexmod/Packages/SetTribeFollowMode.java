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

import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetTribeFollowMode
implements IMessage {
    boolean a = false;
    boolean b;

    public SetTribeFollowMode() {
    }

    public SetTribeFollowMode(boolean bl) {
        this.b = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
    }

    public static class a_inner316
    implements IMessageHandler<SetTribeFollowMode, IMessage> {
        public IMessage a(SetTribeFollowMode fj_class3152, MessageContext messageContext) {
            if (!fj_class3152.a || messageContext.side.isClient()) {
                System.out.println("received an invalid message @SetTribeFollowMode :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = KoboldManager.findTribeIdWith(messageContext.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    return;
                }
                KoboldManager.setTribeAlerted(uUID, fj_class3152.b);
            });
            return null;
        }

                @Override
        public IMessage onMessage(SetTribeFollowMode iMessage, MessageContext messageContext) {
            return this.a((SetTribeFollowMode)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

