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

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.*;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.gui.KoboldInventoryUI;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController.ISoundListener;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class LunaEntity
extends Fighter
implements bh_class82,
        IBeddableSexGirl {
    static public double ap = 0.01;
    public ItemStack ao = new ItemStack(LunaRod.LUNA_ROD);
    final static public DataParameter<Float> Y = EntityDataManager.createKey(LunaEntity.class, DataSerializers.FLOAT).getSerializer().createKey(121);
    final static public DataParameter<ItemStack> az = EntityDataManager.createKey(LunaEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(120);
    final static public DataParameter<Boolean> af = EntityDataManager.createKey(LunaEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(119);
    final static public DataParameter<ItemStack> ag = EntityDataManager.createKey(LunaEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(118);
    final static float ah = 3.0f;
    final static float ax = 1200.0f;
    @Nullable
    public LunaHookEntity lunaHookEntity;
    public float aa = 1.0f;
    public float Z = 0.0f;
    int aj = 8000;
    public boolean ac = false;
    int aw = 0;
    boolean ay = false;
    int ak = 0;
    int ab = 0;
    public BlockPos ai;
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

    public LunaEntity(World world) {
        super(world);
        this.P = 230;
        this.O = 150;
        this.K = 320;
        this.V = new Vec3d(0.0, -0.05999999718368053, 0.10000001192092894);
        if (this.items.getStackInSlot(0) == ItemStack.EMPTY) {
            this.items.setStackInSlot(0, new ItemStack(Items.IRON_AXE));
        }
        if (this.items.getStackInSlot(6) == ItemStack.EMPTY) {
            this.items.setStackInSlot(6, new ItemStack(Items.FISHING_ROD));
        }
    }

    @Override
    public String getGirlName() {
        return "Luna";
    }

    @Override
    public float float_i() {
        return -0.2f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(Y, Float.valueOf(0.0f));
        this.entityDataManager.register(az, ItemStack.EMPTY);
        this.entityDataManager.register(af, false);
        this.entityDataManager.register(ag, ItemStack.EMPTY);
    }

    @Override
    public void SetHome() {
        this.void_a("Love it here owo");
        this.PlaySound(SoundsHandler.GIRLS_LUNA_OWO, new int[0]);
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.currentAction() == Action.COWGIRL_SITTING_CUM && (action == Action.COWGIRL_SITTING_SLOW || action == Action.COWGIRL_SITTING_FAST)) {
            return;
        }
        if (this.currentAction() == Action.TOUCH_BOOBS_CUM && (action == Action.TOUCH_BOOBS_FAST || action == Action.TOUCH_BOOBS_SLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void void_b() {
        this.ac = true;
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
        if (this.world.isRemote && !this.boolean_b(entityPlayer)) {
            this.void_a(I18n.format("bia.dialogue.busy", new Object[0]));
        }
        return true;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        String[] LunaActions = new String[]{"action.names.sex", "action.names.touchboobs", "action.names.headpat"};
        ItemStack[] itemStackArray = new ItemStack[]{new ItemStack(Items.FISH, 3, 0), new ItemStack(Items.FISH, 2, 1), null};
        LunaEntity.CreateGUI(entityPlayer, (GirlEntity)this, LunaActions, itemStackArray);
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    protected static void CreateGUI(EntityPlayer entityPlayer, GirlEntity girlEntity, String[] stringArray, ItemStack[] itemStackArray) {
        Minecraft.getMinecraft().displayGuiScreen(new KoboldInventoryUI(girlEntity, entityPlayer, stringArray, itemStackArray, true));
    }

    public void b(ItemStack itemStack) {
        this.entityDataManager.set(ag, itemStack);
    }

    @Override
    public void void_g() {
        this.avoidWater = new EntityAIWanderAvoidWater(this, 0.35);
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.o);
        this.tasks.addTask(5, this.avoidWater);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (!this.boolean_J()) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        }
        this.void_m();
        this.void_i();
        this.entityDataManager.set(af, this.lunaHookEntity != null && this.entityDataManager.get(ag) == ItemStack.EMPTY);
        if (this.al == this.world.getTotalWorldTime() && this.lunaHookEntity != null) {
            this.world.removeEntity(this.lunaHookEntity);
            this.lunaHookEntity = null;
        }
        if (this.ay) {
            double d = this.net_minecraft_util_math_Vec3d_o().distanceTo(this.getPositionVector());
            if (d < 0.5 || this.ak > 200) {
                this.ay = false;
                this.ak = 0;
                this.entityDataManager.set(G, true);
                this.noClip = true;
                this.setNoGravity(true);
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                this.setCurrentAction(Action.WAIT_CAT);
            } else if (++this.ak == 60 || this.ak == 120) {
                this.getNavigator().clearPath();
                this.getNavigator().tryMoveToXYZ(this.net_minecraft_util_math_Vec3d_o().x, this.net_minecraft_util_math_Vec3d_o().y, this.net_minecraft_util_math_Vec3d_o().z, 0.2);
            }
        }
        if (this.ac) {
            ++this.aw;
            if (this.getPositionVector().equals(this.net_minecraft_util_math_Vec3d_o()) || this.aw > 40) {
                this.ac = false;
                this.aw = 0;
                this.void_b(this.world.getMinecraftServer().getPlayerList().getPlayerByUUID((UUID)this.getID()).rotationYaw + 180.0f);
                this.entityDataManager.set(G, true);
                this.getNavigator().clearPath();
                this.U();
            } else {
                this.rotationYaw = this.java_lang_Float_I().floatValue();
                this.setNoGravity(false);
                Vec3d vec3d = Reference.a(this.getPositionVector(), this.net_minecraft_util_math_Vec3d_o(), 40 - this.aw);
                this.setPosition(vec3d.x, vec3d.y, vec3d.z);
            }
        }
        this.void_d();
        this.entityDataManager.set(az, this.items.getStackInSlot(6));
    }

    void void_d() {
        ItemStack itemStack = this.ao;
        ItemStack itemStack2 = this.entityDataManager.get(az);
        if (itemStack2.equals(ItemStack.EMPTY)) {
            return;
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack2);
        EnchantmentHelper.setEnchantments(map, itemStack);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (Action.WAIT_CAT.equals((Object)this.currentAction())) {
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
            this.void_e(entityPlayer.getPersistentID());
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayer.setPositionAndUpdate(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z);
            this.setCurrentAction(Action.COWGIRL_SITTING_INTRO);
            entityPlayer.setRotationYawHead(this.java_lang_Float_I().floatValue() + 180.0f);
            entityPlayer.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
            entityPlayer.prevRotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
            this.r = this.java_lang_Float_I().floatValue() + 180.0f;
            this.moveCamera(0.0, -0.075f, -0.7109375, 0.0f, 0.0f);
            this.entityDataManager.set(D, 0);
        }
        ++this.ab;
    }

    @SideOnly(value=Side.CLIENT)
    void a(EntityPlayer entityPlayer, int n) {
        EntityPlayerSP entityPlayerSP;
        if (n == 0 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            fh_class313.b();
            entityPlayerSP.setVelocity(0.0, 0.0, 0.0);
            HandlePlayerMovement.a(false);
        }
        if (n == 25 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        }
    }

    @Override
    public void goToSexBed() {
        this.entityDataManager.set(G, false);
        this.setCurrentAction(Action.NULL);
        this.ar = true;
        BlockPos blockPos = this.net_minecraft_util_math_BlockPos_a(this.getPosition());
        if (blockPos == null) {
            this.PlaySound(SoundsHandler.GIRLS_LUNA_GIGGLE, new int[0]);
            PackageHandler.networkWrapper.sendToAllAround((IMessage)new SendChatMessage("<" + this.getGirlName() + "> Heh.. there is no bed nearby.. but I already ate the fish so nya~ hehe", this.dimension, this.girlID()), this.net_minecraftforge_fml_common_network_NetworkRegistry$TargetPoint_P());
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
                double d = this.getPosition().distanceSq(vec3d.add((Vec3d)vec3dArrayArray[n][0]).x, vec3d.add((Vec3d)vec3dArrayArray[n][0]).y, vec3d.add((Vec3d)vec3dArrayArray[n][0]).z);
                double d2 = this.getPosition().distanceSq(vec3d.add((Vec3d)vec3dArrayArray[i][0]).x, vec3d.add((Vec3d)vec3dArrayArray[i][0]).y, vec3d.add((Vec3d)vec3dArrayArray[i][0]).z);
                if (!(d2 < d)) continue;
                n = i;
            }
            if (n == -1) {
                this.PlaySound(SoundsHandler.GIRLS_LUNA_GIGGLE, new int[0]);
                this.void_a("Heh.. the bed is obscured.. but I already ate the fish so nya~ hehe");
                return;
            }
            Vec3d vec3d3 = vec3d.add(vec3dArrayArray[n][0]);
            this.void_b(nArray[n]);
            this.c(new Vec3d(vec3d3.x, vec3d3.y, vec3d3.z));
            this.r = this.java_lang_Float_I().floatValue();
            this.getNavigator().clearPath();
            this.getNavigator().tryMoveToXYZ(vec3d3.x, vec3d3.y, vec3d3.z, 0.2);
            this.ay = true;
            this.ak = 0;
        }
    }

    public void void_j() {
        EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, this.entityDataManager.get(ag));
        Vec3d vec3d = ck_class135.a(new Vec3d(0.0, (double)0.2f + Math.random() * (double)0.1f, (double)-0.2f + Math.random() * (double)-0.1f), this.rotationYaw);
        entityItem.motionX = vec3d.x;
        entityItem.motionY = vec3d.y;
        entityItem.motionZ = vec3d.z;
        this.world.spawnEntity(entityItem);
        this.entityDataManager.set(ag, ItemStack.EMPTY);
    }

    public void void_q() {
        this.ai = null;
        this.at = 0;
        this.as = 0;
        this.am = false;
        this.entityDataManager.set(G, false);
        this.entityDataManager.set(ag, ItemStack.EMPTY);
        this.setSilent(false);
        this.setCurrentAction(Action.NULL);
        if (this.lunaHookEntity != null) {
            this.world.removeEntity(this.lunaHookEntity);
            this.lunaHookEntity = null;
        }
        if (this.getID() != null) {
            return;
        }
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.o);
        if (this.boolean_J()) {
            return;
        }
        this.avoidWater = new EntityAIWanderAvoidWater(this, 0.35);
        this.tasks.addTask(5, this.avoidWater);
    }

    public void void_h() {
        this.void_q();
        if (++this.aq >= 3) {
            this.aq = 0;
            this.aj = 0;
        }
    }

    void void_i() {
        Object object;
        if (this.boolean_J() || this.getID() != null || this.ar) {
            if (this.entityDataManager.get(af).booleanValue()) {
                this.void_q();
            }
            return;
        }
        ++this.aj;
        if ((float)this.aj < 1200.0f) {
            return;
        }
        if (this.lunaHookEntity != null && this.lunaHookEntity.d == 15) {
            ((LunaRod)this.ao.getItem()).a(this.world, this, EnumHand.MAIN_HAND);
            this.al = this.world.getTotalWorldTime() + 20L;
            object = this.entityDataManager.get(ag);
            if (object != ItemStack.EMPTY) {
                if (((ItemStack)object).getItem() instanceof ItemFood) {
                    this.setCurrentAction(Action.FISHING_EAT);
                } else {
                    this.setCurrentAction(Action.FISHING_THROW_AWAY);
                }
            }
        }
        if (!this.currentAction().toString().toLowerCase().contains("fishing")) {
            this.void_n();
            this.void_e();
        }
        if (this.ai != null && this.au == null && this.getNavigator().getPath() == null && !this.inWater && this.onGround) {
            object = this.world.rayTraceBlocks(this.getPositionVector().add(0.0, this.getEyeHeight(), 0.0), new Vec3d(this.ai.getX(), this.ai.getY(), this.ai.getZ()), true);
            this.setSilent(true);
            if (this.avoidWater != null) {
                this.tasks.removeTask(this.avoidWater);
                this.avoidWater = null;
            }
            if (this.o != null) {
                this.tasks.removeTask(this.o);
                this.o = null;
            }
            if (this.currentAction() == Action.NULL) {
                this.setCurrentAction(Action.FISHING_START);
                this.c(this.getPositionVector());
                this.entityDataManager.set(G, true);
                this.void_b((float)Math.atan2(this.posZ - (double)this.ai.getZ(), this.posX - (double)this.ai.getX()) * 57.29578f + 90.0f);
            }
            return;
        }
        this.au = this.getNavigator().getPath();
    }

    public void void_o() {
        this.an.add(this.ai);
        this.void_q();
    }

    void void_e() {
        if (this.ai == null) {
            return;
        }
        PathNavigate pathNavigate = this.getNavigator();
        pathNavigate.tryMoveToXYZ(this.ai.getX(), this.ai.getY(), this.ai.getZ(), 0.35f);
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
        while (++n < 50 && (blockPos = this.a(this.getPosition(), n + 1, Blocks.WATER, 60, 10, new HashSet<Biome>(Arrays.asList(Biomes.RIVER, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.BEACH, Biomes.STONE_BEACH, Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND)))) != null) {
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
        if (this.ai == null || this.at < n2) {
            this.ai = blockPos2;
            this.at = n2;
        }
        if (this.ai.equals(blockPos2)) {
            this.as = 0;
        } else if (++this.as > 20) {
            this.ai = blockPos2;
            this.at = n2;
        }
    }

    void void_m() {
        Path path = this.getNavigator().getPath();
        if (path == null) {
            return;
        }
        PathPoint pathPoint = path.getFinalPathPoint();
        PathPoint pathPoint2 = new PathPoint(be_class78.a(this.posX), be_class78.a(this.posY), be_class78.a(this.posZ));
        if (pathPoint == null) {
            return;
        }
        this.entityDataManager.set(Y, Float.valueOf(pathPoint.distanceTo(pathPoint2)));
    }

    @Override
    public void a(String string, UUID uUID) {
        super.a(string, uUID);
        if ("action.names.touchboobs".equals(string)) {
            this.void_e(uUID);
            this.a(true, true, uUID);
            this.changeDataParameterFromClient("animationFollowUp", "touch_boobs");
            this.changeDataParameterFromClient("currentModel", "0");
            HandlePlayerMovement.a(false);
        }
        if ("action.names.sex".equals(string)) {
            this.void_e(uUID);
            this.a(true, true, uUID);
            this.changeDataParameterFromClient("animationFollowUp", "sex");
            HandlePlayerMovement.a(false);
        }
        if ("action.names.headpat".equals(string)) {
            this.void_e(uUID);
            this.a(true, true, uUID);
            HandlePlayerMovement.a(false);
            this.changeDataParameterFromClient("animationFollowUp", "headpat");
        }
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW) {
            return Action.TOUCH_BOOBS_FAST;
        }
        if (action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_FAST;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW || action == Action.TOUCH_BOOBS_FAST) {
            return Action.TOUCH_BOOBS_CUM;
        }
        if (action == Action.COWGIRL_SITTING_FAST || action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_CUM;
        }
        return null;
    }

    @Override
    protected void U() {
        switch ((String)this.entityDataManager.get(h)) {
            case "touch_boobs": {
                if (this.currentAction() != Action.PAYMENT) {
                    this.setCurrentAction(Action.PAYMENT);
                    return;
                }
                this.setCurrentAction(Action.TOUCH_BOOBS_INTRO);
                break;
            }
            case "sex": {
                if (this.currentAction() != Action.PAYMENT) {
                    this.setCurrentAction(Action.PAYMENT);
                } else {
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendGirlToSex(this.girlID()));
                    PackageHandler.networkWrapper.sendToServer((IMessage)new ResetGirl(this.girlID()));
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
            this.entityDataManager.set(h, "");
        }
    }

    @Override
    protected void playHurtSound(DamageSource damageSource) {
        this.PlaySound(SoundsHandler.GIRLS_LUNA_OUU, new int[0]);
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.cat.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.cat.blink", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.cat.null", true, animationEvent);
                    break;
                }
                if (this.isRiding()) {
                    this.createAnimation("animation.cat.sit", true, animationEvent);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    if (this.onGround && Math.abs(Math.abs(this.prevPosY) - Math.abs(this.posY)) < (double)0.1f) {
                        this.createAnimation(this.entityDataManager.get(Y).floatValue() < 3.0f ? "animation.cat.walk" : "animation.cat.run", true, animationEvent);
                    } else {
                        this.createAnimation("animation.cat.fly", true, animationEvent);
                    }
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation("animation.cat.idle" + (this.ad ? "2" : ""), true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.cat.null", true, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.cat.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case RIDE: 
                    case SIT: {
                        this.createAnimation("animation.cat.sit", true, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.cat.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.cat.throwpearl", true, animationEvent);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.cat.downed", true, animationEvent);
                        break block5;
                    }
                    case FISHING_START: {
                        this.createAnimation("animation.cat.start_fishing", false, animationEvent);
                        break block5;
                    }
                    case FISHING_IDLE: {
                        this.createAnimation("animation.cat.idle_fishing", true, animationEvent);
                        break block5;
                    }
                    case FISHING_EAT: {
                        this.createAnimation("animation.cat.eat_fishing", false, animationEvent);
                        break block5;
                    }
                    case FISHING_THROW_AWAY: {
                        this.createAnimation("animation.cat.throw_away", false, animationEvent);
                        break block5;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.cat.payment", false, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_INTRO: {
                        this.createAnimation("animation.cat.touch_boobs_intro", false, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_SLOW: {
                        this.createAnimation("animation.cat.touch_boobs_slow" + (this.ae ? "1" : ""), true, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_FAST: {
                        this.createAnimation("animation.cat.touch_boobs_fast", true, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_CUM: {
                        this.createAnimation("animation.cat.touch_boobs_cum", false, animationEvent);
                        break block5;
                    }
                    case WAIT_CAT: {
                        this.createAnimation("animation.cat.wait", false, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_INTRO: {
                        this.createAnimation("animation.cat.sitting_intro", false, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_SLOW: {
                        this.createAnimation("animation.cat.sitting_slow", true, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_FAST: {
                        this.createAnimation("animation.cat.sitting_fast", true, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_CUM: {
                        this.createAnimation("animation.cat.sitting_cum", false, animationEvent);
                        break block5;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.cat.head_pat", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        if (this.actionController == null) {
            this.void_p();
        }
        ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.S != 3) break;
                    this.S = 0;
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
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "start_fishingDone": {
                    if (!this.boolean_e()) break;
                    this.setCurrentAction(Action.FISHING_IDLE);
                    break;
                }
                case "rod_shoot": {
                    if (!this.boolean_e()) break;
                    PackageHandler.networkWrapper.sendToServer((IMessage)new CatActivateFishing(this.girlID()));
                    break;
                }
                case "eat": {
                    this.PlaySoundAtPosition(SoundsHandler.a(SoundsHandler.MISC_EAT), 0.5f + 0.5f * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                    this.aa -= 0.33333334f;
                    break;
                }
                case "eatPay": {
                    this.PlaySoundAtPosition(SoundsHandler.a(SoundsHandler.MISC_EAT), 0.5f + 0.5f * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                    this.n -= 0.33333334f;
                    break;
                }
                case "burp": {
                    this.PlaySoundAtPosition(SoundEvents.ENTITY_PLAYER_BURP, 0.5f, this.rand.nextFloat() * 0.1f + 0.9f);
                    break;
                }
                case "eatingDone": {
                    if (this.boolean_e()) {
                        PackageHandler.networkWrapper.sendToServer((IMessage)new CatEatingDone(this.girlID()));
                        this.setCurrentAction(Action.NULL);
                    }
                    this.aa = 1.0f;
                    this.Z = 0.0f;
                    break;
                }
                case "throw_away": {
                    if (this.boolean_e()) {
                        PackageHandler.networkWrapper.sendToServer((IMessage)new CatThrowAwayItem(this.girlID()));
                    }
                    this.aa = 1.0f;
                    this.Z = 0.0f;
                    break;
                }
                case "renderItem": {
                    this.Z = 1.0f;
                    break;
                }
                case "paymentMSG1": {
                    this.a(this.getID(), "Here, I know u like fish and yea.. these are for you");
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "paymentMSG2": {
                    this.void_a("huh~?");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HUH, new int[0]);
                    break;
                }
                case "paymentMSG3": {
                    this.void_a("nyyyaaaa~ :D");
                    int[] nArray = new int[]{1, 7, 10, 11};
                    int n = nArray[this.getRNG().nextInt(nArray.length)];
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[n]);
                    break;
                }
                case "paymentMSG4": {
                    this.void_a("tankuuuu owowowo");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_OWO, new int[0]);
                    break;
                }
                case "paymentDone": {
                    if (this.boolean_e()) {
                        this.U();
                    }
                    this.n = 1.0f;
                    break;
                }
                case "breath": 
                case "rod_breath": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING, new int[0]);
                    break;
                }
                case "happyOh": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HAPPYOH, new int[0]);
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
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HUH, new int[0]);
                    break;
                }
                case "hmph": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HMPH, new int[0]);
                    break;
                }
                case "hehe": 
                case "giggle": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_GIGGLE, new int[0]);
                    break;
                }
                case "singing": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_SINGING, new int[0]);
                    break;
                }
                case "touch_boobsMSG1": {
                    this.void_a("comon~ touch me hihi~");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_GIGGLE, new int[0]);
                    break;
                }
                case "touch": {
                    this.PlaySound(SoundsHandler.MISC_TOUCH, new int[0]);
                    break;
                }
                case "jump": {
                    this.a(SoundsHandler.MISC_JUMP[0], 0.2f);
                    break;
                }
                case "horninya": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA, new int[0]);
                    break;
                }
                case "horninya2": 
                case "touch_boobs_cumMSG3": 
                case "sitting_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[1]);
                    this.a(SoundsHandler.MISC_CUMINFLATION[0], 5.0f);
                    break;
                }
                case "moan": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                    break;
                }
                case "touch_boobs_introDone": {
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    SexUI.d();
                    HandlePlayerMovement.a(false);
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
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "addCumFast": {
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    break;
                }
                case "moanOrNya": {
                    if (Math.random() > 0.5) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                        break;
                    }
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    break;
                }
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "touch_boobs_cumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                    break;
                }
                case "resetGirl": {
                    if (!this.boolean_e()) break;
                    this.void_r();
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
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_GIGGLE, new int[0]);
                    this.void_a("come here - big guy hehe~");
                    break;
                }
                case "pounding": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "sitting_introMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_GIGGLE, new int[0]);
                    this.void_a("hehe~");
                    break;
                }
                case "sitting_introDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.d();
                    break;
                }
                case "sitting_slowMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        if (this.getRNG().nextBoolean()) {
                            this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_HORNINYA));
                            break;
                        }
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                    } else {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "sitting_fastMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    } else {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "sitting_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    Vec3d vec3d = new Vec3d(0.0, -0.075f, -0.7109375);
                    Vec3d vec3d2 = ck_class135.a(vec3d, this.java_lang_Float_I().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.net_minecraft_util_math_Vec3d_o().x + vec3d2.x, this.net_minecraft_util_math_Vec3d_o().y + vec3d2.y, this.net_minecraft_util_math_Vec3d_o().z + vec3d2.z);
                    break;
                }
                case "sitting_fastTp": {
                    if (!this.boolean_n()) break;
                    Vec3d vec3d = new Vec3d(0.0, -0.160625, -0.9925);
                    Vec3d vec3d3 = ck_class135.a(vec3d, this.java_lang_Float_I().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.net_minecraft_util_math_Vec3d_o().x + vec3d3.x, this.net_minecraft_util_math_Vec3d_o().y + vec3d3.y, this.net_minecraft_util_math_Vec3d_o().z + vec3d3.z);
                    break;
                }
                case "headpatMSG1": {
                    this.void_a("huh?~");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HUH, new int[0]);
                    break;
                }
                case "headpatMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_MMM, new int[0]);
                    break;
                }
                case "headpatMSG3": {
                    this.void_a("nya~");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[0]);
                }
            }
        };
        this.movementController.transitionLengthTicks = 10.0;
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setNoGravity(false);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class CreeperLogic {
        @SubscribeEvent
        public void ScareCreepers(EntityJoinWorldEvent entityJoinWorldEvent) {
            Entity entity = entityJoinWorldEvent.getEntity();
            if (entity instanceof EntityCreeper) {
                EntityCreeper creeper = (EntityCreeper)entity;
                creeper.tasks.addTask(3, new EntityAIAvoidEntity<LunaEntity>(creeper, LunaEntity.class, 6.0f, 1.0, 1.2));
            }
        }
    }
}

