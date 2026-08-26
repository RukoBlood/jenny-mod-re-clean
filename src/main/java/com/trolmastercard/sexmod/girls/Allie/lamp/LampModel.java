/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Allie.lamp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.trolmastercard.sexmod.util.SkinHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LampModel extends AnimatedGeoModel<LampItem> {
    ResourceLocation playerSkin = null;

    @Override
    public ResourceLocation getModelLocation(LampItem item) {
        return new ResourceLocation("sexmod", "geo/allie/lamp.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(LampItem item) {
        if (this.playerSkin != null) {
            return this.playerSkin;
        }
        try {
            Minecraft minecraft = Minecraft.getMinecraft();
            BufferedImage skin = SkinHelper.GetPlayerSkin(minecraft.player.getPersistentID());
            Graphics graphics = skin.getGraphics();
            graphics.setColor(new Color(185, 254, 255));
            graphics.fillRect(0, 0, 2, 2);
            graphics.setColor(new Color(255, 255, 255));
            graphics.fillRect(2, 0, 1, 2);
            graphics.setColor(new Color(0, 0, 0));
            graphics.fillRect(3, 0, 1, 2);
            this.playerSkin = minecraft.renderEngine.getDynamicTextureLocation("alliesLamp", new DynamicTexture(skin));
        } catch (IOException iOException) {
            iOException.printStackTrace();
            this.playerSkin = new ResourceLocation("sexmod", "textures/entity/allie/lamp.png");
        }
        return this.playerSkin;
    }

    public ResourceLocation getAnimationFileLocation(LampItem ap_class372) {
        return new ResourceLocation("sexmod", "animations/allie/lamp.animation.json");
    }

    //@Override
    //public ResourceLocation getAnimationFileLocation(Object object) {
    //    return this.a((LampItem)object);
    //}

    //@Override
    //public ResourceLocation getTextureLocation(Object object) {
    //    return this.c((LampItem)object);
    //}

    //@Override
    //public ResourceLocation getModelLocation(Object object) {
    //    return this.b((LampItem)object);
    //}
}

