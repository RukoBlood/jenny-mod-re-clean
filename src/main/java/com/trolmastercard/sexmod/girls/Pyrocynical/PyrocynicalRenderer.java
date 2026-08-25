/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Pyrocynical;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class PyrocynicalRenderer extends Render<PyrocynicalEntity> {
    final static ResourceLocation PYRO_STANDING = new ResourceLocation("sexmod", "textures/entity/pyrocinical/standing.png");
    final static ResourceLocation PYRO_PRAISING = new ResourceLocation("sexmod", "textures/entity/pyrocinical/praising.png");
    final static ResourceLocation PYRO_WALKINGANIM_F1 = new ResourceLocation("sexmod", "textures/entity/pyrocinical/walking1.png");
    final static ResourceLocation PYRO_WALINGANIM_F2 = new ResourceLocation("sexmod", "textures/entity/pyrocinical/walking2.png");
    final static String PYRO_FAT_ANIM_FRAMES = "textures/entity/pyrocinical/fat/";
    final static int j = 30;
    final static float c = 1.4f;
    final static float h = 0.75f;
    Minecraft mc = Minecraft.getMinecraft();
    ResourceLocation cachedTexture = null;
    long lastTextureSwitchTime = 0L;

    public PyrocynicalRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    //a
    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(PyrocynicalEntity entity) {
        return null;
    }

    // a
    @Override
    public void doRender(PyrocynicalEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glDisable(2896);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        EntityPlayerSP player = this.mc.player;
        Vec3d pyroPos = RotationHelper.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), partialTicks);
        Vec3d playerPos = RotationHelper.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), partialTicks);
        Vec3d offset = pyroPos.subtract(playerPos);
        ResourceLocation texture = this.getFatTexture(entity, Math.abs(offset.x) + Math.abs(offset.y) + Math.abs(offset.z));
        this.mc.renderEngine.bindTexture(texture);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, this.getFatShrink(entity, partialTicks));
        GlStateManager.translate(offset.x, offset.y + this.getFatBobOffset(texture), offset.z);
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        float scale = 1.4f + this.getFatProgress(entity, partialTicks);
        GlStateManager.scale(scale, scale, scale);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(-1.0, 0.0, 0.0).tex(0.0, 1.0).endVertex();
        bufferBuilder.pos(1.0, 0.0, 0.0).tex(1.0, 1.0).endVertex();
        bufferBuilder.pos(1.0, 2.0, 0.0).tex(1.0, 0.0).endVertex();
        bufferBuilder.pos(-1.0, 2.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GL11.glEnable(2896);
        GlStateManager.disableAlpha();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
        long now = System.currentTimeMillis();
        if (this.cachedTexture != PyrocynicalRenderer.PYRO_PRAISING && texture == PyrocynicalRenderer.PYRO_PRAISING && now > this.lastTextureSwitchTime + 60000L) {
            this.mc.player.playSound(SoundsHandler.MISC_PYRO[0], 1.0f, 1.0f);
            this.lastTextureSwitchTime = now;
        }
        this.cachedTexture = texture;
    }

    ResourceLocation getFatTexture(PyrocynicalEntity pyrocynical, double distance) {
        if (pyrocynical.triggerTick != -1) {
            return new ResourceLocation("sexmod", String.format("%s%s.png", PYRO_FAT_ANIM_FRAMES, this.getFatIndex(pyrocynical)));
        }
        if (distance < 3.0) {
            return PYRO_PRAISING;
        }
        Vec3d movement = new Vec3d(pyrocynical.lastTickPosX, pyrocynical.lastTickPosY, pyrocynical.lastTickPosZ).subtract(pyrocynical.getPositionVector());
        if (Math.abs(movement.x) + Math.abs(movement.y) + Math.abs(movement.z) == 0.0) {
            return PYRO_STANDING;
        }
        return Math.sin((float)this.mc.player.ticksExisted * 0.75f) > 0.0 ? PYRO_WALKINGANIM_F1 : PYRO_WALINGANIM_F2;
    }

    double getFatBobOffset(ResourceLocation resourceLocation) {
        return !PYRO_WALKINGANIM_F1.equals(resourceLocation) && !PYRO_WALINGANIM_F2.equals(resourceLocation) ? 0.0 : Math.sin((float) this.mc.player.ticksExisted * 0.75f) * (double) 0.1f;
    }

    int getFatIndex(PyrocynicalEntity pyrocynical) {
        return pyrocynical.triggerTick == -1 ? 0 : (int) ThreadNames.clamp(this.mc.player.ticksExisted - pyrocynical.triggerTick, 1.0f, 30.0f);
    }

    float getFatProgress(PyrocynicalEntity pyrocynical, float partialTicks) {
        if (pyrocynical.triggerTick == -1) {
            return 0.0f;
        }
        int index = this.getFatIndex(pyrocynical);
        return index == 30 ? 1.0f : ((float) index + partialTicks) / 30.0f;
    }

    float getFatShrink(PyrocynicalEntity pyrocynical, float partialTicks) {
        if (pyrocynical.triggerTick == -1) {
            return 1.0f;
        }
        if (this.mc.player.ticksExisted - pyrocynical.triggerTick > 120) {
            return 0.0f;
        }
        //int ninety = 90;
        float elapsed = ThreadNames.clamp(this.mc.player.ticksExisted - pyrocynical.triggerTick, 90, 120.0f) - 90.0f;
        float fadeProgress = (elapsed + partialTicks) / 30.0f;
        return 1.0f - fadeProgress;
    }

    //@Override
    //@Nullable
    //protected ResourceLocation getEntityTexture(Entity entity) {
    //    return this.a((EntityPyrocynical)entity);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.a((EntityPyrocynical)entity, d, d2, d3, f, f2);
    //}
}

