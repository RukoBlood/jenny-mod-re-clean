/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Bia;

import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
//go.class
public class BiaHand extends ModelBase implements IRenderer {
    final public ModelRenderer a;

    public BiaHand() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.a = new ModelRenderer(this);
        this.a.setRotationPoint(-5.0f, 2.5f, 0.0f);
        this.a.cubeList.add(new ModelBox(this.a, 0, 0, -2.0f, -6.0f, 0.0f, 2, 6, 2, 0.0f, false));
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float scale) {
        this.a.render(scale);
    }

    public void rotate(ModelRenderer m, float x, float y, float z) {
        m.rotateAngleX = x;
        m.rotateAngleY = y;
        m.rotateAngleZ = z;
    }

    @Override
    public ModelRenderer Render() {
        return this.a;
    }
}

