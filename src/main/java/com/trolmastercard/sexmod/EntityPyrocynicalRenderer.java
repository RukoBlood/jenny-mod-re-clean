/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class EntityPyrocynicalRenderer
extends Render<EntityPyrocynical> {
    final static ResourceLocation g = new ResourceLocation("sexmod", "textures/entity/pyrocinical/standing.png");
    final static ResourceLocation f = new ResourceLocation("sexmod", "textures/entity/pyrocinical/praising.png");
    final static ResourceLocation a = new ResourceLocation("sexmod", "textures/entity/pyrocinical/walking1.png");
    final static ResourceLocation b = new ResourceLocation("sexmod", "textures/entity/pyrocinical/walking2.png");
    final static String e = "textures/entity/pyrocinical/fat/";
    final static int j = 30;
    final static float c = 1.4f;
    final static float h = 0.75f;
    Minecraft d = Minecraft.getMinecraft();
    ResourceLocation k = null;
    long i = 0L;

    public EntityPyrocynicalRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    //a
    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(EntityPyrocynical al_class332) {
        return null;
    }

    // a
    @Override
    public void doRender(EntityPyrocynical al_class332, double d, double d2, double d3, float f, float f2) {
        GL11.glDisable(2896);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        EntityPlayerSP entityPlayerSP = this.d.player;
        Vec3d vec3d = b6_class67.a(new Vec3d(al_class332.lastTickPosX, al_class332.lastTickPosY, al_class332.lastTickPosZ), al_class332.getPositionVector(), (double)f2);
        Vec3d vec3d2 = b6_class67.a(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f2);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        ResourceLocation resourceLocation = this.a(al_class332, Math.abs(vec3d3.x) + Math.abs(vec3d3.y) + Math.abs(vec3d3.z));
        this.d.renderEngine.bindTexture(resourceLocation);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, this.b(al_class332, f2));
        GlStateManager.translate(vec3d3.x, vec3d3.y + this.a(resourceLocation), vec3d3.z);
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        float f3 = 1.4f + this.a(al_class332, f2);
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
        if (this.k != EntityPyrocynicalRenderer.f && resourceLocation == EntityPyrocynicalRenderer.f && l > this.i + 60000L) {
            this.d.player.playSound(SoundEventHandler.MISC_PYRO[0], 1.0f, 1.0f);
            this.i = l;
        }
        this.k = resourceLocation;
    }

    ResourceLocation a(EntityPyrocynical al_class332, double d) {
        if (al_class332.a != -1) {
            return new ResourceLocation("sexmod", String.format("%s%s.png", e, this.b(al_class332)));
        }
        if (d < 3.0) {
            return f;
        }
        Vec3d vec3d = new Vec3d(al_class332.lastTickPosX, al_class332.lastTickPosY, al_class332.lastTickPosZ).subtract(al_class332.getPositionVector());
        if (Math.abs(vec3d.x) + Math.abs(vec3d.y) + Math.abs(vec3d.z) == 0.0) {
            return g;
        }
        return Math.sin((float)this.d.player.ticksExisted * 0.75f) > 0.0 ? a : b;
    }

    double a(ResourceLocation resourceLocation) {
        if (!a.equals(resourceLocation) && !b.equals(resourceLocation)) {
            return 0.0;
        }
        return Math.sin((float)this.d.player.ticksExisted * 0.75f) * (double)0.1f;
    }

    int b(EntityPyrocynical al_class332) {
        if (al_class332.a == -1) {
            return 0;
        }
        return (int)be_class78.b(this.d.player.ticksExisted - al_class332.a, 1.0f, 30.0f);
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
        if (this.d.player.ticksExisted - al_class332.a > 120) {
            return 0.0f;
        }
        int n = 90;
        float f2 = be_class78.b(this.d.player.ticksExisted - al_class332.a, n, 120.0f) - (float)n;
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

