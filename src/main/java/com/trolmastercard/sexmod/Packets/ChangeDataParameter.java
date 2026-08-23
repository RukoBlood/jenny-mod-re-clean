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
    String actionKey;
    String actionValue;

    public ChangeDataParameter() {
        this.b = false;
    }

    public ChangeDataParameter(UUID uUID, String key, String value) {
        this.d = uUID;
        this.actionKey = key;
        this.actionValue = value;
        this.b = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.d = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.actionKey = ByteBufUtils.readUTF8String(byteBuf);
        this.actionValue = ByteBufUtils.readUTF8String(byteBuf);
        this.b = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.d.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.actionKey);
        ByteBufUtils.writeUTF8String(byteBuf, this.actionValue == null ? "null" : this.actionValue);
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
                switch (msg.actionKey) {
                    case "pregnant": {
                        girlEntity.getDataManager().set(SlimeEntity.TicksUntilBirth, Integer.valueOf(msg.actionValue));
                        break;
                    }
                    case "currentModel": {
                        girlEntity.getDataManager().set(GirlEntity.OUTFIT_INDEX, Integer.valueOf(msg.actionValue));
                        break;
                    }
                    case "currentAction": {
                        if (Action.valueOf(msg.actionValue) == Action.ATTACK && girlEntity.getCurrentAction() != Action.NULL) break;
                        girlEntity.setCurrentAction(Action.valueOf(msg.actionValue));
                        break;
                    }
                    case "animationFollowUp": {
                        girlEntity.getDataManager().set(GirlEntity.GIRL_HAND_STATES, msg.actionValue);
                        break;
                    }
                    case "playerSheHasSexWith": {
                        if (msg.actionValue.equals("null")) {
                            girlEntity.setInteractionPlayerUUID(null);
                            break;
                        }
                        girlEntity.setInteractionPlayerUUID(UUID.fromString(msg.actionValue));
                        break;
                    }
                    case "targetPos": {
                        String[] stringArray = msg.actionValue.split("f");
                        Vec3d vec3d = new Vec3d(Double.parseDouble(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]));
                        girlEntity.setTargetPosition(vec3d);
                        break;
                    }
                    case "master": {
                        girlEntity.getDataManager().set(GirlEntity.MASTER, msg.actionValue);
                        break;
                    }
                    case "walk speed": {
                        girlEntity.getDataManager().set(GirlEntity.WALK_SPEED, msg.actionValue);
                        break;
                    }
                    case "shouldbeattargetpos": {
                        girlEntity.getDataManager().set(GirlEntity.IS_ANCHORED, Boolean.valueOf(msg.actionValue));
                    }
                }
            });
            return null;
        }
    }
}

