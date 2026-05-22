/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class hn_class403
extends GeoItemRenderer<KoboldEggItem> {
    ItemStack a = null;

    public hn_class403() {
        super(new KoboldEggModel());
    }

    @Override
    public void render(KoboldEggItem c7_class1162, ItemStack itemStack) {
        this.a = itemStack;
        super.render(c7_class1162, itemStack);
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        String string = geoBone.getName();
        if ("shell".equals(string)) {
            f = (float) KoboldEggRenderer.b.getRed() / 255.0f;
            f2 = (float) KoboldEggRenderer.b.getGreen() / 255.0f;
            f3 = (float) KoboldEggRenderer.b.getBlue() / 255.0f;
        }
        if ("colorSpots".equals(string)) {
            Vec3i vec3i = this.a(this.a).getMainColor();
            f = (float)vec3i.getX() / 255.0f;
            f2 = (float)vec3i.getY() / 255.0f;
            f3 = (float)vec3i.getZ() / 255.0f;
        }
        super.renderRecursively(bufferBuilder, geoBone, f, f2, f3, f4);
    }

    EyeAndKoboldColor a(ItemStack itemStack) {
        return EyeAndKoboldColor.getColorByWoolId(itemStack.getMetadata());
    }
}

