/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

//da_class171
public class GalathRenderer
extends GirlRenderer<GalathEntity>
implements c3_class112 {
    final static public int D = 14;
    final static public HashSet<String> E = new HashSet<String>(){
        {
            this.add("static");
            this.add("turnable");
            this.add("slip");
            this.add("boobs");
            this.add("booty");
            this.add("vagina");
            this.add("fuckhole");
            this.add("futaBallLR");
            this.add("futaBallLL");
            this.add("coin");
            this.add("pentagram");
        }
    };
    final static public f7_class292 y = new f7_class292(0.0f, 0.0f, 0.0f);
    final static gv_class388 H = new gv_class388(152, 45, 62, 255);
    final static gv_class388 I = new gv_class388(84, 66, 88, 255);
    final static bm_class88 C = new bm_class88(0.25f, 0.125f);
    final static bm_class88 x = new bm_class88(0.375f, 0.125f);
    final static float F = 0.125f;
    final static ResourceLocation w = new ResourceLocation("sexmod", "textures/star.png");
    final static int v = 105;
    final static int A = 125;
    final static float B = 0.0296875f;
    final static float J = 0.06484375f;
    final static float z = 0.026124999f;
    final static float u = 0.0570625f;
    final static ef_class245.b_inner247 G = new ef_class245.b_inner247(H, 0.1f, 12, 0.035f, (n, f) -> (float)(Math.sin((double)f * 0.3 + -0.2 * (double)n) * 15.0), (n, f) -> (float)(Math.sin((double)f * -0.15 + -0.2 * (double)n) * 3.0), (n, f) -> 0.0f, 0.03f, 0.005f);
    final static ef_class245.b_inner247 t = new ef_class245.b_inner247(H, 0.0f, 12, 0.0f, (n, f) -> (float)(Math.sin((double)f * 0.3 + -0.2 * (double)n) * 15.0), (n, f) -> (float)(Math.sin((double)f * -0.15 + -0.2 * (double)n) * 3.0), (n, f) -> 0.0f, 0.03f, 0.005f);
    boolean r = false;
    float s = 0.0f;

    public GalathRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    //TODO rename to e
    //formerly a
    @Override
    @Nullable
    protected f7_class292 e(GalathEntity f__class2972) {
        if (f__class2972.world instanceof FakeWorld) {
            return null;
        }
        if (f__class2972.bb) {
            return null;
        }
        return y;
    }

    @Override
    public HashSet<String> a() {
        if (!this.r) {
            E.addAll(gx_class390.a);
            E.addAll(ManglelieRenderer.B);
            this.r = true;
        }
        return E;
    }

    @Override
    protected void b(Tessellator tessellator, BufferBuilder bufferBuilder, GirlEntity em_class2582, f7_class292 f7_class2922, float f) {
        GalathRenderer.a(tessellator, bufferBuilder, em_class2582, f7_class2922, f);
    }

    @Override
    protected void b(GalathEntity f__class2972) {
        float f;
        if (f__class2972.currentAction() != Action.MASTERBATE) {
            return;
        }
        f__class2972.rotationYaw = f = f__class2972.java_lang_Float_I().floatValue();
        f__class2972.prevRenderYawOffset = f;
        f__class2972.renderYawOffset = f;
        f__class2972.prevRotationYawHead = f;
        f__class2972.rotationYawHead = f;
    }

    @Override
    public void doRender(GalathEntity f__class2972, double d, double d2, double d3, float f, float f2) {
        Vec3d vec3d = GalathRenderer.a(f__class2972, f2);
        if (vec3d != null) {
            f__class2972.a(vec3d);
        }
        f__class2972.aG = vec3d;
        GalathEntity.a(f__class2972, f2);
        this.d_24(f__class2972);
        this.c(f__class2972);
        super.doRender(f__class2972, d, d2, d3, f, f2);
        GalathRenderer.a_27((GirlEntity)f__class2972, f2);
        if (f__class2972.boolean_b()) {
            ManglelieRenderer.a((GirlEntity)f__class2972, f2);
        }
    }

    void c(GalathEntity f__class2972) {
        if (f__class2972.currentAction() != Action.RAPE_CHARGE) {
            return;
        }
        f__class2972.prevRenderYawOffset = f__class2972.renderYawOffset = f__class2972.java_lang_Float_I().floatValue();
    }

    // TODO
    void d_24(GalathEntity f__class2972) {
        boolean bl;
        if (!f__class2972.getDataManager().get(GalathEntity.bP).booleanValue()) {
            return;
        }
        Vec3d vec3d = new Vec3d(f__class2972.lastTickPosX, f__class2972.lastTickPosY, f__class2972.lastTickPosZ);
        Vec3d vec3d2 = f__class2972.getPositionVector().subtract(vec3d);
        boolean bl2 = bl = Math.abs(vec3d2.x) + Math.abs(vec3d2.z) < (double)0.05f;
        if (bl) {
            f__class2972.renderYawOffset = this.s;
            f__class2972.prevRenderYawOffset = this.s;
        } else {
            float f;
            f__class2972.renderYawOffset = f = (float)(gc_class360.b(Math.atan2(vec3d2.z, vec3d2.x)) - 90.0);
            f__class2972.prevRenderYawOffset = f;
            this.s = f;
        }
    }

    @Nullable
    public static Vec3d a(GalathEntity f__class2972, float f) {
        float f2 = f__class2972.az();
        if (f2 == -1.0f) {
            f__class2972.af = -1L;
            f__class2972.aH = -1L;
            return null;
        }
        EntityLivingBase entityLivingBase = f__class2972.net_minecraft_entity_EntityLivingBase_M();
        if (entityLivingBase == null) {
            return null;
        }
        Vec3d vec3d = b6_class67.a(new Vec3d(entityLivingBase.prevPosX, entityLivingBase.prevPosY, entityLivingBase.prevPosZ), entityLivingBase.getPositionVector(), (double)f);
        if (f2 == 24.0f && f__class2972.af == -1L) {
            f__class2972.af = GalathRenderer.i.world.getTotalWorldTime();
            f__class2972.aH = f__class2972.af + 8L;
        }
        if (be_class78.a((double)f2, 24.0, 32.0)) {
            Vec3d vec3d2 = ck_class135.a(new Vec3d(0.0, 0.0, 3.0), f__class2972.java_lang_Float_I().floatValue() + 180.0f);
            Vec3d vec3d3 = f__class2972.net_minecraft_util_math_Vec3d_B();
            Vec3d vec3d4 = vec3d.add(0.0, entityLivingBase.getEyeHeight(), 0.0).add(vec3d2);
            float f3 = ((float) GalathRenderer.i.world.getTotalWorldTime() + f - (float)f__class2972.af) / (float)(f__class2972.aH - f__class2972.af);
            return b6_class67.a(vec3d3, vec3d4, (double)f3);
        }
        if (be_class78.a((double)f2, 32.0, 54.0)) {
            Vec3d vec3d5 = ck_class135.a(new Vec3d(0.0, 0.0, 1.5), f__class2972.java_lang_Float_I().floatValue() + 180.0f);
            return vec3d.add(vec3d5);
        }
        return null;
    }

    public static void a_27(GirlEntity em_class2582, float f) {
        EntityPlayerSP entityPlayerSP = GalathRenderer.i.player;
        if (entityPlayerSP == null) {
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        af_class27.a(i, em_class2582, f);
        i.getTextureManager().bindTexture(e);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GalathRenderer.a(em_class2582, bufferBuilder, tessellator, b6_class67.a(em_class2582.prevRenderYawOffset, em_class2582.renderYawOffset, f));
        GalathRenderer.b(em_class2582, bufferBuilder, tessellator, f);
        GalathRenderer.a(em_class2582, bufferBuilder, tessellator);
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    static void b(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator, float f) {
        double d;
        double d2;
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        if (!em_class2582.getDataManager().get(GalathEntity.bP).booleanValue()) {
            return;
        }
        if (em_class2582.getDataManager().get(GalathEntity.L).booleanValue()) {
            return;
        }
        GlStateManager.pushMatrix();
        Vec3d vec3d = em_class2582.b("stars");
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        float f2 = (float) GalathRenderer.i.world.getTotalWorldTime() + f;
        float f3 = (float)(Math.sin((double)f2 * 0.2) * 5.0);
        float f4 = (float)(Math.cos((double)f2 * 0.2) * 5.0);
        float f5 = (float)((double)f2 * 3.0);
        GlStateManager.rotate(f3, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f5, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4, 0.0f, 0.0f, 1.0f);
        float f6 = gc_class360.c(9.0);
        f7_class292 f7_class2922 = GalathEntity.aa;
        i.getTextureManager().bindTexture(e);
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.glLineWidth(GalathRenderer.a(em_class2582, f, 1.0f, 3.0f));
        float f7 = 0.0f;
        while ((double)f7 < Math.PI * 2) {
            d2 = Math.sin(f7) * (double)0.3f;
            d = Math.cos(f7) * (double)0.3f;
            bufferBuilder.pos(d2, 0.0, d).tex(0.0, 0.0).color(f7_class2922.a, f7_class2922.c, f7_class2922.b, 1.0f).endVertex();
            f7 += f6;
        }
        tessellator.draw();
        i.getTextureManager().bindTexture(w);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        f6 = gc_class360.c(60.0);
        f7 = 0.0f;
        while ((double)f7 < Math.PI * 2) {
            d2 = Math.sin(f7) * (double)0.3f;
            d = Math.cos(f7) * (double)0.3f;
            bufferBuilder.pos(d2 - (double)0.1f, 0.1f, d).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            bufferBuilder.pos(d2 + (double)0.1f, 0.1f, d).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            bufferBuilder.pos(d2 + (double)0.1f, -0.1f, d).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            bufferBuilder.pos(d2 - (double)0.1f, -0.1f, d).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            f7 += f6;
        }
        tessellator.draw();
        GlStateManager.popMatrix();
    }

    static void a(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator, float f) {
        if (em_class2582.currentAction() == Action.GIVE_COIN && Action.GIVE_COIN.ticksPlaying[1] > 100) {
            return;
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        Vec3d[][] vec3dArray = af_class27.a(em_class2582, f, "hairStrandStartR", "hairStrandMidR", "hairStrandEndR", 0.0296875f, 0.06484375f, 0.026124999f, 0.0570625f, "head");
        Vec3d[][] vec3dArray2 = af_class27.a(em_class2582, f, "hairStrandStartL", "hairStrandMidL", "hairStrandEndL", 0.0296875f, 0.06484375f, 0.026124999f, 0.0570625f, "head");
        af_class27.a(bufferBuilder, vec3dArray, I);
        af_class27.a(bufferBuilder, vec3dArray2, I);
        tessellator.draw();
    }

    static void a(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator) {
        if (!((b7_class68)((Object)em_class2582)).boolean_a()) {
            return;
        }
        i.getTextureManager().bindTexture(GalathModel.h);
        Vec3d[] vec3dArray = new Vec3d[14];
        Vec3d[] vec3dArray2 = new Vec3d[14];
        for (int i = 0; i < 14; ++i) {
            vec3dArray[i] = em_class2582.b("wingRV" + i);
            vec3dArray2[i] = em_class2582.b("wingLV" + i);
        }
        GalathRenderer.a(bufferBuilder, tessellator, vec3dArray);
        GalathRenderer.a(bufferBuilder, tessellator, vec3dArray2);
    }

    static void a(BufferBuilder bufferBuilder, Tessellator tessellator, Vec3d[] vec3dArray) {
        bufferBuilder.begin(4, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferBuilder.pos(vec3dArray[0].x, vec3dArray[0].y, vec3dArray[0].z).tex(GalathRenderer.C.c, GalathRenderer.C.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[1].x, vec3dArray[1].y, vec3dArray[1].z).tex(GalathRenderer.C.c + 0.125f, GalathRenderer.C.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[2].x, vec3dArray[2].y, vec3dArray[2].z).tex(GalathRenderer.C.c + 0.125f, GalathRenderer.C.a + 0.125f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[11].x, vec3dArray[11].y, vec3dArray[11].z).tex(GalathRenderer.C.c, GalathRenderer.C.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[12].x, vec3dArray[12].y, vec3dArray[12].z).tex(GalathRenderer.C.c + 0.125f, GalathRenderer.C.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[13].x, vec3dArray[13].y, vec3dArray[13].z).tex(GalathRenderer.C.c + 0.125f, GalathRenderer.C.a + 0.125f).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferBuilder.pos(vec3dArray[3].x, vec3dArray[3].y, vec3dArray[3].z).tex(GalathRenderer.x.c, GalathRenderer.x.a + 0.125f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[4].x, vec3dArray[4].y, vec3dArray[4].z).tex(GalathRenderer.x.c, GalathRenderer.x.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[5].x, vec3dArray[5].y, vec3dArray[5].z).tex(GalathRenderer.x.c + 0.125f, GalathRenderer.x.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[6].x, vec3dArray[6].y, vec3dArray[6].z).tex(GalathRenderer.x.c + 0.125f, GalathRenderer.x.a + 0.125f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[7].x, vec3dArray[7].y, vec3dArray[7].z).tex(GalathRenderer.x.c, GalathRenderer.x.a + 0.125f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[8].x, vec3dArray[8].y, vec3dArray[8].z).tex(GalathRenderer.x.c, GalathRenderer.x.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[9].x, vec3dArray[9].y, vec3dArray[9].z).tex(GalathRenderer.x.c + 0.125f, GalathRenderer.x.a).color(255, 255, 255, 255).endVertex();
        bufferBuilder.pos(vec3dArray[10].x, vec3dArray[10].y, vec3dArray[10].z).tex(GalathRenderer.x.c + 0.125f, GalathRenderer.x.a + 0.125f).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
    }

    @Override
    protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, GalathEntity f__class2972, float f, float f2, float f3, float f4, float f5) {
        GeoBone geoBone = geoModel.topLevelBones.get(0);
        GeoBone geoBone2 = null;
        GeoBone geoBone3 = null;
        GeoBone geoBone4 = null;
        GeoBone geoBone5 = null;
        for (GeoBone geoBone6 : geoBone.childBones) {
            switch (geoBone6.getName()) {
                case "steve": {
                    geoBone4 = geoBone6;
                    break;
                }
                case "body": {
                    geoBone2 = geoBone6;
                    break;
                }
                case "coin": {
                    geoBone3 = geoBone6;
                    break;
                }
                case "body2": {
                    geoBone5 = geoBone6;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
        Tessellator.getInstance().draw();
        this.a(bufferBuilder, geoBone3, f__class2972, f5);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.d(this.j));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.renderRecursively(bufferBuilder, geoBone4, f, f2, f3, ((GalathEntity)this.j).float_v());
        Tessellator.getInstance().draw();
        if (geoBone5 != null) {
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            Minecraft.getMinecraft().renderEngine.bindTexture(ManglelieModel.j);
            this.renderRecursively(bufferBuilder, geoBone5, f, f2, f3, ((GalathEntity)this.j).float_v());
            Tessellator.getInstance().draw();
        }
        MATRIX_STACK.pop();
    }

    @Override
    protected void a(BufferBuilder bufferBuilder, String string, GeoBone geoBone) {
        switch (string) {
            case "hairBack": {
                if (i.isGamePaused()) break;
                IBone iBone = ((GalathEntity)this.j).b().getBone("head");
                float f = gc_class360.d(iBone.getRotationX());
                if (f < 0.0f) {
                    geoBone.setRotationX(gc_class360.c(-f));
                    break;
                }
                float f2 = Math.min(1.0f, f / 45.0f);
                geoBone.setRotationX(gc_class360.c(-f));
                geoBone.setPositionY(geoBone.getPositionY() + f2 * 1.5f);
                break;
            }
            case "hairDownSideL": 
            case "hairDownSideR": {
                if (i.isGamePaused()) break;
                IBone iBone = ((GalathEntity)this.j).b().getBone("head");
                float f = gc_class360.d(iBone.getRotationX());
                if (f < 0.0f) {
                    geoBone.setRotationX(gc_class360.c(-f / 2.0f));
                    break;
                }
                float f3 = Math.min(1.0f, f / 45.0f);
                geoBone.setRotationX(gc_class360.c(-f));
                geoBone.setPositionY(geoBone.getPositionY() + f3);
                break;
            }
            case "head": {
                EntityLivingBase entityLivingBase;
                this.c(geoBone);
                Action fp_class3242 = ((GalathEntity)this.j).currentAction();
                if (fp_class3242 != Action.FLY && fp_class3242 != Action.ATTACK_SWORD || (entityLivingBase = ((GalathEntity)this.j).net_minecraft_entity_EntityLivingBase_M()) == null) break;
                float f = i.getRenderPartialTicks();
                Vec3d vec3d = b6_class67.a(new Vec3d(((GalathEntity)this.j).lastTickPosX, ((GalathEntity)this.j).lastTickPosY, ((GalathEntity)this.j).lastTickPosZ), ((GalathEntity)this.j).getPositionVector(), (double)f);
                Vec3d vec3d2 = b6_class67.a(new Vec3d(entityLivingBase.lastTickPosX, entityLivingBase.lastTickPosY, entityLivingBase.lastTickPosZ), ((GalathEntity)this.j).getPositionVector(), (double)f);
                Vec3d vec3d3 = vec3d.subtract(vec3d2);
                float f4 = (float)ck_class135.a((Vec3d)vec3d3, (float)((GalathEntity)this.j).renderYawOffset).z;
                float f5 = (float)Math.atan2(vec3d3.y, f4);
                break;
            }
            case "weapon": {
                if (!((GalathEntity)this.j).ap) break;
                GlStateManager.pushMatrix();
                Tessellator.getInstance().draw();
                p_class418.a(MATRIX_STACK, geoBone);
                GL11.glEnable(2896);
                GlStateManager.scale(1.5, 1.0, 2.0);
                GlStateManager.translate(0.0, 0.0, 0.05);
                GlStateManager.rotate(110.0f, 1.0f, 0.0f, 0.0f);
                Minecraft.getMinecraft().getItemRenderer().renderItem(this.j, new ItemStack(Items.IRON_SWORD), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
                this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
                bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                GL11.glDisable(2896);
                GlStateManager.popMatrix();
                break;
            }
            case "tongue": {
                this.e(bufferBuilder, geoBone);
                break;
            }
            case "mangTongue": {
                this.c(bufferBuilder, geoBone);
                break;
            }
            case "head3": {
                this.d(geoBone);
                break;
            }
            case "irisL": 
            case "irisR": {
                this.a_35(geoBone);
                break;
            }
            case "irsisFaceR2": 
            case "irsisFaceR3": {
                this.b_31(geoBone);
                break;
            }
            case "armL": 
            case "armR": {
                EntityLivingBase entityLivingBase;
                if (((GalathEntity)this.j).currentAction() != Action.RAPE_CHARGE || (entityLivingBase = ((GalathEntity)this.j).net_minecraft_entity_EntityLivingBase_M()) == null) break;
                float f = ((GalathEntity)this.j).renderYawOffset;
                Vec3d vec3d = entityLivingBase.getPositionVector().subtract(((GalathEntity)this.j).getPositionVector());
                vec3d = ck_class135.a(vec3d, f);
                double d = -be_class78.b(vec3d.x, -1.0, 1.0);
                geoBone.setRotationZ(geoBone.getRotationZ() + gc_class360.c(45.0 * d));
            }
        }
        if (((GalathEntity)this.j).boolean_b()) {
            ManglelieRenderer.a(this.j, string, geoBone, true);
        }
    }

    void e(BufferBuilder bufferBuilder, GeoBone geoBone) {
        if (Action.a(this.j, Action.PUSSY_LICKING, Action.MASTERBATE_SITTING)) {
            this.f(bufferBuilder, geoBone);
        } else if (Action.a(this.j, Action.MORNING_BLOWJOB_SLOW)) {
            this.d(bufferBuilder, geoBone);
        }
    }

    void c(BufferBuilder bufferBuilder, GeoBone geoBone) {
        if (!Action.a(this.j, Action.MORNING_BLOWJOB_SLOW) && !((GalathEntity)this.j).aD) {
            return;
        }
        float f = ((GalathEntity)this.j).aD ? 1.0f - Math.min(0.29f, Action.a(this.j, i.getRenderPartialTicks())) / 0.29f : 1.0f;
        this.a(bufferBuilder, geoBone, f);
        this.bindTexture(ManglelieModel.j);
    }

    void d(GeoBone geoBone) {
        if (!Action.a(this.j, Action.MORNING_BLOWJOB_SLOW, Action.MORNING_BLOWJOB_FAST)) {
            return;
        }
        if (i.isGamePaused()) {
            return;
        }
        float f = (float) GalathRenderer.i.player.ticksExisted + i.getRenderPartialTicks();
        float f2 = (float)(Math.sin(f * 0.1f) * (double)0.1f) + 0.2f;
        float f3 = (float)Math.sin(f * 0.1f) * 0.1f;
        if (Action.a(this.j, Action.MORNING_BLOWJOB_SLOW)) {
            geoBone.setRotationY(geoBone.getRotationY() + f2);
            geoBone.setRotationZ(geoBone.getRotationZ() + f3);
            return;
        }
        if (!((GalathEntity)this.j).aD) {
            return;
        }
        float f4 = 1.0f - Math.min(0.5f, Action.a(this.j, i.getRenderPartialTicks())) / 0.5f;
        geoBone.setRotationY(geoBone.getRotationY() + f2 * f4);
        geoBone.setRotationZ(geoBone.getRotationZ() + f3 * f4);
    }

    void c(GeoBone geoBone) {
        if (!Action.a(this.j, Action.MORNING_BLOWJOB_SLOW, Action.MORNING_BLOWJOB_FAST)) {
            return;
        }
        if (i.isGamePaused()) {
            return;
        }
        float f = (float) GalathRenderer.i.player.ticksExisted + i.getRenderPartialTicks();
        float f2 = (float)Math.sin(f * -0.1f) * 0.1f;
        float f3 = (float)Math.sin(f * 0.1f) * 0.1f;
        if (Action.a(this.j, Action.MORNING_BLOWJOB_SLOW)) {
            geoBone.setRotationY(geoBone.getRotationY() + f2);
            geoBone.setRotationZ(geoBone.getRotationZ() + f3);
            return;
        }
        if (!((GalathEntity)this.j).aD) {
            return;
        }
        float f4 = Math.min(0.5f, Action.a(this.j, i.getRenderPartialTicks())) / 0.5f;
        geoBone.setRotationY(geoBone.getRotationY() + f2 * f4);
        geoBone.setRotationZ(geoBone.getRotationZ() + f3 * f4);
        //super.a((GeoBone)null);
    }

    // TODO
    //  this doesnt appear to override anything
    //@Override
    void a_35(GeoBone geoBone) {
        if (!Action.a(this.j, Action.MORNING_BLOWJOB_SLOW)) {
            return;
        }
        if (i.isGamePaused()) {
            return;
        }
        float f = (float) GalathRenderer.i.player.ticksExisted + i.getRenderPartialTicks();
        geoBone.setPositionX((float)((double)geoBone.getPositionX() + Math.sin(f * 0.1f) * (double)-0.1f));
    }

    //@Override
    void b_31(GeoBone geoBone) {
        if (!Action.a(this.j, Action.MORNING_BLOWJOB_SLOW)) {
            return;
        }
        if (i.isGamePaused()) {
            return;
        }
        float f = (float) GalathRenderer.i.player.ticksExisted + i.getRenderPartialTicks();
        geoBone.setPositionX((float)((double)geoBone.getPositionX() + Math.sin(f * 0.1f) * (double)-0.15f));
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, float f2) {
        float f4 = Action.d(this.j, i.getRenderPartialTicks());
        float f5 = f2 * (float)((double)0.02f * ((double)-0.4f * Math.cos(Math.PI * 2 * (double)f4 + 1.05) + (double)0.6f));
        ef_class245.b_inner247 b_inner2472 = new ef_class245.b_inner247(H, 0.0f, 12, f5, (n, f3) -> f2 * (float)(Math.cos(Math.PI * 2 * (double)f4 + (double)0.35f + (double)(-0.2f * (float)n)) * -10.0), (n, f) -> 0.0f, (n, f3) -> f2 * (float)(Math.cos(Math.PI * 2 * (double)f4 + 1.25 + (double)(-0.1f * (float)n)) * -5.0), 0.03f, 0.005f);
        this.a(bufferBuilder, geoBone, b_inner2472);
    }

    void d(BufferBuilder bufferBuilder, GeoBone geoBone) {
        float f3 = Action.d(this.j, i.getRenderPartialTicks());
        ef_class245.b_inner247 b_inner2472 = new ef_class245.b_inner247(H, 0.0f, 12, 0.02f, (n, f2) -> (float)(Math.cos(Math.PI * 2 * (double)f3 + (double)(-0.2f * (float)n)) * 15.0), (n, f2) -> (float)(Math.cos(Math.PI * 2 * (double)f3 + (double)(-0.2f * (float)n)) * 5.0), (n, f) -> 0.0f, 0.03f, 0.005f);
        this.a(bufferBuilder, geoBone, b_inner2472);
    }

    void f(BufferBuilder bufferBuilder, GeoBone geoBone) {
        float f = ((GalathEntity)this.j).float_b(i.getRenderPartialTicks());
        if (f == 0.0f) {
            this.a(bufferBuilder, geoBone, G);
            return;
        }
        if (f == 1.0f) {
            this.a(bufferBuilder, geoBone, t);
            return;
        }
        ef_class245.b_inner247 b_inner2472 = G.a();
        b_inner2472.g = b6_class67.a(GalathRenderer.G.g, 0.0f, f);
        b_inner2472.e = b6_class67.a(GalathRenderer.G.e, 0.0f, f);
        this.a(bufferBuilder, geoBone, b_inner2472);
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, ef_class245.b_inner247 b_inner2472) {
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        p_class418.a(MATRIX_STACK, geoBone);
        GlStateManager.disableCull();
        this.bindTexture(e);
        ef_class245.a(bufferBuilder, Tessellator.getInstance(), i, b_inner2472);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, GalathEntity f__class2972, float f) {
        if (f__class2972.currentAction() != Action.GIVE_COIN) {
            return;
        }
        n = bufferBuilder;
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        if (!this.p.contains(geoBone.getName())) {
            for (GeoCube geoCube : geoBone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.q = geoBone;
                this.a(bufferBuilder, geoCube, 1.0f, 1.0f, 1.0f, 1.0f, 0.0);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
        }
        Tessellator.getInstance().draw();
        GeoBone geoBone2 = geoBone.childBones.get(0);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GL11.glDisable(2896);
        float f2 = be_class78.b((float) Action.GIVE_COIN.ticksPlaying[1] + f, 105.0f, 125.0f);
        float f3 = (f2 - 105.0f) / 20.0f;
        float f4 = b6_class67.a(120.0f, 240.0f, f3);
        f7_class292 f7_class2922 = b6_class67.a(av_class46.f, av_class46.e, (double)f3);
        float f5 = OpenGlHelper.lastBrightnessX;
        float f6 = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f4, f4);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone2);
        MATRIX_STACK.moveToPivot(geoBone2);
        MATRIX_STACK.rotate(geoBone2);
        MATRIX_STACK.scale(geoBone2);
        MATRIX_STACK.moveBackFromPivot(geoBone2);
        if (!this.p.contains(geoBone2.getName())) {
            for (GeoCube geoCube : geoBone2.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.q = geoBone2;
                this.a(bufferBuilder, geoCube, f7_class2922.a, f7_class2922.c, f7_class2922.b, 1.0f, 0.0);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
        }
        MATRIX_STACK.pop();
        MATRIX_STACK.pop();
        Tessellator.getInstance().draw();
        GL11.glEnable(2896);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f5, f6);
    }

    @Override
    protected Vec3d a(GalathEntity f__class2972, float f, Vec3d vec3d) {
        if (f__class2972.currentAction() == Action.RUN) {
            float f2;
            f__class2972.rotationYaw = f2 = f__class2972.java_lang_Float_I().floatValue();
            f__class2972.prevRenderYawOffset = f2;
            f__class2972.renderYawOffset = f2;
            f__class2972.prevRotationYawHead = f2;
            f__class2972.rotationYawHead = f2;
        }
        return vec3d;
    }

    //@Override
    //@Nullable
    //protected f7_class292 e(GirlEntity em_class2582) {
    //    return this.a((GalathEntity)em_class2582);
    //}

    //public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GalathEntity)em_class2582, d, d2, d3, f, f2);
    //}

    //@Override
    //protected void b(GirlEntity em_class2582) {
    //    this.b((GalathEntity)em_class2582);
    //}

    //@Override
    //protected Vec3d a(GirlEntity em_class2582, float f, Vec3d vec3d) {
    //    return this.a((GalathEntity)em_class2582, f, vec3d);
    //}

    //@Override
    //protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, bufferBuilder, (GalathEntity)em_class2582, f, f2, f3, f4, f5);
    //}

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GalathEntity)entityLivingBase, d, d2, d3, f, f2);
    //}
//
    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GalathEntity)entity, d, d2, d3, f, f2);
    //}
}

