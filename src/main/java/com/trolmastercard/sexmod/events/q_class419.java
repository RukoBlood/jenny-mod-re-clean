/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerLoggedInEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerLoggedOutEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.events;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import com.trolmastercard.sexmod.Action;
import com.trolmastercard.sexmod.Packages.InformOfOwnership;
import com.trolmastercard.sexmod.Packages.ResetGirl;
import com.trolmastercard.sexmod.Packages.SendBlocks;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.girls.Allie.LampItem;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.Bia.PlayerBia;
import com.trolmastercard.sexmod.girls.Ellie.PlayerEllie;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class q_class419 {
    final static UUID b = UUID.fromString("b91e6484-8911-4def-ab04-9fa3452fca5f");
    final static UUID a = UUID.fromString("adf20149-2adc-4a9d-9af5-8e9aeda019d6");

    @SubscribeEvent
    public void a(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent) {
        UUID uUID;
        EntityPlayerMP entityPlayerMP = playerLoggedInEvent.player.world.getMinecraftServer().getPlayerList().getPlayerByUUID(playerLoggedInEvent.player.getPersistentID());
        entityPlayerMP.setInvisible(false);
        entityPlayerMP.setNoGravity(false);
        entityPlayerMP.noClip = false;
        if (!entityPlayerMP.capabilities.isCreativeMode && entityPlayerMP.capabilities.isFlying) {
            entityPlayerMP.capabilities.isFlying = false;
        }
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), entityPlayerMP);
        PackageHandler.networkWrapper.sendTo((IMessage)new InformOfOwnership(GalathMangTracker.c(entityPlayerMP.getPersistentID())), entityPlayerMP);
        for (ItemStack object2 : entityPlayerMP.inventory.mainInventory) {
            if (object2.getItem() != LampItem.LAMP_ITEM || !object2.hasTagCompound()) continue;
            object2.getTagCompound().setUniqueId("user", UUID.randomUUID());
        }
        UUID uUID2 = KoboldManager.findTribeIdWith(entityPlayerMP.getPersistentID());
        if (uUID2 != null) {
            HashSet<BlockPos> hashSet = KoboldManager.d(uUID2);
            PackageHandler.networkWrapper.sendTo((IMessage)new SendBlocks(hashSet, true), entityPlayerMP);
        }
        PlayerGirl.void_C();
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(playerLoggedInEvent.player.getPersistentID());
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        this.a(world, (EntityPlayer)entityPlayerMP, playerGirl);
        if (playerGirl != null) {
            playerGirl.void_a(false);
            playerGirl.setCurrentAction(Action.NULL);
            ResetGirl.a_inner422.a(playerGirl);
        }
        if ((uUID = playerLoggedInEvent.player.getPersistentID()).equals(b)) {
            this.a(world, (EntityPlayer)entityPlayerMP, uUID);
        }
        if (uUID.equals(a)) {
            this.b(world, entityPlayerMP, uUID);
        }
        GalathEntity.void_c(entityPlayerMP);
    }

    void a(World world, EntityPlayer entityPlayer, UUID uUID) {
        PlayerBia playerBia = new PlayerBia(world, uUID);
        playerBia.setNoGravity(true);
        playerBia.noClip = true;
        playerBia.motionX = 0.0;
        playerBia.motionY = 0.0;
        playerBia.motionZ = 0.0;
        playerBia.setPosition(entityPlayer.posX, entityPlayer.posY + 69.0, entityPlayer.posZ);
        world.spawnEntity(playerBia);
        playerBia.void_B();
    }

    void b(World world, EntityPlayer entityPlayer, UUID uUID) {
        PlayerEllie playerEllie = new PlayerEllie(world, uUID);
        playerEllie.setNoGravity(true);
        playerEllie.noClip = true;
        playerEllie.motionX = 0.0;
        playerEllie.motionY = 0.0;
        playerEllie.motionZ = 0.0;
        playerEllie.setPosition(entityPlayer.posX, entityPlayer.posY + 69.0, entityPlayer.posZ);
        world.spawnEntity(playerEllie);
        playerEllie.void_B();
    }

    void a(World world, EntityPlayer entityPlayer, PlayerGirl playerGirl) {
        Predicate<PlayerGirl> predicate = ei_class2512 -> true;
        List<PlayerGirl> list = world.getEntities(PlayerGirl.class, predicate::test);
        for (PlayerGirl ei_class2514 : list) {
            if (!ei_class2514.getOwnerUserUUID().equals(entityPlayer.getPersistentID()) || playerGirl != null && ei_class2514.getEntityId() == playerGirl.getEntityId()) continue;
            world.removeEntity(ei_class2514);
        }
    }

    @SubscribeEvent
    public void a(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent) {
        EntityPlayer entityPlayer = playerLoggedOutEvent.player;
        for (GirlEntity girlEntity : GirlEntity.GirlEntityList()) {
            if (girlEntity instanceof PlayerGirl) {
                ((PlayerGirl)girlEntity).void_b(entityPlayer);
            }
            if (girlEntity.getID() == null) continue;
            if (girlEntity.getID().equals(entityPlayer.getPersistentID()) || girlEntity.getID().equals(entityPlayer.getUniqueID())) {
                ResetGirl.a_inner422.a(girlEntity);
                girlEntity.void_a(false);
                girlEntity.setCurrentAction(Action.NULL);
            }
            if (!(girlEntity instanceof PlayerGirl) || !((PlayerGirl)girlEntity).getOwnerUserUUID().equals(entityPlayer.getPersistentID()) || girlEntity.getID() == null) continue;
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)playerLoggedOutEvent.player.world.getPlayerEntityByUUID(girlEntity.getID());
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), entityPlayerMP);
            ResetGirl.a_inner422.a(entityPlayerMP);
            entityPlayer.setInvisible(false);
            girlEntity.void_e((UUID)null);
        }
    }


}

