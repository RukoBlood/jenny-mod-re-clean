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

import com.trolmastercard.sexmod.girls.PlayerGirl;
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

public class cn_class138 {
    Minecraft f;
    float g = 2.0f;
    boolean c = false;
    final static private ResourceLocation e = new ResourceLocation("textures/map/map_background.png");
    IRenderer d;
    ResourceLocation h;
    Vec3i b;
    float a = 0.0f;

    @SubscribeEvent
    public void a(RenderSpecificHandEvent renderSpecificHandEvent) {
        //Object object;
        PlayerGirl.void_C();
        PlayerGirl ei_class2512 = PlayerGirl.d_(Minecraft.getMinecraft().player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        int n = ei_class2512.int_ah();
        this.d = ei_class2512.com_trolmastercard_sexmod_at_class43_a(n);
        this.h = new ResourceLocation("sexmod", ei_class2512.HandTexture(n));
        this.b = ei_class2512.net_minecraft_util_math_Vec3i_b(n);
        if (this.d == null) {
            System.out.println("HAND IS NULL uwu did you forget to assign this girl a hand owo?");
            return;
        }
        this.f = Minecraft.getMinecraft();
        float f = 0.0f;
        float f2 = 0.0f;
        try {
            ItemRenderer object = this.f.getItemRenderer();
            if (ad_class25.GetEnv()) {
                f = ((Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, object, "prevEquippedProgressMainHand")).floatValue();
                f2 = ((Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, object, "equippedProgressMainHand")).floatValue();
            } else {
                f = ((Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, object, "prevEquippedProgressMainHand")).floatValue();
                f2 = ((Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, object, "equippedProgressMainHand")).floatValue();
            }
            this.g = 2.0f - (f + (f2 - f) * renderSpecificHandEvent.getPartialTicks());
        } catch (Exception exception) {
            System.out.println("couldnt do the reflection thingy");
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            Minecraft.getMinecraft().player.sendChatMessage(stringWriter.toString());
        }
        EntityPlayerSP object = this.f.player;
        float f3 = ((EntityLivingBase)object).getSwingProgress(renderSpecificHandEvent.getPartialTicks());
        ItemStack itemStack = this.f.player.getHeldItemMainhand();
        GlStateManager.color((float)this.b.getX() / 255.0f, (float)this.b.getY() / 255.0f, (float)this.b.getZ() / 255.0f);
        if (renderSpecificHandEvent.getHand() == EnumHand.MAIN_HAND) {
            if (itemStack.isEmpty() || itemStack.getItem() instanceof ItemMap) {
                renderSpecificHandEvent.setCanceled(true);
                this.a(itemStack, renderSpecificHandEvent.getPartialTicks(), (AbstractClientPlayer)object, this.g, f3);
                this.c = true;
            } else if (f2 < f) {
                if (this.c) {
                    renderSpecificHandEvent.setCanceled(true);
                    this.a(itemStack, renderSpecificHandEvent.getPartialTicks(), (AbstractClientPlayer)object, this.g, f3);
                }
            } else {
                this.c = false;
            }
        } else if (this.f.player.getHeldItemOffhand().getItem() instanceof ItemMap) {
            renderSpecificHandEvent.setCanceled(true);
            this.a(EnumHandSide.LEFT, this.g - 1.0f, f3, this.f.player.getHeldItemOffhand());
        }
        GlStateManager.resetColor();
    }

    void a(ItemStack itemStack, float f, AbstractClientPlayer abstractClientPlayer, float f2, float f3) {
        if (itemStack.getItem() instanceof ItemMap) {
            if (abstractClientPlayer.getHeldItemOffhand().isEmpty()) {
                this.a(itemStack, abstractClientPlayer, f3, f);
            } else {
                this.a(EnumHandSide.RIGHT, f2 - 1.0f, f3, itemStack);
            }
        } else {
            this.a(f3, f);
        }
    }

    void a(EnumHandSide enumHandSide, float f, float f2, ItemStack itemStack) {
        float f3 = enumHandSide == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.translate(f3 * 0.125f, -0.125f, 0.0f);
        if (!this.f.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f3 * 10.0f, 0.0f, 0.0f, 1.0f);
            this.a(f, f2, enumHandSide);
            GlStateManager.translate(-0.5f, -1.1f, 0.0f);
            if (enumHandSide == EnumHandSide.RIGHT) {
                GlStateManager.translate(0.48f, 0.15f, 0.0f);
            } else {
                GlStateManager.translate(0.44f, 1.3f, 1.0f);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.h);
            this.d.Render().render(0.175f);
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(f3 * 0.51f, -0.08f + f * -1.2f, -0.75f);
        float f4 = MathHelper.sqrt(f2);
        float f5 = MathHelper.sin(f4 * (float)Math.PI);
        float f6 = -0.5f * f5;
        float f7 = 0.4f * MathHelper.sin(f4 * ((float)Math.PI * 2));
        float f8 = -0.3f * MathHelper.sin(f2 * (float)Math.PI);
        GlStateManager.translate(f3 * f6, f7 - 0.3f * f5, f8);
        GlStateManager.rotate(f5 * -45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f3 * f5 * -30.0f, 0.0f, 1.0f, 0.0f);
        this.a(itemStack);
        GlStateManager.popMatrix();
    }

    void a(ItemStack itemStack, AbstractClientPlayer abstractClientPlayer, float f, float f2) {
        float f3 = abstractClientPlayer.prevRotationPitch + (abstractClientPlayer.rotationPitch - abstractClientPlayer.prevRotationPitch) * f2;
        float f4 = MathHelper.sqrt(f);
        float f5 = -0.2f * MathHelper.sin(f * (float)Math.PI);
        float f6 = -0.4f * MathHelper.sin(f4 * (float)Math.PI);
        GlStateManager.translate(0.0f, -f5 / 2.0f, f6);
        float f7 = this.a(f3);
        GlStateManager.translate(0.0f, 0.04f + (this.g - 1.0f) * -1.2f + f7 * -0.5f, -0.72f);
        GlStateManager.rotate(f7 * -85.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        this.a(EnumHandSide.RIGHT);
        this.a(EnumHandSide.LEFT);
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        float f8 = MathHelper.sin(f4 * (float)Math.PI);
        GlStateManager.rotate(f8 * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.a(itemStack);
        GlStateManager.enableLighting();
    }

    void a(ItemStack itemStack) {
        GlStateManager.resetColor();
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.disableLighting();
        this.f.getTextureManager().bindTexture(e);
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
        MapData mapData = ((ItemMap)itemStack.getItem()).getMapData(itemStack, this.f.world);
        if (mapData != null) {
            this.f.entityRenderer.getMapItemRenderer().renderMap(mapData, false);
        }
        GlStateManager.color((float)this.b.getX() / 255.0f, (float)this.b.getY() / 255.0f, (float)this.b.getZ() / 255.0f);
    }

    private void a(EnumHandSide enumHandSide) {
        GlStateManager.pushMatrix();
        float f = enumHandSide == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * -41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(f * 0.3f, -1.1f, 0.45f);
        if (enumHandSide == EnumHandSide.RIGHT) {
            GlStateManager.translate(0.63f, 0.36f, 0.0f);
        } else {
            GlStateManager.translate(1.6f, 0.35f, 0.0f);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.h);
        this.d.Render().render(0.175f);
        GlStateManager.popMatrix();
    }

    private float a(float f) {
        float f2 = 1.0f - f / 45.0f + 0.1f;
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        f2 = -MathHelper.cos(f2 * (float)Math.PI) * 0.5f + 0.5f;
        return f2;
    }

    void a(float f, float f2) {
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        this.a(this.g, f, EnumHandSide.RIGHT);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.h);
        this.d.Render().render(0.175f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private void a(float f, float f2, EnumHandSide enumHandSide) {
        boolean bl = enumHandSide != EnumHandSide.LEFT;
        float f3 = bl ? 1.0f : -1.0f;
        float f4 = MathHelper.sqrt(f2);
        float f5 = -0.3f * MathHelper.sin(f4 * (float)Math.PI);
        float f6 = 0.4f * MathHelper.sin(f4 * ((float)Math.PI * 2));
        float f7 = -0.4f * MathHelper.sin(f2 * (float)Math.PI);
        GlStateManager.translate(f3 * (f5 + 0.64000005f), f6 + -0.6f + f * -0.6f, f7 + -0.71999997f);
        GlStateManager.rotate(f3 * 45.0f, 0.0f, 1.0f, 0.0f);
        float f8 = MathHelper.sin(f2 * f2 * (float)Math.PI);
        float f9 = MathHelper.sin(f4 * (float)Math.PI);
        GlStateManager.rotate(f3 * f9 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * f8 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(f3 * -1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(f3 * 120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f3 * -135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(f3 * 5.6f, 0.0f, 0.0f);
        GlStateManager.translate(0.5f, 1.1f, 0.0f);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

