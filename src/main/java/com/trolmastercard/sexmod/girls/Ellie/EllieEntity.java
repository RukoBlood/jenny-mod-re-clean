/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Ellie;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gui.SexUI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class EllieEntity
extends Fighter
implements bh_class82 {
    final static float ad = 10.0f;
    final static int ao = 16;
    final static int ap = 79;
    final static int ag = 109;
    final static int as = 150;
    final static int ar = 20;
    final static int ab = 110;
    final static int an = 4;
    int ak = -1;
    boolean aq = false;
    boolean ae = false;
    boolean ac = false;
    int af = -1;
    int Y = -1;
    int al = -1;
    int ai = -1;
    boolean ah = false;
    Object[] am;
    int Z = -1;
    int aa = 1;
    boolean aj = false;

    public EllieEntity(World world) {
        super(world);
        this.P = -85;
        this.O = -175;
        this.K = -85;
        this.V = new Vec3d(-0.1, 0.05, 0.0);
    }

    @Override
    public void SetHome() {
        this.void_a("Okay, I will be residing here then..");
        this.a(SoundsHandler.GIRLS_ELLIE_HUH[0], 6.0f);
    }

    @Override
    public String getGirlName() {
        return "Ellie";
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableRegistry.ELLIE_LOOT_TABLE;
    }

    boolean boolean_i() {
        if (this.boolean_h()) {
            return false;
        }
        return this.world.getBlockState(this.getPosition().add(0, 2, 0)).getBlock() != Blocks.AIR;
    }

    @Override
    public float getEyeHeight() {
        return this.boolean_i() ? 1.53f : 1.9f;
    }

    @Override
    public float float_i() {
        return 0.4f;
    }

    @Override
    public void void_b() {
        UUID uUID = this.getID();
        if (uUID == null) {
            this.void_f();
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            this.void_f();
            return;
        }
        float f = entityPlayer.rotationYaw - 180.0f;
        this.void_b(f);
        this.setCurrentAction(Action.CARRY_INTRO);
        this.void_a(true);
    }

    @Override
    public boolean boolean_t() {
        return this.currentAction() != Action.CARRY_INTRO;
    }

    public boolean a(EntityPlayer entityPlayer, boolean bl) {
        if (bl) {
            EllieEntity.a(entityPlayer, this, new String[]{"action.names.cowgirl", "action.names.missionary"}, false);
            return true;
        }
        if ((Integer)this.entityDataManager.get(D) == 0) {
            EllieEntity.a(entityPlayer, this, new String[]{"action.names.dressup"}, true);
            return true;
        }
        EllieEntity.a(entityPlayer, this, new String[]{"Face fuck"}, true);
        return true;
    }

    @Override
    public void goHome() {
        super.goHome();
        this.void_a("stay safe darling~");
        this.a(SoundsHandler.GIRLS_ELLIE_SIGH[1], 6.0f);
    }

    @Override
    public void a(String string, UUID uUID) {
        super.a(string, uUID);
        this.aq = true;
        switch (string) {
            case "action.names.missionary": {
                this.setCurrentAction(Action.HUGSELECTED);
                this.changeDataParameterFromClient("animationFollowUp", "Missionary");
                break;
            }
            case "action.names.cowgirl": {
                this.setCurrentAction(Action.HUGSELECTED);
                this.changeDataParameterFromClient("animationFollowUp", "cowgirl");
                break;
            }
            case "action.names.dressup": 
            case "action.names.strip": {
                this.setCurrentAction(Action.STRIP);
                this.changeDataParameterFromClient("animationFollowUp", "");
                break;
            }
            case "Face fuck": {
                this.a(true, true, uUID);
                HandlePlayerMovement.a(false);
            }
        }
    }

    @Override
    protected void a(EntityPlayerMP entityPlayerMP, boolean bl) {
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3242 == Action.HUGSELECTED && !this.world.isRemote) {
            this.ai = 79;
        }
        if (fp_class3243 == Action.MISSIONARY_CUM && (fp_class3242 == Action.MISSIONARY_FAST || fp_class3242 == Action.MISSIONARY_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.COWGIRLCUM && (fp_class3242 == Action.COWGIRLSLOW || fp_class3242 == Action.COWGIRLFAST)) {
            return;
        }
        if (fp_class3243 == Action.CARRY_CUM && (fp_class3242 == Action.CARRY_SLOW || fp_class3242 == Action.CARRY_FAST)) {
            return;
        }
        if (fp_class3242 == Action.CARRY_INTRO) {
            this.ak = 0;
        }
        super.setCurrentAction(fp_class3242);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void onUpdate() {
        super.onUpdate();
        if (this.ae) {
            this.a(Minecraft.getMinecraft().player, true);
            this.ae = false;
        }
        this.void_m();
        this.void_h();
    }

    void void_h() {
        if (SexUI.getShouldBeRendered()) {
            return;
        }
        if (this.currentAction() != Action.CARRY_SLOW) {
            return;
        }
        SexUI.d();
    }

    void void_e() {
        if (this.ak == -1) {
            return;
        }
        if (++this.ak < 110) {
            return;
        }
        this.ak = -1;
        if (this.currentAction() != Action.CARRY_INTRO) {
            return;
        }
        UUID uUID = this.getID();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        float f = this.java_lang_Float_I().floatValue();
        Vec3d vec3d = this.net_minecraft_util_math_Vec3d_o().add(ck_class135.a(new Vec3d(0.0, 2.5625f - entityPlayer.getEyeHeight(), -0.3125), 180.0f + f));
        entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
    }

    void void_m() {
        if (this.currentAction() != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        if (this.getDistance(entityPlayer) > 1.5f) {
            return;
        }
        if (entityPlayer.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            fh_class313.b();
        }
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.void_o();
        this.void_d();
        this.void_n();
        this.void_q();
        this.void_j();
        this.a_10();
        this.void_t();
        this.u_();
    }

    void void_o() {
        if (this.ac) {
            return;
        }
        this.ac = true;
        this.noClip = false;
        this.setNoGravity(false);
    }

    @Override
    protected void U() {
        Vec3d vec3d;
        Vec3d vec3d2;
        EntityPlayer entityPlayer;
        UUID uUID;
        String string = (String)this.entityDataManager.get(h);
        if ("Missionary".equals(string)) {
            this.entityDataManager.set(D, 0);
            this.setCurrentAction(Action.MISSIONARY_START);
            uUID = this.getID();
            if (uUID == null) {
                return;
            }
            entityPlayer = this.world.getPlayerEntityByUUID(uUID);
            if (entityPlayer == null) {
                this.void_r();
                return;
            }
            entityPlayer.setNoGravity(true);
            entityPlayer.noClip = true;
            vec3d2 = this.net_minecraft_util_math_Vec3d_o();
            entityPlayer.rotationYaw = this.java_lang_Float_I().floatValue();
            vec3d = ck_class135.a(new Vec3d(0.0, 0.0, 0.1), entityPlayer.rotationYaw);
            vec3d2 = vec3d2.add(vec3d);
            entityPlayer.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
            NetworkRegistry.networkWrapper.sendTo((IMessage)new gz_class393(false), (EntityPlayerMP)entityPlayer);
        }
        if ("cowgirl".equals(string)) {
            this.entityDataManager.set(D, 0);
            this.setCurrentAction(Action.COWGIRLSTART);
            uUID = this.getID();
            if (uUID == null) {
                return;
            }
            entityPlayer = this.world.getPlayerEntityByUUID(uUID);
            if (entityPlayer == null) {
                this.void_r();
                return;
            }
            entityPlayer.setNoGravity(true);
            entityPlayer.noClip = true;
            vec3d2 = this.net_minecraft_util_math_Vec3d_o();
            entityPlayer.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
            vec3d = ck_class135.a(new Vec3d(0.0, 1.0 - (double)entityPlayer.eyeHeight, -1.8125), entityPlayer.rotationYaw);
            vec3d2 = vec3d2.add(vec3d);
            entityPlayer.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
            NetworkRegistry.networkWrapper.sendTo((IMessage)new gz_class393(false), (EntityPlayerMP)entityPlayer);
        }
    }

    void u_() {
        if (--this.af != 0) {
            return;
        }
        this.U();
    }

    void void_t() {
        if (this.currentAction() != Action.SITDOWNIDLE || this.af >= 0) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        if (this.getDistance(entityPlayer) > 1.5f) {
            return;
        }
        this.af = 20;
        this.void_e(entityPlayer.getPersistentID());
    }

    void a_10() {
        if (--this.Y != 0) {
            return;
        }
        this.setCurrentAction(Action.HUGIDLE);
    }

    void void_j() {
        if (--this.al != 0) {
            return;
        }
        this.setCurrentAction(Action.SITDOWNIDLE);
    }

    void void_q() {
        if (--this.ai != 0 && !this.ah) {
            return;
        }
        this.ah = true;
        this.entityDataManager.set(G, false);
        this.setCurrentAction(Action.NULL);
        this.noClip = false;
        this.setNoGravity(false);
        if (this.am == null) {
            this.am = this.java_lang_Object_arr_g();
        }
        if (this.am == null) {
            this.h("no bed in sight...");
            this.world.playSound(null, this.getPosition(), SoundsHandler.GIRLS_ELLIE_SIGH[0], SoundCategory.NEUTRAL, 6.0f, 1.0f);
            this.s();
            this.void_f();
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer != null) {
            entityPlayer.setNoGravity(false);
            entityPlayer.noClip = false;
        }
        Vec3d vec3d = (Vec3d)this.am[0];
        int n = (Integer)this.am[1];
        if (vec3d.distanceTo(this.getPositionVector()) > 1.0) {
            this.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.35f);
            this.void_k();
            return;
        }
        this.c(vec3d);
        this.void_b(n);
        this.setCurrentAction(Action.SITDOWN);
        this.entityDataManager.set(G, true);
        this.al = 109;
        this.noClip = true;
        this.setNoGravity(true);
        this.ah = false;
        this.am = null;
    }

    @Override
    public void void_g() {
        super.void_g();
        this.Y = -1;
    }

    Object[] java_lang_Object_arr_g() {
        Vec3d vec3d;
        Object object;
        int n = -1;
        int n2 = 0;
        Vec3d[][] vec3dArrayArray = new Vec3d[][]{{new Vec3d(0.5, 0.0, -0.18), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0)}, {new Vec3d(0.5, 0.0, 1.18), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0)}, {new Vec3d(-0.18, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0)}, {new Vec3d(1.18, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0)}};
        int[] nArray = new int[]{0, 180, -90, 90};
        do {
            if ((object = this.a(this.getPosition(), ++n2)) == null) {
                return null;
            }
            vec3d = new Vec3d(((Vec3i)object).getX(), ((Vec3i)object).getY(), ((Vec3i)object).getZ());
            for (int i = 0; i < vec3dArrayArray.length; ++i) {
                Vec3d vec3d2 = vec3d.add(vec3dArrayArray[i][1]);
                Block block = this.world.getBlockState(new BlockPos(vec3d2.x, vec3d2.y, vec3d2.z)).getBlock();
                Vec3d vec3d3 = vec3d.add(vec3dArrayArray[i][2]);
                Block block2 = this.world.getBlockState(new BlockPos(vec3d3.x, vec3d3.y, vec3d3.z)).getBlock();
                if (block != Blocks.AIR || block2 != Blocks.BED) continue;
                if (n == -1) {
                    n = i;
                    continue;
                }
                double d = this.getPosition().distanceSq(vec3d.add((Vec3d)vec3dArrayArray[n][0]).x, vec3d.add((Vec3d)vec3dArrayArray[n][0]).y, vec3d.add((Vec3d)vec3dArrayArray[n][0]).z);
                double d2 = this.getPosition().distanceSq(vec3d.add((Vec3d)vec3dArrayArray[i][0]).x, vec3d.add((Vec3d)vec3dArrayArray[i][0]).y, vec3d.add((Vec3d)vec3dArrayArray[i][0]).z);
                if (!(d2 < d)) continue;
                n = i;
            }
        } while (n == -1);
        object = vec3d.add(vec3dArrayArray[n][0]);
        return new Object[]{object, nArray[n]};
    }

    void void_d() {
        if (this.getActivePotionEffect(HornyPotion.HORNY_POTION) == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        this.removeActivePotionEffect(HornyPotion.HORNY_POTION);
        this.void_e(entityPlayer.getPersistentID());
        float f = (float)(Math.atan2(this.posZ - entityPlayer.posZ, this.posX - entityPlayer.posX) * 57.29577951308232);
        this.void_b(f);
        this.c(this.getPositionVector());
        this.entityDataManager.set(G, true);
        this.setCurrentAction(Action.DASH);
        this.Z = 16;
        this.setNoGravity(true);
        this.noClip = true;
        NetworkRegistry.networkWrapper.sendTo((IMessage)new gz_class393(false), (EntityPlayerMP)entityPlayer);
        this.tasks.removeTask(this.avoidWater);
        this.tasks.removeTask(this.o);
    }

    void void_n() {
        if (--this.Z != 0) {
            return;
        }
        UUID uUID = this.getID();
        if (uUID == null) {
            this.void_f();
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            this.void_f();
            return;
        }
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        Vec3d vec3d = ck_class135.a(new Vec3d(0.0, 0.0, -0.5), entityPlayer.rotationYaw);
        Vec3d vec3d2 = vec3d.add(entityPlayer.getPositionVector());
        this.c(vec3d2);
        this.void_b(entityPlayer.rotationYaw);
        this.setCurrentAction(Action.HUG);
        this.Y = 150;
    }

    void void_f() {
        this.entityDataManager.set(G, false);
        this.setCurrentAction(Action.NULL);
        this.void_e((UUID)null);
        this.noClip = false;
        this.setNoGravity(false);
        this.ah = false;
        this.Y = -1;
        this.Z = -1;
        this.ai = -1;
        this.am = null;
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (EllieEntity.com_trolmastercard_sexmod_em_class258_d(entityPlayer) != null) {
            return false;
        }
        if (this.getID() != null) {
            return false;
        }
        if (this.world.isRemote) {
            this.a(entityPlayer, false);
        }
        return true;
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
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
                double d = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                if (d == 0.0) {
                    this.createAnimation(this.boolean_i() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, animationEvent);
                    break;
                }
                if (this.boolean_i()) {
                    this.createAnimation("animation.ellie.crouchwalk", true, animationEvent);
                    break;
                }
                switch (this.com_trolmastercard_sexmod_em_class258$a_inner259_q()) {
                    case RUN: {
                        this.createAnimation("animation.ellie.run", true, animationEvent);
                        break;
                    }
                    case FAST_WALK: {
                        this.createAnimation("animation.ellie.fastwalk", true, animationEvent);
                        break;
                    }
                    case WALK: {
                        this.createAnimation("animation.ellie.walk", true, animationEvent);
                    }
                }
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
                        this.createAnimation("animation.ellie.carry_slow" + this.aa, true, animationEvent);
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
                case "becomeNude": {
                    if (!this.boolean_e()) break;
                    this.changeDataParameterFromClient("currentModel", (Integer)this.entityDataManager.get(D) == 1 ? "0" : "1");
                    break;
                }
                case "stripDone": {
                    this.setCurrentAction((Action)null);
                    this.void_r();
                    this.U();
                    break;
                }
                case "hugMSG2": {
                    this.h("Hmm...");
                    this.a(SoundsHandler.GIRLS_ELLIE_HMPH[3], 6.0f);
                    break;
                }
                case "hugMSG3": {
                    this.h("Hey!");
                    this.a(SoundsHandler.GIRLS_ELLIE_HUH[1], 1.0f);
                    break;
                }
                case "hugMSG4": {
                    this.h(I18n.format("ellie.dialogue.mommyhorny", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_MOMMYHORNY, 0.5f);
                    break;
                }
                case "hugMSG5": {
                    this.h(I18n.format("ellie.dialogue.whattodo", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_HUH[1], 6.0f);
                    break;
                }
                case "hugDone": {
                    if (!this.boolean_n()) break;
                    this.a(Minecraft.getMinecraft().player, true);
                    break;
                }
                case "hugselectedMSG1": {
                    this.h(I18n.format("ellie.dialogue.iknow", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_HMPH[3], 6.0f);
                    break;
                }
                case "hugselectedMSG2": {
                    this.h(I18n.format("ellie.dialogue.followmedarling", new Object[0]));
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    if (!this.boolean_n()) break;
                    HandlePlayerMovement.a(true);
                    break;
                }
                case "sitdownMSG1": {
                    this.a(SoundsHandler.GIRLS_ELLIE_COMETOMOMMY, 0.5f);
                    if (!this.boolean_e()) break;
                    this.h(I18n.format("ellie.dialogue.cometomommy", new Object[0]));
                    break;
                }
                case "cowgirlStartMSG0": {
                    this.a(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 6.0f);
                    break;
                }
                case "cowgirlStartMSG1": {
                    if (!this.boolean_e()) break;
                    this.void_a(I18n.format("ellie.dialogue.like", new Object[0]));
                    SexUI.resetCumPercentage();
                    break;
                }
                case "cowgirlStartMSG2": {
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
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
                    if (this.aj) {
                        this.aj = false;
                    } else {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "cowgirlfastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRLSLOW);
                    break;
                }
                case "cowgirlfastdomMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.2);
                    break;
                }
                case "cowgirlcumMSG1": {
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
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
                    this.a(SoundsHandler.GIRLS_ELLIE_GOODBOY, 0.5f);
                    if (!this.boolean_n()) break;
                    this.void_a(I18n.format("ellie.dialogue.goodboy", new Object[0]));
                    break;
                }
                case "cowgirlcumMSG6": 
                case "blackScreen": {
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
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "pearl": {
                    NetworkRegistry.networkWrapper.sendToServer((IMessage)new gg_class366(this.girlID()));
                    break;
                }
                case "openSexUi": {
                    if (!this.boolean_e()) break;
                    SexUI.d();
                    break;
                }
                case "missionary_slowMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() && this.getRNG().nextBoolean()) {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_MOAN), 6.0f);
                    } else {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "missionary_fastMSG1": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() || this.getRNG().nextBoolean()) {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_MOAN), 6.0f);
                    } else {
                        this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.05);
                    break;
                }
                case "missionary_startDone": {
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    SexUI.d();
                    break;
                }
                case "missionary_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
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
                    this.a(SoundsHandler.a(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
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
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIPSOUND, new int[0]);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "carry_slowDone": {
                    int n = this.aa;
                    do {
                        this.aa = this.getRNG().nextInt(4) + 1;
                    } while (this.aa == n);
                    break;
                }
                case "carry_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CARRY_SLOW);
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

