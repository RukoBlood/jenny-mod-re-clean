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
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
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
            task.removeWorker(kobold);
        }
        if (!tribe.members.isEmpty()) {
            tribesMap.replace(uUID, tribe);
            return;
        }
        if (!kobold.hasMaster()) {
            return;
        }
        EntityPlayer master = kobold.getMasterPlayer();
        if (master != null) {
            HashSet<BlockPos> tribeBlocks = new HashSet<BlockPos>();
            tribeBlocks.addAll(tribe.chests);
            tribeBlocks.addAll(tribe.beds);
            for (KoboldTaskInfo task : tribe.tasks) {
                tribeBlocks.addAll(task.targetBlocks);
            }
            PackageHandler.INSTANCE.sendTo((IMessage) new SendBlocks(tribeBlocks, false), (EntityPlayerMP) master);
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
            return task.targetBlocks;
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
            if (!tasks.targetBlocks.contains(pos)) continue;
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
            if (!task.hasWorker(worker)) continue;
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

    public static int getTribeMemberCount(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return 0;
        }
        return tribe.getMemberCount();
    }

    public static List<KoboldEntity> getTribeMembersList(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID + " not found uwu");
            return new ArrayList<>();
        }
        return tribe.members;
    }

    public static void setTribeHomePos(UUID uUID, BlockPos pos) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.setHomePos(pos);
    }

    @Nullable
    public static BlockPos getTribeHomePos(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return tribe.getHomePos();
    }

    public static HashSet<EntityLivingBase> getTribeTargets(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<EntityLivingBase>();
        }
        return tribe.getTargets();
    }

    public static void addTribeTarget(UUID uUID, EntityLivingBase target) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.addTarget(target);
    }

    public static void removeTribeTarget(UUID uUID, EntityLivingBase target) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.removeTarget(target);
    }

    public static boolean hasAssignedMaster(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        for (KoboldEntity member : tribe.members) {
            if (member.playerSheHasSexWith() == null) continue;
            return true;
        }
        return false;
    }

    public static boolean isTribeAlerted(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        return tribe.isAlerted;
    }

    public static void setTribeAlerted(UUID uUID, boolean alerted) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        tribe.isAlerted = alerted;
    }

    @Nullable
    public static UUID findTribeIdWith(UUID targetId) {
        if (targetId == null) {
            return null;
        }
        for (Map.Entry<UUID, KoboldSavedData.KoboldTribe> entry : tribesMap.entrySet()) {
            KoboldSavedData.KoboldTribe tribe = entry.getValue();
            if ((!tribe.getUnloadedMemberPositions().isEmpty() || tribe.getMemberCount() != 0) && targetId.equals(tribe.getMasterUUID())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Nullable
    public static UUID getTribeMasterUUID(UUID uUID) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        List<KoboldEntity> memberList = tribe.members;
        if (memberList.isEmpty()) {
            return null;
        }
        KoboldEntity member = memberList.get(0);
        if (!member.hasMaster()) {
            return null;
        }
        String masterUUIDStr = memberList.get(0).getDataManager().get(GirlEntity.MASTER_UUID);
        return UUID.fromString(masterUUIDStr);
    }

    public static HashSet<BlockPos> getAllTribeBlocks(UUID uUID) {
        //b_inner50.a_inner49 a_inner492 = c.get(uUID);
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        HashSet<BlockPos> allBlocks = new HashSet<BlockPos>();
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return allBlocks;
        }
        for (KoboldTaskInfo task : tribe.tasks) {
            allBlocks.addAll(task.targetBlocks);
        }
        allBlocks.addAll(tribe.chests);
        allBlocks.addAll(tribe.beds);
        return allBlocks;
    }

    public static HashMap<UUID, BlockPos> getUnloadedMembersMap(UUID uUID, World world) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashMap<UUID, BlockPos>();
        }
        HashMap<UUID, BlockPos> unloadedMap = tribe.unloadedMembers;
        ArrayList<UUID> missingUUIDs = new ArrayList<UUID>();

        for (Map.Entry<UUID, BlockPos> entry : unloadedMap.entrySet()) {
            BlockPos pos = entry.getValue();
            UUID koboldUUID = entry.getKey();
            if (!world.isAreaLoaded(pos, 5)) continue;

            AxisAlignedBB checkArea = new AxisAlignedBB(pos.subtract(new Vec3i(-3, -3, -3)), pos.add(3, 3, 3));
            List<KoboldEntity> nearbyKobolds = world.getEntitiesWithinAABB(KoboldEntity.class, checkArea);
            boolean isFound = false;
            for (KoboldEntity kobold : nearbyKobolds) {
                if (!koboldUUID.equals(kobold.girlID())) continue;
                isFound = true;
                break;
            }
            if (isFound) continue;
            missingUUIDs.add(koboldUUID);
        }
        tribe.unloadedMembers = unloadedMap;
        return unloadedMap;
    }

    public static void registerUnloadedMemberPos(UUID tribeId, UUID koboldId, BlockPos pos) {
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(tribeId);
        if (tribe == null) {
            System.out.println("tribe of UUID " + tribeId.toString() + " not found uwu");
            return;
        }
        tribe.registerUnloadedPos(koboldId, pos);
    }

    //static HashMap<UUID, b_inner50.a_inner49> access$000() {
    //    return c;
    //}

    public static class KoboldSavedData extends WorldSavedData {
        public KoboldSavedData(String name) {
            super(name);
        }

        @SubscribeEvent
        public void onWorldSave(WorldEvent.Save save) {
            World world = save.getWorld();
            world.getMapStorage().setData("tribes", this);
            this.markDirty();
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load load) {
            World world = load.getWorld();
            world.getMapStorage().getOrLoadData(KoboldSavedData.class, "tribes");
        }

        @SubscribeEvent
        public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
            if (KoboldManager.isBedAssigned(event.getPos())) {
                event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
            }
        }

        @SubscribeEvent
        public void onBlockPlace(BlockEvent.PlaceEvent event) {
            BlockPos pos = event.getPos();
            IBlockState state = event.getState();
            World world = event.getWorld();

            if (world.isRemote) {
                return;
            }
            if (!(state.getBlock() instanceof BlockChest)) {
                return;
            }

            BlockChest.Type type = ((BlockChest) world.getBlockState((BlockPos) pos).getBlock()).chestType;
            BlockPos pairChestPos = null;
            if (world.getBlockState(pos.north()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) pos.north()).getBlock()).chestType)) {
                pairChestPos = pos.north();
            }
            if (world.getBlockState(pos.east()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) pos.east()).getBlock()).chestType)) {
                pairChestPos = pos.east();
            }
            if (world.getBlockState(pos.south()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) pos.south()).getBlock()).chestType)) {
                pairChestPos = pos.south();
            }
            if (world.getBlockState(pos.west()).getBlock() instanceof BlockChest && type.equals((Object) ((BlockChest) world.getBlockState((BlockPos) pos.west()).getBlock()).chestType)) {
                pairChestPos = pos.west();
            }
            if (pairChestPos == null) {
                return;
            }

            //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
            for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                EntityPlayerMP masterPlayer;
                KoboldTribe tribe = (KoboldTribe) entry.getValue();
                if (!tribe.chests.contains(pairChestPos)) continue;
                tribe.chests.add(pos);
                UUID masterUUID = KoboldManager.getTribeMasterUUID((UUID) entry.getKey());
                if (masterUUID == null || (masterPlayer = (EntityPlayerMP) world.getPlayerEntityByUUID(masterUUID)) == null)
                    continue;
                PackageHandler.INSTANCE.sendTo((IMessage) new SendBlocks(pos, true), masterPlayer);
            }
        }

        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent event) {
            EntityMob mob;
            Entity entity = event.getEntity();
            if (entity instanceof EntityZombie) {
                mob = (EntityZombie) entity;
                mob.targetTasks.addTask(3, new TargetNearestKoboldGoal((EntityCreature) mob, true, false));
            }
            if (entity instanceof AbstractSkeleton) {
                mob = (AbstractSkeleton) entity;
                ((AbstractSkeleton) mob).targetTasks.addTask(3, new TargetNearestKoboldGoal((EntityCreature) mob, true, false));
            }
            if (entity instanceof EntitySpider) {
                mob = (EntitySpider) entity;
                ((EntitySpider) mob).targetTasks.addTask(3, new TargetNearestKoboldGoal((EntityCreature) mob, true, true));
            }
        }

        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event) {
            Object masterPlayer;
            Object masterUUID;
            KoboldTribe tribe;

            BlockPos pos = event.getPos();
            World world = event.getWorld();
            if (world.isRemote) {
                return;
            }
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block instanceof BlockChest) {
                //Map.Entry<UUID, a_inner49> entry
                //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                    tribe = (KoboldTribe) entry.getValue();
                    if (!tribe.chests.contains(pos)) continue;

                    tribe.chests.remove(pos);
                    masterUUID = KoboldManager.getTribeMasterUUID((UUID) entry.getKey());

                    if (masterUUID == null || (masterPlayer = (EntityPlayerMP) world.getPlayerEntityByUUID((UUID) masterUUID)) == null)
                        continue;
                    PackageHandler.INSTANCE.sendTo((IMessage) new SendBlocks(pos, false), (EntityPlayerMP) masterPlayer);
                }
            }

            if (block instanceof BlockBed) {
                //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                    EntityPlayerMP masterPlayerMessed;
                    tribe = (KoboldTribe) entry.getValue();
                    if (!tribe.beds.contains(pos)) continue;

                    masterUUID = WorldUtils.getBedPairPosition(pos, state);
                    tribe.beds.remove(pos);
                    tribe.beds.remove(masterUUID);

                    masterPlayer = KoboldManager.getTribeMasterUUID((UUID) entry.getKey());
                    if (masterPlayer == null || (masterPlayerMessed = (EntityPlayerMP) world.getPlayerEntityByUUID((UUID) masterPlayer)) == null)
                        continue;

                    HashSet<BlockPos> removedBeds = new HashSet<BlockPos>();
                    removedBeds.add(pos);
                    removedBeds.add((BlockPos) masterUUID);
                    PackageHandler.INSTANCE.sendTo((IMessage) new SendBlocks(removedBeds, false), masterPlayerMessed);
                }
            }
        }

        String popNbtString(String key, NBTTagCompound nbt) {
            String value = nbt.getString(key);
            nbt.setString(key, "");
            return value;
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

//        @Override
//        public void readFromNBT(NBTTagCompound nbt) {
//            int tribeIdx = 0;
//
//            label73:
//            while (true) {
//                String tribeIDStr = this.popNbtString("tribeId" + tribeIdx, nbt);
//                if (tribeIDStr.isEmpty()) {
//                    return;
//                }
//
//                UUID tribeId = UUID.fromString(tribeIDStr);
//                EyeAndKoboldColor color = EyeAndKoboldColor.valueOf(this.popNbtString("tribeColor" + tribeIdx, nbt));
//                KoboldManager.createTribe(tribeId, color);
//                String masterStr = this.popNbtString("tribeMaster" + tribeIdx, nbt);
//                if (masterStr.isEmpty()) {
//                    KoboldManager.setTribeMaster(tribeId, UUID.fromString(masterStr));
//                }
//
//                int memberIdx = 0;
//
//                while (true) {
//                    String posStr = this.popNbtString(tribeId + "member" + memberIdx + "pos", nbt);
//                    if (posStr.isEmpty()) {
//                        break;
//                    }
//
//                    String idStr = this.popNbtString(tribeId + "member" + memberIdx + "id", nbt);
//                    if (idStr.isEmpty()) {
//                        break;
//                    }
//
//                    String[] split = posStr.split("\\|");
//                    BlockPos memberPos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
//                    UUID memberUUID = UUID.fromString(idStr);
//                    KoboldManager.registerUnloadedMemberPos(tribeId, memberUUID, memberPos);
//                    ++memberIdx;
//                }
//
//                int bedIdx = 0;
//
//                while (true) {
//                    String bedStr = this.popNbtString(tribeId.toString() + "bed" + bedIdx, nbt);
//                    if (bedStr.isEmpty()) {
//                        int chestIdx = 0;
//
//                        while (true) {
//                            String chestStr = this.popNbtString(tribeId.toString() + "chest" + chestIdx, nbt);
//                            if ("".equals(chestStr)) {
//                                int taskIdx = 0;
//
//                                while (true) {
//                                    String taskKindStr = this.popNbtString(tribeId.toString() + taskIdx + "taskKind", nbt);
//                                    if (taskKindStr.isEmpty()) {
//                                        ++tribeIdx;
//                                        continue label73;
//                                    }
//
//                                    String var32 = this.popNbtString(tribeId.toString() + taskIdx + "facing", nbt);
//                                    EnumFacing var13 = EnumFacing.NORTH;
//                                    if (!var32.isEmpty()) {
//                                        var13 = EnumFacing.byName(var32);
//                                    }
//
//                                    String var14 = this.popNbtString(tribeId.toString() + taskIdx + "pos", nbt);
//                                    String[] var15 = var14.split("\\|");
//                                    BlockPos var16 = new BlockPos(Integer.parseInt(var15[0]), Integer.parseInt(var15[1]), Integer.parseInt(var15[2]));
//                                    HashSet<BlockPos> var17 = new HashSet<>();
//                                    int var18 = 0;
//
//                                    while (true) {
//                                        String var19 = this.popNbtString(tribeId.toString() + taskIdx + "block" + var18, nbt);
//                                        if (var19.isEmpty()) {
//                                            KoboldManager.addTaskToTribe(tribeId, new KoboldTaskInfo(var16, KoboldTaskInfo.KoboldTask.valueOf(taskKindStr), var17, var13));
//                                            ++taskIdx;
//                                            break;
//                                        }
//
//                                        String[] var20 = var19.split("\\|");
//                                        BlockPos var21 = new BlockPos(Integer.parseInt(var20[0]), Integer.parseInt(var20[1]), Integer.parseInt(var20[2]));
//                                        var17.add(var21);
//                                        ++var18;
//                                    }
//                                }
//                            }
//
//                            String[] var29 = chestStr.split("\\|");
//                            BlockPos var31 = new BlockPos(Integer.parseInt(var29[0]), Integer.parseInt(var29[1]), Integer.parseInt(var29[2]));
//                            KoboldManager.registerChest(tribeId, var31);
//                            ++chestIdx;
//                        }
//                    }
//
//                    String[] var25 = bedStr.split("\\|");
//                    BlockPos var28 = new BlockPos(Integer.parseInt(var25[0]), Integer.parseInt(var25[1]), Integer.parseInt(var25[2]));
//                    KoboldManager.registerBed(tribeId, var28);
//                    ++bedIdx;
//                }
//            }
//        }

        //Gemini generated code
        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            int tribeIdx = 0;

            while (true) {
                String tribeIdStr = this.popNbtString("tribeId" + tribeIdx, nbt);
                if (tribeIdStr.isEmpty()) return;

                UUID tribeUUID = UUID.fromString(tribeIdStr);
                EyeAndKoboldColor color = EyeAndKoboldColor.valueOf(this.popNbtString("tribeColor" + tribeIdx, nbt));
                KoboldManager.createTribe(tribeUUID, color);

                String masterStr = this.popNbtString("tribeMaster" + tribeIdx, nbt);
                if (!masterStr.isEmpty()) {
                    KoboldManager.setTribeMaster(tribeUUID, UUID.fromString(masterStr));
                }

                // Загрузка членов племени
                int memberIdx = 0;
                while (true) {
                    String posStr = this.popNbtString(tribeUUID + "member" + memberIdx + "pos", nbt);
                    if (posStr.isEmpty()) break;

                    String idStr = this.popNbtString(tribeUUID + "member" + memberIdx + "id", nbt);
                    if (idStr.isEmpty()) break;

                    String[] split = posStr.split("\\|");
                    BlockPos memberPos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    UUID memberUUID = UUID.fromString(idStr);

                    KoboldManager.registerUnloadedMemberPos(tribeUUID, memberUUID, memberPos);
                    ++memberIdx;
                }

                // Загрузка кроватей
                int bedIdx = 0;
                while (true) {
                    String bedStr = this.popNbtString(tribeUUID + "bed" + bedIdx, nbt);
                    if (bedStr.isEmpty()) break;

                    String[] split = bedStr.split("\\|");
                    BlockPos bedPos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    KoboldManager.registerBed(tribeUUID, bedPos);
                    ++bedIdx;
                }

                // Загрузка сундуков
                int chestIdx = 0;
                while (true) {
                    String chestStr = this.popNbtString(tribeUUID + "chest" + chestIdx, nbt);
                    if (chestStr.isEmpty()) break;

                    String[] split = chestStr.split("\\|");
                    BlockPos chestPos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    KoboldManager.registerChest(tribeUUID, chestPos);
                    ++chestIdx;
                }

                // Загрузка задач (Tasks)
                int taskIdx = 0;
                while (true) {
                    //i Hope this works
                    String taskKindStr = this.popNbtString(tribeUUID.toString() + taskIdx + "taskKind", nbt);
                    if (taskKindStr.isEmpty()) {
                        ++tribeIdx;
                        break;
                    }

                    String facingStr = this.popNbtString(tribeUUID.toString() + taskIdx + "facing", nbt);
                    EnumFacing facing = EnumFacing.NORTH;
                    if (!facingStr.isEmpty()) {
                        facing = EnumFacing.byName(facingStr);
                    }

                    String posStr = this.popNbtString(tribeUUID.toString() + taskIdx + "pos", nbt);
                    String[] split = posStr.split("\\|");
                    BlockPos taskPos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

                    HashSet<BlockPos> taskBlocks = new HashSet<>();
                    int blockIdx = 0;

                    while (true) {
                        String blockStr = this.popNbtString(tribeUUID.toString() + taskIdx + "block" + blockIdx, nbt);
                        if (blockStr.isEmpty()) {
                            KoboldManager.addTaskToTribe(tribeUUID, new KoboldTaskInfo(taskPos, KoboldTaskInfo.KoboldTask.valueOf(taskKindStr), taskBlocks, facing));
                            ++taskIdx;
                            break;
                        }

                        String[] blockSplit = blockStr.split("\\|");
                        BlockPos bPos = new BlockPos(Integer.parseInt(blockSplit[0]), Integer.parseInt(blockSplit[1]), Integer.parseInt(blockSplit[2]));
                        taskBlocks.add(bPos);
                        ++blockIdx;
                    }
                }
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            int tribeIdx = 0;

            for (Map.Entry<UUID, KoboldTribe> entry : KoboldManager.tribesMap.entrySet()) {
                KoboldTribe tribe = (KoboldTribe) entry.getValue();
                UUID tribeId = (UUID) entry.getKey();
                UUID tribeMaster = tribe.getMasterUUID();
                nbt.setString("tribeId" + tribeIdx, tribeId.toString());
                nbt.setString("tribeColor" + tribeIdx, tribe.color.toString());
                if (tribeMaster != null) {
                    nbt.setString("tribeMaster" + tribeIdx, tribeMaster.toString());
                }

                int memberIdx = 0;
                HashSet<UUID> savedMembers = new HashSet();

                for (KoboldEntity member : tribe.members) {
                    if (!member.isDead) {
                        BlockPos pos = member.getPosition();
                        UUID girlID = member.girlID();
                        nbt.setString(tribeId.toString() + "member" + memberIdx + "pos", pos.getX() + "|" + pos.getY() + "|" + pos.getZ());
                        nbt.setString(tribeId.toString() + "member" + memberIdx + "id", girlID.toString());
                        savedMembers.add(girlID);
                        ++memberIdx;
                    }
                }

                for (Map.Entry<UUID, BlockPos> unloadedEntry : tribe.unloadedMembers.entrySet()) {
                    UUID unloadedID = (UUID) unloadedEntry.getKey();
                    BlockPos pos = (BlockPos) unloadedEntry.getValue();
                    if (!savedMembers.contains(unloadedID)) {
                        nbt.setString(tribeId.toString() + "member" + memberIdx + "pos", pos.getX() + "|" + pos.getY() + "|" + pos.getZ());
                        nbt.setString(tribeId.toString() + "member" + memberIdx + "id", unloadedID.toString());
                        savedMembers.add(unloadedID);
                        ++memberIdx;
                    }
                }

                int bedIdx = 0;

                for (BlockPos bedPos : tribe.beds) {
                    nbt.setString(tribeId.toString() + "bed" + bedIdx, bedPos.getX() + "|" + bedPos.getY() + "|" + bedPos.getZ());
                    ++bedIdx;
                }

                int chestIdx = 0;

                for (BlockPos chestPos : tribe.chests) {
                    nbt.setString(tribeId.toString() + "chest" + chestIdx, chestPos.getX() + "|" + chestPos.getY() + "|" + chestPos.getZ());
                    ++chestIdx;
                }

                int taskIdx = 0;

                for (KoboldTaskInfo task : tribe.tasks) {
                    nbt.setString(tribeId.toString() + taskIdx + "taskKind", task.taskType.toString());
                    nbt.setString(tribeId.toString() + taskIdx + "pos", task.originPos.getX() + "|" + task.originPos.getY() + "|" + task.originPos.getZ());
                    nbt.setString(tribeId.toString() + taskIdx + "facing", task.facing.getName());
                    int blockIdx = 0;

                    for (BlockPos blockPos : task.targetBlocks) {
                        nbt.setString(tribeId.toString() + taskIdx + "block" + blockIdx, blockPos.getX() + "|" + blockPos.getY() + "|" + blockPos.getZ());
                        ++blockIdx;
                    }

                    ++taskIdx;
                }

                ++tribeIdx;
            }

            return nbt;
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
            public UUID tribeUUID;
            public UUID masterUUID;
            public KoboldEntity leader;
            public List<KoboldEntity> members;
            public EyeAndKoboldColor color;
            public TribeState state = TribeState.REST;
            public BlockPos homePos = null;
            public Collection<KoboldTaskInfo> tasks = new ArrayList<KoboldTaskInfo>();
            public HashSet<EntityLivingBase> targets = new HashSet();
            public HashSet<BlockPos> chests = new HashSet();
            public HashSet<BlockPos> beds = new HashSet();
            public HashMap<UUID, BlockPos> unloadedMembers = new HashMap();
            public boolean isAlerted = false;

            public KoboldTribe(UUID uUID, EyeAndKoboldColor color, KoboldEntity kobold, List<KoboldEntity> members) {
                this.tribeUUID = uUID;
                this.color = color;
                this.leader = kobold;
                this.members = members;
            }

            public KoboldTribe(UUID uUID, EyeAndKoboldColor color) {
                this.tribeUUID = uUID;
                this.color = color;
                this.members = new ArrayList<KoboldEntity>();
            }

            public void setMasterUUID(UUID uUID) {
                this.masterUUID = uUID;
            }

            public UUID getMasterUUID() {
                return this.masterUUID;
            }

            public void removeTask(KoboldTaskInfo task) {
                if (!this.tasks.contains(task)) {
                    return;
                }
                for (KoboldEntity worker : task.assignedWorkers) {
                    worker.setCurrentAction(Action.NULL);
                    worker.setNoGravity(false);
                    worker.noClip = false;
                    worker.getDataManager().set(GirlEntity.IS_ANCHORED, false);
                }
                this.tasks.remove(task);
                if (task.targetBlocks.isEmpty() || this.masterUUID == null) {
                    return;
                }
                EntityPlayerMP masterPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(this.masterUUID);
                if (masterPlayer != null) {
                    PackageHandler.INSTANCE.sendTo((IMessage) new SendBlocks(task.targetBlocks, false), masterPlayer);
                }
            }

            public HashMap<UUID, BlockPos> getUnloadedMemberPositions() {
                return this.unloadedMembers;
            }

            public void registerUnloadedPos(UUID uUID, BlockPos blockPos) {
                this.unloadedMembers.put(uUID, blockPos);
            }

            public void removeMemberUUID(UUID uUID) {
                this.unloadedMembers.remove(uUID);
            }

            public void removeTarget(EntityLivingBase entityLivingBase) {
                this.targets.remove(entityLivingBase);
            }

            public void addTarget(EntityLivingBase entityLivingBase) {
                this.targets.add(entityLivingBase);
            }

            public HashSet<EntityLivingBase> getTargets() {
                return this.targets;
            }

            public int getMemberCount() {
                HashSet<UUID> hashSet = new HashSet<UUID>();
                for (KoboldEntity object : this.members) {
                    hashSet.add(object.girlID());
                }
                for (Map.Entry entry : this.unloadedMembers.entrySet()) {
                    hashSet.add((UUID) entry.getKey());
                }
                return hashSet.size();
            }

            public BlockPos getHomePos() {
                return this.homePos;
            }

            public void setHomePos(BlockPos blockPos) {
                this.homePos = blockPos;
            }

            public void addTask(KoboldTaskInfo task) {
                this.tasks.add(task);
            }

            public TribeState getState() {
                return this.state;
            }

            public void setState(TribeState state) {
                this.state = state;
            }

            public void addMember(KoboldEntity kobold) {
                if (this.members.contains(kobold)) {
                    return;
                }
                UUID uUID = kobold.girlID();
                ArrayList<KoboldEntity> oldDuplicates = new ArrayList<KoboldEntity>();
                for (KoboldEntity existing : this.members) {
                    if (!existing.girlID().equals(uUID)) continue;
                    oldDuplicates.add(existing);
                }
                for (KoboldEntity dupe : oldDuplicates) {
                    Main.LOGGER.warn("Removed old entry of kobold called {} with UUID {} owned by {}", dupe.getGirlName(), dupe.girlID(), this.masterUUID);
                    this.removeMember(dupe);
                }
                this.members.add(kobold);
            }

            public void removeMember(KoboldEntity kobold) {
                this.members.remove(kobold);
            }

            public KoboldEntity findNewLeader() {
                KoboldEntity candidate = null;
                for (KoboldEntity member : this.members) {
                    if (member.isDead) continue;
                    if (candidate == null) {
                        candidate = member;
                        continue;
                    }
                    float scale1 = candidate.getDataManager().get(KoboldEntity.aE);
                    float scale2 = member.getDataManager().get(KoboldEntity.aE);
                    if (!(scale2 < scale1)) continue;
                    candidate = member;
                }
                return candidate;
            }
        }
    }
}

