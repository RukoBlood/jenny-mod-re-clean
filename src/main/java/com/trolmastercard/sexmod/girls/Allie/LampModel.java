/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Allie;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.trolmastercard.sexmod.y_class432;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LampModel
extends AnimatedGeoModel<LampItem> {
    ResourceLocation a = null;

    @Override
    public ResourceLocation getModelLocation(LampItem ap_class372) {
        return new ResourceLocation("sexmod", "geo/allie/lamp.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(LampItem ap_class372) {
        if (this.a != null) {
            return this.a;
        }
        try {
            Minecraft minecraft = Minecraft.getMinecraft();
            BufferedImage bufferedImage = y_class432.GetPlayerSkin(minecraft.player.getPersistentID());
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setColor(new Color(185, 254, 255));
            graphics.fillRect(0, 0, 2, 2);
            graphics.setColor(new Color(255, 255, 255));
            graphics.fillRect(2, 0, 1, 2);
            graphics.setColor(new Color(0, 0, 0));
            graphics.fillRect(3, 0, 1, 2);
            this.a = minecraft.renderEngine.getDynamicTextureLocation("alliesLamp", new DynamicTexture(bufferedImage));
        } catch (IOException iOException) {
            iOException.printStackTrace();
            this.a = new ResourceLocation("sexmod", "textures/entity/allie/lamp.png");
        }
        return this.a;
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

