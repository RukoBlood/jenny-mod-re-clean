/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Galath.EnergyBall;

import com.trolmastercard.sexmod.util.ColorRGBA;
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

public class EnergyBallRenderer extends Render<EnergyBallEntity> {
    static public EnergyBallRenderer instance;
    final static ColorRGBA COLOR_CYAN;
    final static ColorRGBA COLOR_MAGENTA;
    final static ColorRGBA COLOR_WHITE_ALPHA;
    Minecraft mc = Minecraft.getMinecraft();

    public EnergyBallRenderer(RenderManager renderManager) {
        super(renderManager);
        instance = this;
    }

    //a
    @Override
    protected ResourceLocation getEntityTexture(EnergyBallEntity c4_class1132) {
        return new ResourceLocation("sexmod", "textures/entity/galath/energy_ball.png");
    }

    //a
    @Override
    public void doRender(EnergyBallEntity entity, double d, double d2, double d3, float f, float f2) {
        ColorRGBA innerColor;
        ColorRGBA outerColor;
        GL11.glDisable(2896);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        EntityPlayerSP player = this.mc.player;
        Vec3d vec3d = RotationHelper.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)f2);
        Vec3d vec3d2 = RotationHelper.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), (double)f2);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d3.x, vec3d3.y, vec3d3.z);
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(entity.SCALE_1_0, entity.SCALE_1_0, entity.SCALE_1_0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        this.mc.renderEngine.bindTexture(this.getEntityTexture(entity));
        if (entity.SCALE_1_0 == 1.0) {
            float f3 = (float)this.mc.world.getTotalWorldTime() + this.mc.getRenderPartialTicks();
            double d4 = 0.5 * Math.sin((double)f3 * 0.5) + 0.5;
            outerColor = RotationHelper.LerpColorRGBA(COLOR_CYAN, COLOR_MAGENTA, d4);
            innerColor = RotationHelper.LerpColorRGBA(COLOR_MAGENTA, COLOR_CYAN, d4);
        } else {
            outerColor = RotationHelper.LerpColorRGBA(EnergyBallRenderer.COLOR_WHITE_ALPHA, COLOR_CYAN, entity.SCALE_1_0);
            innerColor = RotationHelper.LerpColorRGBA(EnergyBallRenderer.COLOR_WHITE_ALPHA, COLOR_CYAN, entity.SCALE_1_0);
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        this.renderColor(bufferBuilder, outerColor, 0.0f);
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        GlStateManager.translate(0.0f, 0.075f, 0.0f);
        this.renderColor(bufferBuilder, innerColor, 0.001f);
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GL11.glEnable(2896);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
    }

    void renderColor(BufferBuilder buf, ColorRGBA color, float z) {
        buf.pos(-0.25, 0.0, z).tex(0.0, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
        buf.pos(0.25, 0.0, z).tex(1.0, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
        buf.pos(0.25, 0.5, z).tex(1.0, 1.0).color(color.r, color.g, color.b, color.a).endVertex();
        buf.pos(-0.25, 0.5, z).tex(0.0, 1.0).color(color.r, color.g, color.b, color.a).endVertex();
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
        COLOR_CYAN = new ColorRGBA(0, 255, 251, 255);
        COLOR_MAGENTA = new ColorRGBA(255, 0, 236, 255);
        COLOR_WHITE_ALPHA = new ColorRGBA(255, 255, 255, 0);
    }
}

