/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.Goblin;

import java.util.HashSet;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.AbstractGoblinKoboldEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.AbstractPlayerKoblinGoboldRenderer;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.Vector3f;
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

public class PlayerGoblinRenderer extends AbstractPlayerKoblinGoboldRenderer {
    PlayerGoblin B = null;
    boolean C = false;
    boolean E = false;
    boolean D = false;

    public PlayerGoblinRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected Vec3i resolveBoneColor(String name) {
        String[] stringArray = AbstractGoblinKoboldEntity.SplitDnaIntoGenes(this.renderEntity);
        if (stringArray.length < 8) {
            return DEFAULT_COLOR;
        }
        if (name.contains("band")) {
            return GoblinRenderer.w;
        }
        if (name.contains("eyeColor") || name.contains("eyeColor2")) {
            return GoblinRenderer.unknownCalcVec(stringArray[8]);
        }
        if (name.contains("variant") || name.contains("boob")) {
            return GoblinRenderer.c(stringArray[7]);
        }
        if (name.contains("hair")) {
            return GoblinRenderer.d(stringArray[6]);
        }
        if (GoblinRenderer.D.contains(name)) {
            return GoblinRenderer.c(stringArray[7]);
        }
        if (GoblinRenderer.M.contains(name)) {
            return GoblinRenderer.d(stringArray[6]);
        }
        return DEFAULT_COLOR;
    }

    @Override
    protected Vector4f calculateBoneArmorColor(String boneName, float r, float g, float b) {
        if (boneName.startsWith("crown")) {
            ItemStack itemStack = this.renderEntity.getDataManager().get(Fighter.ITEM_SLOT_3);
            if (itemStack.isEmpty()) {
                return super.calculateBoneArmorColor(boneName, r, g, b);
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
                    r = f5;
                    g = f6;
                    b = f7;
                }
            }
            return new Vector4f(r, g, b, 72.0f * f4 / 4096.0f);
        }
        return super.calculateBoneArmorColor(boneName, r, g, b);
    }

    @Override
    protected boolean c(String string) {
        if (string.startsWith("crown")) {
            return true;
        }
        return super.c(string);
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
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
    protected void onBoneRenderStart(String boneName, GeoBone geoBone) {
        String[] stringArray = AbstractGoblinKoboldEntity.SplitDnaIntoGenes(this.renderEntity);
        if (stringArray.length < 8) {
            return;
        }
        switch (boneName) {
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
                GoblinRenderer.a(this.renderEntity, geoBone);
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
        if (boneName.contains("crown")) {
            GoblinRenderer.a(this.renderEntity, geoBone, stringArray[9]);
        }
    }

    @Override
    public void doRender(GirlEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Object object;
        this.D = forceRenderNextFrame;
        this.B = (PlayerGoblin) entity;
        this.C = -420.69f == entityYaw && entity.currentAction() == Action.SHOULDER_IDLE;
        this.E = -420.69f == entityYaw && entity.currentAction() == Action.PICK_UP;
        this.partialTicks = partialTicks;
        GoblinRenderer.B = entityYaw;
        Action fp_class3242 = entity.currentAction();
        UUID uUID = this.B.java_util_UUID_e();
        if (entity.boolean_h()) {
            object = GoblinRenderer.a(entity.world, entity, uUID, x, y, z);
            x = ((Vec3d)object).x;
            y = ((Vec3d)object).y;
            z = ((Vec3d)object).z;
        }
        if (fp_class3242 == Action.THROWN || fp_class3242 == Action.START_THROWING) {
            if (PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0 && entityYaw == -420.69f && !entity.boolean_h()) {
                return;
            }
            if (!entity.boolean_h()) {
                float f3;
                entity.prevRenderYawOffset = f3 = entity.java_lang_Float_I().floatValue();
                entity.renderYawOffset = f3;
            }
        }
        if (GoblinRenderer.a(entity, fp_class3242)) {
            if (PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
                if (-420.69f != entityYaw) {
                    return;
                }
                entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw + 180.0f;
                entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw + 180.0f;
                object = PlayerGoblinRenderer.mc.player.getLookVec();
                GlStateManager.pushMatrix();
                GlStateManager.translate(((Vec3d)object).x, ((Vec3d)object).y + (double) PlayerGoblinRenderer.mc.player.getEyeHeight(), ((Vec3d)object).z);
                Vec3d vec3d = GoblinEntity.b(new Vec3d(-Math.abs(PlayerGoblinRenderer.mc.player.rotationPitch), 0.0, 0.0), PlayerGoblinRenderer.mc.player.rotationYaw);
                GlStateManager.rotate(PlayerGoblinRenderer.mc.player.rotationPitch, (float)vec3d.x, 0.0f, (float)vec3d.z);
                x = 0.0;
                y = 0.0;
                z = 0.0;
            } else if (!this.B.getOwnerUserUUID().equals(PlayerGoblinRenderer.mc.player.getPersistentID())) {
                if (!entity.boolean_h() || uUID == null || PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
                    if (uUID != null && !PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
                        object = entity.world.getPlayerEntityByUUID(uUID);
                        if (object != null) {
                            entity.renderYawOffset = ((EntityPlayer)object).rotationYaw;
                            entity.prevRenderYawOffset = ((EntityPlayer)object).rotationYaw;
                        }
                    } else {
                        entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
                        entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
                    }
                }
                object = GoblinRenderer.a(entity, this.B.java_util_UUID_e(), partialTicks);
                x = ((Vec3d)object).x;
                y = ((Vec3d)object).y;
                z = ((Vec3d)object).z;
            }
        } else if (this.C) {
            GoblinRenderer.a(partialTicks);
            object = new Vec3d(Reference.LerpFloat(-0.1f, 0.2f, PlayerGoblinRenderer.mc.gameSettings.fovSetting / 110.0f), 0.0, 0.0);
            object = GoblinEntity.b((Vec3d)object, PlayerGoblinRenderer.mc.player.rotationYaw);
            x = ((Vec3d)object).x;
            y = ((Vec3d)object).y;
            z = ((Vec3d)object).z;
            entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
            entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.prevRotationYaw;
            if (PlayerGoblinRenderer.mc.player.isSneaking()) {
                y -= 0.075;
            }
        } else if (fp_class3242 == Action.SHOULDER_IDLE) {
            if (uUID == null) {
                return;
            }
            if (PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID) && PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0) {
                return;
            }
            object = entity.world.getPlayerEntityByUUID(uUID);
            if (object == null) {
                return;
            }
            Vector4f vector4f = GoblinRenderer.a_0((EntityPlayer)object, partialTicks);
            x = vector4f.x;
            y = vector4f.y;
            z = vector4f.z;
            entity.renderYawOffset = vector4f.w;
            if (((Entity)object).isSneaking()) {
                y -= 0.32;
            }
        } else if (fp_class3242 == Action.PICK_UP && uUID != null && (object = entity.world.getPlayerEntityByUUID(uUID)) != null) {
            entity.prevRenderYawOffset = ((EntityPlayer)object).prevRotationYawHead;
            entity.renderYawOffset = ((EntityPlayer)object).rotationYawHead;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (GoblinRenderer.a(entity, fp_class3242) && PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0 && PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void drawOverlayLines(Tessellator tessellator, BufferBuilder buffer, GirlEntity girl, Vector3f rgb, float thickness) {
        PlayerGoblinRenderer.drawCustomOverlayBundle(tessellator, buffer, girl, rgb, thickness);
    }

    @Nullable
    protected Vector3f getAdditionalOverlayColor(GirlEntity entity) {
        if (!this.D) {
            return null;
        }
        if (!(entity instanceof PlayerGoblin)) {
            return null;
        }
        PlayerGoblin eq_class2642 = (PlayerGoblin) entity;
        UUID uUID = eq_class2642.getOwnerUserUUID();
        EntityPlayerSP entityPlayerSP = PlayerGoblinRenderer.mc.player;
        if (uUID == null || PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0 && entityPlayerSP.getPersistentID().equals(uUID)) {
            return null;
        }
        EntityPlayer entityPlayer = eq_class2642.getOwnerPlayerEntity();
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
                return new Vector3f(23.0f, 100.0f, 93.0f);
            }
            case GOLD: {
                return new Vector3f(99.0f, 98.0f, 14.0f);
            }
            case CHAIN: 
            case IRON: {
                return new Vector3f(85.0f, 85.0f, 85.0f);
            }
            case LEATHER: 
        }
        int n = itemArmor.getColor(itemStack);
        float f = n >> 16 & 0xFF;
        float f2 = n >> 8 & 0xFF;
        float f3 = n & 0xFF;
        return new Vector3f(f, f2, f3);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0, -0.77, -0.05);
        GlStateManager.scale(0.5, 0.5, 0.5);
    }

    @Override
    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        super.applyItemPostRotation(isLeftHand, stack);
        if (stack.getItem().getItemUseAction(stack) == EnumAction.BOW) {
            if (isLeftHand) {
                GlStateManager.translate(0.1f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            } else {
                GlStateManager.rotate(170.0f, 1.0f, 0.0f, 0.0f);
            }
            return;
        }
        GlStateManager.rotate(isLeftHand ? 70.0f : 180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0, 0.05, -0.03);
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (isLeftHand) {
            if (isActive) {
                GlStateManager.translate(0.0, 0.2, -0.25);
                GlStateManager.rotate(85.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(38.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            } else {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0, -0.265, -0.04);
            }
        } else if (isActive) {
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

