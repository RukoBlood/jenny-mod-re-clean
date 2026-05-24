/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.Handlers;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.Galath.GalathCoin;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEggItem;
import com.trolmastercard.sexmod.girls.Luna.LunaRod;

public class ItemHandler {
    public static void RegisterItems() {
        HornyPotion.RegisterPotion();
        LampItem.RegisterLamp();
        DragonStaff.RegisterStaff();
        TribeEgg.RegisterTribeEgg();
        GalathCoin.RegisterCoin();
        EditorWand.RegisterWand();
        KoboldEggItem.RegisterEggItem();
        Fire.RegisterFire();
        LunaRod.RegisterRod();
    }
}

