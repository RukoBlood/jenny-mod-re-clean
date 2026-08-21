/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.command;

import java.util.ConcurrentModificationException;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Goblin.GoblinEntity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class LocateGoblinLairCommand extends CommandBase {
    final static public LocateGoblinLairCommand LOCATE_GOBLIN_LAIR_COMMAND = new LocateGoblinLairCommand();

    @Override
    public String getName() {
        return "locatenearestgoblinlair";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/locatenearestgoblinlair";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        Entity entity = iCommandSender.getCommandSenderEntity();
        if (entity != null && entity.dimension != 0) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "goblin lairs don't exist in the " + (entity.dimension == -1 ? TextFormatting.RED + "Nether" : TextFormatting.DARK_PURPLE + "End")));
            return;
        }
        Entity entity2 = null;
        for (GirlEntity em_class2582 : GirlEntity.getGirlEntityList()) {
            if (!(em_class2582 instanceof GoblinEntity)) continue;
            GoblinEntity e3_class2192 = (GoblinEntity) em_class2582;
            if (!e3_class2192.isQueen) continue;
            if (entity2 == null) {
                entity2 = e3_class2192;
                continue;
            }
            if (!(e3_class2192.getDistanceSq(iCommandSender.getPosition()) < entity2.getDistanceSq(iCommandSender.getPosition())))
                continue;
            entity2 = e3_class2192;
        }
        if (entity2 == null) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "No nearby goblin lair found uwu"));
            return;
        }
        BlockPos blockPos = entity2.getPosition();
        iCommandSender.sendMessage(new TextComponentString(String.format("%sgoblin lair found at %s%s %s%s %s%s", TextFormatting.YELLOW, TextFormatting.RED, blockPos.getX(), TextFormatting.GREEN, blockPos.getY(), TextFormatting.BLUE, blockPos.getZ())));
    }
}

