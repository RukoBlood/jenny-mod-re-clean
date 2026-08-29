/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector2f
 *  net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.gameevent.TickEvent$RenderTickEvent
 */
package com.trolmastercard.sexmod.gender_change;

import javax.vecmath.Vector2f;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//e_ -> RenderPlayerGirl
public class RenderPlayerGirl {
    final static public float DONT_RENDER_WITH_THIS_PARTIALTICK = 1.2345679f;
    Vec3d savedPlayerPos = null;
    Vec3d savedPlayerLastTickPos = null;
    PlayerGirl lastRenderedPlayerGirl = null;
    boolean hasCustomEyeHeight = false;

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void renderPlayerGirl(RenderPlayerEvent.Pre event) {
        if (event.getPartialRenderTick() == DONT_RENDER_WITH_THIS_PARTIALTICK) {
            return;
        }
        PlayerGirl.rebuildPlayerGirlTableFromWorld();
        PlayerGirl girl = PlayerGirl.getUUIDHashtable(event.getEntityPlayer().getPersistentID());
        if (girl != null) {
            event.setCanceled(true);
            RenderPlayerGirl.renderPlayerAsGirl(girl, event.getEntityPlayer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick());
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void renderPlayerAsGirl(PlayerGirl playerGirl, EntityPlayer player, double d, double d2, double d3, float f) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(player = playerGirl.resolvePlayerEntity(player)).isInvisibleToPlayer(mc.player) || playerGirl.hasOwner()) {
            RenderManager renderManager = mc.getRenderManager();
            playerGirl.rotationYaw = player.rotationYaw;
            playerGirl.prevRotationYawHead = player.prevRotationYawHead;
            playerGirl.rotationYawHead = player.rotationYawHead;
            playerGirl.prevRotationPitch = player.prevRotationPitch;
            playerGirl.rotationPitch = player.rotationPitch;
            playerGirl.prevRotationYaw = player.prevRotationYaw;
            playerGirl.prevPosX = player.prevPosX;
            playerGirl.prevPosY = player.prevPosY;
            playerGirl.prevPosZ = player.prevPosZ;
            playerGirl.lastTickPosX = player.lastTickPosX;
            playerGirl.lastTickPosY = player.lastTickPosY;
            playerGirl.lastTickPosZ = player.lastTickPosZ;
            playerGirl.renderYawOffset = player.renderYawOffset;
            playerGirl.prevRenderYawOffset = player.prevRenderYawOffset;
            playerGirl.isPlayerSneaking = player.isSneaking();
            playerGirl.isPlayerSprinting = player.isSprinting();
            playerGirl.isPlayerRiding = player.isRiding();
            playerGirl.isPlayerOnGround = player.onGround;
            playerGirl.isUsingItem = player.getItemInUseCount() != 0;
            double delta = player.lastTickPosX - player.posX;
            double deltaZ = player.posZ - player.lastTickPosZ;
            double yaw = Math.PI / 180 * (double) player.rotationYaw;
            playerGirl.moveInputVector = new Vector2f((float) (delta * Math.cos(yaw) + deltaZ * Math.sin(yaw)), (float) (delta * Math.sin(yaw) + deltaZ * Math.cos(yaw)));
            float f2 = playerGirl.isRidingSomething() ? RenderPlayerGirl.manageActions(playerGirl, player) : 0.0f;
            PlayerGirlRenderer.forceRenderNextFrame = true;
            renderManager.renderEntity(playerGirl, d, d2 + (double) f2, d3, 90.0f, f, false);
        }
    }

    static float manageActions(PlayerGirl girl, EntityPlayer player) {
        if (girl.getDataManager().get(GirlEntity.IS_ANCHORED)) {
            return 0.0f;
        }
        if ((player.getHeldItemMainhand().getItem() instanceof ItemBow || player.getHeldItemOffhand().getItem() instanceof ItemBow) && girl.isUsingItem) {
            girl.setCurrentAction(Action.BOW);
        }
        if (girl.getCurrentAction() == Action.BOW && !girl.isUsingItem) {
            girl.setCurrentAction(Action.NULL);
        }
        if (girl.getCurrentAction() == Action.BOW) {
            girl.rotationYaw = girl.rotationYawHead;
            girl.renderYawOffset = girl.rotationYawHead;
            girl.prevRenderYawOffset = girl.prevRotationYawHead;
        }
        if (girl.isPlayerRiding) {
            return player.getRidingEntity() instanceof EntityBoat ? 0.4f : 0.2f;
        }
        return 0.0f;
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            if (event.phase == TickEvent.Phase.END) {
                if (this.savedPlayerPos != null) {
                    mc.player.setPosition(this.savedPlayerPos.x, this.savedPlayerPos.y, this.savedPlayerPos.z);
                    mc.player.lastTickPosX = this.savedPlayerLastTickPos.x;
                    mc.player.lastTickPosY = this.savedPlayerLastTickPos.y;
                    mc.player.lastTickPosZ = this.savedPlayerLastTickPos.z;
                    this.savedPlayerPos = null;
                    this.savedPlayerLastTickPos = null;
                }
                return;
            }
            if (mc.gameSettings.thirdPersonView == 0) {
                PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(mc.player.getPersistentID());
                if (playerGirl != null) {
                    if (playerGirl.isSceneActive()) {
                        this.savedPlayerPos = mc.player.getPositionVector();
                        this.savedPlayerLastTickPos = new Vec3d(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ);
                        Vec3d vec3d = playerGirl.getCachedBoneOffset("girlCam");
                        vec3d = playerGirl.getOwnerAimVector(vec3d, event.renderTickTime);
                        vec3d = vec3d.add(RotationHelper.LerpVec3d(this.savedPlayerLastTickPos, this.savedPlayerPos, event.renderTickTime));
                        mc.player.posX = vec3d.x;
                        mc.player.posY = vec3d.y - (double) mc.player.getEyeHeight();
                        mc.player.posZ = vec3d.z;
                        mc.player.lastTickPosX = vec3d.x;
                        mc.player.lastTickPosY = vec3d.y - (double) mc.player.getEyeHeight();
                        mc.player.lastTickPosZ = vec3d.z;
                        Action action = playerGirl.getCurrentAction();
                        float yaw = playerGirl.getYawRotation();
                        if (!playerGirl.canPerformAction(action, mc.player)) {
                            if (action.flipGirlYaw) {
                                yaw += 180.0f;
                            }
                            if (mc.player.rotationPitch > action.maxGirlPitch) {
                                mc.player.rotationPitch = action.maxGirlPitch;
                                mc.player.prevRotationPitch = action.maxGirlPitch;
                            }
                            if (mc.player.rotationPitch < action.minGirlPitch) {
                                mc.player.rotationPitch = action.minGirlPitch;
                                mc.player.prevRotationPitch = action.minGirlPitch;
                            }
                            if (mc.player.rotationYaw > yaw + 90.0f) {
                                mc.player.rotationYaw = yaw + 90.0f;
                                mc.player.prevRotationYaw = yaw + 90.0f;
                            }
                            if (mc.player.rotationYaw < yaw - 90.0f) {
                                mc.player.rotationYaw = yaw - 90.0f;
                                mc.player.prevRotationYaw = yaw - 90.0f;
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup setup) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player != null) {
            PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(minecraft.player.getPersistentID());
            if (playerGirl != null) {
                if (playerGirl.FAllieBoolean()) {
                    if (playerGirl.isAnchored()) {
                        setup.setRoll(180.0f);
                        setup.setPitch(-setup.getPitch());
                        setup.setYaw(-setup.getYaw());
                    }
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.savedPlayerPos != null) {
            if (mc.gameSettings.thirdPersonView == 0) {
                PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(mc.player.getPersistentID());
                if (playerGirl != null) {
                    Vec3d vec3d = mc.player.getPositionVector();
                    Vec3d vec3d2 = RotationHelper.LerpVec3d(this.savedPlayerLastTickPos, this.savedPlayerPos, event.getPartialTicks());
                    Vec3d vec3d3 = vec3d2.subtract(vec3d);
                    RenderPlayerGirl.renderPlayerAsGirl(playerGirl, mc.player, vec3d3.x, vec3d3.y, vec3d3.z, event.getPartialTicks());
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    GlStateManager.enableAlpha();
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            if (event.phase != TickEvent.Phase.END) {
                PlayerGirl uuidHashtable = PlayerGirl.getUUIDHashtable(mc.player.getPersistentID());
                if (uuidHashtable == null) {
                    if (this.hasCustomEyeHeight) {
                        this.hasCustomEyeHeight = false;
                        mc.player.eyeHeight = mc.player.getDefaultEyeHeight();
                    }
                    return;
                }
                if (uuidHashtable.isAnchored()) {
                    if (this.hasCustomEyeHeight) {
                        this.hasCustomEyeHeight = false;
                        mc.player.eyeHeight = mc.player.getDefaultEyeHeight();
                    }
                    return;
                }
                if (this.lastRenderedPlayerGirl != uuidHashtable) {
                    RenderPlayerGirl.renderPlayerAsGirl(uuidHashtable, mc.player, 0.0, 500.0, 0.0, event.renderTickTime);
                    this.lastRenderedPlayerGirl = uuidHashtable;
                }
                mc.player.eyeHeight = uuidHashtable.getCameraBoneHeight();
                this.hasCustomEyeHeight = true;
            }
        }
    }
}

