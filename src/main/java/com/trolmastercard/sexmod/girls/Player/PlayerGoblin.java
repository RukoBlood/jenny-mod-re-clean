/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$RenderTickEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.ResetGirl;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.girls.Goblin.GoblinRenderer;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.gui.KoboldInventoryUI;
import com.trolmastercard.sexmod.gui.SexUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerGoblin
extends ew_class277
implements ai_class30 {
    final static public float aI = 2.0f;
    final static public DataParameter<String> ax = EntityDataManager.createKey(PlayerGoblin.class, DataSerializers.STRING).getSerializer().createKey(122);
    final static public DataParameter<Boolean> aA = EntityDataManager.createKey(PlayerGoblin.class, DataSerializers.BOOLEAN).getSerializer().createKey(126);
    int aJ = 0;
    int az = -1;
    int aG = 0;
    Action aw = Action.NULL;
    int aE = -1;
    boolean aC = false;
    boolean aB = true;
    boolean ay = true;
    boolean aF = false;
    boolean aH = false;
    String aD = "";

    public PlayerGoblin(World world) {
        super(world);
    }

    public PlayerGoblin(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float float_i() {
        return 0.9f;
    }

    @Override
    public IRenderer com_trolmastercard_sexmod_at_class43_a(int n) {
        return new fv_class331();
    }

    @Override
    public String HandTexture(int n) {
        return "textures/entity/kobold/hand.png";
    }

    @Override
    public Vec3i net_minecraft_util_math_Vec3i_b(int n) {
        String[] stringArray = PlayerGoblin.java_lang_String_arr_a(this);
        if (stringArray.length < 8) {
            return super.net_minecraft_util_math_Vec3i_b(n);
        }
        return by_class106.values()[Integer.parseInt(stringArray[7])].a();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        eh_class250 eh_class2502 = eh_class250.values()[this.getRNG().nextInt(eh_class250.values().length)];
        this.entityDataManager.register(au, new BlockPos(eh_class2502.a()));
        this.entityDataManager.register(as, GoblinEntity.ax.name());
        this.entityDataManager.register(aA, false);
        this.entityDataManager.register(ax, "");
    }

    @Override
    public void b(String string, UUID uUID) {
        if ("anal".equals(string)) {
            this.void_b(uUID);
            this.setCurrentAction(Action.NELSON_INTRO);
            this.a(this.int_ah(), Action.NELSON_INTRO);
            this.f(0);
        }
        if ("paizuri".equals(string)) {
            this.void_b(uUID);
            this.setCurrentAction(Action.PAIZURI_START);
            this.a(this.int_ah(), Action.PAIZURI_START);
            this.f(0);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean boolean_b(EntityPlayer entityPlayer) {
        Minecraft.getMinecraft().displayGuiScreen(new KoboldInventoryUI(this, entityPlayer, new String[]{"anal", "paizuri"}, null, false));
        return true;
    }

    @Override
    public EntityPlayer net_minecraft_entity_player_EntityPlayer_c(EntityPlayer entityPlayer) {
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return entityPlayer;
        }
        EntityPlayer entityPlayer2 = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer2 == null) {
            return entityPlayer;
        }
        return entityPlayer2;
    }

    @Override
    public boolean boolean_d() {
        return this.java_util_UUID_e() == null || !Minecraft.getMinecraft().player.getPersistentID().equals(this.java_util_UUID_m());
    }

    @Override
    public boolean boolean_z() {
        UUID uUID = this.java_util_UUID_e();
        return uUID == null;
    }

    @Override
    public Vec3d c(Vec3d vec3d, float f) {
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return vec3d;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return vec3d;
        }
        Vec3d vec3d2 = entityPlayer.getPositionVector();
        Vec3d vec3d3 = new Vec3d(entityPlayer.lastTickPosX, entityPlayer.lastTickPosY, entityPlayer.lastTickPosZ);
        return Reference.a(vec3d3, vec3d2, (double)f);
    }

    void void_c(EntityPlayer entityPlayer) {
        if (this.currentAction() != Action.NULL) {
            return;
        }
        if (this.java_util_UUID_e() != null) {
            return;
        }
        if (GoblinEntity.d_19(entityPlayer.getPersistentID())) {
            entityPlayer.sendStatusMessage(new TextComponentString("you are already carrying a Goblin"), true);
            return;
        }
        this.void_a(entityPlayer.getPersistentID());
        this.setCurrentAction(Action.PICK_UP);
        this.void_b(45);
        EntityPlayer entityPlayer2 = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer2 == null) {
            return;
        }
        entityPlayer2.setNoGravity(true);
        entityPlayer2.noClip = true;
        if (this.world.isRemote) {
            return;
        }
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer2);
    }

    @Override
    protected String a(StringBuilder stringBuilder) {
        e4_class223.void_a(stringBuilder, 3);
        e4_class223.void_a(stringBuilder, 2);
        e4_class223.void_a(stringBuilder, 2);
        e4_class223.void_a(stringBuilder, 7);
        e4_class223.void_a(stringBuilder, 7);
        e4_class223.void_a(stringBuilder, 5);
        e4_class223.void_a(stringBuilder, g5_class349.values().length - 1);
        e4_class223.void_a(stringBuilder, by_class106.values().length - 1);
        e4_class223.void_a(stringBuilder, eh_class250.values().length - 1);
        e4_class223.c(stringBuilder, 0);
        return stringBuilder.toString();
    }

    @Override
    public ArrayList<Integer> D() {
        return new ArrayList<Integer>(){
            {
                this.add(4);
                this.add(3);
                this.add(3);
                this.add(16);
                this.add(16);
                this.add(6);
                this.add(g5_class349.values().length);
                this.add(by_class106.values().length);
                this.add(eh_class250.values().length);
            }
        };
    }

    @Override
    public List<Integer> u() {
        return Collections.singletonList(2);
    }

    @Override
    protected void void_a() {
        PlayerGoblinRenderer.e();
        GoblinRenderer.c();
    }

    @Override
    public float getEyeHeight() {
        return 0.75f;
    }

    @Override
    public boolean boolean_o() {
        return this.boolean_Q() || this.java_util_UUID_e() != null;
    }

    @Override
    public boolean a(Action fp_class3242, EntityPlayer entityPlayer) {
        float f;
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return false;
        }
        EntityPlayer entityPlayer2 = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer2 == null) {
            return false;
        }
        float f2 = entityPlayer.rotationYaw;
        float f3 = fp_class3242 == Action.PICK_UP ? 180.0f : 0.0f;
        float f4 = entityPlayer2.rotationYaw - 90.0f + f3;
        float f5 = entityPlayer2.rotationYaw + 90.0f + f3;
        if (f2 < f4) {
            entityPlayer.rotationYaw = f4;
        }
        if (f2 > f5) {
            entityPlayer.rotationYaw = f5;
        }
        float f6 = entityPlayer.rotationPitch;
        float f7 = f = fp_class3242 == Action.PICK_UP ? 0.0f : 37.5f;
        if (f6 > f) {
            entityPlayer.rotationPitch = f;
        }
        return true;
    }

    @Override
    public Vec3d b(Vec3d vec3d, float f) {
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return vec3d;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return vec3d;
        }
        float f2 = Reference.Lerp(entityPlayer.prevRenderYawOffset, entityPlayer.renderYawOffset, f);
        Vec3d vec3d2 = vec3d;
        float f3 = 135.0f;
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.PICK_UP) {
            vec3d2 = new Vec3d(vec3d.x, vec3d.y, -vec3d.z);
            f3 += 40.0f;
        } else if (fp_class3242 != Action.START_THROWING) {
            vec3d2 = vec3d2.subtract(0.0, 2.0, 0.0);
        }
        vec3d2 = ck_class135.a(vec3d2, f2 + f3);
        return vec3d2;
    }

    @SideOnly(value=Side.CLIENT)
    void void_f() {
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return;
        }
        if (this.currentAction() == Action.START_THROWING) {
            entityPlayer.isDead = false;
            if (!this.world.loadedEntityList.contains(entityPlayer)) {
                this.world.spawnEntity(entityPlayer);
            }
        }
    }

    @Override
    public void onUpdate() {
        GoblinEntity.e(this);
        this.void_d();
        this.void_j();
        super.onUpdate();
        if (!this.world.isRemote) {
            return;
        }
        this.void_f();
        Action fp_class3242 = this.currentAction();
        this.d(fp_class3242);
        this.void_c(fp_class3242);
        this.aw = fp_class3242;
    }

    @Override
    public boolean boolean_E() {
        return this.java_util_UUID_e() != null;
    }

    void void_j() {
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.THROWN) {
            return;
        }
        if (fp_class3242 == Action.START_THROWING && this.int_a() > 15) {
            return;
        }
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        EntityPlayer entityPlayer2 = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer2 == null) {
            return;
        }
        entityPlayer2.noClip = true;
        entityPlayer2.setNoGravity(true);
        entityPlayer2.setPosition(entityPlayer.posX, entityPlayer.posY + 2.0, entityPlayer.posZ);
    }

    void void_d() {
        PlayerGoblin eq_class2642 = this;
        int n = eq_class2642.int_a();
        if (n == -1) {
            return;
        }
        eq_class2642.void_c(++n);
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return;
        }
        if (n == 15) {
            Vec3d vec3d = GoblinEntity.b(this);
            float f = GoblinEntity.d(this);
            float f2 = GoblinEntity.c(this);
            if (this.world.isRemote && this.boolean_f()) {
                HandlePlayerMovement.a(true);
            }
            Vec3d vec3d2 = GoblinEntity.a(new Vec3d(0.0, 0.0, 1.5), f, f2);
            entityPlayer.motionX = vec3d2.x;
            entityPlayer.motionY = vec3d2.y;
            entityPlayer.motionZ = vec3d2.z;
            if (!this.world.isRemote) {
                this.void_b(f2);
            }
        }
        entityPlayer.noClip = false;
        entityPlayer.setNoGravity(false);
        if (n == 39) {
            this.void_c(-1);
            this.setCurrentAction(Action.THROWN);
            this.void_e((UUID)null);
            this.void_a((UUID)null);
        }
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        GoblinEntity.void_a(this);
        this.void_o();
        this.void_e();
    }

    void void_e() {
        if (this.currentAction() != Action.STAND_UP) {
            return;
        }
        if (++this.aJ < 37) {
            return;
        }
        this.aJ = 0;
        this.setCurrentAction(Action.NULL);
    }

    void void_o() {
        if (this.currentAction() != Action.THROWN) {
            return;
        }
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return;
        }
        if (!entityPlayer.onGround) {
            return;
        }
        int n = this.int_d() + 1;
        this.void_a(n);
        if (n < 30) {
            return;
        }
        this.void_a(0);
        this.setCurrentAction(Action.STAND_UP);
    }

    @Override
    @Nullable
    public UUID java_util_UUID_e() {
        String string = this.entityDataManager.get(ax);
        if ("".equals(string)) {
            return null;
        }
        try {
            return UUID.fromString(this.entityDataManager.get(ax));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void void_a(UUID uUID) {
        if (uUID == null) {
            this.entityDataManager.set(ax, "");
            return;
        }
        this.entityDataManager.set(ax, uUID.toString());
    }

    public EntityPlayer net_minecraft_entity_player_EntityPlayer_r() {
        UUID uUID = this.java_util_UUID_e();
        if (uUID == null) {
            return null;
        }
        return this.world.getPlayerEntityByUUID(uUID);
    }

    @Override
    public void void_c(int n) {
        this.az = n;
    }

    @Override
    public int int_a() {
        return this.az;
    }

    @Override
    public void void_a(int n) {
        this.aG = n;
    }

    @Override
    public int int_d() {
        return this.aG;
    }

    @Override
    public void void_a(Action fp_class3242) {
        this.aw = fp_class3242;
    }

    @Override
    public Action GoblinAction() {
        return this.aw;
    }

    @Override
    public void void_b(int n) {
        this.aE = n;
    }

    @Override
    public int int_c() {
        return this.aE;
    }

    @Override
    public void void_g() {
        super.void_g();
        this.entityDataManager.set(aA, false);
        if (this.java_util_UUID_e() == null) {
            return;
        }
        this.void_a((UUID)null);
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return;
        }
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
    }

    @SideOnly(value=Side.CLIENT)
    void void_c(Action fp_class3242) {
        if (fp_class3242 == Action.NELSON_FAST && this.aw != Action.NELSON_FAST) {
            this.aF = false;
        }
    }

    @SideOnly(value=Side.CLIENT)
    void d(Action fp_class3242) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (!minecraft.player.getPersistentID().equals(this.getID())) {
            return;
        }
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }
        switch (fp_class3242) {
            case NELSON_CUM: 
            case NELSON_FAST: 
            case NELSON_INTRO: 
            case NELSON_SLOW: {
                minecraft.gameSettings.thirdPersonView = 2;
            }
        }
    }

    @Override
    public void void_a(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int n : list) {
            e4_class223.c(stringBuilder, n);
        }
        e4_class223.c(stringBuilder, 1);
        this.entityDataManager.set(at, stringBuilder.toString());
    }

    @Override
    @Nullable
    protected Action FastSexAction(Action action) {
        switch (action) {
            case PAIZURI_IDLE: 
            case PAIZURI_SLOW: {
                return Action.PAIZURI_FAST;
            }
            case BREEDING_SLOW_0: {
                return Action.BREEDING_FAST_0;
            }
            case BREEDING_SLOW_2: {
                return Action.BREEDING_FAST_2;
            }
            case NELSON_SLOW: {
                return Action.NELSON_FAST;
            }
        }
        return null;
    }

    @Override
    public void setCurrentAction(Action fp_class3242) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.PAIZURI_CUM && (fp_class3242 == Action.PAIZURI_SLOW || fp_class3242 == Action.PAIZURI_FAST)) {
            return;
        }
        if (fp_class3243 == Action.NELSON_CUM && (fp_class3242 == Action.NELSON_SLOW || fp_class3242 == Action.NELSON_FAST)) {
            return;
        }
        if (fp_class3243 == Action.BREEDING_CUM_0 && (fp_class3242 == Action.BREEDING_SLOW_0 || fp_class3242 == Action.BREEDING_FAST_0)) {
            return;
        }
        if (fp_class3242 == Action.PAIZURI_START && !this.world.isRemote) {
            this.void_m();
        }
        if (fp_class3242 == Action.NELSON_INTRO && !this.world.isRemote) {
            this.void_q();
        }
        if (fp_class3242 == Action.NELSON_CUM) {
            this.entityDataManager.set(aA, true);
        }
        if (fp_class3243 == Action.NELSON_CUM && fp_class3242 != Action.NELSON_CUM) {
            this.entityDataManager.set(aA, false);
        }
        super.setCurrentAction(fp_class3242);
    }

    void void_q() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer == null) {
            return;
        }
        this.void_b(entityPlayer.rotationYaw);
        this.noClip = true;
        this.setNoGravity(true);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ - 1.0);
    }

    void void_m() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getID());
        if (entityPlayer == null) {
            return;
        }
        this.void_b(entityPlayer.rotationYaw + 180.0f);
        this.noClip = true;
        this.setNoGravity(true);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY - 0.5, entityPlayer.posZ - (double)0.6f);
        entityPlayer.rotationPitch = 70.0f;
        entityPlayer.prevRotationPitch = 70.0f;
    }

    @Override
    public boolean boolean_l() {
        return this.java_util_UUID_e() == null;
    }

    @Override
    public void void_b(EntityPlayer entityPlayer) {
        if (!entityPlayer.getPersistentID().equals(this.java_util_UUID_e())) {
            return;
        }
        ResetGirl.a_inner422.a(this);
        this.void_a(false);
        this.setCurrentAction(Action.NULL);
        this.void_a((UUID)null);
    }

    @Override
    protected Action CumAction(Action action) {
        switch (action) {
            case PAIZURI_SLOW: 
            case PAIZURI_FAST: 
            case PAIZURI_FAST_CONTINUES: {
                return Action.PAIZURI_CUM;
            }
            case BREEDING_1: {
                return Action.BREEDING_CUM_1;
            }
            case BREEDING_SLOW_2: 
            case BREEDING_FAST_2: {
                return Action.BREEDING_CUM_2;
            }
            case NELSON_FAST: 
            case NELSON_SLOW: {
                return Action.NELSON_CUM;
            }
        }
        return null;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block5 : switch (animationEvent.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.goblin.null", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.goblin.blink", true, animationEvent);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, animationEvent);
                    break;
                }
                if (this.ak) {
                    this.createAnimation("animation.goblin.sit", true, animationEvent);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.af) {
                    boolean bl = this.aC = !this.aC;
                }
                if (!this.af) {
                    this.createAnimation("animation.goblin.fly" + (this.aC ? "2" : ""), true, animationEvent);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.aj) {
                        this.movementController.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.goblin.running", true, animationEvent);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation("animation.goblin.walk", true, animationEvent);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation("animation.goblin.backwards_walk", true, animationEvent);
                    break;
                }
                this.createAnimation("animation.goblin.idle", true, animationEvent);
                break;
            }
            case "action": {
                Minecraft minecraft = Minecraft.getMinecraft();
                String string = minecraft.player.getPersistentID().equals(this.java_util_UUID_e()) && minecraft.gameSettings.thirdPersonView == 0 ? "1" : "3";
                switch (this.currentAction()) {
                    case SHOULDER_IDLE: {
                        this.createAnimation("animation.goblin.shoulder_idle", true, animationEvent);
                        break block5;
                    }
                    case PICK_UP: {
                        this.createAnimation(String.format("animation.goblin.pick_up_%sperson", string), true, animationEvent);
                        break block5;
                    }
                    case START_THROWING: {
                        this.createAnimation(String.format("animation.goblin.throw_%sperson", string), true, animationEvent);
                        break block5;
                    }
                    case THROWN: {
                        this.createAnimation("animation.goblin.thrown", true, animationEvent);
                        break block5;
                    }
                    case NULL: {
                        this.createAnimation("animation.goblin.null", true, animationEvent);
                        break block5;
                    }
                    case STAND_UP: {
                        this.createAnimation("animation.goblin.stand_up", false, animationEvent);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.goblin.strip", false, animationEvent);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.goblin.attack" + this.S, false, animationEvent);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.goblin.bowcharge", false, animationEvent);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.goblin.sit", true, animationEvent);
                        break block5;
                    }
                    case NELSON_INTRO: {
                        this.createAnimation("animation.goblin.nelson_intro", true, animationEvent);
                        break block5;
                    }
                    case NELSON_SLOW: {
                        this.createAnimation("animation.goblin.nelson_slow" + (this.ay ? "" : "2"), true, animationEvent);
                        break block5;
                    }
                    case NELSON_FAST: {
                        this.createAnimation("animation.goblin.nelson_fast" + (this.aF ? "c" : "s"), true, animationEvent);
                        break block5;
                    }
                    case NELSON_CUM: {
                        this.createAnimation("animation.goblin.nelson_cum", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_INTRO_0: {
                        this.createAnimation("animation.goblin.breeding_intro_1", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_INTRO_1: {
                        this.createAnimation("animation.goblin.breeding_intro_2", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_INTRO_2: {
                        this.createAnimation("animation.goblin.breeding_intro_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_SLOW_0: {
                        this.createAnimation("animation.goblin.breeding_slow_1" + (this.aB ? "l" : "r"), true, animationEvent);
                        break block5;
                    }
                    case BREEDING_SLOW_2: {
                        this.createAnimation("animation.goblin.breeding_slow_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_FAST_0: {
                        this.createAnimation("animation.goblin.breeding_fast_1" + (this.aH ? "c" : "s"), true, animationEvent);
                        break block5;
                    }
                    case BREEDING_FAST_2: {
                        this.createAnimation("animation.goblin.breeding_fast_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_CUM_0: {
                        this.createAnimation("animation.goblin.breeding_cum_1", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_CUM_1: {
                        this.createAnimation("animation.goblin.breeding_cum_2", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_CUM_2: {
                        this.createAnimation("animation.goblin.breeding_cum_3", true, animationEvent);
                        break block5;
                    }
                    case BREEDING_1: {
                        this.createAnimation("animation.goblin.breeding_2", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.goblin.paizuri_start", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.goblin.paizuri_slow" + this.aD, true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.goblin.paizuri_fast", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_FAST_CONTINUES: {
                        this.createAnimation("animation.goblin.paizuri_fast_countinues", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_IDLE: {
                        this.createAnimation("animation.goblin.paizuri_idle", true, animationEvent);
                        break block5;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.goblin.paizuri_cum", true, animationEvent);
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
                    if (++this.S != 3) break;
                    this.S = 0;
                    break;
                }
                case "catchEh": {
                    this.void_a("ehh..");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchAkward": {
                    this.void_a("awkward..");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchWell": {
                    this.void_a("well...");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchRather": {
                    this.void_a("would you rather have this stupid... thing?");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchMe": {
                    this.void_a("...or use me?~");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchDone": {
                    if (!"bj".equals(this.entityDataManager.get(h))) break;
                    this.setCurrentAction(Action.CATCH_BJ);
                    break;
                }
                case "catchBjDone": {
                    this.setCurrentAction(Action.CATCH_BJ_IDLE);
                    if (!this.boolean_n()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    PlayerGoblin.a(entityPlayerSP, this, new String[]{"use her", "take ur stuff back"}, null, false);
                    break;
                }
                case "paizuriChoice": {
                    this.void_a("good choice!~");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriBoth": {
                    this.void_a("...for both of us!");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizruiUse": {
                    this.void_a("now use me like a fuck toy!~");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriSwitch": {
                    if (this.getRNG().nextBoolean()) break;
                    this.aD = "".equals(this.aD) ? "2" : "";
                    break;
                }
                case "touch": {
                    this.a(SoundsHandler.MISC_TOUCH, 3.0f);
                    break;
                }
                case "pound": {
                    this.a(SoundsHandler.MISC_POUNDING);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "paizuri_startDone": {
                    this.setCurrentAction(Action.PAIZURI_IDLE);
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "paizuriFastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    break;
                }
                case "paizuriFastReady": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PAIZURI_FAST_CONTINUES);
                    break;
                }
                case "paizuriFastContinuesReady": 
                case "neslon_fastBackSwitch": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.N();
                    break;
                }
                case "smallPound": {
                    this.a(SoundsHandler.MISC_POUNDING, 0.25f);
                    if (!this.boolean_n()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "paizruiCam": {
                    if (!this.boolean_n()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    entityPlayerSP.rotationPitch = 70.0f;
                    entityPlayerSP.prevRotationPitch = 70.0f;
                    break;
                }
                case "blackScreen": {
                    if (!this.boolean_n()) break;
                    fh_class313.b();
                    break;
                }
                case "cumSound": {
                    this.a(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "jumpCam": {
                    if (!this.boolean_n()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() + 170.0f;
                    minecraft.player.rotationPitch = -20.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 2;
                    break;
                }
                case "breedingHmm": {
                    if (this.boolean_n()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
                        minecraft.player.rotationPitch = -15.0f;
                        minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                        minecraft.gameSettings.thirdPersonView = 0;
                    }
                    this.void_a("hmm...");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingFound": {
                    this.void_a("guess we found a worthy breeding partner!");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingEnough": {
                    this.void_a("Eh.. go pin him down, before he runs off!");
                    this.a(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingCam2": {
                    if (this.boolean_n()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.gameSettings.thirdPersonView = 2;
                        minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() - 120.0f;
                        minecraft.player.rotationPitch = -30.0f;
                    }
                }
                case "breedingIntroDone": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "breeding_slow1Done": {
                    if (this.getRNG().nextBoolean()) {
                        boolean bl = this.aB = !this.aB;
                    }
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_FAST_0);
                    this.aH = false;
                    break;
                }
                case "breeding_fast1Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.boolean_n()) break;
                    this.aH = false;
                    break;
                }
                case "breeding_fast1Ready": {
                    if (!this.boolean_n() || !HandlePlayerMovement.isThrusting) break;
                    this.aH = true;
                    this.N();
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "cum": {
                    this.a(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "breeding_intro_3Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_2);
                    break;
                }
                case "breeding_3_wiggle": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "breeding_fast_3Done": {
                    if (!this.boolean_n() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_SLOW_2);
                    break;
                }
                case "breeding_intro_2Done": {
                    this.setCurrentAction(Action.BREEDING_1);
                    break;
                }
                case "breeding_cumCam": {
                    if (!this.boolean_n()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.gameSettings.thirdPersonView = 0;
                    minecraft.player.rotationYaw = this.java_lang_Float_I().floatValue() + 180.0f;
                    minecraft.player.rotationPitch = -15.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 0;
                    break;
                }
                case "neslon_introDone": {
                    this.setCurrentAction(Action.NELSON_SLOW);
                    if (!this.boolean_n()) break;
                    SexUI.d();
                    break;
                }
                case "nelson_slowDone": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.ay = !this.ay;
                    break;
                }
                case "neslon_fastSwitch": {
                    if (!this.boolean_n()) {
                        this.aF = true;
                        return;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.aF = true;
                    break;
                }
                case "nelsonFastDone": {
                    this.aF = false;
                    if (!this.boolean_n()) break;
                    this.setCurrentAction(Action.NELSON_SLOW);
                    break;
                }
                case "paizuriCumDone": 
                case "nelson_cumDone": {
                    if (!this.boolean_n()) break;
                    this.void_r();
                    this.setCurrentAction(Action.NULL);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        this.movementController.transitionLengthTicks = 2.0;
        animationData.addAnimationController(this.actionController);
        animationData.addAnimationController(this.movementController);
        animationData.addAnimationController(this.eyesController);
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class a_inner265 {
        HashSet<EntityPlayer> a = new HashSet();

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(RenderHandEvent renderHandEvent) {
            PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(Minecraft.getMinecraft().player);
            if (ei_class2512 == null) {
                return;
            }
            if (!(ei_class2512 instanceof ai_class30)) {
                return;
            }
            if (((ai_class30)((Object)ei_class2512)).java_util_UUID_e() != null) {
                renderHandEvent.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void a(TickEvent.PlayerTickEvent playerTickEvent) {
            EntityPlayer entityPlayer = playerTickEvent.player;
            if (entityPlayer == null) {
                return;
            }
            this.a(entityPlayer);
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(TickEvent.RenderTickEvent renderTickEvent) {
            if (renderTickEvent.phase == TickEvent.Phase.END) {
                return;
            }
            EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
            if (entityPlayerSP == null) {
                return;
            }
            this.a(entityPlayerSP);
        }

        void a(EntityPlayer entityPlayer) {
            PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayer);
            if (!(ei_class2512 instanceof PlayerGoblin)) {
                return;
            }
            Action fp_class3242 = ei_class2512.currentAction();
            if (fp_class3242 == Action.THROWN) {
                return;
            }
            if (fp_class3242 == Action.START_THROWING && ((ai_class30)((Object)ei_class2512)).int_a() > 15) {
                return;
            }
            UUID uUID = ((PlayerGoblin)ei_class2512).java_util_UUID_e();
            if (uUID == null) {
                return;
            }
            EntityPlayer entityPlayer2 = entityPlayer.world.getPlayerEntityByUUID(uUID);
            if (entityPlayer2 == null) {
                return;
            }
            entityPlayer.noClip = true;
            entityPlayer.setNoGravity(true);
            ei_class2512.noClip = true;
            ei_class2512.setNoGravity(true);
            entityPlayer.setPosition(entityPlayer2.posX, entityPlayer2.posY + 2.0, entityPlayer2.posZ);
            entityPlayer.lastTickPosX = entityPlayer2.lastTickPosX;
            entityPlayer.lastTickPosY = entityPlayer2.lastTickPosY + 2.0;
            entityPlayer.lastTickPosZ = entityPlayer2.lastTickPosZ;
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(RenderWorldLastEvent renderWorldLastEvent) {
            Minecraft minecraft = Minecraft.getMinecraft();
            RenderManager renderManager = minecraft.getRenderManager();
            EntityPlayerSP entityPlayerSP = minecraft.player;
            if (minecraft.player == null) {
                return;
            }
            Vec3d vec3d = entityPlayerSP.getPositionVector();
            for (EntityPlayer entityPlayer : this.a) {
                Vec3d vec3d2 = entityPlayer.getPositionVector();
                Vec3d vec3d3 = vec3d2.subtract(vec3d);
                renderManager.renderEntity(entityPlayer, vec3d3.x, vec3d3.y, vec3d3.z, 69.0f, renderWorldLastEvent.getPartialTicks(), true);
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void b(TickEvent.RenderTickEvent renderTickEvent) {
            if (renderTickEvent.phase == TickEvent.Phase.START) {
                this.b();
            } else {
                this.a();
            }
        }

        @SideOnly(value=Side.CLIENT)
        void a() {
            for (EntityPlayer entityPlayer : this.a) {
                entityPlayer.isDead = true;
            }
        }

        @SideOnly(value=Side.CLIENT)
        void b() {
            this.a.clear();
            Minecraft minecraft = Minecraft.getMinecraft();
            EntityPlayerSP entityPlayerSP = minecraft.player;
            if (minecraft.world == null) {
                return;
            }
            for (EntityPlayer entityPlayer : minecraft.world.playerEntities) {
                PlayerGoblin eq_class2642;
                PlayerGirl ei_class2512;
                if (entityPlayer == entityPlayerSP || !((ei_class2512 = PlayerGirl.GetPlayer(entityPlayer)) instanceof PlayerGoblin) || (eq_class2642 = (PlayerGoblin)ei_class2512).java_util_UUID_e() == null) continue;
                Action fp_class3242 = eq_class2642.currentAction();
                if (fp_class3242 == Action.THROWN || fp_class3242 == Action.START_THROWING) {
                    return;
                }
                this.a.add(entityPlayer);
                entityPlayer.isDead = false;
            }
        }

        @SubscribeEvent
        public void a(PlayerInteractEvent.EntityInteract entityInteract) {
            EntityPlayer entityPlayer = entityInteract.getEntityPlayer();
            if (!entityPlayer.isSneaking()) {
                return;
            }
            if (!(entityInteract.getTarget() instanceof EntityPlayer)) {
                return;
            }
            PlayerGirl ei_class2512 = PlayerGirl.d_(entityInteract.getTarget().getPersistentID());
            if (!(ei_class2512 instanceof PlayerGoblin)) {
                return;
            }
            PlayerGirl ei_class2513 = PlayerGirl.d_(entityPlayer.getPersistentID());
            if (ei_class2513 != null) {
                return;
            }
            ((PlayerGoblin)ei_class2512).void_c(entityInteract.getEntityPlayer());
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

