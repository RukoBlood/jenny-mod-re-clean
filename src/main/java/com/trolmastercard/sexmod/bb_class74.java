/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.world.World;

public class bb_class74
extends MultiPartEntityPart {
    public boolean a = false;

    public bb_class74(World world) {
        super(null, "", 0.0f, 0.0f);
    }

    public bb_class74(IEntityMultiPart iEntityMultiPart, String string, float f, float f2) {
        super(iEntityMultiPart, string, f, f2);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.a;
    }
}

