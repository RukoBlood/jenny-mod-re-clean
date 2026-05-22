/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class e8_class231
extends ModelBase {
    final private ModelRenderer b;
    final private ModelRenderer a = new ModelRenderer(this, "glass");

    public e8_class231() {
        this.a.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.b = new ModelRenderer(this, "cube");
        this.b.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        this.a.render(f6);
        GlStateManager.scale(0.875f, 0.875f, 0.875f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        this.a.render(f6);
        GlStateManager.scale(0.875f, 0.875f, 0.875f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        this.b.render(f6);
        GlStateManager.popMatrix();
    }
}

