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

import com.trolmastercard.sexmod.girls.Action;
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

public class PlayerConnectionEvents {
    final static UUID BIA_PLAYER_UUID = UUID.fromString("b91e6484-8911-4def-ab04-9fa3452fca5f");
    final static UUID ELLIE_PLAYER_UUID = UUID.fromString("adf20149-2adc-4a9d-9af5-8e9aeda019d6");

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        UUID playerUUID;
        EntityPlayerMP playerMP = event.player.world.getMinecraftServer().getPlayerList().getPlayerByUUID(event.player.getPersistentID());

        playerMP.setInvisible(false);
        playerMP.setNoGravity(false);
        playerMP.noClip = false;

        if (!playerMP.capabilities.isCreativeMode && playerMP.capabilities.isFlying) {
            playerMP.capabilities.isFlying = false;
        }

        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), playerMP);
        PackageHandler.networkWrapper.sendTo((IMessage)new InformOfOwnership(GalathMangTracker.c(playerMP.getPersistentID())), playerMP);

        for (ItemStack stack : playerMP.inventory.mainInventory) {
            if (stack.getItem() != LampItem.LAMP_ITEM || !stack.hasTagCompound()) continue;
            stack.getTagCompound().setUniqueId("user", UUID.randomUUID());
        }

        UUID tribeID = KoboldManager.findTribeIdWith(playerMP.getPersistentID());
        if (tribeID != null) {
            HashSet<BlockPos> tribeBlocks = KoboldManager.getAllTribeBlocks(tribeID);
            PackageHandler.networkWrapper.sendTo((IMessage)new SendBlocks(tribeBlocks, true), playerMP);
        }

        PlayerGirl.cleanupGlobalRegistry();
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(event.player.getPersistentID());

        World serverWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        this.clearDuplicatePlayerGirls(serverWorld, (EntityPlayer)playerMP, playerGirl);

        if (playerGirl != null) {
            playerGirl.setAnchored(false);
            playerGirl.setCurrentAction(Action.NULL);
            ResetGirl.a_inner422.a(playerGirl);
        }
        if ((playerUUID = event.player.getPersistentID()).equals(BIA_PLAYER_UUID)) {
            this.spawnSpecialBia(serverWorld, (EntityPlayer)playerMP, playerUUID);
        }
        if (playerUUID.equals(ELLIE_PLAYER_UUID)) {
            this.spawnSpecialEllie(serverWorld, playerMP, playerUUID);
        }
        GalathEntity.void_c(playerMP);
    }

    void spawnSpecialBia(World world, EntityPlayer player, UUID playerUUID) {
        PlayerBia bia = new PlayerBia(world, playerUUID);
        bia.setNoGravity(true);
        bia.noClip = true;
        bia.motionX = 0.0;
        bia.motionY = 0.0;
        bia.motionZ = 0.0;
        bia.setPosition(player.posX, player.posY + 69.0, player.posZ);
        world.spawnEntity(bia);
        bia.spawnHitboxHelper();
    }

    void spawnSpecialEllie(World world, EntityPlayer player, UUID uUID) {
        PlayerEllie ellie = new PlayerEllie(world, uUID);
        ellie.setNoGravity(true);
        ellie.noClip = true;
        ellie.motionX = 0.0;
        ellie.motionY = 0.0;
        ellie.motionZ = 0.0;
        ellie.setPosition(player.posX, player.posY + 69.0, player.posZ);
        world.spawnEntity(ellie);
        ellie.spawnHitboxHelper();
    }

    void clearDuplicatePlayerGirls(World world, EntityPlayer player, PlayerGirl playerGirl) {
        Predicate<PlayerGirl> filter = girl -> true;
        List<PlayerGirl> loadedGirls = world.getEntities(PlayerGirl.class, filter::test);
        for (PlayerGirl girl : loadedGirls) {
            if (!girl.getOwnerUserUUID().equals(player.getPersistentID()) || playerGirl != null && girl.getEntityId() == playerGirl.getEntityId()) continue;
            world.removeEntity(girl);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            if (girl instanceof PlayerGirl) {
                ((PlayerGirl)girl).detachPartner(player);
            }
            if (girl.getID() == null) continue;
            if (girl.getID().equals(player.getPersistentID()) || girl.getID().equals(player.getUniqueID())) {
                ResetGirl.a_inner422.a(girl);
                girl.setAnchored(false);
                girl.setCurrentAction(Action.NULL);
            }
            if (!(girl instanceof PlayerGirl) || !((PlayerGirl)girl).getOwnerUserUUID().equals(player.getPersistentID()) || girl.getID() == null) continue;
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)event.player.world.getPlayerEntityByUUID(girl.getID());
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), entityPlayerMP);
            ResetGirl.a_inner422.a(entityPlayerMP);
            player.setInvisible(false);
            girl.setInteractionPlayerUUID((UUID)null);
        }
    }


}

