/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ch_class132
extends GuiScreen {
    Supporter c;
    EntityPlayer a;
    boolean e;
    final static ResourceLocation b = new ResourceLocation("sexmod", "textures/gui/girlinventory.png");
    double d = 0.0;

    public ch_class132(Supporter fo_class3232, EntityPlayer entityPlayer) {
        this.c = fo_class3232;
        this.a = entityPlayer;
        this.e = !"".equals(fo_class3232.getDataManager().get(GirlEntity.v));
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
        this.drawTexturedModalRect(n3 / 2 - 20, 20, this.c.getDataManager().get(Supporter.K) != false ? 0 : 40, 130, 40, 40);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n4 = scaledResolution.getScaledWidth();
        if (this.c.getDataManager().get(Supporter.K).booleanValue() && n >= n4 / 2 - 20 && n <= n4 / 2 + 20 && n2 >= 20 && n2 <= 60) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new f3_class287(this.c.girlID(), this.a.getPersistentID()));
            this.onGuiClosed();
        }
        super.mouseClicked(n, n2, n3);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        super.actionPerformed(guiButton);
        if (guiButton.id == 0) {
            if (this.e) {
                NetworkRegistry.networkWrapper.sendToServer((IMessage)new n_class415(this.c.girlID(), "master", ""));
                this.a.sendMessage(new TextComponentString(I18n.format("bee.dialogue.sad", new Object[0])));
            } else {
                NetworkRegistry.networkWrapper.sendToServer((IMessage)new n_class415(this.c.girlID(), "master", this.a.getPersistentID().toString()));
                this.a.sendMessage(new TextComponentString(I18n.format("bee.dialogue.exited", new Object[0])));
            }
            this.e = !this.e;
            this.a.closeScreen();
        }
        if (guiButton.id == 1) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new gg_class366(this.c.girlID()));
            this.a.closeScreen();
        }
        if (guiButton.id == 2) {
            NetworkRegistry.networkWrapper.sendToServer((IMessage)new a6_class13(this.c.girlID(), new Vec3d(this.c.posX, this.c.posY, this.c.posZ)));
            this.a.closeScreen();
            this.a.sendMessage(new TextComponentString(I18n.format("bee.dialogue.home", new Object[0])));
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

