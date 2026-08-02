package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.base.GirlEntity;

public class SupporterCompanion extends BaseCompanionGoal {
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
    protected States getNewState() {
        boolean shoudFollow;
        float Dist = this.entity.getDistance(this.player);
        shoudFollow = Dist > 5.0f;

        if (this.entity.getID() == null && !shoudFollow && this.CurState == States.FOLLOW) {
            if (++this.LoseTargetTicks > 60) {
                this.LoseTargetTicks = 0;
            } else {
                shoudFollow = true;
            }
        }
        if (shoudFollow) {
            return States.FOLLOW;
        }
        return States.IDLE;
    }

    @Override
    protected void CompanionStates(States states) {
        switch (states) {
            case FOLLOW: {
                double dist = this.entity.getDistance(this.player);

                if ((double)this.pathNavigate.getPathSearchRange() > dist) {
                    this.pathNavigate.clearPath();
                    this.pathNavigate.tryMoveToEntityLiving(this.player, 0.5);
                } else {
                    this.goNearPlayer();
                }

                this.PathfindCooldown = 300;
                this.setGirlSpeed();
                break;
            }
            case IDLE: {
                this.setGirlSpeed();
            }
        }
    }

    @Override
    protected double setGirlSpeed() {
        float dist = this.entity.getDistance(this.player);
        float baseFac = 0.02f;
        double SpeedBonus = Math.min(0.7, Math.floor(dist / 3.0f) * 0.05);
        this.entity.jumpMovementFactor = baseFac = (float)((double)baseFac + SpeedBonus);
        return baseFac;
    }
}

