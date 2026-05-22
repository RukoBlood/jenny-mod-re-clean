/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ReloadCustomModelsCommand
extends CommandBase {
    final static public ReloadCustomModelsCommand RELOAD_CUSTOM_MODELS_COMMAND = new ReloadCustomModelsCommand();

    @Override
    public String getName() {
        return "reloadcustommodels";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/reloadcustommodels";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        CustomModels.b(false);
        for (EntityPlayerMP entityPlayerMP : minecraftServer.getPlayerList().getPlayers()) {
            minecraftServer.addScheduledTask(() -> NetworkRegistry.networkWrapper.sendTo((IMessage)new g6_class350(CustomModels.e()), entityPlayerMP));
        }
    }
}

