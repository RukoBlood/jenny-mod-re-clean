/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4f
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls;

import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.Action;
import com.trolmastercard.sexmod.bu_class100;
import com.trolmastercard.sexmod.util.GeckoMatrixBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class PlayerGirlRenderer extends GirlRenderer<GirlEntity> {
    static public boolean forceRenderNextFrame = false;
    ItemStack mainHandItem = ItemStack.EMPTY;
    ItemStack offHandItem = ItemStack.EMPTY;
    boolean isSneaking = false;
    boolean isUsingItem = false;
    protected PlayerGirl currentGirl;
    protected float partialTicks;
    float bowPullProgress = 0.0f;

    public PlayerGirlRenderer(RenderManager renderManager, AnimatedGeoModel<GirlEntity> animatedGeoModel) {
        super(renderManager, animatedGeoModel, 0.0);
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
    }

    @CheckReturnValue
    boolean shouldProceedWithRender(GirlEntity girl) {
        if (girl.isLocallyRegistered()) {
            return true;
        }
        boolean shouldRender = forceRenderNextFrame;
        forceRenderNextFrame = false;
        return shouldRender;
    }

    public void doRender(GirlEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // TODO check clash above
        if (!this.shouldProceedWithRender(entity)) {
            return;
        }
        PlayerGirl playerGirl = (PlayerGirl) entity;
        if (playerGirl.getOwnerUserUUID() == null) {
            return;
        }
        EntityPlayer owner = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(playerGirl.getOwnerUserUUID());
        if (owner == null) {
            return;
        }
        this.mainHandItem = owner.getHeldItemMainhand();
        this.offHandItem = owner.getHeldItemOffhand();
        this.isUsingItem = playerGirl.ah;
        this.isSneaking = playerGirl.isPlayerSneaking;
        this.currentGirl = (PlayerGirl) entity;
        this.partialTicks = partialTicks;
        playerGirl.syncWithPlayerProperties(owner);
        if (this.shouldRenderNameTag(owner, entity)) {
            this.renderLivingLabel(entity, owner.getName(), x, y + (double)playerGirl.float_i(), z, 300);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public Entity resolveTargetEntity(GirlEntity girl) {
        if (!(girl instanceof PlayerGirl)) {
            return girl;
        }
        PlayerGirl girlOwned = (PlayerGirl)girl;
        EntityPlayer owner = girlOwned.getOwnerPlayerEntity();
        if (owner == null) {
            return girl;
        }
        return owner;
    }

    boolean shouldRenderNameTag(EntityPlayer player, GirlEntity girl) {
        if (player.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            return false;
        }
        Action currentAction = girl.currentAction();
        if (currentAction == null) {
            return true;
        }
        return !currentAction.hideNameTag;
    }

    protected void onBoneRenderStart(String boneName, GeoBone geoBone) {
    }

    protected void onBoneRenderingLayer(String boneName, GeoBone bone, PlayerGirl playerGirl, BufferBuilder buffer) {
    }

    @Override
    public void renderRecursively(BufferBuilder buffer, GeoBone bone, float red, float green, float blue, float alpha) {
        //ItemStack itemStack;
        String boneName = bone.getName();
        if (this.isSneaking) {
            if (boneName.equals("upperBody")) {
                bone.setRotationX(bone.getRotationX() - 0.5f);
            }
            if (boneName.equals("head")) {
                bone.setRotationX(bone.getRotationX() + 0.5f);
            }
        }
        if (boneName.equals("head")) {
            this.renderOverlay(buffer, bone, Color.ofRGB(red, green, blue));
        }
        this.onBoneRenderStart(boneName, bone);
        this.onBoneRenderingLayer(boneName, bone, this.currentGirl, buffer);
        if (this.isUsingItem && (this.mainHandItem.getItem() instanceof ItemBow || this.offHandItem.getItem() instanceof ItemBow)) {
            if (boneName.equals("armR")) {
                bone.setRotationX(bone.getRotationX() - this.renderEntity.rotationPitch / 50.0f);
            }
            if (boneName.equals("armL")) {
                bone.setRotationY(bone.getRotationY() - this.renderEntity.rotationPitch / 50.0f);
            }
            if (this.offHandItem.getItem() instanceof ItemBow) {
                ItemStack itemStack = this.offHandItem;
                this.offHandItem = this.mainHandItem;
                this.mainHandItem = itemStack;
            }
        }
        if (this.isUsingItem && this.mainHandItem.getItem() instanceof ItemShield) {
            if (boneName.equals("armR")) {
                bone.setRotationZ(0.0f);
                bone.setRotationX(0.5f);
            } else if (this.offHandItem.getItem() instanceof ItemShield && boneName.equals("armL")) {
                bone.setRotationZ(0.0f);
                bone.setRotationX(0.5f);
            }
        }
        if (boneName.equals("weapon") && !this.mainHandItem.isEmpty()) {
            this.renderEquippedItem(buffer, bone, false);
        }
        if (boneName.equals("offhand") && !this.offHandItem.isEmpty()) {
            this.renderEquippedItem(buffer, bone, true);
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);
        if ("Head2".equals(boneName) && !this.boolean_c()) {
            MATRIX_STACK.pop();
            return;
        }
        if (("neck".equals(boneName) || "head".equals(boneName)) && this.shouldHideHeadInFirstPerson()) {
            MATRIX_STACK.pop();
            return;
        }
        if (!bone.isHidden) {
            Vector4f itemStack = this.calculateBoneArmorColor(boneName, red, green, blue);
            red = ((Vector4f) itemStack).x;
            green = ((Vector4f) itemStack).y;
            blue = ((Vector4f) itemStack).z;
            double d = ((Vector4f) itemStack).w;
            if (!this.activeCustomPartBones.contains(boneName)) {
                for (GeoCube object : bone.childCubes) {
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.currentRenderingBone = bone;
                    this.renderCubeGeometry(buffer, object, red, green, blue, alpha, d);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }
            for (GeoBone geoBone2 : bone.childBones) {
                if (d == 0.0) {
                    this.renderRecursively(buffer, geoBone2, red, green, blue, alpha);
                    continue;
                }
                this.renderCustomBones(buffer, geoBone2, red, green, blue, alpha, d);
            }
        }
        MATRIX_STACK.pop();
    }

    boolean shouldHideHeadInFirstPerson() {
        if (!((PlayerGirl)this.renderEntity).boolean_f()) {
            return false;
        }
        if (PlayerGirlRenderer.mc.gameSettings.thirdPersonView != 0) {
            return false;
        }
        return !(PlayerGirlRenderer.mc.currentScreen instanceof GuiInventory) && !(PlayerGirlRenderer.mc.currentScreen instanceof GuiContainerCreative);
    }

    void renderOverlay(BufferBuilder buffer, GeoBone bone, Color color) {
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        GeckoMatrixBridge.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, bone);
        GL11.glEnable(GL11.GL_LIGHTING);
        this.preRenderCallback();
        new bu_class100((IGeoRenderer)this).render(this.renderEntity, this.renderEntity.limbSwing, this.renderEntity.limbSwingAmount, this.partialTicks, 0.0f, 0.0f, 0.0f, color);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
    }

    protected void preRenderCallback() {
    }

    void renderEquippedItem(BufferBuilder buffer, GeoBone bone, boolean isLeftHand) {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        GeckoMatrixBridge.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, bone);
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        ItemStack stack = isLeftHand ? this.offHandItem : this.mainHandItem;
        switch (stack.getItem().getItemUseAction(stack)) {
            case BOW: {
                this.applyBowRotation(isLeftHand);
                break;
            }
            case BLOCK: {
                this.applyShieldBlockingTransform(isLeftHand, this.isUsingItem);
            }
        }
        if (this.isUsingItem && !isLeftHand && stack.getItem() instanceof ItemBow) {
            this.bowPullProgress += 0.015f;
            this.renderEntity.d(Math.round(-this.bowPullProgress * 20.0f + (float)stack.getMaxItemUseDuration()));
            this.renderEntity.void_a(stack);
            this.renderEntity.setActiveHand(EnumHand.MAIN_HAND);
            this.renderEntity.W();
        } else {
            this.bowPullProgress = 0.0f;
            this.renderEntity.d(0);
            this.renderEntity.void_a(ItemStack.EMPTY);
            this.renderEntity.W();
        }
        this.applyItemPostRotation(isLeftHand, stack);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        itemRenderer.renderItem(this.renderEntity, stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        GlStateManager.rotate(isLeftHand ? 200.0f : 90.0f, 1.0f, 0.0f, 0.0f);
    }

    protected void applyBowRotation(boolean isLeftHand) {
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
    }

    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        if (isLeftHand) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            if (isActive) {
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(35.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, 0.228f);
            }
        } else if (isActive) {
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.165f, 0.0f);
        }
    }
}

