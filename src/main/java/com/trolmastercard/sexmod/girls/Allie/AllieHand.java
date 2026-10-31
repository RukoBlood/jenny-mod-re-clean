/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Allie;

import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AllieHand extends ModelBase implements IRenderer {
    final private ModelRenderer bone;

    public AllieHand() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.bone = new ModelRenderer(this);
        this.bone.setRotationPoint(-5.0f, 2.5f, 0.0f);
        this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -2.0f, -6.0f, 0.0f, 2, 6, 2, 0.0f, false));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float Scale) {
        this.bone.render(Scale);
    }

    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float v) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = v;
    }

    @Override
    public ModelRenderer Render() {
        return this.bone;
    }
}

