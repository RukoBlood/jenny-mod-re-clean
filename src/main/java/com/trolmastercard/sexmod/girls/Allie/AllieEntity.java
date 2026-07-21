/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Allie;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.MakeRichWish;
import com.trolmastercard.sexmod.Packages.UploadInventoryToServerAlt;
import com.trolmastercard.sexmod.Packages.dc_class174;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.gui.fh_class313;
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
    public float float_i() {
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
        if ((uUID = this.getID()) == null) {
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
            if (this.boolean_n()) {
                PackageHandler.networkWrapper.sendToServer((IMessage)new UploadInventoryToServerAlt(this.girlID()));
                HandlePlayerMovement.a(true);
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
        Vec3d vec3d = this.b("tail" + n).add(this.getPositionVector());
        this.world.spawnParticle(EnumParticleTypes.PORTAL, vec3d.x, vec3d.y, vec3d.z, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, this.getRNG().nextGaussian() * (double)0.01f, new int[0]);
    }

    @SideOnly(value=Side.CLIENT)
    void void_d() {
        this.S = false;
        WorldUtils.SpawnParticleRing(this.world, EnumParticleTypes.PORTAL, this.getPositionVector(), 300, 0.75, 1.5);
    }

    @SideOnly(value=Side.CLIENT)
    void void_c() {
        this.boolean_b(Minecraft.getMinecraft().player);
        this.P = false;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        this.R = false;
        String[] stringArray = new String[]{"action.names.makemerichallie", "action.names.deepthroat", "Reverse cowgirl"};
        AllieEntity.a(entityPlayer, this, stringArray, false);
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() == Action.NULL && this.currentAction().autoBlink) break;
                this.createAnimation("animation.allie.null", true, animationEvent);
                break;
            }
            case "movement": {
                this.createAnimation("animation.allie.tail", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case SUMMON: {
                        this.createAnimation("animation.allie.summon", false, animationEvent);
                        break block5;
                    }
                    case SUMMON_NORMAL: {
                        this.createAnimation("animation.allie.summon_normal", false, animationEvent);
                        break block5;
                    }
                    case SUMMON_NORMAL_WAIT: {
                        this.createAnimation("animation.allie.summon_normal_wait", true, animationEvent);
                        break block5;
                    }
                    case SUMMON_WAIT: {
                        this.createAnimation("animation.allie.summon_wait", true, animationEvent);
                        break block5;
                    }
                    case ALLIE_PREPARE_FIRST_TIME: {
                        this.createAnimation("animation.allie.deepthroat_prepare", false, animationEvent);
                        break block5;
                    }
                    case ALLIE_PREPARE_NORMAL: {
                        this.createAnimation("animation.allie.deepthroat_normal_prepare", false, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_START: {
                        this.createAnimation("animation.allie.deepthroat_start", false, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_SLOW: {
                        this.createAnimation("animation.allie.deepthroat_slow", true, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_FAST: {
                        this.createAnimation("animation.allie.deepthroat_fast", true, animationEvent);
                        break block5;
                    }
                    case DEEPTHROAT_CUM: {
                        this.createAnimation("animation.allie.deepthroat_cum", false, animationEvent);
                        break block5;
                    }
                    case RICH_FIRST_TIME: {
                        this.createAnimation("animation.allie.rich", false, animationEvent);
                        break block5;
                    }
                    case RICH_NORMAL: {
                        this.createAnimation("animation.allie.rich_normal", false, animationEvent);
                        break block5;
                    }
                    case SUMMON_SAND: {
                        this.createAnimation("animation.allie.summon_sand", false, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_start", true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_SLOW: {
                        this.createAnimation("animation.allie.reverse_cowgirl_slow" + this.T, true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_FAST_CONTINUES: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fastc" + this.L, true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_FAST_START: {
                        this.createAnimation("animation.allie.reverse_cowgirl_fasts", true, animationEvent);
                        break block5;
                    }
                    case REVERSE_COWGIRL_CUM: {
                        this.createAnimation("animation.allie.reverse_cowgirl_cum", true, animationEvent);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData animationData) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "summonMSG1": {
                    this.void_a(I18n.format("allie.dialogue.summon1", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ALLIE_SCAWY[0], 0.5f);
                    break;
                }
                case "summonMSG2": {
                    this.void_a(I18n.format("allie.dialogue.summon2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[this.getRNG().nextInt(4)]);
                    break;
                }
                case "summonMSG3": {
                    this.void_a(I18n.format("allie.dialogue.summon3", new Object[0]));
                    break;
                }
                case "summonMSG4": {
                    this.void_a(I18n.format("allie.dialogue.summon4", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIGHTBREATHING[2]);
                    break;
                }
                case "summonMSG5": {
                    this.void_a(I18n.format("allie.dialogue.summon5", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HMPH[4]);
                    break;
                }
                case "summonMSG6": {
                    this.void_a(I18n.format("allie.dialogue.summon6", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[3]);
                    break;
                }
                case "summonMSG7": {
                    this.void_a(I18n.format("allie.dialogue.summon7", new Object[0]));
                    break;
                }
                case "summonMSG8": {
                    this.void_a(I18n.format("allie.dialogue.summon8", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HUH, new int[0]);
                    if (!this.boolean_n()) break;
                    this.boolean_b(this.world.getPlayerEntityByUUID(this.getID()));
                    break;
                }
                case "summonDone": {
                    this.setCurrentAction(Action.SUMMON_WAIT);
                    break;
                }
                case "deepthroat_prepareMSG1": {
                    this.void_a(I18n.format("allie.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE, new int[0]);
                    break;
                }
                case "deepthroat_prepareMSG2": {
                    this.void_a(I18n.format("allie.dialogue.boys", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SIGH[0]);
                    break;
                }
                case "scream": {
                    this.PlaySound(SoundsHandler.MISC_SCREAM, new int[0]);
                    break;
                }
                case "blackscreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "deepthroat_prepareDone": {
                    if (!this.boolean_n()) break;
                    if ("reverse_cowgirl".equals(this.entityDataManager.get(GIRL_HAND_STATES))) {
                        this.rotationPitch = 30.0f;
                        this.setCurrentAction(Action.REVERSE_COWGIRL_START);
                        break;
                    }
                    this.setCurrentAction(Action.DEEPTHROAT_START);
                    PackageHandler.networkWrapper.sendToServer((IMessage)new dc_class174(this.girlID(), this.getID(), false, true));
                    this.cameraYaw = this.rotationYaw + 180.0f;
                    this.moveCamera(0.0, 0.0, (double)1.35f, 0.0f, 30.0f);
                    SexUI.resetCumPercentage();
                    break;
                }
                case "deepthroat_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_startDone": {
                    this.setCurrentAction(Action.DEEPTHROAT_SLOW);
                    break;
                }
                case "deepthroat_fastMSG1": {
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_BJMOAN));
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "deepthroat_slowMSG1": {
                    if (this.getRNG().nextFloat() > 0.33f) {
                        this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    } else {
                        this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_BJMOAN));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "deepthroat_cumMSG1": {
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_MOAN));
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
                    this.a(SoundsHandler.getRandomSound(SoundsHandler.MISC_CUMINFLATION), 1.5f);
                    break;
                }
                case "cowgirl_cumDone": 
                case "deepthroat_cumDone": {
                    if (!this.boolean_n()) break;
                    this.void_r();
                    PackageHandler.networkWrapper.sendToServer((IMessage)new UploadInventoryToServerAlt(this.girlID()));
                    break;
                }
                case "summon_normalMSG1": {
                    this.void_a(I18n.format("allie.dialogue.sup", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_GIGGLE[this.getRNG().nextInt(4)]);
                    break;
                }
                case "summon_normalMSG2": {
                    this.void_a(I18n.format("allie.dialogue.youhave", new Object[0]));
                    break;
                }
                case "summon_normalMSG3": {
                    if (this.entityDataManager.get(itemStack).getTagCompound().getInteger("sexmodUses") == 2) {
                        this.void_a(I18n.format("allie.dialogue.2wishes", new Object[0]));
                    } else {
                        this.void_a(I18n.format("allie.dialogue.1wish", new Object[0]));
                    }
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HMPH[4]);
                    break;
                }
                case "summon_normalMSG4": {
                    this.void_a("So...");
                    break;
                }
                case "summon_normalMSG5": {
                    this.void_a(I18n.format("allie.dialogue.tellme", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_HUH, new int[0]);
                    break;
                }
                case "summon_normalDone": {
                    this.setCurrentAction(Action.SUMMON_NORMAL_WAIT);
                    if (!this.boolean_n()) break;
                    this.boolean_b(Minecraft.getMinecraft().player);
                    break;
                }
                case "deepthroat_normal_prepareMSG1": {
                    this.void_a(I18n.format("allie.dialogue.alright", new Object[0]));
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_GIGGLE));
                    break;
                }
                case "rich_MSG1": {
                    this.void_a(I18n.format("allie.dialogue.wishgranted", new Object[0]));
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.MISC_PLOB));
                    if (!this.boolean_n()) break;
                    PackageHandler.networkWrapper.sendToServer((IMessage)new MakeRichWish(this.getPositionVector()));
                    break;
                }
                case "disappear": {
                    this.U = 0.99f;
                    break;
                }
                case "summon_sandMSG1": {
                    this.void_a(I18n.format("allie.dialogue.nooo", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_SCAWY[2]);
                    break;
                }
                case "summon_sandMSG2": {
                    if (!this.boolean_e()) break;
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
                    this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_MMM));
                    break;
                }
                case "slide": {
                    this.PlaySound(SoundsHandler.MISC_SLIDE, 0, 1, 4, 6);
                    break;
                }
                case "slowMoan": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_AHH));
                    }
                    if (!this.boolean_n()) break;
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
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.M) {
                        this.PlaySound(SoundsHandler.getRandomSound(SoundsHandler.GIRLS_ALLIE_MOAN));
                        this.M = true;
                        break;
                    }
                    this.M = false;
                    break;
                }
                case "fastSwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    Action fp_class3242 = this.currentAction();
                    if (fp_class3242 == Action.REVERSE_COWGIRL_FAST_START) {
                        this.setCurrentAction(Action.REVERSE_COWGIRL_FAST_CONTINUES);
                        break;
                    }
                    this.N();
                    int n = this.L;
                    do {
                        this.L = this.getRNG().nextInt(3) + 1;
                    } while (this.L == n);
                    break;
                }
                case "openSexUi": {
                    if (!this.boolean_n()) break;
                    SexUI.init();
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "aftermoan": {
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_AFTERSESSIONMOAN, new int[0]);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    @Override
    public void a(String string, UUID uUID) {
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

