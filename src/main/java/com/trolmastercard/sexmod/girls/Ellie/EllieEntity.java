/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Ellie;

import java.util.UUID;

import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.bh_class82;
import com.trolmastercard.sexmod.world.FakeWorld;
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

public class EllieEntity extends Fighter
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
        this.slashSwordRot = -85;
        this.stabSwordRot = -175;
        this.holdBowRot = -85;
        this.swordOffsetStab = new Vec3d(-0.1, 0.05, 0.0);
    }

    @Override
    public void SetHome() {
        this.sendLocalClientMessage("Okay, I will be residing here then..");
        this.PlaySound(SoundsHandler.GIRLS_ELLIE_HUH[0], 6.0f);
    }

    @Override
    public String getGirlName() {
        return "Ellie";
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.ELLIE_LOOT_TABLE;
    }

    boolean boolean_i() {
        if (this.isLocallyRegistered()) {
            return false;
        }
        return this.world.getBlockState(this.getPosition().add(0, 2, 0)).getBlock() != Blocks.AIR;
    }

    @Override
    public float getEyeHeight() {
        return this.boolean_i() ? 1.53f : 1.9f;
    }

    @Override
    public float getNameTagHeightOffset() {
        return 0.4f;
    }

    @Override
    public void void_b() {
        UUID uUID = this.playerSheHasSexWith();
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
        this.setYawRotation(f);
        this.setCurrentAction(Action.CARRY_INTRO);
        this.setAnchored(true);
    }

    @Override
    public boolean shouldRenderNameTag() {
        return this.currentAction() != Action.CARRY_INTRO;
    }

    public boolean a(EntityPlayer entityPlayer, boolean bl) {
        if (bl) {
            EllieEntity.openInventoryGui(entityPlayer, this, new String[]{"action.names.cowgirl", "action.names.missionary"}, false);
            return true;
        }
        if ((Integer)this.entityDataManager.get(OUTFIT_INDEX) == 0) {
            EllieEntity.openInventoryGui(entityPlayer, this, new String[]{"action.names.dressup"}, true);
            return true;
        }
        EllieEntity.openInventoryGui(entityPlayer, this, new String[]{"Face fuck"}, true);
        return true;
    }

    @Override
    public void goHome() {
        super.goHome();
        this.sendLocalClientMessage("stay safe darling~");
        this.PlaySound(SoundsHandler.GIRLS_ELLIE_SIGH[1], 6.0f);
    }

    @Override
    public void doAction(String string, UUID player) {
        super.doAction(string, player);
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
                this.triggerActionSync(true, true, player);
                HandlePlayerMovement.setMovementLock(false);
            }
        }
    }

    @Override
    protected void alignPlayerToGirl(EntityPlayerMP player, boolean teleport) {
    }

    @Override
    public void setCurrentAction(Action action) {
        Action fp_class3243 = this.currentAction();
        if (action == Action.HUGSELECTED && !this.world.isRemote) {
            this.ai = 79;
        }
        if (fp_class3243 == Action.MISSIONARY_CUM && (action == Action.MISSIONARY_FAST || action == Action.MISSIONARY_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.COWGIRLCUM && (action == Action.COWGIRLSLOW || action == Action.COWGIRLFAST)) {
            return;
        }
        if (fp_class3243 == Action.CARRY_CUM && (action == Action.CARRY_SLOW || action == Action.CARRY_FAST)) {
            return;
        }
        if (action == Action.CARRY_INTRO) {
            this.ak = 0;
        }
        super.setCurrentAction(action);
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
        SexUI.init();
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
        UUID uUID = this.playerSheHasSexWith();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        float f = this.getYawRotation().floatValue();
        Vec3d vec3d = this.getTargetPosition().add(VectorMath.rotate(new Vec3d(0.0, 2.5625f - entityPlayer.getEyeHeight(), -0.3125), 180.0f + f));
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
            BlackScreenUI.b();
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
        String string = (String)this.entityDataManager.get(GIRL_HAND_STATES);
        if ("Missionary".equals(string)) {
            this.entityDataManager.set(OUTFIT_INDEX, 0);
            this.setCurrentAction(Action.MISSIONARY_START);
            uUID = this.playerSheHasSexWith();
            if (uUID == null) {
                return;
            }
            entityPlayer = this.world.getPlayerEntityByUUID(uUID);
            if (entityPlayer == null) {
                this.resetCameraAndPhysics();
                return;
            }
            entityPlayer.setNoGravity(true);
            entityPlayer.noClip = true;
            vec3d2 = this.getTargetPosition();
            entityPlayer.rotationYaw = this.getYawRotation().floatValue();
            vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, 0.1), entityPlayer.rotationYaw);
            vec3d2 = vec3d2.add(vec3d);
            entityPlayer.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        }
        if ("cowgirl".equals(string)) {
            this.entityDataManager.set(OUTFIT_INDEX, 0);
            this.setCurrentAction(Action.COWGIRLSTART);
            uUID = this.playerSheHasSexWith();
            if (uUID == null) {
                return;
            }
            entityPlayer = this.world.getPlayerEntityByUUID(uUID);
            if (entityPlayer == null) {
                this.resetCameraAndPhysics();
                return;
            }
            entityPlayer.setNoGravity(true);
            entityPlayer.noClip = true;
            vec3d2 = this.getTargetPosition();
            entityPlayer.rotationYaw = this.getYawRotation().floatValue() + 180.0f;
            vec3d = VectorMath.rotate(new Vec3d(0.0, 1.0 - (double)entityPlayer.eyeHeight, -1.8125), entityPlayer.rotationYaw);
            vec3d2 = vec3d2.add(vec3d);
            entityPlayer.setPositionAndUpdate(vec3d2.x, vec3d2.y, vec3d2.z);
            PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
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
        this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
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
        this.entityDataManager.set(IS_ANCHORED, false);
        this.setCurrentAction(Action.NULL);
        this.noClip = false;
        this.setNoGravity(false);
        if (this.am == null) {
            this.am = this.java_lang_Object_arr_g();
        }
        if (this.am == null) {
            this.broadcastChatMessage("no bed in sight...");
            this.world.playSound(null, this.getPosition(), SoundsHandler.GIRLS_ELLIE_SIGH[0], SoundCategory.NEUTRAL, 6.0f, 1.0f);
            this.resetGirlState();
            this.void_f();
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.playerSheHasSexWith());
        if (entityPlayer != null) {
            entityPlayer.setNoGravity(false);
            entityPlayer.noClip = false;
        }
        Vec3d vec3d = (Vec3d)this.am[0];
        int n = (Integer)this.am[1];
        if (vec3d.distanceTo(this.getPositionVector()) > 1.0) {
            this.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.35f);
            this.applyCustomPathNodeVelocity();
            return;
        }
        this.setTargetPosition(vec3d);
        this.setYawRotation(n);
        this.setCurrentAction(Action.SITDOWN);
        this.entityDataManager.set(IS_ANCHORED, true);
        this.al = 109;
        this.noClip = true;
        this.setNoGravity(true);
        this.ah = false;
        this.am = null;
    }

    @Override
    public void ResetNPCTasks() {
        super.ResetNPCTasks();
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
        this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
        float f = (float)(Math.atan2(this.posZ - entityPlayer.posZ, this.posX - entityPlayer.posX) * 57.29577951308232);
        this.setYawRotation(f);
        this.setTargetPosition(this.getPositionVector());
        this.entityDataManager.set(IS_ANCHORED, true);
        this.setCurrentAction(Action.DASH);
        this.Z = 16;
        this.setNoGravity(true);
        this.noClip = true;
        PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer);
        this.tasks.removeTask(this.aiWander);
        this.tasks.removeTask(this.aiLookAtPlayer);
    }

    void void_n() {
        if (--this.Z != 0) {
            return;
        }
        UUID uUID = this.playerSheHasSexWith();
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
        Vec3d vec3d = VectorMath.rotate(new Vec3d(0.0, 0.0, -0.5), entityPlayer.rotationYaw);
        Vec3d vec3d2 = vec3d.add(entityPlayer.getPositionVector());
        this.setTargetPosition(vec3d2);
        this.setYawRotation(entityPlayer.rotationYaw);
        this.setCurrentAction(Action.HUG);
        this.Y = 150;
    }

    void void_f() {
        this.entityDataManager.set(IS_ANCHORED, false);
        this.setCurrentAction(Action.NULL);
        this.setInteractionPlayerUUID((UUID)null);
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
        if (EllieEntity.getActiveSceneInfo(entityPlayer) != null) {
            return false;
        }
        if (this.playerSheHasSexWith() != null) {
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        block5 : switch (event.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                this.createAnimation("animation.ellie.eyes", true, event);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                double d = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                if (d == 0.0) {
                    this.createAnimation(this.boolean_i() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, event);
                    break;
                }
                if (this.boolean_i()) {
                    this.createAnimation("animation.ellie.crouchwalk", true, event);
                    break;
                }
                switch (this.getWalkType()) {
                    case RUN: {
                        this.createAnimation("animation.ellie.run", true, event);
                        break;
                    }
                    case FAST_WALK: {
                        this.createAnimation("animation.ellie.fastwalk", true, event);
                        break;
                    }
                    case WALK: {
                        this.createAnimation("animation.ellie.walk", true, event);
                    }
                }
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.ellie.null", true, event);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.ellie.strip", false, event);
                        break block5;
                    }
                    case DASH: {
                        this.createAnimation("animation.ellie.dash", false, event);
                        break block5;
                    }
                    case HUG: {
                        this.createAnimation("animation.ellie.hug", false, event);
                        break block5;
                    }
                    case HUGIDLE: {
                        this.createAnimation("animation.ellie.hugidle", true, event);
                        break block5;
                    }
                    case HUGSELECTED: {
                        this.createAnimation("animation.ellie.hugselected", false, event);
                        break block5;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.ellie.sitdown", false, event);
                        break block5;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.ellie.sitdownidle", true, event);
                        break block5;
                    }
                    case COWGIRLSTART: {
                        this.createAnimation("animation.ellie.cowgirlstart", false, event);
                        break block5;
                    }
                    case COWGIRLSLOW: {
                        this.createAnimation("animation.ellie.cowgirlslow2", true, event);
                        break block5;
                    }
                    case COWGIRLFAST: {
                        this.createAnimation("animation.ellie.cowgirlfast", true, event);
                        break block5;
                    }
                    case COWGIRLCUM: {
                        this.createAnimation("animation.ellie.cowgirlcum", true, event);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.ellie.attack" + this.nextAttack, false, event);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.ellie.bowcharge", false, event);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.ellie.ride", true, event);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.ellie.sit", true, event);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.ellie.throwpearl", false, event);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.ellie.downed", true, event);
                        break block5;
                    }
                    case MISSIONARY_START: {
                        this.createAnimation("animation.ellie.missionary_start", false, event);
                        break block5;
                    }
                    case MISSIONARY_SLOW: {
                        this.createAnimation("animation.ellie.missionary_slow", true, event);
                        break block5;
                    }
                    case MISSIONARY_FAST: {
                        this.createAnimation("animation.ellie.missionary_fast", true, event);
                        break block5;
                    }
                    case MISSIONARY_CUM: {
                        this.createAnimation("animation.ellie.missionary_cum", false, event);
                        break block5;
                    }
                    case CARRY_INTRO: {
                        this.createAnimation("animation.ellie.carry_intro", false, event);
                        break block5;
                    }
                    case CARRY_SLOW: {
                        this.createAnimation("animation.ellie.carry_slow" + this.aa, true, event);
                        break block5;
                    }
                    case CARRY_FAST: {
                        this.createAnimation("animation.ellie.carry_fast", true, event);
                        break block5;
                    }
                    case CARRY_CUM: {
                        this.createAnimation("animation.ellie.carry_cum", true, event);
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
                case "becomeNude": {
                    if (!this.getClosestPlayerID()) break;
                    this.changeDataParameterFromClient("currentModel", (Integer)this.entityDataManager.get(OUTFIT_INDEX) == 1 ? "0" : "1");
                    break;
                }
                case "stripDone": {
                    this.setCurrentAction((Action)null);
                    this.resetCameraAndPhysics();
                    this.U();
                    break;
                }
                case "hugMSG2": {
                    this.broadcastChatMessage("Hmm...");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HMPH[3], 6.0f);
                    break;
                }
                case "hugMSG3": {
                    this.broadcastChatMessage("Hey!");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HUH[1], 1.0f);
                    break;
                }
                case "hugMSG4": {
                    this.broadcastChatMessage(I18n.format("ellie.dialogue.mommyhorny", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_MOMMYHORNY, 0.5f);
                    break;
                }
                case "hugMSG5": {
                    this.broadcastChatMessage(I18n.format("ellie.dialogue.whattodo", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HUH[1], 6.0f);
                    break;
                }
                case "hugDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.a(Minecraft.getMinecraft().player, true);
                    break;
                }
                case "hugselectedMSG1": {
                    this.broadcastChatMessage(I18n.format("ellie.dialogue.iknow", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HMPH[3], 6.0f);
                    break;
                }
                case "hugselectedMSG2": {
                    this.broadcastChatMessage(I18n.format("ellie.dialogue.followmedarling", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    if (!this.isControlledByLocalPlayer()) break;
                    HandlePlayerMovement.setMovementLock(true);
                    break;
                }
                case "sitdownMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_COMETOMOMMY, 0.5f);
                    if (!this.getClosestPlayerID()) break;
                    this.broadcastChatMessage(I18n.format("ellie.dialogue.cometomommy", new Object[0]));
                    break;
                }
                case "cowgirlStartMSG0": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GIGGLE[4], 6.0f);
                    break;
                }
                case "cowgirlStartMSG1": {
                    if (!this.getClosestPlayerID()) break;
                    this.sendLocalClientMessage(I18n.format("ellie.dialogue.like", new Object[0]));
                    SexUI.resetCumPercentage();
                    break;
                }
                case "cowgirlStartMSG2": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cowgirlStartDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.COWGIRLSLOW);
                    SexUI.init();
                    break;
                }
                case "cowgirlfastMSG1": {
                    if (this.aj) {
                        this.aj = false;
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "cowgirlfastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.COWGIRLSLOW);
                    break;
                }
                case "cowgirlfastdomMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.2);
                    break;
                }
                case "cowgirlcumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_MOAN[5], 3.0f);
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG3": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING), 0.75f);
                    break;
                }
                case "cowgirlcumMSG4": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.hide();
                    break;
                }
                case "cowgirlcumMSG5": 
                case "missionary_cumMSG2": {
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GOODBOY, 0.5f);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.sendLocalClientMessage(I18n.format("ellie.dialogue.goodboy", new Object[0]));
                    break;
                }
                case "cowgirlcumMSG6": 
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.b();
                    break;
                }
                case "missionary_cumDone": 
                case "cowgirlcumDone": 
                case "carry_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.resetCumPercentage();
                    this.resetCameraAndPhysics();
                    break;
                }
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "pearl": {
                    PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "openSexUi": {
                    if (!this.getClosestPlayerID()) break;
                    SexUI.init();
                    break;
                }
                case "missionary_slowMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() && this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_MOAN), 6.0f);
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "missionary_fastMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    if (this.getRNG().nextBoolean() || this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_MOAN), 6.0f);
                    } else {
                        this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    }
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.05);
                    break;
                }
                case "missionary_startDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    SexUI.init();
                    break;
                }
                case "missionary_fastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.MISSIONARY_SLOW);
                    break;
                }
                case "bedRustle": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.MISC_POUNDING));
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    break;
                }
                case "bedRustle1": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[1]);
                    break;
                }
                case "missionary_cumMSG1": {
                    this.PlaySound(SoundsHandler.random(SoundsHandler.GIRLS_ELLIE_AHH), 6.0f);
                    break;
                }
                case "carry_introMSG1": {
                    this.sendLocalClientMessage("I'm hungry..");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_HMPH, 6.0f);
                    break;
                }
                case "carry_introMSG2": {
                    this.sendLocalClientMessage("heh~");
                    this.PlaySound(SoundsHandler.GIRLS_ELLIE_GIGGLE[3], 6.0f);
                    break;
                }
                case "lipsound": {
                    this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIPSOUND, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02);
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_INSERTS, 6.0f);
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    if (!this.isControlledByLocalPlayer()) break;
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
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.CARRY_SLOW);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

