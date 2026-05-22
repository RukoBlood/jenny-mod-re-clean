/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ez_class281
extends ParticleDragonBreath {
    final static public float a = 0.2f;
    final static public float c = 0.5f;
    static public float b = 0.2f;

    public ez_class281(World world, double d, double d2, double d3) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
    }

    @Override
    public void renderParticle(BufferBuilder bufferBuilder, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.particleScale = b;
        float f7 = (float)this.particleTextureIndexX / 16.0f;
        float f8 = f7 + 0.0624375f;
        float f9 = (float)this.particleTextureIndexY / 16.0f;
        float f10 = f9 + 0.0624375f;
        float f11 = 0.1f * this.particleScale;
        if (this.particleTexture != null) {
            f7 = this.particleTexture.getMinU();
            f8 = this.particleTexture.getMaxU();
            f9 = this.particleTexture.getMinV();
            f10 = this.particleTexture.getMaxV();
        }
        float f12 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
        float f13 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
        float f14 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
        int n = this.getBrightnessForRender(f);
        int n2 = n >> 16 & 0xFFFF;
        int n3 = n & 0xFFFF;
        Vec3d[] vec3dArray = new Vec3d[]{new Vec3d(-f2 * f11 - f5 * f11, -f3 * f11, -f4 * f11 - f6 * f11), new Vec3d(-f2 * f11 + f5 * f11, f3 * f11, -f4 * f11 + f6 * f11), new Vec3d(f2 * f11 + f5 * f11, f3 * f11, f4 * f11 + f6 * f11), new Vec3d(f2 * f11 - f5 * f11, -f3 * f11, f4 * f11 - f6 * f11)};
        if (this.particleAngle != 0.0f) {
            float f15 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * f;
            float f16 = MathHelper.cos(f15 * 0.5f);
            float f17 = MathHelper.sin(f15 * 0.5f) * (float)ez_class281.cameraViewDir.x;
            float f18 = MathHelper.sin(f15 * 0.5f) * (float)ez_class281.cameraViewDir.y;
            float f19 = MathHelper.sin(f15 * 0.5f) * (float)ez_class281.cameraViewDir.z;
            Vec3d vec3d = new Vec3d(f17, f18, f19);
            for (int i = 0; i < 4; ++i) {
                vec3dArray[i] = vec3d.scale(2.0 * vec3dArray[i].dotProduct(vec3d)).add(vec3dArray[i].scale((double)(f16 * f16) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(vec3dArray[i]).scale(2.0f * f16));
            }
        }
        bufferBuilder.pos((double)f12 + vec3dArray[0].x, (double)f13 + vec3dArray[0].y, (double)f14 + vec3dArray[0].z).tex(f8, f10).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        bufferBuilder.pos((double)f12 + vec3dArray[1].x, (double)f13 + vec3dArray[1].y, (double)f14 + vec3dArray[1].z).tex(f8, f9).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        bufferBuilder.pos((double)f12 + vec3dArray[2].x, (double)f13 + vec3dArray[2].y, (double)f14 + vec3dArray[2].z).tex(f7, f9).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        bufferBuilder.pos((double)f12 + vec3dArray[3].x, (double)f13 + vec3dArray[3].y, (double)f14 + vec3dArray[3].z).tex(f7, f10).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

