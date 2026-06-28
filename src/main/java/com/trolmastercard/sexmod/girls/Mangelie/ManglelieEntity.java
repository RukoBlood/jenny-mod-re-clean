/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.event.entity.ProjectileImpactEvent$Arrow
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.girls.Mangelie;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ManglelieEntity
extends GirlEntity {
    final static public String ac = "sexmod:mommy";
    final static public float am = 60.0f;
    final static public float ag = 4.0f;
    final static public float P = 3.5f;
    final static public float ah = 28.0f;
    final static public float ae = 15.0f;
    final static public float K = 15.0f;
    final static public float L = 0.65f;
    final static public float ao = 3.65f;
    final static public float O = 6.0f;
    final static public float ak = 80.0f;
    final static public float X = 700.0f;
    final static public DataParameter<String> ad = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.STRING).getSerializer().createKey(111);
    final static public DataParameter<Boolean> ap = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(112);
    final static public DataParameter<Integer> ab = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.VARINT).getSerializer().createKey(113);
    final static public DataParameter<String> al = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.STRING).getSerializer().createKey(114);
    final static public DataParameter<Boolean> ar = EntityDataManager.createKey(ManglelieEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(115);
    private UUID Q = null;
    public boolean aj = true;
    public Vec3d R = Vec3d.ZERO;
    public float V = 0.0f;
    boolean aq = true;
    boolean S = false;
    boolean U = false;
    public float af = 0.0f;
    public float W = 0.0f;
    public float T = 0.0f;
    public float ai = 0.0f;
    boolean aa = false;
    boolean Z = false;
    boolean N = false;
    boolean Y = false;
    boolean M = false;
    public int an = 2;

    public ManglelieEntity(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(ad, "");
        this.entityDataManager.register(ap, false);
        this.entityDataManager.register(ab, -1);
        this.entityDataManager.register(al, "");
        this.entityDataManager.register(ar, false);
    }

    @Override
    public String getGirlName() {
        return "Manglelie";
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new bt_class99(this, 20.0f, 1.0, 1.2));
    }

    @Override
    public float float_i() {
        return 0.0f;
    }

    public void c(boolean bl) {
        this.entityDataManager.set(ap, bl);
    }

    public boolean boolean_r() {
        return this.entityDataManager.get(ap);
    }

    @Nullable
    public UUID java_util_UUID_v() {
        String string = this.entityDataManager.get(ad);
        if ("".equals(string)) {
            return null;
        }
        try {
            return UUID.fromString(string);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean boolean_t() {
        return !this.boolean_r();
    }

    @Nullable
    public GalathEntity com_trolmastercard_sexmod_f__class297_a(boolean bl) {
        GirlEntity em_class2582;
        UUID uUID = this.java_util_UUID_v();
        if (uUID == null) {
            return null;
        }
        GirlEntity em_class2583 = em_class2582 = bl ? GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID) : GirlEntity.getGirlEntity(uUID);
        if (!(em_class2582 instanceof GalathEntity)) {
            return null;
        }
        return (GalathEntity)em_class2582;
    }

    public void void_a(UUID uUID) {
        if (uUID == null) {
            this.entityDataManager.set(ad, "");
            return;
        }
        this.entityDataManager.set(ad, uUID.toString());
    }

    @Override
    public Float java_lang_Float_I() {
        float f = super.java_lang_Float_I().floatValue();
        if (ManglelieModel.boolean_c(this)) {
            f += 180.0f;
        }
        return Float.valueOf(f);
    }

    public void void_q() {
        this.S = true;
    }

    @Override
    public void updateAITasks() {
        if (this.aa) {
            this.world.removeEntity(this);
            return;
        }
        this.void_f();
        this.void_w();
        super.updateAITasks();
        this.void_j();
        this.void_c();
        this.void_d();
        this.void_i();
        this.void_n();
        this.u_();
        this.void_h();
        this.a_();
        this.void_t();
    }

    void void_t() {
        if (this.java_util_UUID_v() != null) {
            this.aq = false;
        }
        if (this.aq) {
            return;
        }
        if (this.com_trolmastercard_sexmod_f__class297_a(true) == null) {
            System.out.println("removed non-wild mang for lack of mommy");
            this.world.removeEntity(this);
        }
    }

    void a_() {
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            return;
        }
        if (f__class2972.aF() == null) {
            return;
        }
        if (this.girlID().equals(f__class2972.aF())) {
            return;
        }
        System.out.println("removed non-wild mang cuz her mommy disowned her and got another mang");
        this.world.removeEntity(this);
    }

    public static GalathEntity a(GirlEntity em_class2582, boolean bl) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return null;
        }
        return ((ManglelieEntity)em_class2582).com_trolmastercard_sexmod_f__class297_a(bl);
    }

    public long long_e() {
        String string = this.entityDataManager.get(al);
        if ("".equals(string)) {
            return -1L;
        }
        try {
            return Long.parseLong(string);
        } catch (Exception exception) {
            return -1L;
        }
    }

    public void a(long l) {
        this.entityDataManager.set(al, Long.toString(l));
        this.U = false;
    }

    void void_h() {
        long l = this.long_e();
        if (l == -1L) {
            return;
        }
        long l2 = this.world.getTotalWorldTime();
        if ((float)l2 < 28.0f + (float)l) {
            return;
        }
        if (this.U) {
            return;
        }
        Entity entity = this.b_7();
        if (entity == null) {
            return;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            return;
        }
        EntityTippedArrow entityTippedArrow = new EntityTippedArrow(this.world, this);
        Vec3d vec3d = f__class2972.getPositionVector().add(0.0, 3.5, 0.0);
        entityTippedArrow.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        Vec3d vec3d2 = entity.getPositionVector();
        Vec3d vec3d3 = vec3d2.subtract(vec3d).normalize();
        entityTippedArrow.motionX = vec3d3.x * 4.0;
        entityTippedArrow.motionY = vec3d3.y * 4.0;
        entityTippedArrow.motionZ = vec3d3.z * 4.0;
        GirlEntity.a((GirlEntity)f__class2972, SoundEvents.ENTITY_ARROW_SHOOT, true);
        this.world.spawnEntity(entityTippedArrow);
        this.U = true;
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect) {
    }

    void u_() {
        boolean bl = this.java_util_UUID_v() != null;
        this.setNoGravity(bl);
        this.noClip = bl;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.java_util_UUID_v() == null;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public Vec3d a(Minecraft minecraft, CustomModelEntity cy_class1532, EntityLivingBase entityLivingBase, float f) {
        if (this.boolean_h()) {
            return super.a(minecraft, cy_class1532, entityLivingBase, f);
        }
        if (!this.boolean_r()) {
            return super.a(minecraft, cy_class1532, entityLivingBase, f);
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return super.a(minecraft, cy_class1532, entityLivingBase, f);
        }
        ManglelieRenderer.a(f__class2972, f, cy_class1532);
        return ManglelieRenderer.b(f__class2972, f);
    }

    public float float_b(float f) {
        long l = this.long_e();
        if (l == -1L) {
            return 0.0f;
        }
        long l2 = this.world.getTotalWorldTime();
        float f2 = l2 - l;
        return (f2 + f) / 28.0f;
    }

    @Nullable
    public Entity b_7() {
        int n = this.entityDataManager.get(ab);
        if (n == -1) {
            return null;
        }
        return this.world.getEntityByID(n);
    }

    void a(int n) {
        this.entityDataManager.set(ab, n);
        this.a(n == -1 ? -1L : this.world.getTotalWorldTime());
    }

    void void_d() {
        Entity entity = this.b_7();
        if (entity == null) {
            return;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            this.a(-1);
            return;
        }
        if (!this.boolean_r()) {
            this.a(-1);
            return;
        }
        if (ManglelieEntity.a(entity, f__class2972)) {
            this.a(-1);
        }
    }

    public static boolean a(Entity entity, GalathEntity f__class2972) {
        if (entity.isDead) {
            return true;
        }
        if (entity.dimension != f__class2972.dimension) {
            return true;
        }
        if (!d_class156.a(entity)) {
            return true;
        }
        if (!d_class156.a(f__class2972.world, f__class2972.net_minecraft_util_math_Vec3d_o().add(0.0, f__class2972.getEyeHeight(), 0.0), entity)) {
            return true;
        }
        Vec3d vec3d = entity.getPositionVector().subtract(f__class2972.getPositionVector());
        if (vec3d.x * vec3d.x + vec3d.z * vec3d.z > 225.0) {
            return true;
        }
        Float f = GalathEntity.a(f__class2972, 0.0f);
        float f2 = f == null ? f__class2972.rotationYawHead : f.floatValue();
        Vec3d vec3d2 = VectorMath.rotate(vec3d, f2);
        return vec3d2.z < 0.0;
    }

    void void_n() {
        if (this.b() != null) {
            return;
        }
        if (!this.boolean_r()) {
            return;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            return;
        }
        if (f__class2972.getID() != null) {
            return;
        }
        if (f__class2972.currentAction() == Action.MASTERBATE) {
            return;
        }
        BlockPos blockPos = f__class2972.getPosition();
        BlockPos blockPos2 = new BlockPos(15.0, 15.0, 15.0);
        List<EntityMob> list = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(blockPos.add(blockPos2), blockPos.subtract(blockPos2)));
        for (EntityMob entityMob : list) {
            if (ManglelieEntity.a(entityMob, f__class2972)) continue;
            this.a(entityMob.getEntityId());
            return;
        }
    }

    void void_i() {
        Entity entity = this.b_7();
        if (entity == null) {
            return;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            return;
        }
        long l = this.long_e();
        if (l == -1L) {
            return;
        }
        long l2 = this.world.getTotalWorldTime();
        long l3 = l2 - this.long_e();
        if ((float)l3 < 60.0f) {
            return;
        }
        this.U = false;
        this.a(-1);
    }

    void void_j() {
        if (this.Q == null) {
            return;
        }
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(this.Q);
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathEntity f__class2972 = (GalathEntity)em_class2582;
        this.void_a(this.Q);
        f__class2972.void_a(this.girlID());
        this.c(true);
        this.setCurrentAction(Action.RIDE_MOMMY_HEAD);
        this.Q = null;
        if (f__class2972.currentAction() == Action.HUG_MANG) {
            f__class2972.void_a(false);
            f__class2972.setCurrentAction((Action)null);
        }
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        if (this.currentAction() == Action.THREESOME_CUM && Action.a(fp_class3242, Action.THREESOME_FAST, Action.THREESOME_SLOW)) {
            return;
        }
        if (!this.world.isRemote && fp_class3242 == Action.THREESOME_CUM) {
            GalathMangTracker.setLastCumTime(this.getID(), this.world.getTotalWorldTime());
        }
        super.setCurrentAction(fp_class3242);
    }

    void void_w() {
        if (!this.boolean_r() || Action.a((GirlEntity)this, Action.THREESOME_SLOW, Action.THREESOME_CUM, Action.THREESOME_FAST)) {
            return;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            return;
        }
        if (f__class2972.isDead || !this.girlID().equals(f__class2972.aF())) {
            Main.LOGGER.warn("A dead mommy has been saved onto a mang. Deleting her and creating a new one");
            this.world.removeEntity(this);
            return;
        }
        this.void_b(0.0f);
        this.c(f__class2972.getPositionVector());
        this.void_a(true);
    }

    @Override
    public void void_b(float f) {
        super.void_b(f);
    }

    @Override
    public Vec3d net_minecraft_util_math_Vec3d_a(Vec3d vec3d, float f) {
        if (!this.boolean_r()) {
            return vec3d;
        }
        if (ManglelieModel.boolean_c(this)) {
            return vec3d;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return vec3d;
        }
        return ManglelieRenderer.b(f__class2972, f);
    }

    void void_c() {
        if (this.boolean_r()) {
            return;
        }
        if (this.java_util_UUID_v() != null) {
            return;
        }
        BlockPos blockPos = this.getPosition();
        BlockPos blockPos2 = blockPos.add(-15.0, -15.0, -15.0);
        BlockPos blockPos3 = blockPos.add(15.0, 15.0, 15.0);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos2, blockPos3);
        List<GalathEntity> list = this.world.getEntitiesWithinAABB(GalathEntity.class, axisAlignedBB);
        Entity entity = null;
        for (GalathEntity object2 : list) {
            if (object2.isDead || object2.com_trolmastercard_sexmod_f8_class293_a(true) != null || !object2.onGround) continue;
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
        if (this.currentAction() == Action.RIDE_MOMMY_HEAD) {
            return;
        }
        this.setCurrentAction(Action.RUN);
        Vec3d vec3d = this.getPositionVector();
        Vec3d vec3d2 = entity.getPositionVector();
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        float f = (float) TrigMath.toDegrees(Math.atan2(vec3d3.z, vec3d3.x)) - 90.0f;
        this.void_b(f);
        this.f = this.getNavigator();
        this.f.clearPath();
        this.f.tryMoveToEntityLiving(entity, 0.65f);
    }

    boolean a(Entity entity, float f) {
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(f == 1.0f);
        if (f__class2972 == null) {
            return false;
        }
        Vec3d vec3d = ak_class32.a(this, f);
        return this.a(ak_class32.a(entity, f).subtract(vec3d), f__class2972, f);
    }

    boolean boolean_a(Vec3d vec3d, float f) {
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(f == 1.0f);
        if (f__class2972 == null) {
            return false;
        }
        Vec3d vec3d2 = ak_class32.a(this, f);
        return this.a(vec3d.subtract(vec3d2), f__class2972, f);
    }

    boolean a(Vec3d vec3d, GalathEntity f__class2972, float f) {
        Vec3d vec3d2 = VectorMath.rotate(vec3d, Reference.LerpAngleDegrees(f__class2972.prevRotationYawHead, f__class2972.rotationYawHead, (double)f));
        return vec3d2.x > 0.35;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            this.void_m();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void void_m() {
        if ((float)Minecraft.getMinecraft().player.ticksExisted % 7.0f != 0.0f) {
            return;
        }
        if (!ManglelieRenderer.b_4(this)) {
            return;
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return;
        }
        Entity entity = this.net_minecraft_entity_Entity_o();
        if (entity == null) {
            this.af = 0.0f;
            this.W = 0.0f;
            return;
        }
        Vec3d vec3d = entity.getPositionVector().add(0.0, entity.getEyeHeight(), 0.0);
        Vec3d vec3d2 = f__class2972.getPositionVector().add(f__class2972.b("mangPos")).add(this.b("head"));
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        float f = (float)(TrigMath.toDegrees(Math.atan2(vec3d3.z, vec3d3.x)) + 90.0);
        Float f2 = GalathEntity.a(f__class2972, 0.0f);
        f -= f__class2972.rotationYawHead;
        if (f2 != null) {
            f -= f2.floatValue();
        }
        this.af = Math.abs(cj_class134.a(0.0f, f)) < 80.0f ? -TrigMath.toRadians(f) : 0.0f;
        this.W = this.af == 0.0f ? 0.0f : (float)be_class78.b(-vec3d3.y / 2.0, -0.75, 0.75);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(damageSource, f);
        }
        GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(true);
        if (f__class2972 == null) {
            return super.attackEntityFrom(damageSource, f);
        }
        f__class2972.attackEntityFrom(damageSource, f);
        return false;
    }

    @Nullable
    Entity net_minecraft_entity_Entity_o() {
        Entity entity = this.b_7();
        if (entity != null) {
            return entity;
        }
        for (EntityPlayer entityPlayer : this.world.playerEntities) {
            float f = entityPlayer.getDistance(this);
            if (f > 6.0f || entity != null && !(entity.getDistance(this) > f)) continue;
            entity = entityPlayer;
        }
        return entity;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        UUID uUID = this.java_util_UUID_v();
        nBTTagCompound.setString(ac, uUID == null ? "" : uUID.toString());
        nBTTagCompound.setBoolean("sexmod:iswild", this.aq);
        if (this.S) {
            nBTTagCompound.setBoolean("sexmod:despawned", true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        String string = nBTTagCompound.getString(ac);
        if (!"".equals(string)) {
            this.Q = UUID.fromString(string);
        }
        if (nBTTagCompound.getBoolean("sexmod:despawned")) {
            this.aa = true;
        }
        this.aq = nBTTagCompound.getBoolean("sexmod:iswild");
    }

    @Override
    protected boolean boolean_X() {
        return false;
    }

    @Override
    public void f(String string) {
        super.f(string);
        bj_class84.a(this);
    }

    void void_f() {
        if (this.Z) {
            return;
        }
        this.f(bj_class84.c(this));
        this.Z = true;
    }

    @Override
    @Nullable
    protected Action FastSexAction(Action action) {
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (Action.a(action, Action.THREESOME_FAST, Action.THREESOME_SLOW)) {
            this.N = true;
        }
        return null;
    }

    @Override
    public void void_g() {
        if (this.boolean_r()) {
            this.setCurrentAction(Action.RIDE_MOMMY_HEAD);
            this.void_b(0.0f);
            this.entityDataManager.setDirty(w);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        if (!super.getCanSpawnHere()) {
            return false;
        }
        BlockPos blockPos = this.getPosition();
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        arrayList.addAll(fq_class325.c);
        arrayList.addAll(fq_class325.b);
        for (BlockPos blockPos2 : arrayList) {
            if (!(Math.sqrt(blockPos.distanceSq(blockPos2)) < 700.0)) continue;
            return false;
        }
        fq_class325.a(blockPos, fq_class325.b);
        return true;
    }

    @Override
    protected boolean a(Action fp_class3242, String string, boolean bl, AnimationEvent animationEvent) {
        if (fp_class3242 == Action.THREESOME_CUM) {
            this.N = false;
            this.Y = false;
            this.M = false;
            this.an = 2;
            this.void_r();
            GalathEntity f__class2972 = this.com_trolmastercard_sexmod_f__class297_a(false);
            if (f__class2972 != null) {
                f__class2972.void_r();
                ga_class358.a(f__class2972);
            }
            ga_class358.a(this);
            return true;
        }
        if (this.N && fp_class3242 == Action.THREESOME_FAST) {
            this.setCurrentAction(Action.THREESOME_CUM);
            this.createAnimation("animation.shared.double_holding_cum", true, animationEvent, true);
            GalathEntity f__class2973 = this.com_trolmastercard_sexmod_f__class297_a(false);
            if (f__class2973 != null) {
                f__class2973.setCurrentAction(Action.MASTERBATE_SITTING_CUM);
            }
            return true;
        }
        if ((this.N || bl) && fp_class3242 == Action.THREESOME_SLOW) {
            this.Y = false;
            this.setCurrentAction(Action.THREESOME_FAST);
            this.createAnimation("animation.shared.double_holding_soft", true, animationEvent, true);
            GalathEntity f__class2974 = this.com_trolmastercard_sexmod_f__class297_a(false);
            if (f__class2974 != null) {
                f__class2974.ak();
            }
            return true;
        }
        if (this.N) {
            return false;
        }
        if (bl && !this.Y && fp_class3242 == Action.THREESOME_FAST) {
            this.Y = true;
            this.createAnimation("animation.shared.double_holding_hard", true, animationEvent, true);
            return true;
        }
        if (!bl && fp_class3242 == Action.THREESOME_FAST) {
            this.M = true;
            this.setCurrentAction(Action.THREESOME_SLOW);
            this.createAnimation("animation.shared.double_holding_back", true, animationEvent, true);
            GalathEntity f__class2975 = this.com_trolmastercard_sexmod_f__class297_a(false);
            if (f__class2975 != null) {
                f__class2975.void_a();
            }
            return true;
        }
        if (this.M && fp_class3242 == Action.THREESOME_SLOW) {
            this.M = false;
            this.createAnimation("animation.shared.double_holding_slow", true, animationEvent, true);
            return true;
        }
        return false;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        AnimationController animationController = animationEvent.getController();
        if (this.eyesController == animationController) {
            if (this.b() == null) {
                return PlayState.STOP;
            }
            this.createAnimation("animation.manglelie.angry_face", true, animationEvent);
            return PlayState.CONTINUE;
        }
        if (this.movementController == animationController) {
            if (this.currentAction() != Action.NULL || this.boolean_r()) {
                return PlayState.STOP;
            }
            if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                if (this.entityDataManager.get(ar).booleanValue()) {
                    this.createAnimation("animation.manglelie.scared_run", true, animationEvent);
                } else {
                    this.createAnimation("animation.manglelie.walk", true, animationEvent);
                }
                this.rotationYaw = this.rotationYawHead;
                return PlayState.CONTINUE;
            }
            this.createAnimation("animation.manglelie.idle", true, animationEvent);
            return PlayState.CONTINUE;
        }
        switch (this.currentAction()) {
            default: {
                return PlayState.STOP;
            }
            case RUN: {
                this.createAnimation("animation.manglelie.running", true, animationEvent);
                break;
            }
            case RIDE_MOMMY_HEAD: {
                this.createAnimation("animation.manglelie.sit_on_galath", true, animationEvent);
                break;
            }
            case THREESOME_SLOW: {
                if (this.M) {
                    this.createAnimation("animation.shared.double_holding_back", true, animationEvent);
                    break;
                }
                this.a("animation.shared.double_holding_slow", 4, 0.33f, animationEvent);
                break;
            }
            case THREESOME_FAST: {
                if (this.Y) {
                    this.a("animation.shared.double_holding_hard", 3, 0.33f, animationEvent);
                    break;
                }
                this.createAnimation("animation.shared.double_holding_soft", true, animationEvent);
                break;
            }
            case THREESOME_CUM: {
                this.createAnimation("animation.shared.double_holding_cum", true, animationEvent);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
        this.actionController.registerSoundListener(soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cs0": {
                    this.an = 0;
                    break;
                }
                case "cs1": {
                    this.an = 1;
                    break;
                }
                case "cs2": {
                    this.an = 2;
                    break;
                }
                case "sexui": {
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "doubleSemen0": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                }
                case "doubleSemen": {
                    ga_class358.a(new ep_class263(10, em_class2582 -> {
                        Vec3d vec3d = em_class2582.d("semenEmitter");
                        Vec3d vec3d2 = em_class2582.d("semenDir");
                        return vec3d.subtract(vec3d2).normalize();
                    }, em_class2582 -> em_class2582.b("semenEmitter").add(em_class2582.net_minecraft_util_math_Vec3d_o()), this, 0.3f, 0.3f));
                    break;
                }
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                }
            }
        });
        animationData.addAnimationController(this.actionController);
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class ArrowLogic {
        @SubscribeEvent
        public void CancelArrows(ProjectileImpactEvent.Arrow arrow) {
            RayTraceResult rayTraceResult = arrow.getRayTraceResult();
            EntityArrow entityArrow = arrow.getArrow();
            if (!(entityArrow.shootingEntity instanceof ManglelieEntity)) {
                return;
            }
            if (rayTraceResult.entityHit instanceof GirlEntity) {
                arrow.setCanceled(true);
            }
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

