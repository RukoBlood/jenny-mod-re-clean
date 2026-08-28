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
    List<String> modelNames = new ArrayList<>();
    byte[] modelData;
    FileTypes packetTypes;
    String modelName;
    int modelIndex = 0;

    public DownloadServerModel() {
    }

    public DownloadServerModel(List<String> list) {
        this.modelNames = list;
    }

    public DownloadServerModel(byte[] modelData, FileTypes types, String name) {
        this.modelData = modelData;
        this.packetTypes = types;
        this.modelName = name;
    }

    public int getModelIndex() {
        return this.modelIndex;
    }

    public void setModelIndex(int index) {
        this.modelIndex = index;
    }

    public void fromBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            if (CustomModel.isGlobalRenderingDisabled()) {
                this.modelName = ByteBufUtils.readUTF8String(byteBuf);
                this.packetTypes = FileTypes.valueOf(ByteBufUtils.readUTF8String(byteBuf));
                this.modelIndex = byteBuf.readInt();
                int n = byteBuf.readInt();
                this.modelData = new byte[n];
                for (int i = 0; i < n; ++i) {
                    this.modelData[i] = byteBuf.readByte();
                }
                this.isValid = true;
                return;
            }
        }
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.modelNames.add(ByteBufUtils.readUTF8String(byteBuf));
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        if (Main.proxy instanceof ClientProxy) {
            byteBuf.writeInt(this.modelNames.size());
            for (String string : this.modelNames) {
                ByteBufUtils.writeUTF8String(byteBuf, string);
            }
            return;
        }
        ByteBufUtils.writeUTF8String(byteBuf, this.modelName);
        ByteBufUtils.writeUTF8String(byteBuf, this.packetTypes.toString());
        byteBuf.writeInt(this.modelIndex);
        byteBuf.writeInt(this.modelData.length);
        for (byte by : this.modelData) {
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
        static int packetCounter = 0;

        @SideOnly(value=Side.CLIENT)
        void sendModelMessage(String string) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
        }

        @SideOnly(value=Side.CLIENT)
        void reloadServerModels() {
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
                String string = msg.modelName;
                FileTypes b_inner1482 = msg.packetTypes;
                byte[] data = msg.modelData;
                String string2 = CustomModel.getCurrentGroup() + "/" + string;
                File file = new File(string2);
                file.mkdirs();
                File file2 = new File(string2 + "/" + string + b_inner1482.ending);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    Throwable throwable = null;
                    try {
                        fileOutputStream.write(data);
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
                    this.sendModelMessage(String.format("%sSuccessfully downloaded the custom model '%s%s%s'!", TextFormatting.GREEN, TextFormatting.YELLOW, string, TextFormatting.GREEN));
                } else {
                    this.sendModelMessage(String.format("%sdownloading custom model '%s%s%s' (%s/%s)...", TextFormatting.GRAY, TextFormatting.YELLOW, string, TextFormatting.GRAY, n, n2));
                }
                if (++packetCounter < msg.modelIndex) {
                    return null;
                }
                packetCounter = 0;
                this.reloadServerModels();
                return null;
            }
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            server.addScheduledTask(() -> {
                List<String> names = msg.modelNames;
                ArrayList<DownloadServerModel> arrayList = new ArrayList<DownloadServerModel>();
                for (String name : names) {
                    String serverDir = "sexmod_custom_models/" + name;
                    for (FileTypes types : FileTypes.values()) {
                        File modelFile = new File(serverDir + "/" + name + types.ending);
                        if (!modelFile.exists()) {
                            System.out.println(modelFile.getAbsolutePath() + " doesnt exist lol");
                            continue;
                        }
                        byte[] fileBytes = null;
                        try {
                            fileBytes = FileUtils.readFileToByteArray(modelFile);
                        } catch (IOException iOException) {
                            throw new RuntimeException(iOException);
                        }
                        if (fileBytes == null) continue;
                        arrayList.add(new DownloadServerModel(fileBytes, types, name));
                    }
                }
                int n = arrayList.size();
                for (DownloadServerModel cu_class1463 : arrayList) {
                    cu_class1463.setModelIndex(n);
                    server.addScheduledTask(() -> PacketHandler.INSTANCE.sendTo(cu_class1463, ctx.getServerHandler().player));
                }
            });
            return null;
        }
    }
}

