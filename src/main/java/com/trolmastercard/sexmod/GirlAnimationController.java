/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.function.Function;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.easing.EasingType;

public class GirlAnimationController<T extends IAnimatable> extends AnimationController<T> {
    public GirlAnimationController(T animatable, String name, float transitionLengthTicks, AnimationController.IAnimationPredicate<T> animationPredicate) {
        super(animatable, name, transitionLengthTicks, animationPredicate);
    }

    public GirlAnimationController(T animatable, String name, float transitionLengthTicks, EasingType easingType, AnimationController.IAnimationPredicate<T> iAnimationPredicate) {
        super(animatable, name, transitionLengthTicks, easingType, iAnimationPredicate);
    }

    public GirlAnimationController(T animatable, String name, float transitionLengthTicks, Function<Double, Double> easingFunction, AnimationController.IAnimationPredicate<T> iAnimationPredicate) {
        super(animatable, name, transitionLengthTicks, easingFunction, iAnimationPredicate);
    }
}

