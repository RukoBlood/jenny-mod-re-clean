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
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.ResetGirl;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.ea_class235;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.world.FakeWorld;
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

public class GoblinEntity
extends e4_class223
implements ai_class30 {
    final static public by_class106 ax = by_class106.DARK_GREEN;
    final static public Vec3i ah = new Vec3i(11, 6, 11);
    final static public Vec3d aB = new Vec3d(5.0, 1.0, 9.0);
    final static public Vec3d af = new Vec3d(3.0, -1.0, 6.0);
    final static public Vec3d ao = new Vec3d(1.0, 1.0, 5.0);
    final static public Vec3d au = new Vec3d(-6.0, -1.0, 3.0);
    final static public Vec3d aM = new Vec3d(5.0, 1.0, 1.0);
    final static public Vec3d W = new Vec3d(-3.0, -1.0, -6.0);
    final static public Vec3d U = new Vec3d(9.0, 1.0, 5.0);
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
    final static int V = 205;
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
    final static HashSet<Item> ag = new HashSet<Item>(Arrays.asList(Items.GOLDEN_HOE, Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT, Items.GOLDEN_APPLE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_PICKAXE, Items.GOLDEN_SWORD, Items.GOLDEN_CARROT, Items.GOLDEN_HELMET, Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLD_INGOT, Items.GOLD_NUGGET, Item.getItemFromBlock(Blocks.GOLD_BLOCK), Item.getItemFromBlock(Blocks.GOLD_ORE)));
    final static public DataParameter<String> Q = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.STRING).getSerializer().createKey(122);
    final static public DataParameter<String> aK = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.STRING).getSerializer().createKey(123);
    final static public DataParameter<ItemStack> a0 = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(124);
    final static public DataParameter<Boolean> aC = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(125);
    final static public DataParameter<Boolean> aV = EntityDataManager.createKey(GoblinEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(126);
    public boolean aX = false;
    public float ac = 0.0f;
    public long av = -1L;
    public Vec3d al = Vec3d.ZERO;
    List<UUID> T = new ArrayList<UUID>();
    int aO = 31520;
    int aQ = -1;
    public int aR = -1;
    boolean aZ = false;
    BlockPos R = null;
    int Y = 0;
    int aa = 0;
    int aJ = 0;
    int an = -1;
    int am = 0;
    long ai = 0L;
    List<GoblinEntity> ab = new ArrayList<GoblinEntity>();
    int aY = -1;
    int az = -1;
    Action currentAction = null;
    public float ar = 1.0f;
    int Z = -1;
    boolean aD = true;
    boolean aF = true;
    boolean X = false;
    String aP = "";
    boolean ay = false;

    public GoblinEntity(World world) {
        super(world);
        this.setSize(GoblinEntity.aS.x, GoblinEntity.aS.y);
    }

    public GoblinEntity(World world, @Nonnull String string, int n) {
        this(world);
        this.entityDataManager.set(aK, string);
        this.entityDataManager.set(M, this.java_lang_String_a(new StringBuilder(), n));
    }

    public GoblinEntity(World world, boolean bl, float f, Vec3d vec3d) {
        this(world);
        if (!bl) {
            return;
        }
        this.entityDataManager.set(M, this.java_lang_String_b(new StringBuilder()));
        this.ac = f;
        this.al = vec3d;
        this.aX = true;
        this.c(vec3d);
        this.void_b(f);
        this.setCurrentAction(Action.SIT);
        this.void_a(true);
        this.setPosition(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public void void_g() {
        super.void_g();
        this.void_a((UUID)null);
        this.noClip = false;
        this.setNoGravity(false);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        eh_class250 eh_class2502 = eh_class250.values()[this.getRNG().nextInt(eh_class250.values().length)];
        this.entityDataManager.register(K, new BlockPos(eh_class2502.a()));
        this.entityDataManager.register(N, ax.name());
        this.entityDataManager.register(Q, "");
        this.entityDataManager.register(aK, "");
        this.entityDataManager.register(a0, ItemStack.EMPTY);
        this.entityDataManager.register(aC, false);
        this.entityDataManager.register(aV, false);
    }

    @Override
    protected void void_a() {
        GoblinRenderer.ResetColors();
    }

    @Override
    public void setDead() {
        super.setDead();
        this.void_a((UUID)null);
        if (this.world.isRemote) {
            return;
        }
        ItemStack itemStack = this.entityDataManager.get(a0);
        if (itemStack == ItemStack.EMPTY) {
            return;
        }
        EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemStack);
        this.world.spawnEntity(entityItem);
    }

    @Override
    public void a(String string, UUID uUID) {
        if ("take ur stuff back".equals(string)) {
            this.setCurrentAction(Action.START_THROWING);
        }
        if ("use her".equals(string)) {
            this.void_c(uUID);
        }
    }

    public void void_c(UUID uUID) {
        this.aY = 0;
        fh_class313.b();
        HandlePlayerMovement.a(false);
        this.void_e(uUID);
    }

    public void void_b(UUID uUID) {
        this.az = 0;
        fh_class313.b();
        HandlePlayerMovement.a(false);
        this.void_e(uUID);
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
    public float float_i() {
        return 0.1f;
    }

    @Override
    public void void_a(UUID uUID) {
        if (uUID == null) {
            this.entityDataManager.set(Q, "");
            return;
        }
        this.entityDataManager.set(Q, uUID.toString());
    }

    @Override
    @Nullable
    public UUID java_util_UUID_e() {
        String string = this.entityDataManager.get(Q);
        if ("".equals(string)) {
            return null;
        }
        try {
            return UUID.fromString(this.entityDataManager.get(Q));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public int int_c() {
        return this.aQ;
    }

    @Override
    public void void_b(int n) {
        this.aQ = n;
    }

    protected String java_lang_String_b(StringBuilder stringBuilder) {
        GoblinEntity.void_a(stringBuilder, 3);
        GoblinEntity.void_a(stringBuilder, 2);
        GoblinEntity.void_a(stringBuilder, 2);
        GoblinEntity.c(stringBuilder, 7);
        GoblinEntity.c(stringBuilder, 7);
        GoblinEntity.void_a(stringBuilder, 5);
        GoblinEntity.void_a(stringBuilder, g5_class349.values().length - 1);
        GoblinEntity.void_a(stringBuilder, by_class106.values().length - 1);
        GoblinEntity.void_a(stringBuilder, eh_class250.values().length - 1);
        GoblinEntity.c(stringBuilder, 1);
        return stringBuilder.toString();
    }

    @Override
    protected String a(StringBuilder stringBuilder) {
        GoblinEntity.void_a(stringBuilder, 3);
        GoblinEntity.void_a(stringBuilder, 2);
        GoblinEntity.void_a(stringBuilder, 2);
        GoblinEntity.void_a(stringBuilder, 8);
        GoblinEntity.void_a(stringBuilder, 8);
        GoblinEntity.void_a(stringBuilder, 5);
        GoblinEntity.void_a(stringBuilder, g5_class349.values().length - 1);
        GoblinEntity.void_a(stringBuilder, by_class106.values().length - 1);
        GoblinEntity.void_a(stringBuilder, eh_class250.values().length - 1);
        GoblinEntity.c(stringBuilder, 0);
        return stringBuilder.toString();
    }

    @Override
    public ArrayList<Integer> D() {
        return new ArrayList<Integer>(){
            {
                this.add(4);
                this.add(3);
                this.add(3);
                this.add(16);
                this.add(16);
                this.add(6);
                this.add(g5_class349.values().length);
                this.add(by_class106.values().length);
                this.add(eh_class250.values().length);
            }
        };
    }

    @Override
    public List<Integer> u() {
        return Collections.singletonList(2);
    }

    @Override
    public Point2D g(int n) {
        switch (n) {
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
        }
        return Point2D.ZERO;
    }

    @Override
    public void void_a(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int n : list) {
            GoblinEntity.c(stringBuilder, n);
        }
        GoblinEntity.c(stringBuilder, Integer.parseInt(GoblinEntity.java_lang_String_arr_a(this)[9]));
        this.entityDataManager.set(M, stringBuilder.toString());
        if (Main.proxy instanceof ClientProxy) {
            GoblinRenderer.ResetColors();
        }
    }

    void void_i() {
        if (this.d == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : this.d) {
            int n = (Integer)((Map.Entry)entry.getValue()).getValue();
            GoblinEntity.c(stringBuilder, n);
        }
        GoblinEntity.c(stringBuilder, Integer.parseInt(GoblinEntity.java_lang_String_arr_a(this)[9]));
        this.entityDataManager.set(M, stringBuilder.toString());
        GoblinRenderer.ResetColors();
    }

    protected String java_lang_String_a(StringBuilder stringBuilder, int n) {
        GoblinEntity.void_a(stringBuilder, 3);
        GoblinEntity.void_a(stringBuilder, 2);
        GoblinEntity.void_a(stringBuilder, 2);
        GoblinEntity.void_a(stringBuilder, 7);
        GoblinEntity.void_a(stringBuilder, 7);
        GoblinEntity.void_a(stringBuilder, 5);
        GoblinEntity.void_a(stringBuilder, g5_class349.values().length - 1);
        GoblinEntity.c(stringBuilder, n);
        GoblinEntity.void_a(stringBuilder, eh_class250.values().length - 1);
        GoblinEntity.c(stringBuilder, 0);
        return stringBuilder.toString();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setString("bodyColor", (String)this.entityDataManager.get(N));
        nBTTagCompound.setInteger("eyeColorX", ((BlockPos)this.entityDataManager.get(K)).getX());
        nBTTagCompound.setInteger("eyeColorY", ((BlockPos)this.entityDataManager.get(K)).getY());
        nBTTagCompound.setInteger("eyeColorZ", ((BlockPos)this.entityDataManager.get(K)).getZ());
        nBTTagCompound.setString("model", (String)this.entityDataManager.get(M));
        nBTTagCompound.setString("girlID", (String)this.entityDataManager.get(GIRL_ID));
        nBTTagCompound.setString("queen", this.entityDataManager.get(aK));
        nBTTagCompound.setBoolean("isQueen", this.aX);
        nBTTagCompound.setBoolean("isTamed", this.entityDataManager.get(aC));
        nBTTagCompound.setInteger("robTicks", this.aO);
        if (!this.aX) {
            return;
        }
        nBTTagCompound.setBoolean("preggo", this.entityDataManager.get(aV));
        nBTTagCompound.setFloat("throneRot", this.ac);
        nBTTagCompound.setDouble("thronePosX", this.al.x);
        nBTTagCompound.setDouble("thronePosY", this.al.y);
        nBTTagCompound.setDouble("thronePosZ", this.al.z);
        nBTTagCompound.setLong("impregnationTick", this.av);
        for (int i = 0; i < this.T.size(); ++i) {
            nBTTagCompound.setString("guard" + i, this.T.get(i).toString());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.aX = nBTTagCompound.getBoolean("isQueen");
        this.entityDataManager.set(M, nBTTagCompound.getString("model"));
        this.entityDataManager.set(N, nBTTagCompound.getString("bodyColor"));
        String[] stringArray = GoblinEntity.java_lang_String_arr_a(this);
        if (Integer.parseInt(stringArray[3]) > 7 || Integer.parseInt(stringArray[4]) > 7) {
            this.entityDataManager.set(M, this.java_lang_String_a(new StringBuilder(), this.int_k()));
            Main.LOGGER.log(Level.INFO, "updated an old Goblin");
        }
        this.entityDataManager.set(K, new BlockPos(nBTTagCompound.getInteger("eyeColorX"), nBTTagCompound.getInteger("eyeColorY"), nBTTagCompound.getInteger("eyeColorZ")));
        this.entityDataManager.set(GIRL_ID, nBTTagCompound.getString("girlID"));
        this.entityDataManager.set(aK, nBTTagCompound.getString("queen"));
        this.entityDataManager.set(aC, nBTTagCompound.getBoolean("isTamed"));
        this.aO = nBTTagCompound.getInteger("robTicks");
        if (!this.aX) {
            return;
        }
        this.ac = nBTTagCompound.getFloat("throneRot");
        this.al = new Vec3d(nBTTagCompound.getDouble("thronePosX"), nBTTagCompound.getDouble("thronePosY"), nBTTagCompound.getDouble("thronePosZ"));
        int n = 0;
        while (!"".equals(nBTTagCompound.getString("guard" + n))) {
            this.T.add(UUID.fromString(nBTTagCompound.getString("guard" + n)));
            ++n;
        }
        this.entityDataManager.set(aV, nBTTagCompound.getBoolean("preggo"));
        this.av = nBTTagCompound.getLong("impregnationTick");
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (this.world.isRemote) {
            return true;
        }
        if (this.aX) {
            return true;
        }
        if (this.currentAction() == Action.RUN) {
            if ((double)this.getDistance(entityPlayer) > 3.5) {
                entityPlayer.sendStatusMessage(new TextComponentString("get a bit closer..."), true);
            } else {
                this.c(entityPlayer.getPositionVector());
                this.void_b(entityPlayer.rotationYaw);
                this.setCurrentAction(Action.CATCH);
                this.entityDataManager.set(h, "bj");
                this.void_a(entityPlayer.getPersistentID());
                this.void_e(entityPlayer.getPersistentID());
                this.getNavigator().clearPath();
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            return true;
        }
        if (GoblinEntity.d_19(entityPlayer.getPersistentID())) {
            entityPlayer.sendStatusMessage(new TextComponentString("you are already carrying a Goblin"), true);
        } else {
            this.void_a(entityPlayer.getPersistentID());
            this.setCurrentAction(Action.PICK_UP);
            this.aQ = 45;
            this.void_a(false);
            this.entityDataManager.set(aC, true);
            this.getNavigator().clearPath();
        }
        return true;
    }

    public static boolean d_19(UUID uUID) {
        if (uUID == null) {
            return false;
        }
        for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
            UUID uUID2;
            if (!(em_class2582 instanceof ai_class30) || em_class2582.world.isRemote || em_class2582.isDead || !uUID.equals(uUID2 = ((ai_class30) ((Object) em_class2582)).java_util_UUID_e()))
                continue;
            return true;
        }
        return false;
    }

    @Override
    protected void initEntityAI() {
        this.o = new df_class178(this, EntityPlayer.class, 2.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new AutoCloseDoorGoal(this));
        this.tasks.addTask(5, this.o);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.void_f();
        GoblinEntity.void_a(this);
        this.void_m();
        this.void_B();
        this.void_J();
        this.void_E();
        this.void_t();
        this.void_w();
        this.void_b();
        this.void_d();
        this.void_h();
        this.void_o();
        this.u_();
        this.void_n();
    }

    @Override
    public boolean canBeCollidedWith() {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.THROWN) {
            return false;
        }
        if (fp_class3242 == Action.RUN) {
            return super.canBeCollidedWith();
        }
        if (fp_class3242 == Action.AWAIT_PICK_UP) {
            return super.canBeCollidedWith();
        }
        if (this.java_util_UUID_e() != null) {
            return false;
        }
        if (fp_class3242 != Action.NULL) {
            return false;
        }
        return super.canBeCollidedWith();
    }

    void void_b(EntityPlayer entityPlayer) {
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(entityPlayer.getPersistentID());
        Vec3d vec3d = new Vec3d(entityPlayer.posX, entityPlayer.posY + (double)(ei_class2512 == null ? entityPlayer.eyeHeight : ei_class2512.getEyeHeight()), entityPlayer.posZ);
        Vec3d vec3d2 = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        double d = vec3d2.distanceTo(vec3d);
        double d2 = vec3d.y - vec3d2.y;
        this.rotationPitch = (float)(-(Math.sin(d2 / d) * 57.29577951308232));
    }

    void void_n() {
        if (!this.entityDataManager.get(aC).booleanValue()) {
            return;
        }
        if (this.getID() != null) {
            return;
        }
        if (this.currentAction() != Action.NULL) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
        if (entityPlayer != null && entityPlayer.getDistance(this) < 2.0f) {
            this.void_b(entityPlayer);
            this.getNavigator().clearPath();
            return;
        }
        if (this.R == null || this.getDistance(this.R.getX(), this.R.getY(), this.R.getZ()) > this.double_l() || this.Y > 100) {
            int n = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
            int n2 = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
            int n3 = WorldUtils.getSurfaceHeight(this.world, this.getPosition().getX() + n, this.getPosition().getZ() + n2);
            this.R = new BlockPos(this.getPosition().getX() + n, n3, this.getPosition().getZ() + n2);
            this.Y = 0;
        }
        if (Math.sqrt(this.R.distanceSq(this.getPosition())) > 2.0) {
            this.getNavigator().tryMoveToXYZ(this.R.getX(), this.R.getY(), this.R.getZ(), 0.3f);
            this.void_k();
        } else {
            ++this.Y;
        }
    }

    double double_l() {
        return Math.sqrt(800.0);
    }

    void u_() {
        if (this.currentAction() != Action.STAND_UP) {
            return;
        }
        if (++this.aa < 37) {
            return;
        }
        this.aa = 0;
        this.setCurrentAction(Action.NULL);
    }

    @Override
    public void void_a(int n) {
        this.aJ = n;
    }

    @Override
    public int int_d() {
        return this.aJ;
    }

    void void_o() {
        if (this.currentAction() != Action.THROWN) {
            return;
        }
        if (!this.onGround) {
            return;
        }
        int n = this.int_d() + 1;
        this.void_a(n);
        if (n < 30) {
            return;
        }
        this.void_a(0);
        this.setCurrentAction(Action.STAND_UP);
    }

    void void_h() {
        if (!this.aX) {
            return;
        }
        if (!this.entityDataManager.get(aV).booleanValue()) {
            return;
        }
        if (this.av + 8400L < this.world.getTotalWorldTime()) {
            this.entityDataManager.set(aV, false);
        }
    }

    void void_d() {
        if (!this.aX) {
            return;
        }
        if (this.ab.isEmpty()) {
            return;
        }
        boolean bl = false;
        for (GoblinEntity e3_class2192 : this.ab) {
            if (!e3_class2192.getDataManager().get(aC).booleanValue()) continue;
            bl = true;
        }
        if (!bl) {
            return;
        }
        this.h("Farewell my knight. You are welcome once I am breedable again.");
        for (GoblinEntity e3_class2192 : this.ab) {
            if (e3_class2192.getDataManager().get(aC).booleanValue()) continue;
            e3_class2192.setCurrentAction(Action.VANISH);
        }
        this.ab.clear();
        this.void_e((UUID)null);
    }

    void void_b() {
        if (!this.aX) {
            return;
        }
        if (this.Z == -1) {
            return;
        }
        if (++this.Z < 100) {
            return;
        }
        this.Z = -1;
        UUID uUID = this.getID();
        if (uUID == null) {
            this.void_r();
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            this.void_r();
            return;
        }
        this.void_e((UUID)null);
        for (GoblinEntity e3_class2192 : this.ab) {
            e3_class2192.void_e((UUID)null);
        }
        List<GoblinEntity> list = this.I();
        float f = this.ac + 180.0f;
        Vec3d vec3d = this.al.add(GoblinEntity.b(aT, f));
        Vec3d vec3d2 = this.al.add(GoblinEntity.b(ap, f));
        Vec3d vec3d3 = this.al.add(GoblinEntity.b(as, f));
        GoblinEntity e3_class2193 = (GoblinEntity)list.get(0);
        GoblinEntity e3_class2194 = (GoblinEntity)list.get(1);
        e3_class2193.c(vec3d);
        e3_class2194.c(vec3d2);
        e3_class2193.void_b(0.0f);
        e3_class2194.void_b(0.0f);
        e3_class2193.void_a(true);
        e3_class2194.void_a(true);
        e3_class2193.setCurrentAction(Action.AWAIT_PICK_UP);
        e3_class2194.setCurrentAction(Action.AWAIT_PICK_UP);
        e3_class2193.setNoGravity(false);
        e3_class2194.setNoGravity(false);
        entityPlayer.setNoGravity(false);
        e3_class2193.noClip = false;
        e3_class2194.noClip = false;
        entityPlayer.noClip = false;
        entityPlayer.rotationYaw = f;
        entityPlayer.rotationPitch = 30.0f;
        entityPlayer.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
        this.h("Thanks to you, my clan is soon going to get a few new members! In return I will bear of one of my guards to serve as your personal Onahole. Choose wisely~");
    }

    void void_w() {
        if (!this.aX) {
            return;
        }
        if (this.an == -1) {
            return;
        }
        if (++this.an < 205) {
            return;
        }
        this.an = -1;
        UUID uUID = this.getID();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = GoblinEntity.b(new Vec3d(0.0, 0.15625 - (double)entityPlayer.getEyeHeight(), -0.8859375), this.ac - 180.0f);
        vec3d = vec3d.add(this.net_minecraft_util_math_Vec3d_o());
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    public static Vec3d b(Vec3d vec3d, float f) {
        return GoblinEntity.a(vec3d, 0.0f, f);
    }

    public static Vec3d a(Vec3d vec3d, float f, float f2) {
        Vec3d vec3d2 = new Vec3d(vec3d.x, vec3d.y * Math.cos((double)f * (Math.PI / 180)) - vec3d.z * Math.sin((double)f * (Math.PI / 180)), vec3d.y * Math.sin((double)f * (Math.PI / 180)) + vec3d.z * Math.cos((double)f * (Math.PI / 180)));
        Vec3d vec3d3 = new Vec3d(-Math.sin((double)(f2 + 90.0f) * (Math.PI / 180)) * vec3d2.x - Math.sin((double)f2 * (Math.PI / 180)) * vec3d2.z, vec3d2.y, Math.cos((double)(f2 + 90.0f) * (Math.PI / 180)) * vec3d2.x + Math.cos((double)f2 * (Math.PI / 180)) * vec3d2.z);
        return vec3d3;
    }

    void void_t() {
        GoblinEntity e3_class2192;
        Vec3d vec3d;
        if (!this.aX) {
            return;
        }
        if (this.currentAction() != Action.JUMP_0) {
            return;
        }
        if (++this.am < 26) {
            return;
        }
        this.am = 0;
        switch ((int)this.ac) {
            case 90: {
                vec3d = this.al.add(au);
                break;
            }
            case 180: {
                vec3d = this.al.add(W);
                break;
            }
            case -90: {
                vec3d = this.al.add(at);
                break;
            }
            default: {
                vec3d = this.al.add(af);
            }
        }
        UUID uUID = this.getID();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        this.c(vec3d);
        this.void_b(this.ac);
        this.setCurrentAction(Action.BREEDING_INTRO_0);
        this.noClip = true;
        this.setNoGravity(true);
        Vec3d vec3d2 = GoblinEntity.b(new Vec3d(0.0, 0.44375 - (double)entityPlayer.eyeHeight, -0.7875), this.ac - 180.0f);
        entityPlayer.noClip = true;
        entityPlayer.setNoGravity(true);
        entityPlayer.setPositionAndUpdate(vec3d2.x + vec3d.x, vec3d2.y + vec3d.y, vec3d2.z + vec3d.z);
        List<GoblinEntity> list = this.I();
        if (list.size() >= 1) {
            e3_class2192 = list.get(0);
            e3_class2192.c(vec3d);
            e3_class2192.void_b(this.ac);
            e3_class2192.setCurrentAction(Action.BREEDING_INTRO_1);
            e3_class2192.noClip = true;
            e3_class2192.setNoGravity(true);
        }
        if (list.size() >= 2) {
            e3_class2192 = list.get(1);
            e3_class2192.c(vec3d);
            e3_class2192.void_b(this.ac);
            e3_class2192.setCurrentAction(Action.BREEDING_INTRO_2);
            e3_class2192.noClip = true;
            e3_class2192.setNoGravity(true);
        }
        this.an = 0;
    }

    AxisAlignedBB a(Vec3d vec3d, Vec3d vec3d2) {
        return new AxisAlignedBB(vec3d.x, vec3d.y, vec3d.z, vec3d2.x, vec3d2.y, vec3d2.z);
    }

    void void_E() {
        if (!this.aX) {
            return;
        }
        if (this.getID() != null) {
            return;
        }
        Vec3d vec3d = null;
        switch ((int)this.ac) {
            case 0: {
                vec3d = aM;
                break;
            }
            case 90: {
                vec3d = U;
                break;
            }
            case 180: {
                vec3d = aB;
                break;
            }
            case -90: {
                vec3d = ao;
            }
        }
        if (vec3d == null) {
            return;
        }
        Vec3d vec3d2 = this.al.subtract(0.5, 0.0, 0.5).subtract(vec3d);
        AxisAlignedBB axisAlignedBB = this.a(vec3d2, vec3d2.add(ah.getX(), ah.getY(), ah.getZ()));
        List<EntityPlayer> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB);
        if (list.isEmpty()) {
            return;
        }
        EntityPlayer entityPlayer = list.get(0);
        if (!entityPlayer.onGround) {
            return;
        }
        if (this.entityDataManager.get(aV).booleanValue()) {
            if (this.ai + 1200L < this.world.getTotalWorldTime()) {
                entityPlayer.sendStatusMessage(new TextComponentString("The Queen is still pregnant - so no breeding for you uwu"), true);
                this.ai = this.world.getTotalWorldTime();
            }
            return;
        }
        UUID uUID = entityPlayer.getPersistentID();
        Vec3d vec3d3 = entityPlayer.getPositionVector();
        float f = entityPlayer.rotationYaw + 180.0f;
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        this.void_e(uUID);
        this.setCurrentAction(Action.JUMP_0);
        this.c(vec3d3);
        this.void_b(f);
        this.void_a(true);
        List<GoblinEntity> list2 = this.I();
        if (list2.size() > 0) {
            GoblinEntity e3_class2192 = list2.get(0);
            e3_class2192.void_e(uUID);
            e3_class2192.setCurrentAction(Action.JUMP_1);
            e3_class2192.c(vec3d3);
            e3_class2192.void_b(f);
            e3_class2192.void_a(true);
            if (list2.size() > 1) {
                GoblinEntity e3_class2193 = list2.get(1);
                e3_class2193.void_e(uUID);
                e3_class2193.setCurrentAction(Action.JUMP_2);
                e3_class2193.c(vec3d3);
                e3_class2193.void_b(f);
                e3_class2193.void_a(true);
            }
        }
    }

    List<GoblinEntity> I() {
        //GoblinEntity e3_class21922;
        if (this.ab.size() > 1) {
            return this.ab;
        }
        for (GoblinEntity e3_class21922 : this.ab) {
            this.world.removeEntity(e3_class21922);
        }
        this.ab.clear();
        GoblinEntity e3_class2193 = new GoblinEntity(this.world, this.girlID().toString(), this.int_k());
        e3_class2193.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(e3_class2193);
        this.ab.add(e3_class2193);
        GoblinEntity e3_class21922 = new GoblinEntity(this.world, this.girlID().toString(), this.int_k());
        e3_class21922.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(e3_class21922);
        this.ab.add(e3_class21922);
        return this.ab;
    }

    void void_f() {
        if (this.aZ) {
            return;
        }
        this.noClip = false;
        this.setNoGravity(false);
        if (!(this.aX || this.entityDataManager.get(aC).booleanValue() || this.entityDataManager.get(aK).equals("") || this.currentAction() != Action.NULL)) {
            this.world.removeEntity(this);
        }
        this.aZ = true;
    }

    void void_e() {
        GoblinEntity e3_class2192 = this;
        int n = e3_class2192.int_a();
        if (n == -1) {
            return;
        }
        e3_class2192.void_c(++n);
        if (n == 15) {
            Vec3d vec3d = GoblinEntity.b(this);
            float f = GoblinEntity.d(this);
            float f2 = GoblinEntity.c(this);
            this.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = GoblinEntity.a(new Vec3d(0.0, 0.0, 1.5), f, f2);
            this.motionX = vec3d2.x;
            this.motionY = vec3d2.y;
            this.motionZ = vec3d2.z;
            if (!this.world.isRemote) {
                this.void_b(f2);
            }
        }
        this.noClip = false;
        this.setNoGravity(false);
        if (n == 39) {
            this.void_c(-1);
            this.setCurrentAction(Action.THROWN);
            this.void_e((UUID)null);
            this.void_a((UUID)null);
        }
    }

    public static Vec3d b(GirlEntity em_class2582) {
        ai_class30 ai_class302 = (ai_class30)((Object)em_class2582);
        UUID uUID = ai_class302.java_util_UUID_e();
        if (uUID == null) {
            return em_class2582.getPositionVector();
        }
        EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return em_class2582.getPositionVector();
        }
        return entityPlayer.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0).add(GoblinEntity.a(new Vec3d(0.4f, 0.0, 0.0), GoblinEntity.d(em_class2582), GoblinEntity.c(em_class2582)));
    }

    public static float c(GirlEntity em_class2582) {
        ai_class30 ai_class302 = (ai_class30)((Object)em_class2582);
        UUID uUID = ai_class302.java_util_UUID_e();
        if (uUID == null) {
            return 0.0f;
        }
        EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return 0.0f;
        }
        return entityPlayer.rotationYawHead;
    }

    public static float d(GirlEntity em_class2582) {
        ai_class30 ai_class302 = (ai_class30)((Object)em_class2582);
        UUID uUID = ai_class302.java_util_UUID_e();
        if (uUID == null) {
            return 0.0f;
        }
        EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return 0.0f;
        }
        return entityPlayer.rotationPitch;
    }

    void void_J() {
        boolean bl;
        if (!this.onGround) {
            return;
        }
        if (this.currentAction() != Action.RUN) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 100.0);
        if (entityPlayer == null) {
            return;
        }
        double d = 20.0;
        do {
            if (d <= 0.0) {
                return;
            }
            Vec3d vec3d = this.getPositionVector().subtract(entityPlayer.getPositionVector());
            Vec3d vec3d2 = new Vec3d(Math.abs(vec3d.x), Math.abs(vec3d.y), Math.abs(vec3d.z));
            double d2 = vec3d2.x / (vec3d2.x + vec3d2.z);
            double d3 = vec3d2.z / (vec3d2.x + vec3d2.z);
            Vec3d vec3d3 = this.getPositionVector().add(new Vec3d((double)(vec3d.x > 0.0 ? 1 : -1) * d2 * d, 0.0, (double)(vec3d.z > 0.0 ? 1 : -1) * d3 * d));
            PathNavigate pathNavigate = this.getNavigator();
            pathNavigate.clearPath();
            bl = pathNavigate.tryMoveToXYZ(vec3d3.x, vec3d3.y, vec3d3.z, 0.825f);
            d -= 1.0;
        } while (!bl);
    }

    @Override
    protected void jump() {
        if (this.currentAction() == Action.RUN && !this.boolean_j()) {
            return;
        }
        super.jump();
    }

    boolean boolean_j() {
        PathNavigate pathNavigate = this.getNavigator();
        Path path = pathNavigate.getPath();
        if (path == null) {
            return true;
        }
        int n = path.getCurrentPathIndex();
        int n2 = path.getCurrentPathLength();
        if (n2 == n || n2 - 1 == n) {
            return true;
        }
        PathPoint pathPoint = path.getPathPointFromIndex(n);
        PathPoint pathPoint2 = path.getPathPointFromIndex(n + 1);
        return pathPoint2.y - pathPoint.y == 1;
    }

    void void_B() {
        if (!this.aX) {
            return;
        }
        if (this.entityDataManager.get(aC).booleanValue()) {
            return;
        }
        if (this.entityDataManager.get(aV).booleanValue()) {
            return;
        }
        if (this.currentAction() != Action.SIT) {
            return;
        }
        if (++this.aO < 32000) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 3000.0);
        if (entityPlayer == null) {
            return;
        }
        if (!entityPlayer.onGround) {
            return;
        }
        if (entityPlayer.isAirBorne) {
            return;
        }
        Integer n = this.java_lang_Integer_c(entityPlayer);
        if (n == null) {
            return;
        }
        Vec3d vec3d = entityPlayer.getPositionVector();
        Vec3d vec3d2 = this.getPositionVector();
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        double d = Math.sqrt(vec3d3.x * vec3d3.x + vec3d3.z * vec3d3.z);
        if (d > 100.0) {
            return;
        }
        ItemStack itemStack = entityPlayer.inventory.getStackInSlot(n).copy();
        GoblinEntity e3_class2192 = new GoblinEntity(this.world, this.girlID().toString(), this.int_k());
        Vec3d vec3d4 = GoblinEntity.b(new Vec3d(0.0, 0.0, -0.2f), entityPlayer.rotationYawHead);
        e3_class2192.setPosition(entityPlayer.posX + vec3d4.x, entityPlayer.posY, entityPlayer.posZ + vec3d4.z);
        e3_class2192.setCurrentAction(Action.RUN);
        this.world.spawnEntity(e3_class2192);
        e3_class2192.entityDataManager.set(a0, itemStack);
        entityPlayer.sendMessage(new TextComponentString(String.format("<%s> I got your %s hehe~", e3_class2192.getGirlName(), itemStack.getDisplayName())));
        entityPlayer.inventory.removeStackFromSlot(n);
        this.aO = 0;
    }

    int int_k() {
        return Integer.parseInt(GoblinEntity.java_lang_String_arr_a(this)[7]);
    }

    @Nullable
    Integer java_lang_Integer_c(EntityPlayer entityPlayer) {
        NonNullList<ItemStack> nonNullList = entityPlayer.inventory.mainInventory;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < nonNullList.size(); ++i) {
            ItemStack itemStack = nonNullList.get(i);
            if (itemStack == ItemStack.EMPTY || !ag.contains(itemStack.getItem())) continue;
            arrayList.add(i);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (Integer)arrayList.get(this.getRNG().nextInt(arrayList.size()));
    }

    void void_m() {
        if (!this.aX) {
            return;
        }
        if (this.getID() != null) {
            return;
        }
        this.c(this.al);
        this.void_b(this.ac);
        this.void_a(true);
        this.setNoGravity(true);
        this.setCurrentAction(Action.SIT);
    }

    @Override
    public void onUpdate() {
        this.void_i();
        GoblinEntity.e(this);
        this.void_e();
        if (this.java_util_UUID_e() != null) {
            this.inPortal = false;
        }
        super.onUpdate();
        this.void_y();
        this.void_H();
        this.void_F();
        if (this.world.isRemote) {
            this.void_v();
            this.void_A();
            if (this.java_util_UUID_e() != null) {
                this.noClip = true;
            }
        }
    }

    @Override
    public Action GoblinAction() {
        return this.currentAction;
    }

    @Override
    public void void_a(Action action) {
        this.currentAction = action;
    }

    @Override
    public void void_c(int n) {
        this.aR = n;
    }

    @Override
    public int int_a() {
        return this.aR;
    }

    public static void e(GirlEntity girlEntity) {
        Action action = girlEntity.currentAction();
        ai_class30 ai_class302 = (ai_class30)((Object)girlEntity);
        if (ai_class302.GoblinAction() != Action.START_THROWING && action == Action.START_THROWING) {
            ai_class302.void_c(0);
        }
        ai_class302.void_a(action);
    }

    @Override
    public void setFire(int n) {
        if (this.java_util_UUID_e() == null) {
            super.setFire(n);
        }
    }

    void void_F() {
        if (this.currentAction() != Action.VANISH) {
            return;
        }
        this.ar -= 0.05f;
        if (this.ar > 0.0f) {
            return;
        }
        this.world.removeEntity(this);
    }

    void void_H() {
        if (this.entityDataManager.get(aC).booleanValue()) {
            return;
        }
        if (this.currentAction() != Action.THROWN) {
            return;
        }
        if (!this.onGround && !this.isInWater()) {
            return;
        }
        this.ar = (float)((double)this.ar - 0.05);
        if (this.ar > 0.0f) {
            return;
        }
        if (this.world.isRemote) {
            return;
        }
        this.setCurrentAction(Action.NULL);
        this.void_e((UUID)null);
        this.void_a((UUID)null);
        this.world.removeEntity(this);
    }

    @SideOnly(value=Side.CLIENT)
    void void_v() {
        if (this.aY == -1) {
            return;
        }
        if (++this.aY != 15) {
            return;
        }
        this.aY = -1;
        this.setCurrentAction(Action.PAIZURI_START);
        Minecraft.getMinecraft().player.closeScreen();
    }

    @SideOnly(value=Side.CLIENT)
    void void_A() {
        if (this.az == -1) {
            return;
        }
        if (++this.az != 15) {
            return;
        }
        this.az = -1;
        this.setCurrentAction(Action.NELSON_INTRO);
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.player.closeScreen();
        minecraft.gameSettings.thirdPersonView = 2;
    }

    @Override
    public void setCurrentAction(Action action) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.PAIZURI_CUM && (action == Action.PAIZURI_SLOW || action == Action.PAIZURI_FAST)) {
            return;
        }
        if (fp_class3243 == Action.NELSON_CUM && (action == Action.NELSON_SLOW || action == Action.NELSON_FAST)) {
            return;
        }
        if (fp_class3243 == Action.BREEDING_CUM_0 && (action == Action.BREEDING_SLOW_0 || action == Action.BREEDING_FAST_0)) {
            return;
        }
        if (action == Action.START_THROWING && !this.world.isRemote) {
            this.void_e(this.java_util_UUID_e());
            this.L_();
        }
        if (action == Action.PAIZURI_START && !this.world.isRemote) {
            this.void_z();
        }
        if (action == Action.NELSON_INTRO && !this.world.isRemote) {
            this.void_q();
        }
        if (this.currentAction() == Action.PAIZURI_CUM && action == Action.NULL && !this.world.isRemote) {
            this.D_();
        }
        if (action == Action.BREEDING_CUM_0) {
            this.entityDataManager.set(aV, true);
            this.av = this.world.getTotalWorldTime();
            this.ai = this.world.getTotalWorldTime();
        }
        if (action == Action.BREEDING_CUM_0) {
            this.Z = 0;
        }
        if (action == Action.NELSON_CUM) {
            this.entityDataManager.set(aV, true);
        }
        if (fp_class3243 == Action.NELSON_CUM && action != Action.NELSON_CUM) {
            this.entityDataManager.set(aV, false);
        }
        super.setCurrentAction(action);
    }

    void D_() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer != null) {
            ResetGirl.a_inner422.a((EntityPlayerMP)entityPlayer);
        }
        this.void_e((UUID)null);
        this.void_a(false);
        this.noClip = false;
        this.setNoGravity(false);
        this.entityDataManager.set(a0, ItemStack.EMPTY);
        if (!this.entityDataManager.get(aC).booleanValue()) {
            this.setPositionAndUpdate(this.homeCoords.x, this.homeCoords.y, this.homeCoords.z);
            this.world.removeEntity(this);
        }
    }

    void void_q() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer == null) {
            return;
        }
        this.void_a((UUID)null);
        this.c(entityPlayer.getPositionVector());
        this.void_b(entityPlayer.rotationYaw);
        this.void_a(true);
        this.noClip = true;
        this.setNoGravity(true);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        this.void_e(entityPlayer.getPersistentID());
    }

    void void_z() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer == null) {
            return;
        }
        this.void_a((UUID)null);
        this.c(entityPlayer.getPositionVector());
        this.void_b(entityPlayer.rotationYaw + 180.0f);
        this.void_a(true);
        this.noClip = true;
        this.setNoGravity(true);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        this.void_e(entityPlayer.getPersistentID());
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY - 0.5, entityPlayer.posZ);
        entityPlayer.rotationPitch = 70.0f;
        entityPlayer.prevRotationPitch = 70.0f;
    }

    void L_() {
        ItemStack itemStack = this.entityDataManager.get(a0);
        if (itemStack == ItemStack.EMPTY) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer == null) {
            return;
        }
        entityPlayer.inventory.addItemStackToInventory(itemStack.copy());
        this.entityDataManager.set(a0, ItemStack.EMPTY);
    }

    public static void void_a(GirlEntity em_class2582) {
        if (em_class2582.currentAction() != Action.PICK_UP) {
            return;
        }
        ai_class30 ai_class302 = (ai_class30)((Object)em_class2582);
        UUID uUID = ai_class302.java_util_UUID_e();
        if (uUID == null) {
            ai_class302.void_b(-1);
            em_class2582.setCurrentAction(Action.NULL);
            ai_class302.void_a((UUID)null);
            return;
        }
        EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            ai_class302.void_b(-1);
            em_class2582.setCurrentAction(Action.NULL);
            ai_class302.void_a((UUID)null);
            return;
        }
        em_class2582.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        if (em_class2582.getPositionVector().distanceTo(entityPlayer.getPositionVector()) > 10.0) {
            ai_class302.void_b(-1);
            em_class2582.setCurrentAction(Action.NULL);
            ai_class302.void_a((UUID)null);
            return;
        }
        int n = ai_class302.int_c() - 1;
        ai_class302.void_b(n);
        if (n != 0) {
            return;
        }
        em_class2582.setCurrentAction(Action.SHOULDER_IDLE);
        em_class2582.noClip = true;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean boolean_t() {
        if (this.currentAction() != Action.NULL) {
            return false;
        }
        if (this.java_util_UUID_e() != null) {
            return false;
        }
        if (!this.entityDataManager.get(aC).booleanValue() && !Minecraft.getMinecraft().player.canEntityBeSeen(this)) {
            return false;
        }
        return this.java_util_UUID_e() == null;
    }

    void void_y() {
        if (this.currentAction() != Action.SHOULDER_IDLE) {
            return;
        }
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        this.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        this.noClip = true;
        this.setNoGravity(true);
    }

    @Override
    protected Action FastSexAction(Action action) {
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
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
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
                for (GoblinEntity e3_class2192 : this.ab) {
                    e3_class2192.CumAction(action);
                }
                return Action.BREEDING_CUM_0;
            }
        }
        return null;
    }

    public boolean boolean_C() {
        Block block = this.world.getBlockState(this.getPosition().add(0, 1, 0)).getBlock();
        return !block.isPassable(this.world, this.getPosition().add(0, 1, 0));
    }

    @Override
    public void fall(float f, float f2) {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.THROWN || fp_class3242 == Action.START_THROWING) {
            return;
        }
        super.fall(f, f2);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        if (this.actionController == null) {
            this.void_p();
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.goblin.blink", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, animationEvent);
                    break;
                }
                double d = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                if (!((Boolean)this.entityDataManager.get(G)).booleanValue() && d > 0.0) {
                    if (this.onGround && Math.abs(Math.abs(this.prevPosY) - Math.abs(this.posY)) < (double)0.1f) {
                        if (d > (double)0.2f) {
                            this.createAnimation("animation.goblin.walk", true, animationEvent);
                        } else {
                            this.createAnimation("animation.goblin.walk", true, animationEvent);
                        }
                        this.rotationYaw = this.rotationYawHead;
                        break;
                    }
                    this.createAnimation("animation.goblin.fly", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.goblin.idle", true, animationEvent);
                break;
            }
            case "action": {
                Minecraft minecraft = Minecraft.getMinecraft();
                String string = minecraft.player.getPersistentID().equals(this.java_util_UUID_e()) && minecraft.gameSettings.thirdPersonView == 0 ? "1" : "3";
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.goblin.null", true, animationEvent);
                        break block5;
                    }
                    case SHOULDER_IDLE: {
                        this.createAnimation("animation.goblin.shoulder_idle", true, animationEvent);
                        break block5;
                    }
                    case PICK_UP: {
                        this.createAnimation(String.format("animation.goblin.pick_up_%sperson", string), true, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.goblin.sit", true, animationEvent);
                        break block5;
                    }
                    case RUN: {
                        if (this.onGround) {
                            this.createAnimation("animation.goblin.running", true, animationEvent);
                            break block5;
                        }
                        this.createAnimation("animation.goblin.fly", true, animationEvent);
                        break block5;
                    }
                    case CATCH: {
                        this.createAnimation(String.format("animation.goblin.catch_%sperson", string), true, animationEvent);
                        break block5;
                    }
                    case CATCH_BJ: {
                        this.createAnimation(String.format("animation.goblin.catch_%spersonBj", string), true, animationEvent);
                        break block5;
                    }
                    case CATCH_BJ_IDLE: {
                        this.createAnimation(String.format("animation.goblin.catch_%spersonBj_idle", string), true, animationEvent);
                        break block5;
                    }
                    case START_THROWING: {
                        this.createAnimation(String.format("animation.goblin.throw_%sperson", string), true, animationEvent);
                        break block5;
                    }
                    case THROWN: {
                        this.createAnimation("animation.goblin.thrown", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.goblin.paizuri_start", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.goblin.paizuri_slow" + this.aP, true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.goblin.paizuri_fast", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_FAST_CONTINUES: {
                        this.createAnimation("animation.goblin.paizuri_fast_countinues", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_IDLE: {
                        this.createAnimation("animation.goblin.paizuri_idle", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.goblin.paizuri_cum", true, animationEvent);
                        break block5;
                    }
                    case JUMP_0: {
                        this.createAnimation("animation.goblin.jump_1", true, animationEvent);
                        break block5;
                    }
                    case JUMP_1: {
                        this.createAnimation("animation.goblin.jump_2", true, animationEvent);
                        break block5;
                    }
                    case JUMP_2: {
                        this.createAnimation("animation.goblin.jump_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_INTRO_0: {
                        this.createAnimation("animation.goblin.breeding_intro_1", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_INTRO_1: {
                        this.createAnimation("animation.goblin.breeding_intro_2", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_INTRO_2: {
                        this.createAnimation("animation.goblin.breeding_intro_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_SLOW_0: {
                        this.createAnimation("animation.goblin.breeding_slow_1" + (this.aD ? "l" : "r"), true, animationEvent);
                        break block5;
                    }
                    case BREEDING_SLOW_2: {
                        this.createAnimation("animation.goblin.breeding_slow_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_FAST_0: {
                        this.createAnimation("animation.goblin.breeding_fast_1" + (this.ay ? "c" : "s"), true, animationEvent);
                        break block5;
                    }
                    case BREEDING_FAST_2: {
                        this.createAnimation("animation.goblin.breeding_fast_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_CUM_0: {
                        this.createAnimation("animation.goblin.breeding_cum_1", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_CUM_1: {
                        this.createAnimation("animation.goblin.breeding_cum_2", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_CUM_2: {
                        this.createAnimation("animation.goblin.breeding_cum_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_1: {
                        this.createAnimation("animation.goblin.breeding_2", true, animationEvent);
                        break block5;
                    }
                    case VANISH: 
                    case AWAIT_PICK_UP: {
                        this.createAnimation("animation.goblin.await_pick_up", true, animationEvent);
                        break block5;
                    }
                    case STAND_UP: {
                        this.createAnimation("animation.goblin.stand_up", false, animationEvent);
                        break block5;
                    }
                    case NELSON_INTRO: {
                        this.createAnimation("animation.goblin.nelson_intro", true, animationEvent);
                        break block5;
                    }
                    case NELSON_SLOW: {
                        this.createAnimation("animation.goblin.nelson_slow" + (this.aF ? "" : "2"), true, animationEvent);
                        break block5;
                    }
                    case NELSON_FAST: {
                        this.createAnimation("animation.goblin.nelson_fast" + (this.X ? "c" : "s"), true, animationEvent);
                        break block5;
                    }
                    case NELSON_CUM: {
                        this.createAnimation("animation.goblin.nelson_cum", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData animationData) {
        if (this.actionController == null) {
            this.void_p();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "catchEh": {
                    this.void_a("ehh..");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "catchAkward": {
                    this.void_a("awkward..");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "catchWell": {
                    this.void_a("well...");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "catchRather": {
                    this.void_a("would you rather have this stupid... thing?");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "catchMe": {
                    this.void_a("...or use me?~");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "catchDone": {
                    if (!"bj".equals(this.entityDataManager.get(h))) break;
                    this.setCurrentAction(Action.CATCH_BJ);
                    break;
                }
                case "catchBjDone": {
                    this.setCurrentAction(Action.CATCH_BJ_IDLE);
                    if (!this.boolean_n()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    GoblinEntity.a(entityPlayerSP, this, new String[]{"use her", "take ur stuff back"}, null, false);
                    break;
                }
                case "paizuriChoice": {
                    this.void_a("good choice!~");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "paizuriBoth": {
                    this.void_a("...for both of us!");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "paizruiUse": {
                    this.void_a("now use me like a fuck toy!~");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "paizuriSwitch": {
                    if (this.getRNG().nextBoolean()) break;
                    this.aP = "".equals(this.aP) ? "2" : "";
                    break;
                }
                case "touch": {
                    this.a(SoundsHandler.MISC_TOUCH, 3.0f);
                    break;
                }
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "paizuri_startDone": {
                    this.setCurrentAction(Action.PAIZURI_IDLE);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "paizuriFastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    break;
                }
                case "paizuriFastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PAIZURI_FAST_CONTINUES);
                    break;
                }
                case "paizuriFastContinuesReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "smallPound": {
                    this.a(SoundsHandler.MISC_POUNDING, 0.25f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "paizruiCam": {
                    if (!this.boolean_n()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    entityPlayerSP.rotationPitch = 70.0f;
                    entityPlayerSP.prevRotationPitch = 70.0f;
                    break;
                }
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "paizuriCumDone": {
                    this.setCurrentAction(Action.NULL);
                    break;
                }
                case "cumSound": {
                    this.a(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "jumpCam": {
                    if (!this.boolean_n()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() + 170.0f;
                    minecraft.player.rotationPitch = -20.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 2;
                    break;
                }
                case "breedingHmm": {
                    if (this.boolean_n()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
                        minecraft.player.rotationPitch = -15.0f;
                        minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                        minecraft.gameSettings.thirdPersonView = 0;
                    }
                    this.void_a("hmm...");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "breedingFound": {
                    this.void_a("guess we found a worthy breeding partner!");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "breedingEnough": {
                    this.void_a("Eh.. go pin him down, before he runs off!");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "breedingCam2": {
                    if (this.boolean_n()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.gameSettings.thirdPersonView = 2;
                        minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() - 120.0f;
                        minecraft.player.rotationPitch = -30.0f;
                    }
                }
                case "breedingIntroDone": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "breeding_slow1Done": {
                    if (this.getRNG().nextBoolean()) {
                        boolean bl = this.aD = !this.aD;
                    }
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_FAST_0);
                    this.ay = false;
                    break;
                }
                case "breeding_fast1Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.boolean_n()) break;
                    this.ay = false;
                    break;
                }
                case "breeding_fast1Ready": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.ay = true;
                    this.N();
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
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
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_SLOW_2);
                    break;
                }
                case "breeding_intro_2Done": {
                    this.setCurrentAction(Action.BREEDING_1);
                    break;
                }
                case "breeding_cumCam": {
                    if (!this.boolean_n()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.gameSettings.thirdPersonView = 0;
                    minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
                    minecraft.player.rotationPitch = -15.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 0;
                    break;
                }
                case "neslon_introDone": {
                    this.setCurrentAction(Action.NELSON_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "nelson_slowDone": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.aF = !this.aF;
                    break;
                }
                case "neslon_fastSwitch": {
                    if (!this.boolean_n()) {
                        this.X = true;
                        return;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.X = true;
                    break;
                }
                case "neslon_fastBackSwitch": {
                    if (!this.boolean_n()) {
                        this.actionController.tickOffset = 0.0;
                        break;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "nelsonFastDone": {
                    this.X = false;
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.NELSON_SLOW);
                    break;
                }
                case "nelson_cumDone": {
                    if (!this.boolean_n()) break;
                    this.void_r();
                    this.setCurrentAction(Action.NULL);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        this.movementController.transitionLengthTicks = 10.0;
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    static EntityDataManager access$000(GoblinEntity e3_class2192) {
        return e3_class2192.entityDataManager;
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class c_inner222 {
        static Minecraft a = null;

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(TickEvent.ClientTickEvent clientTickEvent) {
            if (clientTickEvent.phase == TickEvent.Phase.START) {
                return;
            }
            ArrayList<GoblinEntity> arrayList = new ArrayList<GoblinEntity>();
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                EntityPlayer entityPlayer;
                GoblinEntity e3_class2192;
                UUID uUID;
                if (!em_class2582.world.isRemote || !(em_class2582 instanceof GoblinEntity) || (uUID = (e3_class2192 = (GoblinEntity) em_class2582).java_util_UUID_e()) == null || (entityPlayer = e3_class2192.world.getPlayerEntityByUUID(uUID)) == null || entityPlayer.dimension == e3_class2192.dimension)
                    continue;
                arrayList.add(e3_class2192);
            }
            for (GoblinEntity e3_class2193 : arrayList) {
                e3_class2193.void_a((UUID) null);
                e3_class2193.void_e((UUID) null);
                e3_class2193.setDead();
            }
        }

        @SubscribeEvent
        public void a(PlayerEvent.PlayerChangedDimensionEvent playerChangedDimensionEvent) {
            EntityPlayer entityPlayer = playerChangedDimensionEvent.player;
            UUID uUID = entityPlayer.getPersistentID();
            int n = playerChangedDimensionEvent.toDim;
            World world = entityPlayer.world;
            GoblinEntity e3_class2192 = null;
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                GoblinEntity e3_class2193;
                if (em_class2582.world.isRemote || !(em_class2582 instanceof GoblinEntity) || !uUID.equals((e3_class2193 = (GoblinEntity) em_class2582).java_util_UUID_e()))
                    continue;
                String string = e3_class2193.java_lang_String_C();
                String string2 = e3_class2193.java_lang_String_F();
                e3_class2192 = e3_class2193;
                e3_class2192.void_a((UUID) null);
                e3_class2192.void_e((UUID) null);
                e3_class2192.setCurrentAction(Action.NULL);
                GoblinEntity e3_class2194 = new GoblinEntity(world);
                e3_class2194.dimension = n;
                e3_class2194.forceSpawn = true;
                e3_class2194.f(string);
                e3_class2194.e(string2);
                GoblinEntity.access$000(e3_class2194).set(aC, true);
                world.spawnEntity(e3_class2194);
                e3_class2194.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                e3_class2194.void_a(uUID);
                e3_class2194.setCurrentAction(Action.SHOULDER_IDLE);
                break;
            }
            if (e3_class2192 == null) {
                return;
            }
            world.removeEntity(e3_class2192);
            GirlEntity.GirlEntityList().remove(e3_class2192);
        }

        @SubscribeEvent
        public void a(LivingAttackEvent livingAttackEvent) {
            if (livingAttackEvent.getSource() == DamageSource.OUT_OF_WORLD) {
                return;
            }
            EntityLivingBase entityLivingBase = livingAttackEvent.getEntityLiving();
            if (!(entityLivingBase instanceof GoblinEntity)) {
                return;
            }
            GoblinEntity e3_class2192 = (GoblinEntity)entityLivingBase;
            if (e3_class2192.java_util_UUID_e() != null) {
                livingAttackEvent.setCanceled(true);
            }
        }

        @SubscribeEvent
        @SideOnly(value=Side.CLIENT)
        public void a(InputEvent.KeyInputEvent keyInputEvent) {
            if (a == null) {
                a = Minecraft.getMinecraft();
            }
            if (c_inner222.a.currentScreen instanceof ea_class235) {
                return;
            }
            if (!ClientProxy.keyBindings[0].isPressed()) {
                return;
            }
            GirlEntity em_class2582 = null;
            UUID uUID = Minecraft.getMinecraft().player.getPersistentID();
            for (GirlEntity em_class2583 : GirlEntity.GirlEntityList()) {
                ai_class30 ai_class302;
                if (!em_class2583.world.isRemote || !(em_class2583 instanceof ai_class30) || !uUID.equals((ai_class302 = (ai_class30) ((Object) em_class2583)).java_util_UUID_e()))
                    continue;
                em_class2582 = em_class2583;
                break;
            }
            if (em_class2582 == null) {
                return;
            }
            if (em_class2582.currentAction() != Action.SHOULDER_IDLE) {
                return;
            }
            Minecraft.getMinecraft().displayGuiScreen(new ea_class235(em_class2582));
        }

        private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
            return concurrentModificationException;
        }
    }
}

