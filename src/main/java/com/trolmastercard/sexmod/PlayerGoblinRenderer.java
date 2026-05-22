/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod;

import java.util.HashSet;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.vecmath.Vector4f;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerGoblinRenderer
extends d9_class168 {
    PlayerGoblin B = null;
    boolean C = false;
    boolean E = false;
    boolean D = false;

    public PlayerGoblinRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected Vec3i net_minecraft_util_math_Vec3i_a(String string) {
        String[] stringArray = e4_class223.java_lang_String_arr_a(this.j);
        if (stringArray.length < 8) {
            return z;
        }
        if (string.contains("band")) {
            return GoblinRenderer.w;
        }
        if (string.contains("eyeColor") || string.contains("eyeColor2")) {
            return GoblinRenderer.unknownCalcVec(stringArray[8]);
        }
        if (string.contains("variant") || string.contains("boob")) {
            return GoblinRenderer.c(stringArray[7]);
        }
        if (string.contains("hair")) {
            return GoblinRenderer.d(stringArray[6]);
        }
        if (GoblinRenderer.D.contains(string)) {
            return GoblinRenderer.c(stringArray[7]);
        }
        if (GoblinRenderer.M.contains(string)) {
            return GoblinRenderer.d(stringArray[6]);
        }
        return z;
    }

    @Override
    protected Vector4f a(String string, float f, float f2, float f3) {
        if (string.startsWith("crown")) {
            ItemStack itemStack = this.j.getDataManager().get(Fighter.ITEM_SLOT_3);
            if (itemStack.isEmpty()) {
                return super.a(string, f, f2, f3);
            }
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            ItemArmor.ArmorMaterial armorMaterial = itemArmor.getArmorMaterial();
            float f4 = 0.0f;
            switch (armorMaterial) {
                case GOLD: {
                    f4 = 1.0f;
                    break;
                }
                case CHAIN: 
                case IRON: {
                    f4 = 2.0f;
                    break;
                }
                case LEATHER: {
                    f4 = 4.0f;
                    int n = itemArmor.getColor(itemStack);
                    float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
                    float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
                    float f7 = (float)(n & 0xFF) / 255.0f;
                    f = f5;
                    f2 = f6;
                    f3 = f7;
                }
            }
            return new Vector4f(f, f2, f3, 72.0f * f4 / 4096.0f);
        }
        return super.a(string, f, f2, f3);
    }

    @Override
    protected boolean c(String string) {
        if (string.startsWith("crown")) {
            return true;
        }
        return super.c(string);
    }

    @Override
    public HashSet<String> a() {
        return new HashSet<String>(){
            {
                this.add("boobs");
                this.add("booty");
                this.add("vagina");
                this.add("fuckhole");
                this.add("preggy");
                this.add("LegL");
                this.add("LegR");
                this.add("cheekR");
                this.add("cheekL");
            }
        };
    }

    @Override
    protected void a(String string, GeoBone geoBone) {
        String[] stringArray = e4_class223.java_lang_String_arr_a(this.j);
        if (stringArray.length < 8) {
            return;
        }
        switch (string) {
            case "earL": {
                GoblinRenderer.a(geoBone, stringArray[0], stringArray[1], stringArray[3]);
                break;
            }
            case "earR": {
                GoblinRenderer.a(geoBone, stringArray[0], stringArray[2], stringArray[4]);
                break;
            }
            case "hair": {
                GoblinRenderer.a(geoBone, stringArray[5]);
                break;
            }
            case "body": {
                geoBone.setPivotY(-0.15f);
                GoblinRenderer.a(this.j, geoBone);
                break;
            }
            case "LegR": {
                GoblinRenderer.a(this.C, geoBone, 25.0f, 25.0f);
                break;
            }
            case "boobR": {
                GoblinRenderer.a(this.C, geoBone, 30.0f, 30.0f);
                break;
            }
            case "boobR1": {
                GoblinRenderer.a(this.C, geoBone, 10.0f, 15.0f);
                break;
            }
            case "boobR2": {
                GoblinRenderer.a(this.C, geoBone, 5.0f, 3.0f);
            }
        }
        if (string.contains("crown")) {
            GoblinRenderer.a(this.j, geoBone, stringArray[9]);
        }
    }

    @Override
    public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
        Object object;
        this.D = v;
        this.B = (PlayerGoblin)em_class2582;
        this.C = -420.69f == f && em_class2582.currentAction() == Action.SHOULDER_IDLE;
        this.E = -420.69f == f && em_class2582.currentAction() == Action.PICK_UP;
        this.y = f2;
        GoblinRenderer.B = f;
        Action fp_class3242 = em_class2582.currentAction();
        UUID uUID = this.B.java_util_UUID_e();
        if (em_class2582.boolean_h()) {
            object = GoblinRenderer.a(em_class2582.world, em_class2582, uUID, d, d2, d3);
            d = ((Vec3d)object).x;
            d2 = ((Vec3d)object).y;
            d3 = ((Vec3d)object).z;
        }
        if (fp_class3242 == Action.THROWN || fp_class3242 == Action.START_THROWING) {
            if (PlayerGoblinRenderer.i.gameSettings.thirdPersonView == 0 && f == -420.69f && !em_class2582.boolean_h()) {
                return;
            }
            if (!em_class2582.boolean_h()) {
                float f3;
                em_class2582.prevRenderYawOffset = f3 = em_class2582.java_lang_Float_I().floatValue();
                em_class2582.renderYawOffset = f3;
            }
        }
        if (GoblinRenderer.a(em_class2582, fp_class3242)) {
            if (PlayerGoblinRenderer.i.player.getPersistentID().equals(uUID)) {
                if (-420.69f != f) {
                    return;
                }
                em_class2582.renderYawOffset = PlayerGoblinRenderer.i.player.rotationYaw + 180.0f;
                em_class2582.prevRenderYawOffset = PlayerGoblinRenderer.i.player.rotationYaw + 180.0f;
                object = PlayerGoblinRenderer.i.player.getLookVec();
                GlStateManager.pushMatrix();
                GlStateManager.translate(((Vec3d)object).x, ((Vec3d)object).y + (double) PlayerGoblinRenderer.i.player.getEyeHeight(), ((Vec3d)object).z);
                Vec3d vec3d = GoblinEntity.b(new Vec3d(-Math.abs(PlayerGoblinRenderer.i.player.rotationPitch), 0.0, 0.0), PlayerGoblinRenderer.i.player.rotationYaw);
                GlStateManager.rotate(PlayerGoblinRenderer.i.player.rotationPitch, (float)vec3d.x, 0.0f, (float)vec3d.z);
                d = 0.0;
                d2 = 0.0;
                d3 = 0.0;
            } else if (!this.B.java_util_UUID_m().equals(PlayerGoblinRenderer.i.player.getPersistentID())) {
                if (!em_class2582.boolean_h() || uUID == null || PlayerGoblinRenderer.i.player.getPersistentID().equals(uUID)) {
                    if (uUID != null && !PlayerGoblinRenderer.i.player.getPersistentID().equals(uUID)) {
                        object = em_class2582.world.getPlayerEntityByUUID(uUID);
                        if (object != null) {
                            em_class2582.renderYawOffset = ((EntityPlayer)object).rotationYaw;
                            em_class2582.prevRenderYawOffset = ((EntityPlayer)object).rotationYaw;
                        }
                    } else {
                        em_class2582.renderYawOffset = PlayerGoblinRenderer.i.player.rotationYaw;
                        em_class2582.prevRenderYawOffset = PlayerGoblinRenderer.i.player.rotationYaw;
                    }
                }
                object = GoblinRenderer.a(em_class2582, this.B.java_util_UUID_e(), f2);
                d = ((Vec3d)object).x;
                d2 = ((Vec3d)object).y;
                d3 = ((Vec3d)object).z;
            }
        } else if (this.C) {
            GoblinRenderer.a(f2);
            object = new Vec3d(b6_class67.a(-0.1f, 0.2f, PlayerGoblinRenderer.i.gameSettings.fovSetting / 110.0f), 0.0, 0.0);
            object = GoblinEntity.b((Vec3d)object, PlayerGoblinRenderer.i.player.rotationYaw);
            d = ((Vec3d)object).x;
            d2 = ((Vec3d)object).y;
            d3 = ((Vec3d)object).z;
            em_class2582.renderYawOffset = PlayerGoblinRenderer.i.player.rotationYaw;
            em_class2582.prevRenderYawOffset = PlayerGoblinRenderer.i.player.prevRotationYaw;
            if (PlayerGoblinRenderer.i.player.isSneaking()) {
                d2 -= 0.075;
            }
        } else if (fp_class3242 == Action.SHOULDER_IDLE) {
            if (uUID == null) {
                return;
            }
            if (PlayerGoblinRenderer.i.player.getPersistentID().equals(uUID) && PlayerGoblinRenderer.i.gameSettings.thirdPersonView == 0) {
                return;
            }
            object = em_class2582.world.getPlayerEntityByUUID(uUID);
            if (object == null) {
                return;
            }
            Vector4f vector4f = GoblinRenderer.a_0((EntityPlayer)object, f2);
            d = vector4f.x;
            d2 = vector4f.y;
            d3 = vector4f.z;
            em_class2582.renderYawOffset = vector4f.w;
            if (((Entity)object).isSneaking()) {
                d2 -= 0.32;
            }
        } else if (fp_class3242 == Action.PICK_UP && uUID != null && (object = em_class2582.world.getPlayerEntityByUUID(uUID)) != null) {
            em_class2582.prevRenderYawOffset = ((EntityPlayer)object).prevRotationYawHead;
            em_class2582.renderYawOffset = ((EntityPlayer)object).rotationYawHead;
        }
        super.doRender(em_class2582, d, d2, d3, f, f2);
        if (GoblinRenderer.a(em_class2582, fp_class3242) && PlayerGoblinRenderer.i.gameSettings.thirdPersonView == 0 && PlayerGoblinRenderer.i.player.getPersistentID().equals(uUID)) {
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void b(Tessellator tessellator, BufferBuilder bufferBuilder, GirlEntity em_class2582, f7_class292 f7_class2922, float f) {
        PlayerGoblinRenderer.a(tessellator, bufferBuilder, em_class2582, f7_class2922, f);
    }

    @Nullable
    protected f7_class292 e(GirlEntity em_class2582) {
        if (!this.D) {
            return null;
        }
        if (!(em_class2582 instanceof PlayerGoblin)) {
            return null;
        }
        PlayerGoblin eq_class2642 = (PlayerGoblin)em_class2582;
        UUID uUID = eq_class2642.java_util_UUID_m();
        EntityPlayerSP entityPlayerSP = PlayerGoblinRenderer.i.player;
        if (uUID == null || PlayerGoblinRenderer.i.gameSettings.thirdPersonView == 0 && entityPlayerSP.getPersistentID().equals(uUID)) {
            return null;
        }
        EntityPlayer entityPlayer = eq_class2642.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return null;
        }
        ItemStack itemStack = eq_class2642.getDataManager().get(Fighter.ITEM_SLOT_4);
        if (itemStack.isEmpty()) {
            return null;
        }
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return null;
        }
        ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        switch (itemArmor.getArmorMaterial()) {
            default: {
                return new f7_class292(23.0f, 100.0f, 93.0f);
            }
            case GOLD: {
                return new f7_class292(99.0f, 98.0f, 14.0f);
            }
            case CHAIN: 
            case IRON: {
                return new f7_class292(85.0f, 85.0f, 85.0f);
            }
            case LEATHER: 
        }
        int n = itemArmor.getColor(itemStack);
        float f = n >> 16 & 0xFF;
        float f2 = n >> 8 & 0xFF;
        float f3 = n & 0xFF;
        return new f7_class292(f, f2, f3);
    }

    @Override
    protected void void_c() {
        GlStateManager.translate(0.0, -0.77, -0.05);
        GlStateManager.scale(0.5, 0.5, 0.5);
    }

    @Override
    protected void a(boolean bl, ItemStack itemStack) {
        super.a(bl, itemStack);
        if (itemStack.getItem().getItemUseAction(itemStack) == EnumAction.BOW) {
            if (bl) {
                GlStateManager.translate(0.1f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            } else {
                GlStateManager.rotate(170.0f, 1.0f, 0.0f, 0.0f);
            }
            return;
        }
        GlStateManager.rotate(bl ? 70.0f : 180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0, 0.05, -0.03);
    }

    @Override
    protected void a(boolean bl) {
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        super.a(bl, bl2);
        if (bl) {
            if (bl2) {
                GlStateManager.translate(0.0, 0.2, -0.25);
                GlStateManager.rotate(85.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(38.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            } else {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, -0.265, -0.04);
            }
        } else if (bl2) {
            GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(150.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0, -0.33, -0.1);
        } else {
            GlStateManager.translate(-0.02, -0.05, -0.05);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

