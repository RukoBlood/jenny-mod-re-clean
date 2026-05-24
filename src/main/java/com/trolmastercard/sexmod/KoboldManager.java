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
package com.trolmastercard.sexmod;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
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
    final static int d = 4;
    final static private HashMap<UUID, b_inner50.a_inner49> c = new HashMap<>();
    final static Vec3d[] b = new Vec3d[]{new Vec3d(0.0, 0.0, 0.0), new Vec3d(0.5, 0.0, 0.0), new Vec3d(-0.5, 0.0, 0.0), new Vec3d(0.0, 0.0, 0.5), new Vec3d(0.0, 0.0, -0.5)};
    static HashMap<KoboldEntity, BlockPos[]> a = new HashMap<>();

    public static void clear() {
        c.clear();
        a.clear();
    }

    public static void a(World world, Vec3d vec3d) {
        UUID uUID = UUID.randomUUID();
        float[] fArray = new float[4];
        fArray[0] = 0.25f;
        for (int i = 1; i < fArray.length; ++i) {
            fArray[i] = KoboldEntity.float_j();
        }
        ArrayList<KoboldEntity> arrayList = new ArrayList<KoboldEntity>();
        for (float f : fArray) {
            KoboldEntity ff_class3082 = KoboldEntity.a(world, uUID, f);
            arrayList.add(ff_class3082);
        }
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.values()[Reference.RANDOM.nextInt(EyeAndKoboldColor.values().length)];
        b_inner50.a_inner49 a_inner492 = new b_inner50.a_inner49(uUID, eyeAndKoboldColor_, (KoboldEntity) arrayList.get(0), arrayList);
        c.put(uUID, a_inner492);
        int n = 0;
        for (KoboldEntity ff_class3082 : arrayList) {
            ff_class3082.setPosition(vec3d.x + KoboldManager.b[n].x, vec3d.y, vec3d.z + KoboldManager.b[n].z);
            world.spawnEntity(ff_class3082);
            ++n;
        }
    }

    public static boolean o(UUID uUID) {
        return c.get(uUID) != null;
    }

    public static void a(UUID uUID, UUID uUID2) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            return;
        }
        a_inner492.a(uUID2);
    }

    public static void a(UUID uUID, EyeAndKoboldColor eyeAndKoboldColor_) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 != null) {
            System.out.println("tribe of UUID " + uUID.toString() + " does already exist lol");
            return;
        }
        c.put(uUID, new b_inner50.a_inner49(uUID, eyeAndKoboldColor_));
    }

    public static boolean a(BlockPos blockPos) {
        for (Map.Entry<KoboldEntity, BlockPos[]> entry : a.entrySet()) {
            BlockPos[] blockPosArray = entry.getValue();
            if (blockPosArray[0].equals(blockPos)) {
                return true;
            }
            if (!blockPosArray[1].equals(blockPos)) continue;
            return true;
        }
        return false;
    }

    public static BlockPos[] a(KoboldEntity ff_class3082) {
        return a.get(ff_class3082);
    }

    public static void a(KoboldEntity ff_class3082, BlockPos blockPos) {
        World world = ff_class3082.world;
        BlockPos blockPos2 = null;
        if (world.getBlockState(blockPos.north()).getBlock() instanceof BlockBed) {
            blockPos2 = blockPos.north();
        }
        if (world.getBlockState(blockPos.east()).getBlock() instanceof BlockBed) {
            blockPos2 = blockPos.east();
        }
        if (world.getBlockState(blockPos.south()).getBlock() instanceof BlockBed) {
            blockPos2 = blockPos.south();
        }
        if (world.getBlockState(blockPos.west()).getBlock() instanceof BlockBed) {
            blockPos2 = blockPos.west();
        }
        if (blockPos2 == null) {
            System.out.println("bed @" + blockPos.toString() + " apparently doesn't have another half.. wtf");
            return;
        }
        a.put(ff_class3082, new BlockPos[]{blockPos, blockPos2});
    }

    public static void b(KoboldEntity ff_class3082) {
        a.remove(ff_class3082);
    }

    public static void d(UUID uUID, KoboldEntity ff_class3082) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.g = ff_class3082;
    }

    public static void c(UUID uUID, KoboldEntity ff_class3082) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(ff_class3082);
        c.replace(uUID, a_inner492);
        ff_class3082.getDataManager().set(KoboldEntity.aL, Optional.of(uUID));
        if (!ff_class3082.aA) {
            ff_class3082.getDataManager().set(KoboldEntity.N, a_inner492.h.toString());
        }
    }

    public static void k(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        KoboldEntity ff_class3082 = a_inner492.g;
        if (ff_class3082 == null || ff_class3082.isDead) {
            a_inner492.g = a_inner492.b();
        }
    }

    public static void a(UUID uUID, KoboldEntity ff_class3082) {
        //Object object;
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.b(ff_class3082);
        a_inner492.b(ff_class3082.girlID());
        {
            KoboldEntity object = null;
            if ((object = a_inner492.b()) != null) {
                if (a_inner492.g != null && a_inner492.g.getEntityId() == ff_class3082.getEntityId()) {
                    a_inner492.g = object;
                }
            }
        }
        for (bs_class97 object2 : a_inner492.f) {
            object2.c(ff_class3082);
        }
        if (!a_inner492.a.isEmpty()) {
            c.replace(uUID, a_inner492);
            return;
        }
        if (!ff_class3082.boolean_J()) {
            return;
        }
        EntityPlayer object = ff_class3082.net_minecraft_entity_player_EntityPlayer_z();
        if (object != null) {
            HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
            hashSet.addAll(a_inner492.i);
            hashSet.addAll(a_inner492.b);
            for (bs_class97 bs_class972 : a_inner492.f) {
                hashSet.addAll(bs_class972.b);
            }
            NetworkRegistry.networkWrapper.sendTo((IMessage) new h6_class397(hashSet, false), (EntityPlayerMP) object);
            ((Entity) object).sendMessage(new TextComponentString(String.format("ur %stribe %shas been %seradicated %suwu", new Object[]{TextFormatting.RED, TextFormatting.WHITE, TextFormatting.RED, TextFormatting.WHITE})));
        }
    }

    @Nullable
    public static KoboldEntity f(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return a_inner492.g;
    }

    public static boolean e(UUID uUID, KoboldEntity ff_class3082) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        if (a_inner492.g == null) {
            return false;
        }
        return a_inner492.g.getEntityId() == ff_class3082.getEntityId();
    }

    public static EyeAndKoboldColor l(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return KoboldEntity.KoboldColor;
        }
        return a_inner492.h;
    }

    public static HashSet<BlockPos> j(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<BlockPos>();
        }
        return a_inner492.b;
    }

    public static void a(UUID uUID, BlockPos blockPos) {
        if (blockPos == null) {
            return;
        }
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.b.add(blockPos);
    }

    public static void e(UUID uUID, BlockPos blockPos) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.b.remove(blockPos);
    }

    public static HashSet<BlockPos> q(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return a_inner492.i;
    }

    public static void f(UUID uUID, BlockPos blockPos) {
        if (blockPos == null) {
            return;
        }
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.i.add(blockPos);
    }

    public static void d(UUID uUID, BlockPos blockPos) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.i.remove(blockPos);
    }

    public static HashSet<BlockPos> a(UUID uUID, bs_class97 bs_class972) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<BlockPos>();
        }
        if (bs_class972 != null) {
            a_inner492.b(bs_class972);
            return bs_class972.b;
        }
        return new HashSet<BlockPos>();
    }

    public static HashSet<BlockPos> c(UUID uUID, BlockPos blockPos) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<BlockPos>();
        }
        bs_class97 bs_class972 = null;
        for (bs_class97 bs_class973 : a_inner492.f) {
            if (!bs_class973.b.contains(blockPos)) continue;
            bs_class972 = bs_class973;
            break;
        }
        return KoboldManager.a(uUID, bs_class972);
    }

    public static void b(UUID uUID, bs_class97 bs_class972) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(bs_class972);
    }

    public static void b(UUID uUID, KoboldEntity ff_class3082) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        bs_class97 bs_class972 = null;
        for (bs_class97 bs_class973 : a_inner492.f) {
            if (!bs_class973.b(ff_class3082)) continue;
            bs_class972 = bs_class973;
        }
        if (bs_class972 == null) {
            System.out.println("task of worker " + ff_class3082.girlID() + " not found uwu");
            return;
        }
        a_inner492.b(bs_class972);
    }

    @Nullable
    public static Collection<bs_class97> getTribeMembers(@Nullable UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID + " not found uwu");
            return null;
        }
        return a_inner492.f;
    }

    public static fm_class319 i(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return fm_class319.REST;
        }
        return a_inner492.e();
    }

    public static void a(UUID uUID, fm_class319 fm_class3192) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(fm_class3192);
    }

    public static int h(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return 0;
        }
        return a_inner492.f();
    }

    public static List<KoboldEntity> n(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID + " not found uwu");
            return new ArrayList<>();
        }
        return a_inner492.a;
    }

    public static void b(UUID uUID, BlockPos blockPos) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(blockPos);
    }

    @Nullable
    public static BlockPos m(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        return a_inner492.g();
    }

    public static HashSet<EntityLivingBase> e(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashSet<EntityLivingBase>();
        }
        return a_inner492.c();
    }

    public static void a(UUID uUID, EntityLivingBase entityLivingBase) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.a(entityLivingBase);
    }

    public static void b(UUID uUID, EntityLivingBase entityLivingBase) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return;
        }
        a_inner492.b(entityLivingBase);
    }

    public static boolean g(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        for (KoboldEntity ff_class3082 : a_inner492.a) {
            if (ff_class3082.getID() == null) continue;
            return true;
        }
        return false;
    }

    public static boolean c(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return false;
        }
        return a_inner492.c;
    }

    public static void a(UUID uUID, boolean bl) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
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
        for (Map.Entry<UUID, b_inner50.a_inner49> entry : c.entrySet()) {
            b_inner50.a_inner49 a_inner492 = entry.getValue();
            if ((!a_inner492.d().isEmpty() || a_inner492.f() != 0) && tribeId.equals(a_inner492.a())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Nullable
    public static UUID b(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return null;
        }
        List<KoboldEntity> list = a_inner492.a;
        if (list.isEmpty()) {
            return null;
        }
        KoboldEntity ff_class3082 = list.get(0);
        if (!ff_class3082.boolean_J()) {
            return null;
        }
        String string = list.get(0).getDataManager().get(GirlEntity.v);
        return UUID.fromString(string);
    }

    public static HashSet<BlockPos> d(UUID uUID) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return hashSet;
        }
        for (bs_class97 bs_class972 : a_inner492.f) {
            hashSet.addAll(bs_class972.b);
        }
        hashSet.addAll(a_inner492.i);
        hashSet.addAll(a_inner492.b);
        return hashSet;
    }

    public static HashMap<UUID, BlockPos> a(UUID uUID, World world) {
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
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
        b_inner50.a_inner49 a_inner492 = c.get(uUID);
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

    public static class b_inner50
            extends WorldSavedData {
        public b_inner50(String string) {
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
            world.getMapStorage().getOrLoadData(b_inner50.class, "tribes");
        }

        @SubscribeEvent
        public void a(PlayerSleepInBedEvent playerSleepInBedEvent) {
            if (KoboldManager.a(playerSleepInBedEvent.getPos())) {
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
            for (Map.Entry<UUID, a_inner49> entry : KoboldManager.c.entrySet()) {
                EntityPlayerMP entityPlayerMP;
                b_inner50.a_inner49 a_inner492 = (a_inner49) entry.getValue();
                if (!a_inner492.i.contains(blockPos2)) continue;
                a_inner492.i.add(blockPos);
                UUID uUID = KoboldManager.b((UUID) entry.getKey());
                if (uUID == null || (entityPlayerMP = (EntityPlayerMP) world.getPlayerEntityByUUID(uUID)) == null)
                    continue;
                NetworkRegistry.networkWrapper.sendTo((IMessage) new h6_class397(blockPos, true), entityPlayerMP);
            }
        }

        @SubscribeEvent
        public void a(EntityJoinWorldEvent entityJoinWorldEvent) {
            EntityMob entityMob;
            Entity entity = entityJoinWorldEvent.getEntity();
            if (entity instanceof EntityZombie) {
                entityMob = (EntityZombie) entity;
                entityMob.targetTasks.addTask(3, new aa_class20((EntityCreature) entityMob, true, false));
            }
            if (entity instanceof AbstractSkeleton) {
                entityMob = (AbstractSkeleton) entity;
                ((AbstractSkeleton) entityMob).targetTasks.addTask(3, new aa_class20((EntityCreature) entityMob, true, false));
            }
            if (entity instanceof EntitySpider) {
                entityMob = (EntitySpider) entity;
                ((EntitySpider) entityMob).targetTasks.addTask(3, new aa_class20((EntityCreature) entityMob, true, true));
            }
        }

        @SubscribeEvent
        public void a(BlockEvent.BreakEvent breakEvent) {
            Object object;
            Object object2;
            b_inner50.a_inner49 a_inner492;
            BlockPos blockPos = breakEvent.getPos();
            World world = breakEvent.getWorld();
            if (world.isRemote) {
                return;
            }
            IBlockState iBlockState = world.getBlockState(blockPos);
            Block block = iBlockState.getBlock();
            if (block instanceof BlockChest) {
                //Map.Entry<UUID, a_inner49> entry
                //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                for (Map.Entry<UUID, a_inner49> entry : KoboldManager.c.entrySet()) {
                    a_inner492 = (a_inner49) entry.getValue();
                    if (!a_inner492.i.contains(blockPos)) continue;
                    a_inner492.i.remove(blockPos);
                    object2 = KoboldManager.b((UUID) entry.getKey());
                    if (object2 == null || (object = (EntityPlayerMP) world.getPlayerEntityByUUID((UUID) object2)) == null)
                        continue;
                    NetworkRegistry.networkWrapper.sendTo((IMessage) new h6_class397(blockPos, false), (EntityPlayerMP) object);
                }
            }
            if (block instanceof BlockBed) {
                //for (Map.Entry entry : ax_class48.access$000().entrySet()) {
                for (Map.Entry<UUID, a_inner49> entry : KoboldManager.c.entrySet()) {
                    EntityPlayerMP entityPlayerMP;
                    a_inner492 = (a_inner49) entry.getValue();
                    if (!a_inner492.b.contains(blockPos)) continue;
                    object2 = cj_class134.a(blockPos, iBlockState);
                    a_inner492.b.remove(blockPos);
                    a_inner492.b.remove(object2);
                    object = KoboldManager.b((UUID) entry.getKey());
                    if (object == null || (entityPlayerMP = (EntityPlayerMP) world.getPlayerEntityByUUID((UUID) object)) == null)
                        continue;
                    HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
                    hashSet.add(blockPos);
                    hashSet.add((BlockPos) object2);
                    NetworkRegistry.networkWrapper.sendTo((IMessage) new h6_class397(hashSet, false), entityPlayerMP);
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
                KoboldManager.a(var4, var5);
                String var6 = this.a("tribeMaster" + var2, var1);
                if (!"".equals(var6)) {
                    KoboldManager.a(var4, UUID.fromString(var6));
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
                                            KoboldManager.b(var4, new bs_class97(var16, bs_class97.KoboldTask.valueOf(var30), var17, var13));
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
                            KoboldManager.f(var4, var31);
                            ++var24;
                        }
                    }

                    String[] var25 = var23.split("\\|");
                    BlockPos var28 = new BlockPos(Integer.parseInt(var25[0]), Integer.parseInt(var25[1]), Integer.parseInt(var25[2]));
                    KoboldManager.a(var4, var28);
                    ++var22;
                }
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound var1) {
            int var2 = 0;

            for (Map.Entry var4 : KoboldManager.c.entrySet()) {
                b_inner50.a_inner49 var5 = (a_inner49) var4.getValue();
                UUID var6 = (UUID) var4.getKey();
                UUID var7 = var5.a();
                var1.setString("tribeId" + var2, var6.toString());
                var1.setString("tribeColor" + var2, var5.h.toString());
                if (var7 != null) {
                    var1.setString("tribeMaster" + var2, var7.toString());
                }

                int var8 = 0;
                HashSet var9 = new HashSet();

                for (KoboldEntity var11 : var5.a) {
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

                for (BlockPos var24 : var5.b) {
                    var1.setString(var6.toString() + "bed" + var19, var24.getX() + "|" + var24.getY() + "|" + var24.getZ());
                    ++var19;
                }

                int var22 = 0;

                for (BlockPos var28 : var5.i) {
                    var1.setString(var6.toString() + "chest" + var22, var28.getX() + "|" + var28.getY() + "|" + var28.getZ());
                    ++var22;
                }

                int var26 = 0;

                for (bs_class97 var14 : var5.f) {
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

        public static class a_inner49 {
            UUID m;
            UUID e;
            KoboldEntity g;
            List<KoboldEntity> a;
            EyeAndKoboldColor h;
            fm_class319 d = fm_class319.REST;
            BlockPos l = null;
            Collection<bs_class97> f = new ArrayList<bs_class97>();
            HashSet<EntityLivingBase> j = new HashSet();
            HashSet<BlockPos> i = new HashSet();
            HashSet<BlockPos> b = new HashSet();
            HashMap<UUID, BlockPos> k = new HashMap();
            boolean c = false;

            public a_inner49(UUID uUID, EyeAndKoboldColor eyeAndKoboldColor_, KoboldEntity ff_class3082, List<KoboldEntity> list) {
                this.m = uUID;
                this.h = eyeAndKoboldColor_;
                this.g = ff_class3082;
                this.a = list;
            }

            public a_inner49(UUID uUID, EyeAndKoboldColor eyeAndKoboldColor_) {
                this.m = uUID;
                this.h = eyeAndKoboldColor_;
                this.a = new ArrayList<KoboldEntity>();
            }

            public void a(UUID uUID) {
                this.e = uUID;
            }

            public UUID a() {
                return this.e;
            }

            public void b(bs_class97 bs_class972) {
                if (!this.f.contains(bs_class972)) {
                    return;
                }
                for (KoboldEntity ff_class3082 : bs_class972.f) {
                    ff_class3082.setCurrentAction(Action.NULL);
                    ff_class3082.setNoGravity(false);
                    ff_class3082.noClip = false;
                    ff_class3082.getDataManager().set(GirlEntity.G, false);
                }
                this.f.remove(bs_class972);
                if (bs_class972.b.isEmpty() || this.e == null) {
                    return;
                }
                EntityPlayerMP entityPlayerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(this.e);
                if (entityPlayerMP == null) {
                    return;
                }
                NetworkRegistry.networkWrapper.sendTo((IMessage) new h6_class397(bs_class972.b, false), entityPlayerMP);
            }

            public HashMap<UUID, BlockPos> d() {
                return this.k;
            }

            public void a(UUID uUID, BlockPos blockPos) {
                this.k.put(uUID, blockPos);
            }

            public void b(UUID uUID) {
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
                for (KoboldEntity object : this.a) {
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

            public void a(bs_class97 bs_class972) {
                this.f.add(bs_class972);
            }

            public fm_class319 e() {
                return this.d;
            }

            public void a(fm_class319 fm_class3192) {
                this.d = fm_class3192;
            }

            public void a(KoboldEntity ff_class3082) {
                if (this.a.contains(ff_class3082)) {
                    return;
                }
                UUID uUID = ff_class3082.girlID();
                ArrayList<KoboldEntity> arrayList = new ArrayList<KoboldEntity>();
                for (KoboldEntity ff_class3083 : this.a) {
                    if (!ff_class3083.girlID().equals(uUID)) continue;
                    arrayList.add(ff_class3083);
                }
                for (KoboldEntity ff_class3083 : arrayList) {
                    Main.LOGGER.warn(String.format("Removed old entry of kobold called %s with UUID %s owned by %s", ff_class3083.getGirlName(), ff_class3083.girlID(), this.e));
                    this.b(ff_class3083);
                }
                this.a.add(ff_class3082);
            }

            public void b(KoboldEntity ff_class3082) {
                this.a.remove(ff_class3082);
            }

            KoboldEntity b() {
                KoboldEntity ff_class3082 = null;
                for (KoboldEntity ff_class3083 : this.a) {
                    if (ff_class3083.isDead) continue;
                    if (ff_class3082 == null) {
                        ff_class3082 = ff_class3083;
                        continue;
                    }
                    float f = ff_class3082.getDataManager().get(KoboldEntity.aE).floatValue();
                    float f2 = ff_class3083.getDataManager().get(KoboldEntity.aE).floatValue();
                    if (!(f2 < f)) continue;
                    ff_class3082 = ff_class3083;
                }
                return ff_class3082;
            }

            private static RuntimeException a(RuntimeException runtimeException) {
                return runtimeException;
            }
        }
    }
}

