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
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MakeRichWish
implements IMessage {
    boolean b;
    Vec3d a;

    public MakeRichWish() {
    }

    public MakeRichWish(Vec3d vec3d) {
        this.a = vec3d;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.a.x);
        byteBuf.writeDouble(this.a.y);
        byteBuf.writeDouble(this.a.z);
    }

    public static class a_inner104
    implements IMessageHandler<MakeRichWish, IMessage> {
        public IMessage a(MakeRichWish bw_class1032, MessageContext messageContext) {
            if (!bw_class1032.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @MakeRichWish :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                World world = messageContext.getServerHandler().player.world;
                EntityItem entityItem = new EntityItem(world, bw_class1032.a.x, bw_class1032.a.y, bw_class1032.a.z, new ItemStack(Items.DIAMOND, ReferenceAndRotationHelper.RANDOM.nextInt(2) + 1));
                EntityItem entityItem2 = new EntityItem(world, bw_class1032.a.x, bw_class1032.a.y, bw_class1032.a.z, new ItemStack(Items.EMERALD, ReferenceAndRotationHelper.RANDOM.nextInt(2) + 1));
                EntityItem entityItem3 = new EntityItem(world, bw_class1032.a.x, bw_class1032.a.y, bw_class1032.a.z, new ItemStack(Items.GOLD_INGOT, ReferenceAndRotationHelper.RANDOM.nextInt(2) + 1));
                world.spawnEntity(entityItem);
                world.spawnEntity(entityItem2);
                world.spawnEntity(entityItem3);
            });
            return null;
        }

                @Override
        public IMessage onMessage(MakeRichWish iMessage, MessageContext messageContext) {
            return this.a((MakeRichWish)iMessage, messageContext);
        }
    }
}

