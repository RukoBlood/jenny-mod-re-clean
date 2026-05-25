/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls;

import com.trolmastercard.sexmod.girls.Allie.AllieEntity;
import com.trolmastercard.sexmod.girls.Allie.PlayerAllie;
import com.trolmastercard.sexmod.girls.Bee.BeeEntity;
import com.trolmastercard.sexmod.girls.Bee.PlayerBee;
import com.trolmastercard.sexmod.girls.Bia.BiaEntity;
import com.trolmastercard.sexmod.girls.Bia.PlayerBia;
import com.trolmastercard.sexmod.girls.Ellie.EllieEntity;
import com.trolmastercard.sexmod.girls.Ellie.PlayerEllie;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.PlayerGalath;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import com.trolmastercard.sexmod.girls.Goblin.PlayerGoblin;
import com.trolmastercard.sexmod.girls.Jenny.JennyEntity;
import com.trolmastercard.sexmod.girls.Jenny.PlayerJenny;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.PlayerKobold;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.girls.Luna.PlayerLuna;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieEntity;
import com.trolmastercard.sexmod.girls.Slime.PlayerSlime;
import com.trolmastercard.sexmod.girls.Slime.SlimeEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.entity.Entity;

//fy
public enum PlayerGirlEntity {
    ALLIE(AllieEntity.class, 5614613, PlayerAllie.class, 64867483),
    BEE(BeeEntity.class, 4663354, PlayerBee.class, 48648638),
    BIA(BiaEntity.class, 230053, PlayerBia.class, 65456415),
    ELLIE(EllieEntity.class, 228922, PlayerEllie.class, 46348348),
    GALATH(GalathEntity.class, 314351, PlayerGalath.class, 652535516),
    GOBLIN(GoblinEntity.class, 4567275, PlayerGoblin.class, 6584344, true),
    JENNY(JennyEntity.class, 177013, PlayerJenny.class, 12388645),
    KOBOLD(KoboldEntity.class, 5648456, PlayerKobold.class, 62484851, true),
    LUNA(LunaEntity.class, 6816463, PlayerLuna.class, 81234824),
    MANGLELIE(ManglelieEntity.class, 618151),
    SLIME(SlimeEntity.class, 168597, PlayerSlime.class, 54816432);

    final public int npcID;
    final public int playerID;
    final public Class<? extends GirlEntity> npcClass;
    final public Class<? extends PlayerGirl> playerClass;
    final public boolean isNpcOnly;
    final public int editorID;
    final public boolean hasSpecifics;

    private PlayerGirlEntity(Class<? extends GirlEntity> npcClass, int id, Class<? extends PlayerGirl> playerClass, int playerID, boolean hasSpecifics) {
        this.npcID = id;
        this.playerID = playerID;
        this.npcClass = npcClass;
        this.playerClass = playerClass;
        this.isNpcOnly = false;
        this.hasSpecifics = hasSpecifics;
        this.editorID = Reference.b++;
    }

    private PlayerGirlEntity(Class<? extends GirlEntity> npcClass, int id, Class<? extends PlayerGirl> playerNPCClass, int PlayerID) {
        this.npcID = id;
        this.playerID = PlayerID;
        this.npcClass = npcClass;
        this.playerClass = playerNPCClass;
        this.isNpcOnly = false;
        this.hasSpecifics = false;
        this.editorID = Reference.b++;
    }

    private PlayerGirlEntity(Class<? extends GirlEntity> npcClass, int id) {
        this.npcID = id;
        this.npcClass = npcClass;
        this.isNpcOnly = true;
        this.hasSpecifics = false;
        this.editorID = Reference.b++;
        this.playerClass = null;
        this.playerID = 0;
    }

    public static PlayerGirlEntity a(String string) {
        for (PlayerGirlEntity playerGirlEntity : PlayerGirlEntity.values()) {
            if (!playerGirlEntity.toString().equalsIgnoreCase(string)) continue;
            return playerGirlEntity;
        }
        return JENNY;
    }

    public static PlayerGirlEntity a(Entity entity) {
        if (!(entity instanceof GirlEntity)) {
            return null;
        }
        GirlEntity girlEntity = (GirlEntity) entity;
        Class<?> girlEntityClass = girlEntity.getClass();
        for (PlayerGirlEntity playerGirlEntity : PlayerGirlEntity.values()) {
            if (girlEntityClass.equals(playerGirlEntity.npcClass)) {
                return playerGirlEntity;
            }
            if (!girlEntityClass.equals(playerGirlEntity.playerClass)) continue;
            return playerGirlEntity;
        }
        return null;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

