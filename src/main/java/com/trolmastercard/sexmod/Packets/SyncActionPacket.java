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

import com.trolmastercard.sexmod.util.interfaces.IEllie;
import com.trolmastercard.sexmod.girls.Ellie.EllieEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SyncActionPacket implements IMessage {
    boolean isValid;
    UUID girlId;
    boolean param1;
    boolean param2;
    UUID playerId = null;

    public SyncActionPacket() {
        this.isValid = false;
    }

    public SyncActionPacket(UUID uUID, UUID uUID2, boolean bl, boolean bl2) {
        this.girlId = uUID;
        this.param1 = bl;
        this.playerId = uUID2;
        this.param2 = bl2;
        this.isValid = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.param1 = byteBuf.readBoolean();
        this.param2 = byteBuf.readBoolean();
        String string = ByteBufUtils.readUTF8String(byteBuf);
        this.playerId = string.equals("null") ? null : UUID.fromString(string);
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        byteBuf.writeBoolean(this.param1);
        byteBuf.writeBoolean(this.param2);
        ByteBufUtils.writeUTF8String(byteBuf, this.playerId == null ? "null" : this.playerId.toString());
    }

    public static class Handler implements IMessageHandler<SyncActionPacket, IMessage> {
        public static void execute(UUID uUID, UUID uUID2, boolean bl, boolean bl2) {
            for (GirlEntity girl : GirlEntity.girlList(uUID)) {
                if (girl.world.isRemote) continue;
                if (girl instanceof JennyEntity || girl instanceof EllieEntity || girl instanceof LunaEntity) {
                    girl.tasks.removeTask(girl.watchClosestGirlGoal);
                    girl.tasks.removeTask(girl.aiWander);
                }
                girl.getNavigator().clearPath();
                girl.motionX = 0.0;
                girl.motionY = 0.0;
                girl.motionZ = 0.0;
                if (girl.getInteractionPlayerUUID() == null) {
                    girl.setInteractionPlayerUUID(uUID2);
                }
                if (bl2) {
                    girl.setTargetPosition(girl.getFrontOffsetVector());
                }
                girl.snapPlayerToPosition(girl.getInteractionPlayerUUID());
                if (bl) {
                    if (girl instanceof IEllie) {
                        IEllie ellie = (IEllie) girl;
                        ellie.setDismounted();
                    }
                }
            }
        }

        @Override
        public IMessage onMessage(SyncActionPacket msg, MessageContext ctx) {
            if (msg.isValid && ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> Handler.execute(msg.girlId, msg.playerId, msg.param1, msg.param2));
            }
            return null;
        }
    }
}

