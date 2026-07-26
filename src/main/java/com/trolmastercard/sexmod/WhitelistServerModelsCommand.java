/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.IClientCommand
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

public class WhitelistServerModelsCommand
extends CommandBase
implements IClientCommand {
    final static public WhitelistServerModelsCommand a = new WhitelistServerModelsCommand();

    @Override
    public String getName() {
        return "whitelistserver";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/whitelistserver";
    }

    public boolean allowUsageWithoutPrefix(ICommandSender iCommandSender, String string) {
        return false;
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        boolean bl;
        String string = CustomModel.getGlobalModelOverride();
        if (string == null) {
            iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.YELLOW) + "This is a multiplayer feature only"));
            return;
        }
        if (CustomModel.l(string)) {
            iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GREEN) + "Server is already whitelisted :)"));
            return;
        }
        boolean bl2 = bl = stringArray.length > 0 && "confirm".equals(stringArray[0]);
        if (!bl) {
            iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.YELLOW) + "By whitelisting this server, you allow the server to send you the custom models that are used on it"));
            iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.RED) + "ONLY WHITELIST SERVERS, WHOSE SERVER OWNER YOU KNOW AND TRUST"));
            iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.YELLOW) + "to confirm your decision type:"));
            iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GREEN) + "/whitelistserver confirm"));
            return;
        }
        CustomModel.h(string);
        iCommandSender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GREEN) + "confirmed :)"));
        CustomModel.a();
    }

    private static CommandException a(CommandException commandException) {
        return commandException;
    }
}

