/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util.Handlers;

import com.trolmastercard.sexmod.blocks.SexFire;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.Allie.lamp.LampItem;
import com.trolmastercard.sexmod.girls.Galath.GalathCoin.GalathCoin;
import com.trolmastercard.sexmod.girls.Kobold.DragonStaff.DragonStaffItem;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEgg.KoboldEggItem;
import com.trolmastercard.sexmod.girls.Kobold.TribeEgg.TribeEgg;
import com.trolmastercard.sexmod.girls.Luna.FishingRod.LunaRod;
import com.trolmastercard.sexmod.girls.base.EditorWand.EditorWand;

public class ItemHandler {
    public static void RegisterItems() {
        HornyPotion.RegisterHornyPotion();
        LampItem.RegisterLamp();
        DragonStaffItem.RegisterStaff();
        TribeEgg.RegisterTribeEgg();
        GalathCoin.RegisterCoin();
        EditorWand.RegisterWand();
        KoboldEggItem.RegisterEggItem();
        SexFire.RegisterFire();
        LunaRod.RegisterRod();
    }
}

