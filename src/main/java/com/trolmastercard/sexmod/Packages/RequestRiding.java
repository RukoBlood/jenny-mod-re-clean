/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.girls.Action;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestRiding
implements IMessage {
    boolean valid = false;

    public void fromBytes(ByteBuf byteBuf) {
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class Handler
    implements IMessageHandler<RequestRiding, IMessage> {
        public IMessage a(RequestRiding msg, MessageContext ctx) {
            if (!msg.valid || !ctx.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid message @RequestRiding :(");
                return null;
            }
            EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
            UUID uUID = GalathMangTracker.b(entityPlayerMP);
            GirlEntity em_class2582 = GirlEntity.getServerGirlEntity(uUID);
            if (em_class2582 == null) {
                return null;
            }
            ((Entity)entityPlayerMP).startRiding(em_class2582, true);
            em_class2582.setCurrentAction(Action.CONTROLLED_FLIGHT);
            em_class2582.setInteractionPlayer(entityPlayerMP);
            em_class2582.motionY = 0.25;
            entityPlayerMP.world.getChunk(em_class2582.getPosition()).removeEntity(em_class2582);
            return null;
        }

                @Override
        public IMessage onMessage(RequestRiding iMessage, MessageContext messageContext) {
            return this.a((RequestRiding)iMessage, messageContext);
        }
    }
}

