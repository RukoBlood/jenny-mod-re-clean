/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector2f
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.base.PlayerGirl;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

import com.trolmastercard.sexmod.Packets.ForcePlayerGirlUpdate;
import com.trolmastercard.sexmod.Packets.ResetGirl;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.Packets.SexPrompt;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.SexPromptManager;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//ei
public abstract class PlayerGirl extends Fighter {
    final static public String CUSTOM_MODEL_NBT = "sexmod:CustomModel";
    final static public String ae = "sexmod:GirlSpecific";
    final static public float ac = 0.0f;
    final static public int am = 100;
    final static public int Y = 65;
    static public boolean ag = true; //TODO: what is ag
    public Vector2f ao = new Vector2f(0.0f, 0.0f);
    public boolean isPlayerSneaking = false;
    public boolean isPlayerSprinting = false;
    public boolean isPlayerRiding = false;
    public boolean isPlayerOnGround = true;
    public boolean isUsingItem = false;
    final static protected DataParameter<Optional<UUID>> OWNER;

    static {
        OWNER = EntityDataManager.createKey(GirlEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID).getSerializer().createKey(118);
    }

    static public Hashtable<UUID, PlayerGirl> playerGirlUUIDHashtable = new Hashtable();
    static public List<PlayerGirl> playerGirlList = new ArrayList<PlayerGirl>();
    int stripTimer = -1;
    public boolean guiPending = true;

    protected PlayerGirl(World world) {
        super(world);
        this.setSize(0.01f, 0.01f);
        playerGirlList.add(this);
    }

    protected PlayerGirl(World worldIn, UUID player) {
        this(worldIn);
        this.entityDataManager.set(OWNER, Optional.of(player));
    }

    // TODO clash
    @Nullable
    public static PlayerGirl getUUIDHashtable(UUID uUID) {
        return playerGirlUUIDHashtable.get(uUID);
    }

    @Nullable
    public static PlayerGirl GetPlayer(@Nonnull EntityPlayer entityPlayer) {
        return playerGirlUUIDHashtable.get(entityPlayer.getPersistentID());
    }

    @Nullable
    public static PlayerGirl getByPlayerUUID(UUID uUID) {
        for (GirlEntity girl : PlayerGirl.getGirlEntityList()) {
            PlayerGirl playerGirl;
            if (girl.world.isRemote || !(girl instanceof PlayerGirl) || !uUID.equals((playerGirl = (PlayerGirl)girl).getOwnerUserUUID())) continue;
            return playerGirl;
        }
        return null;
    }

    @Override
    public NetworkRegistry.TargetPoint getTargetNetworkPoint() {
        return new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY - 0.0, this.posZ, 50.0);
    }

    public void sendActionPacket(int n, Action action) {
        PacketHandler.INSTANCE.sendToAllTracking((IMessage)new ForcePlayerGirlUpdate(this.getOwnerUserUUID(), n, action), this.getTargetNetworkPoint());
    }

    public EntityPlayer resolvePlayerEntity(EntityPlayer player) {
        return player;
    }

    public boolean isRidingSomething() {
        return true;
    }

    public Vec3d getOwnerLookVector(Vec3d vec, float partialTicks) {
        return vec;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean canBeInteracted() {
        return true;
    }

    public boolean canMountPlayer() {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    public void beeOpenGUI() {
    }

    public boolean canOpenInteractionMenu() {
        return true;
    }

    public boolean handleActionRequest(String string) {
        return false;
    }

    public boolean useVanillaItemHolding() {
        return true;
    }

    @Override
    public String getGirlName() {
        EntityPlayer player;
        if (this.entityDataManager.get(OWNER).isPresent() && (player = this.world.getPlayerEntityByUUID((UUID)this.entityDataManager.get(OWNER).get())) != null) {
            return player.getName();
        }
        return "anonymous horny girl";
    }

    // Base
    public void handleInteraction() {
    }

    public abstract void handleOwnerCommand(String command, UUID partnerUUID);

    public abstract IRenderer getHandModelRenderer(int index);

    public abstract String getHandTexture(int index);

    public Vec3i getHandColor(int index) {
        return new Vec3i(255, 255, 255);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isNotColliding() {
        return true;
    }

    public boolean FAllieBoolean() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(OWNER, Optional.absent());
    }

    @SideOnly(value=Side.CLIENT)
    public static void resetPlayerGirlCamera() {
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(Minecraft.getMinecraft().player.getPersistentID());
        if (playerGirl != null) {
            playerGirl.resetCameraAndPhysics();
        }
    }

    @Override
    public void resetCameraAndPhysics() {
        this.cameraOriginPos = null;
        this.setNoGravity(false);
        if (this.world.isRemote) {
            this.resetLocalPlayerClientState();
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    protected void resetLocalPlayerClientState() {
        if (this.isControlledByLocalPlayer() || this.hasOwnerUUID()) {
            HandlePlayerMovement.setMovementLock(true);
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            player.setInvisible(false);
            player.setNoGravity(false);
            player.noClip = false;
            this.entityDataManager.set(IS_ANCHORED, false);
            PacketHandler.INSTANCE.sendToServer(new ResetGirl(this.girlID()));
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean hasCustomParts() {
        Minecraft minecraft = Minecraft.getMinecraft();
        return !this.hasOwnerUUID() || minecraft.gameSettings.thirdPersonView != 0;
    }

    protected void handleOwnerUUID(boolean allowFly) {
        if (ag) {
            if (this.getOwnerUserUUID() != null) {
                EntityPlayer player = this.world.getPlayerEntityByUUID(this.getOwnerUserUUID());
                if (player != null) {
                    player.capabilities.allowFlying = allowFly;
                    if (!allowFly) {
                        player.capabilities.isFlying = false;
                    }
                    player.sendPlayerAbilities();
                }
            }
        }
    }

    public static boolean hasPlayerGirlWithUUID(UUID uUID) {
        rebuildPlayerGirlTableFromWorld();

        for (Map.Entry<UUID, PlayerGirl> entry : playerGirlUUIDHashtable.entrySet()) {
            UUID candidateUUID = entry.getKey();
            if (uUID.equals(candidateUUID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOwnerPlayer(EntityPlayer player) {
        return player != null && PlayerGirl.hasPlayerGirlWithUUID(player.getPersistentID());
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return super.getEntityBoundingBox().offset(0.0, 0.5, 0.0);
    }

    protected EntityPlayer getPlayerPartner() {
        List<EntityPlayer> players = this.world.playerEntities;
        EntityPlayer nearest = null;

        for (EntityPlayer player : players) {
            if (!player.getPersistentID().equals(this.entityDataManager.get(OWNER).get())) {
                if (nearest == null) {
                    nearest = player;
                } else {
                    double closestDist = nearest.getDistanceSq(this.getTargetScenePosition().x, this.getTargetScenePosition().y, this.getTargetScenePosition().z);
                    double dist = player.getDistanceSq(this.getTargetScenePosition().x, this.getTargetScenePosition().y, this.getTargetScenePosition().z);
                    if (dist < closestDist) {
                        nearest = player;
                    }
                }
            }
        }
        return nearest;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isLocalPlayerNearby() {
        EntityPlayer player = this.getPlayerPartner();
        return player != null && player.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID());
    }

    public Vec3d getTargetScenePosition() {
        return new Vec3d(this.posX, this.posY - 0.0, this.posZ);
    }

    protected void teleportPlayerToGirl(UUID uUID) {
        EntityPlayerMP player = (EntityPlayerMP)this.world.getPlayerEntityByUUID(uUID);
        EntityPlayerMP ownerPlayer = (EntityPlayerMP)this.world.getPlayerEntityByUUID((UUID)this.entityDataManager.get(OWNER).get());
        PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), player);
        PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), ownerPlayer);
        this.setInteractionPlayerUUID(uUID);
        this.rotationYaw = 0.0f;
        this.rotationYawHead = 0.0f;
        assert player != null;
        player.rotationYaw = 180.0f;
        player.rotationYawHead = 180.0f;
        player.setNoGravity(true);
        player.noClip = true;
        Vec3d pos = this.getPositionVector();
        player.setPositionAndUpdate(pos.x, pos.y, pos.z + 1.0);
        player.capabilities.isFlying = true;
        assert ownerPlayer != null;
        ownerPlayer.capabilities.isFlying = true;
        this.snapPlayerToPosition(uUID);
        this.entityDataManager.set(IS_ANCHORED, true);
        this.setTargetPosition(pos);
        this.setYawRotation(0.0f);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        super.playStepSound(blockPos, block);
    }

    public AxisAlignedBB getPlayerCollisionBox(EntityPlayer player) {
        return player.getEntityBoundingBox();
    }

    @Override
    public void onUpdate() {
        this.noClip = true;
        this.setNoGravity(true);
        super.onUpdate();
        this.updateStripSequence();
        if (this.world.isRemote) {
            if (this.hasOwnerUUID()) {
                SexPromptManager.INSTANCE.tick();
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void updateEyeHeight() {
        Minecraft.getMinecraft().player.eyeHeight = this.getEyeHeight();
    }

    @SideOnly(value=Side.CLIENT)
    public boolean hasOwnerUUID() {
        return this.entityDataManager.get(OWNER).isPresent() && this.entityDataManager.get(OWNER).get().equals(Minecraft.getMinecraft().player.getPersistentID());
    }

    public boolean EGoblinIsOwnerUUIDNotNull() {
        return false;
    }

    void saveOwnerData(EntityPlayer entityPlayer) {
        NBTTagCompound nbt = entityPlayer.getEntityData();
        String modelCode = nbt.getString(CUSTOM_MODEL_NBT + PlayerGirlEntity.getGirlType(this));
        this.setCustomModelCode(modelCode);
    }

    @Override
    public void updateAITasks() {
        //Object object;
        rebuildPlayerGirlTableFromWorld();
        this.tickFollowUpTransitions();
        this.updateCustomModelParts();
        UUID uUID = this.getOwnerUserUUID();
        if (uUID != null) {
            EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
            if (player == null) {
                this.setPositionAndUpdate(this.posX, 0.0, this.posZ);
            } else {
                this.saveOwnerData(player);
                if (this.isAnchored()) {
                    Vec3d targetPos = this.getTargetPosition();
                    this.setPositionAndUpdate(targetPos.x, targetPos.y, targetPos.z);
                } else {
                    this.setPositionAndUpdate(player.posX, player.posY + 0.0, player.posZ);
                }
                Action action = this.getCurrentAction();
                if (action == Action.NULL && player.isSwingInProgress) {
                    this.setCurrentAction(Action.ATTACK);
                }
                if (action == Action.ATTACK && !player.isSwingInProgress) {
                    this.setCurrentAction(Action.NULL);
                }
            }
        }
    }

    void updateStripSequence() {
        if (this.stripTimer != -1) {
            ++this.stripTimer;
            if (!this.world.isRemote && this.stripTimer == 65) {
                this.setOutfitIndex(this.getOutfitIndex() == 0 ? 1 : 0);
            }
            if (this.stripTimer >= 100) {
                if (this.getCurrentAction() == Action.STRIP) {
                    if (this.world.isRemote) {
                        this.handleClientOwner();
                    } else {
                        this.setCurrentAction(Action.NULL);
                    }
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void handleClientOwner() {
        if (this.hasOwnerUUID()) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.gameSettings.thirdPersonView = 0;
            mc.entityRenderer.loadEntityShader(mc.getRenderViewEntity());
            HandlePlayerMovement.setMovementLock(true);
        }
    }

    public boolean isSceneActive() {
        return this.isAnchored();
    }

    //Goblin only
    public Vec3d getOwnerAimVector(Vec3d vec, float partialTicks) {
        return vec;
    }

    public boolean canPerformAction(Action action, EntityPlayer player) {
        return false;
    }

    public boolean isPlayerGirl() {
        return true;
    }

    public void onOwnerInteract(EntityPlayer player) {
    }

    @Override
    public void setCurrentAction(Action action) {
        if (!this.world.isRemote && action == Action.NULL && this.isAnchored()) {
            System.out.println("prevented a potential animation break");
        } else {
            if (action == Action.STRIP) {
                this.stripTimer = this.world.isRemote ? 5 : 0;
            }
            super.setCurrentAction(action);
        }
    }

    void syncArmor(EntityPlayer player) {
        this.entityDataManager.set(HELMET_SLOT, ItemStack.EMPTY);
        this.entityDataManager.set(CHEST_SLOT, ItemStack.EMPTY);
        this.entityDataManager.set(LEGS_SLOT, ItemStack.EMPTY);
        this.entityDataManager.set(BOOTS_SLOT, ItemStack.EMPTY);

        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemElytra) {
                this.entityDataManager.set(CHEST_SLOT, stack);
            } else {
                if (stack.getItem() instanceof ItemArmor) {
                    ItemArmor itemArmor = (ItemArmor) stack.getItem();
                    switch (itemArmor.getEquipmentSlot()) {
                        case HEAD: {
                            this.entityDataManager.set(HELMET_SLOT, stack);
                            break;
                        }
                        case CHEST: {
                            this.entityDataManager.set(CHEST_SLOT, stack);
                            break;
                        }
                        case LEGS: {
                            this.entityDataManager.set(LEGS_SLOT, stack);
                            break;
                        }
                        case FEET: {
                            this.entityDataManager.set(BOOTS_SLOT, stack);
                        }
                    }
                }
            }
        }
    }

    public UUID getOwnerUserUUID() {
        return this.entityDataManager.get(OWNER).isPresent() ? this.entityDataManager.get(OWNER).get() : null;
    }

    @Nullable
    public EntityPlayer getOwnerPlayer() {
        UUID uUID = this.getOwnerUserUUID();
        return uUID == null ? null : this.world.getPlayerEntityByUUID(uUID);
    }

    public void setOwnerId(Optional<UUID> optional) {
        this.entityDataManager.set(OWNER, optional);
    }

    public void onTickClient() {
    }

    //TODO: is this a hitbox helper??
    public void spawnHitboxHelper() {
    }

    public static void rebuildPlayerGirlTableFromWorld() {
        ArrayList<PlayerGirl> toRemove = new ArrayList<PlayerGirl>();

        for (PlayerGirl playerGirl : playerGirlList) {
            if (playerGirl.getOwnerUserUUID() != null) {
                playerGirlUUIDHashtable.put(playerGirl.getOwnerUserUUID(), playerGirl);
                toRemove.add(playerGirl);
            }
        }
        for (PlayerGirl playerGirl : toRemove) {
            playerGirlList.remove(playerGirl);
        }

        rebuildPlayerGirlTableInternal();
    }

    static void rebuildPlayerGirlTableInternal() {
        ArrayList<UUID> toRemove = new ArrayList<UUID>();

        for (Map.Entry<UUID, PlayerGirl> entry : playerGirlUUIDHashtable.entrySet()) {
            if (entry.getValue().isDead) {
                toRemove.add(entry.getKey());
            }
        }
        for (UUID uUID : toRemove) {
            playerGirlUUIDHashtable.remove(uUID);
        }
    }

    protected boolean isOwnerUUID(UUID uUID) {
        if (uUID == null) {
            return false;
        }

        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(uUID);
        return playerGirl != null;
    }

    @Override
    public void doAction(String action, UUID player) {
        if (!this.handleActionRequest(action) && this.entityDataManager.get(OWNER).isPresent()) {
            PacketHandler.INSTANCE.sendToServer(new SexPrompt(action, player, this.entityDataManager.get(OWNER).get(), this.guiPending));
            this.guiPending = true;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("owner", this.entityDataManager.get(OWNER).get().toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.entityDataManager.set(OWNER, Optional.of(UUID.fromString(nbt.getString("owner"))));
        playerGirlList.add(this);
    }

    @Override
    public void PlaySoundAtPosition(SoundEvent sound, float volume, float pitch) {
        Vec3d pos = this.getTargetScenePosition();
        if (this.world.isRemote) {
            this.world.playSound(pos.x, pos.y, pos.z, sound, SoundCategory.NEUTRAL, volume, pitch, false);
        } else {
            this.world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), sound, SoundCategory.PLAYERS, volume, pitch);
        }
    }

    @Override
    public void PlaySound(SoundEvent sound) {
        this.PlaySoundAtPosition(sound, 1.0f, 1.0f);
    }

    public void playRandomSound(SoundEvent[] soundEventArray) {
        this.PlaySoundAtPosition(soundEventArray[this.getRNG().nextInt(soundEventArray.length)], 1.0f, 1.0f);
    }

    @Override
    public void playSoundAtVolume(SoundEvent sound, float volume) {
        this.PlaySoundAtPosition(sound, volume, 1.0f);
    }

    @Override
    protected void doSubAction() {
    }
}

