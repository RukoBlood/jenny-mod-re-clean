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
import com.trolmastercard.sexmod.girls.Bee.BeeEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BeeOpenChest
implements IMessage {
    boolean b = false;
    UUID a;
    UUID c;

    public BeeOpenChest() {
    }

    public BeeOpenChest(UUID uUID, UUID uUID2) {
        this.a = uUID;
        this.c = uUID2;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
    }

    public static class a_inner288
    implements IMessageHandler<BeeOpenChest, IMessage> {
        public IMessage a(BeeOpenChest f3_class2872, MessageContext messageContext) {
            if (!f3_class2872.b) {
                System.out.println("received an invalid message @BeeOpenChest :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(f3_class2872.a);
                for (GirlEntity em_class2582 : arrayList) {
                    EntityPlayerMP entityPlayerMP;
                    BeeEntity fz_class3362;
                    if (em_class2582.world.isRemote || !(em_class2582 instanceof BeeEntity) || !(fz_class3362 = (BeeEntity)em_class2582).getDataManager().get(BeeEntity.IS_TAMED).booleanValue() || (entityPlayerMP = (EntityPlayerMP)fz_class3362.world.getPlayerEntityByUUID(f3_class2872.c)) == null) continue;
                    entityPlayerMP.openGui(Main.instance, 1, em_class2582.world, em_class2582.getPosition().getX(), em_class2582.getPosition().getY(), em_class2582.getPosition().getZ());
                    return;
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(BeeOpenChest iMessage, MessageContext messageContext) {
            return this.a((BeeOpenChest)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

