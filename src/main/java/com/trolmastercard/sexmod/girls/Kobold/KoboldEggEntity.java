/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

// TODO this might be kobold egg class
//  TODO perform string search for '"Tribeid", there are 2 spellings: 'Id' / 'ID'
public class KoboldEggEntity
extends EntityLivingBase
implements IAnimatable {
    final static int e = 12000;
    final private AnimationFactory d = new AnimationFactory(this);
    public UUID f = null;
    static AnimationController<KoboldEggEntity> a;
    final static public DataParameter<String> b;
    final static public DataParameter<Integer> c;

    public KoboldEggEntity(World world) {
        super(world);
        this.setSize(0.5f, 0.5f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(b, KoboldEntity.COLOR.toString());
        this.dataManager.register(c, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        int n = this.dataManager.get(c);
        if (n >= 12000) {
            this.a();
        }
        if (!this.world.isRemote) {
            this.dataManager.set(c, n + 1);
        }
    }

    public boolean canTrample(World world, Block block, BlockPos blockPos, float f) {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        boolean bl = super.attackEntityFrom(damageSource, f);
        if (!bl) {
            return false;
        }
        this.setDead();
        return true;
    }

    void a() {
        for (int i = 0; i < 30; ++i) {
            float f = (float)(Reference.RANDOM.nextBoolean() ? 1 : -1) * Reference.RANDOM.nextFloat();
            float f2 = (float)(Reference.RANDOM.nextBoolean() ? 1 : -1) * Reference.RANDOM.nextFloat();
            float f3 = (float)(Reference.RANDOM.nextBoolean() ? 1 : -1) * Reference.RANDOM.nextFloat();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 0.5 + this.posX, 0.5 + this.posY, 0.5 + this.posZ, f, f2, f3, new int[0]);
        }
        if (this.world.isRemote) {
            return;
        }
        if (this.f == null) {
            this.f = UUID.randomUUID();
        }
        KoboldEntity ff_class3082 = KoboldEntity.a(this.world, this.f);
        KoboldManager.c(this.f, ff_class3082);
        UUID uUID = KoboldManager.b(this.f);
        if (uUID != null) {
            ff_class3082.getDataManager().set(GirlEntity.MASTER_UUID, uUID.toString());
        }
        List<KoboldEntity> list = KoboldManager.n(this.f);
        String string = null;
        for (KoboldEntity ff_class3083 : list) {
            String string2 = ff_class3083.getDataManager().get(KoboldEntity.aU);
            if ("".equals(string2)) continue;
            string = string2;
            break;
        }
        if (string != null) {
            ff_class3082.getDataManager().set(KoboldEntity.aU, string);
        }
        ff_class3082.setPosition(0.5 + this.posX, this.posY, 0.5 + this.posZ);
        this.world.spawnEntity(ff_class3082);
        this.a(ff_class3082);
        this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5f, 1.0f);
        this.world.removeEntity(this);
    }

    void a(KoboldEntity ff_class3082) {
        EntityPlayer entityPlayer = ff_class3082.getMasterPlayer();
        if (entityPlayer == null) {
            return;
        }
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityPlayer;
        EyeAndKoboldColor eyeAndKoboldColor_ = KoboldManager.l(this.f);
        entityPlayer.sendMessage(new TextComponentString(String.format("%s%s %shas become a %snew tribe member%s!", new Object[]{eyeAndKoboldColor_.getTextColor(), ff_class3082.getGirlName(), TextFormatting.WHITE, TextFormatting.RED, TextFormatting.WHITE})));
        entityPlayerMP.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.NEUTRAL, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 1.0f, 1.0f));
        entityPlayerMP.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_FIREWORK_TWINKLE_FAR, SoundCategory.NEUTRAL, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 1.0f, 1.0f));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        a = new AnimationController<KoboldEggEntity>(this, "controller", 5.0f, this::a);
        animationData.addAnimationController(a);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.d;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        if (this.f != null) {
            nBTTagCompound.setString("tribeID", this.f.toString());
        }
        nBTTagCompound.setString("egg_color", this.dataManager.get(b));
        nBTTagCompound.setInteger("eggAge", this.dataManager.get(c));
        super.writeEntityToNBT(nBTTagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        String string = nBTTagCompound.getString("tribeID");
        if (!"".equals(string)) {
            this.f = UUID.fromString(string);
        }
        this.dataManager.set(b, nBTTagCompound.getString("egg_color"));
        this.dataManager.set(c, nBTTagCompound.getInteger("eggAge"));
    }

    protected <E extends IAnimatable> PlayState a(AnimationEvent<E> animationEvent) {
        int n = this.dataManager.get(c);
        if (12000 - n < 20) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.hatch", true));
            return PlayState.CONTINUE;
        }
        float f = (float)n / 12000.0f;
        if ((double)f > 0.98) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.veryfast", true));
            return PlayState.CONTINUE;
        }
        if ((double)f > 0.85) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.fast", true));
            return PlayState.CONTINUE;
        }
        if ((double)f > 0.75) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.medium", true));
            return PlayState.CONTINUE;
        }
        if ((double)f > 0.5) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.slow", true));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
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

    static {
        b = EntityDataManager.createKey(KoboldEggEntity.class, DataSerializers.STRING).getSerializer().createKey(115);
        c = EntityDataManager.createKey(KoboldEggEntity.class, DataSerializers.VARINT).getSerializer().createKey(116);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

