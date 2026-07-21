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

import com.trolmastercard.sexmod.EnumCustomPartCategory;
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
public class CustomModelEntity
extends EntityLivingBase
implements IAnimatable {
    final static float e = 11000.0f;
    final static public DataParameter<String> a = EntityDataManager.createKey(CustomModelEntity.class, DataSerializers.STRING).getSerializer().createKey(101);
    final static public DataParameter<String> b = EntityDataManager.createKey(CustomModelEntity.class, DataSerializers.STRING).getSerializer().createKey(102);
    AnimationFactory g = new AnimationFactory(this);
    public boolean f = false;
    public MatrixStack c = new MatrixStack();
    EnumCustomPartCategory d = null;

    public CustomModelEntity(World world) {
        super(world);
        this.width = 0.1f;
        this.height = 0.1f;
    }

    public CustomModelEntity(World world, UUID uUID, String string) {
        this(world);
        this.dataManager.set(a, uUID.toString());
        this.dataManager.set(b, string);
    }

    public static CustomModelEntity a(World world, UUID uUID, EnumCustomPartCategory gw_class3892) {
        CustomModelEntity cy_class1532 = new CustomModelEntity(world);
        cy_class1532.getDataManager().set(a, uUID.toString());
        cy_class1532.f = true;
        cy_class1532.d = gw_class3892;
        return cy_class1532;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(a, "");
        this.dataManager.register(b, "");
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos blockPos = this.getPosition();
        Vec3i vec3i = new Vec3i(0.5, 0.5, 0.5);
        return new AxisAlignedBB(blockPos.subtract(vec3i), blockPos.add(vec3i));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isInRangeToRender3d(double d, double d2, double d3) {
        double d4 = this.posX - d;
        double d5 = this.posY - d2;
        double d6 = this.posZ - d3;
        double d7 = d4 * d4 + d5 * d5 + d6 * d6;
        return this.isInRangeToRenderDist(d7);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean isInRangeToRenderDist(double d) {
        return d < 11000.0;
    }

    @Nullable
    public UUID b() {
        String string = this.dataManager.get(a);
        if ("".equals(string)) {
            return null;
        }
        return UUID.fromString(string);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (damageSource != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        return super.attackEntityFrom(damageSource, f);
    }

    @Nullable
    public String a() {
        String string = this.dataManager.get(b);
        if ("".equals(string)) {
            return null;
        }
        return string;
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
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.g;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return new ArrayList<ItemStack>();
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

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

