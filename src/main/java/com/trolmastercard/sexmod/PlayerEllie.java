/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import com.google.common.base.Optional;

import java.util.UUID;

import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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

public class PlayerEllie
extends PlayerGirl {
    boolean ar = false;
    boolean aq = false;
    int ap = 1;

    protected PlayerEllie(World world) {
        super(world);
    }

    public PlayerEllie(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 2.05f;
    }

    @Override
    public float getEyeHeight() {
        return this.a_14() ? 1.53f : 1.9f;
    }

    @Override
    public void u_() {
        this.setCurrentAction(Action.SITDOWN);
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("Face fuck".equals(string)) {
            this.void_b(uUID);
            this.setCurrentAction(Action.CARRY_INTRO);
            this.a(this.int_ah(), Action.CARRY_INTRO);
        }
    }

    @Override
    public at_class43 com_trolmastercard_sexmod_at_class43_a(int n) {
        return new cf_class130();
    }

    @Override
    public String HandTexture(int n) {
        if (n == 0) {
            return "textures/entity/ellie/hand_nude.png";
        }
        return "textures/entity/ellie/hand.png";
    }

    @Override
    public boolean boolean_p() {
        return true;
    }

    @Override
    public void a(String string, UUID uUID) {
        if ("action.names.cowgirl".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "Cowgirl");
            return;
        }
        if ("action.names.missionary".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", "Missionary");
            return;
        }
        if (!((Optional)this.entityDataManager.get(ai)).isPresent()) {
            return;
        }
        NetworkRegistry.networkWrapper.sendToServer((IMessage)new g4_class347(string, uUID, (UUID)((Optional)this.entityDataManager.get(ai)).get(), this.ab));
        this.ab = true;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        PlayerEllie.a(entityPlayer, this, new String[]{"Face fuck"}, false);
        return true;
    }

    void void_c(EntityPlayer entityPlayer) {
        PlayerEllie.a(entityPlayer, this, new String[]{"action.names.cowgirl", "action.names.missionary"}, false);
    }

    @Override
    public boolean boolean_A() {
        return false;
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.MISSIONARY_CUM && (fp_class3242 == Action.MISSIONARY_FAST || fp_class3242 == Action.MISSIONARY_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.COWGIRLCUM && (fp_class3242 == Action.COWGIRLSLOW || fp_class3242 == Action.COWGIRLFAST)) {
            return;
        }
        super.setCurrentAction(fp_class3242);
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.COWGIRLSLOW) {
            return Action.COWGIRLFAST;
        }
        if (action == Action.MISSIONARY_SLOW) {
            return Action.MISSIONARY_FAST;
        }
        if (action == Action.CARRY_SLOW) {
            return Action.CARRY_FAST;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.COWGIRLFAST || action == Action.COWGIRLSLOW) {
            return Action.COWGIRLCUM;
        }
        if (action == Action.MISSIONARY_FAST || action == Action.MISSIONARY_SLOW) {
            return Action.MISSIONARY_CUM;
        }
        if (action == Action.CARRY_SLOW || action == Action.CARRY_FAST) {
            return Action.CARRY_CUM;
        }
        return null;
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.currentAction() == Action.SITDOWNIDLE) {
            String string = this.entityDataManager.get(GirlEntity.h);
            if (!"Missionary".equals(string) && !"Cowgirl".equals(string)) {
                return;
            }
            EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_j();
            if (entityPlayer == null || entityPlayer.getDistance(this.net_minecraft_util_math_Vec3d_w().x, this.net_minecraft_util_math_Vec3d_w().y, this.net_minecraft_util_math_Vec3d_w().z) > 1.0) {
                return;
            }
            this.entityDataManager.set(GirlEntity.h, "");
            this.entityDataManager.set(GirlEntity.D, 0);
            this.void_e(entityPlayer.getPersistentID());
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)this.world.getPlayerEntityByUUID((UUID)((Optional)this.entityDataManager.get(ai)).get());
            NetworkRegistry.networkWrapper.sendTo((IMessage)new gz_class393(false), (EntityPlayerMP)entityPlayer);
            NetworkRegistry.networkWrapper.sendTo((IMessage)new gz_class393(false), entityPlayerMP);
            entityPlayer.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            entityPlayerMP.capabilities.isFlying = true;
            entityPlayer.capabilities.isFlying = true;
            entityPlayerMP.noClip = true;
            entityPlayer.noClip = true;
            entityPlayerMP.setNoGravity(true);
            entityPlayer.setNoGravity(true);
            if ("Missionary".equals(string)) {
                this.setCurrentAction(Action.MISSIONARY_START);
                Vec3d vec3d = this.net_minecraft_util_math_Vec3d_w().subtract(0.0, 0.1, 0.0);
                entityPlayer.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, this.java_lang_Float_I().floatValue(), 60.0f);
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            } else {
                this.setCurrentAction(Action.COWGIRLSTART);
                Vec3d vec3d = this.net_minecraft_util_math_Vec3d_w().add(new Vec3d(-Math.sin((double)this.java_lang_Float_I().floatValue() * (Math.PI / 180)) * 1.8, -0.65, Math.cos((double)this.java_lang_Float_I().floatValue() * (Math.PI / 180)) * 1.8));
                entityPlayer.setPositionAndRotation(vec3d.x, vec3d.y, vec3d.z, 180.0f + this.java_lang_Float_I().floatValue(), -30.0f);
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            }
        }
    }

    boolean a_14() {
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return false;
        }
        return this.world.getBlockState(entityPlayer.getPosition().up().up()).getBlock() != Blocks.AIR;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.ellie.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.ellie.eyes", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.ellie.null", true, animationEvent);
                    break;
                }
                if (this.ak) {
                    this.createAnimation("animation.ellie.ride", true, animationEvent);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.af) {
                    boolean bl = this.ar = !this.ar;
                }
                if (!this.af) {
                    this.createAnimation("animation.ellie.fly" + (this.ar ? "2" : ""), true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.aj) {
                        this.movementController.setAnimationSpeed(1.5);
                        this.createAnimation(this.a_14() ? "animation.ellie.crouchwalk" : "animation.ellie.run", true, animationEvent);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation(this.a_14() ? "animation.ellie.crouchwalk" : "animation.ellie.fastwalk", true, animationEvent);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation(this.a_14() ? "animation.ellie.crouchwalk" : "animation.ellie.backwards_walk", true, animationEvent);
                    break;
                }
                this.createAnimation(this.a_14() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.ellie.null", true, animationEvent);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.ellie.strip", false, animationEvent);
                        break block5;
                    }
                    case DASH: {
                        this.createAnimation("animation.ellie.dash", false, animationEvent);
                        break block5;
                    }
                    case HUG: {
                        this.createAnimation("animation.ellie.hug", false, animationEvent);
                        break block5;
                    }
                    case HUGIDLE: {
                        this.createAnimation("animation.ellie.hugidle", true, animationEvent);
                        break block5;
                    }
                    case HUGSELECTED: {
                        this.createAnimation("animation.ellie.hugselected", false, animationEvent);
                        break block5;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.ellie.sitdown", false, animationEvent);
                        break block5;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.ellie.sitdownidle", true, animationEvent);
                        break block5;
                    }
                    case COWGIRLSTART: {
                        this.createAnimation("animation.ellie.cowgirlstart", false, animationEvent);
                        break block5;
                    }
                    case COWGIRLSLOW: {
                        this.createAnimation("animation.ellie.cowgirlslow2", true, animationEvent);
                        break block5;
                    }
                    case COWGIRLFAST: {
                        this.createAnimation("animation.ellie.cowgirlfast", true, animationEvent);
                        break block5;
                    }
                    case COWGIRLCUM: {
                        this.createAnimation("animation.ellie.cowgirlcum", true, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.ellie.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.ellie.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.ellie.ride", true, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.ellie.sit", true, animationEvent);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.ellie.throwpearl", false, animationEvent);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.ellie.downed", true, animationEvent);
                        break block5;
                    }
                    case MISSIONARY_START: {
                        this.createAnimation("animation.ellie.missionary_start", false, animationEvent);
                        break block5;
                    }
                    case MISSIONARY_SLOW: {
                        this.createAnimation("animation.ellie.missionary_slow", true, animationEvent);
                        break block5;
                    }
                    case MISSIONARY_FAST: {
                        this.createAnimation("animation.ellie.missionary_fast", true, animationEvent);
                        break block5;
                    }
                    case MISSIONARY_CUM: {
                        this.createAnimation("animation.ellie.missionary_cum", false, animationEvent);
                        break block5;
                    }
                    case CARRY_INTRO: {
                        this.createAnimation("animation.ellie.carry_intro", false, animationEvent);
                        break block5;
                    }
                    case CARRY_SLOW: {
                        this.createAnimation("animation.ellie.carry_slow" + this.ap, true, animationEvent);
                        break block5;
                    }
                    case CARRY_FAST: {
                        this.createAnimation("animation.ellie.carry_fast", true, animationEvent);
                        break block5;
                    }
                    case CARRY_CUM: {
                        this.createAnimation("animation.ellie.carry_cum", true, animationEvent);
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
            this.void_p();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "dashMSG1": {
                    float f;
                    EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
                    if (entityPlayer == null) break;
                    Vec3d vec3d = this.getPositionVector().subtract(entityPlayer.getPositionVector());
                    this.rotationYaw = f = (float)Math.atan2(vec3d.z, vec3d.x) * 57.29578f;
                    this.rotationYawHead = f;
                    this.renderYawOffset = f;
                    break;
                }
                case "dashReady": {
                    if (!this.boolean_e()) break;
                    break;
                }
                case "dashDone": {
                    float f;
                    this.setCurrentAction(Action.HUG);
                    EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
                    if (entityPlayer == null) break;
                    this.rotationYaw = f = entityPlayer.rotationYaw;
                    this.rotationYawHead = f;
                    this.renderYawOffset = f;
                    break;
                }
                case "hugMSG1": {
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    if (!entityPlayerSP.getPersistentID().equals(this.getID()) && !entityPlayerSP.getUniqueID().equals(this.getID())) break;
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new a8_class16(entityPlayerSP.getUniqueID().toString(), entityPlayerSP.getPositionVector(), entityPlayerSP.rotationYaw - 80.0f, entityPlayerSP.rotationPitch));
                    break;
                }
                case "hugMSG2": {
                    this.h("Hmm...");
                    this.a(SoundsHandler.GIRLS_ELLIE_HMPH[3], 3.0f);
                    break;
                }
                case "hugMSG3": {
                    this.h("Hey!");
                    this.a(SoundsHandler.GIRLS_ELLIE_AHH[2], 3.0f);
                    break;
                }
                case "hugMSG4": {
                    this.h(I18n.format("ellie.dialogue.mommyhorny", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[0], 3.0f);
                    break;
                }
                case "hugMSG5": {
                    this.h(I18n.format("ellie.dialogue.whattodo", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_HUH[1], 3.0f);
                    break;
                }
                case "hugDone": {
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    if (!entityPlayerSP.getPersistentID().equals(this.getID())) break;
                    this.setCurrentAction(Action.HUGIDLE);
                    this.void_c(entityPlayerSP);
                    break;
                }
                case "hugselectedMSG1": {
                    this.h(I18n.format("ellie.dialogue.iknow", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_MMM[0], 3.0f);
                    break;
                }
                case "hugselectedMSG2": {
                    this.h(I18n.format("ellie.dialogue.followmedarling", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 3.0f);
                    break;
                }
                case "hugselectedDone": {
                    if (!this.boolean_e()) break;
                    Vec3d vec3d = this.getPositionVector();
                    vec3d = vec3d.add(-Math.sin((double)(this.rotationYaw + 90.0f) * (Math.PI / 180)) * -0.7803124785423279, 0.0, Math.cos((double)(this.rotationYaw + 90.0f) * (Math.PI / 180)) * -0.7803124785423279);
                    vec3d = vec3d.add(-Math.sin((double)this.rotationYaw * (Math.PI / 180)) * 0.5296875238418579, 0.0, Math.cos((double)this.rotationYaw * (Math.PI / 180)) * 0.5296875238418579);
                    String string = vec3d.x + "f" + vec3d.y + "f" + vec3d.z + "f";
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new n_class415(this.girlID(), "targetPos", string));
                    this.void_r();
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new PacketSendGirlToBed(this.girlID()));
                    this.setCurrentAction(Action.NULL);
                    break;
                }
                case "sitdownMSG1": {
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 3.0f);
                    if (!this.boolean_e()) break;
                    this.h(I18n.format("ellie.dialogue.cometomommy", new Object[0]));
                    break;
                }
                case "sitdownDone": {
                    if (!this.boolean_f()) break;
                    this.setCurrentAction(Action.SITDOWNIDLE);
                    this.void_c(this.world.getPlayerEntityByUUID(this.java_util_UUID_m()));
                    break;
                }
                case "missionary_startDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    SexUI.d();
                    break;
                }
                case "cowgirlStartMSG0": {
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 3.0f);
                    break;
                }
                case "cowgirlStartMSG1": {
                    if (!this.boolean_e()) break;
                    this.void_a(I18n.format("ellie.dialogue.like", new Object[0]));
                    SexUI.resetCumPercentage();
                    break;
                }
                case "cowgirlStartMSG2": {
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cowgirlStartDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.COWGIRLSLOW);
                    SexUI.d();
                    break;
                }
                case "cowgirlfastMSG1": {
                    if (this.aq) {
                        this.aq = false;
                    } else {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    }
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "cowgirlfastReady": {
                    if (!this.boolean_n()) break;
                    if (!HandlePlayerMovement.isThrusting) {
                        this.setCurrentAction(Action.COWGIRLSLOW);
                        break;
                    }
                    if (Reference.RANDOM.nextInt(4) == 1) break;
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "cowgirlfastdomMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.2);
                    break;
                }
                case "cowgirlcumMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG2": {
                    this.a(SoundsHandler.GIRLS_ELLIE_MOAN[5], 3.0f);
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG3": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG4": {
                    if (!this.boolean_n()) break;
                    SexUI.c();
                    break;
                }
                case "cowgirlcumMSG5": 
                case "missionary_cumMSG2": {
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 3.0f);
                    if (!this.boolean_n()) break;
                    this.void_a(I18n.format("ellie.dialogue.goodboy", new Object[0]));
                    break;
                }
                case "cowgirlcumMSG6": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "missionary_cumDone": 
                case "cowgirlcumDone": 
                case "carry_cumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                    break;
                }
                case "attackDone": {
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "pearl": {
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new gg_class366(this.girlID()));
                    break;
                }
                case "openSexUi": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "missionary_slowMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() && this.getRNG().nextBoolean()) {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_MOAN), 3.0f);
                    } else {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "missionary_fastMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() || this.getRNG().nextBoolean()) {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_MOAN), 3.0f);
                    } else {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.05);
                    break;
                }
                case "missionary_fastDone": {
                    if (!this.boolean_n()) break;
                    if (HandlePlayerMovement.isThrusting) {
                        this.setCurrentAction(Action.MISSIONARY_FAST);
                        break;
                    }
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    break;
                }
                case "bedRustle": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    break;
                }
                case "bedRustle1": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[1]);
                    break;
                }
                case "missionary_cumMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 3.0f);
                    break;
                }
                case "carry_introMSG1": {
                    this.void_a("I'm hungry..");
                    this.a(SoundsHandler.GIRLS_ELLIE_HMPH, 6.0f);
                    break;
                }
                case "carry_introMSG2": {
                    this.void_a("heh~");
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    break;
                }
                case "lipsound": {
                    this.a(SoundsHandler.GIRLS_ALLIE_LIPSOUND);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.a(SoundsHandler.MISC_POUNDING);
                    break;
                }
                case "pound": {
                    this.a(SoundsHandler.MISC_POUNDING);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "carry_slowDone": {
                    int n = this.ap;
                    do {
                        this.ap = this.getRNG().nextInt(4) + 1;
                    } while (this.ap == n);
                    break;
                }
                case "carry_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CARRY_SLOW);
                    break;
                }
                case "sexUI": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

