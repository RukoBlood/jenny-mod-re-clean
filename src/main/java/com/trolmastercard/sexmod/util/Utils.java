/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Random;
import java.util.UUID;

import net.minecraft.util.math.Vec3d;

public class Utils {
    public static float getAngleDifferences(double src, double target) {
        double diff;
        src = (src + Math.PI * 2) % (Math.PI * 2);
        target = (target + Math.PI * 2) % (Math.PI * 2);

        for (diff = target - src; diff < -Math.PI; diff += Math.PI * 2) {
            //empty
        }

        while (diff >= Math.PI) {
            diff -= Math.PI * 2;
        }
        return (float)diff;
    }

    public static Rotation2f CalculateLookAngles(Vec3d startPos, Vec3d endPos) {
        Vec3d vec3d3 = endPos.subtract(startPos).normalize();
        return new Rotation2f((float)Math.atan2(vec3d3.x, vec3d3.z), (float)Math.atan2(vec3d3.y, Math.sqrt(vec3d3.x * vec3d3.x + vec3d3.z * vec3d3.z)));
    }

    public static void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, null);
    }

    public static String CapitalizeString(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
    }

    public static boolean isValueInBounds(double v, double min, double max) {
        if (v < min) {
            return false;
        }
        return !(v >= max);
    }

    public static int getWeightedRandomInt(int max) {
        int iter;
        if (max <= 0) {
            return max;
        }
        Random random = new Random();
        int sumOfWeights = 0;
        for (iter = 0; iter <= max; ++iter) {
            sumOfWeights += iter;
        }
        iter = random.nextInt(sumOfWeights) + 1;
        int curWeightSum = 0;
        for (int i = 0; i <= max; ++i) {
            if ((curWeightSum += i) < iter) continue;
            return i;
        }
        return max;
    }

    public static int getRandomSign() {
        return Reference.RANDOM.nextBoolean() ? 1 : -1;
    }

    public static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    public static double clamp(double d, double d2, double d3) {
        return Math.max(d2, Math.min(d3, d));
    }

    public static float getRandomFloat(float maxMult, boolean allowNegative) {
        Random random = new Random();
        return random.nextFloat() * maxMult * (float)(allowNegative && random.nextBoolean() ? -1 : 1);
    }

    public static float approachValue(float val, float target, float step) {
        if (Math.abs(val - target) <= step) {
            return val;
        }
        if (Math.abs(val) < Math.abs(target)) {
            if (target > 0.0f) {
                return target - step;
            }
            return target + step;
        }
        if (val > 0.0f) {
            return val - step;
        }
        return val + step;
    }

    public static int Round(double d) {
        return Math.round((float)d);
    }

    public static void runDelayedTask(int ms, Runnable task) {
        String randomUUID = UUID.randomUUID().toString();
        new Thread(() -> {
            try {
                Thread.sleep(ms);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            task.run();
        }, (ClientServerCheck.getInstance() ? "server sexmod thread " : "client sexmod thread ") + randomUUID).start();
    }
}

