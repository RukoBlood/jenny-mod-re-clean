/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 */
package com.trolmastercard.sexmod.girls.Luna.FishingRod;

import com.google.common.base.Optional;

import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
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

public class LunaHookEntity extends Entity {
    final static public int MAX_HOOK_RANGE = 15;
    final static private DataParameter<Integer> CAUGHT_ENTITY_ID = EntityDataManager.createKey(LunaHookEntity.class, DataSerializers.VARINT).getSerializer().createKey(111);
    final static private DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(LunaHookEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID).getSerializer().createKey(110);
    private boolean isHooked;
    private int despawnTimer;
    private int waterBobCounter;
    public int lureTimer;
    private int catchDelay;
    private int bobMotion;
    private float bobAngle;
    public Entity caughtEntity;
    private HookState hookState = HookState.FLYING;
    private int phase;
    private int fishingLevel;
    static public LunaEntity ownerLuna = null;

    public LunaHookEntity(World world, LunaEntity luna, double height) {
        super(world);
        this.setOwnerLuna(luna);
        this.positionLunaAbove(height);
    }

    public LunaHookEntity(World world) {
        super(world);
    }

    private void setOwnerLuna(LunaEntity luna) {
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
        luna.fishEntity = this;
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(CAUGHT_ENTITY_ID, 0);
        this.getDataManager().register(OWNER_UUID, Optional.of(ownerLuna.girlID()));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getEntityBoundingBox().grow(10.0);
    }

    LunaEntity getOwnerLunaInternal() {
        Optional<UUID> optUuid = this.dataManager.get(OWNER_UUID);
        if (!optUuid.isPresent()) {
            return null;
        } else {
            GirlEntity girl = GirlEntity.getServerGirlEntity((UUID)optUuid.get());
            if (girl == null) {
                return null;
            } else {
                return !(girl instanceof LunaEntity) ? null : (LunaEntity) girl;
            }
        }
    }

    LunaEntity getOwnerLuna() {
        Optional<UUID> optUuid = this.dataManager.get(OWNER_UUID);
        if (!optUuid.isPresent()) {
            return null;
        } else {
            GirlEntity girl = GirlEntity.getClientGirlEntity((UUID)optUuid.get());
            return !(girl instanceof LunaEntity) ? null : (LunaEntity) girl;
        }
    }

    public void setFishingLevel(int level) {
        this.fishingLevel = level;
    }

    public void setLuck(int phase) {
        this.phase = phase;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (!this.world.isRemote) {
            if ((this.caughtEntity != null || this.onGround) && this.lureTimer == 0) {
                this.getOwnerLunaInternal().addCaughtItem();
            }
        }
    }

    public void positionLunaAbove(double height) {
        LunaEntity luna = this.getOwnerLunaInternal();
        if (luna != null) {
            BlockPos anchorPos = luna.chosenFishingSpot;
            float dist = (float) Math.sqrt(luna.getPositionVector().squareDistanceTo(anchorPos.getX(), anchorPos.getY(), anchorPos.getZ()));
            float angle = -22.5f + 45.0f * (dist / 7.0f);
            float yaw = luna.getYawRotation();
            float cosYaw = MathHelper.cos(-yaw * ((float) Math.PI / 180) - (float) Math.PI);
            float sinYaw = MathHelper.sin(-yaw * ((float) Math.PI / 180) - (float) Math.PI);
            float cosTilt = -MathHelper.cos(-angle * ((float) Math.PI / 180));
            float sinTilt = MathHelper.sin(-angle * ((float) Math.PI / 180));
            double interpX = luna.prevPosX + (luna.posX - luna.prevPosX) - (double) sinYaw * 0.3;
            double interpY = luna.prevPosY + (luna.posY - luna.prevPosY) + (double) luna.getEyeHeight();
            double interpZ = luna.prevPosZ + (luna.posZ - luna.prevPosZ) - (double) cosYaw * 0.3;
            this.setLocationAndAngles(interpX, interpY, interpZ, yaw, angle);
            this.motionX = height * (double) (-sinYaw);
            this.motionY = height * (double) MathHelper.clamp(-(sinTilt / cosTilt), -5.0f, 5.0f);
            this.motionZ = height * (double) (-cosYaw);
            float speed = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.motionX *= 0.6 / (double) speed + 0.5 + this.rand.nextGaussian() * 0.0045;
            this.motionY *= 0.6 / (double) speed + 0.5 + this.rand.nextGaussian() * 0.0045;
            this.motionZ *= 0.6 / (double) speed + 0.5 + this.rand.nextGaussian() * 0.0045;
            float horizontalSpeed = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
            this.rotationPitch = (float) (MathHelper.atan2(this.motionY, horizontalSpeed) * 57.29577951308232);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (CAUGHT_ENTITY_ID.equals(key)) {
            int caughtId = this.getDataManager().get(CAUGHT_ENTITY_ID);
            this.caughtEntity = caughtId > 0 ? this.world.getEntityByID(caughtId - 1) : null;
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isInRangeToRenderDist(double distSquared) {
        //double d2 = 64.0;
        return distSquared < 4096.0;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getOwnerLunaInternal() == null) {
            this.setDead();
        } else if (this.world.isRemote || !this.canCatch()) {
            //double d;

            if (this.isHooked) {
                ++this.despawnTimer;
                if (this.despawnTimer >= 1200) {
                    this.setDead();
                    return;
                }
            }

            float liquidHeight = 0.0f;
            BlockPos blockPos = new BlockPos(this);
            IBlockState iBlockState = this.world.getBlockState(blockPos);
            if (iBlockState.getMaterial() == Material.WATER) {
                liquidHeight = BlockLiquid.getBlockLiquidHeight(iBlockState, this.world, blockPos);
            }

            if (this.hookState == HookState.FLYING) {
                if (this.caughtEntity != null) {
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                    this.hookState = HookState.HOOKED_IN_ENTITY;
                    return;
                }
                if (liquidHeight > 0.0f) {
                    this.motionX *= 0.3;
                    this.motionY *= 0.2;
                    this.motionZ *= 0.3;
                    this.hookState = HookState.BOBBING;
                    return;
                }
                if (!this.world.isRemote) {
                    this.checkCatch();
                }

                if (!(this.isHooked || this.onGround || this.collidedHorizontally)) {
                    ++this.waterBobCounter;
                } else {
                    this.waterBobCounter = 0;
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                }
            } else {
                if (this.hookState == HookState.HOOKED_IN_ENTITY) {
                    if (this.caughtEntity != null) {
                        if (this.caughtEntity.isDead) {
                            this.caughtEntity = null;
                            this.hookState = HookState.FLYING;
                        } else {
                            this.posX = this.caughtEntity.posX;
                            double height = this.caughtEntity.height;
                            this.posY = this.caughtEntity.getEntityBoundingBox().minY + height * 0.8;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }
                    return;
                }
                if (this.hookState == HookState.BOBBING) {
                    this.motionX *= 0.9;
                    this.motionZ *= 0.9;
                    double dY = this.posY + this.motionY - (double)blockPos.getY() - (double)liquidHeight;
                    if (Math.abs(dY) < 0.01) {
                        dY += Math.signum(dY) * 0.1;
                    }
                    this.motionY -= dY * (double)this.rand.nextFloat() * 0.2;
                    if (!this.world.isRemote && liquidHeight > 0.0f) {
                        this.spawnLootBlocks(blockPos);
                    }
                }
            }
            if (iBlockState.getMaterial() != Material.WATER) {
                this.motionY -= 0.03;
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.updateVelocity();
            //d = 0.92;
            this.motionX *= 0.92;
            this.motionY *= 0.92;
            this.motionZ *= 0.92;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private boolean canCatch() {
        return false;
    }

    private void updateVelocity() {
        float horizontalSpeed = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(this.motionY, horizontalSpeed) * 57.29577951308232);
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

    private void checkCatch() {
        Vec3d start = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d end = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult rtResult = this.world.rayTraceBlocks(start, end, false, true, false);

        start = new Vec3d(this.posX, this.posY, this.posZ);
        end = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (rtResult != null) {
            end = new Vec3d(rtResult.hitVec.x, rtResult.hitVec.y, rtResult.hitVec.z);
        }

        Entity target = null;
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));

        double closestDist = 0.0;

        for (Entity entity : entities) {
            double distSq;
            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(0.3f);
            RayTraceResult entityRT;

            if (this.isCollidableEntity(entity)
                    && (entity != this.getOwnerLunaInternal()
                    || this.waterBobCounter >= 5)
                    && (entityRT = aabb.calculateIntercept(start, end)) != null
                    && ((distSq = start.squareDistanceTo(entityRT.hitVec)) < closestDist || closestDist == 0.0)) {
                target = entity;
                closestDist = distSq;
            }
        }

        if (target != null) {
            rtResult = new RayTraceResult(target);
        }

        if (rtResult != null && rtResult.typeOfHit != RayTraceResult.Type.MISS) {
            if (rtResult.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.caughtEntity = rtResult.entityHit;
                this.bindTargetEntity();
            } else {
                this.isHooked = true;
            }
        }
    }

    private void bindTargetEntity() {
        this.getDataManager().set(CAUGHT_ENTITY_ID, this.caughtEntity.getEntityId() + 1);
    }

    private void spawnLootBlocks(BlockPos blockPos) {
        WorldServer worldServer = (WorldServer)this.world;
        int lootCount = 1;
        BlockPos pos = blockPos.up();

        if (this.rand.nextFloat() < 0.25f && this.world.isRainingAt(pos)) {
            ++lootCount;
        }
        if (this.rand.nextFloat() < 0.5f && !this.world.canSeeSky(pos)) {
            --lootCount;
        }

        if (this.lureTimer > 0) {
            --this.lureTimer;
            if (this.lureTimer <= 0) {
                this.catchDelay = 0;
                this.bobMotion = 0;
            } else {
                this.motionY -= 0.2 * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        } else if (this.bobMotion > 0) {
            this.bobMotion -= lootCount;
            if (this.bobMotion > 0) {


                this.bobAngle = (float)((double)this.bobAngle + this.rand.nextGaussian() * 4.0);
                float angle = this.bobAngle * ((float)Math.PI / 180);
                float sinAngle = MathHelper.sin(angle);
                float cosAngle = MathHelper.cos(angle);

                double x = this.posX + (double)(sinAngle * (float)this.bobMotion * 0.1f);
                double y = (float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0f;
                double z = this.posZ + (double)(cosAngle * (float)this.bobMotion * 0.1f);
                IBlockState state = worldServer.getBlockState(new BlockPos(x, y - 1.0, z));

                if (state.getMaterial() == Material.WATER) {
                    if (this.rand.nextFloat() < 0.15f) {
                        worldServer.spawnParticle(EnumParticleTypes.WATER_BUBBLE, x, y - (double)0.1f, z, 1, sinAngle, 0.1, cosAngle, 0.0);
                    }
                    float xVel = sinAngle * 0.04f;
                    float zVel = cosAngle * 0.04f;
                    worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, x, y, z, 0, zVel, 0.01, -xVel, 1.0, new int[0]);
                    worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, x, y, z, 0, -zVel, 0.01, xVel, 1.0, new int[0]);
                }
            } else {
                this.motionY = -0.4f * MathHelper.nextFloat(this.rand, 0.6f, 1.0f);
                this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                double y = this.getEntityBoundingBox().minY + 0.5;
                worldServer.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, y, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.2f, new int[0]);
                worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, y, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.2f, new int[0]);
                this.lureTimer = MathHelper.getInt(this.rand, 20, 40);
            }
        } else if (this.catchDelay > 0) {
            this.catchDelay -= lootCount;
            float radius = 0.15f;
            if (this.catchDelay < 20) {
                radius = (float)((double)radius + (double)(20 - this.catchDelay) * 0.05);
            } else if (this.catchDelay < 40) {
                radius = (float)((double)radius + (double)(40 - this.catchDelay) * 0.02);
            } else if (this.catchDelay < 60) {
                radius = (float)((double)radius + (double)(60 - this.catchDelay) * 0.01);
            }
            if (this.rand.nextFloat() < radius) {
                float angle = MathHelper.nextFloat(this.rand, 0.0f, 360.0f) * ((float)Math.PI / 180);
                float spread = MathHelper.nextFloat(this.rand, 25.0f, 60.0f);

                double x = this.posX + (double)(MathHelper.sin(angle) * spread * 0.1f);
                double y = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0f);
                double z = this.posZ + (double)(MathHelper.cos(angle) * spread * 0.1f);

                IBlockState state = worldServer.getBlockState(new BlockPos((int)x, (int)y - 1, (int)z));
                if (state.getMaterial() == Material.WATER) {
                    worldServer.spawnParticle(EnumParticleTypes.WATER_SPLASH, x, y, z, 2 + this.rand.nextInt(2), 0.1f, 0.0, 0.1f, 0.0, new int[0]);
                }
            }
            if (this.catchDelay <= 0) {
                this.bobAngle = MathHelper.nextFloat(this.rand, 0.0f, 360.0f);
                this.bobMotion = MathHelper.getInt(this.rand, 20, 80);
            }
        } else {
            this.catchDelay = MathHelper.getInt(this.rand, 100, 600);
            this.catchDelay -= this.fishingLevel * 20 * 5;
        }
    }

    protected boolean isCollidableEntity(Entity entity) {
        return entity.canBeCollidedWith() || entity instanceof EntityItem;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
    }

    public int handleHookRetraction() {
        if (!this.world.isRemote && this.getOwnerLunaInternal() != null) {
            int result = 0;
            if (this.caughtEntity != null) {
                this.handleCatch();
                this.world.setEntityState(this, (byte)31);
                result = this.caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (this.lureTimer > 0) {
                LootContext.Builder builder = new LootContext.Builder((WorldServer)this.world);
                List<ItemStack> loots = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, builder.build());
                for (ItemStack loot : loots) {
                    LunaEntity luna = this.getOwnerLunaInternal();
                    luna.setHeldItemStack(loot);
                }
                this.lureTimer = 9999;
                result = 1;
            }
            if (this.isHooked) {
                result = 2;
            }
            return result;
        } else {
            return 0;
        }
    }

    protected void handleCatch() {
        LunaEntity luna = this.getOwnerLunaInternal();
        if (luna != null) {
            double dx = luna.posX - this.posX;
            double dy = luna.posY - this.posY;
            double dz = luna.posZ - this.posZ;
            this.caughtEntity.motionX += dx * 0.1;
            this.caughtEntity.motionY += dy * 0.1;
            this.caughtEntity.motionZ += dz * 0.1;
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return null;
    }


    static enum HookState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;
    }
}

