/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class bk_class85
implements IMessage {
    boolean a = false;

    public void fromBytes(ByteBuf byteBuf) {
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class a_inner86
    implements IMessageHandler<bk_class85, IMessage> {
        public IMessage a(bk_class85 bk_class852, MessageContext messageContext) {
            if (!bk_class852.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid message @RequestRiding :(");
                return null;
            }
            EntityPlayerMP entityPlayerMP = messageContext.getServerHandler().player;
            UUID uUID = GalathMangTracker.b(entityPlayerMP);
            GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID);
            if (em_class2582 == null) {
                return null;
            }
            ((Entity)entityPlayerMP).startRiding(em_class2582, true);
            em_class2582.setCurrentAction(Action.CONTROLLED_FLIGHT);
            em_class2582.void_a(entityPlayerMP);
            em_class2582.motionY = 0.25;
            entityPlayerMP.world.getChunk(em_class2582.getPosition()).removeEntity(em_class2582);
            return null;
        }

                @Override
        public IMessage onMessage(bk_class85 iMessage, MessageContext messageContext) {
            return this.a((bk_class85)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

