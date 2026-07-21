/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.UUID;

import com.trolmastercard.sexmod.girls.PlayerGirl;
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

public class bu_class100 extends GeoLayerRenderer {
    final static private ResourceLocation b = new ResourceLocation("textures/entity/elytra.png");
    final private ModelElytra a = new ModelElytra();

    public bu_class100(IGeoRenderer iGeoRenderer) {
        super(iGeoRenderer);
    }

    public void render(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        UUID uUID;
        if (!(entityLivingBase instanceof Fighter)) {
            return;
        }
        Fighter e2_class2182 = (Fighter)entityLivingBase;
        ItemStack itemStack = e2_class2182.getDataManager().get(Fighter.CHEST_SLOT);
        EntityPlayer entityPlayer = null;
        if (e2_class2182 instanceof PlayerGirl && (uUID = ((PlayerGirl)e2_class2182).getOwnerUserUUID()) != null) {
            entityPlayer = entityLivingBase.world.getPlayerEntityByUUID(uUID);
        }
        if (itemStack.getItem() != Items.ELYTRA) {
            return;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(b);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.125f);
        float f7 = this.a();
        this.a.setRotationAngles(f, f2, f4, f5, f6, f7, entityPlayer == null ? entityLivingBase : entityPlayer);
        this.a.render(entityPlayer == null ? entityLivingBase : entityPlayer, f, f2, f4, f5, f6, f7);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public float a() {
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

