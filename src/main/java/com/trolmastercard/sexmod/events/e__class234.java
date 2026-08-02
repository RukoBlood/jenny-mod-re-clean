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
package com.trolmastercard.sexmod.events;

import javax.vecmath.Vector2f;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import com.trolmastercard.sexmod.util.Reference;
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

public class e__class234 {
    final static public float c = 1.2345679f;
    Vec3d b = null;
    Vec3d d = null;
    PlayerGirl a = null;
    boolean e = false;

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderPlayerEvent.Pre pre) {
        if (pre.getPartialRenderTick() == 1.2345679f) {
            return;
        }
        PlayerGirl.cleanupGlobalRegistry();
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(pre.getEntityPlayer().getPersistentID());
        if (playerGirl == null) {
            return;
        }
        pre.setCanceled(true);
        e__class234.a(playerGirl, pre.getEntityPlayer(), pre.getX(), pre.getY(), pre.getZ(), pre.getPartialRenderTick());
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(PlayerGirl playerGirl, EntityPlayer player, double d, double d2, double d3, float f) {
        Minecraft mc = Minecraft.getMinecraft();
        if ((player = playerGirl.getPlayerEntity(player)).isInvisibleToPlayer(mc.player) && !playerGirl.boolean_E()) {
            return;
        }
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
        playerGirl.ah = player.getItemInUseCount() != 0;
        double d4 = player.lastTickPosX - player.posX;
        double d5 = player.posZ - player.lastTickPosZ;
        double d6 = Math.PI / 180 * (double)player.rotationYaw;
        playerGirl.ao = new Vector2f((float)(d4 * Math.cos(d6) + d5 * Math.sin(d6)), (float)(d4 * Math.sin(d6) + d5 * Math.cos(d6)));
        float f2 = playerGirl.boolean_z() ? e__class234.a(playerGirl, player) : 0.0f;
        PlayerGirlRenderer.forceRenderNextFrame = true;
        renderManager.renderEntity(playerGirl, d, d2 + (double)f2, d3, 90.0f, f, false);
    }

    static float a(PlayerGirl pg, EntityPlayer entityPlayer) {
        if (pg.getDataManager().get(GirlEntity.IS_ANCHORED)) {
            return 0.0f;
        }
        if ((entityPlayer.getHeldItemMainhand().getItem() instanceof ItemBow || entityPlayer.getHeldItemOffhand().getItem() instanceof ItemBow) && pg.ah) {
            pg.setCurrentAction(Action.BOW);
        }
        if (pg.currentAction() == Action.BOW && !pg.ah) {
            pg.setCurrentAction(Action.NULL);
        }
        if (pg.currentAction() == Action.BOW) {
            pg.rotationYaw = pg.rotationYawHead;
            pg.renderYawOffset = pg.rotationYawHead;
            pg.prevRenderYawOffset = pg.prevRotationYawHead;
        }
        if (pg.isPlayerRiding) {
            return entityPlayer.getRidingEntity() instanceof EntityBoat ? 0.4f : 0.2f;
        }
        return 0.0f;
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(TickEvent.RenderTickEvent renderTickEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player == null) {
            return;
        }
        if (renderTickEvent.phase == TickEvent.Phase.END) {
            if (this.b != null) {
                minecraft.player.setPosition(this.b.x, this.b.y, this.b.z);
                minecraft.player.lastTickPosX = this.d.x;
                minecraft.player.lastTickPosY = this.d.y;
                minecraft.player.lastTickPosZ = this.d.z;
                this.b = null;
                this.d = null;
            }
            return;
        }
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        if (!ei_class2512.boolean_o()) {
            return;
        }
        this.b = minecraft.player.getPositionVector();
        this.d = new Vec3d(minecraft.player.lastTickPosX, minecraft.player.lastTickPosY, minecraft.player.lastTickPosZ);
        Vec3d vec3d = ei_class2512.getCachedBoneOffset("girlCam");
        vec3d = ei_class2512.b(vec3d, renderTickEvent.renderTickTime);
        vec3d = vec3d.add(Reference.LerpVec3d(this.d, this.b, (double)renderTickEvent.renderTickTime));
        minecraft.player.posX = vec3d.x;
        minecraft.player.posY = vec3d.y - (double)minecraft.player.getEyeHeight();
        minecraft.player.posZ = vec3d.z;
        minecraft.player.lastTickPosX = vec3d.x;
        minecraft.player.lastTickPosY = vec3d.y - (double)minecraft.player.getEyeHeight();
        minecraft.player.lastTickPosZ = vec3d.z;
        Action fp_class3242 = ei_class2512.currentAction();
        float f = ei_class2512.getYawRotation().floatValue();
        if (ei_class2512.a(fp_class3242, minecraft.player)) {
            return;
        }
        if (fp_class3242.flipGirlYaw) {
            f += 180.0f;
        }
        if (minecraft.player.rotationPitch > fp_class3242.maxGirlPitch) {
            minecraft.player.rotationPitch = fp_class3242.maxGirlPitch;
            minecraft.player.prevRotationPitch = fp_class3242.maxGirlPitch;
        }
        if (minecraft.player.rotationPitch < fp_class3242.minGirlPitch) {
            minecraft.player.rotationPitch = fp_class3242.minGirlPitch;
            minecraft.player.prevRotationPitch = fp_class3242.minGirlPitch;
        }
        if (minecraft.player.rotationYaw > f + 90.0f) {
            minecraft.player.rotationYaw = f + 90.0f;
            minecraft.player.prevRotationYaw = f + 90.0f;
        }
        if (minecraft.player.rotationYaw < f - 90.0f) {
            minecraft.player.rotationYaw = f - 90.0f;
            minecraft.player.prevRotationYaw = f - 90.0f;
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(EntityViewRenderEvent.CameraSetup cameraSetup) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player == null) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        if (!ei_class2512.boolean_F()) {
            return;
        }
        if (!ei_class2512.isAnchored()) {
            return;
        }
        cameraSetup.setRoll(180.0f);
        cameraSetup.setPitch(-cameraSetup.getPitch());
        cameraSetup.setYaw(-cameraSetup.getYaw());
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderWorldLastEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (this.b == null) {
            return;
        }
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        Vec3d vec3d = minecraft.player.getPositionVector();
        Vec3d vec3d2 = Reference.LerpVec3d(this.d, this.b, (double)event.getPartialTicks());
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        e__class234.a(ei_class2512, minecraft.player, vec3d3.x, vec3d3.y, vec3d3.z, event.getPartialTicks());
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void b(TickEvent.RenderTickEvent renderTickEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player == null) {
            return;
        }
        if (renderTickEvent.phase == TickEvent.Phase.END) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            if (this.e) {
                this.e = false;
                minecraft.player.eyeHeight = minecraft.player.getDefaultEyeHeight();
            }
            return;
        }
        if (ei_class2512.isAnchored()) {
            if (this.e) {
                this.e = false;
                minecraft.player.eyeHeight = minecraft.player.getDefaultEyeHeight();
            }
            return;
        }
        if (this.a != ei_class2512) {
            e__class234.a(ei_class2512, minecraft.player, 0.0, 500.0, 0.0, renderTickEvent.renderTickTime);
            this.a = ei_class2512;
        }
        minecraft.player.eyeHeight = ei_class2512.getCameraBoneHeight();
        this.e = true;
    }
}

