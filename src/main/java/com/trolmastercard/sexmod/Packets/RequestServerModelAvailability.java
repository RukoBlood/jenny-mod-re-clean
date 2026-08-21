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

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestServerModelAvailability
implements IMessage {
    boolean a = false;
    HashMap<String, Float> b = new HashMap();

    public RequestServerModelAvailability() {
    }

    public RequestServerModelAvailability(HashMap<String, Float> hashMap) {
        this.b = hashMap;
    }

    public void fromBytes(ByteBuf byteBuf) {
        int n;
        if (!(Main.proxy instanceof ClientProxy)) {
            this.a = true;
            return;
        }
        if (!CustomModel.isGlobalRenderingDisabled()) {
            return;
        }
        try {
            n = byteBuf.readInt();
        } catch (IndexOutOfBoundsException e) {
            this.a = true;
            return;
        }
        for (int i = 0; i < n; ++i) {
            this.b.put(ByteBufUtils.readUTF8String(byteBuf), byteBuf.readFloat());
        }
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            return;
        }
        byteBuf.writeInt(this.b.size());
        for (Map.Entry<String, Float> entry : this.b.entrySet()) {
            ByteBufUtils.writeUTF8String(byteBuf, entry.getKey());
            byteBuf.writeFloat(entry.getValue());
        }
    }

    public static class Handler implements IMessageHandler<RequestServerModelAvailability, IMessage> {
        @Override
        public IMessage onMessage(RequestServerModelAvailability msg, MessageContext ctx) {
            if (!msg.a) {
                System.out.println("received an invalid Message @RequestServerModelAvailability :(");
                return null;
            }
            if (ctx.side.isClient()) {
                if (!CustomModel.isGlobalRenderingDisabled()) {
                    return null;
                }
                ArrayList<String> arrayList = new ArrayList<>();
                for (Map.Entry<String, Float> entry : msg.b.entrySet()) {
                    String string = entry.getKey();
                    if (!CustomModel.isModelDisabled(string)) {
                        arrayList.add(string);
                        continue;
                    }
                    float f = CustomModel.getModelZOffset(string);
                    float f2 = entry.getValue();
                    if (!(f2 > f)) continue;
                    arrayList.add(string);
                }
                return new DownloadServerModel(arrayList);
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> PackageHandler.INSTANCE.sendTo(new RequestServerModelAvailability(CustomModel.getModelScales()), ctx.getServerHandler().player));
            return null;
        }
    }
}

