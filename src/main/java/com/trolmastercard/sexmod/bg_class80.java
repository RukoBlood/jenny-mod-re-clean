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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class bg_class80
implements IMessage {
    boolean a = false;

    public void fromBytes(ByteBuf byteBuf) {
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class a_inner81
    implements IMessageHandler<bg_class80, IMessage> {
        public IMessage a(bg_class80 bg_class802, MessageContext messageContext) {
            if (!bg_class802.a || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @SummonAllie :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                EntityPlayerMP entityPlayerMP = messageContext.getServerHandler().player;
                Vec3d vec3d = entityPlayerMP.getPositionVector().add(-Math.sin((double)entityPlayerMP.rotationYawHead * (Math.PI / 180)) * 2.0, 0.0, Math.cos((double)entityPlayerMP.rotationYawHead * (Math.PI / 180)) * 2.0);
                AllieEntity ev_class2752 = new AllieEntity(entityPlayerMP.world, entityPlayerMP.getHeldItemMainhand());
                ev_class2752.void_e(entityPlayerMP.getPersistentID());
                ev_class2752.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, entityPlayerMP.rotationYawHead + 180.0f, entityPlayerMP.rotationPitch);
                ev_class2752.c(ev_class2752.getPositionVector());
                ev_class2752.void_b(entityPlayerMP.rotationYawHead + 180.0f);
                ev_class2752.setNoGravity(true);
                ev_class2752.noClip = true;
                entityPlayerMP.world.spawnEntity(ev_class2752);
                BlockPos blockPos = ev_class2752.getPosition().add(0, -1, 0);
                if (ev_class2752.world.getBlockState(blockPos).getBlock().equals(Blocks.SAND)) {
                    ev_class2752.setCurrentAction(Action.SUMMON_SAND);
                } else {
                    ev_class2752.setCurrentAction(ev_class2752.boolean_f() ? Action.SUMMON : Action.SUMMON_NORMAL);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(bg_class80 iMessage, MessageContext messageContext) {
            return this.a((bg_class80)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

