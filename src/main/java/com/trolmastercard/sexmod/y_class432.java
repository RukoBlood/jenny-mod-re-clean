/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class y_class432 {
    final static public int a = 3;

    @SideOnly(value=Side.CLIENT)
    public static BufferedImage GetPlayerSkin(UUID uUID) throws IOException {
        try {
            URL uRL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uUID.toString().replace("-", ""));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
            String string = bufferedReader.lines().collect(Collectors.joining());
            int n = string.indexOf("\"value\" : ");
            int n2 = n + 11;
            StringBuilder stringBuilder = new StringBuilder();
            int n3 = 0;
            while (string.charAt(n2 + n3) != '\"') {
                stringBuilder.append(string.charAt(n2 + n3));
                ++n3;
            }
            String string2 = new String(Base64.getDecoder().decode(stringBuilder.toString()));
            int n4 = string2.indexOf("\"url\" : ");
            int n5 = n4 + 9;
            StringBuilder stringBuilder2 = new StringBuilder();
            int n6 = 0;
            while (string2.charAt(n5 + n6) != '\"') {
                stringBuilder2.append(string2.charAt(n5 + n6));
                ++n6;
            }
            URL uRL2 = new URL(stringBuilder2.toString());
            return ImageIO.read(uRL2);
        } catch (Exception exception) {
            return ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("sexmod", "textures/player/steve.png")).getInputStream());
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

