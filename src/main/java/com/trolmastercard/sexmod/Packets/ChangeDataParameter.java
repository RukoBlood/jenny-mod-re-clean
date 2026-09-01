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
    boolean isValid;
    UUID girlServerTarget;
    String actionKey;
    String actionValue;

    public ChangeDataParameter() {
        this.isValid = false;
    }

    public ChangeDataParameter(UUID uUID, String key, String value) {
        this.girlServerTarget = uUID;
        this.actionKey = key;
        this.actionValue = value;
        this.isValid = true;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlServerTarget = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.actionKey = ByteBufUtils.readUTF8String(byteBuf);
        this.actionValue = ByteBufUtils.readUTF8String(byteBuf);
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlServerTarget.toString());
        ByteBufUtils.writeUTF8String(byteBuf, this.actionKey);
        ByteBufUtils.writeUTF8String(byteBuf, this.actionValue == null ? "null" : this.actionValue);
    }

    public static class Handler implements IMessageHandler<ChangeDataParameter, IMessage> {

        @Override
        public IMessage onMessage(ChangeDataParameter msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("received an invalid message @ChangeDataParameter :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity girl = GirlEntity.getServerGirlEntity(msg.girlServerTarget);
                if (girl != null) {
                    switch (msg.actionKey) {
                        case "pregnant": {
                            girl.getDataManager().set(SlimeEntity.TicksUntilBirth, Integer.valueOf(msg.actionValue));
                            break;
                        }
                        case "currentModel": {
                            girl.getDataManager().set(GirlEntity.OUTFIT_INDEX, Integer.valueOf(msg.actionValue));
                            break;
                        }
                        case "currentAction": {
                            if (Action.valueOf(msg.actionValue) == Action.ATTACK && girl.getCurrentAction() != Action.NULL)
                                break;
                            girl.setCurrentAction(Action.valueOf(msg.actionValue));
                            break;
                        }
                        case "animationFollowUp": {
                            girl.getDataManager().set(GirlEntity.GIRL_HAND_STATES, msg.actionValue);
                            break;
                        }
                        case "playerSheHasSexWith": {
                            if (msg.actionValue.equals("null")) {
                                girl.setInteractionPlayerUUID(null);
                                break;
                            }
                            girl.setInteractionPlayerUUID(UUID.fromString(msg.actionValue));
                            break;
                        }
                        case "targetPos": {
                            String[] posStrArray = msg.actionValue.split("f");
                            Vec3d targetPos = new Vec3d(Double.parseDouble(posStrArray[0]), Double.parseDouble(posStrArray[1]), Double.parseDouble(posStrArray[2]));
                            girl.setTargetPosition(targetPos);
                            break;
                        }
                        case "master": {
                            girl.getDataManager().set(GirlEntity.MASTER, msg.actionValue);
                            break;
                        }
                        case "walk speed": {
                            girl.getDataManager().set(GirlEntity.WALK_SPEED, msg.actionValue);
                            break;
                        }
                        case "shouldbeattargetpos": {
                            girl.getDataManager().set(GirlEntity.IS_ANCHORED, Boolean.valueOf(msg.actionValue));
                        }
                    }
                }
            });
            return null;
        }
    }
}

