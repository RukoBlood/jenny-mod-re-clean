/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.interfaces;

import java.util.HashSet;

import com.trolmastercard.sexmod.BoneDeformProcessor;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.CheckReturnValue;

public interface IGirlRenderer {
    @CheckReturnValue
    default public HashSet<String> getBlacklistedBoneNames() {
        return BoneDeformProcessor.EXCLUDED_MESH_BONES;
    }

    @CheckReturnValue
    default public boolean isBoneAllowed(HashSet<String> hashSet, GeoBone bone) {
        while (bone.parent != null) {
            String boneName = bone.getName();
            if (hashSet.contains(boneName)) {
                return false;
            }
            if (boneName.startsWith("armor")) {
                return false;
            }
            bone = bone.parent;
        }
        return true;
    }
}
