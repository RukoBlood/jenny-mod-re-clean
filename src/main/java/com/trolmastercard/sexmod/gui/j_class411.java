/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.DragonStaffRenderer;
import com.trolmastercard.sexmod.Packages.*;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.gm_class376;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.lwjgl.opengl.GL11;

public class j_class411 extends GuiScreen {
    final static float f = 100.0f;
    final static float g = 15.0f;
    final static float j = 0.5f;
    final static ResourceLocation h = new ResourceLocation("sexmod", "textures/gui/command.png");
    final static HashSet<Material> MATERIALS_LIST = new HashSet<Material>(Arrays.asList(Material.CLAY, Material.ROCK, Material.SAND, Material.GROUND));
    static public boolean d = false;
    float m = 0.0f;
    float a = 0.0f;
    float k = 0.0f;
    float n = 0.0f;
    float i = 0.0f;
    IBlockState e;
    BlockPos c;
    EnumFacing b;

    public j_class411() {
        Minecraft minecraft = Minecraft.getMinecraft();
        this.c = minecraft.objectMouseOver.getBlockPos();
        this.b = minecraft.objectMouseOver.sideHit == null ? EnumFacing.NORTH : minecraft.objectMouseOver.sideHit.getOpposite();
        if (this.c == null) {
            this.c = BlockPos.ORIGIN;
        }
        this.e = minecraft.world.getBlockState(this.c);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        List<Float> list = Arrays.asList(Float.valueOf(this.a), Float.valueOf(this.k), Float.valueOf(this.n), Float.valueOf(this.i));
        float f = Collections.max(list).floatValue();
        if (f == 0.0f) {
            return;
        }
        if (this.a == f) {
            this.b();
        }
        if (this.k == f) {
            this.d();
        }
        if (this.n == f) {
            this.c();
        }
        if (this.i == f) {
            this.a();
        }
    }

    void b() {
        IBlockState iBlockState = this.mc.world.getBlockState(this.c);
        if (iBlockState.getBlock() instanceof BlockBed || iBlockState.getBlock() instanceof BlockChest) {
            PackageHandler.networkWrapper.sendToServer((IMessage)new SendBlocks(this.c, !gm_class376.a(this.c)));
        }
    }

    void d() {
        PackageHandler.networkWrapper.sendToServer((IMessage)new SetTribeFollowMode(!d));
    }

    void c() {
        DragonStaffRenderer.a();
    }

    void a() {
        Object[] objectArray;
        Block block = this.e.getBlock();
        if (block instanceof BlockLog) {
            if (gm_class376.a(this.c)) {
                PackageHandler.networkWrapper.sendToServer((IMessage)new CancelTask(this.c));
                return;
            }
            PackageHandler.networkWrapper.sendToServer((IMessage)new FallTree(this.c));
        }
        if ((objectArray = this.e()) != null) {
            if (gm_class376.a(this.c)) {
                PackageHandler.networkWrapper.sendToServer((IMessage)new CancelTask(this.c));
                return;
            }
            PackageHandler.networkWrapper.sendToServer((IMessage)new Mine((BlockPos)objectArray[0], (EnumFacing)objectArray[1]));
        }
    }

    @Nullable
    Object[] e() {
        Material material = this.mc.world.getBlockState(this.c).getMaterial();
        EntityPlayerSP entityPlayerSP = this.mc.player;
        if (!MATERIALS_LIST.contains(material)) {
            return null;
        }
        if (((Entity)entityPlayerSP).getPosition().getY() > this.c.getY()) {
            return null;
        }
        BlockPos blockPos = this.c;
        while (this.mc.world.getBlockState(blockPos.down().add(this.b.getOpposite().getDirectionVec())).getBlock() == Blocks.AIR) {
            blockPos = blockPos.down();
        }
        if (this.c.getY() - blockPos.getY() > 3) {
            return null;
        }
        return new Object[]{blockPos, this.b};
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        boolean bl;
        super.drawScreen(n, n2, f);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);
        this.m = Math.min(1.0f, this.m + this.mc.getTickLength() / 5.0f);
        float f2 = (float) this.a((double) this.m);
        float f3 = (1.0f - f2) * 100.0f;
        this.a += (float) (n < this.width / 2 && n2 > this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.k += (float) (n < this.width / 2 && n2 < this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.n += (float) (n > this.width / 2 && n2 > this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.i += (float) (n > this.width / 2 && n2 < this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.a = Utils.clamp(this.a, 0.0f, 1.0f);
        this.k = Utils.clamp(this.k, 0.0f, 1.0f);
        this.n = Utils.clamp(this.n, 0.0f, 1.0f);
        this.i = Utils.clamp(this.i, 0.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) this.width / 2.0f, (float) this.height / 2.0f, 0.0f);
        GlStateManager.scale(f2, f2, f2);
        this.mc.renderEngine.bindTexture(h);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f + this.k * 0.5f, 1.0f + this.k * 0.5f, 1.0f);
        this.drawTexturedModalRect(-62.0f + f3 - this.k * 15.0f, -62.0f + f3 - this.k * 15.0f, 0, 0, 64, 64);
        this.c(f3);
        if (d) {
            this.drawTexturedModalRect(-62.0f + f3 - this.k * 15.0f, -62.0f + f3 - this.k * 15.0f, 128, 64, 64, 64);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f + this.n * 0.5f, 1.0f + this.n * 0.5f, 1.0f);
        this.drawTexturedModalRect(-2.0f - f3 + this.n * 15.0f, -2.0f - f3 + this.n * 15.0f, 0, 0, 64, 64);
        this.a(f3);
        if (DragonStaffRenderer.b()) {
            this.drawTexturedModalRect(-2.0f - f3 + this.n * 15.0f, -2.0f - f3 + this.n * 15.0f, 128, 64, 64, 64);
        }
        GlStateManager.popMatrix();
        Block block = this.e.getBlock();
        boolean bl2 = block instanceof BlockChest;
        boolean bl3 = block instanceof BlockBed;
        if (bl2 || bl3) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f + this.a * 0.5f, 1.0f + this.a * 0.5f, 1.0f);
            this.drawTexturedModalRect(-62.0f + f3 - this.a * 15.0f, -2.0f - f3 + this.a * 15.0f, 0, 0, 64, 64);
            if (bl2) {
                this.d(f3);
            }
            if (bl3) {
                this.f(f3);
            }
            if (gm_class376.a(this.c)) {
                this.drawTexturedModalRect(-62.0f + f3 - this.a * 15.0f, -2.0f - f3 + this.a * 15.0f, 128, 64, 64, 64);
            }
            GlStateManager.popMatrix();
        }
        boolean bl4 = block instanceof BlockLog;
        boolean bl5 = bl = this.e() != null;
        if (bl4 || bl) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f + this.i * 0.5f, 1.0f + this.i * 0.5f, 1.0f);
            this.drawTexturedModalRect(-2.0f - f3 + this.i * 15.0f, -62.0f + f3 - this.i * 15.0f, 0, 0, 64, 64);
            if (bl4) {
                this.e(f3);
            }
            if (bl) {
                this.b(f3);
            }
            if (gm_class376.a(this.c)) {
                this.drawTexturedModalRect(-2.0f - f3 + this.i * 15.0f, -62.0f + f3 - this.i * 15.0f, 128, 64, 64, 64);
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GL11.glDisable(3042);
    }

    void a(float f) {
        this.drawTexturedModalRect(-2.0f - f + this.n * 15.0f, -2.0f - f + this.n * 15.0f, 192, 64, 64, 64);
    }

    void c(float f) {
        this.drawTexturedModalRect(-62.0f + f - this.k * 15.0f, -62.0f + f - this.k * 15.0f, 64, 64, 64, 64);
    }

    void e(float f) {
        this.drawTexturedModalRect(-2.0f - f + this.i * 15.0f, -62.0f + f - this.i * 15.0f, 64, 0, 64, 64);
    }

    void b(float f) {
        this.drawTexturedModalRect(-2.0f - f + this.i * 15.0f, -62.0f + f - this.i * 15.0f, 128, 0, 64, 64);
    }

    void f(float f) {
        this.drawTexturedModalRect(-62.0f + f - this.a * 15.0f, -2.0f - f + this.a * 15.0f, 0, 64, 64, 64);
    }

    void d(float f) {
        this.drawTexturedModalRect(-62.0f + f - this.a * 15.0f, -2.0f - f + this.a * 15.0f, 192, 0, 64, 64);
    }

    double a(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return 1.0 + d3 * Math.pow(d - 1.0, 3.0) + d2 * Math.pow(d - 1.0, 2.0);
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        this.mc.player.closeScreen();
        super.mouseReleased(n, n2, n3);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private static NullPointerException a(NullPointerException nullPointerException) {
        return nullPointerException;
    }
}

