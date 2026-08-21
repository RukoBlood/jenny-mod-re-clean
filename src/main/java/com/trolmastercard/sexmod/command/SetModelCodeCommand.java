/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.IClientCommand
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.command;

import com.trolmastercard.sexmod.Packets.UploadModelString;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.ThreadNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SetModelCodeCommand extends CommandBase implements IClientCommand {
    final static public SetModelCodeCommand SET_MODEL_CODE_COMMAND = new SetModelCodeCommand();

    public boolean allowUsageWithoutPrefix(ICommandSender iCommandSender, String string) {
        return false;
    }

    @Override
    public String getName() {
        return "setmodelcode";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/setmodelcode";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GirlEntity girl;
        //String[] stringArray2;
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP entityPlayerSP = minecraft.player;
        String string = "";
        String string2 = "";
        if (args.length > 0) {
            String[] stringArray2 = args[0].split("\\$");
            string = stringArray2[0];
            if (stringArray2.length > 1) {
                string2 = stringArray2[1];
            }
        }
        {
            RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
            if ((girl = this.checkEntity(result)) == null) {
                entityPlayerSP.sendStatusMessage(new TextComponentString("You gotta transform into the girl you want to apply the model-code to"), true);
                return;
            }
        }
        if (string2.isEmpty()) {
            PackageHandler.INSTANCE.sendToServer(new UploadModelString(string, girl.girlID()));
            entityPlayerSP.sendStatusMessage(new TextComponentString(this.showModelCode(girl)), true);
            return;
        }
        PackageHandler.INSTANCE.sendToServer(new UploadModelString(string, girl.girlID(), GirlEntity.decodePartIdList(string2)));
        entityPlayerSP.sendStatusMessage(new TextComponentString(this.showModelCode(girl)), true);
    }

    String showModelCode(GirlEntity girl) {
        if (girl instanceof PlayerGirl) {
            return TextFormatting.YELLOW + "applied model code to your player-" + ThreadNames.CapitalizeString(PlayerGirlEntity.getGirlType(girl).toString());
        }
        return TextFormatting.YELLOW + "applied model code to this " + girl.getGirlName();
    }

    @SideOnly(value=Side.CLIENT)
    GirlEntity checkEntity(RayTraceResult result) {
        if (result == null) {
            return PlayerGirl.GetPlayer(Minecraft.getMinecraft().player);
        }
        if (GirlEntity.isValidGirl(result.entityHit)) {
            return (GirlEntity)result.entityHit;
        }
        return PlayerGirl.GetPlayer(Minecraft.getMinecraft().player);
    }
}

