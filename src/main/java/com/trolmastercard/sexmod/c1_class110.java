/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.HashMap;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;

public class c1_class110<T extends IAnimatable>
extends AnimationProcessor<T> {
    HashMap<String, IBone> a = new HashMap();

    public c1_class110(IAnimatableModel iAnimatableModel) {
        super(iAnimatableModel);
    }

    @Override
    public IBone getBone(String string) {
        return this.a.get(string);
    }

    @Override
    public void registerModelRenderer(IBone iBone) {
        super.registerModelRenderer(iBone);
        this.a.put(iBone.getName(), iBone);
    }

    @Override
    public void clearModelRendererList() {
        super.clearModelRendererList();
        this.a.clear();
    }
}

