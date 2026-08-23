/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Kobold.DragonStaff;

import java.util.HashSet;

import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
//gm
//Mark related stuff
//P.S. Dragon Staff related
public class StructureMarkerRenderer {
    final static Vec3i COLOR_RED = new Vec3i(255, 0, 0);
    final static Vec3i COLOR_GREEM = new Vec3i(0, 255, 0);
    final static Vec3i COLOR_BLUE = new Vec3i(0, 0, 255);
    final static ResourceLocation MARK_TEXTURE = new ResourceLocation("sexmod", "textures/mark.png");
    static HashSet<BlockPos> markers = new HashSet<>();
    static Minecraft mc = Minecraft.getMinecraft();
    static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

    public static void ClearMarkers() {
        markers.clear();
    }

    public static boolean isMarked(BlockPos pos) {
        return markers.contains(pos);
    }

    public static void renderMarkers() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Vec3d cameraPos = RotationHelper.LerpVec3d(Reference.cameraPosPrevious, Reference.cameraPosCurrent, mc.getRenderPartialTicks());
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
        textureManager.bindTexture(MARK_TEXTURE);
        GlStateManager.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (BlockPos pos : markers) {
            Vec3i color = getBlockColor(pos);
            drawMarkerFace(buffer, pos, color.getX(), color.getY(), color.getZ());
        }
        tessellator.draw();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    static Vec3i getBlockColor(BlockPos pos) {
        Block block = Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
        return block instanceof BlockBed ? COLOR_BLUE : block instanceof BlockChest ? COLOR_GREEM : COLOR_RED;
    }

    static void drawMarkerFace(BufferBuilder buf, BlockPos pos, int r, int g, int b) {
        buf.pos(pos.getX(), pos.getY() + 1, pos.getZ()).tex(0.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY() + 1, pos.getZ()).tex(1.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY(), pos.getZ()).tex(1.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY(), pos.getZ()).tex(0.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY() + 1, 1 + pos.getZ()).tex(0.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY() + 1, 1 + pos.getZ()).tex(1.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY(), 1 + pos.getZ()).tex(1.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY(), 1 + pos.getZ()).tex(0.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY() + 1, pos.getZ()).tex(0.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY() + 1, 1 + pos.getZ()).tex(1.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY(), 1 + pos.getZ()).tex(1.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY(), pos.getZ()).tex(0.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY() + 1, pos.getZ()).tex(0.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY() + 1, 1 + pos.getZ()).tex(1.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY(), 1 + pos.getZ()).tex(1.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY(), pos.getZ()).tex(0.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY(), 1 + pos.getZ()).tex(0.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY(), 1 + pos.getZ()).tex(1.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY(), pos.getZ()).tex(1.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY(), pos.getZ()).tex(0.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY() + 1, 1 + pos.getZ()).tex(0.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY() + 1, 1 + pos.getZ()).tex(1.0, 1.0).color(r, g, b, 255).endVertex();
        buf.pos(1 + pos.getX(), pos.getY() + 1, pos.getZ()).tex(1.0, 0.0).color(r, g, b, 255).endVertex();
        buf.pos(pos.getX(), pos.getY() + 1, pos.getZ()).tex(0.0, 0.0).color(r, g, b, 255).endVertex();
    }

    public static void addMarkers(HashSet<BlockPos> marks) {
        markers.addAll(marks);
    }

    public static void removeMarkers(HashSet<BlockPos> marks) {
        markers.removeAll(marks);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        GlStateManager.enableColorMaterial();
        GL11.glDisable(2896);
        ItemStack itemStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
        if (itemStack.getItem() != DragonStaffItem.DRAGON_STAFF) {
            itemStack = mc.player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (itemStack.getItem() == DragonStaffItem.DRAGON_STAFF) {
            renderMarkers();
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GL11.glEnable(2896);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onPlayerTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
            if (playerSP != null) {
                Reference.cameraPosPrevious = Reference.cameraPosCurrent;
                Reference.cameraPosCurrent = playerSP.getPositionVector();
            }
        }
    }
}

