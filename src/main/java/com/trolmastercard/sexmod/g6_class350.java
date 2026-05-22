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
package com.trolmastercard.sexmod;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class g6_class350
implements IMessage {
    boolean a = false;
    HashMap<String, Float> b = new HashMap();

    public g6_class350() {
    }

    public g6_class350(HashMap<String, Float> hashMap) {
        this.b = hashMap;
    }

    public void fromBytes(ByteBuf byteBuf) {
        int n;
        if (!(Main.proxy instanceof ClientProxy)) {
            this.a = true;
            return;
        }
        if (!CustomModels.b()) {
            return;
        }
        try {
            n = byteBuf.readInt();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.a = true;
            return;
        }
        for (int i = 0; i < n; ++i) {
            this.b.put(ByteBufUtils.readUTF8String((ByteBuf)byteBuf), Float.valueOf(byteBuf.readFloat()));
        }
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            return;
        }
        byteBuf.writeInt(this.b.size());
        for (Map.Entry<String, Float> entry : this.b.entrySet()) {
            ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)entry.getKey());
            byteBuf.writeFloat(entry.getValue().floatValue());
        }
    }

    private static IndexOutOfBoundsException a(IndexOutOfBoundsException indexOutOfBoundsException) {
        return indexOutOfBoundsException;
    }

    public static class a_inner351
    implements IMessageHandler<g6_class350, IMessage> {
        public IMessage a(g6_class350 g6_class3502, MessageContext messageContext) {
            if (!g6_class3502.a) {
                System.out.println("received an invalid Message @RequestServerModelAvailability :(");
                return null;
            }
            if (messageContext.side.isClient()) {
                if (!CustomModels.b()) {
                    return null;
                }
                ArrayList<String> arrayList = new ArrayList<String>();
                for (Map.Entry<String, Float> entry : g6_class3502.b.entrySet()) {
                    String string = entry.getKey();
                    if (!CustomModels.f(string)) {
                        arrayList.add(string);
                        continue;
                    }
                    float f = CustomModels.i(string);
                    float f2 = entry.getValue().floatValue();
                    if (!(f2 > f)) continue;
                    arrayList.add(string);
                }
                return new cu_class146(arrayList);
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> NetworkRegistry.networkWrapper.sendTo((IMessage)new g6_class350(CustomModels.e()), messageContext.getServerHandler().player));
            return null;
        }

                @Override
        public IMessage onMessage(g6_class350 iMessage, MessageContext messageContext) {
            return this.a((g6_class350)iMessage, messageContext);
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

