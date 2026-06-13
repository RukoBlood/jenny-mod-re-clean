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
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.*;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.gui.EscapeMinigameUI;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
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

public class GalathEntity
extends GirlEntity
implements IEntityMultiPart,
        b7_class68 {
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
    final static public Vector3f aa = new Vector3f(0.83137256f, 0.6862745f, 0.21568628f);
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
    final static public DataParameter<Integer> aP = EntityDataManager.createKey(GalathEntity.class, DataSerializers.VARINT).getSerializer().createKey(112);
    final static public DataParameter<Boolean> bN = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(113);
    final static public DataParameter<Boolean> b7 = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(114);
    final static public DataParameter<Boolean> ay = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(115);
    final static public DataParameter<Integer> bH = EntityDataManager.createKey(GalathEntity.class, DataSerializers.VARINT).getSerializer().createKey(116);
    final static public DataParameter<String> b8 = EntityDataManager.createKey(GalathEntity.class, DataSerializers.STRING).getSerializer().createKey(117);
    final static public DataParameter<Boolean> bP = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(118);
    final static public DataParameter<Float> bO = EntityDataManager.createKey(GalathEntity.class, DataSerializers.FLOAT).getSerializer().createKey(119);
    final static public DataParameter<Boolean> L = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(120);
    final static public DataParameter<String> a4 = EntityDataManager.createKey(GalathEntity.class, DataSerializers.STRING).getSerializer().createKey(121);
    final static public DataParameter<Boolean> bT = EntityDataManager.createKey(GalathEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(122);
    final static public double b0 = 0.2;
    final static public float bS = 5.0f;
    final static public int a1 = 60;
    BossInfoServer aO = new BossInfoServer(new TextComponentString(this.getGirlName()), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
    bb_class74 b2 = new bb_class74(this, "energyBallHitBox", 0.75f, 0.75f);
    bb_class74 V = new bb_class74(this, "energyBallHitBox", 0.75f, 0.75f);
    public h8_class399 bZ = null;
    public Vec3d O = null;
    public Vec3d bL = null;
    public int aF = 0;
    public Vec3d bd = null;
    public List<EntityWitherSkeleton> bI = new ArrayList<EntityWitherSkeleton>();
    public float aE = 0.0f;
    public long af = -1L;
    public long aH = -1L;
    public float bw = 0.0f;
    public float bm = 0.0f;
    boolean bU = false;
    public Vec3d aG = null;
    boolean bA = false;
    Vec3d bD;
    Vec3d W;
    Vec3d Z;
    float al = 0.0f;
    boolean U = false;
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
    boolean S = false;
    boolean P = false;
    int b1 = 0;
    boolean aT = false;
    public boolean bx = false;
    public boolean a5 = false;
    public boolean aD = false;
    public boolean bt = false;
    public boolean ap = false;
    public boolean bu = false;
    public boolean aL = true;
    public boolean bb = false;
    boolean a6 = false;

    public GalathEntity(World world) {
        super(world);
    }

    public GalathEntity(World world, @Nonnull EntityPlayer entityPlayer, Vec3d vec3d, boolean bl) {
        this(world);
        UUID uUID = entityPlayer.getPersistentID();
        this.entityDataManager.set(v, uUID.toString());
        this.aO.setVisible(false);
        this.bG = new BlockPos(this.getPositionVector());
        String string = gy_class392.a(uUID, PlayerGirlEntity.GALATH);
        if (string != null) {
            super.g(string);
        }
        if (bl) {
            return;
        }
        if (this.getRNG().nextFloat() > 0.1f) {
            this.setCurrentAction(Action.GALATH_SUMMON);
            return;
        }
        this.setCurrentAction(Action.MASTERBATE);
        this.void_b(180.0f - (float)gc_class360.b(Math.atan2(vec3d.x - entityPlayer.posX, vec3d.z - entityPlayer.posZ)));
        be_class78.a(8000, () -> {
            EntityPlayer p1 = this.net_minecraft_entity_player_EntityPlayer_z();
            if (p1 == null) {
                return;
            }
            if (p1.isDead) {
                return;
            }
            this.c(p1.getPositionVector());
            this.void_b(p1.rotationYaw + 180.0f);
            this.setCurrentAction(Action.RAPE_INTRO);
            this.void_e(p1.getPersistentID());
            this.void_a(true);
        });
    }

    public GalathEntity(World world, @Nonnull EntityPlayer entityPlayer, Vec3d vec3d) {
        this(world, entityPlayer, vec3d, false);
    }

    @Override
    public void f(String string) {
        super.f(string);
        bj_class84.a(this);
    }

    @Override
    public String getGirlName() {
        return "Galath";
    }

    @Override
    public float float_i() {
        return this.aF() == null ? 0.5f : 1.35f;
    }

    @Override
    public float getEyeHeight() {
        return 1.9f;
    }

    // TODO
    public boolean maybeMountedByMangFn() {
        return this.boolean_J();
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    protected void handleJumpWater() {
        if (this.maybeMountedByMangFn()) {
            super.handleJumpWater();
        }
    }

    @Override
    protected float getWaterSlowDown() {
        if (this.maybeMountedByMangFn()) {
            return super.getWaterSlowDown();
        }
        return 0.0f;
    }

    @Override
    public boolean isInWater() {
        if (this.maybeMountedByMangFn()) {
            return super.isInWater();
        }
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        if (this.maybeMountedByMangFn()) {
            return super.handleWaterMovement();
        }
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(bq, -1);
        this.entityDataManager.register(aP, 0);
        this.entityDataManager.register(bN, true);
        this.entityDataManager.register(b7, true);
        this.entityDataManager.register(ay, false);
        this.entityDataManager.register(b8, "null");
        this.entityDataManager.register(bH, -1);
        this.entityDataManager.register(bP, false);
        this.entityDataManager.register(bO, Float.valueOf(0.0f));
        this.entityDataManager.register(L, false);
        this.entityDataManager.register(a4, "");
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
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAITempt((EntityCreature)this, 0.4, false, new HashSet<Item>(I)));
        this.tasks.addTask(3, new hz_class409(this));
        this.tasks.addTask(5, this.o);
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP entityPlayerMP) {
        super.addTrackingPlayer(entityPlayerMP);
        this.aO.addPlayer(entityPlayerMP);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP entityPlayerMP) {
        super.removeTrackingPlayer(entityPlayerMP);
        this.aO.removePlayer(entityPlayerMP);
    }

    @Override
    public Vec3d net_minecraft_util_math_Vec3d_o() {
        if (this.world.isRemote && this.aG != null) {
            return this.aG;
        }
        return super.net_minecraft_util_math_Vec3d_o();
    }

    @Nullable
    public UUID aF() {
        String string = this.entityDataManager.get(a4);
        if ("".equals(string)) {
            return null;
        }
        try {
            return UUID.fromString(string);
        } catch (Exception exception) {
            return null;
        }
    }

    @Nullable
    public ManglelieEntity com_trolmastercard_sexmod_f8_class293_a(boolean bl) {
        GirlEntity em_class2582;
        UUID uUID = this.aF();
        if (uUID == null) {
            return null;
        }
        GirlEntity em_class2583 = em_class2582 = bl ? GalathEntity.com_trolmastercard_sexmod_em_class258_a(uUID) : GalathEntity.getGirlEntity(uUID);
        if (em_class2582 instanceof ManglelieEntity) {
            return (ManglelieEntity)em_class2582;
        }
        return null;
    }

    @Nullable
    public static ManglelieEntity a(GirlEntity em_class2582, boolean bl) {
        if (!(em_class2582 instanceof GalathEntity)) {
            return null;
        }
        return ((GalathEntity)em_class2582).com_trolmastercard_sexmod_f8_class293_a(bl);
    }

    public void void_a(@Nullable UUID uUID) {
        this.entityDataManager.set(a4, uUID == null ? "" : uUID.toString());
    }

    public void aC() {
        this.bA = true;
        ManglelieEntity f8_class2932 = this.com_trolmastercard_sexmod_f8_class293_a(true);
        if (f8_class2932 != null) {
            f8_class2932.void_q();
        }
    }

    public void void_w() {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 != Action.RAPE_ON_GOING) {
            return;
        }
        this.bZ = h8_class399.CHANGE_POSITION;
        this.bZ.b(this);
        this.void_a(false);
        this.setCurrentAction(Action.FLY);
        EntityPlayer entityPlayer = this.getPlayerEntity();
        this.void_e((UUID)null);
        if (entityPlayer != null) {
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
        }
        GirlEntity.a((GirlEntity)this, SoundsHandler.GIRLS_GALATH_DIALOG[0]);
    }

    public Vec3d net_minecraft_util_math_Vec3d_B() {
        String[] stringArray = this.entityDataManager.get(b8).split("\\|");
        return new Vec3d(Double.parseDouble(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]));
    }

    public void e(@Nullable Vec3d vec3d) {
        this.entityDataManager.set(b8, vec3d.x + "|" + vec3d.y + "|" + vec3d.z);
    }

    public int az() {
        return this.entityDataManager.get(bH);
    }

    public void a(int n) {
        this.entityDataManager.set(bH, n);
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public boolean boolean_b() {
        switch (this.currentAction()) {
            default: {
                return false;
            }
            case HUG_MANG: 
            case MORNING_BLOWJOB_SLOW: 
            case MORNING_BLOWJOB_FAST: 
            case MORNING_BLOWJOB_CUM: 
        }
        return true;
    }

    void void_aa() {
        this.Z = new Vec3d(this.motionX, this.motionY, this.motionZ);
        this.bD = this.getPositionVector();
        this.W = this.getPositionVector().add(this.Z);
        this.Z = this.Z.scale(0.9);
    }

    @Override
    public void onUpdate() {
        boolean bl = this.maybeMountedByMangFn();
        if (bl) {
            this.void_E();
        } else {
            this.void_c();
        }
        this.void_aa();
        super.onUpdate();
        if (bl) {
            this.au();
        } else {
            this.void_R();
        }
        if (this.world.isRemote) {
            this.void_X();
        }
    }

    @Override
    public boolean boolean_B() {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    void void_X() {
        if (this.currentAction() != Action.GIVE_COIN) {
            return;
        }
        int n = Action.GIVE_COIN.ticksPlaying[1];
        if (n == 95) {
            GalathCoin.a(Minecraft.getMinecraft().player, this);
        }
        if (n <= 25 || n >= 38) {
            return;
        }
        Vec3d vec3d = this.getPositionVector();
        Vec3d vec3d2 = this.b("weapon").add(vec3d);
        Vec3d vec3d3 = this.b("offhand").add(vec3d);
        ParticleGalathTrail.globalParticleScale = 0.5f;
        for (float f = 0.0f; f < 1.0f; f += 0.2f) {
            Vec3d vec3d4 = Reference.LerpVec3d(vec3d2, vec3d3, (double)f);
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleGalathTrail(this.world, vec3d4.x, vec3d4.y, vec3d4.z));
        }
    }

    void void_E() {
        this.setNoGravity(this.player() != null);
    }

    void au() {
        if (!this.isInWater() && !this.hasNoGravity() && this.motionY < 0.0 && this.currentAction() != Action.MASTERBATE) {
            this.motionY *= (double)0.4f;
        }
        this.aB();
        this.aj();
        this.aq();
        this.aw();
        this.void_C();
        this.Y_();
        this.void_o();
        if (this.net_minecraft_entity_EntityLivingBase_M() == null) {
            this.ap = false;
        }
    }

    void void_o() {
        if (this.world.isRemote) {
            return;
        }
        if (this.currentAction() != Action.RAPE_CUM) {
            return;
        }
        if (Action.RAPE_CUM.ticksPlaying[0] < 28) {
            return;
        }
        this.void_a(false);
        this.setCurrentAction(Action.NULL);
        EntityPlayer entityPlayer = this.getPlayerEntity();
        this.void_e((UUID)null);
        if (entityPlayer == null) {
            return;
        }
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, Math.ceil(entityPlayer.posY) + 1.0, entityPlayer.posZ);
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
    }

    void Y_() {
        if (this.world.isRemote) {
            return;
        }
        if (this.currentAction() != Action.CORRUPT_CUM) {
            return;
        }
        if (Action.CORRUPT_CUM.ticksPlaying[0] < 30) {
            return;
        }
        this.void_a(false);
        this.setCurrentAction(Action.NULL);
        EntityPlayer entityPlayer = this.getPlayerEntity();
        this.void_e((UUID)null);
        if (entityPlayer == null) {
            return;
        }
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, Math.ceil(entityPlayer.posY) + 1.0, entityPlayer.posZ);
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
    }

    static boolean a(BlockPos blockPos, World world) {
        for (BlockPos object : fq_class325.c) {
            if (!(Math.sqrt(blockPos.distanceSq(object)) < 1000.0)) continue;
            return false;
        }
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                if (em_class2582.world.isRemote || !(em_class2582 instanceof GalathEntity) || em_class2582.isDead || !(em_class2582.getDistanceSq(blockPos) < 1000000.0)) continue;
                return false;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        int n = blockPos.getY();
        while ((float)n < 15.0f + (float)blockPos.getY()) {
            if (world.getBlockState(new BlockPos(blockPos.getX(), n, blockPos.getZ())).getBlock() != Blocks.AIR) {
                return false;
            }
            ++n;
        }
        n = blockPos.getY();
        while ((float)n > (float)blockPos.getY() - 5.0f) {
            if (world.getBlockState(new BlockPos(blockPos.getX(), n, blockPos.getZ())).getBlock() instanceof BlockLiquid) {
                return false;
            }
            --n;
        }
        return true;
    }

    void aw() {
        int n;
        EntityPlayer entityPlayer = this.player();
        Action fp_class3242 = this.currentAction();
        if (entityPlayer == null) {
            return;
        }
        if (fp_class3242 != Action.BOOST) {
            return;
        }
        int n2 = n = ClientServerCheck.getInstance() ? 0 : 1;
        if (fp_class3242.ticksPlaying[n] < 13) {
            return;
        }
        if (fp_class3242.ticksPlaying[n] == 13) {
            this.al = 6.0f;
        }
        Vec3d vec3d = entityPlayer.getLook(0.0f).normalize();
        this.motionX = vec3d.x * (double)this.al;
        this.motionY = vec3d.y * (double)this.al;
        this.motionZ = vec3d.z * (double)this.al;
        this.al *= 0.94f;
    }

    void void_c() {
        this.void_n();
        this.void_j();
        this.void_ah();
    }

    void void_R() {
        GalathEntity.a(this, 0.0f);
        this.void_h();
        this.aj();
        this.void_af();
        this.L_();
        this.void_F();
        this.void_C();
        this.u_();
        if (this.world.isRemote) {
            this.void_H();
        }
    }

    void u_() {
        if (this.world.isRemote) {
            return;
        }
        if (this.currentAction() != Action.CORRUPT_CUM) {
            return;
        }
        if (Action.CORRUPT_CUM.ticksPlaying[0] >= 30) {
            this.setCurrentAction(Action.GIVE_COIN);
        }
    }

    void void_C() {
        if (this.entityDataManager.get(L).booleanValue()) {
            this.bb = true;
            return;
        }
        switch (this.currentAction()) {
            case RAPE_INTRO: 
            case RAPE_ON_GOING: 
            case RAPE_CUM: 
            case RAPE_CHARGE: 
            case RAPE_CUM_IDLE: 
            case CORRUPT_SLOW: 
            case CORRUPT_FAST: 
            case CORRUPT_CUM: 
            case MASTERBATE: {
                this.bb = true;
            }
            case RAPE_PREPARE: {
                return;
            }
        }
        this.bb = false;
    }

    @Override
    public boolean boolean_m() {
        if (this.currentAction() != Action.CORRUPT_INTRO) {
            return false;
        }
        return this.U;
    }

    void void_F() {
        if (!this.world.isRemote) {
            return;
        }
        if (this.currentAction() == Action.KNOCK_OUT_STAND_UP) {
            return;
        }
        this.aL = true;
    }

    void void_j() {
        this.aO.setPercent(this.getHealth() / this.getMaxHealth());
    }

    void void_n() {
        if (this.entityDataManager.get(bP).booleanValue()) {
            return;
        }
        this.setNoGravity(this.net_minecraft_entity_EntityLivingBase_M() != null);
    }

    void L_() {
        if (this.currentAction() != Action.ATTACK_SWORD) {
            this.ap = false;
            this.bu = false;
        }
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect) {
    }

    void void_af() {
        if (!this.world.isRemote) {
            return;
        }
        if (!this.bu) {
            return;
        }
        Vec3d vec3d = this.getPositionVector();
        Vec3d vec3d2 = this.b("weaponStart").add(vec3d);
        Vec3d vec3d3 = this.b("weaponEnd").add(vec3d);
        float f = 0.1f;
        Random random = this.getRNG();
        for (float f2 = 0.0f; f2 < 1.0f; f2 += f) {
            Vec3d vec3d4 = Reference.LerpVec3d(vec3d2, vec3d3, (double)f2);
            for (int i = 0; i < 3; ++i) {
                this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d4.x + random.nextDouble() * 0.25 * (double)(random.nextBoolean() ? 1 : -1), vec3d4.y + random.nextDouble() * 0.25 * (double)(random.nextBoolean() ? 1 : -1), vec3d4.z + random.nextDouble() * 0.25 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        for (int i = 0; i < 3; ++i) {
            this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d3.x + random.nextDouble() * 0.25 * (double)(random.nextBoolean() ? 1 : -1) * (double)(random.nextBoolean() ? 1 : -1), vec3d3.y + random.nextDouble() * 0.25 * (double)(random.nextBoolean() ? 1 : -1), vec3d3.z + random.nextDouble() * 0.25 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void ag() {
        if (this.currentAction() == Action.GALATH_DE_SUMMON) {
            return;
        }
        this.actionController.tickOffset = 0.0;
    }

    @Override
    public String java_lang_String_ab() {
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_z();
        if (entityPlayer == null) {
            return super.java_lang_String_ab();
        }
        return String.format("%s %s[%s]", new Object[]{super.java_lang_String_ab(), TextFormatting.DARK_PURPLE, entityPlayer.getName()});
    }

    void void_h() {
        this.b2.a = false;
        this.V.a = false;
        if ((float)this.ad < 9.0f) {
            return;
        }
        if ((float)this.ad > 30.0f) {
            return;
        }
        this.b2.a = true;
        this.V.a = true;
        boolean bl = this.entityDataManager.get(ay);
        Vec3d vec3d = this.getPositionVector().add(ck_class135.a(bl ? ck_class135.c(bz) : bz, 180.0f + this.renderYawOffset));
        Vec3d vec3d2 = this.getPositionVector().add(ck_class135.a(bl ? ck_class135.c(bC) : bC, 180.0f + this.renderYawOffset));
        this.b2.setLocationAndAngles(vec3d.x, vec3d.y, vec3d.z, this.renderYawOffset, 0.0f);
        this.V.setLocationAndAngles(vec3d2.x, vec3d2.y, vec3d2.z, this.renderYawOffset, 0.0f);
        this.b2.onUpdate();
        this.V.onUpdate();
    }

    void void_ah() {
        if (this.currentAction() != Action.SUMMON_SKELETON) {
            this.ad = 0;
            return;
        }
        if (this.ad++ > 45) {
            this.ad = 0;
        }
    }

    @Override
    public f2_class286 com_trolmastercard_sexmod_f2_class286_d() {
        return new f2_class286(this.a9, this.bg, this.b4, this.a_);
    }

    void aj() {
        this.b4 = this.a9;
        this.a_ = this.bg;
        Vec3d vec3d = this.W.subtract(this.bD);
        Vec3d vec3d2 = ck_class135.a(vec3d, this.renderYawOffset + 180.0f);
        this.a9 = gc_class360.c(be_class78.b(vec3d2.z * 40.0, -50.0, 50.0));
        this.bg = gc_class360.c(be_class78.b(vec3d2.x * 40.0, -50.0, 50.0));
    }

    public void f(Vec3d vec3d) {
        if (this.entityDataManager.get(bP).booleanValue()) {
            return;
        }
        this.entityDataManager.set(bP, true);
        if (this.bZ != null) {
            this.bZ.e(this);
        }
        this.bZ = null;
        Vec3d vec3d2 = this.getPositionVector();
        Random random = this.getRNG();
        Vec3d vec3d3 = vec3d == null ? new Vec3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize() : vec3d2.subtract(vec3d).normalize();
        this.setVelocity(vec3d3.x * 1.0, 1.0, vec3d3.z * 1.0);
        this.setCurrentAction(Action.KNOCK_OUT_FLY);
        this.setNoGravity(false);
        this.noClip = false;
        this.getNavigator().clearPath();
        GalathEntity.a((GirlEntity)this, SoundsHandler.GIRLS_GALATH_AAA, true);
    }

    void void_a(Entity entity) {
        GirlEntity.a((GirlEntity)this, (Object)((Object)TextFormatting.YELLOW) + "Galath is paralyzed! Now it's time to corrupt her");
        GirlEntity.a((GirlEntity)this, (Object)((Object)TextFormatting.GRAY) + "(Walk to her and right click her)");
        PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(this.getPositionVector(), true), (Entity)this);
        this.f((Vec3d)null);
        this.entityDataManager.set(L, true);
    }

    @Override
    public void updateAITasks() {
        if (this.P) {
            GalathMangTracker.a(this);
            return;
        }
        this.void_P();
        super.updateAITasks();
        this.o.a = this.boolean_x();
        if (this.maybeMountedByMangFn()) {
            // TODO something with creating a mang
            this.void_ae();
        } else {
            this.an();
        }
    }

    void void_P() {
        if (this.bK) {
            return;
        }
        this.f(bj_class84.c(this));
        this.bK = true;
    }

    boolean boolean_x() {
        if (this.currentAction() != Action.NULL) {
            return false;
        }
        return !(Math.abs(this.motionX) + Math.abs(this.motionZ) > 0.01);
    }

    void aq() {
        if (!this.world.isRemote) {
            return;
        }
        if (this.player() != null) {
            return;
        }
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_z();
        if (entityPlayer == null) {
            return;
        }
        this.void_d(entityPlayer);
    }

    void void_d(EntityPlayer entityPlayer) {
        PlayerGirl ei_class2512 = PlayerGirl.d_(entityPlayer.getPersistentID());
        Vec3d vec3d = new Vec3d(entityPlayer.posX, entityPlayer.posY + (double)(ei_class2512 == null ? entityPlayer.eyeHeight : ei_class2512.getEyeHeight()), entityPlayer.posZ);
        Vec3d vec3d2 = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        double d = vec3d2.distanceTo(vec3d);
        double d2 = vec3d.y - vec3d2.y;
        this.rotationPitch = (float)(-(Math.sin(d2 / d) * 57.29577951308232));
    }

    // TODO something with creating a mang
    void void_ae() {
        this.aO.setVisible(false);
        if (!GalathMangTracker.c(this)) {
            GalathMangTracker.a(this);
            return;
        }
        if (this.player() != null) {
            this.void_y();
            return;
        }
        this.void_m();
        if (this.aF() == null) {
            this.aJ();
        } else {
            this.am();
        }
    }

    // TODO something with creating a mang
    void void_m() {
        if (!GalathMangTracker.doesPlayerOwnGalathMangPair(GalathMangTracker.b(this))) {
            return;
        }
        boolean bl = this.boolean_v();
        if (bl) {
            Main.LOGGER.warn("mommy thinks she got no daughter but she actually does have one. Failsafe called. Hopefully its fixed");
        }
    }

    void am() {
        if (this.boolean_ai()) {
            return;
        }
        this.entityDataManager.set(bT, false);
        this.ao();
    }

    boolean boolean_ai() {
        UUID uUID = GalathMangTracker.b(this);
        if (uUID == null) {
            return false;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return false;
        }
        BlockPos blockPos = entityPlayer.getPosition();
        if (!this.boolean_a(blockPos)) {
            return false;
        }
        if (this.bZ != null) {
            this.bZ.e(this);
            this.bZ = null;
        }
        float f = this.getDistance(entityPlayer);
        PathNavigate pathNavigate = this.getNavigator();
        if (f < 4.0f) {
            pathNavigate.clearPath();
            return false;
        }
        if (f > 16.0f) {
            pathNavigate.clearPath();
            this.void_b(entityPlayer);
            return true;
        }
        if (fl_class318.a(this.aq).distanceSq(blockPos) > 16.0) {
            if (!this.onGround) {
                return true;
            }
            this.aq = this.a(entityPlayer, blockPos);
            if (this.aq == null) {
                this.void_b(entityPlayer);
            } else {
                pathNavigate.setPath(this.aq, 1.0);
            }
        }
        if (this.aq == null || this.aq.isFinished()) {
            return false;
        }
        boolean bl = entityPlayer.isSprinting() || this.getDistance(entityPlayer) > 7.0f;
        double d = bl ? (double)0.55f : 0.5;
        double d2 = Math.floor(f / 5.0f) * 0.2;
        d += d2;
        if (this.isInWater()) {
            d *= 60.0;
        }
        pathNavigate.setSpeed(d);
        this.entityDataManager.set(bT, bl);
        this.setCurrentAction((Action)null);
        return true;
    }

    boolean boolean_a(BlockPos blockPos) {
        if (this.bZ == null) {
            return true;
        }
        BlockPos blockPos2 = this.getPosition();
        int n = Math.abs(blockPos.getX() - blockPos2.getX()) + Math.abs(blockPos.getX() - blockPos2.getX());
        return n > 16;
    }

    protected void void_b(EntityPlayer entityPlayer) {
        BlockPos blockPos;
        int n = 0;
        do {
            blockPos = entityPlayer.getPosition().add(Reference.RANDOM.nextInt(4), 0, Reference.RANDOM.nextInt(4));
        } while (++n < 20 && !this.attemptTeleport(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        if (n >= 20) {
            this.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        }
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    @Nullable
    Path a(EntityPlayer entityPlayer, BlockPos blockPos) {
        PathNavigate pathNavigate = this.getNavigator();
        return pathNavigate.getPathToEntityLiving(entityPlayer);
    }

    void aJ() {
        this.at();
        this.ay();
    }

    void void_y() {
        this.bG = null;
        this.aC = 0;
        if (this.bZ != null) {
            this.bZ.e(this);
            this.bZ = null;
        }
    }

    void at() {
        if (!this.onGround) {
            return;
        }
        if (this.aF() != null) {
            return;
        }
        if (this.currentAction() == Action.HUG_MANG) {
            return;
        }
        if (GalathMangTracker.doesPlayerOwnGalathMangPair(GalathMangTracker.f(this.girlID()))) {
            return;
        }
        BlockPos blockPos = this.getPosition();
        BlockPos blockPos2 = blockPos.add(-15.0, -15.0, -15.0);
        BlockPos blockPos3 = blockPos.add(15.0, 15.0, 15.0);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos2, blockPos3);
        List<ManglelieEntity> list = this.world.getEntitiesWithinAABB(ManglelieEntity.class, axisAlignedBB);
        Entity entity = null;
        for (ManglelieEntity object2 : list) {
            if (object2.isDead || object2.com_trolmastercard_sexmod_f__class297_a(true) != null) continue;
            entity = object2;
            break;
        }
        if (entity == null) {
            if (this.currentAction() == Action.RUN) {
                this.setCurrentAction((Action)null);
                this.getNavigator().clearPath();
            }
            return;
        }
        this.f = this.getNavigator();
        if (entity.getDistance(this) <= 3.65f) {
            this.f.clearPath();
            this.setCurrentAction(Action.HUG_MANG);
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.c(this.getPositionVector());
            this.void_a(true);
            this.void_a(((GirlEntity)entity).girlID());
            ((ManglelieEntity)entity).void_a(this.girlID());
            ((ManglelieEntity)entity).setCurrentAction(Action.RIDE_MOMMY_HEAD);
            GalathMangTracker.e(this.girlID());
            return;
        }
        Vec3d vec3d = this.getPositionVector();
        Vec3d vec3d2 = entity.getPositionVector();
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        float f = (float)gc_class360.b(Math.atan2(vec3d3.z, vec3d3.x)) - 90.0f;
        this.void_b(f);
        this.f.clearPath();
        this.f.tryMoveToEntityLiving(entity, 0.65f);
        this.setCurrentAction(Action.RUN);
    }

    void ay() {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.RUN) {
            return;
        }
        if (fp_class3242 == Action.HUG_MANG) {
            return;
        }
        if (this.boolean_Q() || fp_class3242 == Action.MASTERBATE) {
            this.getNavigator().clearPath();
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
        if (this.boolean_J() && entityPlayer != null && entityPlayer.getDistance(this) < 2.0f && entityPlayer.getPersistentID().equals(this.java_util_UUID_O())) {
            this.getNavigator().clearPath();
            return;
        }
        if (this.bG == null || this.getDistance(this.bG.getX(), this.bG.getY(), this.bG.getZ()) > this.double_i() || this.aC > 175) {
            int n = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(10);
            int n2 = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(10);
            int n3 = this.world.provider.getDimensionType() == DimensionType.NETHER ? (int)Math.ceil(this.posY) : cj_class134.a(this.world, this.getPosition().getX() + n, this.getPosition().getZ() + n2);
            this.bG = new BlockPos(this.getPosition().getX() + n, n3, this.getPosition().getZ() + n2);
            this.aC = 0;
        }
        if (Math.sqrt(this.bG.distanceSq(this.getPosition())) > 2.0) {
            this.getNavigator().tryMoveToXYZ(this.bG.getX(), this.bG.getY(), this.bG.getZ(), 0.35f);
            this.void_k();
        } else {
            ++this.aC;
        }
    }

    BlockPos av() {
        UUID uUID = GalathMangTracker.b(this);
        if (uUID == null) {
            return BlockPos.ORIGIN;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return BlockPos.ORIGIN;
        }
        return entityPlayer.getPosition();
    }

    double double_i() {
        return Math.sqrt(1800.0);
    }

    @Nullable
    public EntityPlayer player() {
        List<Entity> list = this.getPassengers();
        if (list.isEmpty()) {
            return null;
        }
        if (list.get(0) instanceof EntityPlayer) {
            return (EntityPlayer)list.get(0);
        }
        return null;
    }

    @Nullable
    public UUID ax() {
        EntityPlayer entityPlayer = this.player();
        if (entityPlayer == null) {
            return null;
        }
        return entityPlayer.getPersistentID();
    }

    @Override
    public void g(String string) {
        super.g(string);
        UUID uUID = this.java_util_UUID_O();
        if (uUID == null) {
            return;
        }
        gy_class392.a(uUID, PlayerGirlEntity.GALATH, string);
    }

    public void d(Vec3d vec3d) {
        this.motionX += vec3d.x;
        this.motionZ += vec3d.z;
        this.motionY = vec3d.y / 2.0;
    }

    public void void_t() {
        this.void_e((UUID)null);
        this.setCurrentAction((Action)null);
    }

    void aB() {
        EntityPlayer entityPlayer = this.player();
        if (entityPlayer == null) {
            return;
        }
        this.prevRenderYawOffset = entityPlayer.prevRotationYawHead;
        this.renderYawOffset = entityPlayer.rotationYawHead;
    }

    void an() {
        this.aO.setVisible(true);
        this.ao();
        this.as();
    }

    void ao() {
        if (Action.a((GirlEntity)this, Action.MASTERBATE, Action.HUG_MANG)) {
            return;
        }
        if (this.getID() != null) {
            return;
        }
        this.void_Q();
        this.void_I();
        this.D_(); // TODO
        this.void_q();
        this.void_J();
        this.void_T();
        this.void_S();
        this.void_b();
        this.ad_();
        this.aG();
        this.aA();
        this.aD();
        this.void_O();
        this.Z();
    }

    void void_Q() {
        if (!this.maybeMountedByMangFn()) {
            return;
        }
        if (this.net_minecraft_entity_EntityLivingBase_M() != null) {
            return;
        }
        int n = this.entityDataManager.get(bq);
        if (n == -1) {
            return;
        }
        if (this.bZ != null) {
            this.bZ.e(this);
        }
        this.bZ = null;
        this.setCurrentAction(Action.NULL);
    }

    void as() {
        if (this.net_minecraft_entity_EntityLivingBase_M() != null) {
            this.bG = null;
            this.aC = 0;
            return;
        }
        if (this.entityDataManager.get(L).booleanValue()) {
            return;
        }
        if (this.entityDataManager.get(bP).booleanValue()) {
            return;
        }
        this.ay();
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.GALATH_DE_SUMMON) {
            return;
        }
        if (fp_class3243 == Action.CORRUPT_CUM && (fp_class3242 == Action.CORRUPT_FAST || fp_class3242 == Action.CORRUPT_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.RAPE_CUM && fp_class3242 == Action.RAPE_ON_GOING) {
            return;
        }
        if (fp_class3243 == Action.MORNING_BLOWJOB_CUM && (fp_class3242 == Action.MORNING_BLOWJOB_SLOW || fp_class3242 == Action.MORNING_BLOWJOB_FAST)) {
            return;
        }
        if (!this.world.isRemote && Action.a(fp_class3243, Action.CORRUPT_CUM, Action.RAPE_CUM, Action.MORNING_BLOWJOB_CUM)) {
            GalathMangTracker.setLastCumTime(this.getID(), this.world.getTotalWorldTime());
        }
        if (fp_class3242 == Action.CORRUPT_SLOW) {
            this.aT = false;
            if (fp_class3243 == Action.CORRUPT_INTRO) {
                this.d(false);
            }
            if (this.maybeMountedByMangFn() && fp_class3243 == Action.NULL) {
                this.d(true);
            }
        }
        if (fp_class3243 == Action.GIVE_COIN && fp_class3242 == Action.NULL && !this.world.isRemote) {
            this.ap();
        }
        if (fp_class3243 == Action.HUG_MANG && fp_class3242 == Action.NULL) {
            this.al();
        }
        if (fp_class3243 == Action.MORNING_BLOWJOB_CUM && fp_class3242 == Action.NULL) {
            this.aE();
        }
        super.setCurrentAction(fp_class3242);
    }

    void aE() {
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer != null) {
            ResetGirl.a_inner422.a((EntityPlayerMP)entityPlayer);
        }
        ResetGirl.a_inner422.a(this);
    }

    void al() {
        this.void_a(false);
        ManglelieEntity f8_class2932 = this.com_trolmastercard_sexmod_f8_class293_a(true);
        if (f8_class2932 == null) {
            return;
        }
        f8_class2932.c(true);
    }

    void ap() {
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        entityPlayer.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(GalathCoin.GALATH_COIN));
        if (!itemStack.isEmpty()) {
            entityPlayer.inventory.addItemStackToInventory(itemStack);
        }
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
        this.void_e((UUID)null);
        this.a((EntityLivingBase)null);
        entityPlayer.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GRAY) + "Defeating a succubus makes her accept the victor as her master, granting him a coin to which her soul is bound. Using the coin summons her, offering services on demand. If her master uses the coin on her or goes too far, she returns to the coin"));
        GalathMangTracker.a(this);
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, Math.ceil(entityPlayer.posY) + 1.0, entityPlayer.posZ);
    }

    @SideOnly(value=Side.CLIENT)
    void void_H() {
        float f;
        Action fp_class3242 = this.currentAction();
        if (this.ab == Action.CORRUPT_INTRO || fp_class3242 != Action.CORRUPT_INTRO) {
            this.ab = fp_class3242;
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (!entityPlayerSP.getPersistentID().equals(this.getID())) {
            this.ab = fp_class3242;
            return;
        }
        entityPlayerSP.rotationYaw = f = this.maybeMountedByMangFn() ? 0.0f : this.java_lang_Float_I().floatValue() + 180.0f;
        entityPlayerSP.prevRotationYaw = f;
        entityPlayerSP.rotationPitch = 80.0f;
        entityPlayerSP.prevRotationPitch = 80.0f;
        this.ab = fp_class3242;
    }

    void d(boolean bl) {
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = bl ? new Vec3d(-0.5, 0.5f - entityPlayer.getEyeHeight(), 0.4f).add(this.net_minecraft_util_math_Vec3d_o()) : ck_class135.a(new Vec3d(0.5, 0.5f - entityPlayer.getEyeHeight(), 0.4f), this.java_lang_Float_I().floatValue()).add(this.net_minecraft_util_math_Vec3d_o());
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public float float_v() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return 1.0f;
        }
        switch (this.currentAction()) {
            case CORRUPT_INTRO: {
                if (!this.U) break;
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
    protected boolean boolean_X() {
        return false;
    }

    public boolean boolean_v() {
        if (this.com_trolmastercard_sexmod_f8_class293_a(true) != null) {
            return false;
        }
        ManglelieEntity f8_class2932 = new ManglelieEntity(this.world);
        this.void_a(f8_class2932.girlID());
        f8_class2932.void_a(this.girlID());
        f8_class2932.c(true);
        f8_class2932.setCurrentAction(Action.RIDE_MOMMY_HEAD);
        f8_class2932.setPositionAndUpdate(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(f8_class2932);
        return true;
    }

    void Z() {
        if (this.maybeMountedByMangFn()) {
            return;
        }
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 != Action.RAPE_CUM) {
            this.at = 0;
            return;
        }
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer == null) {
            this.at = 0;
            return;
        }
        if (++this.at != 15) {
            return;
        }
        entityPlayer.attackEntityFrom(new a3_class10(this), 2.1474836E9f);
    }

    void void_O() {
        EntityLivingBase entityLivingBase = this.net_minecraft_entity_EntityLivingBase_M();
        if (entityLivingBase == null) {
            return;
        }
        for (EntityWitherSkeleton entityWitherSkeleton : this.bI) {
            if (entityWitherSkeleton.isDead || entityLivingBase.getDistance(entityWitherSkeleton) < 15.0f) continue;
            PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(entityWitherSkeleton.getPositionVector(), true), (Entity)this);
            entityWitherSkeleton.setDead();
            this.world.removeEntity(entityWitherSkeleton);
        }
    }

    void aD() {
        if (!this.entityDataManager.get(bP).booleanValue()) {
            return;
        }
        for (EntityWitherSkeleton entityWitherSkeleton : this.bI) {
            if (entityWitherSkeleton.isDead) continue;
            PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(entityWitherSkeleton.getPositionVector(), true), (Entity)this);
            entityWitherSkeleton.setDead();
            this.world.removeEntity(entityWitherSkeleton);
        }
        this.bI.clear();
    }

    public static void void_c(EntityPlayer entityPlayer) {
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(GalathMangTracker.b(entityPlayer));
        if (em_class2582 == null) {
            return;
        }
        if (em_class2582.equals(entityPlayer.getRidingEntity())) {
            em_class2582.void_e(entityPlayer.getPersistentID());
            em_class2582.setCurrentAction(Action.CONTROLLED_FLIGHT);
        }
    }

    void aA() {
        for (EntityWitherSkeleton entityWitherSkeleton : this.bI) {
            if (entityWitherSkeleton.isDead || entityWitherSkeleton.ticksExisted % 10 != 0) continue;
            Set<? extends EntityPlayer> set = ((WorldServer)this.world).getEntityTracker().getTrackingPlayers(entityWitherSkeleton);
            for (EntityPlayer entityPlayer : set) {
                ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.DRAGON_BREATH, false, (float)entityWitherSkeleton.posX, (float)entityWitherSkeleton.posY, (float)entityWitherSkeleton.posZ, 0.2f * (float)be_class78.a(), entityWitherSkeleton.getEyeHeight() / 2.0f, 0.2f * (float)be_class78.a(), 0.0f, 5, new int[0]));
            }
        }
    }

    void aG() {
        ArrayList<EntityWitherSkeleton> arrayList = new ArrayList<EntityWitherSkeleton>();
        for (EntityWitherSkeleton entityWitherSkeleton : this.bI) {
            if (!entityWitherSkeleton.isDead) continue;
            arrayList.add(entityWitherSkeleton);
        }
        for (EntityWitherSkeleton entityWitherSkeleton : arrayList) {
            this.bI.remove(entityWitherSkeleton);
        }
    }

    void ad_() {
        if (this.currentAction() != Action.KNOCK_OUT_STAND_UP) {
            return;
        }
        ++this.bY;
        if ((double)this.bY == 39.0) {
            this.setNoGravity(true);
            this.setVelocity(0.0, 0.6f, 0.0);
            Vec3d vec3d = this.getPositionVector();
            Vec3d vec3d2 = vec3d.subtract(2.0, 2.0, 2.0);
            Vec3d vec3d3 = vec3d.add(2.0, 2.0, 2.0);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d2.x, vec3d2.y, vec3d2.z, vec3d3.x, vec3d3.y, vec3d3.z);
            List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);
            for (EntityLivingBase entityLivingBase : list) {
                if (entityLivingBase instanceof GalathEntity) continue;
                Vec3d vec3d4 = entityLivingBase.getPositionVector();
                Vec3d vec3d5 = vec3d4.subtract(vec3d).normalize();
                entityLivingBase.motionX = vec3d5.x * 1.0;
                entityLivingBase.motionZ = vec3d5.z * 1.0;
                entityLivingBase.motionY = 1.0;
                entityLivingBase.attackEntityFrom(new GalathDamageSource(this), 0.5f);
                if (!(entityLivingBase instanceof EntityPlayerMP)) continue;
                EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityLivingBase;
                entityPlayerMP.connection.sendPacket(new SPacketEntityVelocity(entityPlayerMP));
            }
        }
        if ((double)this.bY < 58.0) {
            return;
        }
        this.b(Vec3d.ZERO);
        this.entityDataManager.set(bP, false);
        this.bY = 0;
    }

    void void_b() {
        if (this.currentAction() != Action.KNOCK_OUT_GROUND) {
            return;
        }
        if (this.entityDataManager.get(L).booleanValue()) {
            return;
        }
        this.b3++;
        if ((double)this.b3 < 50.0) {
            return;
        }
        this.setCurrentAction(Action.KNOCK_OUT_STAND_UP);
        this.bY = 0;
        this.b3 = 0;
    }

    void void_S() {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 != Action.KNOCK_OUT_GROUND && fp_class3242 != Action.KNOCK_OUT_STAND_UP) {
            return;
        }
        this.motionX = 0.0;
        this.motionZ = 0.0;
        if (this.entityDataManager.get(L).booleanValue()) {
            this.motionY = 0.0;
        }
    }

    void void_T() {
        if (this.currentAction() != Action.KNOCK_OUT_FLY) {
            return;
        }
        BlockPos blockPos = this.getPosition();
        if (this.world.getBlockState(blockPos).getBlock() instanceof BlockLiquid) {
            BlockPos blockPos2 = blockPos;
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
            this.c(new Vec3d(blockPos2));
            PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(new Vec3d(blockPos2), true), (Entity)this);
            for (EntityPlayer entityPlayer : ((WorldServer)this.world).getEntityTracker().getTrackingPlayers(this)) {
                ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.AMBIENT, this.posX, this.posY, this.posZ, 1.0f, 1.0f));
            }
            this.setCurrentAction(Action.KNOCK_OUT_GROUND);
            return;
        }
        if (!this.onGround) {
            return;
        }
        this.setCurrentAction(Action.KNOCK_OUT_GROUND);
    }

    void void_J() {
        if (this.bZ != h8_class399.CHANGE_POSITION) {
            return;
        }
        int n = this.ar();
        boolean bl = this.noClip = n == 0;
        if (!this.world.isAirBlock(this.getPosition())) {
            this.noClip = true;
        }
    }

    void void_q() {
        if (this.bZ == null) {
            return;
        }
        this.bZ.a(this);
    }

    void D_() {
        if (this.net_minecraft_entity_EntityLivingBase_M() == null) {
            this.aH();
            return;
        }
        if (this.bZ == null) {
            this.void_z();
            return;
        }
        if (this.bZ.c(this)) {
            this.void_z();
        }
    }

    void void_z() {
        h8_class399 h8_class3992;
        if (this.entityDataManager.get(bP).booleanValue()) {
            return;
        }
        h8_class399 h8_class3993 = this.bZ;
        if (this.getID() != null) {
            if (h8_class3993 != null) {
                h8_class3993.e(this);
            }
            this.bZ = null;
            return;
        }
        if (h8_class3993 != null && h8_class3993.applyAttackCoolDown) {
            h8_class3993.e(this);
            this.bZ = h8_class399.CHANGE_POSITION;
            this.bZ.b(this);
            return;
        }
        h8_class399[] h8_class399Array = h8_class399.values();
        while (!this.a(h8_class3992 = h8_class399Array[this.getRNG().nextInt(h8_class399Array.length)])) {
        }
        this.bZ = h8_class3992;
        if (h8_class3993 != null) {
            h8_class3993.e(this);
        }
        this.bZ.b(this);
    }

    boolean a(h8_class399 h8_class3992) {
        if (h8_class3992.onlyDoThisOnPlayers && !(this.net_minecraft_entity_EntityLivingBase_M() instanceof EntityPlayer)) {
            return false;
        }
        return h8_class3992.d(this);
    }

    void aH() {
        this.bZ = null;
    }

    void void_I() {
        EntityLivingBase entityLivingBase;
        if (this.boolean_f()) {
            return;
        }
        if (this.getID() != null) {
            return;
        }
        boolean bl = this.maybeMountedByMangFn();
        float f = bl ? 7.0f : 20.0f;
        Vec3d vec3d = new Vec3d(f, f, f);
        Vec3d vec3d2 = this.getPositionVector();
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        Vec3d vec3d4 = vec3d2.add(vec3d);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d3.x, vec3d3.y, vec3d3.z, vec3d4.x, vec3d4.y, vec3d4.z);
        EntityLivingBase entityLivingBase2 = entityLivingBase = bl ? this.a(axisAlignedBB) : this.b(axisAlignedBB);
        if (entityLivingBase == null) {
            this.aI();
            return;
        }
        this.a(entityLivingBase);
        GirlEntity.a((GirlEntity)this, SoundsHandler.GIRLS_GALATH_DIALOG[1], true);
        if (this.bZ != null) {
            this.bZ.e(this);
        }
        this.bZ = h8_class399.CHANGE_POSITION;
        this.bZ.b(this);
    }

    EntityPlayer b(AxisAlignedBB axisAlignedBB) {
        List<EntityPlayer> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB, entityPlayer -> !PlayerGirl.e(entityPlayer) && !entityPlayer.isCreative() && !entityPlayer.isSpectator());
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    EntityMob a(AxisAlignedBB axisAlignedBB) {
        List<EntityMob> list = this.world.getEntitiesWithinAABB(EntityMob.class, axisAlignedBB);
        if (list.isEmpty()) {
            return null;
        }
        ArrayList<EntityMob> arrayList = new ArrayList<EntityMob>();
        for (EntityMob object : list) {
            if (!d_class156.a(object)) continue;
            arrayList.add(object);
        }
        Vec3d vec3d = this.getPositionVector().add(0.0, this.getEyeHeight(), 0.0);
        for (EntityMob entityMob : arrayList) {
            if (!d_class156.a(this.world, vec3d, entityMob)) continue;
            return entityMob;
        }
        return null;
    }

    void aI() {
        if (this.net_minecraft_entity_EntityLivingBase_M() == null) {
            return;
        }
        this.a((EntityLivingBase)null);
        if (this.bZ != null) {
            this.bZ.e(this);
        }
        this.bZ = null;
        if (this.entityDataManager.get(bP).booleanValue()) {
            return;
        }
        this.setCurrentAction(Action.NULL);
    }

    boolean boolean_f() {
        float f;
        EntityLivingBase entityLivingBase = this.net_minecraft_entity_EntityLivingBase_M();
        if (entityLivingBase == null) {
            return false;
        }
        if (entityLivingBase.isDead) {
            return false;
        }
        if (entityLivingBase.dimension != this.dimension) {
            return false;
        }
        float f2 = this.getDistance(entityLivingBase);
        float f3 = f = this.maybeMountedByMangFn() ? 16.0f : 30.0f;
        if (f2 > f) {
            return false;
        }
        if (!(entityLivingBase instanceof EntityPlayer)) {
            return true;
        }
        EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
        if (GirlEntity.com_trolmastercard_sexmod_em_class258_c(entityPlayer.getPersistentID()) != null) {
            return false;
        }
        if (entityPlayer.isCreative()) {
            return false;
        }
        return !entityPlayer.isSpectator();
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public GirlEntity com_trolmastercard_sexmod_em_class258_E() {
        ManglelieEntity f8_class2932 = this.com_trolmastercard_sexmod_f8_class293_a(false);
        if (f8_class2932 == null) {
            return super.com_trolmastercard_sexmod_em_class258_E();
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (((Entity)entityPlayerSP).isSneaking()) {
            return f8_class2932;
        }
        ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString((Object)((Object)TextFormatting.GRAY) + "[sneak] + [right click] if you want to edit Manglelie instead"), true);
        return super.com_trolmastercard_sexmod_em_class258_E();
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (this.maybeMountedByMangFn()) {
            return this.a(entityPlayer, enumHand);
        }
        return this.b(entityPlayer, enumHand);
    }

    boolean a(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (!entityPlayer.getPersistentID().equals(this.java_util_UUID_O())) {
            return false;
        }
        if (Action.a((GirlEntity)this, Action.HUG_MANG, Action.RUN, Action.GALATH_SUMMON, Action.GALATH_DE_SUMMON, Action.MASTERBATE)) {
            return false;
        }
        if (GalathCoin.GALATH_COIN.equals(entityPlayer.getHeldItem(EnumHand.OFF_HAND).getItem()) || GalathCoin.GALATH_COIN.equals(entityPlayer.getHeldItem(EnumHand.MAIN_HAND).getItem())) {
            return false;
        }
        this.PlaySound(SoundsHandler.GIRLS_GALATH_HUH, new int[0]);
        String[] stringArray = !entityPlayer.onGround ? new String[]{"ride"} : (this.com_trolmastercard_sexmod_f8_class293_a(false) == null ? new String[]{"cowgirl", "anal", "ride"} : new String[]{"cowgirl", "anal", "threesome", "ride"});
        if (this.world.isRemote) {
            GalathEntity.a(entityPlayer, this.com_trolmastercard_sexmod_em_class258_af(), stringArray, false);
        }
        return true;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void a(String string, UUID uUID) {
        if ("ride".equals(string)) {
            hf_class401.f();
            PackageHandler.networkWrapper.sendToServer((IMessage)new RequestRiding());
            return;
        }
        if ("anal".equals(string)) {
            fh_class313.b();
            HandlePlayerMovement.a(false);
            be_class78.a(1200, () -> {
                EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                this.c(entityPlayerSP.getPositionVector());
                this.void_b(0.0f);
                this.void_e(entityPlayerSP.getPersistentID());
                this.void_a(true);
                this.setCurrentAction(Action.CORRUPT_SLOW);
            });
            return;
        }
        if ("cowgirl".equals(string)) {
            fh_class313.b();
            HandlePlayerMovement.a(false);
            be_class78.a(1200, () -> {
                EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                this.c(entityPlayerSP.getPositionVector());
                this.void_b(entityPlayerSP.rotationYaw + 180.0f);
                this.setCurrentAction(Action.RAPE_INTRO);
                this.void_e(entityPlayerSP.getPersistentID());
                this.void_a(true);
            });
            return;
        }
        if ("threesome".equals(string)) {
            ManglelieEntity f8_class2932 = this.com_trolmastercard_sexmod_f8_class293_a(false);
            if (f8_class2932 == null) {
                return;
            }
            fh_class313.b();
            HandlePlayerMovement.a(false);
            be_class78.a(1200, () -> {
                Minecraft minecraft = Minecraft.getMinecraft();
                EntityPlayerSP entityPlayerSP = minecraft.player;
                minecraft.gameSettings.thirdPersonView = 1;
                f8_class2932.c(entityPlayerSP.getPositionVector());
                this.c(entityPlayerSP.getPositionVector());
                f8_class2932.void_b(entityPlayerSP.rotationYaw + 180.0f);
                this.void_b(entityPlayerSP.rotationYaw);
                f8_class2932.setCurrentAction(Action.THREESOME_SLOW);
                this.setCurrentAction(Action.PUSSY_LICKING);
                f8_class2932.void_e(entityPlayerSP.getPersistentID());
                this.void_e(entityPlayerSP.getPersistentID());
                f8_class2932.void_a(true);
                this.void_a(true);
            });
        }
    }

    boolean b(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (!this.entityDataManager.get(bP).booleanValue()) {
            return super.processInteract(entityPlayer, enumHand);
        }
        if (this.currentAction() != Action.KNOCK_OUT_GROUND) {
            return super.processInteract(entityPlayer, enumHand);
        }
        if (this.world.isRemote) {
            entityPlayer.rotationYaw -= -128.0f;
            entityPlayer.rotationPitch = 19.0f;
            return true;
        }
        this.setCurrentAction(Action.CORRUPT_INTRO);
        this.void_e(entityPlayer.getPersistentID());
        this.void_a(true);
        this.c(this.getPositionVector());
        this.void_b(entityPlayer.rotationYaw);
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        entityPlayer.setPositionAndUpdate(this.posX, this.posY, this.posZ);
        return true;
    }

    @Override
    @Nullable
    public Entity[] getParts() {
        return new Entity[]{this.V, this.b2};
    }

    public void a(@Nullable EntityLivingBase entityLivingBase) {
        if (entityLivingBase == null) {
            this.entityDataManager.set(bq, -1);
            return;
        }
        this.entityDataManager.set(bq, entityLivingBase.getEntityId());
    }

    public int ar() {
        return this.entityDataManager.get(aP);
    }

    public void b(int n) {
        this.entityDataManager.set(aP, n);
    }

    public EntityLivingBase net_minecraft_entity_EntityLivingBase_M() {
        int n = this.entityDataManager.get(bq);
        if (-1 == n) {
            return null;
        }
        return (EntityLivingBase)this.world.getEntityByID(n);
    }

    public static Float a(GalathEntity f__class2972, float f) {
        float f2;
        Action fp_class3242 = f__class2972.currentAction();
        if (fp_class3242 != Action.FLY && fp_class3242 != Action.SUMMON_SKELETON && fp_class3242 != Action.RAPE_PREPARE) {
            return null;
        }
        EntityLivingBase entityLivingBase = f__class2972.net_minecraft_entity_EntityLivingBase_M();
        if (entityLivingBase == null) {
            return null;
        }
        Vec3d vec3d = Reference.LerpVec3d(new Vec3d(entityLivingBase.lastTickPosX, entityLivingBase.lastTickPosY, entityLivingBase.lastTickPosZ), entityLivingBase.getPositionVector(), (double)f);
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(f__class2972.lastTickPosX, f__class2972.lastTickPosY, f__class2972.lastTickPosZ), f__class2972.getPositionVector(), (double)f);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        f__class2972.renderYawOffset = f2 = (float)gc_class360.b(Math.atan2(vec3d3.z, vec3d3.x)) - 90.0f;
        f__class2972.prevRenderYawOffset = f2;
        return Float.valueOf(f2);
    }

    void c(float f) {
        if (!this.world.isRemote) {
            return;
        }
        if (this.getHealth() - f <= 0.0f) {
            return;
        }
        long l = System.currentTimeMillis();
        if (l < this.bc + 1000L) {
            return;
        }
        this.PlaySound(SoundsHandler.GIRLS_GALATH_UUH, new int[0]);
        this.bc = l;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (damageSource.isFireDamage()) {
            return false;
        }
        if (DamageSource.DROWN.equals(damageSource)) {
            return false;
        }
        if (DamageSource.CACTUS.equals(damageSource)) {
            return false;
        }
        if (DamageSource.FALL.equals(damageSource)) {
            return false;
        }
        if (DamageSource.FLY_INTO_WALL.equals(damageSource)) {
            return false;
        }
        this.c(f);
        return super.attackEntityFrom(damageSource, f);
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart multiPartEntityPart, DamageSource damageSource, float f) {
        if (this.world.isRemote) {
            return false;
        }
        if (!(damageSource.getTrueSource() instanceof EntityPlayer)) {
            return false;
        }
        if (multiPartEntityPart == this.V) {
            this.entityDataManager.set(b7, false);
            PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(this.V.getPositionVector(), false), (Entity)this);
        }
        if (multiPartEntityPart == this.b2) {
            this.entityDataManager.set(bN, false);
            PackageHandler.networkWrapper.sendToAllTracking((IMessage)new SpawnEnergyBallParticlesAlt(this.b2.getPositionVector(), false), (Entity)this);
        }
        return true;
    }

    @Override
    public void void_g() {
        this.a((EntityLivingBase)null);
        this.aH();
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void setFire(int n) {
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    @Nullable
    protected Action FastSexAction(Action action) {
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.CORRUPT_FAST || action == Action.CORRUPT_SLOW) {
            return Action.CORRUPT_CUM;
        }
        if (action == Action.RAPE_ON_GOING) {
            return Action.RAPE_CUM;
        }
        if (Action.a(action, Action.MORNING_BLOWJOB_SLOW, Action.MORNING_BLOWJOB_FAST)) {
            this.S = true;
        }
        return null;
    }

    @Override
    public boolean boolean_c() {
        return this.bb;
    }

    @Override
    public boolean boolean_a() {
        switch (this.currentAction()) {
            case CORRUPT_SLOW: 
            case CORRUPT_FAST: 
            case CORRUPT_CUM: 
            case COWGIRLCUM: {
                return false;
            }
        }
        return true;
    }

    public void c(boolean bl) {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 != Action.RAPE_ON_GOING && fp_class3242 != Action.RAPE_INTRO) {
            return;
        }
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        if (0.0f >= entityPlayer.getHealth() - 1.0f) {
            return;
        }
        if (entityPlayer.capabilities.isCreativeMode) {
            return;
        }
        entityPlayer.attackEntityFrom(new a3_class10(this), 1.0f);
        if (bl) {
            this.heal(1.5f);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setString("sexmod:master", (String)this.entityDataManager.get(v));
        if (this.bA) {
            nBTTagCompound.setBoolean("sexmod:despawned", true);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        String string;
        UUID uUID;
        super.readEntityFromNBT(nBTTagCompound);
        this.entityDataManager.set(v, nBTTagCompound.getString("sexmod:master"));
        if (nBTTagCompound.getBoolean("sexmod:despawned")) {
            this.P = true;
        }
        if ((uUID = this.java_util_UUID_O()) != null && (string = gy_class392.a(uUID, PlayerGirlEntity.GALATH)) != null) {
            this.g(string);
        }
    }

    public void ak() {
        if (this.currentAction() == Action.MASTERBATE_SITTING) {
            return;
        }
        this.bx = true;
        this.setCurrentAction(Action.MASTERBATE_SITTING);
    }

    public void void_a() {
        this.a5 = true;
        this.setCurrentAction(Action.PUSSY_LICKING);
    }

    @Override
    protected boolean a(Action fp_class3242, String string, boolean bl, AnimationEvent animationEvent) {
        if (fp_class3242 == Action.MASTERBATE_SITTING && this.bx) {
            this.bx = false;
            this.createAnimation("animation.galath.masterbating_sitting", true, animationEvent, true);
            return true;
        }
        if (fp_class3242 == Action.MORNING_BLOWJOB_FAST && this.S) {
            this.setCurrentAction(Action.MORNING_BLOWJOB_CUM);
            return true;
        }
        if (fp_class3242 == Action.MORNING_BLOWJOB_FAST && this.aD) {
            this.createAnimation("animation.shared.bed_fast", true, animationEvent, true);
            this.aD = false;
            return true;
        }
        if (fp_class3242 == Action.MORNING_BLOWJOB_CUM) {
            this.setCurrentAction((Action)null);
            return true;
        }
        if (fp_class3242 == Action.PUSSY_LICKING && this.a5) {
            this.a5 = false;
            this.createAnimation("animation.galath.pussy_licking", true, animationEvent, true);
            return true;
        }
        if (fp_class3242 == Action.MORNING_BLOWJOB_SLOW && (this.S || HandlePlayerMovement.isThrusting)) {
            this.aD = true;
            this.setCurrentAction(Action.MORNING_BLOWJOB_FAST);
            this.createAnimation("animation.shared.bed_soft", true, animationEvent, true);
            return true;
        }
        if (fp_class3242 == Action.MORNING_BLOWJOB_SLOW && this.bt) {
            this.bt = false;
            this.createAnimation("animation.shared.bed_slow", true, animationEvent, true);
            return true;
        }
        if (fp_class3242 == Action.MORNING_BLOWJOB_FAST && !HandlePlayerMovement.isThrusting) {
            this.setCurrentAction(Action.MORNING_BLOWJOB_SLOW);
            this.bt = true;
            this.createAnimation("animation.shared.bed_back", true, animationEvent, true);
            return true;
        }
        return false;
    }

    public float float_b(float f) {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.PUSSY_LICKING && !this.a5) {
            return 0.0f;
        }
        if (fp_class3242 == Action.MASTERBATE_SITTING && !this.bx) {
            return 1.0f;
        }
        float f2 = Action.d(this, f);
        if (fp_class3242 == Action.MASTERBATE_SITTING) {
            return f2;
        }
        return 1.0f - f2;
    }

    // TODO
    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.boolean_h()) {
            this.createAnimation("animation.galath.idle", true, animationEvent);
            return PlayState.CONTINUE;
        }
        Action fp_class3242 = this.currentAction();
        AnimationController animationController = animationEvent.getController();
        animationController.setAnimationSpeed(1.0);
        if (animationController.equals(this.eyesController)) {
            if (!fp_class3242.autoBlink || fp_class3242 == Action.GALATH_DE_SUMMON) {
                return PlayState.STOP;
            }
            this.createAnimation("animation.galath.blink", true, animationEvent);
            return PlayState.CONTINUE;
        }
        if (animationController.equals(this.movementController)) {
            if (fp_class3242 != Action.NULL) {
                return PlayState.STOP;
            }
            if (!this.onGround) {
                this.createAnimation("animation.galath.controlled_flight", true, animationEvent);
                return PlayState.CONTINUE;
            }
            Vec3d vec3d = this.getPositionVector().subtract(new Vec3d(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ));
            if (vec3d.equals(Vec3d.ZERO)) {
                this.createAnimation("animation.galath.idle", true, animationEvent);
                return PlayState.CONTINUE;
            }
            this.rotationYaw = this.rotationYawHead;
            this.createAnimation("animation.galath." + (this.entityDataManager.get(bT) != false ? "run" : "walk"), true, animationEvent);
            return PlayState.CONTINUE;
        }
        switch (this.currentAction()) {
            case NULL: {
                return PlayState.STOP;
            }
            case FLY: {
                this.createAnimation("animation.galath.idle_flying", true, animationEvent);
                break;
            }
            case SUMMON_SKELETON: {
                this.createAnimation("animation.galath.summon_skeleton" + (this.entityDataManager.get(ay) != false ? "Mirrored" : ""), true, animationEvent);
                break;
            }
            case ATTACK_SWORD: {
                this.createAnimation("animation.galath.attack", true, animationEvent);
                break;
            }
            case KNOCK_OUT_FLY: {
                animationController.setAnimationSpeed(1.5);
                this.createAnimation("animation.galath.knockout_air", true, animationEvent);
                break;
            }
            case KNOCK_OUT_GROUND: {
                this.createAnimation("animation.galath.knocked_out", true, animationEvent);
                break;
            }
            case KNOCK_OUT_STAND_UP: {
                this.createAnimation("animation.galath.knocked_out_stand_up", true, animationEvent);
                break;
            }
            case RAPE_PREPARE: {
                this.createAnimation("animation.galath.rape_prepare", true, animationEvent);
                break;
            }
            case RAPE_CHARGE: {
                this.createAnimation("animation.galath.rape_charge", true, animationEvent);
                break;
            }
            case RAPE_INTRO: {
                this.createAnimation("animation.galath.rape_intro", true, animationEvent);
                break;
            }
            case RAPE_ON_GOING: {
                this.createAnimation("animation.galath.rape" + this.b1, true, animationEvent);
                break;
            }
            case RAPE_CUM: {
                this.createAnimation("animation.galath.rape_cum", true, animationEvent);
                break;
            }
            case RAPE_CUM_IDLE: {
                this.createAnimation("animation.galath.rape_cum_idle", true, animationEvent);
                break;
            }
            case CORRUPT_FAST: {
                this.createAnimation("animation.galath.corrupt_" + (this.aT ? "hard" : "soft"), true, animationEvent);
                break;
            }
            case CORRUPT_SLOW: {
                this.createAnimation("animation.galath.corrupt_slow", true, animationEvent);
                break;
            }
            case CORRUPT_INTRO: {
                this.createAnimation("animation.galath.corrupt_intro", true, animationEvent);
                break;
            }
            case CORRUPT_CUM: {
                this.createAnimation("animation.galath.corrupt_cum", true, animationEvent);
                break;
            }
            case CONTROLLED_FLIGHT: {
                this.createAnimation("animation.galath.controlled_flight", true, animationEvent);
                break;
            }
            case BOOST: {
                this.createAnimation("animation.galath.boost", true, animationEvent);
                break;
            }
            case GALATH_SUMMON: {
                this.createAnimation("animation.galath.summon", false, animationEvent);
                break;
            }
            case GALATH_DE_SUMMON: {
                this.createAnimation("animation.galath.desummon" + (this.onGround ? "_standing" : ""), true, animationEvent);
                break;
            }
            case GIVE_COIN: {
                this.createAnimation("animation.galath.give_coin", true, animationEvent);
                break;
            }
            case MASTERBATE: {
                this.createAnimation("animation.galath.masterbate", true, animationEvent);
                break;
            }
            case RUN: {
                animationController.setAnimationSpeed(0.7);
                this.createAnimation("animation.galath.running", true, animationEvent);
                break;
            }
            case HUG_MANG: {
                this.createAnimation("animation.galath.hug_mang", true, animationEvent);
                break;
            }
            case PUSSY_LICKING: {
                this.createAnimation(this.a5 ? "animation.galath.pussy_licking_forward" : "animation.galath.pussy_licking", true, animationEvent);
                break;
            }
            case MASTERBATE_SITTING: {
                this.createAnimation(this.bx ? "animation.galath.pussy_licking_back" : "animation.galath.masterbating_sitting", true, animationEvent);
                break;
            }
            case MASTERBATE_SITTING_CUM: {
                this.createAnimation("animation.galath.masterbating_sitting_cum", true, animationEvent);
                break;
            }
            case MORNING_BLOWJOB_SLOW: {
                this.createAnimation(this.bt ? "animation.shared.bed_back" : "animation.shared.bed_slow", true, animationEvent);
                break;
            }
            case MORNING_BLOWJOB_FAST: {
                if (this.aD) {
                    this.createAnimation("animation.shared.bed_soft", true, animationEvent);
                    break;
                }
                this.a("animation.shared.bed_fast", 4, 0.75f, animationEvent);
                break;
            }
            case MORNING_BLOWJOB_CUM: {
                this.createAnimation("animation.shared.bed_cum", true, animationEvent);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData animationData) {
        this.actionController = new bz_class107<>(this, "action", 0.0f, this::predicate);
        this.movementController = new AnimationController<>(this, "movement", 5.0f, this::predicate);
        this.eyesController = new AnimationController<>(this, "eyes", 10.0f, this::predicate);
        this.actionController.registerSoundListener(soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "goodTiming": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_DIALOG[4]);
                    this.void_a("Good timing boy~");
                    break;
                }
                case "huh": {
                    this.PlaySound(SoundsHandler.GIRLS_GALATH_HUH, new int[0]);
                    break;
                }
                case "giggle": {
                    Vec3d vec3d = this.net_minecraft_util_math_Vec3d_A();
                    this.world.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.a(SoundsHandler.GIRLS_GALATH_GIGGLE), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
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
                    Vec3d vec3d = this.net_minecraft_util_math_Vec3d_A();
                    this.world.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.a(SoundsHandler.GIRLS_GALATH_LIGHTCHARGE), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
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
                    this.a(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
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
                    Vec3d vec3d = this.net_minecraft_util_math_Vec3d_A();
                    this.world.playSound(vec3d.x, vec3d.y, vec3d.z, SoundsHandler.a(SoundsHandler.MISC_FLAP), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                case "startRenderSword": {
                    this.ap = true;
                    this.bu = true;
                    break;
                }
                case "stopFadeInParticles": {
                    this.bu = false;
                    break;
                }
                case "stopRenderSword": {
                    this.ap = false;
                    this.bu = false;
                    break;
                }
                case "dontDrawStars": {
                    this.aL = false;
                    break;
                }
                case "setNude": {
                    this.bb = true;
                    Vec3d vec3d = this.getPositionVector();
                    Vec3d vec3d2 = this.b("slipR").add(vec3d);
                    Vec3d vec3d3 = this.b("slipL").add(vec3d);
                    Vec3d vec3d4 = this.b("turnable").add(vec3d);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d2.x, vec3d2.y, vec3d2.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d3.x, vec3d3.y, vec3d3.z, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d4.x, vec3d4.y, vec3d4.z, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case "rapeIntroDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.RAPE_ON_GOING);
                    break;
                }
                case "rape_switch": {
                    EntityPlayerSP entityPlayerSP;
                    Random random = this.getRNG();
                    int n = this.b1;
                    do {
                        this.b1 = random.nextInt(3);
                    } while (this.b1 == n);
                    if (this.maybeMountedByMangFn() || !this.boolean_n() || !(0.0f >= (entityPlayerSP = Minecraft.getMinecraft().player).getHealth() - 1.0f)) break;
                    this.setCurrentAction(Action.RAPE_CUM);
                    break;
                }
                case "poundRape": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.boolean_n()) break;
                    if (this.maybeMountedByMangFn()) {
                        SexUI.addCumPercentage(0.03f);
                        break;
                    }
                    PackageHandler.networkWrapper.sendToServer((IMessage)new GalathRapePounce(true));
                    break;
                }
                case "rapeHurt": {
                    if (this.maybeMountedByMangFn() || !this.boolean_n()) break;
                    PackageHandler.networkWrapper.sendToServer((IMessage)new GalathRapePounce(false));
                    break;
                }
                case "enableRapeUI": {
                    if (!this.boolean_n()) break;
                    if (this.maybeMountedByMangFn()) {
                        SexUI.a(false);
                        break;
                    }
                    EscapeMinigameUI.a();
                    break;
                }
                case "removeUI": {
                    if (!this.boolean_n() || this.maybeMountedByMangFn()) break;
                    EscapeMinigameUI.d();
                    break;
                }
                case "reloadRenderer": {
                    if (!this.boolean_n()) {
                        return;
                    }
                    Minecraft minecraft = Minecraft.getMinecraft();
                    if (minecraft.gameSettings.thirdPersonView == 0) break;
                    minecraft.renderGlobal.loadRenderers();
                    break;
                }
                case "corruptSwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CORRUPT_FAST);
                    break;
                }
                case "corrupt_hard": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.aT = true;
                    this.N();
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
                    ga_class358.a(this);
                    break;
                }
                case "setCamCorrupt": {
                    if (!this.boolean_n()) {
                        return;
                    }
                    this.U = true;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    float f = this.java_lang_Float_I().floatValue() + 220.0f;
                    Vec3d vec3d = ck_class135.a(new Vec3d(0.5, 0.5f - entityPlayerSP.getEyeHeight(), 0.4f), this.java_lang_Float_I().floatValue()).add(this.net_minecraft_util_math_Vec3d_o());
                    PackageHandler.networkWrapper.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, f, 15.0f));
                    SexUI.d();
                    break;
                }
                case "enableBoyCam": {
                    if (!this.boolean_n()) break;
                    this.U = false;
                    break;
                }
                case "masterbateCumming": {
                    if (!FutaCommand.enabled) break;
                    ga_class358.a(new ep_class263(90, girlEntity -> {
                        Vec3d vec3d = girlEntity.d("futaCockTip");
                        Vec3d vec3d2 = girlEntity.d("futaCockTipDirHelp");
                        return vec3d.subtract(vec3d2).normalize();
                    }, em_class2582 -> em_class2582.b("futaCockTip").add(em_class2582.net_minecraft_util_math_Vec3d_o()), this, 0.3f, 0.3f));
                    break;
                }
                case "creampie": {
                    ga_class358.a(new ep_class263(100, em_class2582 -> ck_class135.a(new Vec3d(0.0, 0.0, 0.6f), this.java_lang_Float_I().floatValue()), em_class2582 -> em_class2582.b("creampiePos").add(em_class2582.net_minecraft_util_math_Vec3d_o()), this, 0.6f, 0.5f));
                    // TODO fallthrough looks intentional
                }
                case "creampieGalath": {
                    if (FutaCommand.enabled) {
                        ga_class358.a(new ep_class263(130, em_class2582 -> {
                            Vec3d vec3d = em_class2582.d("futaCockTip");
                            Vec3d vec3d2 = em_class2582.d("futaCockTipDirHelp");
                            return vec3d.subtract(vec3d2).normalize();
                        }, em_class2582 -> em_class2582.b("futaCockTip").add(em_class2582.net_minecraft_util_math_Vec3d_o()), this, 0.3f, 0.3f));
                    }
                    this.a(SoundsHandler.a(SoundsHandler.MISC_SMALLINSERTS), 3.0f);
                    break;
                }
                case "blackScreenTamed": {
                    if (!this.maybeMountedByMangFn()) break;
                }
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "blackScreenMaster": {
                    if (!Minecraft.getMinecraft().player.getPersistentID().equals(this.java_util_UUID_O())) break;
                    fh_class313.b();
                    HandlePlayerMovement.a(false);
                    break;
                }
                case "flapControlled": {
                    if (!this.boolean_n()) break;
                    hf_class401.f();
                    this.PlaySound(SoundsHandler.MISC_FLAP, new int[0]);
                    Minecraft minecraft = Minecraft.getMinecraft();
                    EntityPlayerSP entityPlayerSP = minecraft.player;
                    MovementInput movementInput = entityPlayerSP.movementInput;
                    Vec2f vec2f = movementInput.getMoveVector();
                    if (vec2f.x == 0.0f && vec2f.y == 0.0f) break;
                    Vec3d vec3d = ck_class135.a(new Vec3d(-vec2f.x, 0.0, vec2f.y), Reference.LerpFloat(entityPlayerSP.prevRotationPitch, entityPlayerSP.rotationPitch, minecraft.getRenderPartialTicks()), Reference.LerpFloat(entityPlayerSP.prevRotationYawHead, entityPlayerSP.rotationYawHead, minecraft.getRenderPartialTicks()));
                    PackageHandler.networkWrapper.sendToServer((IMessage)new UpdateVelocity(vec3d, this.girlID()));
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
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    break;
                }
                case "setCoinLook": {
                    float f;
                    if (!this.boolean_n()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    entityPlayerSP.rotationYaw = f = this.java_lang_Float_I().floatValue() + 180.0f;
                    entityPlayerSP.prevRotationYaw = f;
                    entityPlayerSP.rotationPitch = 0.0f;
                    entityPlayerSP.prevRotationPitch = 0.0f;
                    break;
                }
                case "sexui": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "boostSound": {
                    Minecraft.getMinecraft().player.playSound(SoundsHandler.a(SoundsHandler.GIRLS_GALATH_LIGHTCHARGE), 1.0f, 1.0f);
                    Minecraft.getMinecraft().player.playSound(SoundsHandler.a(SoundsHandler.MISC_FLAP), 1.0f, 1.0f);
                }
            }
        });
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.eyesController);
        animationData.addAnimationController(this.movementController);
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class a_inner298 {
        boolean a(GalathEntity galath) {
            return galath.player() != null;
        }

        @SubscribeEvent(priority=EventPriority.LOWEST)
        public void a(LivingSpawnEvent.CheckSpawn checkSpawn) {
            //World world;
            Event.Result result = checkSpawn.getResult();
            if (result == Event.Result.DENY) {
                return;
            }
            if (checkSpawn.isSpawner()) {
                return;
            }
            Entity entity = checkSpawn.getEntity();
            if (!(entity instanceof EntityWitherSkeleton) && !(entity instanceof EntityBlaze)) {
                return;
            }
            BlockPos blockPos = entity.getPosition();
            World world = entity.world;
            if (!GalathEntity.a(blockPos, world)) {
                return;
            }
            checkSpawn.setResult(Event.Result.DENY);
            fq_class325.a(blockPos, fq_class325.c);
            GalathEntity f__class2972 = new GalathEntity(world);
            f__class2972.setPositionAndUpdate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            world.spawnEntity(f__class2972);
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(InputEvent.KeyInputEvent keyInputEvent) {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (!minecraft.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            if (!hf_class401.d()) {
                return;
            }
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (!em_class2582.world.isRemote || !(em_class2582 instanceof GalathEntity) || !minecraft.player.getPersistentID().equals(((GalathEntity)em_class2582).ax())) continue;
                    hf_class401.a();
                    em_class2582.setCurrentAction(Action.BOOST);
                    return;
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }

        @SubscribeEvent
        public void a(EntityMountEvent entityMountEvent) {
            if (entityMountEvent.isMounting()) {
                return;
            }
            Entity entity = entityMountEvent.getEntityBeingMounted();
            if (!(entity instanceof GalathEntity)) {
                return;
            }
            if (entity.world.isRemote) {
                hf_class401.c();
                return;
            }
            ((GalathEntity)entity).void_t();
        }

        @SubscribeEvent(priority=EventPriority.HIGH)
        public void a(LivingDeathEvent livingDeathEvent) {
            Entity entity = livingDeathEvent.getEntity();
            if (!(entity instanceof GalathEntity)) {
                return;
            }
            if (livingDeathEvent.getSource().equals(DamageSource.OUT_OF_WORLD)) {
                return;
            }
            GalathEntity f__class2972 = (GalathEntity)entity;
            if (f__class2972.bU) {
                return;
            }
            if (entity.world.isRemote) {
                return;
            }
            if (!f__class2972.maybeMountedByMangFn()) {
                f__class2972.void_a((Entity)f__class2972.getCombatTracker().getFighter());
            } else {
                GalathCoin.a(f__class2972);
                PackageHandler.networkWrapper.sendToAllTracking((IMessage)new com.trolmastercard.sexmod.Packages.SpawnEnergyBallParticles(f__class2972.girlID(), GalathMangTracker.b(f__class2972)), (Entity)f__class2972);
                be_class78.a(900, () -> GalathMangTracker.a(f__class2972));
                f__class2972.bU = true;
            }
            f__class2972.setHealth(1.0f);
            livingDeathEvent.setCanceled(true);
        }

        @SubscribeEvent
        public void a(PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)playerRespawnEvent.player;
            GirlEntity em_class2582 = GirlEntity.a(entityPlayerMP.getPersistentID(), true);
            if (!(em_class2582 instanceof GalathEntity)) {
                return;
            }
            GalathEntity f__class2972 = (GalathEntity)em_class2582;
            f__class2972.a((EntityLivingBase)null);
            ResetGirl.a_inner422.a(em_class2582);
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), entityPlayerMP);
            em_class2582.setCurrentAction((Action)null);
            if (f__class2972.bZ == null) {
                return;
            }
            f__class2972.bZ.e(f__class2972);
            f__class2972.bZ = null;
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(RenderWorldLastEvent renderWorldLastEvent) {
            Minecraft minecraft = Minecraft.getMinecraft();
            RenderManager renderManager = minecraft.getRenderManager();
            float f = minecraft.getRenderPartialTicks();
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    EnergyBallEntity c4_class1132;
                    Vec3d vec3d;
                    Vec3d vec3d2;
                    double d;
                    if (!(em_class2582 instanceof GalathEntity) || !em_class2582.world.isRemote || em_class2582.currentAction() != Action.SUMMON_SKELETON || (d = (double)((GalathEntity)em_class2582).ad) < 9.0 || d > 30.0) continue;
                    Vec3d vec3d3 = Reference.LerpVec3d(new Vec3d(em_class2582.lastTickPosX, em_class2582.lastTickPosY, em_class2582.lastTickPosZ), em_class2582.getPositionVector(), (double)f);
                    double d2 = (d - 9.0) / 21.0;
                    if (em_class2582.getDataManager().get(bN).booleanValue()) {
                        vec3d2 = em_class2582.b("energyBallR");
                        vec3d = vec3d3.add(vec3d2);
                        c4_class1132 = new EnergyBallEntity(em_class2582.world, (GalathEntity)em_class2582);
                        c4_class1132.g = d2;
                        c4_class1132.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                        renderManager.renderEntity(c4_class1132, 0.0, 0.0, 0.0, 0.0f, f, true);
                        c4_class1132.setPosition(0.0, -500.0, 0.0);
                        c4_class1132.setDead();
                    }
                    if (!em_class2582.getDataManager().get(b7).booleanValue()) continue;
                    vec3d2 = em_class2582.b("energyBallL");
                    vec3d = vec3d3.add(vec3d2);
                    c4_class1132 = new EnergyBallEntity(em_class2582.world, (GalathEntity)em_class2582);
                    c4_class1132.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                    c4_class1132.g = d2;
                    renderManager.renderEntity(c4_class1132, 0.0, 0.0, 0.0, 0.0f, f, true);
                    c4_class1132.setPosition(0.0, -500.0, 0.0);
                    c4_class1132.setDead();
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
        }

        boolean a(World world, BlockPos blockPos, EnumFacing enumFacing) {
            if (enumFacing == EnumFacing.NORTH) {
                if (this.a(world, blockPos = blockPos.west())) {
                    return false;
                }
                if (this.a(world, blockPos.up())) {
                    return false;
                }
                if (this.a(world, blockPos.south())) {
                    return false;
                }
                return !this.a(world, blockPos.south().up());
            }
            if (enumFacing == EnumFacing.WEST) {
                if (this.a(world, blockPos = blockPos.south())) {
                    return false;
                }
                if (this.a(world, blockPos.up())) {
                    return false;
                }
                if (this.a(world, blockPos.east())) {
                    return false;
                }
                return !this.a(world, blockPos.east().up());
            }
            if (enumFacing == EnumFacing.SOUTH) {
                if (this.a(world, blockPos = blockPos.east())) {
                    return false;
                }
                if (this.a(world, blockPos.up())) {
                    return false;
                }
                if (this.a(world, blockPos.north())) {
                    return false;
                }
                return !this.a(world, blockPos.north().up());
            }
            if (enumFacing == EnumFacing.EAST) {
                if (this.a(world, blockPos = blockPos.north())) {
                    return false;
                }
                if (this.a(world, blockPos.up())) {
                    return false;
                }
                if (this.a(world, blockPos.west())) {
                    return false;
                }
                return !this.a(world, blockPos.west().up());
            }
            Main.LOGGER.error("Weird bed orientation, when checking for space next to bed, on galaths morning blowjob animation: " + enumFacing.getName());
            return false;
        }

        boolean a(World world, BlockPos blockPos) {
            Block block = world.getBlockState(blockPos).getBlock();
            for (Class<?> clazz : aS) {
                if (!clazz.isInstance(block)) continue;
                return false;
            }
            return true;
        }

        @SubscribeEvent
        public void a(PlayerWakeUpEvent playerWakeUpEvent) {
            float f;
            EntityPlayer entityPlayer = playerWakeUpEvent.getEntityPlayer();
            if (entityPlayer.world.isRemote) {
                return;
            }
            if (!GalathMangTracker.isReadyForMorningGlory(entityPlayer.getPersistentID(), entityPlayer.world)) {
                return;
            }
            Vec3d vec3d = entityPlayer.getPositionVector();
            BlockPos blockPos = new BlockPos(vec3d);
            if (!this.a(entityPlayer.world, blockPos, entityPlayer.world.getBlockState(blockPos).getValue(BlockHorizontal.FACING))) {
                entityPlayer.sendMessage(new TextComponentString(String.format("%sFor Galath and Manglelie to %swake you up with a blowjob%s, you have to provide enough space to the %sright side%s of your bed. This includes the %stop and bottom half%s of the bed.", new Object[]{TextFormatting.GRAY, TextFormatting.DARK_RED, TextFormatting.GRAY, TextFormatting.DARK_RED, TextFormatting.GRAY, TextFormatting.DARK_RED, TextFormatting.GRAY})));
                return;
            }
            switch (entityPlayer.world.getBlockState(blockPos).getValue(BlockHorizontal.FACING)) {
                case NORTH: {
                    f = 180.0f;
                    break;
                }
                case EAST: {
                    f = -90.0f;
                    break;
                }
                case WEST: {
                    f = 90.0f;
                    break;
                }
                default: {
                    f = 0.0f;
                    break;
                }
            }
            Vec3d vec3d2 = new Vec3d((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5);
            UUID uUID = GalathMangTracker.b(entityPlayer);
            if (uUID != null) {
                GalathMangTracker.a((GalathEntity) GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID));
            }
            GalathEntity f__class2972 = new GalathEntity(entityPlayer.world, entityPlayer, vec3d, true);
            f__class2972.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            entityPlayer.world.spawnEntity(f__class2972);
            GalathMangTracker.a(entityPlayer, f__class2972);
            f__class2972.boolean_v();
            f__class2972.c(vec3d2);
            f__class2972.void_b(f);
            f__class2972.void_a(true);
            f__class2972.void_e(entityPlayer.getPersistentID());
            f__class2972.setCurrentAction(Action.MORNING_BLOWJOB_SLOW);
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
            be_class78.a(500, () -> {
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
                PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerCam(-10.0f, f + 180.0f + 5.0f, 0), (EntityPlayerMP)entityPlayer);
            });
        }
    }
}

