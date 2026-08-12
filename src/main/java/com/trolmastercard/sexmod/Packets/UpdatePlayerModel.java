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
import java.util.ConcurrentModificationException;
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
    boolean b = false;
    PlayerGirlEntity a;

    public UpdatePlayerModel() {
    }

    public UpdatePlayerModel(PlayerGirlEntity fy_class3352) {
        this.a = fy_class3352;
    }

    public void fromBytes(ByteBuf byteBuf) {
        String string = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.a = "player".equals(string) ? null : PlayerGirlEntity.valueOf(string);
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (this.a == null) {
            ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)"player");
        } else {
            ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner72
    implements IMessageHandler<UpdatePlayerModel, IMessage> {
        public IMessage a(UpdatePlayerModel b__class712, MessageContext messageContext) {
            if (!b__class712.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @UpdatePlayerModel :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                PlayerGirl ei_class2512;
                PlayerGirlEntity fy_class3352;
                EntityPlayerMP entityPlayerMP = messageContext.getServerHandler().player;
                World world = entityPlayerMP.world;
                UUID uUID = messageContext.getServerHandler().player.getPersistentID();
                PlayerGirl ei_class2513 = PlayerGirl.getUUIDHashtable(uUID);
                if (ei_class2513 != null) {
                    for (GirlEntity object2 : GirlEntity.GirlEntityList()) {
                        if (object2.world.isRemote || !object2.girlID().equals(ei_class2513.girlID())) continue;
                        world.removeEntity(object2);
                    }
                    ei_class2513.void_y();
                    PlayerGirl.playerGirlUUIDHashtable.remove(uUID);
                    GirlEntity.GirlEntityList().remove(ei_class2513);
                    ei_class2513.a(Optional.absent());
                }
                if ((fy_class3352 = b__class712.a) == null) {
                    return;
                }
                try {
                    Constructor<? extends PlayerGirl> exception = fy_class3352.playerClass.getConstructor(World.class, UUID.class);
                    ei_class2512 = exception.newInstance(world, messageContext.getServerHandler().player.getPersistentID());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }
                ei_class2512.setNoGravity(true);
                ei_class2512.noClip = true;
                ei_class2512.motionX = 0.0;
                ei_class2512.motionY = 0.0;
                ei_class2512.motionZ = 0.0;
                ei_class2512.setPosition(entityPlayerMP.posX, entityPlayerMP.posY + 69.0, entityPlayerMP.posZ);
                world.spawnEntity(ei_class2512);
                ei_class2512.spawnHitboxHelper();
            });
            return null;
        }

                @Override
        public IMessage onMessage(UpdatePlayerModel iMessage, MessageContext messageContext) {
            return this.a((UpdatePlayerModel)iMessage, messageContext);
        }

        private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
            return concurrentModificationException;
        }
    }
}

