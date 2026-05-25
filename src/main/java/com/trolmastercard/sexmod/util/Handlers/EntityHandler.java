/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.registry.EntityRegistry
 */
package com.trolmastercard.sexmod.util.Handlers;

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
import com.trolmastercard.sexmod.girls.Allie.PlayerAllie;
import com.trolmastercard.sexmod.girls.Bee.BeeEntity;
import com.trolmastercard.sexmod.girls.Bee.PlayerBee;
import com.trolmastercard.sexmod.girls.Bia.BiaEntity;
import com.trolmastercard.sexmod.girls.Bia.PlayerBia;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.Ellie.EllieEntity;
import com.trolmastercard.sexmod.girls.Ellie.PlayerEllie;
import com.trolmastercard.sexmod.girls.Galath.EnergyBallEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.PlayerGalath;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.girls.Goblin.PlayerGoblin;
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import com.trolmastercard.sexmod.girls.Jenny.PlayerJenny;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEggEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.PlayerKobold;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaHookEntity;
import com.trolmastercard.sexmod.girls.Luna.PlayerLuna;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.girls.Pyrocynical.PyrocynicalEntity;
import com.trolmastercard.sexmod.girls.Slime.FriendlySlimeEntity;
import com.trolmastercard.sexmod.girls.Slime.PlayerSlime;
import com.trolmastercard.sexmod.girls.Slime.SlimeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
//bi
public class EntityHandler {
    public static void Register() {
        EntityHandler.RegisterNPCEntity("jenny", JennyEntity.class, PlayerGirlEntity.JENNY.npcID, 3286592, 12655237);
        EntityHandler.RegisterNPCEntity("ellie", EllieEntity.class, PlayerGirlEntity.ELLIE.npcID, 0x161616, 0x980000);
        EntityHandler.RegisterNPCEntity("slime", SlimeEntity.class, PlayerGirlEntity.SLIME.npcID, 13167780, 8244330);
        EntityHandler.RegisterNPCEntity("bia", BiaEntity.class, PlayerGirlEntity.BIA.npcID, 7488816, 7254603);
        EntityHandler.RegisterNPCEntity("bee", BeeEntity.class, PlayerGirlEntity.BEE.npcID, 16701032, 4400155);
        EntityHandler.RegisterNPCEntity("luna", LunaEntity.class, PlayerGirlEntity.LUNA.npcID, 7881787, 7940422);
        EntityHandler.RegisterOtherEntity("allie", AllieEntity.class, PlayerGirlEntity.ALLIE.npcID);
        EntityHandler.RegisterOtherEntity("kobold", KoboldEntity.class, PlayerGirlEntity.KOBOLD.npcID);
        EntityHandler.RegisterOtherEntity("kobold_egg", KoboldEggEntity.class, 4674237);
        EntityHandler.RegisterNPCEntity("goblin", GoblinEntity.class, PlayerGirlEntity.GOBLIN.npcID, 39424, 19456);
        EntityHandler.RegisterNPCEntity("galath", GalathEntity.class, PlayerGirlEntity.GALATH.npcID, 0xFF0000, 0xFF0000);
        EntityHandler.RegisterNPCEntity("manglelie", ManglelieEntity.class, PlayerGirlEntity.MANGLELIE.npcID, 0xF9F9F9, 8485574);
        EntityHandler.RegisterOtherEntity("custom_model", CustomModelEntity.class, 6281823);
        EntityHandler.RegisterPlayerEntity("player_jenny", PlayerJenny.class, PlayerGirlEntity.JENNY.playerID);
        EntityHandler.RegisterPlayerEntity("player_ellie", PlayerEllie.class, PlayerGirlEntity.ELLIE.playerID);
        EntityHandler.RegisterPlayerEntity("player_slime", PlayerSlime.class, PlayerGirlEntity.SLIME.playerID);
        EntityHandler.RegisterPlayerEntity("player_bia", PlayerBia.class, PlayerGirlEntity.BIA.playerID);
        EntityHandler.RegisterPlayerEntity("player_bee", PlayerBee.class, PlayerGirlEntity.BEE.playerID);
        EntityHandler.RegisterPlayerEntity("player_allie", PlayerAllie.class, PlayerGirlEntity.ALLIE.playerID);
        EntityHandler.RegisterPlayerEntity("player_kobold", PlayerKobold.class, PlayerGirlEntity.KOBOLD.playerID);
        EntityHandler.RegisterPlayerEntity("player_goblin", PlayerGoblin.class, PlayerGirlEntity.GOBLIN.playerID);
        EntityHandler.RegisterPlayerEntity("player_luna", PlayerLuna.class, PlayerGirlEntity.LUNA.playerID);
        EntityHandler.RegisterPlayerEntity("player_galath", PlayerGalath.class, PlayerGirlEntity.GALATH.playerID);
        EntityHandler.RegisterOtherEntity("friendly_slime", FriendlySlimeEntity.class, 5548484);
        EntityHandler.RegisterOtherEntity("luna_hook", LunaHookEntity.class, 4768742);
        EntityHandler.RegisterOtherEntity("energy_ball", EnergyBallEntity.class, 2565153);
        EntityHandler.RegisterOtherEntity("pyrocinical", PyrocynicalEntity.class, 515153);
        EntityRegistry.addSpawn(SlimeEntity.class, 10, 1, 1, (EnumCreatureType)EnumCreatureType.CREATURE, (Biome[])new Biome[]{Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND});
        EntityRegistry.addSpawn(BeeEntity.class, 5, 1, 1, (EnumCreatureType)EnumCreatureType.CREATURE, (Biome[])new Biome[]{Biomes.FOREST, Biomes.FOREST_HILLS});
        EntityRegistry.addSpawn(PyrocynicalEntity.class, 3, 1, 1, (EnumCreatureType)EnumCreatureType.AMBIENT, (Biome[])new Biome[]{Biomes.HELL});
        EntityRegistry.addSpawn(ManglelieEntity.class, 5, 1, 1, (EnumCreatureType)EnumCreatureType.AMBIENT, (Biome[])new Biome[]{Biomes.HELL});
    }

    private static void RegisterPlayerEntity(String entity, Class<? extends Entity> girlClass, int n) {
        EntityRegistry.registerModEntity((ResourceLocation)new ResourceLocation("sexmod:" + entity), girlClass, (String)entity, (int)n, (Object) Main.instance, 100, 1, (boolean)false);
    }

    private static void RegisterNPCEntity(String entity, Class<? extends Entity> girlClass, int n, int n2, int n3) {
        EntityRegistry.registerModEntity((ResourceLocation)new ResourceLocation("sexmod:" + entity), girlClass, (String)entity, (int)n, (Object) Main.instance, 50, 1, (boolean)true, (int)n2, (int)n3);
    }

    private static void RegisterOtherEntity(String entity, Class<? extends Entity> girlClass, int n) {
        EntityRegistry.registerModEntity((ResourceLocation)new ResourceLocation("sexmod:" + entity), girlClass, (String)entity, (int)n, (Object) Main.instance, 50, 1, (boolean)true);
    }
}

