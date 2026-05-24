/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class bs_class97 {
    final static public int d = 30;
    BlockPos a;
    KoboldTask c;
    HashSet<BlockPos> b;
    List<KoboldEntity> f = new ArrayList<KoboldEntity>();
    EnumFacing e = EnumFacing.NORTH;

    public bs_class97(BlockPos blockPos, KoboldTask a_inner982, HashSet<BlockPos> hashSet) {
        this.a = blockPos;
        this.c = a_inner982;
        this.b = hashSet;
    }

    public bs_class97(BlockPos blockPos, KoboldTask a_inner982, HashSet<BlockPos> hashSet, EnumFacing enumFacing) {
        this.a = blockPos;
        this.c = a_inner982;
        this.b = hashSet;
        this.e = enumFacing;
    }

    public EnumFacing f() {
        return this.e;
    }

    public BlockPos b() {
        return this.a;
    }

    public KoboldTask getTaskType() {
        return this.c;
    }

    public HashSet<BlockPos> g() {
        return this.b;
    }

    public void b(BlockPos blockPos) {
        this.b.add(blockPos);
    }

    public void a(HashSet<BlockPos> hashSet) {
        this.b.addAll(hashSet);
    }

    public void a(BlockPos blockPos) {
        this.b.remove(blockPos);
    }

    public void b(HashSet<BlockPos> hashSet) {
        if (!hashSet.isEmpty()) {
            this.b.removeAll(hashSet);
        }
    }

    public boolean c(BlockPos blockPos) {
        return this.b.contains(blockPos);
    }

    public boolean a(KoboldEntity ff_class3082) {
        if (this.c.a <= this.f.size()) {
            return false;
        }
        this.f.add(ff_class3082);
        return true;
    }

    public List<KoboldEntity> c() {
        return this.f;
    }

    public void a() {
        for (KoboldEntity ff_class3082 : this.f) {
            if (ff_class3082.getID() != null) continue;
            ff_class3082.setNoGravity(false);
            ff_class3082.noClip = false;
            ff_class3082.setCurrentAction(Action.NULL);
            ff_class3082.getDataManager().set(GirlEntity.G, false);
        }
        this.f.clear();
    }

    public void c(KoboldEntity ff_class3082) {
        this.f.remove(ff_class3082);
    }

    public boolean e() {
        return this.c.a <= this.f.size();
    }

    public boolean b(KoboldEntity ff_class3082) {
        return this.f.contains(ff_class3082);
    }

    public static HashSet<BlockPos> a(World world, BlockPos blockPos, UUID uUID) {
        BlockPos blockPos2 = blockPos;
        while (!bs_class97.c(world, blockPos2)) {
            blockPos2 = blockPos.down();
        }
        BlockPos blockPos3 = blockPos;
        while (!bs_class97.b(world, blockPos3)) {
            blockPos3 = blockPos3.up();
        }
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        int n = blockPos3.getY() - blockPos2.getY();
        for (int i = 0; i <= n; ++i) {
            hashSet.add(blockPos2.add(0, i, 0));
        }
        HashSet<BlockPos> hashSet2 = bs_class97.a(world, blockPos2);
        HashSet<BlockPos> hashSet3 = new HashSet<BlockPos>();
        for (BlockPos object2 : hashSet2) {
            if (object2.getX() != blockPos2.getX() || object2.getZ() != blockPos2.getZ()) continue;
            hashSet3.add(object2);
        }
        for (BlockPos blockPos4 : hashSet3) {
            hashSet2.remove(blockPos4);
        }
        hashSet.addAll(hashSet2);
        HashSet hashSet4 = new HashSet();
        block5: for (BlockPos blockPos5 : hashSet) {
            for (bs_class97 bs_class972 : KoboldManager.getTribeMembers(uUID)) {
                HashSet<BlockPos> hashSet5 = bs_class972.g();
                if (!hashSet5.contains(blockPos5)) continue;
                hashSet4.add(blockPos5);
                continue block5;
            }
        }
        hashSet.removeAll(hashSet4);
        bs_class97 bs_class973 = new bs_class97(blockPos2, KoboldTask.FALL_TREE, hashSet);
        KoboldManager.b(uUID, bs_class973);
        return hashSet;
    }

    static boolean b(World world, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos.up()).getBlock();
        return !(block instanceof BlockLog);
    }

    static boolean c(World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos.down());
        return !(iBlockState instanceof BlockLog) && iBlockState.getMaterial() != Material.AIR;
    }

    static HashSet<BlockPos> a(World world, BlockPos blockPos) {
        return bs_class97.a(world, blockPos, new HashSet<BlockPos>());
    }

    static HashSet<BlockPos> a(World world, BlockPos blockPos, HashSet<BlockPos> hashSet) {
        if (hashSet.contains(blockPos)) {
            return new HashSet<BlockPos>();
        }
        hashSet.add(blockPos);
        if (world.getBlockState(blockPos.add(1, 0, 0)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(1, 0, 0), hashSet));
        }
        if (world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(-1, 0, 0), hashSet));
        }
        if (world.getBlockState(blockPos.add(0, 0, 1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(0, 0, 1), hashSet));
        }
        if (world.getBlockState(blockPos.add(0, 0, -1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(0, 0, -1), hashSet));
        }
        if (world.getBlockState(blockPos.add(1, 0, 1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(1, 0, 1), hashSet));
        }
        if (world.getBlockState(blockPos.add(-1, 0, -1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(-1, 0, -1), hashSet));
        }
        if (world.getBlockState(blockPos.add(-1, 0, 1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(-1, 0, 1), hashSet));
        }
        if (world.getBlockState(blockPos.add(1, 0, -1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(1, 0, -1), hashSet));
        }
        if (world.getBlockState(blockPos.add(0, 1, 0)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(0, 1, 0), hashSet));
        }
        if (world.getBlockState(blockPos.add(1, 1, 0)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(1, 1, 0), hashSet));
        }
        if (world.getBlockState(blockPos.add(-1, 1, 0)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(-1, 1, 0), hashSet));
        }
        if (world.getBlockState(blockPos.add(0, 1, 1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(0, 1, 1), hashSet));
        }
        if (world.getBlockState(blockPos.add(0, 1, -1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(0, 1, -1), hashSet));
        }
        if (world.getBlockState(blockPos.add(1, 1, 1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(1, 1, 1), hashSet));
        }
        if (world.getBlockState(blockPos.add(-1, 1, -1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(-1, 1, -1), hashSet));
        }
        if (world.getBlockState(blockPos.add(-1, 1, 1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(-1, 1, 1), hashSet));
        }
        if (world.getBlockState(blockPos.add(1, 1, -1)).getBlock() instanceof BlockLog) {
            hashSet.addAll(bs_class97.a(world, blockPos.add(1, 1, -1), hashSet));
        }
        return hashSet;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static enum KoboldTask {
        FALL_TREE(1),
        MINE(3);

        final int a;

        private KoboldTask(int n2) {
            this.a = n2;
        }

        int a() {
            return this.a;
        }
    }
}

