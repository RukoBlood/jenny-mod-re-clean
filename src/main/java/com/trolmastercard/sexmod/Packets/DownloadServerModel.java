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
 *  org.apache.commons.io.FileUtils
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import io.netty.buffer.ByteBuf;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

public class DownloadServerModel
implements IMessage {
    boolean d;
    List<String> c = new ArrayList<String>();
    byte[] b;
    b_inner148 f;
    String e;
    int a = 0;

    public DownloadServerModel() {
    }

    public DownloadServerModel(List<String> list) {
        this.c = list;
    }

    public DownloadServerModel(byte[] byArray, b_inner148 b_inner1482, String string) {
        this.b = byArray;
        this.f = b_inner1482;
        this.e = string;
    }

    public int a() {
        return this.a;
    }

    public void a(int n) {
        this.a = n;
    }

    public void fromBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            if (!CustomModel.b()) {
                return;
            }
            this.e = ByteBufUtils.readUTF8String((ByteBuf)byteBuf);
            this.f = b_inner148.valueOf(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
            this.a = byteBuf.readInt();
            int n = byteBuf.readInt();
            this.b = new byte[n];
            for (int i = 0; i < n; ++i) {
                this.b[i] = byteBuf.readByte();
            }
            this.d = true;
            return;
        }
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.c.add(ByteBufUtils.readUTF8String((ByteBuf)byteBuf));
        }
        this.d = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            byteBuf.writeInt(this.c.size());
            for (String string : this.c) {
                ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)string);
            }
            return;
        }
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.e);
        ByteBufUtils.writeUTF8String((ByteBuf)byteBuf, (String)this.f.toString());
        byteBuf.writeInt(this.a);
        byteBuf.writeInt(this.b.length);
        for (byte by : this.b) {
            byteBuf.writeByte((int)by);
        }
    }

    public static enum b_inner148 {
        CFG(".cfg"),
        PNG(".png"),
        GEO(".geo.json");

        public String ending;

        private b_inner148(String string2) {
            this.ending = string2;
        }
    }

    public static class a_inner147
    implements IMessageHandler<DownloadServerModel, IMessage> {
        static int a = 0;

        @SideOnly(value=Side.CLIENT)
        void a(String string) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
        }

        @SideOnly(value=Side.CLIENT)
        void a() {
            Minecraft.getMinecraft().addScheduledTask(() -> CustomModel.b(true));
        }

        public IMessage a(DownloadServerModel cu_class1462, MessageContext messageContext) {
            if (!cu_class1462.d) {
                System.out.println("received an invalid Message @DownloadServerModel :(");
                return null;
            }
            if (messageContext.side.isClient()) {
                if (!CustomModel.b()) {
                    return null;
                }
                String string = cu_class1462.e;
                b_inner148 b_inner1482 = cu_class1462.f;
                byte[] byArray = cu_class1462.b;
                String string2 = CustomModel.getCurrentGroup() + "/" + string;
                File file = new File(string2);
                file.mkdirs();
                File file2 = new File(string2 + "/" + string + b_inner1482.ending);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    Throwable throwable = null;
                    try {
                        fileOutputStream.write(byArray);
                    } catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    } finally {
                        if (fileOutputStream != null) {
                            if (throwable != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable throwable3) {
                                    throwable.addSuppressed(throwable3);
                                }
                            } else {
                                fileOutputStream.close();
                            }
                        }
                    }
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                int n = 0;
                int n2 = b_inner148.values().length;
                for (b_inner148 b_inner1483 : b_inner148.values()) {
                    if (!new File(string2 + "/" + string + b_inner1483.ending).exists()) continue;
                    ++n;
                }
                if (n == n2) {
                    this.a(String.format("%sSuccessfully downloaded the custom model '%s%s%s'!", new Object[]{TextFormatting.GREEN, TextFormatting.YELLOW, string, TextFormatting.GREEN}));
                } else {
                    this.a(String.format("%sdownloading custom model '%s%s%s' (%s/%s)...", new Object[]{TextFormatting.GRAY, TextFormatting.YELLOW, string, TextFormatting.GRAY, n, n2}));
                }
                if (++a < cu_class1462.a) {
                    return null;
                }
                a = 0;
                this.a();
                return null;
            }
            MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
            minecraftServer.addScheduledTask(() -> {
                List<String> list = cu_class1462.c;
                ArrayList<DownloadServerModel> arrayList = new ArrayList<DownloadServerModel>();
                for (String object : list) {
                    String string = "sexmod_custom_models/" + object;
                    for (b_inner148 b_inner1482 : b_inner148.values()) {
                        File file = new File(string + "/" + object + b_inner1482.ending);
                        if (!file.exists()) {
                            System.out.println(file.getAbsolutePath() + " doesnt exist lol");
                            continue;
                        }
                        byte[] byArray = null;
                        try {
                            byArray = FileUtils.readFileToByteArray((File)file);
                        } catch (IOException iOException) {
                            throw new RuntimeException(iOException);
                        }
                        if (byArray == null) continue;
                        arrayList.add(new DownloadServerModel(byArray, b_inner1482, object));
                    }
                }
                int n = arrayList.size();
                for (DownloadServerModel cu_class1463 : arrayList) {
                    cu_class1463.a(n);
                    minecraftServer.addScheduledTask(() -> PackageHandler.INSTANCE.sendTo((IMessage)cu_class1463, messageContext.getServerHandler().player));
                }
            });
            return null;
        }

                @Override
        public IMessage onMessage(DownloadServerModel iMessage, MessageContext messageContext) {
            return this.a((DownloadServerModel)iMessage, messageContext);
        }
    }
}

