package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateEquipment implements IMessage {
    boolean valid;
    UUID girlId;
    NBTTagCompound nbt;

    public UpdateEquipment() {
    }

    public UpdateEquipment(UUID uUID, NBTTagCompound nbt) {
        this.girlId = uUID;
        this.nbt = nbt;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.girlId = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.nbt = ByteBufUtils.readTag(byteBuf);
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.girlId.toString());
        ByteBufUtils.writeTag(byteBuf, this.nbt);
    }

    public static class Handler implements IMessageHandler<UpdateEquipment, IMessage> {
        @Override
        public IMessage onMessage(UpdateEquipment msg, MessageContext ctx) {
            if (!msg.valid) {
                System.out.println("received an invalid message @UpdateEquipment :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> girls = GirlEntity.girlList(msg.girlId);
                for (GirlEntity girl : girls) {
                    if (girl instanceof Fighter) {
                        ((Fighter) girl).inventory.deserializeNBT(msg.nbt);
                    }
                }
            });
            return null;
        }
    }
}

