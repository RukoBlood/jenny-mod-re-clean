/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold.KoboldEgg;

import com.trolmastercard.sexmod.girls.Kobold.EyeAndKoboldColor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class KoboldEggColor extends GeoItemRenderer<KoboldEggItem> {
    ItemStack a = null;

    public KoboldEggColor() {
        super(new KoboldEggModelAlt());
    }

    @Override
    public void render(KoboldEggItem koboldEggItem, ItemStack itemStack) {
        this.a = itemStack;
        super.render(koboldEggItem, itemStack);
    }

    @Override
    public void renderRecursively(BufferBuilder builder, GeoBone bone, float red, float green, float blue, float alpha) {
        String boneName = bone.getName();
        if ("shell".equals(boneName)) {
            red = (float) KoboldEggRenderer.b.getRed() / 255.0f;
            green = (float) KoboldEggRenderer.b.getGreen() / 255.0f;
            blue = (float) KoboldEggRenderer.b.getBlue() / 255.0f;
        }
        if ("colorSpots".equals(boneName)) {
            Vec3i vec3i = this.getColor(this.a).getMainColor();
            red = (float)vec3i.getX() / 255.0f;
            green = (float)vec3i.getY() / 255.0f;
            blue = (float)vec3i.getZ() / 255.0f;
        }
        super.renderRecursively(builder, bone, red, green, blue, alpha);
    }

    EyeAndKoboldColor getColor(ItemStack itemStack) {
        return EyeAndKoboldColor.getColorByWoolId(itemStack.getMetadata());
    }
}

