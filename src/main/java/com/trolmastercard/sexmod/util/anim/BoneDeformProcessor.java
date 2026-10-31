package com.trolmastercard.sexmod.util.anim;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.vecmath.Vector3f;

import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IGirlRenderer;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

/**
 * Applies physics-like deformation to model bones during rendering.
 *
 * <p>The processor evaluates whether a given bone is allowed by an
 * {@link IGirlRenderer} filter and, if so, offsets its position based on the
 * bone's rotation relative to a global influence vector (typically derived from
 * the light direction). This produces a subtle elastic deformation effect on
 * approved bones.</p>
 *
 * <p>Bone approval results are cached per renderer to avoid recomputing the
 * filter for every frame.</p>
 */
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
    static protected HashMap<IGirlRenderer, HashMap<String, Boolean>> filterResultCache = new HashMap<>();
    static public Vec3d globalInfluenceVector;

    /**
     * Returns whether the given bone is allowed by the renderer's filter,
     * caching the result for future lookups.
     *
     * @param filter the renderer providing the bone filter
     * @param bone the bone to check
     * @return {@code true} if the bone is allowed, {@code false} otherwise
     */
    static boolean checkAndCacheBoneApproval(IGirlRenderer filter, GeoBone bone) {
        HashMap<String, Boolean> boneMap = filterResultCache.get(filter);
        if (boneMap == null) {
            boneMap = new HashMap<>();
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

    /**
     * Computes a deformed position for the given bone, or returns the original
     * position when the bone is not approved by the renderer's filter.
     *
     * @param filter the renderer providing the bone filter
     * @param bone the bone being rendered
     * @param originPos the original bone position
     * @param rotation the bone rotation
     * @return the deformed position, or {@code originPos} if the bone is not allowed
     */
    public static Vec3d applyBoneDeformation(IGirlRenderer filter, GeoBone bone, Vec3d originPos, Vector3f rotation) {
        return !BoneDeformProcessor.checkAndCacheBoneApproval(filter, bone)
                ? originPos
                : BoneDeformProcessor.calculatePhysicsVector(originPos, rotation, globalInfluenceVector);
    }

    /**
     * Calculates the deformed position of a bone based on its rotation and the
     * global influence vector.
     *
     * <p>The dot product between the rotation and the influence vector decides
     * which modifier vector the position is interpolated towards. Its absolute
     * value is eased and scaled by {@link #ELASTICITY_FACTOR} to produce a
     * smooth, elastic-looking offset.</p>
     *
     * @param origin the original bone position
     * @param rotation the bone rotation
     * @param influence the global influence vector
     * @return the deformed bone position
     */
    public static Vec3d calculatePhysicsVector(Vec3d origin, Vector3f rotation, Vec3d influence) {
        double dotProduct = VectorMath.dotProduct(rotation, influence);
        double easedProduct = RotationHelper.EaseOutQuart(Math.abs(dotProduct));
        return RotationHelper.LerpVec3d(origin, dotProduct > 0.0 ? MASS_CENTER_MODIFIER : COMPENSATE_VECTOR, easedProduct * ELASTICITY_FACTOR);
    }

    /**
     * Updates the global influence vector based on the light direction relative
     * to the given entity.
     *
     * @param entity the entity being rendered
     * @param partialTicks the current frame partial tick time
     */
    public static void updateGlobalInfluence(EntityLivingBase entity, float partialTicks) {
        globalInfluenceVector = WorldUtils.getLightDirectionVector(entity, partialTicks);
    }

    /**
     * Pre-populates the bone approval cache for the given renderer.
     *
     * <p>Evaluates every bone in the provided list once and stores the result,
     * so subsequent calls to {@link #checkAndCacheBoneApproval} avoid
     * recomputing the filter during rendering.</p>
     *
     * @param boneList the bones to pre-warm
     * @param blacklistedBones the blacklisted bone names provided by the renderer
     * @param filter the renderer whose cache is being populated
     */
    public static void preWarmFilterCache(List<IBone> boneList, HashSet<String> blacklistedBones, IGirlRenderer filter) {
        if (filterResultCache.get(filter) == null) {
            HashMap<String, Boolean> preWarmedMap = new HashMap<String, Boolean>();
            for (IBone iBone : boneList) {
                preWarmedMap.put(iBone.getName(), filter.isBoneAllowed(blacklistedBones, (GeoBone) iBone));
            }
            filterResultCache.put(filter, preWarmedMap);
        }
    }
}

