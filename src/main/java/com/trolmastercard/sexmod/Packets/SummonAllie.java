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

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
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

public class SummonAllie
implements IMessage {
    boolean isValid = false;

    public void fromBytes(ByteBuf byteBuf) {
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
    }

    public static class Handler implements IMessageHandler<SummonAllie, IMessage> {
        @Override
        public IMessage onMessage(SummonAllie msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @SummonAllie :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
                Vec3d vec3d = entityPlayerMP.getPositionVector().add(-Math.sin((double)entityPlayerMP.rotationYawHead * (Math.PI / 180)) * 2.0, 0.0, Math.cos((double)entityPlayerMP.rotationYawHead * (Math.PI / 180)) * 2.0);
                AllieEntity allie = new AllieEntity(entityPlayerMP.world, entityPlayerMP.getHeldItemMainhand());
                allie.setInteractionPlayerUUID(entityPlayerMP.getPersistentID());
                allie.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, entityPlayerMP.rotationYawHead + 180.0f, entityPlayerMP.rotationPitch);
                allie.setTargetPosition(allie.getPositionVector());
                allie.setYawRotation(entityPlayerMP.rotationYawHead + 180.0f);
                allie.setNoGravity(true);
                allie.noClip = true;
                entityPlayerMP.world.spawnEntity(allie);
                BlockPos blockPos = allie.getPosition().add(0, -1, 0);
                if (allie.world.getBlockState(blockPos).getBlock().equals(Blocks.SAND)) {
                    allie.setCurrentAction(Action.SUMMON_SAND);
                } else {
                    allie.setCurrentAction(allie.hasLampItem() ? Action.SUMMON : Action.SUMMON_NORMAL);
                }
            });
            return null;
        }
    }
}

