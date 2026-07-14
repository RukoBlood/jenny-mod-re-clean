/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.trolmastercard.sexmod.Packages.UpdatePlayerModel;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//b5.class

public class b5_class66 extends GuiScreen {
    List<EntityLivingBase> a = new ArrayList<EntityLivingBase>();
    int b = 0;
    static float c = 0.0f;

    public b5_class66(HashMap<PlayerGirlEntity, String> hashMap) {
        this.mc = Minecraft.getMinecraft();
        for (PlayerGirlEntity fy_class3352 : PlayerGirlEntity.values()) {
            if (fy_class3352.isNpcOnly) continue;
            try {
                Constructor<? extends GirlEntity> constructor = fy_class3352.npcClass.getConstructor(World.class);
                GirlEntity em_class2582 = constructor.newInstance(this.mc.world);
                em_class2582.b(true);
                this.a.add(em_class2582);
                String string = hashMap.get((Object)fy_class3352);
                if (string == null) continue;
                em_class2582.void_a(GirlEntity.c(string));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        this.a.add(this.mc.player);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        this.buttonList.clear();
        b5_class66.a(this.width / 2, this.height / 2 + 20, 30, this.a.get(this.b));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 30, this.height / 2 - 10, 20, 20, ">"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 2 - 10, 20, 20, "<"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 30, this.height / 2 + 30, 60, 20, "pick"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (">".equals(guiButton.displayString) && ++this.b >= this.a.size()) {
            this.b = 0;
        }
        if ("<".equals(guiButton.displayString) && --this.b < 0) {
            this.b = this.a.size() - 1;
        }
        if (guiButton.id == 0) {
            PackageHandler.networkWrapper.sendToServer((IMessage)new UpdatePlayerModel(PlayerGirlEntity.a(this.a.get(this.b))));
            EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
            ((EntityPlayer)entityPlayerSP).closeScreen();
            entityPlayerSP.eyeHeight = entityPlayerSP.getDefaultEyeHeight();
            if (!entityPlayerSP.capabilities.allowFlying) {
                entityPlayerSP.capabilities.allowFlying = entityPlayerSP.capabilities.isCreativeMode;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void a(int n, int n2, int n3, EntityLivingBase entityLivingBase) {
        float f = entityLivingBase.renderYawOffset;
        float f2 = entityLivingBase.rotationYaw;
        float f3 = entityLivingBase.rotationPitch;
        float f4 = entityLivingBase.prevRotationYawHead;
        float f5 = entityLivingBase.rotationYawHead;
        if (!(entityLivingBase instanceof EntityPlayer)) {
            entityLivingBase.posX = 0.0;
            entityLivingBase.posY = 0.0;
            entityLivingBase.posZ = 0.0;
        }
        entityLivingBase.renderYawOffset = 0.0f;
        entityLivingBase.rotationYaw = 0.0f;
        entityLivingBase.rotationPitch = 0.0f;
        entityLivingBase.prevRotationYawHead = 0.0f;
        entityLivingBase.rotationYawHead = 0.0f;
        float f6 = Minecraft.getDebugFPS();
        if (f6 == 0.0f) {
            f6 = 0.1f;
        }
        c += 60.0f / f6;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(n, n2, 50.0f);
        GlStateManager.scale(-n3, n3, n3);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(c, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.2345679f, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        entityLivingBase.renderYawOffset = f;
        entityLivingBase.rotationYaw = f2;
        entityLivingBase.rotationPitch = f3;
        entityLivingBase.prevRotationYawHead = f4;
        entityLivingBase.rotationYawHead = f5;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

