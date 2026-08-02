/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

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

public class SkinHelper {
    final static public int a = 3;

    @SideOnly(value=Side.CLIENT)
    public static BufferedImage GetPlayerSkin(UUID uUID) throws IOException {
        try {
            URL SessionURL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uUID.toString().replace("-", ""));
            BufferedReader reader = new BufferedReader(new InputStreamReader(SessionURL.openStream()));
            String profileText = reader.lines().collect(Collectors.joining());
            int charsUntilValue = profileText.indexOf("\"value\" : ");
            int charsUntilBase64 = charsUntilValue + 11;
            StringBuilder base64 = new StringBuilder();
            int i = 0;
            while (profileText.charAt(charsUntilBase64 + i) != '\"') {
                base64.append(profileText.charAt(charsUntilBase64 + i));
                ++i;
            }

            String skinText = new String(Base64.getDecoder().decode(base64.toString()));
            int charsUntilURL = skinText.indexOf("\"url\" : ");
            int charsUntilLink = charsUntilURL + 9;
            StringBuilder url = new StringBuilder();
            int j = 0;
            while (skinText.charAt(charsUntilLink + j) != '\"') {
                url.append(skinText.charAt(charsUntilLink + j));
                ++j;
            }

            URL skinURL = new URL(url.toString());
            return ImageIO.read(skinURL);
        } catch (Exception e) {
            return ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("sexmod", "textures/player/steve.png")).getInputStream());
        }
    }
}

