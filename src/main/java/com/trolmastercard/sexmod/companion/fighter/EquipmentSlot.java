/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.items.IItemHandler
 *  net.minecraftforge.items.SlotItemHandler
 */
package com.trolmastercard.sexmod.companion.fighter;

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

public class EquipmentSlot extends SlotItemHandler {
    GirlEquipmentType slotType;

    public EquipmentSlot(GirlEquipmentType type, IItemHandler itemHandler, int index, int xPos, int yPos) {
        super(itemHandler, index, xPos, yPos);
        this.slotType = type;
    }

    public static boolean isSlotValidForItems(ItemStack itemStack, int n) {
        return EquipmentSlot.isItemValidForSlot(itemStack, GirlEquipmentType.getTypeByIndex(n));
    }

    public boolean isItemValid(ItemStack itemStack) {
        return EquipmentSlot.isItemValidForSlot(itemStack, this.slotType);
    }

    static boolean isItemValidForSlot(ItemStack stack, GirlEquipmentType type) {
        Item item = stack.getItem();
        switch (type) {
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


    public static enum GirlEquipmentType {
        WEAPON(0),
        BOW(1),
        HELMET(2),
        CHEST_PLATE(3),
        PANTS(4),
        SHOES(5),
        ROD(6);

        public int id;

        public static GirlEquipmentType getTypeByIndex(int index) {
            switch (index) {
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
            throw new NullPointerException("Girls don't have a slot nr. " + index);
        }

        private GirlEquipmentType(int id) {
            this.id = id;
        }

    }
}

