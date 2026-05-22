/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class an_class35 {
    final static public float a = 9.81f;
    final static public float g = 0.05f;
    final static public float b = 0.05f;
    final static public float c = 0.03f;
    World h;
    Vec3d d;
    Vec3d f;
    Vec3d e;

    public an_class35(World world, Vec3d vec3d, Vec3d vec3d2) {
        this.h = world;
        this.f = vec3d;
        this.d = vec3d;
        this.e = vec3d2;
    }

    public void a() {
        int n;
        int n2;
        int n3;
        if (Vec3d.ZERO.equals(this.e)) {
            this.d = this.f;
            return;
        }
        this.e = new Vec3d(this.e.x * (double)0.95f, (this.e.y - 0.4905000329017639) * (double)0.95f, this.e.z * (double)0.95f);
        this.d = this.f;
        this.f = new Vec3d(this.f.x + this.e.x * (double)0.05f, this.f.y + this.e.y * (double)0.05f, this.f.z + this.e.z * (double)0.05f);
        BlockPos blockPos = new BlockPos(this.d);
        Vec3i vec3i = null;
        for (BlockPos blockPos2 : an_class35.a(new BlockPos(this.d), new BlockPos(this.f))) {
            if (this.h.getBlockState(blockPos2).getBlock() == Blocks.AIR) {
                blockPos = blockPos2;
                continue;
            }
            vec3i = blockPos2;
            break;
        }
        if (vec3i == null) {
            return;
        }
        int n4 = vec3i.getX();
        if (n4 - (n3 = blockPos.getX()) != 0) {
            double d = Math.max(n4, n3);
            double d2 = (this.d.y - this.f.y) / (this.d.x - this.f.x);
            double d3 = this.f.y - d2 * this.f.x;
            double d4 = d2 * d + d3;
            double d5 = (this.d.z - this.f.z) / (this.d.x - this.f.x);
            double d6 = this.f.z - d5 * this.f.x;
            double d7 = d5 * d + d6;
            this.f = new Vec3d(d + (double)(0.03f * (float)(n4 > n3 ? -1 : 1)), d4, d7);
            this.e = new Vec3d(0.0, 0.0, 0.0);
            return;
        }
        int n5 = vec3i.getY();
        if (n5 - (n2 = blockPos.getY()) != 0) {
            double d = Math.max(n5, n2);
            double d8 = (this.d.x - this.f.x) / (this.d.y - this.f.y);
            double d9 = this.f.x - d8 * this.f.y;
            double d10 = d8 * d + d9;
            double d11 = (this.d.z - this.f.z) / (this.d.y - this.f.y);
            double d12 = this.f.z - d11 * this.f.y;
            double d13 = d11 * d + d12;
            this.f = new Vec3d(d10, d + (double)(0.03f * (float)(n5 > n2 ? -1 : 1)), d13);
            this.e = new Vec3d(0.0, 0.0, 0.0);
            return;
        }
        int n6 = vec3i.getZ();
        if (n6 - (n = blockPos.getZ()) != 0) {
            double d = Math.max(n6, n);
            double d14 = (this.d.y - this.f.y) / (this.d.z - this.f.z);
            double d15 = this.f.y - d14 * this.f.z;
            double d16 = d14 * d + d15;
            double d17 = (this.d.x - this.f.x) / (this.d.z - this.f.z);
            double d18 = this.f.x - d17 * this.f.z;
            double d19 = d17 * d + d18;
            this.f = new Vec3d(d19, d16, d + (double)(0.03f * (float)(n6 > n ? -1 : 1)));
            this.e = new Vec3d(0.0, 0.0, 0.0);
            return;
        }
    }

    static List<BlockPos> a(BlockPos blockPos, BlockPos blockPos2) {
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        arrayList.add(blockPos);
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        int n4 = blockPos2.getX();
        int n5 = blockPos2.getY();
        int n6 = blockPos2.getZ();
        int n7 = Math.abs(n4 - n);
        int n8 = Math.abs(n5 - n2);
        int n9 = Math.abs(n6 - n3);
        int n10 = n < n4 ? 1 : -1;
        int n11 = n2 < n5 ? 1 : -1;
        int n12 = n3 < n6 ? 1 : -1;
        int n13 = Math.max(n7, Math.max(n8, n9));
        int n14 = n;
        int n15 = n2;
        int n16 = n3;
        int n17 = n13 / 2;
        int n18 = n13 / 2;
        int n19 = n13 / 2;
        for (int i = 0; i < n13; ++i) {
            arrayList.add(new BlockPos(n14, n15, n16));
            n18 -= n8;
            n19 -= n9;
            if ((n17 -= n7) < 0) {
                n14 += n10;
                n17 += n13;
                continue;
            }
            if (n18 < 0) {
                n15 += n11;
                n18 += n13;
                continue;
            }
            if (n19 >= 0) continue;
            n16 += n12;
            n19 += n13;
        }
        arrayList.add(blockPos2);
        return arrayList;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

