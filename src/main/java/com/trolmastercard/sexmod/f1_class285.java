/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.at_class43;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class f1_class285
extends ModelBase
implements at_class43 {
    final private ModelRenderer a;

    public f1_class285() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.a = new ModelRenderer(this);
        this.a.setRotationPoint(-5.0f, 2.5f, 0.0f);
        this.a.cubeList.add(new ModelBox(this.a, 0, 0, -2.0f, -6.0f, 0.0f, 2, 6, 2, 0.0f, false));
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.a.render(f6);
    }

    public void a(ModelRenderer modelRenderer, float f, float f2, float f3) {
        modelRenderer.rotateAngleX = f;
        modelRenderer.rotateAngleY = f2;
        modelRenderer.rotateAngleZ = f3;
    }

    @Override
    public ModelRenderer a() {
        return this.a;
    }
}

