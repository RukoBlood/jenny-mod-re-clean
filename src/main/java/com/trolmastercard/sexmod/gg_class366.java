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
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.GirlEntity;
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

public class gg_class366 implements IMessage {
    boolean b;
    UUID a;

    public gg_class366() {
    }

    public gg_class366(UUID uUID) {
        this.a = uUID;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.a = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.a.toString());
    }

    public static class a_inner367 implements IMessageHandler<gg_class366, IMessage> {
        public IMessage a(gg_class366 gg_class3662, MessageContext messageContext) {
            if (!gg_class3662.b || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @SendCompanionHome :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(gg_class3662.a);
                for (GirlEntity em_class2582 : arrayList) {
                    if (em_class2582.world.isRemote) continue;
                    if (em_class2582.currentAction() != Action.THROW_PEARL) {
                        em_class2582.setCurrentAction(Action.THROW_PEARL);
                        em_class2582.void_b((float)Math.atan2(em_class2582.posZ - em_class2582.homeCoords.z, em_class2582.posX - em_class2582.homeCoords.x) * 57.29578f + 90.0f);
                        em_class2582.c(em_class2582.getPositionVector());
                        em_class2582.getDataManager().set(GirlEntity.G, true);
                        em_class2582.q = null;
                        continue;
                    }
                    if (em_class2582.q == null) {
                        float f = (float)em_class2582.getPositionVector().distanceTo(em_class2582.homeCoords);
                        em_class2582.q = new ho_class404(em_class2582.world, em_class2582);
                        em_class2582.q.shoot(em_class2582.homeCoords.x - em_class2582.posX, em_class2582.homeCoords.y - em_class2582.posY, em_class2582.homeCoords.z - em_class2582.posZ, Math.min(4.0f, f * 0.1f), 0.0f);
                        em_class2582.world.spawnEntity(em_class2582.q);
                        continue;
                    }
                    WorldServer worldServer = (WorldServer)em_class2582.world;
                    for (int i = 0; i < 32; ++i) {
                        worldServer.spawnParticle(EnumParticleTypes.PORTAL, false, em_class2582.posX, em_class2582.posY + Reference.RANDOM.nextDouble() * 2.0, em_class2582.posZ, 32, 0.2, 0.2, 0.2, Reference.RANDOM.nextGaussian(), new int[0]);
                    }
                    em_class2582.setPosition(em_class2582.homeCoords.x, em_class2582.homeCoords.y, em_class2582.homeCoords.z);
                    em_class2582.q = null;
                    em_class2582.setCurrentAction(Action.NULL);
                    em_class2582.getDataManager().set(GirlEntity.G, false);
                    em_class2582.goHome();
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(gg_class366 iMessage, MessageContext messageContext) {
            return this.a((gg_class366)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

