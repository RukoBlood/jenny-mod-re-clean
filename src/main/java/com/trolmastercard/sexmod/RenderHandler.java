/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.client.registry.RenderingRegistry
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
import com.trolmastercard.sexmod.girls.Allie.AllieModel;
import com.trolmastercard.sexmod.girls.Allie.AllieRenderer;
import com.trolmastercard.sexmod.girls.Bee.BeeEntity;
import com.trolmastercard.sexmod.girls.Bee.BeeModel;
import com.trolmastercard.sexmod.girls.Bee.BeeRenderer;
import com.trolmastercard.sexmod.girls.Bia.BiaEntity;
import com.trolmastercard.sexmod.girls.Bia.BiaModel;
import com.trolmastercard.sexmod.girls.Bia.BiaRenderer;
import com.trolmastercard.sexmod.girls.Ellie.EllieEntity;
import com.trolmastercard.sexmod.girls.Ellie.EllieModel;
import com.trolmastercard.sexmod.girls.Ellie.EllieRenderer;
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import com.trolmastercard.sexmod.girls.Jenny.JennyModel;
import com.trolmastercard.sexmod.girls.Jenny.JennyRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import software.bernie.geckolib3.model.AnimatedGeoModel;
//fk
public class RenderHandler {
    public static void Register() {
        RenderingRegistry.registerEntityRenderingHandler(KoboldEntity.class, renderManager -> new KoboldRenderer(renderManager, (AnimatedGeoModel)new KoboldModel(), -0.4));
        RenderingRegistry.registerEntityRenderingHandler(JennyEntity.class, renderManager -> new JennyRenderer(renderManager, new JennyModel(), -0.15));
        RenderingRegistry.registerEntityRenderingHandler(EllieEntity.class, renderManager -> new EllieRenderer(renderManager, new EllieModel(), 0.05));
        RenderingRegistry.registerEntityRenderingHandler(SlimeEntity.class, renderManager -> new SlimeRenderer(renderManager, (AnimatedGeoModel)new SlimeModel(), -0.2));
        RenderingRegistry.registerEntityRenderingHandler(BiaEntity.class, renderManager -> new BiaRenderer(renderManager, (AnimatedGeoModel)new BiaModel(), -0.4));
        RenderingRegistry.registerEntityRenderingHandler(AllieEntity.class, renderManager -> new AllieRenderer(renderManager, (AnimatedGeoModel)new AllieModel(), -0.4));
        RenderingRegistry.registerEntityRenderingHandler(BeeEntity.class, renderManager -> new BeeRenderer(renderManager, new BeeModel(), -0.4));
        RenderingRegistry.registerEntityRenderingHandler(FriendlySlimeEntity.class, FriendlySlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(LunaEntity.class, renderManager -> new LunaRenderer(renderManager, new CatModel(), -0.4));
        RenderingRegistry.registerEntityRenderingHandler(GoblinEntity.class, renderManager -> new GoblinRenderer(renderManager, (AnimatedGeoModel)new GoblinModel(), -0.6));
        RenderingRegistry.registerEntityRenderingHandler(GalathEntity.class, renderManager -> new GalathRenderer(renderManager, (AnimatedGeoModel)new GalathModel(), -0.05));
        RenderingRegistry.registerEntityRenderingHandler(KoboldEggEntity.class, renderManager -> new KoboldEggRenderer(renderManager, new KoboldEggModel2()));
        RenderingRegistry.registerEntityRenderingHandler(ManglelieEntity.class, renderManager -> new ManglelieRenderer(renderManager, (AnimatedGeoModel)new ManglelieModel(), -0.05));
        RenderingRegistry.registerEntityRenderingHandler(PlayerBia.class, renderManager -> new PlayerBiaRenderer(renderManager, (AnimatedGeoModel)new BiaModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerJenny.class, renderManager -> new PlayerJennyRenderer(renderManager, (AnimatedGeoModel)new JennyModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerEllie.class, renderManager -> new PlayerEllieRenderer(renderManager, (AnimatedGeoModel)new EllieModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerSlime.class, renderManager -> new PlayerSlimeRenderer(renderManager, (AnimatedGeoModel)new SlimeModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerAllie.class, renderManager -> new PlayerAllieRenderer(renderManager, (AnimatedGeoModel)new AllieModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerBee.class, renderManager -> new PlayerBeeRenderer(renderManager, (AnimatedGeoModel)new BeeModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerLuna.class, renderManager -> new PlayerLunaRenderer(renderManager, (AnimatedGeoModel)new CatModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerKobold.class, renderManager -> new PlayerKoboldRenderer(renderManager, (AnimatedGeoModel)new KoboldModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerGoblin.class, renderManager -> new PlayerGoblinRenderer(renderManager, (AnimatedGeoModel)new GoblinModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerGalath.class, renderManager -> new PlayerGalathRenderer(renderManager, (AnimatedGeoModel)new GalathModel()));
        RenderingRegistry.registerEntityRenderingHandler(LunaHookEntity.class, LunaHookEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CustomModelEntity.class, renderManager -> new CustomModelRenderer(renderManager, new CrossModel()));
        RenderingRegistry.registerEntityRenderingHandler(EnergyBallEntity.class, EnergyBallRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPyrocynical.class, EntityPyrocynicalRenderer::new);
    }
}

