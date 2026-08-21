/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
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

    public static class Handler implements IMessageHandler<RequestRiding, IMessage> {
        @Override
        public IMessage onMessage(RequestRiding msg, MessageContext ctx) {
            if (!msg.valid || !ctx.side.equals(Side.SERVER)) {
                System.out.println("received an invalid message @RequestRiding :(");
                return null;
            }
            EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
            UUID uUID = GalathMangTracker.getOwnerOf(entityPlayerMP);
            GirlEntity girl = GirlEntity.getServerGirlEntity(uUID);
            if (girl == null) {
                return null;
            }
            entityPlayerMP.startRiding(girl, true);
            girl.setCurrentAction(Action.CONTROLLED_FLIGHT);
            girl.setInteractionPlayer(entityPlayerMP);
            girl.motionY = 0.25;
            entityPlayerMP.world.getChunk(girl.getPosition()).removeEntity(girl);
            return null;
        }
    }
}

