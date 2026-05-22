/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class c__class120
extends ModelBase {
    final private ModelRenderer a;
    final private ModelRenderer d;
    final private ModelRenderer e;
    final private ModelRenderer c;
    final private ModelRenderer b;

    public c__class120() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.a = new ModelRenderer(this);
        this.a.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.a.cubeList.add(new ModelBox(this.a, 0, 16, -3.0f, 17.0f, -3.0f, 6, 6, 6, 0.0f, true));
        this.d = new ModelRenderer(this);
        this.d.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.d.cubeList.add(new ModelBox(this.d, 32, 0, 1.3f, 18.0f, -3.5f, 2, 2, 2, 0.0f, true));
        this.e = new ModelRenderer(this);
        this.e.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.e.cubeList.add(new ModelBox(this.e, 32, 4, -3.3f, 18.0f, -3.5f, 2, 2, 2, 0.0f, true));
        this.c = new ModelRenderer(this);
        this.c.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.c.cubeList.add(new ModelBox(this.c, 32, 8, -1.0f, 21.0f, -3.5f, 1, 1, 1, 0.0f, true));
        this.b = new ModelRenderer(this);
        this.b.setRotationPoint(-0.5f, 0.0f, 0.1f);
        ModelRenderer modelRenderer = new ModelRenderer(this);
        modelRenderer.setRotationPoint(2.0f, 20.7406f, 4.0504f);
        this.b.addChild(modelRenderer);
        this.a(modelRenderer, 1.0908f, 0.0f, 0.0f);
        modelRenderer.cubeList.add(new ModelBox(modelRenderer, 10, 11, -2.5f, 0.0f, 0.0f, 2, 2, 1, 0.0f, false));
        ModelRenderer modelRenderer2 = new ModelRenderer(this);
        modelRenderer2.setRotationPoint(2.0f, 19.9214f, 3.4768f);
        this.b.addChild(modelRenderer2);
        this.a(modelRenderer2, 0.6109f, 0.0f, 0.0f);
        modelRenderer2.cubeList.add(new ModelBox(modelRenderer2, 10, 11, -3.0f, 0.0f, 0.0f, 3, 1, 1, 0.0f, false));
        ModelRenderer modelRenderer3 = new ModelRenderer(this);
        modelRenderer3.setRotationPoint(2.0f, 19.0074f, 3.0643f);
        this.b.addChild(modelRenderer3);
        this.a(modelRenderer3, 0.3491f, 0.0f, 0.0f);
        modelRenderer3.cubeList.add(new ModelBox(modelRenderer3, 10, 11, -4.0f, 0.0f, 0.075f, 5, 1, 1, 0.0f, false));
        ModelRenderer modelRenderer4 = new ModelRenderer(this);
        modelRenderer4.setRotationPoint(0.0f, 17.925f, 3.5f);
        this.b.addChild(modelRenderer4);
        this.a(modelRenderer4, 0.1309f, 0.0f, 0.0f);
        modelRenderer4.cubeList.add(new ModelBox(modelRenderer4, 10, 11, -3.0f, -1.0f, -0.5f, 7, 2, 1, 0.0f, false));
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.a.render(f6);
        this.d.render(f6);
        this.e.render(f6);
        this.c.render(f6);
        this.b.render(f6);
    }

    public void a(ModelRenderer modelRenderer, float f, float f2, float f3) {
        modelRenderer.rotateAngleX = f;
        modelRenderer.rotateAngleY = f2;
        modelRenderer.rotateAngleZ = f3;
    }
}

