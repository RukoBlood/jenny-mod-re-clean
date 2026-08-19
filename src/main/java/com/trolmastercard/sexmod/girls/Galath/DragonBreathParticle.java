/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Galath;

import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DragonBreathParticle extends ParticleDragonBreath {
    final static public float DEFAULT_ALPHA = 0.2f;
    final static public float MAX_LIFETIME_MODIFIER = 0.5f;
    static public float BREATH_SCALE = 0.2f;

    public DragonBreathParticle(World world, double x, double y, double z) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
    }

    @Override
    public void renderParticle(BufferBuilder buf, Entity entity, float partialTicks, float rotationX, float rotationY, float rotationZ, float rotationXY, float rotationXZ) {
        this.particleScale = BREATH_SCALE;
        float MinU = (float)this.particleTextureIndexX / 16.0f;
        float MaxU = MinU + 0.0624375f;
        float MinV = (float)this.particleTextureIndexY / 16.0f;
        float MaxV = MinV + 0.0624375f;
        float sizeFactor = 0.1f * this.particleScale;

        if (this.particleTexture != null) {
            MinU = this.particleTexture.getMinU();
            MaxU = this.particleTexture.getMaxU();
            MinV = this.particleTexture.getMinV();
            MaxV = this.particleTexture.getMaxV();
        }

        float renderX = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float renderY = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float renderZ = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int brightness = this.getBrightnessForRender(partialTicks);
        int skyLight = brightness >> 16 & 0xFFFF;
        int blockLight = brightness & 0xFFFF;

        Vec3d[] vertices = new Vec3d[]{
                new Vec3d(-rotationX * sizeFactor - rotationXY * sizeFactor, -rotationY * sizeFactor, -rotationZ * sizeFactor - rotationXZ * sizeFactor),
                new Vec3d(-rotationX * sizeFactor + rotationXY * sizeFactor, rotationY * sizeFactor, -rotationZ * sizeFactor + rotationXZ * sizeFactor),
                new Vec3d(rotationX * sizeFactor + rotationXY * sizeFactor, rotationY * sizeFactor, rotationZ * sizeFactor + rotationXZ * sizeFactor),
                new Vec3d(rotationX * sizeFactor - rotationXY * sizeFactor, -rotationY * sizeFactor, rotationZ * sizeFactor - rotationXZ * sizeFactor)
        };

        if (this.particleAngle != 0.0f) {
            float currentAngle = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float cosHalf = MathHelper.cos(currentAngle * 0.5f);
            float sinHalfX = MathHelper.sin(currentAngle * 0.5f) * (float) DragonBreathParticle.cameraViewDir.x;
            float sinHalfY = MathHelper.sin(currentAngle * 0.5f) * (float) DragonBreathParticle.cameraViewDir.y;
            float sinHalfZ = MathHelper.sin(currentAngle * 0.5f) * (float) DragonBreathParticle.cameraViewDir.z;
            Vec3d rotVector = new Vec3d(sinHalfX, sinHalfY, sinHalfZ);

            for (int i = 0; i < 4; ++i) {
                vertices[i] = rotVector.scale(2.0 * vertices[i].dotProduct(rotVector))
                        .add(vertices[i].scale((double)(cosHalf * cosHalf) - rotVector.dotProduct(rotVector)))
                        .add(rotVector.crossProduct(vertices[i]).scale(2.0f * cosHalf));
            }
        }

        buf.pos((double)renderX + vertices[0].x, (double)renderY + vertices[0].y, (double)renderZ + vertices[0].z).tex(MaxU, MaxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLight, blockLight).endVertex();
        buf.pos((double)renderX + vertices[1].x, (double)renderY + vertices[1].y, (double)renderZ + vertices[1].z).tex(MaxU, MinV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLight, blockLight).endVertex();
        buf.pos((double)renderX + vertices[2].x, (double)renderY + vertices[2].y, (double)renderZ + vertices[2].z).tex(MinU, MinV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLight, blockLight).endVertex();
        buf.pos((double)renderX + vertices[3].x, (double)renderY + vertices[3].y, (double)renderZ + vertices[3].z).tex(MinU, MaxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLight, blockLight).endVertex();
    }
}

