/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector2f
 *  net.minecraftforge.event.entity.living.LivingAttackEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerChangedDimensionEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  org.apache.logging.log4j.Level
 */
package com.trolmastercard.sexmod.girls.Goblin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.Packets.ResetGirl;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.companion.DoorInteractAIGoal;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.GoblinUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Point2D;
import com.trolmastercard.sexmod.util.interfaces.IGoblin;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class GoblinEntity extends AbstractNpcOnlyEntity implements IGoblin {
    final static public SkinColor DEFAULT_COLOR = SkinColor.DARK_GREEN;
    final static public Vec3i ah = new Vec3i(11, 6, 11);
    final static public Vec3d aB = new Vec3d(5.0, 1.0, 9.0);
    final static public Vec3d af = new Vec3d(3.0, -1.0, 6.0);
    final static public Vec3d ao = new Vec3d(1.0, 1.0, 5.0);
    final static public Vec3d au = new Vec3d(-6.0, -1.0, 3.0);
    final static public Vec3d aM = new Vec3d(5.0, 1.0, 1.0);
    final static public Vec3d THROW_OFFSET_W = new Vec3d(-3.0, -1.0, -6.0);
    final static public Vec3d THROW_OFFSET_U = new Vec3d(9.0, 1.0, 5.0);
    final static public Vec3d as = new Vec3d(0.0, -1.0, -4.0);
    final static public Vec3d aT = new Vec3d(1.0, -1.0, -3.0);
    final static public Vec3d ap = new Vec3d(-1.0, -1.0, -3.0);
    final static public Vec3d at = new Vec3d(6.0, -1.0, -3.0);
    final static public int aj = 39;
    final static public int ae = 15;
    final static public int aE = 8400;
    final static int aH = 45;
    final static int ad = 32000;
    final static int aw = 26;
    final static int THROW_TICKS = 205;
    final static int aL = 100;
    final static int aA = 1200;
    final static int ak = 30;
    final static int aW = 37;
    final static float aU = 2.0f;
    final static int aI = 5;
    final static int S = 100;
    final static int aq = 20;
    final static float aG = 0.825f;
    final static Vector2f aS = new Vector2f(0.5f, 0.99f);
    final static HashSet<Item> TOOLS_LIST = new HashSet<Item>(Arrays.asList(Items.GOLDEN_HOE, Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT, Items.GOLDEN_APPLE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_PICKAXE, Items.GOLDEN_SWORD, Items.GOLDEN_CARROT, Items.GOLDEN_HELMET, Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLD_INGOT, Items.GOLD_NUGGET, Item.getItemFromBlock(Blocks.GOLD_BLOCK), Item.getItemFromBlock(Blocks.GOLD_ORE)));
    final static public DataParameter<String> OWNER_UUID = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.STRING).getSerializer().createKey(122);
    final static public DataParameter<String> aK = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.STRING).getSerializer().createKey(123);
    final static public DataParameter<ItemStack> a0 = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(124);
    final static public DataParameter<Boolean> aC = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(125);
    final static public DataParameter<Boolean> aV = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(126);
    public boolean isQueen = false;
    public float ac = 0.0f;
    public long av = -1L;
    public Vec3d al = Vec3d.ZERO;
    List<UUID> T = new ArrayList<UUID>();
    int aO = 31520;
    int heldPlayerDistance = -1;
    public int throwProgress = -1;
    boolean aZ = false;
    BlockPos guardPost = null;
    int guardPostTicks = 0;
    int aa = 0;
    int throwTickCount = 0;
    int an = -1;
    int am = 0;
    long ai = 0L;
    List<GoblinEntity> ab = new ArrayList<GoblinEntity>();
    int aY = -1;
    int az = -1;
    Action currentAction = null;
    public float ar = 1.0f;
    int throwCooldown = -1;
    boolean aD = true;
    boolean aF = true;
    boolean X = false;
    String aP = "";
    boolean ay = false;

    public GoblinEntity(World world) {
        super(world);
        this.setSize(GoblinEntity.aS.x, GoblinEntity.aS.y);
    }

    public GoblinEntity(World world, @Nonnull String girlID, int modelPartIndex) {
        this(world);
        this.entityDataManager.set(aK, girlID);
        this.entityDataManager.set(APPEARANCE_DNA, this.buildModelCodeDNA(new StringBuilder(), modelPartIndex));
    }

    public GoblinEntity(World world, boolean bl, float f, Vec3d vec3d) {
        this(world);
        if (bl) {
            this.entityDataManager.set(APPEARANCE_DNA, this.buildModelCodeDNA(new StringBuilder()));
            this.ac = f;
            this.al = vec3d;
            this.isQueen = true;
            this.setTargetPosition(vec3d);
            this.setYawRotation(f);
            this.setCurrentAction(Action.SIT);
            this.setAnchored(true);
            this.setPosition(vec3d.x, vec3d.y, vec3d.z);
        }
    }

    @Override
    public void reInitTasks() {
        super.reInitTasks();
        this.setOwnerUUID(null);
        this.noClip = false;
        this.setNoGravity(false);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        EyeColor color = EyeColor.values()[this.getRNG().nextInt(EyeColor.values().length)];
        this.entityDataManager.register(ACTION_TARGET_POS, new BlockPos(color.a()));
        this.entityDataManager.register(CURRENT_ACTION, DEFAULT_COLOR.name());
        this.entityDataManager.register(OWNER_UUID, "");
        this.entityDataManager.register(aK, "");
        this.entityDataManager.register(a0, ItemStack.EMPTY);
        this.entityDataManager.register(aC, false);
        this.entityDataManager.register(aV, false);
    }

    @Override
    protected void clearBoneColors() {
        GoblinRenderer.clearBoneColors();
    }

    @Override
    public void setDead() {
        super.setDead();
        this.setOwnerUUID((UUID)null);
        if (!this.world.isRemote) {
            ItemStack heldItem = this.entityDataManager.get(a0);
            if (heldItem != ItemStack.EMPTY) {
                EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, heldItem);
                this.world.spawnEntity(entityItem);
            }
        }
    }

    @Override
    public void doAction(String string, UUID uUID) {
        if ("take ur stuff back".equals(string)) {
            this.setCurrentAction(Action.START_THROWING);
        }
        if ("use her".equals(string)) {
            this.setThrowTarget(uUID);
        }
    }

    public void setThrowTarget(UUID uUID) {
        this.aY = 0;
        BlackScreenUI.run();
        HandlePlayerMovement.setMovementLock(false);
        this.setInteractionPlayerUUID(uUID);
    }

    public void setPickupTarget(UUID uUID) {
        this.az = 0;
        BlackScreenUI.run();
        HandlePlayerMovement.setMovementLock(false);
        this.setInteractionPlayerUUID(uUID);
    }

    @Override
    public String getGirlName() {
        return "Goblin";
    }

    @Override
    public float getEyeHeight() {
        return 0.75f;
    }

    @Override
    public float getScaleFactor() {
        return 0.1f;
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        if (uuid == null) {
            this.entityDataManager.set(OWNER_UUID, "");
        } else {
            this.entityDataManager.set(OWNER_UUID, uuid.toString());
        }
    }

    @Override
    @Nullable
    public UUID getOwnerUUID() {
        String ownerStr = this.entityDataManager.get(OWNER_UUID);
        if (ownerStr.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(this.entityDataManager.get(OWNER_UUID));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getHeldPlayerDistance() {
        return this.heldPlayerDistance;
    }

    @Override
    public void setHeldPlayerDistance(int distance) {
        this.heldPlayerDistance = distance;
    }

    protected String buildModelCodeDNA(StringBuilder builder) {
        GoblinEntity.appendPaddedNumber(builder, 3);
        GoblinEntity.appendPaddedNumber(builder, 2);
        GoblinEntity.appendPaddedNumber(builder, 2);
        GoblinEntity.appendPaddedNumberWithFixedValue(builder, 7);
        GoblinEntity.appendPaddedNumberWithFixedValue(builder, 7);
        GoblinEntity.appendPaddedNumber(builder, 5);
        GoblinEntity.appendPaddedNumber(builder, HairColor.values().length - 1);
        GoblinEntity.appendPaddedNumber(builder, SkinColor.values().length - 1);
        GoblinEntity.appendPaddedNumber(builder, EyeColor.values().length - 1);
        GoblinEntity.appendPaddedNumberWithFixedValue(builder, 1);
        return builder.toString();
    }

    @Override
    protected String generateAppearanceDNA(StringBuilder dnaBuilder) {
        GoblinEntity.appendPaddedNumber(dnaBuilder, 3);
        GoblinEntity.appendPaddedNumber(dnaBuilder, 2);
        GoblinEntity.appendPaddedNumber(dnaBuilder, 2);
        GoblinEntity.appendPaddedNumber(dnaBuilder, 8);
        GoblinEntity.appendPaddedNumber(dnaBuilder, 8);
        GoblinEntity.appendPaddedNumber(dnaBuilder, 5);
        GoblinEntity.appendPaddedNumber(dnaBuilder, HairColor.values().length - 1);
        GoblinEntity.appendPaddedNumber(dnaBuilder, SkinColor.values().length - 1);
        GoblinEntity.appendPaddedNumber(dnaBuilder, EyeColor.values().length - 1);
        GoblinEntity.appendPaddedNumberWithFixedValue(dnaBuilder, 0);
        return dnaBuilder.toString();
    }

    @Override
    public ArrayList<Integer> getCustomPartIdList() {
        return new ArrayList<Integer>(){
            {
                this.add(4);
                this.add(3);
                this.add(3);
                this.add(16);
                this.add(16);
                this.add(6);
                this.add(HairColor.values().length);
                this.add(SkinColor.values().length);
                this.add(EyeColor.values().length);
            }
        };
    }

    @Override
    public List<Integer> getCustomPartExtraIdList() {
        return Collections.singletonList(2);
    }

    @Override
    public Point2D getModelPartByIndex(int index) {
        switch (index) {
            case 0: {
                return new Point2D(40, 130);
            }
            case 1: {
                return new Point2D(60, 130);
            }
            case 2: {
                return new Point2D(80, 130);
            }
            case 3: {
                return new Point2D(100, 130);
            }
            case 4: {
                return new Point2D(120, 130);
            }
            case 5: {
                return new Point2D(140, 130);
            }
            case 6: {
                return new Point2D(160, 130);
            }
            case 7: {
                return new Point2D(180, 130);
            }
            case 8: {
                return new Point2D(200, 0);
            }
            case 9: {
                return new Point2D(200, 130);
            }
            default:
                return Point2D.ZERO;
        }
    }

    @Override
    public void setCustomPartList(List<Integer> parts) {
        StringBuilder builder = new StringBuilder();
        for (int partID : parts) {
            GoblinEntity.appendPaddedNumberWithFixedValue(builder, partID);
        }
        GoblinEntity.appendPaddedNumberWithFixedValue(builder, Integer.parseInt(GoblinEntity.getModelCodeParts(this)[9]));

        this.entityDataManager.set(APPEARANCE_DNA, builder.toString());
        if (Main.proxy instanceof ClientProxy) {
            GoblinRenderer.clearBoneColors();
        }
    }

    void updateModelCodeDNA() {
        if (this.customPartsData != null) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry entry : this.customPartsData) {
                int partID = (Integer) ((Map.Entry) entry.getValue()).getValue();
                GoblinEntity.appendPaddedNumberWithFixedValue(builder, partID);
            }
            GoblinEntity.appendPaddedNumberWithFixedValue(builder, Integer.parseInt(GoblinEntity.getModelCodeParts(this)[9]));
            this.entityDataManager.set(APPEARANCE_DNA, builder.toString());
            GoblinRenderer.clearBoneColors();
        }
    }

    protected String buildModelCodeDNA(StringBuilder builder, int partIndex) {
        GoblinEntity.appendPaddedNumber(builder, 3);
        GoblinEntity.appendPaddedNumber(builder, 2);
        GoblinEntity.appendPaddedNumber(builder, 2);
        GoblinEntity.appendPaddedNumber(builder, 7);
        GoblinEntity.appendPaddedNumber(builder, 7);
        GoblinEntity.appendPaddedNumber(builder, 5);
        GoblinEntity.appendPaddedNumber(builder, HairColor.values().length - 1);
        GoblinEntity.appendPaddedNumberWithFixedValue(builder, partIndex);
        GoblinEntity.appendPaddedNumber(builder, EyeColor.values().length - 1);
        GoblinEntity.appendPaddedNumberWithFixedValue(builder, 0);
        return builder.toString();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("bodyColor", (String)this.entityDataManager.get(CURRENT_ACTION));
        nbt.setInteger("eyeColorX", ((BlockPos)this.entityDataManager.get(ACTION_TARGET_POS)).getX());
        nbt.setInteger("eyeColorY", ((BlockPos)this.entityDataManager.get(ACTION_TARGET_POS)).getY());
        nbt.setInteger("eyeColorZ", ((BlockPos)this.entityDataManager.get(ACTION_TARGET_POS)).getZ());
        nbt.setString("model", (String)this.entityDataManager.get(APPEARANCE_DNA));
        nbt.setString("girlID", (String)this.entityDataManager.get(GIRL_ID));
        nbt.setString("queen", this.entityDataManager.get(aK));
        nbt.setBoolean("isQueen", this.isQueen);
        nbt.setBoolean("isTamed", this.entityDataManager.get(aC));
        nbt.setInteger("robTicks", this.aO);
        if (this.isQueen) {
            nbt.setBoolean("preggo", this.entityDataManager.get(aV));
            nbt.setFloat("throneRot", this.ac);
            nbt.setDouble("thronePosX", this.al.x);
            nbt.setDouble("thronePosY", this.al.y);
            nbt.setDouble("thronePosZ", this.al.z);
            nbt.setLong("impregnationTick", this.av);
            for (int i = 0; i < this.T.size(); ++i) {
                nbt.setString("guard" + i, this.T.get(i).toString());
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.isQueen = nbt.getBoolean("isQueen");
        this.entityDataManager.set(APPEARANCE_DNA, nbt.getString("model"));
        this.entityDataManager.set(CURRENT_ACTION, nbt.getString("bodyColor"));
        String[] parts = GoblinEntity.getModelCodeParts(this);
        if (Integer.parseInt(parts[3]) > 7 || Integer.parseInt(parts[4]) > 7) {
            this.entityDataManager.set(APPEARANCE_DNA, this.buildModelCodeDNA(new StringBuilder(), this.getModelPartIndex()));
            Main.LOGGER.log(Level.INFO, "updated an old Goblin");
        }
        this.entityDataManager.set(ACTION_TARGET_POS, new BlockPos(nbt.getInteger("eyeColorX"), nbt.getInteger("eyeColorY"), nbt.getInteger("eyeColorZ")));
        this.entityDataManager.set(GIRL_ID, nbt.getString("girlID"));
        this.entityDataManager.set(aK, nbt.getString("queen"));
        this.entityDataManager.set(aC, nbt.getBoolean("isTamed"));
        this.aO = nbt.getInteger("robTicks");
        if (!this.isQueen) {
            return;
        }
        this.ac = nbt.getFloat("throneRot");
        this.al = new Vec3d(nbt.getDouble("thronePosX"), nbt.getDouble("thronePosY"), nbt.getDouble("thronePosZ"));
        int n = 0;
        while (!nbt.getString("guard" + n).isEmpty()) {
            this.T.add(UUID.fromString(nbt.getString("guard" + n)));
            ++n;
        }
        this.entityDataManager.set(aV, nbt.getBoolean("preggo"));
        this.av = nbt.getLong("impregnationTick");
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (this.world.isRemote) {
            return true;
        }
        if (this.isQueen) {
            return true;
        }

        if (this.getCurrentAction() == Action.RUN) {
            if ((double)this.getDistance(player) > 3.5) {
                player.sendStatusMessage(new TextComponentString("get a bit closer..."), true);
            } else {
                this.setTargetPosition(player.getPositionVector());
                this.setYawRotation(player.rotationYaw);
                this.setCurrentAction(Action.CATCH);
                this.entityDataManager.set(GIRL_HAND_STATES, "bj");
                this.setOwnerUUID(player.getPersistentID());
                this.setInteractionPlayerUUID(player.getPersistentID());
                this.getNavigator().clearPath();
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            return true;
        } else {
            if (GoblinEntity.hasGoblinWithUUID(player.getPersistentID())) {
                player.sendStatusMessage(new TextComponentString("you are already carrying a Goblin"), true);
            } else {
                this.setOwnerUUID(player.getPersistentID());
                this.setCurrentAction(Action.PICK_UP);
                this.heldPlayerDistance = 45;
                this.setAnchored(false);
                this.entityDataManager.set(aC, true);
                this.getNavigator().clearPath();
            }
            return true;
        }
    }

    public static boolean hasGoblinWithUUID(UUID uUID) {
        if (uUID == null) {
            return false;
        }
        for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
            if (girl instanceof IGoblin && !girl.world.isRemote && !girl.isDead && uUID.equals(((IGoblin) ((Object) girl)).getOwnerUUID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void initEntityAI() {
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 2.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new DoorInteractAIGoal(this));
        this.tasks.addTask(5, this.watchClosestGirlGoal);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.handleGravity();
        GoblinEntity.handlePickUpState(this);
        this.handleThrowState();
        this.goldStealCycle();
        this.void_J();
        this.queenBreed();
        this.handleJumpThrow();
        this.handleHoldCooldown();
        this.handleThrowCooldown();
        this.handleHeldParticles();
        this.handleHeldThrow();
        this.handleThrownLand();
        this.handleStandUp();
        this.handleHeldState();
    }

    @Override
    public boolean canBeCollidedWith() {
        Action action = this.getCurrentAction();
        if (action == Action.THROWN) {
            return false;
        }
        if (action == Action.RUN) {
            return super.canBeCollidedWith();
        }
        if (action == Action.AWAIT_PICK_UP) {
            return super.canBeCollidedWith();
        }
        if (this.getOwnerUUID() != null) {
            return false;
        }
        if (action != Action.NULL) {
            return false;
        }
        return super.canBeCollidedWith();
    }

    void handleGoblinOwner(EntityPlayer player) {
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(player.getPersistentID());
        Vec3d HeadPos = new Vec3d(player.posX, player.posY + (double)(playerGirl == null ? player.eyeHeight : playerGirl.getEyeHeight()), player.posZ);
        Vec3d EyePos = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        double dist = EyePos.distanceTo(HeadPos);
        double heightDiff = HeadPos.y - EyePos.y;
        this.rotationPitch = (float)(-(Math.sin(heightDiff / dist) * (180 / Math.PI)));
    }

    void handleHeldState() {
        if (this.entityDataManager.get(aC)) {
            if (this.getInteractionPlayerUUID() == null) {
                if (this.getCurrentAction() == Action.NULL) {
                    EntityPlayer player = this.world.getClosestPlayerToEntity(this, 15.0);
                    if (player != null && player.getDistance(this) < 2.0f) {
                        this.handleGoblinOwner(player);
                        this.getNavigator().clearPath();
                    } else {
                        if (this.guardPost == null || this.getDistance(this.guardPost.getX(), this.guardPost.getY(), this.guardPost.getZ()) > this.getThrowRange() || this.guardPostTicks > 100) {
                            int xOffset = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
                            int zOffset = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
                            int height = WorldUtils.getHeightAt(this.world, this.getPosition().getX() + xOffset, this.getPosition().getZ() + zOffset);
                            this.guardPost = new BlockPos(this.getPosition().getX() + xOffset, height, this.getPosition().getZ() + zOffset);
                            this.guardPostTicks = 0;
                        }
                        if (Math.sqrt(this.guardPost.distanceSq(this.getPosition())) > 2.0) {
                            this.getNavigator().tryMoveToXYZ(this.guardPost.getX(), this.guardPost.getY(), this.guardPost.getZ(), 0.3f);
                            this.tickPathVelocity();
                        } else {
                            ++this.guardPostTicks;
                        }
                    }
                }
            }
        }
    }

    double getThrowRange() {
        return Math.sqrt(800.0);
    }

    void handleStandUp() {
        if (this.getCurrentAction() == Action.STAND_UP) {
            if (++this.aa >= 37) {
                this.aa = 0;
                this.setCurrentAction(Action.NULL);
            }
        }
    }

    @Override
    public void setThrowTickCount(int ticks) {
        this.throwTickCount = ticks;
    }

    @Override
    public int getThrowTickCount() {
        return this.throwTickCount;
    }

    void handleThrownLand() {
        if (this.getCurrentAction() == Action.THROWN) {
            if (this.onGround) {
                int n = this.getThrowTickCount() + 1;
                this.setThrowTickCount(n);
                if (n >= 30) {
                    this.setThrowTickCount(0);
                    this.setCurrentAction(Action.STAND_UP);
                }
            }
        }
    }

    void handleHeldThrow() {
        if (this.isQueen) {
            if (this.entityDataManager.get(aV)) {
                if (this.av + 8400L < this.world.getTotalWorldTime()) {
                    this.entityDataManager.set(aV, false);
                }
            }
        }
    }

    void handleHeldParticles() {
        if (this.isQueen) {
            if (!this.ab.isEmpty()) {
                boolean particlesSpawned = false;
                for (GoblinEntity goblin : this.ab) {
                    if (goblin.getDataManager().get(aC)) {
                        particlesSpawned = true;
                    }
                }
                if (particlesSpawned) {
                    this.sendGirlChatMessage("Farewell my knight. You are welcome once I am breedable again.");
                    for (GoblinEntity goblin : this.ab) {
                        if (!goblin.getDataManager().get(aC)) {
                            goblin.setCurrentAction(Action.VANISH);
                        }
                    }
                    this.ab.clear();
                    this.setInteractionPlayerUUID((UUID) null);
                }
            }
        }
    }

    void handleThrowCooldown() {
        if (this.isQueen) {
            if (this.throwCooldown != -1) {
                if (++this.throwCooldown >= 100) {
                    this.throwCooldown = -1;
                    UUID uUID = this.getInteractionPlayerUUID();
                    if (uUID == null) {
                        this.resetCameraAndPhysics();
                    } else {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
                        if (player == null) {
                            this.resetCameraAndPhysics();
                        } else {
                            this.setInteractionPlayerUUID((UUID) null);

                            for (GoblinEntity goblin : this.ab) {
                                goblin.setInteractionPlayerUUID((UUID) null);
                            }

                            List<GoblinEntity> positions = this.I();
                            float yaw = this.ac + 180.0f;
                            Vec3d leftPos = this.al.add(GoblinEntity.rotateVectorYaw(aT, yaw));
                            Vec3d rightPos = this.al.add(GoblinEntity.rotateVectorYaw(ap, yaw));
                            Vec3d playerPos = this.al.add(GoblinEntity.rotateVectorYaw(as, yaw));
                            GoblinEntity leftGoblin = (GoblinEntity) positions.get(0);
                            GoblinEntity rightGoblin = (GoblinEntity) positions.get(1);
                            leftGoblin.setTargetPosition(leftPos);
                            rightGoblin.setTargetPosition(rightPos);
                            leftGoblin.setYawRotation(0.0f);
                            rightGoblin.setYawRotation(0.0f);
                            leftGoblin.setAnchored(true);
                            rightGoblin.setAnchored(true);
                            leftGoblin.setCurrentAction(Action.AWAIT_PICK_UP);
                            rightGoblin.setCurrentAction(Action.AWAIT_PICK_UP);
                            leftGoblin.setNoGravity(false);
                            rightGoblin.setNoGravity(false);
                            player.setNoGravity(false);
                            leftGoblin.noClip = false;
                            rightGoblin.noClip = false;
                            player.noClip = false;
                            player.rotationYaw = yaw;
                            player.rotationPitch = 30.0f;
                            player.setPositionAndUpdate(playerPos.x, playerPos.y, playerPos.z);
                            PacketHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(true), (EntityPlayerMP) player);
                            this.sendGirlChatMessage("Thanks to you, my clan is soon going to get a few new members! In return I will bear of one of my guards to serve as your personal Onahole. Choose wisely~");
                        }
                    }
                }
            }
        }
    }

    void handleHoldCooldown() {
        if (this.isQueen) {
            if (this.an != -1) {
                if (++this.an >= 205) {
                    this.an = -1;
                    UUID uUID = this.getInteractionPlayerUUID();
                    if (uUID != null) {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
                        if (player != null) {
                            Vec3d pos = GoblinEntity.rotateVectorYaw(new Vec3d(0.0, 0.15625 - (double) player.getEyeHeight(), -0.8859375), this.ac - 180.0f);
                            pos = pos.add(this.getTargetPosition());
                            player.setPositionAndUpdate(pos.x, pos.y, pos.z);
                        }
                    }
                }
            }
        }
    }

    public static Vec3d rotateVectorYaw(Vec3d vec, float yaw) {
        return GoblinEntity.rotateVectorPitchYaw(vec, 0.0f, yaw);
    }

    public static Vec3d rotateVectorPitchYaw(Vec3d vec, float pitch, float yaw) {
        Vec3d rotated = new Vec3d(vec.x, vec.y * Math.cos((double)pitch * (Math.PI / 180)) - vec.z * Math.sin((double)pitch * (Math.PI / 180)), vec.y * Math.sin((double)pitch * (Math.PI / 180)) + vec.z * Math.cos((double)pitch * (Math.PI / 180)));
        return new Vec3d(-Math.sin((double)(yaw + 90.0f) * (Math.PI / 180)) * rotated.x - Math.sin((double)yaw * (Math.PI / 180)) * rotated.z, rotated.y, Math.cos((double)(yaw + 90.0f) * (Math.PI / 180)) * rotated.x + Math.cos((double)yaw * (Math.PI / 180)) * rotated.z);
    }

    void handleJumpThrow() {
        GoblinEntity goblin;
        Vec3d throwPos;
        if (this.isQueen) {
            if (this.getCurrentAction() == Action.JUMP_0) {
                if (++this.am >= 26) {
                    this.am = 0;
                    switch ((int) this.ac) {
                        case 90: {
                            throwPos = this.al.add(au);
                            break;
                        }
                        case 180: {
                            throwPos = this.al.add(THROW_OFFSET_W);
                            break;
                        }
                        case -90: {
                            throwPos = this.al.add(at);
                            break;
                        }
                        default: {
                            throwPos = this.al.add(af);
                        }
                    }

                    UUID uUID = this.getInteractionPlayerUUID();
                    if (uUID != null) {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
                        if (player != null) {
                            this.setTargetPosition(throwPos);
                            this.setYawRotation(this.ac);
                            this.setCurrentAction(Action.BREEDING_INTRO_0);
                            this.noClip = true;
                            this.setNoGravity(true);
                            Vec3d pos = rotateVectorYaw(new Vec3d(0.0, 0.44375 - (double) player.eyeHeight, -0.7875), this.ac - 180.0f);
                            player.noClip = true;
                            player.setNoGravity(true);
                            player.setPositionAndUpdate(pos.x + throwPos.x, pos.y + throwPos.y, pos.z + throwPos.z);
                            List<GoblinEntity> goblins = this.I();
                            if (goblins.size() >= 1) {
                                goblin = goblins.get(0);
                                goblin.setTargetPosition(throwPos);
                                goblin.setYawRotation(this.ac);
                                goblin.setCurrentAction(Action.BREEDING_INTRO_1);
                                goblin.noClip = true;
                                goblin.setNoGravity(true);
                            }
                            if (goblins.size() >= 2) {
                                goblin = goblins.get(1);
                                goblin.setTargetPosition(throwPos);
                                goblin.setYawRotation(this.ac);
                                goblin.setCurrentAction(Action.BREEDING_INTRO_2);
                                goblin.noClip = true;
                                goblin.setNoGravity(true);
                            }
                            this.an = 0;
                        }
                    }
                }
            }
        }
    }

    AxisAlignedBB createThrowHitbox(Vec3d min, Vec3d max) {
        return new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    void queenBreed() {
        if (this.isQueen) {
            if (this.getInteractionPlayerUUID() == null) {
                Vec3d basePos = null;
                switch ((int) this.ac) {
                    case 0: {
                        basePos = aM;
                        break;
                    }
                    case 90: {
                        basePos = THROW_OFFSET_U;
                        break;
                    }
                    case 180: {
                        basePos = aB;
                        break;
                    }
                    case -90: {
                        basePos = ao;
                    }
                }

                if (basePos != null) {
                    Vec3d offset = this.al.subtract(0.5, 0.0, 0.5).subtract(basePos);
                    AxisAlignedBB aabb = this.createThrowHitbox(offset, offset.add(ah.getX(), ah.getY(), ah.getZ()));
                    List<EntityPlayer> players = this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                    if (!players.isEmpty()) {
                        EntityPlayer player = players.get(0);
                        if (player.onGround) {
                            if (this.entityDataManager.get(aV)) {
                                if (this.ai + 1200L < this.world.getTotalWorldTime()) {
                                    player.sendStatusMessage(new TextComponentString("The Queen is still pregnant - so no breeding for you uwu"), true);
                                    this.ai = this.world.getTotalWorldTime();
                                }
                            } else {
                                UUID uUID = player.getPersistentID();
                                Vec3d pos = player.getPositionVector();
                                float yaw = player.rotationYaw + 180.0f;
                                PacketHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(false), (EntityPlayerMP) player);
                                this.setInteractionPlayerUUID(uUID);
                                this.setCurrentAction(Action.JUMP_0);
                                this.setTargetPosition(pos);
                                this.setYawRotation(yaw);
                                this.setAnchored(true);
                                List<GoblinEntity> list2 = this.I();
                                if (!list2.isEmpty()) {
                                    GoblinEntity goblin = list2.get(0);
                                    goblin.setInteractionPlayerUUID(uUID);
                                    goblin.setCurrentAction(Action.JUMP_1);
                                    goblin.setTargetPosition(pos);
                                    goblin.setYawRotation(yaw);
                                    goblin.setAnchored(true);
                                    if (list2.size() > 1) {
                                        GoblinEntity goblin1 = list2.get(1);
                                        goblin1.setInteractionPlayerUUID(uUID);
                                        goblin1.setCurrentAction(Action.JUMP_2);
                                        goblin1.setTargetPosition(pos);
                                        goblin1.setYawRotation(yaw);
                                        goblin1.setAnchored(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    List<GoblinEntity> I() {
        //GoblinEntity e3_class21922;
        if (this.ab.size() > 1) {
            return this.ab;
        }
        for (GoblinEntity goblin : this.ab) {
            this.world.removeEntity(goblin);
        }
        this.ab.clear();
        GoblinEntity goblin = new GoblinEntity(this.world, this.girlID().toString(), this.getModelPartIndex());
        goblin.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(goblin);
        this.ab.add(goblin);
        GoblinEntity goblin1 = new GoblinEntity(this.world, this.girlID().toString(), this.getModelPartIndex());
        goblin1.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(goblin1);
        this.ab.add(goblin1);
        return this.ab;
    }

    void handleGravity() {
        if (this.aZ) {
            return;
        }
        this.noClip = false;
        this.setNoGravity(false);
        if (!(this.isQueen || this.entityDataManager.get(aC) || this.entityDataManager.get(aK).isEmpty() || this.getCurrentAction() != Action.NULL)) {
            this.world.removeEntity(this);
        }
        this.aZ = true;
    }


    void updateThrowProgress() {
        GoblinEntity goblin = this;
        int progress = goblin.getThrowProgress();
        if (progress != -1) {
            goblin.setThrowProgress(++progress);
            if (progress == 15) {
                Vec3d pos = getGoblinThrowPos(this);
                float pitch = getGoblinThrowHeight(this);
                float yaw = getGoblinThrowDistance(this);
                this.setPositionAndUpdate(pos.x, pos.y, pos.z);
                Vec3d vec = GoblinEntity.rotateVectorPitchYaw(new Vec3d(0.0, 0.0, 1.5), pitch, yaw);
                this.motionX = vec.x;
                this.motionY = vec.y;
                this.motionZ = vec.z;
                if (!this.world.isRemote) {
                    this.setYawRotation(yaw);
                }
            }
            this.noClip = false;
            this.setNoGravity(false);
            if (progress == 39) {
                this.setThrowProgress(-1);
                this.setCurrentAction(Action.THROWN);
                this.setInteractionPlayerUUID((UUID) null);
                this.setOwnerUUID((UUID) null);
            }
        }
    }

    public static Vec3d getGoblinThrowPos(GirlEntity girl) {
        IGoblin goblin = (IGoblin)((Object)girl);
        UUID uUID = goblin.getOwnerUUID();
        if (uUID == null) {
            return girl.getPositionVector();
        }
        EntityPlayer player = girl.world.getPlayerEntityByUUID(uUID);
        return player == null ? girl.getPositionVector() : player.getPositionVector().add(0.0, player.getEyeHeight(), 0.0).add(GoblinEntity.rotateVectorPitchYaw(new Vec3d(0.4f, 0.0, 0.0), GoblinEntity.getGoblinThrowHeight(girl), GoblinEntity.getGoblinThrowDistance(girl)));
    }

    public static float getGoblinThrowDistance(GirlEntity girl) {
        IGoblin goblin = (IGoblin)((Object)girl);
        UUID uUID = goblin.getOwnerUUID();
        if (uUID == null) {
            return 0.0f;
        }
        EntityPlayer player = girl.world.getPlayerEntityByUUID(uUID);
        return player == null ? 0.0f : player.rotationYawHead;
    }

    public static float getGoblinThrowHeight(GirlEntity girl) {
        IGoblin goblin = (IGoblin)((Object)girl);
        UUID uUID = goblin.getOwnerUUID();
        if (uUID == null) {
            return 0.0f;
        }
        EntityPlayer player = girl.world.getPlayerEntityByUUID(uUID);
        return player == null ? 0.0f : player.rotationPitch;
    }

    void void_J() {
        //boolean bl = false;
        if (this.onGround) {
            if (this.getCurrentAction() == Action.RUN) {
                EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 100.0);
                if (entityPlayer != null) {
                    double distance = 20.0;
                    while (!(distance <= 0.0)) {
                            Vec3d delta = this.getPositionVector().subtract(entityPlayer.getPositionVector());
                            Vec3d absDelta = new Vec3d(Math.abs(delta.x), Math.abs(delta.y), Math.abs(delta.z));
                            double xWeight = absDelta.x / (absDelta.x + absDelta.z);
                            double zWeight = absDelta.z / (absDelta.x + absDelta.z);
                            Vec3d targetPos = this.getPositionVector().add(new Vec3d((double) (delta.x > 0.0 ? 1 : -1) * xWeight * distance, 0.0, (double) (delta.z > 0.0 ? 1 : -1) * zWeight * distance));
                            PathNavigate navigator = this.getNavigator();
                            navigator.clearPath();
                            boolean moved = navigator.tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, 0.825f);
                            distance -= 1.0;
                            if (moved)
                                return;
                    }
                }
            }
        }
    }

    @Override
    protected void jump() {
        if (this.getCurrentAction() != Action.RUN || this.hasValidPath()) {
            super.jump();
        }
    }

    boolean hasValidPath() {
        PathNavigate navigator = this.getNavigator();
        Path path = navigator.getPath();
        if (path == null) {
            return true;
        } else {
            int currentIndex = path.getCurrentPathIndex();
            int length = path.getCurrentPathLength();
            if (length != currentIndex && length - 1 != currentIndex) {
                PathPoint currentPoint = path.getPathPointFromIndex(currentIndex);
                PathPoint nextPoint = path.getPathPointFromIndex(currentIndex + 1);
                return nextPoint.y - currentPoint.y == 1;
            } else {
                return true;
            }
        }
    }

    void goldStealCycle() {
        if (this.isQueen) {
            if (!this.entityDataManager.get(aC)) {
                if (!this.entityDataManager.get(aV)) {
                    if (this.getCurrentAction() == Action.SIT) {
                        if (++this.aO >= 32000) {
                            EntityPlayer player = this.world.getClosestPlayerToEntity(this, 3000.0);
                            if (player != null) {
                                if (player.onGround) {
                                    if (!player.isAirBorne) {
                                        Integer slotIndex = this.findThrowTarget(player);
                                        if (slotIndex != null) {
                                            Vec3d playerPos = player.getPositionVector();
                                            Vec3d selfPos = this.getPositionVector();
                                            Vec3d delta = playerPos.subtract(selfPos);
                                            double dist = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
                                            if (!(dist > 100.0)) {
                                                ItemStack stolenStack = player.inventory.getStackInSlot(slotIndex).copy();
                                                GoblinEntity goblin = new GoblinEntity(this.world, this.girlID().toString(), this.getModelPartIndex());
                                                Vec3d offset = GoblinEntity.rotateVectorYaw(new Vec3d(0.0, 0.0, -0.2f), player.rotationYawHead);
                                                goblin.setPosition(player.posX + offset.x, player.posY, player.posZ + offset.z);
                                                goblin.setCurrentAction(Action.RUN);
                                                this.world.spawnEntity(goblin);
                                                goblin.entityDataManager.set(a0, stolenStack);
                                                player.sendMessage(new TextComponentString(String.format("<%s> I got your %s hehe~", goblin.getGirlName(), stolenStack.getDisplayName())));
                                                player.inventory.removeStackFromSlot(slotIndex);
                                                this.aO = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    int getModelPartIndex() {
        return Integer.parseInt(GoblinEntity.getModelCodeParts(this)[7]);
    }

    @Nullable
    Integer findThrowTarget(EntityPlayer player) {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        ArrayList<Integer> validSlots = new ArrayList<Integer>();
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack stack = inventory.get(i);
            if (stack == ItemStack.EMPTY || !TOOLS_LIST.contains(stack.getItem())) continue;
            validSlots.add(i);
        }
        return validSlots.isEmpty() ? null : (Integer) validSlots.get(this.getRNG().nextInt(validSlots.size()));
    }

    void handleThrowState() {
        if (this.isQueen) {
            if (this.getInteractionPlayerUUID() == null) {
                this.setTargetPosition(this.al);
                this.setYawRotation(this.ac);
                this.setAnchored(true);
                this.setNoGravity(true);
                this.setCurrentAction(Action.SIT);
            }
        }
    }

    @Override
    public void onUpdate() {
        this.updateModelCodeDNA();
        handleGoblinThrowAction(this);
        this.updateThrowProgress();
        if (this.getOwnerUUID() != null) {
            this.inPortal = false;
        }
        super.onUpdate();
        this.handleShoulderIdle();
        this.void_H();
        this.void_F();
        if (this.world.isRemote) {
            this.handleHoldTick();
            this.void_A();
            if (this.getOwnerUUID() != null) {
                this.noClip = true;
            }
        }
    }

    @Override
    public Action getPreviousAction() {
        return this.currentAction;
    }

    @Override
    public void setPreviousAction(Action action) {
        this.currentAction = action;
    }

    @Override
    public void setThrowProgress(int progress) {
        this.throwProgress = progress;
    }

    @Override
    public int getThrowProgress() {
        return this.throwProgress;
    }

    public static void handleGoblinThrowAction(GirlEntity girl) {
        Action action = girl.getCurrentAction();
        IGoblin goblin = (IGoblin)girl;
        if (goblin.getPreviousAction() != Action.START_THROWING && action == Action.START_THROWING) {
            goblin.setThrowProgress(0);
        }
        goblin.setPreviousAction(action);
    }

    @Override
    public void setFire(int seconds) {
        if (this.getOwnerUUID() == null) {
            super.setFire(seconds);
        }
    }

    void void_F() {
        if (this.getCurrentAction() == Action.VANISH) {
            this.ar -= 0.05f;
            if (!(this.ar > 0.0f)) {
                this.world.removeEntity(this);
            }
        }
    }

    void void_H() {
        if (!this.entityDataManager.get(aC)) {
            if (this.getCurrentAction() == Action.THROWN) {
                if (this.onGround || this.isInWater()) {
                    this.ar = (float) ((double) this.ar - 0.05);
                    if (!(this.ar > 0.0f)) {
                        if (!this.world.isRemote) {
                            this.setCurrentAction(Action.NULL);
                            this.setInteractionPlayerUUID((UUID) null);
                            this.setOwnerUUID((UUID) null);
                            this.world.removeEntity(this);
                        }
                    }
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void handleHoldTick() {
        if (this.aY != -1) {
            if (++this.aY == 15) {
                this.aY = -1;
                this.setCurrentAction(Action.PAIZURI_START);
                Minecraft.getMinecraft().player.closeScreen();
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void void_A() {
        if (this.az != -1) {
            if (++this.az == 15) {
                this.az = -1;
                this.setCurrentAction(Action.NELSON_INTRO);
                Minecraft minecraft = Minecraft.getMinecraft();
                minecraft.player.closeScreen();
                minecraft.gameSettings.thirdPersonView = 2;
            }
        }
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (currentAction != Action.PAIZURI_CUM || (action != Action.PAIZURI_SLOW && action != Action.PAIZURI_FAST)) {
            if (currentAction != Action.NELSON_CUM || (action != Action.NELSON_SLOW && action != Action.NELSON_FAST)) {
                if (currentAction != Action.BREEDING_CUM_0 || (action != Action.BREEDING_SLOW_0 && action != Action.BREEDING_FAST_0)) {
                    if (action == Action.START_THROWING && !this.world.isRemote) {
                        this.setInteractionPlayerUUID(this.getOwnerUUID());
                        this.L_();
                    }
                    if (action == Action.PAIZURI_START && !this.world.isRemote) {
                        this.handlePlayerInteract();
                    }
                    if (action == Action.NELSON_INTRO && !this.world.isRemote) {
                        this.handlePlayerLook();
                    }
                    if (this.getCurrentAction() == Action.PAIZURI_CUM && action == Action.NULL && !this.world.isRemote) {
                        this.D_();
                    }
                    if (action == Action.BREEDING_CUM_0) {
                        this.entityDataManager.set(aV, true);
                        this.av = this.world.getTotalWorldTime();
                        this.ai = this.world.getTotalWorldTime();
                    }
                    if (action == Action.BREEDING_CUM_0) {
                        this.throwCooldown = 0;
                    }
                    if (action == Action.NELSON_CUM) {
                        this.entityDataManager.set(aV, true);
                    }
                    if (currentAction == Action.NELSON_CUM && action != Action.NELSON_CUM) {
                        this.entityDataManager.set(aV, false);
                    }
                    super.setCurrentAction(action);
                }
            }
        }
    }

    void D_() {
        EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
        if (player != null) {
            ResetGirl.EventHandler.resetGirls((EntityPlayerMP)player);
        }
        this.setInteractionPlayerUUID((UUID)null);
        this.setAnchored(false);
        this.noClip = false;
        this.setNoGravity(false);
        this.entityDataManager.set(a0, ItemStack.EMPTY);
        if (!this.entityDataManager.get(aC)) {
            this.setPositionAndUpdate(this.homeCoords.x, this.homeCoords.y, this.homeCoords.z);
            this.world.removeEntity(this);
        }
    }

    void handlePlayerLook() {
        EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
        if (player != null) {
            this.setOwnerUUID((UUID) null);
            this.setTargetPosition(player.getPositionVector());
            this.setYawRotation(player.rotationYaw);
            this.setAnchored(true);
            this.noClip = true;
            this.setNoGravity(true);
            player.setNoGravity(true);
            player.noClip = true;
            this.setInteractionPlayerUUID(player.getPersistentID());
        }
    }

    void handlePlayerInteract() {
        EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
        if (player != null) {
            this.setOwnerUUID((UUID) null);
            this.setTargetPosition(player.getPositionVector());
            this.setYawRotation(player.rotationYaw + 180.0f);
            this.setAnchored(true);
            this.noClip = true;
            this.setNoGravity(true);
            player.setNoGravity(true);
            player.noClip = true;
            this.setInteractionPlayerUUID(player.getPersistentID());
            player.setPositionAndUpdate(player.posX, player.posY - 0.5, player.posZ);
            player.rotationPitch = 70.0f;
            player.prevRotationPitch = 70.0f;
        }
    }

    void L_() {
        ItemStack heldItem = this.entityDataManager.get(a0);
        if (heldItem != ItemStack.EMPTY) {
            EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
            if (player != null) {
                player.inventory.addItemStackToInventory(heldItem.copy());
                this.entityDataManager.set(a0, ItemStack.EMPTY);
            }
        }
    }

    public static void handlePickUpState(GirlEntity girl) {
        if (girl.getCurrentAction() == Action.PICK_UP) {
            IGoblin goblin = (IGoblin)girl;
            UUID uUID = goblin.getOwnerUUID();
            if (uUID == null) {
                goblin.setHeldPlayerDistance(-1);
                girl.setCurrentAction(Action.NULL);
                goblin.setOwnerUUID((UUID) null);
                //return;
            } else {
                EntityPlayer player = girl.world.getPlayerEntityByUUID(uUID);
                if (player == null) {
                    goblin.setHeldPlayerDistance(-1);
                    girl.setCurrentAction(Action.NULL);
                    goblin.setOwnerUUID((UUID) null);
                    //return;
                } else {
                    girl.setPosition(player.posX, player.posY, player.posZ);
                    if (girl.getPositionVector().distanceTo(player.getPositionVector()) > 10.0) {
                        goblin.setHeldPlayerDistance(-1);
                        girl.setCurrentAction(Action.NULL);
                        goblin.setOwnerUUID((UUID) null);
                        //return;
                    } else {
                        int distance = goblin.getHeldPlayerDistance() - 1;
                        goblin.setHeldPlayerDistance(distance);
                        if (distance == 0) {
                            girl.setCurrentAction(Action.SHOULDER_IDLE);
                            girl.noClip = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean shouldRenderNameTag() {
        return this.getCurrentAction() == Action.NULL
                && this.getOwnerUUID() == null && (this.entityDataManager.get(aC)
                || Minecraft.getMinecraft().player.canEntityBeSeen(this))
                && this.getOwnerUUID() == null;
    }

    void handleShoulderIdle() {
        if (this.getCurrentAction() == Action.SHOULDER_IDLE) {
            UUID uUID = this.getOwnerUUID();
            if (uUID != null) {
                EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
                if (player != null) {
                    this.setPosition(player.posX, player.posY, player.posZ);
                    this.noClip = true;
                    this.setNoGravity(true);
                }
            }
        }
    }

    @Override
    protected Action getNextAction(Action action) {
        switch (action) {
            case PAIZURI_IDLE: 
            case PAIZURI_SLOW: {
                return Action.PAIZURI_FAST;
            }
            case BREEDING_SLOW_0: {
                return Action.BREEDING_FAST_0;
            }
            case BREEDING_SLOW_2: {
                return Action.BREEDING_FAST_2;
            }
            case NELSON_SLOW: {
                return Action.NELSON_FAST;
            }
            default:
                return null;
        }
    }

    @Override
    protected Action getCumAction(Action action) {
        switch (action) {
            case PAIZURI_SLOW: 
            case PAIZURI_FAST: 
            case PAIZURI_FAST_CONTINUES: {
                return Action.PAIZURI_CUM;
            }
            case BREEDING_1: {
                return Action.BREEDING_CUM_1;
            }
            case BREEDING_SLOW_2: 
            case BREEDING_FAST_2: {
                return Action.BREEDING_CUM_2;
            }
            case NELSON_SLOW: 
            case NELSON_FAST: {
                return Action.NELSON_CUM;
            }
            case BREEDING_SLOW_0: 
            case BREEDING_FAST_0: {
                for (GoblinEntity goblin : this.ab) {
                    goblin.getCumAction(action);
                }
                return Action.BREEDING_CUM_0;
            }
            default:
                return null;
        }
    }

    public boolean boolean_C() {
        Block block = this.world.getBlockState(this.getPosition().add(0, 1, 0)).getBlock();
        return !block.isPassable(this.world, this.getPosition().add(0, 1, 0));
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        Action action = this.getCurrentAction();
        if (action != Action.THROWN && action != Action.START_THROWING) {
            super.fall(distance, damageMultiplier);
        }
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        
         switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, event);
                    break;
                }
                this.createAnimation("animation.goblin.blink", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, event);
                    break;
                }
                double d = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                if (!((Boolean)this.entityDataManager.get(IS_ANCHORED)).booleanValue() && d > 0.0) {
                    if (this.onGround && Math.abs(Math.abs(this.prevPosY) - Math.abs(this.posY)) < (double)0.1f) {
                        if (d > (double)0.2f) {
                            this.createAnimation("animation.goblin.walk", true, event);
                        } else {
                            this.createAnimation("animation.goblin.walk", true, event);
                        }
                        this.rotationYaw = this.rotationYawHead;
                        break;
                    }
                    this.createAnimation("animation.goblin.fly", true, event);
                    break;
                }
                this.createAnimation("animation.goblin.idle", true, event);
                break;
            }
            case "action": {
                Minecraft mc = Minecraft.getMinecraft();
                String camMode = mc.player.getPersistentID().equals(this.getOwnerUUID()) && mc.gameSettings.thirdPersonView == 0 ? "1" : "3";
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.goblin.null", true, event);
                        break ;
                    }
                    case SHOULDER_IDLE: {
                        this.createAnimation("animation.goblin.shoulder_idle", true, event);
                        break ;
                    }
                    case PICK_UP: {
                        this.createAnimation(String.format("animation.goblin.pick_up_%sperson", camMode), true, event);
                        break ;
                    }
                    case SIT: {
                        this.createAnimation("animation.goblin.sit", true, event);
                        break ;
                    }
                    case RUN: {
                        if (this.onGround) {
                            this.createAnimation("animation.goblin.running", true, event);
                            break ;
                        }
                        this.createAnimation("animation.goblin.fly", true, event);
                        break ;
                    }
                    case CATCH: {
                        this.createAnimation(String.format("animation.goblin.catch_%sperson", camMode), true, event);
                        break ;
                    }
                    case CATCH_BJ: {
                        this.createAnimation(String.format("animation.goblin.catch_%spersonBj", camMode), true, event);
                        break ;
                    }
                    case CATCH_BJ_IDLE: {
                        this.createAnimation(String.format("animation.goblin.catch_%spersonBj_idle", camMode), true, event);
                        break ;
                    }
                    case START_THROWING: {
                        this.createAnimation(String.format("animation.goblin.throw_%sperson", camMode), true, event);
                        break ;
                    }
                    case THROWN: {
                        this.createAnimation("animation.goblin.thrown", true, event);
                        break ;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.goblin.paizuri_start", true, event);
                        break ;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.goblin.paizuri_slow" + this.aP, true, event);
                        break ;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.goblin.paizuri_fast", true, event);
                        break ;
                    }
                    case PAIZURI_FAST_CONTINUES: {
                        this.createAnimation("animation.goblin.paizuri_fast_countinues", true, event);
                        break ;
                    }
                    case PAIZURI_IDLE: {
                        this.createAnimation("animation.goblin.paizuri_idle", true, event);
                        break ;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.goblin.paizuri_cum", true, event);
                        break ;
                    }
                    case JUMP_0: {
                        this.createAnimation("animation.goblin.jump_1", true, event);
                        break ;
                    }
                    case JUMP_1: {
                        this.createAnimation("animation.goblin.jump_2", true, event);
                        break ;
                    }
                    case JUMP_2: {
                        this.createAnimation("animation.goblin.jump_3", true, event);
                        break ;
                    }
                    case BREEDING_INTRO_0: {
                        this.createAnimation("animation.goblin.breeding_intro_1", true, event);
                        break ;
                    }
                    case BREEDING_INTRO_1: {
                        this.createAnimation("animation.goblin.breeding_intro_2", true, event);
                        break ;
                    }
                    case BREEDING_INTRO_2: {
                        this.createAnimation("animation.goblin.breeding_intro_3", true, event);
                        break ;
                    }
                    case BREEDING_SLOW_0: {
                        this.createAnimation("animation.goblin.breeding_slow_1" + (this.aD ? "l" : "r"), true, event);
                        break ;
                    }
                    case BREEDING_SLOW_2: {
                        this.createAnimation("animation.goblin.breeding_slow_3", true, event);
                        break ;
                    }
                    case BREEDING_FAST_0: {
                        this.createAnimation("animation.goblin.breeding_fast_1" + (this.ay ? "c" : "s"), true, event);
                        break ;
                    }
                    case BREEDING_FAST_2: {
                        this.createAnimation("animation.goblin.breeding_fast_3", true, event);
                        break ;
                    }
                    case BREEDING_CUM_0: {
                        this.createAnimation("animation.goblin.breeding_cum_1", true, event);
                        break ;
                    }
                    case BREEDING_CUM_1: {
                        this.createAnimation("animation.goblin.breeding_cum_2", true, event);
                        break ;
                    }
                    case BREEDING_CUM_2: {
                        this.createAnimation("animation.goblin.breeding_cum_3", true, event);
                        break ;
                    }
                    case BREEDING_1: {
                        this.createAnimation("animation.goblin.breeding_2", true, event);
                        break ;
                    }
                    case VANISH: 
                    case AWAIT_PICK_UP: {
                        this.createAnimation("animation.goblin.await_pick_up", true, event);
                        break ;
                    }
                    case STAND_UP: {
                        this.createAnimation("animation.goblin.stand_up", false, event);
                        break ;
                    }
                    case NELSON_INTRO: {
                        this.createAnimation("animation.goblin.nelson_intro", true, event);
                        break ;
                    }
                    case NELSON_SLOW: {
                        this.createAnimation("animation.goblin.nelson_slow" + (this.aF ? "" : "2"), true, event);
                        break ;
                    }
                    case NELSON_FAST: {
                        this.createAnimation("animation.goblin.nelson_fast" + (this.X ? "c" : "s"), true, event);
                        break ;
                    }
                    case NELSON_CUM: {
                        this.createAnimation("animation.goblin.nelson_cum", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "catchEh": {
                    this.sendChatMessage("ehh..");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchAkward": {
                    this.sendChatMessage("awkward..");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchWell": {
                    this.sendChatMessage("well...");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchRather": {
                    this.sendChatMessage("would you rather have this stupid... thing?");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchMe": {
                    this.sendChatMessage("...or use me?~");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchDone": {
                    if (!"bj".equals(this.entityDataManager.get(GIRL_HAND_STATES))) break;
                    this.setCurrentAction(Action.CATCH_BJ);
                    break;
                }
                case "catchBjDone": {
                    this.setCurrentAction(Action.CATCH_BJ_IDLE);
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    GoblinEntity.openInventoryGui(entityPlayerSP, this, new String[]{"use her", "take ur stuff back"}, null, false);
                    break;
                }
                case "paizuriChoice": {
                    this.sendChatMessage("good choice!~");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriBoth": {
                    this.sendChatMessage("...for both of us!");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizruiUse": {
                    this.sendChatMessage("now use me like a fuck toy!~");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriSwitch": {
                    if (this.getRNG().nextBoolean()) break;
                    this.aP = "".equals(this.aP) ? "2" : "";
                    break;
                }
                case "touch": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_TOUCH, 3.0f);
                    break;
                }
                case "pound": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "paizuri_startDone": {
                    this.setCurrentAction(Action.PAIZURI_IDLE);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "paizuriFastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    break;
                }
                case "paizuriFastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PAIZURI_FAST_CONTINUES);
                    break;
                }
                case "paizuriFastContinuesReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "smallPound": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_POUNDING, 0.25f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "paizruiCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    entityPlayerSP.rotationPitch = 70.0f;
                    entityPlayerSP.prevRotationPitch = 70.0f;
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "paizuriCumDone": {
                    this.setCurrentAction(Action.NULL);
                    break;
                }
                case "cumSound": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "jumpCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.player.rotationYaw = this.getYawRotation().floatValue() + 170.0f;
                    minecraft.player.rotationPitch = -20.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 2;
                    break;
                }
                case "breedingHmm": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.player.rotationYaw = this.getYawRotation().floatValue() + 180.0f;
                        minecraft.player.rotationPitch = -15.0f;
                        minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                        minecraft.gameSettings.thirdPersonView = 0;
                    }
                    this.sendChatMessage("hmm...");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingFound": {
                    this.sendChatMessage("guess we found a worthy breeding partner!");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingEnough": {
                    this.sendChatMessage("Eh.. go pin him down, before he runs off!");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingCam2": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.gameSettings.thirdPersonView = 2;
                        minecraft.player.rotationYaw = this.getYawRotation().floatValue() - 120.0f;
                        minecraft.player.rotationPitch = -30.0f;
                    }
                }
                case "breedingIntroDone": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "breeding_slow1Done": {
                    if (this.getRNG().nextBoolean()) {
                        boolean bl = this.aD = !this.aD;
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_FAST_0);
                    this.ay = false;
                    break;
                }
                case "breeding_fast1Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.ay = false;
                    break;
                }
                case "breeding_fast1Ready": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.ay = true;
                    this.resetAnimationControllerOffset();
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "cum": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "breeding_intro_3Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_2);
                    break;
                }
                case "breeding_3_wiggle": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "breeding_fast_3Done": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_SLOW_2);
                    break;
                }
                case "breeding_intro_2Done": {
                    this.setCurrentAction(Action.BREEDING_1);
                    break;
                }
                case "breeding_cumCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Minecraft mc = Minecraft.getMinecraft();
                    mc.gameSettings.thirdPersonView = 0;
                    mc.player.rotationYaw = this.getYawRotation() + 180.0f;
                    mc.player.rotationPitch = -15.0f;
                    mc.player.rotationYawHead = mc.player.rotationYaw;
                    mc.gameSettings.thirdPersonView = 0;
                    break;
                }
                case "neslon_introDone": {
                    this.setCurrentAction(Action.NELSON_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "nelson_slowDone": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.aF = !this.aF;
                    break;
                }
                case "neslon_fastSwitch": {
                    if (!this.isControlledByLocalPlayer()) {
                        this.X = true;
                        return;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.X = true;
                    break;
                }
                case "neslon_fastBackSwitch": {
                    if (!this.isControlledByLocalPlayer()) {
                        this.actionController.tickOffset = 0.0;
                        break;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "nelsonFastDone": {
                    this.X = false;
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.NELSON_SLOW);
                    break;
                }
                case "nelson_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    this.setCurrentAction(Action.NULL);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        this.movementController.transitionLengthTicks = 10.0;
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

//    static EntityDataManager access$000(GoblinEntity goblinEntity) {
//        return goblinEntity.entityDataManager;
//    }

    public static class EventHandler {
        static Minecraft mc = null;

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.START) {
                ArrayList<GoblinEntity> goblins = new ArrayList<GoblinEntity>();
                for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                    EntityPlayer player;
                    GoblinEntity goblin;
                    UUID uUID;
                    if (girl.world.isRemote && girl instanceof GoblinEntity && (uUID = (goblin = (GoblinEntity) girl).getOwnerUUID()) != null && (player = goblin.world.getPlayerEntityByUUID(uUID)) != null && player.dimension != goblin.dimension) {
                        goblins.add(goblin);
                    }
                }
                for (GoblinEntity goblin : goblins) {
                    goblin.setOwnerUUID((UUID) null);
                    goblin.setInteractionPlayerUUID((UUID) null);
                    goblin.setDead();
                }
            }
        }

        @SubscribeEvent
        public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            EntityPlayer player = event.player;
            UUID uUID = player.getPersistentID();
            int dimension = event.toDim;
            World world = player.world;
            GoblinEntity existing = null;

            for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                GoblinEntity goblin;
                if (!girl.world.isRemote && girl instanceof GoblinEntity && uUID.equals((goblin = (GoblinEntity) girl).getOwnerUUID())) {
                    String modelCode = goblin.getCustomModelCode();
                    String partCode = goblin.getCustomPartListCode();
                    existing = goblin;
                    existing.setOwnerUUID((UUID) null);
                    existing.setInteractionPlayerUUID((UUID) null);
                    existing.setCurrentAction(Action.NULL);
                    GoblinEntity newGoblin = new GoblinEntity(world);
                    newGoblin.dimension = dimension;
                    newGoblin.forceSpawn = true;
                    newGoblin.setCustomModelCode(modelCode);
                    newGoblin.setCustomPartListCode(partCode);
                    newGoblin.entityDataManager.set(aC, true);
                    world.spawnEntity(newGoblin);
                    newGoblin.setPositionAndUpdate(player.posX, player.posY, player.posZ);
                    newGoblin.setOwnerUUID(uUID);
                    newGoblin.setCurrentAction(Action.SHOULDER_IDLE);
                    break;
                }
            }
            if (existing != null) {
                world.removeEntity(existing);
                GirlEntity.getGirlEntityList().remove(existing);
            }
        }

        @SubscribeEvent
        public void onLivingAttack(LivingAttackEvent event) {
            if (event.getSource() != DamageSource.OUT_OF_WORLD) {
                EntityLivingBase entity = event.getEntityLiving();
                if (entity instanceof GoblinEntity) {
                    GoblinEntity goblin = (GoblinEntity) entity;
                    if (goblin.getOwnerUUID() != null) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent
        @SideOnly(value=Side.CLIENT)
        public void onKeyInput(InputEvent.KeyInputEvent event) {
            if (mc == null) {
                mc = Minecraft.getMinecraft(); //why ts assigns minecraft onlyin onKeyInput
            }
            if (!(EventHandler.mc.currentScreen instanceof GoblinUI)) {
                if (ClientProxy.keyBindings[0].isPressed()) {
                    GirlEntity interactingGirl = null;
                    UUID uUID = Minecraft.getMinecraft().player.getPersistentID();
                    for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                        if (girl.world.isRemote && girl instanceof IGoblin && uUID.equals(((IGoblin)girl).getOwnerUUID())) {
                            interactingGirl = girl;
                            break;
                        }
                    }
                    if (interactingGirl != null) {
                        if (interactingGirl.getCurrentAction() == Action.SHOULDER_IDLE) {
                            Minecraft.getMinecraft().displayGuiScreen(new GoblinUI(interactingGirl));
                        }
                    }
                }
            }
        }
    }
}

