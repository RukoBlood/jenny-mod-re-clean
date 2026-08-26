/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Allie;

import java.util.UUID;

import com.trolmastercard.sexmod.Packets.MakeRichWish;
import com.trolmastercard.sexmod.Packets.UploadInventoryToServerAlt;
import com.trolmastercard.sexmod.Packets.SyncActionPacket;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class AllieEntity extends GirlEntity {
//    final static public int Q = 300;
//    final static public int K = 8;
    final static public Vec3d LAMP_OFFSET = new Vec3d(0.5, 1.0, 0.0);
    float LAMP_SCALE = 1.0f;
    public boolean isLampActive = false;
    final static public DataParameter<ItemStack> LAMP_ITEM = EntityDataManager.createKey(AllieEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(111);
    boolean needsSpawnParticleRing = true;
    int reverseCowgirlSlowAnimVariant = 1;
    int reverseCowgirlFastAnimVariant = 1;
    boolean skipFastMoanSound = false;
    boolean skipLampActivation = false;

    public AllieEntity(World world) {
        super(world);
        this.setSize((float) AllieEntity.LAMP_OFFSET.x, (float) AllieEntity.LAMP_OFFSET.y);
    }

    public AllieEntity(World world, ItemStack itemStack) {
        this(world);
        this.entityDataManager.set(AllieEntity.LAMP_ITEM, itemStack);
    }

    @Override
    public String getGirlName() {
        return "Allie";
    }

    @Override
    public float getScaleFactor() {
        return 1.0f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(LAMP_ITEM, ItemStack.EMPTY);
    }

    public boolean hasLampItem() {
        NBTTagCompound nbt = this.entityDataManager.get(LAMP_ITEM).getTagCompound();
        return nbt == null || nbt.getInteger("sexmodUses") == 1;
    }

    @Override
    public void updateAITasks() {
        UUID uUID;
        super.updateAITasks();
        if (this.getCurrentAction() == Action.NULL) {
            this.world.removeEntity(this);
        }
        if ((uUID = this.getInteractionPlayerUUID()) != null) {
            EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
            if (player == null) {
                this.world.removeEntity(this);
            }
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void AcSomeUnknownClass() {
        if (!this.skipLampActivation) {
            this.isLampActive = true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.LAMP_SCALE != 1.0f && this.LAMP_SCALE != -69.0f && this.LAMP_SCALE <= 0.0f) {
            if (this.isControlledByLocalPlayer()) {
                PacketHandler.INSTANCE.sendToServer(new UploadInventoryToServerAlt(this.girlID()));
                HandlePlayerMovement.setMovementLock(true);
            }
            this.LAMP_SCALE = -69.0f;
        }
        if (this.world.isRemote) {
            if (this.isLampActive) {
                this.openInteraction();
            }
            if (this.needsSpawnParticleRing) {
                this.resetToDefaultState();
            }
            this.spawnRandomParticles();
        }
    }

    void spawnRandomParticles() {
        if (this.ticksExisted % 10 == 0) {
            int n = this.getRNG().nextInt(8);
            Vec3d vec3d = this.getCachedBoneOffset("tail" + n).add(this.getPositionVector());
            this.world.spawnParticle(EnumParticleTypes.PORTAL, vec3d.x, vec3d.y, vec3d.z, this.getRNG().nextGaussian() * (double) 0.01f, this.getRNG().nextGaussian() * (double) 0.01f, this.getRNG().nextGaussian() * (double) 0.01f);
        }
    }

    @SideOnly(value=Side.CLIENT)
    void resetToDefaultState() {
        this.needsSpawnParticleRing = false;
        WorldUtils.SpawnParticleRing(this.world, EnumParticleTypes.PORTAL, this.getPositionVector(), 300, 0.75, 1.5);
    }

    @SideOnly(value=Side.CLIENT)
    void openInteraction() {
        this.openInteractionMenu(Minecraft.getMinecraft().player);
        this.isLampActive = false;
    }

    @Override
    public boolean openInteractionMenu(EntityPlayer player) {
        this.skipLampActivation = false;
        String[] options = new String[]{"action.names.makemerichallie", "action.names.deepthroat", "Reverse cowgirl"};
        AllieEntity.openInventoryGui(player, this, options, false);
        return true;
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_FAST;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW) {
            return Action.REVERSE_COWGIRL_FAST_START;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.DEEPTHROAT_FAST || action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_CUM;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW || action == Action.REVERSE_COWGIRL_FAST_START || action == Action.REVERSE_COWGIRL_FAST_CONTINUES) {
            return Action.REVERSE_COWGIRL_CUM;
        }
        return null;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() != Action.DEEPTHROAT_CUM || (action != Action.DEEPTHROAT_FAST && action != Action.DEEPTHROAT_SLOW)) {
            if (this.getCurrentAction() != Action.REVERSE_COWGIRL_CUM || (action != Action.REVERSE_COWGIRL_SLOW && action != Action.REVERSE_COWGIRL_FAST_START && action != Action.REVERSE_COWGIRL_FAST_CONTINUES)) {
                if (!this.world.isRemote && action == Action.REVERSE_COWGIRL_START) {
                    this.handleAllieOwner();
                }
                super.setCurrentAction(action);
            }
        }
    }

    void handleAllieOwner() {
        EntityPlayer player = this.getPlayerEntity();
        if (player != null) {
            Vec3d vec3d = this.getTargetPosition();
            player.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        }
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() == Action.NULL && this.getCurrentAction().autoBlink) break;
                this.createAnimation("animation.allie.null", true, event);
                break;
            }
            case "movement": {
                this.createAnimation("animation.allie.tail", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case SUMMON: {
                        this.createAnimation("animation.allie.summon", false, event);
                        break;
                    }
                    case SUMMON_NORMAL: {
                        this.createAnimation("animation.allie.summon_normal", false, event);
                        break;
                    }
                    case SUMMON_NORMAL_WAIT: {
                        this.createAnimation("animation.allie.summon_normal_wait", true, event);
                        break;
                    }
                    case SUMMON_WAIT: {
                        this.createAnimation("animation.allie.summon_wait", true, event);
                        break;
                    }
                    case ALLIE_PREPARE_FIRST_TIME: {
                        this.createAnimation("animation.allie.deepthroat_prepare", false, event);
                        break;
                    }
                    case ALLIE_PREPARE_NORMAL: {
                        this.createAnimation("animation.allie.deepthroat_normal_prepare", false, event);
                        break;
                    }
                    case DEEPTHROAT_START: {
                        this.createAnimation("animation.allie.deepthroat_start", false, event);
                        break;
                    }
                    case DEEPTHROAT_SLOW: {
                        this.createAnimation("animation.allie.deepthroat_slow", true, event);
                        break;
                    }
                    case DEEPTHROAT_FAST: {
                        this.createAnimation("animation.allie.deepthroat_fast", true, event);
                        break;
                    }
                    case DEEPTHROAT_CUM: {
                        this.createAnimation("animation.allie.deepthroat_cum", false, event);
                        break;
                    }
                    case RICH_FIRST_TIME: {
                        this.createAnimation("animation.allie.rich", false, event);
                        break;
                    }
                    case RICH_NORMAL: {
                        this.createAnimation("animation.allie.rich_normal", false, event);
                        break;
                    }
                    case SUMMON_SAND: {
                        this.createAnimation("animation.allie.summon_sand", false, event);
                        break;
                    }
                    case REVERSE_COWGIRL_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_start", true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_SLOW: {
                        this.createAnimation("animation.allie.reverse_cowgirl_slow" + this.reverseCowgirlSlowAnimVariant, true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_FAST_CONTINUES: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fastc" + this.reverseCowgirlFastAnimVariant, true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_FAST_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fasts", true, event);
                        break;
                    }
                    case REVERSE_COWGIRL_CUM: {
                        this.createAnimation("animation.allie.reverse_cowgirl_cum", true, event);
                        break;
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "summonMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon1"));
                    this.playSoundAtVolume(SoundsHandler.GIRLS_ALLIE_SCAWY[0], 0.5f);
                    break;
                }
                case "summonMSG2": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon2"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[this.getRNG().nextInt(4)]);
                    break;
                }
                case "summonMSG3": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon3"));
                    break;
                }
                case "summonMSG4": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon4"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIGHTBREATHING[2]);
                    break;
                }
                case "summonMSG5": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon5"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HMPH[4]);
                    break;
                }
                case "summonMSG6": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon6"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[3]);
                    break;
                }
                case "summonMSG7": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon7"));
                    break;
                }
                case "summonMSG8": {
                    this.sendChatMessage(I18n.format("allie.dialogue.summon8"));
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_HUH);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.openInteractionMenu(this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID()));
                    break;
                }
                case "summonDone": {
                    this.setCurrentAction(Action.SUMMON_WAIT);
                    break;
                }
                case "deepthroat_prepareMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.hihi"));
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_GIGGLE);
                    break;
                }
                case "deepthroat_prepareMSG2": {
                    this.sendChatMessage(I18n.format("allie.dialogue.boys"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SIGH[0]);
                    break;
                }
                case "scream": {
                    this.playRandomSound(SoundsHandler.MISC_SCREAM);
                    break;
                }
                case "blackscreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "deepthroat_prepareDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    if ("reverse_cowgirl".equals(this.entityDataManager.get(GIRL_HAND_STATES))) {
                        this.rotationPitch = 30.0f;
                        this.setCurrentAction(Action.REVERSE_COWGIRL_START);
                        break;
                    }
                    this.setCurrentAction(Action.DEEPTHROAT_START);
                    PacketHandler.INSTANCE.sendToServer(new SyncActionPacket(this.girlID(), this.getInteractionPlayerUUID(), false, true));
                    this.cameraYaw = this.rotationYaw + 180.0f;
                    this.moveCamera(0.0, 0.0, 1.35f, 0.0f, 30.0f);
                    SexUI.resetCumPercentage();
                    break;
                }
                case "deepthroat_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_startDone": {
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_BJMOAN));
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "deepthroat_slowMSG1": {
                    if (this.getRNG().nextFloat() > 0.33f) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_BJMOAN));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "deepthroat_cumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MOAN));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    this.playSoundAtVolume(SoundsHandler.random(SoundsHandler.MISC_CUMINFLATION), 1.5f);
                    break;
                }
                case "cowgirl_cumDone": 
                case "deepthroat_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    PacketHandler.INSTANCE.sendToServer(new UploadInventoryToServerAlt(this.girlID()));
                    break;
                }
                case "summon_normalMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.sup"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[this.getRNG().nextInt(4)]);
                    break;
                }
                case "summon_normalMSG2": {
                    this.sendChatMessage(I18n.format("allie.dialogue.youhave"));
                    break;
                }
                case "summon_normalMSG3": {
                    if (this.entityDataManager.get(LAMP_ITEM).getTagCompound().getInteger("sexmodUses") == 2) {
                        this.sendChatMessage(I18n.format("allie.dialogue.2wishes"));
                    } else {
                        this.sendChatMessage(I18n.format("allie.dialogue.1wish"));
                    }
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HMPH[4]);
                    break;
                }
                case "summon_normalMSG4": {
                    this.sendChatMessage("So...");
                    break;
                }
                case "summon_normalMSG5": {
                    this.sendChatMessage(I18n.format("allie.dialogue.tellme"));
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_HUH);
                    break;
                }
                case "summon_normalDone": {
                    this.setCurrentAction(Action.SUMMON_NORMAL_WAIT);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.openInteractionMenu(Minecraft.getMinecraft().player);
                    break;
                }
                case "deepthroat_normal_prepareMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.alright"));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_GIGGLE));
                    break;
                }
                case "rich_MSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.wishgranted"));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_PLOB));
                    if (!this.isControlledByLocalPlayer()) break;
                    PacketHandler.INSTANCE.sendToServer(new MakeRichWish(this.getPositionVector()));
                    break;
                }
                case "disappear": {
                    this.LAMP_SCALE = 0.99f;
                    break;
                }
                case "summon_sandMSG1": {
                    this.sendChatMessage(I18n.format("allie.dialogue.nooo"));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SCAWY[2]);
                    break;
                }
                case "summon_sandMSG2": {
                    if (!this.isLocalPlayerNearby()) break;
                    this.broadcastChatAround(I18n.format("allie.dialogue.phobia"), true);
                    break;
                }
                case "giggle": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_GIGGLE);
                    break;
                }
                case "pounding": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "moan": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_MOAN);
                    break;
                }
                case "mmm": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MMM));
                    break;
                }
                case "slide": {
                    this.playRandomSound(SoundsHandler.MISC_SLIDE, 0, 1, 4, 6);
                    break;
                }
                case "slowMoan": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_AHH));
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "cowgirlSlowDone": {
                    int n = this.reverseCowgirlSlowAnimVariant;
                    do {
                        this.reverseCowgirlSlowAnimVariant = this.getRNG().nextInt(3) + 1;
                    } while (this.reverseCowgirlSlowAnimVariant == n);
                    break;
                }
                case "fastMoan": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.skipFastMoanSound) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MOAN));
                        this.skipFastMoanSound = true;
                        break;
                    }
                    this.skipFastMoanSound = false;
                    break;
                }
                case "fastSwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    Action fp_class3242 = this.getCurrentAction();
                    if (fp_class3242 == Action.REVERSE_COWGIRL_FAST_START) {
                        this.setCurrentAction(Action.REVERSE_COWGIRL_FAST_CONTINUES);
                        break;
                    }
                    this.resetAnimationControllerOffset();
                    int n = this.reverseCowgirlFastAnimVariant;
                    do {
                        this.reverseCowgirlFastAnimVariant = this.getRNG().nextInt(3) + 1;
                    } while (this.reverseCowgirlFastAnimVariant == n);
                    break;
                }
                case "openSexUi": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "cum": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "aftermoan": {
                    this.playRandomSound(SoundsHandler.GIRLS_ALLIE_AFTERSESSIONMOAN);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

    @Override
    public void doAction(String string, UUID uUID) {
        this.skipLampActivation = true;
        if ("action.names.makemerichallie".equals(string)) {
            this.setCurrentAction(this.hasLampItem() ? Action.RICH_FIRST_TIME : Action.RICH_NORMAL);
            return;
        }
        this.changeDataParameterFromClient("animationFollowUp", "action.names.deepthroat".equals(string) ? "deepthroat" : "reverse_cowgirl");
        this.setCurrentAction(this.hasLampItem() ? Action.ALLIE_PREPARE_FIRST_TIME : Action.ALLIE_PREPARE_NORMAL);
    }
}

