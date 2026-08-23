/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.items.CapabilityItemHandler
 *  net.minecraftforge.items.ItemStackHandler
 */
package com.trolmastercard.sexmod.girls.base;

import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.companion.fighter.FighterCompanion;
import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.Packets.SetNewHome;
import com.trolmastercard.sexmod.Packets.PlayerAction;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class Fighter extends GirlEntity {
    public int nextAttack = 1;
    public int slashSwordRot;
    public int stabSwordRot = 0;
    public int holdBowRot;
    public Vec3d swordOffsetStab = Vec3d.ZERO;
    public boolean downed;
    public ItemStackHandler inventory = new ItemStackHandler(7);
    final static public DataParameter<ItemStack> WEAPON;
    final static public DataParameter<ItemStack> BOW;
    final static public DataParameter<ItemStack> HELMET_SLOT;
    final static public DataParameter<ItemStack> CHEST_SLOT;
    final static public DataParameter<ItemStack> LEGS_SLOT;
    final static public DataParameter<ItemStack> BOOTS_SLOT;
    final static public DataParameter<Integer> ATTACK_MODE;

    //sorted
    static {
        WEAPON = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(117);
        BOW = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(116);
        HELMET_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(115);
        CHEST_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(114);
        LEGS_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(113);
        BOOTS_SLOT = EntityDataManager.createKey(Fighter.class, DataSerializers.ITEM_STACK).getSerializer().createKey(112);
        ATTACK_MODE = EntityDataManager.createKey(Fighter.class, DataSerializers.VARINT).getSerializer().createKey(111);
    }

    protected Fighter(World worldIn) {
        super(worldIn);
        if (this.inventory.getStackInSlot(0) == ItemStack.EMPTY) {
            this.inventory.setStackInSlot(0, new ItemStack(Items.IRON_SWORD));
        }
        if (this.inventory.getStackInSlot(1) == ItemStack.EMPTY) {
            this.inventory.setStackInSlot(1, new ItemStack(Items.BOW));
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(ATTACK_MODE, 0);
        this.entityDataManager.register(WEAPON, ItemStack.EMPTY);
        this.entityDataManager.register(BOW, ItemStack.EMPTY);
        this.entityDataManager.register(HELMET_SLOT, ItemStack.EMPTY);
        this.entityDataManager.register(CHEST_SLOT, ItemStack.EMPTY);
        this.entityDataManager.register(LEGS_SLOT, ItemStack.EMPTY);
        this.entityDataManager.register(BOOTS_SLOT, ItemStack.EMPTY);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new FighterCompanion(this));
    }

    public void SetHome() {
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.ticksExisted % 80 == 0 && this.getHealth() != this.getMaxHealth()) {
            if (!this.hasMaster()) {
                this.heal(1.0f);
            } else {
                List<EntityMob> mobs = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(new BlockPos(this.posX - 7.0, this.posY - 1.0, this.posZ - 7.0), new BlockPos(this.posX + 7.0, this.posY + 1.0, this.posZ + 7.0)));
                int healAmount = mobs.isEmpty() ? 4 : 1;
                this.heal((float) healAmount);
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.HEART, false, this.posX, this.posY + 1.0 + Reference.RANDOM.nextDouble(), this.posZ, healAmount, 1.0, 1.0, 1.0, Reference.RANDOM.nextGaussian(), new int[0]);
            }
        }
        if (this.downed && !this.hasMaster()) {
            this.downed = false;
        }
        this.entityDataManager.set(HAND_STATES, Byte.valueOf("1"));
        this.entityDataManager.set(WEAPON, this.inventory.getStackInSlot(0));
        this.entityDataManager.set(BOW, this.inventory.getStackInSlot(1));
        this.entityDataManager.set(HELMET_SLOT, this.inventory.getStackInSlot(2));
        this.entityDataManager.set(CHEST_SLOT, this.inventory.getStackInSlot(3));
        this.entityDataManager.set(LEGS_SLOT, this.inventory.getStackInSlot(4));
        this.entityDataManager.set(BOOTS_SLOT, this.inventory.getStackInSlot(5));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void doAction(String action, UUID player) {
        if ("action.names.followme".equals(action)) {
            this.changeDataParameterFromClient("master", player.toString());
        } else if ("action.names.stopfollowme".equals(action)) {
            this.goHome();
        } else if ("action.names.equipment".equals(action)) {
            EntityPlayerSP playerEntity = Minecraft.getMinecraft().player;
            PacketHandler.INSTANCE.sendToServer(new PlayerAction(this.girlID(), playerEntity.getPersistentID()));
        } else if ("action.names.gohome".equals(action)) {
            this.goHome();
            PacketHandler.INSTANCE.sendToServer(new SendCompanionHome(this.girlID()));
        } else if ("action.names.setnewhome".equals(action)) {
            this.SetHome();
            PacketHandler.INSTANCE.sendToServer(new SetNewHome(this.girlID(), new Vec3d(this.getPosition())));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setTag("inventory", this.inventory.serializeNBT());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
    }
}

