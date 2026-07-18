/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.Nullable;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.PlayerGirlRenderer;
import com.trolmastercard.sexmod.util.Vector3f;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerGalathRenderer
extends PlayerGirlRenderer {
    final static HashSet<String> z = new HashSet<String>(Arrays.asList("kneeL", "kneeR", "shinL", "shinR", "armorHelmet", "sockL", "sockR", "braBoobL", "braBoobR", "armorNippleR", "armorNippleL", "slip", "turnable", "static"));

    public PlayerGalathRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Nullable
    protected Vector3f e(GirlEntity em_class2582) {
        if (em_class2582.world instanceof FakeWorld) {
            return null;
        }
        if (((b7_class68)((Object)em_class2582)).boolean_c()) {
            return null;
        }
        return GalathRenderer.y;
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        HashSet<String> hashSet = GalathRenderer.E;
        GalathRenderer.E.addAll(gx_class390.a);
        return GalathRenderer.E;
    }

    @Override
    protected void b(Tessellator tessellator, BufferBuilder bufferBuilder, GirlEntity em_class2582, Vector3f f7_class2922, float f) {
        PlayerGalathRenderer.a(tessellator, bufferBuilder, em_class2582, f7_class2922, f);
    }

    @Override
    public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
        super.doRender(em_class2582, d, d2, d3, f, f2);
        if (PlayerGalathRenderer.i.gameSettings.thirdPersonView == 0 && PlayerGalathRenderer.i.player.getPersistentID().equals(((PlayerGirl)em_class2582).java_util_UUID_m()) && !em_class2582.boolean_Q()) {
            return;
        }
        GalathRenderer.a_27(em_class2582, f2);
    }

    @Override
    protected void a(boolean bl) {
        super.a(bl);
        if (bl) {
            GlStateManager.translate(0.15, 0.0, 0.0);
        }
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        super.a(bl, bl2);
        if (bl) {
            GlStateManager.translate(0.0, -0.05, -0.05);
            GlStateManager.rotate(15.0f, 1.0f, 0.0f, 0.0f);
            if (bl2) {
                GlStateManager.translate(0.3, 0.2, 0.0);
                GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(15.0f, 0.0f, 0.0f, 1.0f);
            }
        } else {
            GlStateManager.translate(0.0, 0.0, 0.1);
            GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
            if (bl2) {
                GlStateManager.rotate(-29.0f, 1.0f, 0.0f, 0.0f);
            }
        }
    }

    @Override
    protected Vector4f a(String string, float f, float f2, float f3) {
        if (!z.contains(string)) {
            return this.a(f, f2, f3);
        }
        if ("armorHelmet".equals(string)) {
            return super.a(string, f, f2, f3);
        }
        ItemStack itemStack = ItemStack.EMPTY;
        switch (string) {
            case "braBoobL": 
            case "braBoobR": 
            case "armorNippleR": 
            case "armorNippleL": {
                itemStack = this.j.getDataManager().get(Fighter.ITEM_SLOT_4);
                break;
            }
            case "turnable": 
            case "static": 
            case "slip": {
                itemStack = this.j.getDataManager().get(Fighter.ITEM_SLOT_5);
                break;
            }
            case "shinL": 
            case "shinR": 
            case "sockL": 
            case "sockR": 
            case "kneeL": 
            case "kneeR": {
                itemStack = this.j.getDataManager().get(Fighter.ITEM_SLOT_6);
            }
        }
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return this.a(f, f2, f3);
        }
        Object object = (ItemArmor)itemStack.getItem();
        switch (((ItemArmor)object).getArmorMaterial()) {
            default: {
                return new Vector4f(f, f2, f3, -0.1875f);
            }
            case GOLD: {
                return new Vector4f(f, f2, f3, -0.15625f);
            }
            case IRON: 
            case CHAIN: {
                return new Vector4f(f, f2, f3, -0.125f);
            }
            case LEATHER: 
        }
        int n = ((ItemArmor)object).getColor(itemStack);
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        return new Vector4f(f *= f4, f2 *= f5, f3 *= f6, -0.09375f);
    }

    protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
        GeoBone geoBone = geoModel.topLevelBones.get(0);
        GeoBone geoBone2 = null;
        GeoBone geoBone3 = null;
        for (GeoBone geoBone4 : geoBone.childBones) {
            switch (geoBone4.getName()) {
                case "steve": {
                    geoBone3 = geoBone4;
                    break;
                }
                case "body": {
                    geoBone2 = geoBone4;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
        Tessellator.getInstance().draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.d(this.j));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.renderRecursively(bufferBuilder, geoBone3, f, f2, f3, this.j.float_v());
        Tessellator.getInstance().draw();
        MATRIX_STACK.pop();
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

