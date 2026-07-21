/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.items.CapabilityItemHandler
 *  net.minecraftforge.items.ItemStackHandler
 */
package com.trolmastercard.sexmod;

import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SetNewHome;
import com.trolmastercard.sexmod.Packages.bo_class90;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class Fighter extends GirlEntity {
    public int S = 1;
    public int P;
    public int O = 0;
    public int K;
    public Vec3d V = Vec3d.ZERO;
    public boolean N;
    public ItemStackHandler items = new ItemStackHandler(7);
    final static public DataParameter<ItemStack> ITEM_SLOT_1 = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(117);
    final static public DataParameter<ItemStack> ITEM_SLOT_2 = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(116);
    final static public DataParameter<ItemStack> HELMET_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(115);
    final static public DataParameter<ItemStack> CHEST_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(114);
    final static public DataParameter<ItemStack> LEGS_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(113);
    final static public DataParameter<ItemStack> BOOTS_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(112);
    final static public DataParameter<Integer> M = EntityDataManager.createKey(Fighter.class, DataSerializers.VARINT).getSerializer().createKey(111);

    protected Fighter(World world) {
        super(world);
        if (this.items.getStackInSlot(0) == ItemStack.EMPTY) {
            this.items.setStackInSlot(0, new ItemStack(Items.IRON_SWORD));
        }
        if (this.items.getStackInSlot(1) == ItemStack.EMPTY) {
            this.items.setStackInSlot(1, new ItemStack(Items.BOW));
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(M, 0);
        this.entityDataManager.register(ITEM_SLOT_1, ItemStack.EMPTY);
        this.entityDataManager.register(ITEM_SLOT_2, ItemStack.EMPTY);
        this.entityDataManager.register(HELMET_SLOT, ItemStack.EMPTY);
        this.entityDataManager.register(CHEST_SLOT, ItemStack.EMPTY);
        this.entityDataManager.register(LEGS_SLOT, ItemStack.EMPTY);
        this.entityDataManager.register(BOOTS_SLOT, ItemStack.EMPTY);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new FighterAI(this));
    }

    public void SetHome() {
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.ticksExisted % 80 == 0 && this.getHealth() != this.getMaxHealth()) {
            if (!this.boolean_J()) {
                this.heal(1.0f);
            } else {
                List<EntityMob> entityList = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(new BlockPos(this.posX - 7.0, this.posY - 1.0, this.posZ - 7.0), new BlockPos(this.posX + 7.0, this.posY + 1.0, this.posZ + 7.0)));
                int n = entityList.isEmpty() ? 4 : 1;
                this.heal(n);
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.HEART, false, this.posX, this.posY + 1.0 + Reference.RANDOM.nextDouble(), this.posZ, n, 1.0, 1.0, 1.0, Reference.RANDOM.nextGaussian(), new int[0]);
            }
        }
        if (this.N && !this.boolean_J()) {
            this.N = false;
        }
        this.entityDataManager.set(HAND_STATES, Byte.valueOf("1"));
        this.entityDataManager.set(ITEM_SLOT_1, this.items.getStackInSlot(0));
        this.entityDataManager.set(ITEM_SLOT_2, this.items.getStackInSlot(1));
        this.entityDataManager.set(HELMET_SLOT, this.items.getStackInSlot(2));
        this.entityDataManager.set(CHEST_SLOT, this.items.getStackInSlot(3));
        this.entityDataManager.set(LEGS_SLOT, this.items.getStackInSlot(4));
        this.entityDataManager.set(BOOTS_SLOT, this.items.getStackInSlot(5));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void a(String string, UUID uUID) {
        if ("action.names.followme".equals(string)) {
            this.changeDataParameterFromClient("master", uUID.toString());
        } else if ("action.names.stopfollowme".equals(string)) {
            this.goHome();
        } else if ("action.names.equipment".equals(string)) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            PackageHandler.networkWrapper.sendToServer((IMessage)new bo_class90(this.girlID(), player.getPersistentID()));
        } else if ("action.names.gohome".equals(string)) {
            this.goHome();
            PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
        } else if ("action.names.setnewhome".equals(string)) {
            this.SetHome();
            PackageHandler.networkWrapper.sendToServer((IMessage)new SetNewHome(this.girlID(), new Vec3d(this.getPosition())));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setTag("inventory", this.items.serializeNBT());
        super.writeEntityToNBT(nBTTagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.items.deserializeNBT(nBTTagCompound.getCompoundTag("inventory"));
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing enumFacing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, enumFacing);
    }

    public <T> T getCapability(Capability<T> capability, EnumFacing enumFacing) {
        return (T)(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.items : super.getCapability(capability, enumFacing));
    }
}

