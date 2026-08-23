/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Custom;

import java.util.ArrayList;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.util.CustomPartCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.MatrixStack;
//cy
public class CustomModelEntity extends EntityLivingBase implements IAnimatable {
    final static float DESPAWN_DISTANCE = 11000.0f;
    final static public DataParameter<String> modelCode = EntityDataManager.createKey(CustomModelEntity.class, DataSerializers.STRING).getSerializer().createKey(101);
    final static public DataParameter<String> modelData = EntityDataManager.createKey(CustomModelEntity.class, DataSerializers.STRING).getSerializer().createKey(102);
    AnimationFactory factory = new AnimationFactory(this);
    public boolean isItemModel = false;
    public MatrixStack matrixStack = new MatrixStack();
    CustomPartCategory itemModelData = null;

    public CustomModelEntity(World world) {
        super(world);
        this.width = 0.1f;
        this.height = 0.1f;
    }

    public CustomModelEntity(World world, UUID uUID, String string) {
        this(world);
        this.dataManager.set(modelCode, uUID.toString());
        this.dataManager.set(modelData, string);
    }

    public static CustomModelEntity createCustomModelEntity(World world, UUID uUID, CustomPartCategory category) {
        CustomModelEntity entity = new CustomModelEntity(world);
        entity.getDataManager().set(modelCode, uUID.toString());
        entity.isItemModel = true;
        entity.itemModelData = category;
        return entity;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(modelCode, "");
        this.dataManager.register(modelData, "");
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = this.getPosition();
        Vec3i halfSize = new Vec3i(0.5, 0.5, 0.5);
        return new AxisAlignedBB(pos.subtract(halfSize), pos.add(halfSize));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        double dx = this.posX - x;
        double dy = this.posY - y;
        double dz = this.posZ - z;
        double distSq = dx * dx + dy * dy + dz * dz;
        return this.isInRangeToRenderDist(distSq);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 11000.0;
    }

    @Nullable
    public UUID getGirlUUID() {
        String code = this.dataManager.get(modelCode);
        return code.isEmpty() ? null : UUID.fromString(code);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source == DamageSource.OUT_OF_WORLD && super.attackEntityFrom(source, amount);
    }

    @Nullable
    public String getModelCode() {
        String data = this.dataManager.get(modelData);
        return data.isEmpty() ? null : data;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot entityEquipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot entityEquipmentSlot, ItemStack itemStack) {
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.LEFT;
    }
}

