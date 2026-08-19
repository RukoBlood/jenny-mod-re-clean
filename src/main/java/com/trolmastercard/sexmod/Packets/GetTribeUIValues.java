/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  javax.vecmath.Vector4d
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package com.trolmastercard.sexmod.Packets;

import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.Kobold.EyeAndKoboldColor;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.gui.DragonStaffGUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.vecmath.Vector4d;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GetTribeUIValues implements IMessage {
    boolean a = false;
    boolean b;
    List<Vector4d> c;

    public GetTribeUIValues() {
        this.b = false;
        this.c = new ArrayList<Vector4d>();
    }

    public GetTribeUIValues(boolean bl, List<Vector4d> list) {
        this.b = bl;
        this.c = list;
    }

    static GetTribeUIValues a() {
        return new GetTribeUIValues(false, new ArrayList<Vector4d>());
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.b = byteBuf.readBoolean();
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.c.add(new Vector4d((double)byteBuf.readInt(), (double)byteBuf.readInt(), (double)byteBuf.readInt(), (double)byteBuf.readInt()));
        }
        this.a = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.b);
        byteBuf.writeInt(this.c.size());
        for (Vector4d vector4d : this.c) {
            byteBuf.writeInt((int)vector4d.getX());
            byteBuf.writeInt((int)vector4d.getY());
            byteBuf.writeInt((int)vector4d.getZ());
            byteBuf.writeInt((int)vector4d.getW());
        }
    }

    public static class Handler
    implements IMessageHandler<GetTribeUIValues, IMessage> {
        public IMessage a(GetTribeUIValues msg, MessageContext ctx) {
            if (!msg.a) {
                System.out.println("received an invalid message @GetTribeUIValues :(");
                return null;
            }
            if (ctx.side.isClient()) {
                DragonStaffGUI.isTribeFollowing = msg.b;
                KoboldEntity.aY = msg.c;
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                Object object;
                UUID uUID = KoboldManager.findTribeIdWith(ctx.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    PackageHandler.INSTANCE.sendTo((IMessage) GetTribeUIValues.a(), ctx.getServerHandler().player);
                    return;
                }
                boolean bl = KoboldManager.isTribeAlerted(uUID);
                EntityPlayerMP entityPlayerMP = ctx.getServerHandler().player;
                HashMap<UUID, BlockPos> hashMap = KoboldManager.getUnloadedMembersMap(uUID, entityPlayerMP.world);
                List<KoboldEntity> list = KoboldManager.getTribeMembersList(uUID);
                ArrayList<Vector4d> arrayList = new ArrayList<Vector4d>();
                int koboldColor = KoboldManager.getTribeColor(uUID).getWoolMeta();
                HashSet<Object> hashSet = new HashSet<Object>();
                for (KoboldEntity koboldEntity : list) {
                    if (koboldEntity.isDead || hashSet.contains(object = koboldEntity.girlID())) continue;
                    if (koboldEntity.aA) {
                        koboldColor = EyeAndKoboldColor.safeValueOf(koboldEntity.getDataManager().get(AbstractNpcOnlyEntity.CURRENT_ACTION)).getWoolMeta();
                    }
                    arrayList.add(new Vector4d(koboldEntity.posX, koboldEntity.posY, koboldEntity.posZ, (double)koboldColor));
                    hashSet.add(object);
                }
                for (Map.Entry entry : hashMap.entrySet()) {
                    if (hashSet.contains(entry.getKey())) continue;
                    object = (BlockPos)entry.getValue();
                    arrayList.add(new Vector4d((double)((Vec3i)object).getX(), (double)((Vec3i)object).getY(), (double)((Vec3i)object).getZ(), (double)koboldColor));
                }
                PackageHandler.INSTANCE.sendTo((IMessage)new GetTribeUIValues(bl, arrayList), entityPlayerMP);
            });
            return null;
        }

                @Override
        public IMessage onMessage(GetTribeUIValues iMessage, MessageContext messageContext) {
            return this.a((GetTribeUIValues)iMessage, messageContext);
        }
    }
}

