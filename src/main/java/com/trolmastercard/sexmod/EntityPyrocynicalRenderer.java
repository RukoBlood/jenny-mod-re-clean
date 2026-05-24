/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.util.Reference;
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

public class EntityPyrocynicalRenderer extends Render<EntityPyrocynical> {
    final static ResourceLocation PYRO_STANDING = new ResourceLocation("sexmod", "textures/entity/pyrocinical/standing.png");
    final static ResourceLocation PYRO_PRAISING = new ResourceLocation("sexmod", "textures/entity/pyrocinical/praising.png");
    final static ResourceLocation PYRO_WALKINGANIM_F1 = new ResourceLocation("sexmod", "textures/entity/pyrocinical/walking1.png");
    final static ResourceLocation PYRO_WALINGANIM_F2 = new ResourceLocation("sexmod", "textures/entity/pyrocinical/walking2.png");
    final static String PYRO_FAT_ANIM_FRAMES = "textures/entity/pyrocinical/fat/";
    final static int j = 30;
    final static float c = 1.4f;
    final static float h = 0.75f;
    Minecraft minecraft = Minecraft.getMinecraft();
    ResourceLocation k = null;
    long i = 0L;

    public EntityPyrocynicalRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    //a
    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(EntityPyrocynical entity) {
        return null;
    }

    // a
    @Override
    public void doRender(EntityPyrocynical entity, double x, double y, double z, float f, float f2) {
        GL11.glDisable(2896);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        EntityPlayerSP entityPlayerSP = this.minecraft.player;
        Vec3d vec3d = Reference.a(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)f2);
        Vec3d vec3d2 = Reference.a(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f2);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        ResourceLocation resourceLocation = this.a(entity, Math.abs(vec3d3.x) + Math.abs(vec3d3.y) + Math.abs(vec3d3.z));
        this.minecraft.renderEngine.bindTexture(resourceLocation);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, this.b(entity, f2));
        GlStateManager.translate(vec3d3.x, vec3d3.y + this.a(resourceLocation), vec3d3.z);
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        float f3 = 1.4f + this.a(entity, f2);
        GlStateManager.scale(f3, f3, f3);
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
        long l = System.currentTimeMillis();
        if (this.k != EntityPyrocynicalRenderer.PYRO_PRAISING && resourceLocation == EntityPyrocynicalRenderer.PYRO_PRAISING && l > this.i + 60000L) {
            this.minecraft.player.playSound(SoundsHandler.MISC_PYRO[0], 1.0f, 1.0f);
            this.i = l;
        }
        this.k = resourceLocation;
    }

    ResourceLocation a(EntityPyrocynical pyrocynical, double d) {
        if (pyrocynical.a != -1) {
            return new ResourceLocation("sexmod", String.format("%s%s.png", PYRO_FAT_ANIM_FRAMES, this.b(pyrocynical)));
        }
        if (d < 3.0) {
            return PYRO_PRAISING;
        }
        Vec3d vec3d = new Vec3d(pyrocynical.lastTickPosX, pyrocynical.lastTickPosY, pyrocynical.lastTickPosZ).subtract(pyrocynical.getPositionVector());
        if (Math.abs(vec3d.x) + Math.abs(vec3d.y) + Math.abs(vec3d.z) == 0.0) {
            return PYRO_STANDING;
        }
        return Math.sin((float)this.minecraft.player.ticksExisted * 0.75f) > 0.0 ? PYRO_WALKINGANIM_F1 : PYRO_WALINGANIM_F2;
    }

    double a(ResourceLocation resourceLocation) {
        if (!PYRO_WALKINGANIM_F1.equals(resourceLocation) && !PYRO_WALINGANIM_F2.equals(resourceLocation)) {
            return 0.0;
        }
        return Math.sin((float)this.minecraft.player.ticksExisted * 0.75f) * (double)0.1f;
    }

    int b(EntityPyrocynical al_class332) {
        if (al_class332.a == -1) {
            return 0;
        }
        return (int)be_class78.b(this.minecraft.player.ticksExisted - al_class332.a, 1.0f, 30.0f);
    }

    float a(EntityPyrocynical al_class332, float f) {
        if (al_class332.a == -1) {
            return 0.0f;
        }
        int n = this.b(al_class332);
        if (n == 30) {
            return 1.0f;
        }
        return ((float)n + f) / 30.0f;
    }

    float b(EntityPyrocynical al_class332, float f) {
        if (al_class332.a == -1) {
            return 1.0f;
        }
        if (this.minecraft.player.ticksExisted - al_class332.a > 120) {
            return 0.0f;
        }
        int n = 90;
        float f2 = be_class78.b(this.minecraft.player.ticksExisted - al_class332.a, n, 120.0f) - (float)n;
        float f3 = (f2 + f) / 30.0f;
        return 1.0f - f3;
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

