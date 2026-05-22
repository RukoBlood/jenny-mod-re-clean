/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class z_class433
implements IMessage {
    boolean a;

    public void fromBytes(ByteBuf byteBuf) {
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class a_inner434
    implements IMessageHandler<z_class433, IMessage> {
        public IMessage a(z_class433 z_class4332, MessageContext messageContext) {
            if (!z_class4332.a || !messageContext.side.equals((Object)Side.SERVER)) {
                System.out.println("received an invalid Message @SendEgg :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                EntityPlayerMP entityPlayerMP = messageContext.getServerHandler().player;
                UUID uUID = KoboldManager.findTribeIdWith(entityPlayerMP.getPersistentID());
                if (uUID == null) {
                    return;
                }
                EyeAndKoboldColor eyeAndKoboldColor_ = KoboldManager.l(uUID);
                ItemStack itemStack = new ItemStack(KoboldEggItem.KOBOLD_EGG, 1, eyeAndKoboldColor_.getWoolMeta());
                NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
                if (nBTTagCompound == null) {
                    nBTTagCompound = new NBTTagCompound();
                }
                nBTTagCompound.setString("tribeID", uUID.toString());
                itemStack.setTagCompound(nBTTagCompound);
                entityPlayerMP.inventory.addItemStackToInventory(itemStack);
            });
            return null;
        }

                @Override
        public IMessage onMessage(z_class433 iMessage, MessageContext messageContext) {
            return this.a((z_class433)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

