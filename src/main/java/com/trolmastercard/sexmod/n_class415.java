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
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class n_class415
implements IMessage {
    boolean b;
    UUID d;
    String currectAction;
    String c;

    public n_class415() {
        this.b = false;
    }

    public n_class415(UUID uUID, String string, String string2) {
        this.d = uUID;
        this.currectAction = string;
        this.c = string2;
        this.b = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.d = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.currectAction = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.c = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.d.toString());
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.currectAction);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)(this.c == null ? "null" : this.c));
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner416
    implements IMessageHandler<n_class415, IMessage> {
        public IMessage a(n_class415 n_class4152, MessageContext messageContext) {
            if (!n_class4152.b) {
                System.out.println("received an invalid message @ChangeDataParameter :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girlEntity = GirlEntity.com_trolmastercard_sexmod_em_class258_a(n_class4152.d);
                if (girlEntity == null) {
                    return;
                }
                switch (n_class4152.currectAction) {
                    case "pregnant": {
                        girlEntity.getDataManager().set(SlimeEntity.TicksUntilBirth, Integer.valueOf(n_class4152.c));
                        break;
                    }
                    case "currentModel": {
                        girlEntity.getDataManager().set(GirlEntity.D, Integer.valueOf(n_class4152.c));
                        break;
                    }
                    case "currentAction": {
                        if (Action.valueOf(n_class4152.c) == Action.ATTACK && girlEntity.currentAction() != Action.NULL) break;
                        girlEntity.setCurrentAction(Action.valueOf(n_class4152.c));
                        break;
                    }
                    case "animationFollowUp": {
                        girlEntity.getDataManager().set(GirlEntity.h, n_class4152.c);
                        break;
                    }
                    case "playerSheHasSexWith": {
                        if (n_class4152.c.equals("null")) {
                            girlEntity.void_e((UUID)null);
                            break;
                        }
                        girlEntity.void_e(UUID.fromString(n_class4152.c));
                        break;
                    }
                    case "targetPos": {
                        String[] stringArray = n_class4152.c.split("f");
                        Vec3d vec3d = new Vec3d(Double.parseDouble(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]));
                        girlEntity.c(vec3d);
                        break;
                    }
                    case "master": {
                        girlEntity.getDataManager().set(GirlEntity.v, n_class4152.c);
                        break;
                    }
                    case "walk speed": {
                        girlEntity.getDataManager().set(GirlEntity.a, n_class4152.c);
                        break;
                    }
                    case "shouldbeattargetpos": {
                        girlEntity.getDataManager().set(GirlEntity.G, Boolean.valueOf(n_class4152.c));
                    }
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(n_class415 iMessage, MessageContext messageContext) {
            return this.a((n_class415)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

