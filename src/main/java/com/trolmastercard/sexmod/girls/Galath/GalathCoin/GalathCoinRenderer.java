/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Tuple4f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4f
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Galath.GalathCoin;

import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.Vector3fSexmodSpecial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class GalathCoinRenderer extends GeoItemRenderer<GalathCoin> {
    final static public Vector3fSexmodSpecial COIN_COLOR = new Vector3fSexmodSpecial(0.84705883f, 0.11764706f, 0.35686275f);
    final static public Vector3fSexmodSpecial COIN_COLOR_DARK = new Vector3fSexmodSpecial(0.44705883f, 0.44705883f, 0.44705883f);
    final static public float ROTATION_SPEED = 240.0f;
    final static public float ROTATION_AMPLITUDE = 120.0f;
    final static float BOB_SPEED = 0.05f;
    final static Minecraft mc = Minecraft.getMinecraft();
    boolean isFlipping = false;
    Vector3fSexmodSpecial currentTint;

    public GalathCoinRenderer() {
        super(new GalathCoinModel());
    }

    // a
    @Override
    public void render(GeoModel geoModel, GalathCoin galathCoin, float f, float f2, float f3, float f4, float f5) {
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GeoBone geoBone = null;
        this.isFlipping = false;
        GeoBone geoBone2 = geoModel.topLevelBones.get(0);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone2);
        MATRIX_STACK.moveToPivot(geoBone2);
        MATRIX_STACK.rotate(geoBone2);
        MATRIX_STACK.scale(geoBone2);
        MATRIX_STACK.moveBackFromPivot(geoBone2);
        for (GeoBone geoBone3 : geoBone2.childBones) {
            if ("pentagram".equals(geoBone3.getName())) {
                geoBone = geoBone3;
                continue;
            }
            this.renderRecursively(bufferBuilder, geoBone3, f2, f3, f4, f5);
        }
        Tessellator.getInstance().draw();
        float f6 = this.getCoinScale(f);
        this.currentTint = this.getCoinColor();
        if (!GalathMangTracker.debugEnabled) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f6, f6);
            GL11.glDisable(2896);
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        this.isFlipping = true;
        this.renderRecursively(bufferBuilder, geoBone, f2, f3, f4, f5);
        Tessellator.getInstance().draw();
        GL11.glEnable(2896);
        MATRIX_STACK.pop();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.resetColor();
    }

    float getCoinScale(float partialTicks) {
        if (mc.player.getHeldItemMainhand() != this.currentItemStack && mc.player.getHeldItemOffhand() != this.currentItemStack) {
            return this.getCoinBob(partialTicks);
        }
        long now = System.currentTimeMillis();
        NBTTagCompound nbt = GalathCoinRenderer.mc.player.getEntityData();
        long activationTime = nbt.getLong("sexmod:galath_coin_activation_time");
        long deactivationTime = nbt.getLong("sexmod:galath_coin_deactivation_time");
        if (activationTime != 0L) {
            return this.getCoinSpin(now, activationTime, partialTicks);
        }
        if (deactivationTime != 0L) {
            return this.getCoinFade(now, deactivationTime, partialTicks);
        }
        if (GalathMangTracker.debugEnabled) {
            return 120.0f;
        }
        return this.getCoinBob(partialTicks);
    }

    float getCoinFade(long now, long start, float partialTicks) {
        float elapsed = now - start;
        if (elapsed < 1000.0f) {
            return 120.0f;
        }
        if (elapsed <= 3000.0f) {
            return RotationHelper.LerpFloat(120.0f, 240.0f, (elapsed - 1000.0f) / 2000.0f);
        }
        return 240.0f;
    }

    float getCoinSpin(long now, long start, float f) {
        float elapsed = now - start;
        if (elapsed < 1000.0f) {
            return 240.0f;
        }
        if (elapsed <= 3000.0f) {
            return RotationHelper.LerpFloat(240.0f, 120.0f, (elapsed - 1000.0f) / 2000.0f);
        }
        return 120.0f;
    }

    Vector3fSexmodSpecial getCoinColor() {
        if (mc.player.getHeldItemMainhand() != this.currentItemStack && mc.player.getHeldItemOffhand() != this.currentItemStack) {
            return COIN_COLOR;
        }
        long now = System.currentTimeMillis();
        NBTTagCompound nbt = mc.player.getEntityData();
        long activationTime = nbt.getLong("sexmod:galath_coin_activation_time");
        long deactivationTime = nbt.getLong("sexmod:galath_coin_deactivation_time");
        if (activationTime != 0L) {
            return this.getCoinColorDark(activationTime, now);
        }
        if (deactivationTime != 0L) {
            return this.getCoinColor(deactivationTime, now);
        }
        if (GalathMangTracker.debugEnabled) {
            return COIN_COLOR_DARK;
        }
        return COIN_COLOR;
    }

    Vector3fSexmodSpecial getCoinColor(long start, long now) {
        float elapsed = now - start;
        if (elapsed < 1000.0f) {
            return GalathCoinRenderer.COIN_COLOR_DARK;
        }
        if (elapsed <= 3000.0f) {
            return RotationHelper.LerpVector3f(GalathCoinRenderer.COIN_COLOR_DARK, COIN_COLOR, (double)((elapsed - 1000.0f) / 2000.0f));
        }
        return COIN_COLOR;
    }

    Vector3fSexmodSpecial getCoinColorDark(long start, long now) {
        float elapsed = now - start;
        if (elapsed < 1000.0f) {
            return COIN_COLOR;
        }
        if (elapsed <= 3000.0f) {
            return RotationHelper.LerpVector3f(COIN_COLOR, GalathCoinRenderer.COIN_COLOR_DARK, (double)((elapsed - 1000.0f) / 2000.0f));
        }
        return GalathCoinRenderer.COIN_COLOR_DARK;
    }

    float getCoinBob(float f) {
        return (float)(60.0 * Math.sin(((float) GalathCoinRenderer.mc.player.ticksExisted + f) * 0.05f) + 180.0);
    }

    void renderCoinQuads(BufferBuilder bufferBuilder, GeoCube geoCube) {
        for (GeoQuad geoQuad : geoCube.quads) {
            if (geoQuad == null) continue;
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex(geoVertex.textureU, geoVertex.textureV).color(this.currentTint.x, this.currentTint.y, this.currentTint.z, 1.0f).endVertex();
            }
        }
    }

    @Override
    public void renderCube(BufferBuilder bufferBuilder, GeoCube geoCube, float f, float f2, float f3, float f4) {
        MATRIX_STACK.moveToPivot(geoCube);
        MATRIX_STACK.rotate(geoCube);
        MATRIX_STACK.moveBackFromPivot(geoCube);
        if (this.isFlipping) {
            this.renderCoinQuads(bufferBuilder, geoCube);
            return;
        }
        for (GeoQuad geoQuad : geoCube.quads) {
            if (geoQuad == null) continue;
            javax.vecmath.Vector3f vector3f = new javax.vecmath.Vector3f((float)geoQuad.normal.getX(), (float)geoQuad.normal.getY(), (float)geoQuad.normal.getZ());
            MATRIX_STACK.getNormalMatrix().transform((Tuple3f)vector3f);
            if ((geoCube.size.y == 0.0f || geoCube.size.z == 0.0f) && vector3f.getX() < 0.0f) {
                vector3f.x *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.z == 0.0f) && vector3f.getY() < 0.0f) {
                vector3f.y *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.y == 0.0f) && vector3f.getZ() < 0.0f) {
                vector3f.z *= -1.0f;
            }
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex(geoVertex.textureU, geoVertex.textureV).color(f, f2, f3, f4).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }

    //@Override
    //public void render(GeoModel geoModel, Object object, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, (cc_class124)object, f, f2, f3, f4, f5);
    //}
}

