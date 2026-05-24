/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.registry.EntityRegistry
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
import com.trolmastercard.sexmod.girls.Bee.BeeEntity;
import com.trolmastercard.sexmod.girls.Bia.BiaEntity;
import com.trolmastercard.sexmod.girls.Ellie.EllieEntity;
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
//bi
public class EntityInitialize {
    public static void Register() {
        EntityInitialize.RegisterNPCEntity("jenny", JennyEntity.class, PlayerGirlEntity.JENNY.npcID, 3286592, 12655237);
        EntityInitialize.RegisterNPCEntity("ellie", EllieEntity.class, PlayerGirlEntity.ELLIE.npcID, 0x161616, 0x980000);
        EntityInitialize.RegisterNPCEntity("slime", SlimeEntity.class, PlayerGirlEntity.SLIME.npcID, 13167780, 8244330);
        EntityInitialize.RegisterNPCEntity("bia", BiaEntity.class, PlayerGirlEntity.BIA.npcID, 7488816, 7254603);
        EntityInitialize.RegisterNPCEntity("bee", BeeEntity.class, PlayerGirlEntity.BEE.npcID, 16701032, 4400155);
        EntityInitialize.RegisterNPCEntity("luna", LunaEntity.class, PlayerGirlEntity.LUNA.npcID, 7881787, 7940422);
        EntityInitialize.RegisterOtherEntity("allie", AllieEntity.class, PlayerGirlEntity.ALLIE.npcID);
        EntityInitialize.RegisterOtherEntity("kobold", KoboldEntity.class, PlayerGirlEntity.KOBOLD.npcID);
        EntityInitialize.RegisterOtherEntity("kobold_egg", KoboldEggEntity.class, 4674237);
        EntityInitialize.RegisterNPCEntity("goblin", GoblinEntity.class, PlayerGirlEntity.GOBLIN.npcID, 39424, 19456);
        EntityInitialize.RegisterNPCEntity("galath", GalathEntity.class, PlayerGirlEntity.GALATH.npcID, 0xFF0000, 0xFF0000);
        EntityInitialize.RegisterNPCEntity("manglelie", ManglelieEntity.class, PlayerGirlEntity.MANGLELIE.npcID, 0xF9F9F9, 8485574);
        EntityInitialize.RegisterOtherEntity("custom_model", CustomModelEntity.class, 6281823);
        EntityInitialize.RegisterPlayerEntity("player_jenny", PlayerJenny.class, PlayerGirlEntity.JENNY.playerID);
        EntityInitialize.RegisterPlayerEntity("player_ellie", PlayerEllie.class, PlayerGirlEntity.ELLIE.playerID);
        EntityInitialize.RegisterPlayerEntity("player_slime", PlayerSlime.class, PlayerGirlEntity.SLIME.playerID);
        EntityInitialize.RegisterPlayerEntity("player_bia", PlayerBia.class, PlayerGirlEntity.BIA.playerID);
        EntityInitialize.RegisterPlayerEntity("player_bee", PlayerBee.class, PlayerGirlEntity.BEE.playerID);
        EntityInitialize.RegisterPlayerEntity("player_allie", PlayerAllie.class, PlayerGirlEntity.ALLIE.playerID);
        EntityInitialize.RegisterPlayerEntity("player_kobold", PlayerKobold.class, PlayerGirlEntity.KOBOLD.playerID);
        EntityInitialize.RegisterPlayerEntity("player_goblin", PlayerGoblin.class, PlayerGirlEntity.GOBLIN.playerID);
        EntityInitialize.RegisterPlayerEntity("player_luna", PlayerLuna.class, PlayerGirlEntity.LUNA.playerID);
        EntityInitialize.RegisterPlayerEntity("player_galath", PlayerGalath.class, PlayerGirlEntity.GALATH.playerID);
        EntityInitialize.RegisterOtherEntity("friendly_slime", FriendlySlimeEntity.class, 5548484);
        EntityInitialize.RegisterOtherEntity("luna_hook", LunaHookEntity.class, 4768742);
        EntityInitialize.RegisterOtherEntity("energy_ball", EnergyBallEntity.class, 2565153);
        EntityInitialize.RegisterOtherEntity("pyrocinical", EntityPyrocynical.class, 515153);
        EntityRegistry.addSpawn(SlimeEntity.class, 10, 1, 1, (EnumCreatureType)EnumCreatureType.CREATURE, (Biome[])new Biome[]{Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND});
        EntityRegistry.addSpawn(BeeEntity.class, 5, 1, 1, (EnumCreatureType)EnumCreatureType.CREATURE, (Biome[])new Biome[]{Biomes.FOREST, Biomes.FOREST_HILLS});
        EntityRegistry.addSpawn(EntityPyrocynical.class, 3, 1, 1, (EnumCreatureType)EnumCreatureType.AMBIENT, (Biome[])new Biome[]{Biomes.HELL});
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

