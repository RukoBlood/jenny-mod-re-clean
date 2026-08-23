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

import com.trolmastercard.sexmod.util.Point2D;
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

public class ConfigWorldGenHandler extends WorldSavedData implements IWorldGenerator {
    final static String DATA_NAME = "sexmod:generation";
    //final static int h = 156;
    //final static int a = 62;
    //final static int b = 6;
    //final double f = 0.004f;
    static public boolean GENERATION_ENABLED = true;
    final List<genStructure> structureConfig = new ArrayList<genStructure>();
    final List<StructureData> generatedPositions = new ArrayList<StructureData>();
    static private ConfigWorldGenHandler INSTANCE = null;
    static boolean IS_GENERATING = true;

    public static ConfigWorldGenHandler Generate() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigWorldGenHandler();
        }
        return INSTANCE;
    }

    public ConfigWorldGenHandler(String name) {
        this();
    }

    private ConfigWorldGenHandler() {
        super(DATA_NAME);
        INSTANCE = this;
        this.structureConfig.add(new genStructure("ellie", new HashSet<Biome>(Arrays.asList(Biomes.REDWOOD_TAIGA, Biomes.COLD_TAIGA, Biomes.TAIGA, Biomes.ROOFED_FOREST)), new Vec3i(30, 27, 26), 9, true));
        this.structureConfig.add(new genStructure("jenny", new HashSet<Biome>(Arrays.asList(Biomes.PLAINS, Biomes.FOREST)), new Vec3i(9, 4, 9), 1, true));
        this.structureConfig.add(new genStructure("ellie", new HashSet<Biome>(Arrays.asList(Biomes.REDWOOD_TAIGA, Biomes.COLD_TAIGA, Biomes.TAIGA, Biomes.ROOFED_FOREST)), new Vec3i(30, 27, 26), 9, true));
        this.structureConfig.add(new genStructure("bia", new HashSet<Biome>(Arrays.asList(Biomes.MUTATED_BIRCH_FOREST, Biomes.BIRCH_FOREST)), new Vec3i(11, 9, 15), 2, true));
        this.structureConfig.add(new genStructure("luna", new HashSet<Biome>(Arrays.asList(Biomes.OCEAN, Biomes.DEEP_OCEAN)), new Vec3i(3, 7, 10), 0, false));
    }

    public void clear() {
        this.generatedPositions.clear();
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        World world = event.getWorld();
        world.getMapStorage().setData(DATA_NAME, this);
        this.markDirty();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        world.getMapStorage().getOrLoadData(ConfigWorldGenHandler.class, DATA_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.clear();
        NBTTagCompound tag = nbt.getCompoundTag(DATA_NAME);
        int i = 0;
        while (true) {
            String name = tag.getString("sexmod:name" + i);
            String pos = tag.getString("sexmod:pos" + i);
            if (name.isEmpty() || pos.isEmpty()) break;
            this.generatedPositions.add(new StructureData(parseSpawnEntry(pos), name));
            ++i;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag(DATA_NAME, new NBTTagCompound());
        NBTTagCompound tag = new NBTTagCompound();
        int n = 0;
        for (StructureData data : this.generatedPositions) {
            tag.setString("sexmod:name" + n, data.girlName);
            tag.setString("sexmod:pos" + n++, ConfigWorldGenHandler.getChunkHash(data.pos));
        }
        nbt.setTag(DATA_NAME, tag);
        return nbt;
    }

    static String getChunkHash(Point2D pos) {
        return pos.x + "|" + pos.y;
    }

    static Point2D parseSpawnEntry(String string) {
        String[] split = string.split("\\|");
        return new Point2D(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider) {
        if (GENERATION_ENABLED) {
            if (world.getWorldType() != WorldType.FLAT) {
                this.spawnStructures(world, random, chunkX, chunkZ);
                this.generateChunk(world, random, chunkX, chunkZ);
                this.generateStructure(random, chunkX, chunkZ, world);
            }
        }
    }

    void generateStructure(Random random, int chunkX, int chunkZ, World world) {
        if (IS_GENERATING) {
            IS_GENERATING = false;
            for (genStructure structure : this.structureConfig) {
                this.placeStructure(structure, random, chunkX, chunkZ, world);
            }
            IS_GENERATING = true;
        }
    }

    void placeStructure(genStructure rule, Random random, int chunkX, int chunkZ, World world) {
        int height;
        int z;
        int x;
        int sizeZ;
        int startZ;
        int minDistance;

        for (StructureData data : this.generatedPositions) {
            minDistance = data.girlName.equals(rule.girlName) ? 156 : 62;
            if (data.pos.distanceTo(chunkX, chunkZ) < (float) minDistance) {
                return;
            }
        }

        int sizeX = rule.size.getX();
        minDistance = chunkX * 16 + (16 - sizeX) / 2;
        Biome biome = world.provider.getBiomeForCoords(new BlockPos(minDistance, 80, startZ = chunkZ * 16 + (16 - (sizeZ = rule.size.getZ())) / 2));

        if (rule.biomes.contains(biome)) {
            int maxHeight = Integer.MIN_VALUE;
            int minHeight = Integer.MAX_VALUE;
            for (x = minDistance; x < minDistance + sizeX; ++x) {
                for (z = startZ; z < startZ + sizeZ; ++z) {
                    height = WorldUtils.getHeightAt(world, x, z);
                    if (rule.flattenGround && world.getBlockState(new BlockPos(x, height, z)).getBlock() == Blocks.WATER) {
                        return;
                    }
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                    if (height < minHeight) {
                        minHeight = height;
                    }
                }
            }
            if (maxHeight - minHeight <= rule.maxHeightDiff) {
                x = maxHeight;
                this.generatedPositions.add(new StructureData(new Point2D(chunkX, chunkZ), rule.girlName));
                rule.generator.generate(world, random, new BlockPos(minDistance, x, startZ));
                if (!rule.flattenGround) {
                    return;
                }
                z = 1;
                height = x - 1;
                while (z != 0) {
                    z = 0;
                    Vec3i vec3i = new Vec3i(sizeX + 2, 0, sizeZ + 2);
                    --startZ;
                    for (int i = --minDistance; i < minDistance + vec3i.getX(); ++i) {
                        for (int j = startZ; j < startZ + vec3i.getZ(); ++j) {
                            BlockPos blockPos = new BlockPos(i, height, j);
                            IBlockState state = world.getBlockState(blockPos);
                            if (state.getBlock().isPassable(world, blockPos)) {
                                state = world.canSeeSky(blockPos) ? Blocks.GRASS.getDefaultState() : Blocks.DIRT.getDefaultState();
                                world.setBlockState(blockPos, state);
                                z = 1;
                            }
                        }
                    }
                    --height;
                }
            }
        }
    }

    void spawnStructures(World world, Random random, int chunkX, int chunkZ) {
        if (!(random.nextDouble() > (double) 0.004f)) {
            int x = chunkX * 16 + 8;
            int z = chunkZ * 16 + 8;
            int y = WorldUtils.getHeightAt(world, x, z);
            if (!world.getBlockState(new BlockPos(x, y, z)).getMaterial().isLiquid()) {
                KoboldManager.spawnTribe(world, new Vec3d(x, y, z));
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    void generateChunk(World world, Random random, int chunkX, int chunkZ) {
        int x = 16 * chunkX + 3;
        int z = 16 * chunkZ + 3;
        int y = random.nextInt(255);
        BlockPos origin = new BlockPos(x, y, z);
        ArrayList<BlockPos> spots = new ArrayList<>();

        for(int x2 = 0; x2 <= GoblinEntity.BREEDING_AREA_SIZE.getX(); ++x2) {
            for(int y2 = -1; y2 <= GoblinEntity.BREEDING_AREA_SIZE.getY(); ++y2) {
                for(int z2 = 0; z2 <= GoblinEntity.BREEDING_AREA_SIZE.getZ(); ++z2) {
                    BlockPos pos = origin.add(x2, y2, z2);
                    Material mat = world.getBlockState(pos).getMaterial();
                    boolean solid = mat.isSolid();
                    if (solid || (y2 != -1 && y2 != GoblinEntity.BREEDING_AREA_SIZE.getY())) {
                        if ((x2 == 0 || x2 == GoblinEntity.BREEDING_AREA_SIZE.getX() || z2 == 0 || z2 == GoblinEntity.BREEDING_AREA_SIZE.getZ()) && y2 == 0 && world.isAirBlock(pos) && world.isAirBlock(pos.up())) {
                            spots.add(pos);
                        }
                    }
                }
            }
        }

        if (!spots.isEmpty() && spots.size() <= 4) {
            BlockPos candidate = null;

            for(BlockPos spot : spots) {
                BlockPos center = origin.add(6, 0, 6);
                BlockPos relative = spot.subtract(center);
                if (Math.abs(relative.getX()) != Math.abs(relative.getZ()) && Math.abs(relative.getX()) != Math.abs(relative.getZ()) - 1 && Math.abs(relative.getX()) - 1 != Math.abs(relative.getZ())) {
                    candidate = relative;
                    break;
                }
            }

            if (candidate != null) {
                Vec3i offset = new Vec3i(0, 0, 0);
                float yaw = 0.0F;
                Rotation rotation;
                Vec3d offsetPos;
                if (candidate.getZ() == -6) {
                    rotation = Rotation.NONE;
                    offsetPos = GoblinEntity.OFFSET_SOUTH;
                    yaw = 180.0F;
                } else if (candidate.getX() == 5) {
                    rotation = Rotation.CLOCKWISE_90;
                    offsetPos = GoblinEntity.OFFSET_WEST;
                    offset = new Vec3i(GoblinEntity.BREEDING_AREA_SIZE.getX() - 1, 0, 0);
                    yaw = -90.0F;
                } else if (candidate.getZ() == 5) {
                    rotation = Rotation.CLOCKWISE_180;
                    offsetPos = GoblinEntity.OFFSET_NORTH;
                    offset = new Vec3i(GoblinEntity.BREEDING_AREA_SIZE.getX() - 1, 0, GoblinEntity.BREEDING_AREA_SIZE.getZ() - 1);
                } else {
                    rotation = Rotation.COUNTERCLOCKWISE_90;
                    offsetPos = GoblinEntity.THROW_OFFSET_U;
                    offset = new Vec3i(0, 0, GoblinEntity.BREEDING_AREA_SIZE.getZ() - 1);
                    yaw = 90.0F;
                }

                (new WorldGenCustomStructure("goblin")).generateStructureRotated(world, origin.add(0, -1, 0).add(offset), rotation);
                offsetPos.add(offset.getX(), offset.getY(), offset.getZ());
                offsetPos = new Vec3d((double)origin.getX() + offsetPos.x + 0.5, (double)origin.getY() + offsetPos.y, (double)origin.getZ() + offsetPos.z + 0.5);
                GoblinEntity goblin = new GoblinEntity(world, true, yaw, offsetPos);
                goblin.forceSpawn = true;
                world.spawnEntity(goblin);
                world.getChunk(chunkX, chunkZ).markDirty();
            }
        }
    }

    static class StructureData {
        Point2D pos;
        String girlName;

        public StructureData(Point2D pos, String name) {
            this.pos = pos;
            this.girlName = name;
        }
    }

    static class genStructure {
        final public String girlName;
        final public WorldGenCustomStructure generator;
        final public HashSet<Biome> biomes;
        final public Vec3i size;
        final public boolean flattenGround;
        final public int maxHeightDiff;

        public genStructure(String name, HashSet<Biome> biomes, Vec3i size, int maxHeightDiff, boolean flattenGround) {
            this.girlName = name;
            this.biomes = biomes;
            this.size = size;
            this.flattenGround = flattenGround;
            this.maxHeightDiff = maxHeightDiff;
            this.generator = new WorldGenCustomStructure(name);
        }
    }
}

