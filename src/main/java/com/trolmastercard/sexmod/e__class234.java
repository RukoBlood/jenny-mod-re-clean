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
package com.trolmastercard.sexmod;

import javax.vecmath.Vector2f;
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
        PlayerGirl.void_C();
        PlayerGirl ei_class2512 = PlayerGirl.d_(pre.getEntityPlayer().getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        pre.setCanceled(true);
        e__class234.a(ei_class2512, pre.getEntityPlayer(), pre.getX(), pre.getY(), pre.getZ(), pre.getPartialRenderTick());
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(PlayerGirl ei_class2512, EntityPlayer entityPlayer, double d, double d2, double d3, float f) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if ((entityPlayer = ei_class2512.net_minecraft_entity_player_EntityPlayer_c(entityPlayer)).isInvisibleToPlayer(minecraft.player) && !ei_class2512.boolean_E()) {
            return;
        }
        RenderManager renderManager = minecraft.getRenderManager();
        ei_class2512.rotationYaw = entityPlayer.rotationYaw;
        ei_class2512.prevRotationYawHead = entityPlayer.prevRotationYawHead;
        ei_class2512.rotationYawHead = entityPlayer.rotationYawHead;
        ei_class2512.prevRotationPitch = entityPlayer.prevRotationPitch;
        ei_class2512.rotationPitch = entityPlayer.rotationPitch;
        ei_class2512.prevRotationYaw = entityPlayer.prevRotationYaw;
        ei_class2512.prevPosX = entityPlayer.prevPosX;
        ei_class2512.prevPosY = entityPlayer.prevPosY;
        ei_class2512.prevPosZ = entityPlayer.prevPosZ;
        ei_class2512.lastTickPosX = entityPlayer.lastTickPosX;
        ei_class2512.lastTickPosY = entityPlayer.lastTickPosY;
        ei_class2512.lastTickPosZ = entityPlayer.lastTickPosZ;
        ei_class2512.renderYawOffset = entityPlayer.renderYawOffset;
        ei_class2512.prevRenderYawOffset = entityPlayer.prevRenderYawOffset;
        ei_class2512.ad = entityPlayer.isSneaking();
        ei_class2512.aj = entityPlayer.isSprinting();
        ei_class2512.ak = entityPlayer.isRiding();
        ei_class2512.af = entityPlayer.onGround;
        ei_class2512.ah = entityPlayer.getItemInUseCount() != 0;
        double d4 = entityPlayer.lastTickPosX - entityPlayer.posX;
        double d5 = entityPlayer.posZ - entityPlayer.lastTickPosZ;
        double d6 = Math.PI / 180 * (double)entityPlayer.rotationYaw;
        ei_class2512.ao = new Vector2f((float)(d4 * Math.cos(d6) + d5 * Math.sin(d6)), (float)(d4 * Math.sin(d6) + d5 * Math.cos(d6)));
        float f2 = ei_class2512.boolean_z() ? e__class234.a(ei_class2512, entityPlayer) : 0.0f;
        PlayerGirlRenderer.v = true;
        renderManager.renderEntity(ei_class2512, d, d2 + (double)f2, d3, 90.0f, f, false);
    }

    static float a(PlayerGirl ei_class2512, EntityPlayer entityPlayer) {
        if (ei_class2512.getDataManager().get(GirlEntity.G).booleanValue()) {
            return 0.0f;
        }
        if ((entityPlayer.getHeldItemMainhand().getItem() instanceof ItemBow || entityPlayer.getHeldItemOffhand().getItem() instanceof ItemBow) && ei_class2512.ah) {
            ei_class2512.setCurrentAction(Action.BOW);
        }
        if (ei_class2512.currentAction() == Action.BOW && !ei_class2512.ah) {
            ei_class2512.setCurrentAction(Action.NULL);
        }
        if (ei_class2512.currentAction() == Action.BOW) {
            ei_class2512.rotationYaw = ei_class2512.rotationYawHead;
            ei_class2512.renderYawOffset = ei_class2512.rotationYawHead;
            ei_class2512.prevRenderYawOffset = ei_class2512.prevRotationYawHead;
        }
        if (ei_class2512.ak) {
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
        PlayerGirl ei_class2512 = PlayerGirl.d_(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        if (!ei_class2512.boolean_o()) {
            return;
        }
        this.b = minecraft.player.getPositionVector();
        this.d = new Vec3d(minecraft.player.lastTickPosX, minecraft.player.lastTickPosY, minecraft.player.lastTickPosZ);
        Vec3d vec3d = ei_class2512.b("girlCam");
        vec3d = ei_class2512.b(vec3d, renderTickEvent.renderTickTime);
        vec3d = vec3d.add(b6_class67.a(this.d, this.b, (double)renderTickEvent.renderTickTime));
        minecraft.player.posX = vec3d.x;
        minecraft.player.posY = vec3d.y - (double)minecraft.player.getEyeHeight();
        minecraft.player.posZ = vec3d.z;
        minecraft.player.lastTickPosX = vec3d.x;
        minecraft.player.lastTickPosY = vec3d.y - (double)minecraft.player.getEyeHeight();
        minecraft.player.lastTickPosZ = vec3d.z;
        Action fp_class3242 = ei_class2512.currentAction();
        float f = ei_class2512.java_lang_Float_I().floatValue();
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
        PlayerGirl ei_class2512 = PlayerGirl.d_(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        if (!ei_class2512.boolean_F()) {
            return;
        }
        if (!ei_class2512.boolean_Q()) {
            return;
        }
        cameraSetup.setRoll(180.0f);
        cameraSetup.setPitch(-cameraSetup.getPitch());
        cameraSetup.setYaw(-cameraSetup.getYaw());
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(RenderWorldLastEvent renderWorldLastEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (this.b == null) {
            return;
        }
        if (minecraft.gameSettings.thirdPersonView != 0) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.d_(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        Vec3d vec3d = minecraft.player.getPositionVector();
        Vec3d vec3d2 = b6_class67.a(this.d, this.b, (double)renderWorldLastEvent.getPartialTicks());
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        e__class234.a(ei_class2512, minecraft.player, vec3d3.x, vec3d3.y, vec3d3.z, renderWorldLastEvent.getPartialTicks());
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
        PlayerGirl ei_class2512 = PlayerGirl.d_(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            if (this.e) {
                this.e = false;
                minecraft.player.eyeHeight = minecraft.player.getDefaultEyeHeight();
            }
            return;
        }
        if (ei_class2512.boolean_Q()) {
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
        minecraft.player.eyeHeight = ei_class2512.float_R();
        this.e = true;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

