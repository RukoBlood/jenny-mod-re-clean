/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.b6_class67;
import com.trolmastercard.sexmod.c3_class112;
import com.trolmastercard.sexmod.cj_class134;
import com.trolmastercard.sexmod.ck_class135;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.vecmath.Vector3f;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class gx_class390 {
    final static public Vec3d c = new Vec3d(0.95, 0.65, 0.85);
    final static public Vec3d e = new Vec3d(0.0, 0.2, 0.3);
    final static public float f = 0.1f;
    final static public HashSet<String> a = new HashSet<String>(){
        {
            this.add("boobs");
            this.add("booty");
            this.add("vagina");
            this.add("fuckhole");
        }
    };
    static protected HashMap<c3_class112, HashMap<String, Boolean>> d = new HashMap();
    static public Vec3d b;

    static boolean a(c3_class112 c3_class1122, GeoBone geoBone) {
        HashMap<String, Boolean> hashMap = d.get(c3_class1122);
        if (hashMap == null) {
            hashMap = new HashMap();
            boolean bl = c3_class1122.a(c3_class1122.a(), geoBone);
            hashMap.put(geoBone.getName(), bl);
            d.put(c3_class1122, hashMap);
            return bl;
        }
        Boolean bl = hashMap.get(geoBone.getName());
        if (bl == null) {
            bl = c3_class1122.a(c3_class1122.a(), geoBone);
            hashMap.put(geoBone.getName(), bl);
            d.put(c3_class1122, hashMap);
            return bl;
        }
        return bl;
    }

    public static Vec3d a(c3_class112 c3_class1122, GeoBone geoBone, Vec3d vec3d, Vector3f vector3f) {
        if (!gx_class390.a(c3_class1122, geoBone)) {
            return vec3d;
        }
        return gx_class390.a(vec3d, vector3f, b);
    }

    public static Vec3d a(Vec3d vec3d, Vector3f vector3f, Vec3d vec3d2) {
        double d = ck_class135.a(vector3f, vec3d2);
        double d2 = b6_class67.e(Math.abs(d));
        return b6_class67.a(vec3d, d > 0.0 ? c : e, d2 *= (double)0.1f);
    }

    public static void a(EntityLivingBase entityLivingBase, float f) {
        b = cj_class134.a(entityLivingBase, f);
    }

    public static void a(List<IBone> list, HashSet<String> hashSet, c3_class112 c3_class1122) {
        if (d.get(c3_class1122) != null) {
            return;
        }
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        for (IBone iBone : list) {
            hashMap.put(iBone.getName(), c3_class1122.a(hashSet, (GeoBone)iBone));
        }
        d.put(c3_class1122, hashMap);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

