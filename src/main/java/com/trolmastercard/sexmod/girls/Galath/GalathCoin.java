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
package com.trolmastercard.sexmod.girls.Galath;

import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.InformOfOwnership;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Reference;
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

public class GalathCoin
extends Item
implements IAnimatable {
    final static public GalathCoin GALATH_COIN = new GalathCoin();
    final static public long c = 4000L;
    final static public long g = 1000L;
    final static public long j = 3000L;
    final static public float q = 0.1f;
    final static public float p = -0.01f;
    final static public float e = 0.0015f;
    final static public float k = 2.0f;
    final static public float h = 1.5f;
    final static public float d = 0.03f;
    final static public float s = 100.0f;
    final static public float l = 0.2f;
    final static public float o = 1.5f;
    final static public String b = "sexmod:galath_coin_activation_time";
    final static public String m = "sexmod:galath_coin_deactivation_time";
    final static public String n = "sexmod:galath_coin_de_summoning_animation_time";
    final static public String f = "Defeating a succubus makes her accept the victor as her master, granting him a coin to which her soul is bound. Using the coin summons her, offering services on demand. If her master uses the coin on her or goes too far, she returns to the coin";
    final private AnimationFactory i = new AnimationFactory(this);
    AnimationController<GalathCoin> a;

    public GalathCoin() {
        this.maxStackSize = 1;
    }

    public static void RegisterCoin() {
        GALATH_COIN.setRegistryName("sexmod", "galath_coin");
        GALATH_COIN.setTranslationKey("galath_coin");
        MinecraftForge.EVENT_BUS.register(GalathCoin.class);
    }

    @SubscribeEvent
    public static void a(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(GALATH_COIN);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void a(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation((Item) GALATH_COIN, 0, (ModelResourceLocation)new ModelResourceLocation("sexmod:galath_coin"));
        GALATH_COIN.setTileEntityItemStackRenderer(new av_class46());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand enumHand) {
        NBTTagCompound nBTTagCompound = entityPlayer.getEntityData();
        ActionResult<ItemStack> actionResult = new ActionResult<ItemStack>(EnumActionResult.FAIL, entityPlayer.getHeldItem(enumHand));
        if (nBTTagCompound.getLong(m) != 0L) {
            return actionResult;
        }
        if (nBTTagCompound.getLong(b) != 0L) {
            return actionResult;
        }
        if (!this.a(world, entityPlayer)) {
            world.playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundsHandler.MISC_BEEW[0], SoundCategory.PLAYERS, 1.0f, 1.0f, false);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, entityPlayer.getHeldItem(enumHand));
        }
        world.playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundsHandler.MISC_WEOWEO[1], SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        nBTTagCompound.setLong(b, System.currentTimeMillis());
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, entityPlayer.getHeldItem(enumHand));
    }

    boolean a(World world, EntityPlayer entityPlayer) {
        if (!world.isRemote) {
            return !GalathMangTracker.c(entityPlayer.getPersistentID());
        }
        return !GalathMangTracker.f;
    }

    @SubscribeEvent
    public void a(PlayerInteractEvent.EntityInteract entityInteract) {
        EntityPlayer entityPlayer = entityInteract.getEntityPlayer();
        ItemStack itemStack = entityPlayer.getHeldItem(entityInteract.getHand());
        if (!GALATH_COIN.equals(itemStack.getItem())) {
            return;
        }
        Entity entity = entityInteract.getTarget();
        if (!(entity instanceof GalathEntity)) {
            return;
        }
        GalathEntity f__class2972 = (GalathEntity)entity;
        if (!entityPlayer.getPersistentID().equals(f__class2972.java_util_UUID_O())) {
            return;
        }
        entityPlayer.world.playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundsHandler.MISC_WEOWEO[0], SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        entityPlayer.getEntityData().setLong(m, System.currentTimeMillis());
        entityInteract.setCanceled(true);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int n, boolean bl) {
        super.onUpdate(itemStack, world, entity, n, bl);
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)entity;
        NBTTagCompound nBTTagCompound = entityPlayer.getEntityData();
        long l = nBTTagCompound.getLong("sexmod:galath_coin_activation_time");
        long l2 = nBTTagCompound.getLong("sexmod:galath_coin_deactivation_time");
        long l3 = System.currentTimeMillis();
        this.b(entityPlayer, nBTTagCompound, l3, l);
        this.a(entityPlayer, nBTTagCompound, l3, l2);
        if (l2 != 0L && l3 > l2 + 4000L) {
            nBTTagCompound.setLong("sexmod:galath_coin_deactivation_time", 0L);
            nBTTagCompound.setBoolean("sexmod:galath_coin_de_summoning_animation_time", false);
        }
        if (!world.isRemote) {
            return;
        }
        this.a(entityPlayer, l3, l);
        this.b(entityPlayer, l3, l2);
    }

    @SideOnly(value=Side.CLIENT)
    void b(EntityPlayer entityPlayer, long l, long l2) {
        if (l2 == 0L) {
            return;
        }
        if (l <= l2 + 1000L || l >= l2 + 3000L) {
            return;
        }
        GalathEntity f__class2972 = null;
        try {
            for (GirlEntity object2 : GirlEntity.GirlEntityList()) {
                if (object2.isDead || !object2.world.isRemote || !(object2 instanceof GalathEntity) || !entityPlayer.equals(object2.net_minecraft_entity_player_EntityPlayer_z())) continue;
                f__class2972 = (GalathEntity)object2;
                break;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        if (f__class2972 == null) {
            return;
        }
        Vec3d vec3d = f__class2972.net_minecraft_util_math_Vec3d_o().add(0.0, 1.5, 0.0);
        Vec3d vec3d2 = entityPlayer.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0);
        Vec3d vec3d3 = vec3d2.add(VectorMath.RotateY((float)(entityPlayer.getHeldItemMainhand().getItem().equals(GALATH_COIN) ? 1 : -1) * 0.1f, (double)(-0.01f + entityPlayer.rotationPitch * 0.0015f), 0.0, entityPlayer.renderYawOffset));
        float f = (float)(l - l2 - 1000L) / 2000.0f;
        Vec3d vec3d4 = Reference.LerpVec3d(vec3d, vec3d3, (double)f);
        ParticleGalathTrail.globalParticleScale = 0.2f;
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleGalathTrail(entityPlayer.world, vec3d4.x, vec3d4.y, vec3d4.z));
    }

    @SideOnly(value=Side.CLIENT)
    void a(EntityPlayer entityPlayer) {
        if (!Minecraft.getMinecraft().player.getPersistentID().equals(entityPlayer.getPersistentID())) {
            return;
        }
        GalathMangTracker.f = true;
    }

    @SideOnly(value=Side.CLIENT)
    void a(EntityPlayer entityPlayer, long l, long l2) {
        if (l <= l2 + 1000L || l >= l2 + 3000L) {
            return;
        }
        Vec3d vec3d = entityPlayer.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0);
        Vec3d vec3d2 = vec3d.add(VectorMath.RotateY((float)(entityPlayer.getHeldItemMainhand().getItem().equals(GALATH_COIN) ? 1 : -1) * 0.1f, (double)(-0.01f + entityPlayer.rotationPitch * 0.0015f), 0.0, entityPlayer.renderYawOffset));
        Vec3d vec3d3 = vec3d.add(entityPlayer.getLookVec().normalize().scale(2.0));
        float f = (float)(l - l2 - 1000L) / 2000.0f;
        Vec3d vec3d4 = Reference.LerpVec3d(vec3d2, vec3d3, (double)f);
        ParticleGalathTrail.globalParticleScale = 0.2f;
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleGalathTrail(entityPlayer.world, vec3d4.x, vec3d4.y, vec3d4.z));
    }

    @SubscribeEvent
    public void a(PlayerEvent.PlayerChangedDimensionEvent playerChangedDimensionEvent) {
        EntityPlayer entityPlayer = playerChangedDimensionEvent.player;
        if (entityPlayer.world.isRemote) {
            return;
        }
        UUID uUID = GalathMangTracker.b(entityPlayer);
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID);
        if (em_class2582 == null) {
            return;
        }
        GalathMangTracker.a((GalathEntity)em_class2582);
        PackageHandler.networkWrapper.sendTo((IMessage)new InformOfOwnership(false), (EntityPlayerMP)entityPlayer);
    }

    void b(EntityPlayer entityPlayer, NBTTagCompound nBTTagCompound, long l, long l2) {
        if (l2 == 0L) {
            return;
        }
        if (l - l2 <= 4000L) {
            return;
        }
        nBTTagCompound.setLong(b, 0L);
        Vec3d vec3d = entityPlayer.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0);
        Vec3d vec3d2 = vec3d.add(entityPlayer.getLookVec().normalize().scale(2.0));
        Random random = entityPlayer.getRNG();
        int n = 0;
        while ((float)n < 100.0f) {
            entityPlayer.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d2.x, vec3d2.y, vec3d2.z, (2.0f * random.nextFloat() - 1.0f) * 0.2f, (2.0f * random.nextFloat() - 1.0f) * 0.2f, (2.0f * random.nextFloat() - 1.0f) * 0.2f, new int[0]);
            ++n;
        }
        World world = entityPlayer.world;
        if (world.isRemote) {
            this.a(entityPlayer);
            return;
        }
        GalathEntity f__class2972 = new GalathEntity(entityPlayer.world, entityPlayer, vec3d2);
        f__class2972.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
        GalathMangTracker.a(entityPlayer, f__class2972);
        entityPlayer.world.spawnEntity(f__class2972);
        if (GalathMangTracker.doesPlayerOwnGalathMangPair(entityPlayer.getPersistentID())) {
            f__class2972.boolean_v();
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
        UUID uUID = GalathMangTracker.b(entityPlayer);
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID);
        if (em_class2582 instanceof GalathEntity) {
            GalathCoin.a((GalathEntity)em_class2582);
        }
    }

    public static void a(GalathEntity f__class2972) {
        f__class2972.setCurrentAction(Action.GALATH_DE_SUMMON);
        f__class2972.aC();
        f__class2972.void_a(true);
        f__class2972.c(f__class2972.getPositionVector());
        f__class2972.void_b(f__class2972.rotationYaw);
    }

    @SideOnly(value=Side.CLIENT)
    void b(EntityPlayer entityPlayer) {
        GalathEntity f__class2972 = null;
        try {
            for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                if (em_class2582.isDead || !em_class2582.world.isRemote || !(em_class2582 instanceof GalathEntity) || !entityPlayer.equals(em_class2582.net_minecraft_entity_player_EntityPlayer_z())) continue;
                f__class2972 = (GalathEntity)em_class2582;
                break;
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        if (f__class2972 == null) {
            return;
        }
        GalathCoin.a(entityPlayer, f__class2972);
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(UUID uUID, GalathEntity f__class2972) {
        World world = f__class2972.world;
        Vec3d vec3d = f__class2972.boolean_Q() ? f__class2972.net_minecraft_util_math_Vec3d_o() : f__class2972.getPositionVector();
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
            GalathMangTracker.f = false;
        }
    }

    public static void a(EntityPlayer entityPlayer, GalathEntity f__class2972) {
        GalathCoin.a(entityPlayer.getPersistentID(), f__class2972);
    }

    void a(EntityPlayer entityPlayer, NBTTagCompound nBTTagCompound, long l, long l2) {
        if (l2 == 0L) {
            return;
        }
        long l3 = l - l2;
        World world = entityPlayer.world;
        boolean bl = nBTTagCompound.getBoolean(n);
        if (!bl && l3 > 1000L - (long)(world.isRemote ? 0 : 150)) {
            nBTTagCompound.setBoolean(n, true);
            this.d(entityPlayer);
        }
        if (world.isRemote) {
            return;
        }
        if (l - l2 <= 3000L) {
            return;
        }
        UUID uUID = GalathMangTracker.b(entityPlayer);
        GirlEntity em_class2582 = GirlEntity.com_trolmastercard_sexmod_em_class258_a(uUID);
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathMangTracker.a((GalathEntity)em_class2582);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        this.a = new AnimationController<GalathCoin>(this, "controller", 0.0f, this::a);
        animationData.addAnimationController(this.a);
    }

    @SideOnly(value=Side.CLIENT)
    protected <segs extends IAnimatable> PlayState a(AnimationEvent<segs> animationEvent) {
        NBTTagCompound nBTTagCompound = Minecraft.getMinecraft().player.getEntityData();
        if (nBTTagCompound.getLong(b) == 0L && nBTTagCompound.getLong(m) == 0L) {
            animationEvent.getController().clearAnimationCache();
            return PlayState.STOP;
        }
        this.a.setAnimation(new AnimationBuilder().addAnimation("animation.galath_coin.summon", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.i;
    }

    private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
        return concurrentModificationException;
    }
}

