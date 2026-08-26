/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Vector2f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4d
 */
package com.trolmastercard.sexmod.girls.Kobold.DragonStaff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;

import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.util.MatrixHelper;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class DragonStaffRenderer extends GeoItemRenderer<DragonStaffItem> {
    final static private ResourceLocation c = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    final private CrystalModel crystalModel = new CrystalModel();
    final static float p = 10.0f;
    final static float f = 1.5f;
    final static float m = 0.175f;
    final static float r = 0.1f;
    final static float g = 0.04f;
    final static float d = 8.0f;
    final static float i = 6.0f;
    final static float a = 1.3f;
    final static Vector2f[] l = new Vector2f[]{new Vector2f(1.0f, 0.0f), new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, 0.0f), new Vector2f(0.5f, 0.5f), new Vector2f(0.75f, 0.25f), new Vector2f(0.25f, 0.75f), new Vector2f(0.25f, 0.75f)};
    static boolean isRendering = false;
    Minecraft mc = Minecraft.getMinecraft();
    Vector2f screenPos;
    double animationTicks = 0.0;
    EntityPlayer player;
    ItemStack heldItem;
    static HashMap<ItemStack, Vector3f> n = new HashMap();

    public DragonStaffRenderer() {
        super(new DragonStaffModel());
    }

    public static boolean isRenderingStaff() {
        return isRendering;
    }

    public static void toggleStaffRendering() {
        isRendering = !isRendering;
    }

    // was:
    //this.a((hy_class407)item, itemStack);
    @Override
    public void render(DragonStaffItem item, ItemStack stack) {
        EntityPlayer holder = null;
        for (EntityPlayer player : this.mc.world.playerEntities) {
            if (player.inventory.mainInventory.contains(stack)) {
                holder = player;
                break;
            }

            if (player.inventory.offHandInventory.contains(stack)) {
                holder = player;
                break;
            }
        }
        if (holder != null) {
            double d = holder.posX - holder.lastTickPosX;
            double d2 = holder.posZ - holder.lastTickPosZ;
            double d3 = Math.PI / 180 * (double)holder.rotationYaw;
            this.screenPos = new Vector2f((float)(d * Math.cos(d3) + d2 * Math.sin(d3)), (float)(-d * Math.sin(d3) + d2 * Math.cos(d3)));
        } else {
            this.screenPos = new Vector2f(0.0f, 0.0f);
        }
        if (!Minecraft.getMinecraft().isGamePaused()) {
            this.animationTicks = (float)Minecraft.getMinecraft().player.ticksExisted + this.mc.getRenderPartialTicks();
        }
        this.heldItem = stack;
        this.player = holder;
        super.render(item, stack);
    }

    @Override
    public void renderRecursively(BufferBuilder buffer, GeoBone bone, float red, float green, float blue, float alpha) {
        if ("staff".equals(bone.getName())) {
            GlStateManager.pushMatrix();
            Tessellator.getInstance().draw();
            MatrixHelper.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, bone);
            GlStateManager.translate(0.0, 1.5 + 0.001 * Math.sin(0.005 * this.animationTicks) + 0.001, 0.0);
            Vector3f vector3f = n.get(this.heldItem);
            GlStateManager.scale(this.getBobOffset(), this.getBobOffset(), this.getBobOffset());
            if (vector3f == null) {
                vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
            }
            vector3f.add((Tuple3f)new Vector3f(this.screenPos.x, this.player == null ? 0.0f : (float)(this.player.posY - this.player.lastTickPosY), this.screenPos.y));
            GlStateManager.rotate(vector3f.z * 10.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(vector3f.x * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-vector3f.y * 10.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate((float)(this.animationTicks * (double)0.1f), 1.0f, 1.0f, 1.0f);
            n.put(this.heldItem, vector3f);
            this.mc.getTextureManager().bindTexture(c);
            this.crystalModel.render(Minecraft.getMinecraft().player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
            GlStateManager.popMatrix();
            if (this.player != null) {
                this.collectAnimationBones();
            }
            // was 'KoboldStaffModel().a'
            this.mc.getTextureManager().bindTexture(new DragonStaffModel().getTextureLocation(null));
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        }
        super.renderRecursively(buffer, bone, red, green, blue, alpha);
    }

    void collectAnimationBones() {
        ArrayList<Integer> particleIds = new ArrayList<Integer>();
        ArrayList<Vec3d> particlePositions = new ArrayList<Vec3d>();
        for (Vector4d particleData : KoboldEntity.ACTIVE_TRIBE_SCREEN_POSITIONS) {
            particleIds.add((int)particleData.getW());
            particlePositions.add(new Vec3d(particleData.getX(), particleData.getY(), particleData.getZ()));
        }
        if (!particleIds.isEmpty()) {
            if (isRendering) {
                this.renderParticles(particleIds, particlePositions);
            } else {
                this.animateBones(particleIds);
            }
        }
    }

    void renderParticles(List<Integer> particleIds, List<Vec3d> particlePositions) {
        for (int i = 0; i < particleIds.size(); ++i) {
            float headYaw = RotationHelper.LerpFloat(this.player.prevRotationYawHead, this.player.rotationYawHead, this.mc.getRenderPartialTicks());
            float headPitch = RotationHelper.LerpFloat(this.player.prevRotationPitch, this.player.rotationPitch, this.mc.getRenderPartialTicks());
            Vec3d eyePos = RotationHelper.LerpVec3d(new Vec3d(this.player.prevPosX, this.player.prevPosY + (double)this.player.getEyeHeight(), this.player.prevPosZ), this.player.getPositionVector().add(0.0, this.player.getEyeHeight(), 0.0), (double)this.mc.getRenderPartialTicks());
            Vec3d relative = eyePos.subtract(particlePositions.get(i));
            relative = VectorMath.rotate(relative, -headPitch, headYaw);

            double magnitude = Math.abs(relative.x) + Math.abs(relative.z) + Math.abs(relative.y);
            double easedX = -relative.x / magnitude;
            double easedY = -relative.y / magnitude;
            double easedZ = relative.z / magnitude;
            easedX = this.easeInOut(easedX);
            easedY = this.easeInOut(easedY);
            easedZ = this.easeInOut(easedZ);
            this.renderParticleFrom(particleIds.get(i), (float)(easedX *= (double)1.3f), (float)(easedY *= (double)1.3f), (float)(easedZ *= (double)1.3f));
        }
    }

    void animateBones(List<Integer> particleIds) {
        float step = 1.0f / (float)particleIds.size();
        float progress = 0.0f;
        for (int i = 0; i < particleIds.size(); ++i) {
            this.renderParticleAt(particleIds.get(i), 1.0f - (progress += step), 0.0f + progress, (float) RotationHelper.LerpDouble((double)0.8f, (double)1.2f, (double)i / (double)particleIds.size()));
        }
    }

    double easeInOut(double value) {
        return value * Math.sqrt(1.0 - value * value / 2.0);
    }

    double getBobOffset() {
        return (double)0.175f + 0.025 * Math.sin(0.005 * this.animationTicks) + 0.025;
    }

    void renderParticleAt(int colorId, float x, float y, float z) {
        this.renderItem(new ItemStack(Blocks.WOOL, 1, colorId), x, y, z);
    }

    void renderParticleFrom(int colorId, float x, float y, float z) {
        this.renderItemAt(new ItemStack(Blocks.WOOL, 1, colorId), x, y, z);
    }

    void renderItemAt(ItemStack stack, float x, float y, float z) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 1.5 + 0.001 * Math.sin(0.005 * this.animationTicks) + 0.001, 0.0);
        GlStateManager.scale(0.04f, 0.04f, 0.04f);
        GlStateManager.translate(x * 6.0f, y * 6.0f, z * 6.0f);
        this.mc.getItemRenderer().renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    void renderItem(ItemStack stack, float rotX, float rotY, float rotZ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 1.5 + 0.001 * Math.sin(0.005 * this.animationTicks) + 0.001, 0.0);
        GlStateManager.scale(0.04f, 0.04f, 0.04f);
        GlStateManager.rotate((float)(this.animationTicks * 8.0 * (double)rotZ), 0.0f, rotX, rotY);
        GlStateManager.translate(6.0f, 0.0f, 0.0f);
        this.mc.getItemRenderer().renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    //@Override
    //public void render(Item item, ItemStack itemStack) {
    //    //this.a((hy_class407)item, itemStack);
    //
    //}
}

