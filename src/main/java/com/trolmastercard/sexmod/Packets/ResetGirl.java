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

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ResetGirl implements IMessage {
    boolean isValid;
    UUID girlId;
    boolean a;

    public ResetGirl() {
        this.isValid = false;
    }

    public ResetGirl(UUID uUID) {
        this.girlId = uUID;
        this.a = false;
        this.isValid = true;
    }

    public ResetGirl(UUID uUID, boolean bl) {
        this.girlId = uUID;
        this.a = bl;
        this.isValid = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.a = byteBuf.readBoolean();
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        byteBuf.writeBoolean(this.a);
        this.isValid = true;
    }

    public static class EventHandler implements IMessageHandler<ResetGirl, IMessage> {
        public static void resetGirl(GirlEntity girl) {
            Object object;
            Object object2;
            girl.reInitTasks();
            if (girl instanceof PlayerGirl && girl.world.getPlayerEntityByUUID(((PlayerGirl)girl).getOwnerUserUUID()) != null) {
                PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(true), (EntityPlayerMP)FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(girl.dimension).getPlayerEntityByUUID(((PlayerGirl)girl).getOwnerUserUUID()));
                girl.getDataManager().set(GirlEntity.OUTFIT_INDEX, 1);
                object2 = girl.world.getPlayerEntityByUUID(((PlayerGirl)girl).getOwnerUserUUID());
                ((EntityPlayer)object2).capabilities.isFlying = false;
                ((Entity)object2).setNoGravity(false);
                ((EntityPlayer)object2).noClip = false;
                girl.setAnchored(false);
                girl.setCurrentAction(Action.NULL);
                if (girl.getInteractionPlayerUUID() != null && (object = girl.world.getPlayerEntityByUUID(girl.getInteractionPlayerUUID())) != null) {
                    ((EntityPlayer)object).capabilities.isFlying = false;
                    ((Entity)object).setNoGravity(false);
                    ((EntityPlayer)object).noClip = false;
                }
            }
            girl.setAnchored(false);
            girl.setInteractionPlayerUUID(null);
            girl.cameraOriginPos = null;
            girl.setNoGravity(false);
            girl.noClip = false;
            object2 = girl.world;
            object = girl.getPositionVector();
            while (((World)object2).getBlockState(new BlockPos(((Vec3d)object).x, ((Vec3d)object).y, ((Vec3d)object).z)).getBlock() != Blocks.AIR) {
                object = ((Vec3d)object).add(0.0, 1.0, 0.0);
            }
            girl.setPositionAndUpdate(((Vec3d)object).x, ((Vec3d)object).y, ((Vec3d)object).z);
        }

        public static void resetGirls(EntityPlayerMP player) {
            if (player != null) {
                World world = player.world;
                Vec3d vec3d = player.getPositionVector();
                while (world.getBlockState(new BlockPos(vec3d.x, vec3d.y, vec3d.z)).getBlock() != Blocks.AIR) {
                    vec3d = vec3d.add(0.0, 1.0, 0.0);
                }
                player.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                player.setInvisible(false);
                player.noClip = false;
                player.setNoGravity(false);
                player.capabilities.isFlying = false;
                PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(true), player);
            }
        }

        @Override
        public IMessage onMessage(ResetGirl msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("recieved an unvalid message @ResetGirl :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girlId);
                for (GirlEntity girl : girls) {
                    if (!girl.world.isRemote) {
                        if (girl.getInteractionPlayerUUID() != null) {
                            EventHandler.resetGirls(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(girl.getInteractionPlayerUUID()));
                        }
                        if (!msg.a) {
                            EventHandler.resetGirl(girl);
                        }
                    }
                }
            });
            return null;
        }
    }
}

