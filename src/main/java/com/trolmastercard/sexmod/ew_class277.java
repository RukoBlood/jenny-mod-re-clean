/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ew_class277
extends PlayerGirl {
    final static public DataParameter<String> as = EntityDataManager.createKey(ew_class277.class, DataSerializers.STRING).getSerializer().createKey(119);
    final static public DataParameter<BlockPos> au = EntityDataManager.createKey(ew_class277.class, DataSerializers.BLOCK_POS).getSerializer().createKey(120);
    final static public DataParameter<String> at = EntityDataManager.createKey(ew_class277.class, DataSerializers.STRING).getSerializer().createKey(121);
    boolean ar = true;
    String ap = null;
    String av = null;
    BlockPos aq = null;

    protected ew_class277(World world) {
        super(world);
    }

    protected ew_class277(World world, UUID uUID) {
        super(world, uUID);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.world.isRemote && this.world instanceof FakeWorld) {
            return;
        }
        this.entityDataManager.register(at, this.a(new StringBuilder()));
    }

    protected abstract String a(StringBuilder var1);

    public static String[] java_lang_String_arr_a(GirlEntity em_class2582) {
        return em_class2582.getDataManager().get(at).split("-");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.void_b();
        if (!this.ar) {
            return;
        }
        if (this.world.isRemote) {
            this.void_a();
            this.ar = true;
            return;
        }
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return;
        }
        String string = entityPlayer.getEntityData().getString("sexmod:GirlSpecific" + (Object)((Object) PlayerGirlEntity.a(this)));
        this.ar = false;
        if (!"".equals(string)) {
            this.void_a(ew_class277.c(string));
        }
    }

    void void_b() {
        if (!this.world.isRemote) {
            return;
        }
        String string = this.entityDataManager.get(as);
        String string2 = this.entityDataManager.get(at);
        BlockPos blockPos = this.entityDataManager.get(au);
        if (this.ap == null) {
            this.ap = string;
            this.av = string2;
            this.aq = blockPos;
            return;
        }
        if (!(this.av.equals(string2) && this.ap.equals(string) && this.aq.equals(blockPos))) {
            this.void_a();
        }
        this.ap = string;
        this.av = string2;
        this.aq = blockPos;
    }

    protected abstract void void_a();

    private static RuntimeException d(RuntimeException runtimeException) {
        return runtimeException;
    }
}

