/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderSpecificHandEvent
 *  net.minecraftforge.fml.common.ObfuscationReflectionHelper
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InHandMapRenderer {
    Minecraft mc;
    float equippedProgressInverse = 2.0f;
    boolean isRenderingCustomHand = false;
    final static private ResourceLocation MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    IRenderer handModelRenderer;
    ResourceLocation texture;
    Vec3i handColor;

    @SubscribeEvent
    public void onRenderingSpecificHandEvent(RenderSpecificHandEvent event) {
        //Object object;
        PlayerGirl.cleanupGlobalRegistry();
        PlayerGirl state = PlayerGirl.getUUIDHashtable(Minecraft.getMinecraft().player.getPersistentID());
        if (state == null) {
            return;
        }
        int activeHandIndex = state.getOutfitIndex();
        this.handModelRenderer = state.getHandRenderer(activeHandIndex);
        this.texture = new ResourceLocation("sexmod", state.HandTexture(activeHandIndex));
        this.handColor = state.net_minecraft_util_math_Vec3i_b(activeHandIndex);
        if (this.handModelRenderer == null) {
            System.out.println("HAND IS NULL uwu did you forget to assign this girl a hand owo?");
            return;
        }
        this.mc = Minecraft.getMinecraft();
        float prevEquippedProgress = 0.0f;
        float currentEquippedProgress = 0.0f;

        try {
            ItemRenderer itemRenderer = this.mc.getItemRenderer();
            prevEquippedProgress = ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, itemRenderer, "prevEquippedProgressMainHand");
            currentEquippedProgress = ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, itemRenderer, "equippedProgressMainHand");
            this.equippedProgressInverse = 2.0f - (prevEquippedProgress + (currentEquippedProgress - prevEquippedProgress) * event.getPartialTicks());
        } catch (Exception e) {
            System.out.println("couldnt do the reflection thingy");
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            Minecraft.getMinecraft().player.sendChatMessage(stringWriter.toString());
        }

        EntityPlayerSP player = this.mc.player;
        float swingProgress = ((EntityLivingBase)player).getSwingProgress(event.getPartialTicks());
        ItemStack heldItemMainhand = this.mc.player.getHeldItemMainhand();
        GlStateManager.color((float)this.handColor.getX() / 255.0f, (float)this.handColor.getY() / 255.0f, (float)this.handColor.getZ() / 255.0f);
        if (event.getHand() == EnumHand.MAIN_HAND) {
            if (heldItemMainhand.isEmpty() || heldItemMainhand.getItem() instanceof ItemMap) {
                event.setCanceled(true);
                this.renderHandOrMapStack(heldItemMainhand, event.getPartialTicks(), (AbstractClientPlayer)player, this.equippedProgressInverse, swingProgress);
                this.isRenderingCustomHand = true;
            } else if (currentEquippedProgress < prevEquippedProgress) {
                if (this.isRenderingCustomHand) {
                    event.setCanceled(true);
                    this.renderHandOrMapStack(heldItemMainhand, event.getPartialTicks(), (AbstractClientPlayer)player, this.equippedProgressInverse, swingProgress);
                }
            } else {
                this.isRenderingCustomHand = false;
            }
        } else if (this.mc.player.getHeldItemOffhand().getItem() instanceof ItemMap) {
            event.setCanceled(true);
            this.renderOneHandedMap(EnumHandSide.LEFT, this.equippedProgressInverse - 1.0f, swingProgress, this.mc.player.getHeldItemOffhand());
        }
        GlStateManager.resetColor();
    }

    void renderHandOrMapStack(ItemStack stack, float partialTicks, AbstractClientPlayer player, float equipProgress, float swingProgress) {
        if (stack.getItem() instanceof ItemMap) {
            if (player.getHeldItemOffhand().isEmpty()) {
                this.renderTwoHandedMap(stack, player, swingProgress, partialTicks);
            } else {
                this.renderOneHandedMap(EnumHandSide.RIGHT, equipProgress - 1.0f, swingProgress, stack);
            }
        } else {
            this.renderEmptyHand(swingProgress, partialTicks);
        }
    }

    void renderOneHandedMap(EnumHandSide handSide, float equipProgress, float swingProgress, ItemStack mapStack) {
        float sideSign = handSide == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.translate(sideSign * 0.125f, -0.125f, 0.0f);

        if (!this.mc.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(sideSign * 10.0f, 0.0f, 0.0f, 1.0f);
            this.applyHandTransformation(equipProgress, swingProgress, handSide);
            GlStateManager.translate(-0.5f, -1.1f, 0.0f);
            if (handSide == EnumHandSide.RIGHT) {
                GlStateManager.translate(0.48f, 0.15f, 0.0f);
            } else {
                GlStateManager.translate(0.44f, 1.3f, 1.0f);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
            this.handModelRenderer.Render().render(0.175f);
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(sideSign * 0.51f, -0.08f + equipProgress * -1.2f, -0.75f);
        float sqrtSwing = MathHelper.sqrt(swingProgress);
        float sinSwing1 = MathHelper.sin(sqrtSwing * (float)Math.PI);
        float translateX = -0.5f * sinSwing1;
        float translateY = 0.4f * MathHelper.sin(sqrtSwing * ((float)Math.PI * 2));
        float translateZ = -0.3f * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(sideSign * translateX, translateY - 0.3f * sinSwing1, translateZ);
        GlStateManager.rotate(sinSwing1 * -45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(sideSign * sinSwing1 * -30.0f, 0.0f, 1.0f, 0.0f);
        this.renderMapCanvas(mapStack);
        GlStateManager.popMatrix();
    }

    void renderTwoHandedMap(ItemStack mapStack, AbstractClientPlayer player, float swingProgress, float PartialTicks) {
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * PartialTicks;
        float sqrtSwing = MathHelper.sqrt(swingProgress);
        float swingY = -0.2f * MathHelper.sin(swingProgress * (float)Math.PI);
        float swingZ = -0.4f * MathHelper.sin(sqrtSwing * (float)Math.PI);
        GlStateManager.translate(0.0f, -swingY / 2.0f, swingZ);
        float pitchModifier = this.calculateMapPitchOffset(pitch);
        GlStateManager.translate(0.0f, 0.04f + (this.equippedProgressInverse - 1.0f) * -1.2f + pitchModifier * -0.5f, -0.72f);
        GlStateManager.rotate(pitchModifier * -85.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        this.renderMapHoldingHand(EnumHandSide.RIGHT);
        this.renderMapHoldingHand(EnumHandSide.LEFT);
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        float sinSwing = MathHelper.sin(sqrtSwing * (float)Math.PI);
        GlStateManager.rotate(sinSwing * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.renderMapCanvas(mapStack);
        GlStateManager.enableLighting();
    }

    void renderMapCanvas(ItemStack mapStack) {
        GlStateManager.resetColor();
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5f, -0.5f, 0.0f);
        GlStateManager.scale(0.0078125f, 0.0078125f, 0.0078125f);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        bufferBuilder.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        bufferBuilder.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        bufferBuilder.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapData = ((ItemMap)mapStack.getItem()).getMapData(mapStack, this.mc.world);
        if (mapData != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapData, false);
        }
        GlStateManager.color((float)this.handColor.getX() / 255.0f, (float)this.handColor.getY() / 255.0f, (float)this.handColor.getZ() / 255.0f);
    }

    private void renderMapHoldingHand(EnumHandSide handSide) {
        GlStateManager.pushMatrix();
        float sideSign = handSide == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(sideSign * -41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(sideSign * 0.3f, -1.1f, 0.45f);
        if (handSide == EnumHandSide.RIGHT) {
            GlStateManager.translate(0.63f, 0.36f, 0.0f);
        } else {
            GlStateManager.translate(1.6f, 0.35f, 0.0f);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.handModelRenderer.Render().render(0.175f);
        GlStateManager.popMatrix();
    }

    private float calculateMapPitchOffset(float pitch) {
        float offset = 1.0f - pitch / 45.0f + 0.1f;
        offset = MathHelper.clamp(offset, 0.0f, 1.0f);
        offset = -MathHelper.cos(offset * (float)Math.PI) * 0.5f + 0.5f;
        return offset;
    }

    void renderEmptyHand(float swingProgress, float partialTicks) {
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        this.applyHandTransformation(this.equippedProgressInverse, swingProgress, EnumHandSide.RIGHT);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.handModelRenderer.Render().render(0.175f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private void applyHandTransformation(float equipProgress, float swingProgress, EnumHandSide handSide) {
        boolean isRightHand = handSide != EnumHandSide.LEFT;
        float sideSign = isRightHand ? 1.0f : -1.0f;
        float sqrtSwing = MathHelper.sqrt(swingProgress);
        float swingX = -0.3f * MathHelper.sin(sqrtSwing * (float)Math.PI);
        float swingY = 0.4f * MathHelper.sin(sqrtSwing * ((float)Math.PI * 2));
        float swingZ = -0.4f * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(sideSign * (swingX + 0.64000005f), swingY + -0.6f + equipProgress * -0.6f, swingZ + -0.71999997f);
        GlStateManager.rotate(sideSign * 45.0f, 0.0f, 1.0f, 0.0f);
        float sinSwingSquare = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float sinSwing = MathHelper.sin(sqrtSwing * (float)Math.PI);
        GlStateManager.rotate(sideSign * sinSwing * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(sideSign * sinSwingSquare * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(sideSign * -1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(sideSign * 120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(sideSign * -135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(sideSign * 5.6f, 0.0f, 0.0f);
        GlStateManager.translate(0.5f, 1.1f, 0.0f);
    }
}

