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
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class a1_class7
implements IMessage {
    final static public int b = 100;
    boolean d;
    UUID a;
    UUID c;

    public a1_class7() {
        this.d = false;
    }

    public a1_class7(UUID uUID) {
        this.a = uUID;
        this.d = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.d = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
    }

    public static class a_inner8
    implements IMessageHandler<a1_class7, IMessage> {
        public IMessage a(a1_class7 a1_class72, MessageContext messageContext) {
            if (!a1_class72.d) {
                System.out.println("received an invalid message @ResetController :(");
                return null;
            }
            if (messageContext.side.isServer()) {
                GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(a1_class72.a);
                if (em_class2582 == null) {
                    return null;
                }
                UUID uUID = messageContext.getServerHandler().player.getPersistentID();
                em_class2582.currentAction().ticksPlaying = new int[]{0, 0};
                for (EntityPlayerMP entityPlayerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                    if (uUID.equals(entityPlayerMP.getPersistentID()) || !(entityPlayerMP.getDistance(em_class2582) < 100.0f)) continue;
                    NetworkRegistry.networkWrapper.sendTo((IMessage)new a1_class7(a1_class72.a), entityPlayerMP);
                }
                return null;
            }
            GirlEntity girlEntity = GirlEntity.getGirlEntity(a1_class72.a);
            if (girlEntity != null) {
                girlEntity.ag();
            }
            return null;
        }

                @Override
        public IMessage onMessage(a1_class7 iMessage, MessageContext messageContext) {
            return this.a((a1_class7)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

