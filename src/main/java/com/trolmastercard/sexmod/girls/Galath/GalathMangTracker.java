/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Save
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ServerTickEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  org.apache.logging.log4j.Level
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.InformOfOwnership;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.apache.logging.log4j.Level;

public class GalathMangTracker extends WorldSavedData {
    static public boolean f = true;
    final static public float c = 60.0f;
    final static public String e = "sexmod:galath_owner_ship";
    final static public String d = "sexmod:ownershipdata";
    final static public String g = "sexmod:mangownershipdata";
    final static long a = 0L;
    static gl_class375<UUID, UUID> h = new gl_class375();
    static HashMap<UUID, Long> lastCumTimeMap = new HashMap<>();
    static HashSet<UUID> playersWithGalathMangs = new HashSet<>();

    public GalathMangTracker() {
        super(e);
    }

    public GalathMangTracker(String string) {
        super(e);
    }

    public static void clear() {
        playersWithGalathMangs.clear();
        h.b();
    }

    public static void markAsManglelieOwned(UUID uUID) {
        UUID uUID2 = GalathMangTracker.getManglelieOwnerId(uUID);
        if (uUID2 == null) {
            return;
        }
        playersWithGalathMangs.add(uUID2);
    }

    public static boolean isManglelieOwned(UUID uUID) {
        return playersWithGalathMangs.contains(uUID);
    }

    public static boolean isOwnerNearby(GalathEntity f__class2972) {
        UUID uUID = h.b(f__class2972.girlID());
        if (uUID == null) {
            return false;
        }
        World world = f__class2972.world;
        EntityPlayer entityPlayer = world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return true;
        }
        if (entityPlayer.dimension != f__class2972.dimension) {
            return false;
        }
        return !(entityPlayer.getDistance(f__class2972) > 60.0f);
    }

    public static boolean b(EntityPlayer entityPlayer, GalathEntity f__class2972) {
        return f__class2972.girlID().equals(h.c(entityPlayer.getPersistentID()));
    }

    public static void updateMangleliePartner(GalathEntity f__class2972) {
        UUID uUID;
        ManglelieEntity f8_class2932 = f__class2972.getManglelieUUID(true);
        if (f8_class2932 != null) {
            f__class2972.world.removeEntity(f8_class2932);
        }
        if ((uUID = h.b(f__class2972.girlID())) == null) {
            f__class2972.world.removeEntity(f__class2972);
            return;
        }
        World world = f__class2972.world;
        EntityPlayer entityPlayer = world.getPlayerEntityByUUID(uUID);
        f__class2972.world.removeEntity(f__class2972);
        h.a(uUID);
        if (entityPlayer != null) {
            PackageHandler.INSTANCE.sendTo((IMessage)new InformOfOwnership(false), (EntityPlayerMP)entityPlayer);
        }
    }

    public static boolean c(UUID uUID) {
        return h.c(uUID) != null;
    }

    public static UUID getManglelieOwnerId(UUID uUID) {
        return h.b(uUID);
    }

    public static UUID getManglelieOwnerOf(GalathEntity galath) {
        if (galath != null) {
            return GalathMangTracker.getManglelieOwnerId(galath.girlID());
        }
        return null;
    }

    public static UUID a(UUID uUID) {
        return h.c(uUID);
    }

    public static UUID getOwnerOf(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return null;
        }
        return GalathMangTracker.a(entityPlayer.getPersistentID());
    }

    public static void a(UUID uUID, UUID uUID2) {
        h.a(uUID, uUID2);
    }

    public static void grantOwnership(EntityPlayer entityPlayer, GalathEntity f__class2972) {
        if (entityPlayer == null) {
            return;
        }
        if (f__class2972 == null) {
            return;
        }
        GalathMangTracker.a(entityPlayer.getPersistentID(), f__class2972.girlID());
    }

    public static void d(UUID uUID) {
        h.a(uUID);
    }

    public static void a(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return;
        }
        GalathMangTracker.d(entityPlayer.getPersistentID());
    }

    public static boolean isReadyForMorningGlory(UUID uUID, World world) {
        Long lastCumTime = lastCumTimeMap.get(uUID);
        // TODO this is the limiting factor
        if (!GalathMangTracker.isManglelieOwned(uUID)) {
            return false;
        }
        if (lastCumTime == null) {
            return true;
        }
        return world.getTotalWorldTime() - lastCumTime > 0L;
    }

    public static void saveCumTime(UUID uUID, Long l) {
        if (uUID == null) {
            Main.LOGGER.log(Level.WARN, "tried to save last cum dosage time on NULL player");
            return;
        }
        lastCumTimeMap.put(uUID, l);
    }

    @SubscribeEvent
    public void a(TickEvent.ServerTickEvent serverTickEvent) {
        if (serverTickEvent.phase != TickEvent.Phase.END) {
            return;
        }
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        ArrayList<EntityPlayer> arrayList = new ArrayList<EntityPlayer>();
        for (Map.Entry<UUID, UUID> object : h.c()) {
            UUID uUID = object.getKey();
            UUID uUID2 = object.getValue();
            EntityPlayer entityPlayer = world.getPlayerEntityByUUID(uUID);
            if (entityPlayer == null || GirlEntity.getServerGirlEntity(uUID2) != null) continue;
            arrayList.add(entityPlayer);
        }
        for (EntityPlayer entityPlayer : arrayList) {
            h.a(entityPlayer.getPersistentID());
            PackageHandler.INSTANCE.sendTo((IMessage)new InformOfOwnership(false), (EntityPlayerMP)entityPlayer);
        }
    }

    @SubscribeEvent
    public void a(WorldEvent.Save save) {
        World world = save.getWorld();
        world.getMapStorage().setData(e, this);
        this.markDirty();
    }

    @SubscribeEvent
    public void a(WorldEvent.Load load) {
        World world = load.getWorld();
        world.getMapStorage().getOrLoadData(GalathMangTracker.class, e);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag(d);
        int n = nBTTagCompound2.getInteger("amount");
        for (int i = 0; i < n; ++i) {
            UUID uUID = nBTTagCompound2.getUniqueId("master" + i);
            UUID uUID2 = nBTTagCompound2.getUniqueId("galath" + i);
            long l = nBTTagCompound2.getLong("lastcumdosage" + i);
            if (uUID == null || uUID2 == null
                    // TODO, I added these two bottom conditions
                    || !nBTTagCompound2.hasUniqueId("master" + i) || !nBTTagCompound2.hasUniqueId("galath" + i)) {
                Main.LOGGER.fatal("OMFG WHOOP WHOOP SAVING DIDNT WORK CORRECTLY AAAAAAAAAAA");
                continue;
            }
            h.a(uUID, uUID2);
            lastCumTimeMap.put(uUID, l);
        }
        NBTTagCompound nBTTagCompound3 = nBTTagCompound.getCompoundTag(g);
        int n2 = 0;
        while (nBTTagCompound3.hasUniqueId("mang" + n2)) {
            playersWithGalathMangs.add(nBTTagCompound3.getUniqueId("mang" + n2));
            ++n2;
        }
        nBTTagCompound.setTag(g, new NBTTagCompound());
        nBTTagCompound.setTag(d, new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        nBTTagCompound2.setInteger("amount", h.e());
        int n = 0;
        for (Map.Entry<UUID, UUID> object : h.c()) {
            UUID uUID = object.getKey();
            UUID uUID2 = object.getValue();
            Long l = lastCumTimeMap.get(uUID);
            if (l == null) {
                l = 0L;
            }
            nBTTagCompound2.setUniqueId("galath" + n, uUID2);
            nBTTagCompound2.setUniqueId("master" + n, uUID);
            nBTTagCompound2.setLong("lastcumdosage" + n, l);
            ++n;
        }
        NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
        n = 0;
        for (UUID uUID : playersWithGalathMangs) {
            nBTTagCompound3.setUniqueId("mang" + n++, uUID);
        }
        nBTTagCompound.setTag(d, nBTTagCompound2);
        nBTTagCompound.setTag(g, nBTTagCompound3);
        return nBTTagCompound;
    }
}

