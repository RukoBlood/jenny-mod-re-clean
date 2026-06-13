/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.HashMap;

import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//gu.class
//Related to armor types
public class gu_class386 {
    public gu_class386() {
        ArmorDataBuilder.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.LEATHER, 1, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.GOLD, 2, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.CHAIN, 2, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.IRON, 2, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.DIAMOND, 3, 3);
        ArmorDataBuilder.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.LEATHER, 3, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.GOLD, 5, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.CHAIN, 5, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.IRON, 6, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.DIAMOND, 8, 3);
        ArmorDataBuilder.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.LEATHER, 2, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.GOLD, 3, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.CHAIN, 4, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.IRON, 5, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.DIAMOND, 6, 3);
        ArmorDataBuilder.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.LEATHER, 1, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.GOLD, 1, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.CHAIN, 1, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.IRON, 2, 0);
        ArmorDataBuilder.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.DIAMOND, 3, 3);
    }

    @SubscribeEvent
    public void a(LivingDamageEvent livingDamageEvent) {
        if (!(livingDamageEvent.getEntity() instanceof Fighter)) {
            return;
        }
        Fighter fighter = (Fighter)livingDamageEvent.getEntity();
        //In jenny mod, slots 2-5 is reserved for armor
        ItemStack[] inventory = new ItemStack[]{
                fighter.items.getStackInSlot(2),
                fighter.items.getStackInSlot(3),
                fighter.items.getStackInSlot(4),
                fighter.items.getStackInSlot(5)
        };

        ArrayList<ItemArmor> arrayList = new ArrayList<ItemArmor>();
        ArrayList<ItemStack> arrayList2 = new ArrayList<ItemStack>();
        for (ItemStack itemStack : inventory) {
            if (!(itemStack.getItem() instanceof ItemArmor)) continue;
            arrayList.add((ItemArmor)itemStack.getItem());
            arrayList2.add(itemStack);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        DamageSource damageSource = livingDamageEvent.getSource();
        int n = 0;
        int n2 = 0;
        if (!damageSource.isUnblockable()) {
            for (ItemArmor itemArmor : arrayList) {
                n += ArmorDataBuilder.a(itemArmor.armorType, itemArmor.getArmorMaterial());
                n2 += ArmorDataBuilder.b(itemArmor.armorType, itemArmor.getArmorMaterial());
            }
        }
        float damageAmount = livingDamageEvent.getAmount();
        damageAmount *= 1.0f - Math.min(20.0f, Math.max((float)n / 5.0f, (float)n - 4.0f * damageAmount / ((float)n2 + 8.0f))) / 25.0f;
        float f2 = 0.0f;
        float f3 = damageAmount;
        for (ItemStack itemStack : arrayList2) {
            int enchantment;
            int n4 = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);
            damageAmount -= (float) n4 * 0.04f * damageAmount;
            int n5 = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, itemStack);
            f2 += Reference.RANDOM.nextFloat() < 0.15f * (float) n5 ? Reference.RANDOM.nextFloat() * 4.0f + 1.0f : 0.0f;
            f2 = Math.min(4.0f, f2);

            //Damage logic
            if (damageSource.isFireDamage()) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, itemStack);
                damageAmount -= (float) enchantment * 0.08f * damageAmount;
            }
            if (damageSource.isExplosion()) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, itemStack);
                damageAmount -= (float) enchantment * 0.08f * damageAmount;
            }
            if (damageSource.damageType.equals("fall")) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack);
                damageAmount -= (float) enchantment * 0.12f * damageAmount;
            }


            if (!damageSource.isProjectile()) continue;
            enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, itemStack);
            damageAmount -= (float) enchantment * 0.08f * damageAmount;
        }

        if (f2 > 0.0f && damageSource instanceof EntityDamageSource && damageSource.getTrueSource() != null) {
            damageSource.getTrueSource().attackEntityFrom(DamageSource.causeThornsDamage(fighter), f2);
        }
        livingDamageEvent.setAmount(damageAmount);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    static class ArmorDataBuilder {
        static public HashMap<String, Integer[]> ArmorData = new HashMap();

        ArmorDataBuilder() {
        }

        public static int a(EntityEquipmentSlot entityEquipmentSlot, ItemArmor.ArmorMaterial armorMaterial) {
            try {
                return ArmorData.get(entityEquipmentSlot.toString() + armorMaterial.toString())[0];
            } catch (NullPointerException nullPointerException) {
                return 3;
            }
        }

        public static int b(EntityEquipmentSlot slot, ItemArmor.ArmorMaterial material) {
            try {
                return ArmorData.get(slot.toString() + material.toString())[1];
            } catch (NullPointerException nullPointerException) {
                return 0;
            }
        }

        public static void a(EntityEquipmentSlot slot, ItemArmor.ArmorMaterial material, int n, int n2) {
            ArmorData.put(slot.toString() + material.toString(), new Integer[]{n, n2});
        }
    }
}

