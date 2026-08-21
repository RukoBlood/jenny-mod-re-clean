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

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UploadModelString implements IMessage {
    boolean isValid = false;
    String modelCode;
    List<Integer> partIds = new ArrayList<Integer>();
    UUID girlUUid;

    public UploadModelString() {
    }

    public UploadModelString(String string, UUID uUID) {
        this.modelCode = string;
        this.girlUUid = uUID;
    }

    public UploadModelString(String modelcode, UUID uUID, List<Integer> list) {
        this.modelCode = modelcode;
        this.girlUUid = uUID;
        this.partIds = list;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.modelCode = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.girlUUid = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.partIds.add(byteBuf.readInt());
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.modelCode);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.girlUUid.toString());
        byteBuf.writeInt(this.partIds.size());
        for (int n : this.partIds) {
            byteBuf.writeInt(n);
        }
    }

    public static class Handler implements IMessageHandler<UploadModelString, IMessage> {

        @Override
        public IMessage onMessage(UploadModelString msg, MessageContext ctx) {
            if (!msg.isValid || ctx.side != Side.SERVER) {
                System.out.println("received an invalid message @UploadModelString :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity em_class2582 = GirlEntity.getServerGirlEntity(msg.girlUUid);
                boolean bl = msg.partIds.size() > 0;
                boolean bl2 = false;
                if (bl && (bl2 = this.isValidModelCode(em_class2582, msg.partIds))) {
                    em_class2582.setCustomPartList(msg.partIds);
                }
                if (!(em_class2582 instanceof PlayerGirl)) {
                    em_class2582.setCustomModelCode(msg.modelCode);
                    return;
                }
                EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
                NBTTagCompound nBTTagCompound = entityPlayerMP.getEntityData();
                PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayerMP);
                if (ei_class2512 == null) {
                    return;
                }
                PlayerGirlEntity fy_class3352 = PlayerGirlEntity.getGirlType(ei_class2512);
                nBTTagCompound.setString("sexmod:CustomModel" + fy_class3352.toString(), msg.modelCode);
                if (bl && bl2) {
                    nBTTagCompound.setString("sexmod:GirlSpecific" + fy_class3352.toString(), GirlEntity.encodePartIdList(msg.partIds));
                }
            });
            return null;
        }

        boolean isValidModelCode(GirlEntity girl, List<Integer> list) {
            ArrayList<Integer> arrayList = girl.getCustomPartIdList();
            try {
                for (int i = 0; i < arrayList.size(); ++i) {
                    if (arrayList.get(i) > list.get(i)) continue;
                    return false;
                }
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                return false;
            }
            return true;
        }
    }
}

