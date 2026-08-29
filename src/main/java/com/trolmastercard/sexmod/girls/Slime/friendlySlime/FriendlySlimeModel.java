/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Slime.friendlySlime;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class FriendlySlimeModel extends ModelBase {
    final private ModelRenderer mdl1;
    final private ModelRenderer mdl4;
    final private ModelRenderer mdl5;
    final private ModelRenderer mdl3;
    final private ModelRenderer mdl2;

    public FriendlySlimeModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.mdl1 = new ModelRenderer(this);
        this.mdl1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.mdl1.cubeList.add(new ModelBox(this.mdl1, 0, 16, -3.0f, 17.0f, -3.0f, 6, 6, 6, 0.0f, true));
        this.mdl4 = new ModelRenderer(this);
        this.mdl4.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.mdl4.cubeList.add(new ModelBox(this.mdl4, 32, 0, 1.3f, 18.0f, -3.5f, 2, 2, 2, 0.0f, true));
        this.mdl5 = new ModelRenderer(this);
        this.mdl5.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.mdl5.cubeList.add(new ModelBox(this.mdl5, 32, 4, -3.3f, 18.0f, -3.5f, 2, 2, 2, 0.0f, true));
        this.mdl3 = new ModelRenderer(this);
        this.mdl3.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.mdl3.cubeList.add(new ModelBox(this.mdl3, 32, 8, -1.0f, 21.0f, -3.5f, 1, 1, 1, 0.0f, true));
        this.mdl2 = new ModelRenderer(this);
        this.mdl2.setRotationPoint(-0.5f, 0.0f, 0.1f);
        ModelRenderer modelRenderer = new ModelRenderer(this);
        modelRenderer.setRotationPoint(2.0f, 20.7406f, 4.0504f);
        this.mdl2.addChild(modelRenderer);
        this.Rotate(modelRenderer, 1.0908f, 0.0f, 0.0f);
        modelRenderer.cubeList.add(new ModelBox(modelRenderer, 10, 11, -2.5f, 0.0f, 0.0f, 2, 2, 1, 0.0f, false));
        ModelRenderer modelRenderer2 = new ModelRenderer(this);
        modelRenderer2.setRotationPoint(2.0f, 19.9214f, 3.4768f);
        this.mdl2.addChild(modelRenderer2);
        this.Rotate(modelRenderer2, 0.6109f, 0.0f, 0.0f);
        modelRenderer2.cubeList.add(new ModelBox(modelRenderer2, 10, 11, -3.0f, 0.0f, 0.0f, 3, 1, 1, 0.0f, false));
        ModelRenderer modelRenderer3 = new ModelRenderer(this);
        modelRenderer3.setRotationPoint(2.0f, 19.0074f, 3.0643f);
        this.mdl2.addChild(modelRenderer3);
        this.Rotate(modelRenderer3, 0.3491f, 0.0f, 0.0f);
        modelRenderer3.cubeList.add(new ModelBox(modelRenderer3, 10, 11, -4.0f, 0.0f, 0.075f, 5, 1, 1, 0.0f, false));
        ModelRenderer modelRenderer4 = new ModelRenderer(this);
        modelRenderer4.setRotationPoint(0.0f, 17.925f, 3.5f);
        this.mdl2.addChild(modelRenderer4);
        this.Rotate(modelRenderer4, 0.1309f, 0.0f, 0.0f);
        modelRenderer4.cubeList.add(new ModelBox(modelRenderer4, 10, 11, -3.0f, -1.0f, -0.5f, 7, 2, 1, 0.0f, false));
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.mdl1.render(f6);
        this.mdl4.render(f6);
        this.mdl5.render(f6);
        this.mdl3.render(f6);
        this.mdl2.render(f6);
    }

    public void Rotate(ModelRenderer modelRenderer, float f, float f2, float f3) {
        modelRenderer.rotateAngleX = f;
        modelRenderer.rotateAngleY = f2;
        modelRenderer.rotateAngleZ = f3;
    }
}

