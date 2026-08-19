/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.anim;

import java.util.HashMap;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;

public class CachedAnimationProcessor<T extends IAnimatable> extends AnimationProcessor<T> {
    HashMap<String, IBone> boneCache = new HashMap();

    public CachedAnimationProcessor(IAnimatableModel animatableModel) {
        super(animatableModel);
    }

    @Override
    public IBone getBone(String boneName) {
        return this.boneCache.get(boneName);
    }

    @Override
    public void registerModelRenderer(IBone modelRenderer) {
        super.registerModelRenderer(modelRenderer);
        this.boneCache.put(modelRenderer.getName(), modelRenderer);
    }

    @Override
    public void clearModelRendererList() {
        super.clearModelRendererList();
        this.boneCache.clear();
    }
}

