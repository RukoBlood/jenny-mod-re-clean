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
    final private ModelRenderer modelPart1;
    final private ModelRenderer modelPart2;
    final private ModelRenderer modelPart3;

    public EllieHand() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.modelPart1 = new ModelRenderer(this);
        this.modelPart1.setRotationPoint(-5.0f, 1.5708f, 0.0f);
        this.modelPart2 = new ModelRenderer(this);
        this.modelPart2.setRotationPoint(-1.0f, -3.0f, 1.0f);
        this.modelPart1.addChild(this.modelPart2);
        this.rotate(this.modelPart2, 0.0f, 1.5708f, 0.0f);
        this.modelPart2.cubeList.add(new ModelBox(this.modelPart2, 0, 0, -1.0f, -3.0f, -1.0f, 2, 6, 2, 0.0f, false));
        this.modelPart3 = new ModelRenderer(this);
        this.modelPart3.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float scale) {
        this.modelPart1.render(scale);
        this.modelPart3.render(scale);
    }

    public void rotate(ModelRenderer modelRenderer, float f, float f2, float f3) {
        modelRenderer.rotateAngleX = f;
        modelRenderer.rotateAngleY = f2;
        modelRenderer.rotateAngleZ = f3;
    }

    @Override
    public ModelRenderer Render() {
        return this.modelPart1;
    }
}

