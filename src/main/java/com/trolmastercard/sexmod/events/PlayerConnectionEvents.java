package com.trolmastercard.sexmod.events;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.Packets.InformOfOwnership;
import com.trolmastercard.sexmod.Packets.ResetGirl;
import com.trolmastercard.sexmod.Packets.SendBlocks;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.girls.Allie.lamp.LampItem;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.Bia.PlayerBia;
import com.trolmastercard.sexmod.girls.Ellie.PlayerEllie;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Handles player connection and disconnection events.
 *
 * <p>This class is responsible for restoring player state on login,
 * synchronizing mod data, removing duplicate {@link PlayerGirl} entities,
 * and correctly resetting interactions when a player logs out.</p>
 */
public class PlayerConnectionEvents {
    /**
     * Hard-coded UUID of the player for whom a special {@link PlayerBia} or {@link PlayerEllie}
     * entity is created on login.
     */
    final static UUID BIA_PLAYER_UUID = UUID.fromString("b91e6484-8911-4def-ab04-9fa3452fca5f");
    final static UUID ELLIE_PLAYER_UUID = UUID.fromString("adf20149-2adc-4a9d-9af5-8e9aeda019d6");

    /**
     * Handles a player logging in to the server.
     *
     * <p>This method resets certain player states, sends synchronization
     * packets to the client, updates Kobold tribe data, rebuilds the
     * {@link PlayerGirl} table, removes duplicate entities, and creates
     * special entities for preconfigured UUIDs when needed.</p>
     *
     * @param event the player login event
     */
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Get the server-side representation of the player by persistent UUID
        EntityPlayerMP playerMP = event.player.world.getMinecraftServer().getPlayerList().getPlayerByUUID(event.player.getPersistentID());

        // Reset possible stuck player states.
        playerMP.setInvisible(false);
        playerMP.setNoGravity(false);
        playerMP.noClip = false;

        // If the player is not in creative mode but is flying, disable flight.
        if (!playerMP.capabilities.isCreativeMode && playerMP.capabilities.isFlying) {
            playerMP.capabilities.isFlying = false;
        }

        PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(true), playerMP);
        PacketHandler.INSTANCE.sendTo(new InformOfOwnership(GalathMangTracker.hasOwner(playerMP.getPersistentID())), playerMP);

        for (ItemStack stack : playerMP.inventory.mainInventory) {
            if (stack.getItem() == LampItem.LAMP_ITEM && stack.hasTagCompound()) {
                stack.getTagCompound().setUniqueId("user", UUID.randomUUID());
            }
        }

        UUID tribeID = KoboldManager.findTribeIdWith(playerMP.getPersistentID());
        if (tribeID != null) {
            HashSet<BlockPos> tribeBlocks = KoboldManager.getAllTribeBlocks(tribeID);
            PacketHandler.INSTANCE.sendTo(new SendBlocks(tribeBlocks, true), playerMP);
        }

        PlayerGirl.rebuildPlayerGirlTableFromWorld();
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(event.player.getPersistentID());

        World serverWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        this.clearDuplicatePlayerGirls(serverWorld, playerMP, playerGirl);

        if (playerGirl != null) {
            playerGirl.setAnchored(false);
            playerGirl.setCurrentAction(Action.NULL);
            ResetGirl.EventHandler.resetGirl(playerGirl);
        }

        UUID playerUUID = event.player.getPersistentID();
        if (playerUUID.equals(BIA_PLAYER_UUID)) {
            this.spawnSpecialBia(serverWorld, playerMP, playerUUID);
        }
        if (playerUUID.equals(ELLIE_PLAYER_UUID)) {
            this.spawnSpecialEllie(serverWorld, playerMP, playerUUID);
        }
        GalathEntity.handlePlayerJoin(playerMP);
    }

    /**
     * Creates a special {@link PlayerBia} entity for the given player.
     *
     * <p>The entity is spawned above the player, has no gravity and no
     * collision, and receives a helper hitbox.</p>
     *
     * @param world the world in which to create the entity
     * @param player the player relative to whose position the entity is created
     * @param playerUUID the UUID of the owning player
     */
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

    /**
     * Creates a special {@link PlayerEllie} entity for the given player.
     *
     * <p>The entity is spawned above the player, has no gravity and no
     * collision, and receives a helper hitbox.</p>
     *
     * @param world the world in which to create the entity
     * @param player the player relative to whose position the entity is created
     * @param uUID the UUID of the owning player
     */
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

    /**
     * Removes loaded {@link PlayerGirl} entities owned by the player if they
     * are not the canonical {@code playerGirl} entity.
     *
     * <p>This is used on player login to remove possible duplicates of the
     * PlayerGirl entities owned by that player.</p>
     *
     * @param world the world from which duplicates should be removed
     * @param player the player whose duplicates are being checked
     * @param playerGirl the canonical entity for the player that should be kept;
     *                   may be {@code null}
     */
    void clearDuplicatePlayerGirls(World world, EntityPlayer player, PlayerGirl playerGirl) {
        Predicate<PlayerGirl> filter = girl -> true;
        List<PlayerGirl> loadedGirls = world.getEntities(PlayerGirl.class, filter::test);
        for (PlayerGirl girl : loadedGirls) {
            if (girl.getOwnerUserUUID().equals(player.getPersistentID()) && (playerGirl == null || girl.getEntityId() != playerGirl.getEntityId())) {
                world.removeEntity(girl);
            }
        }
    }

    /**
     * Handles a player logging out of the server.
     *
     * <p>This method iterates over all {@link GirlEntity} instances, resets
     * interactions associated with the leaving player, and clears the state of
     * {@link PlayerGirl} entities owned by that player.</p>
     *
     * @param event the player logout event
     *
     */
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        try {
            for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                if (girl instanceof PlayerGirl) {
                    ((PlayerGirl)girl).onOwnerInteract(player);
                }
                if (girl.getInteractionPlayerUUID() != null) {
                    if (girl.getInteractionPlayerUUID().equals(player.getPersistentID()) || girl.getInteractionPlayerUUID().equals(player.getUniqueID())) {
                        ResetGirl.EventHandler.resetGirl(girl);
                        girl.setAnchored(false);
                        girl.setCurrentAction(Action.NULL);
                    }
                    if (girl instanceof PlayerGirl && ((PlayerGirl) girl).getOwnerUserUUID().equals(player.getPersistentID()) && girl.getInteractionPlayerUUID() != null) {
                        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player.world.getPlayerEntityByUUID(girl.getInteractionPlayerUUID());
                        PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(true), entityPlayerMP);
                        ResetGirl.EventHandler.resetGirls(entityPlayerMP);
                        player.setInvisible(false);
                        girl.setInteractionPlayerUUID(null);
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.printf("Errored out: %s%n", e);
        }
    }


}

