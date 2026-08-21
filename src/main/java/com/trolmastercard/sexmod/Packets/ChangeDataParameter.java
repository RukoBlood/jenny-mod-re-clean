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
import com.trolmastercard.sexmod.girls.Slime.SlimeEntity;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeDataParameter
implements IMessage {
    boolean b;
    UUID d;
    String currentAction;
    String c;

    public ChangeDataParameter() {
        this.b = false;
    }

    public ChangeDataParameter(UUID uUID, String string, String string2) {
        this.d = uUID;
        this.currentAction = string;
        this.c = string2;
        this.b = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.d = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.currentAction = ByteBufUtils.readUTF8String(byteBuf);
        this.c = ByteBufUtils.readUTF8String(byteBuf);
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.d.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.currentAction);
        ByteBufUtils.writeUTF8String(byteBuf, this.c == null ? "null" : this.c);
    }

    public static class Handler implements IMessageHandler<ChangeDataParameter, IMessage> {

        @Override
        public IMessage onMessage(ChangeDataParameter msg, MessageContext ctx) {
            if (!msg.b) {
                System.out.println("received an invalid message @ChangeDataParameter :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girlEntity = GirlEntity.getServerGirlEntity(msg.d);
                if (girlEntity == null) {
                    return;
                }
                switch (msg.currentAction) {
                    case "pregnant": {
                        girlEntity.getDataManager().set(SlimeEntity.TicksUntilBirth, Integer.valueOf(msg.c));
                        break;
                    }
                    case "currentModel": {
                        girlEntity.getDataManager().set(GirlEntity.OUTFIT_INDEX, Integer.valueOf(msg.c));
                        break;
                    }
                    case "currentAction": {
                        if (Action.valueOf(msg.c) == Action.ATTACK && girlEntity.getCurrentAction() != Action.NULL) break;
                        girlEntity.setCurrentAction(Action.valueOf(msg.c));
                        break;
                    }
                    case "animationFollowUp": {
                        girlEntity.getDataManager().set(GirlEntity.GIRL_HAND_STATES, msg.c);
                        break;
                    }
                    case "playerSheHasSexWith": {
                        if (msg.c.equals("null")) {
                            girlEntity.setInteractionPlayerUUID(null);
                            break;
                        }
                        girlEntity.setInteractionPlayerUUID(UUID.fromString(msg.c));
                        break;
                    }
                    case "targetPos": {
                        String[] stringArray = msg.c.split("f");
                        Vec3d vec3d = new Vec3d(Double.parseDouble(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]));
                        girlEntity.setTargetPosition(vec3d);
                        break;
                    }
                    case "master": {
                        girlEntity.getDataManager().set(GirlEntity.MASTER, msg.c);
                        break;
                    }
                    case "walk speed": {
                        girlEntity.getDataManager().set(GirlEntity.WALK_SPEED, msg.c);
                        break;
                    }
                    case "shouldbeattargetpos": {
                        girlEntity.getDataManager().set(GirlEntity.IS_ANCHORED, Boolean.valueOf(msg.c));
                    }
                }
            });
            return null;
        }
    }
}

