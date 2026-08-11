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
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
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
    List<EntityLivingBase> girls = new ArrayList<EntityLivingBase>();
    int b = 0;
    static float angle = 0.0f;

    public b5_class66(HashMap<PlayerGirlEntity, String> hashMap) {
        this.mc = Minecraft.getMinecraft();
        for (PlayerGirlEntity entity : PlayerGirlEntity.values()) {
            if (entity.isNpcOnly) continue;
            try {
                Constructor<? extends GirlEntity> worldConstructor = entity.npcClass.getConstructor(World.class);
                GirlEntity girl = worldConstructor.newInstance(this.mc.world);
                girl.setLocallyRegistered(true);
                this.girls.add(girl);
                String string = hashMap.get((Object)entity);
                if (string == null) continue;
                girl.void_a(GirlEntity.c(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.girls.add(this.mc.player);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        this.buttonList.clear();
        b5_class66.a(this.width / 2, this.height / 2 + 20, 30, this.girls.get(this.b));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 30, this.height / 2 - 10, 20, 20, ">"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 2 - 10, 20, 20, "<"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 30, this.height / 2 + 30, 60, 20, "pick"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (">".equals(guiButton.displayString) && ++this.b >= this.girls.size()) {
            this.b = 0;
        }
        if ("<".equals(guiButton.displayString) && --this.b < 0) {
            this.b = this.girls.size() - 1;
        }
        if (guiButton.id == 0) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new UpdatePlayerModel(PlayerGirlEntity.fromGirl(this.girls.get(this.b))));
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

    public static void a(int n, int n2, int n3, EntityLivingBase entity) {
        float f = entity.renderYawOffset;
        float f2 = entity.rotationYaw;
        float f3 = entity.rotationPitch;
        float f4 = entity.prevRotationYawHead;
        float f5 = entity.rotationYawHead;
        if (!(entity instanceof EntityPlayer)) {
            entity.posX = 0.0;
            entity.posY = 0.0;
            entity.posZ = 0.0;
        }
        entity.renderYawOffset = 0.0f;
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.prevRotationYawHead = 0.0f;
        entity.rotationYawHead = 0.0f;
        float dt = Minecraft.getDebugFPS();
        if (dt == 0.0f) {
            dt = 0.1f;
        }
        angle += 60.0f / dt;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(n, n2, 50.0f);
        GlStateManager.scale(-n3, n3, n3);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entity, 0.0, 0.0, 0.0, 0.0f, 1.2345679f, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        entity.renderYawOffset = f;
        entity.rotationYaw = f2;
        entity.rotationPitch = f3;
        entity.prevRotationYawHead = f4;
        entity.rotationYawHead = f5;
    }
}

