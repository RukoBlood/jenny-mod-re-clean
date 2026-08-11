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
package com.trolmastercard.sexmod.Packages;

import com.trolmastercard.sexmod.gui.b5_class66;
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

public class bd_class76 implements IMessage {
    boolean valid = false;
    EntityPlayer b;
    HashMap<PlayerGirlEntity, String> a = new HashMap();

    public bd_class76() {
    }

    public bd_class76(EntityPlayer entityPlayer) {
        this.b = entityPlayer;
    }

    public void fromBytes(ByteBuf byteBuf) {
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.a.put(PlayerGirlEntity.valueOf(ByteBufUtils.readUTF8String((ByteBuf)byteBuf)), ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        }
        this.valid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        for (PlayerGirlEntity fy_class3352 : PlayerGirlEntity.values()) {
            String string;
            if (!fy_class3352.hasSpecifics || (string = this.b.getEntityData().getString("sexmod:GirlSpecific" + (Object) ((Object) fy_class3352))).isEmpty()) continue;
            this.a.put(fy_class3352, string);
        }
        byteBuf.writeInt(this.a.size());
        for (Map.Entry entry : this.a.entrySet()) {
            ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)((PlayerGirlEntity)((Object)entry.getKey())).toString());
            ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)((String)entry.getValue()));
        }
    }

    public static class Handler implements IMessageHandler<bd_class76, IMessage> {
        public IMessage execMessage(bd_class76 message, MessageContext ctx) {
            if (!message.valid || ctx.side != Side.CLIENT) {
                return null;
            }
            this.CallUIDraw(message.a);
            return null;
        }

        @SideOnly(value=Side.CLIENT)
        public void CallUIDraw(HashMap<PlayerGirlEntity, String> hashMap) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> mc.displayGuiScreen(new b5_class66(hashMap)));
        }

        @Override
        public IMessage onMessage(bd_class76 iMessage, MessageContext messageContext) {
            return this.execMessage((bd_class76)iMessage, messageContext);
        }
    }
}

