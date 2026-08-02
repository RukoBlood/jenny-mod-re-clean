/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold;

import java.util.*;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KoboldTaskInfo {
    final static public int MAX_WORLD_RADIUS = 30;
    BlockPos originPos;
    KoboldTask taskType;
    HashSet<BlockPos> targetBlocks;
    List<KoboldEntity> assignedWorkers = new ArrayList<KoboldEntity>();
    EnumFacing facing = EnumFacing.NORTH;

    public KoboldTaskInfo(BlockPos originPos, KoboldTask taskType, HashSet<BlockPos> targetBlocks) {
        this.originPos = originPos;
        this.taskType = taskType;
        this.targetBlocks = targetBlocks;
    }

    public KoboldTaskInfo(BlockPos originPos, KoboldTask taskType, HashSet<BlockPos> targetBlocks, EnumFacing facing) {
        this.originPos = originPos;
        this.taskType = taskType;
        this.targetBlocks = targetBlocks;
        this.facing = facing;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public BlockPos getOriginPos() {
        return this.originPos;
    }

    public KoboldTask getTaskType() {
        return this.taskType;
    }

    public HashSet<BlockPos> getTargetBlocks() {
        return this.targetBlocks;
    }

    public void addBlocks(BlockPos pos) {
        this.targetBlocks.add(pos);
    }

    public void addAllBlocks(HashSet<BlockPos> positions) {
        this.targetBlocks.addAll(positions);
    }

    public void removeBlocks(BlockPos blockPos) {
        this.targetBlocks.remove(blockPos);
    }

    public void removeAllBlocks(HashSet<BlockPos> hashSet) {
        if (!hashSet.isEmpty()) {
            this.targetBlocks.removeAll(hashSet);
        }
    }

    public boolean containsBlock(BlockPos pos) {
        return this.targetBlocks.contains(pos);
    }

    public boolean assignWorker(KoboldEntity kobold) {
        if (this.taskType.getMaxWorkers() <= this.assignedWorkers.size()) {
            return false;
        }
        this.assignedWorkers.add(kobold);
        return true;
    }

    public List<KoboldEntity> getAssignedWorkers() {
        return this.assignedWorkers;
    }

    public void resetAllWorkers() {
        for (KoboldEntity worker : this.assignedWorkers) {
            if (worker.getID() != null) continue;
            worker.setNoGravity(false);
            worker.noClip = false;
            worker.setCurrentAction(Action.NULL);
            worker.getDataManager().set(GirlEntity.IS_ANCHORED, false);
        }
        this.assignedWorkers.clear();
    }

    public void removeWorker(KoboldEntity kobold) {
        this.assignedWorkers.remove(kobold);
    }

    public boolean isFull() {
        return this.taskType.getMaxWorkers() <= this.assignedWorkers.size();
    }

    public boolean hasWorker(KoboldEntity kobold) {
        return this.assignedWorkers.contains(kobold);
    }

//    public static HashSet<BlockPos> createTreeFellingTask(World world, BlockPos startPos, UUID tribeId) {
//        BlockPos basePos = startPos;
//        while (!KoboldTaskInfo.isBaseLog(world, basePos)) {
//            basePos = startPos.down();
//        }
//        BlockPos topPos = startPos;
//        while (!KoboldTaskInfo.isTopLog(world, topPos)) {
//            topPos = topPos.up();
//        }
//        HashSet<BlockPos> treeBlocks = new HashSet<BlockPos>();
//        int height = topPos.getY() - basePos.getY();
//        for (int i = 0; i <= height; ++i) {
//            treeBlocks.add(basePos.add(0, i, 0));
//        }
//
//        HashSet<BlockPos> connectedLogs = KoboldTaskInfo.findConnectedLogs(world, basePos);
//        HashSet<BlockPos> logsToRemove = new HashSet<BlockPos>();
//
//        for (BlockPos pos : connectedLogs) {
//            if (pos.getX() != basePos.getX() || pos.getZ() != basePos.getZ()) continue;
//            logsToRemove.add(pos);
//        }
//        for (BlockPos logs : logsToRemove) {
//            connectedLogs.remove(logs);
//        }
//
//        treeBlocks.addAll(connectedLogs);
//        HashSet hashSet4 = new HashSet();
//        block5: for (BlockPos trees : treeBlocks) {
//            for (KoboldTaskInfo task : KoboldManager.getTribeTasks(tribeId)) {
//                HashSet<BlockPos> targers = task.getTargetBlocks();
//                if (!targers.contains(trees)) continue;
//                hashSet4.add(trees);
//                continue block5;
//            }
//        }
//        treeBlocks.removeAll(hashSet4);
//        KoboldTaskInfo bs_class973 = new KoboldTaskInfo(basePos, KoboldTask.FALL_TREE, treeBlocks);
//        KoboldManager.addTaskToTribe(tribeId, bs_class973);
//        return treeBlocks;
//    }

    public static HashSet<BlockPos> createTreeFellingTask(World world, BlockPos startPos, UUID tribeUUID) {
        // Поиск самого нижнего блока ствола
        BlockPos basePos = startPos;
        while (!isBaseLog(world, basePos)) {
            basePos = basePos.down();
        }

        // Поиск вершины ствола
        BlockPos topPos = startPos;
        while (!isTopLog(world, topPos)) {
            topPos = topPos.up();
        }

        HashSet<BlockPos> treeBlocks = new HashSet<>();
        int height = topPos.getY() - basePos.getY();
        for (int i = 0; i <= height; ++i) {
            treeBlocks.add(basePos.add(0, i, 0));
        }

        // Сканирование прилегающих ветвей/блоков древесины
        HashSet<BlockPos> connectedLogs = findConnectedLogs(world, basePos);
        HashSet<BlockPos> logsToRemove = new HashSet<>();
        for (BlockPos pos : connectedLogs) {
            if (pos.getX() != basePos.getX() || pos.getZ() != basePos.getZ()) {
                continue;
            }
            logsToRemove.add(pos);
        }
        connectedLogs.removeAll(logsToRemove);
        treeBlocks.addAll(connectedLogs);

        // Фильтрация блоков, которые уже задействованы в других задачах племени
        HashSet<BlockPos> alreadyClaimedBlocks = new HashSet<>();
        Collection<KoboldTaskInfo> existingTasks = KoboldManager.getTribeTasks(tribeUUID);

        if (existingTasks != null) {
            for (BlockPos pos : treeBlocks) {
                for (KoboldTaskInfo task : existingTasks) {
                    if (task.getTargetBlocks().contains(pos)) {
                        alreadyClaimedBlocks.add(pos);
                        break;
                    }
                }
            }
        }
        treeBlocks.removeAll(alreadyClaimedBlocks);

        // Регистрируем новую задачу в менеджер племени
        KoboldTaskInfo newTask = new KoboldTaskInfo(basePos, KoboldTask.FALL_TREE, treeBlocks);
        KoboldManager.addTaskToTribe(tribeUUID, newTask);

        return treeBlocks;
    }

    static boolean isTopLog(World world, BlockPos blockPos) {
        Block blockAbove = world.getBlockState(blockPos.up()).getBlock();
        return !(blockAbove instanceof BlockLog);
    }

    static boolean isBaseLog(World world, BlockPos blockPos) {
        IBlockState stateBelow = world.getBlockState(blockPos.down());
        return !(stateBelow instanceof BlockLog) && stateBelow.getMaterial() != Material.AIR;
    }

    static HashSet<BlockPos> findConnectedLogs(World world, BlockPos blockPos) {
        return KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos, new HashSet<BlockPos>());
    }

    static HashSet<BlockPos> findConnectedLogsRecursive(World world, BlockPos blockPos, HashSet<BlockPos> visited) {
        if (visited.contains(blockPos)) {
            return new HashSet<BlockPos>();
        }
        visited.add(blockPos);
        if (world.getBlockState(blockPos.add(1, 0, 0)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(1, 0, 0), visited));
        }
        if (world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(-1, 0, 0), visited));
        }
        if (world.getBlockState(blockPos.add(0, 0, 1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(0, 0, 1), visited));
        }
        if (world.getBlockState(blockPos.add(0, 0, -1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(0, 0, -1), visited));
        }
        if (world.getBlockState(blockPos.add(1, 0, 1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(1, 0, 1), visited));
        }
        if (world.getBlockState(blockPos.add(-1, 0, -1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(-1, 0, -1), visited));
        }
        if (world.getBlockState(blockPos.add(-1, 0, 1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(-1, 0, 1), visited));
        }
        if (world.getBlockState(blockPos.add(1, 0, -1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(1, 0, -1), visited));
        }
        if (world.getBlockState(blockPos.add(0, 1, 0)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(0, 1, 0), visited));
        }
        if (world.getBlockState(blockPos.add(1, 1, 0)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(1, 1, 0), visited));
        }
        if (world.getBlockState(blockPos.add(-1, 1, 0)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(-1, 1, 0), visited));
        }
        if (world.getBlockState(blockPos.add(0, 1, 1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(0, 1, 1), visited));
        }
        if (world.getBlockState(blockPos.add(0, 1, -1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(0, 1, -1), visited));
        }
        if (world.getBlockState(blockPos.add(1, 1, 1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(1, 1, 1), visited));
        }
        if (world.getBlockState(blockPos.add(-1, 1, -1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(-1, 1, -1), visited));
        }
        if (world.getBlockState(blockPos.add(-1, 1, 1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(-1, 1, 1), visited));
        }
        if (world.getBlockState(blockPos.add(1, 1, -1)).getBlock() instanceof BlockLog) {
            visited.addAll(KoboldTaskInfo.findConnectedLogsRecursive(world, blockPos.add(1, 1, -1), visited));
        }
        return visited;
    }

    public static enum KoboldTask {
        FALL_TREE(1),
        MINE(3);

        final int maxWorkers;

        private KoboldTask(int value) {
            this.maxWorkers = value;
        }

        int getMaxWorkers() {
            return this.maxWorkers;
        }
    }
}

