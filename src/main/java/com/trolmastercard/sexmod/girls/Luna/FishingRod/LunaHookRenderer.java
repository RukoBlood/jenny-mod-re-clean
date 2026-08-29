/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Luna.FishingRod;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class LunaHookRenderer
extends Render<LunaHookEntity> {
    final static double CONST_1 = 0.1896224320030116;
    final static double CONST_2 = -0.5;
    final static double CONST_3 = 0.08742380916962415;
    final static private ResourceLocation particles = new ResourceLocation("textures/particle/particles.png");

    public LunaHookRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    // a
    @Override
    public void doRender(LunaHookEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        LunaEntity luna = entity.getOwnerLuna();
        if (luna != null && !this.renderOutlines && luna.throwBackPercentage != 1.0f) {
            luna.fishEntity = entity;
            ItemStack itemStack = luna.getDataManager().get(LunaEntity.CAUGHT_ITEM);
            if (!itemStack.getItem().equals(Items.AIR)) {
                float fps = Minecraft.getDebugFPS();
                if (fps == 0.0f) {
                    fps = 0.1f;
                }
                luna.throwBackPercentage += 60.0f / fps * 0.01666f * 2.0f;
                luna.throwBackPercentage = Math.min(1.0f, luna.throwBackPercentage);
                EntityPlayer player = Minecraft.getMinecraft().player;
                Vec3d vec3d = RotationHelper.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), partialTicks);
                Vec3d pos = new Vec3d(x, y, z);
                Vec3d vec3d2 = RotationHelper.LerpVec3d(new Vec3d(luna.lastTickPosX, luna.lastTickPosY + 0.875, luna.lastTickPosZ), luna.getPositionVector().add(0.0, 0.875, 0.0), partialTicks);
                vec3d2 = vec3d2.subtract(vec3d);
                pos = RotationHelper.LerpVec3d(pos, vec3d2, luna.throwBackPercentage);
                x = pos.x;
                y = pos.y;
                z = pos.z;
            } else {
                luna.throwBackPercentage = 0.0f;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y, (float) z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }
            if (!itemStack.getItem().equals(Items.AIR)) {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.translate(0.0f, -0.2f, 0.0f);
                Minecraft.getMinecraft().getItemRenderer().renderItem(luna, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
            this.bindEntityTexture(entity);
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            buffer.pos(-0.5, -0.5, 0.0).tex(0.0625, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
            buffer.pos(0.5, -0.5, 0.0).tex(0.125, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
            buffer.pos(0.5, 0.5, 0.0).tex(0.125, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
            buffer.pos(-0.5, 0.5, 0.0).tex(0.0625, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
            tessellator.draw();
            if (this.renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            int n = luna.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
            ItemStack heldItem = luna.getHeldItemMainhand();
            if (!(heldItem.getItem() instanceof ItemFishingRod)) {
                n = -n;
            }
            luna.rotationYaw = luna.getYawRotation();
            luna.renderYawOffset = luna.getYawRotation();
            luna.posX = luna.getTargetPosition().x;
            luna.posY = luna.getTargetPosition().y;
            luna.posZ = luna.getTargetPosition().z;
            luna.prevPosX = luna.getTargetPosition().x;
            luna.prevPosY = luna.getTargetPosition().y;
            luna.prevPosZ = luna.getTargetPosition().z;
            float f4 = (luna.prevRenderYawOffset + (luna.renderYawOffset - luna.prevRenderYawOffset) * partialTicks) * ((float) Math.PI / 180);
            double d4 = MathHelper.sin(f4);
            double d5 = MathHelper.cos(f4);
            double d6 = (double) n * 0.35;
            double d7 = luna.prevPosX + (luna.posX - luna.prevPosX) * (double) partialTicks - d5 * d6 - d4 * 0.8;
            double d8 = luna.prevPosY + (double) luna.getEyeHeight() + (luna.posY - luna.prevPosY) * (double) partialTicks - 0.45;
            double d9 = luna.prevPosZ + (luna.posZ - luna.prevPosZ) * (double) partialTicks - d4 * d6 + d5 * 0.8;
            double d10 = luna.isSneaking() ? -0.1875 : 0.0;
            double d11 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks - Math.sin((double) (luna.getYawRotation() + 90.0f) * (Math.PI / 180)) * 0.1896224320030116 - Math.sin((double) luna.getYawRotation() * (Math.PI / 180)) * 0.08742380916962415;
            double d12 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + 0.25 + -0.5;
            double d13 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks + Math.cos((double) (luna.getYawRotation() + 90.0f) * (Math.PI / 180)) * 0.1896224320030116 + Math.cos((double) luna.getYawRotation() * (Math.PI / 180)) * 0.08742380916962415;
            double d14 = (float) (d7 - d11);
            double d15 = (double) ((float) (d8 - d12)) + d10;
            double d16 = (float) (d9 - d13);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            if (itemStack.getItem().equals(Items.AIR)) {
                buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                for (int i = 0; i <= 16; ++i) {
                    float f5 = (float) i / 16.0f;
                    buffer.pos(x + d14 * (double) f5, y + d15 * (double) (f5 * f5 + f5) * 0.5 + 0.25, z + d16 * (double) f5).color(0, 0, 0, 255).endVertex();
                }
                tessellator.draw();
            }
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    //a
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LunaHookEntity entity) {
        return particles;
    }

    //@Override
    //@Nullable
    //protected ResourceLocation getEntityTexture(Entity entity) {
    //    return this.a((gi_class370)entity);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.a((gi_class370)entity, d, d2, d3, f, f2);
    //}
}

