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
package com.trolmastercard.sexmod;

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
public abstract class GirlEntity
extends EntityCreature
implements IAnimatable {
    static public int j = 22;
    final static protected long t = 20L;
    final private AnimationFactory factory = new AnimationFactory(this);
    public EntityAIWanderAvoidWater avoidWater;
    public df_class178 o;
    static public HashSet<GirlEntity> k = new HashSet();
    public Vec3d B;
    protected float r;
    protected EntityDataManager entityDataManager;
    public PathNavigate f;
    public Vec3d homeCoords = Vec3d.ZERO;
    public EntityEnderPearl q;
    public float n = 1.0f;
    public boolean F = false;
    private boolean i = false;
    HashMap<String, Vec3d> x = new HashMap();
    final static public DataParameter<String> v = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(110);
    final static public DataParameter<Boolean> G = EntityDataManager.createKey(GirlEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(109);
    final static public DataParameter<String> e = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(108);
    final static public DataParameter<Float> w = EntityDataManager.createKey(GirlEntity.class, DataSerializers.FLOAT).getSerializer().createKey(107);
    final static public DataParameter<String> GIRL_ID = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(106);
    final static public DataParameter<Integer> D = EntityDataManager.createKey(GirlEntity.class, DataSerializers.VARINT).getSerializer().createKey(105);
    final static public DataParameter<String> J = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(104);
    final static public DataParameter<String> h = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(103);
    final static public DataParameter<String> y = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(102);
    final static public DataParameter<String> a = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(101);
    final static public DataParameter<String> b = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(100);
    final static public DataParameter<String> c = EntityDataManager.createKey(GirlEntity.class, DataSerializers.STRING).getSerializer().createKey(99);
    final static protected List<Item> I = Arrays.asList(Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.ENDER_PEARL);
    public AnimationController C;
    public AnimationController E;
    public AnimationController s;
    HashMap<String, Pair<Integer, Integer>> A = new HashMap();
    AnimationProcessor<?> H = null;
    public List<String> p = new ArrayList<String>();
    protected List<Map.Entry<gw_class389, Map.Entry<List<String>, Integer>>> d = null;

    public void a(WalkTypes a_inner2592) {
        this.entityDataManager.set(a, a_inner2592.toString());
    }

    public WalkTypes com_trolmastercard_sexmod_em_class258$a_inner259_q() {
        return WalkTypes.valueOf(this.entityDataManager.get(a));
    }

    @SideOnly(value=Side.CLIENT)
    protected void changeDataParameterFromClient(String string, String string2) {
        NetworkRegistry.networkWrapper.sendToServer(new n_class415(this.girlID(), string, string2));
    }

    //f
    public UUID girlID() {
        try {
            return UUID.fromString(this.entityDataManager.get(GIRL_ID));
        } catch (Exception exception) {
            UUID uUID = UUID.randomUUID();
            this.entityDataManager.set(GIRL_ID, uUID.toString());
            return uUID;
        }
    }

    public Action currentAction() {
        return Action.valueOf(this.entityDataManager.get(J));
    }

    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == fp_class3242) {
            return;
        }
        if (fp_class3242 == Action.ATTACK && fp_class3243 != Action.NULL) {
            return;
        }
        Action fp_class3244 = fp_class3242 = fp_class3242 == null ? Action.NULL : fp_class3242;
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("currentAction", fp_class3242.toString());
            return;
        }
        fp_class3243.ticksPlaying = new int[]{0, 0};
        this.entityDataManager.set(J, fp_class3242.toString());
    }

    public int int_ah() {
        return this.entityDataManager.get(D);
    }

    public void f(int n) {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("currentModel", "0");
        } else {
            this.entityDataManager.set(D, n);
        }
    }

    public boolean boolean_m() {
        return false;
    }

    @Nullable
    public EntityPlayer getPlayerEntity() {
        UUID uUID = this.getID();
        if (uUID == null) {
            return null;
        }
        return this.world.getPlayerEntityByUUID(uUID);
    }

    public static void a(GirlEntity em_class2582, String string) {
        for (EntityPlayer entityPlayer : cj_class134.a(em_class2582)) {
            entityPlayer.sendMessage(new TextComponentString(string));
        }
    }

    public static void a(GirlEntity em_class2582, SoundEvent soundEvent, boolean bl) {
        Vec3d vec3d = em_class2582.getPositionVector();
        for (EntityPlayer entityPlayer : cj_class134.a(em_class2582)) {
            Vec3d vec3d2;
            if (!bl) {
                vec3d2 = vec3d;
            } else {
                Vec3d vec3d3 = entityPlayer.getPositionVector();
                Vec3d vec3d4 = vec3d.subtract(vec3d3).normalize();
                vec3d2 = vec3d3.add(vec3d4);
            }
            ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketSoundEffect(soundEvent, SoundCategory.AMBIENT, vec3d2.x, vec3d2.y, vec3d2.z, 1.0f, 1.0f));
        }
    }

    public static void a(GirlEntity em_class2582, SoundEvent soundEvent) {
        GirlEntity.a(em_class2582, soundEvent, false);
    }

    public static void a(GirlEntity em_class2582, SoundEvent[] soundEventArray) {
        GirlEntity.a(em_class2582, SoundEventHandler.a(soundEventArray));
    }

    public static void a(GirlEntity em_class2582, SoundEvent[] soundEventArray, boolean bl) {
        GirlEntity.a(em_class2582, SoundEventHandler.a(soundEventArray), bl);
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d net_minecraft_util_math_Vec3d_A() {
        Vec3d vec3d = Minecraft.getMinecraft().player.getPositionVector();
        Vec3d vec3d2 = this.getPositionVector();
        Vec3d vec3d3 = vec3d2.subtract(vec3d).normalize();
        return vec3d.add(vec3d3);
    }

    @Nullable
    public UUID getID() {
        String string = this.entityDataManager.get(y);
        if (string.equals("null")) {
            return null;
        }
        return UUID.fromString(string);
    }

    public void void_e(UUID uUID) {
        if (this.world.isRemote) {
            if (uUID == null) {
                this.changeDataParameterFromClient("playerSheHasSexWith", (String)null);
            } else {
                this.changeDataParameterFromClient("playerSheHasSexWith", uUID.toString());
            }
            return;
        }
        if (uUID == null) {
            this.entityDataManager.set(y, "null");
        } else {
            this.entityDataManager.set(y, uUID.toString());
        }
    }

    public void void_a(@Nonnull EntityPlayer entityPlayer) {
        this.void_e(entityPlayer.getPersistentID());
    }

    public Vec3d net_minecraft_util_math_Vec3d_o() {
        String[] stringArray = this.entityDataManager.get(e).split("\\|");
        return new Vec3d(Double.parseDouble(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]));
    }

    public void c(Vec3d vec3d) {
        if (this.world.isRemote) {
            String string = vec3d.x + "f" + vec3d.y + "f" + vec3d.z + "f";
            this.changeDataParameterFromClient("targetPos", string);
            return;
        }
        this.entityDataManager.set(e, vec3d.x + "|" + vec3d.y + "|" + vec3d.z);
    }

    public void a(Vec3d vec3d) {
        this.entityDataManager.set(e, vec3d.x + "|" + vec3d.y + "|" + vec3d.z);
    }

    public Float java_lang_Float_I() {
        return this.entityDataManager.get(w);
    }

    public void void_b(float f) {
        this.entityDataManager.set(w, Float.valueOf(f));
    }

    public void void_a(boolean bl) {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("shouldbeattargetpos", String.valueOf(bl));
            return;
        }
        this.entityDataManager.set(G, bl);
    }

    public boolean boolean_Q() {
        return this.entityDataManager.get(G);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    protected GirlEntity(World world) {
        super(world);
        if (world.isRemote) {
            this.void_p();
        }
        if (world.isRemote && world instanceof FakeWorld) {
            return;
        }
        PathNavigate pathNavigate = this.getNavigator();
        if (pathNavigate instanceof PathNavigateGround) {
            ((PathNavigateGround)pathNavigate).setBreakDoors(true);
        }
    }

    @SideOnly(value=Side.CLIENT)
    protected void void_p() {
        this.C = new AnimationController<GirlEntity>(this, "action", 0.0f, this::predicate);
        this.E = new AnimationController<GirlEntity>(this, "movement", 5.0f, this::predicate);
        this.s = new AnimationController<GirlEntity>(this, "eyes", 10.0f, this::predicate);

        // TODO not sure where to insert sound keyframe inserter, or how to even add custom insertions in Java for geckolib

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.f = this.getNavigator();
        this.entityDataManager = this.getDataManager();
        this.entityDataManager.register(GIRL_ID, UUID.randomUUID().toString());
        this.entityDataManager.register(D, 1);
        this.entityDataManager.register(J, Action.NULL.toString());
        this.entityDataManager.register(h, "");
        this.entityDataManager.register(y, "null");
        this.entityDataManager.register(G, false);
        this.entityDataManager.register(w, Float.valueOf(0.0f));
        this.entityDataManager.register(e, "0|0|0");
        this.entityDataManager.register(v, "");
        this.entityDataManager.register(a, WalkTypes.WALK.toString());
        this.entityDataManager.register(b, "");
        this.entityDataManager.register(c, "");
    }

    public void b(boolean bl) {
        this.i = bl;
        if (bl) {
            fs_class327.b(this);
        } else {
            fs_class327.a(this);
        }
    }

    public boolean boolean_h() {
        return this.i;
    }

    public static List<GirlEntity> GirlEntityList() {
        if (!ClientServerCheck.getInstance()) {
            return GirlEntity.Z();
        }
        WorldServer[] worldServerArray = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
        if (worldServerArray.length == 0) {
            return new ArrayList<GirlEntity>();
        }
        ArrayList<GirlEntity> arrayList = new ArrayList<GirlEntity>();
        for (WorldServer worldServer : worldServerArray) {
            arrayList.addAll(worldServer.getEntities(GirlEntity.class, em_class2582 -> true));
        }
        return arrayList;
    }

    @SideOnly(value=Side.CLIENT)
    private static List<GirlEntity> Z() {
        WorldClient worldClient = Minecraft.getMinecraft().world;
        if (worldClient == null) {
            return new ArrayList<GirlEntity>();
        }
        return worldClient.getEntities(GirlEntity.class, em_class2582 -> true);
    }

    public boolean boolean_B() {
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
        this.avoidWater = new EntityAIWanderAvoidWater(this, 0.35);
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAITempt((EntityCreature)this, 0.4, false, new HashSet<Item>(I)));
        this.tasks.addTask(3, new hz_class409(this));
        this.tasks.addTask(5, this.o);
        this.tasks.addTask(5, this.avoidWater);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setDouble("homeX", this.homeCoords.x);
        nBTTagCompound.setDouble("homeY", this.homeCoords.y);
        nBTTagCompound.setDouble("homeZ", this.homeCoords.z);
        nBTTagCompound.setString("girlID", this.entityDataManager.get(GIRL_ID));
        String string = this.java_lang_String_w();
        if (!"".equals(string)) {
            nBTTagCompound.setString("sexmod:customname", string);
        }
        if (this.boolean_X()) {
            nBTTagCompound.setString("sexmod:customModel", this.java_lang_String_C());
        }
        super.writeEntityToNBT(nBTTagCompound);
    }

    protected boolean boolean_X() {
        return GirlEntity.boolean_a((Entity)this);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        String string;
        super.readEntityFromNBT(nBTTagCompound);
        this.homeCoords = new Vec3d(nBTTagCompound.getDouble("homeX"), nBTTagCompound.getDouble("homeY"), nBTTagCompound.getDouble("homeZ"));
        String string2 = nBTTagCompound.getString("sexmod:customname");
        if (!"".equals(string2)) {
            this.g(string2);
        }
        if ("".equals(string = nBTTagCompound.getString("girlID"))) {
            return;
        }
        UUID uUID = UUID.fromString(string);
        boolean bl = false;
        for (GirlEntity em_class2582 : GirlEntity.girlList(uUID)) {
            if (em_class2582.world.isRemote || em_class2582 == this || em_class2582.isDead || !em_class2582.isAddedToWorld()) continue;
            bl = true;
            break;
        }
        if (bl) {
            Main.LOGGER.log(Level.WARN, String.format("got a duped %s with id '%s'. Deleted her", this.getGirlName(), uUID));
            this.world.removeEntity(this);
            return;
        }
        this.entityDataManager.set(GIRL_ID, uUID.toString());
        if (this.boolean_X()) {
            this.f(nBTTagCompound.getString("sexmod:customModel"));
        }
    }

    public boolean boolean_d() {
        return true;
    }

    @Override
    public void setVelocity(double d, double d2, double d3) {
        this.motionX = d;
        this.motionY = d2;
        this.motionZ = d3;
    }

    public void b(Vec3d vec3d) {
        this.motionX = vec3d.x;
        this.motionY = vec3d.y;
        this.motionZ = vec3d.z;
    }

    public Vec3d net_minecraft_util_math_Vec3d_j() {
        return new Vec3d(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ);
    }

    @Override
    public void updateAITasks() {
        if (this.entityDataManager.get(G).booleanValue()) {
            this.setRotationYawHead(this.java_lang_Float_I().floatValue());
            this.setPositionAndRotation(this.net_minecraft_util_math_Vec3d_o().x, this.net_minecraft_util_math_Vec3d_o().y, this.net_minecraft_util_math_Vec3d_o().z, this.java_lang_Float_I().floatValue(), 0.0f);
            this.setRotation(this.java_lang_Float_I().floatValue(), this.rotationPitch);
        }
        if (this.homeCoords.equals(Vec3d.ZERO)) {
            this.homeCoords = new Vec3d(this.getPosition());
        }
        this.G();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.void_l();
    }

    protected void G() {
        if (!CustomModels.e) {
            return;
        }
        HashSet<String> hashSet = this.Y();
        PlayerGirlEntity fy_class3352 = PlayerGirlEntity.a(this);
        HashSet<String> hashSet2 = new HashSet<String>();
        String string = CustomModels.h();
        for (String string2 : hashSet) {
            if (!"".equals(CustomModels.a(string2, string))) {
                hashSet2.add(string2);
                continue;
            }
            HashSet<PlayerGirlEntity> hashSet3 = CustomModels.a(string2);
            if (hashSet3 == null) {
                hashSet2.add(string2);
                continue;
            }
            if (hashSet3.isEmpty() || hashSet3.contains((Object)fy_class3352)) continue;
            hashSet2.add(string2);
        }
        if (hashSet2.isEmpty()) {
            return;
        }
        hashSet.removeAll(hashSet2);
        this.f(GirlEntity.a(hashSet));
    }

    protected void void_l() {
        Action fp_class3242 = this.currentAction();
        int n = this.world.isRemote ? 1 : 0;
        fp_class3242.ticksPlaying[n] = fp_class3242.ticksPlaying[n] + 1;
        if (fp_class3242.ticksPlaying[n] < fp_class3242.length) {
            return;
        }
        if (fp_class3242.followUp == null) {
            return;
        }
        if (!this.world.isRemote) {
            this.setCurrentAction(fp_class3242.followUp);
        }
    }

    protected void void_k() {
        Path path = this.getNavigator().getPath();
        if (path == null) {
            return;
        }
        if (this.onGround || this.isInWater()) {
            return;
        }
        int n = path.getCurrentPathIndex();
        int n2 = path.getCurrentPathLength();
        if (n2 == n || n2 - 1 == n) {
            return;
        }
        PathPoint pathPoint = path.getPathPointFromIndex(n);
        PathPoint pathPoint2 = path.getPathPointFromIndex(n + 1);
        Vec3d vec3d = new Vec3d(pathPoint2.x - pathPoint.x, pathPoint2.y - pathPoint.y, pathPoint2.z - pathPoint.z);
        this.motionX = vec3d.x / 7.0;
        this.motionZ = vec3d.z / 7.0;
    }

    public void void_g() {
    }

    @SideOnly(value=Side.CLIENT)
    public boolean boolean_b(EntityPlayer entityPlayer) {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    protected static void a(EntityPlayer entityPlayer, GirlEntity em_class2582) {
        Minecraft.getMinecraft().displayGuiScreen(new GirlGUI(em_class2582, entityPlayer));
    }

    @SideOnly(value=Side.CLIENT)
    protected static void a(EntityPlayer entityPlayer, GirlEntity em_class2582, String[] stringArray, ItemStack[] itemStackArray, boolean bl) {
        Minecraft.getMinecraft().displayGuiScreen(new GirlGUI(em_class2582, entityPlayer, stringArray, itemStackArray, bl));
    }

    @SideOnly(value=Side.CLIENT)
    protected static void a(EntityPlayer entityPlayer, GirlEntity em_class2582, String[] stringArray, boolean bl) {
        Minecraft.getMinecraft().displayGuiScreen(new GirlGUI(em_class2582, entityPlayer, stringArray, null, bl));
    }

    public void void_a(ItemStack itemStack) {
        this.activeItemStack = itemStack;
    }

    public void d(int n) {
        this.activeItemStackUseCount = n;
    }

    public Vec3d net_minecraft_util_math_Vec3d_M() {
        return new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ);
    }

    protected static Vec3d net_minecraft_util_math_Vec3d_a(GirlEntity em_class2582) {
        return new Vec3d(em_class2582.prevPosX, em_class2582.prevPosY, em_class2582.prevPosZ);
    }

    public GirlEntity com_trolmastercard_sexmod_em_class258_af() {
        return this;
    }

    public void goHome() {
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("master", "");
            this.changeDataParameterFromClient("walk speed", WalkTypes.WALK.toString());
        } else {
            this.entityDataManager.set(v, "");
            this.entityDataManager.set(a, WalkTypes.WALK.toString());
        }
    }

    protected void a(EntityPlayerMP entityPlayerMP, boolean bl) {
        entityPlayerMP.motionX = 0.0;
        entityPlayerMP.motionY = 0.0;
        entityPlayerMP.motionZ = 0.0;
        if (bl) {
            Vec3d vec3d = this.a(0.35);
            entityPlayerMP.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        }
    }

    public void j(UUID uUID) {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        entityPlayer.motionX = 0.0;
        entityPlayer.motionY = 0.0;
        entityPlayer.motionZ = 0.0;
        Vec3d vec3d = this.a(0.35);
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        this.void_b(entityPlayer.rotationYawHead + 180.0f);
    }

    protected void a(boolean bl, boolean bl2, UUID uUID) {
        if (this.world.isRemote) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new dc_class174(this.girlID(), uUID, bl, bl2));
        } else {
            dc_class174.a_inner175.a(this.girlID(), uUID, bl, bl2);
        }
    }

    public static GirlEntity getGirlEntity(UUID uUID) {
        if (uUID == null) {
            return null;
        }
        for (GirlEntity girlEntity : GirlEntity.girlList(uUID)) {
            if (!girlEntity.world.isRemote) continue;
            return girlEntity;
        }
        return null;
    }

    public static GirlEntity com_trolmastercard_sexmod_em_class258_a(UUID uUID) {
        if (uUID == null) {
            return null;
        }
        for (GirlEntity girlEntity : GirlEntity.girlList(uUID)) {
            if (girlEntity.world.isRemote) continue;
            return girlEntity;
        }
        return null;
    }

    // TODO clashes with KoboldEntity 'void g(UUID)'
    public static ArrayList<GirlEntity> girlList(UUID uUID) {
        ArrayList<GirlEntity> arrayList = new ArrayList<GirlEntity>();
        try {
            for (GirlEntity girlEntity : GirlEntity.GirlEntityList()) {
                if (girlEntity == null || !girlEntity.girlID().equals(uUID)) continue;
                arrayList.add(girlEntity);
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            System.out.println("had a ConcurrentModificationException while cycling through the girl list... hopefully nothin borke owo");
            concurrentModificationException.printStackTrace();
        }
        return arrayList;
    }

    protected BlockPos net_minecraft_util_math_BlockPos_a(BlockPos blockPos) {
        return this.a(blockPos, 1);
    }

    public BlockPos a(BlockPos blockPos, int n) {
        return this.a(blockPos, n, Blocks.BED, 22, 3, null);
    }

    public void W() {
        this.entityDataManager.set(HAND_STATES, Byte.valueOf("1"));
    }

    public void K() {
        this.entityDataManager.set(HAND_STATES, Byte.valueOf("0"));
    }

    public BlockPos a(BlockPos blockPos, int n, Block block, int n2, int n3, @Nullable HashSet<Biome> hashSet) {
        int n4 = 1;
        int n5 = -1;
        BlockPos blockPos2 = blockPos;
        int n6 = 0;
        while (n4 < n2) {
            for (int i = 0; i < 2; ++i) {
                int n7;
                int n8;
                n5 *= -1;
                for (n8 = 0; n8 < n4; ++n8) {
                    blockPos2 = blockPos2.add(0, 0, n5);
                    for (n7 = -n3; n7 < n3 + 1; ++n7) {
                        if (this.world.getBlockState(blockPos2.add(0, n7, n5)).getBlock() != block || ++n6 < n || hashSet != null && !hashSet.contains(this.world.getBiome(blockPos2.add(n5, n7, 0)))) continue;
                        return blockPos2.add(0, n7, n5);
                    }
                }
                for (n8 = 0; n8 < n4; ++n8) {
                    blockPos2 = blockPos2.add(n5, 0, 0);
                    for (n7 = -n3; n7 < n3 + 1; ++n7) {
                        if (this.world.getBlockState(blockPos2.add(n5, n7, 0)).getBlock() != block || ++n6 < n || hashSet != null && !hashSet.contains(this.world.getBiome(blockPos2.add(n5, n7, 0)))) continue;
                        return blockPos2.add(n5, n7, 0);
                    }
                }
                ++n4;
            }
        }
        return null;
    }

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

    public boolean boolean_J() {
        return !this.entityDataManager.get(v).isEmpty();
    }

    @Nullable
    public UUID java_util_UUID_O() {
        String string = this.entityDataManager.get(v);
        if ("".equals(string)) {
            return null;
        }
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    @Nullable
    public EntityPlayer net_minecraft_entity_player_EntityPlayer_z() {
        UUID uUID = this.java_util_UUID_O();
        if (uUID == null) {
            return null;
        }
        return this.world.getPlayerEntityByUUID(uUID);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableRegistry.JENNY_LOOT_TABLE;
    }

    @SideOnly(value=Side.CLIENT)
    public void a(String string, UUID uUID) {
    }

    // TODO rename animationPredicateHandler or whatever
    @SideOnly(value=Side.CLIENT)
    protected abstract <E extends IAnimatable> PlayState predicate(AnimationEvent<E> var1);

    @SideOnly(value=Side.CLIENT)
    protected boolean a(Action fp_class3242, String string, boolean bl, AnimationEvent animationEvent) {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    protected void createAnimation(String string, boolean looped, AnimationEvent animationEvent, boolean bl2) {
        if (!bl2 && Action.b(this, animationEvent.getPartialTick()) && this.a(this.currentAction(), string, d3_class161.d, animationEvent)) {
            return;
        }
        ILoopType.EDefaultLoopTypes eDefaultLoopTypes = looped ? ILoopType.EDefaultLoopTypes.LOOP : ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME;
        animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation(string, eDefaultLoopTypes));
        animationEvent.getController().transitionLengthTicks = 0.0;
    }

    @SideOnly(value=Side.CLIENT)
    protected void createAnimation(String string, boolean bl, AnimationEvent animationEvent) {
        this.createAnimation(string, bl, animationEvent, false);
    }

    @SideOnly(value=Side.CLIENT)
    protected void a(String string, int n, float f, AnimationEvent animationEvent, boolean bl) {
        if (!bl && Action.b(this, animationEvent.getPartialTick()) && this.a(this.currentAction(), string, d3_class161.d, animationEvent)) {
            return;
        }
        AnimationController animationController = animationEvent.getController();
        Pair pair = this.A.get(string);
        if (pair == null) {
            pair = Pair.of((Object)0, (Object)0);
        }
        int n2 = (Integer)pair.first();
        int n3 = (Integer)pair.second();
        if (!Action.b(this, animationEvent.getPartialTick())) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation(n2 == 0 ? string : string + n2, ILoopType.EDefaultLoopTypes.LOOP));
            animationEvent.getController().transitionLengthTicks = 0.0;
            return;
        }
        int n4 = this.a(n2, n3, n, f);
        animationController.setAnimation(new AnimationBuilder().addAnimation(n4 == 0 ? string : string + n4, ILoopType.EDefaultLoopTypes.LOOP));
        animationController.transitionLengthTicks = 0.0;
        this.A.put(string, Pair.of(n4, (n4 == 0 ? n3 : n4)));
    }

    @SideOnly(value=Side.CLIENT)
    protected void a(String string, int n, float f, AnimationEvent animationEvent) {
        this.a(string, n, f, animationEvent, false);
    }

    // TODO probably utilized for random sounds
    int a(int n, int n2, int n3, float f) {
        int n4;
        if (n != 0) {
            return 0;
        }
        Random random = this.getRNG();
        if (random.nextFloat() > f) {
            return 0;
        }
        while (true) {
            if (((n4 = random.nextInt(n3)) != n2 && n4 != 0) || n3 <= 2) break;
        }
        return n4;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public abstract void registerControllers(AnimationData var1);

    protected void s() {
        if (this.world.isRemote && this.boolean_n()) {
            this.B = null;
            NetworkRegistry.networkWrapper.sendToServer(new s_class421(this.girlID(), true));
        } else if (!this.world.isRemote) {
            s_class421.a_inner422.a((EntityPlayerMP)this.world.getPlayerEntityByUUID(this.getID()));
        }
    }

    public static GirlEntity com_trolmastercard_sexmod_em_class258_c(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return null;
        }
        return GirlEntity.com_trolmastercard_sexmod_em_class258_i(entityPlayer.getPersistentID());
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d a(Minecraft minecraft, CustomModelEntity cy_class1532, EntityLivingBase entityLivingBase, float f) {
        return CustomModelRenderer.a(minecraft, cy_class1532, entityLivingBase, this, f);
    }

    public static GirlEntity com_trolmastercard_sexmod_em_class258_i(@Nonnull UUID uUID) {
        return GirlEntity.a(uUID, (Boolean) null);
    }

    public static GirlEntity a(@Nonnull UUID uUID, Boolean bl) {
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                if (em_class2582.isDead || !uUID.equals(em_class2582.getID())) continue;
                if (bl == null) {
                    return em_class2582;
                }
                boolean bl2 = em_class2582.world.isRemote;
                if (bl2 && !bl.booleanValue()) {
                    return em_class2582;
                }
                if (bl2 || !bl.booleanValue()) continue;
                return em_class2582;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        return null;
    }

    @Nullable
    public static GirlEntity com_trolmastercard_sexmod_em_class258_c(@Nonnull UUID uUID) {
        boolean bl = FMLCommonHandler.instance().getMinecraftServerInstance() == null;
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                boolean bl2;
                if (em_class2582.isDead || (bl2 = em_class2582.world.isRemote) != bl || !uUID.equals(em_class2582.getID())) continue;
                return em_class2582;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        return null;
    }

    public static GirlEntity com_trolmastercard_sexmod_em_class258_d(@Nonnull EntityPlayer entityPlayer) {
        return GirlEntity.com_trolmastercard_sexmod_em_class258_c(entityPlayer.getPersistentID());
    }

    @SideOnly(value=Side.CLIENT)
    public void ac() {
    }

    public void void_r() {
        this.B = null;
        this.setNoGravity(false);
        this.setCurrentAction((Action)null);
        if (this.world.isRemote) {
            this.V();
        }
    }

    @SideOnly(value=Side.CLIENT)
    protected void V() {
        if (this.boolean_n()) {
            d3_class161.a(true);
            Minecraft.getMinecraft().player.setInvisible(false);
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new s_class421(this.girlID()));
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void k(UUID uUID) {
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                UUID uUID2 = em_class2582.getID();
                if (uUID2 == null || !uUID2.equals(uUID)) continue;
                Action fp_class3242 = em_class2582.FastSexAction(em_class2582.currentAction());
                if (fp_class3242 == null) {
                    return;
                }
                em_class2582.setCurrentAction(fp_class3242);
                return;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void f(UUID uUID) {
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                Action fp_class3242;
                UUID uUID2;
                if (em_class2582.isDead || !em_class2582.world.isRemote || (uUID2 = em_class2582.getID()) == null || !uUID2.equals(uUID) || (fp_class3242 = em_class2582.CumAction(em_class2582.currentAction())) == null) continue;
                em_class2582.setCurrentAction(fp_class3242);
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    public void N() {
        this.ag();
        NetworkRegistry.networkWrapper.sendToServer((IMessage)new a1_class7(this.girlID()));
    }

    @SideOnly(value=Side.CLIENT)
    public void ag() {
        this.C.tickOffset = 0.0;
    }

    @SideOnly(value=Side.CLIENT)
    @Nullable
    protected abstract Action FastSexAction(Action action);

    @SideOnly(value=Side.CLIENT)
    protected abstract Action CumAction(Action action);

    public net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint net_minecraftforge_fml_common_network_NetworkRegistry$TargetPoint_P() {
        return new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50.0);
    }

    protected void a(double d, double d2, double d3, float f, float f2) {
        if (this.getID() == null) {
            System.out.println("couldnt move camera because the player isn't set");
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (this.B == null) {
            this.B = entityPlayer.getPositionVector();
        }
        Vec3d vec3d = this.B;
        vec3d = vec3d.add(-Math.sin((double)(this.r + 90.0f) * (Math.PI / 180)) * d, 0.0, Math.cos((double)(this.r + 90.0f) * (Math.PI / 180)) * d);
        vec3d = vec3d.add(0.0, d2, 0.0);
        vec3d = vec3d.add(-Math.sin((double)this.r * (Math.PI / 180)) * d3, 0.0, Math.cos((double)this.r * (Math.PI / 180)) * d3);
        if (this.world.isRemote) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new a8_class16(entityPlayer.getPersistentID().toString(), vec3d, this.r + f, f2));
            return;
        }
        entityPlayer.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, this.r + f, f2);
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    @SideOnly(value=Side.CLIENT)
    protected boolean boolean_n() {
        if (!this.world.isRemote) {
            return false;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        return entityPlayerSP.getPersistentID().equals(this.getID()) || entityPlayerSP.getUniqueID().equals(this.getID());
    }

    protected void U() {
    }

    public void g(String string) {
        this.entityDataManager.set(c, string);
    }

    public String java_lang_String_w() {
        return this.entityDataManager.get(c);
    }

    public abstract String getGirlName();

    public String java_lang_String_ab() {
        String string = this.entityDataManager.get(c);
        if (!string.isEmpty()) {
            return string;
        }
        return this.getGirlName();
    }

    public abstract float float_i();

    @SideOnly(value=Side.CLIENT)
    public boolean boolean_t() {
        return true;
    }

    public void h(String string) {
        if (!this.world.isRemote) {
            NetworkRegistry.networkWrapper.sendToAllAround((IMessage)new gh_class368(String.format("<%s> %s", this.java_lang_String_ab(), string), this.dimension, this.girlID()), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 40.0));
        } else if (this.boolean_n()) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new gh_class368(String.format("<%s> %s", this.java_lang_String_ab(), string), this.dimension, this.girlID()));
        }
    }

    protected void b(String string, boolean bl) {
        if (!bl) {
            this.h(string);
        }
        if (!this.world.isRemote) {
            NetworkRegistry.networkWrapper.sendToAllAround((IMessage)new gh_class368(string, this.dimension, this.girlID()), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 40.0));
            return;
        }
        if (this.boolean_n()) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new gh_class368(string, this.dimension, this.girlID()));
        }
    }

    protected void void_a(String string) {
        if (this.world.isRemote) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("<%s> %s", this.java_lang_String_ab(), string)));
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

    public void PlaySoundAtPosition(SoundEvent soundEvent, float volume, float pitch) {
        this.world.playSound(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), soundEvent, SoundCategory.NEUTRAL, volume, pitch, false);
    }

    public void PlaySound(SoundEvent soundEvent) {
        this.PlaySoundAtPosition(soundEvent, 1.0f, 1.0f);
    }

    public void PlaySound(SoundEvent[] soundEventArray, int ... nArray) {
        if (nArray.length == 0) {
            this.PlaySound(soundEventArray[this.getRNG().nextInt(soundEventArray.length)]);
            return;
        }
        this.PlaySoundAtPosition(soundEventArray[nArray[this.getRNG().nextInt(nArray.length)]], 1.0f, 1.0f);
    }

    public void a(SoundEvent[] soundEventArray, float f) {
        this.PlaySoundAtPosition(soundEventArray[this.getRNG().nextInt(soundEventArray.length)], f, 1.0f);
    }

    public void a(SoundEvent soundEvent, float f) {
        this.PlaySoundAtPosition(soundEvent, f, 1.0f);
    }

    public static boolean boolean_a(Entity entity) {
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
    public boolean boolean_e() {
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 50.0);
        if (entityPlayer == null) {
            return false;
        }
        return entityPlayer.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID());
    }

    public Vec3d net_minecraft_util_math_Vec3d_aa() {
        return this.a(1.0);
    }

    public Vec3d a(double d) {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        float f = entityPlayer.rotationYaw;
        return entityPlayer.getPositionVector().add(-Math.sin((double)f * (Math.PI / 180)) * d, 0.0, Math.cos((double)f * (Math.PI / 180)) * d);
    }

    public Vec3d net_minecraft_util_math_Vec3d_a(Vec3d vec3d, float f) {
        return vec3d;
    }

    public static void a(EnumParticleTypes enumParticleTypes, GirlEntity em_class2582) {
        double d = ModInfo.f.nextGaussian() * 0.02;
        double d2 = ModInfo.f.nextGaussian() * 0.02;
        double d3 = ModInfo.f.nextGaussian() * 0.02;
        em_class2582.world.spawnParticle(enumParticleTypes, em_class2582.posX + (double)(ModInfo.f.nextFloat() * em_class2582.width * 2.0f) - (double)em_class2582.width, em_class2582.posY + 0.5 + (double)(ModInfo.f.nextFloat() * em_class2582.height), em_class2582.posZ + (double)(ModInfo.f.nextFloat() * em_class2582.width * 2.0f) - (double)em_class2582.width, d, d2, d3, new int[0]);
    }

    public static void a(EnumParticleTypes enumParticleTypes, GirlEntity em_class2582, int n) {
        for (int i = 0; i < n; ++i) {
            GirlEntity.a(enumParticleTypes, em_class2582);
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
                this.void_a("whopa");
            }
            return SoundEventHandler.a(SoundEventHandler.MISC_FART);
        }
        return null;
    }

    public float float_T() {
        return 0.0f;
    }

    public float float_ai() {
        return 0.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public MatrixStack a(String string, boolean bl) {
        IBone iBone;
        if (this.H == null) {
            this.H = this.b();
        }
        if ((iBone = this.H.getBone(string)) == null) {
            if (!GirlModel.e.contains(string)) {
                Main.LOGGER.log(Level.WARN, String.format("The bone '%s' does not exist on %s. " +
                        "Bone model matrix couldn't be calculated", string, this.getGirlName()));
                this.p.remove(string);
            }
            return new MatrixStack();
        }
        GeoBone geoBone = (GeoBone)iBone;
        ArrayList<GeoBone> arrayList = new ArrayList<GeoBone>();
        {
            GeoBone object = geoBone;
            while (object.parent != null) {
                GeoBone geoBone2 = object.parent;
                arrayList.add(geoBone2);
                object = geoBone2;
            }
        }
        Collections.reverse(arrayList);
        MatrixStack object = new MatrixStack();
        if (this.boolean_Q()) {
            ((MatrixStack)object).rotateY((float)(-Math.toRadians(this.java_lang_Float_I().floatValue())));
        } else if (bl) {
            ((MatrixStack)object).rotateY((float)(-Math.toRadians(b6_class67.a(this.prevRenderYawOffset, this.renderYawOffset, Minecraft.getMinecraft().getRenderPartialTicks()))));
        }
        for (GeoBone geoBone3 : arrayList) {
            ((MatrixStack)object).translate(geoBone3);
            ((MatrixStack)object).moveToPivot(geoBone3);
            ((MatrixStack)object).rotate(geoBone3);
            ((MatrixStack)object).scale(geoBone3);
            ((MatrixStack)object).moveBackFromPivot(geoBone3);
        }
        ((MatrixStack)object).translate(geoBone);
        ((MatrixStack)object).moveToPivot(geoBone);
        ((MatrixStack)object).rotate(geoBone);
        ((MatrixStack)object).scale(geoBone);
        object = this.a((MatrixStack)object);
        return object;
    }

    protected MatrixStack a(MatrixStack matrixStack) {
        return matrixStack;
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d b(String string) {
        Vec3d vec3d = this.x.get(string);
        if (vec3d != null) {
            return vec3d;
        }
        if (!this.p.contains(string)) {
            this.p.add(string);
        }
        return Vec3d.ZERO;
    }

    @SideOnly(value=Side.CLIENT)
    public Vec3d d(String string) {
        return this.b(string).add(this.getPositionVector());
    }

    public void a(String string, Vec3d vec3d) {
        this.x.put(string, vec3d);
    }

    @SideOnly(value=Side.CLIENT)
    public float float_R() {
        AnimationProcessor<?> animationProcessor = this.b();
        IBone iBone = animationProcessor.getBone("girlCam");
        if (iBone == null) {
            return 0.0f;
        }
        float f = iBone.getPivotY();
        f = this.a(f);
        return f / 16.0f;
    }

    @SideOnly(value=Side.CLIENT)
    public float float_v() {
        return 1.0f;
    }

    @CheckReturnValue
    protected float a(float f) {
        return f;
    }

    @CheckReturnValue
    public AnimatedGeoModel<? extends GirlEntity> a() {
        Minecraft minecraft = Minecraft.getMinecraft();
        Render render = minecraft.getRenderManager().getEntityRenderObject(this);
        if (render == null) {
            return null;
        }
        if (!(render instanceof GirlRenderer)) {
            return null;
        }
        GeoEntityRenderer geoEntityRenderer = (GeoEntityRenderer)render;
        GeoModelProvider geoModelProvider = geoEntityRenderer.getGeoModelProvider();
        if (geoModelProvider == null) {
            return null;
        }
        if (!(geoModelProvider instanceof AnimatedGeoModel)) {
            return null;
        }
        return (AnimatedGeoModel)geoModelProvider;
    }

    @CheckReturnValue
    public AnimationProcessor<?> b() {
        return this.a().getAnimationProcessor();
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
    public e1_class217 g(int n) {
        return e1_class217.a;
    }

    public void void_a(List<Integer> list) {
        if (!(this instanceof e4_class223) && !(this instanceof ew_class277)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int n : list) {
            e4_class223.c(stringBuilder, n);
        }
        this.entityDataManager.set(e4_class223.M, stringBuilder.toString());
    }

    public String java_lang_String_F() {
        if (this instanceof e4_class223 || this instanceof ew_class277) {
            return this.entityDataManager.get(e4_class223.M);
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
        GirlEntity em_class2582 = Main.proxy instanceof ClientProxy ? GirlEntity.getGirlEntity(uUID) : GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID);
        ArrayList<Integer> arrayList = new ArrayList<Integer>(em_class2582.L());
        if (em_class2582 instanceof e4_class223 || em_class2582 instanceof ew_class277) {
            arrayList.addAll(GirlEntity.c(em_class2582.getDataManager().get(e4_class223.M)));
        }
        return arrayList;
    }

    public ArrayList<Integer> L() {
        return new ArrayList<Integer>();
    }

    public List<Map.Entry<gw_class389, Map.Entry<List<String>, Integer>>> d(UUID uUID) {
        if (this.d != null) {
            return this.d;
        }
        ArrayList<Integer> arrayList = this.D();
        if (arrayList.isEmpty()) {
            this.d = new ArrayList<Map.Entry<gw_class389, Map.Entry<List<String>, Integer>>>();
            return this.d;
        }
        ArrayList<Map.Entry<gw_class389, Map.Entry<List<String>, Integer>>> arrayList2 = new ArrayList<Map.Entry<gw_class389, Map.Entry<List<String>, Integer>>>();
        List<Integer> list = GirlEntity.h(uUID);
        for (int i = 0; i < arrayList.size(); ++i) {
            //arrayList2.add(new AbstractMap.SimpleEntry(
            //        gw_class389.GIRL_SPECIFIC,
            //        new AbstractMap.SimpleEntry(this.e((Integer)var2.get(var5)), var4.get(var5))));

            arrayList2.add(
                    new AbstractMap.SimpleEntry<>(
                                    gw_class389.GIRL_SPECIFIC, new AbstractMap.SimpleEntry<>(this.e(arrayList.get(i)), list.get(i))));
        }
        this.d = arrayList2;
        return arrayList2;
    }

    public void b(List<Map.Entry<gw_class389, Map.Entry<List<String>, Integer>>> list) {
        this.d = list;
    }

    public void a(int n, int n2) {
        if (this.d == null) {
            return;
        }
        if (this.d.size() - 1 < n) {
            return;
        }
        Map.Entry<gw_class389, Map.Entry<List<String>, Integer>> entry = this.d.get(n);
        entry.getValue().setValue(n2);
        this.d.set(n, entry);
    }

    public void e(String string) {
        if (this instanceof e4_class223 || this instanceof ew_class277) {
            this.entityDataManager.set(e4_class223.M, string);
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

    public void f(String string) {
        this.entityDataManager.set(b, string);
    }

    public String java_lang_String_C() {
        return this.entityDataManager.get(b);
    }

    public static String a(HashSet<String> hashSet) {
        if (hashSet == null) {
            return "";
        }
        if (hashSet.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : hashSet) {
            stringBuilder.append(string);
            stringBuilder.append("#");
        }
        return stringBuilder.toString();
    }

    public HashSet<String> Y() {
        String string = this.java_lang_String_C();
        String[] stringArray = string.split("#");
        HashSet<String> hashSet = new HashSet<String>();
        for (String string2 : stringArray) {
            if ("".equals(string2) || "cross".equals(string2)) continue;
            hashSet.add(string2);
        }
        return hashSet;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean boolean_H() {
        return true;
    }

    private static RuntimeException c(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static enum WalkTypes {
        WALK,
        FAST_WALK,
        RUN;
    }
}

