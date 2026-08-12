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
package com.trolmastercard.sexmod.girls.Goblin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.ResetGirl;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.base.AbstractGoblinKoboldEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.ew_class277;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.world.FakeWorld;
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

public class PlayerGoblin extends ew_class277
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
    public float getNameTagHeightOffset() {
        return 0.9f;
    }

    @Override
    public IRenderer getHandRenderer(int n) {
        return new KoboldHand();
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
    public void onGuiActionSelected(String actionName, UUID partnerUUID) {
        if ("anal".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.NELSON_INTRO);
            this.initActionState(this.getOutfitIndex(), Action.NELSON_INTRO);
            this.setOutfitIndex(0);
        }
        if ("paizuri".equals(actionName)) {
            this.bindPlayerPartner(partnerUUID);
            this.setCurrentAction(Action.PAIZURI_START);
            this.initActionState(this.getOutfitIndex(), Action.PAIZURI_START);
            this.setOutfitIndex(0);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean openGuiForPlayer(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "paizuri"}, null, false));
        return true;
    }

    @Override
    public EntityPlayer getPlayerEntity(EntityPlayer entityPlayer) {
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
    public boolean isInteractable() {
        return this.java_util_UUID_e() == null || !Minecraft.getMinecraft().player.getPersistentID().equals(this.getOwnerUserUUID());
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
        return Reference.LerpVec3d(vec3d3, vec3d2, (double)f);
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
        EntityPlayer entityPlayer2 = this.getOwnerPlayerEntity();
        if (entityPlayer2 == null) {
            return;
        }
        entityPlayer2.setNoGravity(true);
        entityPlayer2.noClip = true;
        if (this.world.isRemote) {
            return;
        }
        PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)entityPlayer2);
    }

    @Override
    protected String a(StringBuilder stringBuilder) {
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, 3);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, 2);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, 2);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, 7);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, 7);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, 5);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, g5_class349.values().length - 1);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, by_class106.values().length - 1);
        AbstractGoblinKoboldEntity.appendRandomGeneInclusive(stringBuilder, eh_class250.values().length - 1);
        AbstractGoblinKoboldEntity.appendFixedGene(stringBuilder, 0);
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
    protected void ResetColors() {
        PlayerGoblinRenderer.ResetColors();
        GoblinRenderer.ResetColors();
    }

    @Override
    public float getEyeHeight() {
        return 0.75f;
    }

    @Override
    public boolean boolean_o() {
        return this.isAnchored() || this.java_util_UUID_e() != null;
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
        float f2 = Reference.LerpFloat(entityPlayer.prevRenderYawOffset, entityPlayer.renderYawOffset, f);
        Vec3d vec3d2 = vec3d;
        float f3 = 135.0f;
        Action fp_class3242 = this.currentAction();
        if (fp_class3242 == Action.PICK_UP) {
            vec3d2 = new Vec3d(vec3d.x, vec3d.y, -vec3d.z);
            f3 += 40.0f;
        } else if (fp_class3242 != Action.START_THROWING) {
            vec3d2 = vec3d2.subtract(0.0, 2.0, 0.0);
        }
        vec3d2 = VectorMath.rotate(vec3d2, f2 + f3);
        return vec3d2;
    }

    @SideOnly(value=Side.CLIENT)
    void void_f() {
        EntityPlayer entityPlayer = this.getOwnerPlayerEntity();
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
        EntityPlayer entityPlayer2 = this.getOwnerPlayerEntity();
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
        EntityPlayer entityPlayer = this.getOwnerPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        if (n == 15) {
            Vec3d vec3d = GoblinEntity.b(this);
            float f = GoblinEntity.d(this);
            float f2 = GoblinEntity.c(this);
            if (this.world.isRemote && this.boolean_f()) {
                HandlePlayerMovement.setMovementLock(true);
            }
            Vec3d vec3d2 = GoblinEntity.a(new Vec3d(0.0, 0.0, 1.5), f, f2);
            entityPlayer.motionX = vec3d2.x;
            entityPlayer.motionY = vec3d2.y;
            entityPlayer.motionZ = vec3d2.z;
            if (!this.world.isRemote) {
                this.setYawRotation(f2);
            }
        }
        entityPlayer.noClip = false;
        entityPlayer.setNoGravity(false);
        if (n == 39) {
            this.void_c(-1);
            this.setCurrentAction(Action.THROWN);
            this.setInteractionPlayerUUID((UUID)null);
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
        EntityPlayer entityPlayer = this.getOwnerPlayerEntity();
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
    public void ResetNPCTasks() {
        super.ResetNPCTasks();
        this.entityDataManager.set(aA, false);
        if (this.java_util_UUID_e() == null) {
            return;
        }
        this.void_a((UUID)null);
        EntityPlayer entityPlayer = this.getOwnerPlayerEntity();
        if (entityPlayer == null) {
            return;
        }
        PackageHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(true), (EntityPlayerMP)entityPlayer);
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
        if (!minecraft.player.getPersistentID().equals(this.playerSheHasSexWith())) {
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
            AbstractGoblinKoboldEntity.appendFixedGene(stringBuilder, n);
        }
        AbstractGoblinKoboldEntity.appendFixedGene(stringBuilder, 1);
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
    public void setCurrentAction(Action action) {
        Action fp_class3243 = this.currentAction();
        if (fp_class3243 == Action.PAIZURI_CUM && (action == Action.PAIZURI_SLOW || action == Action.PAIZURI_FAST)) {
            return;
        }
        if (fp_class3243 == Action.NELSON_CUM && (action == Action.NELSON_SLOW || action == Action.NELSON_FAST)) {
            return;
        }
        if (fp_class3243 == Action.BREEDING_CUM_0 && (action == Action.BREEDING_SLOW_0 || action == Action.BREEDING_FAST_0)) {
            return;
        }
        if (action == Action.PAIZURI_START && !this.world.isRemote) {
            this.void_m();
        }
        if (action == Action.NELSON_INTRO && !this.world.isRemote) {
            this.void_q();
        }
        if (action == Action.NELSON_CUM) {
            this.entityDataManager.set(aA, true);
        }
        if (fp_class3243 == Action.NELSON_CUM && action != Action.NELSON_CUM) {
            this.entityDataManager.set(aA, false);
        }
        super.setCurrentAction(action);
    }

    void void_q() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.playerSheHasSexWith());
        if (entityPlayer == null) {
            return;
        }
        this.setYawRotation(entityPlayer.rotationYaw);
        this.noClip = true;
        this.setNoGravity(true);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ - 1.0);
    }

    void void_m() {
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.playerSheHasSexWith());
        if (entityPlayer == null) {
            return;
        }
        this.setYawRotation(entityPlayer.rotationYaw + 180.0f);
        this.noClip = true;
        this.setNoGravity(true);
        entityPlayer.setNoGravity(true);
        entityPlayer.noClip = true;
        entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY - 0.5, entityPlayer.posZ - (double)0.6f);
        entityPlayer.rotationPitch = 70.0f;
        entityPlayer.prevRotationPitch = 70.0f;
    }

    @Override
    public boolean canInteract() {
        return this.java_util_UUID_e() == null;
    }

    @Override
    public void detachPartner(EntityPlayer entityPlayer) {
        if (!entityPlayer.getPersistentID().equals(this.java_util_UUID_e())) {
            return;
        }
        ResetGirl.a_inner422.a(this);
        this.setAnchored(false);
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
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        block5 : switch (event.getController().getName()) {
            case "eyes": {
                if (this.currentAction() != Action.NULL || !this.currentAction().autoBlink) {
                    this.createAnimation("animation.goblin.null", true, event);
                    break;
                }
                this.createAnimation("animation.goblin.blink", true, event);
                break;
            }
            case "movement": {
                if (this.currentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.goblin.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    boolean bl = this.aC = !this.aC;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.goblin.fly" + (this.aC ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.ao.x) + Math.abs(this.ao.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.goblin.running", true, event);
                        break;
                    }
                    if (this.ao.y >= -0.1f) {
                        this.movementController.setAnimationSpeed(2.0);
                        this.createAnimation("animation.goblin.walk", true, event);
                        break;
                    }
                    this.movementController.setAnimationSpeed(1.5);
                    this.createAnimation("animation.goblin.backwards_walk", true, event);
                    break;
                }
                this.createAnimation("animation.goblin.idle", true, event);
                break;
            }
            case "action": {
                Minecraft minecraft = Minecraft.getMinecraft();
                String string = minecraft.player.getPersistentID().equals(this.java_util_UUID_e()) && minecraft.gameSettings.thirdPersonView == 0 ? "1" : "3";
                switch (this.currentAction()) {
                    case SHOULDER_IDLE: {
                        this.createAnimation("animation.goblin.shoulder_idle", true, event);
                        break block5;
                    }
                    case PICK_UP: {
                        this.createAnimation(String.format("animation.goblin.pick_up_%sperson", string), true, event);
                        break block5;
                    }
                    case START_THROWING: {
                        this.createAnimation(String.format("animation.goblin.throw_%sperson", string), true, event);
                        break block5;
                    }
                    case THROWN: {
                        this.createAnimation("animation.goblin.thrown", true, event);
                        break block5;
                    }
                    case NULL: {
                        this.createAnimation("animation.goblin.null", true, event);
                        break block5;
                    }
                    case STAND_UP: {
                        this.createAnimation("animation.goblin.stand_up", false, event);
                        break block5;
                    }
                    case STRIP: {
                        this.createAnimation("animation.goblin.strip", false, event);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.goblin.attack" + this.nextAttack, false, event);
                        break block5;
                    }
                    case BOW: {
                        this.createAnimation("animation.goblin.bowcharge", false, event);
                        break block5;
                    }
                    case SIT: {
                        this.createAnimation("animation.goblin.sit", true, event);
                        break block5;
                    }
                    case NELSON_INTRO: {
                        this.createAnimation("animation.goblin.nelson_intro", true, event);
                        break block5;
                    }
                    case NELSON_SLOW: {
                        this.createAnimation("animation.goblin.nelson_slow" + (this.ay ? "" : "2"), true, event);
                        break block5;
                    }
                    case NELSON_FAST: {
                        this.createAnimation("animation.goblin.nelson_fast" + (this.aF ? "c" : "s"), true, event);
                        break block5;
                    }
                    case NELSON_CUM: {
                        this.createAnimation("animation.goblin.nelson_cum", true, event);
                        break block5;
                    }
                    case BREEDING_INTRO_0: {
                        this.createAnimation("animation.goblin.breeding_intro_1", true, event);
                        break block5;
                    }
                    case BREEDING_INTRO_1: {
                        this.createAnimation("animation.goblin.breeding_intro_2", true, event);
                        break block5;
                    }
                    case BREEDING_INTRO_2: {
                        this.createAnimation("animation.goblin.breeding_intro_3", true, event);
                        break block5;
                    }
                    case BREEDING_SLOW_0: {
                        this.createAnimation("animation.goblin.breeding_slow_1" + (this.aB ? "l" : "r"), true, event);
                        break block5;
                    }
                    case BREEDING_SLOW_2: {
                        this.createAnimation("animation.goblin.breeding_slow_3", true, event);
                        break block5;
                    }
                    case BREEDING_FAST_0: {
                        this.createAnimation("animation.goblin.breeding_fast_1" + (this.aH ? "c" : "s"), true, event);
                        break block5;
                    }
                    case BREEDING_FAST_2: {
                        this.createAnimation("animation.goblin.breeding_fast_3", true, event);
                        break block5;
                    }
                    case BREEDING_CUM_0: {
                        this.createAnimation("animation.goblin.breeding_cum_1", true, event);
                        break block5;
                    }
                    case BREEDING_CUM_1: {
                        this.createAnimation("animation.goblin.breeding_cum_2", true, event);
                        break block5;
                    }
                    case BREEDING_CUM_2: {
                        this.createAnimation("animation.goblin.breeding_cum_3", true, event);
                        break block5;
                    }
                    case BREEDING_1: {
                        this.createAnimation("animation.goblin.breeding_2", true, event);
                        break block5;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.goblin.paizuri_start", true, event);
                        break block5;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.goblin.paizuri_slow" + this.aD, true, event);
                        break block5;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.goblin.paizuri_fast", true, event);
                        break block5;
                    }
                    case PAIZURI_FAST_CONTINUES: {
                        this.createAnimation("animation.goblin.paizuri_fast_countinues", true, event);
                        break block5;
                    }
                    case PAIZURI_IDLE: {
                        this.createAnimation("animation.goblin.paizuri_idle", true, event);
                        break block5;
                    }
                    case PAIZURI_CUM: {
                        this.createAnimation("animation.goblin.paizuri_cum", true, event);
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
                case "attackDone": {
                    if (++this.nextAttack != 3) break;
                    this.nextAttack = 0;
                    break;
                }
                case "catchEh": {
                    this.sendLocalClientMessage("ehh..");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchAkward": {
                    this.sendLocalClientMessage("awkward..");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchWell": {
                    this.sendLocalClientMessage("well...");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchRather": {
                    this.sendLocalClientMessage("would you rather have this stupid... thing?");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchMe": {
                    this.sendLocalClientMessage("...or use me?~");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchDone": {
                    if (!"bj".equals(this.entityDataManager.get(GIRL_HAND_STATES))) break;
                    this.setCurrentAction(Action.CATCH_BJ);
                    break;
                }
                case "catchBjDone": {
                    this.setCurrentAction(Action.CATCH_BJ_IDLE);
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    PlayerGoblin.openInventoryGui(entityPlayerSP, this, new String[]{"use her", "take ur stuff back"}, null, false);
                    break;
                }
                case "paizuriChoice": {
                    this.sendLocalClientMessage("good choice!~");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriBoth": {
                    this.sendLocalClientMessage("...for both of us!");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizruiUse": {
                    this.sendLocalClientMessage("now use me like a fuck toy!~");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriSwitch": {
                    if (this.getRNG().nextBoolean()) break;
                    this.aD = "".equals(this.aD) ? "2" : "";
                    break;
                }
                case "touch": {
                    this.PlaySound(SoundsHandler.MISC_TOUCH, 3.0f);
                    break;
                }
                case "pound": {
                    this.playSoundAroundHer(SoundsHandler.MISC_POUNDING);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "paizuri_startDone": {
                    this.setCurrentAction(Action.PAIZURI_IDLE);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.init();
                    break;
                }
                case "paizuriFastDone": {
                    this.setCurrentAction(Action.PAIZURI_SLOW);
                    break;
                }
                case "paizuriFastReady": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.PAIZURI_FAST_CONTINUES);
                    break;
                }
                case "paizuriFastContinuesReady": 
                case "neslon_fastBackSwitch": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "smallPound": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, 0.25f);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "paizruiCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    entityPlayerSP.rotationPitch = 70.0f;
                    entityPlayerSP.prevRotationPitch = 70.0f;
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "cumSound": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "jumpCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.player.rotationYaw = this.getYawRotation().floatValue() + 170.0f;
                    minecraft.player.rotationPitch = -20.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 2;
                    break;
                }
                case "breedingHmm": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.player.rotationYaw = this.getYawRotation().floatValue() + 180.0f;
                        minecraft.player.rotationPitch = -15.0f;
                        minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                        minecraft.gameSettings.thirdPersonView = 0;
                    }
                    this.sendLocalClientMessage("hmm...");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingFound": {
                    this.sendLocalClientMessage("guess we found a worthy breeding partner!");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingEnough": {
                    this.sendLocalClientMessage("Eh.. go pin him down, before he runs off!");
                    this.playSoundAroundHer(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingCam2": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.gameSettings.thirdPersonView = 2;
                        minecraft.player.rotationYaw = this.getYawRotation().floatValue() - 120.0f;
                        minecraft.player.rotationPitch = -30.0f;
                    }
                }
                case "breedingIntroDone": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.init();
                    break;
                }
                case "breeding_slow1Done": {
                    if (this.getRNG().nextBoolean()) {
                        boolean bl = this.aB = !this.aB;
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_FAST_0);
                    this.aH = false;
                    break;
                }
                case "breeding_fast1Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.aH = false;
                    break;
                }
                case "breeding_fast1Ready": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.aH = true;
                    this.resetAnimationControllerOffset();
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
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
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_SLOW_2);
                    break;
                }
                case "breeding_intro_2Done": {
                    this.setCurrentAction(Action.BREEDING_1);
                    break;
                }
                case "breeding_cumCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.gameSettings.thirdPersonView = 0;
                    minecraft.player.rotationYaw = this.getYawRotation().floatValue() + 180.0f;
                    minecraft.player.rotationPitch = -15.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 0;
                    break;
                }
                case "neslon_introDone": {
                    this.setCurrentAction(Action.NELSON_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.init();
                    break;
                }
                case "nelson_slowDone": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.ay = !this.ay;
                    break;
                }
                case "neslon_fastSwitch": {
                    if (!this.isControlledByLocalPlayer()) {
                        this.aF = true;
                        return;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.aF = true;
                    break;
                }
                case "nelsonFastDone": {
                    this.aF = false;
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.NELSON_SLOW);
                    break;
                }
                case "paizuriCumDone": 
                case "nelson_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    this.setCurrentAction(Action.NULL);
                }
            }
        };
        this.actionController.registerSoundListener(iSoundListener);
        this.movementController.transitionLengthTicks = 2.0;
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class EventHandler {
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
            PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(entityInteract.getTarget().getPersistentID());
            if (!(ei_class2512 instanceof PlayerGoblin)) {
                return;
            }
            PlayerGirl ei_class2513 = PlayerGirl.getUUIDHashtable(entityPlayer.getPersistentID());
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

