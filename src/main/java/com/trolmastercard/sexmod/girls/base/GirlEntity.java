/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.util.Pair
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  org.apache.logging.log4j.Level
 */
package com.trolmastercard.sexmod.girls.base;

import com.mojang.realmsclient.util.Pair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.ChangeDataParameter;
import com.trolmastercard.sexmod.Packages.ResetController;
import com.trolmastercard.sexmod.Packages.ResetGirl;
import com.trolmastercard.sexmod.Packages.TeleportPlayer;
import com.trolmastercard.sexmod.Packages.SendChatMessage;
import com.trolmastercard.sexmod.Packages.SyncActionPacket;
import com.trolmastercard.sexmod.companion.OpenAndCloseDoorBehindHer;
import com.trolmastercard.sexmod.companion.fighter.LookAtNearbyEntity;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.AbstractGoblinKoboldEntity;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.Custom.CustomModelRenderer;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.gui.GirlInventoryUI;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.ClientServerCheck;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.MatrixStack;

// em_class258
public abstract class GirlEntity extends EntityCreature implements IAnimatable {
    static public int maxAgeInTicks = 22;
    final static protected long TICK_RATE = 20L;
    final private AnimationFactory factory = new AnimationFactory(this);
    public EntityAIWanderAvoidWater aiWander;
    public LookAtNearbyEntity aiLookAtPlayer;
    static public HashSet<GirlEntity> GLOBAL_GIRL_CACHE = new HashSet();
    public Vec3d playerCameraOffsetPos;
    protected float cameraYaw;
    protected EntityDataManager entityDataManager;
    public PathNavigate pathNavigator;
    public Vec3d homeCoords = Vec3d.ZERO;
    public EntityEnderPearl activePearl;
    public float scaleFactor = 1.0f;
    public boolean isSpecialState = false;
    private boolean isRegisteredLocally = false;

    HashMap<String, Vec3d> boneTransformCache = new HashMap();
    final static public DataParameter<String> MASTER;
    final static public DataParameter<Boolean> IS_ANCHORED;
    final static public DataParameter<String> TARGET_POS;
    final static public DataParameter<Float> YAW_ROTATION = EntityDataManager.createKey(GirlEntity.class, DataSerializers.FLOAT).getSerializer().createKey(107);
    final static public DataParameter<String> GIRL_ID = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(106);
    final static public DataParameter<Integer> OUTFIT_INDEX = EntityDataManager.createKey(GirlEntity.class, DataSerializers.VARINT).getSerializer().createKey(105);
    final static public DataParameter<String> CUR_ACTION = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(104);
    final static public DataParameter<String> GIRL_HAND_STATES = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(103);
    final static public DataParameter<String> INTERACTION_PARTNER_UUID = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(102);
    final static public DataParameter<String> WALK_SPEED = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(101);
    final static public DataParameter<String> CUSTOM_MODEL_KEY = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(100);
    final static public DataParameter<String> CUSTOM_NAME = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(99);

    final static protected List<Item> TEMPTATION_ITEMS = Arrays.asList(Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.ENDER_PEARL);

    public AnimationController actionController;
    public AnimationController movementController;
    public AnimationController eyesController;
    HashMap<String, Pair<Integer, Integer>> animationVariantMap = new HashMap();
    AnimationProcessor<?> cachedAnimationProcessor = null;
    public List<String> boneTrackingList = new ArrayList<String>();
    protected List<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> customPartsData = null;

    static {
        MASTER = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(110);
        IS_ANCHORED = EntityDataManager.createKey(GirlEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(109);
        TARGET_POS = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(108);

    }

    public void setWalkSpeed(WalkSpeed walkSpeed) {
        this.entityDataManager.set(WALK_SPEED, walkSpeed.toString());
    }

    public WalkSpeed getWalkType() {
        return WalkSpeed.valueOf(this.entityDataManager.get(WALK_SPEED));
    }

    @SideOnly(value=Side.CLIENT)
    protected void changeDataParameterFromClient(String paramKey, String paramValue) {
        PackageHandler.INSTANCE.sendToServer(new ChangeDataParameter(this.girlID(), paramKey, paramValue));
    }

    //f
    public UUID girlID() {
        try {
            return UUID.fromString(this.entityDataManager.get(GIRL_ID));
        } catch (Exception e) {
            UUID uUID = UUID.randomUUID();
            this.entityDataManager.set(GIRL_ID, uUID.toString());
            return uUID;
        }
    }

    public Action currentAction() {
        return Action.valueOf(this.entityDataManager.get(CUR_ACTION));
    }

    public void setCurrentAction(Action action) {
        Action previousAction = this.currentAction();
        if (previousAction == action) {
            return;
        }
        if (action == Action.ATTACK && previousAction != Action.NULL) {
            return;
        }
        Action targetAction = action == null ? Action.NULL : action;
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("currentAction", action.toString());
            return;
        }
        previousAction.ticksPlaying = new int[]{0, 0};
        this.entityDataManager.set(CUR_ACTION, action.toString());
    }

    public int getOutfitIndex() {
        return this.entityDataManager.get(OUTFIT_INDEX);
    }

    public void setOutfitIndex(int index) {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("currentModel", "0");
        } else {
            this.entityDataManager.set(OUTFIT_INDEX, index);
        }
    }

    public boolean isCustomType() {
        return false;
    }

    @Nullable
    public EntityPlayer getPlayerEntity() {
        UUID uUID = this.playerSheHasSexWith();
        if (uUID == null) {
            return null;
        }
        return this.world.getPlayerEntityByUUID(uUID);
    }

    public static void sendMessageToTrackingPlayers(GirlEntity girl, String message) {
        for (EntityPlayer player : WorldUtils.getPlayersTrackingEntity(girl)) {
            player.sendMessage(new TextComponentString(message));
        }
    }

    public static void girlPlaySound(GirlEntity girl, SoundEvent sound, boolean positional3D) {
        Vec3d origin = girl.getPositionVector();
        for (EntityPlayer player : WorldUtils.getPlayersTrackingEntity(girl)) {
            Vec3d playPos;
            if (!positional3D) {
                playPos = origin;
            } else {
                Vec3d playerPos = player.getPositionVector();
                Vec3d dir = origin.subtract(playerPos).normalize();
                playPos = playerPos.add(dir);
            }
            ((EntityPlayerMP)player).connection.sendPacket(new SPacketSoundEffect(sound, SoundCategory.AMBIENT, playPos.x, playPos.y, playPos.z, 1.0f, 1.0f));
        }
    }

    public static void girlPlaySound(GirlEntity girl, SoundEvent sounds) {
        GirlEntity.girlPlaySound(girl, sounds, false);
    }

    public static void playRandomSound(GirlEntity em_class2582, SoundEvent[] sounds) {
        GirlEntity.girlPlaySound(em_class2582, SoundsHandler.random(sounds));
    }

    public static void playRandomSound(GirlEntity girl, SoundEvent[] sounds, boolean positional) {
        GirlEntity.girlPlaySound(girl, SoundsHandler.random(sounds), positional);
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d getVectorTowardPlayer() {
        Vec3d playerPos = Minecraft.getMinecraft().player.getPositionVector();
        Vec3d entityPos = this.getPositionVector();
        Vec3d direction = entityPos.subtract(playerPos).normalize();
        return playerPos.add(direction);
    }

    @Nullable
    public UUID playerSheHasSexWith() {
        String uuidStr = this.entityDataManager.get(INTERACTION_PARTNER_UUID);
        if (uuidStr.equals("null")) {
            return null;
        }
        return UUID.fromString(uuidStr);
    }

    public void setInteractionPlayerUUID(UUID uuid) {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("playerSheHasSexWith", uuid == null ? (String) null : uuid.toString());
            return;
        }
        this.entityDataManager.set(INTERACTION_PARTNER_UUID, uuid == null ? "null" : uuid.toString());
    }

    public void setInteractionPlayer(@Nonnull EntityPlayer player) {
        this.setInteractionPlayerUUID(player.getPersistentID());
    }

    public Vec3d getTargetPosition() {
        String[] coords = this.entityDataManager.get(TARGET_POS).split("\\|");
        return new Vec3d(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));
    }

    public void setTargetPosition(Vec3d pos) {
        if (this.world.isRemote) {
            String formatted = pos.x + "f" + pos.y + "f" + pos.z + "f";
            this.changeDataParameterFromClient("targetPos", formatted);
            return;
        }
        this.entityDataManager.set(TARGET_POS, pos.x + "|" + pos.y + "|" + pos.z);
    }
    //end of reversing #1

    public void setTargetPositionDirect(Vec3d pos) {
        this.entityDataManager.set(TARGET_POS, pos.x + "|" + pos.y + "|" + pos.z);
    }

    public Float getYawRotation() {
        return this.entityDataManager.get(YAW_ROTATION);
    }

    public void setYawRotation(float yaw) {
        this.entityDataManager.set(YAW_ROTATION, yaw);
    }

    public void setAnchored(boolean anchored) {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("shouldbeattargetpos", String.valueOf(anchored));
            return;
        }
        this.entityDataManager.set(IS_ANCHORED, anchored);
    }

    public boolean isAnchored() {
        return this.entityDataManager.get(IS_ANCHORED);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    protected GirlEntity(World world) {
        super(world);
        if (world.isRemote) {
            this.initAnimationControllers();
        }
        if (world.isRemote && world instanceof FakeWorld) {
            return;
        }
        PathNavigate nav = this.getNavigator();
        if (nav instanceof PathNavigateGround) {
            ((PathNavigateGround)nav).setBreakDoors(true);
        }
    }

    @SideOnly(value=Side.CLIENT)
    protected void initAnimationControllers() {
        this.actionController = new AnimationController<GirlEntity>(this, "action", 0.0f, this::predicate);
        this.movementController = new AnimationController<GirlEntity>(this, "movement", 5.0f, this::predicate);
        this.eyesController = new AnimationController<GirlEntity>(this, "eyes", 10.0f, this::predicate);

        // TODO not sure where to insert sound keyframe inserter, or how to even add custom insertions in Java for geckolib

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.pathNavigator = this.getNavigator();
        this.entityDataManager = this.getDataManager();
        this.entityDataManager.register(GIRL_ID, UUID.randomUUID().toString());
        this.entityDataManager.register(OUTFIT_INDEX, 1);
        this.entityDataManager.register(CUR_ACTION, Action.NULL.toString());
        this.entityDataManager.register(GIRL_HAND_STATES, "");
        this.entityDataManager.register(INTERACTION_PARTNER_UUID, "null");
        this.entityDataManager.register(IS_ANCHORED, false);
        this.entityDataManager.register(YAW_ROTATION, 0.0f);
        this.entityDataManager.register(TARGET_POS, "0|0|0");
        this.entityDataManager.register(MASTER, "");
        this.entityDataManager.register(WALK_SPEED, WalkSpeed.WALK.toString());
        this.entityDataManager.register(CUSTOM_MODEL_KEY, "");
        this.entityDataManager.register(CUSTOM_NAME, "");
    }

    public void setLocallyRegistered(boolean registered) {
        this.isRegisteredLocally = registered;
        if (registered) {
            GirlID.PutGirlInList(this);
        } else {
            GirlID.RemoveGirlInList(this);
        }
    }

    public boolean isLocallyRegistered() {
        return this.isRegisteredLocally;
    }

    public static List<GirlEntity> GirlEntityList() {
        if (!ClientServerCheck.getInstance()) {
            return GirlEntity.getClientGirls();
        }
        WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
        if (worlds.length == 0) {
            return new ArrayList<GirlEntity>();
        }
        ArrayList<GirlEntity> list = new ArrayList<GirlEntity>();
        for (WorldServer world : worlds) {
            list.addAll(world.getEntities(GirlEntity.class, girl -> true));
        }
        return list;
    }

    @SideOnly(value=Side.CLIENT)
    private static List<GirlEntity> getClientGirls() {
        WorldClient clientWorld = Minecraft.getMinecraft().world;
        if (clientWorld == null) {
            return new ArrayList<GirlEntity>();
        }
        return clientWorld.getEntities(GirlEntity.class, girl -> true);
    }

    public boolean canBeInteractedWith() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0);
    }

    @Override
    protected void initEntityAI() {
        this.aiWander = new EntityAIWanderAvoidWater(this, 0.35);
        this.aiLookAtPlayer = new LookAtNearbyEntity(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAITempt((EntityCreature)this, 0.4, false, new HashSet<Item>(TEMPTATION_ITEMS)));
        this.tasks.addTask(3, new OpenAndCloseDoorBehindHer(this));
        this.tasks.addTask(5, this.aiLookAtPlayer);
        this.tasks.addTask(5, this.aiWander);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setDouble("homeX", this.homeCoords.x);
        nbt.setDouble("homeY", this.homeCoords.y);
        nbt.setDouble("homeZ", this.homeCoords.z);
        nbt.setString("girlID", this.entityDataManager.get(GIRL_ID));
        String customName = this.getCustomName();
        if (!"".equals(customName)) {
            nbt.setString("sexmod:customname", customName);
        }
        if (this.supportsCustomModels()) {
            nbt.setString("sexmod:customModel", this.getCustomModelKey());
        }
        super.writeEntityToNBT(nbt);
    }

    protected boolean supportsCustomModels() {
        return GirlEntity.isValidGirl((Entity)this);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        String uuidStr;
        super.readEntityFromNBT(nbt);
        this.homeCoords = new Vec3d(nbt.getDouble("homeX"), nbt.getDouble("homeY"), nbt.getDouble("homeZ"));
        String customName = nbt.getString("sexmod:customname");
        if (!customName.isEmpty()) {
            this.setCustomName(customName);
        }

        if ((uuidStr = nbt.getString("girlID")).isEmpty()) {
            return;
        }
        UUID uUID = UUID.fromString(uuidStr);
        boolean isDuplicate = false;
        for (GirlEntity girl : GirlEntity.girlList(uUID)) {
            if (girl.world.isRemote || girl == this || girl.isDead || !girl.isAddedToWorld()) continue;
            isDuplicate = true;
            break;
        }
        if (isDuplicate) {
            Main.LOGGER.log(Level.WARN, String.format("got a duped %s with id '%s'. Deleted her", this.getGirlName(), uUID));
            this.world.removeEntity(this);
            return;
        }
        this.entityDataManager.set(GIRL_ID, uUID.toString());
        if (this.supportsCustomModels()) {
            this.setCustomModelKey(nbt.getString("sexmod:customModel"));
        }
    }

    public boolean isInteractable() {
        return true;
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public void setMotionVector(Vec3d motion) {
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
    }

    public Vec3d getLastTickPosition() {
        return new Vec3d(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ);
    }

    @Override
    public void updateAITasks() {
        if (this.entityDataManager.get(IS_ANCHORED)) {
            this.setRotationYawHead(this.getYawRotation());
            this.setPositionAndRotation(this.getTargetPosition().x, this.getTargetPosition().y, this.getTargetPosition().z, this.getYawRotation().floatValue(), 0.0f);
            this.setRotation(this.getYawRotation(), this.rotationPitch);
        }
        if (this.homeCoords.equals(Vec3d.ZERO)) {
            this.homeCoords = new Vec3d(this.getPosition());
        }
        this.updateCustomModelParts();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.updateActionTicks();
    }

    protected void updateCustomModelParts() {
        if (!CustomModel.isLoaded) {
            return;
        }
        HashSet<String> activeParts = this.getCustomPartsSet();
        PlayerGirlEntity playerGirl = PlayerGirlEntity.fromGirl(this);
        HashSet<String> partsToRemove = new HashSet<String>();
        String currentGroup = CustomModel.getCurrentGroup();
        for (String part : activeParts) {
            if (!CustomModel.getPartName(part, currentGroup).isEmpty()) {
                partsToRemove.add(part);
                continue;
            }
            HashSet<PlayerGirlEntity> allowedEntities = CustomModel.getAllowedEntities(part);
            if (allowedEntities == null) {
                partsToRemove.add(part);
                continue;
            }
            if (allowedEntities.isEmpty() || allowedEntities.contains((Object)playerGirl)) continue;
            partsToRemove.add(part);
        }
        if (partsToRemove.isEmpty()) {
            return;
        }
        activeParts.removeAll(partsToRemove);
        this.setCustomModelKey(GirlEntity.serializePartsSet(activeParts));
    }
    //end of deobfuscation #2

    protected void updateActionTicks() {
        Action action = this.currentAction();
        int sideIndex = this.world.isRemote ? 1 : 0;
        action.ticksPlaying[sideIndex] = action.ticksPlaying[sideIndex] + 1;
        if (action.ticksPlaying[sideIndex] < action.length) {
            return;
        }
        if (action.followUp == null) {
            return;
        }
        if (!this.world.isRemote) {
            this.setCurrentAction(action.followUp);
        }
    }

    protected void applyCustomPathNodeVelocity() {
        Path path = this.getNavigator().getPath();
        if (path == null || this.onGround || this.isInWater()) {
            return;
        }

        int currentIndex = path.getCurrentPathIndex();
        int totalLength = path.getCurrentPathLength();
        if (totalLength == currentIndex || totalLength - 1 == currentIndex) {
            return;
        }

        PathPoint currentPoint = path.getPathPointFromIndex(currentIndex);
        PathPoint nextPoint = path.getPathPointFromIndex(currentIndex + 1);
        Vec3d delta = new Vec3d(nextPoint.x - currentPoint.x, nextPoint.y - currentPoint.y, nextPoint.z - currentPoint.z);
        this.motionX = delta.x / 7.0;
        this.motionZ = delta.z / 7.0;
    }

    //TODO: wth is void_g
    //DOTO: DefaultStuff
    public void ResetNPCTasks() {
    }

    @SideOnly(value=Side.CLIENT)
    public boolean openGuiForPlayer(EntityPlayer player) {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    protected static void openInventoryGui(EntityPlayer player, GirlEntity girl) {
        Minecraft.getMinecraft().displayGuiScreen(new GirlInventoryUI(girl, player));
    }

    @SideOnly(value=Side.CLIENT)
    protected static void openInventoryGui(EntityPlayer player, GirlEntity girl, String[] slots, ItemStack[] items, boolean flag) {
        Minecraft.getMinecraft().displayGuiScreen(new GirlInventoryUI(girl, player, slots, items, flag));
    }

    @SideOnly(value=Side.CLIENT)
    protected static void openInventoryGui(EntityPlayer player, GirlEntity girl, String[] slots, boolean flag) {
        Minecraft.getMinecraft().displayGuiScreen(new GirlInventoryUI(girl, player, slots, null, flag));
    }

    public void setHeldItemOverride(ItemStack stack) {
        this.activeItemStack = stack;
    }

    public void setItemUseCount(int count) {
        this.activeItemStackUseCount = count;
    }

    public Vec3d getPreviousPosition() {
        return new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ);
    }

    protected static Vec3d getPreviousPosition(GirlEntity girl) {
        return new Vec3d(girl.prevPosX, girl.prevPosY, girl.prevPosZ);
    }

    public GirlEntity com_trolmastercard_sexmod_em_class258_af() {
        return this;
    }

    public void goHome() {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("master", "");
            this.changeDataParameterFromClient("walk speed", WalkSpeed.WALK.toString());
        } else {
            this.entityDataManager.set(MASTER, "");
            this.entityDataManager.set(WALK_SPEED, WalkSpeed.WALK.toString());
        }
    }

    protected void alignPlayerToGirl(EntityPlayerMP player, boolean teleport) {
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
        if (teleport) {
            Vec3d pos = this.getFrontOffsetVector(0.35);
            player.setPositionAndUpdate(pos.x, pos.y, pos.z);
        }
    }

    public void snapPlayerToPosition(UUID playerUUID) {
        EntityPlayer player = this.world.getPlayerEntityByUUID(playerUUID);
        assert player != null;
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
        Vec3d pos = this.getFrontOffsetVector(0.35);
        player.setPositionAndUpdate(pos.x, pos.y, pos.z);
        this.setYawRotation(player.rotationYawHead + 180.0f);
    }

    protected void triggerActionSync(boolean param1, boolean param2, UUID playerUUID) {
        if (this.world.isRemote) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new SyncActionPacket(this.girlID(), playerUUID, param1, param2));
        } else {
            SyncActionPacket.Handler.execute(this.girlID(), playerUUID, param1, param2);
        }
    }

    public static GirlEntity getClientGirlEntity(UUID uUID) {
        if (uUID == null) {
            return null;
        }
        for (GirlEntity girl : GirlEntity.girlList(uUID)) {
            if (!girl.world.isRemote) continue;
            return girl;
        }
        return null;
    }

    public static GirlEntity getServerGirlEntity(UUID uUID) {
        if (uUID == null) {
            return null;
        }
        for (GirlEntity girl : GirlEntity.girlList(uUID)) {
            if (girl.world.isRemote) continue;
            return girl;
        }
        return null;
    }

    // TODO clashes with KoboldEntity 'void g(UUID)'
    public static ArrayList<GirlEntity> girlList(UUID uUID) {
        ArrayList<GirlEntity> list = new ArrayList<GirlEntity>();
        try {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (girl == null || !girl.girlID().equals(uUID)) continue;
                list.add(girl);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("had a ConcurrentModificationException while cycling through the girl list... hopefully nothin borke owo");
            e.printStackTrace();
        }
        return list;
    }

    protected BlockPos net_minecraft_util_math_BlockPos_a(BlockPos blockPos) {
        return this.a(blockPos, 1);
    }

    public BlockPos a(BlockPos blockPos, int n) {
        return this.findNearestStructureBlock(blockPos, n, Blocks.BED, 22, 3, null);
    }

    public void W() {
        this.entityDataManager.set(HAND_STATES, Byte.valueOf("1"));
    }

    public void K() {
        this.entityDataManager.set(HAND_STATES, Byte.valueOf("0"));
    }

    public BlockPos findNearestStructureBlock(BlockPos origin, int radius, Block targetBlock, int maxCount, int heightRange, @Nullable HashSet<Biome> allowedBiomes) {
        int step = 1;
        int direction = -1;
        BlockPos current = origin;
        int foundCount = 0;
        while (step < maxCount) {
            for (int i = 0; i < 2; ++i) {
                int y;
                int x;
                direction *= -1;
                for (x = 0; x < step; ++x) {
                    current = current.add(0, 0, direction);
                    for (y = -heightRange; y < heightRange + 1; ++y) {
                        if (this.world.getBlockState(current.add(0, y, direction)).getBlock() != targetBlock || ++foundCount < radius || allowedBiomes != null && !allowedBiomes.contains(this.world.getBiome(current.add(direction, y, 0)))) continue;
                        return current.add(0, y, direction);
                    }
                }
                for (x = 0; x < step; ++x) {
                    current = current.add(direction, 0, 0);
                    for (y = -heightRange; y < heightRange + 1; ++y) {
                        if (this.world.getBlockState(current.add(direction, y, 0)).getBlock() != targetBlock || ++foundCount < radius || allowedBiomes != null && !allowedBiomes.contains(this.world.getBiome(current.add(direction, y, 0)))) continue;
                        return current.add(direction, y, 0);
                    }
                }
                ++step;
            }
        }
        return null;
    }
    //end of deobfuscation step #3

    protected List<BlockPos> a(BlockPos blockPos, Class clazz, int n, int n2, @Nullable HashSet<Biome> hashSet) {
        int n3 = 1;
        int n4 = -1;
        BlockPos blockPos2 = blockPos;
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        while (n3 < n) {
            for (int i = 0; i < 2; ++i) {
                int n5;
                int n6;
                n4 *= -1;
                for (n6 = 0; n6 < n3; ++n6) {
                    blockPos2 = blockPos2.add(0, 0, n4);
                    for (n5 = -n2; n5 < n2 + 1; ++n5) {
                        if (!clazz.isInstance(this.world.getBlockState(blockPos2.add(0, n5, n4)).getBlock()) || hashSet != null && !hashSet.contains(this.world.getBiome(blockPos2.add(n4, n5, 0)))) continue;
                        arrayList.add(blockPos2.add(0, n5, n4));
                    }
                }
                for (n6 = 0; n6 < n3; ++n6) {
                    blockPos2 = blockPos2.add(n4, 0, 0);
                    for (n5 = -n2; n5 < n2 + 1; ++n5) {
                        if (!clazz.isInstance(this.world.getBlockState(blockPos2.add(n4, n5, 0)).getBlock()) || hashSet != null && !hashSet.contains(this.world.getBiome(blockPos2.add(n4, n5, 0)))) continue;
                        arrayList.add(blockPos2.add(n4, n5, 0));
                    }
                }
                ++n3;
            }
        }
        return arrayList;
    }

    public boolean hasMaster() {
        return !this.entityDataManager.get(MASTER).isEmpty();
    }

    @Nullable
    public UUID getMasterUUID() {
        String string = this.entityDataManager.get(MASTER);
        if (string.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public EntityPlayer getMasterPlayer() {
        UUID uUID = this.getMasterUUID();
        if (uUID == null) {
            return null;
        }
        return this.world.getPlayerEntityByUUID(uUID);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.JENNY_LOOT_TABLE;
    }

    @SideOnly(value=Side.CLIENT)
    public void doAction(String string, UUID uUID) {
    }

    // TODO rename animationPredicateHandler or whatever
    // DOTO it's called predicate
    @SideOnly(value=Side.CLIENT)
    protected abstract <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event);

    @SideOnly(value=Side.CLIENT)
    protected boolean handleActionAnimationOverrides(Action action, String animName, boolean flag, AnimationEvent event) {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    protected void createAnimation(String animName, boolean looped, AnimationEvent event, boolean bl2) {
        if (!bl2 && Action.isPlayingInteractiveAction(this, event.getPartialTick()) && this.handleActionAnimationOverrides(this.currentAction(), animName, HandlePlayerMovement.isThrusting, event)) {
            return;
        }
        ILoopType.EDefaultLoopTypes eDefaultLoopTypes = looped ? ILoopType.EDefaultLoopTypes.LOOP : ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME;
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animName, eDefaultLoopTypes));
        event.getController().transitionLengthTicks = 0.0;
    }

    @SideOnly(value=Side.CLIENT)
    protected void createAnimation(String string, boolean bl, AnimationEvent animationEvent) {
        this.createAnimation(string, bl, animationEvent, false);
    }

    @SideOnly(value=Side.CLIENT)
    protected void playRandomizedAnimation(String baseAnimNade, int maxVariants, float chance, AnimationEvent event, boolean disableThrustOverride) {

        if (!disableThrustOverride
                && Action.isPlayingInteractiveAction(this, event.getPartialTick())
                && this.handleActionAnimationOverrides(this.currentAction(), baseAnimNade, HandlePlayerMovement.isThrusting, event)
        ) {
            return;
        }

        AnimationController controller = event.getController();
        Pair state = this.animationVariantMap.get(baseAnimNade);
        if (state == null) {
            state = Pair.of((Object)0, (Object)0);
        }
        int currentVariant = (Integer)state.first();
        int previousVariant = (Integer)state.second();

        if (!Action.isPlayingInteractiveAction(this, event.getPartialTick())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(
                            currentVariant == 0
                                    ? baseAnimNade
                                    : baseAnimNade + currentVariant, ILoopType.EDefaultLoopTypes.LOOP
                            )
                    );
            event.getController().transitionLengthTicks = 0.0;
            return;
        }

        int nextVariant = this.pickRandomVariantIndex(currentVariant, previousVariant, maxVariants, chance);
        controller.setAnimation(new AnimationBuilder().addAnimation(
                nextVariant == 0
                        ? baseAnimNade
                        : baseAnimNade + nextVariant, ILoopType.EDefaultLoopTypes.LOOP));

        controller.transitionLengthTicks = 0.0;
        this.animationVariantMap.put(baseAnimNade, Pair.of(nextVariant, (nextVariant == 0 ? previousVariant : nextVariant)));
    }

    @SideOnly(value=Side.CLIENT)
    protected void playRandomizedAnimation(String baseAnimName, int maxVariants, float chance, AnimationEvent event) {
        this.playRandomizedAnimation(baseAnimName, maxVariants, chance, event, false);
    }

    // TODO probably utilized for random sounds
    // DOTO: No, it's for random variant index
    int pickRandomVariantIndex(int current, int previous, int maxVariants, float chance) {
        int next;
        if (current != 0) {
            return 0;
        }
        Random rng = this.getRNG();
        if (rng.nextFloat() > chance) {
            return 0;
        }
        while (true) {
            if (((next = rng.nextInt(maxVariants)) != previous && next != 0) || maxVariants <= 2) break;
        }
        return next;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public abstract void registerControllers(AnimationData data);

    protected void resetGirlState() {
        if (this.world.isRemote && this.isControlledByLocalPlayer()) {
            this.playerCameraOffsetPos = null;
            PackageHandler.INSTANCE.sendToServer(new ResetGirl(this.girlID(), true));
        } else if (!this.world.isRemote) {
            ResetGirl.a_inner422.a((EntityPlayerMP)this.world.getPlayerEntityByUUID(this.playerSheHasSexWith()));
        }
    }

    public static GirlEntity getCompanionInteractingWithPlayer(EntityPlayer player) {
        if (player == null) {
            return null;
        }
        return GirlEntity.getGirlByUUID(player.getPersistentID());
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d renderCustomModelTransform(Minecraft mc, CustomModelEntity entity, EntityLivingBase renderEntity, float partialTicks) {
        return CustomModelRenderer.renderTransformedModel(mc, entity, renderEntity, this, partialTicks);
    }

    public static GirlEntity getGirlByUUID(@Nonnull UUID uUID) {
        return GirlEntity.getGirlByUUID(uUID, (Boolean) null);
    }

    public static GirlEntity getGirlByUUID(@Nonnull UUID uUID, Boolean isServerSide) {
        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            if (girl.isDead || !uUID.equals(girl.playerSheHasSexWith())) continue;
            if (isServerSide == null) {
                return girl;
            }
            boolean isRemote = girl.world.isRemote;
            if (isRemote && !isServerSide) {
                return girl;
            }
            if (isRemote || !isServerSide) continue;
            return girl;
        }
        return null;
    }

    @Nullable
    public static GirlEntity getActiveSceneInfo(@Nonnull UUID uUID) {
        boolean bl = FMLCommonHandler.instance().getMinecraftServerInstance() == null;
        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            boolean bl2;
            if (girl.isDead || (bl2 = girl.world.isRemote) != bl || !uUID.equals(girl.playerSheHasSexWith()))
                continue;
            return girl;
        }
        return null;
    }

    public static GirlEntity getActiveSceneInfo(@Nonnull EntityPlayer player) {
        return GirlEntity.getActiveSceneInfo(player.getPersistentID());
    }

    @SideOnly(value=Side.CLIENT)
    public void ac() {
    }

    public void resetCameraAndPhysics() {
        this.playerCameraOffsetPos = null;
        this.setNoGravity(false);
        this.setCurrentAction((Action)null);
        if (this.world.isRemote) {
            this.resetLocalPlayerClientState();
        }
    }

    @SideOnly(value=Side.CLIENT)
    protected void resetLocalPlayerClientState() {
        if (this.isControlledByLocalPlayer()) {
            HandlePlayerMovement.setMovementLock(true);
            Minecraft.getMinecraft().player.setInvisible(false);
            PackageHandler.INSTANCE.sendToServer((IMessage)new ResetGirl(this.girlID()));
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void triggerFastSexAction(UUID uUID) {
        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            UUID id = girl.playerSheHasSexWith();
            if (id == null || !id.equals(uUID)) continue;
            Action fastSexAction = girl.FastSexAction(girl.currentAction());
            if (fastSexAction == null) {
                return;
            }
            girl.setCurrentAction(fastSexAction);
            return;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void triggerCumAction(UUID uUID) {
        for (GirlEntity girl : GirlEntity.GirlEntityList()) {
            Action cumAction;
            UUID id;
            if (girl.isDead || !girl.world.isRemote || (id = girl.playerSheHasSexWith()) == null || !id.equals(uUID) || (cumAction = girl.CumAction(girl.currentAction())) == null)
                continue;
            girl.setCurrentAction(cumAction);
        }
    }
    //end of deobfuscation part #4

    public void resetAnimationControllerOffset() {
        this.resetAnimationControllerTicks();
        PackageHandler.INSTANCE.sendToServer((IMessage)new ResetController(this.girlID()));
    }

    @SideOnly(value=Side.CLIENT)
    public void resetAnimationControllerTicks() {
        this.actionController.tickOffset = 0.0;
    }

    @SideOnly(value=Side.CLIENT)
    @Nullable
    protected abstract Action FastSexAction(Action action);

    @SideOnly(value=Side.CLIENT)
    protected abstract Action CumAction(Action action);

    public NetworkRegistry.TargetPoint getTargetNetworkPoint() {
        return new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50.0);
    }

    protected void moveCamera(double x, double y, double z, float yaw, float pitch) {
        if (this.playerSheHasSexWith() == null) {
            System.out.println("couldnt move camera because the player isn't set");
            return;
        }

        EntityPlayer player = this.world.getPlayerEntityByUUID(this.playerSheHasSexWith());
        if (this.playerCameraOffsetPos == null) {
            assert player != null;
            this.playerCameraOffsetPos = player.getPositionVector();
        }

        Vec3d newPos = this.playerCameraOffsetPos;
        newPos = newPos.add(-Math.sin((double)(this.cameraYaw + 90.0f) * (Math.PI / 180)) * x, 0.0, Math.cos((double)(this.cameraYaw + 90.0f) * (Math.PI / 180)) * x);
        newPos = newPos.add(0.0, y, 0.0);
        newPos = newPos.add(-Math.sin((double)this.cameraYaw * (Math.PI / 180)) * z, 0.0, Math.cos((double)this.cameraYaw * (Math.PI / 180)) * z);
        if (this.world.isRemote) {
            assert player != null;
            PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(player.getPersistentID().toString(), newPos, this.cameraYaw + yaw, pitch));
            return;
        }

        assert player != null;
        player.setPositionAndRotation(newPos.x, newPos.y, newPos.z, this.cameraYaw + yaw, pitch);
        player.setPositionAndUpdate(newPos.x, newPos.y, newPos.z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    @SideOnly(value=Side.CLIENT)
    protected boolean isControlledByLocalPlayer() {
        if (!this.world.isRemote) {
            return false;
        }
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        return clientPlayer.getPersistentID().equals(this.playerSheHasSexWith()) || clientPlayer.getUniqueID().equals(this.playerSheHasSexWith());
    }

    protected void U() {
    }

    public void setCustomName(String name) {
        this.entityDataManager.set(CUSTOM_NAME, name);
    }

    public String getCustomName() {
        return this.entityDataManager.get(CUSTOM_NAME);
    }

    public abstract String getGirlName();

    public String getDisplayNameText() {
        String name = this.entityDataManager.get(CUSTOM_NAME);
        if (!name.isEmpty()) {
            return name;
        }
        return this.getGirlName();
    }

    public abstract float getNameTagHeightOffset();

    @SideOnly(value=Side.CLIENT)
    public boolean shouldRenderNameTag() {
        return true;
    }

    public void broadcastChatMessage(String text) {
        if (!this.world.isRemote) {
            PackageHandler.INSTANCE.sendToAllAround((IMessage)new SendChatMessage(String.format("<%s> %s", this.getDisplayNameText(), text), this.dimension, this.girlID()), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 40.0));
        } else if (this.isControlledByLocalPlayer()) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new SendChatMessage(String.format("<%s> %s", this.getDisplayNameText(), text), this.dimension, this.girlID()));
        }
    }

    protected void b(String string, boolean bl) {
        if (!bl) {
            this.broadcastChatMessage(string);
        }
        if (!this.world.isRemote) {
            PackageHandler.INSTANCE.sendToAllAround((IMessage)new SendChatMessage(string, this.dimension, this.girlID()), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 40.0));
            return;
        }
        if (this.isControlledByLocalPlayer()) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new SendChatMessage(string, this.dimension, this.girlID()));
        }
    }

    protected void sendLocalClientMessage(String string) {
        if (this.world.isRemote) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("<%s> %s", this.getDisplayNameText(), string)));
        }
    }

    protected void a(UUID uUID, String string) {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            System.out.println("Player with UUID " + uUID.toString() + " not found");
            return;
        }
        if (this.world.isRemote) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("<" + entityPlayer.getName() + "> " + string));
        }
    }

    public void PlaySoundAtPosition(SoundEvent sound, float volume, float pitch) {
        this.world.playSound(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), sound, SoundCategory.NEUTRAL, volume, pitch, false);
    }

    public void PlaySound(SoundEvent sound) {
        this.PlaySoundAtPosition(sound, 1.0f, 1.0f);
    }

    public void PlaySound(SoundEvent[] soundEventArray, int ... nArray) {
        if (nArray.length == 0) {
            this.PlaySound(soundEventArray[this.getRNG().nextInt(soundEventArray.length)]);
            return;
        }
        this.PlaySoundAtPosition(soundEventArray[nArray[this.getRNG().nextInt(nArray.length)]], 1.0f, 1.0f);
    }

    public void PlaySound(SoundEvent[] sounds, float volume) {
        this.PlaySoundAtPosition(sounds[this.getRNG().nextInt(sounds.length)], volume, 1.0f);
    }

    public void PlaySound(SoundEvent sound, float volume) {
        this.PlaySoundAtPosition(sound, volume, 1.0f);
    }

    public static boolean isValidGirl(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (!(entity instanceof GirlEntity)) {
            return false;
        }
        return !(entity instanceof PlayerGirl);
    }

    @SideOnly(value=Side.CLIENT)
    public GirlEntity com_trolmastercard_sexmod_em_class258_E() {
        return this;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean getClosestPlayerID() {
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 50.0);
        if (entityPlayer == null) {
            return false;
        }
        return entityPlayer.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID());
    }

    public Vec3d getFrontOffsetVector() {
        return this.getFrontOffsetVector(1.0);
    }

    public Vec3d getFrontOffsetVector(double distance) {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.playerSheHasSexWith());
        assert entityPlayer != null;
        float yaw = entityPlayer.rotationYaw;
        return entityPlayer.getPositionVector().add(-Math.sin((double)yaw * (Math.PI / 180)) * distance, 0.0, Math.cos((double)yaw * (Math.PI / 180)) * distance);
    }

    public Vec3d net_minecraft_util_math_Vec3d_a(Vec3d vec3d, float f) {
        return vec3d;
    }

    public static void spawnParticlesAround(EnumParticleTypes particle, GirlEntity girl) {
        double vx = Reference.RANDOM.nextGaussian() * 0.02;
        double vy = Reference.RANDOM.nextGaussian() * 0.02;
        double vz = Reference.RANDOM.nextGaussian() * 0.02;
        girl.world.spawnParticle(particle, girl.posX + (double)(Reference.RANDOM.nextFloat() * girl.width * 2.0f) - (double)girl.width, girl.posY + 0.5 + (double)(Reference.RANDOM.nextFloat() * girl.height), girl.posZ + (double)(Reference.RANDOM.nextFloat() * girl.width * 2.0f) - (double)girl.width, vx, vy, vz, new int[0]);
    }

    public static void spawnParticlesAround(EnumParticleTypes particle, GirlEntity girl, int times) {
        for (int i = 0; i < times; ++i) {
            GirlEntity.spawnParticlesAround(particle, girl);
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    protected SoundEvent getAmbientSound() {
        if (this.getRNG().nextInt(10000) == 0) {
            if (this.world.isRemote && Minecraft.getMinecraft().player.getPositionVector().distanceTo(this.getPositionVector()) < 10.0) {
                this.sendLocalClientMessage("whopa");
            }
            return SoundsHandler.random(SoundsHandler.MISC_FART); //Why fart?
        }
        return null;
    }

    public float getLeftArmRotation() {
        return 0.0f;
    }

    public float getRightArmRotation() {
        return 0.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public MatrixStack getBoneMatrixStack(String boneName, boolean applyYaw) {
        IBone bone;
        if (this.cachedAnimationProcessor == null) {
            this.cachedAnimationProcessor = this.getAnimationProcessor();
        }
        if ((bone = this.cachedAnimationProcessor.getBone(boneName)) == null) {
            if (!GirlModel.CAMERA_PLACEMENTS.contains(boneName)) {
                Main.LOGGER.log(Level.WARN, String.format("The bone '%s' does not exist on %s. " +
                        "Bone model matrix couldn't be calculated", boneName, this.getGirlName()));
                this.boneTrackingList.remove(boneName);
            }
            return new MatrixStack();
        }
        GeoBone geoBone = (GeoBone)bone;
        ArrayList<GeoBone> boneHierarchy = new ArrayList<GeoBone>();
        {
            GeoBone parent = geoBone;
            while (parent.parent != null) {
                GeoBone geoBone2 = parent.parent;
                boneHierarchy.add(geoBone2);
                parent = geoBone2;
            }
        }
        Collections.reverse(boneHierarchy);

        MatrixStack matrixStack = new MatrixStack();
        if (this.isAnchored()) {
            ((MatrixStack)matrixStack).rotateY((float)(-Math.toRadians(this.getYawRotation())));
        } else if (applyYaw) {
            ((MatrixStack)matrixStack).rotateY((float)(-Math.toRadians(Reference.LerpFloat(this.prevRenderYawOffset, this.renderYawOffset, Minecraft.getMinecraft().getRenderPartialTicks()))));
        }

        for (GeoBone ancestor : boneHierarchy) {
            ((MatrixStack)matrixStack).translate(ancestor);
            ((MatrixStack)matrixStack).moveToPivot(ancestor);
            ((MatrixStack)matrixStack).rotate(ancestor);
            ((MatrixStack)matrixStack).scale(ancestor);
            ((MatrixStack)matrixStack).moveBackFromPivot(ancestor);
        }
        ((MatrixStack)matrixStack).translate(geoBone);
        ((MatrixStack)matrixStack).moveToPivot(geoBone);
        ((MatrixStack)matrixStack).rotate(geoBone);
        ((MatrixStack)matrixStack).scale(geoBone);
        matrixStack = this.applyAdditionalMatrixTransformations((MatrixStack)matrixStack);
        return matrixStack;
    }

    protected MatrixStack applyAdditionalMatrixTransformations(MatrixStack stack) {
        return stack;
    }
    //end of deobfuscation part 5

    @SideOnly(value=Side.CLIENT)
    public Vec3d getCachedBoneOffset(String boneName) {
        Vec3d offset = this.boneTransformCache.get(boneName);
        if (offset != null) {
            return offset;
        }
        if (!this.boneTrackingList.contains(boneName)) {
            this.boneTrackingList.add(boneName);
        }
        return Vec3d.ZERO;
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d getBoneWorldPosition(String boneName) {
        return this.getCachedBoneOffset(boneName).add(this.getPositionVector());
    }

    public void setBoneWorldPosition(String string, Vec3d vec3d) {
        this.boneTransformCache.put(string, vec3d);
    }

    @SideOnly(value=Side.CLIENT)
    public float getCameraBoneHeight() {
        AnimationProcessor<?> processor = this.getAnimationProcessor();
        IBone bone = processor.getBone("girlCam");
        if (bone == null) {
            return 0.0f;
        }
        float pivotY = bone.getPivotY();
        pivotY = this.transformCameraPivotY(pivotY);
        return pivotY / 16.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public float float_v() {
        return 1.0f;
    }

    @CheckReturnValue
    protected float transformCameraPivotY(float pivotY) {
        return pivotY;
    }

    @CheckReturnValue
    public AnimatedGeoModel getGeoModel() {
        Minecraft mc = Minecraft.getMinecraft();
        Render render = mc.getRenderManager().getEntityRenderObject(this);
        if (render == null) {
            return null;
        }
        if (!(render instanceof GirlRenderer)) {
            return null;
        }
        GeoEntityRenderer renderer = (GeoEntityRenderer)render;
        GeoModelProvider provider = renderer.getGeoModelProvider();
        if (provider == null) {
            return null;
        }
        if (!(provider instanceof AnimatedGeoModel)) {
            return null;
        }
        return (AnimatedGeoModel)provider;
    }

    @CheckReturnValue
    public AnimationProcessor<?> getAnimationProcessor() {
        return this.getGeoModel().getAnimationProcessor();
    }

    @CheckReturnValue
    public boolean h(int n) {
        ArrayList<Integer> arrayList = this.D();
        if (arrayList.size() - 1 < n) {
            return false;
        }
        return (Integer)arrayList.get(n) == 101;
    }

    @CheckReturnValue
    public Point2D g(int n) {
        return Point2D.ZERO;
    }

    public void void_a(List<Integer> list) {
        if (!(this instanceof AbstractGoblinKoboldEntity) && !(this instanceof ew_class277)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int n : list) {
            AbstractGoblinKoboldEntity.appendFixedGene(stringBuilder, n);
        }
        this.entityDataManager.set(AbstractGoblinKoboldEntity.APPEARANCE_DNA, stringBuilder.toString());
    }

    public String java_lang_String_F() {
        if (this instanceof AbstractGoblinKoboldEntity || this instanceof ew_class277) {
            return this.entityDataManager.get(AbstractGoblinKoboldEntity.APPEARANCE_DNA);
        }
        return "";
    }

    public static String c(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int n : list) {
            stringBuilder.append(n);
            stringBuilder.append("-");
        }
        return stringBuilder.toString();
    }

    public static List<Integer> c(String string) {
        String[] stringArray;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (String string2 : stringArray = string.split("-")) {
            arrayList.add(Integer.parseInt(string2));
        }
        return arrayList;
    }

    // TODO clash downstream
    public static List<Integer> h(UUID uUID) {
        GirlEntity em_class2582 = Main.proxy instanceof ClientProxy ? GirlEntity.getClientGirlEntity(uUID) : GirlEntity.getServerGirlEntity(uUID);
        ArrayList<Integer> arrayList = new ArrayList<Integer>(em_class2582.L());
        if (em_class2582 instanceof AbstractGoblinKoboldEntity || em_class2582 instanceof ew_class277) {
            arrayList.addAll(GirlEntity.c(em_class2582.getDataManager().get(AbstractGoblinKoboldEntity.APPEARANCE_DNA)));
        }
        return arrayList;
    }

    public ArrayList<Integer> L() {
        return new ArrayList<Integer>();
    }

    public List<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> d(UUID uUID) {
        if (this.customPartsData != null) {
            return this.customPartsData;
        }
        ArrayList<Integer> arrayList = this.D();
        if (arrayList.isEmpty()) {
            this.customPartsData = new ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>>();
            return this.customPartsData;
        }
        ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> arrayList2 = new ArrayList<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>>();
        List<Integer> list = GirlEntity.h(uUID);
        for (int i = 0; i < arrayList.size(); ++i) {
            //arrayList2.add(new AbstractMap.SimpleEntry(
            //        gw_class389.GIRL_SPECIFIC,
            //        new AbstractMap.SimpleEntry(this.e((Integer)var2.get(var5)), var4.get(var5))));

            arrayList2.add(
                    new AbstractMap.SimpleEntry<>(
                                    CustomPartCategory.GIRL_SPECIFIC, new AbstractMap.SimpleEntry<>(this.e(arrayList.get(i)), list.get(i))));
        }
        this.customPartsData = arrayList2;
        return arrayList2;
    }

    public void b(List<Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>>> list) {
        this.customPartsData = list;
    }

    public void a(int n, int n2) {
        if (this.customPartsData == null) {
            return;
        }
        if (this.customPartsData.size() - 1 < n) {
            return;
        }
        Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry = this.customPartsData.get(n);
        entry.getValue().setValue(n2);
        this.customPartsData.set(n, entry);
    }

    public void e(String string) {
        if (this instanceof AbstractGoblinKoboldEntity || this instanceof ew_class277) {
            this.entityDataManager.set(AbstractGoblinKoboldEntity.APPEARANCE_DNA, string);
        }
    }

    private List<String> e(int n) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < n; ++i) {
            arrayList.add("");
        }
        return arrayList;
    }

    @CheckReturnValue
    public ArrayList<Integer> D() {
        return new ArrayList<Integer>();
    }

    public List<Integer> u() {
        return new ArrayList<Integer>();
    }

    public void setCustomModelKey(String string) {
        this.entityDataManager.set(CUSTOM_MODEL_KEY, string);
    }

    public String getCustomModelKey() {
        return this.entityDataManager.get(CUSTOM_MODEL_KEY);
    }

    public static String serializePartsSet(HashSet<String> hashSet) {
        if (hashSet == null) {
            return "";
        }
        if (hashSet.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String string : hashSet) {
            sb.append(string);
            sb.append("#");
        }
        return sb.toString();
    }

    public HashSet<String> getCustomPartsSet() {
        String raw = this.getCustomModelKey();
        String[] split = raw.split("#");
        HashSet<String> set = new HashSet<String>();
        for (String string2 : split) {
            if ("".equals(string2) || "cross".equals(string2)) continue;
            set.add(string2);
        }
        return set;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean hasCustomParts() {
        return true;
    }

    public static enum WalkSpeed {
        WALK,
        FAST_WALK,
        RUN;
    }
}

