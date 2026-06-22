/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.client.registry.RenderingRegistry
 */
package com.trolmastercard.sexmod.util.Handlers;

import com.trolmastercard.sexmod.CrossModel;
import com.trolmastercard.sexmod.girls.Allie.*;
import com.trolmastercard.sexmod.girls.Bee.*;
import com.trolmastercard.sexmod.girls.Bia.*;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.Custom.CustomModelRenderer;
import com.trolmastercard.sexmod.girls.Ellie.*;
import com.trolmastercard.sexmod.girls.Galath.*;
import com.trolmastercard.sexmod.girls.Goblin.*;
import com.trolmastercard.sexmod.girls.Jenny.*;
import com.trolmastercard.sexmod.girls.Kobold.*;
import com.trolmastercard.sexmod.girls.Luna.*;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieModel;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieRenderer;
import com.trolmastercard.sexmod.girls.Pyrocynical.PyrocynicalEntity;
import com.trolmastercard.sexmod.girls.Pyrocynical.PyrocynicalRenderer;
import com.trolmastercard.sexmod.girls.Slime.*;
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
        RenderingRegistry.registerEntityRenderingHandler(LunaEntity.class, renderManager -> new LunaRenderer(renderManager, new LunaModel(), -0.4));
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
        RenderingRegistry.registerEntityRenderingHandler(PlayerLuna.class, renderManager -> new PlayerLunaRenderer(renderManager, (AnimatedGeoModel)new LunaModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerKobold.class, renderManager -> new PlayerKoboldRenderer(renderManager, (AnimatedGeoModel)new KoboldModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerGoblin.class, renderManager -> new PlayerGoblinRenderer(renderManager, (AnimatedGeoModel)new GoblinModel()));
        RenderingRegistry.registerEntityRenderingHandler(PlayerGalath.class, renderManager -> new PlayerGalathRenderer(renderManager, (AnimatedGeoModel)new GalathModel()));
        RenderingRegistry.registerEntityRenderingHandler(LunaHookEntity.class, LunaHookRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CustomModelEntity.class, renderManager -> new CustomModelRenderer(renderManager, new CrossModel()));
        RenderingRegistry.registerEntityRenderingHandler(EnergyBallEntity.class, EnergyBallRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PyrocynicalEntity.class, PyrocynicalRenderer::new);
    }
}

