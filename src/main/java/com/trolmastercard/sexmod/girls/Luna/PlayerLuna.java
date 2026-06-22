/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Luna;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
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

public class PlayerLuna
extends PlayerGirl {
    int ar = 0;
    boolean aq = false;
    boolean ap = false;
    boolean as = false;

    protected PlayerLuna(World world) {
        super(world);
    }

    public PlayerLuna(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 1.6f;
    }

    @Override
    public float getEyeHeight() {
        return 1.34f;
    }

    @Override
    public IRenderer com_trolmastercard_sexmod_at_class43_a(int n) {
        return new bf_class79();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/cat/hand.png";
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("action.names.touchboobs".equals(string)) {
            this.a(0, Action.TOUCH_BOOBS_INTRO);
            this.setCurrentAction(Action.TOUCH_BOOBS_INTRO);
            this.entityDataManager.set(D, 0);
            this.void_b(uUID);
        }
        if ("action.names.headpat".equals(string)) {
            this.setCurrentAction(Action.HEAD_PAT);
            this.void_b(uUID);
        }
    }

    @Override
    public void u_() {
        this.setCurrentAction(Action.WAIT_CAT);
    }

    @Override
    public boolean boolean_v() {
        return true;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        PlayerLuna.a(entityPlayer, this, new String[]{"action.names.touchboobs", "action.names.headpat"}, false);
        return true;
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.currentAction() == Action.COWGIRL_SITTING_CUM && (action == Action.COWGIRL_SITTING_SLOW || action == Action.COWGIRL_SITTING_FAST)) {
            return;
        }
        if (this.currentAction() == Action.TOUCH_BOOBS_CUM && (action == Action.TOUCH_BOOBS_FAST || action == Action.TOUCH_BOOBS_SLOW)) {
            return;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (Action.WAIT_CAT.equals((Object)this.currentAction())) {
            this.a_();
        } else {
            this.ar = 0;
        }
    }

    void a_() {
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_j();
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getDistance(this.posX, this.net_minecraft_util_math_Vec3d_w().y, this.posZ) > 1.25) {
            return;
        }
        if (this.world.isRemote) {
            this.a(entityPlayer, this.ar);
        } else if (this.ar == 25) {
            this.void_e(entityPlayer.getPersistentID());
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayer.setPositionAndUpdate(this.getPositionVector().x, this.net_minecraft_util_math_Vec3d_w().y, this.getPositionVector().z);
            this.setCurrentAction(Action.COWGIRL_SITTING_INTRO);
            entityPlayer.setRotationYawHead(this.java_lang_Float_I().floatValue() + 180.0f);
            entityPlayer.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
            entityPlayer.prevRotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
            this.r = this.java_lang_Float_I().floatValue() + 180.0f;
            this.moveCamera(0.0, -0.075f, -0.7109375, 0.0f, 0.0f);
            this.entityDataManager.set(D, 0);
        }
        ++this.ar;
    }

    @SideOnly(value=Side.CLIENT)
    void a(EntityPlayer entityPlayer, int n) {
        EntityPlayerSP entityPlayerSP;
        if (n == 0 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            fh_class313.b();
            entityPlayerSP.setVelocity(0.0, 0.0, 0.0);
            HandlePlayerMovement.a(false);
        }
        if (n == 25 && (entityPlayerSP = Minecraft.getMinecraft().player).getPersistentID().equals(entityPlayer.getPersistentID())) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        }
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW) {
            return Action.TOUCH_BOOBS_FAST;
        }
        if (action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_FAST;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.TOUCH_BOOBS_SLOW || action == Action.TOUCH_BOOBS_FAST) {
            return Action.TOUCH_BOOBS_CUM;
        }
        if (action == Action.COWGIRL_SITTING_FAST || action == Action.COWGIRL_SITTING_SLOW) {
            return Action.COWGIRL_SITTING_CUM;
        }
        return null;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.cat.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.cat.blink", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.cat.null", true, animationEvent);
                    break;
                }
                if (this.ak) {
                    this.createAnimation("animation.cat.sit", true, animationEvent);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.af) {
                    boolean bl = this.aq = !this.aq;
                }
                if (!this.af) {
                    this.createAnimation("animation.cat.fly" + (this.aq ? "2" : ""), true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.aj) {
                        this.movementController.setAnimationSpeed(1.5);
                        this.createAnimation("animation.cat.run", true, animationEvent);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation("animation.cat.fastwalk", true, animationEvent);
                        break;
                    }
                    this.movementController.setAnimationSpeed(2.0);
                    this.createAnimation("animation.cat.backwards_walk", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.cat.idle", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.cat.null", true, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.cat.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case RIDE: 
                    case SIT: {
                        this.createAnimation("animation.cat.sit", true, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.cat.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.cat.throwpearl", true, animationEvent);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.cat.downed", true, animationEvent);
                        break block5;
                    }
                    case FISHING_START: {
                        this.createAnimation("animation.cat.start_fishing", false, animationEvent);
                        break block5;
                    }
                    case FISHING_IDLE: {
                        this.createAnimation("animation.cat.idle_fishing", true, animationEvent);
                        break block5;
                    }
                    case FISHING_EAT: {
                        this.createAnimation("animation.cat.eat_fishing", false, animationEvent);
                        break block5;
                    }
                    case FISHING_THROW_AWAY: {
                        this.createAnimation("animation.cat.throw_away", false, animationEvent);
                        break block5;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.cat.payment", false, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_INTRO: {
                        this.createAnimation("animation.cat.touch_boobs_intro", false, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_SLOW: {
                        this.createAnimation("animation.cat.touch_boobs_slow" + (this.ap ? "1" : ""), true, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_FAST: {
                        this.createAnimation("animation.cat.touch_boobs_fast", true, animationEvent);
                        break block5;
                    }
                    case TOUCH_BOOBS_CUM: {
                        this.createAnimation("animation.cat.touch_boobs_cum", false, animationEvent);
                        break block5;
                    }
                    case WAIT_CAT: {
                        this.createAnimation("animation.cat.wait", false, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_INTRO: {
                        this.createAnimation("animation.cat.sitting_intro", false, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_SLOW: {
                        this.createAnimation("animation.cat.sitting_slow", true, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_FAST: {
                        this.createAnimation("animation.cat.sitting_fast", true, animationEvent);
                        break block5;
                    }
                    case COWGIRL_SITTING_CUM: {
                        this.createAnimation("animation.cat.sitting_cum", true, animationEvent);
                        break block5;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.cat.head_pat", true, animationEvent);
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
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "idleDone": {
                    this.as = this.getRNG().nextInt(10) == 0;
                    break;
                }
                case "idle2Done": {
                    this.as = false;
                    break;
                }
                case "pearl": {
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "paymentMSG1": {
                    this.a(this.getID(), "Here, I know u like fish and yea.. these are for you");
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "paymentMSG2": {
                    this.void_a("huh~?");
                    this.a(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "paymentMSG3": {
                    this.void_a("nyyyaaaa~ :D");
                    int[] nArray = new int[]{1, 7, 10, 11};
                    int n = nArray[this.getRNG().nextInt(nArray.length)];
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[n]);
                    break;
                }
                case "paymentMSG4": {
                    this.void_a("tankuuuu owowowo");
                    this.a(SoundsHandler.GIRLS_LUNA_OWO);
                    break;
                }
                case "paymentDone": {
                    if (this.boolean_e()) {
                        this.U();
                    }
                    this.n = 1.0f;
                    break;
                }
                case "breath": 
                case "rod_breath": {
                    this.a(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING);
                    break;
                }
                case "happyOh": {
                    this.a(SoundsHandler.GIRLS_LUNA_HAPPYOH);
                    break;
                }
                case "cutenya3": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[3]);
                    break;
                }
                case "cutenya2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_CUTENYA[2]);
                    break;
                }
                case "huh": {
                    this.a(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "hmph": {
                    this.a(SoundsHandler.GIRLS_LUNA_HMPH);
                    break;
                }
                case "hehe": 
                case "giggle": {
                    this.a(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    break;
                }
                case "singing": {
                    this.a(SoundsHandler.GIRLS_LUNA_SINGING);
                    break;
                }
                case "touch_boobsMSG1": {
                    this.void_a("comon~ touch me hihi~");
                    this.a(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    break;
                }
                case "touch": {
                    this.a(SoundsHandler.MISC_TOUCH);
                    break;
                }
                case "jump": {
                    this.a(SoundsHandler.MISC_JUMP[0], 0.2f);
                    break;
                }
                case "horninya": {
                    this.a(SoundsHandler.GIRLS_LUNA_HORNINYA);
                    break;
                }
                case "horninya2": 
                case "touch_boobs_cumMSG3": 
                case "sitting_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[1]);
                    this.a(SoundsHandler.MISC_CUMINFLATION[0], 5.0f);
                    break;
                }
                case "moan": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                    break;
                }
                case "touch_boobs_introDone": {
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    SexUI.d();
                    HandlePlayerMovement.a(false);
                    break;
                }
                case "touch_boobs_slowDone": {
                    if (this.ap) {
                        this.ap = false;
                        break;
                    }
                    this.ap = Math.random() < 0.5;
                    break;
                }
                case "addCumSlow": {
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "addCumFast": {
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.TOUCH_BOOBS_SLOW);
                    break;
                }
                case "moanOrNya": {
                    if (Math.random() > 0.5) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                        break;
                    }
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    break;
                }
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "touch_boobs_cumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                    break;
                }
                case "resetGirl": {
                    if (!this.boolean_n()) break;
                    this.void_r();
                    break;
                }
                case "touch_boobs_cumMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[3]);
                    break;
                }
                case "touch_boobs_cumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[9]);
                    break;
                }
                case "call_playerMSG1": {
                    this.a(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    this.void_a("come here - big guy hehe~");
                    break;
                }
                case "pounding": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    break;
                }
                case "sitting_introMSG1": {
                    this.a(SoundsHandler.GIRLS_LUNA_GIGGLE);
                    this.void_a("hehe~");
                    break;
                }
                case "sitting_introDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    SexUI.resetCumPercentage();
                    SexUI.d();
                    break;
                }
                case "sitting_slowMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        if (this.getRNG().nextBoolean()) {
                            this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_HORNINYA));
                            break;
                        }
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                    } else {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_LIGHTBREATHING));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "sitting_fastMSG1": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_HORNINYA));
                    } else {
                        this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_LUNA_MOAN));
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "sitting_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRL_SITTING_SLOW);
                    Vec3d vec3d = new Vec3d(0.0, -0.075f, -0.7109375);
                    Vec3d vec3d2 = VectorMath.rotate(vec3d, this.java_lang_Float_I().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.net_minecraft_util_math_Vec3d_o().x + vec3d2.x, this.net_minecraft_util_math_Vec3d_o().y - 0.0 + vec3d2.y, this.net_minecraft_util_math_Vec3d_o().z + vec3d2.z);
                    break;
                }
                case "sitting_fastTp": {
                    if (!this.boolean_n()) break;
                    Vec3d vec3d = new Vec3d(0.0, -0.160625, -0.9925);
                    Vec3d vec3d3 = VectorMath.rotate(vec3d, this.java_lang_Float_I().floatValue() + 180.0f);
                    Minecraft.getMinecraft().player.setPosition(this.net_minecraft_util_math_Vec3d_o().x + vec3d3.x, this.net_minecraft_util_math_Vec3d_o().y - 0.0 + vec3d3.y, this.net_minecraft_util_math_Vec3d_o().z + vec3d3.z);
                    break;
                }
                case "headpatMSG1": {
                    this.void_a("huh?~");
                    this.a(SoundsHandler.GIRLS_LUNA_HUH);
                    break;
                }
                case "headpatMSG2": {
                    this.a(SoundsHandler.GIRLS_LUNA_MMM);
                    break;
                }
                case "headpatMSG3": {
                    this.void_a("nya~");
                    this.PlaySound(SoundsHandler.GIRLS_LUNA_HORNINYA[0]);
                }
            }
        };
        this.movementController.transitionLengthTicks = 10.0;
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

