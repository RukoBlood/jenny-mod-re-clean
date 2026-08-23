/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Luna;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.Packets.*;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.Luna.FishingRod.LunaHookEntity;
import com.trolmastercard.sexmod.girls.Luna.FishingRod.LunaRod;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
import com.trolmastercard.sexmod.util.interfaces.IEllie;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController.ISoundListener;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class LunaEntity extends Fighter implements IEllie, IBeddableSexGirl {
    static public double ap = 0.01;
    public ItemStack lunaRod = new ItemStack(LunaRod.LUNA_ROD);
    final static public DataParameter<Float> TARGET_DISTANCE;
    final static public DataParameter<ItemStack> FISHING_ROD;
    final static public DataParameter<Boolean> IS_FISHING;
    final static public DataParameter<ItemStack> CAUGHT_ITEM;

    final static float LUNA_WALK_SPEED = 3.0f;
    final static float HUNGER_START_EATING_TICK = 1200.0f;

    @Nullable
    public LunaHookEntity fishEntity;
    public float fishSizePercentage = 1.0f;
    public float throwBackPercentage = 0.0f;
    int hunger = 8000;
    public boolean isPreparingTalk = false;
    int aw = 0;
    boolean ay = false;
    int ak = 0;
    int ab = 0;
    public BlockPos chosenFishingSpot;
    int at = 0;
    int as = 0;
    boolean am;
    long al = 0L;
    boolean ar = false;
    Path au = null;
    int aq = 0;
    HashSet<BlockPos> an = new HashSet();
    boolean ae = false;
    boolean ad = false;

    static {
        TARGET_DISTANCE = EntityDataManager.createKey(LunaEntity.class, DataSerializers.FLOAT).getSerializer().createKey(121);
        FISHING_ROD = EntityDataManager.createKey(LunaEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(120);
        IS_FISHING = EntityDataManager.createKey(LunaEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(119);
        CAUGHT_ITEM = EntityDataManager.createKey(LunaEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(118);
    }

    public LunaEntity(World world) {
        super(world);
        this.slashSwordRot = 230;
        this.stabSwordRot = 150;
        this.holdBowRot = 320;
        this.swordOffsetStab = new Vec3d(0.0, -0.05999999718368053, 0.10000001192092894);
        if (this.inventory.getStackInSlot(0) == ItemStack.EMPTY) {
            this.inventory.setStackInSlot(0, new ItemStack(Items.IRON_AXE));
        }
        if (this.inventory.getStackInSlot(6) == ItemStack.EMPTY) {
            this.inventory.setStackInSlot(6, new ItemStack(Items.FISHING_ROD));
        }
    }

    @Override
    public String getGirlName() {
        return "Luna";
    }

    @Override
    public float getScaleFactor() {
        return -0.2f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(TARGET_DISTANCE, 0.0f);
        this.entityDataManager.register(FISHING_ROD, ItemStack.EMPTY);
        this.entityDataManager.register(IS_FISHING, false);
        this.entityDataManager.register(CAUGHT_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void SetHome() {
        this.sendChatMessage("Love it here owo");
        this.playRandomSound(SoundsHandler.GIRLS_LUNA_OWO);
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.COWGIRL_SITTING_CUM && (action == Action.COWGIRL_SITTING_SLOW || action == Action.COWGIRL_SITTING_FAST)) {
            return;
        }
        if (this.getCurrentAction() == Action.TOUCH_BOOBS_CUM && (action == Action.TOUCH_BOOBS_FAST || action == Action.TOUCH_BOOBS_SLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void setDismounted() {
        this.isPreparingTalk = true;
    }

    @Override
    public float getEyeHeight() {
        return 1.34f;
    }

    @Override
    public boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        boolean bl;
        if (super.processInteract(entityPlayer, enumHand)) {
            return true;
        }
        ItemStack itemStack = entityPlayer.getHeldItem(enumHand);
        boolean bl2 = bl = itemStack.getItem() == Items.NAME_TAG;
        if (bl) {
            itemStack.interactWithEntity(entityPlayer, this, enumHand);
            return true;
        }
        if (this.world.isRemote && !this.openInteractionMenu(entityPlayer)) {
            this.sendChatMessage(I18n.format("bia.dialogue.busy"));
        }
        return true;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        String[] LunaActions = new String[]{"action.names.sex", "action.names.touchboobs", "action.names.headpat"};
        ItemStack[] itemStackArray = new ItemStack[]{new ItemStack(Items.FISH, 3, 0), new ItemStack(Items.FISH, 2, 1), null};
        LunaEntity.CreateGUI(player, this, LunaActions, itemStackArray);
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    protected static void CreateGUI(EntityPlayer entityPlayer, GirlEntity girlEntity, String[] stringArray, ItemStack[] itemStackArray) {
        Minecraft.getMinecraft().displayGuiScreen(new FighterUI(girlEntity, entityPlayer, stringArray, itemStackArray, true));
    }

    public void setHeldItemStack(ItemStack itemStack) {
        this.entityDataManager.set(CAUGHT_ITEM, itemStack);
    }

    @Override
    public void reInitTasks() {
        this.aiWander = new EntityAIWanderAvoidWater(this, 0.35);
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.watchClosestGirlGoal);
        this.tasks.addTask(5, this.aiWander);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (!this.hasMaster()) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        }
        this.void_m();
        this.void_i();
        this.entityDataManager.set(IS_FISHING, this.fishEntity != null && this.entityDataManager.get(CAUGHT_ITEM) == ItemStack.EMPTY);
        if (this.al == this.world.getTotalWorldTime() && this.fishEntity != null) {
            this.world.removeEntity(this.fishEntity);
            this.fishEntity = null;
        }
        if (this.ay) {
            double d = this.getTargetPosition().distanceTo(this.getPositionVector());
            if (d < 0.5 || this.ak > 200) {
                this.ay = false;
                this.ak = 0;
                this.entityDataManager.set(IS_ANCHORED, true);
                this.noClip = true;
                this.setNoGravity(true);
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                this.setCurrentAction(Action.WAIT_CAT);
            } else if (++this.ak == 60 || this.ak == 120) {
                this.getNavigator().clearPath();
                this.getNavigator().tryMoveToXYZ(this.getTargetPosition().x, this.getTargetPosition().y, this.getTargetPosition().z, 0.2);
            }
        }
        if (this.isPreparingTalk) {
            ++this.aw;
            if (this.getPositionVector().equals(this.getTargetPosition()) || this.aw > 40) {
                this.isPreparingTalk = false;
                this.aw = 0;
                this.setYawRotation(this.world.getMinecraftServer().getPlayerList().getPlayerByUUID(this.getInteractionPlayerUUID()).rotationYaw + 180.0f);
                this.entityDataManager.set(IS_ANCHORED, true);
                this.getNavigator().clearPath();
                this.doAction();
            } else {
                this.rotationYaw = this.getYawRotation().floatValue();
                this.setNoGravity(false);
                Vec3d vec3d = RotationHelper.lerpVec3d(this.getPositionVector(), this.getTargetPosition(), 40 - this.aw);
                this.setPosition(vec3d.x, vec3d.y, vec3d.z);
            }
        }
        this.void_d();
        this.entityDataManager.set(FISHING_ROD, this.inventory.getStackInSlot(6));
    }

    void void_d() {
        ItemStack itemStack = this.lunaRod;
        ItemStack itemStack2 = this.entityDataManager.get(FISHING_ROD);
        if (itemStack2.equals(ItemStack.EMPTY)) {
            return;
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack2);
        EnchantmentHelper.setEnchantments(map, itemStack);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (Action.WAIT_CAT.equals(this.getCurrentAction())) {
            this.void_f();
        } else {
            this.ab = 0;
        }
    }

    void void_f() {
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getDistance(this) > 1.25f) {
            return;
        }
        if (this.world.isRemote) {
            this.a(entityPlayer, this.ab);
        } else if (this.ab == 25) {
            this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayer.setPositionAndUpdate(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z);
            this.setCurrentAction(Action.COWGIRL_SITTING_INTRO);
            entityPlayer.setRotationYawHead(this.getYawRotation().floatValue() + 180.0f);
            entityPlayer.rotationYaw = this.getYawRotation().floatValue() + 180.0f;
            entityPlayer.prevRotationYaw = this.getYawRotation().floatValue() + 180.0f;
            this.cameraYaw = this.getYawRotation().floatValue() + 180.0f;
            this.moveCamera(0.0, -0.075f, -0.7109375, 0.0f, 0.0f);
            this.entityDataManager.set(OUTFIT_INDEX, 0);
        }
        ++this.ab;
    }

    @SideOnly(value=Side.CLIENT)
    void a(EntityPlayer entityPlayer, int n) {
        EntityPlayerSP entityPlayerSP;
        if (n == 0 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            BlackScreenUI.run();
            entityPlayerSP.setVelocity(0.0, 0.0, 0.0);
            HandlePlayerMovement.setMovementLock(false);
        }
        if (n == 25 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        }
    }

    @Override
    public void goToSexBed() {
        this.entityDataManager.set(IS_ANCHORED, false);
        this.setCurrentAction(Action.NULL);
        this.ar = true;
        BlockPos blockPos = this.getNearestBed(this.getPosition());
        if (blockPos == null) {
            this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
            PacketHandler.INSTANCE.sendToAllAround(new SendChatMessage("<" + this.getGirlName() + "> Heh.. there is no bed nearby.. but I already ate the fish so nya~ hehe", this.dimension, this.girlID()), this.getTargetNetworkPoint());
        } else {
            Vec3d vec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            int[] nArray = new int[]{0, 180, -90, 90};
            Vec3d[][] vec3dArrayArray = new Vec3d[][]{{new Vec3d(0.5, 0.0, -0.5), new Vec3d(0.0, 0.0, -1.0)}, {new Vec3d(0.5, 0.0, 1.5), new Vec3d(0.0, 0.0, 1.0)}, {new Vec3d(-0.5, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0)}, {new Vec3d(1.5, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0)}};
            int n = -1;
            for (int i = 0; i < vec3dArrayArray.length; ++i) {
                Vec3d vec3d2 = vec3d.add(vec3dArrayArray[i][1]);
                if (this.world.getBlockState(new BlockPos(vec3d2.x, vec3d2.y, vec3d2.z)).getBlock() != Blocks.AIR) continue;
                if (n == -1) {
                    n = i;
                    continue;
                }
                double d = this.getPosition().distanceSq(vec3d.add(vec3dArrayArray[n][0]).x, vec3d.add(vec3dArrayArray[n][0]).y, vec3d.add(vec3dArrayArray[n][0]).z);
                double d2 = this.getPosition().distanceSq(vec3d.add(vec3dArrayArray[i][0]).x, vec3d.add(vec3dArrayArray[i][0]).y, vec3d.add(vec3dArrayArray[i][0]).z);
                if (!(d2 < d)) continue;
                n = i;
            }
            if (n == -1) {
                this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                this.sendChatMessage("Heh.. the bed is obscured.. but I already ate the fish so nya~ hehe");
                return;
            }
            Vec3d vec3d3 = vec3d.add(vec3dArrayArray[n][0]);
            this.setYawRotation(nArray[n]);
            this.setTargetPosition(new Vec3d(vec3d3.x, vec3d3.y, vec3d3.z));
            this.cameraYaw = this.getYawRotation();
            this.getNavigator().clearPath();
            this.getNavigator().tryMoveToXYZ(vec3d3.x, vec3d3.y, vec3d3.z, 0.2);
            this.ay = true;
            this.ak = 0;
        }
    }

    public void void_j() {
        EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, this.entityDataManager.get(CAUGHT_ITEM));
        Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.0, (double)0.2f + Math.random() * (double)0.1f, (double)-0.2f + Math.random() * (double)-0.1f), this.rotationYaw);
        entityItem.motionX = vec3d.x;
        entityItem.motionY = vec3d.y;
        entityItem.motionZ = vec3d.z;
        this.world.spawnEntity(entityItem);
        this.entityDataManager.set(CAUGHT_ITEM, ItemStack.EMPTY);
    }

    public void void_q() {
        this.chosenFishingSpot = null;
        this.at = 0;
        this.as = 0;
        this.am = false;
        this.entityDataManager.set(IS_ANCHORED, false);
        this.entityDataManager.set(CAUGHT_ITEM, ItemStack.EMPTY);
        this.setSilent(false);
        this.setCurrentAction(Action.NULL);
        if (this.fishEntity != null) {
            this.world.removeEntity(this.fishEntity);
            this.fishEntity = null;
        }
        if (this.getInteractionPlayerUUID() != null) {
            return;
        }
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.watchClosestGirlGoal);
        if (this.hasMaster()) {
            return;
        }
        this.aiWander = new EntityAIWanderAvoidWater(this, 0.35);
        this.tasks.addTask(5, this.aiWander);
    }

    public void void_h() {
        this.void_q();
        if (++this.aq >= 3) {
            this.aq = 0;
            this.hunger = 0;
        }
    }

    void void_i() {
        Object object;
        if (this.hasMaster() || this.getInteractionPlayerUUID() != null || this.ar) {
            if (this.entityDataManager.get(IS_FISHING).booleanValue()) {
                this.void_q();
            }
            return;
        }
        ++this.hunger;
        if ((float)this.hunger < 1200.0f) {
            return;
        }
        if (this.fishEntity != null && this.fishEntity.lureTimer == 15) {
            ((LunaRod)this.lunaRod.getItem()).onItemRightClick(this.world, this, EnumHand.MAIN_HAND);
            this.al = this.world.getTotalWorldTime() + 20L;
            object = this.entityDataManager.get(CAUGHT_ITEM);
            if (object != ItemStack.EMPTY) {
                if (((ItemStack)object).getItem() instanceof ItemFood) {
                    this.setCurrentAction(Action.FISHING_EAT);
                } else {
                    this.setCurrentAction(Action.FISHING_THROW_AWAY);
                }
            }
        }
        if (!this.getCurrentAction().toString().toLowerCase().contains("fishing")) {
            this.void_n();
            this.void_e();
        }
        if (this.chosenFishingSpot != null && this.au == null && this.getNavigator().getPath() == null && !this.inWater && this.onGround) {
            object = this.world.rayTraceBlocks(this.getPositionVector().add(0.0, this.getEyeHeight(), 0.0), new Vec3d(this.chosenFishingSpot.getX(), this.chosenFishingSpot.getY(), this.chosenFishingSpot.getZ()), true);
            this.setSilent(true);
            if (this.aiWander != null) {
                this.tasks.removeTask(this.aiWander);
                this.aiWander = null;
            }
            if (this.watchClosestGirlGoal != null) {
                this.tasks.removeTask(this.watchClosestGirlGoal);
                this.watchClosestGirlGoal = null;
            }
            if (this.getCurrentAction() == Action.NULL) {
                this.setCurrentAction(Action.FISHING_START);
                this.setTargetPosition(this.getPositionVector());
                this.entityDataManager.set(IS_ANCHORED, true);
                this.setYawRotation((float)Math.atan2(this.posZ - (double)this.chosenFishingSpot.getZ(), this.posX - (double)this.chosenFishingSpot.getX()) * 57.29578f + 90.0f);
            }
            return;
        }
        this.au = this.getNavigator().getPath();
    }

    public void addCaughtItem() {
        this.an.add(this.chosenFishingSpot);
        this.void_q();
    }

    void void_e() {
        if (this.chosenFishingSpot == null) {
            return;
        }
        PathNavigate pathNavigate = this.getNavigator();
        pathNavigate.tryMoveToXYZ(this.chosenFishingSpot.getX(), this.chosenFishingSpot.getY(), this.chosenFishingSpot.getZ(), 0.35f);
        Path path = pathNavigate.getPath();
        if (path == null) {
            return;
        }
        if (path.getCurrentPathLength() > path.getCurrentPathIndex() + 1) {
            PathPoint pathPoint = path.getPathPointFromIndex(path.getCurrentPathIndex() + 1);
            PathPoint pathPoint2 = path.getPathPointFromIndex(path.getCurrentPathLength() - 1);
            Vec3d vec3d = new Vec3d(pathPoint2.x, pathPoint2.y, pathPoint2.z);
            BlockPos blockPos = new BlockPos(pathPoint.x, pathPoint.y, pathPoint.z);
            if (this.getPositionVector().distanceTo(vec3d) < 0.75) {
                pathNavigate.clearPath();
                this.setPosition(vec3d.x, vec3d.y, vec3d.z);
            }
            if (this.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.WATER) {
                pathNavigate.clearPath();
            }
            if (this.world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
                pathNavigate.clearPath();
            }
            if (this.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.WATER) {
                pathNavigate.clearPath();
            }
        }
    }

    void void_n() {
        BlockPos blockPos;
        int n = 0;
        BlockPos blockPos2 = null;
        int n2 = 0;
        while (++n < 50 && (blockPos = this.findNearestStructureBlock(this.getPosition(), n + 1, Blocks.WATER, 60, 10, new HashSet<Biome>(Arrays.asList(Biomes.RIVER, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.BEACH, Biomes.STONE_BEACH, Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND)))) != null) {
            while (this.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.WATER) {
                blockPos = blockPos.add(0, 1, 0);
            }
            int n3 = 1;
            BlockPos blockPos3 = blockPos;
            while (this.world.getBlockState(blockPos3.add(0, -1, 0)).getBlock() == Blocks.WATER) {
                blockPos3 = blockPos3.add(0, -1, 0);
                ++n3;
            }
            if (this.an.contains(blockPos)) continue;
            if (blockPos2 == null) {
                blockPos2 = blockPos;
                n2 = n3;
                continue;
            }
            if (n3 <= n2) continue;
            blockPos2 = blockPos;
            n2 = n3;
            if (n2 < 6) continue;
            break;
        }
        if (blockPos2 == null) {
            return;
        }
        if (this.chosenFishingSpot == null || this.at < n2) {
            this.chosenFishingSpot = blockPos2;
            this.at = n2;
        }
        if (this.chosenFishingSpot.equals(blockPos2)) {
            this.as = 0;
        } else if (++this.as > 20) {
            this.chosenFishingSpot = blockPos2;
            this.at = n2;
        }
    }

    void void_m() {
        Path path = this.getNavigator().getPath();
        if (path == null) {
            return;
        }
        PathPoint pathPoint = path.getFinalPathPoint();
        PathPoint pathPoint2 = new PathPoint(ThreadNames.Round(this.posX), ThreadNames.Round(this.posY), ThreadNames.Round(this.posZ));
        if (pathPoint == null) {
            return;
        }
        this.entityDataManager.set(TARGET_DISTANCE, Float.valueOf(pathPoint.distanceTo(pathPoint2)));
    }

    @Override
    public void doAction(String action, UUID player) {
        super.doAction(action, player);
        if ("action.names.touchboobs".equals(action)) {
            this.setInteractionPlayerUUID(player);
            this.triggerActionSync(true, true, player);
            this.changeDataParameterFromClient("animationFollowUp", "touch_boobs");
            this.changeDataParameterFromClient("currentModel", "0");
            HandlePlayerMovement.setMovementLock(false);
        }
        if ("action.names.sex".equals(action)) {
            this.setInteractionPlayerUUID(player);
            this.triggerActionSync(true, true, player);
            this.changeDataParameterFromClient("animationFollowUp", "sex");
            HandlePlayerMovement.setMovementLock(false);
        }
        if ("action.names.headpat".equals(action)) {
            this.setInteractionPlayerUUID(player);
            this.triggerActionSync(true, true, player);
            HandlePlayerMovement.setMovementLock(false);
            this.changeDataParameterFromClient("animationFollowUp", "headpat");
        }
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW) {
            return Action.TOUCH_BOOBS_FAST;
        }
        if (action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW || action == Action.TOUCH_BOOBS_FAST) {
            return Action.TOUCH_BOOBS_CUM;
        }
        if (action == Action.COWGIRL_SITTING_FAST || action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_CUM;
        }
        return null;
    }

    @Override
    protected void doAction() {
        switch (this.entityDataManager.get(GIRL_HAND_STATES)) {
            case "touch_boobs": {
                if (this.getCurrentAction() != Action.PAYMENT) {
                    this.setCurrentAction(Action.PAYMENT);
                    return;
                }
                this.setCurrentAction(Action.TOUCH_BOOBS_INTRO);
                break;
            }
            case "sex": {
                if (this.getCurrentAction() != Action.PAYMENT) {
                    this.setCurrentAction(Action.PAYMENT);
                } else {
                    PacketHandler.INSTANCE.sendToServer(new SendGirlToSex(this.girlID()));
                    PacketHandler.INSTANCE.sendToServer(new ResetGirl(this.girlID()));
                }
                return;
            }
            case "headpat": {
                this.setCurrentAction(Action.HEAD_PAT);
            }
        }
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("animationFollowUp", "");
        } else {
            this.entityDataManager.set(GIRL_HAND_STATES, "");
        }
    }

    @Override
    protected void playHurtSound(DamageSource damageSource) {
        this.playRandomSound(SoundsHandler.GIRLS_LUNA_OUU);
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        if (this.getRNG().nextFloat() * 100.0f > 95.0f) {
            return SoundsHandler.GIRLS_ALLIE_SCAWY[2];
        }
        return SoundsHandler.GIRLS_LUNA_OUU[12];
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0);
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return this.isInWater() ? 1.0f : 0.5f;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.cat.null", true, event);
                    break;
                }
                this.createAnimation("animation.cat.blink", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.cat.null", true, event);
                    break;
                }
                if (this.isRiding()) {
                    this.createAnimation("animation.cat.sit", true, event);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    if (this.onGround && Math.abs(Math.abs(this.prevPosY) - Math.abs(this.posY)) < (double)0.1f) {
                        this.createAnimation(this.entityDataManager.get(TARGET_DISTANCE).floatValue() < 3.0f ? "animation.cat.walk" : "animation.cat.run", true, event);
                    } else {
                        this.createAnimation("animation.cat.fly", true, event);
                    }
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation("animation.cat.idle" + (this.ad ? "2" : ""), true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.cat.null", true, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.cat.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case RIDE: 
                    case SIT: {
                        this.createAnimation("animation.cat.sit", true, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.cat.bowcharge", false, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.cat.throwpearl", true, event);
                        break;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.cat.downed", true, event);
                        break;
                    }
                    case FISHING_START: {
                        this.createAnimation("animation.cat.start_fishing", false, event);
                        break;
                    }
                    case FISHING_IDLE: {
                        this.createAnimation("animation.cat.idle_fishing", true, event);
                        break;
                    }
                    case FISHING_EAT: {
                        this.createAnimation("animation.cat.eat_fishing", false, event);
                        break;
                    }
                    case FISHING_THROW_AWAY: {
                        this.createAnimation("animation.cat.throw_away", false, event);
                        break;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.cat.payment", false, event);
                        break;
                    }
                    case TOUCH_BOOBS_INTRO: {
                        this.createAnimation("animation.cat.touch_boobs_intro", false, event);
                        break;
                    }
                    case TOUCH_BOOBS_SLOW: {
                        this.createAnimation("animation.cat.touch_boobs_slow" + (this.ae ? "1" : ""), true, event);
                        break;
                    }
                    case TOUCH_BOOBS_FAST: {
                        this.createAnimation("animation.cat.touch_boobs_fast", true, event);
                        break;
                    }
                    case TOUCH_BOOBS_CUM: {
                        this.createAnimation("animation.cat.touch_boobs_cum", false, event);
                        break;
                    }
                    case WAIT_CAT: {
                        this.createAnimation("animation.cat.wait", false, event);
                        break;
                    }
                    case COWGIRL_SITTING_INTRO: {
                        this.createAnimation("animation.cat.sitting_intro", false, event);
                        break;
                    }
                    case COWGIRL_SITTING_SLOW: {
                        this.createAnimation("animation.cat.sitting_slow", true, event);
                        break;
                    }
                    case COWGIRL_SITTING_FAST: {
                        this.createAnimation("animation.cat.sitting_fast", true, event);
                        break;
                    }
                    case COWGIRL_SITTING_CUM: {
                        this.createAnimation("animation.cat.sitting_cum", false, event);
                        break;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.cat.head_pat", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "idleDone": {
                    this.ad = this.getRNG().nextInt(10) == 0;
                    break;
                }
                case "idle2Done": {
                    this.ad = false;
                    break;
                }
                case "pearl": {
                    PacketHandler.INSTANCE.sendToServer(new SendCompanionHome(this.girlID()));
                    break;
                }
                case "start_fishingDone": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.setCurrentAction(Action.FISHING_IDLE);
                    break;
                }
                case "rod_shoot": {
                    if (!this.isLocalPlayerNearby()) break;
                    PacketHandler.INSTANCE.sendToServer(new CatActivateFishing(this.girlID()));
                    break;
                }
                case "eat": {
                    this.PlaySoundAtPosition(SoundsHandler.random(SoundsHandler.MISC_EAT), 0.5f + 0.5f * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                    this.fishSizePercentage -= 0.33333334f;
                    break;
                }
                case "eatPay": {
                    this.PlaySoundAtPosition(SoundsHandler.random(SoundsHandler.MISC_EAT), 0.5f + 0.5f * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                    this.scaleFactor -= 0.33333334f;
                    break;
                }
                case "burp": {
                    this.PlaySoundAtPosition(SoundEvents.ENTITY_PLAYER_BURP, 0.5f, this.rand.nextFloat() * 0.1f + 0.9f);
                    break;
                }
                case "eatingDone": {
                    if (this.isLocalPlayerNearby()) {
                        PacketHandler.INSTANCE.sendToServer(new CatEatingDone(this.girlID()));
                        this.setCurrentAction(Action.NULL);
                    }
                    this.fishSizePercentage = 1.0f;
                    this.throwBackPercentage = 0.0f;
                    break;
                }
                case "throw_away": {
                    if (this.isLocalPlayerNearby()) {
                        PacketHandler.INSTANCE.sendToServer(new CatThrowAwayItem(this.girlID()));
                    }
                    this.fishSizePercentage = 1.0f;
                    this.throwBackPercentage = 0.0f;
                    break;
                }
                case "renderItem": {
                    this.throwBackPercentage = 1.0f;
                    break;
                }
                case "paymentMSG1": {
                    this.sendChatMessageToPlayer(this.getInteractionPlayerUUID(), "Here, I know u like fish and yea.. these are for you");
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "paymentMSG2": {
                    this.sendChatMessage("huh~?");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "paymentMSG3": {
                    this.sendChatMessage("nyyyaaaa~ :D");
                    int[] nArray = new int[]{1, 7, 10, 11};
                    int n = nArray[this.getRNG().nextInt(nArray.length)];
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[n]);
                    break;
                }
                case "paymentMSG4": {
                    this.sendChatMessage("tankuuuu owowowo");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_OWO);
                    break;
                }
                case "paymentDone": {
                    if (this.isLocalPlayerNearby()) {
                        this.doAction();
                    }
                    this.scaleFactor = 1.0f;
                    break;
                }
                case "breath": 
                case "rod_breath": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING);
                    break;
                }
                case "happyOh": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HAPPYOH);
                    break;
                }
                case "cutenya3": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[3]);
                    break;
                }
                case "cutenya2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[2]);
                    break;
                }
                case "huh": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "hmph": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HMPH);
                    break;
                }
                case "hehe": 
                case "giggle": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    break;
                }
                case "singing": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_SINGING);
                    break;
                }
                case "touch_boobsMSG1": {
                    this.sendChatMessage("comon~ touch me hihi~");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    break;
                }
                case "touch": {
                    this.playRandomSound(SoundsHandler.MISC_TOUCH);
                    break;
                }
                case "jump": {
                    this.playSoundAtVolume(SoundsHandler.MISC_JUMP[0], 0.2f);
                    break;
                }
                case "horninya": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HORNINYA);
                    break;
                }
                case "horninya2": 
                case "touch_boobs_cumMSG3": 
                case "sitting_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[1]);
                    this.playSoundAtVolume(SoundsHandler.MISC_CUMINFLATION[0], 5.0f);
                    break;
                }
                case "moan": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                    break;
                }
                case "touch_boobs_introDone": {
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    SexUI.showUI();
                    HandlePlayerMovement.setMovementLock(false);
                    break;
                }
                case "touch_boobs_slowDone": {
                    if (this.ae) {
                        this.ae = false;
                        break;
                    }
                    this.ae = Math.random() < 0.5;
                    break;
                }
                case "addCumSlow": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "addCumFast": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    break;
                }
                case "moanOrNya": {
                    if (Math.random() > 0.5) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                        break;
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "touch_boobs_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "resetGirl": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.resetCameraAndPhysics();
                    break;
                }
                case "touch_boobs_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[3]);
                    break;
                }
                case "touch_boobs_cumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[9]);
                    break;
                }
                case "call_playerMSG1": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    this.sendChatMessage("come here - big guy hehe~");
                    break;
                }
                case "pounding": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "sitting_introMSG1": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    this.sendChatMessage("hehe~");
                    break;
                }
                case "sitting_introDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.showUI();
                    break;
                }
                case "sitting_slowMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        if (this.getRNG().nextBoolean()) {
                            this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_HORNINYA));
                            break;
                        }
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "sitting_fastMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_LUNA_MOAN));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "sitting_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    Vec3d vec3d = new Vec3d(0.0, -0.075f, -0.7109375);
                    Vec3d vec3d2 = VectorMath.rotateByYaw(vec3d, this.getYawRotation() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.getTargetPosition().x + vec3d2.x, this.getTargetPosition().y + vec3d2.y, this.getTargetPosition().z + vec3d2.z);
                    break;
                }
                case "sitting_fastTp": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Vec3d vec3d = new Vec3d(0.0, -0.160625, -0.9925);
                    Vec3d vec3d3 = VectorMath.rotateByYaw(vec3d, this.getYawRotation().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.getTargetPosition().x + vec3d3.x, this.getTargetPosition().y + vec3d3.y, this.getTargetPosition().z + vec3d3.z);
                    break;
                }
                case "headpatMSG1": {
                    this.sendChatMessage("huh?~");
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "headpatMSG2": {
                    this.playRandomSound(SoundsHandler.GIRLS_LUNA_MMM);
                    break;
                }
                case "headpatMSG3": {
                    this.sendChatMessage("nya~");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[0]);
                }
            }
        };
        this.movementController.transitionLengthTicks = 10.0;
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setNoGravity(false);
    }

    public static class EventHandler {
        @SubscribeEvent
        public void makeCreepersBeAfraid(EntityJoinWorldEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof EntityCreeper) {
                EntityCreeper creeper = (EntityCreeper)entity;
                creeper.tasks.addTask(3, new EntityAIAvoidEntity<LunaEntity>(creeper, LunaEntity.class, 6.0f, 1.0, 1.2));
            }
        }
    }
}

