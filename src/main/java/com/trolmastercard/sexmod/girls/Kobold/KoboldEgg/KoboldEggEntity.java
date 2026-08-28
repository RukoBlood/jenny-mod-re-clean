/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.KoboldEgg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.Kobold.EyeAndKoboldColor;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
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

public class KoboldEggEntity extends EntityLivingBase implements IAnimatable {
    final static int HATCH_TIME = 12000;
    final private AnimationFactory factory = new AnimationFactory(this);
    public UUID tribeId = null;
    static AnimationController<KoboldEggEntity> animationController;
    final static public DataParameter<String> EGG_COLOR;
    final static public DataParameter<Integer> EGG_TYPE;

    public KoboldEggEntity(World world) {
        super(world);
        this.setSize(0.5f, 0.5f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EGG_COLOR, KoboldEntity.COLOR.toString());
        this.dataManager.register(EGG_TYPE, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        int eggType = this.dataManager.get(EGG_TYPE);
        if (eggType >= HATCH_TIME) {
            this.spawnHatchExplosion();
        }
        if (!this.world.isRemote) {
            this.dataManager.set(EGG_TYPE, eggType + 1);
        }
    }

    public boolean canTrample(World world, Block block, BlockPos blockPos, float f) {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean damaged = super.attackEntityFrom(source, amount);
        if (!damaged) {
            return false;
        } else {
            this.setDead();
            return true;
        }
    }

    void spawnHatchExplosion() {
        for (int i = 0; i < 30; ++i) {
            float vx = (float)(Reference.RANDOM.nextBoolean() ? 1 : -1) * Reference.RANDOM.nextFloat();
            float vy = (float)(Reference.RANDOM.nextBoolean() ? 1 : -1) * Reference.RANDOM.nextFloat();
            float vz = (float)(Reference.RANDOM.nextBoolean() ? 1 : -1) * Reference.RANDOM.nextFloat();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 0.5 + this.posX, 0.5 + this.posY, 0.5 + this.posZ, vx, vy, vz);
        }
        if (!this.world.isRemote) {
            if (this.tribeId == null) {
                this.tribeId = UUID.randomUUID();
            }
            KoboldEntity kobold = KoboldEntity.createKobold(this.world, this.tribeId);
            KoboldManager.addTribeMember(this.tribeId, kobold);
            UUID masterId = KoboldManager.getTribeMasterUUID(this.tribeId);
            if (masterId != null) {
                kobold.getDataManager().set(GirlEntity.MASTER, masterId.toString());
            }

            List<KoboldEntity> members = KoboldManager.getTribeMembersList(this.tribeId);
            String masterName = null;
            for (KoboldEntity member : members) {
                String name = member.getDataManager().get(KoboldEntity.TRIBE_NAME);
                if (name.isEmpty()) continue;
                masterName = name;
                break;
            }
            if (masterName != null) {
                kobold.getDataManager().set(KoboldEntity.TRIBE_NAME, masterName);
            }
            kobold.setPosition(0.5 + this.posX, this.posY, 0.5 + this.posZ);
            this.world.spawnEntity(kobold);
            this.hatchEgg(kobold);
            this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5f, 1.0f);
            this.world.removeEntity(this);
        }
    }

    void hatchEgg(KoboldEntity kobold) {
        EntityPlayer player = kobold.getMasterPlayer();
        if (player != null) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            EyeAndKoboldColor color = KoboldManager.getTribeColor(this.tribeId);
            player.sendMessage(new TextComponentString(String.format("%s%s %shas become a %snew tribe member%s!", new Object[]{color.getTextColor(), kobold.getGirlName(), TextFormatting.WHITE, TextFormatting.RED, TextFormatting.WHITE})));
            playerMP.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.NEUTRAL, player.posX, player.posY, player.posZ, 1.0f, 1.0f));
            playerMP.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_FIREWORK_TWINKLE_FAR, SoundCategory.NEUTRAL, player.posX, player.posY, player.posZ, 1.0f, 1.0f));
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationController = new AnimationController<KoboldEggEntity>(this, "controller", 5.0f, this::predicate);
        animationData.addAnimationController(animationController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        if (this.tribeId != null) {
            nbt.setString("tribeID", this.tribeId.toString());
        }
        nbt.setString("egg_color", this.dataManager.get(EGG_COLOR));
        nbt.setInteger("eggAge", this.dataManager.get(EGG_TYPE));
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        String tribeID = nbt.getString("tribeID");
        if (!tribeID.isEmpty()) {
            this.tribeId = UUID.fromString(tribeID);
        }
        this.dataManager.set(EGG_COLOR, nbt.getString("egg_color"));
        this.dataManager.set(EGG_TYPE, nbt.getInteger("eggAge"));
    }

    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        int eggType = this.dataManager.get(EGG_TYPE);
        if (HATCH_TIME - eggType < 20) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.hatch", true));
            return PlayState.CONTINUE;
        }
        float progress = (float)eggType / (float) HATCH_TIME;
        if ((double)progress > 0.98) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.veryfast", true));
            return PlayState.CONTINUE;
        }
        if ((double)progress > 0.85) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.fast", true));
            return PlayState.CONTINUE;
        }
        if ((double)progress > 0.75) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.medium", true));
            return PlayState.CONTINUE;
        }
        if ((double)progress > 0.5) {
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
        EGG_COLOR = EntityDataManager.createKey(KoboldEggEntity.class, DataSerializers.STRING).getSerializer().createKey(115);
        EGG_TYPE = EntityDataManager.createKey(KoboldEggEntity.class, DataSerializers.VARINT).getSerializer().createKey(116);
    }
}

