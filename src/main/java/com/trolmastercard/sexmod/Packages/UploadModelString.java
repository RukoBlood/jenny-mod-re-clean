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

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
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

public class UploadModelString
implements IMessage {
    boolean a = false;
    String c;
    List<Integer> d = new ArrayList<Integer>();
    UUID b;

    public UploadModelString() {
    }

    public UploadModelString(String string, UUID uUID) {
        this.c = string;
        this.b = uUID;
    }

    public UploadModelString(String string, UUID uUID, List<Integer> list) {
        this.c = string;
        this.b = uUID;
        this.d = list;
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.c = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
        this.b = UUID.fromString(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.d.add(byteBuf.readInt());
        }
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.c);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.b.toString());
        byteBuf.writeInt(this.d.size());
        for (int n : this.d) {
            byteBuf.writeInt(n);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner333
    implements IMessageHandler<UploadModelString, IMessage> {
        public IMessage a(UploadModelString fw_class3322, MessageContext messageContext) {
            if (!fw_class3322.a || messageContext.side != Side.SERVER) {
                System.out.println("received an invalid message @UploadModelString :(");
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(fw_class3322.b);
                boolean bl = fw_class3322.d.size() > 0;
                boolean bl2 = false;
                if (bl && (bl2 = this.a(em_class2582, fw_class3322.d))) {
                    em_class2582.void_a(fw_class3322.d);
                }
                if (!(em_class2582 instanceof PlayerGirl)) {
                    em_class2582.f(fw_class3322.c);
                    return;
                }
                EntityPlayerMP entityPlayerMP = messageContext.getServerHandler().player;
                NBTTagCompound nBTTagCompound = entityPlayerMP.getEntityData();
                PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayerMP);
                if (ei_class2512 == null) {
                    return;
                }
                PlayerGirlEntity fy_class3352 = PlayerGirlEntity.a(ei_class2512);
                nBTTagCompound.setString("sexmod:CustomModel" + fy_class3352.toString(), fw_class3322.c);
                if (bl && bl2) {
                    nBTTagCompound.setString("sexmod:GirlSpecific" + fy_class3352.toString(), GirlEntity.c(fw_class3322.d));
                }
            });
            return null;
        }

        boolean a(GirlEntity em_class2582, List<Integer> list) {
            ArrayList<Integer> arrayList = em_class2582.D();
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

                @Override
        public IMessage onMessage(UploadModelString iMessage, MessageContext messageContext) {
            return this.a((UploadModelString)iMessage, messageContext);
        }

        private static IndexOutOfBoundsException a(IndexOutOfBoundsException indexOutOfBoundsException) {
            return indexOutOfBoundsException;
        }
    }
}

