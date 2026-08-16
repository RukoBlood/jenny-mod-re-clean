/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 */
package com.trolmastercard.sexmod;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
//aj
public class NYIWinchesterItem extends Item implements IAnimatable {
    final static public NYIWinchesterItem NYI_WINCHESTER_ITEM = new NYIWinchesterItem();
    final private AnimationFactory animationFactory = new AnimationFactory(this);

    public static void Register() {
        NYI_WINCHESTER_ITEM.setRegistryName("sexmod", "winchester");
        NYI_WINCHESTER_ITEM.setTranslationKey("winchester");
        MinecraftForge.EVENT_BUS.register(NYIWinchesterItem.class);
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }
}

