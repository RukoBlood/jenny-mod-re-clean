/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Ellie;

import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
//cf.class
public class EllieHand extends ModelBase implements IRenderer {
    final private ModelRenderer a;
    final private ModelRenderer b;
    final private ModelRenderer c;

    public EllieHand() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.a = new ModelRenderer(this);
        this.a.setRotationPoint(-5.0f, 1.5708f, 0.0f);
        this.b = new ModelRenderer(this);
        this.b.setRotationPoint(-1.0f, -3.0f, 1.0f);
        this.a.addChild(this.b);
        this.rotate(this.b, 0.0f, 1.5708f, 0.0f);
        this.b.cubeList.add(new ModelBox(this.b, 0, 0, -1.0f, -3.0f, -1.0f, 2, 6, 2, 0.0f, false));
        this.c = new ModelRenderer(this);
        this.c.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float scale) {
        this.a.render(scale);
        this.c.render(scale);
    }

    public void rotate(ModelRenderer modelRenderer, float f, float f2, float f3) {
        modelRenderer.rotateAngleX = f;
        modelRenderer.rotateAngleY = f2;
        modelRenderer.rotateAngleZ = f3;
    }

    @Override
    public ModelRenderer Render() {
        return this.a;
    }
}

