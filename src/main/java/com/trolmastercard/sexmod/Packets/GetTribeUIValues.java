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
import com.trolmastercard.sexmod.gui.DragonStaffUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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
    boolean isValid = false;
    boolean isTribeLeader;
    List<Vector4d> tribeMembers;

    public GetTribeUIValues() {
        this.isTribeLeader = false;
        this.tribeMembers = new ArrayList<>();
    }

    public GetTribeUIValues(boolean bl, List<Vector4d> members) {
        this.isTribeLeader = bl;
        this.tribeMembers = members;
    }

    static GetTribeUIValues createEmptyPacket() {
        return new GetTribeUIValues(false, new ArrayList<>());
    }

    public void fromBytes(ByteBuf byteBuf) {
        this.isTribeLeader = byteBuf.readBoolean();
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.tribeMembers.add(new Vector4d(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()));
        }
        this.isValid = true;
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.isTribeLeader);
        byteBuf.writeInt(this.tribeMembers.size());
        for (Vector4d member : this.tribeMembers) {
            byteBuf.writeInt((int)member.getX());
            byteBuf.writeInt((int)member.getY());
            byteBuf.writeInt((int)member.getZ());
            byteBuf.writeInt((int)member.getW());
        }
    }

    public static class Handler implements IMessageHandler<GetTribeUIValues, IMessage> {
        @Override
        public IMessage onMessage(GetTribeUIValues msg, MessageContext ctx) {
            if (!msg.isValid) {
                System.out.println("received an invalid message @GetTribeUIValues :(");
                return null;
            }
            if (ctx.side.isClient()) {
                DragonStaffUI.isTribeFollowing = msg.isTribeLeader;
                KoboldEntity.ACTIVE_TRIBE_SCREEN_POSITIONS = msg.tribeMembers;
                return null;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                UUID uUID = KoboldManager.findTribeIdWith(ctx.getServerHandler().player.getPersistentID());
                if (uUID == null) {
                    PacketHandler.INSTANCE.sendTo(GetTribeUIValues.createEmptyPacket(), ctx.getServerHandler().player);
                    return;
                }
                boolean tribeAlerted = KoboldManager.isTribeAlerted(uUID);
                EntityPlayerMP player = ctx.getServerHandler().player;
                HashMap<UUID, BlockPos> unloadedMembersMap = KoboldManager.getUnloadedMembersMap(uUID, player.world);
                List<KoboldEntity> kobolds = KoboldManager.getTribeMembersList(uUID);
                ArrayList<Vector4d> arrayList = new ArrayList<>();
                int koboldColor = KoboldManager.getTribeColor(uUID).getWoolMeta();

//                HashSet<Object> hashSet = new HashSet<>();
//                Object object; //TODO
//                for (KoboldEntity koboldEntity : list) {
//                    if (!koboldEntity.isDead && !hashSet.contains(object = koboldEntity.girlID())) {
//                        if (koboldEntity.editedColorManually) {
//                            koboldColor = EyeAndKoboldColor.safeValueOf(koboldEntity.getDataManager().get(AbstractNpcOnlyEntity.CURRENT_ACTION)).getWoolMeta();
//                        }
//                        arrayList.add(new Vector4d(koboldEntity.posX, koboldEntity.posY, koboldEntity.posZ, koboldColor));
//                        hashSet.add(object);
//                    }
//                }
//                for (Map.Entry entry : hashMap.entrySet()) {
//                    if (hashSet.contains(entry.getKey())) continue;
//                    object = entry.getValue();
//                    arrayList.add(new Vector4d(((Vec3i)object).getX(), ((Vec3i)object).getY(), ((Vec3i)object).getZ(), koboldColor));
//                }

                HashSet<UUID> hashSet = new HashSet<>();

                for (KoboldEntity kobold : kobolds) {
                    if (!kobold.isDead && !hashSet.contains(kobold.girlID())) {
                        if (kobold.editedColorManually) {
                            koboldColor = EyeAndKoboldColor.safeValueOf(kobold.getDataManager().get(AbstractNpcOnlyEntity.CURRENT_ACTION)).getWoolMeta();
                        }
                        arrayList.add(new Vector4d(kobold.posX, kobold.posY, kobold.posZ, koboldColor));
                        hashSet.add(kobold.girlID());
                    }
                }

                for (Map.Entry<UUID, BlockPos> posEntry : unloadedMembersMap.entrySet()) {
                    if (!hashSet.contains(posEntry.getKey())) {
                        BlockPos pos = posEntry.getValue();
                        arrayList.add(new Vector4d(pos.getX(), pos.getY(), pos.getZ(), koboldColor));
                    }
                }
                PacketHandler.INSTANCE.sendTo(new GetTribeUIValues(tribeAlerted, arrayList), player);
            });
            return null;
        }
    }
}

