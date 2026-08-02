/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.IWorldGenerator
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.trolmastercard.sexmod.Point2D;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGenStructure extends WorldSavedData implements IWorldGenerator {
    final static String j = "sexmod:generation";
    final static int h = 156;
    final static int a = 62;
    final static int b = 6;
    final double f = 0.004f;
    static public boolean i = true;
    final List<genStructure> e = new ArrayList<genStructure>();
    final List<a_inner345> d = new ArrayList<a_inner345>();
    static private WorldGenStructure worldGenStructure = null;
    static boolean c = true;

    public static WorldGenStructure Generate() {
        if (worldGenStructure == null) {
            worldGenStructure = new WorldGenStructure();
        }
        return worldGenStructure;
    }

    public WorldGenStructure(String string) {
        this();
    }

    private WorldGenStructure() {
        super(j);
        worldGenStructure = this;
        this.e.add(new genStructure("ellie", new HashSet<Biome>(Arrays.asList(Biomes.REDWOOD_TAIGA, Biomes.COLD_TAIGA, Biomes.TAIGA, Biomes.ROOFED_FOREST)), new Vec3i(30, 27, 26), 9, true));
        this.e.add(new genStructure("jenny", new HashSet<Biome>(Arrays.asList(Biomes.PLAINS, Biomes.FOREST)), new Vec3i(9, 4, 9), 1, true));
        this.e.add(new genStructure("ellie", new HashSet<Biome>(Arrays.asList(Biomes.REDWOOD_TAIGA, Biomes.COLD_TAIGA, Biomes.TAIGA, Biomes.ROOFED_FOREST)), new Vec3i(30, 27, 26), 9, true));
        this.e.add(new genStructure("bia", new HashSet<Biome>(Arrays.asList(Biomes.MUTATED_BIRCH_FOREST, Biomes.BIRCH_FOREST)), new Vec3i(11, 9, 15), 2, true));
        this.e.add(new genStructure("luna", new HashSet<Biome>(Arrays.asList(Biomes.OCEAN, Biomes.DEEP_OCEAN)), new Vec3i(3, 7, 10), 0, false));
    }

    public void clear() {
        this.d.clear();
    }

    @SubscribeEvent
    public void a(WorldEvent.Save save) {
        World world = save.getWorld();
        world.getMapStorage().setData(j, this);
        this.markDirty();
    }

    @SubscribeEvent
    public void a(WorldEvent.Load load) {
        World world = load.getWorld();
        world.getMapStorage().getOrLoadData(WorldGenStructure.class, j);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.clear();
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag(j);
        int n = 0;
        while (true) {
            String string = nBTTagCompound2.getString("sexmod:name" + n);
            String string2 = nBTTagCompound2.getString("sexmod:pos" + n);
            if (string.isEmpty() || string2.isEmpty()) break;
            this.d.add(new a_inner345(WorldGenStructure.a(string2), string));
            ++n;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setTag(j, new NBTTagCompound());
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        int n = 0;
        for (a_inner345 a_inner3452 : this.d) {
            nBTTagCompound2.setString("sexmod:name" + n, a_inner3452.a);
            nBTTagCompound2.setString("sexmod:pos" + n++, WorldGenStructure.a(a_inner3452.b));
        }
        nBTTagCompound.setTag(j, nBTTagCompound2);
        return nBTTagCompound;
    }

    static String a(Point2D point) {
        return point.x + "|" + point.y;
    }

    static Point2D a(String string) {
        String[] stringArray = string.split("\\|");
        return new Point2D(Integer.parseInt(stringArray[0]), Integer.parseInt(stringArray[1]));
    }

    public void generate(Random random, int n, int n2, World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider) {
        if (!i) {
            return;
        }
        if (world.getWorldType() == WorldType.FLAT) {
            return;
        }
        this.b(world, random, n, n2);
        this.a(world, random, n, n2);
        this.a(random, n, n2, world);
    }

    void a(Random random, int n, int n2, World world) {
        if (!c) {
            return;
        }
        c = false;
        for (genStructure b_inner3462 : this.e) {
            this.a(b_inner3462, random, n, n2, world);
        }
        c = true;
    }

    void a(genStructure b_inner3462, Random random, int n, int n2, World world) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        for (a_inner345 a_inner3452 : this.d) {
            int n9 = n8 = a_inner3452.a.equals(b_inner3462.f) ? 156 : 62;
            if (!(a_inner3452.b.distanceTo(n, n2) < (float)n8)) continue;
            return;
        }
        int n10 = b_inner3462.c.getX();
        n8 = n * 16 + (16 - n10) / 2;
        Biome biome = world.provider.getBiomeForCoords(new BlockPos(n8, 80, n7 = n2 * 16 + (16 - (n6 = b_inner3462.c.getZ())) / 2));
        if (!b_inner3462.biomes.contains(biome)) {
            return;
        }
        int n11 = Integer.MIN_VALUE;
        int n12 = Integer.MAX_VALUE;
        for (n5 = n8; n5 < n8 + n10; ++n5) {
            for (n4 = n7; n4 < n7 + n6; ++n4) {
                n3 = WorldUtils.getSurfaceHeight(world, n5, n4);
                if (b_inner3462.d && world.getBlockState(new BlockPos(n5, n3, n4)).getBlock() == Blocks.WATER) {
                    return;
                }
                if (n3 > n11) {
                    n11 = n3;
                }
                if (n3 >= n12) continue;
                n12 = n3;
            }
        }
        if (n11 - n12 > b_inner3462.a) {
            return;
        }
        n5 = n11;
        this.d.add(new a_inner345(new Point2D(n, n2), b_inner3462.f));
        b_inner3462.b.generate(world, random, new BlockPos(n8, n5, n7));
        if (!b_inner3462.d) {
            return;
        }
        n4 = 1;
        n3 = n5 - 1;
        while (n4 != 0) {
            n4 = 0;
            Vec3i vec3i = new Vec3i(n10 + 2, 0, n6 + 2);
            --n7;
            for (int i = --n8; i < n8 + vec3i.getX(); ++i) {
                for (int j = n7; j < n7 + vec3i.getZ(); ++j) {
                    BlockPos blockPos = new BlockPos(i, n3, j);
                    IBlockState iBlockState = world.getBlockState(blockPos);
                    if (!iBlockState.getBlock().isPassable(world, blockPos)) continue;
                    iBlockState = world.canSeeSky(blockPos) ? Blocks.GRASS.getDefaultState() : Blocks.DIRT.getDefaultState();
                    world.setBlockState(blockPos, iBlockState);
                    n4 = 1;
                }
            }
            --n3;
        }
    }

    void b(World world, Random random, int n, int n2) {
        if (random.nextDouble() > (double)0.004f) {
            return;
        }
        int n3 = n * 16 + 8;
        int n4 = n2 * 16 + 8;
        int n5 = WorldUtils.getSurfaceHeight(world, n3, n4);
        if (world.getBlockState(new BlockPos(n3, n5, n4)).getMaterial().isLiquid()) {
            return;
        }
        KoboldManager.spawnTribe(world, new Vec3d(n3, n5, n4));
    }

    /*
     * WARNING - void declaration
     */
    void a(World var1, Random var2, int var3, int var4) {
        int var5 = 16 * var3 + 3;
        int var6 = 16 * var4 + 3;
        int var7 = var2.nextInt(255);
        BlockPos var8 = new BlockPos(var5, var7, var6);
        ArrayList<BlockPos> var9 = new ArrayList<>();

        for(int var10 = 0; var10 <= GoblinEntity.ah.getX(); ++var10) {
            for(int var11 = -1; var11 <= GoblinEntity.ah.getY(); ++var11) {
                for(int var12 = 0; var12 <= GoblinEntity.ah.getZ(); ++var12) {
                    BlockPos var13 = var8.add(var10, var11, var12);
                    Material var14 = var1.getBlockState(var13).getMaterial();
                    boolean var15 = var14.isSolid();
                    if (!var15 && (var11 == -1 || var11 == GoblinEntity.ah.getY())) {
                        return;
                    }

                    if ((var10 == 0 || var10 == GoblinEntity.ah.getX() || var12 == 0 || var12 == GoblinEntity.ah.getZ()) && var11 == 0 && var1.isAirBlock(var13) && var1.isAirBlock(var13.up())) {
                        var9.add(var13);
                    }
                }
            }
        }

        if (!var9.isEmpty() && var9.size() <= 4) {
            BlockPos var16 = null;

            for(BlockPos var19 : var9) {
                BlockPos var24 = var8.add(6, 0, 6);
                BlockPos var22 = var19.subtract(var24);
                if (Math.abs(var22.getX()) != Math.abs(var22.getZ()) && Math.abs(var22.getX()) != Math.abs(var22.getZ()) - 1 && Math.abs(var22.getX()) - 1 != Math.abs(var22.getZ())) {
                    var16 = var22;
                    break;
                }
            }

            if (var16 != null) {
                Vec3i var23 = new Vec3i(0, 0, 0);
                float var25 = 0.0F;
                Rotation var18;
                Vec3d var20;
                if (var16.getZ() == -6) {
                    var18 = Rotation.NONE;
                    var20 = GoblinEntity.aB;
                    var25 = 180.0F;
                } else if (var16.getX() == 5) {
                    var18 = Rotation.CLOCKWISE_90;
                    var20 = GoblinEntity.ao;
                    var23 = new Vec3i(GoblinEntity.ah.getX() - 1, 0, 0);
                    var25 = -90.0F;
                } else if (var16.getZ() == 5) {
                    var18 = Rotation.CLOCKWISE_180;
                    var20 = GoblinEntity.aM;
                    var23 = new Vec3i(GoblinEntity.ah.getX() - 1, 0, GoblinEntity.ah.getZ() - 1);
                } else {
                    var18 = Rotation.COUNTERCLOCKWISE_90;
                    var20 = GoblinEntity.U;
                    var23 = new Vec3i(0, 0, GoblinEntity.ah.getZ() - 1);
                    var25 = 90.0F;
                }

                (new WorldGenCustomStructure("goblin")).generateStructureRotated(var1, var8.add(0, -1, 0).add(var23), var18);
                var20.add((double)var23.getX(), (double)var23.getY(), (double)var23.getZ());
                var20 = new Vec3d((double)var8.getX() + var20.x + 0.5, (double)var8.getY() + var20.y, (double)var8.getZ() + var20.z + 0.5);
                GoblinEntity var26 = new GoblinEntity(var1, true, var25, var20);
                var26.forceSpawn = true;
                var1.spawnEntity(var26);
                var1.getChunk(var3, var4).markDirty();
            }
        }
    }

    static class a_inner345 {
        Point2D b;
        String a;

        public a_inner345(Point2D e1_class2172, String string) {
            this.b = e1_class2172;
            this.a = string;
        }
    }

    static class genStructure {
        final public String f;
        final public WorldGenCustomStructure b;
        final public HashSet<Biome> biomes;
        final public Vec3i c;
        final public boolean d;
        final public int a;

        public genStructure(String string, HashSet<Biome> biome, Vec3i vec3i, int n, boolean bl) {
            this.f = string;
            this.biomes = biome;
            this.c = vec3i;
            this.d = bl;
            this.a = n;
            this.b = new WorldGenCustomStructure(string);
        }
    }
}

