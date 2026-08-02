/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Bia;

import java.util.HashSet;

import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BiaRenderer
extends GirlRenderer {
    public BiaRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        return new HashSet<String>(){
            {
                this.add("boobs");
                this.add("booty");
                this.add("vagina");
                this.add("fuckhole");
                this.add("leaf7");
                this.add("leaf8");
            }
        };
    }
}

