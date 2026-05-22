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
package com.trolmastercard.sexmod;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
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
public class gm_class376 {
    final static Vec3i e = new Vec3i(255, 0, 0);
    final static Vec3i g = new Vec3i(0, 255, 0);
    final static Vec3i d = new Vec3i(0, 0, 255);
    final static ResourceLocation b = new ResourceLocation("sexmod", "textures/mark.png");
    static HashSet<BlockPos> f = new HashSet();
    static Minecraft a = Minecraft.getMinecraft();
    static TextureManager c = Minecraft.getMinecraft().getTextureManager();

    public static void a() {
        f.clear();
    }

    public static boolean a(BlockPos blockPos) {
        return f.contains(blockPos);
    }

    public static void b() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Vec3d vec3d = b6_class67.a(ModInfo.k, ModInfo.j, (double)a.getRenderPartialTicks());
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
        c.bindTexture(b);
        GlStateManager.translate(-vec3d.x, -vec3d.y, -vec3d.z);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        try {
            for (BlockPos blockPos : f) {
                Vec3i vec3i = gm_class376.b(blockPos);
                gm_class376.a(bufferBuilder, blockPos, vec3i.getX(), vec3i.getY(), vec3i.getZ());
            }
        } catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        tessellator.draw();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    static Vec3i b(BlockPos blockPos) {
        Block block = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
        if (block instanceof BlockBed) {
            return d;
        }
        if (block instanceof BlockChest) {
            return g;
        }
        return e;
    }

    static void a(BufferBuilder bufferBuilder, BlockPos blockPos, int n, int n2, int n3) {
        bufferBuilder.pos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).tex(0.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).tex(1.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY(), blockPos.getZ()).tex(1.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY(), blockPos.getZ()).tex(0.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY() + 1, 1 + blockPos.getZ()).tex(0.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY() + 1, 1 + blockPos.getZ()).tex(1.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY(), 1 + blockPos.getZ()).tex(1.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY(), 1 + blockPos.getZ()).tex(0.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).tex(0.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY() + 1, 1 + blockPos.getZ()).tex(1.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY(), 1 + blockPos.getZ()).tex(1.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY(), blockPos.getZ()).tex(0.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).tex(0.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY() + 1, 1 + blockPos.getZ()).tex(1.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY(), 1 + blockPos.getZ()).tex(1.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY(), blockPos.getZ()).tex(0.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY(), 1 + blockPos.getZ()).tex(0.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY(), 1 + blockPos.getZ()).tex(1.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY(), blockPos.getZ()).tex(1.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY(), blockPos.getZ()).tex(0.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY() + 1, 1 + blockPos.getZ()).tex(0.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY() + 1, 1 + blockPos.getZ()).tex(1.0, 1.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(1 + blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).tex(1.0, 0.0).color(n, n2, n3, 255).endVertex();
        bufferBuilder.pos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).tex(0.0, 0.0).color(n, n2, n3, 255).endVertex();
    }

    public static void a(HashSet<BlockPos> hashSet) {
        f.addAll(hashSet);
    }

    public static void b(HashSet<BlockPos> hashSet) {
        f.removeAll(hashSet);
    }

    @SubscribeEvent
    public void a(RenderWorldLastEvent renderWorldLastEvent) {
        GlStateManager.enableColorMaterial();
        GL11.glDisable(2896);
        ItemStack itemStack = gm_class376.a.player.getHeldItem(EnumHand.MAIN_HAND);
        if (itemStack.getItem() != DragonStaff.DRAGON_STAFF) {
            itemStack = gm_class376.a.player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (itemStack.getItem() == DragonStaff.DRAGON_STAFF) {
            gm_class376.b();
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
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (entityPlayerSP == null) {
            return;
        }
        ModInfo.k = ModInfo.j;
        ModInfo.j = entityPlayerSP.getPositionVector();
    }

    private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
        return concurrentModificationException;
    }
}

