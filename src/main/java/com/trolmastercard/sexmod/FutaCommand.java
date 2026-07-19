/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.IClientCommand
 */
package com.trolmastercard.sexmod;

import java.io.*;
import java.util.Random;

import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

public class FutaCommand extends CommandBase implements IClientCommand {
    final static String d = "sexmod/futa";
    final static int a = 10;
    final static float c = 0.025f;
    static public boolean enabled = true;
    final static public FutaCommand b = new FutaCommand();


    public FutaCommand() {
        String string = "";
        try {
            new BufferedReader(new FileReader(d)).readLine().toLowerCase();
        } catch (Exception e) {
            //
        }
        if (string.isEmpty()) {
            return;
        }
        if ("true".equals(string)) {
            enabled = true;
        }
        if ("false".equals(string)) {
            enabled = false;
        }
    }

    @Override
    public String getName() {
        return "futa";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/futa <true|false>";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 1) {
            this.FutaYesNoErrorMessage(iCommandSender);
            return;
        }
        String string = stringArray[0].toLowerCase();
        if ("true".equals(string)) {
            enabled = true;
        } else if ("false".equals(string)) {
            enabled = false;
        } else {
            this.FutaYesNoErrorMessage(iCommandSender);
            return;
        }
        try {
            FileWriter fileWriter = new FileWriter(d);
            fileWriter.write(string);
            fileWriter.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        try {
            for (GirlEntity girlEntity : GirlEntity.GirlEntityList()) {
                if (girlEntity.isDead || !girlEntity.world.isRemote || !(girlEntity instanceof GalathEntity)) continue;
                Vec3d vec3d = girlEntity.b("cockParticles").add(girlEntity.getPositionVector());
                Random random = girlEntity.getRNG();
                for (int i = 0; i < 10; ++i) {
                    girlEntity.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, vec3d.x, vec3d.y, vec3d.z, random.nextFloat() * 0.025f * (float) Utils.getRandomSign(), random.nextFloat() * 0.025f * (float) Utils.getRandomSign(), random.nextFloat() * 0.025f * (float) Utils.getRandomSign(), new int[0]);
                }
            }
        } catch (Exception e) {
            System.out.println("wtf he thinks sexmod/futa is a file??");
            e.printStackTrace();
        }

    }

    void FutaYesNoErrorMessage(ICommandSender iCommandSender) {
        iCommandSender.sendMessage(new TextComponentString(String.format("%sYou can either do %s/futa true %sor %s/futa false", new Object[]{TextFormatting.YELLOW, TextFormatting.GRAY, TextFormatting.YELLOW, TextFormatting.GRAY})));
    }

    public boolean allowUsageWithoutPrefix(ICommandSender iCommandSender, String string) {
        return false;
    }
}

