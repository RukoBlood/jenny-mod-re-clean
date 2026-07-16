/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SlimeLimb extends ModelBase implements IRenderer {
    final private ModelRenderer mdl;

    public SlimeLimb() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.mdl = new ModelRenderer(this);
        this.mdl.setRotationPoint(-5.0f, 2.5f, 0.0f);
        this.mdl.cubeList.add(new ModelBox(this.mdl, 0, 0, -2.0f, -6.0f, 0.0f, 2, 6, 2, 0.0f, false));
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.mdl.render(f6);
    }

    public void ModelRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public ModelRenderer Render() {
        return this.mdl;
    }
}

