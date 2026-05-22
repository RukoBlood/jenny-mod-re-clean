/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class aa_class20
extends EntityAINearestAttackableTarget<KoboldEntity> {
    final private int a;
    final private boolean b;

    public aa_class20(EntityCreature entityCreature, boolean bl, boolean bl2) {
        this(entityCreature, bl, false, bl2);
    }

    public aa_class20(EntityCreature entityCreature, boolean bl, boolean bl2, boolean bl3) {
        this(entityCreature, 10, bl, bl2, null, bl3);
    }

    public aa_class20(EntityCreature entityCreature, int n, boolean bl, boolean bl2, @Nullable Predicate predicate, boolean bl3) {
        super(entityCreature, KoboldEntity.class, n, bl, bl2, predicate);
        this.a = n;
        this.b = bl3;
    }

    @Override
    public boolean shouldExecute() {
        float f;
        if (this.b && (f = this.taskOwner.getBrightness()) >= 0.5f) {
            return false;
        }
        if (this.a > 0 && this.taskOwner.getRNG().nextInt(this.a) != 0) {
            return false;
        }
        List<KoboldEntity> list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        }
        ArrayList<KoboldEntity> arrayList = new ArrayList<>();
        for (KoboldEntity ff_class3082 : list) {
            if (!ff_class3082.boolean_J()) continue;
            arrayList.add(ff_class3082);
        }
        if (arrayList.isEmpty()) {
            return false;
        }
        arrayList.sort(this.sorter);
        this.targetEntity = arrayList.get(0);
        return true;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

