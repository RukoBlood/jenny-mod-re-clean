/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.base.PlayerGirl;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.base.Fighter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class PlayerGirlElytraRenderer extends GeoLayerRenderer {
    final static private ResourceLocation ELYTRA_TEXTURE = new ResourceLocation("textures/entity/elytra.png");
    final private ModelElytra modelElytra = new ModelElytra();

    public PlayerGirlElytraRenderer(IGeoRenderer iGeoRenderer) {
        super(iGeoRenderer);
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float unused, float ageInTicks, float netHeadYaw, float headPitch, Color color) {
        UUID uUID;
        if (entity instanceof Fighter) {
            Fighter fighter = (Fighter) entity;
            ItemStack itemStack = fighter.getDataManager().get(Fighter.CHEST_SLOT);
            EntityPlayer entityPlayer = null;
            if (fighter instanceof PlayerGirl && (uUID = ((PlayerGirl) fighter).getOwnerUserUUID()) != null) {
                entityPlayer = entity.world.getPlayerEntityByUUID(uUID);
            }
            if (itemStack.getItem() == Items.ELYTRA) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(ELYTRA_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0f, 0.0f, 0.125f);
                float scaleFactor = this.getScaleFactor();
                this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityPlayer == null ? entity : entityPlayer);
                this.modelElytra.render(entityPlayer == null ? entity : entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public float getScaleFactor() {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

