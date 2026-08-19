/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerChangedDimensionEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.girls.Galath.GalathCoin;

import java.util.Random;
import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.InformOfOwnership;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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

public class GalathCoin extends Item implements IAnimatable {
    final static public GalathCoin GALATH_COIN = new GalathCoin();
    final static public long c = 4000L;
    final static public long g = 1000L;
    final static public long j = 3000L;
    final static public float q = 0.1f;
    final static public float p = -0.01f;
    final static public float PITCH_MULTIPLIER = 0.0015f;
    final static public float k = 2.0f;
    final static public float h = 1.5f;
    final static public float d = 0.03f;
    final static public float s = 100.0f;
    final static public float l = 0.2f;
    final static public float o = 1.5f;
    final static public String ACTIVATION_TIME_KEY = "sexmod:galath_coin_activation_time";
    final static public String DEACTIVATION_TIME_KEY = "sexmod:galath_coin_deactivation_time";
    final static public String DE_SUMMON_ANIMATION_KEY = "sexmod:galath_coin_de_summoning_animation_time";
    final static public String DESCRIPTION = "Defeating a succubus makes her accept the victor as her master, granting him a coin to which her soul is bound. Using the coin summons her, offering services on demand. If her master uses the coin on her or goes too far, she returns to the coin";
    final private AnimationFactory animationFactory = new AnimationFactory(this);
    AnimationController<GalathCoin> controller;

    public GalathCoin() {
        this.maxStackSize = 1;
    }

    public static void RegisterCoin() {
        GALATH_COIN.setRegistryName("sexmod", "galath_coin");
        GALATH_COIN.setTranslationKey("galath_coin");
        MinecraftForge.EVENT_BUS.register(GalathCoin.class);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(GALATH_COIN);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(GALATH_COIN, 0, new ModelResourceLocation("sexmod:galath_coin"));
        GALATH_COIN.setTileEntityItemStackRenderer(new GalathCoinRenderer());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        NBTTagCompound nbtPlayerData = playerIn.getEntityData();
        ActionResult<ItemStack> result = new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));

        if (nbtPlayerData.getLong(DEACTIVATION_TIME_KEY) != 0L || nbtPlayerData.getLong(ACTIVATION_TIME_KEY) != 0L) {
            return result;
        }

        if (!this.canSummon(worldIn, playerIn)) {
            worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.MISC_BEEW[0], SoundCategory.PLAYERS, 1.0f, 1.0f, false);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.MISC_WEOWEO[1], SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        nbtPlayerData.setLong(ACTIVATION_TIME_KEY, System.currentTimeMillis());
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    boolean canSummon(World world, EntityPlayer player) {
        return !world.isRemote ? !GalathMangTracker.hasOwner(player.getPersistentID()) : !GalathMangTracker.debugEnabled;
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItem(event.getHand());
        if (GALATH_COIN.equals(heldItem.getItem())) {
            Entity target = event.getTarget();
            if (target instanceof GalathEntity) {
                GalathEntity galath = (GalathEntity) target;
                if (player.getPersistentID().equals(galath.getMasterUUID())) {
                    player.world.playSound(player.posX, player.posY, player.posZ, SoundsHandler.MISC_WEOWEO[0], SoundCategory.PLAYERS, 1.0f, 1.0f, false);
                    player.getEntityData().setLong(DEACTIVATION_TIME_KEY, System.currentTimeMillis());
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            NBTTagCompound NbtplayerData = player.getEntityData();
            long activationTime = NbtplayerData.getLong("sexmod:galath_coin_activation_time");
            long deactivationTime = NbtplayerData.getLong("sexmod:galath_coin_deactivation_time");
            long now = System.currentTimeMillis();
            this.writeCooldownNBT(player, NbtplayerData, now, activationTime);
            this.writeCooldownNBT2(player, NbtplayerData, now, deactivationTime);
            if (deactivationTime != 0L && now > deactivationTime + 4000L) {
                NbtplayerData.setLong("sexmod:galath_coin_deactivation_time", 0L);
                NbtplayerData.setBoolean("sexmod:galath_coin_de_summoning_animation_time", false);
            }
            if (worldIn.isRemote) {
                this.isSummonWindow(player, now, activationTime);
                this.isCooldownElapsed(player, now, deactivationTime);
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void isCooldownElapsed(EntityPlayer player, long now, long startTime) {
        if (startTime != 0L) {
            if (now > startTime + 1000L && now < startTime + 3000L) {
                GalathEntity galath = null;
                for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                    if (!girl.isDead && girl.world.isRemote && girl instanceof GalathEntity && player.equals(girl.getMasterPlayer())) {
                        galath = (GalathEntity) girl;
                        break;
                    }
                }
                if (galath != null) {
                    Vec3d targetPos = galath.getTargetPosition().add(0.0, 1.5, 0.0);
                    Vec3d eyePos = player.getPositionVector().add(0.0, player.getEyeHeight(), 0.0);
                    Vec3d coinPos = eyePos.add(VectorMath.rotateByYaw((float) (player.getHeldItemMainhand().getItem().equals(GALATH_COIN) ? 1 : -1) * 0.1f, (double) (-0.01f + player.rotationPitch * 0.0015f), 0.0, player.renderYawOffset));
                    float progress = (float) (now - startTime - 1000L) / 2000.0f;
                    Vec3d lerpedPos = ReferenceAndRotationHelper.LerpVec3d(targetPos, coinPos, (double) progress);
                    DragonBreathParticle.BREATH_SCALE = 0.2f;
                    Minecraft.getMinecraft().effectRenderer.addEffect(new DragonBreathParticle(player.world, lerpedPos.x, lerpedPos.y, lerpedPos.z));
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void handleCoinClick(EntityPlayer player) {
        if (Minecraft.getMinecraft().player.getPersistentID().equals(player.getPersistentID())) {
            GalathMangTracker.debugEnabled = true;
        }
    }

    @SideOnly(value=Side.CLIENT)
    void isSummonWindow(EntityPlayer player, long now, long startTime) {
        if (now > startTime + 1000L && now < startTime + 3000L) {
            Vec3d eyePos = player.getPositionVector().add(0.0, player.getEyeHeight(), 0.0);
            Vec3d coinPos = eyePos.add(VectorMath.rotateByYaw((float) (player.getHeldItemMainhand().getItem().equals(GALATH_COIN) ? 1 : -1) * 0.1f, (double) (-0.01f + player.rotationPitch * 0.0015f), 0.0, player.renderYawOffset));
            Vec3d summonPos = eyePos.add(player.getLookVec().normalize().scale(2.0));
            float progress = (float) (now - startTime - 1000L) / 2000.0f;
            Vec3d lerpedPos = ReferenceAndRotationHelper.LerpVec3d(coinPos, summonPos, (double) progress);
            DragonBreathParticle.BREATH_SCALE = 0.2f;
            Minecraft.getMinecraft().effectRenderer.addEffect(new DragonBreathParticle(player.world, lerpedPos.x, lerpedPos.y, lerpedPos.z));
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            UUID ownerUUID = GalathMangTracker.getOwnerOf(player);
            GirlEntity girl = GirlEntity.getServerGirlEntity(ownerUUID);
            if (girl != null) {
                GalathMangTracker.updateMangleliePartner((GalathEntity) girl);
                PackageHandler.INSTANCE.sendTo((IMessage) new InformOfOwnership(false), (EntityPlayerMP) player);
            }
        }
    }

    void writeCooldownNBT(EntityPlayer player, NBTTagCompound nbt, long now, long startTime) {
        if (startTime == 0L) {
            return;
        }
        if (now - startTime <= 4000L) {
            return;
        }
        nbt.setLong(ACTIVATION_TIME_KEY, 0L);
        Vec3d vec3d = player.getPositionVector().add(0.0, player.getEyeHeight(), 0.0);
        Vec3d vec3d2 = vec3d.add(player.getLookVec().normalize().scale(2.0));
        Random random = player.getRNG();
        int n = 0;
        while ((float)n < 100.0f) {
            player.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d2.x, vec3d2.y, vec3d2.z, (2.0f * random.nextFloat() - 1.0f) * 0.2f, (2.0f * random.nextFloat() - 1.0f) * 0.2f, (2.0f * random.nextFloat() - 1.0f) * 0.2f, new int[0]);
            ++n;
        }
        World world = player.world;
        if (world.isRemote) {
            this.handleCoinClick(player);
            return;
        }
        GalathEntity f__class2972 = new GalathEntity(player.world, player, vec3d2);
        f__class2972.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
        GalathMangTracker.grantOwnership(player, f__class2972);
        player.world.spawnEntity(f__class2972);
        if (GalathMangTracker.isManglelieOwned(player.getPersistentID())) {
            f__class2972.canStartPussyLicking();
        }
    }

    void d(EntityPlayer entityPlayer) {
        if (entityPlayer.world.isRemote) {
            this.b(entityPlayer);
        } else {
            this.c(entityPlayer);
        }
    }

    void c(EntityPlayer entityPlayer) {
        UUID uUID = GalathMangTracker.getOwnerOf(entityPlayer);
        GirlEntity em_class2582 = GirlEntity.getServerGirlEntity(uUID);
        if (em_class2582 instanceof GalathEntity) {
            GalathCoin.a((GalathEntity)em_class2582);
        }
    }

    public static void a(GalathEntity f__class2972) {
        f__class2972.setCurrentAction(Action.GALATH_DE_SUMMON);
        f__class2972.aC();
        f__class2972.setAnchored(true);
        f__class2972.setTargetPosition(f__class2972.getPositionVector());
        f__class2972.setYawRotation(f__class2972.rotationYaw);
    }

    @SideOnly(value=Side.CLIENT)
    void b(EntityPlayer entityPlayer) {
        GalathEntity f__class2972 = null;
        for (GirlEntity em_class2582 : GirlEntity.getGirlEntityList()) {
            if (em_class2582.isDead || !em_class2582.world.isRemote || !(em_class2582 instanceof GalathEntity) || !entityPlayer.equals(em_class2582.getMasterPlayer()))
                continue;
            f__class2972 = (GalathEntity) em_class2582;
            break;
        }
        if (f__class2972 == null) {
            return;
        }
        GalathCoin.summonForPlayer(entityPlayer, f__class2972);
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(UUID uUID, GalathEntity f__class2972) {
        World world = f__class2972.world;
        Vec3d vec3d = f__class2972.isAnchored() ? f__class2972.getTargetPosition() : f__class2972.getPositionVector();
        Vec3d vec3d2 = vec3d.add(0.0, 1.5, 0.0);
        Random random = f__class2972.getRNG();
        int n = 0;
        while ((float)n < 100.0f) {
            Vec3d vec3d3 = new Vec3d((random.nextFloat() * 2.0f - 1.0f) * 1.5f, (random.nextFloat() * 2.0f - 1.0f) * 1.5f, (random.nextFloat() * 2.0f - 1.0f) * 1.5f);
            Vec3d vec3d4 = vec3d2.add(vec3d3);
            Vec3d vec3d5 = vec3d3.scale(-0.03f);
            world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d4.x, vec3d4.y, vec3d4.z, vec3d5.x, vec3d5.y, vec3d5.z, new int[0]);
            ++n;
        }
        if (Minecraft.getMinecraft().player.getPersistentID().equals(uUID)) {
            GalathMangTracker.debugEnabled = false;
        }
    }

    public static void summonForPlayer(EntityPlayer entityPlayer, GalathEntity f__class2972) {
        GalathCoin.a(entityPlayer.getPersistentID(), f__class2972);
    }

    void writeCooldownNBT2(EntityPlayer entityPlayer, NBTTagCompound nBTTagCompound, long l, long l2) {
        if (l2 == 0L) {
            return;
        }
        long l3 = l - l2;
        World world = entityPlayer.world;
        boolean bl = nBTTagCompound.getBoolean(DE_SUMMON_ANIMATION_KEY);
        if (!bl && l3 > 1000L - (long)(world.isRemote ? 0 : 150)) {
            nBTTagCompound.setBoolean(DE_SUMMON_ANIMATION_KEY, true);
            this.d(entityPlayer);
        }
        if (world.isRemote) {
            return;
        }
        if (l - l2 <= 3000L) {
            return;
        }
        UUID uUID = GalathMangTracker.getOwnerOf(entityPlayer);
        GirlEntity em_class2582 = GirlEntity.getServerGirlEntity(uUID);
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathMangTracker.updateMangleliePartner((GalathEntity)em_class2582);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        this.controller = new AnimationController<GalathCoin>(this, "controller", 0.0f, this::a);
        animationData.addAnimationController(this.controller);
    }

    @SideOnly(value=Side.CLIENT)
    protected <segs extends IAnimatable> PlayState a(AnimationEvent<segs> animationEvent) {
        NBTTagCompound nBTTagCompound = Minecraft.getMinecraft().player.getEntityData();
        if (nBTTagCompound.getLong(ACTIVATION_TIME_KEY) == 0L && nBTTagCompound.getLong(DEACTIVATION_TIME_KEY) == 0L) {
            animationEvent.getController().clearAnimationCache();
            return PlayState.STOP;
        }
        this.controller.setAnimation(new AnimationBuilder().addAnimation("animation.galath_coin.summon", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }
}

