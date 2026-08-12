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
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class AllieEntity
extends GirlEntity {
    final static public int Q = 300;
    final static public int K = 8;
    final static public Vec3d O = new Vec3d(0.5, 1.0, 0.0);
    float U = 1.0f;
    public boolean P = false;
    final static public DataParameter<ItemStack> itemStack = EntityDataManager.createKey(AllieEntity.class, DataSerializers.ITEM_STACK).getSerializer().createKey(111);
    boolean S = true;
    int T = 1;
    int L = 1;
    boolean M = false;
    boolean R = false;

    public AllieEntity(World world) {
        super(world);
        this.setSize((float) AllieEntity.O.x, (float) AllieEntity.O.y);
    }

    public AllieEntity(World world, ItemStack itemStack) {
        this(world);
        this.entityDataManager.set(AllieEntity.itemStack, itemStack);
    }

    @Override
    public String getGirlName() {
        return "Allie";
    }

    @Override
    public float getNameTagHeightOffset() {
        return 1.0f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(itemStack, ItemStack.EMPTY);
    }

    public boolean boolean_f() {
        NBTTagCompound nBTTagCompound = this.entityDataManager.get(itemStack).getTagCompound();
        if (nBTTagCompound == null) {
            return true;
        }
        return nBTTagCompound.getInteger("sexmodUses") == 1;
    }

    @Override
    public void updateAITasks() {
        UUID uUID;
        super.updateAITasks();
        if (this.currentAction() == Action.NULL) {
            this.world.removeEntity(this);
        }
        if ((uUID = this.playerSheHasSexWith()) == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            this.world.removeEntity(this);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void ac() {
        if (!this.R) {
            this.P = true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.U != 1.0f && this.U != -69.0f && this.U <= 0.0f) {
            if (this.isControlledByLocalPlayer()) {
                PackageHandler.INSTANCE.sendToServer((IMessage)new UploadInventoryToServerAlt(this.girlID()));
                HandlePlayerMovement.setMovementLock(true);
            }
            this.U = -69.0f;
        }
        if (!this.world.isRemote) {
            return;
        }
        if (this.P) {
            this.void_c();
        }
        if (this.S) {
            this.void_d();
        }
        this.b_16();
    }

    void b_16() {
        if (this.ticksExisted % 10 != 0) {
            return;
        }
        int n = this.getRNG().nextInt(8);
        Vec3d vec3d = this.getCachedBoneOffset("tail" + n).add(this.getPositionVector());
        this.world.spawnParticle(EnumParticleTypes.PORTAL, vec3d.x, vec3d.y, vec3d.z, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, new int[0]);
    }

    @SideOnly(value=Side.CLIENT)
    void void_d() {
        this.S = false;
        WorldUtils.SpawnParticleRing(this.world, EnumParticleTypes.PORTAL, this.getPositionVector(), 300, 0.75, 1.5);
    }

    @SideOnly(value=Side.CLIENT)
    void void_c() {
        this.openGuiForPlayer(Minecraft.getMinecraft().player);
        this.P = false;
    }

    @Override
    public boolean openGuiForPlayer(EntityPlayer player) {
        this.R = false;
        String[] stringArray = new String[]{"action.names.makemerichallie", "action.names.deepthroat", "Reverse cowgirl"};
        AllieEntity.openInventoryGui(player, this, stringArray, false);
        return true;
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.DEEPTHROAT_SLOW) {
            return Action.DEEPTHROAT_FAST;
        }
        if (action == Action.REVERSE_COWGIRL_SLOW) {
            return Action.REVERSE_COWGIRL_FAST_START;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
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
        if (this.currentAction() == Action.DEEPTHROAT_CUM && (action == Action.DEEPTHROAT_FAST || action == Action.DEEPTHROAT_SLOW)) {
            return;
        }
        if (this.currentAction() == Action.REVERSE_COWGIRL_CUM && (action == Action.REVERSE_COWGIRL_SLOW || action == Action.REVERSE_COWGIRL_FAST_START || action == Action.REVERSE_COWGIRL_FAST_CONTINUES)) {
            return;
        }
        if (!this.world.isRemote && action == Action.REVERSE_COWGIRL_START) {
            this.a_();
        }
        super.setCurrentAction(action);
    }

    void a_() {
        EntityPlayer entityPlayer = this.getPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        Vec3d vec3d = this.getTargetPosition();
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block5 : switch (event.getController().getName()) {
            case "eyes": {
                if (this.currentAction() == Action.NULL && this.currentAction().autoBlink) break;
                this.createAnimation("animation.allie.null", true, event);
                break;
            }
            case "movement": {
                this.createAnimation("animation.allie.tail", true, event);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case SUMMON: {
                        this.createAnimation("animation.allie.summon", false, event);
                        break block5;
                    }
                    case SUMMON_NORMAL: {
                        this.createAnimation("animation.allie.summon_normal", false, event);
                        break block5;
                    }
                    case SUMMON_NORMAL_WAIT: {
                        this.createAnimation("animation.allie.summon_normal_wait", true, event);
                        break block5;
                    }
                    case SUMMON_WAIT: {
                        this.createAnimation("animation.allie.summon_wait", true, event);
                        break block5;
                    }
                    case ALLIE_PREPARE_FIRST_TIME: {
                        this.createAnimation("animation.allie.deepthroat_prepare", false, event);
                        break block5;
                    }
                    case ALLIE_PREPARE_NORMAL: {
                        this.createAnimation("animation.allie.deepthroat_normal_prepare", false, event);
                        break block5;
                    }
                    case DEEPTHROAT_START: {
                        this.createAnimation("animation.allie.deepthroat_start", false, event);
                        break block5;
                    }
                    case DEEPTHROAT_SLOW: {
                        this.createAnimation("animation.allie.deepthroat_slow", true, event);
                        break block5;
                    }
                    case DEEPTHROAT_FAST: {
                        this.createAnimation("animation.allie.deepthroat_fast", true, event);
                        break block5;
                    }
                    case DEEPTHROAT_CUM: {
                        this.createAnimation("animation.allie.deepthroat_cum", false, event);
                        break block5;
                    }
                    case RICH_FIRST_TIME: {
                        this.createAnimation("animation.allie.rich", false, event);
                        break block5;
                    }
                    case RICH_NORMAL: {
                        this.createAnimation("animation.allie.rich_normal", false, event);
                        break block5;
                    }
                    case SUMMON_SAND: {
                        this.createAnimation("animation.allie.summon_sand", false, event);
                        break block5;
                    }
                    case REVERSE_COWGIRL_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_start", true, event);
                        break block5;
                    }
                    case REVERSE_COWGIRL_SLOW: {
                        this.createAnimation("animation.allie.reverse_cowgirl_slow" + this.T, true, event);
                        break block5;
                    }
                    case REVERSE_COWGIRL_FAST_CONTINUES: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fastc" + this.L, true, event);
                        break block5;
                    }
                    case REVERSE_COWGIRL_FAST_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fasts", true, event);
                        break block5;
                    }
                    case REVERSE_COWGIRL_CUM: {
                        this.createAnimation("animation.allie.reverse_cowgirl_cum", true, event);
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
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon1", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SCAWY[0], 0.5f);
                    break;
                }
                case "summonMSG2": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[this.getRNG().nextInt(4)]);
                    break;
                }
                case "summonMSG3": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon3", new Object[0]));
                    break;
                }
                case "summonMSG4": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon4", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIGHTBREATHING[2]);
                    break;
                }
                case "summonMSG5": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon5", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HMPH[4]);
                    break;
                }
                case "summonMSG6": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon6", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[3]);
                    break;
                }
                case "summonMSG7": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon7", new Object[0]));
                    break;
                }
                case "summonMSG8": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.summon8", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HUH, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.openGuiForPlayer(this.world.getPlayerEntityByUUID(this.playerSheHasSexWith()));
                    break;
                }
                case "summonDone": {
                    this.setCurrentAction(Action.SUMMON_WAIT);
                    break;
                }
                case "deepthroat_prepareMSG1": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE, new int[0]);
                    break;
                }
                case "deepthroat_prepareMSG2": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.boys", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SIGH[0]);
                    break;
                }
                case "scream": {
                    this.PlaySound(SoundsHandler.MISC_SCREAM, new int[0]);
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
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SyncActionPacket(this.girlID(), this.playerSheHasSexWith(), false, true));
                    this.cameraYaw = this.rotationYaw + 180.0f;
                    this.moveCamera(0.0, 0.0, (double)1.35f, 0.0f, 30.0f);
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
                    SexUI.init();
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
                    SexUI.init();
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "deepthroat_cumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MOAN));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_CUMINFLATION), 1.5f);
                    break;
                }
                case "cowgirl_cumDone": 
                case "deepthroat_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    PackageHandler.INSTANCE.sendToServer((IMessage)new UploadInventoryToServerAlt(this.girlID()));
                    break;
                }
                case "summon_normalMSG1": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.sup", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[this.getRNG().nextInt(4)]);
                    break;
                }
                case "summon_normalMSG2": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.youhave", new Object[0]));
                    break;
                }
                case "summon_normalMSG3": {
                    if (this.entityDataManager.get(itemStack).getTagCompound().getInteger("sexmodUses") == 2) {
                        this.sendLocalClientMessage(I18n.format("allie.dialogue.2wishes", new Object[0]));
                    } else {
                        this.sendLocalClientMessage(I18n.format("allie.dialogue.1wish", new Object[0]));
                    }
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HMPH[4]);
                    break;
                }
                case "summon_normalMSG4": {
                    this.sendLocalClientMessage("So...");
                    break;
                }
                case "summon_normalMSG5": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.tellme", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HUH, new int[0]);
                    break;
                }
                case "summon_normalDone": {
                    this.setCurrentAction(Action.SUMMON_NORMAL_WAIT);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.openGuiForPlayer(Minecraft.getMinecraft().player);
                    break;
                }
                case "deepthroat_normal_prepareMSG1": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.alright", new Object[0]));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_GIGGLE));
                    break;
                }
                case "rich_MSG1": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.wishgranted", new Object[0]));
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_PLOB));
                    if (!this.isControlledByLocalPlayer()) break;
                    PackageHandler.INSTANCE.sendToServer((IMessage)new MakeRichWish(this.getPositionVector()));
                    break;
                }
                case "disappear": {
                    this.U = 0.99f;
                    break;
                }
                case "summon_sandMSG1": {
                    this.sendLocalClientMessage(I18n.format("allie.dialogue.nooo", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SCAWY[2]);
                    break;
                }
                case "summon_sandMSG2": {
                    if (!this.getClosestPlayerID()) break;
                    this.b(I18n.format("allie.dialogue.phobia", new Object[0]), true);
                    break;
                }
                case "giggle": {
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE, new int[0]);
                    break;
                }
                case "pounding": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "moan": {
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_MOAN, new int[0]);
                    break;
                }
                case "mmm": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MMM));
                    break;
                }
                case "slide": {
                    this.PlaySound(SoundsHandler.MISC_SLIDE, 0, 1, 4, 6);
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
                    int n = this.T;
                    do {
                        this.T = this.getRNG().nextInt(3) + 1;
                    } while (this.T == n);
                    break;
                }
                case "fastMoan": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.M) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ALLIE_MOAN));
                        this.M = true;
                        break;
                    }
                    this.M = false;
                    break;
                }
                case "fastSwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    Action fp_class3242 = this.currentAction();
                    if (fp_class3242 == Action.REVERSE_COWGIRL_FAST_START) {
                        this.setCurrentAction(Action.REVERSE_COWGIRL_FAST_CONTINUES);
                        break;
                    }
                    this.resetAnimationControllerOffset();
                    int n = this.L;
                    do {
                        this.L = this.getRNG().nextInt(3) + 1;
                    } while (this.L == n);
                    break;
                }
                case "openSexUi": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.init();
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "aftermoan": {
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_AFTERSESSIONMOAN, new int[0]);
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
        this.R = true;
        if ("action.names.makemerichallie".equals(string)) {
            this.setCurrentAction(this.boolean_f() ? Action.RICH_FIRST_TIME : Action.RICH_NORMAL);
            return;
        }
        this.changeDataParameterFromClient("animationFollowUp", "action.names.deepthroat".equals(string) ? "deepthroat" : "reverse_cowgirl");
        this.setCurrentAction(this.boolean_f() ? Action.ALLIE_PREPARE_FIRST_TIME : Action.ALLIE_PREPARE_NORMAL);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

