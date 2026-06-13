/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4d
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Bia;

import java.util.UUID;
import javax.vecmath.Vector4d;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SendGirlToSex;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.LootTableHandler;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.interfaces.IBeddableSexGirl;
import com.trolmastercard.sexmod.util.interfaces.bh_class82;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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

public class BiaEntity
extends Fighter
implements bh_class82,
        IBeddableSexGirl {
    final static int ae = 3;
    public boolean Y = false;
    int ag = 0;
    boolean af = false;
    int Z = 0;
    boolean ab = true;
    int ac = -1;
    boolean aa = false;
    final int[] ai = new int[]{0, 180, -90, 90};
    final Vec3d[][] ad = new Vec3d[][]{{new Vec3d(0.5, 0.0, -0.5), new Vec3d(0.0, 0.0, -1.0)}, {new Vec3d(0.5, 0.0, 1.5), new Vec3d(0.0, 0.0, 1.0)}, {new Vec3d(-0.5, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0)}, {new Vec3d(1.5, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0)}};
    int ah = 1;

    public BiaEntity(World world) {
        super(world);
        this.setSize(0.49f, 1.65f);
        this.P = 140;
        this.O = 50;
        this.K = 140;
        this.V = new Vec3d(0.0, -0.029999997854232782, -0.2);
    }

    @Override
    public String getGirlName() {
        return "Bia";
    }

    @Override
    public float float_i() {
        return -0.2f;
    }

    @Override
    public void SetHome() {
        this.void_a("I am living here now nya~");
        this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH, new int[0]);
    }

    @Override
    public void void_b() {
        this.Y = true;
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.ANAL_CUM || fp_class3243 == Action.PRONE_DOGGY_CUM) {
            this.entityDataManager.set(h, "");
        }
        if (fp_class3243 == Action.ANAL_CUM && (fp_class3242 == Action.ANAL_FAST || fp_class3242 == Action.ANAL_SLOW)) {
            return;
        }
        if (fp_class3243 == Action.PRONE_DOGGY_CUM && (fp_class3242 == Action.PRONE_DOGGY_HARD || fp_class3242 == Action.PRONE_DOGGY_SOFT)) {
            return;
        }
        super.setCurrentAction(fp_class3242);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.BIA_LOOT_TABLE;
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.ab) {
            this.setNoGravity(false);
            this.noClip = false;
            this.ab = false;
        }
        if (this.Y) {
            ++this.ag;
            if (this.getPositionVector().equals(this.net_minecraft_util_math_Vec3d_o()) || this.ag > 40) {
                this.Y = false;
                this.ag = 0;
                this.void_b(this.world.getMinecraftServer().getPlayerList().getPlayerByUUID((UUID)this.getID()).rotationYaw + 180.0f);
                this.entityDataManager.set(G, true);
                this.getNavigator().clearPath();
                this.U();
            } else {
                this.rotationYaw = this.java_lang_Float_I().floatValue();
                try {
                    e.equals(null);
                } catch (NullPointerException nullPointerException) {
                    this.c(this.net_minecraft_util_math_Vec3d_aa());
                }
                this.setNoGravity(false);
                Vec3d vec3d = Reference.a(this.getPositionVector(), this.net_minecraft_util_math_Vec3d_o(), 40 - this.ag);
                this.setPosition(vec3d.x, vec3d.y, vec3d.z);
            }
        }
        if (this.af) {
            if (this.getPositionVector().distanceTo(this.net_minecraft_util_math_Vec3d_o()) < 0.6 || this.Z > 200) {
                this.af = false;
                this.entityDataManager.set(G, true);
                this.Z = 0;
                this.noClip = true;
                this.setNoGravity(true);
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                if ("anal".equals(this.entityDataManager.get(h))) {
                    this.setCurrentAction(Action.ANAL_PREPARE);
                    this.f(0);
                } else {
                    this.setCurrentAction(Action.SITDOWN);
                }
            } else {
                ++this.Z;
                if (this.Z == 60 || this.Z == 120) {
                    this.getNavigator().clearPath();
                    this.getNavigator().tryMoveToXYZ(this.net_minecraft_util_math_Vec3d_o().x, this.net_minecraft_util_math_Vec3d_o().y, this.net_minecraft_util_math_Vec3d_o().z, 0.35);
                }
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        boolean bl;
        if (super.processInteract(entityPlayer, enumHand)) {
            return true;
        }
        if (this.currentAction() == Action.SITDOWNIDLE) {
            return true;
        }
        ItemStack itemStack = entityPlayer.getHeldItem(enumHand);
        boolean bl2 = bl = itemStack.getItem() == Items.NAME_TAG;
        if (bl) {
            itemStack.interactWithEntity(entityPlayer, this, enumHand);
            return true;
        }
        if (this.world.isRemote && !this.boolean_b(entityPlayer)) {
            this.void_a(I18n.format("bia.dialogue.busy", new Object[0]));
        }
        return true;
    }

    @Override
    public boolean boolean_b(EntityPlayer entityPlayer) {
        if (this.getID() == null && (!this.boolean_J() || ((String)this.entityDataManager.get(v)).equals(Minecraft.getMinecraft().player.getPersistentID().toString()))) {
            String[] stringArray = new String[]{(Integer)this.entityDataManager.get(D) == 1 ? "action.names.strip" : "action.names.dressup", "action.names.talk", "action.names.headpat"};
            BiaEntity.a(entityPlayer, this, stringArray, true);
            return true;
        }
        return false;
    }

    void void_b(EntityPlayer entityPlayer) {
        BiaEntity.a(entityPlayer, this, new String[]{"action.names.anal", "doggy"}, false);
    }

    @Override
    public void ac() {
        if (this.boolean_Q() && !this.aa) {
            this.void_r();
        }
        this.aa = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.boolean_n() && this.currentAction() == Action.PRONE_DOGGY_INTRO && !fh_class313.a()) {
            SexUI.d();
        }
        this.void_d();
    }

    @Override
    protected void V() {
        super.V();
        this.ac = -1;
    }

    void void_d() {
        float f;
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 != Action.ANAL_WAIT && fp_class3242 != Action.SITDOWNIDLE) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            return;
        }
        if (entityPlayer.getDistance(this) > 1.0f) {
            return;
        }
        if (this.ac == -1) {
            if (this.world.isRemote) {
                fh_class313.b();
                HandlePlayerMovement.a(false);
            } else {
                this.void_e(entityPlayer.getPersistentID());
            }
            this.ac = j;
            return;
        }
        if (--this.ac > 0) {
            return;
        }
        this.ac = -1;
        entityPlayer.noClip = true;
        entityPlayer.setNoGravity(true);
        if (fp_class3242 == Action.ANAL_WAIT) {
            if (!this.world.isRemote) {
                this.setCurrentAction(Action.ANAL_START);
                Vec3d vec3d = this.net_minecraft_util_math_Vec3d_o().add(ck_class135.a(-0.3, -1.0, -0.5, this.java_lang_Float_I().floatValue()));
                entityPlayer.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
            } else if (this.boolean_n()) {
                SexUI.d();
            }
            return;
        }
        entityPlayer.rotationYaw = f = this.java_lang_Float_I().floatValue();
        entityPlayer.rotationPitch = 60.0f;
        if (!this.world.isRemote) {
            this.f(0);
            this.setCurrentAction(Action.PRONE_DOGGY_INTRO);
            Vec3d vec3d = this.net_minecraft_util_math_Vec3d_o();
            Vec3d vec3d2 = vec3d.add(ck_class135.a(0.0, 0.0, 1.0, f));
            this.c(vec3d2);
            Vec3d vec3d3 = vec3d.add(ck_class135.a(0.0, 1.1875 - (double)entityPlayer.getEyeHeight(), 0.5, f));
            entityPlayer.setPositionAndUpdate(vec3d3.x, vec3d3.y, vec3d3.z);
            this.void_a(true);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void ag() {
        super.ag();
        if (this.currentAction() != Action.PRONE_DOGGY_HARD) {
            return;
        }
        int n = this.ah;
        do {
            this.ah = this.getRNG().nextInt(3) + 1;
        } while (n == this.ah);
    }

    @Override
    public void void_g() {
        this.avoidWater = new EntityAIWanderAvoidWater(this, 0.35);
        this.o = new df_class178(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(5, this.o);
        this.tasks.addTask(5, this.avoidWater);
    }

    @Override
    public void a(String string, UUID uUID) {
        super.a(string, uUID);
        switch (string) {
            case "action.names.talk": {
                this.void_e(Minecraft.getMinecraft().player.getPersistentID());
                this.changeDataParameterFromClient("playerSheHasSexWith", Minecraft.getMinecraft().player.getPersistentID().toString());
                this.changeDataParameterFromClient("animationFollowUp", "talkHorny");
                this.void_a(uUID);
                break;
            }
            case "action.names.headpat": {
                this.void_e(Minecraft.getMinecraft().player.getPersistentID());
                this.changeDataParameterFromClient("playerSheHasSexWith", Minecraft.getMinecraft().player.getPersistentID().toString());
                this.changeDataParameterFromClient("animationFollowUp", "Headpat");
                this.void_a(uUID);
                break;
            }
            case "action.names.anal": {
                this.changeDataParameterFromClient("animationFollowUp", "anal");
                this.setCurrentAction(Action.TALK_RESPONSE);
                this.aa = true;
                break;
            }
            case "doggy": {
                this.changeDataParameterFromClient("animationFollowUp", "doggy");
                this.setCurrentAction(Action.TALK_RESPONSE);
                this.aa = true;
                break;
            }
            case "action.names.dressup": 
            case "action.names.strip": {
                this.setCurrentAction(Action.STRIP);
            }
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.world.isRemote) {
            return;
        }
        EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(Blocks.WOOL, this.getRNG().nextInt(4), 12));
        this.world.spawnEntity(entityItem);
    }

    void void_a(UUID uUID) {
        this.a(true, true, uUID);
        HandlePlayerMovement.a(false);
    }

    Vector4d javax_vecmath_Vector4d_a() {
        BlockPos blockPos = null;
        int n = 0;
        while (!this.boolean_a(blockPos)) {
            blockPos = this.a(this.getPosition(), n);
            if (++n != 50) continue;
        }
        if (blockPos == null || n == 50) {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.void_a(I18n.format("jenny.dialogue.nobedinsight", new Object[0]));
            return null;
        }
        this.tasks.removeTask(this.avoidWater);
        this.tasks.removeTask(this.o);
        Vec3d vec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        int n2 = -1;
        for (int i = 0; i < this.ad.length; ++i) {
            Vec3d vec3d2 = vec3d.add(this.ad[i][1]);
            Vec3d vec3d3 = vec3d.subtract(this.ad[i][1]);
            Block block = this.world.getBlockState(new BlockPos(vec3d2.x, vec3d2.y, vec3d2.z)).getBlock();
            if (block != Blocks.AIR || !cj_class134.b(this.world, new BlockPos(vec3d3))) continue;
            if (n2 == -1) {
                n2 = i;
                continue;
            }
            double d = this.getPosition().distanceSq(vec3d.add((Vec3d)this.ad[n2][0]).x, vec3d.add((Vec3d)this.ad[n2][0]).y, vec3d.add((Vec3d)this.ad[n2][0]).z);
            double d2 = this.getPosition().distanceSq(vec3d.add((Vec3d)this.ad[i][0]).x, vec3d.add((Vec3d)this.ad[i][0]).y, vec3d.add((Vec3d)this.ad[i][0]).z);
            if (!(d2 < d)) continue;
            n2 = i;
        }
        if (n2 == -1) {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.void_a(I18n.format("jenny.dialogue.nobedinsight", new Object[0]));
            return null;
        }
        Vec3d vec3d4 = vec3d.add(this.ad[n2][0]);
        return new Vector4d(vec3d4.x, vec3d4.y, vec3d4.z, (double)this.ai[n2]);
    }

    boolean boolean_a(BlockPos blockPos) {
        if (blockPos == null) {
            return false;
        }
        if (cj_class134.b(this.world, blockPos.north()) && this.world.isAirBlock(blockPos.south())) {
            return true;
        }
        if (cj_class134.b(this.world, blockPos.east()) && this.world.isAirBlock(blockPos.west())) {
            return true;
        }
        if (cj_class134.b(this.world, blockPos.south()) && this.world.isAirBlock(blockPos.north())) {
            return true;
        }
        return cj_class134.b(this.world, blockPos.west()) && this.world.isAirBlock(blockPos.east());
    }

    Vector4d javax_vecmath_Vector4d_b() {
        BlockPos blockPos = this.net_minecraft_util_math_BlockPos_a(this.getPosition());
        if (blockPos == null) {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.void_a(I18n.format("jenny.dialogue.nobedinsight", new Object[0]));
            return null;
        }
        this.tasks.removeTask(this.avoidWater);
        this.tasks.removeTask(this.o);
        Vec3d vec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        int n = -1;
        for (int i = 0; i < this.ad.length; ++i) {
            Vec3d vec3d2 = vec3d.add(this.ad[i][1]);
            if (this.world.getBlockState(new BlockPos(vec3d2.x, vec3d2.y, vec3d2.z)).getBlock() != Blocks.AIR) continue;
            if (n == -1) {
                n = i;
                continue;
            }
            double d = this.getPosition().distanceSq(vec3d.add((Vec3d)this.ad[n][0]).x, vec3d.add((Vec3d)this.ad[n][0]).y, vec3d.add((Vec3d)this.ad[n][0]).z);
            double d2 = this.getPosition().distanceSq(vec3d.add((Vec3d)this.ad[i][0]).x, vec3d.add((Vec3d)this.ad[i][0]).y, vec3d.add((Vec3d)this.ad[i][0]).z);
            if (!(d2 < d)) continue;
            n = i;
        }
        if (n == -1) {
            this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[2]);
            this.void_a(I18n.format("jenny.dialogue.bedobscured", new Object[0]));
            return null;
        }
        Vec3d vec3d3 = vec3d.add(this.ad[n][0]);
        return new Vector4d(vec3d3.x, vec3d3.y, vec3d3.z, (double)this.ai[n]);
    }

    @Override
    public void goToSexBed() {
        Vector4d vector4d;
        String string = (String)this.entityDataManager.get(h);
        Vector4d vector4d2 = vector4d = string.equals("anal") ? this.javax_vecmath_Vector4d_b() : this.javax_vecmath_Vector4d_a();
        if (vector4d == null) {
            return;
        }
        Vec3d vec3d = new Vec3d(vector4d.getX(), vector4d.getY(), vector4d.getZ());
        this.void_b((float)vector4d.getW());
        this.c(vec3d);
        this.r = this.java_lang_Float_I().floatValue();
        this.getNavigator().clearPath();
        this.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.35);
        this.af = true;
        this.Z = 0;
    }

    @Override
    protected Action FastSexAction(Action action) {
        if (action == Action.ANAL_SLOW) {
            return Action.ANAL_FAST;
        }
        if (action == Action.PRONE_DOGGY_INTRO) {
            return Action.PRONE_DOGGY_INSERT;
        }
        return null;
    }

    @Override
    protected Action CumAction(Action action) {
        if (action == Action.ANAL_SLOW || action == Action.ANAL_FAST) {
            return Action.ANAL_CUM;
        }
        if (action == Action.PRONE_DOGGY_SOFT || action == Action.PRONE_DOGGY_HARD) {
            return Action.PRONE_DOGGY_CUM;
        }
        return null;
    }

    @Override
    protected void U() {
        switch ((String)this.entityDataManager.get(h)) {
            case "talkHorny": {
                this.setCurrentAction(Action.TALK_HORNY);
                break;
            }
            case "Headpat": {
                this.setCurrentAction(Action.HEAD_PAT);
                break;
            }
            case "doggy": {
                this.void_r();
                PackageHandler.networkWrapper.sendToServer((IMessage)new SendGirlToSex(this.girlID()));
                return;
            }//И где собственно код??? TODO: Код запилить
            case "anal": {
                this.void_r();
                PackageHandler.networkWrapper.sendToServer((IMessage)new SendGirlToSex(this.girlID()));
                return;
            }
        }
        if (this.world.isRemote) {
            this.changeDataParameterFromClient("animationFollowUp", "");
        } else {
            this.entityDataManager.set(h, "");
        }
    }

    @Override
    public float float_T() {
        return 35.0f;
    }

    @Override
    public float float_ai() {
        return 140.0f;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return null;
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.bia.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.bia.fhappy", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.bia.null", true, animationEvent);
                    break;
                }
                if (this.isRiding()) {
                    this.createAnimation("animation.bia.sit", true, animationEvent);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    switch (this.com_trolmastercard_sexmod_em_class258$a_inner259_q()) {
                        case RUN: {
                            this.createAnimation("animation.bia.run", true, animationEvent);
                            break;
                        }
                        case FAST_WALK: {
                            this.createAnimation("animation.bia.fastwalk", true, animationEvent);
                            break;
                        }
                        case WALK: {
                            this.createAnimation("animation.bia.walk", true, animationEvent);
                        }
                    }
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation("animation.bia.idle", true, animationEvent);
                break;
            }
            case "action": {
                switch (this.currentAction()) {
                    case NULL: {
                        this.createAnimation("animation.bia.null", true, animationEvent);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.bia.strip", false, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.bia.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.bia.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case RIDE: {
                        this.createAnimation("animation.bia.ride", true, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.bia.sit", true, animationEvent);
                        break block5;
                    }
                    case THROW_PEARL: {
                        this.createAnimation("animation.bia.throwpearl", false, animationEvent);
                        break block5;
                    }
                    case DOWNED: {
                        this.createAnimation("animation.bia.downed", true, animationEvent);
                        break block5;
                    }
                    case TALK_HORNY: {
                        this.createAnimation("animation.bia.talk_horny2", true, animationEvent);
                        break block5;
                    }
                    case TALK_IDLE: {
                        this.createAnimation("animation.bia.talk_idle2", true, animationEvent);
                        break block5;
                    }
                    case TALK_RESPONSE: {
                        this.createAnimation("animation.bia.talk_response", true, animationEvent);
                        break block5;
                    }
                    case ANAL_PREPARE: {
                        this.createAnimation("animation.bia.anal_prepare", false, animationEvent);
                        break block5;
                    }
                    case ANAL_WAIT: {
                        this.createAnimation("animation.bia.anal_wait", false, animationEvent);
                        break block5;
                    }
                    case ANAL_START: {
                        this.createAnimation("animation.bia.anal_start", true, animationEvent);
                        break block5;
                    }
                    case ANAL_SLOW: {
                        this.createAnimation("animation.bia.anal_slow", true, animationEvent);
                        break block5;
                    }
                    case ANAL_FAST: {
                        this.createAnimation("animation.bia.anal_fast", true, animationEvent);
                        break block5;
                    }
                    case ANAL_CUM: {
                        this.createAnimation("animation.bia.anal_cum", false, animationEvent);
                        break block5;
                    }
                    case HEAD_PAT: {
                        this.createAnimation("animation.bia.headpat", false, animationEvent);
                        break block5;
                    }
                    case SITDOWN: {
                        this.createAnimation("animation.bia.sitdown", false, animationEvent);
                        break block5;
                    }
                    case SITDOWNIDLE: {
                        this.createAnimation("animation.bia.sitdownidle", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_INTRO: {
                        this.createAnimation("animation.bia.prone_doggy_intro", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_INSERT: {
                        this.createAnimation("animation.bia.prone_doggy_insert", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_SOFT: {
                        this.createAnimation("animation.bia.prone_doggy_soft", true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_HARD: {
                        this.createAnimation("animation.bia.prone_doggy_hard" + this.ah, true, animationEvent);
                        break block5;
                    }
                    case PRONE_DOGGY_CUM: {
                        this.createAnimation("animation.bia.prone_doggy_cum", true, animationEvent);
                        break block5;
                    }
                    case WAVE_IDLE: {
                        this.createAnimation("animation.bia.wave_idle", true, animationEvent);
                        break block5;
                    }
                    case WAVE: {
                        this.createAnimation("animation.bia.wave", true, animationEvent);
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
                case "attackDone": {
                    this.setCurrentAction(Action.NULL);
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "becomeNude": {
                    if (!this.boolean_e()) break;
                    this.changeDataParameterFromClient("currentModel", (Integer)this.entityDataManager.get(D) == 1 ? "0" : "1");
                    break;
                }
                case "stripDone": {
                    this.void_r();
                    this.U();
                    break;
                }
                case "stripMSG1": {
                    this.void_a(I18n.format("bia.dialogue.hihi", new Object[0]));
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_GIGGLE));
                    break;
                }
                case "sexUiOn": {
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "pearl": {
                    PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.girlID()));
                    break;
                }
                case "talk_hornyMSG1": {
                    this.void_a(I18n.format("bia.dialogue.heya", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HEY, new int[0]);
                    break;
                }
                case "talk_hornyMSG2": {
                    this.void_a(I18n.format("bia.dialogue.horny", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[2]);
                    break;
                }
                case "talk_hornyMSG3": {
                    this.void_a(I18n.format("bia.dialogue.so", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "talk_hornyMSG4": {
                    this.void_a(I18n.format("bia.dialogue.fun", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "talk_hornyDone": {
                    this.setCurrentAction(Action.TALK_IDLE);
                    if (!this.boolean_n()) break;
                    this.void_b(Minecraft.getMinecraft().player);
                    break;
                }
                case "talk_responseMSG1": {
                    this.void_a(I18n.format("bia.dialogue.huh", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[2]);
                    break;
                }
                case "talk_responseMSG2": {
                    this.void_a(I18n.format("bia.dialogue.iuhm", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[1]);
                    break;
                }
                case "talk_responseMSG3": {
                    this.void_a(I18n.format("bia.dialogue.yes", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[0]);
                    break;
                }
                case "talk_responseDone": {
                    if (this.boolean_n()) {
                        this.s();
                    }
                    this.U();
                    break;
                }
                case "anal_prepareMSG1": {
                    this.PlaySound(SoundsHandler.MISC_PLOB[0]);
                    break;
                }
                case "anal_prepareMSG2": {
                    this.PlaySound(SoundsHandler.MISC_BEDRUSTLE[0]);
                    break;
                }
                case "anal_prepareDone": {
                    this.setCurrentAction(Action.ANAL_WAIT);
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    break;
                }
                case "anal_startMSG1": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[3]);
                    this.PlaySound(SoundsHandler.MISC_POUNDING[34]);
                    break;
                }
                case "anal_fastMSG1": {
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                }
                case "anal_slowMSG1": 
                case "anal_startMSG2": {
                    if (this.boolean_n()) {
                        SexUI.addCumPercentage(0.02);
                    }
                    this.a(SoundsHandler.a(SoundsHandler.MISC_POUNDING), 0.5f);
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "anal_fastDone": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                }
                case "anal_startDone": {
                    this.setCurrentAction(Action.ANAL_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "anal_cumMSG2": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_BIA_AHH));
                    break;
                }
                case "blackScreen": 
                case "anal_cumBlackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "doggy_cumDone": 
                case "anal_cumDone": {
                    if (!this.boolean_n()) break;
                    SexUI.resetCumPercentage();
                    this.void_r();
                    break;
                }
                case "headpatMSG1": {
                    this.void_a(I18n.format("bia.dialogue.headpats", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH[0]);
                    break;
                }
                case "headpatMSG2": {
                    this.void_a(I18n.format("bia.dialogue.hmm", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[0]);
                    break;
                }
                case "headpatMSG3": {
                    this.void_a(I18n.format("bia.dialogue.huh2", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_HUH[0]);
                    break;
                }
                case "headpatMSG4": {
                    this.void_a(I18n.format("bia.dialogue.thankyou", new Object[0]));
                    this.PlaySound(SoundsHandler.GIRLS_BIA_GIGGLE[1]);
                    break;
                }
                case "headpatDone": {
                    this.void_r();
                    break;
                }
                case "sitdownMSG1": {
                    this.void_a("come here big boy~");
                    this.PlaySound(SoundsHandler.GIRLS_BIA_BREATH, new int[0]);
                    break;
                }
                case "sitdownDone": {
                    this.setCurrentAction(Action.SITDOWNIDLE);
                    break;
                }
                case "slide": {
                    this.PlaySound(SoundsHandler.a(SoundsHandler.MISC_SLIDE));
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.005);
                    break;
                }
                case "pound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "doggyMoan": {
                    this.PlaySound(this.getRNG().nextBoolean() ? SoundsHandler.GIRLS_BIA_AHH : SoundsHandler.GIRLS_BIA_MMM, new int[0]);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04);
                    break;
                }
                case "doggySwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PRONE_DOGGY_HARD);
                    break;
                }
                case "doggyReset": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_INSERTS, 6.0f);
                    break;
                }
                case "orgasm1": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[6]);
                    break;
                }
                case "orgasm2": {
                    this.PlaySound(SoundsHandler.GIRLS_BIA_MMM[7]);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    private static NullPointerException a(NullPointerException nullPointerException) {
        return nullPointerException;
    }
}

