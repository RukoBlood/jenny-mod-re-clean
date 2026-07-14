/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Random;
import java.util.UUID;

import com.trolmastercard.sexmod.util.ClientServerCheck;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.util.math.Vec3d;

public class be_class78 {
    public static float a(double d, double d2) {
        double d3;
        d = (d + Math.PI * 2) % (Math.PI * 2);
        d2 = (d2 + Math.PI * 2) % (Math.PI * 2);
        for (d3 = d2 - d; d3 < -Math.PI; d3 += Math.PI * 2) {
        }
        while (d3 >= Math.PI) {
            d3 -= Math.PI * 2;
        }
        return (float)d3;
    }

    public static bm_class88 a(Vec3d vec3d, Vec3d vec3d2) {
        Vec3d vec3d3 = vec3d2.subtract(vec3d).normalize();
        return new bm_class88((float)Math.atan2(vec3d3.x, vec3d3.z), (float)Math.atan2(vec3d3.y, Math.sqrt(vec3d3.x * vec3d3.x + vec3d3.z * vec3d3.z)));
    }

    public static void a(String string) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, null);
    }

    public static String b(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
    }

    public static boolean a(double d, double d2, double d3) {
        if (d < d2) {
            return false;
        }
        return !(d >= d3);
    }

    public static int a(int n) {
        int n2;
        if (n <= 0) {
            return n;
        }
        Random random = new Random();
        int n3 = 0;
        for (n2 = 0; n2 <= n; ++n2) {
            n3 += n2;
        }
        n2 = random.nextInt(n3) + 1;
        int n4 = 0;
        for (int i = 0; i <= n; ++i) {
            if ((n4 += i) < n2) continue;
            return i;
        }
        return n;
    }

    public static int a() {
        return Reference.RANDOM.nextBoolean() ? 1 : -1;
    }

    public static float b(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    public static double b(double d, double d2, double d3) {
        return Math.max(d2, Math.min(d3, d));
    }

    public static float a(float f, boolean bl) {
        Random random = new Random();
        return random.nextFloat() * f * (float)(bl && random.nextBoolean() ? -1 : 1);
    }

    public static float a(float f, float f2, float f3) {
        if (Math.abs(f - f2) <= f3) {
            return f;
        }
        if (Math.abs(f) < Math.abs(f2)) {
            if (f2 > 0.0f) {
                return f2 - f3;
            }
            return f2 + f3;
        }
        if (f > 0.0f) {
            return f - f3;
        }
        return f + f3;
    }

    public static int a(double d) {
        return Math.round((float)d);
    }

    public static void a(int n, Runnable runnable) {
        String randomUUID = UUID.randomUUID().toString();
        new Thread(() -> {
            try {
                Thread.sleep(n);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            runnable.run();
        }, (ClientServerCheck.getInstance() ? "server sexmod thread " : "client sexmod thread ") + randomUUID).start();
    }
}

