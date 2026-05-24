/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Vector2f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4d
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;

import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldStaffModel;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class fa_class300
extends GeoItemRenderer<DragonStaff> {
    final static private ResourceLocation c = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    final private e8_class231 q = new e8_class231();
    final static float p = 10.0f;
    final static float f = 1.5f;
    final static float m = 0.175f;
    final static float r = 0.1f;
    final static float g = 0.04f;
    final static float d = 8.0f;
    final static float i = 6.0f;
    final static float a = 1.3f;
    final static Vector2f[] l = new Vector2f[]{new Vector2f(1.0f, 0.0f), new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, 0.0f), new Vector2f(0.5f, 0.5f), new Vector2f(0.75f, 0.25f), new Vector2f(0.25f, 0.75f), new Vector2f(0.25f, 0.75f)};
    static boolean o = false;
    Minecraft e = Minecraft.getMinecraft();
    Vector2f j;
    double b = 0.0;
    EntityPlayer k;
    ItemStack h;
    static HashMap<ItemStack, Vector3f> n = new HashMap();

    public fa_class300() {
        super(new KoboldStaffModel());
    }

    public static boolean b() {
        return o;
    }

    public static void a() {
        o = !o;
    }

    // was:
    //this.a((hy_class407)item, itemStack);
    @Override
    public void render(DragonStaff hy_class4072, ItemStack itemStack) {
        EntityPlayer entityPlayer = null;
        for (EntityPlayer entityPlayer2 : this.e.world.playerEntities) {
            if (entityPlayer2.inventory.mainInventory.contains(itemStack)) {
                entityPlayer = entityPlayer2;
                break;
            }
            if (!entityPlayer2.inventory.offHandInventory.contains(itemStack)) continue;
            entityPlayer = entityPlayer2;
            break;
        }
        if (entityPlayer != null) {
            double d = entityPlayer.posX - entityPlayer.lastTickPosX;
            double d2 = entityPlayer.posZ - entityPlayer.lastTickPosZ;
            double d3 = Math.PI / 180 * (double)entityPlayer.rotationYaw;
            this.j = new Vector2f((float)(d * Math.cos(d3) + d2 * Math.sin(d3)), (float)(-d * Math.sin(d3) + d2 * Math.cos(d3)));
        } else {
            this.j = new Vector2f(0.0f, 0.0f);
        }
        if (!Minecraft.getMinecraft().isGamePaused()) {
            this.b = (float)Minecraft.getMinecraft().player.ticksExisted + this.e.getRenderPartialTicks();
        }
        this.h = itemStack;
        this.k = entityPlayer;
        super.render(hy_class4072, itemStack);
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        if ("staff".equals(geoBone.getName())) {
            GlStateManager.pushMatrix();
            Tessellator.getInstance().draw();
            p_class418.a(IGeoRenderer.MATRIX_STACK, geoBone);
            GlStateManager.translate(0.0, 1.5 + 0.001 * Math.sin(0.005 * this.b) + 0.001, 0.0);
            Vector3f vector3f = n.get(this.h);
            GlStateManager.scale(this.d(), this.d(), this.d());
            if (vector3f == null) {
                vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
            }
            vector3f.add((Tuple3f)new Vector3f(this.j.x, this.k == null ? 0.0f : (float)(this.k.posY - this.k.lastTickPosY), this.j.y));
            GlStateManager.rotate(vector3f.z * 10.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(vector3f.x * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-vector3f.y * 10.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate((float)(this.b * (double)0.1f), 1.0f, 1.0f, 1.0f);
            n.put(this.h, vector3f);
            this.e.getTextureManager().bindTexture(c);
            this.q.render(Minecraft.getMinecraft().player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
            GlStateManager.popMatrix();
            if (this.k != null) {
                this.c();
            }
            // was 'KoboldStaffModel().a'
            this.e.getTextureManager().bindTexture(new KoboldStaffModel().getTextureLocation(null));
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        }
        super.renderRecursively(bufferBuilder, geoBone, f, f2, f3, f4);
    }

    void c() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        ArrayList<Vec3d> arrayList2 = new ArrayList<Vec3d>();
        for (Vector4d vector4d : KoboldEntity.aY) {
            arrayList.add((int)vector4d.getW());
            arrayList2.add(new Vec3d(vector4d.getX(), vector4d.getY(), vector4d.getZ()));
        }
        if (arrayList.size() == 0) {
            return;
        }
        if (o) {
            this.a(arrayList, arrayList2);
        } else {
            this.a(arrayList);
        }
    }

    void a(List<Integer> list, List<Vec3d> list2) {
        for (int i = 0; i < list.size(); ++i) {
            float f = Reference.Lerp(this.k.prevRotationYawHead, this.k.rotationYawHead, this.e.getRenderPartialTicks());
            float f2 = Reference.Lerp(this.k.prevRotationPitch, this.k.rotationPitch, this.e.getRenderPartialTicks());
            Vec3d vec3d = Reference.a(new Vec3d(this.k.prevPosX, this.k.prevPosY + (double)this.k.getEyeHeight(), this.k.prevPosZ), this.k.getPositionVector().add(0.0, this.k.getEyeHeight(), 0.0), (double)this.e.getRenderPartialTicks());
            Vec3d vec3d2 = vec3d.subtract(list2.get(i));
            vec3d2 = ck_class135.a(vec3d2, -f2, f);
            double d = Math.abs(vec3d2.x) + Math.abs(vec3d2.z) + Math.abs(vec3d2.y);
            double d2 = -vec3d2.x / d;
            double d3 = -vec3d2.y / d;
            double d4 = vec3d2.z / d;
            d2 = this.a(d2);
            d3 = this.a(d3);
            d4 = this.a(d4);
            this.b(list.get(i), (float)(d2 *= (double)1.3f), (float)(d3 *= (double)1.3f), (float)(d4 *= (double)1.3f));
        }
    }

    void a(List<Integer> list) {
        float f = 1.0f / (float)list.size();
        float f2 = 0.0f;
        for (int i = 0; i < list.size(); ++i) {
            this.a(list.get(i), 1.0f - (f2 += f), 0.0f + f2, (float) Reference.Lerp((double)0.8f, (double)1.2f, (double)i / (double)list.size()));
        }
    }

    double a(double d) {
        return d * Math.sqrt(1.0 - d * d / 2.0);
    }

    double d() {
        return (double)0.175f + 0.025 * Math.sin(0.005 * this.b) + 0.025;
    }

    void a(int n, float f, float f2, float f3) {
        this.a(new ItemStack(Blocks.WOOL, 1, n), f, f2, f3);
    }

    void b(int n, float f, float f2, float f3) {
        this.b(new ItemStack(Blocks.WOOL, 1, n), f, f2, f3);
    }

    void b(ItemStack itemStack, float f, float f2, float f3) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 1.5 + 0.001 * Math.sin(0.005 * this.b) + 0.001, 0.0);
        GlStateManager.scale(0.04f, 0.04f, 0.04f);
        GlStateManager.translate(f * 6.0f, f2 * 6.0f, f3 * 6.0f);
        this.e.getItemRenderer().renderItem(Minecraft.getMinecraft().player, itemStack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    void a(ItemStack itemStack, float f, float f2, float f3) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 1.5 + 0.001 * Math.sin(0.005 * this.b) + 0.001, 0.0);
        GlStateManager.scale(0.04f, 0.04f, 0.04f);
        GlStateManager.rotate((float)(this.b * 8.0 * (double)f3), 0.0f, f, f2);
        GlStateManager.translate(6.0f, 0.0f, 0.0f);
        this.e.getItemRenderer().renderItem(Minecraft.getMinecraft().player, itemStack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    //@Override
    //public void render(Item item, ItemStack itemStack) {
    //    //this.a((hy_class407)item, itemStack);
    //
    //}
}

