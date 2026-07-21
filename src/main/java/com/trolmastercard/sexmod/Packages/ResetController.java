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

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ResetController
implements IMessage {
    final static public int b = 100;
    boolean d;
    UUID a;
    UUID c;

    public ResetController() {
        this.d = false;
    }

    public ResetController(UUID uUID) {
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

    public static class Handler
    implements IMessageHandler<ResetController, IMessage> {
        public IMessage a(ResetController a1_class72, MessageContext messageContext) {
            if (!a1_class72.d) {
                System.out.println("received an invalid message @ResetController :(");
                return null;
            }
            if (messageContext.side.isServer()) {
                GirlEntity em_class2582 = GirlEntity.getServerGirlEntity(a1_class72.a);
                if (em_class2582 == null) {
                    return null;
                }
                UUID uUID = messageContext.getServerHandler().player.getPersistentID();
                em_class2582.currentAction().ticksPlaying = new int[]{0, 0};
                for (EntityPlayerMP entityPlayerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                    if (uUID.equals(entityPlayerMP.getPersistentID()) || !(entityPlayerMP.getDistance(em_class2582) < 100.0f)) continue;
                    PackageHandler.networkWrapper.sendTo((IMessage)new ResetController(a1_class72.a), entityPlayerMP);
                }
                return null;
            }
            GirlEntity girlEntity = GirlEntity.getClientGirlEntity(a1_class72.a);
            if (girlEntity != null) {
                girlEntity.resetAnimationControllerTicks();
            }
            return null;
        }

                @Override
        public IMessage onMessage(ResetController iMessage, MessageContext messageContext) {
            return this.a((ResetController)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

