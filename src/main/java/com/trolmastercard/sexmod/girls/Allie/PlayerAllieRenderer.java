/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.trolmastercard.sexmod.girls.Allie;

import java.util.ArrayList;
import java.util.Collection;

import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlRenderer;
import com.trolmastercard.sexmod.util.RotationHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerAllieRenderer extends PlayerGirlRenderer {
    final static float BOB_SCALE_1 = 8.0f;
    final static float BOB_SCALE_2 = 1.68f;
    final static float BOB_SCALE_3 = 5.0f;
    static Collection<PlayerAllieRenderer> renderers = new ArrayList<>();
    double currentPosX = 0.0;
    double currentPosZ = 0.0;
    double prevPosX = 0.0;
    double prevPosZ = 0.0;
    float prevRotX = 0.0f;
    float prevRotZ = 0.0f;
    float rotX;
    float rotZ;
    double smoothedBob = 0.0;
    double moveMagnitude = 0.0;

    public PlayerAllieRenderer(RenderManager manager, AnimatedGeoModel model) {
        super(manager, model);
        renderers.add(this);
    }

    @Override
    protected void preRenderCallback() {
        GlStateManager.translate(0.0f, -1.1f, 0.0f);
        GlStateManager.scale(0.7f, 0.7f, 0.7f);
    }

    @Override
    protected void applyItemPostRotation(boolean isLeftHand, ItemStack stack) {
        super.applyItemPostRotation(isLeftHand, stack);
        switch (stack.getItem().getItemUseAction(stack)) {
            case BLOCK: 
            case BOW: {
                break;
            }
            default: {
                if (!isLeftHand) {
                    GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.translate(0.0, 0.05, 0.0);
            }
        }
    }

    @Override
    protected void applyBowRotation(boolean isLeftHand) {
        super.applyBowRotation(isLeftHand);
        if (isLeftHand) {
            GlStateManager.translate(0.15, 0.0, 0.0);
        } else {
            GlStateManager.translate(-0.05, 0.0, 0.0);
        }
    }

    @Override
    protected void applyShieldBlockingTransform(boolean isLeftHand, boolean isActive) {
        super.applyShieldBlockingTransform(isLeftHand, isActive);
        if (isLeftHand && !isActive) {
            GlStateManager.translate(-0.025, -0.1, -0.1);
            GlStateManager.rotate(10.0f, 1.0f, 0.0f, 0.0f);
            return;
        }
        if (!isLeftHand && !isActive) {
            GlStateManager.translate(-0.05, -0.125, 0.125);
            GlStateManager.rotate(50.0f, 1.0f, 0.0f, 0.0f);
        }
    }

    @Override
    protected void onBoneRenderStart(String boneName, GeoBone geoBone) {
        if (!this.currentGirl.getDataManager().get(GirlEntity.IS_ANCHORED)) {
            if ("tail".equals(boneName)) {
                this.applyBoneRotation(geoBone, 0.0f, 0.0f, 1.0f);
            }
            if ("body".equals(boneName)) {
                this.updateBoneBob(geoBone);
            }
            if (this.currentGirl.getCurrentAction() != Action.BOW) {
                if ("armL".equals(boneName)) {
                    this.applyBoneRotation(geoBone, 0.0f, -0.34906584f, 0.15f);
                }
                if (this.currentGirl.getCurrentAction() != Action.ATTACK) {
                    if ("armR".equals(boneName)) {
                        this.applyBoneRotation(geoBone, 0.0f, 0.34906584f, 0.15f);
                    }
                }
            }
        }
    }

    void applyBoneRotation(GeoBone geoBone, float f, float f2, float f3) {
        double d = this.currentPosX - this.prevPosX;
        double d2 = this.currentPosZ - this.prevPosZ;
        double d3 = Math.PI / 180 * (double)this.currentGirl.rotationYaw;
        Vec2f vec2f = new Vec2f((float)(d * Math.cos(d3) + d2 * Math.sin(d3)), (float)(-d * Math.sin(d3) + d2 * Math.cos(d3)));
        this.rotX = vec2f.y * -8.0f;
        this.rotZ = vec2f.x * 8.0f;
        this.rotX = ThreadNames.clamp(this.rotX, -1.68f, 1.68f);
        this.rotZ = ThreadNames.clamp(this.rotZ, -1.68f, 1.68f);
        this.rotX = RotationHelper.LerpFloat(this.prevRotX, this.rotX, this.partialTicks);
        this.rotZ = RotationHelper.LerpFloat(this.prevRotZ, this.rotZ, this.partialTicks);
        geoBone.setRotationX(f + this.rotX * f3);
        geoBone.setRotationZ(f2 + this.rotZ * f3);
    }

    void updateBoneBob(GeoBone geoBone) {
        double d = this.currentPosX - this.prevPosX;
        double d2 = this.currentPosZ - this.prevPosZ;
        this.moveMagnitude = (Math.abs(d) + Math.abs(d2)) * 5.0;
        this.moveMagnitude = ThreadNames.clamp((float)this.moveMagnitude, 0.0f, 1.0f);
        geoBone.setPositionY((float) RotationHelper.lerpAngle(5.0, 0.0, RotationHelper.LerpDouble(this.smoothedBob, this.moveMagnitude, this.partialTicks)));
        if (this.currentGirl instanceof PlayerAllie) {
            ((PlayerAllie)this.currentGirl).scaleOffset = (float) RotationHelper.lerpAngle(0.3f, 0.0, RotationHelper.LerpDouble(this.smoothedBob, this.moveMagnitude, this.partialTicks));
        }
    }

    void updateCameraRotations() {
        if (this.currentGirl != null) {
            this.prevRotX = this.rotX;
            this.prevRotZ = this.rotZ;
            this.smoothedBob = this.moveMagnitude;
            if (this.currentGirl.getOwnerUserUUID() != null) {
                EntityPlayer player = this.renderEntity.world.getPlayerEntityByUUID(this.currentGirl.getOwnerUserUUID());
                if (player != null) {
                    this.prevPosX = this.currentPosX;
                    this.prevPosZ = this.currentPosZ;
                    this.currentPosX = player.posX;
                    this.currentPosZ = player.posZ;
                }
            }
        }
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event) {
            for (PlayerAllieRenderer renderer : renderers) {
                renderer.updateCameraRotations();
            }
        }
    }
}

