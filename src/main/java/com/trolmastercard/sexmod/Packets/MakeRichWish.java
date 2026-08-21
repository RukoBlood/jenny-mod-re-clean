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

import com.trolmastercard.sexmod.util.Reference;
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

public class MakeRichWish implements IMessage {
    boolean isValid;
    Vec3d a;

    public MakeRichWish() {
    }

    public MakeRichWish(Vec3d vec3d) {
        this.a = vec3d;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.a.x);
        byteBuf.writeDouble(this.a.y);
        byteBuf.writeDouble(this.a.z);
    }

    public static class Handler implements IMessageHandler<MakeRichWish, IMessage> {
        @Override
        public IMessage onMessage(MakeRichWish msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @MakeRichWish :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                World world = ctx.getServerHandler().player.world;
                EntityItem entityItem = new EntityItem(world, msg.a.x, msg.a.y, msg.a.z, new ItemStack(Items.DIAMOND, Reference.RANDOM.nextInt(2) + 1));
                EntityItem entityItem2 = new EntityItem(world, msg.a.x, msg.a.y, msg.a.z, new ItemStack(Items.EMERALD, Reference.RANDOM.nextInt(2) + 1));
                EntityItem entityItem3 = new EntityItem(world, msg.a.x, msg.a.y, msg.a.z, new ItemStack(Items.GOLD_INGOT, Reference.RANDOM.nextInt(2) + 1));
                world.spawnEntity(entityItem);
                world.spawnEntity(entityItem2);
                world.spawnEntity(entityItem3);
            });
            return null;
        }
    }
}

