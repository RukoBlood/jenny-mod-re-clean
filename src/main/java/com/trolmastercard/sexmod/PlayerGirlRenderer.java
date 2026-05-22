/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4f
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.vecmath.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class PlayerGirlRenderer
extends GirlRenderer<GirlEntity> {
    static public boolean v = false;
    ItemStack s = ItemStack.EMPTY;
    ItemStack x = ItemStack.EMPTY;
    boolean r = false;
    boolean u = false;
    protected PlayerGirl w;
    protected float y;
    float t = 0.0f;

    public PlayerGirlRenderer(RenderManager renderManager, AnimatedGeoModel<GirlEntity> animatedGeoModel) {
        super(renderManager, animatedGeoModel, 0.0);
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double d, double d2, double d3, float f, float f2) {
    }

    @CheckReturnValue
    boolean a_13(GirlEntity em_class2582) {
        if (em_class2582.boolean_h()) {
            return true;
        }
        boolean bl = v;
        v = false;
        return bl;
    }

    public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
        // TODO check clash above
        if (!this.a_13(em_class2582)) {
            return;
        }
        PlayerGirl ei_class2512 = (PlayerGirl)em_class2582;
        if (ei_class2512.java_util_UUID_m() == null) {
            return;
        }
        EntityPlayer entityPlayer = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(ei_class2512.java_util_UUID_m());
        if (entityPlayer == null) {
            return;
        }
        this.s = entityPlayer.getHeldItemMainhand();
        this.x = entityPlayer.getHeldItemOffhand();
        this.u = ei_class2512.ah;
        this.r = ei_class2512.ad;
        this.w = (PlayerGirl)em_class2582;
        this.y = f2;
        ei_class2512.f(entityPlayer);
        if (this.a(entityPlayer, em_class2582)) {
            this.renderLivingLabel(em_class2582, entityPlayer.getName(), d, d2 + (double)ei_class2512.float_i(), d3, 300);
        }
        super.doRender(em_class2582, d, d2, d3, f, f2);
    }

    @Override
    public Entity c(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof PlayerGirl)) {
            return em_class2582;
        }
        PlayerGirl ei_class2512 = (PlayerGirl)em_class2582;
        EntityPlayer entityPlayer = ei_class2512.net_minecraft_entity_player_EntityPlayer_k();
        if (entityPlayer == null) {
            return em_class2582;
        }
        return entityPlayer;
    }

    boolean a(EntityPlayer entityPlayer, GirlEntity em_class2582) {
        if (entityPlayer.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            return false;
        }
        Action fp_class3242 = em_class2582.currentAction();
        if (fp_class3242 == null) {
            return true;
        }
        return !fp_class3242.hideNameTag;
    }

    protected void a(String string, GeoBone geoBone) {
    }

    protected void a(String string, GeoBone geoBone, PlayerGirl ei_class2512, BufferBuilder bufferBuilder) {
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        //ItemStack itemStack;
        String string = geoBone.getName();
        if (this.r) {
            if (string.equals("upperBody")) {
                geoBone.setRotationX(geoBone.getRotationX() - 0.5f);
            }
            if (string.equals("head")) {
                geoBone.setRotationX(geoBone.getRotationX() + 0.5f);
            }
        }
        if (string.equals("head")) {
            this.a(bufferBuilder, geoBone, Color.ofRGB(f, f2, f3));
        }
        this.a(string, geoBone);
        this.a(string, geoBone, this.w, bufferBuilder);
        if (this.u && (this.s.getItem() instanceof ItemBow || this.x.getItem() instanceof ItemBow)) {
            if (string.equals("armR")) {
                geoBone.setRotationX(geoBone.getRotationX() - this.j.rotationPitch / 50.0f);
            }
            if (string.equals("armL")) {
                geoBone.setRotationY(geoBone.getRotationY() - this.j.rotationPitch / 50.0f);
            }
            if (this.x.getItem() instanceof ItemBow) {
                ItemStack itemStack = this.x;
                this.x = this.s;
                this.s = itemStack;
            }
        }
        if (this.u && this.s.getItem() instanceof ItemShield) {
            if (string.equals("armR")) {
                geoBone.setRotationZ(0.0f);
                geoBone.setRotationX(0.5f);
            } else if (this.x.getItem() instanceof ItemShield && string.equals("armL")) {
                geoBone.setRotationZ(0.0f);
                geoBone.setRotationX(0.5f);
            }
        }
        if (string.equals("weapon") && !this.s.isEmpty()) {
            this.a(bufferBuilder, geoBone, false);
        }
        if (string.equals("offhand") && !this.x.isEmpty()) {
            this.a(bufferBuilder, geoBone, true);
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        if ("Head2".equals(string) && !this.boolean_c()) {
            MATRIX_STACK.pop();
            return;
        }
        if (("neck".equals(string) || "head".equals(string)) && !this.boolean_a()) {
            MATRIX_STACK.pop();
            return;
        }
        if (!geoBone.isHidden) {
            Vector4f itemStack = this.a(string, f, f2, f3);
            f = ((Vector4f)itemStack).x;
            f2 = ((Vector4f)itemStack).y;
            f3 = ((Vector4f)itemStack).z;
            double d = ((Vector4f)itemStack).w;
            if (!this.p.contains(string)) {
                for (GeoCube object : geoBone.childCubes) {
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.q = geoBone;
                    this.a(bufferBuilder, object, f, f2, f3, f4, d);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }
            for (GeoBone geoBone2 : geoBone.childBones) {
                if (d == 0.0) {
                    this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
                    continue;
                }
                this.a(bufferBuilder, geoBone2, f, f2, f3, f4, d);
            }
        }
        try {
            MATRIX_STACK.pop();
        } catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
    }

    boolean boolean_a() {
        if (!((PlayerGirl)this.j).boolean_f()) {
            return true;
        }
        if (PlayerGirlRenderer.i.gameSettings.thirdPersonView != 0) {
            return true;
        }
        return PlayerGirlRenderer.i.currentScreen instanceof GuiInventory || PlayerGirlRenderer.i.currentScreen instanceof GuiContainerCreative;
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, Color color) {
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        p_class418.a(IGeoRenderer.MATRIX_STACK, geoBone);
        GL11.glEnable(2896);
        this.void_c();
        new bu_class100((IGeoRenderer)this).render(this.j, this.j.limbSwing, this.j.limbSwingAmount, this.y, 0.0f, 0.0f, 0.0f, color);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
    }

    protected void void_c() {
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, boolean bl) {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        p_class418.a(IGeoRenderer.MATRIX_STACK, geoBone);
        GL11.glEnable(2896);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        ItemStack itemStack = bl ? this.x : this.s;
        switch (itemStack.getItem().getItemUseAction(itemStack)) {
            case BOW: {
                this.a(bl);
                break;
            }
            case BLOCK: {
                this.a(bl, this.u);
            }
        }
        if (this.u && !bl && itemStack.getItem() instanceof ItemBow) {
            this.t += 0.015f;
            this.j.d(Math.round(-this.t * 20.0f + (float)itemStack.getMaxItemUseDuration()));
            this.j.void_a(itemStack);
            this.j.setActiveHand(EnumHand.MAIN_HAND);
            this.j.W();
        } else {
            this.t = 0.0f;
            this.j.d(0);
            this.j.void_a(ItemStack.EMPTY);
            this.j.W();
        }
        this.a(bl, itemStack);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        itemRenderer.renderItem(this.j, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    protected void a(boolean bl, ItemStack itemStack) {
        GlStateManager.rotate(bl ? 200.0f : 90.0f, 1.0f, 0.0f, 0.0f);
    }

    protected void a(boolean bl) {
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
    }

    protected void a(boolean bl, boolean bl2) {
        if (bl) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            if (bl2) {
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(35.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, 0.228f);
            }
        } else if (bl2) {
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.165f, 0.0f);
        }
    }

    private static IllegalStateException b(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

