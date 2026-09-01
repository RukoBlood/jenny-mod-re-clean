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
    Vec3d pos;

    public MakeRichWish() {
    }

    public MakeRichWish(Vec3d vec3d) {
        this.pos = vec3d;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.pos = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(this.pos.x);
        byteBuf.writeDouble(this.pos.y);
        byteBuf.writeDouble(this.pos.z);
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
                EntityItem firstItem = new EntityItem(world, msg.pos.x, msg.pos.y, msg.pos.z, new ItemStack(Items.DIAMOND, Reference.RANDOM.nextInt(2) + 1));
                EntityItem secondItem = new EntityItem(world, msg.pos.x, msg.pos.y, msg.pos.z, new ItemStack(Items.EMERALD, Reference.RANDOM.nextInt(2) + 1));
                EntityItem thirdItem = new EntityItem(world, msg.pos.x, msg.pos.y, msg.pos.z, new ItemStack(Items.GOLD_INGOT, Reference.RANDOM.nextInt(2) + 1));
                world.spawnEntity(firstItem);
                world.spawnEntity(secondItem);
                world.spawnEntity(thirdItem);
            });
            return null;
        }
    }
}

