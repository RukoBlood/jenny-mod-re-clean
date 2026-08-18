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
import com.trolmastercard.sexmod.Packets.*;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.StructureMarkerRenderer;
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

//j.class
//Related to dragon staff
public class DragonStaffGUI extends GuiScreen {
    //final static float f = 100.0f;
    //final static float g = 15.0f;
    //final static float j = 0.5f;
    final static ResourceLocation GUI_TEXTURE = new ResourceLocation("sexmod", "textures/gui/command.png");
    final static HashSet<Material> DIGGABLE_MATERIALS = new HashSet<Material>(Arrays.asList(Material.CLAY, Material.ROCK, Material.SAND, Material.GROUND));
    static public boolean isTribeFollowing = false;
    float openAnimationTime = 0.0f;
    float weightBottomLeft = 0.0f;
    float weightTopLeft = 0.0f;
    float weightBottomRight = 0.0f;
    float weightTopRight = 0.0f;
    IBlockState targetBlockState;
    BlockPos targetPos;
    EnumFacing relativeFacing;

    public DragonStaffGUI() {
        Minecraft mc = Minecraft.getMinecraft();
        this.targetPos = mc.objectMouseOver.getBlockPos();
        this.relativeFacing = mc.objectMouseOver.sideHit == null ? EnumFacing.NORTH : mc.objectMouseOver.sideHit.getOpposite();
        if (this.targetPos == null) {
            this.targetPos = BlockPos.ORIGIN;
        }
        this.targetBlockState = mc.world.getBlockState(this.targetPos);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        List<Float> weights = Arrays.asList(this.weightBottomLeft, this.weightTopLeft, this.weightBottomRight, this.weightTopRight);
        float maxWeight = Collections.max(weights);
        if (maxWeight == 0.0f) {
            return;
        }
        if (this.weightBottomLeft == maxWeight) {
            this.executeBaseObjectInteraction();
        }
        if (this.weightTopLeft == maxWeight) {
            this.toggleTribeFollowMode();
        }
        if (this.weightBottomRight == maxWeight) {
            this.triggerAreaRender();
        }
        if (this.weightTopRight == maxWeight) {
            this.executeResourceHarvestTask();
        }
    }

    void executeBaseObjectInteraction() {
        IBlockState block = this.mc.world.getBlockState(this.targetPos);
        if (block.getBlock() instanceof BlockBed || block.getBlock() instanceof BlockChest) {
            PackageHandler.INSTANCE.sendToServer((IMessage)new SendBlocks(this.targetPos, !StructureMarkerRenderer.a(this.targetPos)));
        }
    }

    void toggleTribeFollowMode() {
        PackageHandler.INSTANCE.sendToServer((IMessage)new SetTribeFollowMode(!isTribeFollowing));
    }

    void triggerAreaRender() {
        DragonStaffRenderer.a();
    }

    void executeResourceHarvestTask() {
        Object[] mineParams;
        Block block = this.targetBlockState.getBlock();
        if (block instanceof BlockLog) {
            if (StructureMarkerRenderer.a(this.targetPos)) {
                PackageHandler.INSTANCE.sendToServer((IMessage)new CancelTask(this.targetPos));
                return;
            }
            PackageHandler.INSTANCE.sendToServer((IMessage)new FallTree(this.targetPos));
        }
        if ((mineParams = this.validateAndCalculateMiningZone()) != null) {
            if (StructureMarkerRenderer.a(this.targetPos)) {
                PackageHandler.INSTANCE.sendToServer((IMessage)new CancelTask(this.targetPos));
                return;
            }
            PackageHandler.INSTANCE.sendToServer((IMessage)new Mine((BlockPos)mineParams[0], (EnumFacing)mineParams[1]));
        }
    }

    @Nullable
    Object[] validateAndCalculateMiningZone() {
        Material material = this.mc.world.getBlockState(this.targetPos).getMaterial();
        EntityPlayerSP player = this.mc.player;
        if (!DIGGABLE_MATERIALS.contains(material)) {
            return null;
        }
        if (((Entity)player).getPosition().getY() > this.targetPos.getY()) {
            return null;
        }
        BlockPos currentCheckPos = this.targetPos;
        while (this.mc.world.getBlockState(currentCheckPos.down().add(this.relativeFacing.getOpposite().getDirectionVec())).getBlock() == Blocks.AIR) {
            currentCheckPos = currentCheckPos.down();
        }
        if (this.targetPos.getY() - currentCheckPos.getY() > 3) {
            return null;
        }
        return new Object[]{currentCheckPos, this.relativeFacing};
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean isDiggable;
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);
        
        this.openAnimationTime = Math.min(1.0f, this.openAnimationTime + this.mc.getTickLength() / 5.0f);
        float animationScale = (float) this.calculateBackEaseOut((double) this.openAnimationTime);
        float offsetDistance = (1.0f - animationScale) * 100.0f;

        this.weightBottomLeft += (float) (mouseX < this.width / 2 && mouseY > this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.weightTopLeft += (float) (mouseX < this.width / 2 && mouseY < this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.weightBottomRight += (float) (mouseX > this.width / 2 && mouseY > this.height / 2 ? 1 : -1) * this.mc.getTickLength();
        this.weightTopRight += (float) (mouseX > this.width / 2 && mouseY < this.height / 2 ? 1 : -1) * this.mc.getTickLength();

        this.weightBottomLeft = ThreadNames.clamp(this.weightBottomLeft, 0.0f, 1.0f);
        this.weightTopLeft = ThreadNames.clamp(this.weightTopLeft, 0.0f, 1.0f);
        this.weightBottomRight = ThreadNames.clamp(this.weightBottomRight, 0.0f, 1.0f);
        this.weightTopRight = ThreadNames.clamp(this.weightTopRight, 0.0f, 1.0f);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) this.width / 2.0f, (float) this.height / 2.0f, 0.0f);
        GlStateManager.scale(animationScale, animationScale, animationScale);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f + this.weightTopLeft * 0.5f, 1.0f + this.weightTopLeft * 0.5f, 1.0f);
        this.drawTexturedModalRect(-62.0f + offsetDistance - this.weightTopLeft * 15.0f, -62.0f + offsetDistance - this.weightTopLeft * 15.0f, 0, 0, 64, 64);
        this.drawFollowIcon(offsetDistance);
        if (isTribeFollowing) {
            this.drawTexturedModalRect(-62.0f + offsetDistance - this.weightTopLeft * 15.0f, -62.0f + offsetDistance - this.weightTopLeft * 15.0f, 128, 64, 64, 64);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f + this.weightBottomRight * 0.5f, 1.0f + this.weightBottomRight * 0.5f, 1.0f);
        this.drawTexturedModalRect(-2.0f - offsetDistance + this.weightBottomRight * 15.0f, -2.0f - offsetDistance + this.weightBottomRight * 15.0f, 0, 0, 64, 64);
        this.drawRenderZoneIcon(offsetDistance);
        if (DragonStaffRenderer.b()) {
            this.drawTexturedModalRect(-2.0f - offsetDistance + this.weightBottomRight * 15.0f, -2.0f - offsetDistance + this.weightBottomRight * 15.0f, 128, 64, 64, 64);
        }
        GlStateManager.popMatrix();

        Block block = this.targetBlockState.getBlock();
        boolean isChest = block instanceof BlockChest;
        boolean isBed = block instanceof BlockBed;

        if (isChest || isBed) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f + this.weightBottomLeft * 0.5f, 1.0f + this.weightBottomLeft * 0.5f, 1.0f);
            this.drawTexturedModalRect(-62.0f + offsetDistance - this.weightBottomLeft * 15.0f, -2.0f - offsetDistance + this.weightBottomLeft * 15.0f, 0, 0, 64, 64);
            if (isChest) {
                this.drawChestIcon(offsetDistance);
            }
            if (isBed) {
                this.drawBedIcon(offsetDistance);
            }
            if (StructureMarkerRenderer.a(this.targetPos)) {
                this.drawTexturedModalRect(-62.0f + offsetDistance - this.weightBottomLeft * 15.0f, -2.0f - offsetDistance + this.weightBottomLeft * 15.0f, 128, 64, 64, 64);
            }
            GlStateManager.popMatrix();
        }

        boolean isLog = block instanceof BlockLog;
        boolean bl5 = isDiggable = this.validateAndCalculateMiningZone() != null;

        if (isLog || isDiggable) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f + this.weightTopRight * 0.5f, 1.0f + this.weightTopRight * 0.5f, 1.0f);
            this.drawTexturedModalRect(-2.0f - offsetDistance + this.weightTopRight * 15.0f, -62.0f + offsetDistance - this.weightTopRight * 15.0f, 0, 0, 64, 64);
            if (isLog) {
                this.drawLumberjackIcon(offsetDistance);
            }
            if (isDiggable) {
                this.drawMiningIcon(offsetDistance);
            }
            if (StructureMarkerRenderer.a(this.targetPos)) {
                this.drawTexturedModalRect(-2.0f - offsetDistance + this.weightTopRight * 15.0f, -62.0f + offsetDistance - this.weightTopRight * 15.0f, 128, 64, 64, 64);
            }
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        GL11.glDisable(GL11.GL_BLEND);
    }

    void drawRenderZoneIcon(float offset) {
        this.drawTexturedModalRect(-2.0f - offset + this.weightBottomRight * 15.0f, -2.0f - offset + this.weightBottomRight * 15.0f, 192, 64, 64, 64);
    }

    void drawFollowIcon(float offset) {
        this.drawTexturedModalRect(-62.0f + offset - this.weightTopLeft * 15.0f, -62.0f + offset - this.weightTopLeft * 15.0f, 64, 64, 64, 64);
    }

    void drawLumberjackIcon(float offset) {
        this.drawTexturedModalRect(-2.0f - offset + this.weightTopRight * 15.0f, -62.0f + offset - this.weightTopRight * 15.0f, 64, 0, 64, 64);
    }

    void drawMiningIcon(float offset) {
        this.drawTexturedModalRect(-2.0f - offset + this.weightTopRight * 15.0f, -62.0f + offset - this.weightTopRight * 15.0f, 128, 0, 64, 64);
    }

    void drawBedIcon(float offset) {
        this.drawTexturedModalRect(-62.0f + offset - this.weightBottomLeft * 15.0f, -2.0f - offset + this.weightBottomLeft * 15.0f, 0, 64, 64, 64);
    }

    void drawChestIcon(float offset) {
        this.drawTexturedModalRect(-62.0f + offset - this.weightBottomLeft * 15.0f, -2.0f - offset + this.weightBottomLeft * 15.0f, 192, 0, 64, 64);
    }

    double calculateBackEaseOut(double progress) {
        double s = 1.70158;
        double s1 = s + 1.0;
        return 1.0 + s1 * Math.pow(progress - 1.0, 3.0) + s * Math.pow(progress - 1.0, 2.0);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.mc.player.closeScreen();
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

