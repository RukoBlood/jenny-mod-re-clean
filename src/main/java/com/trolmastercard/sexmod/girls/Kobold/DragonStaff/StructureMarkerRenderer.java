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

import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
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
    final static Vec3i e = new Vec3i(255, 0, 0);
    final static Vec3i g = new Vec3i(0, 255, 0);
    final static Vec3i d = new Vec3i(0, 0, 255);
    final static ResourceLocation MARK_TEXTURE = new ResourceLocation("sexmod", "textures/mark.png");
    static HashSet<BlockPos> markers = new HashSet();
    static Minecraft minecraft = Minecraft.getMinecraft();
    static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

    public static void ClearMarkers() {
        markers.clear();
    }

    public static boolean a(BlockPos pos) {
        return markers.contains(pos);
    }

    public static void Render() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Vec3d vec3d = ReferenceAndRotationHelper.LerpVec3d(ReferenceAndRotationHelper.k, ReferenceAndRotationHelper.j, (double) minecraft.getRenderPartialTicks());
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
        textureManager.bindTexture(MARK_TEXTURE);
        GlStateManager.translate(-vec3d.x, -vec3d.y, -vec3d.z);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (BlockPos blockPos : markers) {
            Vec3i vec3i = StructureMarkerRenderer.b(blockPos);
            StructureMarkerRenderer.a(bufferBuilder, blockPos, vec3i.getX(), vec3i.getY(), vec3i.getZ());
        }
        tessellator.draw();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    static Vec3i b(BlockPos pos) {
        Block block = Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
        if (block instanceof BlockBed) {
            return d;
        }
        if (block instanceof BlockChest) {
            return g;
        }
        return e;
    }

    static void a(BufferBuilder buf, BlockPos pos, int r, int g, int b) {
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

    public static void AddList(HashSet<BlockPos> list) {
        markers.addAll(list);
    }

    public static void CleanList(HashSet<BlockPos> list) {
        markers.removeAll(list);
    }

    @SubscribeEvent
    public void a(RenderWorldLastEvent event) {
        GlStateManager.enableColorMaterial();
        GL11.glDisable(2896);
        ItemStack itemStack = StructureMarkerRenderer.minecraft.player.getHeldItem(EnumHand.MAIN_HAND);
        if (itemStack.getItem() != DragonStaffItem.DRAGON_STAFF) {
            itemStack = StructureMarkerRenderer.minecraft.player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (itemStack.getItem() == DragonStaffItem.DRAGON_STAFF) {
            StructureMarkerRenderer.Render();
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GL11.glEnable(2896);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        if (playerSP == null) {
            return;
        }
        ReferenceAndRotationHelper.k = ReferenceAndRotationHelper.j;
        ReferenceAndRotationHelper.j = playerSP.getPositionVector();
    }
}

