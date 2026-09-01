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
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ResetController implements IMessage {
    final static public int SOME_SHIT = 100;
    boolean point;
    UUID girlId;

    public ResetController() {
        this.point = false;
    }

    public ResetController(UUID uUID) {
        this.girlId = uUID;
        this.point = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.point = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
    }

    public static class Handler implements IMessageHandler<ResetController, IMessage> {
        @Override
        public IMessage onMessage(ResetController msg, MessageContext ctx) {
            if (!msg.point) {
                System.out.println("received an invalid message @ResetController :(");
                return null;
            }
            if (ctx.side.isServer()) {
                GirlEntity girl = GirlEntity.getServerGirlEntity(msg.girlId);
                if (girl == null) {
                    return null;
                }
                UUID uUID = ctx.getServerHandler().player.getPersistentID();
                girl.getCurrentAction().ticksPlaying = new int[]{0, 0};
                for (EntityPlayerMP entityPlayerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                    if (!uUID.equals(entityPlayerMP.getPersistentID()) && entityPlayerMP.getDistance(girl) < 100.0f) {
                        PacketHandler.INSTANCE.sendTo(new ResetController(msg.girlId), entityPlayerMP);
                    }
                }
                return null;
            }
            GirlEntity girlEntity = GirlEntity.getClientGirlEntity(msg.girlId);
            if (girlEntity != null) {
                girlEntity.resetAnimationControllerTicks();
            }
            return null;
        }
    }
}

