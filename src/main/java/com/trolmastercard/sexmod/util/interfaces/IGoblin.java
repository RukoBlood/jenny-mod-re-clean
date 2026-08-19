/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.trolmastercard.sexmod.util.interfaces;

import com.trolmastercard.sexmod.girls.base.Action;

import java.util.UUID;
import javax.annotation.Nullable;

public interface IGoblin {
    @Nullable
    public UUID getOwnerUUID();

    public void setOwnerUUID(UUID uuid);

    public int getHeldPlayerDistance();

    public void setThrowProgress(int progress);

    public int getThrowProgress();

    public void setThrowTickCount(int ticks);

    public int getThrowTickCount();

    public void setPreviousAction(Action action);

    public Action getPreviousAction();

    public void setHeldPlayerDistance(int distance);
}

