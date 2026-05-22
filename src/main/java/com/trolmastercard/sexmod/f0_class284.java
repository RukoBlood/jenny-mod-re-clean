/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class f0_class284
extends GeoItemRenderer<LampItem> {
    Minecraft a = Minecraft.getMinecraft();
    static ResourceLocation b = null;

    public f0_class284() {
        super(new LampModel());
    }

    ResourceLocation a() {
        if (b == null) {
            try {
                URL uRL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + Minecraft.getMinecraft().player.getPersistentID().toString().replace("-", ""));
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(uRL.openStream()));
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
                BufferedImage bufferedImage = ImageIO.read(uRL2);
                BufferedImage bufferedImage2 = ImageIO.read(this.a.getResourceManager().getResource(new LampModel().getTextureLocation(new LampItem())).getInputStream());
                for (int i = 0; i < bufferedImage2.getWidth(); ++i) {
                    for (int j = 0; j < bufferedImage2.getHeight(); ++j) {
                        int n7 = bufferedImage.getRGB(i, j);
                        if (n7 == 0) continue;
                        bufferedImage2.setRGB(i, j, n7);
                    }
                }
                b = Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation("lamptex", new DynamicTexture(bufferedImage2));
            } catch (Exception exception) {
                b = new LampModel().getTextureLocation(new LampItem());
            }
        }
        return b;
    }

    @Override
    public void render(GeoModel geoModel, LampItem ap_class372, float f, float f2, float f3, float f4, float f5) {
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        this.renderEarly(ap_class372, f, f2, f3, f4, f5);
        this.renderLate(ap_class372, f, f2, f3, f4, f5);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        for (GeoBone geoBone : geoModel.topLevelBones) {
            this.a(bufferBuilder, ap_class372, geoBone, f2, f3, f4, f5);
        }
        Tessellator.getInstance().draw();
        this.renderAfter(ap_class372, f, f2, f3, f4, f5);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
    }

    public void a(BufferBuilder bufferBuilder, LampItem ap_class372, GeoBone geoBone, float f, float f2, float f3, float f4) {
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.a.renderEngine.bindTexture(this.a());
        if (this.a(geoBone.getName())) {
            this.b(bufferBuilder, ap_class372, geoBone, f, f2, f3, f4);
        }
        MATRIX_STACK.pop();
    }

    boolean a(String string) {
        if (!string.equals("leftArm") && !string.equals("rightArm")) {
            return true;
        }
        return this.a.player.getEntityData().getBoolean("sexmodAllieInUse") && this.a.gameSettings.thirdPersonView == 0;
    }

    void b(BufferBuilder bufferBuilder, LampItem ap_class372, GeoBone geoBone, float f, float f2, float f3, float f4) {
        if (!geoBone.isHidden) {
            for (GeoCube object : geoBone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.renderCube(bufferBuilder, object, f, f2, f3, f4);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
            for (GeoBone geoBone2 : geoBone.childBones) {
                this.a(bufferBuilder, ap_class372, geoBone2, f, f2, f3, f4);
            }
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

