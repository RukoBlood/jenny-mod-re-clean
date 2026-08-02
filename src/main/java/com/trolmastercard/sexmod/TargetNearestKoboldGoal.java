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

import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class TargetNearestKoboldGoal extends EntityAINearestAttackableTarget<KoboldEntity> {
    final private int targetChance;
    final private boolean attackOnlyInDark;

    public TargetNearestKoboldGoal(EntityCreature goalOwner, boolean checkSight, boolean attackOnlyinDark) {
        this(goalOwner, checkSight, false, attackOnlyinDark);
    }

    public TargetNearestKoboldGoal(EntityCreature goalOwner, boolean checkSight, boolean nearbyOnly, boolean attackonlyindark) {
        this(goalOwner, 10, checkSight, nearbyOnly, null, attackonlyindark);
    }

    public TargetNearestKoboldGoal(EntityCreature entityCreature, int chance, boolean chackSight, boolean nearbyOnly, @Nullable Predicate targetSelector, boolean aoid) {
        super(entityCreature, KoboldEntity.class, chance, chackSight, nearbyOnly, targetSelector);
        this.targetChance = chance;
        this.attackOnlyInDark = aoid;
    }

    @Override
    public boolean shouldExecute() {
        float brightness;
        if (this.attackOnlyInDark && (brightness = this.taskOwner.getBrightness()) >= 0.5f) {
            return false;
        }
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        List<KoboldEntity> targets = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (targets.isEmpty()) {
            return false;
        }
        ArrayList<KoboldEntity> arrayList = new ArrayList<>();
        for (KoboldEntity kobold : targets) {
            if (!kobold.hasMaster()) continue;
            arrayList.add(kobold);
        }
        if (arrayList.isEmpty()) {
            return false;
        }
        arrayList.sort(this.sorter);
        this.targetEntity = arrayList.get(0);
        return true;
    }
}

