/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.girls.Luna;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.gc_class360;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlRenderer;
import com.trolmastercard.sexmod.util.GeckoMatrixBridge;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class LunaRenderer
extends GirlRenderer {
    float r;

    public LunaRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    protected ItemStack net_minecraft_item_ItemStack_a(@Nullable ItemStack itemStack) {
        switch (this.j.currentAction()) {
            case FISHING_IDLE: 
            case FISHING_START: {
                ItemStack itemStack2 = ((LunaEntity)this.j).ao;
                ItemStack itemStack3 = this.j.getDataManager().get(LunaEntity.az);
                if (itemStack3.equals(ItemStack.EMPTY)) {
                    return itemStack2;
                }
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack3);
                EnchantmentHelper.setEnchantments(map, itemStack2);
                this.j.setHeldItem(EnumHand.MAIN_HAND, itemStack2);
                return itemStack2;
            }
        }
        return itemStack;
    }

    boolean boolean_a() {
        return this.j.getDataManager().get(GirlEntity.G);
    }

    @Override
    protected void a(BufferBuilder bufferBuilder, String string, GeoBone geoBone) {
        if (Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        switch (string) {
            case "head": {
                this.r = geoBone.getRotationX();
                break;
            }
            case "backHair": {
                if (this.boolean_a()) break;
                double d = this.r / gc_class360.c(45.0f);
                float f = (float) Reference.LerpDouble(0.0, 0.75, d);
                geoBone.setPositionZ(f);
                geoBone.setPositionY(f);
                geoBone.setRotationX(-this.r);
                break;
            }
            case "sideHairR": 
            case "sideHairL": {
                if (this.boolean_a()) break;
                double d = this.r / gc_class360.c(45.0f);
                float f = (float) Reference.LerpDouble(0.0, (double)1.3f, d);
                geoBone.setPositionZ(-f);
                geoBone.setPositionY(f);
            }
            case "frontHairL": 
            case "frontHairR": {
                if (this.boolean_a()) break;
                geoBone.setRotationX(-this.r);
                break;
            }
            case "offhand": {
                LunaEntity eb_class2362 = (LunaEntity)this.j;
                ItemStack itemStack = this.j.getDataManager().get(LunaEntity.ag);
                if (itemStack.equals(ItemStack.EMPTY) || eb_class2362.Z != 1.0f) break;
                GlStateManager.pushMatrix();
                Tessellator.getInstance().draw();
                GeckoMatrixBridge.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, geoBone);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.scale(eb_class2362.aa, eb_class2362.aa, eb_class2362.aa);
                Minecraft.getMinecraft().getItemRenderer().renderItem(this.j, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
                GirlRenderer.n.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
                GlStateManager.popMatrix();
            }
        }
    }
}

