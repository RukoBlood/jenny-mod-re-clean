/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Allie.lamp;

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

public class LampRenderer extends GeoItemRenderer<LampItem> {
    Minecraft mc = Minecraft.getMinecraft();
    static ResourceLocation skin = null;

    public LampRenderer() {
        super(new LampModel());
    }

    ResourceLocation getSkin() {
        if (skin == null) {
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
                BufferedImage bufferedImage2 = ImageIO.read(this.mc.getResourceManager().getResource(new LampModel().getTextureLocation(new LampItem())).getInputStream());
                for (int i = 0; i < bufferedImage2.getWidth(); ++i) {
                    for (int j = 0; j < bufferedImage2.getHeight(); ++j) {
                        int n7 = bufferedImage.getRGB(i, j);
                        if (n7 == 0) continue;
                        bufferedImage2.setRGB(i, j, n7);
                    }
                }
                skin = Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation("lamptex", new DynamicTexture(bufferedImage2));
            } catch (Exception exception) {
                skin = new LampModel().getTextureLocation(new LampItem());
            }
        }
        return skin;
    }

    @Override
    public void render(GeoModel model, LampItem animatable, float partialTicks, float red, float green, float blue, float alpha) {
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        this.renderEarly(animatable, partialTicks, red, green, blue, alpha);
        this.renderLate(animatable, partialTicks, red, green, blue, alpha);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        for (GeoBone geoBone : model.topLevelBones) {
            this.renderLampBone(bufferBuilder, animatable, geoBone, red, green, blue, alpha);
        }
        Tessellator.getInstance().draw();
        this.renderAfter(animatable, partialTicks, red, green, blue, alpha);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
    }

    public void renderLampBone(BufferBuilder bufferBuilder, LampItem ap_class372, GeoBone geoBone, float f, float f2, float f3, float f4) {
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.mc.renderEngine.bindTexture(this.getSkin());
        if (this.isNotArmBone(geoBone.getName())) {
            this.renderLampEffect(bufferBuilder, ap_class372, geoBone, f, f2, f3, f4);
        }
        MATRIX_STACK.pop();
    }

    boolean isNotArmBone(String string) {
        return !string.equals("leftArm") && !string.equals("rightArm") || this.mc.player.getEntityData().getBoolean("sexmodAllieInUse") && this.mc.gameSettings.thirdPersonView == 0;
    }

    void renderLampEffect(BufferBuilder buffer, LampItem item, GeoBone bone, float r, float g, float b, float a) {
        if (!bone.isHidden) {
            for (GeoCube object : bone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.renderCube(buffer, object, r, g, b, a);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
            for (GeoBone geoBone2 : bone.childBones) {
                this.renderLampBone(buffer, item, geoBone2, r, g, b, a);
            }
        }
    }
}

