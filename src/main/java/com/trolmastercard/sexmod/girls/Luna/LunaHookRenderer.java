/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Luna;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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
    final static double b = 0.1896224320030116;
    final static double d = -0.5;
    final static double c = 0.08742380916962415;
    final static private ResourceLocation particles = new ResourceLocation("textures/particle/particles.png");

    public LunaHookRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    // a
    @Override
    public void doRender(LunaHookEntity gi_class3702, double d, double d2, double d3, float f, float f2) {
        Object object;
        Object object2;
        LunaEntity eb_class2362 = gi_class3702.g();
        if (eb_class2362 == null || this.renderOutlines || eb_class2362.Z == 1.0f) {
            return;
        }
        eb_class2362.lunaHookEntity = gi_class3702;
        ItemStack itemStack = eb_class2362.getDataManager().get(LunaEntity.ag);
        if (!itemStack.getItem().equals(Items.AIR)) {
            float f3 = Minecraft.getDebugFPS();
            if (f3 == 0.0f) {
                f3 = 0.1f;
            }
            eb_class2362.Z += 60.0f / f3 * 0.01666f * 2.0f;
            eb_class2362.Z = Math.min(1.0f, eb_class2362.Z);
            object2 = Minecraft.getMinecraft().player;
            Vec3d vec3d = Reference.LerpVec3d(new Vec3d(((EntityPlayer)object2).lastTickPosX, ((EntityPlayer)object2).lastTickPosY, ((EntityPlayer)object2).lastTickPosZ), ((Entity)object2).getPositionVector(), (double)f2);
            object = new Vec3d(d, d2, d3);
            Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(eb_class2362.lastTickPosX, eb_class2362.lastTickPosY + 0.875, eb_class2362.lastTickPosZ), eb_class2362.getPositionVector().add(0.0, 0.875, 0.0), (double)f2);
            vec3d2 = vec3d2.subtract(vec3d);
            object = Reference.LerpVec3d((Vec3d)object, vec3d2, (double)eb_class2362.Z);
            d = ((Vec3d)object).x;
            d2 = ((Vec3d)object).y;
            d3 = ((Vec3d)object).z;
        } else {
            eb_class2362.Z = 0.0f;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        this.bindEntityTexture(gi_class3702);
        Tessellator tessellator = Tessellator.getInstance();
        object2 = tessellator.getBuffer();
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(gi_class3702));
        }
        if (!itemStack.getItem().equals(Items.AIR)) {
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.translate(0.0f, -0.2f, 0.0f);
            Minecraft.getMinecraft().getItemRenderer().renderItem(eb_class2362, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
        }
        this.bindEntityTexture(gi_class3702);
        ((BufferBuilder)object2).begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        ((BufferBuilder)object2).pos(-0.5, -0.5, 0.0).tex(0.0625, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
        ((BufferBuilder)object2).pos(0.5, -0.5, 0.0).tex(0.125, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
        ((BufferBuilder)object2).pos(0.5, 0.5, 0.0).tex(0.125, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
        ((BufferBuilder)object2).pos(-0.5, 0.5, 0.0).tex(0.0625, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        int n = eb_class2362.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
        object = eb_class2362.getHeldItemMainhand();
        if (!(((ItemStack)object).getItem() instanceof ItemFishingRod)) {
            n = -n;
        }
        eb_class2362.rotationYaw = eb_class2362.java_lang_Float_I().floatValue();
        eb_class2362.renderYawOffset = eb_class2362.java_lang_Float_I().floatValue();
        eb_class2362.posX = eb_class2362.getTargetPosition().x;
        eb_class2362.posY = eb_class2362.getTargetPosition().y;
        eb_class2362.posZ = eb_class2362.getTargetPosition().z;
        eb_class2362.prevPosX = eb_class2362.getTargetPosition().x;
        eb_class2362.prevPosY = eb_class2362.getTargetPosition().y;
        eb_class2362.prevPosZ = eb_class2362.getTargetPosition().z;
        float f4 = (eb_class2362.prevRenderYawOffset + (eb_class2362.renderYawOffset - eb_class2362.prevRenderYawOffset) * f2) * ((float)Math.PI / 180);
        double d4 = MathHelper.sin(f4);
        double d5 = MathHelper.cos(f4);
        double d6 = (double)n * 0.35;
        double d7 = eb_class2362.prevPosX + (eb_class2362.posX - eb_class2362.prevPosX) * (double)f2 - d5 * d6 - d4 * 0.8;
        double d8 = eb_class2362.prevPosY + (double)eb_class2362.getEyeHeight() + (eb_class2362.posY - eb_class2362.prevPosY) * (double)f2 - 0.45;
        double d9 = eb_class2362.prevPosZ + (eb_class2362.posZ - eb_class2362.prevPosZ) * (double)f2 - d4 * d6 + d5 * 0.8;
        double d10 = eb_class2362.isSneaking() ? -0.1875 : 0.0;
        double d11 = gi_class3702.prevPosX + (gi_class3702.posX - gi_class3702.prevPosX) * (double)f2 - Math.sin((double)(eb_class2362.java_lang_Float_I().floatValue() + 90.0f) * (Math.PI / 180)) * 0.1896224320030116 - Math.sin((double)eb_class2362.java_lang_Float_I().floatValue() * (Math.PI / 180)) * 0.08742380916962415;
        double d12 = gi_class3702.prevPosY + (gi_class3702.posY - gi_class3702.prevPosY) * (double)f2 + 0.25 + -0.5;
        double d13 = gi_class3702.prevPosZ + (gi_class3702.posZ - gi_class3702.prevPosZ) * (double)f2 + Math.cos((double)(eb_class2362.java_lang_Float_I().floatValue() + 90.0f) * (Math.PI / 180)) * 0.1896224320030116 + Math.cos((double)eb_class2362.java_lang_Float_I().floatValue() * (Math.PI / 180)) * 0.08742380916962415;
        double d14 = (float)(d7 - d11);
        double d15 = (double)((float)(d8 - d12)) + d10;
        double d16 = (float)(d9 - d13);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (itemStack.getItem().equals(Items.AIR)) {
            ((BufferBuilder)object2).begin(3, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 16; ++i) {
                float f5 = (float)i / 16.0f;
                ((BufferBuilder)object2).pos(d + d14 * (double)f5, d2 + d15 * (double)(f5 * f5 + f5) * 0.5 + 0.25, d3 + d16 * (double)f5).color(0, 0, 0, 255).endVertex();
            }
            tessellator.draw();
        }
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        super.doRender(gi_class3702, d, d2, d3, f, f2);
    }

    //a
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LunaHookEntity gi_class3702) {
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

