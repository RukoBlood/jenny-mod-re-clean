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

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class bo_class90
implements IMessage {
    boolean c;
    UUID a;
    UUID b;

    public bo_class90() {
    }

    public bo_class90(UUID uUID, UUID uUID2) {
        this.a = uUID;
        this.b = uUID2;
        this.c = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.c = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
    }

    public static class a_inner91
    implements IMessageHandler<bo_class90, IMessage> {
        public IMessage a(bo_class90 bo_class902, MessageContext messageContext) {
            if (!bo_class902.c || messageContext.side != Side.SERVER) {
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (em_class2582.world.isRemote || !em_class2582.girlID().equals(bo_class902.a)) continue;
                    ((EntityPlayerMP)em_class2582.world.getPlayerEntityByUUID(bo_class902.b)).openGui(Main.instance, 0, em_class2582.world, em_class2582.getPosition().getX(), em_class2582.getPosition().getY(), em_class2582.getPosition().getZ());
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(bo_class90 iMessage, MessageContext messageContext) {
            return this.a((bo_class90)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

