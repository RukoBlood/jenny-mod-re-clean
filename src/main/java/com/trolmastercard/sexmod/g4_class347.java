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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class g4_class347
implements IMessage {
    boolean e = false;
    String c;
    UUID b;
    UUID a;
    boolean d;

    public g4_class347() {
    }

    public g4_class347(String string, UUID uUID, UUID uUID2, boolean bl) {
        this.c = string;
        this.b = uUID;
        this.a = uUID2;
        this.d = bl;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.d = byteBuf.readBoolean();
        this.e = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        byteBuf.writeBoolean(this.d);
    }

    public static class a_inner348
    implements IMessageHandler<g4_class347, IMessage> {
        public IMessage a(g4_class347 g4_class3472, MessageContext messageContext) {
            if (!g4_class3472.e) {
                System.out.println("received an invalid message @SexPrompt :(");
                return null;
            }
            if (messageContext.side.equals((Object)Side.CLIENT)) {
                w_class427.a.a(new w_class427.a_inner428(g4_class3472.c, g4_class3472.b, g4_class3472.a, g4_class3472.d));
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                World world = messageContext.getServerHandler().player.world;
                EntityPlayer entityPlayer = world.getPlayerEntityByUUID(g4_class3472.a);
                EntityPlayer entityPlayer2 = world.getPlayerEntityByUUID(g4_class3472.b);
                if (entityPlayer == null) {
                    System.out.println("Sex prompt invalid -> female player not found");
                    return;
                }
                if (entityPlayer2 == null) {
                    System.out.println("Sex prompt invalid -> male player not found");
                    return;
                }
                NetworkRegistry.networkWrapper.sendTo((IMessage)new g4_class347(g4_class3472.c, g4_class3472.b, g4_class3472.a, g4_class3472.d), (EntityPlayerMP)(g4_class3472.d ? entityPlayer : entityPlayer2));
            });
            return null;
        }

                @Override
        public IMessage onMessage(g4_class347 iMessage, MessageContext messageContext) {
            return this.a((g4_class347)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

