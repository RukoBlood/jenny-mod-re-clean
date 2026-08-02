/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Jenny;

import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/*
* Related to PlayerJenny
* */
public class JennyHand extends ModelBase implements IRenderer {
    final private ModelRenderer renderer;

    public JennyHand() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.renderer = new ModelRenderer(this);
        this.renderer.setRotationPoint(-5.0f, 2.5f, 0.0f);
        this.renderer.cubeList.add(new ModelBox(this.renderer, 0, 0, -2.0f, -6.0f, 0.0f, 2, 6, 2, 0.0f, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.renderer.render(scale);
    }

    public void SetRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public ModelRenderer Render() {
        return this.renderer;
    }
}

