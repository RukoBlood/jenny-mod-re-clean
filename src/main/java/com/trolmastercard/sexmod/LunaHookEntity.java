/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 */
package com.trolmastercard.sexmod;

import com.google.common.base.Optional;

import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LunaHookEntity
extends Entity {
    final static public int m = 15;
    final static private DataParameter<Integer> g = EntityDataManager.createKey(LunaHookEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    final static private DataParameter<Optional<UUID>> f = EntityDataManager.createKey(LunaHookEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID).getSerializer().createKey(110);
    private boolean k;
    private int l;
    private int h;
    public int d;
    private int c;
    private int j;
    private float e;
    public Entity i;
    private HookState hookState = HookState.FLYING;
    private int a;
    private int o;
    static public LunaEntity b = null;

    public LunaHookEntity(World world, LunaEntity eb_class2362, double d) {
        super(world);
        this.a(eb_class2362);
        this.a(d);
    }

    public LunaHookEntity(World world) {
        super(world);
    }

    private void a(LunaEntity lunaEntity) {
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
        lunaEntity.lunaHookEntity = this;
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(g, 0);
        this.getDataManager().register(f, Optional.of(b.girlID()));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getEntityBoundingBox().grow(10.0);
    }

    LunaEntity b() {
        Optional<UUID> optional = this.dataManager.get(f);
        if (!optional.isPresent()) {
            return null;
        }
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a((UUID)optional.get());
        if (em_class2582 == null) {
            return null;
        }
        if (!(em_class2582 instanceof LunaEntity)) {
            return null;
        }
        return (LunaEntity)em_class2582;
    }

    LunaEntity g() {
        Optional<UUID> optional = this.dataManager.get(f);
        if (!optional.isPresent()) {
            return null;
        }
        GirlEntity em_class2582 = GirlEntity.getGirlEntity((UUID)optional.get());
        if (!(em_class2582 instanceof LunaEntity)) {
            return null;
        }
        return (LunaEntity)em_class2582;
    }

    public void b(int n) {
        this.o = n;
    }

    public void a(int n) {
        this.a = n;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (this.world.isRemote) {
            return;
        }
        if ((this.i != null || this.onGround) && this.d == 0) {
            this.b().void_o();
        }
    }

    public void a(double d) {
        LunaEntity eb_class2362 = this.b();
        if (eb_class2362 == null) {
            return;
        }
        BlockPos blockPos = eb_class2362.ai;
        float f = (float)Math.sqrt(eb_class2362.getPositionVector().squareDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        float f2 = -22.5f + 45.0f * (f / 7.0f);
        float f3 = eb_class2362.java_lang_Float_I().floatValue();
        float f4 = MathHelper.cos(-f3 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = MathHelper.sin(-f3 * ((float)Math.PI / 180) - (float)Math.PI);
        float f6 = -MathHelper.cos(-f2 * ((float)Math.PI / 180));
        float f7 = MathHelper.sin(-f2 * ((float)Math.PI / 180));
        double d2 = eb_class2362.prevPosX + (eb_class2362.posX - eb_class2362.prevPosX) - (double)f5 * 0.3;
        double d3 = eb_class2362.prevPosY + (eb_class2362.posY - eb_class2362.prevPosY) + (double)eb_class2362.getEyeHeight();
        double d4 = eb_class2362.prevPosZ + (eb_class2362.posZ - eb_class2362.prevPosZ) - (double)f4 * 0.3;
        this.setLocationAndAngles(d2, d3, d4, f3, f2);
        this.motionX = d * (double)(-f5);
        this.motionY = d * (double)MathHelper.clamp(-(f7 / f6), -5.0f, 5.0f);
        this.motionZ = d * (double)(-f4);
        float f8 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX *= 0.6 / (double)f8 + 0.5 + this.rand.nextGaussian() * 0.0045;
        this.motionY *= 0.6 / (double)f8 + 0.5 + this.rand.nextGaussian() * 0.0045;
        this.motionZ *= 0.6 / (double)f8 + 0.5 + this.rand.nextGaussian() * 0.0045;
        float f9 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f9) * 57.29577951308232);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> dataParameter) {
        if (g.equals(dataParameter)) {
            int n = this.getDataManager().get(g);
            this.i = n > 0 ? this.world.getEntityByID(n - 1) : null;
        }
        super.notifyDataManagerChange(dataParameter);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isInRangeToRenderDist(double d) {
        double d2 = 64.0;
        return d < 4096.0;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void setPositionAndRotationDirect(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.b() == null) {
            this.setDead();
        } else if (this.world.isRemote || !this.f()) {
            double d;
            if (this.k) {
                ++this.l;
                if (this.l >= 1200) {
                    this.setDead();
                    return;
                }
            }
            float f = 0.0f;
            BlockPos blockPos = new BlockPos(this);
            IBlockState iBlockState = this.world.getBlockState(blockPos);
            if (iBlockState.getMaterial() == Material.WATER) {
                f = BlockLiquid.getBlockLiquidHeight(iBlockState, this.world, blockPos);
            }
            if (this.hookState == HookState.FLYING) {
                if (this.i != null) {
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                    this.hookState = HookState.HOOKED_IN_ENTITY;
                    return;
                }
                if (f > 0.0f) {
                    this.motionX *= 0.3;
                    this.motionY *= 0.2;
                    this.motionZ *= 0.3;
                    this.hookState = HookState.BOBBING;
                    return;
                }
                if (!this.world.isRemote) {
                    this.e();
                }
                if (!(this.k || this.onGround || this.collidedHorizontally)) {
                    ++this.h;
                } else {
                    this.h = 0;
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                }
            } else {
                if (this.hookState == HookState.HOOKED_IN_ENTITY) {
                    if (this.i != null) {
                        if (this.i.isDead) {
                            this.i = null;
                            this.hookState = HookState.FLYING;
                        } else {
                            this.posX = this.i.posX;
                            double d2 = this.i.height;
                            this.posY = this.i.getEntityBoundingBox().minY + d2 * 0.8;
                            this.posZ = this.i.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }
                    return;
                }
                if (this.hookState == HookState.BOBBING) {
                    this.motionX *= 0.9;
                    this.motionZ *= 0.9;
                    d = this.posY + this.motionY - (double)blockPos.getY() - (double)f;
                    if (Math.abs(d) < 0.01) {
                        d += Math.signum(d) * 0.1;
                    }
                    this.motionY -= d * (double)this.rand.nextFloat() * 0.2;
                    if (!this.world.isRemote && f > 0.0f) {
                        this.a(blockPos);
                    }
                }
            }
            if (iBlockState.getMaterial() != Material.WATER) {
                this.motionY -= 0.03;
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.h();
            d = 0.92;
            this.motionX *= 0.92;
            this.motionY *= 0.92;
            this.motionZ *= 0.92;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private boolean f() {
        return false;
    }

    private void h() {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232);
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
            this.prevRotationYaw -= 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
            this.prevRotationYaw += 360.0f;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
    }

    private void e() {
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult rayTraceResult = this.world.rayTraceBlocks(vec3d, vec3d2, false, true, false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (rayTraceResult != null) {
            vec3d2 = new Vec3d(rayTraceResult.hitVec.x, rayTraceResult.hitVec.y, rayTraceResult.hitVec.z);
        }
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
        double d = 0.0;
        for (Entity entity2 : list) {
            double d2;
            AxisAlignedBB axisAlignedBB;
            RayTraceResult rayTraceResult2;
            if (!this.a(entity2) || entity2 == this.b() && this.h < 5 || (rayTraceResult2 = (axisAlignedBB = entity2.getEntityBoundingBox().grow(0.3f)).calculateIntercept(vec3d, vec3d2)) == null || !((d2 = vec3d.squareDistanceTo(rayTraceResult2.hitVec)) < d) && d != 0.0) continue;
            entity = entity2;
            d = d2;
        }
        if (entity != null) {
            rayTraceResult = new RayTraceResult(entity);
        }
        if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS) {
            if (rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.i = rayTraceResult.entityHit;
                this.a();
            } else {
                this.k = true;
            }
        }
    }

    private void a() {
        this.getDataManager().set(g, this.i.getEntityId() + 1);
    }

    private void a(BlockPos blockPos) {
        WorldServer worldServer = (WorldServer)this.world;
        int n = 1;
        BlockPos blockPos2 = blockPos.up();
        if (this.rand.nextFloat() < 0.25f && this.world.isRainingAt(blockPos2)) {
            ++n;
        }
        if (this.rand.nextFloat() < 0.5f && !this.world.canSeeSky(blockPos2)) {
            --n;
        }
        if (this.d > 0) {
            --this.d;
            if (this.d <= 0) {
                this.c = 0;
                this.j = 0;
            } else {
                this.motionY -= 0.2 * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        } else if (this.j > 0) {
            this.j -= n;
            if (this.j > 0) {
                double d;
                this.e = (float)((double)this.e + this.rand.nextGaussian() * 4.0);
                float f = this.e * ((float)Math.PI / 180);
                float f2 = MathHelper.sin(f);
                float f3 = MathHelper.cos(f);
                double d2 = this.posX + (double)(f2 * (float)this.j * 0.1f);
                double d3 = (float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0f;
                IBlockState iBlockState = worldServer.getBlockState(new BlockPos(d2, d3 - 1.0, d = this.posZ + (double)(f3 * (float)this.j * 0.1f)));
                if (iBlockState.getMaterial() == Material.WATER) {
                    if (this.rand.nextFloat() < 0.15f) {
                        worldServer.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d2, d3 - (double)0.1f, d, 1, f2, 0.1, f3, 0.0, new int[0]);
                    }
                    float f4 = f2 * 0.04f;
                    float f5 = f3 * 0.04f;
                    worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, d2, d3, d, 0, f5, 0.01, -f4, 1.0, new int[0]);
                    worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, d2, d3, d, 0, -f5, 0.01, f4, 1.0, new int[0]);
                }
            } else {
                this.motionY = -0.4f * MathHelper.nextFloat(this.rand, 0.6f, 1.0f);
                this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                double d = this.getEntityBoundingBox().minY + 0.5;
                worldServer.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, d, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.2f, new int[0]);
                worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, d, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.2f, new int[0]);
                this.d = MathHelper.getInt(this.rand, 20, 40);
            }
        } else if (this.c > 0) {
            this.c -= n;
            float f = 0.15f;
            if (this.c < 20) {
                f = (float)((double)f + (double)(20 - this.c) * 0.05);
            } else if (this.c < 40) {
                f = (float)((double)f + (double)(40 - this.c) * 0.02);
            } else if (this.c < 60) {
                f = (float)((double)f + (double)(60 - this.c) * 0.01);
            }
            if (this.rand.nextFloat() < f) {
                double d;
                double d4;
                float f6 = MathHelper.nextFloat(this.rand, 0.0f, 360.0f) * ((float)Math.PI / 180);
                float f7 = MathHelper.nextFloat(this.rand, 25.0f, 60.0f);
                double d5 = this.posX + (double)(MathHelper.sin(f6) * f7 * 0.1f);
                IBlockState iBlockState = worldServer.getBlockState(new BlockPos((int)d5, (int)(d4 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0f)) - 1, (int)(d = this.posZ + (double)(MathHelper.cos(f6) * f7 * 0.1f))));
                if (iBlockState.getMaterial() == Material.WATER) {
                    worldServer.spawnParticle(EnumParticleTypes.WATER_SPLASH, d5, d4, d, 2 + this.rand.nextInt(2), 0.1f, 0.0, 0.1f, 0.0, new int[0]);
                }
            }
            if (this.c <= 0) {
                this.e = MathHelper.nextFloat(this.rand, 0.0f, 360.0f);
                this.j = MathHelper.getInt(this.rand, 20, 80);
            }
        } else {
            this.c = MathHelper.getInt(this.rand, 100, 600);
            this.c -= this.o * 20 * 5;
        }
    }

    protected boolean a(Entity entity) {
        return entity.canBeCollidedWith() || entity instanceof EntityItem;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
    }

    public int c() {
        if (!this.world.isRemote && this.b() != null) {
            int n = 0;
            // TODO there is a Object var2
            //  fishing rod thats never used here...
            //Object var2_2 = null;
            if (this.i != null) {
                this.d();
                this.world.setEntityState(this, (byte)31);
                n = this.i instanceof EntityItem ? 3 : 5;
            } else if (this.d > 0) {
                LootContext.Builder builder = new LootContext.Builder((WorldServer)this.world);
                List<ItemStack> list = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, builder.build());
                for (ItemStack itemStack : list) {
                    LunaEntity eb_class2362 = this.b();
                    eb_class2362.b(itemStack);
                }
                this.d = 9999;
                n = 1;
            }
            if (this.k) {
                n = 2;
            }
            return n;
        }
        return 0;
    }

    protected void d() {
        LunaEntity eb_class2362 = this.b();
        if (eb_class2362 != null) {
            double d = eb_class2362.posX - this.posX;
            double d2 = eb_class2362.posY - this.posY;
            double d3 = eb_class2362.posZ - this.posZ;
            double d4 = 0.1;
            this.i.motionX += d * 0.1;
            this.i.motionY += d2 * 0.1;
            this.i.motionZ += d3 * 0.1;
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        return null;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    static enum HookState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;

    }
}

