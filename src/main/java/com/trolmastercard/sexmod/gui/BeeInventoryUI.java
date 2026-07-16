/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui;

import java.io.IOException;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.BeeOpenChest;
import com.trolmastercard.sexmod.Packages.ChangeDataParameter;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SetNewHome;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BeeInventoryUI extends GuiScreen {
    Supporter supporter;
    EntityPlayer player;
    boolean e;
    final static ResourceLocation b = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    double d = 0.0;

    public BeeInventoryUI(Supporter supporter, EntityPlayer entityPlayer) {
        this.supporter = supporter;
        this.player = entityPlayer;
        this.e = !"".equals(supporter.getDataManager().get(GirlEntity.v));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        this.buttonList.clear();
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n3 = scaledResolution.getScaledWidth();
        this.d = Math.min(1.0, this.d + (double)(this.mc.getTickLength() / 5.0f));
        this.buttonList.add(new GuiButton(0, n3 / 2 - 119 + (int)(100.0 - 100.0 * this.d), 30, (int)(this.d * 100.0), 20, this.e ? I18n.format("action.names.stopfollowme", new Object[0]) : I18n.format("action.names.followme", new Object[0])));
        this.buttonList.add(new GuiButton(1, n3 / 2 + 19, 30, (int)(this.d * 100.0), 20, I18n.format("action.names.gohome", new Object[0])));
        this.mc.renderEngine.bindTexture(b);
        this.drawTexturedModalRect(n3 / 2 - 7, 61 - (int)(15.0 - this.d * 15.0), 32, 0, 15, 15);
        this.buttonList.add(new GuiButton(2, n3 / 2 - 10, 59 - (int)(15.0 - this.d * 15.0), 20, 20, ""));
        this.drawTexturedModalRect(n3 / 2 - 20, 20, this.supporter.getDataManager().get(Supporter.HAS_CHEST) ? 0 : 40, 130, 40, 40);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n4 = scaledResolution.getScaledWidth();
        if (this.supporter.getDataManager().get(Supporter.HAS_CHEST).booleanValue() && n >= n4 / 2 - 20 && n <= n4 / 2 + 20 && n2 >= 20 && n2 <= 60) {
            PackageHandler.networkWrapper.sendToServer((IMessage)new BeeOpenChest(this.supporter.girlID(), this.player.getPersistentID()));
            this.onGuiClosed();
        }
        super.mouseClicked(n, n2, n3);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        super.actionPerformed(guiButton);
        if (guiButton.id == 0) {
            if (this.e) {
                PackageHandler.networkWrapper.sendToServer((IMessage)new ChangeDataParameter(this.supporter.girlID(), "master", ""));
                this.player.sendMessage(new TextComponentString(I18n.format("bee.dialogue.sad", new Object[0])));
            } else {
                PackageHandler.networkWrapper.sendToServer((IMessage)new ChangeDataParameter(this.supporter.girlID(), "master", this.player.getPersistentID().toString()));
                this.player.sendMessage(new TextComponentString(I18n.format("bee.dialogue.exited", new Object[0])));
            }
            this.e = !this.e;
            this.player.closeScreen();
        }
        if (guiButton.id == 1) {
            PackageHandler.networkWrapper.sendToServer((IMessage)new SendCompanionHome(this.supporter.girlID()));
            this.player.closeScreen();
        }
        if (guiButton.id == 2) {
            PackageHandler.networkWrapper.sendToServer((IMessage)new SetNewHome(this.supporter.girlID(), new Vec3d(this.supporter.posX, this.supporter.posY, this.supporter.posZ)));
            this.player.closeScreen();
            this.player.sendMessage(new TextComponentString(I18n.format("bee.dialogue.home", new Object[0])));
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

