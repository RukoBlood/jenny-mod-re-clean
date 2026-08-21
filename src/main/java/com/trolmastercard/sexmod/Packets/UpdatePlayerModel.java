/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.google.common.base.Optional;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdatePlayerModel
implements IMessage {
    boolean isValid = false;
    PlayerGirlEntity a;

    public UpdatePlayerModel() {
    }

    public UpdatePlayerModel(PlayerGirlEntity fy_class3352) {
        this.a = fy_class3352;
    }

    public void fromBytes(ByteBuf byteBuf) {
        String string = ByteBufUtils.readUTF8String(byteBuf);
        this.a = "player".equals(string) ? null : PlayerGirlEntity.valueOf(string);
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (this.a == null) {
            ByteBufUtils.writeUTF8String(byteBuf, "player");
        } else {
            ByteBufUtils.writeUTF8String(byteBuf, this.a.toString());
        }
    }

    public static class Handler implements IMessageHandler<UpdatePlayerModel, IMessage> {
        @Override
        public IMessage onMessage(UpdatePlayerModel msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @UpdatePlayerModel :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                PlayerGirl playerGirl;
                PlayerGirlEntity pgEntity;
                EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
                World world = entityPlayerMP.world;
                UUID uUID = ctx.getServerHandler().player.getPersistentID();
                PlayerGirl ei_class2513 = PlayerGirl.getUUIDHashtable(uUID);
                if (ei_class2513 != null) {
                    for (GirlEntity object2 : GirlEntity.getGirlEntityList()) {
                        if (object2.world.isRemote || !object2.girlID().equals(ei_class2513.girlID())) continue;
                        world.removeEntity(object2);
                    }
                    ei_class2513.onTickClient();
                    PlayerGirl.playerGirlUUIDHashtable.remove(uUID);
                    GirlEntity.getGirlEntityList().remove(ei_class2513);
                    ei_class2513.setOwnerId(Optional.absent());
                }
                if ((pgEntity = msg.a) == null) {
                    return;
                }
                try {
                    Constructor<? extends PlayerGirl> exception = pgEntity.playerClass.getConstructor(World.class, UUID.class);
                    playerGirl = exception.newInstance(world, ctx.getServerHandler().player.getPersistentID());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }
                playerGirl.setNoGravity(true);
                playerGirl.noClip = true;
                playerGirl.motionX = 0.0;
                playerGirl.motionY = 0.0;
                playerGirl.motionZ = 0.0;
                playerGirl.setPosition(entityPlayerMP.posX, entityPlayerMP.posY + 69.0, entityPlayerMP.posZ);
                world.spawnEntity(playerGirl);
                playerGirl.spawnHitboxHelper();
            });
            return null;
        }
    }
}

