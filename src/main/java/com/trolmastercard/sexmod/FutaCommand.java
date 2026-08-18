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
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.util.ThreadNames;
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
    final static String CONFIG_PATH = "sexmod/futa";
    final static int a = 10;
    final static float c = 0.025f;
    static public boolean enabled = true;
    final static public FutaCommand FUTA_COMMAND = new FutaCommand();


    public FutaCommand() {
        String line = "";
        try {
            new BufferedReader(new FileReader(CONFIG_PATH)).readLine().toLowerCase();
        } catch (Exception e) {
            System.out.println("FutaCommand.class: Error reading config.");
            e.printStackTrace();
        }
        if (line.isEmpty()) {
            return;
        }
        if ("true".equals(line)) {
            enabled = true;
        }
        if ("false".equals(line)) {
            enabled = false;
        }
    }

    @Override
    public String getName() {
        return "futa";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/futa <true|false>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            this.FutaYesNoErrorMessage(sender);
            return;
        }
        String arg = args[0].toLowerCase();
        if ("true".equals(arg)) {
            enabled = true;
        } else if ("false".equals(arg)) {
            enabled = false;
        } else {
            this.FutaYesNoErrorMessage(sender);
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(CONFIG_PATH);
            fileWriter.write(arg);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("FutaCommand.class: Error writing to file.");
            e.printStackTrace();
        }

        try {
            for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                if (girl.isDead || !girl.world.isRemote || !(girl instanceof GalathEntity)) continue;

                Vec3d pos = girl.getCachedBoneOffset("cockParticles").add(girl.getPositionVector());
                Random random = girl.getRNG();
                for (int i = 0; i < 10; ++i) {
                    girl.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, random.nextFloat() * 0.025f * (float) ThreadNames.getRandomSign(), random.nextFloat() * 0.025f * (float) ThreadNames.getRandomSign(), random.nextFloat() * 0.025f * (float) ThreadNames.getRandomSign(), new int[0]);
                }
            }
        } catch (Exception e) {
            System.out.println("No galath nearby, o algo");
            e.printStackTrace();
        }

    }

    void FutaYesNoErrorMessage(ICommandSender sender) {
        sender.sendMessage(new TextComponentString(String.format("%sYou can either do %s/futa true %sor %s/futa false", new Object[]{TextFormatting.YELLOW, TextFormatting.GRAY, TextFormatting.YELLOW, TextFormatting.GRAY})));
    }

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }
}

