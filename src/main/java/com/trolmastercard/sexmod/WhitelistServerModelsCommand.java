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

public class WhitelistServerModelsCommand extends CommandBase implements IClientCommand {
    final static public WhitelistServerModelsCommand WHITELIST_SERVER_MODELS_COMMAND = new WhitelistServerModelsCommand();

    @Override
    public String getName() {
        return "whitelistserver";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/whitelistserver";
    }

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        boolean argument;
        String override = CustomModel.getGlobalModelOverride();

        if (override == null) {
            sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.YELLOW) + "This is a multiplayer feature only"));
            return;
        }

        if (CustomModel.l(override)) {
            sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GREEN) + "Server is already whitelisted :)"));
            return;
        }
        argument = args.length > 0 && "confirm".equals(args[0]);
        if (!argument) {
            sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.YELLOW) + "By whitelisting this server, you allow the server to send you the custom models that are used on it"));
            sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.RED) + "ONLY WHITELIST SERVERS, WHOSE SERVER OWNER YOU KNOW AND TRUST"));
            sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.YELLOW) + "to confirm your decision type:"));
            sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GREEN) + "/whitelistserver confirm"));
            return;
        }
        CustomModel.h(override);
        sender.sendMessage(new TextComponentString((Object)((Object)TextFormatting.GREEN) + "confirmed :)"));
        CustomModel.a();
    }
}

