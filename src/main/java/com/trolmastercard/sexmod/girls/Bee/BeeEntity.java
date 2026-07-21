/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bee;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.BeeInventoryUI;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class BeeEntity extends Supporter {
    public float N = 3200.0f;
    int P = 0;
    final static float O = 4800.0f;
    final static float Q = 10.0f;
    final static public DataParameter<Boolean> M = EntityDataManager.createKey(BeeEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(112);

    public BeeEntity(World world) {
        super(world);
        this.moveHelper = new EntityFlyHelper(this);
        this.setSize(0.3f, 1.5f);
    }

    @Override
    public String getGirlName() {
        return "Bee";
    }

    @Override
    public float float_i() {
        return -0.1f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(M, false);
    }

    @Override
    protected PathNavigate createNavigator(World world) {
        PathNavigateFlying pathNavigateFlying = new PathNavigateFlying(this, world);
        pathNavigateFlying.setCanOpenDoors(false);
        pathNavigateFlying.setCanFloat(true);
        pathNavigateFlying.setCanEnterDoors(true);
        this.pathNavigator = pathNavigateFlying;
        return pathNavigateFlying;
    }

    @Override
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        this.getAttributeMap().registerAttribute(SWIM_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4f);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f);
    }

    @Override
    protected void initEntityAI() {
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new FollowPlayerGoal(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.o);
        this.tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, 1.0));
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.isPotionActive(HornyPotion.HORNY_POTION) && this.N < 4800.0f && this.getID() == null) {
            this.removePotionEffect(HornyPotion.HORNY_POTION);
            this.N = 6.9420184E7f;
        }
        this.void_c();
        if (this.currentAction().equals((Object) Action.CITIZEN_CUM)) {
            this.P = Math.max(1, this.P);
        }
        this.a_();
        this.b_15();
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.currentAction() == Action.CITIZEN_CUM && (action == Action.CITIZEN_FAST || action == Action.COWGIRLSLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    void void_c() {
        if (this.getID() != null) {
            return;
        }
        if (this.boolean_J()) {
            return;
        }
        this.N += 1.0f;
        if (this.N < 4800.0f) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        if (BeeEntity.getActiveSceneInfo(entityPlayer) != null) {
            return;
        }
        if (PlayerGirl.e(entityPlayer)) {
            return;
        }
        if (entityPlayer.getDistance(this) < 1.5f) {
            this.N = 0.0f;
            this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setTargetPosition(this.net_minecraft_util_math_Vec3d_aa());
            this.void_b(entityPlayer.rotationYaw - 180.0f);
            this.pathNavigator.clearPath();
            PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
            this.setCurrentAction(Action.CITIZEN_START);
            Vec3d vec3d = this.a(0.2);
            entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        } else {
            this.pathNavigator.clearPath();
            this.pathNavigator.tryMoveToEntityLiving(entityPlayer, 1.0);
        }
    }

    void b_15() {
        RayTraceResult rayTraceResult = this.world.rayTraceBlocks(this.getPositionVector(), new Vec3d(this.posX, 0.0, this.posZ));
        if (rayTraceResult == null) {
            return;
        }
        BlockPos blockPos = rayTraceResult.getBlockPos();
        double d = this.posY - (double)blockPos.getY();
        if (d > 3.0 && this.motionY > 0.0) {
            this.motionY = 0.0;
        }
    }

    void a_() {
        if (this.P == 0) {
            return;
        }
        ++this.P;
        if (this.entityDataManager.get(M).booleanValue()) {
            if (this.P < 40) {
                for (EntityPlayer entityPlayer : this.world.playerEntities) {
                    if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
                    ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.HEART, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 1, new int[0]));
                }
            } else {
                this.P = 0;
            }
        } else if (this.P < 200) {
            for (EntityPlayer entityPlayer : this.world.playerEntities) {
                if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
                ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.SPELL, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 1, new int[0]));
            }
        } else if (this.P == 200) {
            this.entityDataManager.set(M, this.getRNG().nextBoolean());
        } else if (this.P < 250) {
            for (EntityPlayer entityPlayer : this.world.playerEntities) {
                if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
                ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(this.entityDataManager.get(M) != false ? EnumParticleTypes.HEART : EnumParticleTypes.VILLAGER_ANGRY, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 3, new int[0]));
            }
        } else {
            this.P = 0;
        }
        for (EntityPlayer entityPlayer : this.world.playerEntities) {
            if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
            ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.SPELL, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 10, new int[0]));
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.N < 4800.0f && !this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.4;
        }
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (this.entityDataManager.get(M).booleanValue() && !((Boolean)this.entityDataManager.get(HAS_CHEST)).booleanValue() && entityPlayer.getHeldItem(enumHand).getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
            this.entityDataManager.set(HAS_CHEST, true);
            entityPlayer.getHeldItem(enumHand).shrink(1);
            return super.processInteract(entityPlayer, enumHand);
        }
        if (this.world.isRemote && this.entityDataManager.get(M).booleanValue()) {
            this.DisplayBeeUI(entityPlayer);
        }
        return super.processInteract(entityPlayer, enumHand);
    }

    @SideOnly(value=Side.CLIENT)
    void DisplayBeeUI(EntityPlayer entityPlayer) {
        Minecraft.getMinecraft().displayGuiScreen(new BeeInventoryUI(this, entityPlayer));
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public void a(String string, UUID uUID) {
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_FAST;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.CITIZEN_FAST || action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_CUM;
        }
        return null;
    }

    @Override
    protected void U() {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("isTamed", this.entityDataManager.get(M));
        nBTTagCompound.setBoolean("hasChest", (Boolean)this.entityDataManager.get(HAS_CHEST));
        nBTTagCompound.setTag("inventory", this.invHandler.serializeNBT());
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("isTamed")) {
            this.entityDataManager.set(M, nBTTagCompound.getBoolean("isTamed"));
        }
        this.entityDataManager.set(HAS_CHEST, nBTTagCompound.getBoolean("hasChest"));
        this.invHandler.deserializeNBT(nBTTagCompound.getCompoundTag("inventory"));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block4 : switch (animationEvent.getController().getName()) {
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.bee.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.bee." + ((Boolean) this.entityDataManager.get(HAS_CHEST) ? "idle_has_chest" : "idle"), true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case CITIZEN_START: {
                        this.createAnimation("animation.bee.sex_start", false, animationEvent);
                        break block4;
                    }
                    case CITIZEN_SLOW: {
                        this.createAnimation("animation.bee.sex_slow", true, animationEvent);
                        break block4;
                    }
                    case CITIZEN_FAST: {
                        this.createAnimation("animation.bee.sex_fast", true, animationEvent);
                        break block4;
                    }
                    case CITIZEN_CUM: {
                        this.createAnimation("animation.bee.sex_cum", false, animationEvent);
                        break block4;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bee.throw_pearl", true, animationEvent);
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
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "pearl": {
                    if (!this.boolean_e() || this.currentAction() != Action.THROW_PEARL) break;
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "resetCumPercentage": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "sex_fastMSG1": {
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "sex_startMSG1": {
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "sex_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                }
                case "sex_startDone": {
                    this.setCurrentAction(Action.CITIZEN_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "sex_cumMSG1": {
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_CUMINFLATION), 2.0f);
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "blackscreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "sex_cumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                    break;
                }
                case "sex_fastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    private static RuntimeException b(RuntimeException runtimeException) {
        return runtimeException;
    }
}

