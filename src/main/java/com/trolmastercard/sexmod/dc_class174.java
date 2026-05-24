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

import com.trolmastercard.sexmod.girls.Ellie.EllieEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import io.netty.buffer.ByteBuf;
import java.util.ConcurrentModificationException;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class dc_class174
implements IMessage {
    boolean c;
    UUID a;
    boolean b;
    boolean d;
    UUID e = null;

    public dc_class174() {
        this.c = false;
    }

    public dc_class174(UUID uUID, UUID uUID2, boolean bl, boolean bl2) {
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

    public static class a_inner175
    implements IMessageHandler<dc_class174, IMessage> {
        public static void a(UUID uUID, UUID uUID2, boolean bl, boolean bl2) {
            try {
                for (GirlEntity em_class2582 : GirlEntity.girlList(uUID)) {
                    if (em_class2582.world.isRemote) continue;
                    if (em_class2582 instanceof JennyEntity || em_class2582 instanceof EllieEntity || em_class2582 instanceof LunaEntity) {
                        em_class2582.tasks.removeTask(em_class2582.o);
                        em_class2582.tasks.removeTask(em_class2582.avoidWater);
                    }
                    em_class2582.getNavigator().clearPath();
                    em_class2582.motionX = 0.0;
                    em_class2582.motionY = 0.0;
                    em_class2582.motionZ = 0.0;
                    if (em_class2582.getID() == null) {
                        em_class2582.void_e(uUID2);
                    }
                    if (bl2) {
                        em_class2582.c(em_class2582.net_minecraft_util_math_Vec3d_aa());
                    }
                    em_class2582.j(em_class2582.getID());
                    if (!bl) {
                        return;
                    }
                    if (!(em_class2582 instanceof bh_class82)) {
                        return;
                    }
                    bh_class82 bh_class822 = (bh_class82)((Object)em_class2582);
                    bh_class822.void_b();
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }

        public IMessage a(dc_class174 dc_class1742, MessageContext messageContext) {
            if (dc_class1742.c && messageContext.side == Side.SERVER) {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> a_inner175.a(dc_class1742.a, dc_class1742.e, dc_class1742.b, dc_class1742.d));
            }
            return null;
        }

                @Override
        public IMessage onMessage(dc_class174 iMessage, MessageContext messageContext) {
            return this.a((dc_class174)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

