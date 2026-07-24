/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  javax.annotation.Nullable
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.event.entity.player.PlayerSleepInBedEvent
 *  net.minecraftforge.event.world.BlockEvent$BreakEvent
 *  net.minecraftforge.event.world.BlockEvent$PlaceEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Kobold;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendBlocks;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KoboldManager {
    final static int MAX_TRIBE_MEMBERS = 4;
    final static private HashMap<UUID, KoboldSavedData.KoboldTribe> tribesMap = new HashMap<>();

    final static Vec3d[] SPAWN_OFFSETS = new Vec3d[]{
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(0.5, 0.0, 0.0),
            new Vec3d(-0.5, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 0.5),
            new Vec3d(0.0, 0.0, -0.5)
    };

    static HashMap<KoboldEntity, BlockPos[]> bedAssignments = new HashMap<>();

    public static void clear() {
        tribesMap.clear();
        bedAssignments.clear();
    }

    public static void spawnTribe(World world, Vec3d spawnPos) {
        UUID tribeUUID = UUID.randomUUID();

        float[] scales = new float[4];
        scales[0] = 0.25f;
        for (int i = 1; i < scales.length; ++i) {
            scales[i] = KoboldEntity.float_j();
        }

        ArrayList<KoboldEntity> members = new ArrayList<KoboldEntity>();
        for (float scale : scales) {
            KoboldEntity kobold = KoboldEntity.a(world, tribeUUID, scale);
            members.add(kobold);
        }

        EyeAndKoboldColor color = EyeAndKoboldColor.values()[Reference.RANDOM.nextInt(EyeAndKoboldColor.values().length)];
        KoboldSavedData.KoboldTribe tribe = new KoboldSavedData.KoboldTribe(tribeUUID, color, (KoboldEntity) members.get(0), members);
        tribesMap.put(tribeUUID, tribe);

        int index = 0;
        for (KoboldEntity kobold : members) {
            kobold.setPosition(spawnPos.x + KoboldManager.SPAWN_OFFSETS[index].x, spawnPos.y, spawnPos.z + KoboldManager.SPAWN_OFFSETS[index].z);
            world.spawnEntity(kobold);
            ++index;
        }
    }

    public static boolean doesTribeExist(UUID uUID) {
        return tribesMap.get(uUID) != null;
    }

    public static void setTribeMaster(UUID tribeUUID, UUID masterPlayerUUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(tribeUUID);
        if (tribe == null) {
            return;
        }
        tribe.setMasterUUID(masterPlayerUUID);
    }

    public static void createTribe(UUID tribeUUID, EyeAndKoboldColor color) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(tribeUUID);
        if (tribe != null) {
            System.out.println("tribe of UUID " + tribeUUID.toString() + " does already exist lol");
            return;
        }
        tribesMap.put(tribeUUID, new KoboldSavedData.KoboldTribe(tribeUUID, color));
    }

    public static boolean isBedAssigned(BlockPos pos) {
        for (Map.Entry<KoboldEntity, BlockPos[]> entry : bedAssignments.entrySet()) {
            BlockPos[] pair = entry.getValue();
            if (pair[0].equals(pos)) {
                return true;
            }
            if (!pair[1].equals(pos)) continue;
            return true;
        }
        return false;
    }

    public static BlockPos[] getBedForKobold(KoboldEntity kobold) {
        return bedAssignments.get(kobold);
    }

    public static void assignBedToKobold(KoboldEntity kobold, BlockPos blockPos) {
        World world = kobold.world;
        BlockPos pairPos = null;
        if (world.getBlockState(blockPos.north()).getBlock() instanceof BlockBed) {
            pairPos = blockPos.north();
        }
        if (world.getBlockState(blockPos.east()).getBlock() instanceof BlockBed) {
            pairPos = blockPos.east();
        }
        if (world.getBlockState(blockPos.south()).getBlock() instanceof BlockBed) {
            pairPos = blockPos.south();
        }
        if (world.getBlockState(blockPos.west()).getBlock() instanceof BlockBed) {
            pairPos = blockPos.west();
        }
        if (pairPos == null) {
            System.out.println("bed @" + blockPos.toString() + " apparently doesn't have another half.. wtf");
            return;
        }
        bedAssignments.put(kobold, new BlockPos[]{blockPos, pairPos});
    }

    public static void removeBedForKobold(KoboldEntity kobold) {
        bedAssignments.remove(kobold);
    }

    public static void setTribeLeader(UUID tribeUUID, KoboldEntity kobold) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(tribeUUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + tribeUUID.toString() + " not found uwu");
            return;
        }
        tribe.leader = kobold;
    }

    public static void addMemberToTribe(UUID tribeUUID, KoboldEntity kobold) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(tribeUUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + tribeUUID.toString() + " not found uwu");
            return;
        }
        tribe.addMember(kobold);
        tribesMap.replace(tribeUUID, tribe);
        kobold.getDataManager().set(KoboldEntity.aL, Optional.of(tribeUUID));
        if (!kobold.aA) {
            kobold.getDataManager().set(KoboldEntity.CURRENT_ACTION, tribe.color.toString());
        }
    }

    public static void updateLeaderIfDead(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        KoboldEntity leader = tribe.leader;
        if (leader == null || leader.isDead) {
            tribe.leader = tribe.findNewLeader();
        }
    }

    public static void removeMemberFromTribe(UUID uUID, KoboldEntity kobold) {
        //Object object;
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.removeMember(kobold);
        tribe.removeMemberUUID(kobold.girlID());
        {
            KoboldEntity newLeader = null;
            if ((newLeader = tribe.findNewLeader()) != null) {
                if (tribe.leader != null && tribe.leader.getEntityId() == kobold.getEntityId()) {
                    tribe.leader = newLeader;
                }
            }
        }
        for (KoboldTaskInfo task : tribe.tasks) {
            task.c(kobold);
        }
        if (!tribe.members.isEmpty()) {
            tribesMap.replace(uUID, tribe);
            return;
        }
        if (!kobold.isMasterAssigned()) {
            return;
        }
        EntityPlayer master = kobold.getMasterPlayer();
        if (master != null) {
            HashSet<BlockPos> tribeBlocks = new HashSet<BlockPos>();
            tribeBlocks.addAll(tribe.chests);
            tribeBlocks.addAll(tribe.beds);
            for (KoboldTaskInfo task : tribe.tasks) {
                tribeBlocks.addAll(task.b);
            }
            PackageHandler.networkWrapper.sendTo((IMessage) new SendBlocks(tribeBlocks, false), (EntityPlayerMP) master);
            ((Entity) master).sendMessage(new TextComponentString(String.format("ur %stribe %shas been %seradicated %suwu", new Object[]{TextFormatting.RED, TextFormatting.WHITE, TextFormatting.RED, TextFormatting.WHITE})));
        }
    }

    @Nullable
    public static KoboldEntity getTribeLeader(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return tribe.leader;
    }

    public static boolean isTribeLeader(UUID uUID, KoboldEntity kobold) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        if (tribe.leader == null) {
            return false;
        }
        return tribe.leader.getEntityId() == kobold.getEntityId();
    }

    public static EyeAndKoboldColor getTribeColor(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return KoboldEntity.COLOR;
        }
        return tribe.color;
    }

    public static HashSet<BlockPos> getTribeBeds(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<BlockPos>();
        }
        return tribe.beds;
    }

    public static void registerBed(UUID uUID, BlockPos pos) {
        if (pos == null) {
            return;
        }
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.beds.add(pos);
    }

    public static void unregisterBed(UUID uUID, BlockPos pos) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.beds.remove(pos);
    }

    public static HashSet<BlockPos> getTribeChests(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return tribe.chests;
    }

    public static void registerChest(UUID uUID, BlockPos pos) {
        if (pos == null) {
            return;
        }
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.chests.add(pos);
    }

    public static void unregisterChest(UUID uUID, BlockPos pos) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.chests.remove(pos);
    }

    public static HashSet<BlockPos> removeTaskAndGetBlocks(UUID uUID, KoboldTaskInfo task) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<BlockPos>();
        }
        if (task != null) {
            tribe.removeTask(task);
            return task.b;
        }
        return new HashSet<BlockPos>();
    }

    public static HashSet<BlockPos> removeTaskByBlockPos(UUID uUID, BlockPos pos) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<BlockPos>();
        }
        KoboldTaskInfo task = null;
        for (KoboldTaskInfo tasks : tribe.tasks) {
            if (!tasks.b.contains(pos)) continue;
            task = tasks;
            break;
        }
        return KoboldManager.removeTaskAndGetBlocks(uUID, task);
    }

    public static void addTaskToTribe(UUID uUID, KoboldTaskInfo task) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.addTask(task);
    }

    public static void removeWorkerTask(UUID uUID, KoboldEntity worker) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        KoboldTaskInfo targetTask = null;
        for (KoboldTaskInfo task : tribe.tasks) {
            if (!task.b(worker)) continue;
            targetTask = task;
        }
        if (targetTask == null) {
            System.out.println("task of worker " + worker.girlID() + " not found uwu");
            return;
        }
        tribe.removeTask(targetTask);
    }

    @Nullable
    public static Collection<KoboldTaskInfo> getTribeTasks(@Nullable UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID + " not found uwu");
            return null;
        }
        return tribe.tasks;
    }

    public static TribeState getTribeState(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return TribeState.REST;
        }
        return tribe.getState();
    }

    public static void setTribeState(UUID uUID, TribeState state) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.setState(state);
    }

    public static int h(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return 0;
        }
        return a_inner492.f();
    }

    public static List<KoboldEntity> n(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID + " not found uwu");
            return new ArrayList<>();
        }
        return a_inner492.members;
    }

    public static void b(UUID uUID, BlockPos blockPos) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(blockPos);
    }

    @Nullable
    public static BlockPos m(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return a_inner492.g();
    }

    public static HashSet<EntityLivingBase> e(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<EntityLivingBase>();
        }
        return a_inner492.c();
    }

    public static void a(UUID uUID, EntityLivingBase entityLivingBase) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(entityLivingBase);
    }

    public static void b(UUID uUID, EntityLivingBase entityLivingBase) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.b(entityLivingBase);
    }

    public static boolean g(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        for (KoboldEntity ff_class3082 : a_inner492.members) {
            if (ff_class3082.getID() == null) continue;
            return true;
        }
        return false;
    }

    public static boolean c(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        return a_inner492.c;
    }

    public static void a(UUID uUID, boolean bl) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.c = bl;
    }

    @Nullable
    public static UUID findTribeIdWith(UUID tribeId) {
        if (tribeId == null) {
            return null;
        }
        for (Map.Entry<UUID, KoboldSavedData.KoboldTribe> entry : tribesMap.entrySet()) {
            KoboldSavedData.KoboldTribe a_inner492 = entry.getValue();
            if ((!a_inner492.d().isEmpty() || a_inner492.f() != 0) && tribeId.equals(a_inner492.a())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Nullable
    public static UUID b(UUID uUID) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        List<KoboldEntity> list = a_inner492.members;
        if (list.isEmpty()) {
            return null;
        }
        KoboldEntity ff_class3082 = list.get(0);
        if (!ff_class3082.isMasterAssigned()) {
            return null;
        }
        String string = list.get(0).getDataManager().get(GirlEntity.MASTER_UUID);
        return UUID.fromString(string);
    }

    public static HashSet<BlockPos> d(UUID uUID) {
        //b_inner50.a_inner49 a_inner492 = c.get(uUID);
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return hashSet;
        }
        for (KoboldTaskInfo bs_class972 : a_inner492.tasks) {
            hashSet.addAll(bs_class972.b);
        }
        hashSet.addAll(a_inner492.chests);
        hashSet.addAll(a_inner492.beds);
        return hashSet;
    }

    public static HashMap<UUID, BlockPos> a(UUID uUID, World world) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashMap<UUID, BlockPos>();
        }
        HashMap<UUID, BlockPos> hashMap = a_inner492.k;
        ArrayList<UUID> arrayList = new ArrayList<UUID>();
        for (Map.Entry<UUID, BlockPos> entry : hashMap.entrySet()) {
            BlockPos blockPos = entry.getValue();
            UUID uUID2 = entry.getKey();
            if (!world.isAreaLoaded(blockPos, 5)) continue;
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.subtract(new Vec3i(-3, -3, -3)), blockPos.add(3, 3, 3));
            List<KoboldEntity> list = world.getEntitiesWithinAABB(KoboldEntity.class, axisAlignedBB);
            boolean bl = false;
            for (KoboldEntity ff_class3082 : list) {
                if (!uUID2.equals(ff_class3082.girlID())) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            arrayList.add(uUID2);
        }
        a_inner492.k = hashMap;
        return hashMap;
    }

    public static void a(UUID uUID, UUID uUID2, BlockPos blockPos) {
        KoboldSavedData.KoboldTribe a_inner492 = tribesMap.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(uUID2, blockPos);
    }

    //static HashMap<UUID, b_inner50.a_inner49> access$000() {
    //    return c;
    //}

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class KoboldSavedData extends WorldSavedData {
        public KoboldSavedData(String string) {
            super(string);
        }

        @SubscribeEvent
        public void a(WorldEvent.Save save) {
            World world = save.getWorld();
            world.getMapStorage().setData("tribes", this);
            this.markDirty();
        }

        @SubscribeEvent
        public void a(WorldEvent.Load load) {
            World world = load.getWorld();
            world.getMapStorage().getOrLoadData(KoboldSavedData.class, "tribes");
        }

        @SubscribeEvent
        public void a(PlayerSleepInBedEvent playerSleepInBedEvent) {
            if (KoboldManager.isBedAssigned(playerSleepInBedEvent.getPos())) {
                playerSleepInBedEvent.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
            }
        }

        @SubscribeEvent
        public void a(BlockEvent.PlaceEvent placeEvent) {
            BlockPos blockPos = placeEvent.getPos();
            IBlockState iBlockState = placeEvent.getState();
            World world = placeEvent.getWorld();
            if (world.isRemote) {
                return;
            }
            if (!(iBlockState.getBlock() instanceof BlockChest)) {
                return;
            }
            BlockChest.Type type = ((BlockChest) world.getBlockState((BlockPos) blockPos).getBlock()).chestType;
            BlockPos blockPos2 = null;
            if (world.getBlockState(blockPos.north()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) blockPos.north()).getBlock()).chestType)) {
                blockPos2 = blockPos.north();
            }
            if (world.getBlockState(blockPos.east()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) blockPos.east()).getBlock()).chestType)) {
                blockPos2 = blockPos.east();
            }
            if (world.getBlockState(blockPos.south()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) blockPos.south()).getBlock()).chestType)) {
                blockPos2 = blockPos.south();
            }
            if (world.getBlockState(blockPos.west()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) blockPos.west()).getBlock()).chestType)) {
                blockPos2 = blockPos.west();
            }
            if (blockPos2 == null) {
                return;
            }
            //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
            for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                EntityPlayerMP entityPlayerMP;
                KoboldTribe a_inner492 = (KoboldTribe) entry.getValue();
                if (!a_inner492.chests.contains(blockPos2)) continue;
                a_inner492.chests.add(blockPos);
                UUID uUID = KoboldManager.b((UUID) entry.getKey());
                if (uUID == null || (entityPlayerMP = (EntityPlayerMP) world.getPlayerEntityByUUID(uUID)) == null)
                    continue;
                PackageHandler.networkWrapper.sendTo((IMessage) new SendBlocks(blockPos, true), entityPlayerMP);
            }
        }

        @SubscribeEvent
        public void a(EntityJoinWorldEvent event) {
            EntityMob entityMob;
            Entity entity = event.getEntity();
            if (entity instanceof EntityZombie) {
                entityMob = (EntityZombie) entity;
                entityMob.targetTasks.addTask(3, new TargetNearestKoboldGoal((EntityCreature) entityMob, true, false));
            }
            if (entity instanceof AbstractSkeleton) {
                entityMob = (AbstractSkeleton) entity;
                ((AbstractSkeleton) entityMob).targetTasks.addTask(3, new TargetNearestKoboldGoal((EntityCreature) entityMob, true, false));
            }
            if (entity instanceof EntitySpider) {
                entityMob = (EntitySpider) entity;
                ((EntitySpider) entityMob).targetTasks.addTask(3, new TargetNearestKoboldGoal((EntityCreature) entityMob, true, true));
            }
        }

        @SubscribeEvent
        public void a(BlockEvent.BreakEvent event) {
            Object object;
            Object object2;
            KoboldTribe a_inner492;
            BlockPos blockPos = event.getPos();
            World world = event.getWorld();
            if (world.isRemote) {
                return;
            }
            IBlockState iBlockState = world.getBlockState(blockPos);
            Block block = iBlockState.getBlock();
            if (block instanceof BlockChest) {
                //Map.Entry<UUID, a_inner49> entry
                //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                    a_inner492 = (KoboldTribe) entry.getValue();
                    if (!a_inner492.chests.contains(blockPos)) continue;
                    a_inner492.chests.remove(blockPos);
                    object2 = KoboldManager.b((UUID) entry.getKey());
                    if (object2 == null || (object = (EntityPlayerMP) world.getPlayerEntityByUUID((UUID) object2)) == null)
                        continue;
                    PackageHandler.networkWrapper.sendTo((IMessage) new SendBlocks(blockPos, false), (EntityPlayerMP) object);
                }
            }
            if (block instanceof BlockBed) {
                //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                    EntityPlayerMP entityPlayerMP;
                    a_inner492 = (KoboldTribe) entry.getValue();
                    if (!a_inner492.beds.contains(blockPos)) continue;
                    object2 = WorldUtils.getBedPairPosition(blockPos, iBlockState);
                    a_inner492.beds.remove(blockPos);
                    a_inner492.beds.remove(object2);
                    object = KoboldManager.b((UUID) entry.getKey());
                    if (object == null || (entityPlayerMP = (EntityPlayerMP) world.getPlayerEntityByUUID((UUID) object)) == null)
                        continue;
                    HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
                    hashSet.add(blockPos);
                    hashSet.add((BlockPos) object2);
                    PackageHandler.networkWrapper.sendTo((IMessage) new SendBlocks(hashSet, false), entityPlayerMP);
                }
            }
        }

        String a(String string, NBTTagCompound nBTTagCompound) {
            String string2 = nBTTagCompound.getString(string);
            nBTTagCompound.setString(string, "");
            return string2;
        }

        /*
        @Deprecated
        //@Override
        public void readFromNBT___(NBTTagCompound nBTTagCompound) {
            String string;
            int n = 0;
            while (!"".equals(string = this.a("tribeId" + n, nBTTagCompound))) {
                Object object;
                Object object2;
                Object object3;
                String string2;
                String string3;
                UUID uUID = UUID.fromString(string);
                EyeAndKoboldColor_class2 eyeAndKoboldColor_class2 = EyeAndKoboldColor_class2.valueOf(this.a("tribeColor" + n, nBTTagCompound));
                ax_class48.a(uUID, eyeAndKoboldColor_class2);
                String string4 = this.a("tribeMaster" + n, nBTTagCompound);
                if (!"".equals(string4)) {
                    ax_class48.a(uUID, UUID.fromString(string4));
                }
                int n2 = 0;
                while (!"".equals(string3 = this.a(uUID.toString() + "member" + n2 + "pos", nBTTagCompound)) && !"".equals(string2 = this.a(uUID.toString() + "member" + n2 + "id", nBTTagCompound))) {
                    object3 = string3.split("\\|");
                    object2 = new BlockPos(Integer.parseInt(object3[0]), Integer.parseInt(object3[1]), Integer.parseInt(object3[2]));
                    object = UUID.fromString(string2);
                    ax_class48.a(uUID, (UUID)object, (BlockPos)object2);
                    ++n2;
                }
                int n3 = 0;
                while (!"".equals(string2 = this.a(uUID.toString() + "bed" + n3, nBTTagCompound))) {
                    object3 = string2.split("\\|");
                    object2 = new BlockPos(Integer.parseInt(object3[0]), Integer.parseInt(object3[1]), Integer.parseInt(object3[2]));
                    ax_class48.a(uUID, (BlockPos)object2);
                    ++n3;
                }
                int n4 = 0;
                while (!"".equals(object3 = this.a(uUID.toString() + "chest" + n4, nBTTagCompound))) {
                    object2 = ((String)object3).split("\\|");
                    object = new BlockPos(Integer.parseInt(object2[0]), Integer.parseInt(object2[1]), Integer.parseInt(object2[2]));
                    ax_class48.f(uUID, (BlockPos)object);
                    ++n4;
                }
                int n5 = 0;
                while (!"".equals(object2 = this.a(uUID.toString() + n5 + "taskKind", nBTTagCompound))) {
                    String string5;
                    object = this.a(uUID.toString() + n5 + "facing", nBTTagCompound);
                    EnumFacing enumFacing = EnumFacing.NORTH;
                    if (!"".equals(object)) {
                        enumFacing = EnumFacing.byName((String)object);
                    }
                    String string6 = this.a(uUID.toString() + n5 + "pos", nBTTagCompound);
                    String[] stringArray = string6.split("\\|");
                    BlockPos blockPos = new BlockPos(Integer.parseInt(stringArray[0]), Integer.parseInt(stringArray[1]), Integer.parseInt(stringArray[2]));
                    HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
                    int n6 = 0;
                    while (!"".equals(string5 = this.a(uUID.toString() + n5 + "block" + n6, nBTTagCompound))) {
                        String[] stringArray2 = string5.split("\\|");
                        BlockPos blockPos2 = new BlockPos(Integer.parseInt(stringArray2[0]), Integer.parseInt(stringArray2[1]), Integer.parseInt(stringArray2[2]));
                        hashSet.add(blockPos2);
                        ++n6;
                    }
                    ax_class48.b(uUID, new bs_class97(blockPos, bs_class97.a_inner98.valueOf((String)object2), hashSet, enumFacing));
                    ++n5;
                }
                ++n;
            }
        }*/

        @Override
        public void readFromNBT(NBTTagCompound var1) {
            int var2 = 0;

            label73:
            while (true) {
                String var3 = this.a("tribeId" + var2, var1);
                if (var3.isEmpty()) {
                    return;
                }

                UUID var4 = UUID.fromString(var3);
                EyeAndKoboldColor var5 = EyeAndKoboldColor.valueOf(this.a("tribeColor" + var2, var1));
                KoboldManager.createTribe(var4, var5);
                String var6 = this.a("tribeMaster" + var2, var1);
                if (!"".equals(var6)) {
                    KoboldManager.setTribeMaster(var4, UUID.fromString(var6));
                }

                int var7 = 0;

                while (true) {
                    String var8 = this.a(var4 + "member" + var7 + "pos", var1);
                    if (var8.isEmpty()) {
                        break;
                    }

                    String var9 = this.a(var4 + "member" + var7 + "id", var1);
                    if (var9.isEmpty()) {
                        break;
                    }

                    String[] var10 = var8.split("\\|");
                    BlockPos var11 = new BlockPos(Integer.parseInt(var10[0]), Integer.parseInt(var10[1]), Integer.parseInt(var10[2]));
                    UUID var12 = UUID.fromString(var9);
                    KoboldManager.a(var4, var12, var11);
                    ++var7;
                }

                int var22 = 0;

                while (true) {
                    String var23 = this.a(var4.toString() + "bed" + var22, var1);
                    if ("".equals(var23)) {
                        int var24 = 0;

                        while (true) {
                            String var26 = this.a(var4.toString() + "chest" + var24, var1);
                            if ("".equals(var26)) {
                                int var27 = 0;

                                while (true) {
                                    String var30 = this.a(var4.toString() + var27 + "taskKind", var1);
                                    if (var30.isEmpty()) {
                                        ++var2;
                                        continue label73;
                                    }

                                    String var32 = this.a(var4.toString() + var27 + "facing", var1);
                                    EnumFacing var13 = EnumFacing.NORTH;
                                    if (!var32.isEmpty()) {
                                        var13 = EnumFacing.byName(var32);
                                    }

                                    String var14 = this.a(var4.toString() + var27 + "pos", var1);
                                    String[] var15 = var14.split("\\|");
                                    BlockPos var16 = new BlockPos(Integer.parseInt(var15[0]), Integer.parseInt(var15[1]), Integer.parseInt(var15[2]));
                                    HashSet<BlockPos> var17 = new HashSet<>();
                                    int var18 = 0;

                                    while (true) {
                                        String var19 = this.a(var4.toString() + var27 + "block" + var18, var1);
                                        if (var19.isEmpty()) {
                                            KoboldManager.addTaskToTribe(var4, new KoboldTaskInfo(var16, KoboldTaskInfo.KoboldTask.valueOf(var30), var17, var13));
                                            ++var27;
                                            break;
                                        }

                                        String[] var20 = var19.split("\\|");
                                        BlockPos var21 = new BlockPos(Integer.parseInt(var20[0]), Integer.parseInt(var20[1]), Integer.parseInt(var20[2]));
                                        var17.add(var21);
                                        ++var18;
                                    }
                                }
                            }

                            String[] var29 = var26.split("\\|");
                            BlockPos var31 = new BlockPos(Integer.parseInt(var29[0]), Integer.parseInt(var29[1]), Integer.parseInt(var29[2]));
                            KoboldManager.registerChest(var4, var31);
                            ++var24;
                        }
                    }

                    String[] var25 = var23.split("\\|");
                    BlockPos var28 = new BlockPos(Integer.parseInt(var25[0]), Integer.parseInt(var25[1]), Integer.parseInt(var25[2]));
                    KoboldManager.registerBed(var4, var28);
                    ++var22;
                }
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound var1) {
            int var2 = 0;

            for (Map.Entry var4 : KoboldManager.tribesMap.entrySet()) {
                KoboldTribe var5 = (KoboldTribe) var4.getValue();
                UUID var6 = (UUID) var4.getKey();
                UUID var7 = var5.a();
                var1.setString("tribeId" + var2, var6.toString());
                var1.setString("tribeColor" + var2, var5.color.toString());
                if (var7 != null) {
                    var1.setString("tribeMaster" + var2, var7.toString());
                }

                int var8 = 0;
                HashSet var9 = new HashSet();

                for (KoboldEntity var11 : var5.members) {
                    if (!var11.isDead) {
                        BlockPos var12 = var11.getPosition();
                        UUID var13 = var11.girlID();
                        var1.setString(var6.toString() + "member" + var8 + "pos", var12.getX() + "|" + var12.getY() + "|" + var12.getZ());
                        var1.setString(var6.toString() + "member" + var8 + "id", var13.toString());
                        var9.add(var13);
                        ++var8;
                    }
                }

                for (Map.Entry var20 : var5.k.entrySet()) {
                    UUID var23 = (UUID) var20.getKey();
                    BlockPos var27 = (BlockPos) var20.getValue();
                    if (!var9.contains(var23)) {
                        var1.setString(var6.toString() + "member" + var8 + "pos", var27.getX() + "|" + var27.getY() + "|" + var27.getZ());
                        var1.setString(var6.toString() + "member" + var8 + "id", var23.toString());
                        var9.add(var23);
                        ++var8;
                    }
                }

                int var19 = 0;

                for (BlockPos var24 : var5.beds) {
                    var1.setString(var6.toString() + "bed" + var19, var24.getX() + "|" + var24.getY() + "|" + var24.getZ());
                    ++var19;
                }

                int var22 = 0;

                for (BlockPos var28 : var5.chests) {
                    var1.setString(var6.toString() + "chest" + var22, var28.getX() + "|" + var28.getY() + "|" + var28.getZ());
                    ++var22;
                }

                int var26 = 0;

                for (KoboldTaskInfo var14 : var5.tasks) {
                    var1.setString(var6.toString() + var26 + "taskKind", var14.c.toString());
                    var1.setString(var6.toString() + var26 + "pos", var14.a.getX() + "|" + var14.a.getY() + "|" + var14.a.getZ());
                    var1.setString(var6.toString() + var26 + "facing", var14.e.getName());
                    int var15 = 0;

                    for (BlockPos var17 : var14.b) {
                        var1.setString(var6.toString() + var26 + "block" + var15, var17.getX() + "|" + var17.getY() + "|" + var17.getZ());
                        ++var15;
                    }

                    ++var26;
                }

                ++var2;
            }

            return var1;
        }

        /*
         * WARNING - void declaration
         */
        /*
        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
            int n = 0;
            for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                Iterator<BlockPos> iterator2;
                b_inner50.a_inner49 a_inner492 = (a_inner49)entry.getValue();
                UUID uUID = (UUID)entry.getKey();
                UUID uUID2 = a_inner492.a();
                nBTTagCompound.setString("tribeId" + n, uUID.toString());
                nBTTagCompound.setString("tribeColor" + n, a_inner492.h.toString());
                if (uUID2 != null) {
                    nBTTagCompound.setString("tribeMaster" + n, uUID2.toString());
                }
                int n2 = 0;
                HashSet<Object> hashSet = new HashSet<Object>();
                for (KoboldEntity iterator32 : a_inner492.a) {
                    if (iterator32.isDead) continue;
                    iterator2 = iterator32.getPosition();
                    UUID uUID3 = iterator32.java_util_UUID_f();
                    nBTTagCompound.setString(uUID.toString() + "member" + n2 + "pos", ((Vec3i)((Object)iterator2)).getX() + "|" + ((Vec3i)((Object)iterator2)).getY() + "|" + ((Vec3i)((Object)iterator2)).getZ());
                    nBTTagCompound.setString(uUID.toString() + "member" + n2 + "id", uUID3.toString());
                    hashSet.add(uUID3);
                    ++n2;
                }
                for (Map.Entry entry2 : a_inner492.k.entrySet()) {
                    iterator2 = (UUID)entry2.getKey();
                    BlockPos blockPos = (BlockPos)entry2.getValue();
                    if (hashSet.contains(iterator2)) continue;
                    nBTTagCompound.setString(uUID.toString() + "member" + n2 + "pos", blockPos.getX() + "|" + blockPos.getY() + "|" + blockPos.getZ());
                    nBTTagCompound.setString(uUID.toString() + "member" + n2 + "id", ((UUID)((Object)iterator2)).toString());
                    hashSet.add(iterator2);
                    ++n2;
                }
                int n3 = 0;
                for (Iterator<BlockPos> iterator2 : a_inner492.b) {
                    nBTTagCompound.setString(uUID.toString() + "bed" + n3, ((Vec3i)((Object)iterator2)).getX() + "|" + ((Vec3i)((Object)iterator2)).getY() + "|" + ((Vec3i)((Object)iterator2)).getZ());
                    ++n3;
                }
                boolean bl = false;
                iterator2 = a_inner492.i.iterator();
                while (iterator2.hasNext()) {
                    void n4;
                    BlockPos blockPos = iterator2.next();
                    nBTTagCompound.setString(uUID.toString() + "chest" + (int)n4, blockPos.getX() + "|" + blockPos.getY() + "|" + blockPos.getZ());
                    ++n4;
                }
                int n4 = 0;
                for (bs_class97 bs_class972 : a_inner492.f) {
                    nBTTagCompound.setString(uUID.toString() + n4 + "taskKind", bs_class972.c.toString());
                    nBTTagCompound.setString(uUID.toString() + n4 + "pos", bs_class972.a.getX() + "|" + bs_class972.a.getY() + "|" + bs_class972.a.getZ());
                    nBTTagCompound.setString(uUID.toString() + n4 + "facing", bs_class972.e.getName());
                    int n5 = 0;
                    for (BlockPos blockPos : bs_class972.b) {
                        nBTTagCompound.setString(uUID.toString() + n4 + "block" + n5, blockPos.getX() + "|" + blockPos.getY() + "|" + blockPos.getZ());
                        ++n5;
                    }
                    ++n4;
                }
                ++n;
            }
            return nBTTagCompound;
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }*/

        public static class KoboldTribe {
            UUID m;
            UUID e;
            KoboldEntity leader;
            List<KoboldEntity> members;
            EyeAndKoboldColor color;
            TribeState d = TribeState.REST;
            BlockPos l = null;
            Collection<KoboldTaskInfo> tasks = new ArrayList<KoboldTaskInfo>();
            HashSet<EntityLivingBase> j = new HashSet();
            HashSet<BlockPos> chests = new HashSet();
            HashSet<BlockPos> beds = new HashSet();
            HashMap<UUID, BlockPos> k = new HashMap();
            boolean c = false;

            public KoboldTribe(UUID uUID, EyeAndKoboldColor eyeAndKoboldColor_, KoboldEntity ff_class3082, List<KoboldEntity> list) {
                this.m = uUID;
                this.color = eyeAndKoboldColor_;
                this.leader = ff_class3082;
                this.members = list;
            }

            public KoboldTribe(UUID uUID, EyeAndKoboldColor eyeAndKoboldColor_) {
                this.m = uUID;
                this.color = eyeAndKoboldColor_;
                this.members = new ArrayList<KoboldEntity>();
            }

            public void setMasterUUID(UUID uUID) {
                this.e = uUID;
            }

            public UUID a() {
                return this.e;
            }

            public void removeTask(KoboldTaskInfo bs_class972) {
                if (!this.tasks.contains(bs_class972)) {
                    return;
                }
                for (KoboldEntity ff_class3082 : bs_class972.f) {
                    ff_class3082.setCurrentAction(Action.NULL);
                    ff_class3082.setNoGravity(false);
                    ff_class3082.noClip = false;
                    ff_class3082.getDataManager().set(GirlEntity.IS_ANCHORED, false);
                }
                this.tasks.remove(bs_class972);
                if (bs_class972.b.isEmpty() || this.e == null) {
                    return;
                }
                EntityPlayerMP entityPlayerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(this.e);
                if (entityPlayerMP == null) {
                    return;
                }
                PackageHandler.networkWrapper.sendTo((IMessage) new SendBlocks(bs_class972.b, false), entityPlayerMP);
            }

            public HashMap<UUID, BlockPos> d() {
                return this.k;
            }

            public void a(UUID uUID, BlockPos blockPos) {
                this.k.put(uUID, blockPos);
            }

            public void removeMemberUUID(UUID uUID) {
                this.k.remove(uUID);
            }

            public void b(EntityLivingBase entityLivingBase) {
                this.j.remove(entityLivingBase);
            }

            public void a(EntityLivingBase entityLivingBase) {
                this.j.add(entityLivingBase);
            }

            public HashSet<EntityLivingBase> c() {
                return this.j;
            }

            public int f() {
                HashSet<UUID> hashSet = new HashSet<UUID>();
                for (KoboldEntity object : this.members) {
                    hashSet.add(object.girlID());
                }
                for (Map.Entry entry : this.k.entrySet()) {
                    hashSet.add((UUID) entry.getKey());
                }
                return hashSet.size();
            }

            public BlockPos g() {
                return this.l;
            }

            public void a(BlockPos blockPos) {
                this.l = blockPos;
            }

            public void addTask(KoboldTaskInfo bs_class972) {
                this.tasks.add(bs_class972);
            }

            public TribeState getState() {
                return this.d;
            }

            public void setState(TribeState fm_class3192) {
                this.d = fm_class3192;
            }

            public void addMember(KoboldEntity ff_class3082) {
                if (this.members.contains(ff_class3082)) {
                    return;
                }
                UUID uUID = ff_class3082.girlID();
                ArrayList<KoboldEntity> arrayList = new ArrayList<KoboldEntity>();
                for (KoboldEntity ff_class3083 : this.members) {
                    if (!ff_class3083.girlID().equals(uUID)) continue;
                    arrayList.add(ff_class3083);
                }
                for (KoboldEntity ff_class3083 : arrayList) {
                    Main.LOGGER.warn(String.format("Removed old entry of kobold called %s with UUID %s owned by %s", ff_class3083.getGirlName(), ff_class3083.girlID(), this.e));
                    this.removeMember(ff_class3083);
                }
                this.members.add(ff_class3082);
            }

            public void removeMember(KoboldEntity ff_class3082) {
                this.members.remove(ff_class3082);
            }

            KoboldEntity findNewLeader() {
                KoboldEntity kobold = null;
                for (KoboldEntity ff_class3083 : this.members) {
                    if (ff_class3083.isDead) continue;
                    if (kobold == null) {
                        kobold = ff_class3083;
                        continue;
                    }
                    float f = kobold.getDataManager().get(KoboldEntity.aE).floatValue();
                    float f2 = ff_class3083.getDataManager().get(KoboldEntity.aE).floatValue();
                    if (!(f2 < f)) continue;
                    kobold = ff_class3083;
                }
                return kobold;
            }
        }
    }
}

