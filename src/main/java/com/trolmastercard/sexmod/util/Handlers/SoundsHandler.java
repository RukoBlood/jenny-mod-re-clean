/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.registry.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.util.Handlers;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
//
public class SoundsHandler {
    final static public SoundEvent[] MISC_PLOB = new SoundEvent[1];
    final static public SoundEvent[] MISC_BELLJINGLE = new SoundEvent[1];
    final static public SoundEvent[] MISC_BEDRUSTLE = new SoundEvent[2];
    final static public SoundEvent[] MISC_SLAP = new SoundEvent[2];
    final static public SoundEvent[] MISC_TOUCH = new SoundEvent[2];
    final static public SoundEvent[] MISC_POUNDING = new SoundEvent[35];
    final static public SoundEvent[] MISC_SMALLINSERTS = new SoundEvent[5];
    final static public SoundEvent[] MISC_INSERTS = new SoundEvent[5];
    final static public SoundEvent[] MISC_CUMINFLATION = new SoundEvent[1];
    final static public SoundEvent[] MISC_SCREAM = new SoundEvent[2];
    final static public SoundEvent[] MISC_FART = new SoundEvent[3];
    final static public SoundEvent[] MISC_JUMP = new SoundEvent[1];
    final static public SoundEvent[] MISC_EAT = new SoundEvent[3];
    final static public SoundEvent[] MISC_SLIDE = new SoundEvent[7];
    final static public SoundEvent[] GIRLS_JENNY_AFTERSESSIONMOAN = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_JENNY_AHH = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_JENNY_BJMOAN = new SoundEvent[13];
    final static public SoundEvent[] GIRLS_JENNY_GIGGLE = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_JENNY_HAPPYOH = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_JENNY_HEAVYBREATHING = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_JENNY_HMPH = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_JENNY_HUH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_JENNY_LIGHTBREATHING = new SoundEvent[12];
    final static public SoundEvent[] GIRLS_JENNY_LIPSOUND = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_JENNY_MMM = new SoundEvent[9];
    final static public SoundEvent[] GIRLS_JENNY_MOAN = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_JENNY_SADOH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_JENNY_SIGH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ELLIE_AFTERSESSIONMOAN = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_ELLIE_AHH = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_ELLIE_BJMOAN = new SoundEvent[13];
    final static public SoundEvent[] GIRLS_ELLIE_GIGGLE = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_ELLIE_HAPPYOH = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_ELLIE_HEAVYBREATHING = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_ELLIE_HMPH = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_ELLIE_HUH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ELLIE_LIGHTBREATHING = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_ELLIE_LIPSOUND = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_ELLIE_MMM = new SoundEvent[9];
    final static public SoundEvent[] GIRLS_ELLIE_MOAN = new SoundEvent[9];
    final static public SoundEvent[] GIRLS_ELLIE_SADOH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ELLIE_SIGH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ELLIE_COMETOMOMMY = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ELLIE_GOODBOY = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ELLIE_MOMMYHORNY = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_BIA_AHH = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_BIA_BJMOAN = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_BIA_BREATH = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_BIA_GIGGLE = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_BIA_HEY = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_BIA_HUH = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_BIA_MMM = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_LUNA_AHH = new SoundEvent[18];
    final static public SoundEvent[] GIRLS_LUNA_CUTENYA = new SoundEvent[12];
    final static public SoundEvent[] GIRLS_LUNA_HAPPYOH = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_LUNA_HMPH = new SoundEvent[6];
    final static public SoundEvent[] GIRLS_LUNA_HORNINYA = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_LUNA_HUH = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_LUNA_LIGHTBREATHING = new SoundEvent[25];
    final static public SoundEvent[] GIRLS_LUNA_MMM = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_LUNA_MOAN = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_LUNA_SADOH = new SoundEvent[7];
    final static public SoundEvent[] GIRLS_LUNA_SIGH = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_LUNA_SINGING = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_LUNA_GIGGLE = new SoundEvent[15];
    final static public SoundEvent[] GIRLS_LUNA_OUU = new SoundEvent[13];
    final static public SoundEvent[] GIRLS_LUNA_OWO = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_ALLIE_AFTERSESSIONMOAN = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_ALLIE_AHH = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_ALLIE_BJMOAN = new SoundEvent[14];
    final static public SoundEvent[] GIRLS_ALLIE_GIGGLE = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_ALLIE_HAPPYOH = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_ALLIE_HEAVYBREATHING = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_ALLIE_HMPH = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_ALLIE_HUH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ALLIE_LIGHTBREATHING = new SoundEvent[11];
    final static public SoundEvent[] GIRLS_ALLIE_LIPSOUND = new SoundEvent[14];
    final static public SoundEvent[] GIRLS_ALLIE_MMM = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_ALLIE_MOAN = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_ALLIE_SADOH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ALLIE_SIGH = new SoundEvent[2];
    final static public SoundEvent[] GIRLS_ALLIE_SCAWY = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_KOBOLD_BJMOAN = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_KOBOLD_GIGGLE = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_KOBOLD_HAA = new SoundEvent[7];
    final static public SoundEvent[] GIRLS_KOBOLD_HEYMASTER = new SoundEvent[6];
    final static public SoundEvent[] GIRLS_KOBOLD_INTERESTED = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_KOBOLD_LIGHTBREATHING = new SoundEvent[12];
    final static public SoundEvent[] GIRLS_KOBOLD_MASTER = new SoundEvent[6];
    final static public SoundEvent[] GIRLS_KOBOLD_MOAN = new SoundEvent[10];
    final static public SoundEvent[] GIRLS_KOBOLD_ORGASM = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_KOBOLD_SAD = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_KOBOLD_YEP = new SoundEvent[7];
    final static public SoundEvent[] MISC_FLAP = new SoundEvent[4];
    final static public SoundEvent[] MISC_SHATTER = new SoundEvent[1];
    final static public SoundEvent[] MISC_WEOWEO = new SoundEvent[4];
    final static public SoundEvent[] MISC_BEEW = new SoundEvent[3];
    final static public SoundEvent[] MISC_CLAP = new SoundEvent[1];
    final static public SoundEvent[] GIRLS_GALATH_AHH = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_GALATH_BREATHING = new SoundEvent[7];
    final static public SoundEvent[] GIRLS_GALATH_DIALOG = new SoundEvent[6];
    final static public SoundEvent[] GIRLS_GALATH_GIGGLE = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_GALATH_HMPH = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_GALATH_HUH = new SoundEvent[3];
    final static public SoundEvent[] GIRLS_GALATH_LIGHTCHARGE = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_GALATH_MOAN = new SoundEvent[8];
    final static public SoundEvent[] GIRLS_GALATH_STRONGCHARGE = new SoundEvent[4];
    final static public SoundEvent[] GIRLS_GALATH_UUH = new SoundEvent[7];
    final static public SoundEvent[] GIRLS_GALATH_ORGASM = new SoundEvent[5];
    final static public SoundEvent[] GIRLS_GALATH_AAA = new SoundEvent[2];
    final static public SoundEvent[] MISC_PYRO = new SoundEvent[1];
    static HashMap<SoundEvent, Integer> lastRandomSound = new HashMap();

    public static void RegisterSounds() {
        for (Field field : SoundsHandler.class.getDeclaredFields()) {
            SoundEvent[] soundEventArray;
            Class<?> clazz = field.getType();
            if (!clazz.isArray() || clazz.getComponentType() != SoundEvent.class) continue;
            try {
                soundEventArray = (SoundEvent[])field.get(null);
            } catch (Exception exception) {
                Main.LOGGER.error("Error registering sound: {}", exception.getMessage());
                continue;
            }
            String string = field.getName().toLowerCase().replace("_", ".");
            String[] stringArray = string.split("\\.");
            String string2 = stringArray.length > 2 ? stringArray[2] : stringArray[1];
            for (int i = 0; i < soundEventArray.length; ++i) {
                soundEventArray[i] = SoundsHandler.registerSound(String.format("%s.%s%s", string, string2, i));
            }
        }
    }

    public static SoundEvent registerSound(String path) {
        ResourceLocation resourceLocation = new ResourceLocation("sexmod", path);
        SoundEvent soundEvent = new SoundEvent(resourceLocation);
        soundEvent.setRegistryName(path);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);
        return soundEvent;
    }

    public static SoundEvent random(SoundEvent[] soundArray) {
        int random;
        lastRandomSound.putIfAbsent(soundArray[0], -69);
        int trys = 0;
        do {
            random = ReferenceAndRotationHelper.RANDOM.nextInt(soundArray.length);
        } while (++trys < 10 && random == lastRandomSound.get(soundArray[0]));
        lastRandomSound.replace(soundArray[0], random);
        return soundArray[random];
    }
}

