/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerLunaRenderer
extends PlayerGirlRenderer {
    float z = 0.0f;

    public PlayerLunaRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected void void_c() {
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.65f, 0.65f, 0.65f);
    }

    @Override
    protected ItemStack net_minecraft_item_ItemStack_a(@Nullable ItemStack itemStack) {
        switch (this.j.currentAction()) {
            case FISHING_IDLE: 
            case FISHING_START: {
                ItemStack itemStack2 = ((LunaEntity)this.j).ao;
                this.j.setHeldItem(EnumHand.MAIN_HAND, itemStack2);
                return itemStack2;
            }
        }
        return itemStack;
    }

    boolean boolean_b() {
        return this.j.getDataManager().get(GirlEntity.G);
    }

    @Override
    protected void a(String string, GeoBone geoBone) {
        if (Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        switch (string) {
            case "head": {
                this.z = geoBone.getRotationX();
                break;
            }
            case "backHair": {
                if (this.boolean_b() || !(this.z > 0.0f)) break;
                double d = this.z / gc_class360.c(45.0f);
                float f = (float)b6_class67.b(0.0, 0.75, d);
                geoBone.setPositionZ(f);
                geoBone.setPositionY(f);
                geoBone.setRotationX(-this.z);
                break;
            }
            case "frontHairL": 
            case "frontHairR": {
                if (this.boolean_b()) break;
                geoBone.setRotationX(-this.z);
            }
        }
    }

    @Override
    protected void a(boolean bl, ItemStack itemStack) {
        super.a(bl, itemStack);
        switch (itemStack.getItem().getItemUseAction(itemStack)) {
            case BLOCK: 
            case BOW: {
                break;
            }
            default: {
                GlStateManager.rotate(bl ? 60.0f : 150.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, 0.08, -0.05);
            }
        }
    }

    @Override
    protected void a(boolean bl) {
        GlStateManager.rotate(bl ? 60.0f : 150.0f, 1.0f, 0.0f, 0.0f);
        if (bl) {
            GlStateManager.translate(0.12, 0.0, 0.0);
        }
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        super.a(bl, bl2);
        if (!bl && bl2) {
            GlStateManager.rotate(120.0f, 0.0f, 1.0f, 0.0f);
            return;
        }
        if (!bl && !bl2) {
            GlStateManager.translate(0.0, 0.3, -0.15);
            GlStateManager.rotate(-45.0f, 1.0f, 0.0f, 0.0f);
            return;
        }
        if (bl && !bl2) {
            GlStateManager.translate(-0.025, -0.05, 0.0);
            return;
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

