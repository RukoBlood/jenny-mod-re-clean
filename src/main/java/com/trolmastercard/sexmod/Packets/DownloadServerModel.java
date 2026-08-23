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
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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
    boolean isValid;
    List<String> c = new ArrayList<String>();
    byte[] b;
    FileTypes f;
    String e;
    int a = 0;

    public DownloadServerModel() {
    }

    public DownloadServerModel(List<String> list) {
        this.c = list;
    }

    public DownloadServerModel(byte[] byArray, FileTypes b_inner1482, String string) {
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
            if (!CustomModel.isGlobalRenderingDisabled()) {
                return;
            }
            this.e = ByteBufUtils.readUTF8String(byteBuf);
            this.f = FileTypes.valueOf(ByteBufUtils.readUTF8String(byteBuf));
            this.a = byteBuf.readInt();
            int n = byteBuf.readInt();
            this.b = new byte[n];
            for (int i = 0; i < n; ++i) {
                this.b[i] = byteBuf.readByte();
            }
            this.isValid = true;
            return;
        }
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.c.add(ByteBufUtils.readUTF8String(byteBuf));
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            byteBuf.writeInt(this.c.size());
            for (String string : this.c) {
                ByteBufUtils.writeUTF8String(byteBuf, string);
            }
            return;
        }
        ByteBufUtils.writeUTF8String(byteBuf, this.e);
        ByteBufUtils.writeUTF8String(byteBuf, this.f.toString());
        byteBuf.writeInt(this.a);
        byteBuf.writeInt(this.b.length);
        for (byte by : this.b) {
            byteBuf.writeByte(by);
        }
    }

    public enum FileTypes {
        CFG(".cfg"),
        PNG(".png"),
        GEO(".geo.json");

        public String ending;

        FileTypes(String ending) {
            this.ending = ending;
        }
    }

    public static class Handler implements IMessageHandler<DownloadServerModel, IMessage> {
        static int a = 0;

        @SideOnly(value=Side.CLIENT)
        void a(String string) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
        }

        @SideOnly(value=Side.CLIENT)
        void a() {
            Minecraft.getMinecraft().addScheduledTask(() -> CustomModel.getModelCount(true));
        }

        @Override
        public IMessage onMessage(DownloadServerModel msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("received an invalid Message @DownloadServerModel :(");
                return null;
            }
            if (ctx.side.isClient()) {
                if (!CustomModel.isGlobalRenderingDisabled()) {
                    return null;
                }
                String string = msg.e;
                FileTypes b_inner1482 = msg.f;
                byte[] byArray = msg.b;
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
                int n2 = FileTypes.values().length;
                for (FileTypes b_inner1483 : FileTypes.values()) {
                    if (!new File(string2 + "/" + string + b_inner1483.ending).exists()) continue;
                    ++n;
                }
                if (n == n2) {
                    this.a(String.format("%sSuccessfully downloaded the custom model '%s%s%s'!", TextFormatting.GREEN, TextFormatting.YELLOW, string, TextFormatting.GREEN));
                } else {
                    this.a(String.format("%sdownloading custom model '%s%s%s' (%s/%s)...", TextFormatting.GRAY, TextFormatting.YELLOW, string, TextFormatting.GRAY, n, n2));
                }
                if (++a < msg.a) {
                    return null;
                }
                a = 0;
                this.a();
                return null;
            }
            MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
            minecraftServer.addScheduledTask(() -> {
                List<String> list = msg.c;
                ArrayList<DownloadServerModel> arrayList = new ArrayList<DownloadServerModel>();
                for (String object : list) {
                    String string = "sexmod_custom_models/" + object;
                    for (FileTypes b_inner1482 : FileTypes.values()) {
                        File file = new File(string + "/" + object + b_inner1482.ending);
                        if (!file.exists()) {
                            System.out.println(file.getAbsolutePath() + " doesnt exist lol");
                            continue;
                        }
                        byte[] byArray = null;
                        try {
                            byArray = FileUtils.readFileToByteArray(file);
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
                    minecraftServer.addScheduledTask(() -> PacketHandler.INSTANCE.sendTo(cu_class1463, ctx.getServerHandler().player));
                }
            });
            return null;
        }
    }
}

