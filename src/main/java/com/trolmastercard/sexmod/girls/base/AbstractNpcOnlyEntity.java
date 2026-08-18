/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.base;

import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractNpcOnlyEntity extends GirlEntity {
    final static public DataParameter<String> CURRENT_ACTION = EntityDataManager.createKey(AbstractNpcOnlyEntity.class, DataSerializers.STRING).getSerializer().createKey(119);
    final static public DataParameter<BlockPos> ACTION_TARGET_POS = EntityDataManager.createKey(AbstractNpcOnlyEntity.class, DataSerializers.BLOCK_POS).getSerializer().createKey(120);
    final static public DataParameter<String> APPEARANCE_DNA = EntityDataManager.createKey(AbstractNpcOnlyEntity.class, DataSerializers.STRING).getSerializer().createKey(121);
    String lastCachedAction = null;
    String lastCachedDNA = null;
    BlockPos lastCachedTargetPos = null;

    protected AbstractNpcOnlyEntity(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.world.isRemote && this.world instanceof FakeWorld) {
            return;
        }
        this.entityDataManager.register(APPEARANCE_DNA, this.generateAppearanceDNA(new StringBuilder()));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.tickClientDataCheck();
    }

    void tickClientDataCheck() {
        if (!this.world.isRemote) {
            return;
        }
        String liveAction = this.entityDataManager.get(CURRENT_ACTION);
        String liveDNA = this.entityDataManager.get(APPEARANCE_DNA);
        BlockPos liveTargetPos = this.entityDataManager.get(ACTION_TARGET_POS);
        if (this.lastCachedAction == null) {
            this.lastCachedAction = liveAction;
            this.lastCachedDNA = liveDNA;
            this.lastCachedTargetPos = liveTargetPos;
            return;
        }
        if (!(this.lastCachedDNA.equals(liveDNA) && this.lastCachedAction.equals(liveAction) && this.lastCachedTargetPos.equals(liveTargetPos))) {
            this.onDataWatcherUpdate();
        }
        this.lastCachedAction = liveAction;
        this.lastCachedDNA = liveDNA;
        this.lastCachedTargetPos = liveTargetPos;
    }

    protected abstract void onDataWatcherUpdate();

    protected abstract String generateAppearanceDNA(StringBuilder dnaBuilder);

    public static void appendPaddedNumber2(StringBuilder dnaBuilder, int value) {
        if (value < 10) {
            dnaBuilder.append(0);
        }
        dnaBuilder.append(value);
        dnaBuilder.append("-");
    }

    public static void appendPaddedNumber(StringBuilder stringBuilder, int bound) {
        int randomVal = ReferenceAndRotationHelper.RANDOM.nextInt(bound + 1);
        if (randomVal < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(randomVal);
        stringBuilder.append("-");
    }

    public static void appendGaussianBodyGene(StringBuilder dnaBuilder) {
        double randDouble = ReferenceAndRotationHelper.RANDOM.nextDouble();
        double gaussianVal = Math.pow(Math.E, -Math.pow(-2.5 + 5.0 * randDouble, 2.0));
        String formattedString = String.format("%.2f", gaussianVal);
        String[] splitParts = formattedString.split("\\.");
        if (splitParts.length < 2) {
            splitParts = formattedString.split(",");
        }
        formattedString = splitParts[1];
        dnaBuilder.append(formattedString).append("-");
    }

    public static void appendRandomGeneExclusive(StringBuilder dnaBuilder, int bound) {
        int randomVal = ReferenceAndRotationHelper.RANDOM.nextInt(bound);
        if (randomVal < 10) {
            dnaBuilder.append(0);
        }
        dnaBuilder.append(randomVal);
        dnaBuilder.append("-");
    }

    public static String[] getModelCodeParts(GirlEntity girl) {
        return girl.getDataManager().get(APPEARANCE_DNA).split("-");
    }
    
}

