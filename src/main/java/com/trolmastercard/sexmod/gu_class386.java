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

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class gu_class386 {
    public gu_class386() {
        a_inner387.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.LEATHER, 1, 0);
        a_inner387.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.GOLD, 2, 0);
        a_inner387.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.CHAIN, 2, 0);
        a_inner387.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.IRON, 2, 0);
        a_inner387.a(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.DIAMOND, 3, 3);
        a_inner387.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.LEATHER, 3, 0);
        a_inner387.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.GOLD, 5, 0);
        a_inner387.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.CHAIN, 5, 0);
        a_inner387.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.IRON, 6, 0);
        a_inner387.a(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.DIAMOND, 8, 3);
        a_inner387.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.LEATHER, 2, 0);
        a_inner387.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.GOLD, 3, 0);
        a_inner387.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.CHAIN, 4, 0);
        a_inner387.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.IRON, 5, 0);
        a_inner387.a(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.DIAMOND, 6, 3);
        a_inner387.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.LEATHER, 1, 0);
        a_inner387.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.GOLD, 1, 0);
        a_inner387.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.CHAIN, 1, 0);
        a_inner387.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.IRON, 2, 0);
        a_inner387.a(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.DIAMOND, 3, 3);
    }

    @SubscribeEvent
    public void a(LivingDamageEvent livingDamageEvent) {
        if (!(livingDamageEvent.getEntity() instanceof Fighter)) {
            return;
        }
        Fighter e2_class2182 = (Fighter)livingDamageEvent.getEntity();
        ItemStack[] itemStackArray = new ItemStack[]{e2_class2182.items.getStackInSlot(2), e2_class2182.items.getStackInSlot(3), e2_class2182.items.getStackInSlot(4), e2_class2182.items.getStackInSlot(5)};
        ArrayList<ItemArmor> arrayList = new ArrayList<ItemArmor>();
        ArrayList<ItemStack> arrayList2 = new ArrayList<ItemStack>();
        for (ItemStack itemStack : itemStackArray) {
            if (!(itemStack.getItem() instanceof ItemArmor)) continue;
            arrayList.add((ItemArmor)itemStack.getItem());
            arrayList2.add(itemStack);
        }
        if (arrayList.size() == 0) {
            return;
        }
        DamageSource damageSource = livingDamageEvent.getSource();
        int n = 0;
        int n2 = 0;
        if (!damageSource.isUnblockable()) {
            for (ItemArmor itemArmor : arrayList) {
                n += a_inner387.a(itemArmor.armorType, itemArmor.getArmorMaterial());
                n2 += a_inner387.b(itemArmor.armorType, itemArmor.getArmorMaterial());
            }
        }
        float f = livingDamageEvent.getAmount();
        f *= 1.0f - Math.min(20.0f, Math.max((float)n / 5.0f, (float)n - 4.0f * f / ((float)n2 + 8.0f))) / 25.0f;
        float f2 = 0.0f;
        float f3 = f;
        for (ItemStack itemStack : arrayList2) {
            int enchantment;
            int n4 = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);
            f -= (float) n4 * 0.04f * f;
            int n5 = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, itemStack);
            f2 += ModInfo.f.nextFloat() < 0.15f * (float) n5 ? ModInfo.f.nextFloat() * 4.0f + 1.0f : 0.0f;
            f2 = Math.min(4.0f, f2);
            if (damageSource.isFireDamage()) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, itemStack);
                f -= (float) enchantment * 0.08f * f;
            }
            if (damageSource.isExplosion()) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, itemStack);
                f -= (float) enchantment * 0.08f * f;
            }
            if (damageSource.damageType.equals("fall")) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack);
                f -= (float) enchantment * 0.12f * f;
            }
            if (!damageSource.isProjectile()) continue;
            enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, itemStack);
            f -= (float) enchantment * 0.08f * f;
        }

        if (f2 > 0.0f && damageSource instanceof EntityDamageSource && damageSource.getTrueSource() != null) {
            damageSource.getTrueSource().attackEntityFrom(DamageSource.causeThornsDamage(e2_class2182), f2);
        }
        livingDamageEvent.setAmount(f);
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    static class a_inner387 {
        static public HashMap<String, Integer[]> a = new HashMap();

        a_inner387() {
        }

        public static int a(EntityEquipmentSlot entityEquipmentSlot, ItemArmor.ArmorMaterial armorMaterial) {
            try {
                return a.get(entityEquipmentSlot.toString() + armorMaterial.toString())[0];
            } catch (NullPointerException nullPointerException) {
                return 3;
            }
        }

        public static int b(EntityEquipmentSlot entityEquipmentSlot, ItemArmor.ArmorMaterial armorMaterial) {
            try {
                return a.get(entityEquipmentSlot.toString() + armorMaterial.toString())[1];
            } catch (NullPointerException nullPointerException) {
                return 0;
            }
        }

        public static void a(EntityEquipmentSlot entityEquipmentSlot, ItemArmor.ArmorMaterial armorMaterial, int n, int n2) {
            a.put(entityEquipmentSlot.toString() + armorMaterial.toString(), new Integer[]{n, n2});
        }
    }
}

