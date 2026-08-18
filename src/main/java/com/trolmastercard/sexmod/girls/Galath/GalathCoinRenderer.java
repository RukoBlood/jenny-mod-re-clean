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
package com.trolmastercard.sexmod.girls.Galath;

import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
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
    final static public Vector3fSexmodSpecial e = new Vector3fSexmodSpecial(0.84705883f, 0.11764706f, 0.35686275f);
    final static public Vector3fSexmodSpecial f = new Vector3fSexmodSpecial(0.44705883f, 0.44705883f, 0.44705883f);
    final static public float b = 240.0f;
    final static public float g = 120.0f;
    final static float h = 0.05f;
    final static Minecraft a = Minecraft.getMinecraft();
    boolean c = false;
    Vector3fSexmodSpecial d;

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
        this.c = false;
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
        float f6 = this.a(f);
        this.d = this.a();
        if (!GalathMangTracker.f) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f6, f6);
            GL11.glDisable(2896);
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        this.c = true;
        this.renderRecursively(bufferBuilder, geoBone, f2, f3, f4, f5);
        Tessellator.getInstance().draw();
        GL11.glEnable(2896);
        MATRIX_STACK.pop();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.resetColor();
    }

    float a(float f) {
        if (GalathCoinRenderer.a.player.getHeldItemMainhand() != this.currentItemStack && GalathCoinRenderer.a.player.getHeldItemOffhand() != this.currentItemStack) {
            return this.b(f);
        }
        long l = System.currentTimeMillis();
        NBTTagCompound nBTTagCompound = GalathCoinRenderer.a.player.getEntityData();
        long l2 = nBTTagCompound.getLong("sexmod:galath_coin_activation_time");
        long l3 = nBTTagCompound.getLong("sexmod:galath_coin_deactivation_time");
        if (l2 != 0L) {
            return this.a(l, l2, f);
        }
        if (l3 != 0L) {
            return this.b(l, l3, f);
        }
        if (GalathMangTracker.f) {
            return 120.0f;
        }
        return this.b(f);
    }

    float b(long l, long l2, float f) {
        float f2 = l - l2;
        if (f2 < 1000.0f) {
            return 120.0f;
        }
        if (f2 <= 3000.0f) {
            return ReferenceAndRotationHelper.LerpFloat(120.0f, 240.0f, (f2 - 1000.0f) / 2000.0f);
        }
        return 240.0f;
    }

    float a(long l, long l2, float f) {
        float f2 = l - l2;
        if (f2 < 1000.0f) {
            return 240.0f;
        }
        if (f2 <= 3000.0f) {
            return ReferenceAndRotationHelper.LerpFloat(240.0f, 120.0f, (f2 - 1000.0f) / 2000.0f);
        }
        return 120.0f;
    }

    Vector3fSexmodSpecial a() {
        if (GalathCoinRenderer.a.player.getHeldItemMainhand() != this.currentItemStack && GalathCoinRenderer.a.player.getHeldItemOffhand() != this.currentItemStack) {
            return e;
        }
        long l = System.currentTimeMillis();
        NBTTagCompound nBTTagCompound = GalathCoinRenderer.a.player.getEntityData();
        long l2 = nBTTagCompound.getLong("sexmod:galath_coin_activation_time");
        long l3 = nBTTagCompound.getLong("sexmod:galath_coin_deactivation_time");
        if (l2 != 0L) {
            return this.b(l2, l);
        }
        if (l3 != 0L) {
            return this.a(l3, l);
        }
        if (GalathMangTracker.f) {
            return f;
        }
        return e;
    }

    Vector3fSexmodSpecial a(long l, long l2) {
        float f = l2 - l;
        if (f < 1000.0f) {
            return GalathCoinRenderer.f;
        }
        if (f <= 3000.0f) {
            return ReferenceAndRotationHelper.LerpVector3f(GalathCoinRenderer.f, e, (double)((f - 1000.0f) / 2000.0f));
        }
        return e;
    }

    Vector3fSexmodSpecial b(long l, long l2) {
        float f = l2 - l;
        if (f < 1000.0f) {
            return e;
        }
        if (f <= 3000.0f) {
            return ReferenceAndRotationHelper.LerpVector3f(e, GalathCoinRenderer.f, (double)((f - 1000.0f) / 2000.0f));
        }
        return GalathCoinRenderer.f;
    }

    float b(float f) {
        return (float)(60.0 * Math.sin(((float) GalathCoinRenderer.a.player.ticksExisted + f) * 0.05f) + 180.0);
    }

    void a(BufferBuilder bufferBuilder, GeoCube geoCube) {
        for (GeoQuad geoQuad : geoCube.quads) {
            if (geoQuad == null) continue;
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex(geoVertex.textureU, geoVertex.textureV).color(this.d.x, this.d.y, this.d.z, 1.0f).endVertex();
            }
        }
    }

    @Override
    public void renderCube(BufferBuilder bufferBuilder, GeoCube geoCube, float f, float f2, float f3, float f4) {
        MATRIX_STACK.moveToPivot(geoCube);
        MATRIX_STACK.rotate(geoCube);
        MATRIX_STACK.moveBackFromPivot(geoCube);
        if (this.c) {
            this.a(bufferBuilder, geoCube);
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

