/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Pre
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.LootTableLoadEvent
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerLoggedOutEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.girls.Allie.lamp;

import java.util.HashSet;
import java.util.List;

import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.world.WorldUtils;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class LampItem extends Item implements IAnimatable {
    final static String ALLIE_IN_USE = "sexmodAllieInUse";
    final static String ALLIE_IN_USE_TICKS = "sexmodAllieInUseTicks";
    final static public String USES = "sexmodUses";
    final static public String ALLIE_ID = "sexmodAllieID";
    final static Integer SUMMON_TICK = 95;
    final static Integer PARTICLE_START_TICK = 50;
    final static public int PARTICLE_COUNT = 150;
    final static public float PARTICLE_SPREAD = 0.75f;

    final static public LampItem LAMP_ITEM = new LampItem();
    final private AnimationFactory factory = new AnimationFactory(this);
    AnimationController<LampItem> controller;

    public LampItem() {
        this.setCreativeTab(CreativeTabs.MISC);
        this.maxStackSize = 1;
    }

    public static void RegisterLamp() {
        LAMP_ITEM.setRegistryName("sexmod", "allies_lamp");
        LAMP_ITEM.setTranslationKey("allies_lamp");
        MinecraftForge.EVENT_BUS.register(LampItem.class);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(LAMP_ITEM);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(LAMP_ITEM, 0, new ModelResourceLocation("sexmod:allies_lamp"));
        LAMP_ITEM.setTileEntityItemStackRenderer(new LampRenderer());
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void hideHotbar(RenderGameOverlayEvent.Pre event) {
        NBTTagCompound nBTTagCompound = Minecraft.getMinecraft().player.getEntityData();
        if (nBTTagCompound.getBoolean(ALLIE_IN_USE)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void putInChests(LootTableLoadEvent event) {
        HashSet<ResourceLocation> lootChests = new HashSet<ResourceLocation>();
        lootChests.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
        lootChests.add(LootTableList.CHESTS_DESERT_PYRAMID);
        lootChests.add(LootTableList.CHESTS_SIMPLE_DUNGEON);
        lootChests.add(LootTableList.CHESTS_WOODLAND_MANSION);
        if (lootChests.contains(event.getName())) {
            LootPool lootPool = event.getTable().getPool("pool3");
            if (lootPool == null) {
                lootPool = event.getTable().getPool("pool2");
            }
            if (lootPool != null) {
                lootPool.addEntry(new LootEntryItem(LAMP_ITEM, 5, 0, new LootFunction[0], new LootCondition[0], "sexmod:allies_lamp"));
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        this.controller = new AnimationController<LampItem>(this, "controller", 2.0f, this::predicate);
        animationData.addAnimationController(this.controller);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag iTooltipFlag) {
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        if (nBTTagCompound == null) {
            return;
        }
        int n = 3 - itemStack.getTagCompound().getInteger(USES);
        switch (n) {
            case 2: {
                list.add("2 wishes left");
                break;
            }
            case 1: {
                list.add("1 wish left");
                break;
            }
            case 0: {
                list.add("no wishes left");
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        NBTTagCompound nBTTagCompound = entityPlayerSP.getEntityData();
        boolean bl = nBTTagCompound.getBoolean(ALLIE_IN_USE);
        if (!bl) {
            event.getController().clearAnimationCache();
            return PlayState.STOP;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lamp.rub", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        return PlayState.CONTINUE;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int n, boolean bl) {
        Vec3d vec3d;
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)entity;
        NBTTagCompound nBTTagCompound = entity.getEntityData();
        if (!itemStack.equals(entityPlayer.getHeldItemMainhand()) && !itemStack.equals(entityPlayer.getHeldItemOffhand())) {
            return;
        }
        boolean bl2 = nBTTagCompound.getBoolean(ALLIE_IN_USE);
        int n2 = nBTTagCompound.getInteger(ALLIE_IN_USE_TICKS);
        if (!bl2) {
            return;
        }
        nBTTagCompound.setInteger(ALLIE_IN_USE_TICKS, n2 + 1);
        if (n2 > PARTICLE_START_TICK && n2 < SUMMON_TICK) {
            double d = (float)(n2 - PARTICLE_START_TICK) / (float)(SUMMON_TICK - PARTICLE_START_TICK);
            d = RotationHelper.smoothStep(d);
            vec3d = new Vec3d(0.0, (double)entityPlayer.eyeHeight * (1.0 - d), 0.0);
            WorldUtils.SpawnParticleRing(world, EnumParticleTypes.CRIT_MAGIC, this.getLampOffset(entityPlayer).add(vec3d), (int)(d * 150.0), d * 0.75, d);
        }
        if (n2 < SUMMON_TICK) {
            return;
        }
        WorldUtils.SpawnParticleRing(world, EnumParticleTypes.CRIT_MAGIC, this.getLampOffset(entityPlayer), 150, 0.75, 2.0);
        nBTTagCompound.setBoolean(ALLIE_IN_USE, false);
        nBTTagCompound.setInteger(ALLIE_IN_USE_TICKS, 0);
        if (world.isRemote) {
            HandlePlayerMovement.setMovementLock(false);
            return;
        }
        NBTTagCompound nBTTagCompound2 = itemStack.getTagCompound();
        if (nBTTagCompound2 == null) {
            nBTTagCompound2 = new NBTTagCompound();
        }
        nBTTagCompound2.setInteger(USES, nBTTagCompound2.getInteger(USES) + 1);
        AllieEntity allie = new AllieEntity(entityPlayer.world, entityPlayer.getHeldItemMainhand());
        allie.setInteractionPlayerUUID(entityPlayer.getPersistentID());
        vec3d = this.getLampOffset(entityPlayer);
        allie.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, entityPlayer.rotationYaw + 180.0f, entityPlayer.rotationPitch);
        allie.setTargetPosition(allie.getPositionVector());
        allie.setYawRotation(entityPlayer.rotationYaw + 180.0f);
        allie.setAnchored(true);
        allie.setNoGravity(true);
        allie.noClip = true;
        entityPlayer.world.spawnEntity(allie);
        BlockPos blockPos = allie.getPosition().add(0, -1, 0);
        if (allie.world.getBlockState(blockPos).getBlock().equals(Blocks.SAND)) {
            allie.setCurrentAction(Action.SUMMON_SAND);
        } else {
            allie.setCurrentAction(allie.hasLampItem() ? Action.SUMMON : Action.SUMMON_NORMAL);
        }
        itemStack.setTagCompound(nBTTagCompound2);
    }

    Vec3d getLampOffset(EntityPlayer entityPlayer) {
        return entityPlayer.getPositionVector().add(VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 2.0), entityPlayer.rotationYawHead));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            event.player.getEntityData().setBoolean(LampItem.ALLIE_IN_USE, false);
        }

        @SubscribeEvent
        public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            EntityPlayer player = event.getEntityPlayer();
            EnumHand hand = event.getHand();
            ItemStack stack = player.getHeldItem(hand);
            if (!PlayerGirl.isOwnerPlayer(player)) {
                if (!player.world.isRemote || HandlePlayerMovement.isActive()) {
                    if (!player.world.isRemote) {
                        for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                            if (!girl.isDead && girl instanceof AllieEntity && stack.equals(((AllieEntity) girl).getDataManager().get(AllieEntity.LAMP_ITEM))) {
                                return;
                            }
                        }
                    }
                    if (stack.getItem() == LAMP_ITEM) {
                        NBTTagCompound nBTTagCompound = stack.getTagCompound();
                        if (nBTTagCompound == null || nBTTagCompound.getInteger(LampItem.USES) < 3) {
                            NBTTagCompound nBTTagCompound2 = player.getEntityData();
                            boolean bl = nBTTagCompound2.getBoolean(LampItem.ALLIE_IN_USE);
                            if (!bl) {
                                nBTTagCompound2.setBoolean(LampItem.ALLIE_IN_USE, true);
                                nBTTagCompound2.setInteger(LampItem.ALLIE_IN_USE_TICKS, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}

