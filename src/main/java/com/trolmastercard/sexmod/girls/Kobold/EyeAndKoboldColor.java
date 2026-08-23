/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Kobold;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;

public enum EyeAndKoboldColor {
    GREEN(69, 141, 113, 91, 167, 128, 9, TextFormatting.DARK_GREEN),
    YELLOW(241, 177, 77, 255, 226, 170, 4, TextFormatting.YELLOW),
    RED(230, 27, 57, 253, 232, 239, 14, TextFormatting.RED),
    PURPLE(196, 148, 207, 246, 188, 96, 10, TextFormatting.DARK_PURPLE),
    LIGHT_GREEN(170, 208, 47, 230, 214, 104, 5, TextFormatting.GREEN),
    OLD_BLUE(173, 138, 128, 118, 151, 180, 2, TextFormatting.LIGHT_PURPLE),
    DARK_GREY(92, 92, 110, 198, 193, 165, 7, TextFormatting.DARK_GRAY),
    BROWN(200, 145, 112, 253, 228, 198, 12, TextFormatting.GOLD),
    DARK_BLUE(65, 84, 116, 104, 137, 146, 11, TextFormatting.DARK_BLUE),
    LIGHT_BLUE(100, 163, 206, 138, 235, 242, 3, TextFormatting.DARK_AQUA),
    SILVER(136, 136, 134, 255, 255, 255, 0, TextFormatting.GRAY);

    final private Vec3i mainColor;
    final private Vec3i secondaryColor;
    final private int woolMeta;
    final private TextFormatting textColor;

    EyeAndKoboldColor(int main_R, int main_G, int main_B, int sub_R, int sub_G, int sub_B, int woolData, TextFormatting textFormatting) {
        this.mainColor = new Vec3i(main_R, main_G, main_B);
        this.secondaryColor = new Vec3i(sub_R, sub_G, sub_B);
        this.woolMeta = woolData;
        this.textColor = textFormatting;
    }

    public static int indexOf(EyeAndKoboldColor col) {
        int n = 0;
        for (EyeAndKoboldColor colorID : EyeAndKoboldColor.values()) {
            if (col == colorID) {
                return n;
            }
            ++n;
        }
        return n;
    }

    public static EyeAndKoboldColor safeValueOf(String string) {
        try {
            return EyeAndKoboldColor.valueOf(string);
        } catch (IllegalArgumentException e) {
            return KoboldEntity.COLOR;
        }
    }

    public static EyeAndKoboldColor safeValueOf(Vec3i vec3i) {
        for (EyeAndKoboldColor color : EyeAndKoboldColor.values()) {
            if (vec3i.equals(color.getMainColor())) {
                return color;
            }
        }
        return KoboldEntity.COLOR;
    }

    public static EyeAndKoboldColor getColorByWoolId(int id) {
        for (EyeAndKoboldColor color : EyeAndKoboldColor.values()) {
            if (color.getWoolMeta() == id) {
                return color;
            }
        }
        return KoboldEntity.COLOR;
    }

    public Vec3i getMainColor() {
        return this.mainColor;
    }

    public Vec3i getSecondaryColor() {
        return this.secondaryColor;
    }

    public int getWoolMeta() {
        return this.woolMeta;
    }

    public TextFormatting getTextColor() {
        return this.textColor;
    }
}

