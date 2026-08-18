package com.trolmastercard.sexmod.companion.supporter;

import com.trolmastercard.sexmod.companion.CompanionBase;
import com.trolmastercard.sexmod.girls.base.GirlEntity;

public class SupporterCompanion extends CompanionBase {
    int LoseTargetTicks = 0;
    int PathfindCooldown = 0;

    public SupporterCompanion(GirlEntity girl) {
        super(girl);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.jumpMovementFactor = 0.02f;
    }

    @Override
    protected Mode updateMode() {
        boolean shoudFollow;
        float Dist = this.entity.getDistance(this.master);
        shoudFollow = Dist > 5.0f;

        if (this.entity.getInteractionPlayerUUID() == null && !shoudFollow && this.CurState == Mode.FOLLOW) {
            if (++this.LoseTargetTicks > 60) {
                this.LoseTargetTicks = 0;
            } else {
                shoudFollow = true;
            }
        }
        if (shoudFollow) {
            return Mode.FOLLOW;
        }
        return Mode.IDLE;
    }

    @Override
    protected void CompanionStates(Mode mode) {
        switch (mode) {
            case FOLLOW: {
                double dist = this.entity.getDistance(this.master);

                if ((double)this.navigator.getPathSearchRange() > dist) {
                    this.navigator.clearPath();
                    this.navigator.tryMoveToEntityLiving(this.master, 0.5);
                } else {
                    this.tpToPlayer();
                }

                this.PathfindCooldown = 300;
                this.setMovementSpeed();
                break;
            }
            case IDLE: {
                this.setMovementSpeed();
            }
        }
    }

    @Override
    protected double setMovementSpeed() {
        float dist = this.entity.getDistance(this.master);
        float baseFac = 0.02f;
        double SpeedBonus = Math.min(0.7, Math.floor(dist / 3.0f) * 0.05);
        this.entity.jumpMovementFactor = baseFac = (float)((double)baseFac + SpeedBonus);
        return baseFac;
    }
}

