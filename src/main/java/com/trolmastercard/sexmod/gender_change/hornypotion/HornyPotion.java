/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.registry.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.gender_change.hornypotion;

import com.trolmastercard.sexmod.Packages.bd_class76;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class HornyPotion extends Potion {
    final static public Potion HORNY_POTION = new HornyPotion("horny potion", false, 16736968, 0, 0);
    final static public PotionType POTION_TYPE = (PotionType)new PotionType("horny_potion", new PotionEffect(HORNY_POTION, 3600), new PotionEffect(MobEffects.NAUSEA, 200, 1)).setRegistryName("horny_potion");

    public HornyPotion() {
        super(false, 0);
    }

    public HornyPotion(String name, boolean bl, int n, int n2, int n3) {
        super(bl, n);
        this.setPotionName(name);
        this.setIconIndex(n2, n3);
        this.setRegistryName(new ResourceLocation("sexmod:" + name));
    }

    public static void RegisterPotion() {
        ForgeRegistries.POTIONS.register(HORNY_POTION);
        ForgeRegistries.POTION_TYPES.register(POTION_TYPE);
        PotionHelper.addMix(PotionTypes.MUNDANE, Item.getItemFromBlock(Blocks.RED_FLOWER), POTION_TYPE);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent tickEvent) {
        EntityPlayer player = tickEvent.player;
        PotionEffect effect = player.getActivePotionEffect(HORNY_POTION);
        if (player.world.isRemote) {
            return;
        }
        if (effect == null) {
            return;
        }
        if (effect.getDuration() > 3500) {
            return;
        }
        player.removePotionEffect(HORNY_POTION);
        PackageHandler.INSTANCE.sendTo((IMessage)new bd_class76(player), (EntityPlayerMP)player);
    }

    @SubscribeEvent
    public void a(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        EntityAgeable entityAgeable;
        if (livingUpdateEvent.getEntity() instanceof EntityVillager && (entityAgeable = (EntityVillager)livingUpdateEvent.getEntity()).isPotionActive(HORNY_POTION)) {
            ((EntityVillager)entityAgeable).tasks.addTask(2, new EntityAIVillagerJustBangHerWithoutThinking((EntityVillager)entityAgeable));
            entityAgeable.removePotionEffect(HORNY_POTION);
        }
        if (!(livingUpdateEvent.getEntity() instanceof EntityAnimal)) {
            return;
        }
        entityAgeable = (EntityAnimal)livingUpdateEvent.getEntity();
        if (entityAgeable.isPotionActive(HORNY_POTION)) {
            if (entityAgeable.getGrowingAge() >= 0) {
                entityAgeable.setGrowingAge(0);
                ((EntityAnimal)entityAgeable).resetInLove();
                ((EntityAnimal)entityAgeable).setInLove(((EntityAnimal)entityAgeable).world.getClosestPlayerToEntity(entityAgeable, 30.0));
            }
            entityAgeable.removePotionEffect(HORNY_POTION);
        }
    }
}

