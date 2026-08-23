/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bee;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.SendCompanionHome;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.companion.supporter.SupporterCompanion;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.Supporter;
import com.trolmastercard.sexmod.gui.Menu.SupporterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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
    public float hornyLevel = 3200.0f;
    int particleTicks = 0;
    final static float HORNY_SEX_LEVEL = 4800.0f;
    final static float HORNY_RANGE = 10.0f;
    final static public DataParameter<Boolean> IS_TAMED;

    static {
        IS_TAMED = EntityDataManager.createKey(BeeEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(112);
    }

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
    public float getScaleFactor() {
        return -0.1f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(IS_TAMED, false);
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
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new SupporterCompanion(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.watchClosestGirlGoal);
        this.tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, 1.0));
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.isPotionActive(HornyPotion.HORNY_POTION) && this.hornyLevel < 4800.0f && this.getInteractionPlayerUUID() == null) {
            this.removePotionEffect(HornyPotion.HORNY_POTION);
            this.hornyLevel = 6.9420184E7f;
        }
        this.sexLogic();
        if (this.getCurrentAction().equals((Object) Action.CITIZEN_CUM)) {
            this.particleTicks = Math.max(1, this.particleTicks);
        }
        this.doParticleStuff();
        this.b_15();
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.CITIZEN_CUM && (action == Action.CITIZEN_FAST || action == Action.COWGIRLSLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    void sexLogic() {
        if (this.getInteractionPlayerUUID() != null) {
            return;
        }
        if (this.hasMaster()) {
            return;
        }
        this.hornyLevel += 1.0f;
        if (this.hornyLevel < 4800.0f) {
            return;
        }
        EntityPlayer closestPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (closestPlayer == null) {
            return;
        }
        if (BeeEntity.getActiveSceneInfo(closestPlayer) != null) {
            return;
        }
        if (PlayerGirl.isOwnerPlayer(closestPlayer)) {
            return;
        }
        if (closestPlayer.getDistance(this) < 1.5f) {
            this.hornyLevel = 0.0f;
            this.setInteractionPlayerUUID(closestPlayer.getPersistentID());
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setTargetPosition(this.getFrontOffsetVector());
            this.setYawRotation(closestPlayer.rotationYaw - 180.0f);
            this.pathNavigator.clearPath();
            PacketHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)closestPlayer);
            this.setCurrentAction(Action.CITIZEN_START);
            Vec3d forward = this.getFrontOffsetVector(0.2);
            closestPlayer.setPositionAndUpdate(forward.x, forward.y, forward.z);
        } else {
            this.pathNavigator.clearPath();
            this.pathNavigator.tryMoveToEntityLiving(closestPlayer, 1.0);
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

    void doParticleStuff() {
        if (this.particleTicks == 0) {
            return;
        }
        ++this.particleTicks;
        if (this.entityDataManager.get(IS_TAMED)) {
            if (this.particleTicks < 40) {
                for (EntityPlayer entityPlayer : this.world.playerEntities) {
                    if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
                    ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.HEART, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 1, new int[0]));
                }
            } else {
                this.particleTicks = 0;
            }
        } else if (this.particleTicks < 200) {
            for (EntityPlayer entityPlayer : this.world.playerEntities) {
                if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
                ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.SPELL, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 1, new int[0]));
            }
        } else if (this.particleTicks == 200) {
            this.entityDataManager.set(IS_TAMED, this.getRNG().nextBoolean());
        } else if (this.particleTicks < 250) {
            for (EntityPlayer entityPlayer : this.world.playerEntities) {
                if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
                ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(this.entityDataManager.get(IS_TAMED) ? EnumParticleTypes.HEART : EnumParticleTypes.VILLAGER_ANGRY, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 3, new int[0]));
            }
        } else {
            this.particleTicks = 0;
        }
        for (EntityPlayer entityPlayer : this.world.playerEntities) {
            if (!(entityPlayer.getDistance(this) < 15.0f)) continue;
            ((EntityPlayerMP)entityPlayer).connection.sendPacket(new SPacketParticles(EnumParticleTypes.SPELL, true, (float)this.posX, (float)this.posY + 0.3f, (float)this.posZ, 0.2f, 0.3f, 0.2f, 0.25f, 10, new int[0]));
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.hornyLevel < HORNY_SEX_LEVEL && !this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.4;
        }
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (this.entityDataManager.get(IS_TAMED) && !(Boolean) this.entityDataManager.get(HAS_CHEST) && entityPlayer.getHeldItem(enumHand).getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
            this.entityDataManager.set(HAS_CHEST, true);
            entityPlayer.getHeldItem(enumHand).shrink(1);
            return super.processInteract(entityPlayer, enumHand);
        }
        if (this.world.isRemote && this.entityDataManager.get(IS_TAMED)) {
            this.DisplayBeeUI(entityPlayer);
        }
        return super.processInteract(entityPlayer, enumHand);
    }

    @SideOnly(value=Side.CLIENT)
    void DisplayBeeUI(EntityPlayer entityPlayer) {
        Minecraft.getMinecraft().displayGuiScreen(new SupporterUI(this, entityPlayer));
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        return false;
    }

    @Override
    public void doAction(String string, UUID uUID) {
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.CITIZEN_FAST || action == Action.CITIZEN_SLOW) {
            return Action.CITIZEN_CUM;
        }
        return null;
    }

    @Override
    protected void doAction() {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("isTamed", this.entityDataManager.get(IS_TAMED));
        nbt.setBoolean("hasChest", (Boolean)this.entityDataManager.get(HAS_CHEST));
        nbt.setTag("inventory", this.invHandler.serializeNBT());
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("isTamed")) {
            this.entityDataManager.set(IS_TAMED, nBTTagCompound.getBoolean("isTamed"));
        }
        this.entityDataManager.set(HAS_CHEST, nBTTagCompound.getBoolean("hasChest"));
        this.invHandler.deserializeNBT(nBTTagCompound.getCompoundTag("inventory"));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        switch (event.getController().getName()) {
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.bee.null", true, event);
                    break;
                }
                this.createAnimation("animation.bee." + ((Boolean) this.entityDataManager.get(HAS_CHEST) ? "idle_has_chest" : "idle"), true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case CITIZEN_START: {
                        this.createAnimation("animation.bee.sex_start", false, event);
                        break;
                    }
                    case CITIZEN_SLOW: {
                        this.createAnimation("animation.bee.sex_slow", true, event);
                        break;
                    }
                    case CITIZEN_FAST: {
                        this.createAnimation("animation.bee.sex_fast", true, event);
                        break;
                    }
                    case CITIZEN_CUM: {
                        this.createAnimation("animation.bee.sex_cum", false, event);
                        break;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bee.throw_pearl", true, event);
                        break;
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
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "pearl": {
                    if (!this.isLocalPlayerNearby() || this.getCurrentAction() != Action.THROW_PEARL) break;
                    PacketHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "resetCumPercentage": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "sex_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "sex_startMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "sex_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                }
                case "sex_startDone": {
                    this.setCurrentAction(Action.CITIZEN_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "sex_cumMSG1": {
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_CUMINFLATION), 2.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "blackscreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "sex_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "sex_fastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
}

