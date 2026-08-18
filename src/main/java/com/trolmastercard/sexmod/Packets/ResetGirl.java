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
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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

public class ResetGirl
implements IMessage {
    boolean b;
    UUID c;
    boolean a;

    public ResetGirl() {
        this.b = false;
    }

    public ResetGirl(UUID uUID) {
        this.c = uUID;
        this.a = false;
        this.b = true;
    }

    public ResetGirl(UUID uUID, boolean bl) {
        this.c = uUID;
        this.a = bl;
        this.b = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.a = byteBuf.readBoolean();
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        byteBuf.writeBoolean(this.a);
        this.b = true;
    }

    public static class EventHandler
    implements IMessageHandler<ResetGirl, IMessage> {
        public static void resetGirl(GirlEntity em_class2582) {
            Object object;
            Object object2;
            em_class2582.ResetNPCTasks();
            if (em_class2582 instanceof PlayerGirl && em_class2582.world.getPlayerEntityByUUID(((PlayerGirl)em_class2582).getOwnerUserUUID()) != null) {
                PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(em_class2582.dimension).getPlayerEntityByUUID(((PlayerGirl)em_class2582).getOwnerUserUUID()));
                em_class2582.getDataManager().set(GirlEntity.OUTFIT_INDEX, 1);
                object2 = em_class2582.world.getPlayerEntityByUUID(((PlayerGirl)em_class2582).getOwnerUserUUID());
                ((EntityPlayer)object2).capabilities.isFlying = false;
                ((Entity)object2).setNoGravity(false);
                ((EntityPlayer)object2).noClip = false;
                em_class2582.setAnchored(false);
                em_class2582.setCurrentAction(Action.NULL);
                if (em_class2582.getInteractionPlayerUUID() != null && (object = em_class2582.world.getPlayerEntityByUUID(em_class2582.getInteractionPlayerUUID())) != null) {
                    ((EntityPlayer)object).capabilities.isFlying = false;
                    ((Entity)object).setNoGravity(false);
                    ((EntityPlayer)object).noClip = false;
                }
            }
            em_class2582.setAnchored(false);
            em_class2582.setInteractionPlayerUUID((UUID)null);
            em_class2582.playerCameraOffsetPos = null;
            em_class2582.setNoGravity(false);
            em_class2582.noClip = false;
            object2 = em_class2582.world;
            object = em_class2582.getPositionVector();
            while (((World)object2).getBlockState(new BlockPos(((Vec3d)object).x, ((Vec3d)object).y, ((Vec3d)object).z)).getBlock() != Blocks.AIR) {
                object = ((Vec3d)object).add(0.0, 1.0, 0.0);
            }
            em_class2582.setPositionAndUpdate(((Vec3d)object).x, ((Vec3d)object).y, ((Vec3d)object).z);
        }

        public static void resetGirls(EntityPlayerMP entityPlayerMP) {
            if (entityPlayerMP == null) {
                return;
            }
            World world = entityPlayerMP.world;
            Vec3d vec3d = entityPlayerMP.getPositionVector();
            while (world.getBlockState(new BlockPos(vec3d.x, vec3d.y, vec3d.z)).getBlock() != Blocks.AIR) {
                vec3d = vec3d.add(0.0, 1.0, 0.0);
            }
            entityPlayerMP.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            entityPlayerMP.setInvisible(false);
            entityPlayerMP.noClip = false;
            entityPlayerMP.setNoGravity(false);
            entityPlayerMP.capabilities.isFlying = false;
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(true), entityPlayerMP);
        }

        public IMessage a(ResetGirl s_class4212, MessageContext messageContext) {
            if (!s_class4212.b || messageContext.side != Side.SERVER) {
                System.out.println("recieved an unvalid message @ResetGirl :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(s_class4212.c);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote) continue;
                    if (em_class2582.getInteractionPlayerUUID() != null) {
                        EventHandler.resetGirls(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(em_class2582.getInteractionPlayerUUID()));
                    }
                    if (s_class4212.a) continue;
                    EventHandler.resetGirl(em_class2582);
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(ResetGirl iMessage, MessageContext messageContext) {
            return this.a((ResetGirl)iMessage, messageContext);
        }
    }
}

