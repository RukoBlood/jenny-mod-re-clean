/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.companion.fighter;

import java.util.ArrayList;
import java.util.HashMap;

import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
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
public class DamageCalculation {
    public DamageCalculation() {
        Armor.addArmor(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.LEATHER, 1, 0);
        Armor.addArmor(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.GOLD, 2, 0);
        Armor.addArmor(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.CHAIN, 2, 0);
        Armor.addArmor(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.IRON, 2, 0);
        Armor.addArmor(EntityEquipmentSlot.HEAD, ItemArmor.ArmorMaterial.DIAMOND, 3, 3);
        Armor.addArmor(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.LEATHER, 3, 0);
        Armor.addArmor(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.GOLD, 5, 0);
        Armor.addArmor(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.CHAIN, 5, 0);
        Armor.addArmor(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.IRON, 6, 0);
        Armor.addArmor(EntityEquipmentSlot.CHEST, ItemArmor.ArmorMaterial.DIAMOND, 8, 3);
        Armor.addArmor(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.LEATHER, 2, 0);
        Armor.addArmor(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.GOLD, 3, 0);
        Armor.addArmor(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.CHAIN, 4, 0);
        Armor.addArmor(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.IRON, 5, 0);
        Armor.addArmor(EntityEquipmentSlot.LEGS, ItemArmor.ArmorMaterial.DIAMOND, 6, 3);
        Armor.addArmor(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.LEATHER, 1, 0);
        Armor.addArmor(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.GOLD, 1, 0);
        Armor.addArmor(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.CHAIN, 1, 0);
        Armor.addArmor(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.IRON, 2, 0);
        Armor.addArmor(EntityEquipmentSlot.FEET, ItemArmor.ArmorMaterial.DIAMOND, 3, 3);
    }

    @SubscribeEvent
    public void calculateDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Fighter)) {
            return;
        }
        Fighter girl = (Fighter)event.getEntity();
        //In jenny mod, slots 2-5 is reserved for armor
        ItemStack[] potentialArmors = new ItemStack[]{
                girl.inventory.getStackInSlot(2),
                girl.inventory.getStackInSlot(3),
                girl.inventory.getStackInSlot(4),
                girl.inventory.getStackInSlot(5)
        };

        ArrayList<ItemArmor> armorItems = new ArrayList<ItemArmor>();
        ArrayList<ItemStack> armorStacks = new ArrayList<ItemStack>();

        for (ItemStack stack : potentialArmors) {
            if (!(stack.getItem() instanceof ItemArmor)) continue;
            armorItems.add((ItemArmor)stack.getItem());
            armorStacks.add(stack);
        }
        if (armorItems.isEmpty()) {
            return;
        }
        DamageSource source = event.getSource();
        int totalArmorPoints = 0;
        int totalToughnessPoints = 0;
        if (!source.isUnblockable()) {
            for (ItemArmor itemArmor : armorItems) {
                totalArmorPoints += Armor.getDefencePoints(itemArmor.armorType, itemArmor.getArmorMaterial());
                totalToughnessPoints += Armor.getToughnessValue(itemArmor.armorType, itemArmor.getArmorMaterial());
            }
        }
        float finalDamage = event.getAmount();
        finalDamage *= 1.0f - Math.min(20.0f, Math.max((float)totalArmorPoints / 5.0f, (float)totalArmorPoints - 4.0f * finalDamage / ((float)totalToughnessPoints + 8.0f))) / 25.0f;
        float f2 = 0.0f;
        float f3 = finalDamage;
        for (ItemStack itemStack : armorStacks) {
            int enchantment;
            int n4 = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);
            finalDamage -= (float) n4 * 0.04f * finalDamage;
            int n5 = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, itemStack);
            f2 += ReferenceAndRotationHelper.RANDOM.nextFloat() < 0.15f * (float) n5 ? ReferenceAndRotationHelper.RANDOM.nextFloat() * 4.0f + 1.0f : 0.0f;
            f2 = Math.min(4.0f, f2);

            //Damage logic
            if (source.isFireDamage()) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, itemStack);
                finalDamage -= (float) enchantment * 0.08f * finalDamage;
            }
            if (source.isExplosion()) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, itemStack);
                finalDamage -= (float) enchantment * 0.08f * finalDamage;
            }
            if (source.damageType.equals("fall")) {
                enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack);
                finalDamage -= (float) enchantment * 0.12f * finalDamage;
            }


            if (!source.isProjectile()) continue;
            enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, itemStack);
            finalDamage -= (float) enchantment * 0.08f * finalDamage;
        }

        if (f2 > 0.0f && source instanceof EntityDamageSource && source.getTrueSource() != null) {
            source.getTrueSource().attackEntityFrom(DamageSource.causeThornsDamage(girl), f2);
        }
        event.setAmount(finalDamage);
    }

    static class Armor {
        static public HashMap<String, Integer[]> STATS = new HashMap();

        Armor() {
        }

        public static int getDefencePoints(EntityEquipmentSlot slot, ItemArmor.ArmorMaterial material) {
            try {
                return STATS.get(slot.toString() + material.toString())[0];
            } catch (NullPointerException nullPointerException) {
                return 3;
            }
        }

        public static int getToughnessValue(EntityEquipmentSlot slot, ItemArmor.ArmorMaterial material) {
            try {
                return STATS.get(slot.toString() + material.toString())[1];
            } catch (NullPointerException nullPointerException) {
                return 0;
            }
        }

        public static void addArmor(EntityEquipmentSlot slot, ItemArmor.ArmorMaterial material, int armor, int toughness) {
            STATS.put(slot.toString() + material.toString(), new Integer[]{armor, toughness});
        }
    }
}

