/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.items.IItemHandler
 *  net.minecraftforge.items.SlotItemHandler
 */
package com.trolmastercard.sexmod;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class fe_class305
extends SlotItemHandler {
    b_inner307 a;

    public fe_class305(b_inner307 b_inner3072, IItemHandler iItemHandler, int n, int n2, int n3) {
        super(iItemHandler, n, n2, n3);
        this.a = b_inner3072;
    }

    public static boolean a(ItemStack itemStack, int n) {
        return fe_class305.a(itemStack, b_inner307.a(n));
    }

    public boolean isItemValid(ItemStack itemStack) {
        return fe_class305.a(itemStack, this.a);
    }

    static boolean a(ItemStack itemStack, b_inner307 b_inner3072) {
        Item item = itemStack.getItem();
        switch (b_inner3072) {
            case WEAPON: {
                return item instanceof ItemSword || item instanceof ItemTool;
            }
            case BOW: {
                return item instanceof ItemBow;
            }
            case HELMET: {
                return item instanceof ItemArmor && ((ItemArmor)item).armorType == EntityEquipmentSlot.HEAD;
            }
            case CHEST_PLATE: {
                return item instanceof ItemArmor && ((ItemArmor)item).armorType == EntityEquipmentSlot.CHEST;
            }
            case PANTS: {
                return item instanceof ItemArmor && ((ItemArmor)item).armorType == EntityEquipmentSlot.LEGS;
            }
            case SHOES: {
                return item instanceof ItemArmor && ((ItemArmor)item).armorType == EntityEquipmentSlot.FEET;
            }
            case ROD: {
                return item instanceof ItemFishingRod;
            }
        }
        return false;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static enum b_inner307 {
        WEAPON(0),
        BOW(1),
        HELMET(2),
        CHEST_PLATE(3),
        PANTS(4),
        SHOES(5),
        ROD(6);

        public int id;

        public static b_inner307 a(int n) {
            switch (n) {
                case 0: {
                    return WEAPON;
                }
                case 1: {
                    return BOW;
                }
                case 2: {
                    return HELMET;
                }
                case 3: {
                    return CHEST_PLATE;
                }
                case 4: {
                    return PANTS;
                }
                case 5: {
                    return SHOES;
                }
                case 6: {
                    return ROD;
                }
            }
            throw new NullPointerException("Girls don't have a slot nr. " + n);
        }

        private b_inner307(int n2) {
            this.id = n2;
        }

        private static NullPointerException a(NullPointerException nullPointerException) {
            return nullPointerException;
        }
    }
}

