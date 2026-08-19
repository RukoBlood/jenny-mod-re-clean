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

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.Packets.InformOfOwnership;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.BiDirectionalMap;
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
    static public boolean debugEnabled = true;
    final static public float CUM_TIMEOUT = 60.0f;
    final static public String GALATH_OWNERSHIP = "sexmod:galath_owner_ship";
    final static public String OWNERSHIP_DATA = "sexmod:ownershipdata";
    final static public String MANG_OWNERSHIP = "sexmod:mangownershipdata";
    final static long a = 0L;
    static BiDirectionalMap<UUID, UUID> OwnerAndGalath = new BiDirectionalMap();
    static HashMap<UUID, Long> lastCumTimeMap = new HashMap<>();
    static HashSet<UUID> mangOwnershipSet = new HashSet<>();

    public GalathMangTracker() {
        super(GALATH_OWNERSHIP);
    }

    public GalathMangTracker(String string) {
        super(GALATH_OWNERSHIP);
    }

    public static void clear() {
        mangOwnershipSet.clear();
        OwnerAndGalath.clear();
    }

    public static void markAsManglelieOwned(UUID uUID) {
        UUID ownerId = GalathMangTracker.getManglelieOwnerId(uUID);
        if (ownerId != null) {
            mangOwnershipSet.add(ownerId);
        }
    }

    public static boolean isManglelieOwned(UUID uUID) {
        return mangOwnershipSet.contains(uUID);
    }

    public static boolean isOwnerNearby(GalathEntity galath) {
        UUID ownerID = OwnerAndGalath.getByValue(galath.girlID());
        if (ownerID == null) {
            return false;
        } else {
            World world = galath.world;
            EntityPlayer player = world.getPlayerEntityByUUID(ownerID);
            return player == null || player.dimension == galath.dimension && !(player.getDistance(galath) > CUM_TIMEOUT);
        }
    }

    public static boolean isOwnerOf(EntityPlayer player, GalathEntity galath) {
        return galath.girlID().equals(OwnerAndGalath.getbyKey(player.getPersistentID()));
    }

    public static void updateMangleliePartner(GalathEntity galath) {
        UUID uUID;
        ManglelieEntity manglelie = galath.getManglelieUUID(true);

        if (manglelie != null) {
            galath.world.removeEntity(manglelie);
        }
        if ((uUID = OwnerAndGalath.getByValue(galath.girlID())) == null) {
            galath.world.removeEntity(galath);
            //return;
        } else {
            World world = galath.world;
            EntityPlayer player = world.getPlayerEntityByUUID(uUID);
            galath.world.removeEntity(galath);
            OwnerAndGalath.removeByKey(uUID);
            if (player != null) {
                PackageHandler.INSTANCE.sendTo((IMessage)new InformOfOwnership(false), (EntityPlayerMP)player);
            }
        }
    }

    public static boolean hasOwner(UUID uUID) {
        return OwnerAndGalath.getbyKey(uUID) != null;
    }

    public static UUID getManglelieOwnerId(UUID uUID) {
        return OwnerAndGalath.getByValue(uUID);
    }

    public static UUID getManglelieOwnerOf(GalathEntity galath) {
        return galath != null ? GalathMangTracker.getManglelieOwnerId(galath.girlID()) : null;
    }

    public static UUID getOwnerId(UUID uUID) {
        return OwnerAndGalath.getbyKey(uUID);
    }

    public static UUID getOwnerOf(EntityPlayer player) {
        return player == null ? null : GalathMangTracker.getOwnerId(player.getPersistentID());
    }

    public static void setOwnership(UUID playerUUID, UUID girlUUID) {
        OwnerAndGalath.put(playerUUID, girlUUID);
    }

    public static void grantOwnership(EntityPlayer player, GalathEntity galath) {
        if (player != null && galath != null) {
            GalathMangTracker.setOwnership(player.getPersistentID(), galath.girlID());
        }
    }

    public static void removeOwner(UUID ownerID) {
        OwnerAndGalath.removeByKey(ownerID);
    }

    public static void removeOwnerOf(EntityPlayer entityPlayer) {
        if (entityPlayer != null) {
            GalathMangTracker.removeOwner(entityPlayer.getPersistentID());
        }
    }

    public static boolean shouldDespawn(UUID uUID, World world) {
        Long lastCumTime = lastCumTimeMap.get(uUID);
        return GalathMangTracker.isManglelieOwned(uUID) && (lastCumTime == null || world.getTotalWorldTime() - lastCumTime > 0L);
    }

    public static void saveCumTime(UUID uUID, Long time) {
        if (uUID == null) {
            Main.LOGGER.log(Level.WARN, "tried to save last cum dosage time on NULL player");
        } else {
            lastCumTimeMap.put(uUID, time);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
            ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();

            for (Map.Entry<UUID, UUID> entries : OwnerAndGalath.entrySet()) {
                UUID ownerUUID = entries.getKey();
                UUID girlUUID = entries.getValue();
                EntityPlayer player = world.getPlayerEntityByUUID(ownerUUID);
                if (player != null && GirlEntity.getServerGirlEntity(girlUUID) == null) {
                    players.add(player);
                }
            }
            for (EntityPlayer entityPlayer : players) {
                OwnerAndGalath.removeByKey(entityPlayer.getPersistentID());
                PackageHandler.INSTANCE.sendTo((IMessage) new InformOfOwnership(false), (EntityPlayerMP) entityPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        World world = event.getWorld();
        world.getMapStorage().setData(GALATH_OWNERSHIP, this);
        this.markDirty();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        world.getMapStorage().getOrLoadData(GalathMangTracker.class, GALATH_OWNERSHIP);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = nbt.getCompoundTag(OWNERSHIP_DATA);
        int count = tag.getInteger("amount");
        for (int i = 0; i < count; ++i) {
            UUID masterId = tag.getUniqueId("master" + i);
            UUID GalathId = tag.getUniqueId("galath" + i);
            long lastCumTime = tag.getLong("lastcumdosage" + i);
            if (masterId != null && GalathId != null && tag.hasUniqueId("master" + i) && tag.hasUniqueId("galath" + i)) {
                OwnerAndGalath.put(masterId, GalathId);
                lastCumTimeMap.put(masterId, lastCumTime);
            } else {
                Main.LOGGER.fatal("OMFG WHOOP WHOOP SAVING DIDNT WORK CORRECTLY AAAAAAAAAAA");
            }
        }
        NBTTagCompound mangTag = nbt.getCompoundTag(MANG_OWNERSHIP);
        int i2 = 0;
        while (mangTag.hasUniqueId("mang" + i2)) {
            mangOwnershipSet.add(mangTag.getUniqueId("mang" + i2));
            ++i2;
        }
        nbt.setTag(MANG_OWNERSHIP, new NBTTagCompound());
        nbt.setTag(OWNERSHIP_DATA, new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("amount", OwnerAndGalath.size());
        int i = 0;
        for (Map.Entry<UUID, UUID> entry : OwnerAndGalath.entrySet()) {
            UUID masterUUID = entry.getKey();
            UUID galathUUID = entry.getValue();
            Long cumTime = lastCumTimeMap.get(masterUUID);
            if (cumTime == null) {
                cumTime = 0L;
            }

            tag.setUniqueId("galath" + i, galathUUID);
            tag.setUniqueId("master" + i, masterUUID);
            tag.setLong("lastcumdosage" + i, cumTime);
            ++i;
        }
        
        NBTTagCompound mangTag = new NBTTagCompound();
        i = 0;
        for (UUID uUID : mangOwnershipSet) {
            mangTag.setUniqueId("mang" + i++, uUID);
        }
        nbt.setTag(OWNERSHIP_DATA, tag);
        nbt.setTag(MANG_OWNERSHIP, mangTag);
        return nbt;
    }
}

