/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.entity.EntityMountEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingSpawnEvent$CheckSpawn
 *  net.minecraftforge.event.entity.player.PlayerWakeUpEvent
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerRespawnEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.*;
import com.trolmastercard.sexmod.companion.DoorInteractAIGoal;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.gui.Galath.EscapeMinigameUI;
import com.trolmastercard.sexmod.gui.Galath.GalathFlightUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.util.interfaces.IGalath;
import com.trolmastercard.sexmod.world.AllieWorldData;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class GalathEntity extends GirlEntity implements IEntityMultiPart, IGalath {
    final static public float a2 = 0.6f;
    final static public float b6 = 0.6f;
    final static public int bj = 10;
    final static public int an = 20;
    final static public float aU = 50.0f;
    final static public float ba = 40.0f;
    final static public int bM = 5;
    final static public int bs = 25;
    final static public float bJ = 30.0f;
    final static public float aA = 3.0f;
    final static public int a3 = 23;
    final static public int X = 45;
    final static public float ca = 0.3f;
    final static public float a8 = 9.0f;
    final static public float aX = 30.0f;
    final static public int bE = 24;
    final static public int aQ = 32;
    final static public int av = 5;
    final static public int bQ = 36;
    final static public int aR = 40;
    final static public int aB = 54;
    final static public int by = 10;
    final static public float b_ = 0.25f;
    final static public double ax = 3.0;
    final static public double bF = 1.0;
    final static public double bv = 1.5;
    final static public double az = (double)0.3f;
    final static public double ag = 40.0;
    final static public double au = 5.0;
    final static public double ae = 0.2;
    final static public double aV = 3.0;
    final static public double ar = (double)0.1f;
    final static public double ai = 6.0;
    final static public double ah = 50.0;
    final static public double bR = 39.0;
    final static public double bV = 58.0;
    final static public double aZ = 2.0;
    final static public double Q = 1.0;
    final static public float aJ = 0.5f;
    final static public Vector3fSexmodSpecial STAR_PARTICLE_COLOR = new Vector3fSexmodSpecial(0.83137256f, 0.6862745f, 0.21568628f);
    final static public Vec3d bz = new Vec3d(-1.049342f, 2.0547213554382324, -0.05048239231109619);
    final static public Vec3d bC = new Vec3d(1.2522261142730713, 1.435773253440857, 0.23570987582206726);
    final static public int aN = 10;
    final static public float ak = 0.2f;
    final static public int am = 5;
    final static public float T = 15.0f;
    final static public int aM = 48;
    final static public float be = 0.05f;
    final static public float a7 = 0.65f;
    final static public float bh = 0.9f;
    final static public float K = 45.0f;
    final static public float a0 = 1.0f;
    final static public float bn = 1.5f;
    final static public float ao = 110.0f;
    final static public int aj = 15;
    final static public float aw = 6.0f;
    final static public float bp = 0.94f;
    final static public int R = 13;
    final static public int bW = 40;
    final static public int bl = 25;
    final static public int aY = 38;
    final static public int N = 95;
    final static int bB = 10;
    final static int aI = 30;
    final static int bf = 175;
    final static float as = 2.0f;
    final static public float bo = 0.25f;
    final static public float Y = 1000.0f;
    final static public float bX = 15.0f;
    final static public float b9 = 5.0f;
    final static public int aW = 8000;
    final static public float aK = 0.1f;
    final static public float ac = 5.0f;
    final static public float b5 = -10.0f;
    final static public int bk = 16;
    final static public int br = 7;
    final static public int cb = 4;
    final static public float M = 0.5f;
    final static public float bi = 0.55f;
    final static Class<?>[] aS = new Class[]{BlockAir.class, BlockCarpet.class, BlockBush.class, BlockButton.class, BlockLadder.class, BlockTorch.class, BlockSign.class, BlockBanner.class};
    final static public DataParameter<Integer> bq = EntityDataManager.createKey(GalathEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    final static public DataParameter<Integer> FLY_TICKS = EntityDataManager.createKey(GalathEntity.class, DataSerializers.VARINT).getSerializer().createKey(112);
    final static public DataParameter<Boolean> bN = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(113);
    final static public DataParameter<Boolean> b7 = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(114);
    final static public DataParameter<Boolean> ay = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(115);
    final static public DataParameter<Integer> SWORD_ATTACK_PROGRESS = EntityDataManager.createKey(GalathEntity.class, DataSerializers.VARINT).getSerializer().createKey(116);
    final static public DataParameter<String> FLIGHT_TARGET_POS = EntityDataManager.createKey(GalathEntity.class, DataSerializers.STRING).getSerializer().createKey(117);
    final static public DataParameter<Boolean> IS_FLYING_FLAG = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(118);
    final static public DataParameter<Float> SPIN_YAW_FACTOR = EntityDataManager.createKey(GalathEntity.class, DataSerializers.FLOAT).getSerializer().createKey(119);
    final static public DataParameter<Boolean> HIDE_EFFECTS_FLAG = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(120);
    final static public DataParameter<String> MANGLELIE_UUID = EntityDataManager.createKey(GalathEntity.class, DataSerializers.STRING).getSerializer().createKey(121);
    final static public DataParameter<Boolean> bT = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(122);
    final static public double b0 = 0.2;
    final static public float bS = 5.0f;
    final static public int a1 = 60;
    BossInfoServer healthBar = new BossInfoServer(new TextComponentString(this.getGirlName()), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
    MultiPartHitbox energyBallHitboxLeft = new MultiPartHitbox(this, "energyBallHitBox", 0.75f, 0.75f);
    MultiPartHitbox energyBallHitboxRight = new MultiPartHitbox(this, "energyBallHitBox", 0.75f, 0.75f);
    public GalathFlightData bZ = null;
    public Vec3d targetFlyPos = null;
    public Vec3d previousPos = null;
    public int aF = 0;
    public Vec3d bd = null;
    public List<EntityWitherSkeleton> witherSkeletons = new ArrayList<EntityWitherSkeleton>();
    public float cachedHeadRotationX = 0.0f;
    public long dashStartWorldTime = -1L;
    public long dashEndWorldTime = -1L;
    public float bodyRotationY = 0.0f;
    public float bodyScaleY = 0.0f;
    boolean bU = false;
    public Vec3d targetDashPosition = null;
    boolean isDespawned = false;
    Vec3d position;
    Vec3d predicatedPosition;
    Vec3d velocity;
    float al = 0.0f;
    boolean corruptIntroActive = false;
    public int ad = 0;
    double a9 = 0.0;
    double bg = 0.0;
    double b4 = 0.0;
    double a_ = 0.0;
    boolean bK = false;
    Path aq = null;
    BlockPos bG = null;
    int aC = 0;
    Action ab = null;
    int at = 0;
    int bY = 0;
    int b3 = 0;
    long bc = 0L;
    boolean morningBlowjobStarted = false;
    boolean despawned = false;
    int b1 = 0;
    boolean aT = false;
    public boolean bx = false;
    public boolean a5 = false;
    public boolean isTransformingManglelie = false;
    public boolean bt = false;
    public boolean hasSwordEquipped = false;
    public boolean bu = false;
    public boolean aL = true;
    public boolean isRenderingOverlayDisabled = false;
    boolean a6 = false;

    public GalathEntity(World world) {
        super(world);
    }

    public GalathEntity(World world, @Nonnull EntityPlayer player, Vec3d pos, boolean spawnStructure) {
        this(world);
        UUID uUID = player.getPersistentID();

        this.entityDataManager.set(MASTER, uUID.toString());
        this.healthBar.setVisible(false);
        this.bG = new BlockPos(this.getPositionVector());
        String npcName = AllieWorldData.getCustomName(uUID, PlayerGirlEntity.GALATH);
        if (npcName != null) {
            super.setCustomNameOverride(npcName);
        }

        if (!spawnStructure) {
            if (this.getRNG().nextFloat() > 0.1f) {
                this.setCurrentAction(Action.GALATH_SUMMON);
            } else {
                this.setCurrentAction(Action.MASTERBATE);
                this.setYawRotation(180.0f - (float) TrigMath.sinDegrees(Math.atan2(pos.x - player.posX, pos.z - player.posZ)));
                ThreadNames.createDaemonThread(8000, () -> {
                    EntityPlayer master = this.getMasterPlayer();
                    if (master != null) {
                        if (!master.isDead) {
                            this.setTargetPosition(master.getPositionVector());
                            this.setYawRotation(master.rotationYaw + 180.0f);
                            this.setCurrentAction(Action.RAPE_INTRO);
                            this.setInteractionPlayerUUID(master.getPersistentID());
                            this.setAnchored(true);
                        }
                    }
                });
            }
        }
    }

    public GalathEntity(World world, @Nonnull EntityPlayer player, Vec3d pos) {
        this(world, player, pos, false);
    }

    @Override
    public void setCustomModelCode(String string) {
        super.setCustomModelCode(string);
        GirlWorldData.setCustomModelCode(this);
    }

    @Override
    public String getGirlName() {
        return "Galath";
    }

    @Override
    public float getScaleFactor() {
        return this.getManglelieUUID() == null ? 0.5f : 1.35f;
    }

    @Override
    public float getEyeHeight() {
        return 1.9f;
    }

    public boolean hasMasterOAlgo() {
        return this.hasMaster();
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    protected void handleJumpWater() {
        if (this.hasMasterOAlgo()) {
            super.handleJumpWater();
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return this.hasMasterOAlgo() ? super.getWaterSlowDown() : 0.0f;
    }

    @Override
    public boolean isInWater() {
//        if (this.hasMasterOAlgo()) {
//            return super.isInWater();
//        }
//        return false;
        return this.hasMasterOAlgo() ? super.isInWater() : false;
    }

    @Override
    public boolean handleWaterMovement() {
        if (this.hasMasterOAlgo()) {
            return super.handleWaterMovement();
        }
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(bq, -1);
        this.entityDataManager.register(FLY_TICKS, 0);
        this.entityDataManager.register(bN, true);
        this.entityDataManager.register(b7, true);
        this.entityDataManager.register(ay, false);
        this.entityDataManager.register(FLIGHT_TARGET_POS, "null");
        this.entityDataManager.register(SWORD_ATTACK_PROGRESS, -1);
        this.entityDataManager.register(IS_FLYING_FLAG, false);
        this.entityDataManager.register(SPIN_YAW_FACTOR, 0.0f);
        this.entityDataManager.register(HIDE_EFFECTS_FLAG, false);
        this.entityDataManager.register(MANGLELIE_UUID, "");
        this.entityDataManager.register(bT, false);
    }

    @Override
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        this.getAttributeMap().registerAttribute(SWIM_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(110.0);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.6f);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6f);
    }

    @Override
    protected void initEntityAI() {
        this.aiLookAtPlayer = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAITempt((EntityCreature)this, 0.4, false, new HashSet<Item>(TEMPTATION_ITEMS)));
        this.tasks.addTask(3, new DoorInteractAIGoal(this));
        this.tasks.addTask(5, this.aiLookAtPlayer);
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP playerMP) {
        super.addTrackingPlayer(playerMP);
        this.healthBar.addPlayer(playerMP);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP playerMP) {
        super.removeTrackingPlayer(playerMP);
        this.healthBar.removePlayer(playerMP);
    }

    @Override
    public Vec3d getTargetPosition() {
        if (this.world.isRemote && this.targetDashPosition != null) {
            return this.targetDashPosition;
        }
        return super.getTargetPosition();
    }

    @Nullable
    public UUID getManglelieUUID() {
        String string = this.entityDataManager.get(MANGLELIE_UUID);
        if (string.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(string);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public ManglelieEntity getManglelieUUID(boolean server) {
        //GirlEntity girlEntity;
        UUID uUID = this.getManglelieUUID();
        if (uUID == null) {
            return null;
        }
        GirlEntity girl = server ? GalathEntity.getServerGirlEntity(uUID) : GalathEntity.getClientGirlEntity(uUID);
        return girl instanceof ManglelieEntity ? (ManglelieEntity) girl : null;
    }

    @Nullable
    public static ManglelieEntity getMangleliePartnerOf(GirlEntity girl, boolean server) {
        return !(girl instanceof GalathEntity) ? null : ((GalathEntity) girl).getManglelieUUID(server);
    }

    public void setManglelieUUID(@Nullable UUID uUID) {
        this.entityDataManager.set(MANGLELIE_UUID, uUID == null ? "" : uUID.toString());
    }

    public void aC() {
        this.isDespawned = true;
        ManglelieEntity manglelie = this.getManglelieUUID(true);
        if (manglelie != null) {
            manglelie.markDespawned();
        }
    }

    public void handleRapeState() {
        Action currentAction = this.getCurrentAction();
        if (currentAction == Action.RAPE_ON_GOING) {
            this.bZ = GalathFlightData.CHANGE_POSITION;
            this.bZ.executeStart(this);
            this.setAnchored(false);
            this.setCurrentAction(Action.FLY);
            EntityPlayer entityPlayer = this.getPlayerEntity();
            this.setInteractionPlayerUUID((UUID) null);
            if (entityPlayer != null) {
                PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(true), (EntityPlayerMP) entityPlayer);
            }
            GirlEntity.girlPlaySound((GirlEntity) this, SoundsHandler.GIRLS_GALATH_DIALOG[0]);
        }
    }

    public Vec3d getAnchorTargetPosition() {
        String[] parts = this.entityDataManager.get(FLIGHT_TARGET_POS).split("\\|");
        return new Vec3d(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }

    public void setFlightTargetPos(@Nullable Vec3d pos) {
        this.entityDataManager.set(FLIGHT_TARGET_POS, pos.x + "|" + pos.y + "|" + pos.z);
    }

    public int getSwordAttackProgress() {
        return this.entityDataManager.get(SWORD_ATTACK_PROGRESS);
    }

    public void setSwordAttackProgress(int progress) {
        this.entityDataManager.set(SWORD_ATTACK_PROGRESS, progress);
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public boolean isHuggingManglelie() {
        switch (this.getCurrentAction()) {
            case HUG_MANG: 
            case MORNING_BLOWJOB_SLOW: 
            case MORNING_BLOWJOB_FAST: 
            case MORNING_BLOWJOB_CUM:
                return true;
            default:
                return false;
        }
    }

    void void_aa() {
        this.velocity = new Vec3d(this.motionX, this.motionY, this.motionZ);
        this.position = this.getPositionVector();
        this.predicatedPosition = this.getPositionVector().add(this.velocity);
        this.velocity = this.velocity.scale(0.9);
    }

    @Override
    public void onUpdate() {
        boolean hasMasterFlag = this.hasMasterOAlgo();
        if (hasMasterFlag) {
            this.void_E();
        } else {
            this.updateFlightUI();
        }

        this.void_aa();
        super.onUpdate();
        if (hasMasterFlag) {
            this.au();
        } else {
            this.void_R();
        }
        if (this.world.isRemote) {
            this.void_X();
        }
    }

    @Override
    public boolean canBeInteractedWith() {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    void void_X() {
        if (this.getCurrentAction() == Action.GIVE_COIN) {
            int coinTick = Action.GIVE_COIN.ticksPlaying[1];
            if (coinTick == 95) {
                GalathCoin.summonForPlayer(Minecraft.getMinecraft().player, this);
            }
            if (coinTick > 25 && coinTick < 38) {
                Vec3d pos = this.getPositionVector();
                Vec3d weaponPos = this.getCachedBoneOffset("weapon").add(pos);
                Vec3d offhandPos = this.getCachedBoneOffset("offhand").add(pos);
                DragonBreathParticle.BREATH_SCALE = 0.5f;

                for (float t = 0.0f; t < 1.0f; t += 0.2f) {
                    Vec3d lerped = ReferenceAndRotationHelper.LerpVec3d(weaponPos, offhandPos, (double) t);
                    Minecraft.getMinecraft().effectRenderer.addEffect(new DragonBreathParticle(this.world, lerped.x, lerped.y, lerped.z));
                }
            }
        }
    }

    void void_E() {
        this.setNoGravity(this.getRidingPlayer() != null);
    }

    void au() {
        if (!this.isInWater() && !this.hasNoGravity() && this.motionY < 0.0 && this.getCurrentAction() != Action.MASTERBATE) {
            this.motionY *= (double)0.4f;
        }
        this.aB();
        this.aj();
        this.aq();
        this.aw();
        this.void_C();
        this.Y_();
        this.handleRapeCum();
        if (this.getAttackTarget() == null) {
            this.hasSwordEquipped = false;
        }
    }

    void handleRapeCum() {
        if (!this.world.isRemote) {
            if (this.getCurrentAction() == Action.RAPE_CUM) {
                if (Action.RAPE_CUM.ticksPlaying[0] >= 28) {
                    this.setAnchored(false);
                    this.setCurrentAction(Action.NULL);
                    EntityPlayer entityPlayer = this.getPlayerEntity();
                    this.setInteractionPlayerUUID((UUID) null);
                    if (entityPlayer != null) {
                        entityPlayer.setPositionAndUpdate(entityPlayer.posX, Math.ceil(entityPlayer.posY) + 1.0, entityPlayer.posZ);
                        PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(true), (EntityPlayerMP) entityPlayer);
                    }
                }
            }
        }
    }

    void Y_() {
        if (!this.world.isRemote) {
            if (this.getCurrentAction() == Action.CORRUPT_CUM) {
                if (Action.CORRUPT_CUM.ticksPlaying[0] >= 30) {
                    this.setAnchored(false);
                    this.setCurrentAction(Action.NULL);
                    EntityPlayer entityPlayer = this.getPlayerEntity();
                    this.setInteractionPlayerUUID((UUID) null);
                    if (entityPlayer != null) {
                        entityPlayer.setPositionAndUpdate(entityPlayer.posX, Math.ceil(entityPlayer.posY) + 1.0, entityPlayer.posZ);
                        PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(true), (EntityPlayerMP) entityPlayer);
                    }
                }
            }
        }
    }

    static boolean isNearHive(BlockPos pos, World world) {
        for (BlockPos hivePos : StructureTracker.STRUCTURE_POSITIONS) {
            if (!(Math.sqrt(pos.distanceSq(hivePos)) < 1000.0))
                return false;
        }
        for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
            if (girl.world.isRemote || !(girl instanceof GalathEntity) || girl.isDead || !(girl.getDistanceSq(pos) < 1000000.0))
                return false;
        }

        int y = pos.getY();
        while ((float) y < 15.0f + (float) pos.getY()) {
            if (world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock() != Blocks.AIR) {
                return false;
            }
            ++y;
        }
        while ((float) y > (float) pos.getY() - 5.0f) {
            if (world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock() instanceof BlockLiquid) {
                return false;
            }
            --y;
        }
        return true;
    }

    void aw() {
        EntityPlayer player = this.getRidingPlayer();
        Action action = this.getCurrentAction();
        if (player != null) {
            if (action == Action.BOOST) {
                int sideIndex = ClientServerCheck.getInstance() ? 0 : 1;
                if (action.ticksPlaying[sideIndex] >= 13) {
                    if (action.ticksPlaying[sideIndex] == 13) {
                        this.al = 6.0f;
                    }
                    Vec3d vec3d = player.getLook(0.0f).normalize();
                    this.motionX = vec3d.x * (double) this.al;
                    this.motionY = vec3d.y * (double) this.al;
                    this.motionZ = vec3d.z * (double) this.al;
                    this.al *= 0.94f;
                }
            }
        }
    }

    void updateFlightUI() {
        this.updateGravity();
        this.updateHealthBar();
        this.void_ah();
    }

    void void_R() {
        getAimYaw(this, 0.0f);
        this.resetEnergyBalls();
        this.aj();
        this.void_af();
        this.L_();
        this.void_F();
        this.void_C();
        this.handleCorruptCum();
        if (this.world.isRemote) {
            this.void_H();
        }
    }

    void handleCorruptCum() {
        if (!this.world.isRemote) {
            if (this.getCurrentAction() == Action.CORRUPT_CUM) {
                if (Action.CORRUPT_CUM.ticksPlaying[0] >= 30) {
                    this.setCurrentAction(Action.GIVE_COIN);
                }
            }
        }
    }

    void void_C() {
        if (this.entityDataManager.get(HIDE_EFFECTS_FLAG)) {
            this.isRenderingOverlayDisabled = true;
        } else {
            switch (this.getCurrentAction()) {
                case RAPE_INTRO:
                case RAPE_ON_GOING:
                case RAPE_CUM:
                case RAPE_CHARGE:
                case RAPE_CUM_IDLE:
                case CORRUPT_SLOW:
                case CORRUPT_FAST:
                case CORRUPT_CUM:
                case MASTERBATE:
                    this.isRenderingOverlayDisabled = true;
                case RAPE_PREPARE:
                    return;
                default:
                    this.isRenderingOverlayDisabled = false;
            }
        }

    }

    @Override
    public boolean isCustomType() {
        return this.getCurrentAction() == Action.CORRUPT_INTRO && this.corruptIntroActive;
    }

    void void_F() {
        if (this.world.isRemote) {
            if (this.getCurrentAction() != Action.KNOCK_OUT_STAND_UP) {
                this.aL = true;
            }
        }
    }

    void updateHealthBar() {
        this.healthBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    void updateGravity() {
        if (!this.entityDataManager.get(IS_FLYING_FLAG)) {
            this.setNoGravity(this.getAttackTarget() != null);
        }
    }

    void L_() {
        if (this.getCurrentAction() != Action.ATTACK_SWORD) {
            this.hasSwordEquipped = false;
            this.bu = false;
        }
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
    }

    void void_af() {
        if (this.world.isRemote) {
            if (this.bu) {
                Vec3d pos = this.getPositionVector();
                Vec3d startPos = this.getCachedBoneOffset("weaponStart").add(pos);
                Vec3d endPos = this.getCachedBoneOffset("weaponEnd").add(pos);
                float step = 0.1f;
                Random random = this.getRNG();

                for (float t = 0.0f; t < 1.0f; t += step) {
                    Vec3d lerped = ReferenceAndRotationHelper.LerpVec3d(startPos, endPos, (double) t);

                    for (int i = 0; i < 3; ++i) {
                        this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, lerped.x + random.nextDouble() * 0.25 * (double) (random.nextBoolean() ? 1 : -1), lerped.y + random.nextDouble() * 0.25 * (double) (random.nextBoolean() ? 1 : -1), lerped.z + random.nextDouble() * 0.25 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.0, 0.0, new int[0]);
                    }
                }
                for (int i = 0; i < 3; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, endPos.x + random.nextDouble() * 0.25 * (double) (random.nextBoolean() ? 1 : -1) * (double) (random.nextBoolean() ? 1 : -1), endPos.y + random.nextDouble() * 0.25 * (double) (random.nextBoolean() ? 1 : -1), endPos.z + random.nextDouble() * 0.25 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void resetAnimationControllerTicks() {
        if (this.getCurrentAction() != Action.GALATH_DE_SUMMON) {
            this.actionController.tickOffset = 0.0;
        }
    }

    @Override
    public String getDisplayNameText() {
        EntityPlayer masterPlayer = this.getMasterPlayer();
        return masterPlayer == null ? super.getDisplayNameText() : String.format("%s %s[%s]", new Object[]{super.getDisplayNameText(), TextFormatting.DARK_PURPLE, masterPlayer.getName()});
    }

    void resetEnergyBalls() {
        this.energyBallHitboxLeft.isActive = false;
        this.energyBallHitboxRight.isActive = false;
        if (!((float) this.ad < 9.0f)) {
            if (!((float) this.ad > 30.0f)) {
                this.energyBallHitboxLeft.isActive = true;
                this.energyBallHitboxRight.isActive = true;
                boolean reset = this.entityDataManager.get(ay);
                Vec3d vec3d = this.getPositionVector().add(VectorMath.rotate(reset ? VectorMath.MirrorXZ(bz) : bz, 180.0f + this.renderYawOffset));
                Vec3d vec3d2 = this.getPositionVector().add(VectorMath.rotate(reset ? VectorMath.MirrorXZ(bC) : bC, 180.0f + this.renderYawOffset));
                this.energyBallHitboxLeft.setLocationAndAngles(vec3d.x, vec3d.y, vec3d.z, this.renderYawOffset, 0.0f);
                this.energyBallHitboxRight.setLocationAndAngles(vec3d2.x, vec3d2.y, vec3d2.z, this.renderYawOffset, 0.0f);
                this.energyBallHitboxLeft.onUpdate();
                this.energyBallHitboxRight.onUpdate();
            }
        }
    }

    void void_ah() {
        if (this.getCurrentAction() != Action.SUMMON_SKELETON) {
            this.ad = 0;
        } else {
            if (this.ad++ > 45) {
                this.ad = 0;
            }
        }
    }

    @Override
    public Vector4d getFlightData() {
        return new Vector4d(this.a9, this.bg, this.b4, this.a_);
    }

    void aj() {
        this.b4 = this.a9;
        this.a_ = this.bg;
        Vec3d delta = this.predicatedPosition.subtract(this.position);
        Vec3d rotated = VectorMath.rotate(delta, this.renderYawOffset + 180.0f);
        this.a9 = TrigMath.toRadians(ThreadNames.clamp(rotated.z * 40.0, -50.0, 50.0));
        this.bg = TrigMath.toRadians(ThreadNames.clamp(rotated.x * 40.0, -50.0, 50.0));
    }

    public void setFlightVelocity(Vec3d targetPos) {
        if (!this.entityDataManager.get(IS_FLYING_FLAG)) {
            this.entityDataManager.set(IS_FLYING_FLAG, true);
            if (this.bZ != null) {
                this.bZ.executeStop(this);
            }
            this.bZ = null;
            Vec3d pos = this.getPositionVector();
            Random random = this.getRNG();
            Vec3d vel = targetPos == null
                    ? new Vec3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize()
                    : pos.subtract(targetPos).normalize();
            this.setVelocity(vel.x * 1.0, 1.0, vel.z * 1.0);
            this.setCurrentAction(Action.KNOCK_OUT_FLY);
            this.setNoGravity(false);
            this.noClip = false;
            this.getNavigator().clearPath();
            GalathEntity.playRandomSound((GirlEntity) this, SoundsHandler.GIRLS_GALATH_AAA, true);
        }
    }

    void sendTrackingMessage(Entity entity) {
        GirlEntity.sendMessageToTrackingPlayers((GirlEntity)this, (Object)((Object)TextFormatting.YELLOW) + "Galath is paralyzed! Now it's time to corrupt her");
        GirlEntity.sendMessageToTrackingPlayers((GirlEntity)this, (Object)((Object)TextFormatting.GRAY) + "(Walk to her and right click her)");
        PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesPacket2(this.getPositionVector(), true), (Entity)this);
        this.setFlightVelocity((Vec3d)null);
        this.entityDataManager.set(HIDE_EFFECTS_FLAG, true);
    }

    @Override
    public void updateAITasks() {
        if (this.despawned) {
            GalathMangTracker.updateMangleliePartner(this);
        }else {
            this.void_P();
            super.updateAITasks();
            this.aiLookAtPlayer.isWatching = this.isFlyingIdle();
            if (this.hasMasterOAlgo()) {
                this.void_ae();
            } else {
                this.an();
            }
        }
    }

    void void_P() {
        if (!this.bK) {
            this.setCustomModelCode(GirlWorldData.getCustomModelCode(this));
            this.bK = true;
        }
    }

    boolean isFlyingIdle() {
        return this.getCurrentAction() == Action.NULL && !(Math.abs(this.motionX) + Math.abs(this.motionZ) > 0.01);
    }

    void aq() {
        if (this.world.isRemote) {
            if (this.getRidingPlayer() == null) {
                EntityPlayer master = this.getMasterPlayer();
                if (master != null) {
                    this.handleGalathPlayer(master);
                }
            }
        }
    }

    void handleGalathPlayer(EntityPlayer player) {
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(player.getPersistentID());
        Vec3d headPos = new Vec3d(player.posX, player.posY + (double)(playerGirl == null ? player.eyeHeight : playerGirl.getEyeHeight()), player.posZ);
        Vec3d eyePos = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        double dist = eyePos.distanceTo(headPos);
        double heightDiff = headPos.y - eyePos.y;
        this.rotationPitch = (float)(-(Math.sin(heightDiff / dist) * (180.0 * Math.PI)));
    }

    void void_ae() {
        this.healthBar.setVisible(false);
        if (!GalathMangTracker.isOwnerNearby(this)) {
            GalathMangTracker.updateMangleliePartner(this);
        }
        else if (this.getRidingPlayer() != null) {
            this.clearFlightData();
        }
        else {
            this.handleManglelieOwned();
            if (this.getManglelieUUID() == null) {
                this.aJ();
            } else {
                this.am();
            }
        }
    }

    void handleManglelieOwned() {
        if (GalathMangTracker.isManglelieOwned(GalathMangTracker.getManglelieOwnerOf(this))) {
            boolean canLick = this.canStartPussyLicking();
            if (canLick) {
                Main.LOGGER.warn("mommy thinks she got no daughter but she actually does have one. Failsafe called. Hopefully its fixed");
            }
        }
    }

    void am() {
        if (!this.boolean_ai()) {
            this.entityDataManager.set(bT, false);
            this.ao();
        }
    }

    boolean boolean_ai() {
        UUID ownerUUID = GalathMangTracker.getManglelieOwnerOf(this);
        if (ownerUUID == null) {
            return false;
        }
        
        EntityPlayer owner = this.world.getPlayerEntityByUUID(ownerUUID);
        if (owner == null) {
            return false;
        }

        BlockPos ownerPos = owner.getPosition();
        if (!this.isFlightBlocked(ownerPos)) {
            return false;
        }

        if (this.bZ != null) {
            this.bZ.executeStop(this);
            this.bZ = null;
        }

        float dist = this.getDistance(owner);
        PathNavigate navigator = this.getNavigator();
        if (dist < 4.0f) {
            navigator.clearPath();
            return false;
        }
        if (dist > 16.0f) {
            navigator.clearPath();
            this.handlePlayerRide(owner);
            return true;
        }

        if (PathUtils.getPathEnd(this.aq).distanceSq(ownerPos) > 16.0) {
            if (!this.onGround) {
                return true;
            }

            this.aq = this.getPathToPlayer(owner, ownerPos);
            if (this.aq == null) {
                this.handlePlayerRide(owner);
            } else {
                navigator.setPath(this.aq, 1.0);
            }
        }

        if (this.aq != null && !this.aq.isFinished()) {
            boolean sprinting = owner.isSprinting() || this.getDistance(owner) > 7.0f;
            double speed = sprinting ? (double) 0.55f : 0.5;
            double extra = Math.floor(dist / 5.0f) * 0.2;
            speed += extra;
            if (this.isInWater()) {
                speed *= 60.0;
            }

            navigator.setSpeed(speed);
            this.entityDataManager.set(bT, sprinting);
            this.setCurrentAction((Action) null);
            return true;
        } else {
            return false;
        }
    }

    boolean isFlightBlocked(BlockPos pos) {
        if (this.bZ == null) {
            return true;
        }
        BlockPos selfPos = this.getPosition();
        int dist = Math.abs(pos.getX() - selfPos.getX()) + Math.abs(pos.getX() - selfPos.getX());
        return dist > 16;
    }

    protected void handlePlayerRide(EntityPlayer player) {
        BlockPos teleportPos;
        int attempts = 0;
        do {
            teleportPos = player.getPosition().add(ReferenceAndRotationHelper.RANDOM.nextInt(4), 0, ReferenceAndRotationHelper.RANDOM.nextInt(4));
        } while (++attempts < 20 && !this.attemptTeleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ()));

        if (attempts >= 20) {
            this.setPosition(player.posX, player.posY, player.posZ);
        }
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    @Nullable
    Path getPathToPlayer(EntityPlayer player, BlockPos pos) {
        PathNavigate navigator = this.getNavigator();
        return navigator.getPathToEntityLiving(player);
    }

    void aJ() {
        this.at();
        this.ay();
    }

    void clearFlightData() {
        this.bG = null;
        this.aC = 0;
        if (this.bZ != null) {
            this.bZ.executeStop(this);
            this.bZ = null;
        }
    }

    void at() {
        if (this.onGround) {
            if (this.getManglelieUUID() == null) {
                if (this.getCurrentAction() != Action.HUG_MANG) {
                    if (!GalathMangTracker.isManglelieOwned(GalathMangTracker.getManglelieOwnerId(this.girlID()))) {
                        BlockPos center = this.getPosition();
                        BlockPos min = center.add(-15.0, -15.0, -15.0);
                        BlockPos max = center.add(15.0, 15.0, 15.0);
                        AxisAlignedBB aabb = new AxisAlignedBB(min, max);
                        List<ManglelieEntity> manglelies = this.world.getEntitiesWithinAABB(ManglelieEntity.class, aabb);
                        ManglelieEntity chosen = null;

                        for (ManglelieEntity manglelie : manglelies) {
                            if (!manglelie.isDead && manglelie.getMommyGalath(true) == null) {
                                chosen = manglelie;
                                break;
                            }
                        }
                        if (chosen == null) {
                            if (this.getCurrentAction() == Action.RUN) {
                                this.setCurrentAction((Action) null);
                                this.getNavigator().clearPath();
                            }
                        } else {
                            this.pathNavigator = this.getNavigator();
                            if (chosen.getDistance(this) <= 3.65f) {
                                this.pathNavigator.clearPath();
                                this.setCurrentAction(Action.HUG_MANG);
                                this.motionX = 0.0;
                                this.motionY = 0.0;
                                this.motionZ = 0.0;
                                this.setTargetPosition(this.getPositionVector());
                                this.setAnchored(true);
                                this.setManglelieUUID(chosen.girlID());
                                chosen.setMommyUUID(this.girlID());
                                chosen.setCurrentAction(Action.RIDE_MOMMY_HEAD);
                                GalathMangTracker.markAsManglelieOwned(this.girlID());
                            } else {
                                Vec3d selfPos = this.getPositionVector();
                                Vec3d partnerPos = chosen.getPositionVector();
                                Vec3d delta = partnerPos.subtract(selfPos);
                                float yaw = (float) TrigMath.sinDegrees(Math.atan2(delta.z, delta.x)) - 90.0f;
                                this.setYawRotation(yaw);
                                this.pathNavigator.clearPath();
                                this.pathNavigator.tryMoveToEntityLiving(chosen, 0.65f);
                                this.setCurrentAction(Action.RUN);
                            }
                        }
                    }
                }
            }
        }
    }

    void ay() {
        Action action = this.getCurrentAction();
        if (action != Action.RUN) {
            if (action != Action.HUG_MANG) {
                if (this.isAnchored() || action == Action.MASTERBATE) {
                    this.getNavigator().clearPath();
                } else {
                    EntityPlayer player = this.world.getClosestPlayerToEntity(this, 15.0);
                    if (this.hasMaster() && player != null && player.getDistance(this) < 2.0f && player.getPersistentID().equals(this.getMasterUUID())) {
                        this.getNavigator().clearPath();
                    } else {
                        if (this.bG == null || this.getDistance(this.bG.getX(), this.bG.getY(), this.bG.getZ()) > this.getFlightRange() || this.aC > 175) {
                            int n = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(10);
                            int n2 = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(10);
                            int n3 = this.world.provider.getDimensionType() == DimensionType.NETHER ? (int) Math.ceil(this.posY) : WorldUtils.getSurfaceHeight(this.world, this.getPosition().getX() + n, this.getPosition().getZ() + n2);
                            this.bG = new BlockPos(this.getPosition().getX() + n, n3, this.getPosition().getZ() + n2);
                            this.aC = 0;
                        }

                        if (Math.sqrt(this.bG.distanceSq(this.getPosition())) > 2.0) {
                            this.getNavigator().tryMoveToXYZ(this.bG.getX(), this.bG.getY(), this.bG.getZ(), 0.35f);
                            this.applyCustomPathNodeVelocity();
                        } else {
                            ++this.aC;
                        }
                    }
                }
            }
        }
    }

    BlockPos av() {
        UUID ownerID = GalathMangTracker.getManglelieOwnerOf(this);
        if (ownerID == null) {
            return BlockPos.ORIGIN;
        }

        EntityPlayer owner = this.world.getPlayerEntityByUUID(ownerID);
        if (owner == null) {
            return BlockPos.ORIGIN;
        }
        return owner.getPosition();
    }

    double getFlightRange() {
        return Math.sqrt(1800.0);
    }

    @Nullable
    public EntityPlayer getRidingPlayer() {
        List<Entity> passengers = this.getPassengers();
        if (passengers.isEmpty()) {
            return null;
        } else {
            return passengers.get(0) instanceof EntityPlayer ? (EntityPlayer) passengers.get(0) : null;
        }

    }

    @Nullable
    public UUID ax() {
        EntityPlayer rider = this.getRidingPlayer();
        return rider == null ? null : rider.getPersistentID();
    }

    @Override
    public void setCustomNameOverride(String name) {
        super.setCustomNameOverride(name);
        UUID masterUUID = this.getMasterUUID();
        if (masterUUID != null) {
            AllieWorldData.addAllie(masterUUID, PlayerGirlEntity.GALATH, name);
        }
    }

    public void applyVelocityDelta(Vec3d delta) {
        this.motionX += delta.x;
        this.motionZ += delta.z;
        this.motionY = delta.y / 2.0;
    }

    public void resetInteractionState() {
        this.setInteractionPlayerUUID((UUID)null);
        this.setCurrentAction((Action)null);
    }

    void aB() {
        EntityPlayer rider = this.getRidingPlayer();
        if (rider != null) {
            this.prevRenderYawOffset = rider.prevRotationYawHead;
            this.renderYawOffset = rider.rotationYawHead;
        }
    }

    void an() {
        this.healthBar.setVisible(true);
        this.ao();
        this.as();
    }

    void ao() {
        if (!Action.isAnyAction((GirlEntity) this, Action.MASTERBATE, Action.HUG_MANG)) {
            if (this.getInteractionPlayerUUID() == null) {
                this.void_Q();
                this.void_I();
                this.D_(); // TODO
                this.checkFlightFinished();
                this.void_J();
                this.void_T();
                this.void_S();
                this.handleKnockOut();
                this.ad_();
                this.aG();
                this.aA();
                this.KillWitherSkeletons();
                this.void_O();
                this.Z();
            }
        }
    }

    void void_Q() {
        if (this.hasMasterOAlgo()) {
            if (this.getAttackTarget() == null) {
                int n = this.entityDataManager.get(bq);
                if (n != -1) {
                    if (this.bZ != null) {
                        this.bZ.executeStop(this);
                    }
                    this.bZ = null;
                    this.setCurrentAction(Action.NULL);
                }
            }
        }
    }

    void as() {
        if (this.getAttackTarget() != null) {
            this.bG = null;
            this.aC = 0;
        } else if (!this.entityDataManager.get(HIDE_EFFECTS_FLAG)) {
            if (!this.entityDataManager.get(IS_FLYING_FLAG)) {
                this.ay();
            }
        }
    }

    @Override
    public void setCurrentAction(Action action) {
        Action currentAction = this.getCurrentAction();
        if (currentAction != Action.GALATH_DE_SUMMON) {
            if (currentAction != Action.CORRUPT_CUM || (action != Action.CORRUPT_FAST && action != Action.CORRUPT_SLOW)) {
                if (currentAction != Action.RAPE_CUM || action != Action.RAPE_ON_GOING) {
                    if (currentAction != Action.MORNING_BLOWJOB_CUM || (action != Action.MORNING_BLOWJOB_SLOW && action != Action.MORNING_BLOWJOB_FAST)) {
                        if (!this.world.isRemote && Action.isAny(currentAction, Action.CORRUPT_CUM, Action.RAPE_CUM, Action.MORNING_BLOWJOB_CUM)) {
                            GalathMangTracker.saveCumTime(this.getInteractionPlayerUUID(), this.world.getTotalWorldTime());
                        }

                        if (action == Action.CORRUPT_SLOW) {
                            this.aT = false;
                            if (currentAction == Action.CORRUPT_INTRO) {
                                this.setFlying(false);
                            }
                            if (this.hasMasterOAlgo() && currentAction == Action.NULL) {
                                this.setFlying(true);
                            }
                        }

                        if (currentAction == Action.GIVE_COIN && action == Action.NULL && !this.world.isRemote) {
                            this.GiveCoinToPlayer();
                        }
                        if (currentAction == Action.HUG_MANG && action == Action.NULL) {
                            this.al();
                        }
                        if (currentAction == Action.MORNING_BLOWJOB_CUM && action == Action.NULL) {
                            this.resetGirls();
                        }
                        super.setCurrentAction(action);
                    }
                }
            }
        }
    }

    void resetGirls() {
        EntityPlayer player = this.getPlayerEntity();
        if (player != null) {
            ResetGirl.EventHandler.resetGirls((EntityPlayerMP)player);
        }
        ResetGirl.EventHandler.resetGirl(this);
    }

    void al() {
        this.setAnchored(false);
        ManglelieEntity manglelie = this.getManglelieUUID(true);
        if (manglelie != null) {
            manglelie.setCorrupting(true);
        }
    }

    void GiveCoinToPlayer() {
        EntityPlayer player = this.getPlayerEntity();
        if (player != null) {
            ItemStack heldItem = player.getHeldItemMainhand();

            //Give player coin. If slot is not empty, just throw item in inventory.
            player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(GalathCoin.GALATH_COIN));
            if (!heldItem.isEmpty()) {
                player.inventory.addItemStackToInventory(heldItem);
            }

            PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(true), (EntityPlayerMP) player);

            this.setInteractionPlayerUUID((UUID) null);
            this.setTargetEntity((EntityLivingBase) null);
            //Congrats, you defeated and graped me. Here is the coin to spawn me.
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Defeating a succubus makes her accept the victor as her master, granting him a coin to which her soul is bound. Using the coin summons her, offering services on demand. If her master uses the coin on her or goes too far, she returns to the coin"));

            GalathMangTracker.updateMangleliePartner(this);
            player.setPositionAndUpdate(player.posX, Math.ceil(player.posY) + 1.0, player.posZ);
        }
    }

    @SideOnly(value=Side.CLIENT)
    void void_H() {
        float yaw;
        Action action = this.getCurrentAction();
        if (this.ab == Action.CORRUPT_INTRO || action != Action.CORRUPT_INTRO) {
            this.ab = action;
        } else {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (!player.getPersistentID().equals(this.getInteractionPlayerUUID())) {
                this.ab = action;
            } else {
                player.rotationYaw = yaw = this.hasMasterOAlgo() ? 0.0f : this.getYawRotation() + 180.0f;
                player.prevRotationYaw = yaw;
                player.rotationPitch = 80.0f;
                player.prevRotationPitch = 80.0f;
                this.ab = action;
            }
        }
    }

    void setFlying(boolean flying) {
        EntityPlayer player = this.getPlayerEntity();
        if (player != null) {
            Vec3d pos = flying ? new Vec3d(-0.5, 0.5f - player.getEyeHeight(), 0.4f).add(this.getTargetPosition()) : VectorMath.rotate(new Vec3d(0.5, 0.5f - player.getEyeHeight(), 0.4f), this.getYawRotation()).add(this.getTargetPosition());
            player.setPositionAndUpdate(pos.x, pos.y, pos.z);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public float getRenderScaleFactor() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.thirdPersonView != 0) {
            return 1.0f;
        }

        switch (this.getCurrentAction()) {
            case CORRUPT_INTRO: {
                if (!this.corruptIntroActive) break;
            }
            case CORRUPT_SLOW: 
            case CORRUPT_FAST: 
            case CORRUPT_CUM: {
                return 0.5f;
            }
        }
        return 1.0f;
    }

    @Override
    protected boolean supportsCustomModels() {
        return false;
    }

    public boolean canStartPussyLicking() {
        if (this.getManglelieUUID(true) != null) {
            return false;
        }

        ManglelieEntity manglelie = new ManglelieEntity(this.world);
        this.setManglelieUUID(manglelie.girlID());
        manglelie.setMommyUUID(this.girlID());
        manglelie.setCorrupting(true);
        manglelie.setCurrentAction(Action.RIDE_MOMMY_HEAD);
        manglelie.setPositionAndUpdate(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(manglelie);
        return true;
    }

    void Z() {
        if (!this.hasMasterOAlgo()) {
            Action action = this.getCurrentAction();
            if (action != Action.RAPE_CUM) {
                this.at = 0;
            } else {
                EntityPlayer player = this.getPlayerEntity();
                if (player == null) {
                    this.at = 0;
                }
                else if (++this.at == 15) {
                    player.attackEntityFrom(new CumDrainDamageSource(this), 2.1474836E9f);
                }

            }
        }
    }

    void void_O() {
        EntityLivingBase target = this.getAttackTarget();
        if (target != null) {
            for (EntityWitherSkeleton skeleton : this.witherSkeletons) {
                if (!skeleton.isDead && !(target.getDistance(skeleton) < 15.0f)) {
                    PackageHandler.INSTANCE.sendToAllTracking((IMessage) new SpawnEnergyBallParticlesPacket2(skeleton.getPositionVector(), true), (Entity) this);
                    skeleton.setDead();
                    this.world.removeEntity(skeleton);
                }
            }
        }
    }

    void KillWitherSkeletons() {
        if (this.entityDataManager.get(IS_FLYING_FLAG)) {
            for (EntityWitherSkeleton skeleton : this.witherSkeletons) {
                if (!skeleton.isDead) {
                    PackageHandler.INSTANCE.sendToAllTracking((IMessage) new SpawnEnergyBallParticlesPacket2(skeleton.getPositionVector(), true), (Entity) this);
                    skeleton.setDead();
                    this.world.removeEntity(skeleton);
                }
            }
            this.witherSkeletons.clear();
        }
    }

    public static void handlePlayerJoin(EntityPlayer player) {
        GirlEntity girl = GirlEntity.getServerGirlEntity(GalathMangTracker.getOwnerOf(player));
        if (girl != null) {
            if (girl.equals(player.getRidingEntity())) {
                girl.setInteractionPlayerUUID(player.getPersistentID());
                girl.setCurrentAction(Action.CONTROLLED_FLIGHT);
            }
        }
    }

    void aA() {
        for (EntityWitherSkeleton skeleton : this.witherSkeletons) {
            if (!skeleton.isDead && skeleton.ticksExisted % 10 == 0) {
                Set<? extends EntityPlayer> players = ((WorldServer) this.world).getEntityTracker().getTrackingPlayers(skeleton);
                for (EntityPlayer player : players) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketParticles(EnumParticleTypes.DRAGON_BREATH, false, (float) skeleton.posX, (float) skeleton.posY, (float) skeleton.posZ, 0.2f * (float) ThreadNames.getRandomSign(), skeleton.getEyeHeight() / 2.0f, 0.2f * (float) ThreadNames.getRandomSign(), 0.0f, 5, new int[0]));
                }
            }
        }
    }

    void aG() {
        ArrayList<EntityWitherSkeleton> deadSkeletons = new ArrayList<>();
        for (EntityWitherSkeleton skeleton : this.witherSkeletons) {
            if (skeleton.isDead) {
                deadSkeletons.add(skeleton);
            }
        }
        for (EntityWitherSkeleton skeleton : deadSkeletons) {
            this.witherSkeletons.remove(skeleton);
        }
    }

    void ad_() {
        if (this.getCurrentAction() == Action.KNOCK_OUT_STAND_UP) {
            ++this.bY;
            if ((double) this.bY == 39.0) {
                this.setNoGravity(true);
                this.setVelocity(0.0, 0.6f, 0.0);
                Vec3d pos = this.getPositionVector();
                Vec3d min = pos.subtract(2.0, 2.0, 2.0);
                Vec3d max = pos.add(2.0, 2.0, 2.0);
                AxisAlignedBB aabb = new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z);
                List<EntityLivingBase> entitiesInArea = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
                
                for (EntityLivingBase entity : entitiesInArea) {
                    if (!(entity instanceof GalathEntity)) {
                        Vec3d entityPos = entity.getPositionVector();
                        Vec3d dir = entityPos.subtract(pos).normalize();
                        entity.motionX = dir.x * 1.0;
                        entity.motionZ = dir.z * 1.0;
                        entity.motionY = 1.0;
                        entity.attackEntityFrom(new GalathDamageSource(this), 0.5f);
                        if (entity instanceof EntityPlayerMP) {
                            EntityPlayerMP player = (EntityPlayerMP) entity;
                            player.connection.sendPacket(new SPacketEntityVelocity(player));
                        }
                    }
                }
            }
            if (!(this.bY < 58.0)) {
                this.setMotionVector(Vec3d.ZERO);
                this.entityDataManager.set(IS_FLYING_FLAG, false);
                this.bY = 0;
            }
        }
    }

    void handleKnockOut() {
        if (this.getCurrentAction() == Action.KNOCK_OUT_GROUND) {
            if (!this.entityDataManager.get(HIDE_EFFECTS_FLAG)) {
                //this.b3++;
                if (!(++this.b3 < 50.0)) {
                    this.setCurrentAction(Action.KNOCK_OUT_STAND_UP);
                    this.bY = 0;
                    this.b3 = 0;
                }
            }
        }
    }

    void void_S() {
        Action action = this.getCurrentAction();
        if (action == Action.KNOCK_OUT_GROUND || action == Action.KNOCK_OUT_STAND_UP) {
            this.motionX = 0.0;
            this.motionZ = 0.0;
            if (this.entityDataManager.get(HIDE_EFFECTS_FLAG)) {
                this.motionY = 0.0;
            }
        }
    }

    void void_T() {
        if (this.getCurrentAction() == Action.KNOCK_OUT_FLY) {
            BlockPos pos = this.getPosition();
            if (this.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                BlockPos blockPos2 = pos;
                while (this.world.getBlockState(blockPos2.up()).getBlock() instanceof BlockLiquid) {
                    blockPos2 = blockPos2.up();
                }
                for (int i = -1; i < 2; ++i) {
                    for (int j = -1; j < 2; ++j) {
                        this.world.setBlockState(blockPos2.add(i, 0, j), Blocks.OBSIDIAN.getDefaultState());
                    }
                }
                blockPos2 = blockPos2.up();
                this.setPositionAndUpdate(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                this.setTargetPosition(new Vec3d(blockPos2));
                PackageHandler.INSTANCE.sendToAllTracking((IMessage) new SpawnEnergyBallParticlesPacket2(new Vec3d(blockPos2), true), (Entity) this);
                for (EntityPlayer entityPlayer : ((WorldServer) this.world).getEntityTracker().getTrackingPlayers(this)) {
                    ((EntityPlayerMP) entityPlayer).connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.AMBIENT, this.posX, this.posY, this.posZ, 1.0f, 1.0f));
                }
                this.setCurrentAction(Action.KNOCK_OUT_GROUND);
                return;
            }
            if (this.onGround) {
                this.setCurrentAction(Action.KNOCK_OUT_GROUND);
            }
        }
    }

    void void_J() {
        if (this.bZ == GalathFlightData.CHANGE_POSITION) {
            int progress = this.getFlyTicks();
            this.noClip = progress == 0;
            if (!this.world.isAirBlock(this.getPosition())) {
                this.noClip = true;
            }
        }
    }

    void checkFlightFinished() {
        if (this.bZ != null) {
            this.bZ.checkFinished(this);
        }
    }

    void D_() {
        if (this.getAttackTarget() == null) {
            this.aH();
        } else if (this.bZ == null) {
            this.initFlightData();
        } else {
            if (this.bZ.executeStart(this)) {
                this.initFlightData();
            }
        }
    }

    void initFlightData() {
        GalathFlightData chosen;
        if (!this.entityDataManager.get(IS_FLYING_FLAG)) {
            GalathFlightData flightData = this.bZ;
            if (this.getInteractionPlayerUUID() != null) {
                if (flightData != null) {
                    flightData.executeStop(this);
                }
                this.bZ = null;
                return;
            }
            if (flightData != null && flightData.applyAttackCoolDown) {
                flightData.executeStop(this);
                this.bZ = GalathFlightData.CHANGE_POSITION;
                this.bZ.executeStart(this);
                return;
            }
            GalathFlightData[] values = GalathFlightData.values();

            do {
                chosen = values[this.getRNG().nextInt(values.length)];
            } while (!this.canInitFlight(chosen));

            this.bZ = chosen;
            if (flightData != null) {
                flightData.executeStop(this);
            }
            this.bZ.executeStart(this);
        }
    }

    boolean canInitFlight(GalathFlightData flightData) {
        return (!flightData.onlyDoThisOnPlayers || this.getAttackTarget() instanceof EntityPlayer) && flightData.canExecute(this);
    }

    void aH() {
        this.bZ = null;
    }

    /*
    * Searches entities in area, then does something
    */

    void void_I() {
       // EntityLivingBase target;
        if (!this.hasFlightTarget()) {
            if (this.getInteractionPlayerUUID() == null) {
                boolean hasMaster = this.hasMasterOAlgo();
                float offset = hasMaster ? 7.0f : 20.0f;
                Vec3d area = new Vec3d(offset, offset, offset);
                Vec3d posVector = this.getPositionVector();
                Vec3d startPos = posVector.subtract(area);
                Vec3d EndPos = posVector.add(area);
                AxisAlignedBB SearchArea = new AxisAlignedBB(startPos.x, startPos.y, startPos.z, EndPos.x, EndPos.y, EndPos.z);
                EntityLivingBase target = hasMaster ? this.getMobsInBox(SearchArea) : this.getPlayerNearby(SearchArea);
                if (target == null) {
                    this.aI();
                } else {
                    this.setTargetEntity(target);
                    GirlEntity.girlPlaySound((GirlEntity) this, SoundsHandler.GIRLS_GALATH_DIALOG[1], true);
                    if (this.bZ != null) {
                        this.bZ.executeStop(this);
                    }
                    this.bZ = GalathFlightData.CHANGE_POSITION;
                    this.bZ.executeStart(this);
                }
            }
        }
    }

    /*
    * Searches for the players, that is in survival (or adventure, who tf uses adventure mod) and not having schmex in area
    * Then returns first player
    */

    EntityPlayer getPlayerNearby(AxisAlignedBB aabb) {
        List<EntityPlayer> players = this.world.getEntitiesWithinAABB(
                EntityPlayer.class,
                aabb,
                entityPlayer -> !PlayerGirl.isOwnerPlayer(entityPlayer) && !entityPlayer.isCreative() && !entityPlayer.isSpectator());
        return players.isEmpty() ? null : players.get(0);
    }

    /*
    * Выбирай:
    * Либо наждачкой подтереться (фикс геморроя)
    * Либо реверсить это.
    */

    EntityMob getMobsInBox(AxisAlignedBB aabb) {
        List<EntityMob> mobList = this.world.getEntitiesWithinAABB(EntityMob.class, aabb);
        if (mobList.isEmpty()) {
            return null;
        }

        ArrayList<EntityMob> validMobs = new ArrayList<EntityMob>();
        for (EntityMob mob : mobList) {
            if (GalathMobTarget.isValidTarget(mob)) {
                validMobs.add(mob);
            }
        }
        Vec3d eyePos = this.getPositionVector().add(0.0, this.getEyeHeight(), 0.0);
        for (EntityMob mob : validMobs) {
            if (GalathMobTarget.hasLineOfSight(this.world, eyePos, mob)) {
                return mob;
            }
        }
        return null;
    }

    void aI() {
        if (this.getAttackTarget() != null) {
            this.setTargetEntity((EntityLivingBase) null);
            if (this.bZ != null) {
                this.bZ.executeStop(this);
            }
            this.bZ = null;
            if (!this.entityDataManager.get(IS_FLYING_FLAG)) {
                this.setCurrentAction(Action.NULL);
            }
        }
    }

    boolean hasFlightTarget() {
        EntityLivingBase target = this.getAttackTarget();
        if (target == null) {
            return false;
        } else if (target.isDead) {
            return false;
        } else if (target.dimension != this.dimension) {
            return false;
        } else {
            float dist = this.getDistance(target);
            float maxDist = this.hasMasterOAlgo() ? 16.0f : 30.0f;
            if (dist > maxDist) {
                return false;
            } else if (!(target instanceof EntityPlayer)) {
                return true;
            } else {
                EntityPlayer player = (EntityPlayer)target;
                if (GirlEntity.getActiveSceneInfo(player.getPersistentID()) != null) {
                    return false;
                } else {
                    return !player.isCreative() && !player.isSpectator();
                }
            }
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public GirlEntity asGirl() {
        ManglelieEntity manglelie = this.getManglelieUUID(false);
        if (manglelie == null) {
            return super.asGirl();
        }

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.isSneaking()) {
            return manglelie;
        }
        player.sendStatusMessage(new TextComponentString((Object)((Object)TextFormatting.GRAY) + "[sneak] + [right click] if you want to edit Manglelie instead"), true);
        return super.asGirl();
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (this.hasMasterOAlgo()) {
            return this.a(entityPlayer, enumHand);
        }
        return this.processGirlInteract(entityPlayer, enumHand);
    }

    boolean a(EntityPlayer player, EnumHand hand) {
        if (!player.getPersistentID().equals(this.getMasterUUID())) {
            return false;
        }

        if (Action.isAnyAction((GirlEntity)this, Action.HUG_MANG, Action.RUN, Action.GALATH_SUMMON, Action.GALATH_DE_SUMMON, Action.MASTERBATE)) {
            return false;
        }

        if (!GalathCoin.GALATH_COIN.equals(player.getHeldItem(EnumHand.OFF_HAND).getItem()) && !GalathCoin.GALATH_COIN.equals(player.getHeldItem(EnumHand.MAIN_HAND).getItem())) {
            this.PlaySound(SoundsHandler.GIRLS_GALATH_HUH, new int[0]);
            String[] options = !player.onGround ? new String[]{"ride"} : (this.getManglelieUUID(false) == null ? new String[]{"cowgirl", "anal", "ride"} : new String[]{"cowgirl", "anal", "threesome", "ride"});
            if (this.world.isRemote) {
                GalathEntity.openInventoryGui(player, this.com_trolmastercard_sexmod_em_class258_af(), options, false);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void doAction(String action, UUID uUID) {
        if ("ride".equals(action)) {
            GalathFlightUI.showUI();
            PackageHandler.INSTANCE.sendToServer((IMessage)new RequestRiding());
            return;
        }
        if ("anal".equals(action)) {
            BlackScreenUI.run();
            HandlePlayerMovement.setMovementLock(false);
            ThreadNames.createDaemonThread(1200, () -> {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                this.setTargetPosition(player.getPositionVector());
                this.setYawRotation(0.0f);
                this.setInteractionPlayerUUID(player.getPersistentID());
                this.setAnchored(true);
                this.setCurrentAction(Action.CORRUPT_SLOW);
            });
            return;
        }
        if ("cowgirl".equals(action)) {
            BlackScreenUI.run();
            HandlePlayerMovement.setMovementLock(false);
            ThreadNames.createDaemonThread(1200, () -> {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                this.setTargetPosition(player.getPositionVector());
                this.setYawRotation(player.rotationYaw + 180.0f);
                this.setCurrentAction(Action.RAPE_INTRO);
                this.setInteractionPlayerUUID(player.getPersistentID());
                this.setAnchored(true);
            });
            return;
        }
        if ("threesome".equals(action)) {
            ManglelieEntity manglelie = this.getManglelieUUID(false);
            if (manglelie != null) {
                BlackScreenUI.run();
                HandlePlayerMovement.setMovementLock(false);
                ThreadNames.createDaemonThread(1200, () -> {
                    Minecraft minecraft = Minecraft.getMinecraft();
                    EntityPlayerSP entityPlayerSP = minecraft.player;
                    minecraft.gameSettings.thirdPersonView = 1;
                    manglelie.setTargetPosition(entityPlayerSP.getPositionVector());
                    this.setTargetPosition(entityPlayerSP.getPositionVector());
                    manglelie.setYawRotation(entityPlayerSP.rotationYaw + 180.0f);
                    this.setYawRotation(entityPlayerSP.rotationYaw);
                    manglelie.setCurrentAction(Action.THREESOME_SLOW);
                    this.setCurrentAction(Action.PUSSY_LICKING);
                    manglelie.setInteractionPlayerUUID(entityPlayerSP.getPersistentID());
                    this.setInteractionPlayerUUID(entityPlayerSP.getPersistentID());
                    manglelie.setAnchored(true);
                    this.setAnchored(true);
                });
            }
        }
    }

    boolean processGirlInteract(EntityPlayer player, EnumHand hand) {
        if (!this.entityDataManager.get(IS_FLYING_FLAG)) {
            return super.processInteract(player, hand);
        } else if (this.getCurrentAction() != Action.KNOCK_OUT_GROUND) {
            return super.processInteract(player, hand);
        } else if (this.world.isRemote) {
            player.rotationYaw -= -128.0f;
            player.rotationPitch = 19.0f;
            return true;
        } else {
            this.setCurrentAction(Action.CORRUPT_INTRO);
            this.setInteractionPlayerUUID(player.getPersistentID());
            this.setAnchored(true);
            this.setTargetPosition(this.getPositionVector());
            this.setYawRotation(player.rotationYaw);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)player);
            player.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            return true;
        }
    }

    @Override
    @Nullable
    public Entity[] getParts() {
        return new Entity[]{this.energyBallHitboxRight, this.energyBallHitboxLeft};
    }

    public void setTargetEntity(@Nullable EntityLivingBase entity) {
        if (entity == null) {
            this.entityDataManager.set(bq, -1);
        } else {
            this.entityDataManager.set(bq, entity.getEntityId());
        }
    }

    public int getFlyTicks() {
        return this.entityDataManager.get(FLY_TICKS);
    }

    public void setFlyTicks(int ticks) {
        this.entityDataManager.set(FLY_TICKS, ticks);
    }

    public EntityLivingBase getAttackTarget() {
        int targetID = this.entityDataManager.get(bq);
        return -1 == targetID ? null : (EntityLivingBase) this.world.getEntityByID(targetID);
    }

    public static Float getAimYaw(GalathEntity galath, float partialTicks) {
        float yaw;
        Action action = galath.getCurrentAction();
        if (action != Action.FLY && action != Action.SUMMON_SKELETON && action != Action.RAPE_PREPARE) {
            return null;
        }

        EntityLivingBase target = galath.getAttackTarget();
        if (target == null) {
            return null;
        }

        Vec3d targetPos = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ), target.getPositionVector(), (double)partialTicks);
        Vec3d selfPos = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(galath.lastTickPosX, galath.lastTickPosY, galath.lastTickPosZ), galath.getPositionVector(), (double)partialTicks);
        Vec3d delta = targetPos.subtract(selfPos);
        galath.renderYawOffset = yaw = (float) TrigMath.sinDegrees(Math.atan2(delta.z, delta.x)) - 90.0f;
        galath.prevRenderYawOffset = yaw;
        return yaw;
    }

    void playHurtSound(float damage) {
        if (this.world.isRemote) {
            if (!(this.getHealth() - damage <= 0.0f)) {
                long now = System.currentTimeMillis();
                if (now >= this.bc + 1000L) {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_UUH);
                    this.bc = now;
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isFireDamage()) {
            return false;
        }
        if (DamageSource.DROWN.equals(source)) {
            return false;
        }
        if (DamageSource.CACTUS.equals(source)) {
            return false;
        }
        if (DamageSource.FALL.equals(source)) {
            return false;
        }
        if (DamageSource.FLY_INTO_WALL.equals(source)) {
            return false;
        }

        this.playHurtSound(amount);
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
        if (this.world.isRemote) {
            return false;
        }
        if (!(source.getTrueSource() instanceof EntityPlayer)) {
            return false;
        }

        if (part == this.energyBallHitboxRight) {
            this.entityDataManager.set(b7, false);
            PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesPacket2(this.energyBallHitboxRight.getPositionVector(), false), (Entity)this);
        }
        if (part == this.energyBallHitboxLeft) {
            this.entityDataManager.set(bN, false);
            PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesPacket2(this.energyBallHitboxLeft.getPositionVector(), false), (Entity)this);
        }
        return true;
    }

    @Override
    public void ResetNPCTasks() {
        this.setTargetEntity((EntityLivingBase)null);
        this.aH();
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void setFire(int seconds) {
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    @Nullable
    protected Action getNextAction(Action action) {
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.CORRUPT_FAST || action == Action.CORRUPT_SLOW) {
            return Action.CORRUPT_CUM;
        }
        if (action == Action.RAPE_ON_GOING) {
            return Action.RAPE_CUM;
        }
        if (Action.isAny(action, Action.MORNING_BLOWJOB_SLOW, Action.MORNING_BLOWJOB_FAST)) {
            this.morningBlowjobStarted = true;
        }
        return null;
    }

    @Override
    public boolean isWingsAnimated() {
        return this.isRenderingOverlayDisabled;
    }

    @Override
    public boolean isWingsVisible() {
        switch (this.getCurrentAction()) {
            case CORRUPT_SLOW: 
            case CORRUPT_FAST: 
            case CORRUPT_CUM: 
            case COWGIRLCUM: {
                return false;
            }
        }
        return true;
    }

    public void handleRapeAction(boolean applyDamage) {
        Action action = this.getCurrentAction();
        if (action == Action.RAPE_ON_GOING || action == Action.RAPE_INTRO) {
            EntityPlayer player = this.getPlayerEntity();
            if (player != null) {
                if (!(0.0f >= player.getHealth() - 1.0f)) {
                    if (!player.capabilities.isCreativeMode) {
                        player.attackEntityFrom(new CumDrainDamageSource(this), 1.0f);
                        if (applyDamage) {
                            this.heal(1.5f);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("sexmod:master", (String)this.entityDataManager.get(MASTER));
        if (this.isDespawned) {
            nbt.setBoolean("sexmod:despawned", true);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        String npcName;
        UUID masterUuid;
        super.readEntityFromNBT(nbt);
        this.entityDataManager.set(MASTER, nbt.getString("sexmod:master"));
        if (nbt.getBoolean("sexmod:despawned")) {
            this.despawned = true;
        }
        if ((masterUuid = this.getMasterUUID()) != null && (npcName = AllieWorldData.getCustomName(masterUuid, PlayerGirlEntity.GALATH)) != null) {
            this.setCustomNameOverride(npcName);
        }
    }

    public void startFastAction() {
        if (this.getCurrentAction() != Action.MASTERBATE_SITTING) {
            this.bx = true;
            this.setCurrentAction(Action.MASTERBATE_SITTING);
        }
    }

    public void startSlowAction() {
        this.a5 = true;
        this.setCurrentAction(Action.PUSSY_LICKING);
    }

    @Override
    protected boolean handleActionAnimationOverrides(Action action, String animName, boolean flag, AnimationEvent event) {
        if (action == Action.MASTERBATE_SITTING && this.bx) {
            this.bx = false;
            this.createAnimation("animation.galath.masterbating_sitting", true, event, true);
            return true;
        }
        if (action == Action.MORNING_BLOWJOB_FAST && this.morningBlowjobStarted) {
            this.setCurrentAction(Action.MORNING_BLOWJOB_CUM);
            return true;
        }
        if (action == Action.MORNING_BLOWJOB_FAST && this.isTransformingManglelie) {
            this.createAnimation("animation.shared.bed_fast", true, event, true);
            this.isTransformingManglelie = false;
            return true;
        }
        if (action == Action.MORNING_BLOWJOB_CUM) {
            this.setCurrentAction((Action)null);
            return true;
        }
        if (action == Action.PUSSY_LICKING && this.a5) {
            this.a5 = false;
            this.createAnimation("animation.galath.pussy_licking", true, event, true);
            return true;
        }
        if (action == Action.MORNING_BLOWJOB_SLOW && (this.morningBlowjobStarted || HandlePlayerMovement.isThrusting)) {
            this.isTransformingManglelie = true;
            this.setCurrentAction(Action.MORNING_BLOWJOB_FAST);
            this.createAnimation("animation.shared.bed_soft", true, event, true);
            return true;
        }
        if (action == Action.MORNING_BLOWJOB_SLOW && this.bt) {
            this.bt = false;
            this.createAnimation("animation.shared.bed_slow", true, event, true);
            return true;
        }
        if (action == Action.MORNING_BLOWJOB_FAST && !HandlePlayerMovement.isThrusting) {
            this.setCurrentAction(Action.MORNING_BLOWJOB_SLOW);
            this.bt = true;
            this.createAnimation("animation.shared.bed_back", true, event, true);
            return true;
        }
        return false;
    }

    public float getSwordAttackProgres(float partialTicks) {
        Action action = this.getCurrentAction();
        if (action == Action.PUSSY_LICKING && !this.a5) {
            return 0.0f;
        }
        if (action == Action.MASTERBATE_SITTING && !this.bx) {
            return 1.0f;
        }
        float scale = Action.getActionTimeScale(this, partialTicks);
        return action == Action.MASTERBATE_SITTING ? scale : 1.0f - scale;
    }

    // TODO
    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isLocallyRegistered()) {
            this.createAnimation("animation.galath.idle", true, event);
            return PlayState.CONTINUE;
        }
        Action action = this.getCurrentAction();
        AnimationController controller = event.getController();
        controller.setAnimationSpeed(1.0);
        if (controller.equals(this.eyesController)) {
            if (!action.autoBlink || action == Action.GALATH_DE_SUMMON) {
                return PlayState.STOP;
            }
            this.createAnimation("animation.galath.blink", true, event);
            return PlayState.CONTINUE;
        }
        if (controller.equals(this.movementController)) {
            if (action != Action.NULL) {
                return PlayState.STOP;
            }
            if (!this.onGround) {
                this.createAnimation("animation.galath.controlled_flight", true, event);
                return PlayState.CONTINUE;
            }
            Vec3d vec3d = this.getPositionVector().subtract(new Vec3d(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ));
            if (vec3d.equals(Vec3d.ZERO)) {
                this.createAnimation("animation.galath.idle", true, event);
                return PlayState.CONTINUE;
            }
            this.rotationYaw = this.rotationYawHead;
            this.createAnimation("animation.galath." + (this.entityDataManager.get(bT) != false ? "run" : "walk"), true, event);
            return PlayState.CONTINUE;
        }
        switch (this.getCurrentAction()) {
            case NULL: {
                return PlayState.STOP;
            }
            case FLY: {
                this.createAnimation("animation.galath.idle_flying", true, event);
                break;
            }
            case SUMMON_SKELETON: {
                this.createAnimation("animation.galath.summon_skeleton" + (this.entityDataManager.get(ay) != false ? "Mirrored" : ""), true, event);
                break;
            }
            case ATTACK_SWORD: {
                this.createAnimation("animation.galath.attack", true, event);
                break;
            }
            case KNOCK_OUT_FLY: {
                controller.setAnimationSpeed(1.5);
                this.createAnimation("animation.galath.knockout_air", true, event);
                break;
            }
            case KNOCK_OUT_GROUND: {
                this.createAnimation("animation.galath.knocked_out", true, event);
                break;
            }
            case KNOCK_OUT_STAND_UP: {
                this.createAnimation("animation.galath.knocked_out_stand_up", true, event);
                break;
            }
            case RAPE_PREPARE: {
                this.createAnimation("animation.galath.rape_prepare", true, event);
                break;
            }
            case RAPE_CHARGE: {
                this.createAnimation("animation.galath.rape_charge", true, event);
                break;
            }
            case RAPE_INTRO: {
                this.createAnimation("animation.galath.rape_intro", true, event);
                break;
            }
            case RAPE_ON_GOING: {
                this.createAnimation("animation.galath.rape" + this.b1, true, event);
                break;
            }
            case RAPE_CUM: {
                this.createAnimation("animation.galath.rape_cum", true, event);
                break;
            }
            case RAPE_CUM_IDLE: {
                this.createAnimation("animation.galath.rape_cum_idle", true, event);
                break;
            }
            case CORRUPT_FAST: {
                this.createAnimation("animation.galath.corrupt_" + (this.aT ? "hard" : "soft"), true, event);
                break;
            }
            case CORRUPT_SLOW: {
                this.createAnimation("animation.galath.corrupt_slow", true, event);
                break;
            }
            case CORRUPT_INTRO: {
                this.createAnimation("animation.galath.corrupt_intro", true, event);
                break;
            }
            case CORRUPT_CUM: {
                this.createAnimation("animation.galath.corrupt_cum", true, event);
                break;
            }
            case CONTROLLED_FLIGHT: {
                this.createAnimation("animation.galath.controlled_flight", true, event);
                break;
            }
            case BOOST: {
                this.createAnimation("animation.galath.boost", true, event);
                break;
            }
            case GALATH_SUMMON: {
                this.createAnimation("animation.galath.summon", false, event);
                break;
            }
            case GALATH_DE_SUMMON: {
                this.createAnimation("animation.galath.desummon" + (this.onGround ? "_standing" : ""), true, event);
                break;
            }
            case GIVE_COIN: {
                this.createAnimation("animation.galath.give_coin", true, event);
                break;
            }
            case MASTERBATE: {
                this.createAnimation("animation.galath.masterbate", true, event);
                break;
            }
            case RUN: {
                controller.setAnimationSpeed(0.7);
                this.createAnimation("animation.galath.running", true, event);
                break;
            }
            case HUG_MANG: {
                this.createAnimation("animation.galath.hug_mang", true, event);
                break;
            }
            case PUSSY_LICKING: {
                this.createAnimation(this.a5 ? "animation.galath.pussy_licking_forward" : "animation.galath.pussy_licking", true, event);
                break;
            }
            case MASTERBATE_SITTING: {
                this.createAnimation(this.bx ? "animation.galath.pussy_licking_back" : "animation.galath.masterbating_sitting", true, event);
                break;
            }
            case MASTERBATE_SITTING_CUM: {
                this.createAnimation("animation.galath.masterbating_sitting_cum", true, event);
                break;
            }
            case MORNING_BLOWJOB_SLOW: {
                this.createAnimation(this.bt ? "animation.shared.bed_back" : "animation.shared.bed_slow", true, event);
                break;
            }
            case MORNING_BLOWJOB_FAST: {
                if (this.isTransformingManglelie) {
                    this.createAnimation("animation.shared.bed_soft", true, event);
                    break;
                }
                this.playRandomizedAnimation("animation.shared.bed_fast", 4, 0.75f, event);
                break;
            }
            case MORNING_BLOWJOB_CUM: {
                this.createAnimation("animation.shared.bed_cum", true, event);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        this.actionController = new bz_class107<>(this, "action", 0.0f, this::predicate);
        this.movementController = new AnimationController<>(this, "movement", 5.0f, this::predicate);
        this.eyesController = new AnimationController<>(this, "eyes", 10.0f, this::predicate);
        this.actionController.registerSoundListener(soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "goodTiming": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_DIALOG[4]);
                    this.sendLocalClientMessage("Good timing boy~");
                    break;
                }
                case "huh": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_HUH, new int[0]);
                    break;
                }
                case "giggle": {
                    Vec3d vec3d = this.getVectorTowardPlayer();
                    this.world.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.random(SoundsHandler.GIRLS_GALATH_GIGGLE), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                case "dialog1": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_DIALOG[1]);
                    break;
                }
                case "moan": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_MOAN, new int[0]);
                    break;
                }
                case "breath": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_BREATHING, new int[0]);
                    break;
                }
                case "dialog5": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_DIALOG[5]);
                    break;
                }
                case "switchmoan": {
                    if (this.a6) {
                        this.PlaySound(SoundsHandler.GIRLS_GALATH_BREATHING, new int[0]);
                    } else {
                        this.PlaySound(this.getRNG().nextBoolean() ? SoundsHandler.GIRLS_GALATH_MOAN : SoundsHandler.GIRLS_GALATH_AHH, new int[0]);
                    }
                    this.a6 = !this.a6;
                    break;
                }
                case "lightcharge": {
                    Vec3d vec3d = this.getVectorTowardPlayer();
                    this.world.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.random(SoundsHandler.GIRLS_GALATH_LIGHTCHARGE), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                case "strongcharge": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_STRONGCHARGE, new int[0]);
                    break;
                }
                case "hmph": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_HMPH, new int[0]);
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "giggle0": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_GIGGLE[0]);
                    break;
                }
                case "orgasm": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_ORGASM, new int[0]);
                    break;
                }
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "flap": {
                    Vec3d vec3d = this.getVectorTowardPlayer();
                    this.world.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.random(SoundsHandler.MISC_FLAP), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                case "startRenderSword": {
                    this.hasSwordEquipped = true;
                    this.bu = true;
                    break;
                }
                case "stopFadeInParticles": {
                    this.bu = false;
                    break;
                }
                case "stopRenderSword": {
                    this.hasSwordEquipped = false;
                    this.bu = false;
                    break;
                }
                case "dontDrawStars": {
                    this.aL = false;
                    break;
                }
                case "setNude": {
                    this.isRenderingOverlayDisabled = true;
                    Vec3d vec3d = this.getPositionVector();
                    Vec3d vec3d2 = this.getCachedBoneOffset("slipR").add(vec3d);
                    Vec3d vec3d3 = this.getCachedBoneOffset("slipL").add(vec3d);
                    Vec3d vec3d4 = this.getCachedBoneOffset("turnable").add(vec3d);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d2.x, vec3d2.y, vec3d2.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d3.x, vec3d3.y, vec3d3.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d4.x, vec3d4.y, vec3d4.z, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case "rapeIntroDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.RAPE_ON_GOING);
                    break;
                }
                case "rape_switch": {
                    EntityPlayerSP player;
                    Random random = this.getRNG();
                    int n = this.b1;
                    do {
                        this.b1 = random.nextInt(3);
                    } while (this.b1 == n);
                    if (this.hasMasterOAlgo() || !this.isControlledByLocalPlayer() || !(0.0f >= (player = Minecraft.getMinecraft().player).getHealth() - 1.0f)) break;
                    this.setCurrentAction(Action.RAPE_CUM);
                    break;
                }
                case "poundRape": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
                    if (this.hasMasterOAlgo()) {
                        SexUI.addCumPercentage(0.03f);
                        break;
                    }
                    PackageHandler.INSTANCE.sendToServer((IMessage)new GalathRapePounce(true));
                    break;
                }
                case "rapeHurt": {
                    if (this.hasMasterOAlgo() || !this.isControlledByLocalPlayer()) break;
                    PackageHandler.INSTANCE.sendToServer((IMessage)new GalathRapePounce(false));
                    break;
                }
                case "enableRapeUI": {
                    if (!this.isControlledByLocalPlayer()) break;
                    if (this.hasMasterOAlgo()) {
                        SexUI.a(false);
                        break;
                    }
                    EscapeMinigameUI.StartMinigame();
                    break;
                }
                case "removeUI": {
                    if (!this.isControlledByLocalPlayer() || this.hasMasterOAlgo()) break;
                    EscapeMinigameUI.StartClosingAnimation();
                    break;
                }
                case "reloadRenderer": {
                    if (!this.isControlledByLocalPlayer()) {
                        return;
                    }
                    Minecraft minecraft = Minecraft.getMinecraft();
                    if (minecraft.gameSettings.thirdPersonView == 0) break;
                    minecraft.renderGlobal.loadRenderers();
                    break;
                }
                case "corruptSwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CORRUPT_FAST);
                    break;
                }
                case "corrupt_hard": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.aT = true;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "corrupt_hard_end": {
                    this.setCurrentAction(Action.CORRUPT_SLOW);
                    this.aT = false;
                    break;
                }
                case "addCum": {
                    SexUI.addCumPercentage(0.03);
                    break;
                }
                case "clearcum": {
                    ParticlesManager.spawnSexParticles(this);
                    break;
                }
                case "setCamCorrupt": {
                    if (!this.isControlledByLocalPlayer()) {
                        return;
                    }
                    this.corruptIntroActive = true;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    float f = this.getYawRotation().floatValue() + 220.0f;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(0.5, 0.5f - entityPlayerSP.getEyeHeight(), 0.4f), this.getYawRotation().floatValue()).add(this.getTargetPosition());
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, f, 15.0f));
                    SexUI.showUI();
                    break;
                }
                case "enableBoyCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.corruptIntroActive = false;
                    break;
                }
                case "masterbateCumming": {
                    if (!FutaCommand.enabled) break;
                    ParticlesManager.a(new DynamicTrailRenderer(90, girlEntity -> {
                        Vec3d vec3d = girlEntity.getBoneWorldPosition("futaCockTip");
                        Vec3d vec3d2 = girlEntity.getBoneWorldPosition("futaCockTipDirHelp");
                        return vec3d.subtract(vec3d2).normalize();
                    }, em_class2582 -> em_class2582.getCachedBoneOffset("futaCockTip").add(em_class2582.getTargetPosition()), this, 0.3f, 0.3f));
                    break;
                }
                case "creampie": {
                    ParticlesManager.a(new DynamicTrailRenderer(100, em_class2582 -> VectorMath.rotate(new Vec3d(0.0, 0.0, 0.6f), this.getYawRotation().floatValue()), em_class2582 -> em_class2582.getCachedBoneOffset("creampiePos").add(em_class2582.getTargetPosition()), this, 0.6f, 0.5f));
                    // TODO fallthrough looks intentional
                }
                case "creampieGalath": {
                    if (FutaCommand.enabled) {
                        ParticlesManager.a(new DynamicTrailRenderer(130, em_class2582 -> {
                            Vec3d vec3d = em_class2582.getBoneWorldPosition("futaCockTip");
                            Vec3d vec3d2 = em_class2582.getBoneWorldPosition("futaCockTipDirHelp");
                            return vec3d.subtract(vec3d2).normalize();
                        }, em_class2582 -> em_class2582.getCachedBoneOffset("futaCockTip").add(em_class2582.getTargetPosition()), this, 0.3f, 0.3f));
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_SMALLINSERTS), 3.0f);
                    break;
                }
                case "blackScreenTamed": {
                    if (!this.hasMasterOAlgo()) break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "blackScreenMaster": {
                    if (!Minecraft.getMinecraft().player.getPersistentID().equals(this.getMasterUUID())) break;
                    BlackScreenUI.run();
                    HandlePlayerMovement.setMovementLock(false);
                    break;
                }
                case "flapControlled": {
                    if (!this.isControlledByLocalPlayer()) break;
                    GalathFlightUI.showUI();
                    this.PlaySound(SoundsHandler.MISC_FLAP, new int[0]);
                    Minecraft minecraft = Minecraft.getMinecraft();
                    EntityPlayerSP entityPlayerSP = minecraft.player;
                    MovementInput movementInput = entityPlayerSP.movementInput;
                    Vec2f vec2f = movementInput.getMoveVector();
                    if (vec2f.x == 0.0f && vec2f.y == 0.0f) break;
                    Vec3d vec3d = VectorMath.rotate(new Vec3d(-vec2f.x, 0.0, vec2f.y), ReferenceAndRotationHelper.LerpFloat(entityPlayerSP.prevRotationPitch, entityPlayerSP.rotationPitch, minecraft.getRenderPartialTicks()), ReferenceAndRotationHelper.LerpFloat(entityPlayerSP.prevRotationYawHead, entityPlayerSP.rotationYawHead, minecraft.getRenderPartialTicks()));
                    PackageHandler.INSTANCE.sendToServer((IMessage)new UpdateVelocity(vec3d, this.girlID()));
                    break;
                }
                case "clap": {
                    this.PlaySound(SoundsHandler.MISC_CLAP, new int[0]);
                    break;
                }
                case "energysound": {
                    this.PlaySound(SoundsHandler.MISC_BEEW[1]);
                    break;
                }
                case "energy2": {
                    this.PlaySound(SoundsHandler.MISC_BEEW[2]);
                    break;
                }
                case "tpSound": {
                    this.PlaySound(SoundsHandler.MISC_WEOWEO[2]);
                    break;
                }
                case "lick": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    break;
                }
                case "setCoinLook": {
                    float f;
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                    playerSP.rotationYaw = f = this.getYawRotation() + 180.0f;
                    playerSP.prevRotationYaw = f;
                    playerSP.rotationPitch = 0.0f;
                    playerSP.prevRotationPitch = 0.0f;
                    break;
                }
                case "sexui": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "boostSound": {
                    Minecraft.getMinecraft().player.playSound(SoundsHandler.random(SoundsHandler.GIRLS_GALATH_LIGHTCHARGE), 1.0f, 1.0f);
                    Minecraft.getMinecraft().player.playSound(SoundsHandler.random(SoundsHandler.MISC_FLAP), 1.0f, 1.0f);
                }
            }
        });
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.eyesController);
        data.addAnimationController(this.movementController);
    }

    public static class EventHandLer {
        boolean hasRidingPlayer(GalathEntity galath) {
            return galath.getRidingPlayer() != null;
        }

        @SubscribeEvent(priority=EventPriority.LOWEST)
        public void canSpawn(LivingSpawnEvent.CheckSpawn event) {
            //World world;
            Event.Result result = event.getResult();
            if (result != Event.Result.DENY) {
                if (!event.isSpawner()) {
                    Entity entity = event.getEntity();
                    if (entity instanceof EntityWitherSkeleton || entity instanceof EntityBlaze) {
                        BlockPos blockPos = entity.getPosition();
                        World world = entity.world;
                        if (GalathEntity.isNearHive(blockPos, world)) {
                            event.setResult(Event.Result.DENY);
                            StructureTracker.addPosInList(blockPos, StructureTracker.STRUCTURE_POSITIONS);
                            GalathEntity galath = new GalathEntity(world);
                            galath.setPositionAndUpdate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            world.spawnEntity(galath);
                        }
                    }
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (GalathFlightUI.canUseCharge()) {
                    for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                        if (!girl.world.isRemote || !(girl instanceof GalathEntity) || !mc.player.getPersistentID().equals(((GalathEntity) girl).ax()))
                            continue;
                        GalathFlightUI.consumeCharge();
                        girl.setCurrentAction(Action.BOOST);
                        return;
                    }
                }
            }
        }

        @SubscribeEvent
        public void onMount(EntityMountEvent event) {
            if (!event.isMounting()) {
                Entity entity = event.getEntityBeingMounted();
                if (entity instanceof GalathEntity) {
                    if (entity.world.isRemote) {
                        GalathFlightUI.startFadeOutTimer();
                    } else {
                        ((GalathEntity) entity).resetInteractionState();
                    }
                }
            }
        }

        @SubscribeEvent(priority=EventPriority.HIGH)
        public void onLivingDeath(LivingDeathEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof GalathEntity) {
                if (!event.getSource().equals(DamageSource.OUT_OF_WORLD)) {
                    GalathEntity galath = (GalathEntity) entity;
                    if (!galath.bU) {
                        if (!entity.world.isRemote) {
                            if (!galath.hasMasterOAlgo()) {
                                galath.sendTrackingMessage((Entity) galath.getCombatTracker().getFighter());
                            } else {
                                GalathCoin.a(galath);
                                PackageHandler.INSTANCE.sendToAllTracking((IMessage) new SpawnEnergyBallParticles(galath.girlID(), GalathMangTracker.getManglelieOwnerOf(galath)), (Entity) galath);
                                ThreadNames.createDaemonThread(900, () -> GalathMangTracker.updateMangleliePartner(galath));
                                galath.bU = true;
                            }
                            galath.setHealth(1.0f);
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            GirlEntity girl = GirlEntity.getGirlByUUID(player.getPersistentID(), true);
            if (girl instanceof GalathEntity) {
                GalathEntity galath = (GalathEntity) girl;
                galath.setTargetEntity((EntityLivingBase) null);
                ResetGirl.EventHandler.resetGirl(girl);
                PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(true), player);
                girl.setCurrentAction((Action) null);
                if (galath.bZ != null) {
                    galath.bZ.executeStop(galath);
                    galath.bZ = null;
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderManager manager = mc.getRenderManager();
            float partialTicks = mc.getRenderPartialTicks();

            for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                EnergyBallEntity energyBallEntity;
                Vec3d pos;
                Vec3d offset;
                double progress;
                if (girl instanceof GalathEntity && girl.world.isRemote && girl.getCurrentAction() == Action.SUMMON_SKELETON && !((progress = (double) ((GalathEntity) girl).ad) < 9.0) && !(progress > 30.0)) {
                    Vec3d basePos = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(girl.lastTickPosX, girl.lastTickPosY, girl.lastTickPosZ), girl.getPositionVector(), (double) partialTicks);
                    double scale = (progress - 9.0) / 21.0;
                    if (girl.getDataManager().get(bN)) {
                        offset = girl.getCachedBoneOffset("energyBallR");
                        pos = basePos.add(offset);
                        energyBallEntity = new EnergyBallEntity(girl.world, (GalathEntity) girl);
                        energyBallEntity.SCALE_1_0 = scale;
                        energyBallEntity.setPositionAndUpdate(pos.x, pos.y, pos.z);
                        manager.renderEntity(energyBallEntity, 0.0, 0.0, 0.0, 0.0f, partialTicks, true);
                        energyBallEntity.setPosition(0.0, -500.0, 0.0);
                        energyBallEntity.setDead();
                    }
                    if (girl.getDataManager().get(b7)) {
                        offset = girl.getCachedBoneOffset("energyBallL");
                        pos = basePos.add(offset);
                        energyBallEntity = new EnergyBallEntity(girl.world, (GalathEntity) girl);
                        energyBallEntity.setPositionAndUpdate(pos.x, pos.y, pos.z);
                        energyBallEntity.SCALE_1_0 = scale;
                        manager.renderEntity(energyBallEntity, 0.0, 0.0, 0.0, 0.0f, partialTicks, true);
                        energyBallEntity.setPosition(0.0, -500.0, 0.0);
                        energyBallEntity.setDead();
                    }
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
        }

        boolean spawnStructure(World world, BlockPos pos, EnumFacing facing) {
            if (facing == EnumFacing.NORTH) {
                if (this.isValidFlightBlock(world, pos = pos.west())) {
                    return false;
                } else if (this.isValidFlightBlock(world, pos.up())) {
                    return false;
                } else {
                    return !this.isValidFlightBlock(world, pos.south()) && !this.isValidFlightBlock(world, pos.south().up());
                }

            }
            if (facing == EnumFacing.WEST) {
                if (this.isValidFlightBlock(world, pos = pos.south())) {
                    return false;
                }
                if (this.isValidFlightBlock(world, pos.up())) {
                    return false;
                }
                if (this.isValidFlightBlock(world, pos.east())) {
                    return false;
                }
                return !this.isValidFlightBlock(world, pos.east().up());
            }
            if (facing == EnumFacing.SOUTH) {
                if (this.isValidFlightBlock(world, pos = pos.east())) {
                    return false;
                }
                if (this.isValidFlightBlock(world, pos.up())) {
                    return false;
                }
                if (this.isValidFlightBlock(world, pos.north())) {
                    return false;
                }
                return !this.isValidFlightBlock(world, pos.north().up());
            }
            if (facing == EnumFacing.EAST) {
                if (this.isValidFlightBlock(world, pos = pos.north())) {
                    return false;
                }
                if (this.isValidFlightBlock(world, pos.up())) {
                    return false;
                }
                if (this.isValidFlightBlock(world, pos.west())) {
                    return false;
                }
                return !this.isValidFlightBlock(world, pos.west().up());
            }
            Main.LOGGER.error("Weird bed orientation, when checking for space next to bed, on galaths morning blowjob animation: " + facing.getName());
            return false;
        }

        boolean isValidFlightBlock(World world, BlockPos pos) {
            Block block = world.getBlockState(pos).getBlock();
            for (Class<?> blockClass : aS) {
                if (blockClass.isInstance(block)) {
                    return false;
                }
            }
            return true;
        }

        @SubscribeEvent
        public void onWake(PlayerWakeUpEvent event) {
            float yaw;
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                if (GalathMangTracker.isReadyForMorningGlory(player.getPersistentID(), player.world)) {
                    Vec3d vec3d = player.getPositionVector();
                    BlockPos blockPos = new BlockPos(vec3d);
                    if (!this.spawnStructure(player.world, blockPos, player.world.getBlockState(blockPos).getValue(BlockHorizontal.FACING))) {
                        player.sendMessage(new TextComponentString(String.format("%sFor Galath and Manglelie to %swake you up with a blowjob%s, you have to provide enough space to the %sright side%s of your bed. This includes the %stop and bottom half%s of the bed.", new Object[]{TextFormatting.GRAY, TextFormatting.DARK_RED, TextFormatting.GRAY, TextFormatting.DARK_RED, TextFormatting.GRAY, TextFormatting.DARK_RED, TextFormatting.GRAY})));
                    } else {
                        switch (player.world.getBlockState(blockPos).getValue(BlockHorizontal.FACING)) {
                            case NORTH: {
                                yaw = 180.0f;
                                break;
                            }
                            case EAST: {
                                yaw = -90.0f;
                                break;
                            }
                            case WEST: {
                                yaw = 90.0f;
                                break;
                            }
                            default: {
                                yaw = 0.0f;
                                break;
                            }
                        }

                        Vec3d spawnPos = new Vec3d((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5);
                        UUID uUID = GalathMangTracker.getOwnerOf(player);
                        if (uUID != null) {
                            GalathMangTracker.updateMangleliePartner((GalathEntity) GirlEntity.getServerGirlEntity(uUID));
                        }
                        GalathEntity galath = new GalathEntity(player.world, player, vec3d, true);
                        galath.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                        player.world.spawnEntity(galath);
                        GalathMangTracker.grantOwnership(player, galath);
                        galath.canStartPussyLicking();
                        galath.setTargetPosition(spawnPos);
                        galath.setYawRotation(yaw);
                        galath.setAnchored(true);
                        galath.setInteractionPlayerUUID(player.getPersistentID());
                        galath.setCurrentAction(Action.MORNING_BLOWJOB_SLOW);
                        PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerMovement(false), (EntityPlayerMP) player);
                        ThreadNames.createDaemonThread(500, () -> {
                            player.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                            PackageHandler.INSTANCE.sendTo((IMessage) new SetPlayerCam(-10.0f, yaw + 180.0f + 5.0f, 0), (EntityPlayerMP) player);
                        });
                    }
                }
            }
        }
    }
}

