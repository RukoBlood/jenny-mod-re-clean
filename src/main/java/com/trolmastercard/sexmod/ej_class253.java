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
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ej_class253
implements IMessage {
    boolean b = false;
    UUID a;

    public ej_class253() {
    }

    public ej_class253(UUID uUID) {
        this.a = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
    }

    public static class a_inner254
    implements IMessageHandler<ej_class253, IMessage> {
        public IMessage a(ej_class253 ej_class2532, MessageContext messageContext) {
            if (!ej_class2532.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @CatActivateFishing :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(ej_class2532.a);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote || !(em_class2582 instanceof LunaEntity)) continue;
                    LunaEntity eb_class2362 = (LunaEntity)em_class2582;
                    ItemStack itemStack = eb_class2362.ao;
                    LunaRod gp_class3792 = (LunaRod)itemStack.getItem();
                    gp_class3792.a(messageContext.getServerHandler().player.world, eb_class2362, EnumHand.MAIN_HAND);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(ej_class253 iMessage, MessageContext messageContext) {
            return this.a((ej_class253)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

