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
    PlayerGirlEntity type;

    public UpdatePlayerModel() {
    }

    public UpdatePlayerModel(PlayerGirlEntity type) {
        this.type = type;
    }

    public void fromBytes(ByteBuf byteBuf) {
        String typeStr = ByteBufUtils.readUTF8String(byteBuf);
        this.type = "player".equals(typeStr) ? null : PlayerGirlEntity.valueOf(typeStr);
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (this.type == null) {
            ByteBufUtils.writeUTF8String(byteBuf, "player");
        } else {
            ByteBufUtils.writeUTF8String(byteBuf, this.type.toString());
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
                EntityPlayerMP player = ctx.getServerHandler().player;
                World world = player.world;
                UUID uUID = ctx.getServerHandler().player.getPersistentID();
                PlayerGirl pg = PlayerGirl.getUUIDHashtable(uUID);
                if (pg != null) {
                    for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                        if (girl.world.isRemote || !girl.girlID().equals(pg.girlID())) continue;
                        world.removeEntity(girl);
                    }
                    pg.onTickClient();
                    PlayerGirl.playerGirlUUIDHashtable.remove(uUID);
                    GirlEntity.getGirlEntityList().remove(pg);
                    pg.setOwnerId(Optional.absent());
                }
                if ((pgEntity = msg.type) == null) {
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
                playerGirl.setPosition(player.posX, player.posY + 69.0, player.posZ);
                world.spawnEntity(playerGirl);
                playerGirl.spawnHitboxHelper();
            });
            return null;
        }
    }
}

