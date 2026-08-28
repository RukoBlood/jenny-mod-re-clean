/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.DragonStaff;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class CrystalModel extends ModelBase {
    final private ModelRenderer inner;
    final private ModelRenderer outer = new ModelRenderer(this, "glass");

    public CrystalModel() {
        this.outer.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.inner = new ModelRenderer(this, "cube");
        this.inner.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.rotate(limbSwingAmount, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        this.outer.render(scale);
        GlStateManager.scale(0.875f, 0.875f, 0.875f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(limbSwingAmount, 0.0f, 1.0f, 0.0f);
        this.outer.render(scale);
        GlStateManager.scale(0.875f, 0.875f, 0.875f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(limbSwingAmount, 0.0f, 1.0f, 0.0f);
        this.inner.render(scale);
        GlStateManager.popMatrix();
    }
}

