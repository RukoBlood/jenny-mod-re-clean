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
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.girls.Luna.FishingRod.LunaRod;
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

public class CatActivateFishing implements IMessage {
    boolean isValid = false;
    UUID girls;

    public CatActivateFishing() {
    }

    public CatActivateFishing(UUID uUID) {
        this.girls = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girls = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girls.toString());
    }

    public static class Handler implements IMessageHandler<CatActivateFishing, IMessage> {
        @Override
        public IMessage onMessage(CatActivateFishing msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @CatActivateFishing :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girls);
                for (GirlEntity girl : girls) {
                    if (!girl.world.isRemote && girl instanceof LunaEntity) {
                        LunaEntity luna = (LunaEntity) girl;
                        ItemStack rod = luna.lunaRod;
                        LunaRod rodItem = (LunaRod) rod.getItem();
                        rodItem.onItemRightClick(ctx.getServerHandler().player.world, luna, EnumHand.MAIN_HAND);
                    }
                }
            });
            return null;
        }
    }
}

