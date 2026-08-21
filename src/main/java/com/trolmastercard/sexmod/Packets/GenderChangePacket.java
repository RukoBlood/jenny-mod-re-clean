/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraftforge.fml.common.network.ByteBufUtils
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.gui.GenderChange.GenderChangeUI;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenderChangePacket implements IMessage {
    boolean valid = false;
    EntityPlayer player;
    HashMap<PlayerGirlEntity, String> girlsList = new HashMap();

    public GenderChangePacket() {
    }

    public GenderChangePacket(EntityPlayer player) {
        this.player = player;
    }

    public void fromBytes(ByteBuf buf) {
        int index = buf.readInt();
        for (int i = 0; i < index; ++i) {
            this.girlsList.put(PlayerGirlEntity.valueOf(ByteBufUtils.readUTF8String(buf)), ByteBufUtils.readUTF8String(buf));
        }
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        for (PlayerGirlEntity girl : PlayerGirlEntity.values()) {
            String string;
            if (!girl.hasSpecifics || (string = this.player.getEntityData().getString("sexmod:GirlSpecific" + girl)).isEmpty()) continue;
            this.girlsList.put(girl, string);
        }
        byteBuf.writeInt(this.girlsList.size());
        for (Map.Entry entry : this.girlsList.entrySet()) {
            ByteBufUtils.writeUTF8String(byteBuf, entry.getKey().toString());
            ByteBufUtils.writeUTF8String(byteBuf, (String) entry.getValue());
        }
    }

    public static class Handler implements IMessageHandler<GenderChangePacket, IMessage> {
        @SideOnly(value=Side.CLIENT)
        public void CallUIDraw(HashMap<PlayerGirlEntity, String> hashMap) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GenderChangeUI(hashMap)));
        }

        @Override
        public IMessage onMessage(GenderChangePacket msg, MessageContext ctx) {
            if (!msg.valid || ctx.side != Side.CLIENT) {
                return null;
            }
            this.CallUIDraw(msg.girlsList);
            return null;
        }
    }
}

