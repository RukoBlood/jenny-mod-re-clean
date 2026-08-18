/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.vecmath.Vector3f;

import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IModelBoneFilter;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class BoneDeformProcessor {
    final static public Vec3d MASS_CENTER_MODIFIER = new Vec3d(0.95, 0.65, 0.85);
    final static public Vec3d COMPENSATE_VECTOR = new Vec3d(0.0, 0.2, 0.3);
    final static public float ELASTICITY_FACTOR = 0.1f;
    final static public HashSet<String> EXCLUDED_MESH_BONES = new HashSet<String>(){
        {
            this.add("boobs");
            this.add("booty");
            this.add("vagina");
            this.add("fuckhole");
        }
    };
    static protected HashMap<IModelBoneFilter, HashMap<String, Boolean>> filterResultCache = new HashMap();
    static public Vec3d globalInfluenceVector;

    static boolean checkAndCacheBoneApproval(IModelBoneFilter filter, GeoBone bone) {
        HashMap<String, Boolean> boneMap = filterResultCache.get(filter);
        if (boneMap == null) {
            boneMap = new HashMap();
            boolean isAllowed = filter.isBoneAllowed(filter.getBlacklistedBoneNames(), bone);
            boneMap.put(bone.getName(), isAllowed);
            filterResultCache.put(filter, boneMap);
            return isAllowed;
        }
        Boolean cachedResult = boneMap.get(bone.getName());
        if (cachedResult == null) {
            cachedResult = filter.isBoneAllowed(filter.getBlacklistedBoneNames(), bone);
            boneMap.put(bone.getName(), cachedResult);
            filterResultCache.put(filter, boneMap);
            return cachedResult;
        }
        return cachedResult;
    }

    public static Vec3d applyBoneDeformation(IModelBoneFilter filter, GeoBone bone, Vec3d originPos, Vector3f rotation) {
        if (!BoneDeformProcessor.checkAndCacheBoneApproval(filter, bone)) {
            return originPos;
        }
        return BoneDeformProcessor.calculatePhysicsVector(originPos, rotation, globalInfluenceVector);
    }

    public static Vec3d calculatePhysicsVector(Vec3d origin, Vector3f rotation, Vec3d influende) {
        double d = VectorMath.dotProduct(rotation, influende);
        double d2 = ReferenceAndRotationHelper.EaseOutQuart(Math.abs(d));
        return ReferenceAndRotationHelper.LerpVec3d(origin, d > 0.0 ? MASS_CENTER_MODIFIER : COMPENSATE_VECTOR, d2 *= (double)ELASTICITY_FACTOR);
    }

    public static void updateGlobalInfluence(EntityLivingBase entity, float partialTicks) {
        globalInfluenceVector = WorldUtils.getLightDirectionVector(entity, partialTicks);
    }

    public static void preWarmFilterCache(List<IBone> boneList, HashSet<String> blacklistedBones, IModelBoneFilter filter) {
        if (filterResultCache.get(filter) != null) {
            return;
        }
        HashMap<String, Boolean> preWarmedMap = new HashMap<String, Boolean>();
        for (IBone iBone : boneList) {
            preWarmedMap.put(iBone.getName(), filter.isBoneAllowed(blacklistedBones, (GeoBone)iBone));
        }
        filterResultCache.put(filter, preWarmedMap);
    }
}

