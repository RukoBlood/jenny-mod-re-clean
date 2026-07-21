/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.IClientCommand
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.Packages.UploadModelString;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Utils;
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

public class SetModelCodeCommand
extends CommandBase
implements IClientCommand {
    final static public SetModelCodeCommand a = new SetModelCodeCommand();

    public boolean allowUsageWithoutPrefix(ICommandSender iCommandSender, String string) {
        return false;
    }

    @Override
    public String getName() {
        return "setmodelcode";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/setmodelcode";
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        GirlEntity em_class2582;
        //String[] stringArray2;
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP entityPlayerSP = minecraft.player;
        String string = "";
        String string2 = "";
        if (stringArray.length > 0) {
            String[] stringArray2 = stringArray[0].split("\\$");
            string = stringArray2[0];
            if (stringArray2.length > 1) {
                string2 = stringArray2[1];
            }
        }
        {
            RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
            if ((em_class2582 = this.a(result)) == null) {
                ((EntityPlayer) entityPlayerSP).sendStatusMessage(new TextComponentString("You gotta transform into the girl you want to apply the model-code to"), true);
                return;
            }
        }
        if ("".equals(string2)) {
            PackageHandler.networkWrapper.sendToServer((IMessage)new UploadModelString(string, em_class2582.girlID()));
            ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString(this.a(em_class2582)), true);
            return;
        }
        PackageHandler.networkWrapper.sendToServer((IMessage)new UploadModelString(string, em_class2582.girlID(), GirlEntity.c(string2)));
        ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString(this.a(em_class2582)), true);
    }

    String a(GirlEntity em_class2582) {
        if (em_class2582 instanceof PlayerGirl) {
            return (Object)((Object)TextFormatting.YELLOW) + "applied model code to your player-" + Utils.CapitalizeString(PlayerGirlEntity.fromGirl(em_class2582).toString());
        }
        return (Object)((Object)TextFormatting.YELLOW) + "applied model code to this " + em_class2582.getGirlName();
    }

    @SideOnly(value=Side.CLIENT)
    GirlEntity a(RayTraceResult rayTraceResult) {
        if (rayTraceResult == null) {
            return PlayerGirl.GetPlayer(Minecraft.getMinecraft().player);
        }
        if (GirlEntity.isValidGirl(rayTraceResult.entityHit)) {
            return (GirlEntity)rayTraceResult.entityHit;
        }
        return PlayerGirl.GetPlayer(Minecraft.getMinecraft().player);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

