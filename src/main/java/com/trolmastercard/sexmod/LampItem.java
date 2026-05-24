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
package com.trolmastercard.sexmod;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;

import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
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

public class LampItem
extends Item
implements IAnimatable {
    final static String e = "sexmodAllieInUse";
    final static String d = "sexmodAllieInUseTicks";
    final static public String j = "sexmodUses";
    final static public String h = "sexmodAllieID";
    final static Integer c = 95;
    final static Integer k = 50;
    final static public int a = 150;
    final static public float f = 0.75f;
    final static public LampItem LAMP_ITEM = new LampItem();
    final private AnimationFactory i = new AnimationFactory(this);
    AnimationController<LampItem> g;

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
    public static void a(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(LAMP_ITEM);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void a(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation((Item) LAMP_ITEM, 0, (ModelResourceLocation)new ModelResourceLocation("sexmod:allies_lamp"));
        LAMP_ITEM.setTileEntityItemStackRenderer(new f0_class284());
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderGameOverlayEvent.Pre pre) {
        NBTTagCompound nBTTagCompound = Minecraft.getMinecraft().player.getEntityData();
        if (nBTTagCompound.getBoolean(e)) {
            pre.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void a(LootTableLoadEvent lootTableLoadEvent) {
        HashSet<ResourceLocation> hashSet = new HashSet<ResourceLocation>();
        hashSet.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
        hashSet.add(LootTableList.CHESTS_DESERT_PYRAMID);
        hashSet.add(LootTableList.CHESTS_SIMPLE_DUNGEON);
        hashSet.add(LootTableList.CHESTS_WOODLAND_MANSION);
        if (hashSet.contains(lootTableLoadEvent.getName())) {
            LootPool lootPool = lootTableLoadEvent.getTable().getPool("pool3");
            if (lootPool == null) {
                lootPool = lootTableLoadEvent.getTable().getPool("pool2");
            }
            if (lootPool != null) {
                lootPool.addEntry(new LootEntryItem((Item) LAMP_ITEM, 5, 0, new LootFunction[0], new LootCondition[0], "sexmod:allies_lamp"));
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        this.g = new AnimationController<LampItem>(this, "controller", 2.0f, this::a);
        animationData.addAnimationController(this.g);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag iTooltipFlag) {
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        if (nBTTagCompound == null) {
            return;
        }
        int n = 3 - itemStack.getTagCompound().getInteger(j);
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
    protected <segs extends IAnimatable> PlayState a(AnimationEvent<segs> animationEvent) {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        NBTTagCompound nBTTagCompound = entityPlayerSP.getEntityData();
        boolean bl = nBTTagCompound.getBoolean(e);
        if (!bl) {
            animationEvent.getController().clearAnimationCache();
            return PlayState.STOP;
        }
        animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lamp.rub", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
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
        boolean bl2 = nBTTagCompound.getBoolean(e);
        int n2 = nBTTagCompound.getInteger(d);
        if (!bl2) {
            return;
        }
        nBTTagCompound.setInteger(d, n2 + 1);
        if (n2 > k && n2 < c) {
            double d = (float)(n2 - k) / (float)(c - k);
            d = Reference.h(d);
            vec3d = new Vec3d(0.0, (double)entityPlayer.eyeHeight * (1.0 - d), 0.0);
            cj_class134.a(world, EnumParticleTypes.CRIT_MAGIC, this.a(entityPlayer).add(vec3d), (int)(d * 150.0), d * 0.75, d);
        }
        if (n2 < c) {
            return;
        }
        cj_class134.a(world, EnumParticleTypes.CRIT_MAGIC, this.a(entityPlayer), 150, 0.75, 2.0);
        nBTTagCompound.setBoolean(e, false);
        nBTTagCompound.setInteger(d, 0);
        if (world.isRemote) {
            HandlePlayerMovement.a(false);
            return;
        }
        NBTTagCompound nBTTagCompound2 = itemStack.getTagCompound();
        if (nBTTagCompound2 == null) {
            nBTTagCompound2 = new NBTTagCompound();
        }
        nBTTagCompound2.setInteger(j, nBTTagCompound2.getInteger(j) + 1);
        AllieEntity ev_class2752 = new AllieEntity(entityPlayer.world, entityPlayer.getHeldItemMainhand());
        ev_class2752.void_e(entityPlayer.getPersistentID());
        vec3d = this.a(entityPlayer);
        ev_class2752.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, entityPlayer.rotationYaw + 180.0f, entityPlayer.rotationPitch);
        ev_class2752.c(ev_class2752.getPositionVector());
        ev_class2752.void_b(entityPlayer.rotationYaw + 180.0f);
        ev_class2752.void_a(true);
        ev_class2752.setNoGravity(true);
        ev_class2752.noClip = true;
        entityPlayer.world.spawnEntity(ev_class2752);
        BlockPos blockPos = ev_class2752.getPosition().add(0, -1, 0);
        if (ev_class2752.world.getBlockState(blockPos).getBlock().equals(Blocks.SAND)) {
            ev_class2752.setCurrentAction(Action.SUMMON_SAND);
        } else {
            ev_class2752.setCurrentAction(ev_class2752.boolean_f() ? Action.SUMMON : Action.SUMMON_NORMAL);
        }
        itemStack.setTagCompound(nBTTagCompound2);
    }

    Vec3d a(EntityPlayer entityPlayer) {
        return entityPlayer.getPositionVector().add(ck_class135.a(new Vec3d(0.0, 0.0, 2.0), entityPlayer.rotationYawHead));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.i;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner38 {
        @SubscribeEvent
        public void a(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent) {
            playerLoggedOutEvent.player.getEntityData().setBoolean(LampItem.e, false);
        }

        @SubscribeEvent
        public void a(PlayerInteractEvent.RightClickItem rightClickItem) {
            EntityPlayer entityPlayer = rightClickItem.getEntityPlayer();
            EnumHand enumHand = rightClickItem.getHand();
            ItemStack itemStack = entityPlayer.getHeldItem(enumHand);
            if (PlayerGirl.e(entityPlayer)) {
                return;
            }
            if (entityPlayer.world.isRemote && !HandlePlayerMovement.b()) {
                return;
            }
            if (!entityPlayer.world.isRemote) {
                try {
                    for (GirlEntity object2 : GirlEntity.GirlEntityList()) {
                        AllieEntity bl;
                        ItemStack itemStack2;
                        if (object2.isDead || !(object2 instanceof AllieEntity) || !itemStack.equals(itemStack2 = (bl = (AllieEntity)object2).getDataManager().get(AllieEntity.itemStack))) continue;
                        return;
                    }
                } catch (ConcurrentModificationException concurrentModificationException) {
                    // empty catch block
                }
            }
            if (itemStack.getItem() != LAMP_ITEM) {
                return;
            }
            NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
            if (nBTTagCompound != null && nBTTagCompound.getInteger(LampItem.j) >= 3) {
                return;
            }
            NBTTagCompound nBTTagCompound2 = entityPlayer.getEntityData();
            boolean bl = nBTTagCompound2.getBoolean(LampItem.e);
            if (bl) {
                return;
            }
            nBTTagCompound2.setBoolean(LampItem.e, true);
            nBTTagCompound2.setInteger(LampItem.d, 0);
        }

        private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
            return concurrentModificationException;
        }
    }
}

