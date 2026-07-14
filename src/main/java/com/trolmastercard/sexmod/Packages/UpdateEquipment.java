package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.Fighter;
import com.trolmastercard.sexmod.girls.GirlEntity;
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
    UUID c;
    NBTTagCompound b;

    public UpdateEquipment() {
    }

    public UpdateEquipment(UUID uUID, NBTTagCompound nBTTagCompound) {
        this.c = uUID;
        this.b = nBTTagCompound;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        this.b = ByteBufUtils.readTag((ByteBuf)byteBuf);
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c.toString());
        ByteBufUtils.writeTag((ByteBuf)byteBuf, (NBTTagCompound)this.b);
    }

    public static class Handler implements IMessageHandler<UpdateEquipment, IMessage> {
        public IMessage a(UpdateEquipment msg, MessageContext ctx) {
            if (!msg.valid) {
                System.out.println("received an invalid message @UpdateEquipment :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ArrayList<GirlEntity> arrayList = GirlEntity.girlList(msg.c);
                for (GirlEntity em_class2582 : arrayList) {
                    if (!(em_class2582 instanceof Fighter)) continue;
                    ((Fighter)em_class2582).items.deserializeNBT(msg.b);
                }
            });
            return null;
        }

        @Override
        public IMessage onMessage(UpdateEquipment iMessage, MessageContext messageContext) {
            return this.a((UpdateEquipment)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

