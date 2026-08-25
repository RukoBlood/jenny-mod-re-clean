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

import com.trolmastercard.sexmod.Packets.ResetGirl;
import com.trolmastercard.sexmod.Packets.SetPlayerMovement;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.Kobold.KoboldHand;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.WorkerPlayerEntity;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IGoblin;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import com.trolmastercard.sexmod.util.RotationHelper;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlayerGoblin extends WorkerPlayerEntity implements IGoblin {
    final static public float THROW_HEIGHT_MODIFIER = 2.0f;
    final static public DataParameter<String> OWNER_UUID = EntityDataManager.createKey(PlayerGoblin.class, DataSerializers.STRING).getSerializer().createKey(122);
    final static public DataParameter<Boolean> IS_CUMMING = EntityDataManager.createKey(PlayerGoblin.class, DataSerializers.BOOLEAN).getSerializer().createKey(126);
    int standUpTicks = 0;
    int throwProgress = -1;
    int throwTicksOnGround = 0;
    Action previousAction = Action.NULL;
    int heldPlayerDistance = -1;
    boolean alternateFlyAnim = false;
    boolean breedingSlowAlternate = true;
    boolean nelsonSlowAlternate = true;
    boolean nelsonFastState = false;
    boolean breedingFastState = false;
    String paizuriAnimSuffix = "";

    public PlayerGoblin(World world) {
        super(world);
    }

    public PlayerGoblin(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    public float getScaleFactor() {
        return 0.9f;
    }

    @Override
    public IRenderer getHandModelRenderer(int index) {
        return new KoboldHand();
    }

    @Override
    public String getHandTexture(int index) {
        return "textures/entity/kobold/hand.png";
    }

    @Override
    public Vec3i getHandColor(int index) {
        String[] parts = PlayerGoblin.getModelCodeParts(this);
        if (parts.length < 8) {
            return super.getHandColor(index);
        }
        return SkinColor.values()[Integer.parseInt(parts[7])].getColor();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        EyeColor color = EyeColor.values()[this.getRNG().nextInt(EyeColor.values().length)];
        this.entityDataManager.register(WORK_POS, new BlockPos(color.getColor()));
        this.entityDataManager.register(MODEL_CODE, GoblinEntity.DEFAULT_COLOR.name());
        this.entityDataManager.register(IS_CUMMING, false);
        this.entityDataManager.register(OWNER_UUID, "");
    }

    @Override
    public void handleOwnerCommand(String command, UUID partnerUUID) {
        if ("anal".equals(command)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.NELSON_INTRO);
            this.sendActionPacket(this.getOutfitIndex(), Action.NELSON_INTRO);
            this.setOutfitIndex(0);
        }
        if ("paizuri".equals(command)) {
            this.teleportPlayerToGirl(partnerUUID);
            this.setCurrentAction(Action.PAIZURI_START);
            this.sendActionPacket(this.getOutfitIndex(), Action.PAIZURI_START);
            this.setOutfitIndex(0);
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean openInteractionMenu(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "paizuri"}, null, false));
        return true;
    }

    @Override
    public EntityPlayer resolvePlayerEntity(EntityPlayer entityPlayer) {
        UUID uUID = this.getOwnerUUID();
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
        return this.getOwnerUUID() == null || !Minecraft.getMinecraft().player.getPersistentID().equals(this.getOwnerUserUUID());
    }

    @Override
    public boolean isRidingSomething() {
        UUID uUID = this.getOwnerUUID();
        return uUID == null;
    }

    @Override
    public Vec3d getOwnerLookVector(Vec3d vec, float partialTicks) {
        UUID uUID = this.getOwnerUUID();
        if (uUID == null) {
            return vec;
        }
        EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
        if (player == null) {
            return vec;
        }
        Vec3d pos = player.getPositionVector();
        Vec3d lastTickPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
        return RotationHelper.LerpVec3d(lastTickPos, pos, partialTicks);
    }

    void handlePlayerThrow(EntityPlayer player) {
        if (this.getCurrentAction() == Action.NULL) {
            if (this.getOwnerUUID() == null) {
                if (GoblinEntity.hasGoblinWithUUID(player.getPersistentID())) {
                    player.sendStatusMessage(new TextComponentString("you are already carrying a Goblin"), true);
                } else {
                    this.setOwnerUUID(player.getPersistentID());
                    this.setCurrentAction(Action.PICK_UP);
                    this.setHeldPlayerDistance(45);
                    EntityPlayer owner = this.getOwnerPlayer();
                    if (owner != null) {
                        owner.setNoGravity(true);
                        owner.noClip = true;
                        if (!this.world.isRemote) {
                            PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), (EntityPlayerMP) owner);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String buildModelCodeDNA(StringBuilder builder) {
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 3);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 2);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 2);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 7);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 7);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, 5);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, HairColor.values().length - 1);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, SkinColor.values().length - 1);
        AbstractNpcOnlyEntity.appendPaddedNumber(builder, EyeColor.values().length - 1);
        AbstractNpcOnlyEntity.appendPaddedNumberWithFixedValue(builder, 0);
        return builder.toString();
    }

    @Override
    public ArrayList<Integer> getCustomPartIdList() {
        return new ArrayList<Integer>(){
            {
                this.add(4);
                this.add(3);
                this.add(3);
                this.add(16);
                this.add(16);
                this.add(6);
                this.add(HairColor.values().length);
                this.add(SkinColor.values().length);
                this.add(EyeColor.values().length);
            }
        };
    }

    @Override
    public List<Integer> getCustomPartExtraIdList() {
        return Collections.singletonList(2);
    }

    @Override
    protected void ResetColors() {
        PlayerGoblinRenderer.ResetColors();
        GoblinRenderer.clearBoneColors();
    }

    @Override
    public float getEyeHeight() {
        return 0.75f;
    }

    @Override
    public boolean isSceneActive() {
        return this.isAnchored() || this.getOwnerUUID() != null;
    }

    @Override
    public boolean canPerformAction(Action action, EntityPlayer player) {
        float f;
        UUID uUID = this.getOwnerUUID();
        if (uUID == null) {
            return false;
        }
        EntityPlayer player1 = this.world.getPlayerEntityByUUID(uUID);
        if (player1 == null) {
            return false;
        }
        float yaw = player.rotationYaw;
        float f3 = action == Action.PICK_UP ? 180.0f : 0.0f;
        float f4 = player1.rotationYaw - 90.0f + f3;
        float f5 = player1.rotationYaw + 90.0f + f3;
        if (yaw < f4) {
            player.rotationYaw = f4;
        }
        if (yaw > f5) {
            player.rotationYaw = f5;
        }
        float f6 = player.rotationPitch;
        f = action == Action.PICK_UP ? 0.0f : 37.5f;
        if (f6 > f) {
            player.rotationPitch = f;
        }
        return true;
    }

    @Override
    public Vec3d getOwnerAimVector(Vec3d vec, float partialTicks) {
        UUID uUID = this.getOwnerUUID();
        if (uUID == null) {
            return vec;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return vec;
        }
        float f2 = RotationHelper.LerpFloat(entityPlayer.prevRenderYawOffset, entityPlayer.renderYawOffset, partialTicks);
        Vec3d vec3d2 = vec;
        float f3 = 135.0f;
        Action fp_class3242 = this.getCurrentAction();
        if (fp_class3242 == Action.PICK_UP) {
            vec3d2 = new Vec3d(vec.x, vec.y, -vec.z);
            f3 += 40.0f;
        } else if (fp_class3242 != Action.START_THROWING) {
            vec3d2 = vec3d2.subtract(0.0, 2.0, 0.0);
        }
        vec3d2 = VectorMath.rotateByYaw(vec3d2, f2 + f3);
        return vec3d2;
    }

    @SideOnly(value=Side.CLIENT)
    void handleOwnerThrow() {
        EntityPlayer entityPlayer = this.getOwnerPlayer();
        if (entityPlayer == null) {
            return;
        }
        if (this.getCurrentAction() == Action.START_THROWING) {
            entityPlayer.isDead = false;
            if (!this.world.loadedEntityList.contains(entityPlayer)) {
                this.world.spawnEntity(entityPlayer);
            }
        }
    }

    @Override
    public void onUpdate() {
        GoblinEntity.handleGoblinThrowAction(this);
        this.updatePlayerThrowProgress();
        this.handleThrowAction();
        super.onUpdate();
        if (this.world.isRemote) {
            this.handleOwnerThrow();
            Action action = this.getCurrentAction();
            this.handleLocalAction(action);
            this.handleNelsonAction(action);
            this.previousAction = action;
        }
    }

    @Override
    public boolean hasOwner() {
        return this.getOwnerUUID() != null;
    }

    void handleThrowAction() {
        Action action = this.getCurrentAction();
        if (action != Action.THROWN) {
            if (action != Action.START_THROWING || this.getThrowProgress() <= 15) {
                UUID uUID = this.getOwnerUUID();
                if (uUID != null) {
                    EntityPlayer player = this.world.getPlayerEntityByUUID(uUID);
                    if (player != null) {
                        EntityPlayer ownerPlayer = this.getOwnerPlayer();
                        if (ownerPlayer != null) {
                            ownerPlayer.noClip = true;
                            ownerPlayer.setNoGravity(true);
                            ownerPlayer.setPosition(player.posX, player.posY + 2.0, player.posZ);
                        }
                    }
                }
            }
        }
    }

    void updatePlayerThrowProgress() {
        PlayerGoblin goblin = this;
        int throwProgress = goblin.getThrowProgress();
        if (throwProgress != -1) {
            goblin.setThrowProgress(++throwProgress);
            EntityPlayer player = this.getOwnerPlayer();
            if (player != null) {
                if (throwProgress == 15) {
                    //Vec3d vec3d = GoblinEntity.getGoblinThrowPos(this);
                    float height = GoblinEntity.getGoblinThrowHeight(this);
                    float distance = GoblinEntity.getGoblinThrowDistance(this);
                    if (this.world.isRemote && this.hasOwnerUUID()) {
                        HandlePlayerMovement.setMovementLock(true);
                    }
                    Vec3d rot = GoblinEntity.rotateVectorPitchYaw(new Vec3d(0.0, 0.0, 1.5), height, distance);
                    player.motionX = rot.x;
                    player.motionY = rot.y;
                    player.motionZ = rot.z;
                    if (!this.world.isRemote) {
                        this.setYawRotation(distance);
                    }
                }
                player.noClip = false;
                player.setNoGravity(false);
                if (throwProgress == 39) {
                    this.setThrowProgress(-1);
                    this.setCurrentAction(Action.THROWN);
                    this.setInteractionPlayerUUID(null);
                    this.setOwnerUUID(null);
                }
            }
        }
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        GoblinEntity.handlePickUpState(this);
        this.handlePlayerThrown();
        this.handlePlayerStandUp();
    }

    void handlePlayerStandUp() {
        if (this.getCurrentAction() == Action.STAND_UP) {
            if (++this.standUpTicks >= 37) {
                this.standUpTicks = 0;
                this.setCurrentAction(Action.NULL);
            }
        }
    }

    void handlePlayerThrown() {
        if (this.getCurrentAction() == Action.THROWN) {
            EntityPlayer player = this.getOwnerPlayer();
            if (player != null) {
                if (player.onGround) {
                    int nextThrowTick = this.getThrowTickCount() + 1;
                    this.setThrowTickCount(nextThrowTick);
                    if (nextThrowTick >= 30) {
                        this.setThrowTickCount(0);
                        this.setCurrentAction(Action.STAND_UP);
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public UUID getOwnerUUID() {
        String string = this.entityDataManager.get(OWNER_UUID);
        if (string.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(this.entityDataManager.get(OWNER_UUID));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        if (uuid == null) {
            this.entityDataManager.set(OWNER_UUID, "");
            return;
        }
        this.entityDataManager.set(OWNER_UUID, uuid.toString());
    }

    public EntityPlayer gobGetOwnerPlayer() {
        UUID uUID = this.getOwnerUUID();
        return uUID == null ? null : this.world.getPlayerEntityByUUID(uUID);
    }

    @Override
    public void setThrowProgress(int progress) {
        this.throwProgress = progress;
    }

    @Override
    public int getThrowProgress() {
        return this.throwProgress;
    }

    @Override
    public void setThrowTickCount(int ticks) {
        this.throwTicksOnGround = ticks;
    }

    @Override
    public int getThrowTickCount() {
        return this.throwTicksOnGround;
    }

    @Override
    public void setPreviousAction(Action action) {
        this.previousAction = action;
    }

    @Override
    public Action getPreviousAction() {
        return this.previousAction;
    }

    @Override
    public void setHeldPlayerDistance(int distance) {
        this.heldPlayerDistance = distance;
    }

    @Override
    public int getHeldPlayerDistance() {
        return this.heldPlayerDistance;
    }

    @Override
    public void reInitTasks() {
        super.reInitTasks();
        this.entityDataManager.set(IS_CUMMING, false);
        if (this.getOwnerUUID() != null) {
            this.setOwnerUUID(null);
            EntityPlayer player = this.getOwnerPlayer();
            if (player != null) {
                PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(true), (EntityPlayerMP) player);
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    void handleNelsonAction(Action action) {
        if (action == Action.NELSON_FAST && this.previousAction != Action.NELSON_FAST) {
            this.nelsonFastState = false;
        }
    }

    @SideOnly(value=Side.CLIENT)
    void handleLocalAction(Action action) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.getPersistentID().equals(this.getInteractionPlayerUUID())) {
            if (mc.gameSettings.thirdPersonView == 0) {
                switch (action) {
                    case NELSON_CUM:
                    case NELSON_FAST:
                    case NELSON_INTRO:
                    case NELSON_SLOW: {
                        mc.gameSettings.thirdPersonView = 2;
                    }
                }
            }
        }
    }

    @Override
    public void setCustomPartList(List<Integer> parts) {
        StringBuilder builder = new StringBuilder();
        for (int part : parts) {
            AbstractNpcOnlyEntity.appendPaddedNumberWithFixedValue(builder, part);
        }
        AbstractNpcOnlyEntity.appendPaddedNumberWithFixedValue(builder, 1);
        this.entityDataManager.set(DNA_CODE, builder.toString());
    }

    @Override
    @Nullable
    protected Action getNextAction(Action action) {
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
        Action currentAction = this.getCurrentAction();
        if (currentAction != Action.PAIZURI_CUM || (action != Action.PAIZURI_SLOW && action != Action.PAIZURI_FAST)) {
            if (currentAction != Action.NELSON_CUM || (action != Action.NELSON_SLOW && action != Action.NELSON_FAST)) {
                if (currentAction != Action.BREEDING_CUM_0 || (action != Action.BREEDING_SLOW_0 && action != Action.BREEDING_FAST_0)) {
                    if (action == Action.PAIZURI_START && !this.world.isRemote) {
                        this.handlePlayerInteract();
                    }
                    if (action == Action.NELSON_INTRO && !this.world.isRemote) {
                        this.handlePlayerLook();
                    }
                    if (action == Action.NELSON_CUM) {
                        this.entityDataManager.set(IS_CUMMING, true);
                    }
                    if (currentAction == Action.NELSON_CUM && action != Action.NELSON_CUM) {
                        this.entityDataManager.set(IS_CUMMING, false);
                    }
                    super.setCurrentAction(action);
                }
            }
        }
    }

    void handlePlayerLook() {
        EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
        if (player != null) {
            this.setYawRotation(player.rotationYaw);
            this.noClip = true;
            this.setNoGravity(true);
            player.setNoGravity(true);
            player.noClip = true;
            player.setPositionAndUpdate(player.posX, player.posY, player.posZ - 1.0);
        }
    }

    void handlePlayerInteract() {
        EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
        if (player != null) {
            this.setYawRotation(player.rotationYaw + 180.0f);
            this.noClip = true;
            this.setNoGravity(true);
            player.setNoGravity(true);
            player.noClip = true;
            player.setPositionAndUpdate(player.posX, player.posY - 0.5, player.posZ - (double) 0.6f);
            player.rotationPitch = 70.0f;
            player.prevRotationPitch = 70.0f;
        }
    }

    @Override
    public boolean isPlayerGirl() {
        return this.getOwnerUUID() == null;
    }

    @Override
    public void onOwnerInteract(EntityPlayer entityPlayer) {
        if (!entityPlayer.getPersistentID().equals(this.getOwnerUUID())) {
            return;
        }
        ResetGirl.EventHandler.resetGirl(this);
        this.setAnchored(false);
        this.setCurrentAction(Action.NULL);
        this.setOwnerUUID(null);
    }

    @Override
    protected Action getCumAction(Action action) {
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
        switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL || !this.getCurrentAction().autoBlink) {
                    this.createAnimation("animation.goblin.null", true, event);
                    break;
                }
                this.createAnimation("animation.goblin.blink", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.goblin.null", true, event);
                    break;
                }
                if (this.isPlayerRiding) {
                    this.createAnimation("animation.goblin.sit", true, event);
                    break;
                }
                if (this.movementController.getCurrentAnimation() != null && this.movementController.getCurrentAnimation().animationName.contains("fly") && this.isPlayerOnGround) {
                    this.alternateFlyAnim = !this.alternateFlyAnim;
                }
                if (!this.isPlayerOnGround) {
                    this.createAnimation("animation.goblin.fly" + (this.alternateFlyAnim ? "2" : ""), true, event);
                    break;
                }
                if (Math.abs(this.moveInputVector.x) + Math.abs(this.moveInputVector.y) > 0.0f) {
                    if (this.isPlayerSprinting) {
                        this.movementController.setAnimationSpeed(1.2f);
                        this.createAnimation("animation.goblin.running", true, event);
                        break;
                    }
                    if (this.moveInputVector.y >= -0.1f) {
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
                String string = minecraft.player.getPersistentID().equals(this.getOwnerUUID()) && minecraft.gameSettings.thirdPersonView == 0 ? "1" : "3";
                switch (this.getCurrentAction()) {
                    case SHOULDER_IDLE: {
                        this.createAnimation("animation.goblin.shoulder_idle", true, event);
                        break;
                    }
                    case PICK_UP: {
                        this.createAnimation(String.format("animation.goblin.pick_up_%sperson", string), true, event);
                        break;
                    }
                    case START_THROWING: {
                        this.createAnimation(String.format("animation.goblin.throw_%sperson", string), true, event);
                        break;
                    }
                    case THROWN: {
                        this.createAnimation("animation.goblin.thrown", true, event);
                        break;
                    }
                    case NULL: {
                        this.createAnimation("animation.goblin.null", true, event);
                        break;
                    }
                    case STAND_UP: {
                        this.createAnimation("animation.goblin.stand_up", false, event);
                        break;
                    }
                    case STRIP: {
                        this.createAnimation("animation.goblin.strip", false, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.goblin.attack" + this.nextAttack, false, event);
                        break;
                    }
                    case BOW: {
                        this.createAnimation("animation.goblin.bowcharge", false, event);
                        break;
                    }
                    case SIT: {
                        this.createAnimation("animation.goblin.sit", true, event);
                        break;
                    }
                    case NELSON_INTRO: {
                        this.createAnimation("animation.goblin.nelson_intro", true, event);
                        break;
                    }
                    case NELSON_SLOW: {
                        this.createAnimation("animation.goblin.nelson_slow" + (this.nelsonSlowAlternate ? "" : "2"), true, event);
                        break;
                    }
                    case NELSON_FAST: {
                        this.createAnimation("animation.goblin.nelson_fast" + (this.nelsonFastState ? "c" : "s"), true, event);
                        break;
                    }
                    case NELSON_CUM: {
                        this.createAnimation("animation.goblin.nelson_cum", true, event);
                        break;
                    }
                    case BREEDING_INTRO_0: {
                        this.createAnimation("animation.goblin.breeding_intro_1", true, event);
                        break;
                    }
                    case BREEDING_INTRO_1: {
                        this.createAnimation("animation.goblin.breeding_intro_2", true, event);
                        break;
                    }
                    case BREEDING_INTRO_2: {
                        this.createAnimation("animation.goblin.breeding_intro_3", true, event);
                        break;
                    }
                    case BREEDING_SLOW_0: {
                        this.createAnimation("animation.goblin.breeding_slow_1" + (this.breedingSlowAlternate ? "l" : "r"), true, event);
                        break;
                    }
                    case BREEDING_SLOW_2: {
                        this.createAnimation("animation.goblin.breeding_slow_3", true, event);
                        break;
                    }
                    case BREEDING_FAST_0: {
                        this.createAnimation("animation.goblin.breeding_fast_1" + (this.breedingFastState ? "c" : "s"), true, event);
                        break;
                    }
                    case BREEDING_FAST_2: {
                        this.createAnimation("animation.goblin.breeding_fast_3", true, event);
                        break;
                    }
                    case BREEDING_CUM_0: {
                        this.createAnimation("animation.goblin.breeding_cum_1", true, event);
                        break;
                    }
                    case BREEDING_CUM_1: {
                        this.createAnimation("animation.goblin.breeding_cum_2", true, event);
                        break;
                    }
                    case BREEDING_CUM_2: {
                        this.createAnimation("animation.goblin.breeding_cum_3", true, event);
                        break;
                    }
                    case BREEDING_1: {
                        this.createAnimation("animation.goblin.breeding_2", true, event);
                        break;
                    }
                    case PAIZURI_START: {
                        this.createAnimation("animation.goblin.paizuri_start", true, event);
                        break;
                    }
                    case PAIZURI_SLOW: {
                        this.createAnimation("animation.goblin.paizuri_slow" + this.paizuriAnimSuffix, true, event);
                        break;
                    }
                    case PAIZURI_FAST: {
                        this.createAnimation("animation.goblin.paizuri_fast", true, event);
                        break;
                    }
                    case PAIZURI_FAST_CONTINUES: {
                        this.createAnimation("animation.goblin.paizuri_fast_countinues", true, event);
                        break;
                    }
                    case PAIZURI_IDLE: {
                        this.createAnimation("animation.goblin.paizuri_idle", true, event);
                        break;
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
                    this.sendChatMessage("ehh..");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchAkward": {
                    this.sendChatMessage("awkward..");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchWell": {
                    this.sendChatMessage("well...");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchRather": {
                    this.sendChatMessage("would you rather have this stupid... thing?");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "catchMe": {
                    this.sendChatMessage("...or use me?~");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
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
                    this.sendChatMessage("good choice!~");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriBoth": {
                    this.sendChatMessage("...for both of us!");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizruiUse": {
                    this.sendChatMessage("now use me like a fuck toy!~");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "paizuriSwitch": {
                    if (this.getRNG().nextBoolean()) break;
                    this.paizuriAnimSuffix = "".equals(this.paizuriAnimSuffix) ? "2" : "";
                    break;
                }
                case "touch": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_TOUCH, 3.0f);
                    break;
                }
                case "pound": {
                    this.playRandomSound(SoundsHandler.MISC_POUNDING);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "paizuri_startDone": {
                    this.setCurrentAction(Action.PAIZURI_IDLE);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
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
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_POUNDING, 0.25f);
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
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "jumpCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    Minecraft minecraft = Minecraft.getMinecraft();
                    minecraft.player.rotationYaw = this.getYawRotation() + 170.0f;
                    minecraft.player.rotationPitch = -20.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 2;
                    break;
                }
                case "breedingHmm": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.player.rotationYaw = this.getYawRotation() + 180.0f;
                        minecraft.player.rotationPitch = -15.0f;
                        minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                        minecraft.gameSettings.thirdPersonView = 0;
                    }
                    this.sendChatMessage("hmm...");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingFound": {
                    this.sendChatMessage("guess we found a worthy breeding partner!");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingEnough": {
                    this.sendChatMessage("Eh.. go pin him down, before he runs off!");
                    this.playRandomSound(SoundsHandler.MISC_PLOB);
                    break;
                }
                case "breedingCam2": {
                    if (this.isControlledByLocalPlayer()) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        minecraft.gameSettings.thirdPersonView = 2;
                        minecraft.player.rotationYaw = this.getYawRotation() - 120.0f;
                        minecraft.player.rotationPitch = -30.0f;
                    }
                }
                case "breedingIntroDone": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "breeding_slow1Done": {
                    if (this.getRNG().nextBoolean()) {
                        this.breedingSlowAlternate = !this.breedingSlowAlternate;
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.BREEDING_FAST_0);
                    this.breedingFastState = false;
                    break;
                }
                case "breeding_fast1Done": {
                    this.setCurrentAction(Action.BREEDING_SLOW_0);
                    if (!this.isControlledByLocalPlayer()) break;
                    this.breedingFastState = false;
                    break;
                }
                case "breeding_fast1Ready": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.breedingFastState = true;
                    this.resetAnimationControllerOffset();
                    this.actionController.tickOffset = 0.0;
                    break;
                }
                case "cum": {
                    this.playRandomSoundAtVolume(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
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
                    minecraft.player.rotationYaw = this.getYawRotation() + 180.0f;
                    minecraft.player.rotationPitch = -15.0f;
                    minecraft.player.rotationYawHead = minecraft.player.rotationYaw;
                    minecraft.gameSettings.thirdPersonView = 0;
                    break;
                }
                case "neslon_introDone": {
                    this.setCurrentAction(Action.NELSON_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "nelson_slowDone": {
                    if (!this.getRNG().nextBoolean()) break;
                    this.nelsonSlowAlternate = !this.nelsonSlowAlternate;
                    break;
                }
                case "neslon_fastSwitch": {
                    if (!this.isControlledByLocalPlayer()) {
                        this.nelsonFastState = true;
                        return;
                    }
                    if (!HandlePlayerMovement.isThrusting) break;
                    this.nelsonFastState = true;
                    break;
                }
                case "nelsonFastDone": {
                    this.nelsonFastState = false;
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

    public static class EventHandler {
        HashSet<EntityPlayer> players = new HashSet();

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onRenderHand(RenderHandEvent event) {
            PlayerGirl playerGirl = PlayerGirl.GetPlayer(Minecraft.getMinecraft().player);
            if (playerGirl != null) {
                if (playerGirl instanceof IGoblin) {
                    if (((IGoblin) playerGirl).getOwnerUUID() != null) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            EntityPlayer player = event.player;
            if (player != null) {
                this.handlePlayerOwner(player);
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onPlayerTickSync(TickEvent.RenderTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player != null) {
                    this.handlePlayerOwner(player);
                }
            }
        }

        void handlePlayerOwner(EntityPlayer player) {
            PlayerGirl playerGirl = PlayerGirl.GetPlayer(player);
            if (playerGirl instanceof PlayerGoblin) {
                Action action = playerGirl.getCurrentAction();
                if (action != Action.THROWN) {
                    if (action != Action.START_THROWING || ((IGoblin) playerGirl).getThrowProgress() <= 15) {
                        UUID uUID = ((PlayerGoblin) playerGirl).getOwnerUUID();
                        if (uUID != null) {
                            EntityPlayer owner = player.world.getPlayerEntityByUUID(uUID);
                            if (owner != null) {
                                player.noClip = true;
                                player.setNoGravity(true);
                                playerGirl.noClip = true;
                                playerGirl.setNoGravity(true);
                                player.setPosition(owner.posX, owner.posY + 2.0, owner.posZ);
                                player.lastTickPosX = owner.lastTickPosX;
                                player.lastTickPosY = owner.lastTickPosY + 2.0;
                                player.lastTickPosZ = owner.lastTickPosZ;
                            }
                        }
                    }
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderManager renderManager = mc.getRenderManager();
            EntityPlayerSP player = mc.player;
            if (mc.player != null) {
                Vec3d playerPos = player.getPositionVector();
                for (EntityPlayer owner : this.players) {
                    Vec3d pos = owner.getPositionVector();
                    Vec3d targetPos = pos.subtract(playerPos);
                    renderManager.renderEntity(owner, targetPos.x, targetPos.y, targetPos.z, 69.0f, event.getPartialTicks(), true);
                }
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onRenderTick(TickEvent.RenderTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                this.clearFakePlayers();
            } else {
                this.killFakePlayers();
            }
        }

        @SideOnly(value=Side.CLIENT)
        void killFakePlayers() {
            for (EntityPlayer player : this.players) {
                player.isDead = true;
            }
        }

        @SideOnly(value=Side.CLIENT)
        void clearFakePlayers() {
            this.players.clear();
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;
            if (mc.world != null) {
                for (EntityPlayer owner : mc.world.playerEntities) {
                    PlayerGoblin goblin;
                    PlayerGirl playerGirl;
                    if (owner != player && (playerGirl = PlayerGirl.GetPlayer(owner)) instanceof PlayerGoblin && (goblin = (PlayerGoblin) playerGirl).getOwnerUUID() != null) {
                        Action action = goblin.getCurrentAction();
                        if (action != Action.THROWN && action != Action.START_THROWING) {
                            this.players.add(owner);
                            owner.isDead = false;
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.isSneaking()) {
                if (event.getTarget() instanceof EntityPlayer) {
                    PlayerGirl target = PlayerGirl.getUUIDHashtable(event.getTarget().getPersistentID());
                    if (target instanceof PlayerGoblin) {
                        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(player.getPersistentID());
                        if (playerGirl == null) {
                            ((PlayerGoblin) target).handlePlayerThrow(event.getEntityPlayer());
                        }
                    }
                }
            }
        }
    }
}

