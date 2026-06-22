/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.world.World;

public class MultiPartHitbox extends MultiPartEntityPart {
    public boolean isCollidable = false;

    public MultiPartHitbox(World world) {
        super(null, "", 0.0f, 0.0f);
    }

    public MultiPartHitbox(IEntityMultiPart parent, String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isCollidable;
    }
}

