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
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.ho_class404;
import com.trolmastercard.sexmod.util.Reference;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SendCompanionHome implements IMessage {
    boolean b;
    UUID a;

    public SendCompanionHome() {
    }

    public SendCompanionHome(UUID uUID) {
        this.a = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
    }

    public static class Handler implements IMessageHandler<SendCompanionHome, IMessage> {
        public IMessage a(SendCompanionHome gg_class3662, MessageContext messageContext) {
            if (!gg_class3662.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @SendCompanionHome :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(gg_class3662.a);
                for (GirlEntity girlEntity : arrayList) {
                    if (girlEntity.world.isRemote) continue;
                    if (girlEntity.currentAction() != Action.THROW_PEARL) {
                        girlEntity.setCurrentAction(Action.THROW_PEARL);
                        girlEntity.setYawRotation((float)Math.atan2(girlEntity.posZ - girlEntity.homeCoords.z, girlEntity.posX - girlEntity.homeCoords.x) * 57.29578f + 90.0f);
                        girlEntity.setTargetPosition(girlEntity.getPositionVector());
                        girlEntity.getDataManager().set(GirlEntity.IS_ANCHORED, true);
                        girlEntity.activePearl = null;
                        continue;
                    }
                    if (girlEntity.activePearl == null) {
                        float f = (float)girlEntity.getPositionVector().distanceTo(girlEntity.homeCoords);
                        girlEntity.activePearl = new ho_class404(girlEntity.world, girlEntity);
                        girlEntity.activePearl.shoot(girlEntity.homeCoords.x - girlEntity.posX, girlEntity.homeCoords.y - girlEntity.posY, girlEntity.homeCoords.z - girlEntity.posZ, Math.min(4.0f, f * 0.1f), 0.0f);
                        girlEntity.world.spawnEntity(girlEntity.activePearl);
                        continue;
                    }
                    WorldServer worldServer = (WorldServer)girlEntity.world;
                    for (int i = 0; i < 32; ++i) {
                        worldServer.spawnParticle(EnumParticleTypes.PORTAL, false, girlEntity.posX, girlEntity.posY + Reference.RANDOM.nextDouble() * 2.0, girlEntity.posZ, 32, 0.2, 0.2, 0.2, Reference.RANDOM.nextGaussian(), new int[0]);
                    }
                    girlEntity.setPosition(girlEntity.homeCoords.x, girlEntity.homeCoords.y, girlEntity.homeCoords.z);
                    girlEntity.activePearl = null;
                    girlEntity.setCurrentAction(Action.NULL);
                    girlEntity.getDataManager().set(GirlEntity.IS_ANCHORED, false);
                    girlEntity.goHome();
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(SendCompanionHome iMessage, MessageContext messageContext) {
            return this.a((SendCompanionHome)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

