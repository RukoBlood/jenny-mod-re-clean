/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

public class h_class395
extends f_class282 {
    int j = 0;
    int i = 0;

    public h_class395(GirlEntity em_class2582) {
        super(em_class2582);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.jumpMovementFactor = 0.02f;
    }

    @Override
    protected States abstractStates() {
        boolean bl;
        float f = this.entity.getDistance(this.player);
        boolean bl2 = bl = f > 5.0f;
        if (this.entity.getID() == null && !bl && this.f == States.FOLLOW) {
            if (++this.j > 60) {
                bl = false;
                this.j = 0;
            } else {
                bl = true;
            }
        }
        if (bl) {
            return States.FOLLOW;
        }
        return States.IDLE;
    }

    @Override
    protected void CompanionStates(States a_inner2832) {
        switch (a_inner2832) {
            case FOLLOW: {
                double d = this.entity.getDistance(this.player);
                if ((double)this.pathNavigate.getPathSearchRange() > d) {
                    this.pathNavigate.clearPath();
                    this.pathNavigate.tryMoveToEntityLiving(this.player, 0.5);
                } else {
                    this.c();
                }
                this.i = 300;
                this.double_b();
                break;
            }
            case IDLE: {
                this.double_b();
            }
        }
    }

    @Override
    protected double double_b() {
        float f = this.entity.getDistance(this.player);
        float f2 = 0.02f;
        double d = Math.min(0.7, Math.floor(f / 3.0f) * 0.05);
        this.entity.jumpMovementFactor = f2 = (float)((double)f2 + d);
        return f2;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

