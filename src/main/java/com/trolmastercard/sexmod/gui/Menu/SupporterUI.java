/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.gui.Menu;

import java.io.IOException;

import com.trolmastercard.sexmod.Packages.BeeOpenChest;
import com.trolmastercard.sexmod.Packages.ChangeDataParameter;
import com.trolmastercard.sexmod.Packages.SendCompanionHome;
import com.trolmastercard.sexmod.Packages.SetNewHome;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.Supporter;
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

public class SupporterUI extends GuiScreen {
    Supporter girl;
    EntityPlayer player;
    boolean isFollowing;
    final static ResourceLocation ITEMS_BACKGROUND = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    double mu = 0.0;

    public SupporterUI(Supporter girl, EntityPlayer player) {
        this.girl = girl;
        this.player = player;
        this.isFollowing = !girl.getDataManager().get(GirlEntity.MASTER).isEmpty();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.buttonList.clear();
        ScaledResolution resolution = new ScaledResolution(this.mc);
        int screenWidth = resolution.getScaledWidth();
        this.mu = Math.min(1.0, this.mu + (double)(this.mc.getTickLength() / 5.0f));
        this.buttonList.add(new GuiButton(0, screenWidth / 2 - 119 + (int)(100.0 - 100.0 * this.mu), 30, (int)(this.mu * 100.0), 20, this.isFollowing ? I18n.format("action.names.stopfollowme", new Object[0]) : I18n.format("action.names.followme", new Object[0])));
        this.buttonList.add(new GuiButton(1, screenWidth / 2 + 19, 30, (int)(this.mu * 100.0), 20, I18n.format("action.names.gohome", new Object[0])));
        this.mc.renderEngine.bindTexture(ITEMS_BACKGROUND);
        this.drawTexturedModalRect(screenWidth / 2 - 7, 61 - (int)(15.0 - this.mu * 15.0), 32, 0, 15, 15);
        this.buttonList.add(new GuiButton(2, screenWidth / 2 - 10, 59 - (int)(15.0 - this.mu * 15.0), 20, 20, ""));
        this.drawTexturedModalRect(screenWidth / 2 - 20, 20, this.girl.getDataManager().get(Supporter.HAS_CHEST) ? 0 : 40, 130, 40, 40);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution resolution = new ScaledResolution(this.mc);
        int screenWidth = resolution.getScaledWidth();
        if (this.girl.getDataManager().get(Supporter.HAS_CHEST) && mouseX >= screenWidth / 2 - 20 && mouseX <= screenWidth / 2 + 20 && mouseY >= 20 && mouseY <= 60) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new BeeOpenChest(this.girl.girlID(), this.player.getPersistentID()));
            this.onGuiClosed();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            if (this.isFollowing) {
                PackageHandler.INSTANCE.sendToServer((IMessage)new ChangeDataParameter(this.girl.girlID(), "master", ""));
                this.player.sendMessage(new TextComponentString(I18n.format("bee.dialogue.sad", new Object[0])));
            } else {
                PackageHandler.INSTANCE.sendToServer((IMessage)new ChangeDataParameter(this.girl.girlID(), "master", this.player.getPersistentID().toString()));
                this.player.sendMessage(new TextComponentString(I18n.format("bee.dialogue.exited", new Object[0])));
            }
            this.isFollowing = !this.isFollowing;
            this.player.closeScreen();
        }
        if (button.id == 1) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new SendCompanionHome(this.girl.girlID()));
            this.player.closeScreen();
        }
        if (button.id == 2) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new SetNewHome(this.girl.girlID(), new Vec3d(this.girl.posX, this.girl.posY, this.girl.posZ)));
            this.player.closeScreen();
            this.player.sendMessage(new TextComponentString(I18n.format("bee.dialogue.home", new Object[0])));
        }
    }
}

