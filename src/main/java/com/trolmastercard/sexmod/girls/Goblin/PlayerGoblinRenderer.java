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

import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.Fighter;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.WorkerPlayerRenderer;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.Vector3fSexmodSpecial;
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

public class PlayerGoblinRenderer extends WorkerPlayerRenderer {
    PlayerGoblin playerGoblin = null;
    boolean isShoulderIdle = false;
    boolean isPickup = false;
    boolean forceRender = false;

    public PlayerGoblinRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
    }

    @Override
    protected Vec3i resolveBoneColor(String name) {
        String[] parts = AbstractNpcOnlyEntity.getModelCodeParts(this.renderEntity);
        if (parts.length < 8) {
            return DEFAULT_COLOR;
        }
        if (name.contains("band")) {
            return GoblinRenderer.DEFAULT_BONE_COLOR;
        }
        if (name.contains("eyeColor") || name.contains("eyeColor2")) {
            return GoblinRenderer.getEyeColor(parts[8]);
        }
        if (name.contains("variant") || name.contains("boob")) {
            return GoblinRenderer.getSkinColor(parts[7]);
        }
        if (name.contains("hair")) {
            return GoblinRenderer.getHairColor(parts[6]);
        }
        if (GoblinRenderer.NUDE_BONE_NAMES.contains(name)) {
            return GoblinRenderer.getSkinColor(parts[7]);
        }
        if (GoblinRenderer.LASH_BONE_NAMES.contains(name)) {
            return GoblinRenderer.getHairColor(parts[6]);
        }
        return DEFAULT_COLOR;
    }

    @Override
    protected Vector4f calculateBoneArmorColor(String boneName, float r, float g, float b) {
        if (boneName.startsWith("crown")) {
            ItemStack itemStack = this.renderEntity.getDataManager().get(Fighter.HELMET_SLOT);
            if (itemStack.isEmpty()) {
                return super.calculateBoneArmorColor(boneName, r, g, b);
            }
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            ItemArmor.ArmorMaterial material = itemArmor.getArmorMaterial();
            float f4 = 0.0f;
            switch (material) {
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
    protected boolean isArmor(String name) {
        return name.startsWith("crown") || super.isArmor(name);
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
        String[] parts = AbstractNpcOnlyEntity.getModelCodeParts(this.renderEntity);
        if (parts.length >= 8) {
            switch (boneName) {
                case "earL": {
                    GoblinRenderer.applyBoneParts(geoBone, parts[0], parts[1], parts[3]);
                    break;
                }
                case "earR": {
                    GoblinRenderer.applyBoneParts(geoBone, parts[0], parts[2], parts[4]);
                    break;
                }
                case "hair": {
                    GoblinRenderer.applyBonePart(geoBone, parts[5]);
                    break;
                }
                case "body": {
                    geoBone.setPivotY(-0.15f);
                    GoblinRenderer.applyBoneState(this.renderEntity, geoBone);
                    break;
                }
                case "LegR": {
                    GoblinRenderer.applyBoneRot(this.isShoulderIdle, geoBone, 25.0f, 25.0f);
                    break;
                }
                case "boobR": {
                    GoblinRenderer.applyBoneRot(this.isShoulderIdle, geoBone, 30.0f, 30.0f);
                    break;
                }
                case "boobR1": {
                    GoblinRenderer.applyBoneRot(this.isShoulderIdle, geoBone, 10.0f, 15.0f);
                    break;
                }
                case "boobR2": {
                    GoblinRenderer.applyBoneRot(this.isShoulderIdle, geoBone, 5.0f, 3.0f);
                }
            }
            if (boneName.contains("crown")) {
                GoblinRenderer.applyBoneColor(this.renderEntity, geoBone, parts[9]);
            }
        }
    }

//    @Override
//    public void doRender(GirlEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
//        Object object; //TODO
//        this.forceRender = forceRenderNextFrame;
//        this.playerGoblin = (PlayerGoblin) entity;
//        this.isShoulderIdle = -420.69f == entityYaw && entity.getCurrentAction() == Action.SHOULDER_IDLE;
//        this.isPickup = -420.69f == entityYaw && entity.getCurrentAction() == Action.PICK_UP;
//        this.partialTicks = partialTicks;
//        GoblinRenderer.currentActionValue = entityYaw;
//        Action action = entity.getCurrentAction();
//        UUID uUID = this.playerGoblin.getOwnerUUID();
//        if (entity.isLocallyRegistered()) {
//            object = GoblinRenderer.getThrowPosition(entity.world, entity, uUID, x, y, z);
//            x = ((Vec3d)object).x;
//            y = ((Vec3d)object).y;
//            z = ((Vec3d)object).z;
//        }
//        if (action == Action.THROWN || action == Action.START_THROWING) {
//            if (PlayerGoblinRenderer.mc.gameSettings.thirdPersonView != 0 || entityYaw != -420.69f || entity.isLocallyRegistered()) {
//                if (!entity.isLocallyRegistered()) {
//                    float f3;
//                    entity.prevRenderYawOffset = f3 = entity.getYawRotation();
//                    entity.renderYawOffset = f3;
//                }
//            }
//        }
//        if (GoblinRenderer.isThrowAction(entity, action)) {
//            if (PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
//                if (-420.69f == entityYaw) {
//                    entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw + 180.0f;
//                    entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw + 180.0f;
//                    object = PlayerGoblinRenderer.mc.player.getLookVec();
//                    GlStateManager.pushMatrix();
//                    GlStateManager.translate(((Vec3d) object).x, ((Vec3d) object).y + (double) PlayerGoblinRenderer.mc.player.getEyeHeight(), ((Vec3d) object).z);
//                    Vec3d vec3d = GoblinEntity.rotateVectorYaw(new Vec3d(-Math.abs(PlayerGoblinRenderer.mc.player.rotationPitch), 0.0, 0.0), PlayerGoblinRenderer.mc.player.rotationYaw);
//                    GlStateManager.rotate(PlayerGoblinRenderer.mc.player.rotationPitch, (float) vec3d.x, 0.0f, (float) vec3d.z);
//                    x = 0.0;
//                    y = 0.0;
//                    z = 0.0;
//                }
//            } else if (!this.playerGoblin.getOwnerUserUUID().equals(PlayerGoblinRenderer.mc.player.getPersistentID())) {
//                if (!entity.isLocallyRegistered() || uUID == null || PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
//                    if (uUID != null && !PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
//                        object = entity.world.getPlayerEntityByUUID(uUID);
//                        if (object != null) {
//                            entity.renderYawOffset = ((EntityPlayer)object).rotationYaw;
//                            entity.prevRenderYawOffset = ((EntityPlayer)object).rotationYaw;
//                        }
//                    } else {
//                        entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
//                        entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
//                    }
//                }
//                object = GoblinRenderer.getThrowAim(entity, this.playerGoblin.getOwnerUUID(), partialTicks);
//                x = ((Vec3d)object).x;
//                y = ((Vec3d)object).y;
//                z = ((Vec3d)object).z;
//            }
//        } else if (this.isShoulderIdle) {
//            GoblinRenderer.setFirstPersonCamera(partialTicks);
//            object = new Vec3d(RotationHelper.LerpFloat(-0.1f, 0.2f, PlayerGoblinRenderer.mc.gameSettings.fovSetting / 110.0f), 0.0, 0.0);
//            object = GoblinEntity.rotateVectorYaw((Vec3d)object, PlayerGoblinRenderer.mc.player.rotationYaw);
//            x = ((Vec3d)object).x;
//            y = ((Vec3d)object).y;
//            z = ((Vec3d)object).z;
//            entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
//            entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.prevRotationYaw;
//            if (PlayerGoblinRenderer.mc.player.isSneaking()) {
//                y -= 0.075;
//            }
//        } else if (action == Action.SHOULDER_IDLE) {
//            if (uUID == null) {
//                return;
//            }
//            if (PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID) && PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0) {
//                return;
//            }
//            object = entity.world.getPlayerEntityByUUID(uUID);
//            if (object == null) {
//                return;
//            }
//            Vector4f vector4f = GoblinRenderer.getFirstPersonView((EntityPlayer)object, partialTicks);
//            x = vector4f.x;
//            y = vector4f.y;
//            z = vector4f.z;
//            entity.renderYawOffset = vector4f.w;
//            if (((Entity)object).isSneaking()) {
//                y -= 0.32;
//            }
//        } else if (action == Action.PICK_UP && uUID != null && (object = entity.world.getPlayerEntityByUUID(uUID)) != null) {
//            entity.prevRenderYawOffset = ((EntityPlayer)object).prevRotationYawHead;
//            entity.renderYawOffset = ((EntityPlayer)object).rotationYawHead;
//        }
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
//        if (GoblinRenderer.isThrowAction(entity, action) && PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0 && PlayerGoblinRenderer.mc.player.getPersistentID().equals(uUID)) {
//            GlStateManager.popMatrix();
//        }
//    }

    @Override
    public void doRender(GirlEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.forceRender = forceRenderNextFrame;
        this.playerGoblin = (PlayerGoblin) entity;
        this.isShoulderIdle = -420.69f == entityYaw && entity.getCurrentAction() == Action.SHOULDER_IDLE;
        this.isPickup = -420.69f == entityYaw && entity.getCurrentAction() == Action.PICK_UP;
        this.partialTicks = partialTicks;
        GoblinRenderer.currentActionValue = entityYaw;
        Action action = entity.getCurrentAction();
        UUID ownerUUID = this.playerGoblin.getOwnerUUID();

        if (entity.isLocallyRegistered()) {
            Vec3d throwPosition = GoblinRenderer.getThrowPosition(entity.world, entity, ownerUUID, x, y, z);
            x = throwPosition.x;
            y = throwPosition.y;
            z = throwPosition.z;
        }

        if (action == Action.THROWN || action == Action.START_THROWING) {
            if (PlayerGoblinRenderer.mc.gameSettings.thirdPersonView != 0 || entityYaw != -420.69f || entity.isLocallyRegistered()) {
                if (!entity.isLocallyRegistered()) {
                    float yaw = entity.getYawRotation();
                    entity.prevRenderYawOffset = yaw;
                    entity.renderYawOffset = yaw;
                }
            }
        }

        if (GoblinRenderer.isThrowAction(entity, action)) {
            if (PlayerGoblinRenderer.mc.player.getPersistentID().equals(ownerUUID)) {
                if (-420.69f == entityYaw) {
                    entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw + 180.0f;
                    entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw + 180.0f;

                    Vec3d lookVector = PlayerGoblinRenderer.mc.player.getLookVec();

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(lookVector.x, lookVector.y + PlayerGoblinRenderer.mc.player.getEyeHeight(), lookVector.z);

                    Vec3d rotationVector = GoblinEntity.rotateVectorYaw(new Vec3d(-Math.abs(PlayerGoblinRenderer.mc.player.rotationPitch), 0.0, 0.0), PlayerGoblinRenderer.mc.player.rotationYaw);

                    GlStateManager.rotate(PlayerGoblinRenderer.mc.player.rotationPitch, (float) rotationVector.x, 0.0f, (float) rotationVector.z);

                    x = 0.0;
                    y = 0.0;
                    z = 0.0;
                }
            } else if (!this.playerGoblin.getOwnerUserUUID().equals(PlayerGoblinRenderer.mc.player.getPersistentID())) {
                if (!entity.isLocallyRegistered() || ownerUUID == null || PlayerGoblinRenderer.mc.player.getPersistentID().equals(ownerUUID)) {
                    if (ownerUUID != null && !PlayerGoblinRenderer.mc.player.getPersistentID().equals(ownerUUID)) {
                        EntityPlayer ownerPlayer = entity.world.getPlayerEntityByUUID(ownerUUID);
                        if (ownerPlayer != null) {
                            entity.renderYawOffset = ownerPlayer.rotationYaw;
                            entity.prevRenderYawOffset = ownerPlayer.rotationYaw;
                        }
                    } else {
                        entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
                        entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
                    }
                }

                Vec3d throwAim = GoblinRenderer.getThrowAim(entity, this.playerGoblin.getOwnerUUID(), partialTicks);

                x = throwAim.x;
                y = throwAim.y;
                z = throwAim.z;
            }
        } else if (this.isShoulderIdle) {
            GoblinRenderer.setFirstPersonCamera(partialTicks);
            Vec3d cameraOffset = new Vec3d(RotationHelper.LerpFloat(-0.1f, 0.2f, PlayerGoblinRenderer.mc.gameSettings.fovSetting / 110.0f), 0.0, 0.0);

            cameraOffset = GoblinEntity.rotateVectorYaw(cameraOffset, PlayerGoblinRenderer.mc.player.rotationYaw);

            x = cameraOffset.x;
            y = cameraOffset.y;
            z = cameraOffset.z;

            entity.renderYawOffset = PlayerGoblinRenderer.mc.player.rotationYaw;
            entity.prevRenderYawOffset = PlayerGoblinRenderer.mc.player.prevRotationYaw;

            if (PlayerGoblinRenderer.mc.player.isSneaking()) {
                y -= 0.075;
            }
        } else if (action == Action.SHOULDER_IDLE) {
            if (ownerUUID != null) {
                if (!PlayerGoblinRenderer.mc.player.getPersistentID().equals(ownerUUID) || PlayerGoblinRenderer.mc.gameSettings.thirdPersonView != 0) {
                    EntityPlayer ownerPlayer = entity.world.getPlayerEntityByUUID(ownerUUID);
                    if (ownerPlayer != null) {
                        Vector4f firstPersonView = GoblinRenderer.getFirstPersonView(ownerPlayer, partialTicks);

                        x = firstPersonView.x;
                        y = firstPersonView.y;
                        z = firstPersonView.z;
                        entity.renderYawOffset = firstPersonView.w;

                        if (ownerPlayer.isSneaking()) {
                            y -= 0.32;
                        }
                    }
                }
            }
        } else if (action == Action.PICK_UP && ownerUUID != null) {
            EntityPlayer ownerPlayer = entity.world.getPlayerEntityByUUID(ownerUUID);
            if (ownerPlayer != null) {
                entity.prevRenderYawOffset = ownerPlayer.prevRotationYawHead;
                entity.renderYawOffset = ownerPlayer.rotationYawHead;
            }
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if (GoblinRenderer.isThrowAction(entity, action) && PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0 && PlayerGoblinRenderer.mc.player.getPersistentID().equals(ownerUUID)) {
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void drawOverlayLines(Tessellator tessellator, BufferBuilder buffer, GirlEntity girl, Vector3fSexmodSpecial rgb, float thickness) {
        PlayerGoblinRenderer.drawCustomOverlayBundle(tessellator, buffer, girl, rgb, thickness);
    }

    @Nullable
    protected Vector3fSexmodSpecial getAdditionalOverlayColor(GirlEntity entity) {
        if (!this.forceRender) {
            return null;
        }
        if (!(entity instanceof PlayerGoblin)) {
            return null;
        }
        PlayerGoblin pgob = (PlayerGoblin) entity;
        UUID ownerId = pgob.getOwnerUserUUID();
        EntityPlayerSP entityPlayerSP = PlayerGoblinRenderer.mc.player;
        if (ownerId == null || PlayerGoblinRenderer.mc.gameSettings.thirdPersonView == 0 && entityPlayerSP.getPersistentID().equals(ownerId)) {
            return null;
        }
        EntityPlayer ownerPlayer = pgob.getOwnerPlayer();
        if (ownerPlayer == null) {
            return null;
        }
        ItemStack itemStack = pgob.getDataManager().get(Fighter.CHEST_SLOT);
        if (itemStack.isEmpty()) {
            return null;
        }
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return null;
        }
        ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        switch (itemArmor.getArmorMaterial()) {
            default: {
                return new Vector3fSexmodSpecial(23.0f, 100.0f, 93.0f);
            }
            case GOLD: {
                return new Vector3fSexmodSpecial(99.0f, 98.0f, 14.0f);
            }
            case CHAIN: 
            case IRON: {
                return new Vector3fSexmodSpecial(85.0f, 85.0f, 85.0f);
            }
            case LEATHER: 
        }
        int n = itemArmor.getColor(itemStack);
        float f = n >> 16 & 0xFF;
        float f2 = n >> 8 & 0xFF;
        float f3 = n & 0xFF;
        return new Vector3fSexmodSpecial(f, f2, f3);
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
}

