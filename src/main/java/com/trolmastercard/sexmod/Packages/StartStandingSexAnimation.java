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
import com.trolmastercard.sexmod.girls.PlayerGirl;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class StartStandingSexAnimation
implements IMessage {
    boolean c;
    UUID a;
    UUID b;
    String d;

    public StartStandingSexAnimation() {
    }

    public StartStandingSexAnimation(UUID uUID, UUID uUID2, String string) {
        this.a = uUID;
        this.b = uUID2;
        this.d = string;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.d = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.d);
    }

    public static class a_inner274
    implements IMessageHandler<StartStandingSexAnimation, IMessage> {
        public IMessage a(StartStandingSexAnimation eu_class2732, MessageContext messageContext) {
            if (!eu_class2732.c || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @StartStandingSexAnimation :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(eu_class2732.a);
                if (ei_class2512 == null) {
                    return;
                }
                if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
                    try {
                        for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                            if (!(em_class2582 instanceof PlayerGirl)) continue;
                            ei_class2512 = (PlayerGirl)em_class2582;
                            if (ei_class2512.world.isRemote || !ei_class2512.java_util_UUID_m().equals(eu_class2732.a)) continue;
                            break;
                        }
                    } catch (Exception exception) {
                        // empty catch block
                    }
                }
                ei_class2512.b(eu_class2732.d, eu_class2732.b);
            });
            return null;
        }

                @Override
        public IMessage onMessage(StartStandingSexAnimation iMessage, MessageContext messageContext) {
            return this.a((StartStandingSexAnimation)iMessage, messageContext);
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }
}

