/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.gx_class390;
import java.util.HashSet;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.CheckReturnValue;

public interface c3_class112 {
    @CheckReturnValue
    default public HashSet<String> a() {
        return gx_class390.a;
    }

    @CheckReturnValue
    default public boolean a(HashSet<String> hashSet, GeoBone geoBone) {
        while (geoBone.parent != null) {
            String string = geoBone.getName();
            if (hashSet.contains(string)) {
                return false;
            }
            if (string.startsWith("armor")) {
                return false;
            }
            geoBone = geoBone.parent;
        }
        return true;
    }
}
