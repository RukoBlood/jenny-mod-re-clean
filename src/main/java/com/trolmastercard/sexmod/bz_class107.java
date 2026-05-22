/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.function.Function;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.easing.EasingType;

public class bz_class107<T extends IAnimatable>
extends AnimationController<T> {
    public bz_class107(T t, String string, float f, AnimationController.IAnimationPredicate<T> iAnimationPredicate) {
        super(t, string, f, iAnimationPredicate);
    }

    public bz_class107(T t, String string, float f, EasingType easingType, AnimationController.IAnimationPredicate<T> iAnimationPredicate) {
        super(t, string, f, easingType, iAnimationPredicate);
    }

    public bz_class107(T t, String string, float f, Function<Double, Double> function, AnimationController.IAnimationPredicate<T> iAnimationPredicate) {
        super(t, string, f, function, iAnimationPredicate);
    }
}

