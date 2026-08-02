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

import com.trolmastercard.sexmod.util.interfaces.bh_class82;
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

public class SyncActionPacket
implements IMessage {
    boolean c;
    UUID a;
    boolean b;
    boolean d;
    UUID e = null;

    public SyncActionPacket() {
        this.c = false;
    }

    public SyncActionPacket(UUID uUID, UUID uUID2, boolean bl, boolean bl2) {
        this.a = uUID;
        this.b = bl;
        this.e = uUID2;
        this.d = bl2;
        this.c = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = byteBuf.readBoolean();
        this.d = byteBuf.readBoolean();
        String string = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.e = string.equals("null") ? null : UUID.fromString(string);
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        byteBuf.writeBoolean(this.b);
        byteBuf.writeBoolean(this.d);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)(this.e == null ? "null" : this.e.toString()));
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class Handler implements IMessageHandler<SyncActionPacket, IMessage> {
        public static void execute(UUID uUID, UUID uUID2, boolean bl, boolean bl2) {
            for (GirlEntity girl : GirlEntity.girlList(uUID)) {
                if (girl.world.isRemote) continue;
                if (girl instanceof JennyEntity || girl instanceof EllieEntity || girl instanceof LunaEntity) {
                    girl.tasks.removeTask(girl.aiLookAtPlayer);
                    girl.tasks.removeTask(girl.aiWander);
                }
                girl.getNavigator().clearPath();
                girl.motionX = 0.0;
                girl.motionY = 0.0;
                girl.motionZ = 0.0;
                if (girl.getID() == null) {
                    girl.setInteractionPlayerUUID(uUID2);
                }
                if (bl2) {
                    girl.setTargetPosition(girl.getFrontOffsetVector());
                }
                girl.snapPlayerToPosition(girl.getID());
                if (!bl) {
                    return;
                }
                if (!(girl instanceof bh_class82)) {
                    return;
                }
                bh_class82 bh_class822 = (bh_class82) ((Object) girl);
                bh_class822.void_b();
            }
        }

        public IMessage a(SyncActionPacket dc_class1742, MessageContext messageContext) {
            if (dc_class1742.c && messageContext.side == Side.SERVER) {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> Handler.execute(dc_class1742.a, dc_class1742.e, dc_class1742.b, dc_class1742.d));
            }
            return null;
        }

                @Override
        public IMessage onMessage(SyncActionPacket iMessage, MessageContext messageContext) {
            return this.a((SyncActionPacket)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

