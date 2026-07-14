/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class e4_class223 extends GirlEntity {
    final static public DataParameter<String> N = EntityDataManager.createKey(e4_class223.class, DataSerializers.STRING).getSerializer().createKey(119);
    final static public DataParameter<BlockPos> K = EntityDataManager.createKey(e4_class223.class, DataSerializers.BLOCK_POS).getSerializer().createKey(120);
    final static public DataParameter<String> M = EntityDataManager.createKey(e4_class223.class, DataSerializers.STRING).getSerializer().createKey(121);
    String P = null;
    String O = null;
    BlockPos L = null;

    protected e4_class223(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.world.isRemote && this.world instanceof FakeWorld) {
            return;
        }
        this.entityDataManager.register(M, this.a(new StringBuilder()));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.void_c();
    }

    void void_c() {
        if (!this.world.isRemote) {
            return;
        }
        String string = this.entityDataManager.get(N);
        String string2 = this.entityDataManager.get(M);
        BlockPos blockPos = this.entityDataManager.get(K);
        if (this.P == null) {
            this.P = string;
            this.O = string2;
            this.L = blockPos;
            return;
        }
        if (!(this.O.equals(string2) && this.P.equals(string) && this.L.equals(blockPos))) {
            this.void_a();
        }
        this.P = string;
        this.O = string2;
        this.L = blockPos;
    }

    protected abstract void void_a();

    protected abstract String a(StringBuilder var1);

    public static void c(StringBuilder stringBuilder, int n) {
        if (n < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(n);
        stringBuilder.append("-");
    }

    public static void void_a(StringBuilder stringBuilder, int n) {
        int n2 = Reference.RANDOM.nextInt(n + 1);
        if (n2 < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(n2);
        stringBuilder.append("-");
    }

    public static void void_b(StringBuilder stringBuilder) {
        double d = Reference.RANDOM.nextDouble();
        double d2 = Math.pow(Math.E, -Math.pow(-2.5 + 5.0 * d, 2.0));
        String string = String.format("%.2f", d2);
        String[] stringArray = string.split("\\.");
        if (stringArray.length < 2) {
            stringArray = string.split(",");
        }
        string = stringArray[1];
        stringBuilder.append(string).append("-");
    }

    public static void b(StringBuilder stringBuilder, int n) {
        int n2 = Reference.RANDOM.nextInt(n);
        if (n2 < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(n2);
        stringBuilder.append("-");
    }

    public static String[] java_lang_String_arr_a(GirlEntity em_class2582) {
        return em_class2582.getDataManager().get(M).split("-");
    }
    
}

