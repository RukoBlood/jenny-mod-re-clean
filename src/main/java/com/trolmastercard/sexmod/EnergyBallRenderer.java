/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

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

import javax.annotation.Nonnull;

public class EnergyBallRenderer
extends Render<EnergyBallEntity> {
    static public EnergyBallRenderer a;
    final static gv_class388 e;
    final static gv_class388 b;
    final static gv_class388 d;
    Minecraft c = Minecraft.getMinecraft();

    public EnergyBallRenderer(RenderManager renderManager) {
        super(renderManager);
        a = this;
    }

    //a
    @Override
    protected ResourceLocation getEntityTexture(EnergyBallEntity c4_class1132) {
        return new ResourceLocation("sexmod", "textures/entity/galath/energy_ball.png");
    }

    //a
    @Override
    public void doRender(EnergyBallEntity c4_class1132, double d, double d2, double d3, float f, float f2) {
        gv_class388 gv_class3882;
        gv_class388 gv_class3883;
        GL11.glDisable(2896);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        EntityPlayerSP entityPlayerSP = this.c.player;
        Vec3d vec3d = b6_class67.a(new Vec3d(c4_class1132.lastTickPosX, c4_class1132.lastTickPosY, c4_class1132.lastTickPosZ), c4_class1132.getPositionVector(), (double)f2);
        Vec3d vec3d2 = b6_class67.a(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f2);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d3.x, vec3d3.y, vec3d3.z);
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(c4_class1132.g, c4_class1132.g, c4_class1132.g);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        this.c.renderEngine.bindTexture(this.getEntityTexture(c4_class1132));
        if (c4_class1132.g == 1.0) {
            float f3 = (float)this.c.world.getTotalWorldTime() + this.c.getRenderPartialTicks();
            double d4 = 0.5 * Math.sin((double)f3 * 0.5) + 0.5;
            gv_class3883 = b6_class67.a(e, b, d4);
            gv_class3882 = b6_class67.a(b, e, d4);
        } else {
            gv_class3883 = b6_class67.a(EnergyBallRenderer.d, e, c4_class1132.g);
            gv_class3882 = b6_class67.a(EnergyBallRenderer.d, e, c4_class1132.g);
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        this.a(bufferBuilder, gv_class3883, 0.0f);
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        GlStateManager.translate(0.0f, 0.075f, 0.0f);
        this.a(bufferBuilder, gv_class3882, 0.001f);
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GL11.glEnable(2896);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
    }

    void a(BufferBuilder bufferBuilder, gv_class388 gv_class3882, float f) {
        bufferBuilder.pos(-0.25, 0.0, f).tex(0.0, 0.0).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(0.25, 0.0, f).tex(1.0, 0.0).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(0.25, 0.5, f).tex(1.0, 1.0).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(-0.25, 0.5, f).tex(0.0, 1.0).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
    }

    //@Override
    //@Nullable
    //protected ResourceLocation getEntityTexture(Entity entity) {
    //    return this.a((c4_class113)entity);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.a((c4_class113)entity, d, d2, d3, f, f2);
    //}

    static {
        e = new gv_class388(0, 255, 251, 255);
        b = new gv_class388(255, 0, 236, 255);
        d = new gv_class388(255, 255, 255, 0);
    }
}

