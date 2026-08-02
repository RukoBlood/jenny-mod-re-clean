/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Slime;

import java.util.HashSet;

import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeRenderer
extends GirlRenderer {
    public SlimeRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        HashSet<String> hashSet = super.getBlacklistedBoneNames();
        hashSet.add("figure");
        return hashSet;
    }
}

