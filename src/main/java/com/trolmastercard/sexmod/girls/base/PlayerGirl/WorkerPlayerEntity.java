/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.base.PlayerGirl;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*Used by playerGoblin and PlayerKobold*/
public abstract class WorkerPlayerEntity extends PlayerGirl {
    final static public DataParameter<String> MODEL_CODE = EntityDataManager.createKey(WorkerPlayerEntity.class, DataSerializers.STRING).getSerializer().createKey(119);
    final static public DataParameter<BlockPos> WORK_POS = EntityDataManager.createKey(WorkerPlayerEntity.class, DataSerializers.BLOCK_POS).getSerializer().createKey(120);
    final static public DataParameter<String> DNA_CODE = EntityDataManager.createKey(WorkerPlayerEntity.class, DataSerializers.STRING).getSerializer().createKey(121);
    boolean isFirstInit = true;
    String cachedModelCode = null;
    String cachedDnaCode = null;
    BlockPos cachedWorkPos = null;

    protected WorkerPlayerEntity(World world) {
        super(world);
    }

    protected WorkerPlayerEntity(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (!this.world.isRemote || !(this.world instanceof FakeWorld)) {
            this.entityDataManager.register(DNA_CODE, this.buildModelCodeDNA(new StringBuilder()));
        }
    }

    protected abstract String buildModelCodeDNA(StringBuilder builder);

    public static String[] getModelCodeParts(GirlEntity buildModelCodeDNA) {
        return buildModelCodeDNA.getDataManager().get(DNA_CODE).split("-");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.syncModelCodeClient();
        if (this.isFirstInit) {
            if (this.world.isRemote) {
                this.ResetColors();
                this.isFirstInit = true;
                return;
            }
            EntityPlayer player = this.getOwnerPlayer();
            if (player != null) {
                String modelCode = player.getEntityData().getString("sexmod:GirlSpecific" + PlayerGirlEntity.getGirlType(this));
                this.isFirstInit = false;
                if (!modelCode.isEmpty()) {
                    this.setCustomPartList(WorkerPlayerEntity.decodePartIdList(modelCode));
                }
            }
        }
    }

    void syncModelCodeClient() {
        if (this.world.isRemote) {
            String currentModelCode = this.entityDataManager.get(MODEL_CODE);
            String currentDnaCode = this.entityDataManager.get(DNA_CODE);
            BlockPos currentWorkPos = this.entityDataManager.get(WORK_POS);
            if (this.cachedModelCode != null) {
                if (!(this.cachedDnaCode.equals(currentDnaCode) && this.cachedModelCode.equals(currentModelCode) && this.cachedWorkPos.equals(currentWorkPos))) {
                    this.ResetColors();
                }
            }
            this.cachedModelCode = currentModelCode;
            this.cachedDnaCode = currentDnaCode;
            this.cachedWorkPos = currentWorkPos;
        }
    }

    protected abstract void ResetColors();
}

