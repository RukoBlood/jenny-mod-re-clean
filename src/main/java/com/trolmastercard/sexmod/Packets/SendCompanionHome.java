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
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.companion.CompanionPearl;
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
    boolean isValid;
    UUID girlId;

    public SendCompanionHome() {
    }

    public SendCompanionHome(UUID uUID) {
        this.girlId = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
    }

    public static class Handler implements IMessageHandler<SendCompanionHome, IMessage> {
        @Override
        public IMessage onMessage(SendCompanionHome msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @SendCompanionHome :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girlId);
                for (GirlEntity girl : girls) {
                    if (!girl.world.isRemote) {
                        if (girl.getCurrentAction() != Action.THROW_PEARL) {
                            girl.setCurrentAction(Action.THROW_PEARL);
                            girl.setYawRotation((float) Math.atan2(girl.posZ - girl.homeCoords.z, girl.posX - girl.homeCoords.x) * 57.29578f + 90.0f);
                            girl.setTargetPosition(girl.getPositionVector());
                            girl.getDataManager().set(GirlEntity.IS_ANCHORED, true);
                            girl.activePearl = null;
                            continue;
                        }
                        if (girl.activePearl == null) {
                            float f = (float) girl.getPositionVector().distanceTo(girl.homeCoords);
                            girl.activePearl = new CompanionPearl(girl.world, girl);
                            girl.activePearl.shoot(girl.homeCoords.x - girl.posX, girl.homeCoords.y - girl.posY, girl.homeCoords.z - girl.posZ, Math.min(4.0f, f * 0.1f), 0.0f);
                            girl.world.spawnEntity(girl.activePearl);
                            continue;
                        }
                        WorldServer worldServer = (WorldServer) girl.world;
                        for (int i = 0; i < 32; ++i) {
                            worldServer.spawnParticle(EnumParticleTypes.PORTAL, false, girl.posX, girl.posY + Reference.RANDOM.nextDouble() * 2.0, girl.posZ, 32, 0.2, 0.2, 0.2, Reference.RANDOM.nextGaussian());
                        }
                        girl.setPosition(girl.homeCoords.x, girl.homeCoords.y, girl.homeCoords.z);
                        girl.activePearl = null;
                        girl.setCurrentAction(Action.NULL);
                        girl.getDataManager().set(GirlEntity.IS_ANCHORED, false);
                        girl.goHome();
                    }
                }
            });
            return null;
        }
    }
}

