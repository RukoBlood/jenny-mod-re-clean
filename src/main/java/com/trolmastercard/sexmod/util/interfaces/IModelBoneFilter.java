/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.interfaces;

import java.util.HashSet;

import com.trolmastercard.sexmod.gx_class390;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.CheckReturnValue;

public interface IModelBoneFilter {
    @CheckReturnValue
    default public HashSet<String> getBlacklistedBoneNames() {
        return gx_class390.a;
    }

    @CheckReturnValue
    default public boolean isBoneAllowed(HashSet<String> hashSet, GeoBone geoBone) {
        while (geoBone.parent != null) {
            String boneName = geoBone.getName();
            if (hashSet.contains(boneName)) {
                return false;
            }
            if (boneName.startsWith("armor")) {
                return false;
            }
            geoBone = geoBone.parent;
        }
        return true;
    }
}
