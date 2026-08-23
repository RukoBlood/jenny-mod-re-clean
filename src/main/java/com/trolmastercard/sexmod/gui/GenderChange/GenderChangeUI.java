/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui.GenderChange;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.trolmastercard.sexmod.Packets.UpdatePlayerModel;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
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

public class GenderChangeUI extends GuiScreen {
    List<EntityLivingBase> entities = new ArrayList<>();
    int i = 0;
    static float rot = 0.0f;

    public GenderChangeUI(HashMap<PlayerGirlEntity, String> hashMap) {
        this.mc = Minecraft.getMinecraft();
        for (PlayerGirlEntity entity : PlayerGirlEntity.values()) {
            if (entity.isNpcOnly) continue;
            try {
                Constructor<? extends GirlEntity> worldConstructor = entity.npcClass.getConstructor(World.class);
                GirlEntity girl = worldConstructor.newInstance(this.mc.world);
                girl.setLocallyRegistered(true);
                this.entities.add(girl);
                String string = hashMap.get((Object)entity);
                if (string == null) continue;
                girl.setCustomPartList(GirlEntity.decodePartIdList(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.entities.add(this.mc.player);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.buttonList.clear();
        GenderChangeUI.drawEntityOnScreen(this.width / 2, this.height / 2 + 20, 30, this.entities.get(this.i));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 30, this.height / 2 - 10, 20, 20, ">"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 2 - 10, 20, 20, "<"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 30, this.height / 2 + 30, 60, 20, "pick"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (">".equals(button.displayString) && ++this.i >= this.entities.size()) {
            this.i = 0;
        }
        if ("<".equals(button.displayString) && --this.i < 0) {
            this.i = this.entities.size() - 1;
        }
        if (button.id == 0) {
            PacketHandler.INSTANCE.sendToServer((IMessage)new UpdatePlayerModel(PlayerGirlEntity.getGirlType(this.entities.get(this.i))));
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ((EntityPlayer)player).closeScreen();
            player.eyeHeight = player.getDefaultEyeHeight();
            if (!player.capabilities.allowFlying) {
                player.capabilities.allowFlying = player.capabilities.isCreativeMode;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent) {
        float renderYawOffset = ent.renderYawOffset;
        float rotationYaw = ent.rotationYaw;
        float rotationPitch = ent.rotationPitch;
        float prevRotationYawHead = ent.prevRotationYawHead;
        float rotationYawHead = ent.rotationYawHead;

        if (!(ent instanceof EntityPlayer)) {
            ent.posX = 0.0;
            ent.posY = 0.0;
            ent.posZ = 0.0;
        }

        ent.renderYawOffset = 0.0f;
        ent.rotationYaw = 0.0f;
        ent.rotationPitch = 0.0f;
        ent.prevRotationYawHead = 0.0f;
        ent.rotationYawHead = 0.0f;

        float dt = Minecraft.getDebugFPS();
        if (dt == 0.0f) {
            dt = 0.1f;
        }
        rot += 60.0f / dt;

        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(rot, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        manager.setPlayerViewY(180.0f);
        manager.setRenderShadow(false);
        manager.renderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.2345679f, false);
        manager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        ent.renderYawOffset = renderYawOffset;
        ent.rotationYaw = rotationYaw;
        ent.rotationPitch = rotationPitch;
        ent.prevRotationYawHead = prevRotationYawHead;
        ent.rotationYawHead = rotationYawHead;
    }
}

